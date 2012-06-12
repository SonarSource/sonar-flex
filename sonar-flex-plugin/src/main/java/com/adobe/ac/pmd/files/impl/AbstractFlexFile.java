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
package com.adobe.ac.pmd.files.impl;

import com.adobe.ac.pmd.files.IFlexFile;
import com.adobe.ac.utils.StackTraceUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Abstract class representing a Flex File (either MXML or AS)
 *
 * @author xagnetti
 */
abstract class AbstractFlexFile implements IFlexFile {
  private static final Logger LOGGER = Logger.getLogger(AbstractFlexFile.class.getName());

  /**
   * @param filePath
   * @param rootPath
   * @param className
   * @param fileSeparator
   * @return
   */
  protected static String computePackageName(final String filePath,
      final CharSequence rootPath,
      final String className,
      final String fileSeparator)
  {
    String temporaryPackage;

    temporaryPackage = filePath.replace(className,
        "").replace(rootPath,
        "").replace(fileSeparator,
        ".");
    if (temporaryPackage.endsWith("."))
    {
      temporaryPackage = temporaryPackage.substring(0,
          temporaryPackage.length() - 1);
    }
    if (temporaryPackage.length() > 0
        && temporaryPackage.charAt(0) == '.')
    {
      temporaryPackage = temporaryPackage.substring(1,
          temporaryPackage.length());
    }
    return temporaryPackage;
  }

  private static boolean doesCurrentLineContain(final String line,
      final String search)
  {
    return line.contains(search);
  }

  private final String className;
  private final File file;
  private final List<String> lines;
  private final String packageName;

  /**
   * @param underlyingFile
   * @param rootDirectory
   */
  protected AbstractFlexFile(final File underlyingFile,
      final File rootDirectory)
  {
    final String filePath = underlyingFile.getPath();
    final CharSequence rootPath = rootDirectory == null ? ""
        : rootDirectory.getPath();

    file = underlyingFile;
    className = underlyingFile.getName();
    packageName = computePackageName(filePath,
        rootPath,
        className,
        System.getProperty("file.separator"));
    lines = new ArrayList<String>();
    try
    {
      String[] linesArray;
      linesArray = FileUtils.readLines(underlyingFile);
      for (final String string : linesArray)
      {
        lines.add(string);
      }
    } catch (final IOException e)
    {
      LOGGER.warning(StackTraceUtils.print(e));
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#contains(java.lang.String, int)
   */
  public final boolean contains(final String stringToLookup,
      final Set<Integer> linesToBeIgnored)
  {
    int lineIndex = 1;
    boolean found = false;

    for (final String line : lines)
    {
      if (doesCurrentLineContain(line,
          stringToLookup)
          && !linesToBeIgnored.contains(lineIndex))
      {
        found = true;
        break;
      }
      lineIndex++;
    }
    return found;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(final Object obj)
  {
    if (obj == null || obj.getClass() != getClass()) {
      return false;
    }
    AbstractFlexFile other = (AbstractFlexFile) obj;
    return this.file.equals(other.file);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getClassName()
   */
  public final String getClassName()
  {
    return className;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getCommentClosingTag()
   */
  public abstract String getCommentClosingTag();

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getCommentOpeningTag()
   */
  public abstract String getCommentOpeningTag();

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getFilename()
   */
  public final String getFilename()
  {
    return file.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getFilePath()
   */
  public final String getFilePath()
  {
    return file.toURI().getPath();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getFullyQualifiedName()
   */
  public final String getFullyQualifiedName()
  {
    return (StringUtils.isEmpty(packageName) ? ""
        : packageName
            + ".")
        + className;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getLineAt(int)
   */
  public String getLineAt(final int lineIndex)
  {
    return lines.get(lineIndex - 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getLines()
   */
  public final List<String> getLines()
  {
    return lines;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getLinesNb()
   */
  public int getLinesNb()
  {
    return lines.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getPackageName()
   */
  public final String getPackageName()
  {
    return packageName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return getFilePath().hashCode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#isMainApplication()
   */
  public abstract boolean isMainApplication();

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#isMxml()
   */
  public abstract boolean isMxml();
}
