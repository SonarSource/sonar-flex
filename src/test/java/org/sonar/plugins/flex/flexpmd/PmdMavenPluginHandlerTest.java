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
package org.sonar.plugins.flex.flexpmd;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.resources.ProjectFileSystem;
import org.sonar.api.resources.Project;
import org.sonar.api.test.MavenTestUtils;

public class PmdMavenPluginHandlerTest {
  
  private Project project;
  private ProjectFileSystem fs;
  private File fakeSonarConfig;
  private FlexPmdRulesRepository repo;
  private MavenPlugin plugin;
  private FlexPmdMavenPluginHandler handler;
  
  @Before
  public void setup() {
    project = mock(Project.class);
    fs = mock(ProjectFileSystem.class);
    fakeSonarConfig = mock(File.class);
    repo = mock(FlexPmdRulesRepository.class);
    plugin = mock(MavenPlugin.class);
    handler = new FlexPmdMavenPluginHandler();
  }
  
  @Test
  public void doReuseExistingRulesConfig() throws Exception {
    setupConfig();  
    // See sonar 583
    when(project.getReuseExistingRulesConfig()).thenReturn(true);
    when(plugin.getParameter("configLocation")).thenReturn("existingConfig.xml");
    
    //handler.configureRuleSet(project, plugin);
    verify(plugin, never()).setParameter(eq("configLocation"), anyString());
    verify(plugin, never()).setParameter(eq("rulesets/ruleset"), anyString());
  }
  
  private void setupConfig() throws IOException {
    when(fakeSonarConfig.getCanonicalPath()).thenReturn("fakeSonarConfig.xml");
    when(project.getFileSystem()).thenReturn(fs);
    when(fs.writeToWorkingDirectory(anyString(), anyString())).thenReturn(fakeSonarConfig);
  }
  
  @Ignore
  public void configureExclusions() throws IOException {

    when(project.getExclusionPatterns()).thenReturn(new String[]{"**/*", "foo", "*/bar", "**/MyClass", "**/MyOtherClass.java"});

    //handler.configureExclusions(project, plugin);

    verify(plugin).addParameter("excludes/exclude", "**/*");
    verify(plugin).addParameter("excludes/exclude", "foo");
    verify(plugin).addParameter("excludes/exclude", "*/bar");
    verify(plugin).addParameter("excludes/exclude", "**/MyClass");
    verify(plugin).addParameter("excludes/exclude", "**/MyOtherClass.java");
  }
}
