/*
 * SonarQube Flex Plugin
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

import com.adobe.ac.pmd.FlexPmdParameters;
import com.adobe.ac.pmd.FlexPmdViolations;
import com.adobe.ac.pmd.engines.FlexPmdXmlEngine;
import com.google.common.annotations.VisibleForTesting;
import net.sourceforge.pmd.PMDException;
import org.apache.commons.io.FileUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.SonarException;
import org.sonar.plugins.flex.core.Flex;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class FlexPmdSensor implements Sensor {

  private final RuleFinder ruleFinder;
  private final FlexPmdProfileExporter profileExporter;
  private final RulesProfile rulesProfile;

  public FlexPmdSensor(RuleFinder ruleFinder, FlexPmdProfileExporter profileExporter, RulesProfile rulesProfile) {
    this.ruleFinder = ruleFinder;
    this.profileExporter = profileExporter;
    this.rulesProfile = rulesProfile;
  }

  public boolean shouldExecuteOnProject(Project project) {
    return Flex.KEY.equals(project.getLanguageKey())
        && !rulesProfile.getActiveRulesByRepository(FlexPmdRuleRepository.REPOSITORY_KEY).isEmpty();
  }

  public void analyse(Project project, SensorContext context) {
    FlexPmdXmlReportParser parser = getStaxParser(project, context);
    File report;
    try {
      report = execute(project);
    } catch (Exception e) {
      throw new SonarException(e);
    }
    parser.parse(report);
  }

  private FlexPmdXmlReportParser getStaxParser(Project project, SensorContext context) {
    return new FlexPmdXmlReportParser(project, context, ruleFinder);
  }

  /**
   * @return file with generated report
   */
  @VisibleForTesting
  File execute(Project project) throws IOException, PMDException, URISyntaxException {
    File rules = saveConfigXml(project);
    File workDir = new File(project.getFileSystem().getSonarWorkingDirectory(), "flexpmd");
    prepareWorkDir(workDir);
    List<File> sourceDirs = project.getFileSystem().getSourceDirs();
    FlexPmdParameters parameters = new FlexPmdParameters("", workDir, rules, null, sourceDirs);
    FlexPmdViolations violations = new FlexPmdViolations();
    new FlexPmdXmlEngine(parameters).executeReport(violations);

    return new File(workDir, "pmd.xml");
  }

  private static void prepareWorkDir(File dir) {
    try {
      FileUtils.forceMkdir(dir);
      // directory is cleaned, because Sonar 3.0 will not do this for us
      FileUtils.cleanDirectory(dir);
    } catch (IOException e) {
      throw new SonarException("Cannot create directory: " + dir, e);
    }
  }

  private File saveConfigXml(Project project) throws IOException {
    return project.getFileSystem().writeToWorkingDirectory(profileExporter.exportProfileToString(rulesProfile), "pmd.xml");
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
