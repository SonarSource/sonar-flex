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
package org.sonar.flex.parser;

import org.sonar.flex.api.FlexGrammar;
import org.sonar.flex.api.FlexTokenType;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static com.sonar.sslr.api.GenericTokenType.LITERAL;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Advanced.adjacent;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Advanced.anyTokenButNot;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Predicate.next;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Predicate.not;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.and;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.o2n;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.one2n;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.opt;
import static com.sonar.sslr.impl.matcher.GrammarFunctions.Standard.or;
import static org.sonar.flex.api.FlexKeyword.AS;
import static org.sonar.flex.api.FlexKeyword.BREAK;
import static org.sonar.flex.api.FlexKeyword.CASE;
import static org.sonar.flex.api.FlexKeyword.CATCH;
import static org.sonar.flex.api.FlexKeyword.CLASS;
import static org.sonar.flex.api.FlexKeyword.CONST;
import static org.sonar.flex.api.FlexKeyword.CONTINUE;
import static org.sonar.flex.api.FlexKeyword.DEFAULT;
import static org.sonar.flex.api.FlexKeyword.DELETE;
import static org.sonar.flex.api.FlexKeyword.DO;
import static org.sonar.flex.api.FlexKeyword.DYNAMIC;
import static org.sonar.flex.api.FlexKeyword.EACH;
import static org.sonar.flex.api.FlexKeyword.ELSE;
import static org.sonar.flex.api.FlexKeyword.EXTENDS;
import static org.sonar.flex.api.FlexKeyword.FALSE;
import static org.sonar.flex.api.FlexKeyword.FINAL;
import static org.sonar.flex.api.FlexKeyword.FINALLY;
import static org.sonar.flex.api.FlexKeyword.FOR;
import static org.sonar.flex.api.FlexKeyword.FUNCTION;
import static org.sonar.flex.api.FlexKeyword.IF;
import static org.sonar.flex.api.FlexKeyword.IMPLEMENTS;
import static org.sonar.flex.api.FlexKeyword.IMPORT;
import static org.sonar.flex.api.FlexKeyword.IN;
import static org.sonar.flex.api.FlexKeyword.INCLUDE;
import static org.sonar.flex.api.FlexKeyword.INSTANCEOF;
import static org.sonar.flex.api.FlexKeyword.INTERFACE;
import static org.sonar.flex.api.FlexKeyword.INTERNAL;
import static org.sonar.flex.api.FlexKeyword.IS;
import static org.sonar.flex.api.FlexKeyword.NATIVE;
import static org.sonar.flex.api.FlexKeyword.NEW;
import static org.sonar.flex.api.FlexKeyword.NULL;
import static org.sonar.flex.api.FlexKeyword.OVERRIDE;
import static org.sonar.flex.api.FlexKeyword.PACKAGE;
import static org.sonar.flex.api.FlexKeyword.PRIVATE;
import static org.sonar.flex.api.FlexKeyword.PROTECTED;
import static org.sonar.flex.api.FlexKeyword.PUBLIC;
import static org.sonar.flex.api.FlexKeyword.RETURN;
import static org.sonar.flex.api.FlexKeyword.STATIC;
import static org.sonar.flex.api.FlexKeyword.SWITCH;
import static org.sonar.flex.api.FlexKeyword.THROW;
import static org.sonar.flex.api.FlexKeyword.TRUE;
import static org.sonar.flex.api.FlexKeyword.TRY;
import static org.sonar.flex.api.FlexKeyword.TYPEOF;
import static org.sonar.flex.api.FlexKeyword.USE;
import static org.sonar.flex.api.FlexKeyword.VAR;
import static org.sonar.flex.api.FlexKeyword.VOID;
import static org.sonar.flex.api.FlexKeyword.WHILE;
import static org.sonar.flex.api.FlexKeyword.WITH;
import static org.sonar.flex.api.FlexPunctuator.ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.BAND;
import static org.sonar.flex.api.FlexPunctuator.BAND_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.BNOT;
import static org.sonar.flex.api.FlexPunctuator.BOR;
import static org.sonar.flex.api.FlexPunctuator.BOR_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.BSR;
import static org.sonar.flex.api.FlexPunctuator.BSR_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.BXOR;
import static org.sonar.flex.api.FlexPunctuator.BXOR_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.COLON;
import static org.sonar.flex.api.FlexPunctuator.COMMA;
import static org.sonar.flex.api.FlexPunctuator.DBL_COLON;
import static org.sonar.flex.api.FlexPunctuator.DEC;
import static org.sonar.flex.api.FlexPunctuator.DIV;
import static org.sonar.flex.api.FlexPunctuator.DIV_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.DOT;
import static org.sonar.flex.api.FlexPunctuator.E4X_ATTRI;
import static org.sonar.flex.api.FlexPunctuator.EQUAL;
import static org.sonar.flex.api.FlexPunctuator.GT;
import static org.sonar.flex.api.FlexPunctuator.INC;
import static org.sonar.flex.api.FlexPunctuator.LAND;
import static org.sonar.flex.api.FlexPunctuator.LAND_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.LBRACK;
import static org.sonar.flex.api.FlexPunctuator.LCURLY;
import static org.sonar.flex.api.FlexPunctuator.LE;
import static org.sonar.flex.api.FlexPunctuator.LNOT;
import static org.sonar.flex.api.FlexPunctuator.LOR;
import static org.sonar.flex.api.FlexPunctuator.LOR_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.LPAREN;
import static org.sonar.flex.api.FlexPunctuator.LT;
import static org.sonar.flex.api.FlexPunctuator.MINUS;
import static org.sonar.flex.api.FlexPunctuator.MINUS_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.MOD;
import static org.sonar.flex.api.FlexPunctuator.MOD_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.NOT_EQUAL;
import static org.sonar.flex.api.FlexPunctuator.PLUS;
import static org.sonar.flex.api.FlexPunctuator.PLUS_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.QUESTION;
import static org.sonar.flex.api.FlexPunctuator.RBRACK;
import static org.sonar.flex.api.FlexPunctuator.RCURLY;
import static org.sonar.flex.api.FlexPunctuator.REST;
import static org.sonar.flex.api.FlexPunctuator.RPAREN;
import static org.sonar.flex.api.FlexPunctuator.SEMI;
import static org.sonar.flex.api.FlexPunctuator.SL;
import static org.sonar.flex.api.FlexPunctuator.SL_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.SR;
import static org.sonar.flex.api.FlexPunctuator.SR_ASSIGN;
import static org.sonar.flex.api.FlexPunctuator.STAR;
import static org.sonar.flex.api.FlexPunctuator.STRICT_EQUAL;
import static org.sonar.flex.api.FlexPunctuator.STRICT_NOT_EQUAL;
import static org.sonar.flex.api.FlexTokenType.NUMERIC_LITERAL;
import static org.sonar.flex.api.FlexTokenType.REGULAR_EXPRESSION_LITERAL;

public class FlexGrammarImpl extends FlexGrammar {

  public FlexGrammarImpl() {
    ge.is(GT, adjacent(ASSIGN));
    star_assign.is(STAR, adjacent(ASSIGN));

    compilationUnit.is(
        // TODO this is a temporary way to handle BOM:
        o2n(FlexTokenType.UNKNOWN),
        opt(packageDecl),
        o2n(packageBlockEntry),
        EOF);

    includeDirective.is(INCLUDE, LITERAL, SEMI);

    arrayLiteral.is(or(
        and(LBRACK, opt(elision), RBRACK),
        and(LBRACK, elementList, RBRACK),
        and(LBRACK, elementList, COMMA, opt(elision), RBRACK)));
    elementList.is(and(opt(elision), assignmentExpression), o2n(COMMA, opt(elision), assignmentExpression));
    elision.is(one2n(COMMA));

    objectLiteral.is(LCURLY, opt(fieldList), RCURLY);
    fieldList.is(literalField, o2n(COMMA, opt(literalField)));
    literalField.is(fieldName, COLON, element);
    element.is(assignmentExpression);
    fieldName.is(or(
        IDENTIFIER,
        LITERAL,
        NUMERIC_LITERAL));

    annotation.is(LBRACK, IDENTIFIER, opt(LPAREN, opt(annotationParam, o2n(COMMA, annotationParam)), RPAREN), RBRACK);
    annotationParam.is(or(
        and(IDENTIFIER, ASSIGN, constant),
        constant,
        IDENTIFIER));

    e4xAttributeIdentifier.is(E4X_ATTRI, or(
        qualifiedIdent,
        STAR,
        and(LBRACK, expression, RBRACK)));

    definitions();
    expressions();
    statements();
    xml();
  }

  private void definitions() {
    identifier.is(IDENTIFIER, o2n(DOT, IDENTIFIER));

    packageDecl.is(PACKAGE, opt(identifier), packageBlock);
    packageBlock.is(LCURLY, o2n(packageBlockEntry), RCURLY);
    packageBlockEntry.is(or(
        annotation,
        variableDefinition,
        importDefinition,
        classDefinition,
        interfaceDefinition,
        useNamespaceDirective,
        methodDefinition,
        block));

    useNamespaceDirective.is(USE, "namespace", IDENTIFIER, SEMI);

    importDefinition.is(IMPORT, IDENTIFIER, o2n(DOT, IDENTIFIER), opt(DOT, STAR), SEMI);

    classDefinition.is(opt(modifiers), CLASS, identifier, classExtendsClause, implementsClause, typeBlock);
    classExtendsClause.is(opt(EXTENDS, identifier));
    implementsClause.is(opt(IMPLEMENTS, identifier, o2n(COMMA, identifier)));

    interfaceDefinition.is(opt(modifiers), INTERFACE, identifier, interfaceExtendsClause, typeBlock);
    interfaceExtendsClause.is(opt(EXTENDS, identifier, o2n(COMMA, identifier)));
    typeBlock.is(LCURLY, o2n(typeBlockEntry), RCURLY);

    typeBlockEntry.is(or(
        importDefinition,
        includeDirective,
        annotation,
        variableDefinition,
        methodDefinition,
        and(IDENTIFIER, DBL_COLON, IDENTIFIER, LCURLY, o2n(typeBlockEntry), RCURLY), // CONFIG::debug { }
        useNamespaceDirective,
        staticLinkEntry,
        block,
        SEMI));
    methodDefinition.is(
        opt(modifiers),
        FUNCTION,
        or(
            and(IDENTIFIER, next(LPAREN)),
            and(opt(accessorRole), IDENTIFIER)),
        parameterDeclarationList,
        opt(typeExpression),
        or(
            block,
            opt(SEMI)));
    accessorRole.is(or(
        "get",
        "set"));
    parameterDeclarationList.is(LPAREN, opt(parameterDeclaration, o2n(COMMA, parameterDeclaration)), RPAREN);
    parameterDeclaration.is(or(
        basicParameterDeclaration,
        parameterRestDeclaration));
    basicParameterDeclaration.is(opt(CONST), IDENTIFIER, opt(typeExpression), opt(parameterDefault));
    parameterDefault.is(ASSIGN, assignmentExpression);
    parameterRestDeclaration.is(REST, IDENTIFIER, opt(typeExpression));

    modifiers.is(o2n(modifier));
    modifier.is(or(
        namespaceName,
        PUBLIC,
        PRIVATE,
        PROTECTED,
        INTERNAL,
        STATIC,
        FINAL,
        OVERRIDE,
        DYNAMIC,
        NATIVE));
    namespaceName.is(not("namespace"), IDENTIFIER);

    variableDefinition.is(opt(modifiers), or(VAR, CONST, "namespace"), variableDeclarator, o2n(COMMA, variableDeclarator), opt(SEMI));
    variableDeclarator.is(IDENTIFIER, opt(typeExpression), opt(variableInitializer));
    variableInitializer.is(ASSIGN, assignmentExpression);

    typeExpression.is(COLON, or(
        and(identifier, opt(DOT, LT, identifier, GT)),
        VOID,
        STAR));

    staticLinkEntry.is(IDENTIFIER, SEMI);
  }

  private void statements() {
    statement.is(or(
        block,
        labelledStatement,
        defaultXmlNamespaceStatement,
        declarationStatement,
        expressionStatement,
        ifStatement,
        forEachStatement,
        forStatement,
        whileStatement,
        doWhileStatement,
        withStatement,
        switchStatement,
        breakStatement,
        continueStatement,
        returnStatement,
        throwStatement,
        tryStatement,
        includeDirective,
        emptyStatement));

    block.is(LCURLY, o2n(statement), RCURLY);

    arguments.is(LPAREN, opt(expressionList), RPAREN);
    expressionList.is(assignmentExpression, o2n(COMMA, assignmentExpression));

    defaultXmlNamespaceStatement.is(DEFAULT, "xml", "namespace", ASSIGN, expression, eos);
    declarationStatement.is(opt(modifier), or(VAR, CONST), variableDeclarator, o2n(COMMA, variableDeclarator), eos);
    expressionStatement.is(expression, eos);
    labelledStatement.is(IDENTIFIER, COLON, statement);
    ifStatement.is(IF, condition, statement, opt(ELSE, statement));
    doWhileStatement.is(DO, statement, WHILE, condition, eos);
    whileStatement.is(WHILE, condition, statement);
    forEachStatement.is(FOR, EACH, LPAREN, forInClause, RPAREN, statement);
    forInClause.is(or(and(VAR, variableDeclarator), IDENTIFIER), IN, expressionList);
    forStatement.is(
        FOR, LPAREN,
        or(
            forInClause,
            and(opt(or(and(VAR, variableDeclarator, o2n(COMMA, variableDeclarator)), expressionList)), SEMI, opt(expressionList), SEMI, opt(expressionList))),
        RPAREN, statement);
    continueStatement.is(CONTINUE, opt(IDENTIFIER), eos);
    breakStatement.is(BREAK, opt(IDENTIFIER), eos);
    returnStatement.is(RETURN, opt(expression), eos);
    withStatement.is(WITH, condition, statement);

    switchStatement.is(SWITCH, condition, LCURLY, o2n(caseClause), opt(defaultClause), RCURLY);
    caseClause.is(CASE, expression, COLON, o2n(statement));
    defaultClause.is(DEFAULT, COLON, o2n(statement));

    throwStatement.is(THROW, expression, eos);

    tryStatement.is(TRY, block, or(and(one2n(catchBlock), opt(finallyBlock)), finallyBlock));
    catchBlock.is(CATCH, LPAREN, IDENTIFIER, typeExpression, RPAREN, block);
    finallyBlock.is(FINALLY, block);

    emptyStatement.is(SEMI);

    eos.is(opt(SEMI));

    condition.is(LPAREN, expression, RPAREN);
  }

  private void expressions() {
    expression.is(assignmentExpression);

    assignmentExpression.is(conditionalExpression, o2n(assignmentOperator, assignmentExpression)).skipIfOneChild();
    assignmentOperator.is(or(ASSIGN,
        star_assign,
        DIV_ASSIGN,
        MOD_ASSIGN,
        PLUS_ASSIGN,
        MINUS_ASSIGN,
        SL_ASSIGN,
        SR_ASSIGN,
        BSR_ASSIGN,
        BAND_ASSIGN,
        BXOR_ASSIGN,
        BOR_ASSIGN,
        LAND_ASSIGN,
        LOR_ASSIGN)).skip();

    conditionalExpression.is(logicalOrExpression, opt(QUESTION, assignmentExpression, COLON, assignmentExpression)).skipIfOneChild();

    logicalOrExpression.is(logicalAndExpression, o2n(logicalOrOperator, logicalAndExpression)).skipIfOneChild();
    logicalOrOperator.is(LOR).skip();

    logicalAndExpression.is(bitwiseOrExpression, o2n(logicalAndOperator, bitwiseOrExpression)).skipIfOneChild();
    logicalAndOperator.is(LAND).skip();

    bitwiseOrExpression.is(bitwiseXorExpression, o2n(BOR, bitwiseXorExpression)).skipIfOneChild();
    bitwiseXorExpression.is(bitwiseAndExpression, o2n(BXOR, bitwiseAndExpression)).skipIfOneChild();
    bitwiseAndExpression.is(equalityExpression, o2n(BAND, equalityExpression)).skipIfOneChild();
    equalityExpression.is(relationalExpression, o2n(equalityOperator, relationalExpression)).skipIfOneChild();
    equalityOperator.is(or(
        STRICT_EQUAL,
        STRICT_NOT_EQUAL,
        NOT_EQUAL,
        EQUAL)).skip();
    relationalExpression.is(shiftExpression, o2n(relationalOperator, shiftExpression)).skipIfOneChild();
    relationalOperator.is(or(
        ge,
        LT,
        GT,
        LE,
        IS,
        AS,
        INSTANCEOF)).skip();
    shiftExpression.is(additiveExpression, o2n(shiftOperator, additiveExpression)).skipIfOneChild();
    shiftOperator.is(or(
        SL,
        SR,
        BSR)).skip();
    additiveExpression.is(multiplicativeExpression, o2n(additiveOperator, multiplicativeExpression)).skipIfOneChild();
    additiveOperator.is(or(
        PLUS,
        MINUS)).skip();
    multiplicativeExpression.is(unaryExpression, o2n(multiplicativeOperator, unaryExpression));
    multiplicativeOperator.is(or(
        STAR,
        DIV,
        MOD)).skip();
    unaryExpression.is(or(
        postfixExpression,
        and(DELETE, unaryExpression),
        and(VOID, unaryExpression),
        and(TYPEOF, unaryExpression),
        and(INC, unaryExpression),
        and(DEC, unaryExpression),
        and(PLUS, unaryExpression),
        and(MINUS, unaryExpression),
        and(LNOT, unaryExpression),
        and(BNOT, unaryExpression))).skipIfOneChild();

    postfixExpression.is(
        primaryExpression,
        o2n(or(
            and(DOT, qualifiedIdent),
            and(LBRACK, expression, RBRACK),
            and(DOT, LPAREN, expression, RPAREN),
            and(DOT, e4xAttributeIdentifier),
            and(DOT, STAR),
            DOT,
            arguments)),
        opt(or(INC, DEC))).skipIfOneChild();

    primaryExpression.is(or(
        // UNDEFINED
        constant,
        arrayLiteral,
        objectLiteral,
        functionExpression,
        and(LPAREN, expression, RPAREN),
        newExpression,
        e4xAttributeIdentifier,
        and(LT, identifier, GT),
        qualifiedIdent)).skipIfOneChild();
    constant.is(or(
        LITERAL,
        NUMERIC_LITERAL,
        REGULAR_EXPRESSION_LITERAL,
        TRUE,
        FALSE,
        NULL,
        xmlLiteral)).skip();

    functionExpression.is(FUNCTION, opt(IDENTIFIER), parameterDeclarationList, opt(typeExpression), block);

    newExpression.is(NEW, primaryExpression, o2n(or(and(DOT, qualifiedIdent), and(LBRACK, expressionList, RBRACK))));

    qualifiedIdent.is(opt(namespaceName, DBL_COLON), or(IDENTIFIER, and(LBRACK, expression, RBRACK)), opt(DOT, LT, identifier, GT));
  }

  private void xml() {
    xmlLiteral.is(or(xmlNode, xmlCData));
    xmlNode.is(
        "<", IDENTIFIER, o2n(xmlAttribute),
        or(
            and(">", o2n(xmlNodeContent), "<", "/", IDENTIFIER, ">"),
            and("/", ">")));
    xmlNodeContent.is(or(
        xmlNode,
        xmlTextNode,
        xmlCData,
        xmlComment,
        and("<", "?", o2n(anyTokenButNot("?")), "?", ">")));
    xmlAttribute.is(IDENTIFIER, ASSIGN, or(LITERAL, xmlBinding));
    xmlTextNode.is(anyTokenButNot(or("/", "<")));
    xmlComment.is("<", "!", "--", o2n(anyTokenButNot("--")), "--", ">");
    xmlCData.is("<", "!", "[", "CDATA", "[", o2n(anyTokenButNot("]")), "]", "]", ">");
    xmlBinding.is("{", o2n(anyTokenButNot(or("{", "}"))), "}");
  }

}
