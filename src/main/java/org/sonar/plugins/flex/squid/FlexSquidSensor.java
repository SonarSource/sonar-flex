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

package org.sonar.plugins.flex.squid;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.squid.measures.Metric;
import org.sonar.squid.text.Source;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.FlexUtils;
import org.sonar.plugins.flex.FlexFile;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class FlexSquidSensor implements Sensor {


  public boolean shouldExecuteOnProject(Project project) {
    return Flex.KEY.equals(project.getLanguageKey());
  }

  public void analyse(Project project, SensorContext sensorContext) {
    for (File flexFile : project.getFileSystem().getSourceFiles(Flex.INSTANCE)) {
      try {
        analyzeFile(flexFile, project.getFileSystem(), sensorContext);
      }
      catch (Exception e) {
        FlexUtils.LOG.error("Can not analyze the file " + flexFile.getAbsolutePath(), e);
      }
    }
  }

  protected void analyzeFile(File file, ProjectFileSystem projectFileSystem, SensorContext sensorContext) throws IOException {
    Reader reader = null;
    try {
      reader = new StringReader(FileUtils.readFileToString(file, projectFileSystem.getSourceCharset().name()));
      Resource resource = FlexFile.fromIOFile(file, projectFileSystem.getSourceDirs());
      Source source = new Source(reader, new FlexRecognizer(), "");
      sensorContext.saveMeasure(resource, CoreMetrics.LINES, (double) source.getMeasure(Metric.LINES));
    } finally {
      IOUtils.closeQuietly(reader);
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
