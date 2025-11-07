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
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.metrics.FileLinesVisitor;

@Rule(key = "S1151")
public class TooManyLinesInCaseCheck extends FlexCheck {

  private static final int DEFAULT = 5;

  @RuleProperty(
    key = "max",
    description = "Maximum number of lines",
    defaultValue = "" + DEFAULT)
  int max = DEFAULT;


  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CASE_ELEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    FileLinesVisitor linesVisitor = new FileLinesVisitor();
    linesVisitor.scanNode(astNode);

    // Lines which contain case labels are not taken into account in LoC
    List<AstNode> children = astNode.getChildren(FlexGrammar.CASE_LABEL);
    AstNode firstLabelNode = children.get(0);
    AstNode lastLabelNode = children.get(children.size() - 1);
    int caseLabelLines = lastLabelNode.getTokenLine() - firstLabelNode.getTokenLine();

    int lines = linesVisitor.linesOfCode().size() - caseLabelLines;
    if (lines > max) {
      addIssue(
        MessageFormat.format("Reduce this switch case number of lines of code from {0} to at most {1}, for example by extracting code into methods.", lines, max),
        lastLabelNode);
    }
  }

}
