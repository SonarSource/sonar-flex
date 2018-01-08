/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.rule.internal.DefaultActiveRules;
import org.sonar.api.batch.rule.internal.NewActiveRule;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlexSquidSensorTest {

  private static final File TEST_DIR = new File("src/test/resources/org/sonar/plugins/flex/squid");

  private FlexSquidSensor sensor;
  private SensorContextTester tester;

  @Before
  public void setUp() throws Exception {
    NewActiveRule ar = new ActiveRulesBuilder().create(RuleKey.of("flex", "S1125")).setSeverity("BLOCKER");
    ActiveRules activeRules = new DefaultActiveRules(Collections.singletonList(ar));
    CheckFactory checkFactory = new CheckFactory(activeRules);
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    when(fileLinesContextFactory.createFor(Mockito.any(InputFile.class))).thenReturn(mock(FileLinesContext.class));
    sensor = new FlexSquidSensor(checkFactory, fileLinesContextFactory);
    tester = SensorContextTester.create(TEST_DIR);
  }

  @Test
  public void analyse() throws FileNotFoundException {
    DefaultFileSystem fs = new DefaultFileSystem(TEST_DIR);
    tester.setFileSystem(fs);
    DefaultInputFile inputFile = new DefaultInputFile("key", "SmallFile.as")
      .setType(InputFile.Type.MAIN)
      .setLanguage(Flex.KEY)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(new File(TEST_DIR, "SmallFile.as"))));
    fs.add(inputFile);
    inputFile = new DefaultInputFile("key", "bom.as")
      .setType(InputFile.Type.MAIN)
      .setLanguage(Flex.KEY)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(new File(TEST_DIR, "bom.as"))));
    fs.add(inputFile);

    sensor.execute(tester);

    String componentKey = "key:SmallFile.as";
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY_IN_CLASSES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(11);
    assertThat(tester.measure(componentKey, CoreMetrics.COMMENT_LINES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.STATEMENTS).value()).isEqualTo(3);
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTIONS).value()).isEqualTo(2);
    assertThat(tester.measure(componentKey, CoreMetrics.CLASSES).value()).isEqualTo(1);
    assertThat(tester.measure(componentKey, CoreMetrics.COMPLEXITY).value()).isEqualTo(3);
    assertThat(tester.measure(componentKey, CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("0=1;5=0;10=0;20=0;30=0;60=0;90=0");
    assertThat(tester.measure(componentKey, CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION).value()).isEqualTo("1=1;2=1;4=0;6=0;8=0;10=0;12=0");

    assertThat(tester.cpdTokens(componentKey)).hasSize(10);

    assertThat(tester.highlightingTypeAt(componentKey, 1, 0)).containsOnly(TypeOfText.KEYWORD);
    assertThat(tester.highlightingTypeAt(componentKey, 3, 0)).containsOnly(TypeOfText.KEYWORD);
    assertThat(tester.highlightingTypeAt(componentKey, 3, 7)).containsOnly(TypeOfText.KEYWORD);
    assertThat(tester.highlightingTypeAt(componentKey, 11, 0)).containsOnly(TypeOfText.KEYWORD);
    assertThat(tester.highlightingTypeAt(componentKey, 5, 4)).containsOnly(TypeOfText.COMMENT);
    assertThat(tester.highlightingTypeAt(componentKey, 6, 10)).containsOnly(TypeOfText.CONSTANT);
    assertThat(tester.highlightingTypeAt(componentKey, 7, 10)).containsOnly(TypeOfText.STRING);

    assertThat(tester.allIssues()).hasSize(1);

    componentKey = "key:bom.as";
    assertThat(tester.highlightingTypeAt(componentKey, 1, 0)).containsOnly(TypeOfText.COMMENT);
    assertThat(tester.highlightingTypeAt(componentKey, 2, 0)).containsOnly(TypeOfText.COMMENT);
  }

  @Test
  public void analyse2() throws FileNotFoundException {
    DefaultFileSystem fs = new DefaultFileSystem(TEST_DIR);
    tester.setFileSystem(fs);
    DefaultInputFile inputFile = new DefaultInputFile("key", "TimeFormatter.as")
      .setType(InputFile.Type.MAIN)
      .setLanguage(Flex.KEY)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(new File(TEST_DIR, "TimeFormatter.as"))));
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

    assertThat(tester.cpdTokens(componentKey)).hasSize(0);

    assertThat(tester.allIssues()).hasSize(0);
  }

  @Test
  public void testDescriptor() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    sensor.describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("Flex");
    assertThat(descriptor.languages()).containsOnly("flex");
  }
}
