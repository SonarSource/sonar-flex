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

import java.util.Objects;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SetUtilsTest {

  @Test
  public void test_returns_unmodifiable_set() {
    Set<String> set = SetUtils.immutableSetOf();
    Assert.assertThrows(UnsupportedOperationException.class, () -> set.add("value"));
  }

  @Test
  public void test_construct_strings_set() {
    Set<String> set = SetUtils.immutableSetOf("value1", "value2");

    assertThat(set).containsExactlyInAnyOrder("value1", "value2");
  }

  @Test
  public void test_construct_any_set() {
    Set<SomeType> set = SetUtils.immutableSetOf(new SomeType("value1"), new SomeType("value2"));

    assertThat(set).containsExactlyInAnyOrder(new SomeType("value1"), new SomeType("value2"));
  }

  private static class SomeType {
    final String value;

    private SomeType(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SomeType value1 = (SomeType) o;
      return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }
}
