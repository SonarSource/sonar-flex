/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
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
