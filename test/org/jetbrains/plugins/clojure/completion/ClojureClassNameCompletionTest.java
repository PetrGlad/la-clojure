package org.jetbrains.plugins.clojure.completion;

import java.io.IOException;

/**
 * @author Alefas
 * @since 16.01.13
 */
public class ClojureClassNameCompletionTest extends ClojureCompletionTestBase {
  public void testSimpleClassName() throws IOException {
    configureFromFileText("dummy.clj", "(ArrayList<caret>)");
    final CompleteResult complete = complete(2);
    assertNotNull(complete);
    completeLookupItem(complete.getElements()[0]);
    checkResultByText("(ns dummy.clj\n" +
        "  (:import [java.util ArrayList]))\n" +
        "\n" +
        "(ArrayList<caret>)");
  }

  public void testMoreClassName() throws IOException {
    configureFromFileText("dummy.clj",
        "(ns dummy.clj\n" +
            "  (:import [java.util ArrayList]))\n" +
            "\n" +
            "(Iterator<caret>)");
    final CompleteResult complete = complete(2);
    assertNotNull(complete);
    completeLookupItem(complete.getElements()[0]);
    checkResultByText("(ns dummy.clj\n" +
        "  (:import [java.util ArrayList Iterator]))\n" +
        "\n" +
        "(Iterator<caret>)");
  }

  public void testClassNameInImport() throws IOException {
    String fileText =
        "(import ArrayList<caret>)";
    configureFromFileText("dummy.clj", fileText);
    final CompleteResult complete = complete(2);
    assertNotNull(complete);
    completeLookupItem(complete.getElements()[0]);
    checkResultByText("(import java.util.ArrayList<caret>)");
  }
}
