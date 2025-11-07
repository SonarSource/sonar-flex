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
package org.sonar.flex.checks.utils;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;

public final class Modifiers {

  private Modifiers() {
  }

  public static Set<AstNodeType> getModifiers(@Nullable AstNode attributes) {
    Set<AstNodeType> modifiersSet = new HashSet<>();

    if (attributes != null && attributes.is(FlexGrammar.ATTRIBUTES)) {
      for (AstNode attribute : attributes.getChildren(FlexGrammar.ATTRIBUTE)) {

        if (attribute.getFirstChild().is(FlexGrammar.RESERVED_NAMESPACE)) {
          modifiersSet.add(attribute.getFirstChild(FlexGrammar.RESERVED_NAMESPACE).getFirstChild().getType());

        } else if (attribute.getFirstChild().is(FlexGrammar.ATTRIBUTE_EXPR) && attribute.getFirstChild().getNumberOfChildren() == 1) {
          modifiersSet.add(attribute.getFirstChild().getFirstChild(FlexGrammar.IDENTIFIER).getFirstChild().getType());
        }
      }
    }
    return modifiersSet;
  }

  public static boolean isNonPublic(Set<AstNodeType> modifiers) {
    for (AstNodeType modifier : modifiers) {
      if (modifier.equals(FlexKeyword.INTERNAL) || modifier.equals(FlexKeyword.PROTECTED) || modifier.equals(FlexKeyword.PRIVATE)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isPublic(AstNode attriutes) {
    Set<AstNodeType> modifiers = getModifiers(attriutes);
    return modifiers.contains(FlexKeyword.PUBLIC) || !isNonPublic(modifiers);
  }

}
