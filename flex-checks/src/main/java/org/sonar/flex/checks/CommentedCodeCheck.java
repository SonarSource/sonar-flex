/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.flex.checks;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.squidbridge.recognizer.CodeRecognizer;
import org.sonar.squidbridge.recognizer.ContainsDetector;
import org.sonar.squidbridge.recognizer.Detector;
import org.sonar.squidbridge.recognizer.EndWithDetector;
import org.sonar.squidbridge.recognizer.KeywordsDetector;
import org.sonar.squidbridge.recognizer.LanguageFootprint;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Set;
import java.util.regex.Pattern;

@Rule(
  key = "CommentedCode",
  tags = {Tags.MISRA, Tags.UNUSED},
  name = "Sections of code should not be \"commented out\"",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("5min")
public class CommentedCodeCheck extends SquidCheck<LexerlessGrammar> implements AstAndTokenVisitor {

  private static final double THRESHOLD = 0.9;

  private final CodeRecognizer codeRecognizer = new CodeRecognizer(THRESHOLD, new FlexRecognizer());
  private final Pattern regexpToDivideStringByLine = Pattern.compile("(\r?\n)|(\r)");

  private static class FlexRecognizer implements LanguageFootprint {

    @Override
    public Set<Detector> getDetectors() {
      return ImmutableSet.of(
        new EndWithDetector(0.95, '}', ';', '{'),
        new KeywordsDetector(0.3, FlexKeyword.keywordValues()),
        new ContainsDetector(0.95, "++", "--"),
        new ContainsDetector(0.95, "*=", "/=", "%=", "+=", "-=", "<<=", ">>=", ">>>=", "&=", "^=", "|="),
        new ContainsDetector(0.95, "==", "!=", "===", "!=="));
    }

  }

  @Override
  public void visitToken(Token token) {
    for (Trivia trivia : token.getTrivia()) {
      if (trivia.isComment()) {
        String[] lines = regexpToDivideStringByLine.split(getContext().getCommentAnalyser().getContents(trivia.getToken().getOriginalValue()));
        for (int lineOffset = 0; lineOffset < lines.length; lineOffset++) {
          if (codeRecognizer.isLineOfCode(lines[lineOffset])) {
            getContext().createLineViolation(this, "Sections of code should not be \"commented out\".", trivia.getToken().getLine() + lineOffset);
            break;
          }
        }
      }
    }
  }

}
