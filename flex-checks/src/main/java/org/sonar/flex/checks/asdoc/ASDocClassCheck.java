/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
      check.getContext().createLineViolation(check, "Add the missing ASDoc for this class.", astNode);
    }

    return false;
  }

  private static boolean isAttributes(@Nullable AstNode classDefPreviousNode) {
    return classDefPreviousNode != null && classDefPreviousNode.is(FlexGrammar.ATTRIBUTES);
  }

}
