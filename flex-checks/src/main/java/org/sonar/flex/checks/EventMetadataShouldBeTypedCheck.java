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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.MetadataTag;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Map;

@Rule(
  key = "S1463",
  name = "Event types should be defined in metadata tags",
  tags = Tags.DESIGN,

  priority = Priority.MAJOR)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("5min")
public class EventMetadataShouldBeTypedCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.METADATA_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (MetadataTag.isTag(astNode, "Event")) {
      Map<String, String> properties = MetadataTag.getTagPropertiesMap(astNode);

      if (properties != null && !properties.containsKey("type")) {
        getContext().createLineViolation(this, "The {0} event type is missing in this metadata tag", astNode, properties.get("name"));
      }
    }

  }
}
