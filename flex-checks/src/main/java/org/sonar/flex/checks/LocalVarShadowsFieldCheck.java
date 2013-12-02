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
import com.sonar.sslr.squid.checks.SquidCheck;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Stack;

@Rule(key = "S1117",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class LocalVarShadowsFieldCheck extends SquidCheck<LexerlessGrammar> {


  private static class ClassState {
    private final Map<String, AstNode> classFields;
    public final String className;

    public ClassState(AstNode classDef) {
      className = Clazz.getName(classDef);
      this.classFields = Maps.newHashMap();
      initFieldsMap(classDef);
    }

    private void initFieldsMap(AstNode classDef) {
      for (AstNode varDeclaration : Clazz.getFields(classDef)) {

        for (AstNode varBinding : varDeclaration.getFirstChild(FlexGrammar.VARIABLE_DEF).getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST).getChildren(FlexGrammar.VARIABLE_BINDING)) {
          AstNode identifier = varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER);

          classFields.put(identifier.getTokenValue(), identifier);
        }
      }
    }

    public AstNode getFieldNamed(String name) {
      return classFields.get(name);
    }
  }

  private Stack<ClassState> classStack = new Stack<ClassState>();
  private int functionNestedLevel;

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.CLASS_DEF,
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.VARIABLE_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    classStack.clear();
    functionNestedLevel = 0;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classStack.push(new ClassState(astNode));
    } else if (!classStack.empty() && astNode.is(FlexGrammar.FUNCTION_DEF) && !Clazz.isConstructor(astNode, classStack.peek().className) && !Clazz.isAccessor(astNode)) {
      functionNestedLevel++;
      checkParamaeters(astNode);
    } else if (!classStack.empty() && functionNestedLevel > 0 && astNode.is(FlexGrammar.VARIABLE_DEF)) {
      checkVariableNames(astNode);
    }
  }

  private void checkVariableNames(AstNode varDef) {
    for (AstNode varBinding : varDef.getFirstChild(FlexGrammar.VARIABLE_BINDING_LIST).getChildren(FlexGrammar.VARIABLE_BINDING)) {
      String varName = varBinding.getFirstChild(FlexGrammar.TYPED_IDENTIFIER).getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue();
      AstNode field = classStack.peek().getFieldNamed(varName);
      if (field != null) {
        getContext().createLineViolation(this, "Rename \"{0}\" which hides the field declared at line {1}.", varBinding,
          varName, field.getToken().getLine());
      }
    }
  }

  private void checkParamaeters(AstNode functionDef) {
    AstNode paramaters = functionDef
      .getFirstChild(FlexGrammar.FUNCTION_COMMON)
      .getFirstChild(FlexGrammar.FUNCTION_SIGNATURE)
      .getFirstChild(FlexGrammar.PARAMETERS);

    if (paramaters != null) {
      for (AstNode param : paramaters.getChildren(FlexGrammar.PARAMETER)) {
        String paramName = param
          .getFirstChild(FlexGrammar.TYPED_IDENTIFIER)
          .getFirstChild(FlexGrammar.IDENTIFIER)
          .getTokenValue();
        AstNode field = classStack.peek().getFieldNamed(paramName);

        if (field != null) {
          getContext().createLineViolation(this, "Rename \"{0}\" which hides the field declared at line {1}.", param,
            paramName, field.getToken().getLine());
        }
      }
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.CLASS_DEF)) {
      classStack.pop();
    } else if (!classStack.empty() && astNode.is(FlexGrammar.FUNCTION_DEF) && functionNestedLevel > 0) {
      functionNestedLevel--;
    }
  }
}
