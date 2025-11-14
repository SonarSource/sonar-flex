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
package org.sonar.flex.metrics;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.flex.FlexCommentAnalyser;
import org.sonar.flex.FlexVisitor;

public class FileLinesVisitor extends FlexVisitor {

  private final Set<Integer> linesOfCode = new HashSet<>();
  private final Set<Integer> linesOfComments = new HashSet<>();
  private final Set<Integer> noSonarLines = new HashSet<>();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    linesOfCode.clear();
    linesOfComments.clear();
    noSonarLines.clear();
  }

  @Override
  public void visitToken(Token token) {
    if (!token.getType().equals(GenericTokenType.EOF)) {
      linesOfCode.add(token.getLine());
    }

    for (Trivia trivia : token.getTrivia()) {
      if (trivia.isComment()) {
        visitComment(trivia);
      }
    }
  }

  private void visitComment(Trivia trivia) {
    String[] commentLines = FlexCommentAnalyser.getContents(trivia.getToken().getOriginalValue()).split("(\r)?\n|\r", -1);
    int line = trivia.getToken().getLine();
    for (String commentLine : commentLines) {
      if (commentLine.contains("NOSONAR")) {
        linesOfComments.remove(line);
        noSonarLines.add(line);
      } else if (!FlexCommentAnalyser.isBlank(commentLine) && !noSonarLines.contains(line)) {
        linesOfComments.add(line);
      }
      line++;
    }
  }

  public Set<Integer> linesOfCode() {
    return linesOfCode;
  }

  public Set<Integer> linesOfComments() {
    return linesOfComments;
  }

  public Set<Integer> noSonarLines() {
    return noSonarLines;
  }
}
