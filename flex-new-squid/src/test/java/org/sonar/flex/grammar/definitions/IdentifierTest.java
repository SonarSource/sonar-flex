/*
 * Sonar Flex Plugin
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
package org.sonar.flex.grammar.definitions;

import org.junit.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class IdentifierTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void unicodeEscapeSequence() {
    Assertions.assertThat(g.rule(FlexGrammar.UNICODE_ESCAPE_SEQUENCE))
      .matches("u12aF");
  }

  @Test
  public void identifierStart() {
    Assertions.assertThat(g.rule(FlexGrammar.IDENTIFIER_START))
      .matches("$")
      .matches("N");
  }

  @Test
  public void identifierPart() {
    Assertions.assertThat(g.rule(FlexGrammar.IDENTIFIER))
      .matches("$var")
      .matches("_arg ")
      .notMatches("if");
  }
}
