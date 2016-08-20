package org.jetbrains.plugins.clojure.repl.actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import org.jetbrains.annotations.NotNull;

/**
 * @author ilyas
 */
public class ExecuteStatementAction extends EditorAction {

  public static String ID = "ClojureExecuteStatementAction";

  protected ExecuteStatementAction() {
    super(new MyHandler());
  }

  private static class MyHandler extends EditorWriteActionHandler {
    public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
      return false;
    }

    public void executeWriteAction(Editor editor, Caret caret, DataContext dataContext) {
    }
  }

  @Override
  public void update(Editor editor, Presentation presentation, DataContext dataContext) {
    super.update(editor, presentation, dataContext);
  }
}
