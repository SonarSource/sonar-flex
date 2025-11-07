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
import org.sonar.flex.checks.utils.Clazz;

@Rule(key = "S1186")
public class EmptyMethodCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode function : Clazz.getFunctions(astNode)) {
      AstNode block = function.getFirstChild(FlexGrammar.FUNCTION_COMMON).getFirstChild(FlexGrammar.BLOCK);

      if (block != null && isEmptyBlock(block)) {
        addIssue(
          "Add a nested comment explaining why this method is empty, throw an NotSupportedException or complete the implementation.",
          function);
      }
    }
  }

  private static boolean isEmptyBlock(AstNode block) {
    AstNode rightCurlyBrace = block.getFirstChild(FlexPunctuator.RCURLYBRACE);
    return !block.getFirstChild(FlexGrammar.DIRECTIVES).hasChildren() && !hasComment(rightCurlyBrace);
  }

  private static boolean hasComment(AstNode node) {
    return node.getToken().hasTrivia();
  }
}
