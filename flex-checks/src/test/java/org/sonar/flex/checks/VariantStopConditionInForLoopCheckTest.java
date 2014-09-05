/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.flex.checks;

import org.junit.Test;
import org.sonar.flex.FlexAstScanner;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import java.io.File;

public class VariantStopConditionInForLoopCheckTest {

  private VariantStopConditionInForLoopCheck check = new VariantStopConditionInForLoopCheck();

  @Test
  public void defaults() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/VariantStopConditionInForLoop.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(3).withMessage("Do not update the loop counter \"a\" within the loop body.")
      .next().atLine(7)
      .next().atLine(8)
      .next().atLine(13)
      .next().atLine(16)
      .next().atLine(17)
      .next().atLine(19)
      .next().atLine(26)
      .next().atLine(27)
      .next().atLine(37)
      .next().atLine(38)
      .next().atLine(39)
      .next().atLine(40)
      .next().atLine(44)
      .next().atLine(53)
      .next().atLine(55)
      .next().atLine(59).withMessage("Calculate the stop condition value outside the loop and set it to a variable.")
      .next().atLine(62)
      .next().atLine(71)
      .noMore();
  }

}
