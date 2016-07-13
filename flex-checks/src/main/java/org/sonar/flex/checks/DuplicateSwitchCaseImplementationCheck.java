/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Rule(
  key = "S1871",
  name = "Two branches in the same conditional structure should not have exactly the same implementation",
  priority = Priority.MAJOR,
  tags = {Tags.DESIGN, Tags.SUSPICIOUS})
@SqaleConstantRemediation("10min")
public class DuplicateSwitchCaseImplementationCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    Map<AstNode, List<Token>> cases = Maps.newLinkedHashMap();

    for (AstNode caseElement : astNode.getChildren(FlexGrammar.CASE_ELEMENT)) {
      AstNode caseLabel = caseElement.getLastChild(FlexGrammar.CASE_LABEL);
      List<Token> tokens = getCaseImplementationTokens(caseElement);

      if (!tokens.isEmpty()) {
        AstNode duplicatedCase = getDuplicatedCase(cases, tokens);

        if (duplicatedCase != null) {
          getContext().createLineViolation(this, "Either merge this case with the identical one on line \"{0}\" or change one of the implementations.",
            caseElement, duplicatedCase.getTokenLine());
        } else {
          cases.put(caseLabel, tokens);
        }
      }
    }
  }

  private static List<Token> getCaseImplementationTokens(AstNode caseElement) {
    List<Token> tokens = Lists.newArrayList();

    for (AstNode directive : caseElement.getChildren(FlexGrammar.DIRECTIVE)) {
      tokens.addAll(directive.getTokens());
    }
    return tokens;
  }

  /**
   * Compares case implementation to previous ones.
   *
   * @return duplicated case condition if there is, null otherwise.
   */
  @Nullable
  private static AstNode getDuplicatedCase(Map<AstNode, List<Token>> cases, List<Token> tokens) {
    for (Map.Entry<AstNode, List<Token>> entry : cases.entrySet()) {

      if (areTokenValuesIdentical(entry.getValue(), tokens)) {
        return entry.getKey();
      }
    }
    return null;
  }

  private static boolean areTokenValuesIdentical(List<Token> implementation1, List<Token> implementation2) {
    int nbToken = implementation1.size();

    if (implementation2.size() != nbToken) {
      return false;
    }

    for (int i = 0; i < nbToken; i++) {
      if (!implementation1.get(i).getValue().equals(implementation2.get(i).getValue())) {
        return false;
      }
    }

    return true;
  }

}
