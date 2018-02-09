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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "S1439",
  name = "Only \"while\", \"do\" and \"for\" statements should be labelled",
  priority = Priority.MAJOR,
  tags = Tags.PITFALL)
@ActivatedByDefault
@SqaleConstantRemediation("20min")
public class LabelPlacementCheck extends FlexCheck {

  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.LABELED_STATEMENT);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (!isIterationStatement(astNode.getFirstChild(FlexGrammar.SUB_STATEMENT))) {
      addIssue(MessageFormat.format("Remove this ''{0}'' label.", astNode.getFirstChild(FlexGrammar.IDENTIFIER).getTokenValue()), astNode);
    }

  }

  private static boolean isIterationStatement(AstNode subStatement) {
    AstNode astNode = subStatement.getFirstChild();
    return astNode.is(FlexGrammar.STATEMENT)
      && astNode.getFirstChild().is(FlexGrammar.WHILE_STATEMENT, FlexGrammar.DO_STATEMENT, FlexGrammar.FOR_STATEMENT);
  }

}
