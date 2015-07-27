/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010 SonarSource
 * sonarqube@googlegroups.com
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
package org.sonar.flex.checks;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Trivia;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.asdoc.ASDocClassCheck;
import org.sonar.flex.checks.asdoc.ASDocMemberCheck;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;

@Rule(
  key = "S1176",
  priority = Priority.MAJOR)
public class ASDocCheck extends SquidCheck<LexerlessGrammar> {

  public static final String INHERIT_TAG = "@inheritDoc";
  public static final String PRIVATE_TAG = "@private";

  private static final boolean DEFAULT_CLASSES = true;
  private static final boolean DEFAULT_PROPERTIES = true;
  private static final boolean DEFAULT_METHODS = true;
  private static final boolean DEFAULT_METHOD_PARAMS = true;
  private static final boolean DEFAULT_METHOD_RETURN = true;

  @RuleProperty(
    key = "asdoc_classes",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_CLASSES)
  public boolean classes = DEFAULT_CLASSES;

  @RuleProperty(
    key = "asdoc_properties",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_PROPERTIES)
  public boolean properties = DEFAULT_PROPERTIES;

  @RuleProperty(
    key = "asdoc_methods",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_METHODS)
  public boolean methods = DEFAULT_METHODS;

  @RuleProperty(
    key = "asdoc_method_param",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_METHOD_PARAMS)
  public boolean methodParams = DEFAULT_METHOD_PARAMS;

  @RuleProperty(
    key = "asdoc_method_return",
    type = "BOOLEAN",
    defaultValue = "" + DEFAULT_METHOD_RETURN)
  public boolean methodReturn = DEFAULT_METHOD_RETURN;

  private ASDocClassCheck classChecker = new ASDocClassCheck();
  private ASDocMemberCheck memberChecker = new ASDocMemberCheck();

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
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
