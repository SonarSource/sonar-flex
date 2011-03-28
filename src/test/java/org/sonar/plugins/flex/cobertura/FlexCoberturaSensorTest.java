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

package org.sonar.plugins.flex.cobertura;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.FlexFile;

import java.io.File;

public class FlexCoberturaSensorTest {
  private FlexCoberturaSensor sensor;
  private Project project;

  @Before
  public void setUp() throws Exception {
    sensor = new FlexCoberturaSensor();
    project = mock(Project.class);
  }

  @Test
  public void shouldAnalyseIfReuseReports() {
    when(project.getLanguageKey()).thenReturn(Flex.KEY);
    when(project.getAnalysisType())
        .thenReturn(Project.AnalysisType.REUSE_REPORTS);
    assertThat(sensor.shouldExecuteOnProject(project), is(true));
  }

  @Test
  public void shouldParseReport() throws Exception {
    FlexFile flexFile = new FlexFile("com.tgeorgiev.util.BasicArithmeticOperations");
    SensorContext context = mock(SensorContext.class);
    when(context.getResource(any(Resource.class))).thenReturn(flexFile);

    File xmlFile = new File(getClass().getResource("coverage.xml").toURI());
    sensor.parseReport(xmlFile, context);

    verify(context).saveMeasure(
        eq(flexFile),
        argThat(new IsMeasure(CoreMetrics.COVERAGE, 66.7)));
    verify(context).saveMeasure(
        eq(flexFile),
        argThat(new IsMeasure(CoreMetrics.LINE_COVERAGE, 66.7)));
    verify(context).saveMeasure(
        eq(flexFile),
        argThat(new IsMeasure(CoreMetrics.LINES_TO_COVER, 6.0)));
    verify(context).saveMeasure(
        eq(flexFile),
        argThat(new IsMeasure(CoreMetrics.UNCOVERED_LINES, 2.0)));
    verify(context).saveMeasure(
        eq(flexFile),
        argThat(new IsMeasure(CoreMetrics.COVERAGE_LINE_HITS_DATA,
            "1=1;12=1;17=1;22=0;26=0;7=1")));
  }
}
