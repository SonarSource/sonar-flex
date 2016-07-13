/*
 * SonarQube Flex Plugin
 * Copyright (C) 2012-2016 SonarSource SA
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
package com.sonar.it.flex;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.SonarScanner;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * This test class is used to reproduce various issues.
 */
public class FlexSimpleProjectTest {

  private static final String PROJECT = "simple-project";

  @ClassRule
  public static Orchestrator orchestrator = Tests.ORCHESTRATOR;

  private static Sonar sonar = null;

  private static String keyFor(String s) {
    return PROJECT + ":src/" + s;
  }

  @BeforeClass
  public static void init() {
    orchestrator.resetData();
    sonar = orchestrator.getServer().getWsClient();

    SonarScanner runner = Tests.createSonarScanner()
      .setProjectDir(new File("projects/simple-project"))
      .setProperty("sonar.profile", "it-profile");
    orchestrator.executeBuild(runner);
  }

  @Test
  public void projectIsAnalyzed() {
    assertThat(sonar.find(new ResourceQuery(PROJECT)).getName()).isEqualTo("Flex IT :: Simple Project");
    assertThat(sonar.find(new ResourceQuery(PROJECT)).getVersion()).isEqualTo("1.0");
  }

  /**
   * SONARPLUGINS-903: metrics from FlexMetrics were associated to the wrong file
   * if 2 files had the same name but different extensions
   */
  @Test
  public void projectTestMetrics() {
    assertThat(getFileMeasure(keyFor("same_name/XMLUtil.as"), "ncloc").getIntValue()).isEqualTo(49);
    // TODO mxml support has been broken
    // assertThat(getFileMeasure(PROJECT + ":same_name/XMLUtil.mxml", "ncloc").getIntValue(), is(12));
  }

  /**
   * SONARPLUGINS-2181
   */
  @Test
  public void should_be_compatible_with_DevCockpit() {
    String fileKey = keyFor("DevCockpit.as");
    assertThat(getFileMeasure(fileKey, "ncloc_data").getData())
      .contains("1=1")
      .doesNotContain("2=1");
    assertThat(getFileMeasure(fileKey, "comment_lines_data").getData())
      .doesNotContain("1=1")
      .contains("2=1");
  }

  /* Helper methods */

  private Measure getFileMeasure(String fileKey, String metricKey) {
    Resource resource = sonar.find(ResourceQuery.createForMetrics(fileKey, metricKey));
    return resource != null ? resource.getMeasure(metricKey) : null;
  }

}
