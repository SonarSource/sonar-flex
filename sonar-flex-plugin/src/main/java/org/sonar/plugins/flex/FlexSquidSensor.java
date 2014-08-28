/*
 * SonarQube Flex Plugin
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
package org.sonar.plugins.flex;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.checks.AnnotationCheckFactory;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issue;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.ActiveRule;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.sonar.flex.FlexAstScanner;
import org.sonar.flex.FlexConfiguration;
import org.sonar.flex.FlexSquidPackage;
import org.sonar.flex.api.FlexMetric;
import org.sonar.flex.checks.CheckList;
import org.sonar.flex.metrics.FileLinesVisitor;
import org.sonar.plugins.flex.core.Flex;
import org.sonar.plugins.flex.core.FlexResourceBridge;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;
import org.sonar.squidbridge.api.*;
import org.sonar.squidbridge.indexer.QueryByParent;
import org.sonar.squidbridge.indexer.QueryByType;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class FlexSquidSensor implements Sensor {

  private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = {1, 2, 4, 6, 8, 10, 12};
  private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = {0, 5, 10, 20, 30, 60, 90};

  private static final Predicate<java.io.File> MXML_FILTER = new Predicate<java.io.File>() {
    public boolean apply(java.io.File input) {
      return input != null && input.getAbsolutePath().endsWith(".mxml");
    }
  };

  private final AnnotationCheckFactory annotationCheckFactory;
  private final FlexResourceBridge resourceBridge;
  private final FileLinesContextFactory fileLinesContextFactory;
  private final ModuleFileSystem fileSystem;
  private final ResourcePerspectives resourcePerspectives;

  private Project project;
  private SensorContext context;
  private AstScanner<LexerlessGrammar> scanner;

  public FlexSquidSensor(RulesProfile profile, FlexResourceBridge resourceBridge, FileLinesContextFactory fileLinesContextFactory,
                         ModuleFileSystem fileSystem, ResourcePerspectives resourcePerspectives) {
    this.annotationCheckFactory = AnnotationCheckFactory.create(profile, CheckList.REPOSITORY_KEY, CheckList.getChecks());
    this.resourceBridge = resourceBridge;
    this.fileLinesContextFactory = fileLinesContextFactory;
    this.fileSystem = fileSystem;
    this.resourcePerspectives = resourcePerspectives;
  }

  public boolean shouldExecuteOnProject(Project project) {
    // compatiblity with 3.7
    return Flex.KEY.equals(project.getLanguageKey())
      || (StringUtils.isBlank(project.getLanguageKey()) && !fileSystem.files(Flex.FILE_QUERY_ON_SOURCES).isEmpty());
  }

  public void analyse(Project project, SensorContext context) {
    this.project = project;
    this.context = context;

    Collection<SquidAstVisitor<LexerlessGrammar>> squidChecks = annotationCheckFactory.getChecks();
    List<SquidAstVisitor<LexerlessGrammar>> visitors = Lists.newArrayList(squidChecks);
    visitors.add(new FileLinesVisitor(project, fileLinesContextFactory));
    this.scanner = FlexAstScanner.create(createConfiguration(), visitors.toArray(new SquidAstVisitor[visitors.size()]));
    Collection<java.io.File> files = fileSystem.files(Flex.FILE_QUERY_ON_SOURCES);
    files = ImmutableList.copyOf(Collections2.filter(files, Predicates.not(MXML_FILTER)));
    scanner.scanFiles(files);

    Collection<SourceCode> squidSourceFiles = scanner.getIndex().search(new QueryByType(SourceFile.class));
    save(squidSourceFiles);

    Collection<SourceCode> squidPackages = scanner.getIndex().search(new QueryByType(FlexSquidPackage.class));
    for (SourceCode pkg : squidPackages) {
      String packageName = pkg.getKey();
      if (!"".equals(packageName)) {
        Directory directory = resourceBridge.findDirectory(packageName);
        context.saveMeasure(directory, CoreMetrics.PACKAGES, 1.0);
      }
    }
  }

  private FlexConfiguration createConfiguration() {
    return new FlexConfiguration(fileSystem.sourceCharset());
  }

  private void save(Collection<SourceCode> squidSourceFiles) {
    for (SourceCode squidSourceFile : squidSourceFiles) {
      SourceFile squidFile = (SourceFile) squidSourceFile;

      File sonarFile = File.fromIOFile(new java.io.File(squidFile.getKey()), project);

      saveClassComplexity(sonarFile, squidFile);
      saveMeasures(sonarFile, squidFile);
      saveFunctionsComplexityDistribution(sonarFile, squidFile);
      saveFilesComplexityDistribution(sonarFile, squidFile);
      saveViolations(sonarFile, squidFile);
    }
  }

  private void saveMeasures(File sonarFile, SourceFile squidFile) {
    context.saveMeasure(sonarFile, CoreMetrics.FILES, squidFile.getDouble(FlexMetric.FILES));
    context.saveMeasure(sonarFile, CoreMetrics.LINES, squidFile.getDouble(FlexMetric.LINES));
    context.saveMeasure(sonarFile, CoreMetrics.NCLOC, squidFile.getDouble(FlexMetric.LINES_OF_CODE));
    context.saveMeasure(sonarFile, CoreMetrics.COMMENT_LINES, squidFile.getDouble(FlexMetric.COMMENT_LINES));
    context.saveMeasure(sonarFile, CoreMetrics.CLASSES, squidFile.getDouble(FlexMetric.CLASSES));
    context.saveMeasure(sonarFile, CoreMetrics.FUNCTIONS, squidFile.getDouble(FlexMetric.FUNCTIONS));
    context.saveMeasure(sonarFile, CoreMetrics.STATEMENTS, squidFile.getDouble(FlexMetric.STATEMENTS));
    context.saveMeasure(sonarFile, CoreMetrics.COMPLEXITY, squidFile.getDouble(FlexMetric.COMPLEXITY));
  }

  private void saveClassComplexity(org.sonar.api.resources.File sonarFile, SourceFile squidFile) {
    Collection<SourceCode> classes = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceClass.class));
    double complexityInClasses = 0;
    for (SourceCode squidClass : classes) {
      double classComplexity = squidClass.getDouble(FlexMetric.COMPLEXITY);
      complexityInClasses += classComplexity;
    }
    context.saveMeasure(sonarFile, CoreMetrics.COMPLEXITY_IN_CLASSES, complexityInClasses);
  }

  private void saveFunctionsComplexityDistribution(File sonarFile, SourceFile squidFile) {
    Collection<SourceCode> squidFunctionsInFile = scanner.getIndex().search(new QueryByParent(squidFile), new QueryByType(SourceFunction.class));
    RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
    for (SourceCode squidFunction : squidFunctionsInFile) {
      complexityDistribution.add(squidFunction.getDouble(FlexMetric.COMPLEXITY));
    }
    context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  }

  private void saveFilesComplexityDistribution(File sonarFile, SourceFile squidFile) {
    RangeDistributionBuilder complexityDistribution = new RangeDistributionBuilder(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
    complexityDistribution.add(squidFile.getDouble(FlexMetric.COMPLEXITY));
    context.saveMeasure(sonarFile, complexityDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
  }

  private void saveViolations(File sonarFile, SourceFile squidFile) {
    Collection<CheckMessage> messages = squidFile.getCheckMessages();
    if (messages != null) {

      for (CheckMessage message : messages) {
        ActiveRule rule = annotationCheckFactory.getActiveRule(message.getCheck());
        Issuable issuable = resourcePerspectives.as(Issuable.class, sonarFile);

        if (issuable != null) {
          Issue issue = issuable.newIssueBuilder()
            .ruleKey(RuleKey.of(rule.getRepositoryKey(), rule.getRuleKey()))
            .line(message.getLine())
            .message(message.getText(Locale.ENGLISH))
            .build();

          issuable.addIssue(issue);
        }
      }
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
