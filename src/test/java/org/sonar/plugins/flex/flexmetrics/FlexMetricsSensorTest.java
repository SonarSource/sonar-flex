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

package org.sonar.plugins.flex.flexmetrics;

import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.sonar.api.utils.XpathParser;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.test.IsMeasure;
import org.sonar.plugins.flex.FlexPackage;
import org.sonar.plugins.flex.FlexFile;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.argThat;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;

public class FlexMetricsSensorTest {

  private FlexMetricsSensor sensor;
  SensorContext context;
  XpathParser parser;

  @Before
  public void init() {
    sensor = new FlexMetricsSensor(null);
    context= mock(SensorContext.class);
    File xmlFile = FileUtils.toFile(getClass().getResource("/org/sonar/plugins/flex/javancss-raw-report.xml"));
    parser = new XpathParser();
    parser.parse(xmlFile);
  }

  @Test
  public void testGetPackageAndClassFromFunction() {
    FlexMetricsSensor sensor = new FlexMetricsSensor(null);

    assertThat(sensor.getPackageAndClassFromFunction("com.almirun.common.controllers.PapervisionCameraController::PapervisionCameraController"),
      is("com.almirun.common.controllers.PapervisionCameraController"));
  }

  @Test
  public void testCollectPackageMeasures() {
    try {
      sensor.collectPackageMeasures(parser, context);
    }
    catch (ParseException e) {
    }
    verify(context).saveMeasure(new FlexPackage("com.almirun.common.controllers"), CoreMetrics.COMMENT_LINES, 30.0);
    verify(context).saveMeasure(new FlexPackage("com.almirun.common.events"), CoreMetrics.COMMENT_LINES, 0.0);
    verify(context).saveMeasure(new FlexPackage("com.almirun.common.events"), CoreMetrics.PACKAGES, 1.0);
  }

  @Test
  public void testCollectFileMeasures() {
    try {
      sensor.collectFileMeasures(parser, context);
    }
    catch (ParseException e) {
    }
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.StringManipulator"), CoreMetrics.NCLOC, 40.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.TimeFormatter"), CoreMetrics.NCLOC, 73.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.TimeFormatter"), CoreMetrics.CLASSES, 1.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.loremipsum.LoremIpsumUrlLoader"), CoreMetrics.CLASSES, 1.0);
    verify(context).saveMeasure(new FlexFile("com.almirun.common.util.loremipsum.LoremIpsumUrlLoader"), CoreMetrics.FUNCTIONS, 3.0);

    verify(context, never()).saveMeasure(eq(new FlexFile("com.almirun.common.controllers")), eq(CoreMetrics.NCLOC), anyDouble());
    verify(context, never()).saveMeasure(eq(new FlexFile("com.almirun.common.controllers")), eq(CoreMetrics.FUNCTIONS), anyDouble());
  }

  @Test
  public void testcComplexityClassMeasures() {
    try {
      sensor.createComplexityClassMeasures(parser, context);
    }
    catch (ParseException e) {
    }
    verify(context).saveMeasure(new FlexFile("com.almirun.common.data.BatchedQuery"), CoreMetrics.COMPLEXITY, 6.0);
    verify(context).saveMeasure(eq(new FlexFile("com.almirun.common.data.BatchedQuery")),argThat(
        new IsMeasure(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, "1=3;2=1;4=0;6=0;8=0;10=0;12=0")));
  }
}
