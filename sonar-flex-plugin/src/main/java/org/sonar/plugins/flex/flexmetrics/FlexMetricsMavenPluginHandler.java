/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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

package org.sonar.plugins.flex.flexmetrics;

import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;

/**
 * Class used to launch the Flex PMD Metrics Maven plugin that generates various metrics like loc, cc, ...
 */
public class FlexMetricsMavenPluginHandler implements MavenPluginHandler {

  /**
   * {@inheritDoc}
   */
  public String getGroupId() {
    return "com.adobe.ac";
  }

  /**
   * {@inheritDoc}
   */
  public String getArtifactId() {
    return "flex-pmd-metrics-maven-plugin";
  }

  /**
   * {@inheritDoc}
   */
  public String getVersion() {
    return "1.2";
  }

  /**
   * {@inheritDoc}
   */
  public boolean isFixedVersion() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getGoals() {
    return new String[] {"check"};
  }

  /**
   * {@inheritDoc}
   */
  public void configure(Project project, MavenPlugin plugin) {
  }
}
