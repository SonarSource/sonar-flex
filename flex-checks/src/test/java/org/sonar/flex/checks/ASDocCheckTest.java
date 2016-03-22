/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;
import java.lang.reflect.Field;

public class ASDocCheckTest {

  protected static final File TEST_FILE = new File("src/test/resources/checks/ASDoc.as");
  protected ASDocCheck check = new ASDocCheck();

  @Test
  public void test_default() {
    SourceFile file = FlexAstScanner.scanSingleFile(TEST_FILE, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      // class A
      .next().atLine(1).withMessage("Add the missing ASDoc for this class.")
      .next().atLine(7).withMessage("Add the missing ASDoc for this field declaration.")
      .next().atLine(12).withMessage("Add the missing \"@return\" ASDoc for the return value of this method.")
      .next().atLine(17).withMessage("Add the missing \"@param\" ASDoc for: param1.")
      .next().atLine(19).withMessage("Add the missing ASDoc for this method.")

      // class B
      .next().atLine(30).withMessage("Add the missing ASDoc for this class.")
      .next().atLine(33).withMessage("Add the missing ASDoc for this method.")
      .next().atLine(38).withMessage("Add the missing \"@param\" ASDoc for: p1, p2.")
      .next().atLine(43).withMessage("Add the missing ASDoc for this method.")

      .noMore();
  }

  @Test
  public void test_all_off() throws IllegalAccessException {
    deactivateAll();

    SourceFile file = FlexAstScanner.scanSingleFile(TEST_FILE, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
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
