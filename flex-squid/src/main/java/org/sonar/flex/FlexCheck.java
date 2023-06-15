/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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
