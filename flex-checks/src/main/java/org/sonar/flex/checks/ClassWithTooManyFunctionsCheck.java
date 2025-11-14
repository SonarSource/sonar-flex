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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;

@Rule(key = "S1448")
public class ClassWithTooManyFunctionsCheck extends FlexCheck {


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
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbMethods = getNumberOfMethods(astNode);

    if (nbMethods > maximumFunctionThreshold) {
      String message = MessageFormat.format(
        "Class \"{0}\" has {1} methods, which is greater than {2} authorized. Split it into smaller classes.",
        Clazz.getName(astNode), nbMethods, maximumFunctionThreshold);
      addIssue(message, astNode);
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
