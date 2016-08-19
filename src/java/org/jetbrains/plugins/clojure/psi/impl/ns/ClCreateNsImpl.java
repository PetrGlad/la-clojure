package org.jetbrains.plugins.clojure.psi.impl.ns;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.IStubElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.plugins.clojure.psi.stubs.api.ClNsStub;

/**
 * @author ilyas
 */
public class ClCreateNsImpl extends ClNsImpl{
  public ClCreateNsImpl(ClNsStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public ClCreateNsImpl(ASTNode node) {
    super(node);
  }
}
