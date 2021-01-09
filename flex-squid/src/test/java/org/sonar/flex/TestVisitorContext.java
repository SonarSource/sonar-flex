/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.flex;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.impl.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.sonar.flex.parser.FlexParser;
import org.sonar.sslr.parser.LexerlessGrammar;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestVisitorContext {

  public static FlexVisitorContext create(File file) {
    String fileContent;
    try {
      fileContent =  new String(Files.readAllBytes(Paths.get(file.getPath())), UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Cannot read " + file, e);
    }
    Parser<LexerlessGrammar> parser = FlexParser.create(UTF_8);
    AstNode root = parser.parse(fileContent);
    return new FlexVisitorContext(fileContent, root);
  }

}
