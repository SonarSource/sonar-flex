/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
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

package org.sonar.plugins.flex.cpd;

import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.api.CoreProperties;
import org.apache.commons.configuration.Configuration;

public class FlexCpdMavenPluginHandler implements MavenPluginHandler {
  private Configuration configuration;

  public FlexCpdMavenPluginHandler(Configuration configuration) {
    this.configuration = configuration;
  }

  public String getGroupId() {
    return "com.adobe.ac";
  }

  public String getArtifactId() {
    return "flex-pmd-cpd-maven-plugin";
  }

  public String getVersion() {
    return "1.1";
  }

  public boolean isFixedVersion() {
    return true;
  }

  public String[] getGoals() {
    return new String[]{"check"};
  }

  public void configure(Project pom, MavenPlugin plugin) {
    plugin.setParameter(
        "minimumTokenCount",
        configuration.getString(
            CoreProperties.CPD_MINIMUM_TOKENS_PROPERTY,
            Integer.toString(CoreProperties.CPD_MINIMUM_TOKENS_DEFAULT_VALUE)
        )
    );
  }
}
