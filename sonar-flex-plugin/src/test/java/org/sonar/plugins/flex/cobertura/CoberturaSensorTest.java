/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.test.TestUtils;

import java.io.File;
import java.util.Properties;

public class CoberturaSensorTest {

  private Project project;
  private SensorContext context;
  private static final String TEST_DIR = "org/sonar/plugins/flex/cobertura/";

  @Before
  public void setUp() throws Exception {
    context = mock(SensorContext.class);
    project = new Project("key");
  }

  @Test
  public void shouldParseReport() throws Exception {
    DefaultFileSystem fs = new DefaultFileSystem();
    fs.setBaseDir(TestUtils.getResource(TEST_DIR));
    File srcFile = TestUtils.getResource(TEST_DIR + "src/example/File.as");
    InputFile inputFile = new DefaultInputFile(srcFile.getName())
      .setLanguage(Flex.KEY)
      .setType(InputFile.Type.MAIN)
      .setAbsolutePath(srcFile.getAbsolutePath());

    fs.add(inputFile);

    Sensor sensor = newSensorWithProperty(FlexPlugin.COBERTURA_REPORT_PATH, TestUtils.getResource(TEST_DIR + "coverage.xml").getPath(), fs);
    sensor.analyse(project, context);

    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.LINES_TO_COVER, 2.0)));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.UNCOVERED_LINES, 2.0)));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.COVERAGE_LINE_HITS_DATA, "1=0;7=0")));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.CONDITIONS_TO_COVER, 2.0)));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.UNCOVERED_CONDITIONS, 1.0)));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.CONDITIONS_BY_LINE, "1=2")));
    verify(context).saveMeasure(eq(inputFile), eq(new Measure(CoreMetrics.COVERED_CONDITIONS_BY_LINE, "1=1")));
  }

  @Test
  public void reportNotFound() {
    Sensor sensor = newSensorWithProperty(FlexPlugin.COBERTURA_REPORT_PATH, "/fake/path", new DefaultFileSystem());
    sensor.analyse(project, context);

    verify(context, never()).saveMeasure(any(org.sonar.api.resources.File.class), any(Metric.class), anyDouble());
  }

  @Test
  public void noReport() {
    CoberturaSensor sensor = new CoberturaSensor(new Settings(), new DefaultFileSystem());
    sensor.analyse(project, context);

    verify(context, never()).saveMeasure(any(org.sonar.api.resources.File.class), any(Metric.class), anyDouble());
  }

  @Test
  public void test_should_execute_on_project() {
    DefaultFileSystem fs = new DefaultFileSystem();
    CoberturaSensor sensor = new CoberturaSensor(new Settings(), fs);

    // No Flex file in file system
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    // With Flex source file
    fs.add(new DefaultInputFile("Dummy.as").setLanguage(Flex.KEY).setType(InputFile.Type.MAIN));
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
  }

  private Sensor newSensorWithProperty(String key, String value, DefaultFileSystem fs) {
    Properties props = new Properties();
    if (key != null && value != null) {
      props.put(key, value);
    }

    Settings settings = new Settings();
    settings.addProperties(props);

    return new CoberturaSensor(settings, fs);
  }

}
