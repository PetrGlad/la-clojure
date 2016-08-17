package org.jetbrains.plugins.clojure.config;

import com.intellij.facet.ui.libraries.LibraryInfo;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.plugins.clojure.utils.ClojureUtils;

import static org.jetbrains.plugins.clojure.config.util.ClojureMavenLibraryUtil.createJarDownloadInfo;


/**
 * @author ilyas
 */
public enum ClojureVersion {

  Clojure_1_0("1.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "", ClojureUtils.CLOJURE_MAIN),
      createJarDownloadInfo("clojure-contrib.jar", ""),
  }),

  Clojure_1_1_0("1.1.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.1.0", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_2("1.2", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.2", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_3("1.3.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.3.0", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_4("1.4.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.4.0", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_5_0("1.5.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.5.0", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_5_1("1.5.1", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.5.1", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_6_0("1.6.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.6.0", ClojureUtils.CLOJURE_MAIN),
  }),

  Clojure_1_8_0("1.8.0", new LibraryInfo[]{
      createJarDownloadInfo("clojure.jar", "1.8.0", ClojureUtils.CLOJURE_MAIN),
  });

  private final String myName;
  private final LibraryInfo[] myJars;

  private ClojureVersion(@NonNls String name, LibraryInfo[] infos) {
    myName = name;
    myJars = infos;
  }

  public LibraryInfo[] getJars() {
    return myJars;
  }

  public String toString() {
    return myName;
  }

}
