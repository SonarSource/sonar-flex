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

package org.sonar.plugins.flex.cpd;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Evgeny Mandrikov
 */
public class FlexCpdMavenPluginHandlerTest {
  private FlexCpdMavenPluginHandler handler;

  @Before
  public void setUp() throws Exception {
    handler = new FlexCpdMavenPluginHandler(null);
  }

  @Test
  public void fixedVersion() throws Exception {
    assertThat(handler.isFixedVersion(), is(true));
  }

  @Test
  public void pluginDefinition() throws Exception {
    assertThat(handler.getGroupId(), is("com.adobe.ac"));
    assertThat(handler.getArtifactId(), is("flex-pmd-cpd-maven-plugin"));
    assertThat(handler.getVersion(), is("1.2"));
    assertThat(handler.getGoals(), is(new String[] { "check" }));
  }
}
