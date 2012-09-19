/*
 * Sonar Flex Plugin
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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.api.FlexGrammar;

@Rule(
  key = "FunctionSinglePointOfExit",
  priority = Priority.MINOR)
public class FunctionSinglePointOfExitCheck extends SquidCheck<FlexGrammar> {

  private int returnStatements;

  @Override
  public void init() {
    subscribeTo(getContext().getGrammar().methodDefinition, getContext().getGrammar().returnStatement);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(getContext().getGrammar().methodDefinition)) {
      returnStatements = 0;
    } else if (node.is(getContext().getGrammar().returnStatement)) {
      returnStatements++;
    }
  }

  @Override
  public void leaveNode(AstNode node) {
    if (node.is(getContext().getGrammar().methodDefinition) && (returnStatements != 0) && (returnStatements > 1 || !hasReturnAtEnd(node))) {
      getContext().createLineViolation(this, "A function shall have a single point of exit at the end of the function.", node);
    }
  }

  private boolean hasReturnAtEnd(AstNode methodDefinition) {
    AstNode compoundStatement = methodDefinition.findFirstDirectChild(getContext().getGrammar().block);
    return compoundStatement.getChild(compoundStatement.getNumberOfChildren() - 2).getFirstChild().is(getContext().getGrammar().returnStatement);
  }

}
