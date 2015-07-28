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
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.List;
import java.util.regex.Pattern;

@Rule(
  key = "S117",
  name = "Local variable and function parameter names should comply with a naming convention",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("2min")
public class LocalVarAndParameterNameCheck extends SquidCheck<LexerlessGrammar> {


  private static final String DEFAULT = "^[_a-z][a-zA-Z0-9]*$";
  private static final String MESSAGE = "Rename this local variable \"{0}\" to match the regular expression {1}";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the names against.",
    defaultValue = DEFAULT)
  String format = DEFAULT;


  @Override
  public void init() {
    subscribeTo(FlexGrammar.FUNCTION_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    checkFunctionParametersName(astNode);

    if (astNode.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK) != null) {
      checkLocalVariableName(astNode.getFirstChild(FlexGrammar.FUNCTION_COMMON)
        .getFirstChild(FlexGrammar.BLOCK)
        .getFirstChild(FlexGrammar.DIRECTIVES)
        .getChildren(FlexGrammar.DIRECTIVE));
    }
  }

  private void checkLocalVariableName(List<AstNode> functionDirectives) {
    for (AstNode directive : functionDirectives) {

      if (Variable.isVariable(directive)) {
        AstNode variableDeclStatement = directive
          .getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)
          .getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);

        for (AstNode identifier : Variable.getDeclaredIdentifiers(variableDeclStatement)) {
          String varName = identifier.getTokenValue();

          if (!pattern.matcher(varName).matches()) {
            getContext().createLineViolation(this, MESSAGE, identifier, varName, format);
          }
        }
      }
    }
  }

  private void checkFunctionParametersName(AstNode functionDef) {
    for (AstNode paramIdentifier : Function.getParametersIdentifiers(functionDef)) {
      String paramName = paramIdentifier.getTokenValue();

      if (!pattern.matcher(paramName).matches()) {
        getContext().createLineViolation(this, MESSAGE, paramIdentifier, paramName, format);
      }
    }
  }
}
