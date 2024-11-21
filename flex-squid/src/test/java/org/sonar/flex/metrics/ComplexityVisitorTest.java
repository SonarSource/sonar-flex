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
package org.sonar.flex.metrics;

import com.sonar.sslr.impl.Parser;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.parser.FlexParser;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.assertj.core.api.Assertions.assertThat;

public class ComplexityVisitorTest {

  private Parser<LexerlessGrammar> parser = FlexParser.create(StandardCharsets.UTF_8);

  @Test
  public void simple_statement() {
    assertThat(complexity("a = 1;")).isZero();
  }

  @Test
  public void and_operator() {
    assertThat(complexity("a == x && y;")).isEqualTo(1);
  }

  @Test
  public void or_operator() {
    assertThat(complexity("a = x || y;")).isEqualTo(1);
  }

  @Test
  public void if_statement() {
    assertThat(complexity("if (x) { a = 1; }")).isEqualTo(1);
  }

  @Test
  public void while_statement() {
    assertThat(complexity("while(x) { a = 1; }")).isEqualTo(1);
  }

  @Test
  public void for_statement() {
    assertThat(complexity("for (i = 1; i < x; i++) {}")).isEqualTo(1);
  }

  @Test
  public void function_definition() {
    assertThat(complexity("function f() {}")).isEqualTo(1);
  }

  @Test
  public void nested_function_definition() {
    assertThat(complexity("function f() { function nested() { x = a && b; } }")).isEqualTo(3);
    assertThat(functionComplexity("function f() { function nested() { x = a && b; } }")).isEqualTo(1);
    assertThat(functionComplexity("function f() { x = a && b; }")).isEqualTo(2);
  }

  private int complexity(String source) {
    return ComplexityVisitor.complexity(parser.parse(source));
  }

  private int functionComplexity(String source) {
    return ComplexityVisitor.functionComplexity(parser.parse(source).getFirstDescendant(FlexGrammar.FUNCTION_DEF));
  }

}
