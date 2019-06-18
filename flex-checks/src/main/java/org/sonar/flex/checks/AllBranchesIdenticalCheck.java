/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2019 SonarSource SA
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

import static java.util.Collections.singletonList;

@Rule(key = "S3923")
public class AllBranchesIdenticalCheck extends FlexCheck {

  private Set<AstNode> visitedIfStatements = new HashSet<>();

  @Override
  public void visitFile(@Nullable AstNode node) {
    visitedIfStatements.clear();
  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT);
  }

  @Override
  public void visitNode(AstNode node) {
    ConditionalStructure conditionalStructure;
    if (node.is(FlexGrammar.IF_STATEMENT)) {
      if (!visitedIfStatements.add(node)) {
        return;
      }
      conditionalStructure = ifStatement(node);
    } else {
      conditionalStructure = switchStatement(node);
    }
    if (conditionalStructure.allBranchesArePresent && conditionalStructure.areAllEquivalentBranches()) {
      addIssue("Remove this conditional structure or edit its code blocks so that they're not all the same.", node.getToken());
    }
  }

  private ConditionalStructure ifStatement(AstNode node) {
    List<List<AstNode>> branches = new ArrayList<>();
    boolean allBranchesArePresent = false;

    branches.add(singletonList(node.getFirstChild(FlexGrammar.SUB_STATEMENT)));
    AstNode currentIfStatement = node;

    while (currentIfStatement.hasDirectChildren(FlexKeyword.ELSE)) {
      AstNode elseStatement = currentIfStatement.getLastChild(FlexGrammar.SUB_STATEMENT).getFirstChild(FlexGrammar.STATEMENT);
      if (elseStatement != null && elseStatement.hasDirectChildren(FlexGrammar.IF_STATEMENT)) {
        currentIfStatement = elseStatement.getFirstChild(FlexGrammar.IF_STATEMENT);
        visitedIfStatements.add(currentIfStatement);
        branches.add(singletonList(currentIfStatement.getFirstChild(FlexGrammar.SUB_STATEMENT)));
      } else {
        branches.add(singletonList(currentIfStatement.getLastChild(FlexGrammar.SUB_STATEMENT)));
        allBranchesArePresent = true;
        break;
      }
    }

    return new ConditionalStructure(branches, allBranchesArePresent);
  }

  private ConditionalStructure switchStatement(AstNode node) {
    List<List<AstNode>> branches = new ArrayList<>();
    boolean allBranchesArePresent = false;

    for (AstNode caseElement : node.getChildren(FlexGrammar.CASE_ELEMENT)) {
      List<AstNode> directives = caseElement.getChildren(FlexGrammar.DIRECTIVE);
      if (!directives.isEmpty() && isBreakStatement(directives.get(directives.size() - 1).getFirstChild())) {
        directives = directives.subList(0, directives.size() - 1);
      }
      branches.add(directives);
      for (AstNode caseLabelNode : caseElement.getChildren(FlexGrammar.CASE_LABEL)) {
        if (caseLabelNode.hasDirectChildren(FlexKeyword.DEFAULT)) {
          allBranchesArePresent = true;
        }
      }
    }

    return new ConditionalStructure(branches, allBranchesArePresent);
  }

  private boolean isBreakStatement(AstNode node) {
    return node.is(FlexGrammar.STATEMENT) && node.hasDirectChildren(FlexGrammar.BREAK_STATEMENT);
  }

  private class ConditionalStructure {

    private final List<List<AstNode>> branches;
    private final boolean allBranchesArePresent;

    private ConditionalStructure(List<List<AstNode>> branches, boolean allBranchesArePresent) {
      this.branches = branches;
      this.allBranchesArePresent = allBranchesArePresent;
    }

    private boolean areAllEquivalentBranches() {
      List<AstNode> first = branches.get(0);
      return branches.stream().skip(1).allMatch(next -> SyntacticEquivalence.areEquivalent(first, next));
    }
  }
}
