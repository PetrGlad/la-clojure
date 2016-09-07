package org.jetbrains.plugins.clojure.editor.braceHighlighter;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.parser.ClojureElementTypes;
import org.jetbrains.plugins.clojure.psi.api.ClListLike;
import org.jetbrains.plugins.clojure.settings.ClojureProjectSettings;

import static org.jetbrains.plugins.clojure.lexer.ClojureTokenTypes.ALL_PARENS;

/**
 * @author ilyas
 */
public class ClojureBraceHighlighter implements Annotator {

  public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    if (psiElement instanceof LeafPsiElement &&
        ClojureProjectSettings.getInstance(psiElement.getProject()).coloredParentheses) {
      IElementType type = ((LeafPsiElement) psiElement).getElementType();
      if (ALL_PARENS.contains(type)) {
        int level = getLevel(psiElement);
        if (level >= 0) {
          final EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
          TextAttributes attrs = ClojureBraceAttributes.getBraceAttributes(level, scheme.getDefaultBackground());
          annotationHolder.createInfoAnnotation(psiElement, null).setEnforcedTextAttributes(attrs);
        }
      }
    }
  }

  private static int getLevel(PsiElement psiElement) {
    int level = -1;
    PsiElement eachParent = psiElement;
    while (eachParent != null) {
      if (eachParent instanceof ClListLike) {
        level++;
      }
      eachParent = eachParent.getParent();
    }
    return level;
  }

}
