/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
import org.junit.Test;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

public class ClassNameCheckTest {

  private ClassNameCheck check = new ClassNameCheck();

  @Test
  public void defaults() {
    CheckMessagesVerifier.verify(FlexCheckTester.checkMessages(new File("src/test/resources/checks/ClassName.as"), check))
      .next().atLine(4);
  }

  @Test
  public void custom() {
    check.format = "^[a-z][a-zA-Z]*$";

    CheckMessagesVerifier.verify(FlexCheckTester.checkMessages(new File("src/test/resources/checks/ClassName.as"), check))
      .next().atLine(1);
  }

}
