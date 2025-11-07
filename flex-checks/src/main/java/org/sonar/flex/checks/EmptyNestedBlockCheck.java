/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
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
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;

@Rule(key = "S108")
public class EmptyNestedBlockCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.BLOCK);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!astNode.getFirstChild(FlexGrammar.DIRECTIVES).hasChildren() && isNested(astNode) && !hasComment(astNode)) {
      addIssue("Either remove or fill this block of code.", astNode);
    }
  }

  private static boolean isNested(AstNode blockNode) {
    return !blockNode.getParent().is(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.INTERFACE_DEF,
      FlexGrammar.PACKAGE_DEF,
      FlexGrammar.FUNCTION_COMMON);
  }

  private static boolean hasComment(AstNode blockNode) {
    return blockNode.getFirstChild(FlexPunctuator.RCURLYBRACE).getToken().hasTrivia();
  }

}
