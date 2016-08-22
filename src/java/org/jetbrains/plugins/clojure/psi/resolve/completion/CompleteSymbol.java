package org.jetbrains.plugins.clojure.psi.resolve.completion;

import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.util.MethodSignature;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.HashMap;
import com.intellij.util.containers.HashSet;
import org.jetbrains.plugins.clojure.ClojureIcons;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import org.jetbrains.plugins.clojure.psi.impl.ns.ClSyntheticNamespace;
import org.jetbrains.plugins.clojure.psi.resolve.ClojureResolveResult;
import org.jetbrains.plugins.clojure.psi.resolve.ResolveUtil;

import java.util.*;

/**
 * @author ilyas
 */
public class CompleteSymbol {

  public static Object[] getVariants(ClSymbol symbol) {
    Collection<Object> variants = new ArrayList<Object>();

    ClSymbol qualifier = symbol.getQualifierSymbol();
    final CompletionProcessor processor = new CompletionProcessor(symbol, symbol.getKinds());
    if (qualifier == null) {
      ResolveUtil.treeWalkUp(symbol, processor);
    } else {
      for (ResolveResult result : qualifier.multiResolve(false)) {
        final PsiElement element = result.getElement();
        if (element != null) {
          final PsiElement sep = symbol.getSeparatorToken();
          final String sepText = sep == null ? "." : sep.getText();
          if ("/".equals(sepText) && isNamespaceLike(element)) {
            element.processDeclarations(processor, ResolveState.initial(), null, symbol);
          } else if (".".equals(sepText)) {
            element.processDeclarations(processor, ResolveState.initial(), null, symbol);
          }
        }
      }
    }

    final ClojureResolveResult[] candidates = processor.getCandidates();
    if (candidates.length == 0) return PsiNamedElement.EMPTY_ARRAY;

    List<PsiElement> psiElements = ContainerUtil.newArrayList();
    for (ClojureResolveResult candidate : candidates) {
      PsiElement element = candidate.getElement();
      if (element != null && element != symbol) {
        variants.add(new ClojureLookupItem(element));
        psiElements.add(element);
      }
    }

    // Add Java methods for all imported classes
    final boolean withoutDot = mayBeMethodReference(symbol);
    if (symbol.getChildren().length == 0 && symbol.getText().startsWith(".") ||
        withoutDot) {
      addJavaMethods(psiElements.toArray(new PsiElement[psiElements.size()]), variants, withoutDot);
    }

    return variants.toArray(new Object[variants.size()]);
  }

  private static boolean isNamespaceLike(PsiElement element) {
    return element instanceof PsiClass || element instanceof ClSyntheticNamespace;
  }

  private static boolean mayBeMethodReference(ClSymbol symbol) {
    final PsiElement parent = symbol.getParent();
    if (parent == null) return false;

//    if (parent.getParent() instanceof ClList && ".".equals(((ClList) parent.getParent()).getHeadText())) return true;
    return false;
  }

  private static void addJavaMethods(PsiElement[] psiElements, Collection<Object> variants, boolean withoutDot) {
    final Map<MethodSignature, Set<PsiMethod>> sig2Methods = collectAvailableMethods(psiElements);

    for (Map.Entry<MethodSignature, Set<PsiMethod>> entry : sig2Methods.entrySet()) {
      final MethodSignature sig = entry.getKey();
      final String name = sig.getName();

      final StringBuffer buffer = new StringBuffer();
      buffer.append(name).append("(");
      buffer.append(StringUtil.join(ContainerUtil.map2Array(sig.getParameterTypes(), String.class, new Function<PsiType, String>() {
            public String fun(PsiType psiType) {
              return psiType.getPresentableText();
            }
          }), ", ")
      ).append(")");

      final String methodText = buffer.toString();

      final StringBuffer tailBuffer = new StringBuffer();
      tailBuffer.append(" in ");
      final ArrayList<String> list = new ArrayList<String>();
      for (PsiMethod method : entry.getValue()) {
        final PsiClass clazz = method.getContainingClass();
        if (clazz != null) {
          list.add(clazz.getQualifiedName());
        }
      }
      tailBuffer.append(StringUtil.join(list, ", "));

      final LookupItem item = new LookupItem(methodText, (withoutDot ? "" : ".") + name);
      item.setIcon(ClojureIcons.JAVA_METHOD);
      item.setTailText(tailBuffer.toString(), true);

      variants.add(item);
    }
  }

  public static Map<MethodSignature, Set<PsiMethod>> collectAvailableMethods(PsiElement[] psiElements) {
    final Map<MethodSignature, Set<PsiMethod>> sig2Methods = new HashMap<MethodSignature, Set<PsiMethod>>();
    for (PsiElement element : psiElements) {
      if (element instanceof PsiClass) {
        PsiClass clazz = (PsiClass) element;
        for (PsiMethod method : clazz.getAllMethods()) {
          if (!method.isConstructor() && method.hasModifierProperty(PsiModifier.PUBLIC)) {
            final MethodSignature sig = method.getSignature(PsiSubstitutor.EMPTY);
            final Set<PsiMethod> set = sig2Methods.get(sig);
            if (set == null) {
              final Set<PsiMethod> newSet = new HashSet<PsiMethod>();
              newSet.add(method);
              sig2Methods.put(sig, newSet);
            } else {
              set.add(method);
            }
          }
        }
      }
    }
    return sig2Methods;
  }

}
