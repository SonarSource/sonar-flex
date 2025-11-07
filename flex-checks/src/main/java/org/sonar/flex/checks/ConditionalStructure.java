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
import com.sonar.sslr.api.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

import static java.util.Collections.singletonList;

class ConditionalStructure {
  static class BranchAndContent {
    AstNode branch;
    List<AstNode> content;
    boolean oneLiner;
    BranchAndContent(AstNode branch, List<AstNode> content, boolean oneLiner) {
      this.branch = branch;
      this.content = content;
      this.oneLiner = oneLiner;
    }
  }
  static BranchAndContent branchAndContentIf(AstNode branch, AstNode subStatement) {
    return new BranchAndContent(branch, singletonList(subStatement), isOnelinerSubStatement(subStatement));
  }
  static BranchAndContent branchAndContentSwitch(AstNode branch, List<AstNode> directives) {
    return new BranchAndContent(branch, directives, isOnelinerDirectives(directives));
  }

  final List<BranchAndContent> branches;
  final boolean allBranchesArePresent;
  final AstNode node;

  ConditionalStructure(AstNode node, List<BranchAndContent> branches, boolean allBranchesArePresent) {
    this.node = node;
    this.branches = branches;
    this.allBranchesArePresent = allBranchesArePresent;
  }

  boolean areAllEquivalentBranches() {
    if (branches.isEmpty()) {
      return false;
    }
    BranchAndContent first = branches.get(0);
    return branches.stream().skip(1).allMatch(next -> SyntacticEquivalence.areEquivalent(first.content, next.content));
  }

  @FunctionalInterface
  interface DuplicatedBranchCallback extends BiConsumer<AstNode,AstNode> {
    void accept(AstNode branchFirstNode1, AstNode branchFirstNode2);
  }

  void forEachBranchDuplication(DuplicatedBranchCallback callback) {
    boolean allEquivalentBranches = areAllEquivalentBranches();
    if (allBranchesArePresent && allEquivalentBranches) {
      return;
    }

    for (BranchAndContent branch1 : branches) {
      if (!branch1.oneLiner || allEquivalentBranches) {
        for (BranchAndContent branch2 : branches) {
          if (branch1 == branch2) {
            break;
          }
          if (SyntacticEquivalence.areEquivalent(branch1.content, branch2.content)) {
            callback.accept(branch1.branch, branch2.branch);
          }
        }
      }
    }
  }

  static boolean isOnelinerNonBlock(AstNode nonBlock) {
    List<Token> tokens = nonBlock.getTokens();
    if (!tokens.isEmpty()) {
      return tokens.get(0).isOnSameLineThan(tokens.get(tokens.size() - 1));
    }
    return false;
  }

  static boolean isOnelinerSubStatement(AstNode node) {
    AstNode child;
    AstNode grandchild;
    if ((child = node.getFirstChild(FlexGrammar.STATEMENT)) != null && (grandchild = child.getFirstChild(FlexGrammar.BLOCK)) != null) {
      return isOnelinerDirectives(grandchild.getFirstChild(FlexGrammar.DIRECTIVES).getChildren(FlexGrammar.DIRECTIVE));
    }
    return isOnelinerNonBlock(node);
  }

  static boolean isOnelinerDirectives(List<AstNode> directives) {
    if (directives.isEmpty()) {
      return true;
    }
    Token firstToken = directives.get(0).getTokens().get(0);
    Token lastToken = directives.get(directives.size() - 1).getLastToken();
    return firstToken.isOnSameLineThan(lastToken);
  }

  static ConditionalStructure ifStatement(AstNode node, Set<AstNode> visitedIfStatements) {
    List<BranchAndContent> branches = new ArrayList<>();
    boolean allBranchesArePresent = false;

    branches.add(branchAndContentIf(node, node.getFirstChild(FlexGrammar.SUB_STATEMENT)));
    AstNode currentIfStatement = node;

    while (currentIfStatement.hasDirectChildren(FlexKeyword.ELSE)) {
      AstNode elseStatement = currentIfStatement.getLastChild(FlexGrammar.SUB_STATEMENT).getFirstChild(FlexGrammar.STATEMENT);
      if (elseStatement != null && elseStatement.hasDirectChildren(FlexGrammar.IF_STATEMENT)) {
        currentIfStatement = elseStatement.getFirstChild(FlexGrammar.IF_STATEMENT);
        visitedIfStatements.add(currentIfStatement);
        branches.add(branchAndContentIf(currentIfStatement, currentIfStatement.getFirstChild(FlexGrammar.SUB_STATEMENT)));
      } else {
        AstNode theElse = currentIfStatement.getFirstChild(FlexKeyword.ELSE);
        if (theElse != null) {
          branches.add(branchAndContentIf(theElse, currentIfStatement.getLastChild(FlexGrammar.SUB_STATEMENT)));
        }
        allBranchesArePresent = true;
        break;
      }
    }

    return new ConditionalStructure(node, branches, allBranchesArePresent);
  }

  static ConditionalStructure switchStatement(AstNode node) {
    List<BranchAndContent> branches = new ArrayList<>();
    boolean allBranchesArePresent = false;

    for (AstNode caseElement : node.getChildren(FlexGrammar.CASE_ELEMENT)) {
      List<AstNode> directives = caseElement.getChildren(FlexGrammar.DIRECTIVE);
      if (!directives.isEmpty() && isBreakStatement(directives.get(directives.size() - 1).getFirstChild())) {
        directives = directives.subList(0, directives.size() - 1);
      }
      branches.add(branchAndContentSwitch(caseElement, directives));
      for (AstNode caseLabelNode : caseElement.getChildren(FlexGrammar.CASE_LABEL)) {
        if (caseLabelNode.hasDirectChildren(FlexKeyword.DEFAULT)) {
          allBranchesArePresent = true;
        }
      }
    }

    return new ConditionalStructure(node, branches, allBranchesArePresent);
  }

  private static boolean isBreakStatement(AstNode node) {
    return node.is(FlexGrammar.STATEMENT) && node.hasDirectChildren(FlexGrammar.BREAK_STATEMENT);
  }

  AstNode getNode() {
    return node;
  }

}
