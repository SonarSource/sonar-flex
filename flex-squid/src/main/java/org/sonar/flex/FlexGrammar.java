/*
 * SonarQube Flex Plugin
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
package org.sonar.flex;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.List;

import static org.sonar.flex.FlexKeyword.AS;
import static org.sonar.flex.FlexKeyword.BREAK;
import static org.sonar.flex.FlexKeyword.CASE;
import static org.sonar.flex.FlexKeyword.CATCH;
import static org.sonar.flex.FlexKeyword.CLASS;
import static org.sonar.flex.FlexKeyword.CONST;
import static org.sonar.flex.FlexKeyword.CONTINUE;
import static org.sonar.flex.FlexKeyword.DEFAULT;
import static org.sonar.flex.FlexKeyword.DELETE;
import static org.sonar.flex.FlexKeyword.DO;
import static org.sonar.flex.FlexKeyword.DYNAMIC;
import static org.sonar.flex.FlexKeyword.EACH;
import static org.sonar.flex.FlexKeyword.ELSE;
import static org.sonar.flex.FlexKeyword.EXTENDS;
import static org.sonar.flex.FlexKeyword.FALSE;
import static org.sonar.flex.FlexKeyword.FINALLY;
import static org.sonar.flex.FlexKeyword.FOR;
import static org.sonar.flex.FlexKeyword.FUNCTION;
import static org.sonar.flex.FlexKeyword.GET;
import static org.sonar.flex.FlexKeyword.IF;
import static org.sonar.flex.FlexKeyword.IMPLEMENTS;
import static org.sonar.flex.FlexKeyword.IMPORT;
import static org.sonar.flex.FlexKeyword.IN;
import static org.sonar.flex.FlexKeyword.INCLUDE;
import static org.sonar.flex.FlexKeyword.INSTANCEOF;
import static org.sonar.flex.FlexKeyword.INTERFACE;
import static org.sonar.flex.FlexKeyword.INTERNAL;
import static org.sonar.flex.FlexKeyword.IS;
import static org.sonar.flex.FlexKeyword.NAMESPACE;
import static org.sonar.flex.FlexKeyword.NEW;
import static org.sonar.flex.FlexKeyword.NULL;
import static org.sonar.flex.FlexKeyword.PACKAGE;
import static org.sonar.flex.FlexKeyword.PRIVATE;
import static org.sonar.flex.FlexKeyword.PROTECTED;
import static org.sonar.flex.FlexKeyword.PUBLIC;
import static org.sonar.flex.FlexKeyword.RETURN;
import static org.sonar.flex.FlexKeyword.SET;
import static org.sonar.flex.FlexKeyword.STATIC;
import static org.sonar.flex.FlexKeyword.SUPER;
import static org.sonar.flex.FlexKeyword.SWITCH;
import static org.sonar.flex.FlexKeyword.THIS;
import static org.sonar.flex.FlexKeyword.THROW;
import static org.sonar.flex.FlexKeyword.TRUE;
import static org.sonar.flex.FlexKeyword.TRY;
import static org.sonar.flex.FlexKeyword.TYPEOF;
import static org.sonar.flex.FlexKeyword.USE;
import static org.sonar.flex.FlexKeyword.VAR;
import static org.sonar.flex.FlexKeyword.VOID;
import static org.sonar.flex.FlexKeyword.WHILE;
import static org.sonar.flex.FlexKeyword.WITH;
import static org.sonar.flex.FlexKeyword.XML;
import static org.sonar.flex.FlexPunctuator.AND;
import static org.sonar.flex.FlexPunctuator.ANDAND;
import static org.sonar.flex.FlexPunctuator.ANDAND_EQU;
import static org.sonar.flex.FlexPunctuator.AND_EQU;
import static org.sonar.flex.FlexPunctuator.AT_SIGN;
import static org.sonar.flex.FlexPunctuator.COLON;
import static org.sonar.flex.FlexPunctuator.COMMA;
import static org.sonar.flex.FlexPunctuator.DIV;
import static org.sonar.flex.FlexPunctuator.DIV_EQU;
import static org.sonar.flex.FlexPunctuator.DOT;
import static org.sonar.flex.FlexPunctuator.DOUBLE_COLON;
import static org.sonar.flex.FlexPunctuator.DOUBLE_DOT;
import static org.sonar.flex.FlexPunctuator.DOUBLE_MINUS;
import static org.sonar.flex.FlexPunctuator.DOUBLE_PLUS;
import static org.sonar.flex.FlexPunctuator.EQUAL1;
import static org.sonar.flex.FlexPunctuator.EQUAL2;
import static org.sonar.flex.FlexPunctuator.EQUAL3;
import static org.sonar.flex.FlexPunctuator.GE;
import static org.sonar.flex.FlexPunctuator.GT;
import static org.sonar.flex.FlexPunctuator.LBRAKET;
import static org.sonar.flex.FlexPunctuator.LCURLYBRACE;
import static org.sonar.flex.FlexPunctuator.LE;
import static org.sonar.flex.FlexPunctuator.LPARENTHESIS;
import static org.sonar.flex.FlexPunctuator.LT;
import static org.sonar.flex.FlexPunctuator.MINUS;
import static org.sonar.flex.FlexPunctuator.MINUS_EQU;
import static org.sonar.flex.FlexPunctuator.MOD;
import static org.sonar.flex.FlexPunctuator.MOD_EQU;
import static org.sonar.flex.FlexPunctuator.NOT;
import static org.sonar.flex.FlexPunctuator.NOTEQUAL1;
import static org.sonar.flex.FlexPunctuator.NOTEQUAL2;
import static org.sonar.flex.FlexPunctuator.OR;
import static org.sonar.flex.FlexPunctuator.OROR;
import static org.sonar.flex.FlexPunctuator.OROR_EQU;
import static org.sonar.flex.FlexPunctuator.OR_EQU;
import static org.sonar.flex.FlexPunctuator.PLUS;
import static org.sonar.flex.FlexPunctuator.PLUS_EQU;
import static org.sonar.flex.FlexPunctuator.QUERY;
import static org.sonar.flex.FlexPunctuator.RBRAKET;
import static org.sonar.flex.FlexPunctuator.RCURLYBRACE;
import static org.sonar.flex.FlexPunctuator.RPARENTHESIS;
import static org.sonar.flex.FlexPunctuator.SEMICOLON;
import static org.sonar.flex.FlexPunctuator.SL;
import static org.sonar.flex.FlexPunctuator.SL_EQU;
import static org.sonar.flex.FlexPunctuator.SR;
import static org.sonar.flex.FlexPunctuator.SR2;
import static org.sonar.flex.FlexPunctuator.SR_EQU;
import static org.sonar.flex.FlexPunctuator.SR_EQU2;
import static org.sonar.flex.FlexPunctuator.STAR;
import static org.sonar.flex.FlexPunctuator.STAR_EQU;
import static org.sonar.flex.FlexPunctuator.TILD;
import static org.sonar.flex.FlexPunctuator.TRIPLE_DOTS;
import static org.sonar.flex.FlexPunctuator.XOR;
import static org.sonar.flex.FlexPunctuator.XORXOR_EQU;
import static org.sonar.flex.FlexPunctuator.XOR_EQU;

public enum FlexGrammar implements GrammarRuleKey {

  WHITESPACE,
  SPACING,
  SPACING_NO_LB,
  NEXT_NOT_LB,

  EOS,
  EOS_NO_LB,

  STRING,
  NUMBER,
  DECIMAL,
  HEXADECIMAL,
  OCTAL,

  /**
   * EXPRESSIONS
   */
  // <editor-fold defaultstate="collapsed" desc="Expression">
  PRIMARY_EXPR,
  RESERVED_NAMESPACE,
  PARENTHESIZED_EXPR,
  PARENTHESIZED_LIST_EXPR,
  FUNCTION_EXPR,
  OBJECT_INITIALISER,
  FIELD_NAME,
  LITERAL_FIELD,
  ARRAY_INITIALISER,
  ELEMENT_LIST,
  LITERAL_ELEMENT,
  CONDITIONAL_EXPR,
  CONDITIONAL_EXPR_NO_IN,
  POSTFIX_EXPR,
  COMPOUND_ASSIGNMENT,
  LOGICAL_ASSIGNMENT,
  SUPER_EXPR,
  // Identifiers
  PROPERTY_IDENTIFIER,
  QUALIFIER,
  SIMPLE_QUALIFIED_IDENTIFIER,
  EXPR_QUALIFIED_IDENTIFIER,
  NON_ATTRIBUTE_QUALIFIED_IDENTIFIER,
  QUALIFIED_IDENTIFIER,
  BRACKETS,
  IDENTIFIER,
  IDENTIFIER_PART,
  // New expressions
  FULL_NEW_EXPR,
  FULL_NEW_SUB_EXPR,
  SHORT_NEW_EXPR,
  SHORT_NEW_SUB_EXPR,
  PROPERTY_OPERATOR,
  QUERY_OPERATOR,
  // Call expression
  ARGUMENTS,
  // Unary expression
  UNARY_EXPR,
  // Binary expression
  MULTIPLICATIVE_EXPR,
  ADDITIVE_EXPR,
  ADDITIVE_OPERATOR,
  SHIFT_EXPR,
  RELATIONAL_EXPR,
  RELATIONAL_EXPR_NO_IN,
  RELATIONAL_OPERATOR,
  RELATIONAL_OPERATOR_NO_IN,
  EQUALITY_EXPR,
  EQUALITY_EXPR_NO_IN,
  EQUALITY_OPERATOR,
  BITEWISE_AND_EXPR,
  BITEWISE_AND_EXPR_NO_IN,
  BITEWISE_XOR_EXPR,
  BITEWISE_XOR_EXPR_NO_IN,
  BITEWISE_OR_EXPR,
  BITEWISE_OR_EXPR_NO_IN,
  LOGICAL_AND_EXPR,
  LOGICAL_AND_EXPR_NO_IN,
  LOGICAL_AND_OPERATOR,
  LOGICAL_OR_EXPR,
  LOGICAL_OR_EXPR_NO_IN,
  LOGICAL_OR_OPERATOR,
  // Assignment expression
  ASSIGNMENT_EXPR,
  ASSIGNMENT_EXPR_NO_IN,
  ASSIGNMENT_OPERATOR,
  // List expression
  LIST_EXPRESSION,
  LIST_EXPRESSION_NO_IN,
  // Non assignment expression
  NON_ASSIGNMENT_EXPR,
  NON_ASSIGNMENT_EXPR_NO_IN,
  // Type expression
  TYPE_EXPR,
  TYPE_EXPR_NO_IN,
  TYPE_APPLICATION,
  VECTOR_LITERAL_EXPRESSION,
  // XML Initialiser
  XML_INITIALISER,
  XML_MARKUP,
  XML_ELEMENT,
  XML_TAG_CONTENT,
  XML_WHITESPACE,
  XML_TAG_NAME,
  XML_ATTRIBUTE,
  XML_ATTRIBUTES,
  XML_ATTRIBUTE_VALUE,
  XML_NAME,
  XML_ELEMENT_CONTENT,
  XML_TEXT,
  XML_COMMENT,
  XML_CDATA,
  XML_PI,
  KEYWORDS,
  REGULAR_EXPRESSION,
  // </editor-fold>

  /**
   * DEFINITIONS
   */
  // <editor-fold defaultstate="collapsed" desc="Definitions">
  // Variable
  VARIABLE_DEF,
  VARIABLE_DEF_NO_IN,
  VARIABLE_DEF_KIND,
  VARIABLE_BINDING_LIST,
  VARIABLE_BINDING_LIST_NO_IN,
  VARIABLE_BINDING,
  VARIABLE_BINDING_NO_IN,
  VARIABLE_INITIALISATION,
  VARIABLE_INITIALISATION_NO_IN,
  TYPED_IDENTIFIER,
  TYPED_IDENTIFIER_NO_IN,
  VARIABLE_INITIALISER,
  VARIABLE_INITIALISER_NO_IN,
  // Function
  FUNCTION_DEF,
  FUNCTION_NAME,
  FUNCTION_COMMON,
  FUNCTION_SIGNATURE,
  RESULT_TYPE,
  PARAMETERS,
  PARAMETER,
  REST_PARAMETERS,
  // Class
  CLASS_DEF,
  CLASS_NAME,
  INHERITENCE,
  CLASS_IDENTIFIERS,
  TYPE_EXPRESSION_LIST,
  // Interface
  INTERFACE_DEF,
  EXTENDS_LIST,
  // Package
  PACKAGE_DEF,
  PACKAGE_NAME,
  // Namespace
  NAMESPACE_DEF,
  NAMESPACE_BINDING,
  NAMESPACE_INITIALISATION,
  // Program
  PROGRAM,
  // </editor-fold>

  /**
   * STATEMENTS
   */
  // <editor-fold defaultstate="collapsed" desc="Statements">
  STATEMENT,
  SUPER_STATEMENT,
  SWITCH_STATEMENT,
  IF_STATEMENT,
  DO_STATEMENT,
  WHILE_STATEMENT,
  FOR_STATEMENT,
  WITH_STATEMENT,
  CONTINUE_STATEMENT,
  BREAK_STATEMENT,
  RETURN_STATEMENT,
  THROW_STATEMENT,
  TRY_STATEMENT,
  EXPRESSION_STATEMENT,
  LABELED_STATEMENT,
  DEFAULT_XML_NAMESPACE_DIRECTIVE,
  SUB_STATEMENT,
  EMPTY_STATEMENT,
  VARIABLE_DECLARATION_STATEMENT,
  BLOCK,
  CASE_ELEMENT,
  CASE_LABEL,
  FOR_INITIALISER,
  FOR_IN_BINDING,
  CATCH_CLAUSE,
  CATCH_CLAUSES,
  DECIMAL_DIGITS,
  EXPONENT_PART,
  DECIMAL_INTEGER,
  // </editor-fold>

  /**
   * DIRECTIVES
   */
  // <editor-fold defaultstate="collapsed" desc="Directives">
  DIRECTIVES,
  DIRECTIVE,
  CONFIG_CONDITION,
  ANNOTABLE_DIRECTIVE,
  USE_DIRECTIVE,
  IMPORT_DIRECTIVE,
  INCLUDE_DIRECTIVE,
  ATTRIBUTES,
  ATTRIBUTE,
  ATTRIBUTE_COMBINATION,
  ATTRIBUTE_EXPR;
  // </editor-fold>

  private static final String UNICODE_LETTER = "\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}";
  private static final String UNICODE_DIGIT = "\\p{Nd}";
  private static final String UNICODE_COMBINING_MARK = "\\p{Mn}\\p{Mc}";
  private static final String UNICODE_CONNECTOR_PUNCTUATION = "\\p{Pc}";

  private static final String UNICODE_ESCAPE_SEQUENCE_REGEXP = "u[0-9a-fA-F]{4,4}";
  private static final String IDENTIFIER_START_REGEXP = "(?:[$_" + UNICODE_LETTER + "]|\\\\" + UNICODE_ESCAPE_SEQUENCE_REGEXP + ")";
  private static final String IDENTIFIER_PART_REGEXP = "(?:" +
    IDENTIFIER_START_REGEXP + "|[" + UNICODE_COMBINING_MARK + UNICODE_DIGIT + UNICODE_CONNECTOR_PUNCTUATION + "])";

  private static final String EXPONENT_PART_REGEXP = "([eE][-+]?[0-9]++)?";
  private static final String DECIMAL_INTEGER_REGEXP = "(0|([1-9][0-9]*+))";
  private static final String DECIMAL_DIGITS_REGEXP = "([0-9]*+)";
  private static final String DECIMAL_REGEXP =
    DECIMAL_INTEGER_REGEXP + "\\." + DECIMAL_DIGITS_REGEXP + "?" + EXPONENT_PART_REGEXP +
      "|\\." + DECIMAL_DIGITS_REGEXP + EXPONENT_PART_REGEXP +
      "|" + DECIMAL_INTEGER_REGEXP + EXPONENT_PART_REGEXP;

  private static final String SINGLE_LINE_COMMENT_REGEXP = "//[^\\n\\r]*+";

  private static final String MULTI_LINE_COMMENT_REGEXP = "/\\*[\\s\\S]*?\\*/";
  private static final String MULTI_LINE_COMMENT_NO_LB_REGEXP = "/\\*[^\\n\\r]*?\\*/";

  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    // TODO extract whitespaces and line terminators into constants:
    b.rule(WHITESPACE).is(b.regexp("[\\s\\uFEFF]*+"));

    b.rule(SPACING).is(
      b.skippedTrivia(WHITESPACE),
      b.zeroOrMore(
        b.commentTrivia(b.regexp("(?:" + SINGLE_LINE_COMMENT_REGEXP + "|" + MULTI_LINE_COMMENT_REGEXP + ")")),
        b.skippedTrivia(WHITESPACE))
    ).skip();

    b.rule(SPACING_NO_LB).is(b.zeroOrMore(b.firstOf(
      b.skippedTrivia(b.regexp("[\\s&&[^\n\r]]++")),
      b.commentTrivia(b.regexp("(?:" + SINGLE_LINE_COMMENT_REGEXP + "|" + MULTI_LINE_COMMENT_NO_LB_REGEXP + ")"))
    ))).skip();
    b.rule(NEXT_NOT_LB).is(b.nextNot(b.regexp("(?:" + "[\n\r]" + "|" + MULTI_LINE_COMMENT_REGEXP + ")"))).skip();

    b.rule(EOS).is(b.firstOf(
      b.sequence(SPACING, ";"),
      b.sequence(SPACING_NO_LB, b.regexp("(?:\\n|\\r\\n|\\r)")),
      b.sequence(SPACING_NO_LB, b.next("}")),
      b.sequence(SPACING, b.endOfInput())
    ));
    b.rule(EOS_NO_LB).is(b.firstOf(
      b.sequence(SPACING_NO_LB, ";"),
      b.sequence(SPACING_NO_LB, b.regexp("(?:\\n|\\r\\n|\\r)")),
      b.sequence(SPACING_NO_LB, b.next("}")),
      b.sequence(SPACING_NO_LB, b.endOfInput())
    ));

    punctuators(b);
    keywords(b);
    literals(b);
    expressions(b);
    statements(b);
    directives(b);
    definitions(b);
    xml(b);

    b.setRootRule(PROGRAM);

    return b.build();
  }

  private static void literals(LexerlessGrammarBuilder b) {
    b.rule(STRING).is(
      SPACING,
      b.firstOf(
        b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""),
        b.regexp("\'([^\'\\\\]*+(\\\\[\\s\\S])?+)*+\'")
      ));

    b.rule(HEXADECIMAL).is(SPACING, b.regexp("0[xX][0-9a-fA-F]++"));
    b.rule(OCTAL).is(SPACING, b.regexp("0[0-7]++"));
    b.rule(DECIMAL).is(SPACING, b.regexp(DECIMAL_REGEXP));
    b.rule(NUMBER).is(b.firstOf(OCTAL, DECIMAL, HEXADECIMAL));

    // Regular expression according to ECMA 262
    b.rule(REGULAR_EXPRESSION).is(SPACING, b.regexp(
      "/"
        // Regular expression first char
        + "([^\\n\\r\\*\\\\/]|(\\\\[^\\n\\r]))"
        // Regular expression chars
        + "([^\\n\\r\\\\/]|(\\\\[^\\n\\r]))*"
        + "/"
        // Regular expression flags
        + IDENTIFIER_PART_REGEXP + "*+"));
  }

  private static void expressions(LexerlessGrammarBuilder b) {
    // Identifiers
    b.rule(IDENTIFIER).is(b.firstOf(
      DYNAMIC,
      EACH,
      GET,
      INCLUDE,
      NAMESPACE,
      SET,
      STATIC,
      b.sequence(SPACING, b.nextNot(KEYWORDS), b.regexp(IDENTIFIER_START_REGEXP + IDENTIFIER_PART_REGEXP + "*+"))
    ));
    b.rule(IDENTIFIER_PART).is(b.regexp(IDENTIFIER_PART_REGEXP));

    b.rule(PROPERTY_IDENTIFIER).is(b.firstOf(
      IDENTIFIER,
      STAR));

    b.rule(QUALIFIER).is(b.firstOf(
      PROPERTY_IDENTIFIER,
      RESERVED_NAMESPACE));

    b.rule(SIMPLE_QUALIFIED_IDENTIFIER).is(b.firstOf(
      b.sequence(QUALIFIER, DOUBLE_COLON, PROPERTY_IDENTIFIER),
      b.sequence(QUALIFIER, DOUBLE_COLON, BRACKETS),
      PROPERTY_IDENTIFIER));

    b.rule(EXPR_QUALIFIED_IDENTIFIER).is(b.firstOf(
      b.sequence(PARENTHESIZED_EXPR, DOUBLE_COLON, PROPERTY_IDENTIFIER),
      b.sequence(PARENTHESIZED_EXPR, BRACKETS)));

    b.rule(NON_ATTRIBUTE_QUALIFIED_IDENTIFIER).is(b.firstOf(
      SIMPLE_QUALIFIED_IDENTIFIER,
      EXPR_QUALIFIED_IDENTIFIER));

    b.rule(QUALIFIED_IDENTIFIER).is(b.firstOf(
      b.sequence(AT_SIGN, BRACKETS),
      b.sequence(AT_SIGN, NON_ATTRIBUTE_QUALIFIED_IDENTIFIER),
      NON_ATTRIBUTE_QUALIFIED_IDENTIFIER));

    b.rule(PRIMARY_EXPR).is(b.firstOf(
      NULL,
      TRUE,
      FALSE,
      HEXADECIMAL,
      NUMBER,
      STRING,
      THIS,
      REGULAR_EXPRESSION,
      XML_INITIALISER,
      QUALIFIED_IDENTIFIER,
      RESERVED_NAMESPACE,
      PARENTHESIZED_EXPR,
      ARRAY_INITIALISER,
      OBJECT_INITIALISER,
      FUNCTION_EXPR));

    b.rule(RESERVED_NAMESPACE).is(b.firstOf(PUBLIC, PRIVATE, PROTECTED, INTERNAL));

    b.rule(PARENTHESIZED_EXPR).is(LPARENTHESIS, ASSIGNMENT_EXPR, RPARENTHESIS);
    b.rule(PARENTHESIZED_LIST_EXPR).is(LPARENTHESIS, LIST_EXPRESSION, RPARENTHESIS);

    b.rule(FUNCTION_EXPR).is(b.firstOf(
      b.sequence(FUNCTION, FUNCTION_COMMON),
      b.sequence(FUNCTION, IDENTIFIER, FUNCTION_COMMON)));

    b.rule(OBJECT_INITIALISER).is(LCURLYBRACE, b.optional(LITERAL_FIELD, b.zeroOrMore(COMMA, LITERAL_FIELD)), RCURLYBRACE);
    b.rule(LITERAL_FIELD).is(FIELD_NAME, COLON, ASSIGNMENT_EXPR);
    b.rule(FIELD_NAME).is(b.firstOf(
      NON_ATTRIBUTE_QUALIFIED_IDENTIFIER,
      STRING,
      NUMBER));

    // Array initialiser
    b.rule(ARRAY_INITIALISER).is(LBRAKET, b.optional(ELEMENT_LIST), RBRAKET);
    b.rule(ELEMENT_LIST).is(b.optional(COMMA), LITERAL_ELEMENT, b.zeroOrMore(COMMA, LITERAL_ELEMENT), b.optional(COMMA));
    b.rule(LITERAL_ELEMENT).is(ASSIGNMENT_EXPR);

    // Assignement expressions
    b.rule(ASSIGNMENT_EXPR).is(b.firstOf(
      b.sequence(POSTFIX_EXPR, ASSIGNMENT_OPERATOR, ASSIGNMENT_EXPR),
      CONDITIONAL_EXPR));
    b.rule(ASSIGNMENT_EXPR_NO_IN).is(b.firstOf(
      b.sequence(POSTFIX_EXPR, ASSIGNMENT_OPERATOR, ASSIGNMENT_EXPR_NO_IN),
      CONDITIONAL_EXPR));
    b.rule(ASSIGNMENT_OPERATOR).is(b.firstOf(EQUAL1, COMPOUND_ASSIGNMENT, LOGICAL_ASSIGNMENT));
    b.rule(COMPOUND_ASSIGNMENT).is(b.firstOf(STAR_EQU, DIV_EQU, MOD_EQU, PLUS_EQU, MINUS_EQU, SL_EQU, SR_EQU, SR_EQU2, AND_EQU, XOR_EQU, OR_EQU));
    b.rule(LOGICAL_ASSIGNMENT).is(b.firstOf(ANDAND_EQU, XORXOR_EQU, OROR_EQU));

    // Super expression
    b.rule(SUPER_EXPR).is(b.firstOf(
      b.sequence(SUPER, ARGUMENTS),
      SUPER));

    b.rule(POSTFIX_EXPR).is(b.firstOf(
      FULL_NEW_EXPR,
      b.sequence(SUPER_EXPR, PROPERTY_OPERATOR),
      PRIMARY_EXPR,
      SHORT_NEW_EXPR),
      b.zeroOrMore(b.firstOf(
        PROPERTY_OPERATOR,
        ARGUMENTS,
        QUERY_OPERATOR,
        /* No line break */ b.sequence(SPACING_NO_LB, NEXT_NOT_LB, DOUBLE_PLUS),
        /* No line break */ b.sequence(SPACING_NO_LB, NEXT_NOT_LB, DOUBLE_MINUS)))
    );

    // New expressions
    b.rule(FULL_NEW_EXPR).is(NEW, b.firstOf(FULL_NEW_SUB_EXPR, VECTOR_LITERAL_EXPRESSION), ARGUMENTS);
    b.rule(FULL_NEW_SUB_EXPR).is(b.firstOf(
      PRIMARY_EXPR,
      b.sequence(FULL_NEW_EXPR, PROPERTY_OPERATOR),
      FULL_NEW_EXPR,
      b.sequence(SUPER_EXPR, PROPERTY_OPERATOR)));

    b.rule(SHORT_NEW_EXPR).is(NEW, b.firstOf(SHORT_NEW_SUB_EXPR, VECTOR_LITERAL_EXPRESSION));
    b.rule(SHORT_NEW_SUB_EXPR).is(b.firstOf(
      FULL_NEW_SUB_EXPR,
      SHORT_NEW_EXPR));

    // Property accessors
    b.rule(PROPERTY_OPERATOR).is(b.firstOf(
      b.sequence(DOT, QUALIFIED_IDENTIFIER),
      // not in specs:
      TYPE_APPLICATION,
      BRACKETS));
    b.rule(BRACKETS).is(LBRAKET, LIST_EXPRESSION, RBRAKET);

    // Query operators
    b.rule(QUERY_OPERATOR).is(b.firstOf(
      b.sequence(DOUBLE_DOT, QUALIFIED_IDENTIFIER),
      b.sequence(DOT, LPARENTHESIS, LIST_EXPRESSION, RPARENTHESIS)));

    // Call expresions
    b.rule(ARGUMENTS).is(LPARENTHESIS, b.optional(LIST_EXPRESSION), RPARENTHESIS);

    // Unary expression
    b.rule(UNARY_EXPR).is(b.firstOf(
      b.sequence(b.firstOf(DELETE, DOUBLE_PLUS, DOUBLE_MINUS), POSTFIX_EXPR),
      b.sequence(b.firstOf(VOID, TYPEOF, PLUS, MINUS, NOT, TILD), UNARY_EXPR),
      POSTFIX_EXPR)).skipIfOneChild();

    // Binary expressions
    b.rule(MULTIPLICATIVE_EXPR).is(UNARY_EXPR, b.zeroOrMore(b.firstOf(STAR, DIV, MOD), UNARY_EXPR)).skipIfOneChild();
    b.rule(ADDITIVE_EXPR).is(MULTIPLICATIVE_EXPR, b.zeroOrMore(ADDITIVE_OPERATOR, MULTIPLICATIVE_EXPR)).skipIfOneChild();
    b.rule(ADDITIVE_OPERATOR).is(b.firstOf(PLUS, MINUS, /* Action Script 2: */ word(b, "add")));
    b.rule(SHIFT_EXPR).is(ADDITIVE_EXPR, b.zeroOrMore(b.firstOf(SL, SR2, SR), ADDITIVE_EXPR)).skipIfOneChild();

    b.rule(RELATIONAL_EXPR).is(SHIFT_EXPR, b.zeroOrMore(RELATIONAL_OPERATOR, SHIFT_EXPR)).skipIfOneChild();
    b.rule(RELATIONAL_EXPR_NO_IN).is(SHIFT_EXPR, b.zeroOrMore(RELATIONAL_OPERATOR_NO_IN, SHIFT_EXPR)).skipIfOneChild();
    b.rule(RELATIONAL_OPERATOR).is(b.firstOf(LE, GE, LT, GT, IN, INSTANCEOF, IS, AS, /* Action Script 2: */ word(b, "le"), word(b, "ge"), word(b, "lt"), word(b, "gt")));
    b.rule(RELATIONAL_OPERATOR_NO_IN).is(b.firstOf(LE, GE, LT, GT, INSTANCEOF, IS, AS, /* Action Script 2: */ word(b, "le"), word(b, "ge"), word(b, "lt"), word(b, "gt")));

    b.rule(EQUALITY_EXPR).is(RELATIONAL_EXPR, b.zeroOrMore(EQUALITY_OPERATOR, RELATIONAL_EXPR)).skipIfOneChild();
    b.rule(EQUALITY_EXPR_NO_IN).is(RELATIONAL_EXPR_NO_IN, b.zeroOrMore(EQUALITY_OPERATOR, RELATIONAL_EXPR_NO_IN)).skipIfOneChild();
    b.rule(EQUALITY_OPERATOR).is(b.firstOf(
      NOTEQUAL2,
      EQUAL3,
      EQUAL2,
      NOTEQUAL1,
      /* ActionScript 2: */
      b.sequence(SPACING, "<>"),
      word(b, "eq"),
      word(b, "ne")
    ));

    b.rule(BITEWISE_AND_EXPR).is(EQUALITY_EXPR, b.zeroOrMore(AND, EQUALITY_EXPR)).skipIfOneChild();
    b.rule(BITEWISE_AND_EXPR_NO_IN).is(EQUALITY_EXPR_NO_IN, b.zeroOrMore(AND, EQUALITY_EXPR_NO_IN)).skipIfOneChild();

    b.rule(BITEWISE_XOR_EXPR).is(BITEWISE_AND_EXPR, b.zeroOrMore(XOR, BITEWISE_AND_EXPR)).skipIfOneChild();
    b.rule(BITEWISE_XOR_EXPR_NO_IN).is(BITEWISE_AND_EXPR_NO_IN, b.zeroOrMore(XOR, BITEWISE_AND_EXPR_NO_IN)).skipIfOneChild();

    b.rule(BITEWISE_OR_EXPR).is(BITEWISE_XOR_EXPR, b.zeroOrMore(OR, BITEWISE_XOR_EXPR)).skipIfOneChild();
    b.rule(BITEWISE_OR_EXPR_NO_IN).is(BITEWISE_XOR_EXPR_NO_IN, b.zeroOrMore(OR, BITEWISE_XOR_EXPR_NO_IN)).skipIfOneChild();

    b.rule(LOGICAL_AND_EXPR).is(BITEWISE_OR_EXPR, b.zeroOrMore(LOGICAL_AND_OPERATOR, BITEWISE_XOR_EXPR)).skipIfOneChild();
    b.rule(LOGICAL_AND_EXPR_NO_IN).is(BITEWISE_OR_EXPR_NO_IN, b.zeroOrMore(LOGICAL_AND_OPERATOR, BITEWISE_XOR_EXPR_NO_IN)).skipIfOneChild();
    b.rule(LOGICAL_AND_OPERATOR).is(b.firstOf(
      ANDAND,
      /* ActionScript 2: */
      b.sequence(SPACING, "and", b.nextNot(IDENTIFIER_PART))
    ));

    b.rule(LOGICAL_OR_EXPR).is(LOGICAL_AND_EXPR, b.zeroOrMore(LOGICAL_OR_OPERATOR, LOGICAL_AND_EXPR)).skipIfOneChild();
    b.rule(LOGICAL_OR_EXPR_NO_IN).is(LOGICAL_AND_EXPR_NO_IN, b.zeroOrMore(LOGICAL_OR_OPERATOR, LOGICAL_AND_EXPR_NO_IN)).skipIfOneChild();
    b.rule(LOGICAL_OR_OPERATOR).is(b.firstOf(
      OROR,
      /* ActionScript 2: */
      b.sequence(SPACING, "or", b.nextNot(IDENTIFIER_PART))
    ));

    // Conditional expression
    b.rule(CONDITIONAL_EXPR).is(LOGICAL_OR_EXPR, b.optional(QUERY, ASSIGNMENT_EXPR, COLON, ASSIGNMENT_EXPR)).skipIfOneChild();
    b.rule(CONDITIONAL_EXPR_NO_IN).is(LOGICAL_OR_EXPR_NO_IN, b.optional(QUERY, ASSIGNMENT_EXPR_NO_IN, COLON, ASSIGNMENT_EXPR_NO_IN)).skipIfOneChild();

    // Non assignment expression
    b.rule(NON_ASSIGNMENT_EXPR).is(LOGICAL_OR_EXPR, b.optional(QUERY, NON_ASSIGNMENT_EXPR, COLON, NON_ASSIGNMENT_EXPR)).skipIfOneChild();
    b.rule(NON_ASSIGNMENT_EXPR_NO_IN).is(LOGICAL_OR_EXPR_NO_IN, b.optional(QUERY, NON_ASSIGNMENT_EXPR_NO_IN, COLON, NON_ASSIGNMENT_EXPR_NO_IN)).skipIfOneChild();

    b.rule(LIST_EXPRESSION).is(ASSIGNMENT_EXPR, b.zeroOrMore(b.sequence(COMMA, ASSIGNMENT_EXPR)));
    b.rule(LIST_EXPRESSION_NO_IN).is(ASSIGNMENT_EXPR_NO_IN, b.zeroOrMore(b.sequence(COMMA, ASSIGNMENT_EXPR_NO_IN)));

    b.rule(TYPE_EXPR).is(b.firstOf(
      STAR,
      b.sequence(/* Godin: not sure about QUALIFIED_IDENTIFIER, but it works: */QUALIFIED_IDENTIFIER, b.zeroOrMore(DOT, QUALIFIED_IDENTIFIER), b.optional(TYPE_APPLICATION))
    ));
    b.rule(TYPE_APPLICATION).is(DOT, LT, TYPE_EXPRESSION_LIST, GT);
    b.rule(TYPE_EXPR_NO_IN).is(TYPE_EXPR);

    b.rule(VECTOR_LITERAL_EXPRESSION).is(LT, TYPE_EXPR, GT, BRACKETS);
  }

  private static void statements(LexerlessGrammarBuilder b) {
    b.rule(STATEMENT).is(b.firstOf(
      SUPER_STATEMENT,
      BLOCK,
      IF_STATEMENT,
      SWITCH_STATEMENT,
      DO_STATEMENT,
      WHILE_STATEMENT,
      FOR_STATEMENT,
      WITH_STATEMENT,
      CONTINUE_STATEMENT,
      BREAK_STATEMENT,
      RETURN_STATEMENT,
      THROW_STATEMENT,
      TRY_STATEMENT,
      b.sequence(EXPRESSION_STATEMENT, EOS),
      LABELED_STATEMENT));

    b.rule(SUB_STATEMENT).is(b.firstOf(
      EMPTY_STATEMENT,
      STATEMENT,
      VARIABLE_DECLARATION_STATEMENT));

    b.rule(VARIABLE_DECLARATION_STATEMENT).is(VARIABLE_DEF, EOS);

    b.rule(EMPTY_STATEMENT).is(SEMICOLON);

    b.rule(SUPER_STATEMENT).is(SUPER, ARGUMENTS, EOS);

    b.rule(BLOCK).is(LCURLYBRACE, DIRECTIVES, RCURLYBRACE);

    b.rule(LABELED_STATEMENT).is(IDENTIFIER, COLON, SUB_STATEMENT);

    b.rule(IF_STATEMENT).is(IF, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT, b.optional(ELSE, SUB_STATEMENT));

    b.rule(SWITCH_STATEMENT).is(SWITCH, PARENTHESIZED_LIST_EXPR, LCURLYBRACE, b.zeroOrMore(CASE_ELEMENT), RCURLYBRACE);
    b.rule(CASE_ELEMENT).is(b.oneOrMore(CASE_LABEL), b.zeroOrMore(DIRECTIVE));
    b.rule(CASE_LABEL).is(b.firstOf(DEFAULT, b.sequence(CASE, LIST_EXPRESSION)), COLON);

    b.rule(DO_STATEMENT).is(DO, SUB_STATEMENT, WHILE, PARENTHESIZED_LIST_EXPR, EOS);

    b.rule(WHILE_STATEMENT).is(WHILE, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT);

    b.rule(FOR_STATEMENT).is(b.firstOf(
      b.sequence(FOR, LPARENTHESIS, b.optional(FOR_INITIALISER), SEMICOLON, b.optional(LIST_EXPRESSION), SEMICOLON, b.optional(LIST_EXPRESSION), RPARENTHESIS, SUB_STATEMENT),
      b.sequence(FOR, LPARENTHESIS, FOR_IN_BINDING, IN, LIST_EXPRESSION, RPARENTHESIS, SUB_STATEMENT),
      b.sequence(FOR, b.sequence(/* No line break */ SPACING_NO_LB, NEXT_NOT_LB, EACH, LPARENTHESIS, FOR_IN_BINDING, IN, LIST_EXPRESSION, RPARENTHESIS, SUB_STATEMENT))));
    b.rule(FOR_INITIALISER).is(b.firstOf(LIST_EXPRESSION_NO_IN, VARIABLE_DEF_NO_IN));
    b.rule(FOR_IN_BINDING).is(b.firstOf(
      b.sequence(VARIABLE_DEF_KIND, VARIABLE_BINDING_NO_IN),
      POSTFIX_EXPR));

    b.rule(CONTINUE_STATEMENT).is(CONTINUE, b.firstOf(
      b.sequence(/* No line break */ SPACING_NO_LB, NEXT_NOT_LB, IDENTIFIER, EOS),
      EOS_NO_LB));

    b.rule(BREAK_STATEMENT).is(BREAK, b.firstOf(
      b.sequence(/* No line break */ SPACING_NO_LB, NEXT_NOT_LB, IDENTIFIER, EOS),
      EOS_NO_LB));

    b.rule(WITH_STATEMENT).is(WITH, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT);

    b.rule(RETURN_STATEMENT).is(RETURN, b.firstOf(
      b.sequence(/* No line break */ SPACING_NO_LB, NEXT_NOT_LB, LIST_EXPRESSION, EOS),
      EOS_NO_LB));

    b.rule(THROW_STATEMENT).is(THROW, b.firstOf(
      b.sequence(/* No line break */ SPACING_NO_LB, NEXT_NOT_LB, LIST_EXPRESSION, EOS),
      EOS_NO_LB));

    b.rule(TRY_STATEMENT).is(TRY, BLOCK, b.firstOf(
      b.sequence(CATCH_CLAUSES, b.optional(FINALLY, BLOCK)),
      b.sequence(FINALLY, BLOCK)
    ));
    b.rule(CATCH_CLAUSES).is(CATCH_CLAUSE, b.zeroOrMore(CATCH_CLAUSE));
    b.rule(CATCH_CLAUSE).is(CATCH, LPARENTHESIS, PARAMETER, RPARENTHESIS, BLOCK);

    b.rule(EXPRESSION_STATEMENT).is(b.nextNot(b.firstOf(FUNCTION, LCURLYBRACE)), LIST_EXPRESSION);
  }

  private static void directives(LexerlessGrammarBuilder b) {
    b.rule(DIRECTIVE).is(b.firstOf(
      CONFIG_CONDITION,
      EMPTY_STATEMENT,
      ANNOTABLE_DIRECTIVE,
      STATEMENT,
      DEFAULT_XML_NAMESPACE_DIRECTIVE,
      b.sequence(ATTRIBUTES, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, ANNOTABLE_DIRECTIVE),
      b.sequence(INCLUDE_DIRECTIVE, /* No line break */ EOS_NO_LB),
      b.sequence(IMPORT_DIRECTIVE, /* No line break */ EOS_NO_LB),
      b.sequence(USE_DIRECTIVE, /* No line break */ EOS_NO_LB)));

    b.rule(CONFIG_CONDITION).is(IDENTIFIER, DOUBLE_COLON, IDENTIFIER, LCURLYBRACE, DIRECTIVES, RCURLYBRACE);

    b.rule(ANNOTABLE_DIRECTIVE).is(b.firstOf(
      VARIABLE_DECLARATION_STATEMENT,
      FUNCTION_DEF,
      CLASS_DEF,
      INTERFACE_DEF,
      NAMESPACE_DEF));

    b.rule(DIRECTIVES).is(b.zeroOrMore(DIRECTIVE));

    b.rule(ATTRIBUTES).is(b.oneOrMore(ATTRIBUTE));
    b.rule(ATTRIBUTE_COMBINATION).is(ATTRIBUTE, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, ATTRIBUTES);
    b.rule(ATTRIBUTE).is(b.firstOf(
      b.sequence(/* hack: */b.nextNot(NAMESPACE), ATTRIBUTE_EXPR),
      RESERVED_NAMESPACE,
      b.sequence(LBRAKET, ASSIGNMENT_EXPR, RBRAKET)));
    b.rule(ATTRIBUTE_EXPR).is(IDENTIFIER, b.zeroOrMore(PROPERTY_OPERATOR));

    b.rule(IMPORT_DIRECTIVE).is(IMPORT, PACKAGE_NAME, b.optional(DOT, STAR));

    b.rule(INCLUDE_DIRECTIVE).is(INCLUDE, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, STRING);

    b.rule(USE_DIRECTIVE).is(USE, NAMESPACE, LIST_EXPRESSION);

    b.rule(DEFAULT_XML_NAMESPACE_DIRECTIVE).is(DEFAULT, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, XML,
    /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, NAMESPACE, EQUAL1, NON_ASSIGNMENT_EXPR, EOS);
  }

  private static void definitions(LexerlessGrammarBuilder b) {
    b.rule(VARIABLE_DEF).is(VARIABLE_DEF_KIND, VARIABLE_BINDING_LIST);
    b.rule(VARIABLE_DEF_NO_IN).is(VARIABLE_DEF_KIND, VARIABLE_BINDING_LIST_NO_IN);

    b.rule(VARIABLE_DEF_KIND).is(b.firstOf(VAR, CONST));

    b.rule(VARIABLE_BINDING_LIST).is(VARIABLE_BINDING, b.zeroOrMore(COMMA, VARIABLE_BINDING));
    b.rule(VARIABLE_BINDING_LIST_NO_IN).is(VARIABLE_BINDING_NO_IN, b.zeroOrMore(COMMA, VARIABLE_BINDING_NO_IN));

    b.rule(VARIABLE_BINDING).is(TYPED_IDENTIFIER, b.optional(VARIABLE_INITIALISATION));
    b.rule(VARIABLE_BINDING_NO_IN).is(TYPED_IDENTIFIER_NO_IN, b.optional(VARIABLE_INITIALISATION_NO_IN));

    b.rule(VARIABLE_INITIALISATION).is(EQUAL1, VARIABLE_INITIALISER);
    b.rule(VARIABLE_INITIALISATION_NO_IN).is(EQUAL1, VARIABLE_INITIALISER_NO_IN);

    b.rule(VARIABLE_INITIALISER).is(b.firstOf(
      ASSIGNMENT_EXPR,
      ATTRIBUTE_COMBINATION));
    b.rule(VARIABLE_INITIALISER_NO_IN).is(b.firstOf(
      ASSIGNMENT_EXPR_NO_IN,
      ATTRIBUTE_COMBINATION));

    b.rule(TYPED_IDENTIFIER).is(b.firstOf(
      b.sequence(IDENTIFIER, COLON, TYPE_EXPR),
      IDENTIFIER));
    b.rule(TYPED_IDENTIFIER_NO_IN).is(b.firstOf(
      b.sequence(IDENTIFIER, COLON, TYPE_EXPR_NO_IN),
      IDENTIFIER));

    b.rule(FUNCTION_DEF).is(FUNCTION, FUNCTION_NAME, FUNCTION_COMMON);
    b.rule(FUNCTION_NAME).is(b.firstOf(
      b.sequence(GET, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, IDENTIFIER),
      b.sequence(SET, /* No line break */ SPACING_NO_LB, NEXT_NOT_LB, IDENTIFIER),
      IDENTIFIER));

    b.rule(FUNCTION_COMMON).is(b.firstOf(
      b.sequence(FUNCTION_SIGNATURE, BLOCK),
      b.sequence(FUNCTION_SIGNATURE, EOS)));

    b.rule(FUNCTION_SIGNATURE).is(b.sequence(LPARENTHESIS, b.optional(PARAMETERS), RPARENTHESIS, RESULT_TYPE));

    b.rule(PARAMETERS).is(b.firstOf(
      b.sequence(PARAMETER, b.zeroOrMore(COMMA, PARAMETER), b.optional(COMMA, REST_PARAMETERS)),
      REST_PARAMETERS
    ));

    b.rule(PARAMETER).is(b.firstOf(
      b.sequence(TYPED_IDENTIFIER, EQUAL1, ASSIGNMENT_EXPR),
      TYPED_IDENTIFIER));

    b.rule(REST_PARAMETERS).is(b.firstOf(
      b.sequence(TRIPLE_DOTS, TYPED_IDENTIFIER),
      TRIPLE_DOTS));

    b.rule(RESULT_TYPE).is(b.optional(COLON, b.firstOf(VOID, TYPE_EXPR)));

    b.rule(CLASS_DEF).is(CLASS, CLASS_NAME, b.optional(INHERITENCE), BLOCK);
    b.rule(CLASS_NAME).is(CLASS_IDENTIFIERS);
    b.rule(CLASS_IDENTIFIERS).is(IDENTIFIER, b.zeroOrMore(b.sequence(DOT, IDENTIFIER)));
    b.rule(INHERITENCE).is(b.firstOf(
      b.sequence(IMPLEMENTS, TYPE_EXPRESSION_LIST),
      b.sequence(EXTENDS, TYPE_EXPR, IMPLEMENTS, TYPE_EXPRESSION_LIST),
      b.sequence(EXTENDS, TYPE_EXPR)));

    b.rule(TYPE_EXPRESSION_LIST).is(TYPE_EXPR, b.zeroOrMore(b.sequence(COMMA, TYPE_EXPR)));

    b.rule(INTERFACE_DEF).is(INTERFACE, CLASS_NAME, b.optional(EXTENDS_LIST), BLOCK);
    b.rule(EXTENDS_LIST).is(EXTENDS, TYPE_EXPRESSION_LIST);

    b.rule(PACKAGE_DEF).is(PACKAGE, b.optional(PACKAGE_NAME), BLOCK);
    b.rule(PACKAGE_NAME).is(IDENTIFIER, b.zeroOrMore(DOT, IDENTIFIER));

    b.rule(NAMESPACE_DEF).is(NAMESPACE, NAMESPACE_BINDING, EOS);
    b.rule(NAMESPACE_BINDING).is(IDENTIFIER, b.optional(NAMESPACE_INITIALISATION));
    b.rule(NAMESPACE_INITIALISATION).is(EQUAL1, ASSIGNMENT_EXPR);

    b.rule(PROGRAM).is(
      b.firstOf(
        b.sequence(PACKAGE_DEF, PROGRAM),
        DIRECTIVES),
      SPACING,
      b.token(GenericTokenType.EOF, b.endOfInput()));
  }

  private static void xml(LexerlessGrammarBuilder b) {
    b.rule(XML_INITIALISER).is(b.firstOf(
      XML_MARKUP,
      XML_ELEMENT,
      b.sequence(LT, GT, XML_ELEMENT_CONTENT, LT, DIV, GT)));

    b.rule(XML_ELEMENT).is(b.firstOf(
      b.sequence(LT, XML_TAG_CONTENT, b.optional(XML_WHITESPACE), DIV, GT),
      b.sequence(LT, XML_TAG_CONTENT, b.optional(XML_WHITESPACE), XML_ELEMENT_CONTENT, LT, DIV, XML_TAG_NAME, b.optional(XML_WHITESPACE), GT)));

    b.rule(XML_TAG_CONTENT).is(XML_TAG_NAME, XML_ATTRIBUTES);

    b.rule(XML_TAG_NAME).is(b.firstOf(
      b.sequence(LCURLYBRACE, EXPRESSION_STATEMENT, RCURLYBRACE),
      XML_NAME));

    b.rule(XML_ATTRIBUTES).is(b.optional(b.firstOf(
      b.sequence(XML_ATTRIBUTE, XML_ATTRIBUTES),
      b.sequence(XML_WHITESPACE, LCURLYBRACE, EXPRESSION_STATEMENT, RCURLYBRACE))));

    b.rule(XML_ATTRIBUTE).is(b.firstOf(
      b.sequence(b.zeroOrMore(XML_WHITESPACE), XML_NAME,
        b.zeroOrMore(XML_WHITESPACE), EQUAL1, b.zeroOrMore(XML_WHITESPACE),
        LCURLYBRACE, EXPRESSION_STATEMENT, RCURLYBRACE),
      b.sequence(b.zeroOrMore(XML_WHITESPACE), XML_NAME,
        b.zeroOrMore(XML_WHITESPACE), EQUAL1, b.zeroOrMore(XML_WHITESPACE),
        XML_ATTRIBUTE_VALUE)));

    b.rule(XML_ELEMENT_CONTENT).is(b.optional(
      b.firstOf(
        b.sequence(LCURLYBRACE, EXPRESSION_STATEMENT, RCURLYBRACE, XML_ELEMENT_CONTENT),
        b.sequence(XML_MARKUP, XML_ELEMENT_CONTENT),
        b.sequence(XML_TEXT, XML_ELEMENT_CONTENT),
        b.sequence(XML_ELEMENT, XML_ELEMENT_CONTENT)
      )));

    b.rule(XML_MARKUP).is(b.firstOf(
      XML_COMMENT,
      XML_CDATA,
      XML_PI));

    b.rule(XML_COMMENT).is(SPACING, b.regexp("<!--(?:(?!--)[\\s\\S])*?-->"));
    b.rule(XML_CDATA).is(SPACING, b.regexp("<!\\[CDATA\\[(?:(?!]])[\\s\\S])*?]]>"));
    b.rule(XML_PI).is(SPACING, b.regexp("<\\?(?:(?!\\?>)[\\s\\S])*?\\?>"));
    b.rule(XML_TEXT).is(SPACING, b.regexp("[^{<]++"));
    b.rule(XML_NAME).is(SPACING, b.regexp("[" + UNICODE_LETTER + "_:" + "]" + "[" + UNICODE_LETTER + UNICODE_DIGIT + "\\.\\-_:" + "]*"));
    b.rule(XML_ATTRIBUTE_VALUE).is(b.regexp("(\"([^\"]*[//s//S]*)\")|(\'([^\']*[//s//S]*)\')"));
    b.rule(XML_WHITESPACE).is(b.regexp("[ \\t\\r\\n]+"));
  }

  private static void keywords(LexerlessGrammarBuilder b) {
    for (FlexKeyword k : FlexKeyword.values()) {
      b.rule(k).is(SPACING, k.getValue(), b.nextNot(IDENTIFIER_PART));
    }

    List<FlexKeyword> keywords = FlexKeyword.keywords();
    Object[] rest = new Object[keywords.size() - 2];
    for (int i = 2; i < keywords.size(); i++) {
      rest[i - 2] = keywords.get(i);
    }
    b.rule(KEYWORDS).is(b.firstOf(keywords.get(0), keywords.get(1), rest));
  }

  private static void punctuators(LexerlessGrammarBuilder b) {
    for (FlexPunctuator p : FlexPunctuator.values()) {
      b.rule(p).is(SPACING, p.getValue());
    }
  }

  private static Object word(LexerlessGrammarBuilder b, String word) {
    return b.sequence(SPACING, word, b.nextNot(IDENTIFIER_PART));
  }

}
