/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
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

package org.sonar.plugins.flex.surefire;

import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;

/**
 * @author Evgeny Mandrikov
 */
public class FlexMojosMavenPluginHandler implements MavenPluginHandler {

  public static final String GROUP_ID = "org.sonatype.flexmojos";
  public static final String ARTIFACT_ID = "flexmojos-maven-plugin";
  public static final String VERSION = "3.6.1";

  public String getGroupId() {
    return GROUP_ID;
  }

  public String getArtifactId() {
    return ARTIFACT_ID;
  }

  public String getVersion() {
    return VERSION;
  }

  public boolean isFixedVersion() {
    return false;
  }

  public String[] getGoals() {
    return new String[]{"test-run"};
  }

  public void configure(Project project, MavenPlugin plugin) {
    MavenPlugin flexmojos = MavenPlugin.registerPlugin(project.getPom(), GROUP_ID, ARTIFACT_ID, VERSION, false);
    flexmojos.setParameter("testFailureIgnore", "true");
  }
  
}
