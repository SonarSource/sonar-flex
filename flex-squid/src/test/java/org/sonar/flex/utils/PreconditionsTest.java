/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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
package org.sonar.flex.utils;

import org.junit.Assert;
import org.junit.Test;

public class PreconditionsTest {

  @Test
  public void test_check_argument_condition_is_true() {
    try {
      Preconditions.checkArgument(true);
    } catch (Exception ex) {
      Assert.fail(String.format("Caught %s. No exception should be thrown!", ex.getClass()));
    }
  }

  @Test
  public void test_check_state_condition_is_true() {
    try {
      Preconditions.checkState(true);
    } catch (Exception ex) {
      Assert.fail(String.format("Caught %s. No exception should be thrown!", ex.getClass()));
    }
  }

  @Test
  public void test_check_argument_condition_is_false() {
    Assert.assertThrows(IllegalArgumentException.class, () -> Preconditions.checkArgument(false));
  }

  @Test
  public void test_check_state_condition_is_false() {
    Assert.assertThrows(IllegalStateException.class, () -> Preconditions.checkState(false));
  }
}