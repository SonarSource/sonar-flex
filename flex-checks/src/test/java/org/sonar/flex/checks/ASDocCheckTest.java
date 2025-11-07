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
import java.lang.reflect.Field;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class ASDocCheckTest {

  protected static final File TEST_FILE = new File("src/test/resources/checks/ASDoc.as");
  protected ASDocCheck check = new ASDocCheck();

  @Test
  public void test_default() {
    FlexVerifier.verify(TEST_FILE, check);
  }

  @Test
  public void test_all_off() throws IllegalAccessException {
    deactivateAll();

    FlexVerifier.verifyNoIssueIgnoringExpected(TEST_FILE, check);
  }

  protected void activeOnly(String... fieldNames) throws IllegalAccessException {
    for (Field f : check.getClass().getFields()) {
      if (!f.getType().equals(boolean.class)) {
        continue;
      }
      f.setBoolean(check, Arrays.asList(fieldNames).contains(f.getName()));
    }
  }

  protected void deactivateAll() throws IllegalAccessException {
    activeOnly();
  }
}
