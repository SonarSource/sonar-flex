/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.plugins.flex;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlexSquidSensorTest {

  private static final File TEST_DIR = new File("src/test/resources/org/sonar/plugins/flex/");

  private FlexSquidSensor sensor;
  private SensorContextTester tester;

  @Before
  public void setUp() throws Exception {
    CheckFactory checkFactory = new CheckFactory(mock(ActiveRules.class));
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    when(fileLinesContextFactory.createFor(Mockito.any(InputFile.class))).thenReturn(mock(FileLinesContext.class));
    sensor = new FlexSquidSensor(checkFactory, fileLinesContextFactory);
    tester = SensorContextTester.create(TEST_DIR);
  }

  @Test
  public void analyse() throws FileNotFoundException {
    DefaultFileSystem fs = new DefaultFileSystem(new File(TEST_DIR, "duplications"));
    tester.setFileSystem(fs);
    DefaultInputFile inputFile = new DefaultInputFile("key", "SmallFile.as")
      .setType(InputFile.Type.MAIN)
      .setLanguage(Flex.KEY)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(new File(TEST_DIR, "duplications/SmallFile.as"))));
    fs.add(inputFile);

    sensor.execute(tester);

    String componentKey = inputFile.key();
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY_IN_CLASSES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(9);
    assertThat(tester.measure(componentKey, CoreMetrics.COMMENT_LINES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.STATEMENTS).value()).isEqualTo(2);
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTIONS).value()).isEqualTo(2);
    assertThat(tester.measure(componentKey, CoreMetrics.CLASSES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY).value()).isEqualTo(2);
    assertThat(tester.measure(componentKey, CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("0=1;5=0;10=0;20=0;30=0;60=0;90=0");
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("1=2;2=0;4=0;6=0;8=0;10=0;12=0");
  }

  @Test
  public void analyse2() throws FileNotFoundException {
    DefaultFileSystem fs = new DefaultFileSystem(new File(TEST_DIR, "squid"));
    tester.setFileSystem(fs);
    DefaultInputFile inputFile = new DefaultInputFile("key", "TimeFormatter.as")
      .setType(InputFile.Type.MAIN)
      .setLanguage(Flex.KEY)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(new File(TEST_DIR, "squid/TimeFormatter.as"))));
    fs.add(inputFile);

    sensor.execute(tester);

    String componentKey = inputFile.key();
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY_IN_CLASSES).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.COMMENT_LINES).value()).isEqualTo(59);
    assertThat(tester.measure(componentKey, CoreMetrics.STATEMENTS).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTIONS).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.CLASSES).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY).value()).isEqualTo(0);
    assertThat(tester.measure(componentKey, CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("0=1;5=0;10=0;20=0;30=0;60=0;90=0");
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("1=0;2=0;4=0;6=0;8=0;10=0;12=0");
  }
}
