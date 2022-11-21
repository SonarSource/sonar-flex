/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
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
package org.sonar.flex.metrics;

import java.io.File;
import org.junit.Test;
import org.sonar.flex.TestVisitorContext;

import static org.fest.assertions.Assertions.assertThat;

public class FileMetricsTest {

  @Test
  public void comments() {
    assertThat(metrics("comments.as").commentLines()).containsOnly(2, 6, 10);
    assertThat(metrics("comments.as").nosonarLines()).containsOnly(13);
  }

  @Test
  public void lines_of_code() {
    assertThat(metrics("lines_of_code.as").linesOfCode()).containsOnly(8, 12, 14, 15);
  }

  @Test
  public void statements() {
    assertThat(metrics("statements.as").numberOfStatements()).isEqualTo(18);
  }

  @Test
  public void executable_lines() {
    assertThat(metrics("statements.as").executableLines()).isEqualTo("2=1;4=1;5=1;7=1;9=1;13=1;15=1;18=1;20=1;23=1;27=1;31=1;35=1;41=1;46=1;48=1;50=1;");
  }

  @Test
  public void functions() {
    assertThat(metrics("functions.as").numberOfFunctions()).isEqualTo(3);
  }

  @Test
  public void classes() {
    assertThat(metrics("classes.as").numberOfClasses()).isEqualTo(2);
  }

  private FileMetrics metrics(String fileName) {
    File baseDir = new File("src/test/resources/metrics/");
    File file = new File(baseDir, fileName);
    return new FileMetrics(TestVisitorContext.create(file));
  }

}
