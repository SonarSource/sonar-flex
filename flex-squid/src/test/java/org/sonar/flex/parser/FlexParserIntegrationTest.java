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
package org.sonar.flex.parser;

import com.sonar.sslr.impl.Parser;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.sonar.flex.api.FlexGrammar;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class FlexParserIntegrationTest {

  private final File file;
  private final Parser<FlexGrammar> parser = FlexParser.create();

  public FlexParserIntegrationTest(File file) {
    this.file = file;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> data() throws URISyntaxException {
    Collection<Object[]> parameters = new ArrayList<Object[]>();
    addParametersForPath(parameters, "/integration/");
    return parameters;
  }

  @Test
  public void parse() {
    try {
      parser.parse(file);
    } catch (Exception e) {
      throw new RuntimeException(file.toString(), e);
    }
  }

  protected static void addParametersForPath(Collection<Object[]> parameters, String path) throws URISyntaxException {
    Collection<File> files;
    files = listFiles(path, true);
    for (File file : files) {
      parameters.add(new Object[] {file});
    }
  }

  private static Collection<File> listFiles(String path, boolean recursive) throws URISyntaxException {
    return FileUtils.listFiles(new File("src/test/resources"), new String[] {"as"}, recursive);
  }

}
