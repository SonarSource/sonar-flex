/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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
package org.sonar.flex;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class Issue {

  private final Integer line;
  private final String message;
  private final Double cost;

  private Issue(@Nullable Integer line, String message, @Nullable Double cost) {
    this.line = line;
    this.message = message;
    this.cost = cost;
  }

  public static Issue fileIssue(String message) {
    return new Issue(null, message, null);
  }

  public static Issue lineIssue(int line, String message) {
    return new Issue(line, message, null);
  }

  public static Issue lineIssue(int line, String message, double cost) {
    return new Issue(line, message, cost);
  }

  @CheckForNull
  public Integer line() {
    return line;
  }

  public String message() {
    return message;
  }

  @CheckForNull
  public Double cost() {
    return cost;
  }
}
