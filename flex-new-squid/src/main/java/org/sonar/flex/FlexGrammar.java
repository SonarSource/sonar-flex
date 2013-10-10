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
package org.sonar.flex;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.internal.vm.FirstOfExpression;
import org.sonar.sslr.parser.LexerlessGrammar;

public enum FlexGrammar implements GrammarRuleKey {

  /**
   * KEYWORDS
   */
  // <editor-fold defaultstate="collapsed" desc="Keywords">
  AS,
  BREAK,
  CASE,
  CATCH,
  CLASS,
  CONST,
  CONTINUE,
  DEFAULT,
  DELETE,
  DO,
  ELSE,
  EXTENDS,
  FALSE,
  FINALLY,
  FOR,
  FUNCTION,
  IF,
  IMPLEMENTS,
  IMPORT,
  IN,
  INSTANCEOF,
  INTERFACE,
  INTERNAL,
  IS,
  NATIVE,
  NEW,
  NULL,
  PACKAGE,
  PRIVATE,
  PROTECTED,
  PUBLIC,
  RETURN,
  SUPER,
  SWITCH,
  THIS,
  THROW,
  TO,
  TRUE,
  TRY,
  TYPEOF,
  USE,
  VAR,
  VOID,
  WHILE,
  WITH,
  // </editor-fold>
  
  /**
   * SYNTATIC KEYWORDS 
   */
  // <editor-fold defaultstate="collapsed" desc="Syntactic Keywords">
  EACH,
  GET,
  SET,
  NAMESPACE,
  INCLUDE,
  DYNAMIC,
  FINAL,
  OVERRIDE,
  STATIC,
  STRING,
  NUMBER,
  WHITESPACE,
  IDENTIFIER_PART,
  // </editor-fold>
  
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
  FIELD_LIST,
  FIELD_NAME,
  NON_EMPTY_FIELD_LIST,
  LITERAL_FIELD,
  ARRAY_INITIALISER,
  ELEMENT_LIST,
  LITERAL_ELEMENT,
  CONDITIONAL_EXPR,
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
  SYNTACTIC_IDENTIFIER,
  LEXICAL_IDENTIFIER,
  // New expressions
  FULL_NEW_EXPR,
  FULL_NEW_SUB_EXPR,
  SHORT_NEW_EXPR,
  SHORT_NEW_SUB_EXPR,
  PROPERTY_OPERATOR,
  QUERY_OPERATOR,
  // Call expression
  ARGUMENTS,
  ARGUMENTS_LIST,
  // Unary expression
  UNARY_EXPR,
  // Binary expression
  MULTIPLICATIVE_EXPR,
  ADDITIVE_EXPR,
  SHIFT_EXPR,
  RELATIONAL_EXPR,
  EQUALITY_EXPR,
  BITEWISE_AND_EXPR,
  BITEWISE_XOR_EXPR,
  BITEWISE_OR_EXPR,
  LOGICAL_AND_EXPR,
  LOGICAL_OR_EXPR,
  // Assignment expression
  ASSIGNMENT_EXPR,
  // List expression
  LIST_EXPRESSION,
  // Non assignment expression
  NON_ASSIGNMENT_EXPR,
  // Type expression
  TYPE_EXPR,
  // </editor-fold>
  
  /**
   * DEFINITIONS
   */
  // <editor-fold defaultstate="collapsed" desc="Definitions">
  // Variable
  VARIABLE_DEF,
  VARIABLE_DEF_KIND,
  VARIABLE_BINDING_LIST,
  VARIABLE_BINDING,
  VARIABLE_INITIALISATION,
  TYPED_IDENTIFIER,
  VARIABLE_INITIALISER,
  // Function
  FUNCTION_DEF,
  FUNCTION_NAME,
  FUNCTION_COMMON,
  FUNCTION_SIGNATURE,
  RESULT_TYPE,
  PARAMETERS,
  PARAMETER,
  NON_EMPTY_PARAMETERS,
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
  BLOCK,
  EMPTY_STATEMENT,
  STATEMENT,
  SEMICOLON,
  // </editor-fold>
   
  /**
   * DIRECTIVES
   */
  // <editor-fold defaultstate="collapsed" desc="Directives">
  DIRECTIVES,
  DIRECTIVE,
  DIRECTIVES_PREFIX,
  ANNOTABLE_DIRECTIVE,
  USE_DIRECTIVE,
  IMPORT_DIRECTIVE,
  INCLUDE_DIRECTIVE,
  ATTRIBUTES,
  ATTRIBUTE,
  ATTRIBUTE_COMBINATION,
  ATTRIBUTE_EXPR,
  
  // </editor-fold>
  
  /**
   * PONCTUATORS
   */
  // <editor-fold defaultstate="collapsed" desc="Ponctuators">
  AT_SIGN,
  COMMA,
  COLON,
  DOUBLE_COLON,
  DOT,
  DOUBLE_DOT,
  RCURLYBARCE,
  LCURLYBARCE,
  RBRAKET,
  LBRAKET,
  RPARENTHESIS,
  LPARENTHESIS,
  STAR,
  PLUS,
  MINUS,
  DOUBLE_PLUS,
  DOUBLE_MINUS,
  DIV,
  MOD,
  NOT,
  EQU,
  EQUAL,
  EQUAL2,
  NOTEQUAL,
  NOTEQUAL2,
  SL,
  SR,
  SR2,
  STAR_EQU,
  DIV_EQU,
  MOD_EQU,
  PLUS_EQU,
  MINUS_EQU,
  SL_EQU,
  SR_EQU,
  SR_EQU2,
  AND_EQU,
  XOR_EQU,
  OR_EQU,
  ANDAND_EQU,
  XORXOR_EQU,
  OROR_EQU,
  LT,
  GT,
  LE,
  GE,
  AND,
  ANDAND,
  XOR,
  OR,
  OROR,
  QUERY,
  TILD,
  TRIPLE_DOTS;
  // </editor-fold>
  
  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    b.rule(WHITESPACE).is(b.regexp("\\s*"));
    b.rule(IDENTIFIER_PART).is(b.regexp("[a-zA-Z0-9]"), WHITESPACE);

    b.rule(STRING).is(b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""), WHITESPACE);
    b.rule(NUMBER).is(b.regexp("[0-9]+"), WHITESPACE);
    b.rule(SYNTACTIC_IDENTIFIER).is(b.regexp("[a-zA-Z0-9]+"), WHITESPACE);

    /** 
     * KEYWORDS
     */
    // <editor-fold defaultstate="collapsed" desc="Keywords definition">
    b.rule(AS).is("as", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(BREAK).is("break", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(CASE).is("case", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(CATCH).is("catch", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(CLASS).is("class", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(CONST).is("const", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(CONTINUE).is("continue", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(DEFAULT).is("default", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(DELETE).is("delete", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(DO).is("do", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(ELSE).is("else", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(EXTENDS).is("extends", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FALSE).is("false", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FINALLY).is("finally", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FOR).is("for", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FUNCTION).is("function", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(IF).is("if", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(IMPLEMENTS).is("implements", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(IMPORT).is("import", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(IN).is("in", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(INSTANCEOF).is("instanceof", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(INTERFACE).is("interface", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(INTERNAL).is("internal", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(IS).is("is", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(NATIVE).is("native", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(NEW).is("new", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(NULL).is("null", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(PACKAGE).is("package", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(PRIVATE).is("private", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(PROTECTED).is("protected", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(PUBLIC).is("plublic", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(RETURN).is("return", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(SUPER).is("super", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(SWITCH).is("switch", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(THIS).is("this", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(THROW).is("throw", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(TO).is("to", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(TRUE).is("true", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(TRY).is("try", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(TYPEOF).is("typeof", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(USE).is("use", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(VAR).is("var", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(VOID).is("void", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(WHILE).is("while", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(WITH).is("with", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    // </editor-fold>

    /** 
     * SYNTACTIC KEYWORDS
     */
    // <editor-fold defaultstate="collapsed" desc="Syntactic keywords definition">
    b.rule(EACH).is("each", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(GET).is("get", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(SET).is("set", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(NAMESPACE).is("namespace", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(INCLUDE).is("include", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(DYNAMIC).is("dynamic", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FINAL).is("final", b.nextNot(IDENTIFIER_PART), WHITESPACE); 
    b.rule(OVERRIDE).is("override", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(STATIC).is("static", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    // </editor-fold>
    
    /** 
     * PONCTUATORS
     */
    // <editor-fold defaultstate="collapsed" desc="Ponctuator definition">
    b.rule(AT_SIGN).is("@", WHITESPACE);
    b.rule(COMMA).is(",", WHITESPACE);
    b.rule(COLON).is(":", WHITESPACE);
    b.rule(DOUBLE_COLON).is("::", WHITESPACE);
    b.rule(DOT).is(".", WHITESPACE);
    b.rule(TRIPLE_DOTS).is("...", WHITESPACE);
    b.rule(DOUBLE_DOT).is("..", WHITESPACE);
    b.rule(RCURLYBARCE).is("}", WHITESPACE);
    b.rule(LCURLYBARCE).is("{", WHITESPACE);
    b.rule(RBRAKET).is("]", WHITESPACE);
    b.rule(LBRAKET).is("[", WHITESPACE);
    b.rule(RPARENTHESIS).is(")", WHITESPACE);
    b.rule(LPARENTHESIS).is("(", WHITESPACE);
    b.rule(DIV).is("/", WHITESPACE);
    b.rule(MOD).is("%", WHITESPACE);
    b.rule(STAR).is("*", WHITESPACE);
    b.rule(PLUS).is("+", WHITESPACE);
    b.rule(MINUS).is("-", WHITESPACE);
    b.rule(DOUBLE_PLUS).is("++", WHITESPACE);
    b.rule(DOUBLE_MINUS).is("--", WHITESPACE);
    b.rule(SL).is("<<", WHITESPACE);
    b.rule(SR).is(">>", WHITESPACE);
    b.rule(SR2).is(">>>", WHITESPACE);
    b.rule(NOT).is("!", WHITESPACE);
    b.rule(EQU).is("=", WHITESPACE);
    b.rule(EQUAL).is("==", WHITESPACE);
    b.rule(EQUAL2).is("===", WHITESPACE);
    b.rule(NOTEQUAL).is("!=", WHITESPACE);
    b.rule(NOTEQUAL2).is("!==", WHITESPACE);
    b.rule(STAR_EQU).is("*=", WHITESPACE);
    b.rule(DIV_EQU).is("/=", WHITESPACE);
    b.rule(MOD_EQU).is("%=", WHITESPACE);
    b.rule(PLUS_EQU).is("+=", WHITESPACE);
    b.rule(MINUS_EQU).is("-=", WHITESPACE);
    b.rule(SL_EQU).is("<<=", WHITESPACE);
    b.rule(SR_EQU).is(">>=", WHITESPACE);
    b.rule(SR_EQU2).is(">>>=", WHITESPACE);
    b.rule(AND_EQU).is("&=", WHITESPACE);
    b.rule(XOR_EQU).is("^=", WHITESPACE);
    b.rule(OR_EQU).is("|=", WHITESPACE);
    b.rule(ANDAND_EQU).is("&&=", WHITESPACE);
    b.rule(XORXOR_EQU).is("^^=", WHITESPACE);
    b.rule(OROR_EQU).is("||=", WHITESPACE);
    b.rule(LT).is("<", WHITESPACE);
    b.rule(GT).is(">", WHITESPACE);
    b.rule(LE).is("<=", WHITESPACE);
    b.rule(GE).is(">=", WHITESPACE);
    b.rule(XOR).is("^", WHITESPACE);
    b.rule(AND).is("&", WHITESPACE);
    b.rule(ANDAND).is("&&", WHITESPACE);
    b.rule(OR).is("|", WHITESPACE);
    b.rule(OROR).is("||", WHITESPACE);
    b.rule(QUERY).is("?", WHITESPACE);
    b.rule(TILD).is("~", WHITESPACE);
    // </editor-fold>

    /** 
     * EXPRESSIONS
     */
    // <editor-fold defaultstate="collapsed" desc="Expressions definition">
    // Identifiers
    b.rule(LEXICAL_IDENTIFIER).is(b.firstOf(
      DYNAMIC,
      EACH,
      GET,
      INCLUDE,
      NAMESPACE,
      SET,
      STATIC,
      SYNTACTIC_IDENTIFIER));

    b.rule(PROPERTY_IDENTIFIER).is(b.firstOf(
      SYNTACTIC_IDENTIFIER,
      STAR));

    b.rule(QUALIFIER).is(b.firstOf(
      PROPERTY_IDENTIFIER,
      RESERVED_NAMESPACE));

    b.rule(SIMPLE_QUALIFIED_IDENTIFIER).is(b.firstOf(
      PROPERTY_IDENTIFIER,
      b.sequence(QUALIFIER, DOUBLE_COLON, PROPERTY_IDENTIFIER,
      b.sequence(QUALIFIER, DOUBLE_COLON, BRACKETS))));

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

    //Primary expressions
    b.rule(PRIMARY_EXPR).is(b.firstOf(
      NULL,
      TRUE,
      FALSE,
      NUMBER,
      STRING,
      THIS,
      // Regular expression
      // XMLInitializer
      RESERVED_NAMESPACE,
      PARENTHESIZED_EXPR,
      ARRAY_INITIALISER,
      OBJECT_INITIALISER,
      QUALIFIED_IDENTIFIER,
      FUNCTION_EXPR));

    // Reseved namespace
    b.rule(RESERVED_NAMESPACE).is(b.firstOf(
      PUBLIC,
      PRIVATE,
      PROTECTED,
      INTERNAL));

    // Parenthesized expressions
    b.rule(PARENTHESIZED_EXPR).is(LPARENTHESIS, ASSIGNMENT_EXPR, RPARENTHESIS); // <!-- here
    b.rule(PARENTHESIZED_LIST_EXPR).is(b.firstOf(
      PARENTHESIZED_EXPR,
      b.sequence(LPARENTHESIS, LIST_EXPRESSION, COMMA,
      ASSIGNMENT_EXPR, RPARENTHESIS)));

    b.rule(FUNCTION_EXPR).is(b.firstOf(
      b.sequence(FUNCTION, FUNCTION_COMMON),
      b.sequence(FUNCTION, LEXICAL_IDENTIFIER, FUNCTION_COMMON)));

    // Object initialiser
    b.rule(OBJECT_INITIALISER).is(LCURLYBARCE, FIELD_LIST, RCURLYBARCE);

    b.rule(FIELD_LIST).is(b.optional(NON_EMPTY_FIELD_LIST));

    b.rule(NON_EMPTY_FIELD_LIST).is(b.firstOf(
      b.sequence(LITERAL_FIELD, COMMA, NON_EMPTY_FIELD_LIST),
      LITERAL_FIELD));

    b.rule(LITERAL_FIELD).is(FIELD_NAME, COLON, ASSIGNMENT_EXPR);

    b.rule(FIELD_NAME).is(b.firstOf(
      NON_ATTRIBUTE_QUALIFIED_IDENTIFIER,
      STRING,
      NUMBER));

    // Array initialiser
    b.rule(ARRAY_INITIALISER).is(LBRAKET, ELEMENT_LIST, RBRAKET);

    b.rule(ELEMENT_LIST).is(b.optional(b.firstOf(
      LITERAL_ELEMENT,
      b.sequence(COMMA, ELEMENT_LIST),
      b.sequence(LITERAL_ELEMENT, COMMA, ELEMENT_LIST))));

    b.rule(LITERAL_ELEMENT).is(ASSIGNMENT_EXPR);

    // Assignement expressions
    b.rule(ASSIGNMENT_EXPR).is(b.firstOf(
      CONDITIONAL_EXPR,
      b.sequence(POSTFIX_EXPR, EQUAL, ASSIGNMENT_EXPR),
      b.sequence(POSTFIX_EXPR, COMPOUND_ASSIGNMENT, ASSIGNMENT_EXPR),
      b.sequence(POSTFIX_EXPR, LOGICAL_ASSIGNMENT, ASSIGNMENT_EXPR)));

    b.rule(COMPOUND_ASSIGNMENT).is(b.firstOf(
      STAR_EQU,
      DIV_EQU,
      MOD_EQU,
      PLUS_EQU,
      MINUS_EQU,
      SL_EQU,
      SR_EQU,
      SR_EQU2,
      AND_EQU,
      XOR_EQU,
      OR_EQU));

    b.rule(LOGICAL_ASSIGNMENT).is(b.firstOf(
      ANDAND_EQU,
      XORXOR_EQU,
      OROR_EQU));

    // Super expression
    b.rule(SUPER_EXPR).is(b.firstOf(
      b.sequence(SUPER, ARGUMENTS),
      SUPER));

    // §14.10 Postfix expressions
    // Merged PostfixExpression and FullPostfixExpression rules in order to 
    // remove PostfixExpression indirect left recursion.
    b.rule(POSTFIX_EXPR).is(b.firstOf(
      FULL_NEW_EXPR,
      b.sequence(SUPER_EXPR, PROPERTY_OPERATOR),
      PRIMARY_EXPR,
      SHORT_NEW_EXPR),
      b.zeroOrMore(b.firstOf(
        PROPERTY_OPERATOR,
        ARGUMENTS,
        QUERY_OPERATOR,
        /* No line break */ DOUBLE_PLUS,
        /* No line break */ DOUBLE_MINUS))
    );


    // New expressions
    b.rule(FULL_NEW_EXPR).is(NEW, FULL_NEW_SUB_EXPR, ARGUMENTS);
    b.rule(FULL_NEW_SUB_EXPR).is(b.firstOf(
      PRIMARY_EXPR,
      FULL_NEW_EXPR,
      b.sequence(FULL_NEW_EXPR, PROPERTY_OPERATOR),
      b.sequence(SUPER_EXPR, PROPERTY_OPERATOR)));

    b.rule(SHORT_NEW_EXPR).is(NEW, SHORT_NEW_SUB_EXPR);
    b.rule(SHORT_NEW_SUB_EXPR).is(b.firstOf(
      FULL_NEW_SUB_EXPR,
      SHORT_NEW_EXPR));

    // Property accessors
    b.rule(PROPERTY_OPERATOR).is(b.firstOf(
      b.sequence(DOT, QUALIFIED_IDENTIFIER),
      BRACKETS));
    b.rule(BRACKETS).is(LBRAKET, LIST_EXPRESSION, RBRAKET);

    // Query operators
    b.rule(QUERY_OPERATOR).is(b.firstOf(
      b.sequence(DOUBLE_DOT, QUALIFIED_IDENTIFIER),
      b.sequence(DOT, LPARENTHESIS, LIST_EXPRESSION, RPARENTHESIS)));

    //Call expresions
    b.rule(ARGUMENTS).is(b.firstOf(
      b.sequence(LPARENTHESIS, RPARENTHESIS),
      b.sequence(LPARENTHESIS, LIST_EXPRESSION, RPARENTHESIS)));

    b.rule(ARGUMENTS_LIST).is(ASSIGNMENT_EXPR,
      b.zeroOrMore(COMMA, ASSIGNMENT_EXPR));

    // Unary expression
    b.rule(UNARY_EXPR).is(b.firstOf(
      b.sequence(b.firstOf(DELETE, DOUBLE_PLUS, DOUBLE_MINUS), POSTFIX_EXPR),
      b.sequence(b.firstOf(VOID, TYPEOF, PLUS, MINUS, NOT, TILD), UNARY_EXPR),
      POSTFIX_EXPR
      // NegatedMinLong
      ));

    // Binary expressions
    b.rule(MULTIPLICATIVE_EXPR).is(UNARY_EXPR,
      b.zeroOrMore(b.firstOf(STAR, DIV, MOD), UNARY_EXPR));
    b.rule(ADDITIVE_EXPR).is(MULTIPLICATIVE_EXPR,
      b.zeroOrMore(b.firstOf(PLUS, MINUS), MULTIPLICATIVE_EXPR));
    b.rule(SHIFT_EXPR).is(ADDITIVE_EXPR,
      b.zeroOrMore(b.firstOf(SL, SR2, SR), ADDITIVE_EXPR));
    b.rule(RELATIONAL_EXPR).is(SHIFT_EXPR, b.zeroOrMore(b.firstOf(
      b.sequence(LE, SHIFT_EXPR),
      b.sequence(GE, SHIFT_EXPR),
      b.sequence(LT, SHIFT_EXPR),
      b.sequence(GT, SHIFT_EXPR),
      b.sequence(IN, SHIFT_EXPR),
      b.sequence(INSTANCEOF, SHIFT_EXPR),
      b.sequence(IS, SHIFT_EXPR),
      b.sequence(AS, SHIFT_EXPR))));
    b.rule(EQUALITY_EXPR).is(RELATIONAL_EXPR, b.zeroOrMore(b.firstOf(
      b.sequence(NOTEQUAL2, RELATIONAL_EXPR),
      b.sequence(EQUAL2, RELATIONAL_EXPR),
      b.sequence(EQUAL, RELATIONAL_EXPR),
      b.sequence(NOTEQUAL, RELATIONAL_EXPR))));
    b.rule(BITEWISE_AND_EXPR).is(EQUALITY_EXPR,
      b.zeroOrMore(b.sequence(AND, EQUALITY_EXPR)));
    b.rule(BITEWISE_XOR_EXPR).is(BITEWISE_AND_EXPR,
      b.zeroOrMore(b.sequence(XOR, BITEWISE_AND_EXPR)));
    b.rule(BITEWISE_OR_EXPR).is(BITEWISE_XOR_EXPR,
      b.zeroOrMore(b.sequence(OR, BITEWISE_XOR_EXPR)));
    b.rule(LOGICAL_AND_EXPR).is(BITEWISE_OR_EXPR,
      b.zeroOrMore(b.sequence(ANDAND, BITEWISE_XOR_EXPR)));
    b.rule(LOGICAL_OR_EXPR).is(LOGICAL_AND_EXPR,
      b.zeroOrMore(b.sequence(OROR, LOGICAL_AND_EXPR)));

    // Conditional expression
    b.rule(CONDITIONAL_EXPR).is(b.firstOf(
      LOGICAL_OR_EXPR,
      b.sequence(LOGICAL_OR_EXPR, QUERY, ASSIGNMENT_EXPR,
      COLON, ASSIGNMENT_EXPR)));

    // Non assignment expression
    b.rule(NON_ASSIGNMENT_EXPR).is(b.firstOf(
      LOGICAL_OR_EXPR,
      b.sequence(LOGICAL_OR_EXPR, QUERY, NON_ASSIGNMENT_EXPR,
      COLON, NON_ASSIGNMENT_EXPR)));

    b.rule(LIST_EXPRESSION).is(ASSIGNMENT_EXPR,
      b.zeroOrMore(b.sequence(COMMA, ASSIGNMENT_EXPR)));

    b.rule(TYPE_EXPR).is(NON_ASSIGNMENT_EXPR);

    // </editor-fold> 
    
    /** 
     * DEFINITIONS
     */
    // <editor-fold defaultstate="collapsed" desc="Definitions">
    // Variable
    b.rule(VARIABLE_DEF).is(VARIABLE_DEF_KIND, VARIABLE_BINDING_LIST);
    b.rule(VARIABLE_DEF_KIND).is(b.firstOf(VAR, CONST));
    b.rule(VARIABLE_BINDING_LIST).is(VARIABLE_BINDING, 
                                     b.zeroOrMore(COMMA, VARIABLE_BINDING));
    
    b.rule(VARIABLE_BINDING).is(TYPED_IDENTIFIER, VARIABLE_INITIALISATION);
    b.rule(VARIABLE_INITIALISATION).is(b.optional(EQU, VARIABLE_INITIALISER));
    
    b.rule(VARIABLE_INITIALISER).is(b.firstOf(
      ASSIGNMENT_EXPR, 
      ATTRIBUTE_COMBINATION));
    
    b.rule(TYPED_IDENTIFIER).is(b.firstOf(
      b.sequence(LEXICAL_IDENTIFIER, COLON, TYPE_EXPR),
      LEXICAL_IDENTIFIER));
    
    //Function
    b.rule(FUNCTION_DEF).is(FUNCTION, FUNCTION_NAME, FUNCTION_COMMON);
    b.rule(FUNCTION_NAME).is(b.firstOf(
      b.sequence(GET, /*Non line break*/ SYNTACTIC_IDENTIFIER),
      b.sequence(SET, /*Non line break*/ SYNTACTIC_IDENTIFIER),
      SYNTACTIC_IDENTIFIER));
    
    b.rule(FUNCTION_COMMON).is(b.firstOf(
      b.sequence(FUNCTION_SIGNATURE, BLOCK),
      FUNCTION_SIGNATURE));
    
    b.rule(FUNCTION_SIGNATURE).is(b.firstOf(
      b.sequence(LPARENTHESIS, RPARENTHESIS, RESULT_TYPE),
      b.sequence(LPARENTHESIS, PARAMETERS, RPARENTHESIS, RESULT_TYPE)));
    
    b.rule(PARAMETERS).is(b.optional(NON_EMPTY_PARAMETERS));
    b.rule(NON_EMPTY_PARAMETERS).is(b.firstOf(
      PARAMETER,
      b.sequence(PARAMETER, COMMA, NON_EMPTY_PARAMETERS),
      REST_PARAMETERS));
    
    b.rule(PARAMETER).is(b.firstOf(
      TYPED_IDENTIFIER,
      b.sequence(TYPED_IDENTIFIER, EQU, ASSIGNMENT_EXPR)));
    
    b.rule(REST_PARAMETERS).is(b.firstOf(
      TRIPLE_DOTS,
      b.sequence(TRIPLE_DOTS, SYNTACTIC_IDENTIFIER)));
    
    b.rule(RESULT_TYPE).is(b.optional(COLON, TYPE_EXPR));
    
    // Class
    b.rule(CLASS_DEF).is(CLASS, CLASS_NAME, INHERITENCE, BLOCK);
    b.rule(CLASS_NAME).is(CLASS_IDENTIFIERS);
    b.rule(CLASS_IDENTIFIERS).is(SYNTACTIC_IDENTIFIER, 
      b.zeroOrMore(b.sequence(DOT, SYNTACTIC_IDENTIFIER)));
    b.rule(INHERITENCE).is(b.optional(b.firstOf(
      b.sequence(EXTENDS, TYPE_EXPR),
      b.sequence(IMPLEMENTS, TYPE_EXPRESSION_LIST),
      b.sequence(EXTENDS, TYPE_EXPR, IMPLEMENTS, TYPE_EXPRESSION_LIST))));
    
    b.rule(TYPE_EXPRESSION_LIST).is(TYPE_EXPR, 
      b.zeroOrMore(b.sequence(COMMA, TYPE_EXPR)));
    
    b.rule(INTERFACE_DEF).is(INTERFACE, CLASS_NAME, EXTENDS_LIST, BLOCK);
    b.rule(EXTENDS_LIST).is(b.optional(EXTENDS, TYPE_EXPRESSION_LIST));
    
    // Package
    b.rule(PACKAGE_DEF).is(PACKAGE, b.optional(PACKAGE_NAME), BLOCK);
    b.rule(PACKAGE_NAME).is(SYNTACTIC_IDENTIFIER,
                            b.optional(DOT, SYNTACTIC_IDENTIFIER));

    // Namespace
    b.rule(NAMESPACE_DEF).is(NAMESPACE, NAMESPACE_BINDING);
    b.rule(NAMESPACE_BINDING).is(SYNTACTIC_IDENTIFIER, 
                                 NAMESPACE_INITIALISATION);
    b.rule(NAMESPACE_INITIALISATION).is(b.optional(EQU, ASSIGNMENT_EXPR));

    // Program
    b.rule(PROGRAM).is(b.firstOf(DIRECTIVES, b.sequence(PACKAGE_DEF, PROGRAM)));
    // </editor-fold>
    
    /** 
     * STATEMENTS
     */
    // <editor-fold defaultstate="collapsed" desc="Statements">
    b.rule(BLOCK).is(LCURLYBARCE, DIRECTIVES, RCURLYBARCE);
    b.rule(EMPTY_STATEMENT).is(b.nothing());
    b.rule(STATEMENT).is(b.nothing());
    b.rule(SEMICOLON).is(b.nothing());
    // </editor-fold>
    
    /** 
     * DIRECTIVES
     */
    // <editor-fold defaultstate="collapsed" desc="Directives definitions">
    // Directives
    b.rule(DIRECTIVE).is(b.firstOf(
      EMPTY_STATEMENT,
      STATEMENT,
      ANNOTABLE_DIRECTIVE,
      b.sequence(ATTRIBUTES, /*No line break*/ ANNOTABLE_DIRECTIVE),
      b.sequence(INCLUDE_DIRECTIVE, /*No line break*/ SEMICOLON),
      b.sequence(IMPORT_DIRECTIVE, /*No line break*/ SEMICOLON),
      b.sequence(USE_DIRECTIVE, /*No line break*/ SEMICOLON)));
    
    b.rule(ANNOTABLE_DIRECTIVE).is(b.firstOf(
      b.sequence(VARIABLE_DEF, SEMICOLON),
      FUNCTION_DEF,
      CLASS_DEF,
      INTERFACE_DEF,
      b.sequence(NAMESPACE_DEF, SEMICOLON)));
    
    b.rule(DIRECTIVES).is(b.optional(DIRECTIVES_PREFIX, DIRECTIVE/*ABBREV*/));

    b.rule(DIRECTIVES_PREFIX).is(b.optional(DIRECTIVE/*full*/));
    
    // Attributes
    b.rule(ATTRIBUTES).is(b.firstOf(ATTRIBUTE, ATTRIBUTE_COMBINATION));
    b.rule(ATTRIBUTE_COMBINATION).is(ATTRIBUTE, /*No line break*/ ATTRIBUTES);
    b.rule(ATTRIBUTE).is(b.firstOf(
      ATTRIBUTE_EXPR,
      RESERVED_NAMESPACE,
      b.sequence(LBRAKET, ASSIGNMENT_EXPR, RBRAKET)));
    
    b.rule(ATTRIBUTE_EXPR).is(LEXICAL_IDENTIFIER, 
                              b.zeroOrMore(PROPERTY_OPERATOR));
   
    // Import
    b.rule(IMPORT_DIRECTIVE).is(b.firstOf(
      b.sequence(IMPORT, PACKAGE_NAME, DOT, STAR),
      b.sequence(IMPORT, PACKAGE_NAME, DOT, SYNTACTIC_IDENTIFIER)
      ));
    
    // Include
    b.rule(INCLUDE_DIRECTIVE).is(INCLUDE, /*No line break*/ STRING);
    
    // Use
    b.rule(USE_DIRECTIVE).is(USE, NAMESPACE, LIST_EXPRESSION);
    
    // </editor-fold>
   
    return b.build();
  }
}
