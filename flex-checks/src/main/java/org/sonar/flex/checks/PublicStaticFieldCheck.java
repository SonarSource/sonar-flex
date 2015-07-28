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

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.api.server.rule.RulesDefinition.SubCharacteristics;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Set;

@Rule(
  key = "S1444",
  name = "\"public static\" fields should be constant",
  tags = {Tags.CWE, Tags.CERT, Tags.SECURITY},
  priority = Priority.CRITICAL)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@SqaleSubCharacteristic(SubCharacteristics.SECURITY_FEATURES)
@SqaleConstantRemediation("20min")
public class PublicStaticFieldCheck extends SquidCheck<LexerlessGrammar> {

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode directive : Clazz.getDirectives(astNode)) {

      if (Variable.isVariable(directive)) {
        Set<AstNodeType> varModifiers = Modifiers.getModifiers(directive.getFirstChild(FlexGrammar.ATTRIBUTES));

        if (varModifiers.contains(FlexKeyword.PUBLIC) && varModifiers.contains(FlexKeyword.STATIC)) {
          getContext().createLineViolation(this, "Make this \"public static\" field const", directive);
        }
      }
    }
  }
}
