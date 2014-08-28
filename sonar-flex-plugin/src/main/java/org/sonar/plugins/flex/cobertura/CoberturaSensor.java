/*
 * SonarQube Flex Plugin
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
package org.sonar.plugins.flex.cobertura;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;

public class CoberturaSensor implements Sensor {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoberturaSensor.class);
  private final Settings settings;
  private final ModuleFileSystem fileSystem;

  public CoberturaSensor(Settings settings, ModuleFileSystem fileSystem) {
    this.settings = settings;
    this.fileSystem = fileSystem;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    // compatiblity with 3.7
    return Flex.KEY.equals(project.getLanguageKey())
      || (StringUtils.isBlank(project.getLanguageKey()) && !fileSystem.files(Flex.FILE_QUERY_ON_SOURCES).isEmpty());
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    String reportPath = settings.getString(FlexPlugin.COBERTURA_REPORT_PATH);

    if (reportPath != null) {
      File xmlFile = getIOFile(reportPath);

      if (xmlFile.exists()) {
        LOGGER.info("Analyzing Cobertura report: " + reportPath);
        CoberturaReportPasrer.parseReport(xmlFile, context, project, fileSystem);
      } else {
        LOGGER.info("Cobertura xml report not found: " + reportPath);
      }
    } else {
      LOGGER.info("No Cobertura report provided (see '" + FlexPlugin.COBERTURA_REPORT_PATH + "' property)");
    }
  }

  /**
   * Returns a java.io.File for the given path.
   * If path is not absolute, returns a File with module base directory as parent path.
   */
  private File getIOFile(String path) {
    File file = new File(path);
    if (!file.isAbsolute()) {
      file = new File(fileSystem.baseDir(), path);
    }

    return file;
  }

}
