/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer. 
 * 
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in 
 *    the documentation and/or other materials provided with the 
 *    distribution. 
 *
 * 3. Neither the name of the Jalopy project nor the names of its 
 *    contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS 
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE 
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS 
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id$
 */

// $ANTLR 2.7.2a2 (20020112-1): "src/java/de/hunsicker/jalopy/parser/java.g" -> "JavaLexer.java"$
package de.hunsicker.jalopy.parser;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface JavaTokenTypes
{
    //~ Instance variables ····················································

    int ABSTRACT = 45;
    int ARRAY_DECLARATOR = 22;
    int ASSIGN = 90;
    int BAND = 125;
    int BAND_ASSIGN = 117;
    int BLOCK = 9;
    int BLOCK_STATEMENT = 52;
    int BNOT = 142;
    int BOF = 49;
    int BOR = 123;
    int BOR_ASSIGN = 119;
    int BSR = 135;
    int BSR_ASSIGN = 115;
    int BXOR = 124;
    int BXOR_ASSIGN = 118;
    int CASESLIST = 51;
    int CHAR_LITERAL = 149;
    int CLASS_DEF = 19;
    int COLON = 92;
    int COMMA = 83;
    int COMMENT = 157;
    int CTOR_CALL = 48;
    int CTOR_DEF = 13;
    int DEC = 141;
    int DIV = 138;
    int DIV_ASSIGN = 112;
    int DOT = 70;
    int EOF = 1;
    int EXPR = 33;
    int ARRAY_INIT = 34;
    int CASE_GROUP = 38;
    int ELIST = 39;
    int EMPTY_STAT = 43;
    int EQUAL = 127;
    int ESC = 159;
    int EXPONENT = 162;
    int EXTENDS_CLAUSE = 23;
    int FINAL = 44;
    int FLOAT_SUFFIX = 163;
    int FOR_CONDITION = 41;
    int FOR_INIT = 40;
    int FOR_ITERATOR = 42;
    int GE = 131;
    int GT = 129;
    int HEX_DIGIT = 160;
    int IDENT = 69;
    int IMPLEMENTS_CLAUSE = 24;
    int IMPORT = 35;
    int INC = 140;
    int INDEX_OP = 29;
    int INSTANCE_INIT = 16;
    int INTERFACE_DEF = 20;
    int JAVADOC_COMMENT = 6;
    int LABELED_STAT = 27;
    int LAND = 122;
    int LBRACK = 58;
    int LCURLY = 7;
    int LE = 130;
    int LITERAL_assert = 99;
    int LITERAL_boolean = 61;
    int LITERAL_break = 98;
    int LITERAL_byte = 62;
    int LITERAL_case = 104;
    int LITERAL_catch = 107;
    int LITERAL_char = 63;
    int LITERAL_class = 80;
    int LITERAL_continue = 100;
    int LITERAL_default = 105;
    int LITERAL_do = 97;
    int LITERAL_double = 68;
    int LITERAL_else = 94;
    int LITERAL_extends = 81;
    int LITERAL_false = 145;
    int LITERAL_finally = 108;
    int LITERAL_float = 66;
    int LITERAL_for = 95;
    int LITERAL_if = 93;
    int LITERAL_implements = 84;
    int LITERAL_import = 57;
    int LITERAL_instanceof = 132;
    int LITERAL_int = 65;
    int LITERAL_interface = 82;
    int LITERAL_long = 67;
    int LITERAL_native = 77;
    int LITERAL_new = 147;
    int LITERAL_null = 146;
    int LITERAL_package = 55;
    int LITERAL_private = 72;
    int LITERAL_protected = 74;
    int LITERAL_public = 73;
    int LITERAL_return = 101;
    int LITERAL_short = 64;
    int LITERAL_static = 75;
    int LITERAL_super = 88;
    int LITERAL_switch = 102;
    int LITERAL_synchronized = 78;
    int LITERAL_this = 87;
    int LITERAL_throw = 103;
    int LITERAL_throws = 91;
    int LITERAL_transient = 76;
    int LITERAL_true = 144;
    int LITERAL_try = 106;
    int LITERAL_void = 60;
    int LITERAL_volatile = 79;
    int LITERAL_while = 96;
    int LNOT = 143;
    int LOR = 121;
    int LPAREN = 85;
    int LT = 128;
    int METHOD_CALL = 32;
    int METHOD_DEF = 14;
    int MINUS = 137;
    int MINUS_ASSIGN = 110;
    int ML_COMMENT = 158;
    int MOD = 139;
    int MODIFIERS = 10;
    int MOD_ASSIGN = 113;
    int NOT_EQUAL = 126;
    int NULL_TREE_LOOKAHEAD = 3;
    int NUM_DOUBLE = 153;
    int NUM_FLOAT = 151;
    int NUM_INT = 148;
    int NUM_LONG = 152;
    int OBJBLOCK = 11;
    int PACKAGE_DEF = 21;
    int PARAMETERS = 25;
    int PARAMETER_DEF = 26;
    int PLUS = 136;
    int PLUS_ASSIGN = 109;
    int POST_DEC = 31;
    int POST_INC = 30;
    int QUESTION = 120;
    int RBRACK = 59;
    int RCURLY = 8;
    int ROOT = 50;
    int RPAREN = 86;
    int SEMI = 56;
    int SEPARATOR_COMMENT = 53;
    int SL = 133;
    int SLIST = 12;
    int SL_ASSIGN = 116;
    int SL_COMMENT = 156;
    int SPECIAL_COMMENT = 155;
    int SR = 134;
    int SR_ASSIGN = 114;
    int STAR = 71;
    int STAR_ASSIGN = 111;
    int STATIC_INIT = 17;
    int STRICTFP = 46;
    int STRING_LITERAL = 150;
    int STR_supper = 89;
    int SUPER_CTOR_CALL = 47;
    int SYNBLOCK = 54;
    int TYPE = 18;
    int TYPECAST = 28;
    int UNARY_MINUS = 36;
    int UNARY_PLUS = 37;
    int VARIABLE_DEF = 15;
    int VOCAB = 161;
    int WS = 154;
}
