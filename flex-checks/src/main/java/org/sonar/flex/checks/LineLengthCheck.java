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

import com.google.common.io.Files;
import com.sonar.sslr.api.AstNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.api.CharsetAwareVisitor;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

@Rule(
  key = "LineLength",
  name = "Lines should not be too long",
  tags = Tags.CONVENTION,
  priority = Priority.MINOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("1min")
public class LineLengthCheck extends SquidCheck<LexerlessGrammar> implements CharsetAwareVisitor {

  private static final int DEFAULT_MAXIMUM_LINE_LENHGTH = 80;
  private static final Logger LOG = LoggerFactory.getLogger(LineLengthCheck.class);
  private Charset charset;

  @RuleProperty(
    key = "maximumLineLength",
    description = "The maximum authorized line length.",
    defaultValue = "" + DEFAULT_MAXIMUM_LINE_LENHGTH)
  public int maximumLineLength = DEFAULT_MAXIMUM_LINE_LENHGTH;

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    List<String> lines = Collections.emptyList();

    try {
      lines = Files.readLines(getContext().getFile(), charset);
    } catch (IOException e) {
      LOG.error("Unable to execute rule \"LineLength\" for file {} because of error: {}",
        getContext().getFile().getName(), e);
    }
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.length() > maximumLineLength) {
        getContext().createLineViolation(this, "Split this {0} characters long line (which is greater than {1} authorized).", i + 1, line.length(), maximumLineLength);
      }
    }
  }
}
