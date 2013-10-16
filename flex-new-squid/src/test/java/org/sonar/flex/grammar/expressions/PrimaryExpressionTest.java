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
package org.sonar.flex.grammar.expressions;

import org.sonar.flex.*;
import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class PrimaryExpressionTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void String() {
    Assertions.assertThat(g.rule(FlexGrammar.PRIMARY_EXPR))
      .matches("\"Sonar source\"");
  }

  @Test
  public void reservedNamespace() {
    Assertions.assertThat(g.rule(FlexGrammar.PRIMARY_EXPR))
      .matches("internal  ");
  }

  @Test
  public void emptyArrayInitialiser() {
    Assertions.assertThat(g.rule(FlexGrammar.PRIMARY_EXPR))
      .matches("[]")
      .matches("[   ]");
  }

  @Test
  public void emptyObjectInitialiser() {
    Assertions.assertThat(g.rule(FlexGrammar.PRIMARY_EXPR))
      .matches("{}")
      .matches("{   }");
  }

  @Test
  public void filledObjectInitialiser() {
    Assertions.assertThat(g.rule(FlexGrammar.PRIMARY_EXPR))
      .matches("{ FirstName : \"John\", LastName: \"Smith\"}")
      .matches("{ Age : 43}");
  }
}
