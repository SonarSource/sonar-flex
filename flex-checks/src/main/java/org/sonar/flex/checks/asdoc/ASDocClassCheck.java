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
package org.sonar.flex.checks.asdoc;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.ASDocCheck;

public class ASDocClassCheck {

  public void visitNode(ASDocCheck check, AstNode astNode) {
    if (check.classes) {
      checkASDocPresence(check, astNode);
    }
  }

  private void checkASDocPresence(ASDocCheck check, AstNode classDef) {
    AstNode attributes = classDef.getPreviousAstNode();
    Token t = isAttributes(attributes) ? attributes.getToken() : classDef.getToken();

    if (!check.hasASDoc(t.getTrivia())) {
      check.getContext().createLineViolation(check, "Add the missing ASDoc for this class.", classDef);
    }
  }

  private boolean isAttributes(AstNode classDefPreviousNode) {
    return classDefPreviousNode != null && classDefPreviousNode.is(FlexGrammar.ATTRIBUTES);
  }


}
