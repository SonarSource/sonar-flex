/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.testfixtures.log.LogTesterJUnit5;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;

import static org.assertj.core.api.Assertions.assertThat;

public class CoberturaSensorTest {

  private static final String TEST_DIR = "src/test/resources/org/sonar/plugins/flex/cobertura/";

  private CoberturaSensor sensor;
  private SensorContextTester tester;

  @RegisterExtension
  public LogTesterJUnit5 logTester = new LogTesterJUnit5();

  @BeforeEach
  public void setUp() throws Exception {
    sensor = new CoberturaSensor();
    tester = SensorContextTester.create(new File(TEST_DIR));
  }

  @Test
  public void shouldParseReport() throws Exception {
    setUpInputFile();

    tester.settings().setProperty(FlexPlugin.COBERTURA_REPORT_PATHS, "coverage.xml");
    sensor.execute(tester);

    String componentKey = "key:src/example/File.as";
    Integer[] expectedConditions = {2, null, null, null, null, null, null, null, null, null};
    Integer[] expectedCoveredConditions = {1, null, null, null, null, null, null, null, null, null};
    Integer[] expectedHits = {0, null, null, null, null, null, 0, null, null, null};
    for (int line = 1; line <= expectedConditions.length; line++) {
      assertThat(tester.coveredConditions(componentKey, line)).as("line " + line).isEqualTo(expectedCoveredConditions[line - 1]);
      assertThat(tester.conditions(componentKey, line)).as("line " + line).isEqualTo(expectedConditions[line - 1]);
      assertThat(tester.lineHits(componentKey, line)).as("line " + line).isEqualTo(expectedHits[line - 1]);
    }

    assertThat(logTester.logs()).containsOnly("Analyzing Cobertura report: coverage.xml");
  }

  @Test
  public void reportNotFound() {
    tester.settings().setProperty(FlexPlugin.COBERTURA_REPORT_PATHS, "/fake/path");
    sensor.execute(tester);

    assertThat(logTester.logs()).containsOnly("Cobertura xml report not found: /fake/path");
  }

  @Test
  public void noReport() {
    sensor.execute(tester);

    assertThat(logTester.logs()).containsOnly("No Cobertura report provided (see 'sonar.flex.cobertura.reportPaths' property)");
  }

  @Test
  public void testDescriptor() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    sensor.describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("Flex Cobertura");
    assertThat(descriptor.languages()).containsOnly("flex");
  }

  @Test
  public void reportWithBlankValues() throws IOException {
    setUpInputFile();

    tester.settings().setProperty(FlexPlugin.COBERTURA_REPORT_PATHS, "coverage-blank.xml");
    sensor.execute(tester);

    assertThat(logTester.logs()).containsOnly("Analyzing Cobertura report: coverage-blank.xml");
  }

  private void setUpInputFile() throws IOException {
    String content = new String(Files.readAllBytes(Paths.get(TEST_DIR, "src/example/File.as")), StandardCharsets.UTF_8);
    DefaultInputFile inputFile = TestInputFileBuilder.create("key", "src/example/File.as")
      .setLanguage(Flex.KEY)
      .setType(InputFile.Type.MAIN)
      .initMetadata(content)
      .build();

    tester.fileSystem().add(inputFile);
  }

}
