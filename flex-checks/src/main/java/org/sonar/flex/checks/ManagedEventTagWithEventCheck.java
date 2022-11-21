/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.MetadataTag;

@Rule(key = "S1464")
public class ManagedEventTagWithEventCheck extends FlexCheck {

  private Map<String, Boolean> isDeclaredInEventTag = new HashMap<>();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.METADATA_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!MetadataTag.isTag(astNode, "ManagedEvents")) {
      return;
    }

    initEventMap(MetadataTag.getSinglePropertyAsList(astNode));
    searchEventInPreviousTag(astNode);
    searchEventInNextTag(astNode);

    for (Map.Entry<String, Boolean> entry: isDeclaredInEventTag.entrySet()) {
      if (!entry.getValue()) {
        addIssue(MessageFormat.format("The managed event {0} is either misspelled or is missing a companion Event metadata tag", entry.getKey()), astNode);
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
