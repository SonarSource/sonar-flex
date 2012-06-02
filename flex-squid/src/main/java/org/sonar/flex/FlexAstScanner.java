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

import com.sonar.sslr.api.CommentAnalyser;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.squid.AstScanner;
import com.sonar.sslr.squid.SquidAstVisitor;
import com.sonar.sslr.squid.SquidAstVisitorContextImpl;
import com.sonar.sslr.squid.metrics.CommentsVisitor;
import com.sonar.sslr.squid.metrics.CounterVisitor;
import com.sonar.sslr.squid.metrics.LinesOfCodeVisitor;
import com.sonar.sslr.squid.metrics.LinesVisitor;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.api.FlexMetric;
import org.sonar.flex.parser.FlexParser;
import org.sonar.squid.api.SourceCode;
import org.sonar.squid.api.SourceFile;
import org.sonar.squid.api.SourceProject;
import org.sonar.squid.indexer.QueryByType;

import java.io.File;
import java.util.Collection;

public class FlexAstScanner {

  private FlexAstScanner() {
  }

  /**
   * Helper method for testing checks without having to deploy them on a Sonar instance.
   */
  public static SourceFile scanSingleFile(File file, SquidAstVisitor<FlexGrammar>... visitors) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }
    AstScanner<FlexGrammar> scanner = create(new FlexConfiguration(), visitors);
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    return (SourceFile) sources.iterator().next();
  }

  public static AstScanner<FlexGrammar> create(FlexConfiguration conf, SquidAstVisitor<FlexGrammar>... visitors) {
    final SquidAstVisitorContextImpl<FlexGrammar> context = new SquidAstVisitorContextImpl<FlexGrammar>(new SourceProject("Flex Project"));
    final Parser<FlexGrammar> parser = FlexParser.create(conf);

    AstScanner.Builder<FlexGrammar> builder = AstScanner.<FlexGrammar> builder(context).setBaseParser(parser);

    /* Metrics */
    builder.withMetrics(FlexMetric.values());

    /* Comments */
    builder.setCommentAnalyser(
        new CommentAnalyser() {
          @Override
          public boolean isBlank(String line) {
            for (int i = 0; i < line.length(); i++) {
              if (Character.isLetterOrDigit(line.charAt(i))) {
                return false;
              }
            }
            return true;
          }

          @Override
          public String getContents(String comment) {
            return comment.startsWith("//") ? comment.substring(2) : comment.substring(2, comment.length() - 2);
          }
        });

    /* Files */
    builder.setFilesMetric(FlexMetric.FILES);

    /* Classes */
    builder.withSquidAstVisitor(CounterVisitor.<FlexGrammar> builder()
        .setMetricDef(FlexMetric.CLASSES)
        .subscribeTo(parser.getGrammar().classDefinition)
        .build());

    /* Functions */
    builder.withSquidAstVisitor(CounterVisitor.<FlexGrammar> builder()
        .setMetricDef(FlexMetric.FUNCTIONS)
        .subscribeTo(parser.getGrammar().methodDefinition)
        .build());

    /* Metrics */
    builder.withSquidAstVisitor(new LinesVisitor<FlexGrammar>(FlexMetric.LINES));
    builder.withSquidAstVisitor(new LinesOfCodeVisitor<FlexGrammar>(FlexMetric.LINES_OF_CODE));
    builder.withSquidAstVisitor(CommentsVisitor.<FlexGrammar> builder().withCommentMetric(FlexMetric.COMMENT_LINES)
        .withBlankCommentMetric(FlexMetric.COMMENT_BLANK_LINES)
        .withNoSonar(true)
        .withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
        .build());
    builder.withSquidAstVisitor(CounterVisitor.<FlexGrammar> builder()
        .setMetricDef(FlexMetric.STATEMENTS)
        .subscribeTo(parser.getGrammar().statement)
        .build());

    /* External visitors (typically Check ones) */
    for (SquidAstVisitor<FlexGrammar> visitor : visitors) {
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }

}
