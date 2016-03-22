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

public class UnusedFunctionParametersCheckTest {

  @Test
  public void test() {
    UnusedFunctionParametersCheck check = new UnusedFunctionParametersCheck();

    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/UnusedFunctionParameters.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(1).withMessage("Remove the unused function parameter \"b\".")
      .next().atLine(5).withMessage("Remove the unused function parameters \"b, c\".")
      .next().atLine(11)
      .next().atLine(16).withMessage("Remove the unused function parameter \"b\".")
      .next().atLine(17).withMessage("Remove the unused function parameter \"c\".")
      .next().atLine(63).withMessage("Remove the unused function parameter \"e\".")
      .next().atLine(64).withMessage("Remove the unused function parameter \"e\".")
      .next().atLine(65).withMessage("Remove the unused function parameter \"e\".")
      .next().atLine(74).withMessage("Remove the unused function parameter \"e\".")
      .next().atLine(77).withMessage("Remove the unused function parameter \"args\".")
      .next().atLine(78).withMessage("Remove the unused function parameter \"args\".")
      .next().atLine(83).withMessage("Remove the unused function parameters \"e, args\".")
      .next().atLine(86).withMessage("Remove the unused function parameter \"e\".")
      .noMore();
  }

}
