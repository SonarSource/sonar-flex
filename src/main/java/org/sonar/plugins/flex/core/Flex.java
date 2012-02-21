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

package org.sonar.plugins.flex.core;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.api.resources.Directory;
import org.sonar.api.resources.File;
import org.sonar.plugins.flex.FlexPlugin;

/**
 * Class that actually represents ActionScript, the language of Flex.
 */
public class Flex extends AbstractLanguage {

  public static final String NAME = "Flex";
  public static final String KEY = "flex";

  public static final String DEFAULT_FILE_SUFFIXES = "as";

  private Configuration configuration;

  /**
   * Creates the {@link Flex} language.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   * 
   * @param configuration the configuration
   */
  public Flex(Configuration configuration) {
    super(KEY, NAME);
    this.configuration = configuration;
  }

  /**
   * {@inheritDoc}
   */
  public String[] getFileSuffixes() {
    String[] suffixes = configuration.getStringArray(FlexPlugin.FILE_SUFFIXES_KEY);
    if (suffixes == null || suffixes.length == 0) {
      suffixes = StringUtils.split(DEFAULT_FILE_SUFFIXES, ",");
    }
    return suffixes;
  }

  /**
   * Returns the corresponding Sonar File given the name of an ActionScript class or class+method.
   * 
   * For instance:
   * <ul>
   * <li>com.adobe.air.crypto.EncryptionKeyGenerator => File:com/adobe/air/crypto/EncryptionKeyGenerator</li>
   * <li>com.adobe.air.crypto.EncryptionKeyGenerator::generateEncryptionKey => File:com/adobe/air/crypto/EncryptionKeyGenerator</li>
   * </ul>
   * 
   * @param actionScriptClassName the name of the class
   * @return the corresponding Sonar file
   */
//  public static File getFileFromLogicalName(String actionScriptName) {
//    return new File(StringUtils.substringBefore(actionScriptName, "::").replace('.', '/') + ".as");
//  }

  /**
   * Returns the corresponding Sonar Directory given the name of an ActionScript package.
   * 
   * For instance:
   * <ul>
   * <li>com.adobe.air.crypto => Directory:com/adobe/air/crypto</li>
   * </ul>
   * 
   * @param actionScriptClassName the name of the package
   * @return the corresponding Sonar directory
   */
//  public static Directory getDirFromLogicalName(String actionScriptName) {
//    return new Directory(actionScriptName.replace('.', '/'));
//  }

}
