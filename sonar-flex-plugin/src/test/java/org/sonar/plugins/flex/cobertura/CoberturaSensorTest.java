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
package org.sonar.plugins.flex.cobertura;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.sensor.coverage.CoverageType;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;
import java.io.FileReader;

import static org.fest.assertions.Assertions.assertThat;

public class CoberturaSensorTest {

  private static final String TEST_DIR = "src/test/resources/org/sonar/plugins/flex/cobertura/";

  private CoberturaSensor sensor;
  private SensorContextTester tester;

  @Before
  public void setUp() throws Exception {
    sensor = new CoberturaSensor();
    tester = SensorContextTester.create(new File(TEST_DIR));
  }

  @Test
  public void shouldParseReport() throws Exception {
    DefaultInputFile inputFile = new DefaultInputFile("key", "src/example/File.as")
      .setLanguage(Flex.KEY)
      .setType(InputFile.Type.MAIN)
      .initMetadata(new FileMetadata().readMetadata(new FileReader(TEST_DIR + "src/example/File.as")));

    tester.fileSystem().add(inputFile);

    tester.settings().setProperty(FlexPlugin.COBERTURA_REPORT_PATH, "coverage.xml");
    sensor.execute(tester);

    String componentKey = "key:src/example/File.as";
    assertThat(tester.coveredConditions(componentKey, CoverageType.UNIT, 1)).isEqualTo(1);
  }

  @Test
  public void reportNotFound() {
    tester.settings().setProperty(FlexPlugin.COBERTURA_REPORT_PATH, "/fake/path");
    sensor.execute(tester);
  }

  @Test
  public void noReport() {
    sensor.execute(tester);
  }

  @Test
  public void testDescriptor() {
    DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
    sensor.describe(descriptor);
    assertThat(descriptor.name()).isEqualTo("Flex Cobertura");
    assertThat(descriptor.languages()).containsOnly("flex");
  }

}
