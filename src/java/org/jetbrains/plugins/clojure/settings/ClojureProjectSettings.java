/*
 * Copyright 2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jetbrains.plugins.clojure.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

/**
 * Project specific settings.
 * <p/>
 * This is the persistent state.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @see ClojureConfigurable
 */
@State(name = "ClojureProjectSettings",
    storages = {
        @Storage(id = "default", file = "$PROJECT_FILE$"),
        @Storage(id = "dir", file = "$PROJECT_CONFIG_DIR$/clojure_project.xml", scheme = StorageScheme.DIRECTORY_BASED)
    })
public final class ClojureProjectSettings implements PersistentStateComponent<ClojureProjectSettings> {


  public static ClojureProjectSettings getInstance(Project project) {
    return ServiceManager.getService(project, ClojureProjectSettings.class);
  }

  public boolean autoStartRepl;
  public boolean coloredParentheses;


  public ClojureProjectSettings() {
  }

  // PersistentStateComponent =================================================

  public ClojureProjectSettings getState() {
    return this;
  }

  public void loadState(ClojureProjectSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

}
