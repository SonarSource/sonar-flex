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
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;
import java.util.Set;

@Rule(
  key = "S1448",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class ClassWithTooManyFunctionsCheck extends SquidCheck<LexerlessGrammar> {


  private static final int DEFAULT_MAX = 20;
  private static final boolean DEFAULT_INCLUDE_NON_PUBLIC = true;

  @RuleProperty(
    key = "maximumFunctionThreshold",
    defaultValue = "" + DEFAULT_MAX)
  int maximumFunctionThreshold = DEFAULT_MAX;

  @RuleProperty(
    key = "maximumFunctionThreshold",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_INCLUDE_NON_PUBLIC)
  boolean countNonpublicMethods = DEFAULT_INCLUDE_NON_PUBLIC;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbMethods = getNumberOfMethods(astNode);

    if (nbMethods > maximumFunctionThreshold) {
      getContext().createLineViolation(this, "Class \"{0}\" has {1} functions, which is greater than {2} authorized. Split it into smaller classes.",
        astNode, Clazz.getName(astNode), nbMethods, maximumFunctionThreshold);
    }
  }

  private int getNumberOfMethods(AstNode classNode) {
    List<AstNode> methods = Clazz.getFunctions(classNode);
    int nbMethods = methods.size();

    if (!countNonpublicMethods) {
      nbMethods -= getNumberOfNonPublicMethods(methods);
    }

    return nbMethods;
  }

  private int getNumberOfNonPublicMethods(List<AstNode> methods) {
    int nbNonPublicMethod = 0;

    for (AstNode method : methods) {
      Set<AstNodeType> modifiers = Modifiers.getModifiers(method.getPreviousAstNode());

      if (modifiers.contains(FlexKeyword.PRIVATE) || modifiers.contains(FlexKeyword.PROTECTED) || modifiers.contains(FlexKeyword.INTERNAL)) {
        nbNonPublicMethod++;
      }
    }
    return nbNonPublicMethod;
  }

}
