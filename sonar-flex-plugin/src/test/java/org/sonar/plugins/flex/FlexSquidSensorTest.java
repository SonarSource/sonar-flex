/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Resource;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.core.FlexResourceBridge;
import org.sonar.test.TestUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collections;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class FlexSquidSensorTest {

  private FlexSquidSensor sensor;

  private ModuleFileSystem fs = mock(ModuleFileSystem.class);
  private ResourcePerspectives perspectives = mock(ResourcePerspectives.class);
  private final Project dummyProject = new Project("dummy");

  @Before
  public void setUp() {
    FlexResourceBridge resourceBridge = new FlexResourceBridge();
    FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
    FileLinesContext fileLinesContext = mock(FileLinesContext.class);
    when(fileLinesContextFactory.createFor(Mockito.any(Resource.class))).thenReturn(fileLinesContext);

    sensor = new FlexSquidSensor(mock(RulesProfile.class), resourceBridge, fileLinesContextFactory, fs, perspectives);

    when(fs.sourceCharset()).thenReturn(Charset.forName("UTF-8"));
  }

  @Test
  public void test_should_execute_on_project() {
    Project project = mock(Project.class);

    // Multi-language mode check
    when(project.getLanguageKey()).thenReturn(null);

    when(fs.files(any(FileQuery.class))).thenReturn(Collections.<File>emptyList());
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(fs.files(any(FileQuery.class))).thenReturn(Collections.singletonList(mock(File.class)));
    assertThat(sensor.shouldExecuteOnProject(project)).isTrue();

    // Compatibility 3.7
    when(project.getLanguageKey()).thenReturn("java");
    assertThat(sensor.shouldExecuteOnProject(project)).isFalse();

    when(project.getLanguageKey()).thenReturn(Flex.KEY);
    assertThat(sensor.shouldExecuteOnProject(project)).isTrue();
  }

  @Test
  public void should_analyse() {
    dummyProject.setFileSystem(mockProjectFileSystem("org/sonar/plugins/flex/duplications"));

    ImmutableList<File> files = ImmutableList.of(TestUtils.getResource("org/sonar/plugins/flex/duplications/SmallFile.as"));
    when(fs.files(any(FileQuery.class))).thenReturn(files);
    SensorContext context = mock(SensorContext.class);

    sensor.analyse(dummyProject, context);

    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMPLEXITY_IN_CLASSES), Mockito.eq(1.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.FILES), Mockito.eq(1.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.LINES), Mockito.eq(13.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.NCLOC), Mockito.eq(9.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMMENT_LINES), Mockito.eq(1.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.STATEMENTS), Mockito.eq(2.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.FUNCTIONS), Mockito.eq(2.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.CLASSES), Mockito.eq(1.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMPLEXITY), Mockito.eq(2.0));
  }

  @Test
  public void should_analyse2() {
    dummyProject.setFileSystem(mockProjectFileSystem("org/sonar/plugins/flex/squid/"));

    ImmutableList<File> files = ImmutableList.of(TestUtils.getResource("org/sonar/plugins/flex/squid/TimeFormatter.as"));
    when(fs.files(any(FileQuery.class))).thenReturn(files);
    SensorContext context = mock(SensorContext.class);

    sensor.analyse(dummyProject, context);

    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.FILES), Mockito.eq(1.0));
    // TODO Godin: why 103 ? I expected 102 - see SSLRSQBR-10
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMPLEXITY_IN_CLASSES), Mockito.eq(0.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.LINES), Mockito.eq(103.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.NCLOC), Mockito.eq(0.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMMENT_LINES), Mockito.eq(59.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.FUNCTIONS), Mockito.eq(0.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.CLASSES), Mockito.eq(0.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.STATEMENTS), Mockito.eq(0.0));
    verify(context).saveMeasure(Mockito.any(Resource.class), Mockito.eq(CoreMetrics.COMPLEXITY), Mockito.eq(0.0));
  }

  @Test
  public void should_compute_number_of_packages() {
    File baseDir = TestUtils.getResource("org/sonar/plugins/flex/squid/");
    File file1 = new File(baseDir, "package1.as");
    File file2 = new File(baseDir, "package2.as");
    File file3 = new File(baseDir, "package3.as");
    when(fs.files(any(FileQuery.class))).thenReturn(ImmutableList.of(file1, file2, file3));

    dummyProject.setFileSystem(mockProjectFileSystem("org/sonar/plugins/flex/squid/"));

    SensorContext context = mock(SensorContext.class);

    sensor.analyse(dummyProject, context);
    verify(context, times(2)).saveMeasure(Mockito.any(Directory.class), Mockito.eq(CoreMetrics.PACKAGES), Mockito.eq(1.0));
  }

  private static ProjectFileSystem mockProjectFileSystem(String srcDir) {
    ProjectFileSystem pfs = mock(ProjectFileSystem.class);
    when(pfs.getSourceDirs()).thenReturn(ImmutableList.of(TestUtils.getResource(srcDir)));
    return pfs;
  }
}
