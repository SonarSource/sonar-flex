/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource Sàrl.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.flex.checks.asdoc;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.sonar.flex.checks.ASDocCheckTest;
import org.sonar.flex.checks.FlexVerifier;

public class ASDocMemberCheckTest extends ASDocCheckTest {

  @Test
  public void custom_only_properties_property_on() throws IllegalAccessException {
    activeOnly("properties");

    FlexVerifier.verify(new File("src/test/resources/checks/ASDoc-properties.as"), check);
  }

  @Test
  public void custom_only_methods() throws IllegalAccessException {
    activeOnly("methods");

    FlexVerifier.verify(new File("src/test/resources/checks/ASDoc-methods.as"), check);
  }

}
