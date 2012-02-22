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

package org.sonar.plugins.flex.surefire;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.core.FlexResourceBridge;
import org.sonar.plugins.flex.core.FlexUtils;
import org.sonar.plugins.surefire.api.AbstractSurefireParser;
import org.sonar.plugins.surefire.api.SurefireUtils;

public class FlexSurefireSensor implements Sensor, DependsUponMavenPlugin {

  private static final Logger LOG = LoggerFactory.getLogger(FlexSurefireSensor.class);

  private FlexMojosMavenPluginHandler handler;
  private FlexResourceBridge resourceBridge;

  public FlexSurefireSensor(FlexMojosMavenPluginHandler handler, FlexResourceBridge resourceBridge) {
    this.handler = handler;
    this.resourceBridge = resourceBridge;
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    if (project.getAnalysisType().equals(Project.AnalysisType.DYNAMIC)) {
      return handler;
    }
    return null;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return project.getAnalysisType().isDynamic(true) && Flex.KEY.equals(project.getLanguageKey());
  }

  public void analyse(Project project, SensorContext context) {
    File dir = SurefireUtils.getReportsDirectory(project);
    collect(project, context, dir);
  }

  protected void collect(Project project, SensorContext context, File reportsDir) {
    LOG.info("parsing {}", reportsDir);
    new AbstractSurefireParser() {
      @Override
      protected Resource<?> getUnitTestResource(String classKey) {
        String className = classKey.replaceAll("::", ".");
        org.sonar.api.resources.File file = resourceBridge.findFile(className);
        if (file == null) {
          // in case the test source folder hasn't been specified...
          file = new org.sonar.api.resources.File(className);
          file.setQualifier(Qualifiers.UNIT_TEST_FILE);
        }
        return file;
      }
    }.collect(project, context, reportsDir);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
