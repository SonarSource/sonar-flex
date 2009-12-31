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

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.in.SMInputCursor;
import org.sonar.api.batch.AbstractViolationsStaxParser;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.RulesManager;
import org.sonar.plugins.flex.FlexPlugin;
import org.sonar.plugins.flex.FlexFile;

import javax.xml.stream.XMLStreamException;

class FlexPmdViolationsXmlParser extends AbstractViolationsStaxParser {

  private Project project;

  FlexPmdViolationsXmlParser(Project project, SensorContext context, RulesManager rulesManager, RulesProfile profile) {
    super(context, rulesManager, profile);
    this.project = project;
  }

  @Override
  protected String keyForPlugin() {
    return FlexPlugin.FLEXPMD_PLUGIN;
  }

  @Override
  protected SMInputCursor cursorForResources(SMInputCursor rootCursor) throws XMLStreamException {
    return rootCursor.descendantElementCursor("file");
  }

  @Override
  protected SMInputCursor cursorForViolations(SMInputCursor resourcesCursor) throws XMLStreamException {
    return resourcesCursor.descendantElementCursor("violation");
  }

  @Override
  protected String lineNumberForViolation(SMInputCursor violationCursor) throws XMLStreamException {
    return violationCursor.getAttrValue("beginline");
  }

  @Override
  protected String messageFor(SMInputCursor violationCursor) throws XMLStreamException {
    return StringUtils.trim(violationCursor.collectDescendantText());
  }

  @Override
  protected String ruleKey(SMInputCursor violationCursor) throws XMLStreamException {
    String ruleKey = "com." + violationCursor.getAttrValue("rule") + "Rule";
    return ruleKey;
  }

  @Override
  protected Resource toResource(SMInputCursor resourcesCursor) throws XMLStreamException {
    return FlexFile.fromAbsolutePath(resourcesCursor.getAttrValue("name"), project.getFileSystem().getSourceDirs(), false);
  }
}
