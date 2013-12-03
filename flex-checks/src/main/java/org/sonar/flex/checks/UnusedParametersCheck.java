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
import org.sonar.flex.checks.utils.Function;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.Map;

@Rule(
  key = "S1172",
  priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class UnusedParametersCheck extends SquidCheck<LexerlessGrammar> {

  private static class Parameter {
    final AstNode declaration;
    int usages;

    private Parameter(AstNode declaration, int usages) {
      this.declaration = declaration;
      this.usages = usages;
    }
  }


  private static class Scope {
    private final Scope outerScope;
    private final Map<String, Parameter> parameters;

    public Scope(Scope outerScope, AstNode functionDef) {
      this.outerScope = outerScope;
      this.parameters = Maps.newHashMap();
      retrieveAllParameters(functionDef);
    }

    private void retrieveAllParameters(AstNode functionDef) {
      for (AstNode paramIdentifier : Function.getParametersIdentifiers(functionDef)) {
        parameters.put(paramIdentifier.getTokenValue(), new Parameter(paramIdentifier, 0));
      }
    }

    private void use(AstNode astNode) {
      String identifier = astNode.getTokenValue();
      Scope scope = this;

      while (scope != null) {
        Parameter var = scope.parameters.get(identifier);
        if (var != null) {
          var.usages++;
          return;
        }
        scope = scope.outerScope;
      }
    }
  }

  private Scope currentScope;

  @Override
  public void init() {
    subscribeTo(
      FlexGrammar.FUNCTION_DEF,
      FlexGrammar.QUALIFIED_IDENTIFIER);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    currentScope = null;
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.FUNCTION_DEF)) {
      currentScope = new Scope(currentScope, astNode);
    } else if (currentScope != null && astNode.is(FlexGrammar.QUALIFIED_IDENTIFIER)) {
      currentScope.use(astNode);
    }
  }

  @Override
  public void leaveNode(AstNode astNode) {
    if (astNode.is(FlexGrammar.FUNCTION_DEF)) {
      reportUnusedPrivateField();
      currentScope = currentScope.outerScope;
    }
  }

  private void reportUnusedPrivateField() {
    for (Map.Entry<String, Parameter> entry : currentScope.parameters.entrySet()) {
      if (entry.getValue().usages == 0) {
        getContext().createLineViolation(this, "Remove this unused function parameter \"{0}\".", entry.getValue().declaration, entry.getKey());
      }
    }
  }
}
