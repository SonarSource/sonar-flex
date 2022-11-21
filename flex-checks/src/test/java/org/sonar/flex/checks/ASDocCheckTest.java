/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
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
package org.sonar.flex.checks;

import java.io.File;
import java.lang.reflect.Field;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

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
      if (ArrayUtils.contains(fieldNames, f.getName())) {
        f.setBoolean(check, true);
      } else {
        f.setBoolean(check, false);
      }
    }
  }

  protected void deactivateAll() throws IllegalAccessException {
    activeOnly();
  }
}
