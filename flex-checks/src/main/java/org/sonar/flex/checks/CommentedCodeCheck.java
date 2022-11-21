/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
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

import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexCommentAnalyser;
import org.sonar.flex.FlexKeyword;
import org.sonarsource.analyzer.commons.recognizers.CodeRecognizer;
import org.sonarsource.analyzer.commons.recognizers.ContainsDetector;
import org.sonarsource.analyzer.commons.recognizers.Detector;
import org.sonarsource.analyzer.commons.recognizers.EndWithDetector;
import org.sonarsource.analyzer.commons.recognizers.KeywordsDetector;
import org.sonarsource.analyzer.commons.recognizers.LanguageFootprint;

@Rule(key = "CommentedCode")
public class CommentedCodeCheck extends FlexCheck {

  private static final double THRESHOLD = 0.9;

  private final CodeRecognizer codeRecognizer = new CodeRecognizer(THRESHOLD, new FlexRecognizer());
  private final Pattern regexpToDivideStringByLine = Pattern.compile("(\r?\n)|(\r)");

  private static class FlexRecognizer implements LanguageFootprint {

    @Override
    public Set<Detector> getDetectors() {
      return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(
        new EndWithDetector(0.95, '}', ';', '{'),
        new KeywordsDetector(0.3, FlexKeyword.keywordValues()),
        new ContainsDetector(0.95, "++", "--"),
        new ContainsDetector(0.95, "*=", "/=", "%=", "+=", "-=", "<<=", ">>=", ">>>=", "&=", "^=", "|="),
        new ContainsDetector(0.95, "==", "!=", "===", "!=="))));
    }

  }

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.emptyList();
  }

  @Override
  public void visitToken(Token token) {
    for (Trivia trivia : token.getTrivia()) {
      String[] lines = regexpToDivideStringByLine.split(FlexCommentAnalyser.getContents(trivia.getToken().getOriginalValue()));
      for (int lineOffset = 0; lineOffset < lines.length; lineOffset++) {
        if (codeRecognizer.isLineOfCode(lines[lineOffset])) {
          addIssueAtLine("Sections of code should not be \"commented out\".", trivia.getToken().getLine() + lineOffset);
          break;
        }
      }
    }
  }

}
