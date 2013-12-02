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
package org.sonar.flex.checks.utils;

import com.sonar.sslr.api.AstNode;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

public class Modifiers {

  public boolean isPrivate = false;
  public boolean isProtected = false;
  public boolean isPublic = false;
  public boolean isStatic = false;
  public boolean isDynamic = false;
  public boolean isInternal = false;

  public Modifiers() {
  }

  public static Modifiers getModifiers(AstNode attributes) {
    Modifiers modifiers = new Modifiers();
    if (attributes != null && attributes.is(FlexGrammar.ATTRIBUTES)) {

      for (AstNode attribute : attributes.getChildren(FlexGrammar.ATTRIBUTE)) {

        if (attribute.getFirstChild().is(FlexGrammar.RESERVED_NAMESPACE)) {
          AstNode reservedNamespace = attribute.getFirstChild(FlexGrammar.RESERVED_NAMESPACE).getFirstChild();

          if (reservedNamespace.is(FlexKeyword.PUBLIC)) {
            modifiers.isPublic = true;
          } else if (reservedNamespace.is(FlexKeyword.PRIVATE)) {
            modifiers.isPrivate = true;
          } else if (reservedNamespace.is(FlexKeyword.PROTECTED)) {
            modifiers.isProtected = true;
          } else if (reservedNamespace.is(FlexKeyword.INTERNAL)) {
            modifiers.isInternal = true;
          }
        } else if (attribute.getFirstChild().is(FlexGrammar.ATTRIBUTE_EXPR) && attribute.getFirstChild().getNumberOfChildren() == 1) {
          AstNode identifier = attribute.getFirstChild().getFirstChild(FlexGrammar.IDENTIFIER).getFirstChild();

          if (identifier.is(FlexKeyword.DYNAMIC)) {
            modifiers.isDynamic = true;
          } else if (identifier.is(FlexKeyword.STATIC)) {
            modifiers.isStatic = true;
          }
        }
      }
    }
    return modifiers;
  }
}
