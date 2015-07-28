/*
 * Flex Plugin :: Integration Tests
 * Copyright (C) 2012 SonarSource
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
package com.sonar.it.flex;

import com.sonar.orchestrator.Orchestrator;
import com.sonar.orchestrator.build.MavenBuild;
import static junit.framework.Assert.assertNull;
import static org.fest.assertions.Assertions.assertThat;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.io.File;

public class FlexIntegrationTest {

  @ClassRule
  public static Orchestrator orchestrator = Tests.ORCHESTRATOR;

  private static final String PROJECT_AS3COMMONS = "org.as3commons:as3commons-project";
  private static final String MODULE_COMMONS_LANG = "org.as3commons:as3commons-lang";
  private static final String PACKAGE_COMMONS_LANG = keyFor("org/as3commons/lang");
  private static final String FILE_OBJECT_UTILS = keyFor("org/as3commons/lang/ObjectUtils.as");

  private static String keyFor(String s) {
    return "org.as3commons:as3commons-lang:src/main/actionscript/"  + s;
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
    assertThat(orchestrator.getServer().getWsClient().find(new ResourceQuery(PROJECT_AS3COMMONS)).getName()).isEqualTo("AS3Commons Project");
    assertThat(orchestrator.getServer().getWsClient().find(new ResourceQuery(PROJECT_AS3COMMONS)).getVersion()).isEqualTo("1.0.0");
    assertThat(orchestrator.getServer().getWsClient().find(new ResourceQuery(MODULE_COMMONS_LANG)).getName()).isEqualTo("AS3Commons Lang");
    assertThat(orchestrator.getServer().getWsClient().find(new ResourceQuery(PACKAGE_COMMONS_LANG)).getName()).isEqualTo("src/main/actionscript/org/as3commons/lang");
    assertThat(orchestrator.getServer().getWsClient().find(new ResourceQuery(FILE_OBJECT_UTILS)).getName()).isEqualTo("ObjectUtils.as");
  }

  @Test
  public void projectsMetrics() {
    assertThat(getProjectMeasure("ncloc").getIntValue()).isEqualTo(3673);
    assertThat(getProjectMeasure("statements").getIntValue()).isEqualTo(1814);
    assertThat(getProjectMeasure("lines").getIntValue()).isEqualTo(9627);
    assertThat(getProjectMeasure("files").getIntValue()).isEqualTo(69);
    assertThat(getProjectMeasure("classes").getIntValue()).isEqualTo(72);
    assertThat(getProjectMeasure("functions").getIntValue()).isEqualTo(484);
    assertThat(getProjectMeasure("comment_lines").getIntValue()).isEqualTo(3032);
    assertThat(getProjectMeasure("comment_lines_density").getValue()).isEqualTo(45.2);

    assertThat(getProjectMeasure("duplicated_lines").getIntValue()).isEqualTo(0);
    assertThat(getProjectMeasure("duplicated_blocks").getIntValue()).isEqualTo(0);
    assertThat(getProjectMeasure("duplicated_lines_density").getValue()).isEqualTo(0.0);
    assertThat(getProjectMeasure("duplicated_files").getIntValue()).isEqualTo(0);

    assertThat(getProjectMeasure("complexity").getIntValue()).isEqualTo(1084);
    assertThat(getProjectMeasure("function_complexity").getValue()).isEqualTo(2.2);
    assertThat(getProjectMeasure("class_complexity").getValue()).isEqualTo(15.1);
    assertThat(getProjectMeasure("file_complexity").getValue()).isEqualTo(15.7);
    assertThat(getProjectMeasure("function_complexity_distribution").getData()).isEqualTo("1=187;2=100;4=47;6=28;8=10;10=2;12=10");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertThat(getProjectMeasure("class_complexity_distribution")).isNull();
    assertThat(getProjectMeasure("file_complexity_distribution").getData()).isEqualTo("0=31;5=16;10=9;20=6;30=5;60=1;90=1");

    // SONARPLUGINS-670: Different number of violations reported on different OSs
    // 216 on Linux, 217 on Windows
    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getProjectMeasure("violations").getIntValue(), anyOf(is(217), is(216)));
  }

  @Test
  public void moduleMetrics() {
    assertThat(getModuleMeasure("ncloc").getIntValue()).isEqualTo(1620);
    assertThat(getModuleMeasure("lines").getIntValue()).isEqualTo(4503);
    assertThat(getModuleMeasure("files").getIntValue()).isEqualTo(19);
    assertThat(getModuleMeasure("statements").getIntValue()).isEqualTo(895);
    assertThat(getModuleMeasure("classes").getIntValue()).isEqualTo(19);
    assertThat(getModuleMeasure("functions").getIntValue()).isEqualTo(183);
    assertThat(getModuleMeasure("comment_lines").getIntValue()).isEqualTo(1664);
    assertThat(getModuleMeasure("comment_lines_density").getValue()).isEqualTo(50.7);

    assertThat(getModuleMeasure("duplicated_lines").getIntValue()).isEqualTo(0);
    assertThat(getModuleMeasure("duplicated_blocks").getIntValue()).isEqualTo(0);
    assertThat(getModuleMeasure("duplicated_lines_density").getValue()).isEqualTo(0.0);
    assertThat(getModuleMeasure("duplicated_files").getIntValue()).isEqualTo(0);

    assertThat(getModuleMeasure("complexity").getIntValue()).isEqualTo(639);
    assertThat(getModuleMeasure("function_complexity").getValue()).isEqualTo(3.5);
    assertThat(getModuleMeasure("class_complexity").getValue()).isEqualTo(33.6);
    assertThat(getModuleMeasure("file_complexity").getValue()).isEqualTo(33.6);
    assertThat(getModuleMeasure("function_complexity_distribution").getData()).isEqualTo("1=65;2=49;4=32;6=18;8=6;10=1;12=8");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertNull(getModuleMeasure("class_complexity_distribution"));
    assertThat(getModuleMeasure("file_complexity_distribution").getData()).isEqualTo("0=7;5=4;10=2;20=1;30=3;60=1;90=1");

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getModuleMeasure("violations").getIntValue()).isEqualTo(114);
  }

  @Test
  public void packagesMetrics() {
    assertThat(getPackageMeasure("ncloc").getIntValue()).isEqualTo(1439);
    assertThat(getPackageMeasure("lines").getIntValue()).isEqualTo(4142);
    assertThat(getPackageMeasure("files").getIntValue()).isEqualTo(16);
    assertThat(getPackageMeasure("statements").getIntValue()).isEqualTo(795);
    assertThat(getPackageMeasure("classes").getIntValue()).isEqualTo(16);
    assertThat(getPackageMeasure("functions").getIntValue()).isEqualTo(165);
    assertThat(getPackageMeasure("comment_lines").getIntValue()).isEqualTo(1603);
    assertThat(getPackageMeasure("comment_lines_density").getValue()).isEqualTo(52.7);

    assertThat(getPackageMeasure("duplicated_lines").getIntValue()).isEqualTo(0);
    assertThat(getPackageMeasure("duplicated_blocks").getIntValue()).isEqualTo(0);
    assertThat(getPackageMeasure("duplicated_lines_density").getValue()).isEqualTo(0.0);
    assertThat(getPackageMeasure("duplicated_files").getIntValue()).isEqualTo(0);

    assertThat(getPackageMeasure("complexity").getIntValue()).isEqualTo(576);
    assertThat(getPackageMeasure("function_complexity").getValue()).isEqualTo(3.5);
    assertThat(getPackageMeasure("class_complexity").getValue()).isEqualTo(36.0);
    assertThat(getPackageMeasure("file_complexity").getValue()).isEqualTo(36.0);
    assertThat(getPackageMeasure("function_complexity_distribution").getData()).isEqualTo("1=58;2=45;4=30;6=15;8=6;10=1;12=7");
    // SONARPLUGINS-1708 class_complexity_distribution replaced by file_complexity_distribution
    assertNull(getPackageMeasure("class_complexity_distribution"));
    assertThat(getPackageMeasure("file_complexity_distribution").getData()).isEqualTo("0=6;5=4;10=1;20=1;30=2;60=1;90=1");

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getPackageMeasure("violations").getIntValue()).isEqualTo(93);
  }

  @Test
  public void filesMetrics() {
    assertThat(getFileMeasure("ncloc").getIntValue()).isEqualTo(75);
    assertThat(getFileMeasure("lines").getIntValue()).isEqualTo(194);
    assertThat(getFileMeasure("files").getIntValue()).isEqualTo(1);
    assertThat(getFileMeasure("statements").getIntValue()).isEqualTo(39);
    assertThat(getFileMeasure("classes").getIntValue()).isEqualTo(1);
    assertThat(getFileMeasure("functions").getIntValue()).isEqualTo(10);
    assertThat(getFileMeasure("comment_lines").getIntValue()).isEqualTo(62);
    assertThat(getFileMeasure("comment_lines_density").getValue()).isEqualTo(45.3);

    assertNull(getFileMeasure("duplicated_lines"));
    assertNull(getFileMeasure("duplicated_blocks"));
    assertNull(getFileMeasure("duplicated_files"));
    assertNull(getFileMeasure("duplicated_lines_density"));

    assertThat(getFileMeasure("complexity").getIntValue()).isEqualTo(20);
    assertThat(getFileMeasure("function_complexity").getValue()).isEqualTo(2.0);
    assertThat(getFileMeasure("class_complexity").getValue()).isEqualTo(20.0);
    assertThat(getFileMeasure("file_complexity").getValue()).isEqualTo(20.0);
    assertNull(getFileMeasure("function_complexity_distribution"));
    assertNull(getFileMeasure("class_complexity_distribution"));
    assertNull(getFileMeasure("file_complexity_distribution"));

    // TODO we should be sure that numbers are stable, whereas this is not the case, because profile may change
    // assertThat(getFileMeasure("violations").getIntValue()).isEqualTo(24);
  }

  /* Helper methods */

  private Measure getProjectMeasure(String metricKey) {
    Resource resource = orchestrator.getServer().getWsClient().find(ResourceQuery.createForMetrics(PROJECT_AS3COMMONS, metricKey));
    return resource != null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getModuleMeasure(String metricKey) {
    Resource resource = orchestrator.getServer().getWsClient().find(ResourceQuery.createForMetrics(MODULE_COMMONS_LANG, metricKey));
    return resource != null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getPackageMeasure(String metricKey) {
    Resource resource = orchestrator.getServer().getWsClient().find(ResourceQuery.createForMetrics(PACKAGE_COMMONS_LANG, metricKey));
    return resource != null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getFileMeasure(String metricKey) {
    Resource resource = orchestrator.getServer().getWsClient().find(ResourceQuery.createForMetrics(FILE_OBJECT_UTILS, metricKey));
    return resource != null ? resource.getMeasure(metricKey) : null;
  }

}
