/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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

import com.sonar.sslr.api.AstNode;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;

@Rule(
  key = "S1448",
  name = "Classes should not have too many methods",
  priority = Priority.MAJOR,
  tags = Tags.BRAIN_OVERLOAD)
@ActivatedByDefault
@SqaleConstantRemediation("1h")
public class ClassWithTooManyFunctionsCheck extends SquidCheck<LexerlessGrammar> {


  private static final int DEFAULT_MAX = 20;
  private static final boolean DEFAULT_INCLUDE_NON_PUBLIC = true;

  @RuleProperty(
    key = "maximumFunctionThreshold",
    description = "The maximum number of methods",
    defaultValue = "" + DEFAULT_MAX)
  int maximumFunctionThreshold = DEFAULT_MAX;

  @RuleProperty(
    key = "countNonpublicMethods",
    description = "Whether or not to include non-public methods in the count",
    defaultValue = "" + DEFAULT_INCLUDE_NON_PUBLIC,
    type = "BOOLEAN")
  boolean countNonpublicMethods = DEFAULT_INCLUDE_NON_PUBLIC;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbMethods = getNumberOfMethods(astNode);

    if (nbMethods > maximumFunctionThreshold) {
      getContext().createLineViolation(this, "Class \"{0}\" has {1} methods, which is greater than {2} authorized. Split it into smaller classes.",
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

  private static int getNumberOfNonPublicMethods(List<AstNode> methods) {
    int nbNonPublicMethod = 0;

    for (AstNode method : methods) {
      if (Modifiers.isNonPublic(Modifiers.getModifiers(method.getPreviousAstNode()))) {
        nbNonPublicMethod++;
      }
    }
    return nbNonPublicMethod;
  }

}
