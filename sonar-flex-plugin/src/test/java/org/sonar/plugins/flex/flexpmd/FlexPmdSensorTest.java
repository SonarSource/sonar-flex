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

package org.sonar.plugins.flex.flexpmd;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FlexPmdSensorTest {

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();

  private RuleFinder ruleFinder;
  private RulesProfile profile;
  private FlexPmdSensor sensor;
  private FlexPmdProfileExporter profileExporter;

  @Before
  public void setUp() {
    ruleFinder = mock(RuleFinder.class);
    profile = mock(RulesProfile.class);
    profileExporter = mock(FlexPmdProfileExporter.class);
    sensor = new FlexPmdSensor(ruleFinder, profileExporter, profile);
  }

  @Test
  public void shouldExecuteOnProject() {
    Project project = mock(Project.class);
    when(project.getLanguageKey()).thenReturn(Flex.KEY);
    when(profile.getActiveRulesByRepository(FlexPmdRuleRepository.REPOSITORY_KEY))
        .thenReturn(Arrays.asList(new ActiveRule()));
    assertThat(sensor.shouldExecuteOnProject(project), is(true));
  }

  @Test
  public void shouldNotExecuteWhenNoActiveRules() {
    Project project = mock(Project.class);
    when(project.getLanguageKey()).thenReturn(Flex.KEY);
    when(profile.getActiveRulesByRepository(FlexPmdRuleRepository.REPOSITORY_KEY))
        .thenReturn(Collections.EMPTY_LIST);
    assertThat(sensor.shouldExecuteOnProject(project), is(false));
  }

  @Test
  public void shouldNotExecuteOnJavaProject() {
    Project project = mock(Project.class);
    when(project.getLanguageKey()).thenReturn("java");
    assertThat(sensor.shouldExecuteOnProject(project), is(false));
  }

  @Test
  public void shouldExecuteFlexPMD() throws Exception {
    File workDir = tempFolder.newFolder();
    ProjectFileSystem fs = mock(ProjectFileSystem.class);
    when(fs.getSonarWorkingDirectory()).thenReturn(workDir);
    when(fs.getSourceDirs()).thenReturn(Arrays.asList(new File("src/test/resources/org/sonar/plugins/flex/flexpmd/mxml/")));

    File rulesFile = tempFolder.newFile("rules.xml");
    FileUtils.write(rulesFile, "<ruleset>" +
        "<rule class=\"com.adobe.ac.pmd.rules.style.CopyrightMissingRule\" message=\"The copyright header is missing in this file\">" +
        "</rule>" +
        "</ruleset>");
    when(fs.writeToWorkingDirectory(Mockito.anyString(), Mockito.anyString())).thenReturn(rulesFile);

    Project project = mock(Project.class);
    when(project.getFileSystem()).thenReturn(fs);

    File resultFile = sensor.execute(project);

    assertThat(resultFile.exists(), is(true));
    String result = FileUtils.readFileToString(resultFile);
    assertThat(result, containsString("Example.mxml"));
  }

}
