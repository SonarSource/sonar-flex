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

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.Project;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RuleQuery;
import org.sonar.api.rules.Violation;
import org.sonar.api.utils.StaxParser;
import org.sonar.plugins.flex.FlexFile;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.FlexUtils;

import java.io.File;

import javax.xml.stream.XMLStreamException;

public class FlexPmdXmlReportParser {

  private Project project;
  private SensorContext context;
  private RuleFinder ruleFinder;

  public FlexPmdXmlReportParser(Project project, SensorContext context, RuleFinder ruleFinder) {
    this.project = project;
    this.context = context;
    this.ruleFinder = ruleFinder;
  }

  public void parse(File xmlFile) {
    StaxParser parser = new StaxParser(new StaxParser.XmlStreamHandler() {
      public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        rootCursor.advance();

        SMInputCursor fileCursor = rootCursor.descendantElementCursor("file");
        while (fileCursor.getNext() != null) {
          String fileName = fileCursor.getAttrValue("name");

          SMInputCursor violationCursor = fileCursor.descendantElementCursor("violation");
          while (violationCursor.getNext() != null) {
            String beginLine = violationCursor.getAttrValue("beginline");
            String ruleKey = "com." + violationCursor.getAttrValue("rule") + "Rule";
            String message = StringUtils.trim(violationCursor.collectDescendantText());

            log(context, ruleKey, fileName, Integer.parseInt(beginLine), message);
          }
        }
      }
    });

    try {
      parser.parse(xmlFile);
    } catch (XMLStreamException e) {
      FlexUtils.LOG.error("Error parsing file : {}", xmlFile);
    }
  }

  void log(SensorContext context, String ruleKey, String fileName, Integer line, String message) {
    RuleQuery ruleQuery = RuleQuery.create()
        .withRepositoryKey(FlexPlugin.FLEXPMD_PLUGIN)
        .withConfigKey(ruleKey);
    Rule rule = ruleFinder.find(ruleQuery);
    if (rule != null) {
      FlexFile resource = FlexFile.fromAbsolutePath(fileName, project.getFileSystem().getSourceDirs(), false);
      Violation violation = Violation.create(rule, resource).setLineId(line).setMessage(message);
      context.saveViolation(violation);
    }
  }
}
