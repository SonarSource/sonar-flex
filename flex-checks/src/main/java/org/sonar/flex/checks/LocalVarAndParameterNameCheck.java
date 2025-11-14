/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Variable;

@Rule(key = "S117")
public class LocalVarAndParameterNameCheck extends FlexCheck {


  private static final String DEFAULT = "^[_a-z][a-zA-Z0-9]*$";
  private static final String MESSAGE = "Rename this local variable \"{0}\" to match the regular expression {1}";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the names against.",
    defaultValue = DEFAULT)
  String format = DEFAULT;


  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.FUNCTION_DEF);
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

        checkVariableDeclStatement(variableDeclStatement);
      }
    }
  }

  private void checkVariableDeclStatement(AstNode variableDeclStatement) {
    for (AstNode identifier : Variable.getDeclaredIdentifiers(variableDeclStatement)) {
      String varName = identifier.getTokenValue();

      if (!pattern.matcher(varName).matches()) {
        addIssue(MessageFormat.format(MESSAGE, varName, format), identifier);
      }
    }
  }

  private void checkFunctionParametersName(AstNode functionDef) {
    for (AstNode paramIdentifier : Function.getParametersIdentifiers(functionDef)) {
      String paramName = paramIdentifier.getTokenValue();

      if (!pattern.matcher(paramName).matches()) {
        addIssue(MessageFormat.format(MESSAGE, paramName, format), paramIdentifier);
      }
    }
  }
}
