/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class XPathCheckTest {

  @Test
  public void line_issue() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "//IDENTIFIER[string-length(@tokenValue) >= 10]";
    check.message = "Avoid identifiers which are too long!";

    FlexVerifier.verify(new File("src/test/resources/checks/Xpath.as"), check);
  }

  @Test
  public void file_issue() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "count(//IDENTIFIER) > 0";
    check.message = "message!";

    FlexVerifier.verify(new File("src/test/resources/checks/Xpath-count.as"), check);
  }

  @Test
  public void parsing_issue() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "//IDENTIFIER[string-length(@tokenValue) >= 10]";
    check.message = "message!";
    FlexVerifier.verifyNoIssueIgnoringExpected(new File("src/test/resources/checks/ParsingError.as"), check);
  }

  @Test
  public void test_bad_regex() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "[a-";
    final File file = new File("src/test/resources/checks/ParsingError.as");
    RuntimeException e = assertThrows(RuntimeException.class, () -> FlexVerifier.verifyNoIssueIgnoringExpected(file, check));
    assertEquals("Unable to initialize the XPath engine, perhaps because of an invalid query: [a-", e.getMessage());
  }

  @Test
  public void test_no_issue() {
    XPathCheck check = new XPathCheck();
    check.xpathQuery = "count(//IDENTIFIER)";
    check.message = "message!";
    FlexVerifier.verifyNoIssue(new File("src/test/resources/checks/Xpath-noissue.as"), check);
  }
}
