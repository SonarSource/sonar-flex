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

import java.io.File;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.plugins.flex.core.Flex;

/**
 * Sensor that will launch the required Maven plugin and analyse its report.
 */
public class FlexMetricsSensor implements Sensor, DependsUponMavenPlugin {

  private static final String REPORT_FILE_NAME = "javancss-raw-report.xml";
  private FlexMetricsMavenPluginHandler pluginHandler;
  private FlexMetricsParser metricsParser;

  /**
   * Constructs a {@link FlexMetricsSensor}.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   * 
   * @param pluginHandler the Maven handler
   */
  public FlexMetricsSensor(FlexMetricsMavenPluginHandler pluginHandler, FlexMetricsParser metricsParser) {
    this.pluginHandler = pluginHandler;
    this.metricsParser = metricsParser;
  }

  /**
   * {@inheritDoc}
   */
  public boolean shouldExecuteOnProject(Project project) {
    return Flex.KEY.equals(project.getLanguageKey());
  }

  /**
   * {@inheritDoc}
   */
  public void analyse(Project project, SensorContext context) {
    File xmlFile = new File(project.getFileSystem().getBuildDir(), REPORT_FILE_NAME);
    metricsParser.parse(xmlFile);
  }

  /**
   * {@inheritDoc}
   */
  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return pluginHandler;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
