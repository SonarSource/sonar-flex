/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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

public class ActionScript2CheckTest {

  private final ActionScript2Check check = new ActionScript2Check();

  @Test
  public void test() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/checks/ActionScript2.as"), check);
    CheckMessagesVerifier.verify(file.getCheckMessages())
//      .next().atLine(1).withMessage("'intrinsic' not available in ActionScript 3.0")
      .next().atLine(2).withMessage("Operator '<>' not available in ActionScript 3.0")
//      .next().atLine(4).withMessage("Operator 'not' not available in ActionScript 3.0")
      .next().atLine(6).withMessage("Operator 'or' not available in ActionScript 3.0")
      .next().atLine(8).withMessage("Operator 'and' not available in ActionScript 3.0")
      .next().atLine(10).withMessage("Operator 'lt' not available in ActionScript 3.0")
//      .next().atLine(12).withMessage("'set variable statement' not available in ActionScript 3.0")
      .noMore();
  }

}
