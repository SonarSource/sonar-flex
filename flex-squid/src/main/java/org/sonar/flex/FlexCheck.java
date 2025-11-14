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
package org.sonar.flex;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class FlexCheck extends FlexVisitor {

  private List<Issue> issues = new ArrayList<>();

  public List<Issue> scanFileForIssues(FlexVisitorContext context) {
    issues = new ArrayList<>();
    scanFile(context);
    return Collections.unmodifiableList(issues);
  }

  public void addIssueAtLine(String message, int line) {
    issues.add(Issue.lineIssue(line, message));
  }

  public void addIssue(String message, AstNode node) {
    addIssueAtLine(message, node.getTokenLine());
  }

  public void addIssue(String message, Token token) {
    addIssueAtLine(message, token.getLine());
  }

  public void addFileIssue(String message) {
    issues.add(Issue.fileIssue(message));
  }

  public void addIssueWithCost(String message, AstNode node, double cost) {
    issues.add(Issue.lineIssue(node.getTokenLine(), message, cost));
  }

}
