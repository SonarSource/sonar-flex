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
package com.adobe.ac.pmd.files.impl;

import com.adobe.ac.ncss.filters.FlexFilter;
import com.adobe.ac.pmd.files.IFlexFile;
import net.sourceforge.pmd.PMDException;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author xagnetti
 */
public final class FileUtils
{
  /**
   * @param source
   * @param sourceList
   * @param packageToExclude
   * @param excludePatterns
   * @return
   * @throws PMDException
   */
  public static Map<String, IFlexFile> computeFilesList(final File source,
      final List<File> sourceList,
      final String packageToExclude,
      final List<String> excludePatterns) throws PMDException
  {
    final Map<String, IFlexFile> files = new LinkedHashMap<String, IFlexFile>();
    final FlexFilter flexFilter = new FlexFilter();
    final Collection<File> foundFiles = getFlexFiles(source,
        sourceList,
        flexFilter);

    for (final File sourceFile : foundFiles)
    {
      final AbstractFlexFile file = create(sourceFile,
          source);

      if (("".equals(packageToExclude) || !file.getFullyQualifiedName().startsWith(packageToExclude))
          && !currentPackageIncludedInExcludePatterns(file.getFullyQualifiedName(),
              excludePatterns))
      {
        files.put(file.getFullyQualifiedName(),
            file);
      }
    }

    return files;
  }

  /**
   * @param sourceFile
   * @param sourceDirectory
   * @return
   */
  public static AbstractFlexFile create(final File sourceFile,
      final File sourceDirectory)
  {
    AbstractFlexFile file;

    if (sourceFile.getName().endsWith(".as"))
    {
      file = new As3File(sourceFile, sourceDirectory);
    }
    else
    {
      file = new MxmlFile(sourceFile, sourceDirectory);
    }

    return file;
  }

  /**
   * @param file
   * @return
   * @throws IOException
   */
  public static String[] readLines(final File file) throws IOException
  {
    final List<String> lines = com.adobe.ac.ncss.utils.FileUtils.readFile(file);

    return lines.toArray(new String[lines.size()]);
  }

  private static boolean currentPackageIncludedInExcludePatterns(final String fullyQualifiedName,
      final List<String> excludePatterns)
  {
    if (excludePatterns != null)
    {
      for (final String excludePattern : excludePatterns)
      {
        if (fullyQualifiedName.startsWith(excludePattern))
        {
          return true;
        }
      }
    }
    return false;
  }

  private static Collection<File> getFlexFiles(final File source,
      final List<File> sourceList,
      final FlexFilter flexFilter) throws PMDException
  {
    if (source == null
        && sourceList == null)
    {
      throw new PMDException("sourceDirectory is not specified", null);
    }
    Collection<File> foundFiles;
    if (source == null)
    {
      foundFiles = com.adobe.ac.ncss.utils.FileUtils.listFiles(sourceList,
          flexFilter,
          true);
    }
    else
    {
      if (source.isDirectory())
      {
        foundFiles = com.adobe.ac.ncss.utils.FileUtils.listFiles(source,
            flexFilter,
            true);
      }
      else
      {
        foundFiles = new ArrayList<File>();
        foundFiles.add(source);
      }
    }
    if (foundFiles.isEmpty())
    {
      return new ArrayList<File>();
    }
    return foundFiles;
  }

  private FileUtils()
  {
  }
}
