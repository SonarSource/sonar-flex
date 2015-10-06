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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.regex.Pattern;

@Rule(
  key = "S120",
  name = "Package names should comply with a naming convention",
  priority = Priority.MINOR,
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.READABILITY)
@SqaleConstantRemediation("10min")
public class PackageNameCheck extends SquidCheck<LexerlessGrammar> {

  private static final String DEFAULT = "^[a-z]+(\\.[a-z][a-z0-9]*)*$";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the package names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.PACKAGE_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode nameNode = astNode.getFirstChild(FlexGrammar.PACKAGE_NAME);
    if (nameNode != null) {
      String packageIdentifier = concatenate(nameNode);
      if (!pattern.matcher(packageIdentifier).matches()) {
        getContext().createLineViolation(this, "Rename this package name to match the regular expression {0}", astNode, format);
      }
    }
  }

  private static String concatenate(AstNode astNode) {
    StringBuilder sb = new StringBuilder();
    for (Token token : astNode.getTokens()) {
      sb.append(token.getValue());
    }
    return sb.toString();
  }
}
