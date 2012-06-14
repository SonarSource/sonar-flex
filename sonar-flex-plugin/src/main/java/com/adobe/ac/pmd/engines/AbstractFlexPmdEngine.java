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
package com.adobe.ac.pmd.engines;

import com.adobe.ac.pmd.FlexPmdParameters;
import com.adobe.ac.pmd.FlexPmdViolations;
import com.adobe.ac.pmd.IFlexViolation;
import com.adobe.ac.utils.StackTraceUtils;
import net.sourceforge.pmd.PMDException;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractFlexPmdEngine
{
  private static final Logger LOGGER = Logger.getLogger(AbstractFlexPmdEngine.class.getName());

  private static int computeViolationNumber(final FlexPmdViolations flexPmdViolations)
  {
    int foundViolations = 0;
    for (final List<IFlexViolation> violations : flexPmdViolations.getViolations().values())
    {
      foundViolations += violations.size();
    }
    LOGGER.info("Violations number found: "
        + foundViolations);
    return foundViolations;
  }

  private static File extractDefaultRuleSet() throws URISyntaxException,
      IOException
  {
    final String rulesetURI = "/com/adobe/ac/pmd/default_flex.xml";
    final InputStream resourceAsStream = AbstractFlexPmdEngine.class.getResourceAsStream(rulesetURI);
    final File temporaryRuleset = File.createTempFile("default_flex",
        ".xml");
    temporaryRuleset.deleteOnExit();
    final FileOutputStream writter = new FileOutputStream(temporaryRuleset);
    IOUtils.copy(resourceAsStream, writter);

    resourceAsStream.close();
    return temporaryRuleset;
  }

  private final File outputDirectory;
  private final String packageToExclude;
  private RuleSet ruleSet;
  private final File source;
  private final List<File> sourceList;

  public AbstractFlexPmdEngine(final FlexPmdParameters parameters)
  {
    super();

    source = parameters.getSource();
    sourceList = parameters.getSourceList();
    outputDirectory = parameters.getOutputDirectory();
    packageToExclude = parameters.getExcludePackage();
    try
    {
      ruleSet = loadRuleset(parameters.getRuleSet());
    } catch (final URISyntaxException e)
    {
      LOGGER.warning(StackTraceUtils.print(e));
    } catch (final IOException e)
    {
      LOGGER.warning(StackTraceUtils.print(e));
    }
  }

  /**
   * @param flexPmdViolations
   * @return The number of violations with the given ruleset and the result
   *         wrapper in case of reuse
   * @throws PMDException
   * @throws URISyntaxException
   * @throws IOException
   */
  public final void executeReport(final FlexPmdViolations flexPmdViolations) throws PMDException
  {
    if (source == null
        && sourceList == null)
    {
      throw new PMDException("unspecified sourceDirectory");
    }
    if (outputDirectory == null)
    {
      throw new PMDException("unspecified outputDirectory");
    }

    if (ruleSet != null)
    {
      if (!flexPmdViolations.hasViolationsBeenComputed())
      {
        computeViolations(flexPmdViolations);
      }
      computeViolationNumber(flexPmdViolations);
      writeAnyReport(flexPmdViolations);
    }
  }

  public final RuleSet getRuleSet()
  {
    return ruleSet;
  }

  protected File getOutputDirectory()
  {
    return outputDirectory;
  }

  protected abstract void writeReport(final FlexPmdViolations pmd) throws PMDException;

  private void computeViolations(final FlexPmdViolations flexPmdViolations) throws PMDException
  {
    final long startTime = System.currentTimeMillis();

    flexPmdViolations.computeViolations(source,
        sourceList,
        ruleSet,
        packageToExclude);
    final long ellapsedTime = System.currentTimeMillis()
        - startTime;
    LOGGER.info("It took "
        + ellapsedTime + "ms to compute violations");
  }

  private File extractRuleset(final File ruleSetFile) throws URISyntaxException,
      IOException
  {
    return ruleSetFile == null ? extractDefaultRuleSet()
        : ruleSetFile;
  }

  private String getReportType()
  {
    return StringUtils.substringBefore(StringUtils.substringAfter(getClass().getName(),
        "FlexPmd"),
        "Engine");
  }

  private RuleSet loadRuleset(final File ruleSetFile) throws URISyntaxException,
      IOException
  {
    final File realRuleSet = extractRuleset(ruleSetFile);
    final FileInputStream inputStream = new FileInputStream(realRuleSet);
    final RuleSet loadedRuleSet = new RuleSetFactory().createRuleSet(inputStream);

    LOGGER.info("Ruleset: "
        + realRuleSet.getAbsolutePath());
    LOGGER.info("Rules number in the ruleSet: "
        + loadedRuleSet.getRules().size());
    inputStream.close();

    return loadedRuleSet;
  }

  private void writeAnyReport(final FlexPmdViolations flexPmdViolations) throws PMDException
  {
    long startTime;
    long ellapsedTime;
    startTime = System.currentTimeMillis();
    writeReport(flexPmdViolations);
    ellapsedTime = System.currentTimeMillis()
        - startTime;

    LOGGER.info("It took "
        + ellapsedTime + "ms to write the " + getReportType() + " report");
  }
}
