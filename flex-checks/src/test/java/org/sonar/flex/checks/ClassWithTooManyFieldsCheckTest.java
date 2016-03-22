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

import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class ClassWithTooManyFieldsCheckTest {

  private ClassWithTooManyFieldsCheck check = new ClassWithTooManyFieldsCheck();

  @Test
  public void testDefault() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/ClassWithTooManyFields.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .noMore();
  }

  @Test
  public void custom_maximum_field_threshold() {
    check.maximumFieldThreshold = 5;

    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/ClassWithTooManyFields.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Refactor this class so it has no more than " + check.maximumFieldThreshold + " fields, rather than the 6 it currently has.")
      .noMore();
  }

  @Test
  public void custom_both_parameters() {
    check.maximumFieldThreshold = 2;
    check.countNonpublicFields = false;

    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/ClassWithTooManyFields.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Refactor this class so it has no more than " + check.maximumFieldThreshold + " public fields, rather than the 3 it currently has.")
      .noMore();
  }

}
