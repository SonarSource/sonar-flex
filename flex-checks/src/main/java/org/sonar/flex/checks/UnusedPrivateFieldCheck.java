/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2024 SonarSource SA
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
import java.text.MessageFormat;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Variable;

@Rule(key = "S1068")
public class UnusedPrivateFieldCheck extends FlexCheck {

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
      this.privateFields = new HashMap<>();
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
      }
    }
  }


  private Deque<ClassState> classStack = new ArrayDeque<>();

  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
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
        addIssue(MessageFormat.format("Remove this unused ''{0}'' private field", entry.getKey()), entry.getValue().declaration);
      }
    }
  }
}
