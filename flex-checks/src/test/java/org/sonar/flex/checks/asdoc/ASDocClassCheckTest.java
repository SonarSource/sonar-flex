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
package org.sonar.flex.checks.asdoc;

import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.flex.checks.ASDocCheckTest;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class ASDocClassCheckTest extends ASDocCheckTest {

  @Test
  public void custom_only_classes_property_on() throws IllegalAccessException {
    activeOnly("classes");

    SourceFile file = FlexAstScanner.scanSingleFile(TEST_FILE, check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      // class A
      .next().atLine(1).withMessage("Add the missing ASDoc for this class.")

      // class B
      .next().atLine(30).withMessage("Add the missing ASDoc for this class.")

      .noMore();
  }

}
