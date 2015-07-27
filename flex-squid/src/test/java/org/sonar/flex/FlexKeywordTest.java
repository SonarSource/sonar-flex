/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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
package org.sonar.flex;

import org.junit.Test;
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
      .matches("native")
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

    assertThat(FlexKeyword.keywords()).hasSize(43);
    assertThat(FlexKeyword.values()).hasSize(43 + /* syntactic: */ 11);
  }
}
