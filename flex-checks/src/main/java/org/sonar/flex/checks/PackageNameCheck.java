/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2023 SonarSource SA
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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

@Rule(key = "S120")
public class PackageNameCheck extends FlexCheck {

  private static final String DEFAULT = "^[a-z]+(\\.[a-z][a-z0-9]*)*$";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the package names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.PACKAGE_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    AstNode nameNode = astNode.getFirstChild(FlexGrammar.PACKAGE_NAME);
    if (nameNode != null) {
      String packageIdentifier = concatenate(nameNode);
      if (!pattern.matcher(packageIdentifier).matches()) {
        addIssue(MessageFormat.format("Rename this package name to match the regular expression {0}", format), astNode);
      }
    }
  }

  private static String concatenate(AstNode astNode) {
    StringBuilder sb = new StringBuilder();
    for (Token token : astNode.getTokens()) {
      sb.append(token.getValue());
    }
    return sb.toString();
  }
}
