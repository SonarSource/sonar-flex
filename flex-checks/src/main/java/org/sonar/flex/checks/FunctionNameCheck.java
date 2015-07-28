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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Pattern;

@Rule(
  key = "S100",
  name = "Function names should comply with a naming convention",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("5min")
public class FunctionNameCheck extends SquidCheck<LexerlessGrammar> {


  private static final String DEFAULT = "^[a-z][a-zA-Z0-9]*$";
  private Pattern pattern = null;
  private Deque<String> classes = new ArrayDeque<String>();

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the function names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;


  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
    classes.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classes.push(Clazz.getName(astNode));
    } else {
      String functionName = Function.getName(astNode);

      if (!isConstructor(astNode) && !pattern.matcher(functionName).matches()) {
        getContext().createLineViolation(this, "Rename this \"{0}\" function to match the regular expression {1}", astNode, functionName, format);
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classes.pop();
    }
  }

  private boolean isConstructor(AstNode functionNode) {
    return !classes.isEmpty() && Function.isConstructor(functionNode, classes.peek());
  }
}
