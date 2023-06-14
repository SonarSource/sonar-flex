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
package org.sonar.flex.checks;

import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.impl.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexVisitorContext;
import org.sonar.flex.Issue;
import org.sonar.flex.parser.FlexParser;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonarsource.analyzer.commons.checks.verifier.CommentParser;
import org.sonarsource.analyzer.commons.checks.verifier.SingleFileVerifier;

import static org.assertj.core.api.Assertions.assertThat;

public class FlexVerifier {

  public static void verify(File file, FlexCheck check) {
    createVerifier(file, check, true).assertOneOrMoreIssues();
  }

  public static void verifyNoIssue(File file, FlexCheck check) {
    createVerifier(file, check, true).assertNoIssues();
  }

  public static void verifyNoIssueIgnoringExpected(File file, FlexCheck check) {
    createVerifier(file, check, false).assertNoIssues();
  }

  public static void verifySingleIssueOnFile(File file, FlexCheck check, String expectedIssueMessage) {
    List<Issue> issues = check.scanFileForIssues(createContext(file));
    assertThat(issues).hasSize(1);
    assertThat(issues.get(0).line()).isNull();
    assertThat(issues.get(0).message()).isEqualTo(expectedIssueMessage);
  }

  private static SingleFileVerifier createVerifier(File file, FlexCheck check, boolean addCommentsAsExpectedIssues) {
    SingleFileVerifier verifier = SingleFileVerifier.create(file.toPath(), StandardCharsets.UTF_8);

    FlexVisitorContext context = createContext(file);

    for (Issue issue : check.scanFileForIssues(context)) {
      SingleFileVerifier.IssueBuilder issueBuilder = verifier.reportIssue(issue.message());
      Integer line = issue.line();
      SingleFileVerifier.Issue verifierIssue;
      if (line != null) {
        verifierIssue = issueBuilder.onLine(line);
      } else {
        verifierIssue = issueBuilder.onFile();
      }
      verifierIssue.withGap(issue.cost());
    }

    if (addCommentsAsExpectedIssues) {
      CommentParser commentParser = CommentParser.create().addSingleLineCommentSyntax("//");
      commentParser.parseInto(file.toPath(), verifier);
    }

    return verifier;
  }

  private static FlexVisitorContext createContext(File file) {
    Parser<LexerlessGrammar> parser = FlexParser.create(StandardCharsets.UTF_8);
    String fileContent;
    try {
      fileContent = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + file, e);
    }
    FlexVisitorContext context;
    try {
      context = new FlexVisitorContext(fileContent, parser.parse(file));
    } catch (RecognitionException e) {
      context = new FlexVisitorContext(fileContent, e);
    }
    return context;
  }

}
