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
package org.sonar.flex;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Token;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public abstract class FlexVisitor {

  private FlexVisitorContext context;

  private Set<AstNodeType> subscribedKinds = null;

  public abstract List<AstNodeType> subscribedTo();

  private Set<AstNodeType> subscribedKinds() {
    if (subscribedKinds == null) {
      subscribedKinds = ImmutableSet.copyOf(subscribedTo());
    }
    return subscribedKinds;
  }

  public void visitFile(@Nullable AstNode node) {
  }

  public void leaveFile(@Nullable AstNode node) {
  }

  public void visitNode(AstNode node) {
  }

  public void leaveNode(AstNode node) {
  }

  public void visitToken(Token token) {
  }

  public FlexVisitorContext getContext() {
    return context;
  }

  public void scanFile(FlexVisitorContext context) {
    this.context = context;
    AstNode tree = context.rootTree();
    visitFile(tree);
    if (tree != null) {
      scanNode(tree);
    }
    leaveFile(tree);
  }

  public void scanNode(AstNode node) {
    boolean isSubscribedType = subscribedKinds().contains(node.getType());

    if (isSubscribedType) {
      visitNode(node);
    }

    List<AstNode> children = node.getChildren();
    if (children.isEmpty()) {
      node.getTokens().forEach(this::visitToken);
    } else {
      children.forEach(this::scanNode);
    }

    if (isSubscribedType) {
      leaveNode(node);
    }
  }

}
