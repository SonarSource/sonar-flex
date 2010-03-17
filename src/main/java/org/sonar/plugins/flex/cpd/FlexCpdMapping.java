/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
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

package org.sonar.plugins.flex.cpd;

import org.sonar.api.batch.CpdMapping;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Resource;
import org.sonar.plugins.flex.Flex;
import org.sonar.plugins.flex.FlexFile;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.JavaTokenizer;

import java.io.File;
import java.util.List;

public class FlexCpdMapping implements CpdMapping {
  public Tokenizer getTokenizer() {
    return new JavaTokenizer();
  }

  public Language getLanguage() {
    return Flex.INSTANCE;
  }

  public Resource createResource(File file, List<File> sourceDirs) {
    return FlexFile.fromIOFile(file, sourceDirs);
  }
}
