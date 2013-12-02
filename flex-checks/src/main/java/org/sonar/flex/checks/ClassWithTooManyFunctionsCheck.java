/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1448",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class ClassWithTooManyFunctionsCheck extends SquidCheck<LexerlessGrammar> {


  private static final int DEFAULT = 20;

  @RuleProperty(
    key = "maximumFunctionThreshold",
    defaultValue = "" + DEFAULT)
  int maximumFunctionThreshold = DEFAULT;


  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbFunction = Clazz.getFunctions(astNode).size();

    if (nbFunction > maximumFunctionThreshold) {
      getContext().createLineViolation(this, "Class \"{0}\" has {1} functions, which is greater than {2} authorized. Split it into smaller classes.",
        astNode, Clazz.getName(astNode), nbFunction, maximumFunctionThreshold);
    }
  }
}
