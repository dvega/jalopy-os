/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

// $ANTLR 2.7.2a2 (20020112-1): "main/src/java/de/hunsicker/jalopy/language/java.g" -> "JavaLexer.java"$
package de.hunsicker.jalopy.language;

/**
 * Token type constants for the Java recognizer.
 * 
 * <p>
 * This is an <a href="http://www.antlr.org/">ANTLR</a> automated generated file.
 * <strong>DO NOT EDIT</strong> but rather change the associated grammar and rebuild.
 * </p>
 */
public final class JavaTokenTypes
{
    //~ Static variables/initializers ----------------------------------------------------

    /** DOCUMENT ME! */
    public static final int EOF = 1;

    /** DOCUMENT ME! */
    public static final int NULL_TREE_LOOKAHEAD = 3;

    /** DOCUMENT ME! */
    public static final int JAVADOC_COMMENT = 6;

    /** DOCUMENT ME! */
    public static final int LCURLY = 7;

    /** DOCUMENT ME! */
    public static final int RCURLY = 8;

    /** DOCUMENT ME! */
    public static final int MODIFIERS = 9;

    /** DOCUMENT ME! */
    public static final int OBJBLOCK = 10;

    /** DOCUMENT ME! */
    public static final int SLIST = 11;

    /** DOCUMENT ME! */
    public static final int CTOR_DEF = 12;

    /** DOCUMENT ME! */
    public static final int METHOD_DEF = 13;

    /** DOCUMENT ME! */
    public static final int VARIABLE_DEF = 14;

    /** DOCUMENT ME! */
    public static final int INSTANCE_INIT = 15;

    /** DOCUMENT ME! */
    public static final int STATIC_INIT = 16;

    /** DOCUMENT ME! */
    public static final int TYPE = 17;

    /** DOCUMENT ME! */
    public static final int CLASS_DEF = 18;

    /** DOCUMENT ME! */
    public static final int INTERFACE_DEF = 19;

    /** DOCUMENT ME! */
    public static final int PACKAGE_DEF = 20;

    /** DOCUMENT ME! */
    public static final int ARRAY_DECLARATOR = 21;

    /** DOCUMENT ME! */
    public static final int EXTENDS_CLAUSE = 22;

    /** DOCUMENT ME! */
    public static final int IMPLEMENTS_CLAUSE = 23;

    /** DOCUMENT ME! */
    public static final int PARAMETERS = 24;

    /** DOCUMENT ME! */
    public static final int PARAMETER_DEF = 25;

    /** DOCUMENT ME! */
    public static final int LABELED_STAT = 26;

    /** DOCUMENT ME! */
    public static final int TYPECAST = 27;

    /** DOCUMENT ME! */
    public static final int INDEX_OP = 28;

    /** DOCUMENT ME! */
    public static final int POST_INC = 29;

    /** DOCUMENT ME! */
    public static final int POST_DEC = 30;

    /** DOCUMENT ME! */
    public static final int METHOD_CALL = 31;

    /** DOCUMENT ME! */
    public static final int EXPR = 32;

    /** DOCUMENT ME! */
    public static final int ARRAY_INIT = 33;

    /** DOCUMENT ME! */
    public static final int IMPORT = 34;

    /** DOCUMENT ME! */
    public static final int UNARY_MINUS = 35;

    /** DOCUMENT ME! */
    public static final int UNARY_PLUS = 36;

    /** DOCUMENT ME! */
    public static final int CASE_GROUP = 37;

    /** DOCUMENT ME! */
    public static final int ELIST = 38;

    /** DOCUMENT ME! */
    public static final int FOR_INIT = 39;

    /** DOCUMENT ME! */
    public static final int FOR_CONDITION = 40;

    /** DOCUMENT ME! */
    public static final int FOR_ITERATOR = 41;

    /** DOCUMENT ME! */
    public static final int EMPTY_STAT = 42;

    /** DOCUMENT ME! */
    public static final int FINAL = 43;

    /** DOCUMENT ME! */
    public static final int ABSTRACT = 44;

    /** DOCUMENT ME! */
    public static final int STRICTFP = 45;

    /** DOCUMENT ME! */
    public static final int SUPER_CTOR_CALL = 46;

    /** DOCUMENT ME! */
    public static final int CTOR_CALL = 47;

    /** DOCUMENT ME! */
    public static final int BOF = 48;

    /** DOCUMENT ME! */
    public static final int ROOT = 49;

    /** DOCUMENT ME! */
    public static final int CASESLIST = 50;

    /** DOCUMENT ME! */
    public static final int BLOCK_STATEMENT = 51;

    /** DOCUMENT ME! */
    public static final int SEPARATOR_COMMENT = 52;

    /** DOCUMENT ME! */
    public static final int SYNBLOCK = 53;

    /** DOCUMENT ME! */
    public static final int LITERAL_package = 54;

    /** DOCUMENT ME! */
    public static final int SEMI = 55;

    /** DOCUMENT ME! */
    public static final int LITERAL_import = 56;

    /** DOCUMENT ME! */
    public static final int LBRACK = 57;

    /** DOCUMENT ME! */
    public static final int RBRACK = 58;

    /** DOCUMENT ME! */
    public static final int LITERAL_void = 59;

    /** DOCUMENT ME! */
    public static final int LITERAL_boolean = 60;

    /** DOCUMENT ME! */
    public static final int LITERAL_byte = 61;

    /** DOCUMENT ME! */
    public static final int LITERAL_char = 62;

    /** DOCUMENT ME! */
    public static final int LITERAL_short = 63;

    /** DOCUMENT ME! */
    public static final int LITERAL_int = 64;

    /** DOCUMENT ME! */
    public static final int LITERAL_float = 65;

    /** DOCUMENT ME! */
    public static final int LITERAL_long = 66;

    /** DOCUMENT ME! */
    public static final int LITERAL_double = 67;

    /** DOCUMENT ME! */
    public static final int IDENT = 68;

    /** DOCUMENT ME! */
    public static final int DOT = 69;

    /** DOCUMENT ME! */
    public static final int STAR = 70;

    /** DOCUMENT ME! */
    public static final int LITERAL_private = 71;

    /** DOCUMENT ME! */
    public static final int LITERAL_public = 72;

    /** DOCUMENT ME! */
    public static final int LITERAL_protected = 73;

    /** DOCUMENT ME! */
    public static final int LITERAL_static = 74;

    /** DOCUMENT ME! */
    public static final int LITERAL_transient = 75;

    /** DOCUMENT ME! */
    public static final int LITERAL_native = 76;

    /** DOCUMENT ME! */
    public static final int LITERAL_synchronized = 77;

    /** DOCUMENT ME! */
    public static final int LITERAL_volatile = 78;

    /** DOCUMENT ME! */
    public static final int LITERAL_class = 79;

    /** DOCUMENT ME! */
    public static final int LITERAL_extends = 80;

    /** DOCUMENT ME! */
    public static final int LITERAL_interface = 81;

    /** DOCUMENT ME! */
    public static final int COMMA = 82;

    /** DOCUMENT ME! */
    public static final int LITERAL_implements = 83;

    /** DOCUMENT ME! */
    public static final int LPAREN = 84;

    /** DOCUMENT ME! */
    public static final int RPAREN = 85;

    /** DOCUMENT ME! */
    public static final int LITERAL_this = 86;

    /** DOCUMENT ME! */
    public static final int LITERAL_super = 87;

    /** DOCUMENT ME! */
    public static final int STR_supper = 88;

    /** DOCUMENT ME! */
    public static final int ASSIGN = 89;

    /** DOCUMENT ME! */
    public static final int LITERAL_throws = 90;

    /** DOCUMENT ME! */
    public static final int COLON = 91;

    /** DOCUMENT ME! */
    public static final int LITERAL_if = 92;

    /** DOCUMENT ME! */
    public static final int LITERAL_else = 93;

    /** DOCUMENT ME! */
    public static final int LITERAL_for = 94;

    /** DOCUMENT ME! */
    public static final int LITERAL_while = 95;

    /** DOCUMENT ME! */
    public static final int LITERAL_do = 96;

    /** DOCUMENT ME! */
    public static final int LITERAL_break = 97;

    /** DOCUMENT ME! */
    public static final int LITERAL_assert = 98;

    /** DOCUMENT ME! */
    public static final int LITERAL_continue = 99;

    /** DOCUMENT ME! */
    public static final int LITERAL_return = 100;

    /** DOCUMENT ME! */
    public static final int LITERAL_switch = 101;

    /** DOCUMENT ME! */
    public static final int LITERAL_throw = 102;

    /** DOCUMENT ME! */
    public static final int LITERAL_case = 103;

    /** DOCUMENT ME! */
    public static final int LITERAL_default = 104;

    /** DOCUMENT ME! */
    public static final int LITERAL_try = 105;

    /** DOCUMENT ME! */
    public static final int LITERAL_catch = 106;

    /** DOCUMENT ME! */
    public static final int LITERAL_finally = 107;

    /** DOCUMENT ME! */
    public static final int PLUS_ASSIGN = 108;

    /** DOCUMENT ME! */
    public static final int MINUS_ASSIGN = 109;

    /** DOCUMENT ME! */
    public static final int STAR_ASSIGN = 110;

    /** DOCUMENT ME! */
    public static final int DIV_ASSIGN = 111;

    /** DOCUMENT ME! */
    public static final int MOD_ASSIGN = 112;

    /** DOCUMENT ME! */
    public static final int SR_ASSIGN = 113;

    /** DOCUMENT ME! */
    public static final int BSR_ASSIGN = 114;

    /** DOCUMENT ME! */
    public static final int SL_ASSIGN = 115;

    /** DOCUMENT ME! */
    public static final int BAND_ASSIGN = 116;

    /** DOCUMENT ME! */
    public static final int BXOR_ASSIGN = 117;

    /** DOCUMENT ME! */
    public static final int BOR_ASSIGN = 118;

    /** DOCUMENT ME! */
    public static final int QUESTION = 119;

    /** DOCUMENT ME! */
    public static final int LOR = 120;

    /** DOCUMENT ME! */
    public static final int LAND = 121;

    /** DOCUMENT ME! */
    public static final int BOR = 122;

    /** DOCUMENT ME! */
    public static final int BXOR = 123;

    /** DOCUMENT ME! */
    public static final int BAND = 124;

    /** DOCUMENT ME! */
    public static final int NOT_EQUAL = 125;

    /** DOCUMENT ME! */
    public static final int EQUAL = 126;

    /** DOCUMENT ME! */
    public static final int LT = 127;

    /** DOCUMENT ME! */
    public static final int GT = 128;

    /** DOCUMENT ME! */
    public static final int LE = 129;

    /** DOCUMENT ME! */
    public static final int GE = 130;

    /** DOCUMENT ME! */
    public static final int LITERAL_instanceof = 131;

    /** DOCUMENT ME! */
    public static final int SL = 132;

    /** DOCUMENT ME! */
    public static final int SR = 133;

    /** DOCUMENT ME! */
    public static final int BSR = 134;

    /** DOCUMENT ME! */
    public static final int PLUS = 135;

    /** DOCUMENT ME! */
    public static final int MINUS = 136;

    /** DOCUMENT ME! */
    public static final int DIV = 137;

    /** DOCUMENT ME! */
    public static final int MOD = 138;

    /** DOCUMENT ME! */
    public static final int INC = 139;

    /** DOCUMENT ME! */
    public static final int DEC = 140;

    /** DOCUMENT ME! */
    public static final int BNOT = 141;

    /** DOCUMENT ME! */
    public static final int LNOT = 142;

    /** DOCUMENT ME! */
    public static final int LITERAL_true = 143;

    /** DOCUMENT ME! */
    public static final int LITERAL_false = 144;

    /** DOCUMENT ME! */
    public static final int LITERAL_null = 145;

    /** DOCUMENT ME! */
    public static final int LITERAL_new = 146;

    /** DOCUMENT ME! */
    public static final int NUM_INT = 147;

    /** DOCUMENT ME! */
    public static final int CHAR_LITERAL = 148;

    /** DOCUMENT ME! */
    public static final int STRING_LITERAL = 149;

    /** DOCUMENT ME! */
    public static final int NUM_FLOAT = 150;

    /** DOCUMENT ME! */
    public static final int NUM_LONG = 151;

    /** DOCUMENT ME! */
    public static final int NUM_DOUBLE = 152;

    /** DOCUMENT ME! */
    public static final int WS = 153;

    /** DOCUMENT ME! */
    public static final int SPECIAL_COMMENT = 154;

    /** DOCUMENT ME! */
    public static final int SL_COMMENT = 155;

    /** DOCUMENT ME! */
    public static final int COMMENT = 156;

    /** DOCUMENT ME! */
    public static final int ML_COMMENT = 157;

    /** DOCUMENT ME! */
    public static final int ESC = 158;

    /** DOCUMENT ME! */
    public static final int HEX_DIGIT = 159;

    /** DOCUMENT ME! */
    public static final int VOCAB = 160;

    /** DOCUMENT ME! */
    public static final int EXPONENT = 161;

    /** DOCUMENT ME! */
    public static final int FLOAT_SUFFIX = 162;

    //~ Constructors ---------------------------------------------------------------------

    private JavaTokenTypes()
    {
    }
}
