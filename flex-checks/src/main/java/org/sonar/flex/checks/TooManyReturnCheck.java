/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
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
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "S1142")
public class TooManyReturnCheck extends FlexCheck {

  private static final int DEFAULT = 3;
  private final Deque<Integer> returnStatementCounter = new ArrayDeque<>();

  @RuleProperty(
    key = "max",
    description = "Maximum allowed return statements per function",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(FlexGrammar.FUNCTION_COMMON, FlexGrammar.RETURN_STATEMENT);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    returnStatementCounter.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.RETURN_STATEMENT)) {
      setReturnStatementCounter(getReturnStatementCounter() + 1);
    } else {
      returnStatementCounter.push(0);
    }

  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.FUNCTION_COMMON)) {
      if (getReturnStatementCounter() > max) {
        addIssue(
          MessageFormat.format(
            "Reduce the number of returns of this function {0,number,integer}, down to the maximum allowed {1,number,integer}.",
            getReturnStatementCounter(),
            max),
          astNode);
      }
      returnStatementCounter.pop();
    }

  }

  private int getReturnStatementCounter() {
    return returnStatementCounter.peek();
  }

  private void setReturnStatementCounter(int value) {
    returnStatementCounter.pop();
    returnStatementCounter.push(value);
  }
}
