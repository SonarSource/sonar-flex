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
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.MetadataTag;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(
  key = "S1464",
  name = "Each ManagedEvents metadata tag should have a companion Event metadata tag",
  tags = Tags.BUG,
  priority = Priority.BLOCKER)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.BLOCKER)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_RELIABILITY)
@SqaleConstantRemediation("5min")
public class ManagedEventTagWithEventCheck extends SquidCheck<LexerlessGrammar> {

  private Map<String, Boolean> isDeclaredInEventTag = new HashMap<String, Boolean>();

  @Override
  public void init() {
    subscribeTo(FlexGrammar.METADATA_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!MetadataTag.isTag(astNode, "ManagedEvents")) {
      return;
    }

    initEventMap(MetadataTag.getSinglePropertyAsList(astNode));
    searchEventInPreviousTag(astNode);
    searchEventInNextTag(astNode);

    for (String eventName : isDeclaredInEventTag.keySet()) {

      if (!isDeclaredInEventTag.get(eventName)) {
        getContext().createLineViolation(this, "The managed event {0} is either misspelled or is missing a companion Event metadata tag",
          astNode, eventName);
      }
    }

    isDeclaredInEventTag.clear();
  }

  private void initEventMap(List<String> managedEventNames) {
    for (String eventName : managedEventNames) {
      isDeclaredInEventTag.put("\"" + eventName + "\"", false);
    }
  }

  private void searchEventInPreviousTag(AstNode metadata) {
    AstNode previousDirective = metadata.getParent().getParent().getPreviousAstNode();

    while (previousDirective != null && MetadataTag.isMetadataTag(previousDirective)) {
      checkEventTag(previousDirective);
      previousDirective = previousDirective.getPreviousAstNode();
    }
  }

  private void searchEventInNextTag(AstNode metadata) {
    AstNode nextDirective = metadata.getParent().getParent().getNextAstNode();

    while (nextDirective != null && MetadataTag.isMetadataTag(nextDirective)) {
      checkEventTag(nextDirective);
      nextDirective = nextDirective.getNextAstNode();
    }
  }


  private void checkEventTag(AstNode directive) {
    AstNode metadataStmt = directive.getFirstChild().getFirstChild(FlexGrammar.METADATA_STATEMENT);

    if (MetadataTag.isTag(metadataStmt, "Event")) {
      String eventName = MetadataTag.getTagPropertiesMap(metadataStmt).get("name");


      if (isDeclaredInEventTag.containsKey(eventName)) {
        isDeclaredInEventTag.put(eventName, true);
      }
    }
  }
}
