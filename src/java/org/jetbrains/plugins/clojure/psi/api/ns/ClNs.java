package org.jetbrains.plugins.clojure.psi.api.ns;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.clojure.psi.api.ClList;
import org.jetbrains.plugins.clojure.psi.api.ClListLike;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;

/**
 * @author ilyas
 */
public interface ClNs extends ClList, PsiNamedElement {
  @Nullable
  ClSymbol getNameSymbol();

  String getDefinedName();

  @Nullable
  ClList findImportClause(@Nullable PsiElement place);

  @Nullable
  ClList findImportClause();

  @NotNull
  ClList findOrCreateImportClause(@Nullable PsiElement place);

  @NotNull
  ClList findOrCreateImportClause();

  @Nullable
  ClListLike addImportForClass(PsiElement place, PsiClass clazz);

}
