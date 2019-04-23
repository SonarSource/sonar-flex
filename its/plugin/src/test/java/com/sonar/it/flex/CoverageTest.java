/*
 * SonarQube Flex Plugin
 * Copyright (C) 2012-2019 SonarSource SA
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
package com.sonar.it.flex;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarScanner;
import java.io.File;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static com.sonar.it.flex.Tests.getMeasureAsInteger;
import static org.assertj.core.api.Assertions.assertThat;

public class CoverageTest {

  @ClassRule
  public static Orchestrator orchestrator = Tests.ORCHESTRATOR;

  private static final String REPORT_PATH = "reports/coverage/coverage.xml";
  private static final String REPORT_PROPERTY_KEY = "sonar.flex.cobertura.reportPath";
  private static final String SRC_DIR = "src/main/flex";
  private static final File PROJECT_DIR = new File("projects/coverage");

  @Before
  public void clean() {
    orchestrator.resetData();
  }

  @Test
  public void report_path_can_be_relative() throws Exception {
    SonarScanner build = Tests.createSonarScanner()
      .setProjectDir(PROJECT_DIR)
      .setProjectKey("project")
      .setProjectName("project")
      .setProjectVersion("1.0")
      .setSourceDirs(SRC_DIR)
      .setProperty(REPORT_PROPERTY_KEY, REPORT_PATH);
    orchestrator.executeBuild(build);

    assertThat(getProjectMeasureAsInteger("lines_to_cover")).isEqualTo(2);
    assertThat(getProjectMeasureAsInteger("uncovered_lines")).isEqualTo(2);
    assertThat(getProjectMeasureAsInteger("conditions_to_cover")).isNull();
    assertThat(getProjectMeasureAsInteger("uncovered_conditions")).isNull();
  }

  @Test
  public void report_path_can_be_absolute() {
    SonarScanner build = Tests.createSonarScanner()
      .setProjectDir(PROJECT_DIR)
      .setProjectKey("project")
      .setProjectName("project")
      .setProjectVersion("1.0")
      .setSourceDirs(SRC_DIR)
      .setProperty(REPORT_PROPERTY_KEY, new File("projects/coverage/" + REPORT_PATH).getAbsolutePath());
    orchestrator.executeBuild(build);

    assertThat(getProjectMeasureAsInteger("lines_to_cover")).isEqualTo(2);
    assertThat(getProjectMeasureAsInteger("uncovered_lines")).isEqualTo(2);
    assertThat(getProjectMeasureAsInteger("conditions_to_cover")).isNull();
    assertThat(getProjectMeasureAsInteger("uncovered_conditions")).isNull();
  }

  private Integer getProjectMeasureAsInteger(String metricKey) {
    return getMeasureAsInteger("project", metricKey);
  }

}
