/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
package org.sonar.flex.grammar.expressions;

import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class PostfixExpressionTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void postfixExpression() {
    Assertions.assertThat(g.rule(FlexGrammar.POSTFIX_EXPR))
      .matches("new a()")
      .matches("new a")

      .matches("super().f.c.a")
      .matches("super.field")
      .matches("super().field")
      .matches("super()")
      .matches("super")

      .matches("a ().a")

      .matches("a++")
      .notMatches("a\n++")

      .matches("a--")
      .notMatches("a\n--")

      .matches("a..n..z");
  }
}
