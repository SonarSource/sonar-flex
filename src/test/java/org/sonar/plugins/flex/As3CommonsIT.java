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

package org.sonar.plugins.flex;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.ResourceQuery;
import org.sonar.wsclient.services.Measure;
import static junit.framework.Assert.assertNull;

public class As3CommonsIT {

  private static Sonar sonar;
  private static final String PROJECT_AS3COMMONS = "org.as3commons:as3commons-project";
  private static final String MODULE_COMMONS_LANG = "org.as3commons:as3commons-lang";
  private static final String PACKAGE_COMMONS_LANG = "org.as3commons:as3commons-lang:org.as3commons.lang";
  private static final String FILE_OBJECT_UTILS = "org.as3commons:as3commons-lang:org.as3commons.lang.ObjectUtils";

  @BeforeClass
  public static void buildServer() {
    sonar = Sonar.create("http://localhost:9000");
  }

  @Test
  public void commonsCollectionsIsAnalyzed() {
    assertThat(sonar.find(new ResourceQuery(PROJECT_AS3COMMONS)).getName(), is("AS3Commons Project"));
    assertThat(sonar.find(new ResourceQuery(PROJECT_AS3COMMONS)).getVersion(), is("1.0.0"));
    assertThat(sonar.find(new ResourceQuery(MODULE_COMMONS_LANG)).getName(), is("AS3Commons Lang"));
    assertThat(sonar.find(new ResourceQuery(PACKAGE_COMMONS_LANG)).getName(), is("org.as3commons.lang"));
    assertThat(sonar.find(new ResourceQuery(FILE_OBJECT_UTILS)).getName(), is("ObjectUtils"));
  }

  @Test
  public void projectsMetrics() {
    assertThat(getProjectMeasure("ncloc").getIntValue(), is(1368));
    assertThat(getProjectMeasure("lines").getIntValue(), is(9603));
    assertThat(getProjectMeasure("files").getIntValue(), is(69));
    assertThat(getProjectMeasure("classes").getIntValue(), is(69));
    assertThat(getProjectMeasure("packages").getIntValue(), is(15));
    assertThat(getProjectMeasure("functions").getIntValue(), is(340));
    assertThat(getProjectMeasure("comment_lines_density").getValue(), is(52.0));
    assertThat(getProjectMeasure("comment_lines").getIntValue(), is(1481));

    // CDP does not give same results where run with JDK 5 or 6
    assertThat(getProjectMeasure("duplicated_lines").getIntValue(), anyOf(is(416), is(422)));
    assertThat(getProjectMeasure("duplicated_blocks").getIntValue(), anyOf(is(70), is(72)));
    assertThat(getProjectMeasure("duplicated_lines_density").getValue(), anyOf(is(4.3), is(4.4)));

    assertThat(getProjectMeasure("duplicated_files").getIntValue(), is(15));
    assertThat(getProjectMeasure("complexity").getIntValue(), is(538));
    assertThat(getProjectMeasure("function_complexity").getValue(), is(1.6));
    assertThat(getProjectMeasure("class_complexity").getValue(), is(7.8));
    assertThat(getProjectMeasure("violations").getIntValue(), is(202));
    assertThat(getProjectMeasure("violations_density").getValue(), is(50.4));
    assertThat(getProjectMeasure("class_complexity_distribution").getData(), is("0=30;5=14;10=16;20=1;30=2;60=1;90=0"));
    assertThat(getProjectMeasure("function_complexity_distribution").getData(), is("1=222;2=54;4=27;6=4;8=0;10=0;12=3"));
  }

  @Test
  public void moduleMetrics() {
    assertThat(getModuleMeasure("ncloc").getIntValue(), is(375));
    assertThat(getModuleMeasure("lines").getIntValue(), is(4495));
    assertThat(getModuleMeasure("files").getIntValue(), is(19));
    assertThat(getModuleMeasure("classes").getIntValue(), is(19));
    assertThat(getModuleMeasure("packages").getIntValue(), is(2));
    assertThat(getModuleMeasure("functions").getIntValue(), is(81));
    assertThat(getModuleMeasure("comment_lines_density").getValue(), is(56.7));
    assertThat(getModuleMeasure("comment_lines").getIntValue(), is(491));

    // CDP does not give same results where run with JDK 5 or 6
    assertThat(getModuleMeasure("duplicated_lines").getIntValue(), anyOf(is(242), is(248)));
    assertThat(getModuleMeasure("duplicated_blocks").getIntValue(), anyOf(is(40), is(42)));
    assertThat(getModuleMeasure("duplicated_lines_density").getValue(), anyOf(is(5.4), is(5.5)));

    assertThat(getModuleMeasure("duplicated_files").getIntValue(), is(3));
    assertThat(getModuleMeasure("complexity").getIntValue(), is(179));
    assertThat(getModuleMeasure("function_complexity").getValue(), is(2.2));
    assertThat(getModuleMeasure("class_complexity").getValue(), is(9.4));
    assertThat(getModuleMeasure("violations").getIntValue(), is(93));
    assertThat(getModuleMeasure("violations_density").getValue(), is(33.1));
    assertThat(getModuleMeasure("class_complexity_distribution").getData(), is("0=8;5=3;10=3;20=1;30=2;60=0;90=0"));
    assertThat(getModuleMeasure("function_complexity_distribution").getData(), is("1=39;2=21;4=17;6=0;8=0;10=0;12=1"));
  }

  @Test
  public void packagesMetrics() {
    assertThat(getPackageMeasure("ncloc").getIntValue(), is(278));
    assertThat(getPackageMeasure("lines").getIntValue(), is(4134));
    assertThat(getPackageMeasure("files").getIntValue(), is(16));
    assertThat(getPackageMeasure("classes").getIntValue(), is(16));
    assertThat(getPackageMeasure("packages").getIntValue(), is(1));
    assertThat(getPackageMeasure("functions").getIntValue(), is(63));
    assertThat(getPackageMeasure("comment_lines_density").getValue(), is(60.1));
    assertThat(getPackageMeasure("comment_lines").getIntValue(), is(419));

    // CDP does not give same results where run with JDK 5 or 6
    assertThat(getPackageMeasure("duplicated_lines").getIntValue(), anyOf(is(242), is(248)));
    assertThat(getPackageMeasure("duplicated_blocks").getIntValue(), anyOf(is(40), is(42)));
    assertThat(getPackageMeasure("duplicated_lines_density").getValue(),  anyOf(is(6.0), is(5.9)));

    assertThat(getPackageMeasure("duplicated_files").getIntValue(), is(3));
    assertThat(getPackageMeasure("complexity").getIntValue(), is(132));
    assertThat(getPackageMeasure("function_complexity").getValue(), is(2.1));

    // Should be 8.3, see http://jira.codehaus.org/browse/SONARPLUGINS-496
    assertThat(getPackageMeasure("class_complexity").getValue(), is(9.4));
    assertThat(getPackageMeasure("violations").getIntValue(), is(78));
    assertThat(getPackageMeasure("violations_density").getValue(), is(28.4));
    assertThat(getPackageMeasure("class_complexity_distribution").getData(), is("0=7;5=3;10=2;20=1;30=1;60=0;90=0"));
    assertThat(getPackageMeasure("function_complexity_distribution").getData(), is("1=30;2=16;4=14;6=0;8=0;10=0;12=0"));
  }

  @Test
  public void filesMetrics() {
    assertThat(getFileMeasure("ncloc").getIntValue(), is(54));
    assertThat(getFileMeasure("lines").getIntValue(), is(194));
    assertThat(getFileMeasure("files").getIntValue(), is(1));
    assertThat(getFileMeasure("classes").getIntValue(), is(1));
    assertNull(getFileMeasure("packages"));
    assertThat(getFileMeasure("functions").getIntValue(), is(10));
    assertThat(getFileMeasure("comment_lines_density").getValue(), is(54.6));
    assertThat(getFileMeasure("comment_lines").getIntValue(), is(65));
    assertNull(getFileMeasure("duplicated_lines"));
    assertNull(getFileMeasure("duplicated_blocks"));
    assertNull(getFileMeasure("duplicated_files"));
    assertNull(getFileMeasure("duplicated_lines_density"));
    assertThat(getFileMeasure("complexity").getIntValue(), is(17));
    assertThat(getFileMeasure("function_complexity").getValue(), is(1.7));
    assertThat(getFileMeasure("class_complexity").getValue(), is(17.0));
    assertThat(getFileMeasure("violations").getIntValue(), is(15));
    assertThat(getFileMeasure("violations_density").getValue(), is(3.7));
    assertNull(getFileMeasure("class_complexity_distribution"));
    assertNull(getFileMeasure("function_complexity_distribution"));
  }

  private Measure getProjectMeasure(String metricKey) {
    return sonar.find(ResourceQuery.createForMetrics(PROJECT_AS3COMMONS, metricKey)).getMeasure(metricKey);
  }

  private Measure getModuleMeasure(String metricKey) {
    return sonar.find(ResourceQuery.createForMetrics(MODULE_COMMONS_LANG, metricKey)).getMeasure(metricKey);
  }

  private Measure getPackageMeasure(String metricKey) {
    return sonar.find(ResourceQuery.createForMetrics(PACKAGE_COMMONS_LANG, metricKey)).getMeasure(metricKey);
  }

  private Measure getFileMeasure(String metricKey) {
    return sonar.find(ResourceQuery.createForMetrics(FILE_OBJECT_UTILS, metricKey)).getMeasure(metricKey);
  }
}