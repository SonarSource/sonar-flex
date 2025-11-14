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
import com.sonar.sslr.api.Token;
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexPunctuator;

@Rule(key = "S1438")
public class SemicolonCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(FlexGrammar.EOS, FlexGrammar.EOS_NO_LB);
  }

  @Override
  public void visitNode(AstNode astNode) {
    Token lastToken = astNode.getLastToken();
    if (lastToken == null || !FlexPunctuator.SEMICOLON.getValue().equals(lastToken.getValue())) {
      addIssue("Add a semicolon at the end of this statement", astNode.getParent().getLastToken());
    }
  }

}
