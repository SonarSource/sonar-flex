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

import com.adobe.ac.pmd.files.IAs3File;

import java.io.File;

/**
 * @author xagnetti
 */
class As3File extends AbstractFlexFile implements IAs3File {
  /**
   * @param file
   * @param rootDirectory
   */
  protected As3File(final File file,
      final File rootDirectory)
  {
    super(file, rootDirectory);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#getCommentClosingTag()
   */
  @Override
  public final String getCommentClosingTag()
  {
    return "*/";
  }

  /*
   * (non-Javadoc)
   *
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#getCommentOpeningTag()
   */
  @Override
  public final String getCommentOpeningTag()
  {
    return "/*";
  }

  /*
   * (non-Javadoc)
   *
   * @see com.adobe.ac.pmd.files.IFlexFile#getSingleLineComment()
   */
  public String getSingleLineComment()
  {
    return "//";
  }

  /*
   * (non-Javadoc)
   *
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#isMainApplication()
   */
  @Override
  public final boolean isMainApplication()
  {
    return false;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.adobe.ac.pmd.files.impl.AbstractFlexFile#isMxml()
   */
  @Override
  public final boolean isMxml()
  {
    return false;
  }
}
