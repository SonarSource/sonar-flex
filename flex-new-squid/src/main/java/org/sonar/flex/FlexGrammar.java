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
  XML,
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
  INTEGER,
  FLOAT,
  WHITESPACE,
  SPACING,
  INLINE_COMMENT,
  MULTILINE_COMMENT,
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
  IDENTIFIER,
  IDENTIFIER_NAME,
  IDENTIFIER_PART,
  IDENTIFIER_START,
  UNICODE_ESCAPE_SEQUENCE,
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
  // XML Initialiser
  XML_INITIALISER,
  XML_MARKUP,
  XML_ELEMENT,
  XML_TAG_CONTENT,
  XML_WHITESPACE,
  XML_WHITESPACE_CHARACTER,
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
  UNICODE,
  KEYWORDS,
  REGULAR_EXPRESSION,
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
  DEFAULT_XMLN_NAMESPACE_STATEMENT,
  SUB_STATEMENT,
  SUB_STATEMENTS,
  EMPTY_STATEMENT,
  SIMPLE_VARIABLE_DEF,
  SUB_STATEMENT_PREFIX,
  BLOCK,
  CASE_ELEMENT,
  CASE_ELEMENTS,
  CASE_ELEMENT_PREFIX,
  CASE_LABEL,
  FOR_INITIALISER,
  FOR_IN_BINDING,
  OPTIONAL_EXPRESSION,
  CATCH_CLAUSE,
  CATCH_CLAUSES,
  CATCH_CLAUSES_OPT,
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
  
  regular_expr_flags,
  regular_expr_char,
  regular_expr_first_char,
  regular_expr_chars,
  regular_expr_body,
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
  RCURLYBRACE,
  LCURLYBRACE,
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
  TRIPLE_DOTS,
  SEMICOLON,
  UNDERSCORE,
  BACKSLASH,
  DOLLAR;
  
  // </editor-fold>
  
  public static FlexGrammar[] KEYWORDS_LIST = {AS, BREAK, CASE, CATCH, CLASS, 
                                          CONST, CONTINUE, DEFAULT, DELETE,
                                          DO, ELSE, EXTENDS, FALSE, FINALLY,
                                          FOR, IF, IMPLEMENTS, IMPORT, IN,
                                          INSTANCEOF, INTERFACE, INTERNAL, IS,
                                          NATIVE, NEW, NULL, PACKAGE, PRIVATE,
                                          PROTECTED, PUBLIC, RETURN, SUPER, 
                                          SWITCH, THIS, THROW, TO, TRUE, TRY,
                                          TYPEOF, USE, VAR, VOID, WHILE, WITH,
                                          XML,};
  
  private static final String UNICODE_LETTER = "\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}";
  private static final String UNICODE_DIGIT = "\\p{Nd}";
  private static final String UNICODE_COMBINING_MARK = "\\p{Mn}\\p{Mc}";
  private static final String UNICODE_CONNECTOR_PUNCTUATION = "\\p{Mn}\\p{Mc}";
    
  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();
    
    b.rule(WHITESPACE).is(b.regexp("\\s*"));
    b.rule(INLINE_COMMENT).is(b.regexp("//[^\\n\\r]*+"));
    b.rule(MULTILINE_COMMENT).is(b.regexp("/\\*[\\s\\S*]*\\*/"));
    b.rule(SPACING).is(WHITESPACE, 
      b.zeroOrMore(b.firstOf(INLINE_COMMENT, MULTILINE_COMMENT), WHITESPACE));
    b.rule(UNICODE_ESCAPE_SEQUENCE).is(b.regexp("u[0-9a-fA-F]{4,4}"));

     // Keywords
    Object[] rest = new Object[KEYWORDS_LIST.length - 2];
    for (int i = 2; i < KEYWORDS_LIST.length; i++){
      rest[i - 2] = KEYWORDS_LIST[i];
    }
    b.rule(KEYWORDS).is(b.firstOf(KEYWORDS_LIST[0], KEYWORDS_LIST[1], rest));
    
    b.rule(STRING).is(b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""), SPACING);
    b.rule(INTEGER).is(b.regexp("-?[0-9]+"), SPACING);
    b.rule(FLOAT).is(b.regexp("-?[0-9]+\\.[0-9]+"), SPACING);
    b.rule(NUMBER).is(b.firstOf(FLOAT, INTEGER));

    /** 
     * KEYWORDS
     */
    // <editor-fold defaultstate="collapsed" desc="Keywords definition">
    b.rule(AS).is("as", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(BREAK).is("break", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(CASE).is("case", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(CATCH).is("catch", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(CLASS).is("class", b.nextNot(IDENTIFIER_NAME), SPACING);   
    b.rule(CONST).is("const", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(CONTINUE).is("continue", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(DEFAULT).is("default", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(DELETE).is("delete", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(DO).is("do", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(ELSE).is("else", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(EXTENDS).is("extends", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(FALSE).is("false", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(FINALLY).is("finally", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(FOR).is("for", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(FUNCTION).is("function", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(IF).is("if", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(IMPLEMENTS).is("implements", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(IMPORT).is("import", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(IN).is("in", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(INSTANCEOF).is("instanceof", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(INTERFACE).is("interface", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(INTERNAL).is("internal", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(IS).is("is", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(NATIVE).is("native", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(NEW).is("new", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(NULL).is("null", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(PACKAGE).is("package", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(PRIVATE).is("private", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(PROTECTED).is("protected", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(PUBLIC).is("plublic", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(RETURN).is("return", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(SUPER).is("super", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(SWITCH).is("switch", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(THIS).is("this", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(THROW).is("throw", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(TO).is("to", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(TRUE).is("true", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(TRY).is("try", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(TYPEOF).is("typeof", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(USE).is("use", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(VAR).is("var", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(VOID).is("void", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(WHILE).is("while", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(WITH).is("with", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(XML).is("xml", b.nextNot(IDENTIFIER_NAME), SPACING);
    // </editor-fold>

    /** 
     * SYNTACTIC KEYWORDS
     */
    // <editor-fold defaultstate="collapsed" desc="Syntactic keywords definition">
    b.rule(EACH).is("each", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(GET).is("get", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(SET).is("set", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(NAMESPACE).is("namespace", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(INCLUDE).is("include", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(DYNAMIC).is("dynamic", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(FINAL).is("final", b.nextNot(IDENTIFIER_NAME), SPACING); 
    b.rule(OVERRIDE).is("override", b.nextNot(IDENTIFIER_NAME), SPACING);
    b.rule(STATIC).is("static", b.nextNot(IDENTIFIER_NAME), SPACING);
    // </editor-fold>
    
    /** 
     * PONCTUATORS
     */
    // <editor-fold defaultstate="collapsed" desc="Ponctuator definition">
    b.rule(AT_SIGN).is("@", SPACING);
    b.rule(COMMA).is(",", SPACING);
    b.rule(COLON).is(":", SPACING);
    b.rule(DOUBLE_COLON).is("::", SPACING);
    b.rule(DOT).is(".", SPACING);
    b.rule(TRIPLE_DOTS).is("...", SPACING);
    b.rule(DOUBLE_DOT).is("..", SPACING);
    b.rule(RCURLYBRACE).is("}", SPACING);
    b.rule(LCURLYBRACE).is("{", SPACING);
    b.rule(RBRAKET).is("]", SPACING);
    b.rule(LBRAKET).is("[", SPACING);
    b.rule(RPARENTHESIS).is(")", SPACING);
    b.rule(LPARENTHESIS).is("(", SPACING);
    b.rule(DIV).is("/", SPACING);
    b.rule(MOD).is("%", SPACING);
    b.rule(STAR).is("*", SPACING);
    b.rule(PLUS).is("+", SPACING);
    b.rule(MINUS).is("-", SPACING);
    b.rule(DOUBLE_PLUS).is("++", SPACING);
    b.rule(DOUBLE_MINUS).is("--", SPACING);
    b.rule(SL).is("<<", SPACING);
    b.rule(SR).is(">>", SPACING);
    b.rule(SR2).is(">>>", SPACING);
    b.rule(NOT).is("!", SPACING);
    b.rule(EQU).is("=", SPACING);
    b.rule(EQUAL).is("==", SPACING);
    b.rule(EQUAL2).is("===", SPACING);
    b.rule(NOTEQUAL).is("!=", SPACING);
    b.rule(NOTEQUAL2).is("!==", SPACING);
    b.rule(STAR_EQU).is("*=", SPACING);
    b.rule(DIV_EQU).is("/=", SPACING);
    b.rule(MOD_EQU).is("%=", SPACING);
    b.rule(PLUS_EQU).is("+=", SPACING);
    b.rule(MINUS_EQU).is("-=", SPACING);
    b.rule(SL_EQU).is("<<=", SPACING);
    b.rule(SR_EQU).is(">>=", SPACING);
    b.rule(SR_EQU2).is(">>>=", SPACING);
    b.rule(AND_EQU).is("&=", SPACING);
    b.rule(XOR_EQU).is("^=", SPACING);
    b.rule(OR_EQU).is("|=", SPACING);
    b.rule(ANDAND_EQU).is("&&=", SPACING);
    b.rule(XORXOR_EQU).is("^^=", SPACING);
    b.rule(OROR_EQU).is("||=", SPACING);
    b.rule(LT).is("<", SPACING);
    b.rule(GT).is(">", SPACING);
    b.rule(LE).is("<=", SPACING);
    b.rule(GE).is(">=", SPACING);
    b.rule(XOR).is("^", SPACING);
    b.rule(AND).is("&", SPACING);
    b.rule(ANDAND).is("&&", SPACING);
    b.rule(OR).is("|", SPACING);
    b.rule(OROR).is("||", SPACING);
    b.rule(QUERY).is("?", SPACING);
    b.rule(TILD).is("~", SPACING);
    // Consider that all statement ends with a semicolon
    b.rule(SEMICOLON).is(";", SPACING);
    b.rule(UNDERSCORE).is("_", SPACING);
    b.rule(BACKSLASH).is("\\", SPACING);
    b.rule(DOLLAR).is("$", SPACING);
    // </editor-fold>

    
    /** 
     * EXPRESSIONS
     */
    // <editor-fold defaultstate="collapsed" desc="Expressions definition">
    // Identifiers
    b.rule(IDENTIFIER).is(b.firstOf(
      DYNAMIC,
      EACH,
      GET,
      INCLUDE,
      NAMESPACE,
      SET,
      STATIC,
      b.sequence(b.nextNot(KEYWORDS), IDENTIFIER_NAME)));

    b.rule(IDENTIFIER_NAME).is(IDENTIFIER_START, 
                               b.zeroOrMore(IDENTIFIER_PART), SPACING);
    
    b.rule(IDENTIFIER_START).is(b.firstOf(
      b.regexp("[" + UNICODE_LETTER + "]"),
      DOLLAR,
      UNDERSCORE,
      b.sequence(BACKSLASH, UNICODE_ESCAPE_SEQUENCE)));
    
    b.rule(IDENTIFIER_PART).is(b.firstOf(
      IDENTIFIER_START,
      b.regexp("[" + UNICODE_COMBINING_MARK + "]"),
      b.regexp("[" + UNICODE_DIGIT + "]"),
      b.regexp("[" + UNICODE_CONNECTOR_PUNCTUATION + "]"),
      UNICODE_ESCAPE_SEQUENCE));
    
    b.rule(PROPERTY_IDENTIFIER).is(b.firstOf(
      IDENTIFIER,
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
    
    // Regular expression according to ECMA 262
    b.rule(REGULAR_EXPRESSION).is(b.regexp(
       "/"
       // Regular expression first char
     + "([^\\n\\r\\*\\\\/]|(\\\\[^\\n\\r]))"
       // Regular expression chars
     + "([^\\n\\r\\\\/]|(\\\\[^\\n\\r]))*"
     + "/"
      // Regular expression flags
     + "["+ UNICODE_LETTER + "\\$_(\\\\" + UNICODE_ESCAPE_SEQUENCE +")" 
          + UNICODE_COMBINING_MARK + UNICODE_DIGIT 
          + UNICODE_CONNECTOR_PUNCTUATION + "]*"));
    
    //Primary expressions
    b.rule(PRIMARY_EXPR).is(b.firstOf(
      NULL,
      TRUE,
      FALSE,
      NUMBER,
      STRING,
      THIS,
      REGULAR_EXPRESSION,
      XML_INITIALISER,
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
      b.sequence(FUNCTION, IDENTIFIER, FUNCTION_COMMON)));

    // Object initialiser
    b.rule(OBJECT_INITIALISER).is(LCURLYBRACE, FIELD_LIST, RCURLYBRACE);

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
      b.sequence(POSTFIX_EXPR, EQU, ASSIGNMENT_EXPR),
      b.sequence(POSTFIX_EXPR, COMPOUND_ASSIGNMENT, ASSIGNMENT_EXPR),
      b.sequence(POSTFIX_EXPR, LOGICAL_ASSIGNMENT, ASSIGNMENT_EXPR),
      CONDITIONAL_EXPR));

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
    
    // XML INITIALISER
    b.rule(XML_INITIALISER).is(b.firstOf(
      XML_MARKUP,
      XML_ELEMENT,
      b.sequence(LT, GT, XML_ELEMENT_CONTENT, LT, DIV, GT)));
    
    b.rule(XML_ELEMENT).is(b.firstOf(
      b.sequence(LT, XML_TAG_CONTENT, b.optional(XML_WHITESPACE), DIV, GT),
      b.sequence(LT, XML_TAG_CONTENT, b.optional(XML_WHITESPACE), 
                 XML_ELEMENT_CONTENT, LT, DIV, XML_TAG_NAME, 
                 b.optional(XML_WHITESPACE), GT)));
    
    b.rule(XML_TAG_CONTENT).is(XML_TAG_NAME, XML_ATTRIBUTES);
    
    b.rule(XML_TAG_NAME).is(b.firstOf(
      b.sequence(LCURLYBRACE, EXPRESSION_STATEMENT),
      XML_NAME));
    
    b.rule(XML_ATTRIBUTES).is(b.optional(b.firstOf(
      b.sequence(XML_WHITESPACE, LCURLYBRACE, EXPRESSION_STATEMENT, RCURLYBRACE),
      b.sequence(XML_ATTRIBUTE, XML_ATTRIBUTES))));
 
    b.rule(XML_ATTRIBUTE).is(b.firstOf(
      b.sequence(b.zeroOrMore(XML_WHITESPACE), XML_NAME, 
                 b.optional(b.zeroOrMore(XML_WHITESPACE)), EQU,
                 b.optional(b.zeroOrMore(XML_WHITESPACE)), LCURLYBRACE,
                 EXPRESSION_STATEMENT, RCURLYBRACE),
      b.sequence(b.zeroOrMore(XML_WHITESPACE), XML_NAME, 
                 b.optional(b.zeroOrMore(XML_WHITESPACE)), EQU,
                 b.optional(b.zeroOrMore(XML_WHITESPACE)), 
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
     
    b.rule(XML_COMMENT).is(b.regexp("<!--(?:(?!--)[\\s\\S])*?-->"));
    b.rule(XML_CDATA  ).is(b.regexp("<!\\[CDATA\\[(?:(?!]])[\\s\\S])*?]]>"));
    b.rule(XML_PI).is(b.regexp("<\\?(?:(?!\\?>)[\\s\\S])*?\\?>"));
    b.rule(XML_TEXT).is(b.regexp("[^{<]++"));
    b.rule(XML_NAME).is(b.regexp("[" + UNICODE_LETTER + "_:" + "]" + "[" + UNICODE_LETTER + UNICODE_DIGIT + "\\.\\-_:" + "]*"));
    b.rule(XML_ATTRIBUTE_VALUE).is(b.regexp("(\"([^\"]*[//s//S]*)\")|(\'([^\']*[//s//S]*)\')"));
    b.rule(XML_WHITESPACE).is(b.regexp("[ \\t\\r\\n]+"));
    
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
      b.sequence(IDENTIFIER, COLON, TYPE_EXPR),
      IDENTIFIER));
    
    //Function
    b.rule(FUNCTION_DEF).is(FUNCTION, FUNCTION_NAME, FUNCTION_COMMON);
    b.rule(FUNCTION_NAME).is(b.firstOf(
      b.sequence(GET, /*Non line break*/ IDENTIFIER),
      b.sequence(SET, /*Non line break*/ IDENTIFIER),
      IDENTIFIER));
    
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
      b.sequence(TRIPLE_DOTS, IDENTIFIER)));
    
    // TODO WTF in specs? other grammars don't use type_expr
    b.rule(RESULT_TYPE).is(b.optional(COLON, TYPE_EXPR));
    
    // Class
    b.rule(CLASS_DEF).is(CLASS, CLASS_NAME, INHERITENCE, BLOCK);
    b.rule(CLASS_NAME).is(CLASS_IDENTIFIERS);
    b.rule(CLASS_IDENTIFIERS).is(IDENTIFIER, 
      b.zeroOrMore(b.sequence(DOT, IDENTIFIER)));
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
    b.rule(PACKAGE_NAME).is(IDENTIFIER,
                            b.optional(DOT, IDENTIFIER));

    // Namespace
    b.rule(NAMESPACE_DEF).is(NAMESPACE, NAMESPACE_BINDING);
    b.rule(NAMESPACE_BINDING).is(IDENTIFIER, 
                                 NAMESPACE_INITIALISATION);
    b.rule(NAMESPACE_INITIALISATION).is(b.optional(EQU, ASSIGNMENT_EXPR));

    // Program
    b.rule(PROGRAM).is(b.firstOf(b.sequence(PACKAGE_DEF, PROGRAM), DIRECTIVES));
    // </editor-fold>
    
    /** 
     * STATEMENTS
     */
    // <editor-fold defaultstate="collapsed" desc="Statements">
    // Statements
    b.rule(STATEMENT).is(b.firstOf(
      b.sequence(SUPER_STATEMENT, SEMICOLON),
      BLOCK,
      IF_STATEMENT,
      SWITCH_STATEMENT,
      b.sequence(DO_STATEMENT, SEMICOLON),
      WHILE_STATEMENT,
      FOR_STATEMENT,
      WITH_STATEMENT,
      b.sequence(CONTINUE_STATEMENT, SEMICOLON),
      b.sequence(BREAK_STATEMENT, SEMICOLON),
      b.sequence(RETURN_STATEMENT, SEMICOLON),
      b.sequence(THROW_STATEMENT, SEMICOLON),
      TRY_STATEMENT,
      b.sequence(EXPRESSION_STATEMENT, SEMICOLON),
      b.sequence(CONTINUE_STATEMENT, SEMICOLON),
      LABELED_STATEMENT,
      DEFAULT_XMLN_NAMESPACE_STATEMENT));
    
    b.rule(SUB_STATEMENT).is(b.firstOf(
      EMPTY_STATEMENT,
      STATEMENT,
      b.sequence(VARIABLE_DEF, SEMICOLON)));
    
    b.rule(SUB_STATEMENTS).is(b.optional(SUB_STATEMENT_PREFIX, SUB_STATEMENT));
    b.rule(SUB_STATEMENT_PREFIX).is(b.optional(SUB_STATEMENT));

    // Empty
    b.rule(EMPTY_STATEMENT).is(SEMICOLON);
    b.rule(SUPER_STATEMENT).is(SUPER, ARGUMENTS);
    b.rule(BLOCK).is(LCURLYBRACE, DIRECTIVES, RCURLYBRACE);
    b.rule(LABELED_STATEMENT).is(IDENTIFIER, COLON, SUB_STATEMENT);
    
    // Conditional statements
    b.rule(IF_STATEMENT).is(IF, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT, 
                            b.optional(ELSE, SUB_STATEMENT));
    // 3 other rules with annotations
    
    b.rule(SWITCH_STATEMENT).is(SWITCH, PARENTHESIZED_LIST_EXPR,
                                LCURLYBRACE, CASE_ELEMENTS, RCURLYBRACE);
    b.rule(CASE_ELEMENTS).is(b.optional(b.firstOf(
      // BEFORE: b.sequence(CASE_LABEL, CASE_ELEMENT_PREFIX, CASE_ELEMENT)
      b.sequence(CASE_LABEL,  b.zeroOrMore(CASE_ELEMENT)),
      CASE_LABEL)));
    
    b.rule(CASE_ELEMENT).is(b.firstOf(CASE_LABEL, DIRECTIVE));
    b.rule(CASE_LABEL).is(b.firstOf(
      b.sequence(DEFAULT, COLON),
      b.sequence(CASE, LIST_EXPRESSION, COLON)
      ));
    
    // Itteration
    b.rule(DO_STATEMENT).is(DO, SUB_STATEMENT, WHILE,
                                    PARENTHESIZED_LIST_EXPR);
    
    b.rule(WHILE_STATEMENT).is(WHILE, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT);
    
    b.rule(FOR_STATEMENT).is(b.firstOf(
      b.sequence(FOR, LPARENTHESIS, FOR_INITIALISER, SEMICOLON, 
                 OPTIONAL_EXPRESSION, SEMICOLON, 
                 OPTIONAL_EXPRESSION, RPARENTHESIS, SUB_STATEMENT),
      b.sequence(FOR, LPARENTHESIS, FOR_IN_BINDING, IN, LIST_EXPRESSION,
                  RPARENTHESIS, SUB_STATEMENT),
      b.sequence(FOR, /*No line break*/ EACH, LPARENTHESIS, FOR_IN_BINDING,
                 IN, LIST_EXPRESSION, RPARENTHESIS, SUB_STATEMENT)));
    
    b.rule(FOR_INITIALISER).is(b.optional(b.firstOf(
      LIST_EXPRESSION,
      VARIABLE_DEF)));
    
    b.rule(FOR_IN_BINDING).is(b.firstOf(
      b.sequence(VARIABLE_DEF_KIND, VARIABLE_BINDING),
      POSTFIX_EXPR));
    
    b.rule(OPTIONAL_EXPRESSION).is(b.optional(LIST_EXPRESSION));
    b.rule(CONTINUE_STATEMENT).is(b.firstOf(
      CONTINUE,
      b.sequence(CONTINUE, /*No line break*/ IDENTIFIER)));
     
    b.rule(BREAK_STATEMENT).is(b.firstOf(
      BREAK,
      b.sequence(BREAK, /*No line break*/ IDENTIFIER)));
    
    b.rule(WITH_STATEMENT).is(WITH, PARENTHESIZED_LIST_EXPR, SUB_STATEMENT);
    
    b.rule(RETURN_STATEMENT).is(b.firstOf(
      RETURN, 
      b.sequence(RETURN, /*No line break*/ LIST_EXPRESSION)));
    
    b.rule(THROW_STATEMENT).is(THROW, /*No line break*/ LIST_EXPRESSION);
    
    // Try
    b.rule(TRY_STATEMENT).is(b.firstOf(
      b.sequence(TRY, BLOCK, CATCH_CLAUSES),
      b.sequence(TRY, BLOCK, CATCH_CLAUSES_OPT, FINALLY, BLOCK)));
    
    b.rule(CATCH_CLAUSES_OPT).is(b.optional(CATCH_CLAUSES));
    
    b.rule(CATCH_CLAUSES).is(CATCH_CLAUSE, b.zeroOrMore(CATCH_CLAUSE));
    
    b.rule(CATCH_CLAUSE).is(CATCH, LPARENTHESIS, PARAMETER, RPARENTHESIS, BLOCK);
   
    b.rule(DEFAULT_XMLN_NAMESPACE_STATEMENT).is(DEFAULT, /*No line break*/ XML,
            /*no line break*/ NAMESPACE, EQU, NON_ASSIGNMENT_EXPR);
    
    // Note as [lookahead !{ function, { }] in adobe doc 
    b.rule(EXPRESSION_STATEMENT).is(LIST_EXPRESSION,
      b.nextNot(FUNCTION, b.sequence(LCURLYBRACE, RCURLYBRACE)));
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

    b.rule(DIRECTIVES).is(b.zeroOrMore(DIRECTIVE));

    // Attributes
    b.rule(ATTRIBUTES).is(b.firstOf(ATTRIBUTE, ATTRIBUTE_COMBINATION));
    b.rule(ATTRIBUTE_COMBINATION).is(ATTRIBUTE, /*No line break*/ ATTRIBUTES);
    b.rule(ATTRIBUTE).is(b.firstOf(
      ATTRIBUTE_EXPR,
      RESERVED_NAMESPACE,
      b.sequence(LBRAKET, ASSIGNMENT_EXPR, RBRAKET)));
    
    b.rule(ATTRIBUTE_EXPR).is(IDENTIFIER, 
                              b.zeroOrMore(PROPERTY_OPERATOR));
   
    // Import
    b.rule(IMPORT_DIRECTIVE).is(b.firstOf(
      b.sequence(IMPORT, PACKAGE_NAME, DOT, STAR),
      b.sequence(IMPORT, PACKAGE_NAME, DOT, IDENTIFIER)
      ));
    
    // Include
    b.rule(INCLUDE_DIRECTIVE).is(INCLUDE, /*No line break*/ STRING);
    
    // Use
    b.rule(USE_DIRECTIVE).is(USE, NAMESPACE, LIST_EXPRESSION);
    
    // </editor-fold>
   
    return b.build();
  }
}
