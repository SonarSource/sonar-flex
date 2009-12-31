/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex;

import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.DefaultProjectFileSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.File;
import java.util.List;

public class FlexFile implements Resource<FlexPackage> {

  private String key;
  private String filename;
  private String longName;
  private String packageKey;
  private boolean unitTest;
  private FlexPackage parent = null;

  public FlexFile(String key) {
    this.key = key.trim();
    this.unitTest = false;

    if (this.key.contains(".")) {
      this.filename = StringUtils.substringAfterLast(this.key, ".");
      this.packageKey = StringUtils.substringBeforeLast(this.key, ".");
      this.longName = this.key;

    } else {
      this.filename = this.key;
      this.longName = this.key;
      this.packageKey = FlexPackage.DEFAULT_PACKAGE_NAME;
      this.key = new StringBuilder().append(FlexPackage.DEFAULT_PACKAGE_NAME).append(".").append(this.key).toString();
    }
  }

  public FlexFile(String packageKey, String className) {
    this.filename = className.trim();
    if (StringUtils.isBlank(packageKey)) {
      this.packageKey = FlexPackage.DEFAULT_PACKAGE_NAME;
      this.longName = this.filename;
      this.key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
    } else {
      this.packageKey = packageKey.trim();
      this.key = new StringBuilder().append(this.packageKey).append(".").append(this.filename).toString();
      this.longName = this.key;
    }
  }



  public FlexPackage getParent() {
    if (parent == null) {
      parent = new FlexPackage(packageKey);
    }
    return parent;
  }

  public String getKey() {
    return key;
  }

  public String getDescription() {
    return null;
  }

  public Language getLanguage() {
    return Flex.INSTANCE;
  }

  public String getName() {
    return filename;
  }

  public String getLongName() {
    return longName;
  }

  public String getScope() {
    return Resource.SCOPE_ENTITY;
  }

  public String getQualifier() {
    return unitTest ? Resource.QUALIFIER_UNIT_TEST_CLASS : Resource.QUALIFIER_CLASS;
  }

  public boolean isUnitTest() {
    return unitTest;
  }

  public boolean matchFilePattern(String antPattern) {
    String patternWithoutFileSuffix = StringUtils.substringBeforeLast(antPattern, ".");
    WildcardPattern matcher = WildcardPattern.create(patternWithoutFileSuffix, ".");
    return matcher.match(getKey());
  }

  public static FlexFile fromIOFile(File file, List<File> sourceDirs) {
    if (file == null) {
      return null;
    }
    String relativePath = DefaultProjectFileSystem.getRelativePath(file, sourceDirs);
    if (relativePath != null) {
      String pacname = null;
      String classname = relativePath;

      if (relativePath.indexOf('/') >= 0) {
        pacname = StringUtils.substringBeforeLast(relativePath, "/");
        pacname = StringUtils.replace(pacname, "/", ".");
        classname = StringUtils.substringAfterLast(relativePath, "/");
      }
      classname = StringUtils.substringBeforeLast(classname, ".");
      return new FlexFile(pacname, classname);
    }
    return null;
  }

  public static FlexFile fromAbsolutePath(String path, List<File> sourceDirs, boolean unitTest) {
    if (path == null) {
      return null;
    }
    return fromIOFile(new File(path), sourceDirs);
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof FlexFile)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    FlexFile other = (FlexFile) obj;
    return StringUtils.equals(key, other.getKey());
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
      .append("key", key)
      .append("package", packageKey)
      .append("longName", longName)
      .append("unitTest", unitTest)
      .toString();
  }
}

