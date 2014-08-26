/*
 * SonarQube Flex Plugin
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
package org.sonar.flex.checks;

import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Function;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

@Rule(key = "S1117",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class LocalVarShadowsFieldCheck extends SquidCheck<LexerlessGrammar> {


  private static class ClassState {
    private final Map<String, AstNode> classFields;
    private final String className;

    public ClassState(AstNode classDef) {
      className = Clazz.getName(classDef);
      this.classFields = Maps.newHashMap();
      initFieldsMap(classDef);
    }

    private void initFieldsMap(AstNode classDef) {
      for (AstNode varDeclStatement : Clazz.getFields(classDef)) {

        for (AstNode identifier : Variable.getDeclaredIdentifiers(varDeclStatement)) {
          classFields.put(identifier.getTokenValue(), identifier);
        }
      }
    }

    public AstNode getFieldNamed(String name) {
      return classFields.get(name);
    }

    public String getClassName() {
      return this.className;
    }
  }

  private Deque<ClassState> classStack = new ArrayDeque<ClassState>();
  private int functionNestedLevel;
  private static final String MESSAGE = "Rename \"{0}\" which hides the field declared at line {1}.";

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    classStack.clear();
    functionNestedLevel = 0;
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(FlexGrammar.CLASS_DEF)) {
      classStack.push(new ClassState(node));
    } else if (isClassFunctionNotConstructor(node) && !isAccessor(node) && !isStatic(node)) {
      functionNestedLevel++;
      checkParameters(node);
    } else if (!classStack.isEmpty() && functionNestedLevel > 0 && node.is(FlexGrammar.VARIABLE_DECLARATION_STATEMENT)) {
      checkVariableNames(node);
    }
  }

  private boolean isClassFunctionNotConstructor(AstNode node) {
    return !classStack.isEmpty() && node.is(FlexGrammar.FUNCTION_DEF)
      && !Function.isConstructor(node, classStack.peek().getClassName());
  }

  private boolean isStatic(AstNode functionDef) {
    return Modifiers.getModifiers(functionDef.getParent().getPreviousAstNode()).contains(FlexKeyword.STATIC);
  }

  private boolean isAccessor(AstNode functionDef) {
    String functionName = Function.getName(functionDef);
    return Function.isAccessor(functionDef)
      || functionName.length() > 2 && "set".equals(functionName.substring(0, 3));
  }

  private void checkVariableNames(AstNode varDeclStatement) {
    for (AstNode identifier : Variable.getDeclaredIdentifiers(varDeclStatement)) {
      String varName = identifier.getTokenValue();
      AstNode field = classStack.peek().getFieldNamed(varName);

      if (field != null) {
        getContext().createLineViolation(this, MESSAGE, identifier, varName, field.getToken().getLine());
      }
    }
  }

  private void checkParameters(AstNode functionDef) {
    for (AstNode paramIdentifier : Function.getParametersIdentifiers(functionDef)) {
      String paramName = paramIdentifier.getTokenValue();
      AstNode field = classStack.peek().getFieldNamed(paramName);

      if (field != null) {
        getContext().createLineViolation(this, MESSAGE, paramIdentifier, paramName, field.getToken().getLine());
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classStack.pop();
    } else if (!classStack.isEmpty() && astNode.is(FlexGrammar.FUNCTION_DEF) && functionNestedLevel > 0) {
      functionNestedLevel--;
    }
  }
}
