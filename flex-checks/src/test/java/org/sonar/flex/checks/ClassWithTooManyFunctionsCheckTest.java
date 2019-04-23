/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2019 SonarSource SA
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

public class ClassWithTooManyFunctionsCheckTest {

  private ClassWithTooManyFunctionsCheck check = new ClassWithTooManyFunctionsCheck();

  @Test
  public void defaults() {
    FlexVerifier.verify(new File("src/test/resources/checks/ClassWithTooManyFunctions.as"), check);
  }

  @Test
  public void custom_maximum_function_threshold() {
    check.maximumFunctionThreshold = 1;

    FlexVerifier.verify(new File("src/test/resources/checks/ClassWithTooManyFunctions-1.as"), check);
  }

  @Test
  public void custom_count_non_publicMethods() {
    check.countNonpublicMethods = false;

    FlexVerifier.verifyNoIssueIgnoringExpected(new File("src/test/resources/checks/ClassWithTooManyFunctions.as"), check);
  }

  @Test
  public void custom_both_parameters() {
    check.maximumFunctionThreshold = 1;
    check.countNonpublicMethods = false;

    FlexVerifier.verify(new File("src/test/resources/checks/ClassWithTooManyFunctions-1-public.as"), check);
  }
}
