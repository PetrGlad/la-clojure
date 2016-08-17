package org.jetbrains.plugins.clojure.config.util;

import com.intellij.facet.ui.libraries.LibraryInfo;
import org.jetbrains.annotations.NonNls;

/**
 * @author ilyas
 */
public final class ClojureMavenLibraryUtil {
  @NonNls
  private static final String DOWNLOAD_MAVEN_ORG = "http://repo1.maven.org";
  @NonNls
  private static final String MAVEN_DOWNLOADING_URL = DOWNLOAD_MAVEN_ORG + "/maven2/org/clojure/clojure/";

  private ClojureMavenLibraryUtil() {
  }

  public static LibraryInfo createJarDownloadInfo(final String jarName, final String version,
                                                  final String... requiredClasses) {
    return new LibraryInfo(jarName, MAVEN_DOWNLOADING_URL + (version.isEmpty() ? "" : version + "/")
        + jarName.substring(0, jarName.lastIndexOf('.')) + "-" + version + ".jar", DOWNLOAD_MAVEN_ORG, null,
        requiredClasses);
  }
}
