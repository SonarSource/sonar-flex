/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
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

import java.io.File;
import org.junit.jupiter.api.Test;

public class ClassNameCheckTest {

  private ClassNameCheck check = new ClassNameCheck();

  @Test
  public void defaults() {
    FlexVerifier.verify(new File("src/test/resources/checks/ClassName.as"), check);
  }

  @Test
  public void custom() {
    check.format = "^[a-z][a-zA-Z]*$";

    FlexVerifier.verify(new File("src/test/resources/checks/ClassName2.as"), check);
  }

}
