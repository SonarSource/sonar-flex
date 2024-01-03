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

public class ClassWithTooManyFieldsCheckTest {

  private ClassWithTooManyFieldsCheck check = new ClassWithTooManyFieldsCheck();

  @Test
  public void testDefault() {
    FlexVerifier.verifyNoIssue(new File("src/test/resources/checks/ClassWithTooManyFields.as"), check);
  }

  @Test
  public void custom_maximum_field_threshold() {
    check.maximumFieldThreshold = 5;
    FlexVerifier.verify(new File("src/test/resources/checks/ClassWithTooManyFields-5.as"), check);
  }

  @Test
  public void custom_both_parameters() {
    check.maximumFieldThreshold = 2;
    check.countNonpublicFields = false;

    FlexVerifier.verify(new File("src/test/resources/checks/ClassWithTooManyFields-2.as"), check);
  }

}
