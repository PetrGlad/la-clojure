package org.jetbrains.plugins.clojure.formatter.processors;

import com.intellij.formatting.Spacing;
import com.intellij.formatting.Block;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.plugins.clojure.formatter.ClojureBlock;
import org.jetbrains.plugins.clojure.parser.ClojureElementTypes;

/**
 * @author ilyas
 */
public class ClojureSpacingProcessor implements ClojureElementTypes {

  private static final Spacing NO_SPACING = Spacing.createSpacing(0, 0, 0, false, 0);
  private static final Spacing NO_SPACING_WITH_NEWLINE = Spacing.createSpacing(0, 0, 0, true, 1);
  private static final Spacing COMMON_SPACING = Spacing.createSpacing(1, 1, 0, true, 100);

  public static Spacing getSpacing(Block child1, Block child2) {
    if (!(child1 instanceof ClojureBlock) || !(child2 instanceof ClojureBlock)) return null;
    ClojureBlock block1 = (ClojureBlock) child1;
    ClojureBlock block2 = (ClojureBlock) child2;

    ASTNode node1 = block1.getNode();
    ASTNode node2 = block2.getNode();

    IElementType type1 = node1.getElementType();
    IElementType type2 = node2.getElementType();

    if (MODIFIERS.contains(type1)) {
      return NO_SPACING;
    }

    String text1 = node1.getText();
    String text2 = node2.getText();

    if (text1.trim().startsWith(",") || text2.trim().startsWith(",")) {
      return null;
    }

    if (BRACES.contains(type1) || BRACES.contains(type2)) {
      return NO_SPACING_WITH_NEWLINE;
    }

    return COMMON_SPACING;
  }
}
