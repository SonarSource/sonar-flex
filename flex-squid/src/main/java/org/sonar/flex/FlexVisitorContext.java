/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2022 SonarSource SA
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
import com.sonar.sslr.api.RecognitionException;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public class FlexVisitorContext {

  private final String fileContent;
  private final AstNode rootTree;
  private final RecognitionException parsingException;

  public FlexVisitorContext(String fileContent, AstNode tree) {
    this(fileContent, tree, null);
  }

  public FlexVisitorContext(String fileContent, RecognitionException parsingException) {
    this(fileContent, null, parsingException);
  }

  private FlexVisitorContext(String fileContent, @Nullable AstNode rootTree, @Nullable RecognitionException parsingException) {
    this.fileContent = fileContent;
    this.rootTree = rootTree;
    this.parsingException = parsingException;
  }

  @CheckForNull
  public AstNode rootTree() {
    return rootTree;
  }

  public RecognitionException parsingException() {
    return parsingException;
  }

  public String fileContent() {
    return fileContent;
  }
}
