package org.jetbrains.plugins.clojure.formatter.codeStyle;

import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;

/**
 * @author ilyas
 */
public class ClojureCodeStyleSettings extends CustomCodeStyleSettings{

  public boolean ALIGN_CLOJURE_FORMS = false;

  protected ClojureCodeStyleSettings(CodeStyleSettings container) {
    super("ClojureCodeStyleSettings", container);
  }
}
