/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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

import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "OneStatementPerLine")
public class OneStatementPerLineCheck extends FlexCheck {

  private final Map<Integer, Integer> statementsPerLine = Maps.newHashMap();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(FlexGrammar.STATEMENT, FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }

  @Override
  public void visitFile(AstNode astNode) {
    statementsPerLine.clear();
  }

  @Override
  public void visitNode(AstNode statementNode) {
    if (!isExcluded(statementNode)) {
      int line = statementNode.getTokenLine();
      statementsPerLine.compute(line, (k, v) -> v == null ? 1 : (v + 1));
    }
  }

  @Override
  public void leaveFile(AstNode astNode) {
    for (Map.Entry<Integer, Integer> statementsAtLine : statementsPerLine.entrySet()) {
      if (statementsAtLine.getValue() > 1) {
        addIssueAtLine(
          MessageFormat.format("At most one statement is allowed per line, but {0} statements were found on this line.", statementsAtLine.getValue()),
          statementsAtLine.getKey());
      }
    }
  }

  public boolean isExcluded(AstNode astNode) {
    AstNode statementNode = astNode.getFirstChild();
    return statementNode.is(FlexGrammar.BLOCK)
      || statementNode.is(FlexGrammar.EMPTY_STATEMENT)
      || statementNode.is(FlexGrammar.LABELED_STATEMENT);
  }

}
