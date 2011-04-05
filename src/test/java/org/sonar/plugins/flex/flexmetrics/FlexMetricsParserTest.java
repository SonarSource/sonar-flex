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

package org.sonar.plugins.flex.flexmetrics;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.flex.FlexFile;
import org.sonar.plugins.flex.FlexPackage;

import java.io.File;
import java.text.ParseException;

public class FlexMetricsParserTest {

  private SensorContext context;
  private FlexMetricsParser parser;
  private File xmlFile;

  @Before
  public void setUp() {
    context = mock(SensorContext.class);
    xmlFile = FileUtils.toFile(getClass().getResource("/org/sonar/plugins/flex/flexmetrics/javancss-raw-report.xml"));
    parser = new FlexMetricsParser(context);
  }

  @Test
  public void shouldGetPackageAndClassFromFunction() {
    assertThat(
        FlexMetricsParser.getPackageAndClassFromFunction("com.almirun.common.controllers.PapervisionCameraController::PapervisionCameraController"),
        is("com.almirun.common.controllers.PapervisionCameraController"));
  }

  @Test
  public void shouldCollectPackageMeasures() {
    parser.parse(xmlFile);

    verify(context).saveMeasure(new FlexPackage("com.almirun.common.events"), CoreMetrics.PACKAGES, 1.0);

    verify(context, never()).saveMeasure(eq(new FlexPackage("com.almirun.common.controllers")), eq(CoreMetrics.COMMENT_LINES), anyDouble());
    verify(context, never()).saveMeasure(eq(new FlexPackage("com.almirun.common.events")), eq(CoreMetrics.COMMENT_LINES), anyDouble());
  }

  @Test
  public void shouldCollectFileMeasures() {
    parser.parse(xmlFile);

    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.StringManipulator"), CoreMetrics.NCLOC, 40.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.TimeFormatter"), CoreMetrics.NCLOC, 73.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.TimeFormatter"), CoreMetrics.CLASSES, 1.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.loremipsum.LoremIpsumUrlLoader"), CoreMetrics.CLASSES, 1.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.loremipsum.LoremIpsumUrlLoader"), CoreMetrics.FUNCTIONS, 3.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.net.SmartUrlLoader"), CoreMetrics.COMMENT_LINES, 46.0);

    verify(context, never()).saveMeasure(eq(new FlexFile("com.almirun.common.controllers")), eq(CoreMetrics.NCLOC), anyDouble());
    verify(context, never()).saveMeasure(eq(new FlexFile("com.almirun.common.controllers")), eq(CoreMetrics.FUNCTIONS), anyDouble());
  }

  @Test
  public void shouldCollectComplexityMeasures() throws ParseException {
    parser.parse(xmlFile);

    verify(context).saveMeasure(
        new FlexFile("com.almirun.common.data.BatchedQuery"), CoreMetrics.COMPLEXITY, 6.0);
    verify(context).saveMeasure(
        eq(new FlexFile("com.almirun.common.data.BatchedQuery")),
        argThat(new IsMeasure(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, "1=3;2=1;4=0;6=0;8=0;10=0;12=0")));
    verify(context).saveMeasure(
        eq(new FlexFile("com.almirun.common.data.BatchedQuery")),
        argThat(new IsMeasure(CoreMetrics.CLASS_COMPLEXITY_DISTRIBUTION, "0=0;5=1;10=0;20=0;30=0;60=0;90=0")));
  }

}
