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

import com.adobe.ac.pmd.files.IMxmlFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xagnetti
 */
class MxmlFile extends AbstractFlexFile implements IMxmlFile
{
  private static final Pattern METADATA_REGEX = Pattern.compile("<\\s*/?\\s*(fx:|mx:)?Metadata\\s*>", Pattern.MULTILINE);
  private static final Pattern SCRIPT_REGEX = Pattern.compile("<\\s*/?\\s*(fx:|mx:)?Script\\s*>", Pattern.MULTILINE);
  private String[] actualScriptBlock;
  private int endLine;
  private boolean mainApplication = false;
  private String[] scriptBlock;
  private int startLine;

  /**
   * @param file
   * @param rootDirectory
   */
  protected MxmlFile(final File file,
      final File rootDirectory)
  {
    super(file, rootDirectory);

    computeIfIsMainApplication();
    if (getLinesNb() > 0)
    {
      extractScriptBlock();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IMxmlFile#getActualScriptBlock()
   */
  public final String[] getActualScriptBlock()
  {
    return actualScriptBlock; // NOPMD
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IMxmlFile#getBeginningScriptBlock()
   */
  public int getBeginningScriptBlock()
  {
    return startLine;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#getCommentClosingTag()
   */
  @Override
  public final String getCommentClosingTag()
  {
    return "-->";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#getCommentOpeningTag()
   */
  @Override
  public final String getCommentOpeningTag()
  {
    return "<!--";
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IMxmlFile#getEndingScriptBlock()
   */
  public int getEndingScriptBlock()
  {
    return endLine;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IMxmlFile#getScriptBlock()
   */
  public final String[] getScriptBlock()
  {
    return scriptBlock; // NOPMD by xagnetti on 7/7/09 3:15 PM
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.IFlexFile#getSingleLineComment()
   */
  public String getSingleLineComment()
  {
    return getCommentOpeningTag();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#isMainApplication()
   */
  @Override
  public final boolean isMainApplication()
  {
    return mainApplication;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#isMxml()
   */
  @Override
  public final boolean isMxml()
  {
    return true;
  }

  private void computeIfIsMainApplication()
  {
    for (final String line : getLines())
    {
      if (line.contains("Application ")
          && line.charAt(0) == '<')
      {
        mainApplication = true;
        break;
      }
    }
  }

  private int computeScriptOffSet(final int startingLineIndex)
  {
    int currentLineIndex = startingLineIndex + 1;
    while (getLines().get(currentLineIndex).contains("CDATA[")
        || getLines().get(currentLineIndex).contains("//") || containsCloseComment(currentLineIndex)
        || getLines().get(currentLineIndex).trim().equals(""))
    {
      currentLineIndex++;
    }
    return currentLineIndex
        - startingLineIndex;
  }

  private boolean containsCloseComment(final int currentLineIndex)
  {
    final boolean closedAsComment = getLines().get(currentLineIndex).contains("/*")
        && getLines().get(currentLineIndex).contains("*/");
    final boolean closeMxmlComment = getLines().get(currentLineIndex).contains("<!--")
        && getLines().get(currentLineIndex).contains("-->");
    return closedAsComment
        || closeMxmlComment;
  }

  private void copyScriptLinesKeepingOriginalLineIndices()
  {
    final List<String> scriptLines = extractScriptLines();
    final List<String> metaDataLines = extractMetaDataLines();
    final String packageLine = "package "
        + getPackageName() + "{";
    final String classLine = "class "
        + getClassName().split("\\.")[0] + "{";

    scriptLines.set(0,
        packageLine);

    if (metaDataLines.isEmpty()
        || metaDataLines.get(0).compareTo("HostComponent") == 0)
    {
      if (scriptLines.size() > 1)
      {
        scriptLines.set(1,
            classLine);
      }
    }
    else
    {
      final int firstMetaDataLine = getFirstMetaDataLine(getLines());

      for (int i = firstMetaDataLine; i < firstMetaDataLine
          + metaDataLines.size(); i++)
      {
        scriptLines.set(i,
            metaDataLines.get(i
                - firstMetaDataLine));
      }
      scriptLines.set(firstMetaDataLine
          + metaDataLines.size(),
          classLine);
    }

    scriptLines.set(scriptLines.size() - 1,
        "}}");
    scriptBlock = scriptLines.toArray(new String[scriptLines.size()]);
  }

  private List<String> extractMetaDataLines()
  {
    final ArrayList<String> metaDataLines = new ArrayList<String>();
    int currentLineIndex = 0;
    int start = 0;
    int end = 0;

    for (final String line : getLines())
    {
      if (isMetaDataLine(line))
      {
        if (line.contains("</"))
        {
          end = currentLineIndex
              - (getLines().get(currentLineIndex - 1).contains("]]>") ? 1
                  : 0);
          if (line.contains("<fx")
              || line.contains("<mx"))
          {
            start = end;
          }
          break;
        }
        if (line.contains("<"))
        {
          start = currentLineIndex
              + (getLines().get(currentLineIndex + 1).contains("CDATA[") ? 2
                  : 1);
        }
      }
      currentLineIndex++;
    }
    metaDataLines.addAll(getLines().subList(start,
        end));
    return metaDataLines;
  }

  private void extractScriptBlock()
  {
    int currentLineIndex = 0;
    startLine = 0;
    endLine = 0;

    for (final String line : getLines())
    {
      if (isScriptLine(line))
      {
        if (line.contains("</"))
        {
          endLine = currentLineIndex
              - (getLines().get(currentLineIndex - 1).contains("]]>") ? 1
                  : 0);
          break;
        }
        else if (line.contains("<"))
        {
          startLine = currentLineIndex
              + computeScriptOffSet(currentLineIndex);
        }
      }
      currentLineIndex++;
    }

    copyScriptLinesKeepingOriginalLineIndices();
  }

  private List<String> extractScriptLines()
  {
    final List<String> scriptLines = new ArrayList<String>();

    for (int j = 0; j < startLine; j++)
    {
      scriptLines.add("");
    }
    if (startLine < endLine)
    {
      actualScriptBlock = getLines().subList(startLine,
          endLine).toArray(new String[endLine
          - startLine]);
      scriptLines.addAll(new ArrayList<String>(getLines().subList(startLine,
          endLine)));
    }
    for (int j = endLine; j < getLines().size(); j++)
    {
      scriptLines.add("");
    }
    return scriptLines;
  }

  private int getFirstMetaDataLine(final List<String> lines)
  {
    for (int i = 0; i < lines.size(); i++)
    {
      final String line = lines.get(i);

      if (isMetaDataLine(line))
      {
        return i;
      }
    }
    return 0;
  }

  private boolean isScriptLine(String line)
  {
    Matcher matcher = SCRIPT_REGEX.matcher(line);
    return matcher.find();
  }

  private boolean isMetaDataLine(String line)
  {
    Matcher matcher = METADATA_REGEX.matcher(line);
    return matcher.find();
  }

}
