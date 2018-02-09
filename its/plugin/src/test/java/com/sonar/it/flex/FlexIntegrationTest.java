/*
 * SonarQube Flex Plugin
 * Copyright (C) 2012-2018 SonarSource SA
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
import com.sonar.orchestrator.build.MavenBuild;
import java.io.File;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonarqube.ws.WsMeasures.Measure;

import static com.sonar.it.flex.Tests.getComponent;
import static com.sonar.it.flex.Tests.getMeasure;
import static com.sonar.it.flex.Tests.getMeasureAsDouble;
import static junit.framework.Assert.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

public class FlexIntegrationTest {

  @ClassRule
  public static Orchestrator orchestrator = Tests.ORCHESTRATOR;

  private static final String PROJECT_AS3COMMONS = "org.as3commons:as3commons-project";
  private static final String MODULE_COMMONS_LANG = "org.as3commons:as3commons-lang";
  private static final String PACKAGE_COMMONS_LANG = keyFor("org/as3commons/lang");
  private static final String FILE_OBJECT_UTILS = keyFor("org/as3commons/lang/ObjectUtils.as");

  private static String keyFor(String s) {
    return "org.as3commons:as3commons-lang:src/main/actionscript/" + s;
  }

  @BeforeClass
  public static void init() {
    orchestrator.resetData();

    MavenBuild build = Tests.createMavenBuild()
      .setPom(new File("projects/as3commons/pom.xml"))
      .setCleanSonarGoals();
    orchestrator.executeBuild(build);
  }

  @Test
  public void commonsCollectionsIsAnalyzed() {
    assertThat(getComponent(PROJECT_AS3COMMONS).getName()).isEqualTo("AS3Commons Project");
    assertThat(getComponent(MODULE_COMMONS_LANG).getName()).isEqualTo("AS3Commons Lang");
    assertThat(getComponent(PACKAGE_COMMONS_LANG).getName()).isEqualTo("src/main/actionscript/org/as3commons/lang");
    assertThat(getComponent(FILE_OBJECT_UTILS).getName()).isEqualTo("ObjectUtils.as");
  }

  @Test
  public void projectsMetrics() {
    assertThat(getProjectMeasureAsDouble("ncloc")).isEqualTo(3673d);
    assertThat(getProjectMeasureAsDouble("statements")).isEqualTo(1814d);
    assertThat(getProjectMeasureAsDouble("lines")).isEqualTo(9627d);
    assertThat(getProjectMeasureAsDouble("files")).isEqualTo(69d);
    assertThat(getProjectMeasureAsDouble("classes")).isEqualTo(72d);
    assertThat(getProjectMeasureAsDouble("functions")).isEqualTo(484d);
    assertThat(getProjectMeasureAsDouble("comment_lines")).isEqualTo(3032d);
    assertThat(getProjectMeasureAsDouble("comment_lines_density")).isEqualTo(45.2);

    assertThat(getProjectMeasureAsDouble("duplicated_lines")).isZero();
    assertThat(getProjectMeasureAsDouble("duplicated_blocks")).isZero();
    assertThat(getProjectMeasureAsDouble("duplicated_lines_density")).isZero();
    assertThat(getProjectMeasureAsDouble("duplicated_files")).isZero();

    assertThat(getProjectMeasureAsDouble("complexity")).isEqualTo(1084);

    assertThat(getProjectMeasureAsDouble("file_complexity")).isEqualTo(15.7);
    assertThat(getProjectMeasure("function_complexity_distribution").getValue()).isEqualTo("1=187;2=100;4=47;6=28;8=10;10=2;12=10");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertThat(getProjectMeasureAsDouble("class_complexity_distribution")).isNull();
    assertThat(getProjectMeasure("file_complexity_distribution").getValue()).isEqualTo("0=31;5=16;10=9;20=6;30=5;60=1;90=1");

    // SONARPLUGINS-670: Different number of violations reported on different OSs
    // 216 on Linux, 217 on Windows
    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getProjectMeasure("violations"), anyOf(is(217), is(216)));
  }

  @Test
  public void moduleMetrics() {
    assertThat(getModuleMeasureAsDouble("ncloc")).isEqualTo(1620d);
    assertThat(getModuleMeasureAsDouble("lines")).isEqualTo(4503d);
    assertThat(getModuleMeasureAsDouble("files")).isEqualTo(19d);
    assertThat(getModuleMeasureAsDouble("statements")).isEqualTo(895d);
    assertThat(getModuleMeasureAsDouble("classes")).isEqualTo(19d);
    assertThat(getModuleMeasureAsDouble("functions")).isEqualTo(183d);
    assertThat(getModuleMeasureAsDouble("comment_lines")).isEqualTo(1664d);
    assertThat(getModuleMeasureAsDouble("comment_lines_density")).isEqualTo(50.7);

    assertThat(getModuleMeasureAsDouble("duplicated_lines")).isZero();
    assertThat(getModuleMeasureAsDouble("duplicated_blocks")).isZero();
    assertThat(getModuleMeasureAsDouble("duplicated_lines_density")).isZero();
    assertThat(getModuleMeasureAsDouble("duplicated_files")).isZero();

    assertThat(getModuleMeasureAsDouble("complexity")).isEqualTo(639d);

    assertThat(getModuleMeasureAsDouble("file_complexity")).isEqualTo(33.6);
    assertThat(getModuleMeasure("function_complexity_distribution").getValue()).isEqualTo("1=65;2=49;4=32;6=18;8=6;10=1;12=8");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertNull(getModuleMeasureAsDouble("class_complexity_distribution"));
    assertThat(getModuleMeasure("file_complexity_distribution").getValue()).isEqualTo("0=7;5=4;10=2;20=1;30=3;60=1;90=1");

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getModuleMeasure("violations")).isEqualTo(114);
  }

  @Test
  public void packagesMetrics() {
    assertThat(getPackageMeasureAsDouble("ncloc")).isEqualTo(1439d);
    assertThat(getPackageMeasureAsDouble("lines")).isEqualTo(4142d);
    assertThat(getPackageMeasureAsDouble("files")).isEqualTo(16d);
    assertThat(getPackageMeasureAsDouble("statements")).isEqualTo(795d);
    assertThat(getPackageMeasureAsDouble("classes")).isEqualTo(16d);
    assertThat(getPackageMeasureAsDouble("functions")).isEqualTo(165d);
    assertThat(getPackageMeasureAsDouble("comment_lines")).isEqualTo(1603d);
    assertThat(getPackageMeasureAsDouble("comment_lines_density")).isEqualTo(52.7);

    assertThat(getPackageMeasureAsDouble("duplicated_lines")).isZero();
    assertThat(getPackageMeasureAsDouble("duplicated_blocks")).isZero();
    assertThat(getPackageMeasureAsDouble("duplicated_lines_density")).isZero();
    assertThat(getPackageMeasureAsDouble("duplicated_files")).isZero();

    assertThat(getPackageMeasureAsDouble("complexity")).isEqualTo(576);

    assertThat(getPackageMeasureAsDouble("class_complexity")).isEqualTo(36.0);
    assertThat(getPackageMeasureAsDouble("file_complexity")).isEqualTo(36.0);
    assertThat(getPackageMeasure("function_complexity_distribution").getValue()).isEqualTo("1=58;2=45;4=30;6=15;8=6;10=1;12=7");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertNull(getPackageMeasureAsDouble("class_complexity_distribution"));
    assertThat(getPackageMeasure("file_complexity_distribution").getValue()).isEqualTo("0=6;5=4;10=1;20=1;30=2;60=1;90=1");

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getPackageMeasure("violations")).isEqualTo(93);
  }

  @Test
  public void filesMetrics() {
    assertThat(getFileMeasureAsDouble("ncloc")).isEqualTo(75d);
    assertThat(getFileMeasureAsDouble("lines")).isEqualTo(194d);
    assertThat(getFileMeasureAsDouble("files")).isEqualTo(1d);
    assertThat(getFileMeasureAsDouble("statements")).isEqualTo(39d);
    assertThat(getFileMeasureAsDouble("classes")).isEqualTo(1d);
    assertThat(getFileMeasureAsDouble("functions")).isEqualTo(10d);
    assertThat(getFileMeasureAsDouble("comment_lines")).isEqualTo(62d);
    assertThat(getFileMeasureAsDouble("comment_lines_density")).isEqualTo(45.3);

    assertThat(getFileMeasureAsDouble("duplicated_lines")).isZero();
    assertThat(getFileMeasureAsDouble("duplicated_blocks")).isZero();
    assertThat(getFileMeasureAsDouble("duplicated_files")).isZero();
    assertThat(getFileMeasureAsDouble("duplicated_lines_density")).isZero();

    assertThat(getFileMeasureAsDouble("complexity")).isEqualTo(20d);

    assertThat(getFileMeasureAsDouble("file_complexity")).isEqualTo(20.0);
    assertNull(getFileMeasureAsDouble("function_complexity_distribution"));
    assertNull(getFileMeasureAsDouble("class_complexity_distribution"));
    assertNull(getFileMeasureAsDouble("file_complexity_distribution"));

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getFileMeasure("violations")).isEqualTo(24);
  }

  /* Helper methods */

  private Measure getProjectMeasure(String metricKey) {
    return getMeasure(PROJECT_AS3COMMONS, metricKey);
  }

  private Double getProjectMeasureAsDouble(String metricKey) {
    return getMeasureAsDouble(PROJECT_AS3COMMONS, metricKey);
  }

  private Measure getModuleMeasure(String metricKey) {
    return getMeasure(MODULE_COMMONS_LANG, metricKey);
  }

  private Double getModuleMeasureAsDouble(String metricKey) {
    return getMeasureAsDouble(MODULE_COMMONS_LANG, metricKey);
  }

  private Measure getPackageMeasure(String metricKey) {
    return getMeasure(PACKAGE_COMMONS_LANG, metricKey);
  }

  private Double getPackageMeasureAsDouble(String metricKey) {
    return getMeasureAsDouble(PACKAGE_COMMONS_LANG, metricKey);
  }

  private Double getFileMeasureAsDouble(String metricKey) {
    return getMeasureAsDouble(FILE_OBJECT_UTILS, metricKey);
  }

}
