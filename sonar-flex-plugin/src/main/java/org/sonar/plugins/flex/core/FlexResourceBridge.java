/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.flex.core;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;

import java.util.Map;

/**
 * Class that helps finding which physical resource (a file) contains a specific Action Script class or function, or "mxml" definition.
 * It is used most notably to find resources from names given by FlexMetrics tool.
 *
 * Note that:
 * <ul>
 * <li>for AS files (that contain class definitions), this works only if they contain 1 and only 1 class each.</li>
 * <li>for MXML files (and other files different from "AS" files), the logical name contains the file extension.</li>
 * </ul>
 */
public class FlexResourceBridge implements BatchExtension {

  private Map<String, File> resourcesMap;
  private boolean canIndexFiles;

  /**
   * Creates a new {@link FlexResourceBridge}.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   */
  public FlexResourceBridge() {
    resourcesMap = Maps.newHashMap();
    canIndexFiles = true;
  }

  /**
   * Indexes a file to be able to find it with logical names.
   *
   * @param file the file to index
   * @throws IllegalStateException
   *           if the {@link FlexResourceBridge} is locked and cannot index more files
   */
  public void indexFile(org.sonar.api.resources.File file) {
    if (canIndexFiles) {
      resourcesMap.put(computeLogicalKey(file), file);
    } else {
      throw new IllegalStateException(
          "The FlexResourceBridge has been locked to prevent future modifications. It is impossible to index new files.");
    }
  }

  /**
   * After invoking this method, the {@link FlexResourceBridge} class won't be able to index files anymore: if
   * {@link #indexFile(File)} is called, a {@link IllegalStateException} will be thrown.
   */
  public void lock() {
    this.canIndexFiles = false;
  }

  public File findFile(String methodOrClassName) {
    return resourcesMap.get(StringUtils.substringBeforeLast(methodOrClassName, "::"));
  }

  public Directory findDirectory(String packageName) {
    return new Directory(packageName.replace('.', '/'));
  }

  private String computeLogicalKey(org.sonar.api.resources.File file) {
    String logicalName = file.getKey();
    if (StringUtils.endsWith(file.getKey(), ".as")) {
      // For ".as" files, the extension should be removed.
      logicalName = StringUtils.substringBeforeLast(logicalName, ".");
    }
    // and all the '/' should be replaced by a package-separator '.'
    return logicalName.replace('/', '.');
  }

}
