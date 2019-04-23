/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2019 SonarSource SA
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
package org.sonar.flex.grammar.literals;

import org.junit.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class DecimalTest {

  private final LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void test() {
    Assertions.assertThat(g.rule(FlexGrammar.DECIMAL))
      .matches("0")
      .matches("1")
      .matches("12")
      .notMatches("01")

      .matches(".2")
      .matches("2.")
      .matches("0.2")

      .matches("1e2")
      .matches("1.2E-3")
      .matches("1.2E+3");

  }

}
