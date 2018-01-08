/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2018 SonarSource SA
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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;
import java.util.Set;

@Rule(
  key = "S1784",
  name = "Method visibility should be explicitly declared",
  priority = Priority.MINOR,
  tags = Tags.CONVENTION)
@SqaleConstantRemediation("2min")
public class MethodVisibilityCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    List<AstNode> directives = astNode.getFirstChild(FlexGrammar.BLOCK).getFirstChild(FlexGrammar.DIRECTIVES).getChildren();
    if (directives == null) {
      return;
    }

    for (AstNode directive : directives) {
      AstNode annotableDirective = directive.getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE);

      if (annotableDirective != null) {
        AstNode annotableDirectiveChild = annotableDirective.getFirstChild();

        if (annotableDirectiveChild.is(FlexGrammar.FUNCTION_DEF) && !hasVisibility(annotableDirectiveChild)) {
          getContext().createLineViolation(this, "Explicitly declare the visibility of this method \"{0}\".", annotableDirectiveChild,
            Function.getName(annotableDirectiveChild));
        }
      }
    }
  }

  private static boolean hasVisibility(AstNode functionDef) {
    Set<AstNodeType> modifiers = Modifiers.getModifiers(functionDef.getPreviousAstNode());

    for (AstNodeType modifier : modifiers) {
      if (isVisibility(modifier)) {
        return true;
      }
    }

    return false;
  }

  private static boolean isVisibility(AstNodeType modifier) {
    return modifier.equals(FlexKeyword.PUBLIC) || modifier.equals(FlexKeyword.INTERNAL)
      || modifier.equals(FlexKeyword.PROTECTED) || modifier.equals(FlexKeyword.PRIVATE);
  }

}
