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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.parser.FlexParser;
import org.sonar.sslr.parser.LexerlessGrammar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.sonar.flex.checks.SyntacticEquivalence.areEquivalent;

public class SyntacticEquivalenceTest {

  private static final Parser<LexerlessGrammar> PARSER = FlexParser.create(StandardCharsets.UTF_8);

  @Test
  public void test_areEquivalent() {
    assertThat(areEquivalent(expression("a"), expression("a"))).isTrue();
    assertThat(areEquivalent(expression("a"), expression("b"))).isFalse();
    assertThat(areEquivalent(expression("a"), expression("a + b"))).isFalse();
    assertThat(areEquivalent(expression("a + b"), expression("a + b"))).isTrue();
    assertThat(areEquivalent(expression("a + b"), expression("a + c"))).isFalse();
    assertThat(areEquivalent(program("if (a) {}"), program("if (a) {}"))).isTrue();
    assertThat(areEquivalent(program("if (a) {}"), program("if (a) { foo(); }"))).isFalse();
  }

  private AstNode expression(String code) {
    return program(code).getFirstDescendant(FlexGrammar.EXPRESSION);
  }

  private AstNode program(String code) {
    return PARSER.parse(code);
  }
}
