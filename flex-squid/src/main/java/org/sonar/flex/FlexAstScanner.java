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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.CommentAnalyser;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.squid.*;
import com.sonar.sslr.squid.metrics.*;
import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.api.FlexMetric;
import org.sonar.flex.api.FlexPunctuator;
import org.sonar.flex.parser.FlexParser;
import org.sonar.squid.api.*;
import org.sonar.squid.indexer.QueryByType;

import java.io.File;
import java.util.Collection;

public final class FlexAstScanner {

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
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<FlexGrammar>(new SourceCodeBuilderCallback() {
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        String className = astNode.getChild(1).getTokenValue();
        SourceClass cls = new SourceClass(className + ":" + astNode.getToken().getLine());
        cls.setStartAtLine(astNode.getTokenLine());
        return cls;
      }
    }, parser.getGrammar().classDefinition, parser.getGrammar().interfaceDefinition));

    builder.withSquidAstVisitor(CounterVisitor.<FlexGrammar> builder()
        .setMetricDef(FlexMetric.CLASSES)
        .subscribeTo(parser.getGrammar().classDefinition, parser.getGrammar().interfaceDefinition)
        .build());

    /* Functions */
    builder.withSquidAstVisitor(new SourceCodeBuilderVisitor<FlexGrammar>(new SourceCodeBuilderCallback() {
      public SourceCode createSourceCode(SourceCode parentSourceCode, AstNode astNode) {
        String functionName = astNode.getChild(1).getTokenValue();
        SourceFunction function = new SourceFunction(functionName + ":" + astNode.getToken().getLine());
        function.setStartAtLine(astNode.getTokenLine());
        return function;
      }
    }, parser.getGrammar().methodDefinition));

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
        .subscribeTo(
            parser.getGrammar().labelledStatement,
            parser.getGrammar().defaultXmlNamespaceStatement,
            parser.getGrammar().declarationStatement,
            parser.getGrammar().expressionStatement,
            parser.getGrammar().ifStatement,
            parser.getGrammar().forEachStatement,
            parser.getGrammar().forStatement,
            parser.getGrammar().whileStatement,
            parser.getGrammar().doWhileStatement,
            parser.getGrammar().withStatement,
            parser.getGrammar().switchStatement,
            parser.getGrammar().breakStatement,
            parser.getGrammar().continueStatement,
            parser.getGrammar().returnStatement,
            parser.getGrammar().throwStatement,
            parser.getGrammar().tryStatement,
            parser.getGrammar().includeDirective,
            parser.getGrammar().emptyStatement)
        .build());

    AstNodeType[] complexityAstNodeType = new AstNodeType[] {
      // Entry points
      parser.getGrammar().methodDefinition,
      parser.getGrammar().functionExpression,

      // Branching nodes
      parser.getGrammar().ifStatement,
      parser.getGrammar().forStatement,
      parser.getGrammar().forEachStatement,
      parser.getGrammar().whileStatement,
      parser.getGrammar().doWhileStatement,
      parser.getGrammar().switchStatement,
      parser.getGrammar().caseClause,
      parser.getGrammar().defaultClause,
      parser.getGrammar().catchBlock,
      parser.getGrammar().returnStatement,
      parser.getGrammar().throwStatement,

      // Expressions
      FlexPunctuator.QUESTION,
      FlexPunctuator.LAND,
      FlexPunctuator.LOR
    };
    builder.withSquidAstVisitor(ComplexityVisitor.<FlexGrammar> builder()
        .setMetricDef(FlexMetric.COMPLEXITY)
        .subscribeTo(complexityAstNodeType)
        .build());

    /* External visitors (typically Check ones) */
    for (SquidAstVisitor<FlexGrammar> visitor : visitors) {
      builder.withSquidAstVisitor(visitor);
    }

    return builder.build();
  }

}
