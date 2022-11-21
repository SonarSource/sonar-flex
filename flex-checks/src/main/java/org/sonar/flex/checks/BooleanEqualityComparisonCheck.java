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
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

@Rule(key = "S1125")
public class BooleanEqualityComparisonCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.EQUALITY_EXPR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (hasBooleanLiteralOperand(astNode.getFirstChild(FlexGrammar.EQUALITY_OPERATOR).getNextAstNode())) {
      addIssue("Remove the unnecessary boolean comparison to simplify this expression.", astNode);
    }
  }

  private static boolean hasBooleanLiteralOperand(AstNode astNode) {
    return astNode.is(FlexGrammar.POSTFIX_EXPR)
      && astNode.getFirstChild().is(FlexGrammar.PRIMARY_EXPR)
      && astNode.getFirstChild().getFirstChild().is(FlexKeyword.TRUE, FlexKeyword.FALSE);
  }
}
