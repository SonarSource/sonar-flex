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
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1434",
  name = "The \"Object\" type should not be used when creating a new variable",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.DATA_RELIABILITY)
@SqaleConstantRemediation("20min")
public class ObjectTypeUseCheck extends SquidCheck<LexerlessGrammar> {

  private static final String OBJECT_TYPE = "Object";

  @Override
  public void init() {
    subscribeTo(FlexGrammar.VARIABLE_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode varBinding : astNode.getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST).getChildren(FlexGrammar.VARIABLE_BINDING)) {
      if (isDeclareAsObject(varBinding) || isInitialisedAsObject(varBinding.getFirstChild(FlexGrammar.VARIABLE_INITIALISATION))) {

        getContext().createLineViolation(this, "Clearly define the type of this ''{0}'' variable", astNode,
          varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue());
      }
    }
  }

  private static boolean isDeclareAsObject(AstNode varBinding) {
    AstNode typeExpr = varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.TYPE_EXPR);
    return typeExpr != null && OBJECT_TYPE.equals(typeExpr.getTokenValue());
  }

  private static boolean isInitialisedAsObject(AstNode varInitialisation) {
    if (varInitialisation != null) {
      AstNode assignmentExpr = varInitialisation.getFirstChild(FlexGrammar.VARIABLE_INITIALISER).getFirstChild(FlexGrammar.ASSIGNMENT_EXPR);

      if (assignmentExpr != null && assignmentExpr.getNumberOfChildren() == 1 && assignmentExpr.getFirstChild().is(FlexGrammar.POSTFIX_EXPR)) {
        AstNode postfixExprChild = assignmentExpr.getFirstChild(FlexGrammar.POSTFIX_EXPR).getFirstChild();

        // Check for object initialiser, e.g {attr1:Type, attr2:Type}
        if (postfixExprChild.is(FlexGrammar.PRIMARY_EXPR)) {
          return postfixExprChild.getFirstChild().is(FlexGrammar.OBJECT_INITIALISER);

          // Check for instantiation of Object, e.g new Object()
        } else if (postfixExprChild.is(FlexGrammar.FULL_NEW_EXPR, FlexGrammar.SHORT_NEW_EXPR)) {
          AstNode subExpr = postfixExprChild.getFirstChild(FlexGrammar.FULL_NEW_SUB_EXPR, FlexGrammar.SHORT_NEW_SUB_EXPR);
          return subExpr != null && OBJECT_TYPE.equals(subExpr.getTokenValue());
        }
      }
    }
    return false;
  }
}
