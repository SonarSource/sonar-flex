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

import com.google.common.collect.ImmutableList;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.test.TestUtils;

import java.io.File;
import java.util.Collections;
import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CoberturaSensorTest {

  private Project project;
  private SensorContext context;
  private org.sonar.api.resources.File flexFile;

  @Before
  public void setUp() throws Exception {
    flexFile = new org.sonar.api.resources.File("example/File.as");
    context = mock(SensorContext.class);
    when(context.getResource(any(Resource.class))).thenReturn(flexFile);

    project = mockProject(Flex.KEY);

  }

  @Test
  public void shouldParseReport() throws Exception {
    File srcDir = TestUtils.getResource("org/sonar/plugins/flex/cobertura");

    ModuleFileSystem fs = mock(ModuleFileSystem.class);
    when(fs.sourceDirs()).thenReturn(ImmutableList.of(srcDir));

    ProjectFileSystem pfs = mock(ProjectFileSystem.class);
    when(pfs.getSourceDirs()).thenReturn(ImmutableList.of(srcDir));

    Project project1 = mock(Project.class);
    when(project1.getFileSystem()).thenReturn(pfs);

    Sensor sensor = newSensorWithProperty(FlexPlugin.COBERTURA_REPORT_PATH, TestUtils.getResource("org/sonar/plugins/flex/cobertura/coverage.xml").getPath(), fs);
    sensor.analyse(project1, context);

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.LINES_TO_COVER, 2.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.UNCOVERED_LINES, 2.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.COVERAGE_LINE_HITS_DATA, "1=0;7=0")));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.CONDITIONS_TO_COVER, 2.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.UNCOVERED_CONDITIONS, 1.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.CONDITIONS_BY_LINE, "1=2")));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.COVERED_CONDITIONS_BY_LINE, "1=1")));
  }

  @Test
  public void reportNotFound() {
    Sensor sensor = newSensorWithProperty(FlexPlugin.COBERTURA_REPORT_PATH, "/fake/path", mock(ModuleFileSystem.class));
    sensor.analyse(project, context);

    verify(context, never()).saveMeasure(any(org.sonar.api.resources.File.class), any(Metric.class), anyDouble());
  }

  @Test
  public void noReport() {
    CoberturaSensor sensor = new CoberturaSensor(new Settings(), null);
    sensor.analyse(project, context);

    verify(context, never()).saveMeasure(any(org.sonar.api.resources.File.class), any(Metric.class), anyDouble());
  }

  @Test
  public void test_should_execute_on_project() {
    Project project = mock(Project.class);
    ModuleFileSystem fs = mock(ModuleFileSystem.class);
    CoberturaSensor sensor = new CoberturaSensor(null, fs);


    // Multi-language mode check
    when(project.getLanguageKey()).thenReturn(null);

    when(fs.files(any(FileQuery.class))).thenReturn(Collections.<File>emptyList());
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(fs.files(any(FileQuery.class))).thenReturn(Collections.<File>singletonList(mock(File.class)));
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isTrue();

    // Compatibility 3.7
    when(project.getLanguageKey()).thenReturn("java");
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(project.getLanguageKey()).thenReturn(Flex.KEY);
    Assertions.assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
  }

  private Project mockProject(String languageKey) {
    Project prj = mock(Project.class);
    when(prj.getLanguageKey()).thenReturn(languageKey);

    return prj;
  }

  private Sensor newSensorWithProperty(String key, String value, ModuleFileSystem fs) {

    Properties props = new Properties();
    if (key != null && value != null) {
      props.put(key, value);
    }

    Settings settings = new Settings();
    settings.addProperties(props);

    return new CoberturaSensor(settings, fs);
  }

}
