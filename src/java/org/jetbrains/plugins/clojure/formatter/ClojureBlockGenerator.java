package org.jetbrains.plugins.clojure.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.plugins.clojure.formatter.codeStyle.ClojureCodeStyleSettings;
import org.jetbrains.plugins.clojure.formatter.processors.ClojureIndentProcessor;
import org.jetbrains.plugins.clojure.lexer.ClojureTokenTypes;
import org.jetbrains.plugins.clojure.psi.api.*;
import org.jetbrains.plugins.clojure.psi.api.defs.ClDef;
import org.jetbrains.plugins.clojure.psi.api.symbols.ClSymbol;
import org.jetbrains.plugins.clojure.psi.util.ClojurePsiCheckers;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ilyas
 */
public class ClojureBlockGenerator {
  private static final TokenSet RIGHT_BRACES = TokenSet.create(ClojureTokenTypes.RIGHT_CURLY, ClojureTokenTypes.RIGHT_SQUARE);
  
  private ASTNode myNode;
  private Wrap myWrap;
  private CodeStyleSettings mySettings;
  private ClojureBlock myBlock;

  public List<Block> generateSubBlocks(ASTNode node, Wrap wrap, CodeStyleSettings settings, ClojureBlock block) {
    myNode = node;
    myWrap = wrap;
    mySettings = settings;
    myBlock = block;

    PsiElement blockPsi = myBlock.getNode().getPsi();

    final ArrayList<Block> subBlocks = new ArrayList<Block>();
    ASTNode children[] = myNode.getChildren(null);
    ASTNode prevChildNode = null;
    final ClojureCodeStyleSettings clSettings = block.getSettings().getCustomSettings(ClojureCodeStyleSettings.class);


    for (ASTNode childNode : children) {
      if (canBeCorrectBlock(childNode)) {

        final PsiElement childPsi = childNode.getPsi();
        final boolean mustAlign = mustAlign(blockPsi, childPsi, clSettings);

        final Indent indent = ClojureIndentProcessor.getChildIndent(myBlock, prevChildNode, childNode);
        subBlocks.add(new ClojureBlock(childNode,
            mustAlign ? block.childAlignment : Alignment.createAlignment(),
            indent, myWrap, mySettings));
        prevChildNode = childNode;

      }
    }
    return subBlocks;
  }

  public static boolean mustAlign(PsiElement blockPsi, PsiElement child, ClojureCodeStyleSettings settings) {

    if (blockPsi instanceof ClVector || blockPsi instanceof ClMap) {
      return !(child instanceof LeafPsiElement) || RIGHT_BRACES.contains(child.getNode().getElementType()) ||
          (child instanceof PsiComment);
    }

    if (blockPsi instanceof ClList &&
        !(blockPsi instanceof ClDef)) {
      final ClList list = (ClList) blockPsi;
      PsiElement first = list.getFirstNonLeafElement();

      if (settings.ALIGN_CLOJURE_FORMS || ClojurePsiCheckers.isImportMember(list)) {
        if (first == child && !applicationStart(first)) return true;
        if (first != null &&
            !applicationStart(first) &&
            first.getTextRange().getEndOffset() <= child.getTextRange().getStartOffset()) {
          return true;
        }
        final PsiElement second = list.getSecondNonLeafElement();
        if (second != null && child != null &&
            second.getTextRange().getEndOffset() <= child.getTextRange().getStartOffset()) {
          return true;
        }
      }
      // CLJ-98
      if (first instanceof ClKeyword && child != null &&
          first.getTextRange().getEndOffset() <= child.getTextRange().getStartOffset()) {
        return true;
      }
    }


    if (blockPsi instanceof ClLiteral) {
      ASTNode node = blockPsi.getNode();
      assert node != null;
      ASTNode[] elements = node.getChildren(null);
      if (elements.length > 0 && elements[0].getElementType() == ClojureTokenTypes.STRING_LITERAL) {
        return true;
      }
    }
    return false;
  }

  private static boolean applicationStart(PsiElement first) {
    return first instanceof ClSymbol;
  }

  private static boolean canBeCorrectBlock(final ASTNode node) {
    return (node.getText().trim().length() > 0);
  }


}
