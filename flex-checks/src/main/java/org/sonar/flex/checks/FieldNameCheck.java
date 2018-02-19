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
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.flex.FlexCheck;
import org.sonar.flex.FlexGrammar;
import org.sonar.flex.checks.utils.Clazz;
import org.sonar.flex.checks.utils.Tags;
import org.sonar.flex.checks.utils.Variable;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;


@Rule(
  key = "S116",
  name = "Field names should comply with a naming convention",
  priority = Priority.MINOR,
  tags = Tags.CONVENTION)
@ActivatedByDefault
@SqaleConstantRemediation("2min")
public class FieldNameCheck extends FlexCheck {

  private static final String DEFAULT = "^[_a-z][a-zA-Z0-9]*$";
  private Pattern pattern = null;

  @RuleProperty(
    key = "format",
    description = "Regular expression used to check the field names against",
    defaultValue = DEFAULT)
  String format = DEFAULT;


  @Override
  public List<AstNodeType> subscribedTo() {
    return Collections.singletonList(FlexGrammar.CLASS_DEF);
  }

  @Override
  public void visitFile(@Nullable AstNode astNode) {
    if (pattern == null) {
      pattern = Pattern.compile(format);
    }
  }

  @Override
  public void visitNode(AstNode astNode) {
    for (AstNode directive : Clazz.getDirectives(astNode)) {

      if (Variable.isVariable(directive)) {
        AstNode variableDeclStatement = directive
          .getFirstChild(FlexGrammar.ANNOTABLE_DIRECTIVE)
          .getFirstChild(FlexGrammar.VARIABLE_DECLARATION_STATEMENT);

        visitVariableDeclStatement(variableDeclStatement);
      }
    }
  }

  private void visitVariableDeclStatement(AstNode variableDeclStatement) {
    for (AstNode identifier : Variable.getDeclaredIdentifiers(variableDeclStatement)) {
      if (!pattern.matcher(identifier.getTokenValue()).matches()) {
        addIssue(MessageFormat.format("Rename this field name to match the regular expression {0}", format), identifier);
      }
    }
  }
}
