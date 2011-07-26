/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
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

package org.sonar.plugins.flex;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
    assertThat(getProjectMeasure("ncloc").getIntValue(), is(1746));
    assertThat(getProjectMeasure("lines").getIntValue(), is(9603));
    assertThat(getProjectMeasure("files").getIntValue(), is(69));
    assertThat(getProjectMeasure("classes").getIntValue(), is(69));
    assertThat(getProjectMeasure("packages").getIntValue(), is(15));
    assertThat(getProjectMeasure("functions").getIntValue(), is(421));
    assertThat(getProjectMeasure("comment_lines_density").getValue(), is(62.2));
    assertThat(getProjectMeasure("comment_lines").getIntValue(), is(2870));

    assertThat(getProjectMeasure("duplicated_lines").getIntValue(), is(0));
    assertThat(getProjectMeasure("duplicated_blocks").getIntValue(), is(0));
    assertThat(getProjectMeasure("duplicated_lines_density").getValue(), is(0.0));
    assertThat(getProjectMeasure("duplicated_files").getIntValue(), is(0));

    assertThat(getProjectMeasure("complexity").getIntValue(), is(784));
    assertThat(getProjectMeasure("function_complexity").getValue(), is(1.9));
    assertThat(getProjectMeasure("class_complexity").getValue(), is(11.4));
    assertThat(getProjectMeasure("class_complexity_distribution").getData(), is("0=30;5=14;10=16;20=1;30=2;60=1;90=1"));
    assertThat(getProjectMeasure("function_complexity_distribution").getData(), is("1=252;2=83;4=42;6=4;8=5;10=0;12=5"));

    // SONARPLUGINS-670: Different number of violations reported on different OSs
    // 216 on Linux, 217 on Windows
    assertThat(getProjectMeasure("violations").getIntValue(), anyOf(is(217), is(216)));
    // 18.8 on Linux, 18.3 on Windows
    assertThat(getProjectMeasure("violations_density").getValue(), anyOf(is(18.8), is(18.3)));
  }

  @Test
  public void moduleMetrics() {
    assertThat(getModuleMeasure("ncloc").getIntValue(), is(753));
    assertThat(getModuleMeasure("lines").getIntValue(), is(4495));
    assertThat(getModuleMeasure("files").getIntValue(), is(19));
    assertThat(getModuleMeasure("classes").getIntValue(), is(19));
    assertThat(getModuleMeasure("packages").getIntValue(), is(2));
    assertThat(getModuleMeasure("functions").getIntValue(), is(162));
    assertThat(getModuleMeasure("comment_lines_density").getValue(), is(71.6));
    assertThat(getModuleMeasure("comment_lines").getIntValue(), is(1898));

    assertThat(getModuleMeasure("duplicated_lines").getIntValue(), is(0));
    assertThat(getModuleMeasure("duplicated_blocks").getIntValue(), is(0));
    assertThat(getModuleMeasure("duplicated_lines_density").getValue(), is(0.0));
    assertThat(getModuleMeasure("duplicated_files").getIntValue(), is(0));

    assertThat(getModuleMeasure("complexity").getIntValue(), is(425));
    assertThat(getModuleMeasure("function_complexity").getValue(), is(2.6));
    assertThat(getModuleMeasure("class_complexity").getValue(), is(22.4));
    assertThat(getModuleMeasure("violations").getIntValue(), is(114));
    assertThat(getModuleMeasure("violations_density").getValue(), is(10.9));
    assertThat(getModuleMeasure("class_complexity_distribution").getData(), is("0=8;5=3;10=3;20=1;30=2;60=0;90=1"));
    assertThat(getModuleMeasure("function_complexity_distribution").getData(), is("1=69;2=50;4=32;6=0;8=5;10=0;12=3"));
  }

  @Test
  public void packagesMetrics() {
    assertThat(getPackageMeasure("ncloc").getIntValue(), is(656));
    assertThat(getPackageMeasure("lines").getIntValue(), is(4134));
    assertThat(getPackageMeasure("files").getIntValue(), is(16));
    assertThat(getPackageMeasure("classes").getIntValue(), is(16));
    assertThat(getPackageMeasure("packages").getIntValue(), is(1));
    assertThat(getPackageMeasure("functions").getIntValue(), is(144));
    assertThat(getPackageMeasure("comment_lines_density").getValue(), is(73.6));
    assertThat(getPackageMeasure("comment_lines").getIntValue(), is(1826));

    assertThat(getPackageMeasure("duplicated_lines").getIntValue(), is(0));
    assertThat(getPackageMeasure("duplicated_blocks").getIntValue(), is(0));
    assertThat(getPackageMeasure("duplicated_lines_density").getValue(), is(0.0));
    assertThat(getPackageMeasure("duplicated_files").getIntValue(), is(0));

    assertThat(getPackageMeasure("complexity").getIntValue(), is(378));
    assertThat(getPackageMeasure("function_complexity").getValue(), is(2.6));

    // We will get class_complexity=23.6 under Sonar 2.0.1
    assertThat(getPackageMeasure("class_complexity").getValue(), is(25.2));
    assertThat(getPackageMeasure("violations").getIntValue(), is(93));
    assertThat(getPackageMeasure("violations_density").getValue(), is(21.2));
    assertThat(getPackageMeasure("class_complexity_distribution").getData(), is("0=7;5=3;10=2;20=1;30=1;60=0;90=1"));
    assertThat(getPackageMeasure("function_complexity_distribution").getData(), is("1=60;2=45;4=29;6=0;8=5;10=0;12=2"));
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
    assertThat(getFileMeasure("violations").getIntValue(), is(24));
    assertThat(getFileMeasure("violations_density").getValue(), is(0.0));
    assertNull(getFileMeasure("class_complexity_distribution"));
    assertNull(getFileMeasure("function_complexity_distribution"));
  }

  private Measure getProjectMeasure(String metricKey) {
    Resource resource = sonar.find(ResourceQuery.createForMetrics(PROJECT_AS3COMMONS, metricKey));
    return resource!=null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getModuleMeasure(String metricKey) {
    Resource resource = sonar.find(ResourceQuery.createForMetrics(MODULE_COMMONS_LANG, metricKey));
    return resource!=null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getPackageMeasure(String metricKey) {
    Resource resource = sonar.find(ResourceQuery.createForMetrics(PACKAGE_COMMONS_LANG, metricKey));
    return resource!=null ? resource.getMeasure(metricKey) : null;
  }

  private Measure getFileMeasure(String metricKey) {
    Resource resource = sonar.find(ResourceQuery.createForMetrics(FILE_OBJECT_UTILS, metricKey));
    return resource!=null ? resource.getMeasure(metricKey) : null;
  }
}
