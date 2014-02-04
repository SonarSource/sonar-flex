/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.ArrayDeque;
import java.util.Deque;

@Rule(
  key = "S1142",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class TooManyReturnCheck extends SquidCheck<LexerlessGrammar> {

  private static final int DEFAULT = 3;
  private final Deque<Integer> returnStatementCounter = new ArrayDeque<Integer>();

  @RuleProperty(
    key = "max",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public void init() {
    subscribeTo(FlexGrammar.FUNCTION_COMMON, FlexGrammar.RETURN_STATEMENT);
  }

  @Override
  public void visitFile(AstNode astNode) {
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
        getContext().createLineViolation(this, "Reduce the number of returns of this function {0,number,integer}, down to the maximum allowed {1,number,integer}.",
          astNode, getReturnStatementCounter(), max);
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
