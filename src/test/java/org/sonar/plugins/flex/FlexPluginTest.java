/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2010 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */

package org.sonar.plugins.flex;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class FlexPluginTest {
  private FlexPlugin plugin;

  @Before
  public void setUp() throws Exception {
    plugin = new FlexPlugin();
  }

  @Test
  public void testGetKey() {
    assertThat("the flexpmd rules engine and the flex plugin must have the same key due to SONAR-1342", plugin.getKey(), is("flexplugin"));
  }

  @Test
  public void testGetExtensions() throws Exception {
    assertThat(plugin.getExtensions().size(), is(12));
  }
}
