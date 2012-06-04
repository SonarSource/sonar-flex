/*
 * Sonar Flex Plugin
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
package org.sonar.flex.api;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Rule;

public class FlexGrammar extends Grammar {

  public Rule annotation;
  public Rule annotationParam;

  public Rule e4xAttributeIdentifier;

  public Rule identifier;

  public Rule compilationUnit;

  public Rule packageDecl;
  public Rule packageBlock;
  public Rule packageBlockEntry;

  public Rule namespaceDefinition;
  public Rule useNamespaceDirective;

  public Rule importDefinition;

  public Rule classDefinition;
  public Rule classExtendsClause;
  public Rule implementsClause;

  public Rule interfaceDefinition;
  public Rule interfaceExtendsClause;

  public Rule typeBlock;
  public Rule typeBlockEntry;

  public Rule methodDefinition;
  public Rule accessorRole;

  public Rule parameterDeclarationList;
  public Rule parameterDeclaration;
  public Rule basicParameterDeclaration;
  public Rule parameterDefault;
  public Rule parameterRestDeclaration;

  public Rule modifiers;
  public Rule modifier;
  public Rule namespaceName;

  public Rule typeExpression;

  public Rule variableDefinition;
  public Rule variableDeclarator;
  public Rule variableInitializer;

  public Rule arrayLiteral;
  public Rule elementList;
  public Rule nonemptyElementList;

  public Rule objectLiteral;
  public Rule fieldList;
  public Rule literalField;
  public Rule element;
  public Rule fieldName;

  /*
   * Statements
   */

  public Rule block;

  public Rule eos;

  public Rule statement;

  public Rule superStatement;
  public Rule arguments;
  public Rule expressionList;

  public Rule declarationStatement;
  public Rule expressionStatement;
  public Rule ifStatement;
  public Rule forEachStatement;
  public Rule forInClause;
  public Rule forStatement;
  public Rule whileStatement;
  public Rule doWhileStatement;
  public Rule withStatement;
  public Rule switchStatement;
  public Rule caseStatement;
  public Rule defaultStatement;
  public Rule switchBlock;
  public Rule breakStatement;
  public Rule continueStatement;
  public Rule returnStatement;
  public Rule throwStatement;

  public Rule tryStatement;
  public Rule catchBlock;
  public Rule finallyBlock;

  public Rule emptyStatement;

  /*
   * Expressions
   */

  public Rule expression;
  public Rule condition;

  public Rule assignmentExpression;
  public Rule assignmentOperator;
  public Rule conditionalExpression;
  public Rule logicalOrExpression;
  public Rule logicalOrOperator;
  public Rule logicalAndExpression;
  public Rule logicalAndOperator;
  public Rule bitwiseOrExpression;
  public Rule bitwiseXorExpression;
  public Rule bitwiseAndExpression;
  public Rule equalityExpression;
  public Rule equalityOperator;
  public Rule relationalExpression;
  public Rule relationalOperator;
  public Rule shiftExpression;
  public Rule shiftOperator;
  public Rule additiveExpression;
  public Rule additiveOperator;
  public Rule multiplicativeExpression;
  public Rule multiplicativeOperator;
  public Rule unaryExpression;
  public Rule postfixExpression;
  public Rule primaryExpression;
  public Rule constant;
  public Rule newExpression;
  public Rule qualifiedIdent;

  @Override
  public Rule getRootRule() {
    return compilationUnit;
  }

}
