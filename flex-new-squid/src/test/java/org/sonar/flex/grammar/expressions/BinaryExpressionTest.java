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

public class BinaryExpressionTest {

  private LexerlessGrammar g = FlexGrammar.createGrammar();

  @Test
  public void multiplicative() {
    Assertions.assertThat(g.rule(FlexGrammar.MULTIPLICATIVE_EXPR))
      .matches("25*3")
      .matches("25*10*2")
      .matches("25 %  10*2");
  }

  @Test
  public void additive() {
    Assertions.assertThat(g.rule(FlexGrammar.ADDITIVE_EXPR))
      .matches("3+  7")
      .matches("3+  7+ 2");
  }
  
  
  @Test
  public void shift() {
    Assertions.assertThat(g.rule(FlexGrammar.SHIFT_EXPR))
      .matches("3 <<  7")
      .matches("3+  7 >>2")
      .matches("3+  7 >>>2");
  }
  
  @Test
  public void relational() {
    Assertions.assertThat(g.rule(FlexGrammar.RELATIONAL_EXPR))
      .matches("3 > 2 < 1")
      .matches("( 3 >= 2) < 1")
      .matches("(true) < 1")
      .matches("f is I");
  }
  
  @Test
  public void equality() {
    Assertions.assertThat(g.rule(FlexGrammar.EQUALITY_EXPR))
      .matches("x == new Object()")
      .matches("x == f(y, z)")
      .matches("x == f(y , z).yeah");
  }
  
  @Test
  public void bitewise() {
    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_AND_EXPR))
      .matches("x&f(y , z).yeah");
    
    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_XOR_EXPR))
      .matches("a + b ^ c");
    
    Assertions.assertThat(g.rule(FlexGrammar.BITEWISE_OR_EXPR))
      .matches("x().z |f(y, z)");
  }
  
  
  @Test
  public void logical() {
    Assertions.assertThat(g.rule(FlexGrammar.LOGICAL_AND_EXPR))
      .matches("x&f(y , z).yeah &&plouf");
    
    Assertions.assertThat(g.rule(FlexGrammar.LOGICAL_OR_EXPR))
      .matches("x&f(y , z).yeah ||plouf || pelouse43");
  }
}
