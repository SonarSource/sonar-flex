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
package com.adobe.ac.pmd;

import com.adobe.ac.pmd.files.FileSetUtils;
import com.adobe.ac.pmd.files.IFlexFile;
import com.adobe.ac.pmd.files.impl.FileUtils;
import com.adobe.ac.pmd.nodes.IPackage;
import com.adobe.ac.pmd.nodes.impl.NodeFactory;
import com.adobe.ac.pmd.parser.IParserNode;
import com.adobe.ac.pmd.rules.core.IFlexAstRule;
import com.adobe.ac.pmd.rules.core.IFlexRule;
import com.adobe.ac.utils.StackTraceUtils;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleReference;
import net.sourceforge.pmd.RuleSet;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlexPmdViolations implements Serializable
{
  private static final Logger LOGGER;

  /**
   *
   */
  private static final long serialVersionUID = -3683680443330143504L;

  static
  {
    LOGGER = Logger.getLogger(FlexPmdViolations.class.getName());
    LOGGER.setLevel(Level.WARNING);
  }

  private Map<String, IPackage> asts;
  private Map<String, IFlexFile> files;
  private boolean hasBeenComputed;
  private final Map<String, IFlexRule> rules;
  private final Map<IFlexRule, Long> ruleSpeeds;
  private final Map<IFlexFile, List<IFlexViolation>> violations;

  public FlexPmdViolations()
  {
    violations = new LinkedHashMap<IFlexFile, List<IFlexViolation>>();
    rules = new LinkedHashMap<String, IFlexRule>();
    ruleSpeeds = new LinkedHashMap<IFlexRule, Long>();
    hasBeenComputed = false;
  }

  public final void computeViolations(final File source,
      final List<File> sourceList,
      final RuleSet ruleSet,
      final String packageToExclude) throws PMDException
  {
    hasBeenComputed = true;

    if (ruleSet != null)
    {
      computeRules(ruleSet);
      computeFiles(source,
          sourceList,
          packageToExclude,
          ruleSet.getExcludePatterns());
      computeAsts();
      processRules();
      sortViolations();
    }
  }

  public final Map<IFlexFile, List<IFlexViolation>> getViolations()
  {
    return violations;
  }

  public final boolean hasViolationsBeenComputed()
  {
    return hasBeenComputed;
  }

  private void computeAsts() throws PMDException
  {
    LOGGER.info("computing Asts");

    final long startTime = System.currentTimeMillis();
    asts = computeAsts(files);

    LOGGER.info("computed Asts in "
        + (System.currentTimeMillis() - startTime) + " ms");
  }

  public static Map<String, IPackage> computeAsts(final Map<String, IFlexFile> files) throws PMDException
  {
    LOGGER.setLevel(Level.SEVERE);
    final Map<String, IPackage> asts = new LinkedHashMap<String, IPackage>();

    for (final Entry<String, IFlexFile> fileEntry : files.entrySet())
    {
      final IFlexFile file = fileEntry.getValue();

      try
      {
        final IParserNode node = FileSetUtils.buildAst(file);

        asts.put(file.getFullyQualifiedName(),
            NodeFactory.createPackage(node));
      } catch (final NoClassDefFoundError e)
      {
        LOGGER.warning(buildLogMessage(file,
            e.getMessage()));
      } catch (final CancellationException e)
      {
        LOGGER.warning(buildLogMessage(file,
            e.getMessage()));
      } catch (Exception e) {
        LOGGER.warning(buildLogMessage(file,
            e.getMessage()));
      }
    }
    return asts;
  }

  protected static String buildLogMessage(final IFlexFile file,
      final String message)
  {
    return "While building AST on "
        + file.getFullyQualifiedName() + ", an error occured: " + message;
  }

  private void computeFiles(final File source,
      final List<File> sourceList,
      final String packageToExclude,
      final List<String> excludePatterns) throws PMDException
  {
    LOGGER.info("computing FilesList");

    final long startTime = System.currentTimeMillis();

    files = FileUtils.computeFilesList(source,
        sourceList,
        packageToExclude,
        excludePatterns);
    LOGGER.info("computed FilesList in "
        + (System.currentTimeMillis() - startTime) + " ms");
  }

  private void computeRules(final RuleSet ruleSet)
  {
    LOGGER.info("computing RulesList");

    final long startTime = System.currentTimeMillis();
    Set<String> excludes = new HashSet<String>(ruleSet.getExcludePatterns());

    for (Rule rule : ruleSet.getRules())
    {
      while (rule instanceof RuleReference)
      {
        excludes = ((RuleReference) rule).getRuleSetReference().getExcludes();
        rule = ((RuleReference) rule).getRule();
      }
      final IFlexRule flexRule = (IFlexRule) rule;

      if (excludes != null
          && !excludes.isEmpty())
      {
        flexRule.setExcludes(excludes);
      }
      rules.put(flexRule.getRuleName(),
          flexRule);
    }

    LOGGER.info("computed RulesList in "
        + (System.currentTimeMillis() - startTime) + " ms");
  }

  private void processFile(final IFlexRule currentRule,
      final IFlexFile currentFile)
  {
    try
    {
      final String fullyQualifiedName = currentFile.getFullyQualifiedName();
      final IPackage ast = currentRule instanceof IFlexAstRule ? asts.get(fullyQualifiedName)
          : null;
      final List<IFlexViolation> foundViolations = currentRule.processFile(currentFile,
          ast,
          files);

      if (!foundViolations.isEmpty())
      {
        if (violations.containsKey(currentFile))
        {
          violations.get(currentFile).addAll(foundViolations);
        }
        else
        {
          violations.put(currentFile,
              foundViolations);
        }
      }
    } catch (final Exception e)
    {
      LOGGER.warning(StackTraceUtils.print(currentFile.getFullyQualifiedName(),
          e));
    }
  }

  private void processRule(final IFlexRule currentRule)
  {
    LOGGER.fine("Processing "
        + currentRule.getRuleName() + "...");

    for (final Entry<String, IFlexFile> currentFileEntry : files.entrySet())
    {
      processFile(currentRule,
          currentFileEntry.getValue());
    }
  }

  private void processRule(final String currentRuleName,
      final IFlexRule currentRule)
  {
    final long startTime = System.currentTimeMillis();

    processRule(currentRule);
    final long ellapsedTime = System.currentTimeMillis()
        - startTime;

    if (LOGGER.isLoggable(Level.FINE))
    {
      LOGGER.fine("rule "
          + currentRuleName + " computed in " + ellapsedTime + "ms");
    }
    if (LOGGER.isLoggable(Level.FINER))
    {
      ruleSpeeds.put(currentRule,
          ellapsedTime);
    }
  }

  private void processRules()
  {
    for (final Entry<String, IFlexRule> currentRuleEntry : rules.entrySet())
    {
      processRule(currentRuleEntry.getKey(),
          currentRuleEntry.getValue());
    }
  }

  private void sortViolations()
  {
    for (final Entry<String, IFlexFile> entry : files.entrySet())
    {
      if (violations.containsKey(entry.getValue()))
      {
        Collections.sort(violations.get(entry.getValue()));
      }
    }
  }
}
