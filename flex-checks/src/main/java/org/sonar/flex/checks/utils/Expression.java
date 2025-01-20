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
package org.sonar.flex.checks.utils;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;

public final class Expression {

  private Expression() {
  }

  public static String exprToString(AstNode expression) {
    StringBuilder builder = new StringBuilder();
    for (Token t : expression.getTokens()) {
      builder.append(t.getValue());
    }
    return builder.toString();
  }

}
