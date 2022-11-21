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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Trivia;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.asdoc.ASDocClassCheck;
import org.sonar.flex.checks.asdoc.ASDocMemberCheck;
import org.sonar.flex.checks.utils.Clazz;

@Rule(key = "S1176")
public class ASDocCheck extends FlexCheck {

  public static final String INHERIT_TAG = "@inheritDoc";
  public static final String PRIVATE_TAG = "@private";

  private static final boolean DEFAULT_CLASSES = true;
  private static final boolean DEFAULT_PROPERTIES = true;
  private static final boolean DEFAULT_METHODS = true;
  private static final boolean DEFAULT_METHOD_PARAMS = true;
  private static final boolean DEFAULT_METHOD_RETURN = true;

  @RuleProperty(
    key = "asdoc_classes",
    description = "Public classes should be documented",
    defaultValue = "" + DEFAULT_CLASSES,
    type = "BOOLEAN")
  public boolean classes = DEFAULT_CLASSES;

  @RuleProperty(
    key = "asdoc_properties",
    description = "Public properties should be documented",
    defaultValue = "" + DEFAULT_PROPERTIES,
    type = "BOOLEAN")
  public boolean properties = DEFAULT_PROPERTIES;

  @RuleProperty(
    key = "asdoc_methods",
    description = "Public methods should be documented",
    defaultValue = "" + DEFAULT_METHODS,
    type = "BOOLEAN")
  public boolean methods = DEFAULT_METHODS;

  @RuleProperty(
    key = "asdoc_method_param",
    description = "All parameters of public methods should be documented",
    defaultValue = "" + DEFAULT_METHOD_PARAMS,
    type = "BOOLEAN")
  public boolean methodParams = DEFAULT_METHOD_PARAMS;

  @RuleProperty(
    key = "asdoc_method_return",
    description = "All return type of public methods should be documented",
    defaultValue = "" + DEFAULT_METHOD_RETURN,
    type = "BOOLEAN")
  public boolean methodReturn = DEFAULT_METHOD_RETURN;

  private ASDocClassCheck classChecker = new ASDocClassCheck();
  private ASDocMemberCheck memberChecker = new ASDocMemberCheck();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    boolean hasPrivateTag = classChecker.visitNode(this, astNode);

    if (!hasPrivateTag) {
      memberChecker.visitNode(this, Clazz.getDirectives(astNode));
    }
  }

  public boolean hasASDoc(List<Trivia> trivia) {
    String value;

    for (Trivia comment : trivia) {
      value = comment.getToken().getValue().trim();
      if (value.startsWith("/**") && value.endsWith("*/")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if comment contains one of the string given as parameters.
   */
  public boolean containsOnOfTags(List<Trivia> trivia, String...tags) {
    for (Trivia comment : trivia) {
      String value = comment.getToken().getValue();
      for (String tag : tags) {
        if (value.contains(tag)) {
          return true;
        }
      }
    }
    return false;
  }
}
