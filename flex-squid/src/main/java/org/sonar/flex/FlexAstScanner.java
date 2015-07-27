/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import org.sonar.flex.api.CharsetAwareVisitor;
import org.sonar.flex.api.FlexMetric;
import org.sonar.flex.metrics.ComplexityVisitor;
import org.sonar.flex.parser.FlexParser;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.CommentAnalyser;
import org.sonar.squidbridge.ProgressAstScanner;
import org.sonar.squidbridge.SourceCodeBuilderCallback;
import org.sonar.squidbridge.SourceCodeBuilderVisitor;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.SquidAstVisitorContextImpl;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.api.SourceProject;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.squidbridge.metrics.CommentsVisitor;
import org.sonar.squidbridge.metrics.CounterVisitor;
import org.sonar.squidbridge.metrics.LinesOfCodeVisitor;
import org.sonar.squidbridge.metrics.LinesVisitor;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.io.File;
import java.util.Collection;

public final class FlexAstScanner {

  private static class FlexCommentAnalyser extends CommentAnalyser {
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
  }

  private static class PackageSourceCodeBuilderCallback implements SourceCodeBuilderCallback {
    public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
      AstNode packageNameNode = astNode.getFirstChild(FlexGrammar.PACKAGE_NAME);
      final String packageName;
      if (packageNameNode != null) {
        StringBuilder sb = new StringBuilder();
        for (AstNode part : packageNameNode.getChildren()) {
          sb.append(part.getTokenValue());
        }
        packageName = sb.toString();
      } else {
        packageName = "";
      }
      return new FlexSquidPackage(packageName);
    }
  }

  private FlexAstScanner() {
  }

  /**
   * Helper method for testing checks without having to deploy them on a Sonar instance.
   */
  public static SourceFile scanSingleFile(File file, SquidAstVisitor<LexerlessGrammar>... visitors) {
    if (!file.isFile()) {
      throw new IllegalArgumentException("File '" + file + "' not found.");
    }

    AstScanner<LexerlessGrammar> scanner = create(new FlexConfiguration(Charsets.UTF_8), visitors);
    scanner.scanFile(file);
    Collection<SourceCode> sources = scanner.getIndex().search(new QueryByType(SourceFile.class));
    if (sources.size() != 1) {
      throw new IllegalStateException("Only one SourceFile was expected whereas " + sources.size() + " has been returned.");
    }
    return (SourceFile) sources.iterator().next();
  }

  public static AstScanner<LexerlessGrammar> create(FlexConfiguration conf, SquidAstVisitor<LexerlessGrammar>... visitors) {
    final SquidAstVisitorContextImpl<LexerlessGrammar> context = new SquidAstVisitorContextImpl<LexerlessGrammar>(new SourceProject("Flex Project"));
    final Parser<LexerlessGrammar> parser = FlexParser.create(conf);

    AstScanner.Builder<LexerlessGrammar> builder = new ProgressAstScanner.Builder(context).setBaseParser(parser);

    /* Metrics */
    builder.withMetrics(FlexMetric.values());

    /* Comments */
    builder.setCommentAnalyser(new FlexCommentAnalyser());

    /* Files */
    builder.setFilesMetric(FlexMetric.FILES);

    /* Packages */
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new PackageSourceCodeBuilderCallback(), FlexGrammar.PACKAGE_DEF));

    /* Classes */
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
      private int seq = 0;
      @Override
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        seq++;
        SourceClass cls = new SourceClass("class:" + seq);
        cls.setStartAtLine(astNode.getTokenLine());
        return cls;
      }
    }, FlexGrammar.CLASS_DEF, FlexGrammar.INTERFACE_DEF));

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar> builder()
        .setMetricDef(FlexMetric.CLASSES)
        .subscribeTo(FlexGrammar.CLASS_DEF, FlexGrammar.INTERFACE_DEF)
        .build());

    /* Functions */
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<LexerlessGrammar>(new SourceCodeBuilderCallback() {
      private int seq = 0;

      @Override
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        seq++;
        SourceFunction function = new SourceFunction("function:" + seq);
        function.setStartAtLine(astNode.getTokenLine());
        return function;
      }
    }, FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR));

    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar> builder()
        .setMetricDef(FlexMetric.FUNCTIONS)
        .subscribeTo(FlexGrammar.FUNCTION_DEF, FlexGrammar.FUNCTION_EXPR)
        .build());

    /* Metrics */
    builder.withSquidAstVisitor(new LinesVisitor<LexerlessGrammar>(FlexMetric.LINES));
    builder.withSquidAstVisitor(new LinesOfCodeVisitor<LexerlessGrammar>(FlexMetric.LINES_OF_CODE));
    builder.withSquidAstVisitor(CommentsVisitor.<LexerlessGrammar> builder().withCommentMetric(FlexMetric.COMMENT_LINES)
        .withNoSonar(true)
        .withIgnoreHeaderComment(conf.getIgnoreHeaderComments())
        .build());
    builder.withSquidAstVisitor(CounterVisitor.<LexerlessGrammar> builder()
        .setMetricDef(FlexMetric.STATEMENTS)
        .subscribeTo(
          FlexGrammar.DEFAULT_XML_NAMESPACE_DIRECTIVE,
          FlexGrammar.VARIABLE_DECLARATION_STATEMENT,
          FlexGrammar.EXPRESSION_STATEMENT,
          FlexGrammar.IF_STATEMENT,
          FlexGrammar.FOR_STATEMENT,
          FlexGrammar.WHILE_STATEMENT,
          FlexGrammar.DO_STATEMENT,
          FlexGrammar.WITH_STATEMENT,
          FlexGrammar.SWITCH_STATEMENT,
          FlexGrammar.BREAK_STATEMENT,
          FlexGrammar.CONTINUE_STATEMENT,
          FlexGrammar.RETURN_STATEMENT,
          FlexGrammar.THROW_STATEMENT,
          FlexGrammar.TRY_STATEMENT,
           FlexGrammar.EMPTY_STATEMENT)
        .build());

    builder.withSquidAstVisitor(new ComplexityVisitor());

    /* External visitors (typically Check ones) */
    for (SquidAstVisitor<LexerlessGrammar> visitor : visitors) {
      if (visitor instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) visitor).setCharset(conf.getCharset());
      }
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }

}
