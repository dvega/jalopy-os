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

// $ANTLR 2.7.2a2 (20020112-1): "src/java/de/hunsicker/jalopy/parser/java.doc.g" -> "JavadocParser.java"$
package de.hunsicker.jalopy.parser;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface JavadocTokenTypes
{
    //~ Instance variables ····················································

    int AT = 123;
    int ATTR = 134;
    int BR = 36;
    int CACRO = 118;
    int CADDRESS = 53;
    int CANCHOR = 120;
    int CBIG = 96;
    int CBOLD = 88;
    int CBQUOTE = 75;
    int CCAP = 80;
    int CCENTER = 73;
    int CCITE = 116;
    int CCODE = 90;
    int CDDEF = 67;
    int CDFN = 108;
    int CDIR = 69;
    int CDIR_OR_CDIV = 124;
    int CDIV = 71;
    int CDLIST = 61;
    int CDTERM = 65;
    int CEM = 104;
    int CFONT = 122;
    int CH1 = 41;
    int CH2 = 43;
    int CH3 = 45;
    int CH4 = 47;
    int CH5 = 49;
    int CH6 = 51;
    int CITALIC = 86;
    int CKBD = 112;
    int CLITEM = 63;
    int COLIST = 59;
    int COMMENT = 38;
    int COMMENT_DATA = 131;
    int CPARA = 55;
    int CSAMP = 110;
    int CSMALL = 98;
    int CSTRIKE = 94;
    int CSTRIKE_OR_CSTRONG = 128;
    int CSTRONG = 106;
    int CSUB = 100;
    int CSUB_OR_CSUP = 129;
    int CSUP = 102;
    int CTABLE = 78;
    int CTD = 12;
    int CTH = 10;
    int CTTYPE = 84;
    int CULIST = 57;
    int CUNDER = 92;
    int CVAR = 114;
    int C_TH_OR_TD = 126;
    int C_TR = 82;
    int DIGIT = 141;
    int EOF = 1;
    int HEXDIGIT = 142;
    int HEXINT = 140;
    int HEXNUM = 138;
    int HR = 34;
    int IMG = 35;
    int INT = 139;
    int JAVADOC_CLOSE = 33;
    int JAVADOC_COMMENT = 6;
    int JAVADOC_OPEN = 32;
    int LCLETTER = 143;
    int LCURLY = 7;
    int NEWLINE = 133;
    int NULL_TREE_LOOKAHEAD = 3;
    int OACRO = 117;
    int OADDRESS = 52;
    int OANCHOR = 119;
    int OBIG = 95;
    int OBOLD = 87;
    int OBQUOTE = 74;
    int OCAP = 79;
    int OCENTER = 72;
    int OCITE = 115;
    int OCODE = 89;
    int ODDEF = 66;
    int ODFN = 107;
    int ODIR = 68;
    int ODIV = 70;
    int ODLIST = 60;
    int ODTERM = 64;
    int OEM = 103;
    int OFONT = 121;
    int OH1 = 40;
    int OH2 = 42;
    int OH3 = 44;
    int OH4 = 46;
    int OH5 = 48;
    int OH6 = 50;
    int OITALIC = 85;
    int OKBD = 111;
    int OLITEM = 62;
    int OOLIST = 58;
    int OPARA = 54;
    int OSAMP = 109;
    int OSMALL = 97;
    int OSTRIKE = 93;
    int OSTRIKE_OR_OSTRONG = 127;
    int OSTRONG = 105;
    int OSUB = 99;
    int OSUP = 101;
    int OTABLE = 77;
    int OTD = 11;
    int OTH = 9;
    int OTTYPE = 83;
    int OULIST = 56;
    int OUNDER = 91;
    int OVAR = 113;
    int O_TH_OR_TD = 125;
    int O_TR = 81;
    int PCDATA = 39;
    int PRE = 76;
    int RCURLY = 8;
    int SPECIAL = 137;
    int STAR = 130;
    int STRING = 136;
    int TAG = 37;
    int TAG_AUTHOR = 14;
    int TAG_CUSTOM = 13;
    int TAG_DEPRECATED = 15;
    int TAG_EXCEPTION = 16;
    int TAG_INLINE_CUSTOM = 26;
    int TAG_INLINE_DOCROOT = 27;
    int TAG_INLINE_INHERITDOC = 28;
    int TAG_INLINE_LINK = 29;
    int TAG_INLINE_LINKPLAIN = 30;
    int TAG_INLINE_VALUE = 31;
    int TAG_PARAM = 18;
    int TAG_RETURN = 19;
    int TAG_SEE = 20;
    int TAG_SERIAL = 22;
    int TAG_SERIAL_DATA = 23;
    int TAG_SERIAL_FIELD = 24;
    int TAG_SINCE = 21;
    int TAG_THROWS = 17;
    int TAG_VERSION = 25;
    int WORD = 135;
    int WS = 132;
}
