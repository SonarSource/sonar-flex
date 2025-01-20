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
package org.sonar.flex;

import org.junit.jupiter.api.Test;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.fest.assertions.Assertions.assertThat;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class FlexKeywordTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void test() {
    assertThat(g.rule(FlexGrammar.KEYWORDS))
      .notMatches("break2");

    assertThat(g.rule(FlexGrammar.KEYWORDS))
      .matches("break")
      .matches("case")
      .matches("catch")
      .matches("class")
      .matches("const")
      .matches("continue")
      .matches("default")
      .matches("delete")
      .matches("do")
      .matches("else")
      .matches("extends")
      .matches("false")
      .matches("finally")
      .matches("for")
      .matches("function")
      .matches("if")
      .matches("implements")
      .matches("import")
      .matches("in")
      .matches("instanceof")
      .matches("interface")
      .matches("internal")
      .matches("is")
      .matches("new")
      .matches("null")
      .matches("package")
      .matches("private")
      .matches("protected")
      .matches("public")
      .matches("return")
      .matches("super")
      .matches("switch")
      .matches("this")
      .matches("throw")
      .matches("true")
      .matches("try")
      .matches("typeof")
      .matches("use")
      .matches("var")
      .matches("void")
      .matches("while")
      .matches("with");

    assertThat(FlexKeyword.keywords()).hasSize(42);
    assertThat(FlexKeyword.values()).hasSize(42 + /* syntactic: */ 12);
  }
}
