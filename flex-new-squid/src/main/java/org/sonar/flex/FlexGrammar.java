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

import com.sun.tools.internal.xjc.Plugin;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

public enum FlexGrammar implements GrammarRuleKey {

  // Keywords
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
  // Syntatic keywords
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
  //Expressions
  PRIMARY_EXPR,
  RESERVED_NAMESPACE_EXPR,
  PARENTHESIZED_EXPR,
  PARENTHESIZED_LIST_EXPR,
  FUNCTION_EXPR,
  OBJECT_INITIALISER,
  FIELD_LIST,
  FIELD_NAME,
  NON_EMPTY_FIELD_LIST,
  LITERAL_FIELD,
  NON_ATTRIBUTE_QUALIFIED_IDENTIFIER,
  ARRAY_INITIALISER,
  ELEMENT_LIST,
  LITERAL_ELEMENT,
  // Ponctuators
  COMMA,
  COLON,
  RCURLYBARCE,
  LCURLYBARCE,
  RBRAKET,
  LBRAKET,
  // Unknown
  FUNCTION_COMMON,
  ASSIGNEMENT_EXPRESSION,
  LIST_EXPRESSION,
  IDENTIFIER,;

  public static LexerlessGrammar createGrammar() {
    LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

    b.rule(WHITESPACE).is(b.regexp("\\s*"));
    b.rule(IDENTIFIER_PART).is(b.regexp("[a-zA-Z0-9]"));

    b.rule(STRING).is(b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""));
    b.rule(NUMBER).is(b.regexp("[0-9]+"));

    // Keywords
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

    // Syntatic keywords
    b.rule(EACH).is("each", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(GET).is("get", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(SET).is("set", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(NAMESPACE).is("namespace", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(INCLUDE).is("include", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(DYNAMIC).is("dynamic", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(FINAL).is("final", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(OVERRIDE).is("override", b.nextNot(IDENTIFIER_PART), WHITESPACE);
    b.rule(STATIC).is("static", b.nextNot(IDENTIFIER_PART), WHITESPACE);

    // Ponctuator
    b.rule(COMMA).is(",", WHITESPACE);
    b.rule(COLON).is(":", WHITESPACE);
    b.rule(RCURLYBARCE).is("}", WHITESPACE);
    b.rule(LCURLYBARCE).is("{", WHITESPACE);
    b.rule(RBRAKET).is("]", WHITESPACE);
    b.rule(LBRAKET).is("[", WHITESPACE);

    // Expressions
    b.rule(PRIMARY_EXPR).is(b.firstOf(
      NULL,
      TRUE,
      FALSE,
      NUMBER,
      STRING,
      THIS // Regular expression
      // QualifiedIdentifier
      // XMLInitializer
      // Reserved namespace
      // Parent list expression
      // Array initialiser
      // Object initialiser
      // Function expression
      ));

    b.rule(RESERVED_NAMESPACE_EXPR).is(b.firstOf(
      PUBLIC,
      PRIVATE,
      PROTECTED,
      INTERNAL));

    b.rule(PARENTHESIZED_EXPR).is(ASSIGNEMENT_EXPRESSION);
    b.rule(PARENTHESIZED_LIST_EXPR).is(b.firstOf(
      LIST_EXPRESSION,
      ASSIGNEMENT_EXPRESSION));

    b.rule(FUNCTION_EXPR).is(b.firstOf(
      b.sequence(FUNCTION, FUNCTION_COMMON),
      b.sequence(FUNCTION, IDENTIFIER, FUNCTION_COMMON)));

    // Object initialiser
    b.rule(OBJECT_INITIALISER).is(LCURLYBARCE, FIELD_LIST, RCURLYBARCE);

    b.rule(FIELD_LIST).is(b.optional(NON_EMPTY_FIELD_LIST));

    b.rule(NON_EMPTY_FIELD_LIST).is(b.firstOf(
      LITERAL_FIELD,
      b.sequence(LITERAL_FIELD, NON_EMPTY_FIELD_LIST)));

    b.rule(LITERAL_FIELD).is(FIELD_NAME, COLON, ASSIGNEMENT_EXPRESSION);

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

    b.rule(LITERAL_ELEMENT).is(ASSIGNEMENT_EXPRESSION);

    // Unknown
    b.rule(FUNCTION_COMMON).is(b.nothing());
    b.rule(LIST_EXPRESSION).is(b.nothing());
    b.rule(ASSIGNEMENT_EXPRESSION).is(b.nothing());
    b.rule(IDENTIFIER).is(b.nothing());
    b.rule(NON_ATTRIBUTE_QUALIFIED_IDENTIFIER).is(b.nothing());

    return b.build();
  }
}
