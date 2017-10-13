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
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.FlexKeyword;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.checks.SquidCheck;
import org.sonar.sslr.parser.LexerlessGrammar;

import javax.annotation.Nullable;
import java.util.regex.Pattern;


@Rule(
  key = "S115",
  name = "Constant names should comply with a naming convention",
  priority = Priority.MINOR,
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class ConstantNameCheck extends SquidCheck<LexerlessGrammar> {

  private static final String DEFAULT = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the constant names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;

  @Override
  public void init() {
    subscribeTo(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.getFirstChild(FlexGrammar.VARIABLE_DEF).getFirstChild(FlexGrammar.VARIABLE_DEF_KIND).getFirstChild(FlexKeyword.CONST) != null) {

      for (AstNode identifier : Variable.getDeclaredIdentifiers(astNode)) {
        String varName = identifier.getTokenValue();

        if (!pattern.matcher(varName).matches()) {
          getContext().createLineViolation(this, "Rename this constant '" + varName + "' to match the regular expression " + format + "",
            identifier);
        }
      }
    }
  }
}
