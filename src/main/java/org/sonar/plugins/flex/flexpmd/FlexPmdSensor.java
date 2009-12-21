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

import org.sonar.api.CoreProperties;
import org.sonar.api.batch.AbstractViolationsStaxParser;
import org.sonar.api.batch.GeneratesViolations;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.DependsUponMavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RulesManager;
import org.sonar.api.utils.XmlParserException;

import javax.xml.stream.XMLStreamException;
import java.io.File;

public class FlexPmdSensor implements Sensor, DependsUponMavenPlugin, GeneratesViolations {

  private RulesProfile profile;
  private RulesManager rulesManager;
  private FlexPmdMavenPluginHandler pluginHandler;

  public FlexPmdSensor(RulesProfile profile, RulesManager rulesManager, FlexPmdMavenPluginHandler pluginHandler) {
    this.profile = profile;
    this.rulesManager = rulesManager;
    this.pluginHandler = pluginHandler;
  }

  public void analyse(Project project, SensorContext context) {
    try {
      AbstractViolationsStaxParser parser = getStaxParser(project, context);
      File report = new File(project.getFileSystem().getBuildDir(), "flexpmd.xml");
      parser.parse(report);

    } catch (XMLStreamException e) {
      throw new XmlParserException(e);
    }
  }

  public boolean shouldExecuteOnProject(Project project) {
    return project.getFileSystem().hasJavaSourceFiles() &&
        (!profile.getActiveRulesByPlugin(CoreProperties.PMD_PLUGIN).isEmpty() || project.getReuseExistingRulesConfig());
  }

  public MavenPluginHandler getMavenPluginHandler(Project project) {
    return pluginHandler;
  }

  private AbstractViolationsStaxParser getStaxParser(Project project, SensorContext context) {
    return new PmdViolationsXmlParser(project, context, rulesManager, profile);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}