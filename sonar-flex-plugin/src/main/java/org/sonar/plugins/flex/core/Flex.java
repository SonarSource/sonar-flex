/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2025 SonarSource Sàrl
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the Sonar Source-Available License Version 1, as published by SonarSource SA.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the Sonar Source-Available License for more details.
 *
 * You should have received a copy of the Sonar Source-Available License
 * along with this program; if not, see https://sonarsource.com/license/ssal/
 */
package org.sonar.plugins.flex.core;

import java.util.ArrayList;
import java.util.List;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;
import org.sonar.plugins.flex.FlexPlugin;

/**
 * Class that actually represents ActionScript, the language of Flex.
 */
public class Flex extends AbstractLanguage {

  public static final String NAME = "Flex";
  public static final String KEY = "flex";

  public static final String DEFAULT_FILE_SUFFIXES = "as";

  private final Configuration config;

  /**
   * Creates the {@link Flex} language.
   * <br/>
   * <b>Do not call, this constructor is called by Pico container.</b>
   */
  public Flex(Configuration config) {
    super(KEY, NAME);
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = filterEmptyStrings(config.getStringArray(FlexPlugin.FILE_SUFFIXES_KEY));
    if (suffixes.length == 0) {
      suffixes = new String[] { DEFAULT_FILE_SUFFIXES };
    }
    return suffixes;
  }

  private static String[] filterEmptyStrings(String[] stringArray) {
    List<String> nonEmptyStrings = new ArrayList<>();
    for (String string : stringArray) {
      if (string != null && !string.trim().isEmpty()) {
        nonEmptyStrings.add(string.trim());
      }
    }
    return nonEmptyStrings.toArray(new String[nonEmptyStrings.size()]);
  }

}
