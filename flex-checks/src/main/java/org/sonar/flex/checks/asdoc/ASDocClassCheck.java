/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
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
package org.sonar.flex.checks.asdoc;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Trivia;
import javax.annotation.Nullable;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.ASDocCheck;

import java.util.List;

public class ASDocClassCheck {

  /**
   * Return true if class has @private tag in ASDoc comment.
   */
  public boolean visitNode(ASDocCheck check, AstNode astNode) {
    AstNode attributes = astNode.getPreviousAstNode();
    List<Trivia> triviaList = isAttributes(attributes) ? attributes.getToken().getTrivia() : astNode.getToken().getTrivia();

    if (check.containsOnOfTags(triviaList, ASDocCheck.PRIVATE_TAG)) {
      return true;

    } else if (check.classes && !check.hasASDoc(triviaList)) {
      check.addIssue("Add the missing ASDoc for this class.", astNode);
    }

    return false;
  }

  private static boolean isAttributes(@Nullable AstNode classDefPreviousNode) {
    return classDefPreviousNode != null && classDefPreviousNode.is(FlexGrammar.ATTRIBUTES);
  }

}
