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
package com.adobe.ac.pmd;

import java.io.File;
import java.util.List;

public class FlexPmdParameters
{
  private final String excludePackage;
  private final boolean failOnError;
  private final boolean failOnRuleViolation;
  private final File outputDirectory;
  private final File ruleSet;
  private final File source;
  private final List<File> sourceList;

  public FlexPmdParameters(final String excludePackageToBeSet,
      final boolean failOnErrorToBeSet,
      final boolean failOnRuleViolationToBeSet,
      final File outputDirectoryToBeSet,
      final File ruleSetToBeSet,
      final File sourceToBeSet)
  {
    this(excludePackageToBeSet,
        failOnErrorToBeSet,
        failOnRuleViolationToBeSet,
        outputDirectoryToBeSet,
        ruleSetToBeSet,
        sourceToBeSet,
        null);
  }

  public FlexPmdParameters(final String excludePackageToBeSet,
      final boolean failOnErrorToBeSet,
      final boolean failOnRuleViolationToBeSet,
      final File outputDirectoryToBeSet,
      final File ruleSetToBeSet,
      final File sourceToBeSet,
      final List<File> sourceListToBeSet)
  {
    super();
    excludePackage = excludePackageToBeSet;
    failOnError = failOnErrorToBeSet;
    failOnRuleViolation = failOnRuleViolationToBeSet;
    outputDirectory = outputDirectoryToBeSet;
    ruleSet = ruleSetToBeSet;
    source = sourceToBeSet;
    sourceList = sourceListToBeSet;
  }

  public FlexPmdParameters(final String excludePackageToBeSet,
      final File outputDirectoryToBeSet,
      final File ruleSetToBeSet,
      final File sourceToBeSet)
  {
    this(excludePackageToBeSet, false, false, outputDirectoryToBeSet, ruleSetToBeSet, sourceToBeSet, null);
  }

  public FlexPmdParameters(final String excludePackageToBeSet,
      final File outputDirectoryToBeSet,
      final File ruleSetToBeSet,
      final File sourceToBeSet,
      final List<File> sourceListToBeSet)
  {
    this(excludePackageToBeSet,
        false,
        false,
        outputDirectoryToBeSet,
        ruleSetToBeSet,
        sourceToBeSet,
        sourceListToBeSet);
  }

  public final String getExcludePackage()
  {
    return excludePackage;
  }

  public final File getOutputDirectory()
  {
    return outputDirectory;
  }

  public final File getRuleSet()
  {
    return ruleSet;
  }

  public final File getSource()
  {
    return source;
  }

  public List<File> getSourceList()
  {
    return sourceList;
  }

  public final boolean isFailOnError()
  {
    return failOnError;
  }

  public boolean isFailOnRuleViolation()
  {
    return failOnRuleViolation;
  }
}
