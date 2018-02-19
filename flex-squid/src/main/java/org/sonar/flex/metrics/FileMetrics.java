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
package org.sonar.flex.metrics;

import com.sonar.sslr.api.AstNode;
import java.util.Objects;
import java.util.Set;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexVisitorContext;

public class FileMetrics {

  private final int numberOfStatements;
  private final int numberOfClasses;
  private final int numberOfFunctions;
  private final FileLinesVisitor fileLinesVisitor = new FileLinesVisitor();

  public FileMetrics(FlexVisitorContext context) {
    AstNode rootTree = context.rootTree();
    Objects.requireNonNull(rootTree, "Cannot compute metrics without a root tree");
    numberOfStatements = rootTree.getDescendants(
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
      FlexGrammar.EMPTY_STATEMENT).size();
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

}
