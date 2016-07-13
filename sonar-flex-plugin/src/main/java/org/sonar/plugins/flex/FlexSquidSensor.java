/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
package org.sonar.plugins.flex;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.scan.filesystem.PathResolver;
import org.sonar.flex.FlexAstScanner;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.api.FlexMetric;
import org.sonar.flex.checks.CheckList;
import org.sonar.flex.metrics.FileLinesVisitor;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.CheckMessage;
import org.sonar.squidbridge.api.SourceClass;
import org.sonar.squidbridge.api.SourceCode;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.api.SourceFunction;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class FlexSquidSensor implements Sensor {

  private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = {1, 2, 4, 6, 8, 10, 12};
  private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = {0, 5, 10, 20, 30, 60, 90};

  private static final Predicate<java.io.File> MXML_FILTER = file -> file != null && file.getAbsolutePath().endsWith(".mxml");

  private final Checks<SquidCheck<LexerlessGrammar>> checks;
  private final FileLinesContextFactory fileLinesContextFactory;
  private final FileSystem fileSystem;
  private final ResourcePerspectives resourcePerspectives;
  private final FilePredicate mainFilePredicates;
  private final PathResolver pathResolver;

  private SensorContext context;
  private AstScanner<LexerlessGrammar> scanner;

  public FlexSquidSensor(CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory,
                         FileSystem fileSystem, ResourcePerspectives resourcePerspectives, PathResolver pathResolver) {
    this.pathResolver = pathResolver;
    this.checks = checkFactory
      .<SquidCheck<LexerlessGrammar>>create(CheckList.REPOSITORY_KEY)
      .addAnnotatedChecks(CheckList.getChecks());
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.fileSystem = fileSystem;
    this.resourcePerspectives = resourcePerspectives;
    this.mainFilePredicates = fileSystem.predicates().and(
      fileSystem.predicates().hasLanguage(Flex.KEY),
      fileSystem.predicates().hasType(InputFile.Type.MAIN));
  }

  @Override
  public boolean shouldExecuteOnProject(Project project) {
    return fileSystem.hasFiles(mainFilePredicates);
  }

  @Override
  public void analyse(Project project, SensorContext context) {
    this.context = context;
    List<SquidAstVisitor<LexerlessGrammar>> visitors = Lists.<SquidAstVisitor<LexerlessGrammar>>newArrayList(checks.all());
    visitors.add(new FileLinesVisitor(fileLinesContextFactory, fileSystem));
    this.scanner = FlexAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));

    Iterable<java.io.File> files = fileSystem.files(mainFilePredicates);
    scanner.scanFiles(ImmutableList.copyOf(Iterables.filter(files, Predicates.not(MXML_FILTER))));

    Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
    save(squidSourceFiles);
  }

  private FlexConfiguration createConfiguration() {
    return new FlexConfiguration(fileSystem.encoding());
  }

  private void save(Collection<SourceCode> squidSourceFiles) {
    for (SourceCode squidSourceFile : squidSourceFiles) {
      SourceFile squidFile = (SourceFile) squidSourceFile;

      String relativePath = pathResolver.relativePath(fileSystem.baseDir(), new java.io.File(squidFile.getKey()));
      InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(relativePath));

      saveClassComplexity(inputFile, squidFile);
      saveMeasures(inputFile, squidFile);
      saveFunctionsComplexityDistribution(inputFile, squidFile);
      saveFilesComplexityDistribution(inputFile, squidFile);
      saveViolations(inputFile, squidFile);
    }
  }

  private void saveMeasures(InputFile inputFile, SourceFile squidFile) {
    context.saveMeasure(inputFile, CoreMetrics.NCLOC, squidFile.getDouble(FlexMetric.LINES_OF_CODE));
    context.saveMeasure(inputFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(FlexMetric.COMMENT_LINES));
    context.saveMeasure(inputFile, CoreMetrics.CLASSES, squidFile.getDouble(FlexMetric.CLASSES));
    context.saveMeasure(inputFile, CoreMetrics.FUNCTIONS, squidFile.getDouble(FlexMetric.FUNCTIONS));
    context.saveMeasure(inputFile, CoreMetrics.STATEMENTS, squidFile.getDouble(FlexMetric.STATEMENTS));
    context.saveMeasure(inputFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(FlexMetric.COMPLEXITY));
  }

  private void saveClassComplexity(InputFile inputFile, SourceFile squidFile) {
    Collection<SourceCode> classes = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceClass.class));
    double complexityInClasses = 0;
    for (SourceCode squidClass : classes) {
      double classComplexity = squidClass.getDouble(FlexMetric.COMPLEXITY);
      complexityInClasses += classComplexity;
    }
    context.saveMeasure(inputFile, CoreMetrics.COMPLEXITY_IN_CLASSES, complexityInClasses);
  }

  private void saveFunctionsComplexityDistribution(InputFile inputFile, SourceFile squidFile) {
    Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceFunction.class));
    RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
    for (SourceCode squidFunction : squidFunctionsInFile) {
      complexityDistribution.add(squidFunction.getDouble(FlexMetric.COMPLEXITY));
    }
    context.saveMeasure(inputFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  }

  private void saveFilesComplexityDistribution(InputFile inputFile, SourceFile squidFile) {
    RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
    complexityDistribution.add(squidFile.getDouble(FlexMetric.COMPLEXITY));
    context.saveMeasure(inputFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  }

  private void saveViolations(InputFile inputFile, SourceFile squidFile) {
    Collection<CheckMessage> messages = squidFile.getCheckMessages();
    if (messages != null) {

      for (CheckMessage message : messages) {
        RuleKey ruleKey = checks.ruleKey((SquidCheck<LexerlessGrammar>) message.getCheck());
        Issuable issuable = resourcePerspectives.as(Issuable.class, inputFile);

        if (issuable != null) {
          IssueBuilder issueBuilder = issuable.newIssueBuilder()
              .ruleKey(ruleKey)
              .line(message.getLine())
              .message(message.getText(Locale.ENGLISH));

          if (message.getCost() != null) {
            issueBuilder.effortToFix(message.getCost());
          }

          issuable.addIssue(issueBuilder.build());
        }
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
