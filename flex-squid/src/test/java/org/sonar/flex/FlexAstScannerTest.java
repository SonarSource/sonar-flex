/*
 * Sonar Flex Plugin
 * Copyright (C) 2010 SonarSource
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.flex;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.sonar.sslr.squid.AstScanner;
import org.junit.Test;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.api.FlexMetric;
import org.sonar.squid.api.SourceCode;
import org.sonar.squid.api.SourceFile;
import org.sonar.squid.api.SourceProject;
import org.sonar.squid.indexer.QueryByType;

import java.io.File;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class FlexAstScannerTest {

  @Test
  public void files() {
    AstScanner<FlexGrammar> scanner = FlexAstScanner.create(new FlexConfiguration(Charsets.UTF_8));
    scanner.scanFiles(ImmutableList.of(new File("src/test/resources/metrics/lines.as"), new File("src/test/resources/metrics/lines_of_code.as")));
    SourceProject project = (SourceProject) scanner.getIndex().search(new QueryByType(SourceProject.class)).iterator().next();
    assertThat(project.getInt(FlexMetric.FILES)).isEqualTo(2);
  }

  @Test
  public void comments() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/comments.as"));
    assertThat(file.getInt(FlexMetric.COMMENT_BLANK_LINES)).isEqualTo(6);
    assertThat(file.getInt(FlexMetric.COMMENT_LINES)).isEqualTo(3);
    assertThat(file.getNoSonarTagLines()).contains(13);
    assertThat(file.getNoSonarTagLines().size()).isEqualTo(1);
  }

  @Test
  public void lines() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/lines.as"));
    assertThat(file.getInt(FlexMetric.LINES)).isEqualTo(16);
  }

  @Test
  public void lines_of_code() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/lines_of_code.as"));
    assertThat(file.getInt(FlexMetric.LINES_OF_CODE)).isEqualTo(4);
  }

  @Test
  public void statements() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/statements.as"));
    assertThat(file.getInt(FlexMetric.STATEMENTS)).isEqualTo(16);
  }

  @Test
  public void functions() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/functions.as"));
    assertThat(file.getInt(FlexMetric.FUNCTIONS)).isEqualTo(3);
  }

  @Test
  public void classes() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/classes.as"));
    assertThat(file.getInt(FlexMetric.CLASSES)).isEqualTo(2);
  }

  @Test
  public void packages() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/packages.as"));
    Set<SourceCode> set = file.getChildren();
    assertThat(set.size()).isEqualTo(1);
    assertThat(set).contains(new FlexSquidPackage("foo.bar.baz"));
  }

  @Test
  public void complexity() {
    SourceFile file = FlexAstScanner.scanSingleFile(new File("src/test/resources/metrics/complexity.as"));
    assertThat(file.getInt(FlexMetric.COMPLEXITY)).isEqualTo(16);
  }

}
