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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Modifiers;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;

@Rule(
  key = "S1820",
  name = "Classes should not have too many fields",
  priority = Priority.MAJOR)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.ARCHITECTURE_CHANGEABILITY)
@SqaleConstantRemediation("1h")
public class ClassWithTooManyFieldsCheck extends SquidCheck<LexerlessGrammar> {


  public static final int DEFAULT_MAX = 20;
  public static final boolean DEFAULT_COUNT_NON_PUBLIC = true;

  @RuleProperty(
    key = "maximumFieldThreshold",
    description = "The maximum number of field",
    defaultValue = "" + DEFAULT_MAX)
  int maximumFieldThreshold = DEFAULT_MAX;

  @RuleProperty(
    key = "countNonpublicFields",
    type = "BOOLEAN",
    description = "Whether or not to include non-public fields in the count",
    defaultValue = "" + DEFAULT_COUNT_NON_PUBLIC)
  boolean countNonpublicFields = DEFAULT_COUNT_NON_PUBLIC;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitNode(AstNode astNode) {
    int nbFields = getNumberOfFields(astNode);

    if (nbFields > maximumFieldThreshold) {
      String msg = countNonpublicFields ? String.valueOf(maximumFieldThreshold) : maximumFieldThreshold + " public";
      getContext().createLineViolation(this, "Refactor this class so it has no more than {0} fields, rather than the {1} it currently has.", astNode,
        msg, nbFields);
    }
  }

  private int getNumberOfFields(AstNode classDef) {
    List<AstNode> fields = Clazz.getFields(classDef);
    int nbFields = fields.size();

    if (!countNonpublicFields) {
      nbFields -= getNumberOfNonPublicFields(fields);
    }
    return nbFields;
  }

  private static int getNumberOfNonPublicFields(List<AstNode> fields) {
    int nbNonPublicFields = 0;

    for (AstNode field : fields) {
      if (Modifiers.isNonPublic(Modifiers.getModifiers(field.getPreviousAstNode()))) {
        nbNonPublicFields++;
      }
    }
    return nbNonPublicFields;
  }

}
