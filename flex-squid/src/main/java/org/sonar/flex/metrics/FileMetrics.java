/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
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
package org.sonar.flex.metrics;

import com.sonar.sslr.api.AstNode;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexVisitorContext;

public class FileMetrics {

  private final int numberOfStatements;
  private final int numberOfClasses;
  private final int numberOfFunctions;
  private final FileLinesVisitor fileLinesVisitor = new FileLinesVisitor();
  private final String executableLines;

  public FileMetrics(FlexVisitorContext context) {
    AstNode rootTree = context.rootTree();
    Objects.requireNonNull(rootTree, "Cannot compute metrics without a root tree");
    List<AstNode> statements = rootTree.getDescendants(
      FlexGrammar.DEFAULT_XML_NAMESPACE_DIRECTIVE,
      FlexGrammar.VARIABLE_DECLARATION_STATEMENT,
      FlexGrammar.EXPRESSION_STATEMENT,
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.FOR_STATEMENT,
      FlexGrammar.WHILE_STATEMENT,
      FlexGrammar.DO_STATEMENT,
      FlexGrammar.WITH_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT,
      FlexGrammar.BREAK_STATEMENT,
      FlexGrammar.CONTINUE_STATEMENT,
      FlexGrammar.RETURN_STATEMENT,
      FlexGrammar.THROW_STATEMENT,
      FlexGrammar.TRY_STATEMENT,
      FlexGrammar.EMPTY_STATEMENT);

    Set<Integer> alreadyMarked = new HashSet<>();
    StringBuilder sb = new StringBuilder();
    for (AstNode descendant : statements) {
      int line = descendant.getTokenLine();
      if (alreadyMarked.add(line)) {
        sb.append(line).append("=1;");
      }
    }
    executableLines = sb.toString();

    numberOfStatements = statements.size();
    numberOfClasses = rootTree.getDescendants(FlexGrammar.CLASS_DEF, FlexGrammar.INTERFACE_DEF).size();
    numberOfFunctions = rootTree.getDescendants(FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR).size();
    fileLinesVisitor.scanFile(context);
  }

  public Set<Integer> linesOfCode() {
    return fileLinesVisitor.linesOfCode();
  }

  public Set<Integer> commentLines() {
    return fileLinesVisitor.linesOfComments();
  }

  public Set<Integer> nosonarLines() {
    return fileLinesVisitor.noSonarLines();
  }

  public int numberOfClasses() {
    return numberOfClasses;
  }

  public int numberOfFunctions() {
    return numberOfFunctions;
  }

  public int numberOfStatements() {
    return numberOfStatements;
  }

  public String executableLines() {
    return executableLines;
  }
}
