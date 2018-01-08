/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1447",
  name = "Constructor bodies should be as lightweight as possible",
  priority = Priority.MAJOR,
  tags = Tags.PERFORMANCE)
@ActivatedByDefault
@SqaleConstantRemediation("5min")
public class ConstructorNotLightweightCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode constructorDef = Clazz.getConstructor(astNode);

    if (constructorDef != null && containsBranch(constructorDef)) {
      getContext().createLineViolation(this, "Extract the content of this \"{0}\" constructor into a dedicated function",
        constructorDef, Function.getName(constructorDef));
    }
  }

  private static boolean containsBranch(AstNode constructorDef) {
    AstNode blockNode = constructorDef.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

    if (blockNode != null) {

      for (AstNode directive : blockNode.getFirstChild(FlexGrammar.DIRECTIVES).getChildren()) {
        if (isBranch(directive)) {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean isBranch(AstNode directive) {
    AstNode astNode = directive.getFirstChild();
    return astNode.is(FlexGrammar.STATEMENT)
      && astNode.getFirstChild().is(
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.FOR_STATEMENT);
  }

}
