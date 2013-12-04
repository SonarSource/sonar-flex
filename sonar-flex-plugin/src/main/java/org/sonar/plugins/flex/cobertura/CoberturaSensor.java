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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;

public class CoberturaSensor implements Sensor {

  private Settings settings;
  private static final Logger logger = LoggerFactory.getLogger(CoberturaSensor.class);

  public CoberturaSensor(Settings settings) {
    this.settings = settings;
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return Flex.KEY.equals(project.getLanguageKey());
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    String reportPath = settings.getString(FlexPlugin.COBERTURA_REPORT_PATH);

    if (reportPath != null) {
      File xmlFile = new File(reportPath);

      if (xmlFile.exists()) {
        logger.info("Analyzing Cobertura report: " + reportPath);
        CoberturaReportPasrer.parseReport(xmlFile, context);
      } else {
        logger.info("Cobertura xml report not found: " + reportPath);
      }
    }
    logger.info("No Cobertura report provided (see 'sonar.flex.cobertura.reportPath' property)");
  }
}
