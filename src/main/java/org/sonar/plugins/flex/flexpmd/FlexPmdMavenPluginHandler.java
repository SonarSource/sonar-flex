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

import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.io.IOException;

public class FlexPmdMavenPluginHandler implements MavenPluginHandler {
  private RulesProfile rulesProfile;
  private FlexPmdRulesRepository rulesRepository;

  public FlexPmdMavenPluginHandler(RulesProfile rulesProfile, FlexPmdRulesRepository rulesRepository) {
    this.rulesProfile = rulesProfile;
    this.rulesRepository = rulesRepository;
  }

  public String getGroupId() {
    return "com.adobe.ac";
  }

  public String getArtifactId() {
    return "flex-pmd-maven-plugin";
  }

  public String getVersion() {
    return "1.2";
  }

  public boolean isFixedVersion() {
    return true;
  }

  public String[] getGoals() {
    return new String[]{"check"};
  }

  public void configure(Project project, MavenPlugin plugin) {
    try {
      File configFile = saveConfigXml(project);
      plugin.setParameter("ruleSet", configFile.getCanonicalPath());
    } catch (IOException e) {
      throw new SonarException("fail to save the pmd XML configuration", e);
    }
  }

  private File saveConfigXml(Project project) throws IOException {
    String configuration = rulesRepository.exportConfiguration(rulesProfile);
    return project.getFileSystem().writeToWorkingDirectory(configuration, "pmd.xml");
  }
}
