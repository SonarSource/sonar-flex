/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CommentRegularExpressionCheckTest {

  @Test
  public void test() {
    CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

    check.regularExpression = "(?i).*TODO.*";
    check.message = "Avoid TODO";

    FlexVerifier.verify(new File("src/test/resources/checks/CommentRegularExpression.as"), check);
  }

  @Test
  public void test_default_regex() {
    FlexVerifier.verifyNoIssue(new File("src/test/resources/checks/CommentRegularExpressionDefault.as"), new CommentRegularExpressionCheck());
  }

  @Test
  public void test_bad_regex() {
    CommentRegularExpressionCheck check = new CommentRegularExpressionCheck();

    check.regularExpression = "[a-z";
    check.message = "Avoid TODO";

    final File file = new File("src/test/resources/checks/CommentRegularExpression.as");
    RuntimeException e = assertThrows(RuntimeException.class, () -> FlexVerifier.verify(file, check));
    assertEquals("Unable to compile regular expression: [a-z", e.getMessage());
  }

}
