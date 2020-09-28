/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2020 SonarSource SA
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;

public abstract class ConditionalStructureCheckBase extends FlexCheck {
  private Set<AstNode> visitedIfStatements = new HashSet<>();
  @Override
  public void visitFile(@Nullable AstNode node) {
    visitedIfStatements.clear();
  }
  @Override
  public List<AstNodeType> subscribedTo() {
    return Arrays.asList(
      FlexGrammar.IF_STATEMENT,
      FlexGrammar.SWITCH_STATEMENT);
  }
  @Override
  public void visitNode(AstNode node) {
    ConditionalStructure conditionalStructure;
    if (node.is(FlexGrammar.IF_STATEMENT)) {
      if (!visitedIfStatements.add(node)) {
        return;
      }
      conditionalStructure = ConditionalStructure.ifStatement(node, visitedIfStatements);
    } else {
      conditionalStructure = ConditionalStructure.switchStatement(node);
    }
    visitConditionalStructure(conditionalStructure);
  }

  abstract void visitConditionalStructure(ConditionalStructure cond);

}
