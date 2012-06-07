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
package com.adobe.ac.pmd.files;

import java.util.Set;

/**
 * @author xagnetti
 */
public interface IFlexFile {
  /**
   * @param stringToLookup
   * @param linesToBeIgnored
   * @return
   */
  boolean contains(final String stringToLookup,
      final Set<Integer> linesToBeIgnored);

  /**
   * @return
   */
  String getClassName();

  /**
   * @return the token for comment closing
   */
  String getCommentClosingTag();

  /**
   * @return the token for comment opening
   */
  String getCommentOpeningTag();

  /**
   * @return java.io.File name
   */
  String getFilename();

  /**
   * @return java.io.File absolute path
   */
  String getFilePath();

  /**
   * @return
   */
  String getFullyQualifiedName();

  /**
   * @param lineIndex
   * @return
   */
  String getLineAt(int lineIndex);

  /**
   * @return
   */
  int getLinesNb();

  /**
   * @return
   */
  String getPackageName();

  /**
   * @return the token for one line comment
   */
  String getSingleLineComment();

  /**
   * @return true if the file is a main MXML file
   */
  boolean isMainApplication();

  /**
   * @return true if the file is a MXML file
   */
  boolean isMxml();
}
