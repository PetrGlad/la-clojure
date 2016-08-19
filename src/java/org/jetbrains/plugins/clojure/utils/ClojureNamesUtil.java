package org.jetbrains.plugins.clojure.utils;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.application.ApplicationManager;
import org.jetbrains.plugins.clojure.lexer.ClojureFlexLexer;
import org.jetbrains.plugins.clojure.lexer.ClojureTokenTypes;

/**
 * @author ilyas
 */
public class ClojureNamesUtil {

  public static boolean isIdentifier(String text) {
    ApplicationManager.getApplication().assertReadAccessAllowed();
    if (text == null) return false;
    Lexer lexer = new ClojureFlexLexer();
    lexer.start(text, 0, text.length(), 0);
    if (lexer.getTokenType() != ClojureTokenTypes.symATOM) return false;
    lexer.advance();
    return lexer.getTokenType() == null;
  }

}
