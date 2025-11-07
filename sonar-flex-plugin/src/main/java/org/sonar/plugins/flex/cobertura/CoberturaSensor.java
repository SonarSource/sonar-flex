/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.plugins.flex.cobertura;

import java.io.File;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;

public class CoberturaSensor implements Sensor {

  private static final Logger LOGGER = Loggers.get(CoberturaSensor.class);

  @Override
  public void describe(SensorDescriptor descriptor) {
    descriptor
      .name("Flex Cobertura")
      .onlyOnFileType(InputFile.Type.MAIN)
      .onlyOnLanguage(Flex.KEY);
  }

  @Override
  public void execute(SensorContext context) {
    String[] reportPaths = context.config().getStringArray(FlexPlugin.COBERTURA_REPORT_PATHS);

    if (reportPaths.length > 0) {
      for(String reportPath: reportPaths) {
        File xmlFile = getIOFile(context.fileSystem(), reportPath);

        if (xmlFile.exists()) {
          LOGGER.info("Analyzing Cobertura report: {}", reportPath);
          CoberturaReportParser.parseReport(xmlFile, context);
        } else {
          LOGGER.info("Cobertura xml report not found: {}", reportPath);
        }
      }
    } else {
      LOGGER.info("No Cobertura report provided (see '{}' property)", FlexPlugin.COBERTURA_REPORT_PATHS);
    }
  }

  /**
   * Returns a java.io.File for the given path.
   * If path is not absolute, returns a File with module base directory as parent path.
   */
  private static File getIOFile(FileSystem fileSystem, String path) {
    File file = new File(path);
    if (!file.isAbsolute()) {
      file = new File(fileSystem.baseDir(), path);
    }

    return file;
  }

}
