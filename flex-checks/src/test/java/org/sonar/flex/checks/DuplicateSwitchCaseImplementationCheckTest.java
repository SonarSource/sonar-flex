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

public class DuplicateSwitchCaseImplementationCheckTest {

  private DuplicateSwitchCaseImplementationCheck check = new DuplicateSwitchCaseImplementationCheck();

  @Test
  public void test() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/DuplicateSwitchCaseImplementation.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(8).withMessage("Either merge this case with the identical one on line \"5\" or change one of the implementations.")
      .next().atLine(24).withMessage("Either merge this case with the identical one on line \"18\" or change one of the implementations.")
      .next().atLine(36).withMessage("Either merge this case with the identical one on line \"30\" or change one of the implementations.")
      .noMore();
  }
}
