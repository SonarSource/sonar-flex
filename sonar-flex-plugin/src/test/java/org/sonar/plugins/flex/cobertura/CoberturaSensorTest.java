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

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.test.TestUtils;

import java.util.Properties;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CoberturaSensorTest {

  private CoberturaSensor sensor;
  private Project project;

  @Before
  public void setUp() throws Exception {
    Settings settings = new Settings();
    Properties props = new Properties();
    props.put(FlexPlugin.COBERTURA_REPORT_PATH, TestUtils.getResource("org/sonar/plugins/flex/cobertura/coverage.xml").getPath());
    settings.addProperties(props);

    project = mock(Project.class);

    sensor = new CoberturaSensor(settings);
  }

  @Test
  public void shouldParseReport() throws Exception {
    org.sonar.api.resources.File flexFile = new org.sonar.api.resources.File("example/File.as");
    SensorContext context = mock(SensorContext.class);
    when(context.getResource(any(Resource.class))).thenReturn(flexFile);

    sensor.analyse(project, context);

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.LINES_TO_COVER, 2.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.UNCOVERED_LINES, 2.0)));

    verify(context).saveMeasure(
      eq(flexFile),
      argThat(new IsMeasure(CoreMetrics.COVERAGE_LINE_HITS_DATA, "1=0;7=0")));

  }
}
