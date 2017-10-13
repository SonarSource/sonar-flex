/*
 * SonarQube Flex Plugin
 * Copyright (C) 2010-2017 SonarSource SA
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
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexPunctuator;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

@Rule(
  key = "S1440",
  name = "\"===\" and \"!==\" should be used instead of \"==\" and \"!=\"",
  priority = Priority.MAJOR,
  tags = Tags.SUSPICIOUS)
@ActivatedByDefault
@SqaleConstantRemediation("5min")
public class EqEqEqCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexPunctuator.EQUAL2, FlexPunctuator.NOTEQUAL1);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(FlexPunctuator.EQUAL2)) {
      getContext().createLineViolation(this, "Replace == with ===", astNode);
    } else {
      getContext().createLineViolation(this, "Replace != with !==", astNode);
    }
  }

}
