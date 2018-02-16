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
package org.sonar.flex;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.io.File;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class FlexCheckTest {

  @Test
  public void lineIssue() {
    FlexCheck check = new FlexCheck() {
      @Override
      public List<AstNodeType> subscribedTo() {
        return Collections.singletonList(FlexGrammar.ASSIGNMENT_OPERATOR);
      }
      @Override
      public void visitNode(AstNode node) {
        addIssue("message!", node);
      }
    };
    List<Issue> issues = check.scanFileForIssues(context());
    assertThat(issues).extracting("line", "message", "cost").containsExactly(tuple(2, "message!", null));
  }

  @Test
  public void lineIsueWithCost() {
    FlexCheck check = new FlexCheck() {
      @Override
      public List<AstNodeType> subscribedTo() {
        return Collections.singletonList(FlexGrammar.ASSIGNMENT_OPERATOR);
      }
      @Override
      public void visitNode(AstNode node) {
        addIssueWithCost("message!", node, 42.);
      }
    };
    List<Issue> issues = check.scanFileForIssues(context());
    assertThat(issues).extracting("line", "message", "cost").containsExactly(tuple(2, "message!", 42.));
  }

  @Test
  public void fileIssue() {
    FlexCheck check = new FlexCheck() {
      @Override
      public List<AstNodeType> subscribedTo() {
        return Collections.emptyList();
      }
      @Override
      public void visitFile(AstNode node) {
        addFileIssue("bad file!");
      }
    };
    List<Issue> issues = check.scanFileForIssues(context());
    assertThat(issues).extracting("line", "message", "cost").containsExactly(tuple(null, "bad file!", null));
  }

  private FlexVisitorContext context() {
    return TestVisitorContext.create(new File("src/test/resources/org/sonar/flex/flexcheck.as"));
  }
}
