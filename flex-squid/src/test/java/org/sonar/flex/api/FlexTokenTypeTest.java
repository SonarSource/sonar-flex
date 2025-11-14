/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.api;

import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

public class FlexTokenTypeTest {

  @Test
  public void test() {
    assertThat(FlexTokenType.values().length).isEqualTo(2);

    for (FlexTokenType tokenType : FlexTokenType.values()) {
      assertThat(tokenType.getName()).isEqualTo(tokenType.name());
      assertThat(tokenType.getValue()).isEqualTo(tokenType.name());
      assertThat(tokenType.hasToBeSkippedFromAst(null)).isFalse();
    }
  }

}
