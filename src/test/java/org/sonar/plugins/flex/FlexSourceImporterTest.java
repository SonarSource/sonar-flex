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

package org.sonar.plugins.flex;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.resources.Resource;
import org.sonar.api.resources.ResourceUtils;

import java.io.File;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Evgeny Mandrikov
 */
public class FlexSourceImporterTest {

  private FlexSourceImporter importer;

  @Before
  public void setUp() throws Exception {
    importer = new FlexSourceImporter(Flex.INSTANCE);
  }

  @Test
  public void shouldCreateResource() {
    Resource resource = importer.createResource(new File(newDir("source1"), "/ArrayUtils.as"), Arrays.asList(newDir("source1")), false);
    assertThat(resource, is(FlexFile.class));
    assertThat(ResourceUtils.isUnitTestClass(resource), is(false));
    assertThat(resource.getKey(), is(FlexPackage.DEFAULT_PACKAGE_NAME + ".ArrayUtils"));
    assertThat(resource.getName(), is("ArrayUtils"));
  }

  @Test
  public void shouldCreateTestResource() {
    Resource resource = importer.createResource(new File(newDir("tests"), "/ArrayUtilsTest.as"), Arrays.asList(newDir("tests")), true);
    assertThat(resource, is(FlexFile.class));
    assertThat(ResourceUtils.isUnitTestClass(resource), is(true));
  }

  private File newDir(String relativePath) {
    return new File("target", relativePath);
  }

}
