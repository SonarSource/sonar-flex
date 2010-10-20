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

package org.sonar.plugins.flex.surefire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.FlexFile;
import org.sonar.plugins.surefire.api.AbstractSurefireParser;
import org.sonar.plugins.surefire.api.SurefireUtils;

import java.io.File;

/**
 * TODO copied from sonar-surefire-plugin with modifications
 */
public class SurefireSensor implements Sensor, DependsUponMavenPlugin {

  private static Logger logger = LoggerFactory.getLogger(SurefireSensor.class);

  private FlexMojosMavenPluginHandler handler;

  public SurefireSensor(FlexMojosMavenPluginHandler handler) {
    this.handler = handler;
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
    logger.info("parsing {}", reportsDir);
    new AbstractSurefireParser() {
      @Override
      protected Resource<?> getUnitTestResource(String classKey) {
        return new FlexFile(classKey, true);
      }
    }.collect(project, context, reportsDir);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
