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

import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

@Rule(
  key = "S1068",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class UnusedPrivateFieldCheck extends SquidCheck<LexerlessGrammar> {

  private static class PrivateField {
    final AstNode declaration;
    int usages;

    public PrivateField(AstNode declaration, int usages) {
      this.declaration = declaration;
      this.usages = usages;
    }
  }

  private static class ClassState {
    private final Map<String, PrivateField> privateFields;

    public ClassState(AstNode classDef) {
      this.privateFields = Maps.newHashMap();
      retrieveAllPrivateFields(classDef);
    }

    private void retrieveAllPrivateFields(AstNode classDef) {
      for (AstNode varDeclaration : Clazz.getFields(classDef)) {
        if (Modifiers.getModifiers(varDeclaration.getParent().getPreviousAstNode()).contains(FlexKeyword.PRIVATE)) {
          for (AstNode identifier : Variable.getDeclaredIdentifiers(varDeclaration)) {

            privateFields.put(identifier.getTokenValue(), new PrivateField(identifier, 0));
          }
        }
      }
    }

    private void use(AstNode astNode) {
      PrivateField field = privateFields.get(astNode.getTokenValue());

      if (field != null) {
        field.usages++;
        return;
      }
    }
  }


  private Deque<ClassState> classStack = new ArrayDeque<ClassState>();

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.QUALIFIED_IDENTIFIER);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    classStack.clear();
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classStack.push(new ClassState(astNode));
    } else if (!classStack.isEmpty() && astNode.is(FlexGrammar.QUALIFIED_IDENTIFIER)) {
      classStack.peek().use(astNode);
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      reportUnusedPrivateField();
    }
  }

  private void reportUnusedPrivateField() {
    for (Map.Entry<String, PrivateField> entry : classStack.pop().privateFields.entrySet()) {
      if (entry.getValue().usages == 0) {
        getContext().createLineViolation(this, "Remove this unused ''{0}'' private field", entry.getValue().declaration, entry.getKey());
      }
    }
  }
}
