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

package org.sonar.plugins.flex.flexpmd;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.plugins.flex.Flex;

import java.io.File;

public class FlexPmdSensor implements Sensor, DependsUponMavenPlugin {

  private FlexPmdMavenPluginHandler pluginHandler;
  private RuleFinder ruleFinder;

  public FlexPmdSensor(RuleFinder ruleFinder, FlexPmdMavenPluginHandler pluginHandler) {
    this.ruleFinder = ruleFinder;
    this.pluginHandler = pluginHandler;
  }

  public void analyse(Project project, SensorContext context) {
    FlexPmdXmlReportParser parser = getStaxParser(project, context);
    File report = new File(project.getFileSystem().getBuildDir(), "pmd.xml");
    parser.parse(report);
  }

  public boolean shouldExecuteOnProject(Project project) {
    return Flex.KEY.equals(project.getLanguageKey());
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return pluginHandler;
  }

  private FlexPmdXmlReportParser getStaxParser(Project project, SensorContext context) {
    return new FlexPmdXmlReportParser(project, context, ruleFinder);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
