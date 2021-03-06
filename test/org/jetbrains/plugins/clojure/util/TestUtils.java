package org.jetbrains.plugins.clojure.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.OrderEnumerator;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.impl.VirtualFilePointerManagerImpl;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.openapi.vfs.pointers.VirtualFilePointerManager;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * @author ilyas
 */
public class TestUtils {

  private static final Logger LOG = Logger.getInstance("org.jetbrains.plugins.clojure.util.TestUtils");

  private static final String[] RUN_PATHES = new String[]{
      "out/test/clojure-plugin",
// if tests are run using ant script
      "dist/testClasses"};

  private static String TEST_DATA_PATH = null;

  public static final String CARET_MARKER = "<caret>";
  public static final String BEGIN_MARKER = "<begin>";
  public static final String END_MARKER = "<end>";

  @NotNull
  public static String getTestDataPath() {
    if (TEST_DATA_PATH == null) {
      ClassLoader loader = TestUtils.class.getClassLoader();
      URL resource = loader.getResource("testdata");
      try {
        TEST_DATA_PATH = new File("testdata").getAbsolutePath();
        if (resource != null) {
          TEST_DATA_PATH = new File(resource.toURI()).getPath().replace(File.separatorChar, '/');
        }
      } catch (URISyntaxException e) {
        LOG.error(e);
        return null;
      }
      VfsRootAccess.allowRootAccess(TestUtils.getMockClojureLib());
      VfsRootAccess.allowRootAccess(TestUtils.getMockClojureContribLib());
    }
    return TEST_DATA_PATH;
  }

  public static String getMockClojureLib() {
    // TODO (tests, refactoring) Get rid of binary blobs
    return getTestDataPath() + "/mockClojureLib/clojure-1.8.0.jar" ;
  }

  public static String getMockClojureContribLib() {
    // TODO (tests, refactoring) Get rid of binary blobs
    // TODO (tests, refactoring) Use Clojure's post 1.3 fine grained deps instead of clojure-contrib
    return getTestDataPath() + "/mockClojureLib/clojure-contrib.jar" ;
  }

  @Nullable
  public static String getDataPath(@NotNull Class clazz) {
    final String classDir = getClassRelativePath(clazz);
    String moduleDir = getModulePath(clazz);
    return classDir != null && moduleDir != null ? moduleDir + "/" + classDir + "/data/" : null;
  }

  public static String getOutputPath(final Class clazz) {
    final String classDir = getClassRelativePath(clazz);
    String moduleDir = getModulePath(clazz);
    return classDir != null && moduleDir != null ? moduleDir + "/" + classDir + "/output/" : null;
  }

  @Nullable
  public static String getDataPath(@NotNull Class s, @NotNull final String relativePath) {
    return getDataPath(s) + "/" + relativePath;
  }

  @Nullable
  public static String getClassRelativePath(@NotNull Class s) {
    String classFullPath = getClassFullPath(s);
    for (String path : RUN_PATHES) {
      final String dataPath = getClassDirPath(classFullPath, path);
      if (dataPath != null) {
        return dataPath;
      }
    }
    return null;
  }

  @Nullable
  public static String getModulePath(@NotNull Class s) {
    String classFullPath = getClassFullPath(s);
    for (String path : RUN_PATHES) {
      final String dataPath = getModulePath(classFullPath, path);
      if (dataPath != null) {
        return dataPath;
      }
    }
    return null;
  }

  public static String getClassFullPath(@NotNull final Class s) {
    String name = s.getSimpleName() + ".class";
    final URL url = s.getResource(name);
    return url.getPath();
  }

  @Nullable
  private static String getModulePath(@NotNull String s, @NotNull final String indicator) {
    int n = s.indexOf(indicator);
    if (n == -1) {
      return null;
    }
    return s.substring(0, n - 1);
  }

  @Nullable
  private static String getClassDirPath(@NotNull String s, @NotNull final String indicator) {
    int n = s.indexOf(indicator);
    if (n == -1) {
      return null;
    }
    s = "test" + s.substring(n + indicator.length());
    s = s.substring(0, s.lastIndexOf('/'));
    return s;
  }

  public static ModifiableRootModel addLibrary(ModifiableRootModel rootModel,
                                               ModuleRootManager rootManager, OrderEnumerator libs,
                                               List<Library.ModifiableModel> libModels,
                                               final String clojureLibraryName, String mockLib, String mockLibSrc) {
    class CustomProcessor implements Processor<Library> {
      public boolean result = true;

      public boolean process(Library library) {
        boolean res = library.getName().equals(clojureLibraryName);
        if (res) result = false;
        return result;
      }
    }
    CustomProcessor processor = new CustomProcessor();
    libs.forEachLibrary(processor);
    if (processor.result) {
      if (rootModel == null) {
        rootModel = rootManager.getModifiableModel();
      }
      final LibraryTable libraryTable = rootModel.getModuleLibraryTable();
      final Library scalaLib = libraryTable.createLibrary(clojureLibraryName);
      final Library.ModifiableModel libModel = scalaLib.getModifiableModel();
      libModels.add(libModel);
      addLibraryRoots(libModel, mockLib, mockLibSrc);
    }
    return rootModel;
  }

  public static void addLibraryRoots(Library.ModifiableModel libModel, String mockLib, String mockLibSrc) {
    final File libRoot = new File(mockLib);
    assert (libRoot.exists());


    libModel.addRoot(VfsUtil.getUrlForLibraryRoot(libRoot), OrderRootType.CLASSES);
    if (mockLibSrc != null) {
      final File srcRoot = new File(mockLibSrc);
      assert (srcRoot.exists());
      libModel.addRoot(VfsUtil.getUrlForLibraryRoot(srcRoot), OrderRootType.SOURCES);
    }
    ((VirtualFilePointerManagerImpl) VirtualFilePointerManager.getInstance()).storePointers();
  }

}
