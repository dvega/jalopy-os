/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */

// $ANTLR 2.7.2a2 (20020112-1): "main/src/java/de/hunsicker/jalopy/language/java.doc.g" -> "JavadocParser.java"$
package de.hunsicker.jalopy.language;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import de.hunsicker.antlr.ASTPair;
import de.hunsicker.antlr.MismatchedTokenException;
import de.hunsicker.antlr.NoViableAltException;
import de.hunsicker.antlr.ParserSharedInputState;
import de.hunsicker.antlr.RecognitionException;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.TokenBuffer;
import de.hunsicker.antlr.TokenStream;
import de.hunsicker.antlr.TokenStreamException;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.antlr.collections.impl.BitSet;
import de.hunsicker.util.Lcs;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * Parser for Javadoc comments.
 * 
 * <p>
 * <strong>Sample Usage:</strong>
 * </p>
 * <pre>
 * <blockquote style="background:lightgrey">
 *  // an input source
 *  Reader in = new BufferedReader(new FileReader(new File(argv[0])));
 * 
 *  // create a lexer
 *  Lexer lexer = new JavadocLexer();
 * 
 *  // set up the lexer to read from the input source
 *  lexer.setInputBuffer(in);
 * 
 *  // get the corresponding parser
 *  Parser parser = lexer.getParser();
 * 
 *  // and start the parsing process
 *  parser.parse();
 * </blockquote>
 * </pre>
 * 
 * <p>
 * This is an <a href="http://www.antlr.org">ANTLR</a> automated generated file.
 * <strong>DO NOT EDIT</strong> but rather change the associated grammar
 * (<code>java.doc.g</code>) and rebuild.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version 1.0
 *
 * @see de.hunsicker.jalopy.language.JavadocLexer
 * @see de.hunsicker.jalopy.language.Recognizer
 */
public final class JavadocParser
    extends de.hunsicker.antlr.LLkParser
    implements Parser
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The empty Javadoc comment. */
    public static final Node EMPTY_JAVADOC_COMMENT =
        new Node(JavadocTokenTypes.JAVADOC_COMMENT, "<JAVADOC_COMMENT>");

    /** Indicates a standard Javadoc tag. */
    private static final String TYPE_STANDARD = "TAG_";

    /** Indicates an inline Javadoc tag. */
    private static final String TYPE_INLINE = "TAG_INLINE_";
    private static final String JAVADOC_COMMENT = "JAVADOC_COMMENT";

    /** DOCUMENT ME! */
    public static final String[] _tokenNames =
    {
        "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "<4>", "<5>", "JAVADOC_COMMENT",
        "LCURLY", "RCURLY", "OTH", "CTH", "OTD", "CTD", "TAG_CUSTOM", "TAG_AUTHOR",
        "TAG_DEPRECATED", "TAG_EXCEPTION", "TAG_THROWS", "TAG_PARAM", "TAG_RETURN",
        "TAG_SEE", "TAG_SINCE", "TAG_SERIAL", "TAG_SERIAL_DATA", "TAG_SERIAL_FIELD",
        "TAG_VERSION", "TAG_INLINE_CUSTOM", "TAG_INLINE_DOCROOT", "TAG_INLINE_INHERITDOC",
        "TAG_INLINE_LINK", "TAG_INLINE_LINKPLAIN", "TAG_INLINE_VALUE", "TAG_TODO",
        "JAVADOC_OPEN", "JAVADOC_CLOSE", "HR", "IMG", "BR", "TAG", "COMMENT", "PCDATA",
        "OH1", "CH1", "OH2", "CH2", "OH3", "CH3", "OH4", "CH4", "OH5", "CH5", "OH6", "CH6",
        "OADDRESS", "CADDRESS", "OPARA", "CPARA", "OULIST", "CULIST", "OOLIST", "COLIST",
        "ODLIST", "CDLIST", "OLITEM", "CLITEM", "ODTERM", "CDTERM", "ODDEF", "CDDEF",
        "ODIR", "CDIR", "ODIV", "CDIV", "OCENTER", "CCENTER", "OBQUOTE", "CBQUOTE", "PRE",
        "OTABLE", "CTABLE", "OCAP", "CCAP", "O_TR", "C_TR", "OTTYPE", "CTTYPE", "OITALIC",
        "CITALIC", "OBOLD", "CBOLD", "OCODE", "CCODE", "OUNDER", "CUNDER", "OSTRIKE",
        "CSTRIKE", "OBIG", "CBIG", "OSMALL", "CSMALL", "OSUB", "CSUB", "OSUP", "CSUP",
        "OEM", "CEM", "OSTRONG", "CSTRONG", "ODFN", "CDFN", "OSAMP", "CSAMP", "OKBD",
        "CKBD", "OVAR", "CVAR", "OCITE", "CCITE", "OACRO", "CACRO", "OANCHOR", "CANCHOR",
        "OFONT", "CFONT", "AT", "CDIR_OR_CDIV", "O_TH_OR_TD", "C_TH_OR_TD",
        "OSTRIKE_OR_OSTRONG", "CSTRIKE_OR_CSTRONG", "CSUB_OR_CSUP", "STAR", "COMMENT_DATA",
        "WS", "NEWLINE", "ATTR", "WORD", "STRING", "SPECIAL", "HEXNUM", "INT", "HEXINT",
        "DIGIT", "HEXDIGIT", "LCLETTER"
    };
    private static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    private static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    private static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    private static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    private static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    private static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    private static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    private static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
    private static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
    private static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
    private static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
    private static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
    private static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
    private static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
    private static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
    private static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
    private static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
    private static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
    private static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());

    //~ Instance variables ---------------------------------------------------------------

    /** Logging. */
    private final Logger _logger =
        Logger.getLogger("de.hunsicker.jalopy.language.javadoc");

    /** The corresponding lexer. */
    private JavadocLexer _lexer;

    /** The token types for the parser/lexer. */
    private Map _tokenTypes; // Map of <String>

    /** Holds all valid inline tag names. */
    private Set _inlineTags = new HashSet(); // Set of <String>

    /** Holds all valid tag names. */
    private Set _standardTags = new HashSet(); // Set of <String>

    /** Starting column of the comment in the source file. */
    private int _startColumn;

    /** Starting line of the comment in the source file. */
    private int _startLine;

    /** Last modification stamp of the property file for inline Javadoc tags. */
    private long _inlineStamp;

    /** Last modification stamp of the property file for standard Javadoc tags. */
    private long _standardStamp;

    {
        loadTokenTypeInfo();
        loadTagInfo(true);
    }

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JavadocParser object.
     *
     * @param tokenBuf DOCUMENT ME!
     */
    public JavadocParser(TokenBuffer tokenBuf)
    {
        this(tokenBuf, 1);
    }


    /**
     * Creates a new JavadocParser object.
     *
     * @param lexer DOCUMENT ME!
     */
    public JavadocParser(TokenStream lexer)
    {
        this(lexer, 1);
    }


    /**
     * Creates a new JavadocParser object.
     *
     * @param state DOCUMENT ME!
     */
    public JavadocParser(ParserSharedInputState state)
    {
        super(state, 1);
        tokenNames = _tokenNames;
    }


    /**
     * Creates a new JavadocParser object.
     *
     * @param tokenBuf DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    protected JavadocParser(
        TokenBuffer tokenBuf,
        int         k)
    {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }


    /**
     * Creates a new JavadocParser object.
     *
     * @param lexer DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    protected JavadocParser(
        TokenStream lexer,
        int         k)
    {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Sets the custom Javadoc in-line tags to recognize.
     *
     * @param tags tags.
     */
    public void setCustomInlineTags(Collection tags)
    {
        _inlineTags.addAll(tags);
    }


    /**
     * Sets the custom Javadoc standard tags to recognize.
     *
     * @param tags tags.
     */
    public void setCustomStandardTags(Collection tags)
    {
        _standardTags.addAll(tags);
    }


    /**
     * Sets the corresponding Javadoc lexer for the parser.
     *
     * @param lexer corresponding Javadoc lexer.
     */
    public void setLexer(JavadocLexer lexer)
    {
        _lexer = lexer;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AST getParseTree()
    {
        // can be null for empty comments. The empty node indicates that this
        // comment can be savely ignored. It is up to the caller to handle
        // it appropriate
        if (this.returnAST == null)
        {
            return EMPTY_JAVADOC_COMMENT;
        }

        Node current = (Node) this.returnAST;
        Node root =
            new Node(_startLine, _startColumn, current.endLine, current.endColumn);
        root.setType(JavadocTokenTypes.JAVADOC_COMMENT);
        root.setText(JAVADOC_COMMENT);
        root.setFirstChild(this.returnAST);

        return root;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void acronym()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST acronym_AST = null;

        try
        { // for error handling

            AST tmp70_AST = null;
            tmp70_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp70_AST);
            match(JavadocTokenTypes.OACRO);

            {
                int _cnt162 = 0;
_loop162: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt162 >= 1)
                        {
                            break _loop162;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt162++;
                }
                while (true);
            }

            AST tmp71_AST = null;
            tmp71_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp71_AST);
            match(JavadocTokenTypes.CACRO);
            acronym_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = acronym_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void address()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST address_AST = null;

        try
        { // for error handling

            AST tmp9_AST = null;
            tmp9_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp9_AST);
            match(JavadocTokenTypes.OADDRESS);
_loop28: 
            do
            {
                if ((LA(1) == JavadocTokenTypes.PCDATA))
                {
                    AST tmp10_AST = null;
                    tmp10_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp10_AST);
                    match(JavadocTokenTypes.PCDATA);
                }
                else
                {
                    break _loop28;
                }
            }
            while (true);

            AST tmp11_AST = null;
            tmp11_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp11_AST);
            match(JavadocTokenTypes.CADDRESS);
            address_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = address_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void anchor()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST anchor_AST = null;

        try
        { // for error handling

            AST tmp74_AST = null;
            tmp74_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp74_AST);
            match(JavadocTokenTypes.OANCHOR);
_loop165: 
            do
            {
                if ((_tokenSet_10.member(LA(1))))
                {
                    anchor_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop165;
                }
            }
            while (true);

            AST tmp75_AST = null;
            tmp75_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp75_AST);
            match(JavadocTokenTypes.CANCHOR);
            anchor_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = anchor_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void anchor_content()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST anchor_content_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OTTYPE :
                case JavadocTokenTypes.OITALIC :
                case JavadocTokenTypes.OBOLD :
                case JavadocTokenTypes.OUNDER :
                case JavadocTokenTypes.OSTRIKE :
                case JavadocTokenTypes.OBIG :
                case JavadocTokenTypes.OSMALL :
                case JavadocTokenTypes.OSUB :
                case JavadocTokenTypes.OSUP :
                {
                    font();
                    astFactory.addASTChild(currentAST, returnAST);
                    anchor_content_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OCODE :
                case JavadocTokenTypes.OEM :
                case JavadocTokenTypes.OSTRONG :
                case JavadocTokenTypes.ODFN :
                case JavadocTokenTypes.OSAMP :
                case JavadocTokenTypes.OKBD :
                case JavadocTokenTypes.OVAR :
                case JavadocTokenTypes.OCITE :
                case JavadocTokenTypes.OACRO :
                {
                    phrase();
                    astFactory.addASTChild(currentAST, returnAST);
                    anchor_content_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.PCDATA :
                {
                    AST tmp114_AST = null;
                    tmp114_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp114_AST);
                    match(JavadocTokenTypes.PCDATA);
                    anchor_content_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_18);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = anchor_content_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void big()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST big_AST = null;

        try
        { // for error handling

            AST tmp46_AST = null;
            tmp46_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp46_AST);
            match(JavadocTokenTypes.OBIG);

            {
                int _cnt129 = 0;
_loop129: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt129 >= 1)
                        {
                            break _loop129;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt129++;
                }
                while (true);
            }

            AST tmp47_AST = null;
            tmp47_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp47_AST);
            match(JavadocTokenTypes.CBIG);
            big_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = big_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void block()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST block_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OPARA :
                {
                    paragraph();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OULIST :
                case JavadocTokenTypes.OOLIST :
                case JavadocTokenTypes.ODLIST :
                {
                    list();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.PRE :
                {
                    preformatted();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.ODIV :
                {
                    div();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OCENTER :
                {
                    center();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OBQUOTE :
                {
                    blockquote();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.HR :
                {
                    AST tmp8_AST = null;
                    tmp8_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp8_AST);
                    match(JavadocTokenTypes.HR);
                    block_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OTABLE :
                {
                    table();
                    astFactory.addASTChild(currentAST, returnAST);
                    block_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_5);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = block_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void blockquote()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST blockquote_AST = null;

        try
        { // for error handling

            AST tmp31_AST = null;
            tmp31_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp31_AST);
            match(JavadocTokenTypes.OBQUOTE);
_loop80: 
            do
            {
                if ((_tokenSet_0.member(LA(1))))
                {
                    body_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop80;
                }
            }
            while (true);

            AST tmp32_AST = null;
            tmp32_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp32_AST);
            match(JavadocTokenTypes.CBQUOTE);
            blockquote_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = blockquote_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void body_content()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST body_content_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.HR :
                case JavadocTokenTypes.OH1 :
                case JavadocTokenTypes.OH2 :
                case JavadocTokenTypes.OH3 :
                case JavadocTokenTypes.OH4 :
                case JavadocTokenTypes.OH5 :
                case JavadocTokenTypes.OH6 :
                case JavadocTokenTypes.OADDRESS :
                case JavadocTokenTypes.OPARA :
                case JavadocTokenTypes.OULIST :
                case JavadocTokenTypes.OOLIST :
                case JavadocTokenTypes.ODLIST :
                case JavadocTokenTypes.ODIV :
                case JavadocTokenTypes.OCENTER :
                case JavadocTokenTypes.OBQUOTE :
                case JavadocTokenTypes.PRE :
                case JavadocTokenTypes.OTABLE :
                {
                    body_tag();
                    astFactory.addASTChild(currentAST, returnAST);
                    body_content_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.LCURLY :
                case JavadocTokenTypes.RCURLY :
                case JavadocTokenTypes.IMG :
                case JavadocTokenTypes.BR :
                case JavadocTokenTypes.COMMENT :
                case JavadocTokenTypes.PCDATA :
                case JavadocTokenTypes.OTTYPE :
                case JavadocTokenTypes.OITALIC :
                case JavadocTokenTypes.OBOLD :
                case JavadocTokenTypes.OCODE :
                case JavadocTokenTypes.OUNDER :
                case JavadocTokenTypes.OSTRIKE :
                case JavadocTokenTypes.OBIG :
                case JavadocTokenTypes.OSMALL :
                case JavadocTokenTypes.OSUB :
                case JavadocTokenTypes.OSUP :
                case JavadocTokenTypes.OEM :
                case JavadocTokenTypes.OSTRONG :
                case JavadocTokenTypes.ODFN :
                case JavadocTokenTypes.OSAMP :
                case JavadocTokenTypes.OKBD :
                case JavadocTokenTypes.OVAR :
                case JavadocTokenTypes.OCITE :
                case JavadocTokenTypes.OACRO :
                case JavadocTokenTypes.OANCHOR :
                case JavadocTokenTypes.OFONT :
                {
                    text();
                    astFactory.addASTChild(currentAST, returnAST);
                    body_content_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = body_content_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void body_tag()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST body_tag_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.HR :
                case JavadocTokenTypes.OPARA :
                case JavadocTokenTypes.OULIST :
                case JavadocTokenTypes.OOLIST :
                case JavadocTokenTypes.ODLIST :
                case JavadocTokenTypes.ODIV :
                case JavadocTokenTypes.OCENTER :
                case JavadocTokenTypes.OBQUOTE :
                case JavadocTokenTypes.PRE :
                case JavadocTokenTypes.OTABLE :
                {
                    block();
                    astFactory.addASTChild(currentAST, returnAST);
                    body_tag_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OADDRESS :
                {
                    address();
                    astFactory.addASTChild(currentAST, returnAST);
                    body_tag_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH1 :
                case JavadocTokenTypes.OH2 :
                case JavadocTokenTypes.OH3 :
                case JavadocTokenTypes.OH4 :
                case JavadocTokenTypes.OH5 :
                case JavadocTokenTypes.OH6 :
                {
                    heading();
                    astFactory.addASTChild(currentAST, returnAST);
                    body_tag_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = body_tag_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void bold()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST bold_AST = null;

        try
        { // for error handling

            AST tmp40_AST = null;
            tmp40_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp40_AST);
            match(JavadocTokenTypes.OBOLD);

            {
                int _cnt117 = 0;
_loop117: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt117 >= 1)
                        {
                            break _loop117;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt117++;
                }
                while (true);
            }

            AST tmp41_AST = null;
            tmp41_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp41_AST);
            match(JavadocTokenTypes.CBOLD);
            bold_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = bold_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void caption()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST caption_AST = null;

        try
        { // for error handling

            AST tmp101_AST = null;
            tmp101_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp101_AST);
            match(JavadocTokenTypes.OCAP);
_loop90: 
            do
            {
                if ((_tokenSet_6.member(LA(1))))
                {
                    text();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop90;
                }
            }
            while (true);

            AST tmp102_AST = null;
            tmp102_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp102_AST);
            match(JavadocTokenTypes.CCAP);
            caption_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_15);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = caption_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void center()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST center_AST = null;

        try
        { // for error handling

            AST tmp29_AST = null;
            tmp29_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp29_AST);
            match(JavadocTokenTypes.OCENTER);
_loop77: 
            do
            {
                if ((_tokenSet_0.member(LA(1))))
                {
                    body_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop77;
                }
            }
            while (true);

            AST tmp30_AST = null;
            tmp30_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp30_AST);
            match(JavadocTokenTypes.CCENTER);
            center_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = center_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void citation()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST citation_AST = null;

        try
        { // for error handling

            AST tmp68_AST = null;
            tmp68_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp68_AST);
            match(JavadocTokenTypes.OCITE);

            {
                int _cnt159 = 0;
_loop159: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt159 >= 1)
                        {
                            break _loop159;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt159++;
                }
                while (true);
            }

            AST tmp69_AST = null;
            tmp69_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp69_AST);
            match(JavadocTokenTypes.CCITE);
            citation_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = citation_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void code()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST code_AST = null;

        try
        { // for error handling

            AST tmp60_AST = null;
            tmp60_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp60_AST);
            match(JavadocTokenTypes.OCODE);

            {
                int _cnt120 = 0;
_loop120: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt120 >= 1)
                        {
                            break _loop120;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt120++;
                }
                while (true);
            }

            AST tmp61_AST = null;
            tmp61_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp61_AST);
            match(JavadocTokenTypes.CCODE);
            code_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = code_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void dd()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST dd_AST = null;

        try
        { // for error handling

            AST tmp96_AST = null;
            tmp96_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp96_AST);
            match(JavadocTokenTypes.ODDEF);

            {
                int _cnt64 = 0;
_loop64: 
                do
                {
                    switch (LA(1))
                    {
                        case JavadocTokenTypes.LCURLY :
                        case JavadocTokenTypes.RCURLY :
                        case JavadocTokenTypes.IMG :
                        case JavadocTokenTypes.BR :
                        case JavadocTokenTypes.COMMENT :
                        case JavadocTokenTypes.PCDATA :
                        case JavadocTokenTypes.OTTYPE :
                        case JavadocTokenTypes.OITALIC :
                        case JavadocTokenTypes.OBOLD :
                        case JavadocTokenTypes.OCODE :
                        case JavadocTokenTypes.OUNDER :
                        case JavadocTokenTypes.OSTRIKE :
                        case JavadocTokenTypes.OBIG :
                        case JavadocTokenTypes.OSMALL :
                        case JavadocTokenTypes.OSUB :
                        case JavadocTokenTypes.OSUP :
                        case JavadocTokenTypes.OEM :
                        case JavadocTokenTypes.OSTRONG :
                        case JavadocTokenTypes.ODFN :
                        case JavadocTokenTypes.OSAMP :
                        case JavadocTokenTypes.OKBD :
                        case JavadocTokenTypes.OVAR :
                        case JavadocTokenTypes.OCITE :
                        case JavadocTokenTypes.OACRO :
                        case JavadocTokenTypes.OANCHOR :
                        case JavadocTokenTypes.OFONT :
                        {
                            text();
                            astFactory.addASTChild(currentAST, returnAST);

                            break;
                        }

                        case JavadocTokenTypes.HR :
                        case JavadocTokenTypes.OPARA :
                        case JavadocTokenTypes.OULIST :
                        case JavadocTokenTypes.OOLIST :
                        case JavadocTokenTypes.ODLIST :
                        case JavadocTokenTypes.ODIV :
                        case JavadocTokenTypes.OCENTER :
                        case JavadocTokenTypes.OBQUOTE :
                        case JavadocTokenTypes.PRE :
                        case JavadocTokenTypes.OTABLE :
                        {
                            block();
                            astFactory.addASTChild(currentAST, returnAST);

                            break;
                        }

                        default :
                        {
                            if (_cnt64 >= 1)
                            {
                                break _loop64;
                            }
                            else
                            {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                    }

                    _cnt64++;
                }
                while (true);
            }

            switch (LA(1))
            {
                case JavadocTokenTypes.CDDEF :
                {
                    AST tmp97_AST = null;
                    tmp97_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp97_AST);
                    match(JavadocTokenTypes.CDDEF);
_loop67: 
                    do
                    {
                        if ((LA(1) == JavadocTokenTypes.PCDATA))
                        {
                            AST tmp98_AST = null;
                            tmp98_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp98_AST);
                            match(JavadocTokenTypes.PCDATA);
                        }
                        else
                        {
                            break _loop67;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.CDLIST :
                case JavadocTokenTypes.ODTERM :
                case JavadocTokenTypes.ODDEF :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            dd_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_14);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = dd_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void def_list()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST def_list_AST = null;

        try
        { // for error handling

            AST tmp87_AST = null;
            tmp87_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp87_AST);
            match(JavadocTokenTypes.ODLIST);
_loop46: 
            do
            {
                if ((LA(1) == JavadocTokenTypes.PCDATA))
                {
                    AST tmp88_AST = null;
                    tmp88_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp88_AST);
                    match(JavadocTokenTypes.PCDATA);
                }
                else
                {
                    break _loop46;
                }
            }
            while (true);

            {
                int _cnt48 = 0;
_loop48: 
                do
                {
                    if (
                        ((LA(1) == JavadocTokenTypes.ODTERM)
                        || (LA(1) == JavadocTokenTypes.ODDEF)))
                    {
                        def_list_item();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt48 >= 1)
                        {
                            break _loop48;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt48++;
                }
                while (true);
            }

            AST tmp89_AST = null;
            tmp89_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp89_AST);
            match(JavadocTokenTypes.CDLIST);
            def_list_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = def_list_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void def_list_item()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST def_list_item_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.ODTERM :
                {
                    dt();
                    astFactory.addASTChild(currentAST, returnAST);
                    def_list_item_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.ODDEF :
                {
                    dd();
                    astFactory.addASTChild(currentAST, returnAST);
                    def_list_item_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_14);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = def_list_item_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void definition()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST definition_AST = null;

        try
        { // for error handling

            AST tmp58_AST = null;
            tmp58_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp58_AST);
            match(JavadocTokenTypes.ODFN);

            {
                int _cnt147 = 0;
_loop147: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt147 >= 1)
                        {
                            break _loop147;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt147++;
                }
                while (true);
            }

            AST tmp59_AST = null;
            tmp59_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp59_AST);
            match(JavadocTokenTypes.CDFN);
            definition_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = definition_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void dir()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST dir_AST = null;

        try
        { // for error handling

            AST tmp99_AST = null;
            tmp99_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp99_AST);
            match(JavadocTokenTypes.ODIR);

            {
                int _cnt70 = 0;
_loop70: 
                do
                {
                    if ((LA(1) == JavadocTokenTypes.OLITEM))
                    {
                        list_item();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt70 >= 1)
                        {
                            break _loop70;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt70++;
                }
                while (true);
            }

            switch (LA(1))
            {
                case JavadocTokenTypes.CDIR :
                {
                    AST tmp100_AST = null;
                    tmp100_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp100_AST);
                    match(JavadocTokenTypes.CDIR);

                    break;
                }

                case JavadocTokenTypes.EOF :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            dir_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_1);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = dir_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void div()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST div_AST = null;

        try
        { // for error handling

            AST tmp27_AST = null;
            tmp27_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp27_AST);
            match(JavadocTokenTypes.ODIV);
_loop74: 
            do
            {
                if ((_tokenSet_0.member(LA(1))))
                {
                    body_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop74;
                }
            }
            while (true);

            AST tmp28_AST = null;
            tmp28_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp28_AST);
            match(JavadocTokenTypes.CDIV);
            div_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = div_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void dt()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST dt_AST = null;

        try
        { // for error handling

            AST tmp93_AST = null;
            tmp93_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp93_AST);
            match(JavadocTokenTypes.ODTERM);

            {
                int _cnt58 = 0;
_loop58: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt58 >= 1)
                        {
                            break _loop58;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt58++;
                }
                while (true);
            }

            switch (LA(1))
            {
                case JavadocTokenTypes.CDTERM :
                {
                    AST tmp94_AST = null;
                    tmp94_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp94_AST);
                    match(JavadocTokenTypes.CDTERM);
_loop61: 
                    do
                    {
                        if ((LA(1) == JavadocTokenTypes.PCDATA))
                        {
                            AST tmp95_AST = null;
                            tmp95_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp95_AST);
                            match(JavadocTokenTypes.PCDATA);
                        }
                        else
                        {
                            break _loop61;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.CDLIST :
                case JavadocTokenTypes.ODTERM :
                case JavadocTokenTypes.ODDEF :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            dt_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_14);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = dt_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void emphasize()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST emphasize_AST = null;

        try
        { // for error handling

            AST tmp54_AST = null;
            tmp54_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp54_AST);
            match(JavadocTokenTypes.OEM);

            {
                int _cnt141 = 0;
_loop141: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt141 >= 1)
                        {
                            break _loop141;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt141++;
                }
                while (true);
            }

            AST tmp55_AST = null;
            tmp55_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp55_AST);
            match(JavadocTokenTypes.CEM);
            emphasize_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = emphasize_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void font()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST font_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OTTYPE :
                {
                    teletype();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OITALIC :
                {
                    italic();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OBOLD :
                {
                    bold();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OUNDER :
                {
                    underline();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSTRIKE :
                {
                    strike();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OBIG :
                {
                    big();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSMALL :
                {
                    small();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSUB :
                {
                    subscript();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSUP :
                {
                    superscript();
                    astFactory.addASTChild(currentAST, returnAST);
                    font_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = font_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void font_dfn()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST font_dfn_AST = null;

        try
        { // for error handling

            AST tmp76_AST = null;
            tmp76_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp76_AST);
            match(JavadocTokenTypes.OFONT);
_loop169: 
            do
            {
                if ((_tokenSet_6.member(LA(1))))
                {
                    text();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop169;
                }
            }
            while (true);

            AST tmp77_AST = null;
            tmp77_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp77_AST);
            match(JavadocTokenTypes.CFONT);
            font_dfn_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = font_dfn_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h1()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h1_AST = null;

        try
        { // for error handling

            AST tmp12_AST = null;
            tmp12_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp12_AST);
            match(JavadocTokenTypes.OH1);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp13_AST = null;
            tmp13_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp13_AST);
            match(JavadocTokenTypes.CH1);
            h1_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h1_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h2()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h2_AST = null;

        try
        { // for error handling

            AST tmp14_AST = null;
            tmp14_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp14_AST);
            match(JavadocTokenTypes.OH2);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp15_AST = null;
            tmp15_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp15_AST);
            match(JavadocTokenTypes.CH2);
            h2_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h2_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h3()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h3_AST = null;

        try
        { // for error handling

            AST tmp16_AST = null;
            tmp16_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp16_AST);
            match(JavadocTokenTypes.OH3);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp17_AST = null;
            tmp17_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp17_AST);
            match(JavadocTokenTypes.CH3);
            h3_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h3_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h4()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h4_AST = null;

        try
        { // for error handling

            AST tmp18_AST = null;
            tmp18_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp18_AST);
            match(JavadocTokenTypes.OH4);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp19_AST = null;
            tmp19_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp19_AST);
            match(JavadocTokenTypes.CH4);
            h4_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h4_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h5()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h5_AST = null;

        try
        { // for error handling

            AST tmp20_AST = null;
            tmp20_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp20_AST);
            match(JavadocTokenTypes.OH5);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp21_AST = null;
            tmp21_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp21_AST);
            match(JavadocTokenTypes.CH5);
            h5_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h5_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void h6()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST h6_AST = null;

        try
        { // for error handling

            AST tmp22_AST = null;
            tmp22_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp22_AST);
            match(JavadocTokenTypes.OH6);
            heading_content();
            astFactory.addASTChild(currentAST, returnAST);

            AST tmp23_AST = null;
            tmp23_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp23_AST);
            match(JavadocTokenTypes.CH6);
            h6_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = h6_AST;
    }


    /**
     * Handler for recoverable errors. If the error can't be handled it will be rethrown.
     *
     * @param ex the input violation exception.
     */
    public void handleRecoverableError(RecognitionException ex)
    {
        if (ex instanceof de.hunsicker.antlr.MismatchedTokenException)
        {
            MismatchedTokenException mtex = (MismatchedTokenException) ex;

            if (mtex.token != null)
            {
                switch (mtex.expecting)
                {
                    // missing closing </p> tag
                    case JavadocTokenTypes.CPARA :
                        System.err.println(
                            "[WARN] ambigious missing </p> tag around line "
                            + mtex.token.getLine() + " ");

                        // we add the found token manually
                        ASTPair currentAST = new ASTPair();
                        AST tmp2_AST = this.astFactory.create(mtex.token);
                        this.astFactory.makeASTRoot(currentAST, tmp2_AST);
                        this.astFactory.addASTChild(currentAST, this.returnAST);

                        // we also have to increase the line counter
                        _lexer.setLine(_lexer.getLine() + 1);

                        // now the parsing can go on
                        break;

                    default :
                        // we can't handle the error situation
                        reportError(ex);
                }
            }
            else
            {
                // we can't handle the error situation
                reportError(ex);
            }
        }
        else
        {
            // we can't handle the error situation
            reportError(ex);
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void heading()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST heading_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OH1 :
                {
                    h1();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH2 :
                {
                    h2();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH3 :
                {
                    h3();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH4 :
                {
                    h4();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH5 :
                {
                    h5();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OH6 :
                {
                    h6();
                    astFactory.addASTChild(currentAST, returnAST);
                    heading_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_2);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = heading_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void heading_content()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST heading_content_AST = null;

        try
        { // for error handling
_loop25: 
            do
            {
                switch (LA(1))
                {
                    case JavadocTokenTypes.HR :
                    case JavadocTokenTypes.OPARA :
                    case JavadocTokenTypes.OULIST :
                    case JavadocTokenTypes.OOLIST :
                    case JavadocTokenTypes.ODLIST :
                    case JavadocTokenTypes.ODIV :
                    case JavadocTokenTypes.OCENTER :
                    case JavadocTokenTypes.OBQUOTE :
                    case JavadocTokenTypes.PRE :
                    case JavadocTokenTypes.OTABLE :
                    {
                        block();
                        astFactory.addASTChild(currentAST, returnAST);

                        break;
                    }

                    case JavadocTokenTypes.LCURLY :
                    case JavadocTokenTypes.RCURLY :
                    case JavadocTokenTypes.IMG :
                    case JavadocTokenTypes.BR :
                    case JavadocTokenTypes.COMMENT :
                    case JavadocTokenTypes.PCDATA :
                    case JavadocTokenTypes.OTTYPE :
                    case JavadocTokenTypes.OITALIC :
                    case JavadocTokenTypes.OBOLD :
                    case JavadocTokenTypes.OCODE :
                    case JavadocTokenTypes.OUNDER :
                    case JavadocTokenTypes.OSTRIKE :
                    case JavadocTokenTypes.OBIG :
                    case JavadocTokenTypes.OSMALL :
                    case JavadocTokenTypes.OSUB :
                    case JavadocTokenTypes.OSUP :
                    case JavadocTokenTypes.OEM :
                    case JavadocTokenTypes.OSTRONG :
                    case JavadocTokenTypes.ODFN :
                    case JavadocTokenTypes.OSAMP :
                    case JavadocTokenTypes.OKBD :
                    case JavadocTokenTypes.OVAR :
                    case JavadocTokenTypes.OCITE :
                    case JavadocTokenTypes.OACRO :
                    case JavadocTokenTypes.OANCHOR :
                    case JavadocTokenTypes.OFONT :
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);

                        break;
                    }

                    default :
                        break _loop25;
                }
            }
            while (true);

            heading_content_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_12);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = heading_content_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void inline_tag()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST inline_tag_AST = null;
        Token tag = null;
        AST tag_AST = null;

        try
        { // for error handling
            match(JavadocTokenTypes.LCURLY);
            tag = LT(1);
            tag_AST = astFactory.create(tag);
            astFactory.makeASTRoot(currentAST, tag_AST);
            match(JavadocTokenTypes.TAG);

            if (inputState.guessing == 0)
            {
                setTagType(tag_AST, TYPE_INLINE);
            }

_loop176: 
            do
            {
                if ((_tokenSet_11.member(LA(1))))
                {
                    AST tmp79_AST = null;
                    tmp79_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp79_AST);
                    matchNot(JavadocTokenTypes.RCURLY);
                }
                else
                {
                    break _loop176;
                }
            }
            while (true);

            match(JavadocTokenTypes.RCURLY);
            inline_tag_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = inline_tag_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void italic()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST italic_AST = null;

        try
        { // for error handling

            AST tmp38_AST = null;
            tmp38_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp38_AST);
            match(JavadocTokenTypes.OITALIC);

            {
                int _cnt114 = 0;
_loop114: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt114 >= 1)
                        {
                            break _loop114;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt114++;
                }
                while (true);
            }

            AST tmp39_AST = null;
            tmp39_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp39_AST);
            match(JavadocTokenTypes.CITALIC);
            italic_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = italic_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void keyboard()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST keyboard_AST = null;

        try
        { // for error handling

            AST tmp64_AST = null;
            tmp64_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp64_AST);
            match(JavadocTokenTypes.OKBD);

            {
                int _cnt153 = 0;
_loop153: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt153 >= 1)
                        {
                            break _loop153;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt153++;
                }
                while (true);
            }

            AST tmp65_AST = null;
            tmp65_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp65_AST);
            match(JavadocTokenTypes.CKBD);
            keyboard_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = keyboard_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void list()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST list_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OULIST :
                {
                    unordered_list();
                    astFactory.addASTChild(currentAST, returnAST);
                    list_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OOLIST :
                {
                    ordered_list();
                    astFactory.addASTChild(currentAST, returnAST);
                    list_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.ODLIST :
                {
                    def_list();
                    astFactory.addASTChild(currentAST, returnAST);
                    list_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = list_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void list_item()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST list_item_AST = null;

        try
        { // for error handling

            AST tmp90_AST = null;
            tmp90_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp90_AST);
            match(JavadocTokenTypes.OLITEM);

            {
                int _cnt51 = 0;
_loop51: 
                do
                {
                    switch (LA(1))
                    {
                        case JavadocTokenTypes.LCURLY :
                        case JavadocTokenTypes.RCURLY :
                        case JavadocTokenTypes.IMG :
                        case JavadocTokenTypes.BR :
                        case JavadocTokenTypes.COMMENT :
                        case JavadocTokenTypes.PCDATA :
                        case JavadocTokenTypes.OTTYPE :
                        case JavadocTokenTypes.OITALIC :
                        case JavadocTokenTypes.OBOLD :
                        case JavadocTokenTypes.OCODE :
                        case JavadocTokenTypes.OUNDER :
                        case JavadocTokenTypes.OSTRIKE :
                        case JavadocTokenTypes.OBIG :
                        case JavadocTokenTypes.OSMALL :
                        case JavadocTokenTypes.OSUB :
                        case JavadocTokenTypes.OSUP :
                        case JavadocTokenTypes.OEM :
                        case JavadocTokenTypes.OSTRONG :
                        case JavadocTokenTypes.ODFN :
                        case JavadocTokenTypes.OSAMP :
                        case JavadocTokenTypes.OKBD :
                        case JavadocTokenTypes.OVAR :
                        case JavadocTokenTypes.OCITE :
                        case JavadocTokenTypes.OACRO :
                        case JavadocTokenTypes.OANCHOR :
                        case JavadocTokenTypes.OFONT :
                        {
                            text();
                            astFactory.addASTChild(currentAST, returnAST);

                            break;
                        }

                        case JavadocTokenTypes.HR :
                        case JavadocTokenTypes.OPARA :
                        case JavadocTokenTypes.OULIST :
                        case JavadocTokenTypes.OOLIST :
                        case JavadocTokenTypes.ODLIST :
                        case JavadocTokenTypes.ODIV :
                        case JavadocTokenTypes.OCENTER :
                        case JavadocTokenTypes.OBQUOTE :
                        case JavadocTokenTypes.PRE :
                        case JavadocTokenTypes.OTABLE :
                        {
                            block();
                            astFactory.addASTChild(currentAST, returnAST);

                            break;
                        }

                        default :
                        {
                            if (_cnt51 >= 1)
                            {
                                break _loop51;
                            }
                            else
                            {
                                throw new NoViableAltException(LT(1), getFilename());
                            }
                        }
                    }

                    _cnt51++;
                }
                while (true);
            }

            switch (LA(1))
            {
                case JavadocTokenTypes.CLITEM :
                {
                    AST tmp91_AST = null;
                    tmp91_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp91_AST);
                    match(JavadocTokenTypes.CLITEM);
_loop54: 
                    do
                    {
                        if ((LA(1) == JavadocTokenTypes.PCDATA))
                        {
                            AST tmp92_AST = null;
                            tmp92_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp92_AST);
                            match(JavadocTokenTypes.PCDATA);
                        }
                        else
                        {
                            break _loop54;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.EOF :
                case JavadocTokenTypes.CULIST :
                case JavadocTokenTypes.COLIST :
                case JavadocTokenTypes.OLITEM :
                case JavadocTokenTypes.CDIR :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            list_item_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_13);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = list_item_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void ordered_list()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST ordered_list_AST = null;

        try
        { // for error handling

            AST tmp84_AST = null;
            tmp84_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp84_AST);
            match(JavadocTokenTypes.OOLIST);
_loop41: 
            do
            {
                if ((LA(1) == JavadocTokenTypes.PCDATA))
                {
                    AST tmp85_AST = null;
                    tmp85_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp85_AST);
                    match(JavadocTokenTypes.PCDATA);
                }
                else
                {
                    break _loop41;
                }
            }
            while (true);

            {
                int _cnt43 = 0;
_loop43: 
                do
                {
                    if ((LA(1) == JavadocTokenTypes.OLITEM))
                    {
                        list_item();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt43 >= 1)
                        {
                            break _loop43;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt43++;
                }
                while (true);
            }

            AST tmp86_AST = null;
            tmp86_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp86_AST);
            match(JavadocTokenTypes.COLIST);
            ordered_list_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = ordered_list_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void paragraph()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST paragraph_AST = null;

        try
        { // for error handling

            AST tmp24_AST = null;
            tmp24_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp24_AST);
            match(JavadocTokenTypes.OPARA);
_loop31: 
            do
            {
                if ((_tokenSet_6.member(LA(1))))
                {
                    text();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((_tokenSet_7.member(LA(1))))
                {
                    list();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == JavadocTokenTypes.ODIV))
                {
                    div();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == JavadocTokenTypes.OCENTER))
                {
                    center();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == JavadocTokenTypes.OBQUOTE))
                {
                    blockquote();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == JavadocTokenTypes.OTABLE))
                {
                    table();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else if ((LA(1) == JavadocTokenTypes.PRE))
                {
                    preformatted();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop31;
                }
            }
            while (true);

            switch (LA(1))
            {
                case JavadocTokenTypes.CPARA :
                {
                    match(JavadocTokenTypes.CPARA);

                    break;
                }

                case JavadocTokenTypes.EOF :
                case JavadocTokenTypes.LCURLY :
                case JavadocTokenTypes.RCURLY :
                case JavadocTokenTypes.OTH :
                case JavadocTokenTypes.CTH :
                case JavadocTokenTypes.OTD :
                case JavadocTokenTypes.CTD :
                case JavadocTokenTypes.JAVADOC_CLOSE :
                case JavadocTokenTypes.HR :
                case JavadocTokenTypes.IMG :
                case JavadocTokenTypes.BR :
                case JavadocTokenTypes.TAG :
                case JavadocTokenTypes.COMMENT :
                case JavadocTokenTypes.PCDATA :
                case JavadocTokenTypes.OH1 :
                case JavadocTokenTypes.CH1 :
                case JavadocTokenTypes.OH2 :
                case JavadocTokenTypes.CH2 :
                case JavadocTokenTypes.OH3 :
                case JavadocTokenTypes.CH3 :
                case JavadocTokenTypes.OH4 :
                case JavadocTokenTypes.CH4 :
                case JavadocTokenTypes.OH5 :
                case JavadocTokenTypes.CH5 :
                case JavadocTokenTypes.OH6 :
                case JavadocTokenTypes.CH6 :
                case JavadocTokenTypes.OADDRESS :
                case JavadocTokenTypes.OPARA :
                case JavadocTokenTypes.OULIST :
                case JavadocTokenTypes.CULIST :
                case JavadocTokenTypes.OOLIST :
                case JavadocTokenTypes.COLIST :
                case JavadocTokenTypes.ODLIST :
                case JavadocTokenTypes.CDLIST :
                case JavadocTokenTypes.OLITEM :
                case JavadocTokenTypes.CLITEM :
                case JavadocTokenTypes.ODTERM :
                case JavadocTokenTypes.ODDEF :
                case JavadocTokenTypes.CDDEF :
                case JavadocTokenTypes.CDIR :
                case JavadocTokenTypes.ODIV :
                case JavadocTokenTypes.CDIV :
                case JavadocTokenTypes.OCENTER :
                case JavadocTokenTypes.CCENTER :
                case JavadocTokenTypes.OBQUOTE :
                case JavadocTokenTypes.CBQUOTE :
                case JavadocTokenTypes.PRE :
                case JavadocTokenTypes.OTABLE :
                case JavadocTokenTypes.CTABLE :
                case JavadocTokenTypes.O_TR :
                case JavadocTokenTypes.C_TR :
                case JavadocTokenTypes.OTTYPE :
                case JavadocTokenTypes.OITALIC :
                case JavadocTokenTypes.OBOLD :
                case JavadocTokenTypes.OCODE :
                case JavadocTokenTypes.OUNDER :
                case JavadocTokenTypes.OSTRIKE :
                case JavadocTokenTypes.OBIG :
                case JavadocTokenTypes.OSMALL :
                case JavadocTokenTypes.OSUB :
                case JavadocTokenTypes.OSUP :
                case JavadocTokenTypes.OEM :
                case JavadocTokenTypes.OSTRONG :
                case JavadocTokenTypes.ODFN :
                case JavadocTokenTypes.OSAMP :
                case JavadocTokenTypes.OKBD :
                case JavadocTokenTypes.OVAR :
                case JavadocTokenTypes.OCITE :
                case JavadocTokenTypes.OACRO :
                case JavadocTokenTypes.OANCHOR :
                case JavadocTokenTypes.OFONT :
                case JavadocTokenTypes.AT :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            paragraph_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                handleRecoverableError(ex);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = paragraph_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void parse()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST parse_AST = null;

        try
        { // for error handling

            if (inputState.guessing == 0)
            {
                // Uncomment to check the tag definition files for update
                //loadTagInfo(false);
                _startLine = _lexer.getLine();
                _startColumn = _lexer.getColumn();
            }

            match(JavadocTokenTypes.JAVADOC_OPEN);
_loop3: 
            do
            {
                if ((_tokenSet_0.member(LA(1))))
                {
                    body_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop3;
                }
            }
            while (true);

_loop5: 
            do
            {
                if (((LA(1) == JavadocTokenTypes.TAG) || (LA(1) == JavadocTokenTypes.AT)))
                {
                    standard_tag();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop5;
                }
            }
            while (true);

            match(JavadocTokenTypes.JAVADOC_CLOSE);
            parse_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_1);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = parse_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void phrase()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST phrase_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OEM :
                {
                    emphasize();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSTRONG :
                {
                    strong();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.ODFN :
                {
                    definition();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OCODE :
                {
                    code();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OSAMP :
                {
                    sample();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OKBD :
                {
                    keyboard();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OVAR :
                {
                    variable();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OCITE :
                {
                    citation();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OACRO :
                {
                    acronym();
                    astFactory.addASTChild(currentAST, returnAST);
                    phrase_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = phrase_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void preformatted()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST preformatted_AST = null;

        try
        { // for error handling

            AST tmp26_AST = null;
            tmp26_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp26_AST);
            match(JavadocTokenTypes.PRE);
            preformatted_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = preformatted_AST;
    }


    /**
     * Reports the given error.
     *
     * @param ex encountered exception.
     */
    public void reportError(RecognitionException ex)
    {
        Object[] args =
        { getFilename(), new Integer(ex.line), new Integer(ex.column), ex.getMessage() };
        _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, ex);
    }


    /**
     * Reports the given error.
     *
     * @param message error message.
     */
    public void reportError(String message)
    {
        Object[] args =
        {
            getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()),
            message
        };
        _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, null);
    }


    /**
     * Reports the given warning.
     *
     * @param message warning message.
     */
    public void reportWarning(String message)
    {
        Object[] args =
        {
            getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()),
            message
        };
        _logger.l7dlog(Level.WARN, message, args, null);
    }


    /**
     * Resets the parser.
     */
    public void reset()
    {
        if (this.inputState != null)
        {
            this.inputState.reset();
        }

        setFilename(Recognizer.UNKNOWN_FILE);
        this.returnAST = null;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void sample()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST sample_AST = null;

        try
        { // for error handling

            AST tmp62_AST = null;
            tmp62_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp62_AST);
            match(JavadocTokenTypes.OSAMP);

            {
                int _cnt150 = 0;
_loop150: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt150 >= 1)
                        {
                            break _loop150;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt150++;
                }
                while (true);
            }

            AST tmp63_AST = null;
            tmp63_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp63_AST);
            match(JavadocTokenTypes.CSAMP);
            sample_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = sample_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void small()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST small_AST = null;

        try
        { // for error handling

            AST tmp48_AST = null;
            tmp48_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp48_AST);
            match(JavadocTokenTypes.OSMALL);

            {
                int _cnt132 = 0;
_loop132: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt132 >= 1)
                        {
                            break _loop132;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt132++;
                }
                while (true);
            }

            AST tmp49_AST = null;
            tmp49_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp49_AST);
            match(JavadocTokenTypes.CSMALL);
            small_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = small_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void special()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST special_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OANCHOR :
                {
                    anchor();
                    astFactory.addASTChild(currentAST, returnAST);
                    special_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.IMG :
                {
                    AST tmp72_AST = null;
                    tmp72_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp72_AST);
                    match(JavadocTokenTypes.IMG);
                    special_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OFONT :
                {
                    font_dfn();
                    astFactory.addASTChild(currentAST, returnAST);
                    special_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.BR :
                {
                    AST tmp73_AST = null;
                    tmp73_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp73_AST);
                    match(JavadocTokenTypes.BR);
                    special_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = special_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void standard_tag()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST standard_tag_AST = null;
        Token tag = null;
        AST tag_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.TAG :
                {
                    tag = LT(1);
                    tag_AST = astFactory.create(tag);
                    astFactory.makeASTRoot(currentAST, tag_AST);
                    match(JavadocTokenTypes.TAG);

                    if (inputState.guessing == 0)
                    {
                        setTagType(tag_AST, TYPE_STANDARD);
                    }

_loop173: 
                    do
                    {
                        switch (LA(1))
                        {
                            case JavadocTokenTypes.LCURLY :
                            case JavadocTokenTypes.RCURLY :
                            case JavadocTokenTypes.IMG :
                            case JavadocTokenTypes.BR :
                            case JavadocTokenTypes.COMMENT :
                            case JavadocTokenTypes.PCDATA :
                            case JavadocTokenTypes.OTTYPE :
                            case JavadocTokenTypes.OITALIC :
                            case JavadocTokenTypes.OBOLD :
                            case JavadocTokenTypes.OCODE :
                            case JavadocTokenTypes.OUNDER :
                            case JavadocTokenTypes.OSTRIKE :
                            case JavadocTokenTypes.OBIG :
                            case JavadocTokenTypes.OSMALL :
                            case JavadocTokenTypes.OSUB :
                            case JavadocTokenTypes.OSUP :
                            case JavadocTokenTypes.OEM :
                            case JavadocTokenTypes.OSTRONG :
                            case JavadocTokenTypes.ODFN :
                            case JavadocTokenTypes.OSAMP :
                            case JavadocTokenTypes.OKBD :
                            case JavadocTokenTypes.OVAR :
                            case JavadocTokenTypes.OCITE :
                            case JavadocTokenTypes.OACRO :
                            case JavadocTokenTypes.OANCHOR :
                            case JavadocTokenTypes.OFONT :
                            {
                                text();
                                astFactory.addASTChild(currentAST, returnAST);

                                break;
                            }

                            case JavadocTokenTypes.HR :
                            case JavadocTokenTypes.OPARA :
                            case JavadocTokenTypes.OULIST :
                            case JavadocTokenTypes.OOLIST :
                            case JavadocTokenTypes.ODLIST :
                            case JavadocTokenTypes.ODIV :
                            case JavadocTokenTypes.OCENTER :
                            case JavadocTokenTypes.OBQUOTE :
                            case JavadocTokenTypes.PRE :
                            case JavadocTokenTypes.OTABLE :
                            {
                                block();
                                astFactory.addASTChild(currentAST, returnAST);

                                break;
                            }

                            default :
                                break _loop173;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.AT :
                {
                    AST tmp3_AST = null;
                    tmp3_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp3_AST);
                    match(JavadocTokenTypes.AT);

                    if (inputState.guessing == 0)
                    {
                        standard_tag_AST = currentAST.root;
                        standard_tag_AST.setType(JavadocTokenTypes.AT);
                    }

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            standard_tag_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_3);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = standard_tag_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void strike()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST strike_AST = null;

        try
        { // for error handling

            AST tmp44_AST = null;
            tmp44_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp44_AST);
            match(JavadocTokenTypes.OSTRIKE);

            {
                int _cnt126 = 0;
_loop126: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt126 >= 1)
                        {
                            break _loop126;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt126++;
                }
                while (true);
            }

            AST tmp45_AST = null;
            tmp45_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp45_AST);
            match(JavadocTokenTypes.CSTRIKE);
            strike_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = strike_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void strong()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST strong_AST = null;

        try
        { // for error handling

            AST tmp56_AST = null;
            tmp56_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp56_AST);
            match(JavadocTokenTypes.OSTRONG);

            {
                int _cnt144 = 0;
_loop144: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt144 >= 1)
                        {
                            break _loop144;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt144++;
                }
                while (true);
            }

            AST tmp57_AST = null;
            tmp57_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp57_AST);
            match(JavadocTokenTypes.CSTRONG);
            strong_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = strong_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void subscript()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST subscript_AST = null;

        try
        { // for error handling

            AST tmp50_AST = null;
            tmp50_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp50_AST);
            match(JavadocTokenTypes.OSUB);

            {
                int _cnt135 = 0;
_loop135: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt135 >= 1)
                        {
                            break _loop135;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt135++;
                }
                while (true);
            }

            AST tmp51_AST = null;
            tmp51_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp51_AST);
            match(JavadocTokenTypes.CSUB);
            subscript_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = subscript_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void superscript()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST superscript_AST = null;

        try
        { // for error handling

            AST tmp52_AST = null;
            tmp52_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp52_AST);
            match(JavadocTokenTypes.OSUP);

            {
                int _cnt138 = 0;
_loop138: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt138 >= 1)
                        {
                            break _loop138;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt138++;
                }
                while (true);
            }

            AST tmp53_AST = null;
            tmp53_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp53_AST);
            match(JavadocTokenTypes.CSUP);
            superscript_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = superscript_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void table()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST table_AST = null;

        try
        { // for error handling

            AST tmp33_AST = null;
            tmp33_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp33_AST);
            match(JavadocTokenTypes.OTABLE);

            switch (LA(1))
            {
                case JavadocTokenTypes.OCAP :
                {
                    caption();
                    astFactory.addASTChild(currentAST, returnAST);

                    break;
                }

                case JavadocTokenTypes.PCDATA :
                case JavadocTokenTypes.O_TR :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

_loop85: 
            do
            {
                if ((LA(1) == JavadocTokenTypes.PCDATA))
                {
                    AST tmp34_AST = null;
                    tmp34_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp34_AST);
                    match(JavadocTokenTypes.PCDATA);
                }
                else
                {
                    break _loop85;
                }
            }
            while (true);

            {
                int _cnt87 = 0;
_loop87: 
                do
                {
                    if ((LA(1) == JavadocTokenTypes.O_TR))
                    {
                        tr();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt87 >= 1)
                        {
                            break _loop87;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt87++;
                }
                while (true);
            }

            match(JavadocTokenTypes.CTABLE);
            table_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = table_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void teletype()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST teletype_AST = null;

        try
        { // for error handling

            AST tmp36_AST = null;
            tmp36_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp36_AST);
            match(JavadocTokenTypes.OTTYPE);

            {
                int _cnt111 = 0;
_loop111: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt111 >= 1)
                        {
                            break _loop111;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt111++;
                }
                while (true);
            }

            AST tmp37_AST = null;
            tmp37_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp37_AST);
            match(JavadocTokenTypes.CTTYPE);
            teletype_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = teletype_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void text()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST text_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.RCURLY :
                {
                    AST tmp4_AST = null;
                    tmp4_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp4_AST);
                    match(JavadocTokenTypes.RCURLY);
                    text_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.IMG :
                case JavadocTokenTypes.BR :
                case JavadocTokenTypes.OTTYPE :
                case JavadocTokenTypes.OITALIC :
                case JavadocTokenTypes.OBOLD :
                case JavadocTokenTypes.OCODE :
                case JavadocTokenTypes.OUNDER :
                case JavadocTokenTypes.OSTRIKE :
                case JavadocTokenTypes.OBIG :
                case JavadocTokenTypes.OSMALL :
                case JavadocTokenTypes.OSUB :
                case JavadocTokenTypes.OSUP :
                case JavadocTokenTypes.OEM :
                case JavadocTokenTypes.OSTRONG :
                case JavadocTokenTypes.ODFN :
                case JavadocTokenTypes.OSAMP :
                case JavadocTokenTypes.OKBD :
                case JavadocTokenTypes.OVAR :
                case JavadocTokenTypes.OCITE :
                case JavadocTokenTypes.OACRO :
                case JavadocTokenTypes.OANCHOR :
                case JavadocTokenTypes.OFONT :
                {
                    text_tag();
                    astFactory.addASTChild(currentAST, returnAST);
                    text_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.COMMENT :
                {
                    AST tmp5_AST = null;
                    tmp5_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp5_AST);
                    match(JavadocTokenTypes.COMMENT);
                    text_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.PCDATA :
                {
                    AST tmp6_AST = null;
                    tmp6_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp6_AST);
                    match(JavadocTokenTypes.PCDATA);
                    text_AST = currentAST.root;

                    break;
                }

                default :

                    boolean synPredMatched16 = false;

                    if (((LA(1) == JavadocTokenTypes.LCURLY)))
                    {
                        int _m16 = mark();
                        synPredMatched16 = true;
                        inputState.guessing++;

                        try
                        {
                            match(JavadocTokenTypes.LCURLY);
                            match(JavadocTokenTypes.TAG);
                        }
                        catch (RecognitionException pe)
                        {
                            synPredMatched16 = false;
                        }

                        rewind(_m16);
                        inputState.guessing--;
                    }

                    if (synPredMatched16)
                    {
                        inline_tag();
                        astFactory.addASTChild(currentAST, returnAST);
                        text_AST = currentAST.root;
                    }
                    else if ((LA(1) == JavadocTokenTypes.LCURLY))
                    {
                        AST tmp7_AST = null;
                        tmp7_AST = astFactory.create(LT(1));
                        astFactory.addASTChild(currentAST, tmp7_AST);
                        match(JavadocTokenTypes.LCURLY);
                        text_AST = currentAST.root;
                    }
                    else
                    {
                        throw new NoViableAltException(LT(1), getFilename());
                    }
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = text_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void text_tag()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST text_tag_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OTTYPE :
                case JavadocTokenTypes.OITALIC :
                case JavadocTokenTypes.OBOLD :
                case JavadocTokenTypes.OUNDER :
                case JavadocTokenTypes.OSTRIKE :
                case JavadocTokenTypes.OBIG :
                case JavadocTokenTypes.OSMALL :
                case JavadocTokenTypes.OSUB :
                case JavadocTokenTypes.OSUP :
                {
                    font();
                    astFactory.addASTChild(currentAST, returnAST);
                    text_tag_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.OCODE :
                case JavadocTokenTypes.OEM :
                case JavadocTokenTypes.OSTRONG :
                case JavadocTokenTypes.ODFN :
                case JavadocTokenTypes.OSAMP :
                case JavadocTokenTypes.OKBD :
                case JavadocTokenTypes.OVAR :
                case JavadocTokenTypes.OCITE :
                case JavadocTokenTypes.OACRO :
                {
                    phrase();
                    astFactory.addASTChild(currentAST, returnAST);
                    text_tag_AST = currentAST.root;

                    break;
                }

                case JavadocTokenTypes.IMG :
                case JavadocTokenTypes.BR :
                case JavadocTokenTypes.OANCHOR :
                case JavadocTokenTypes.OFONT :
                {
                    special();
                    astFactory.addASTChild(currentAST, returnAST);
                    text_tag_AST = currentAST.root;

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_4);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = text_tag_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void th_or_td()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST th_or_td_AST = null;

        try
        { // for error handling

            switch (LA(1))
            {
                case JavadocTokenTypes.OTH :
                {
                    AST tmp109_AST = null;
                    tmp109_AST = astFactory.create(LT(1));
                    astFactory.makeASTRoot(currentAST, tmp109_AST);
                    match(JavadocTokenTypes.OTH);

                    break;
                }

                case JavadocTokenTypes.OTD :
                {
                    AST tmp110_AST = null;
                    tmp110_AST = astFactory.create(LT(1));
                    astFactory.makeASTRoot(currentAST, tmp110_AST);
                    match(JavadocTokenTypes.OTD);

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

_loop104: 
            do
            {
                if ((_tokenSet_0.member(LA(1))))
                {
                    body_content();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop104;
                }
            }
            while (true);

            switch (LA(1))
            {
                case JavadocTokenTypes.CTH :
                case JavadocTokenTypes.CTD :
                {
                    switch (LA(1))
                    {
                        case JavadocTokenTypes.CTH :
                        {
                            match(JavadocTokenTypes.CTH);

                            break;
                        }

                        case JavadocTokenTypes.CTD :
                        {
                            match(JavadocTokenTypes.CTD);

                            break;
                        }

                        default :
                            throw new NoViableAltException(LT(1), getFilename());
                    }

_loop108: 
                    do
                    {
                        if ((LA(1) == JavadocTokenTypes.PCDATA))
                        {
                            AST tmp113_AST = null;
                            tmp113_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp113_AST);
                            match(JavadocTokenTypes.PCDATA);
                        }
                        else
                        {
                            break _loop108;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.OTH :
                case JavadocTokenTypes.OTD :
                case JavadocTokenTypes.CTABLE :
                case JavadocTokenTypes.O_TR :
                case JavadocTokenTypes.C_TR :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            th_or_td_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_17);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = th_or_td_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void tr()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST tr_AST = null;

        try
        { // for error handling

            AST tmp103_AST = null;
            tmp103_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp103_AST);
            match(JavadocTokenTypes.O_TR);

            switch (LA(1))
            {
                case JavadocTokenTypes.OTH :
                case JavadocTokenTypes.OTD :
                case JavadocTokenTypes.PCDATA :
                case JavadocTokenTypes.CTABLE :
                case JavadocTokenTypes.O_TR :
                case JavadocTokenTypes.C_TR :
                {
_loop94: 
                    do
                    {
                        if ((LA(1) == JavadocTokenTypes.PCDATA))
                        {
                            AST tmp104_AST = null;
                            tmp104_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp104_AST);
                            match(JavadocTokenTypes.PCDATA);
                        }
                        else
                        {
                            break _loop94;
                        }
                    }
                    while (true);

                    break;
                }

                case JavadocTokenTypes.COMMENT :
                {
                    AST tmp105_AST = null;
                    tmp105_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp105_AST);
                    match(JavadocTokenTypes.COMMENT);

                    break;
                }

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

_loop96: 
            do
            {
                if (
                    ((LA(1) == JavadocTokenTypes.OTH) || (LA(1) == JavadocTokenTypes.OTD)))
                {
                    th_or_td();
                    astFactory.addASTChild(currentAST, returnAST);
                }
                else
                {
                    break _loop96;
                }
            }
            while (true);

            switch (LA(1))
            {
                case JavadocTokenTypes.C_TR :
                {
                    match(JavadocTokenTypes.C_TR);

                    switch (LA(1))
                    {
                        case JavadocTokenTypes.PCDATA :
                        case JavadocTokenTypes.CTABLE :
                        case JavadocTokenTypes.O_TR :
                        {
_loop100: 
                            do
                            {
                                if ((LA(1) == JavadocTokenTypes.PCDATA))
                                {
                                    AST tmp107_AST = null;
                                    tmp107_AST = astFactory.create(LT(1));
                                    astFactory.addASTChild(currentAST, tmp107_AST);
                                    match(JavadocTokenTypes.PCDATA);
                                }
                                else
                                {
                                    break _loop100;
                                }
                            }
                            while (true);

                            break;
                        }

                        case JavadocTokenTypes.COMMENT :
                        {
                            AST tmp108_AST = null;
                            tmp108_AST = astFactory.create(LT(1));
                            astFactory.addASTChild(currentAST, tmp108_AST);
                            match(JavadocTokenTypes.COMMENT);

                            break;
                        }

                        default :
                            throw new NoViableAltException(LT(1), getFilename());
                    }

                    break;
                }

                case JavadocTokenTypes.CTABLE :
                case JavadocTokenTypes.O_TR :
                    break;

                default :
                    throw new NoViableAltException(LT(1), getFilename());
            }

            tr_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_16);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = tr_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void underline()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST underline_AST = null;

        try
        { // for error handling

            AST tmp42_AST = null;
            tmp42_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp42_AST);
            match(JavadocTokenTypes.OUNDER);

            {
                int _cnt123 = 0;
_loop123: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt123 >= 1)
                        {
                            break _loop123;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt123++;
                }
                while (true);
            }

            AST tmp43_AST = null;
            tmp43_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp43_AST);
            match(JavadocTokenTypes.CUNDER);
            underline_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = underline_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void unordered_list()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST unordered_list_AST = null;

        try
        { // for error handling

            AST tmp81_AST = null;
            tmp81_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp81_AST);
            match(JavadocTokenTypes.OULIST);
_loop36: 
            do
            {
                if ((LA(1) == JavadocTokenTypes.PCDATA))
                {
                    AST tmp82_AST = null;
                    tmp82_AST = astFactory.create(LT(1));
                    astFactory.addASTChild(currentAST, tmp82_AST);
                    match(JavadocTokenTypes.PCDATA);
                }
                else
                {
                    break _loop36;
                }
            }
            while (true);

            {
                int _cnt38 = 0;
_loop38: 
                do
                {
                    if ((LA(1) == JavadocTokenTypes.OLITEM))
                    {
                        list_item();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt38 >= 1)
                        {
                            break _loop38;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt38++;
                }
                while (true);
            }

            AST tmp83_AST = null;
            tmp83_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp83_AST);
            match(JavadocTokenTypes.CULIST);
            unordered_list_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_8);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = unordered_list_AST;
    }


    /**
     * DOCUMENT ME!
     *
     * @throws RecognitionException DOCUMENT ME!
     * @throws TokenStreamException DOCUMENT ME!
     */
    public final void variable()
      throws RecognitionException, TokenStreamException
    {
        returnAST = null;

        ASTPair currentAST = new ASTPair();
        AST variable_AST = null;

        try
        { // for error handling

            AST tmp66_AST = null;
            tmp66_AST = astFactory.create(LT(1));
            astFactory.makeASTRoot(currentAST, tmp66_AST);
            match(JavadocTokenTypes.OVAR);

            {
                int _cnt156 = 0;
_loop156: 
                do
                {
                    if ((_tokenSet_6.member(LA(1))))
                    {
                        text();
                        astFactory.addASTChild(currentAST, returnAST);
                    }
                    else
                    {
                        if (_cnt156 >= 1)
                        {
                            break _loop156;
                        }
                        else
                        {
                            throw new NoViableAltException(LT(1), getFilename());
                        }
                    }

                    _cnt156++;
                }
                while (true);
            }

            AST tmp67_AST = null;
            tmp67_AST = astFactory.create(LT(1));
            astFactory.addASTChild(currentAST, tmp67_AST);
            match(JavadocTokenTypes.CVAR);
            variable_AST = currentAST.root;
        }
        catch (RecognitionException ex)
        {
            if (inputState.guessing == 0)
            {
                reportError(ex);
                consume();
                consumeUntil(_tokenSet_9);
            }
            else
            {
                throw ex;
            }
        }

        returnAST = variable_AST;
    }


    /**
     * Returns the matching tag for the given text, if any.
     *
     * @param text text to match against.
     * @param type the type of the tag. Either {@link #TYPE_STANDARD} or {@link
     *        #TYPE_INLINE}
     *
     * @return matching Javadoc tag. Returns <code>null</code> to indicate that no valid
     *         tag could be found for the given text. If the given text represents a
     *         valid tag the given text reference will be returned.
     */
    String getTag(
        String text,
        String type)
    {
        Set tags = null;

        if (type == TYPE_STANDARD)
        {
            tags = _standardTags;
        }
        else
        {
            tags = _inlineTags;
        }

        if (tags.contains(text))
        {
            return text;
        }

        Lcs lcs = new Lcs();

        for (Iterator i = tags.iterator(); i.hasNext();)
        {
            String tag = (String) i.next();

            lcs.init(text, tag);

            double similarity = lcs.getPercentage();

            // XXX evaluate whether this is appropriate
            if (similarity > 70.0)
            {
                // mispelled tag found
                return tag;
            }
        }

        // no match found means invalid tag
        return null;
    }


    /**
     * Tries to determine and set the correct type for the given tag node.
     *
     * @param tag tag AST to set the correct type for.
     * @param type the type of the tag. Either {@link #TYPE_STANDARD} or {@link
     *        #TYPE_INLINE}
     */
    private void setTagType(
        AST    tag,
        String type)
    {
        String text = tag.getText();
        String name = getTag(text, type);

        if (name == null) // invalid tag
        {
            Object[] args =
            {
                getFilename(), new Integer(_lexer.getLine()),
                new Integer(_lexer.getColumn()), text
            };
            _logger.l7dlog(Level.ERROR, "TAG_INVALID", args, null);
        }
        else
        {
            if (name != text) // mispelled tag
            {
                // correct the tag name
                tag.setText(name);

                Object[] args =
                {
                    getFilename(), new Integer(_lexer.getLine()),
                    new Integer(_lexer.getColumn()), text, name
                };
                _logger.l7dlog(Level.WARN, "TAG_MISSPELLED_NAME", args, null);
            }

            String t = (String) _tokenTypes.get(type + name.substring(1).toUpperCase());

            // not found in the mapping table means custom tag
            if (t == null)
            {
                if (type == TYPE_STANDARD)
                {
                    tag.setType(JavadocTokenTypes.TAG_CUSTOM);
                }
                else
                {
                    tag.setType(JavadocTokenTypes.TAG_INLINE_CUSTOM);
                }
            }
            else
            {
                try
                {
                    int result = Integer.parseInt(t);
                    tag.setType(result);
                }
                catch (NumberFormatException neverOccurs)
                {
                    ;
                }
            }
        }
    }


    /**
     * Loads the custom tag info from the given tag definition. The found tags will be
     * added to the given set.
     *
     * @param file the tag definition file.
     * @param tags set to add the tags to.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private void loadCustomTagInfo(
        File file,
        Set  tags)
    {
        try
        {
            Properties props = loadProperties(file);
            tags.addAll(props.keySet());
        }
        catch (Exception ex)
        {
            throw new RuntimeException("failed loading tag definition file -- " + file);
        }
    }


    /**
     * Loads the Javadoc inline tag info.
     *
     * @param force if <code>true</code> forces a loading of the tag info.
     */
    private void loadInlineTagInfo(boolean force)
    {
        //if (force || INLINE_TAGS.lastModified() > _inlineStamp)
        if (force)
        {
            _inlineTags.clear();
            _inlineTags.add("@docRoot");
            _inlineTags.add("@inheritDoc");
            _inlineTags.add("@link");
            _inlineTags.add("@linkPlain");
            _inlineTags.add("@value");
            //loadCustomTagInfo(INLINE_TAGS, _inlineTags);
            //_inlineStamp = INLINE_TAGS.lastModified();
        }
    }


    /**
     * Loads the properties from the given file.
     *
     * @param file property file.
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private Properties loadProperties(File file)
      throws IOException
    {
        Properties props = new Properties();

        if (file.exists())
        {
            InputStream in = null;

            try
            {
                in = new BufferedInputStream(new FileInputStream(file));
                props.load(in);
            }
            finally
            {
                in.close();
            }
        }

        return props;
    }


    /**
     * Loads the Javadoc standard tag info.
     *
     * @param force if <code>true</code> forces a loading of the tag info.
     */
    private void loadStandardTagInfo(boolean force)
    {
        //if (force || STANDARD_TAGS.lastModified() > _standardStamp)
        if (force)
        {
            _standardTags.clear();
            _standardTags.add("@author");
            _standardTags.add("@deprecated");
            _standardTags.add("@exception");
            _standardTags.add("@param");
            _standardTags.add("@return");
            _standardTags.add("@see");
            _standardTags.add("@serial");
            _standardTags.add("@serialData");
            _standardTags.add("@serialField");
            _standardTags.add("@since");
            _standardTags.add("@throws");
            _standardTags.add("@todo");
            _standardTags.add("@version");
            //loadCustomTagInfo(STANDARD_TAGS, _standardTags);
            //_standardStamp = STANDARD_TAGS.lastModified();
        }
    }


    /**
     * Loads the Javadoc tag info.
     *
     * @param force if <code>true</code> forces a loading of the tag info.
     */
    private void loadTagInfo(boolean force)
    {
        if (force)
        {
            _standardTags = new HashSet(15);
            _inlineTags = new HashSet(8);
        }

        loadStandardTagInfo(force);
        loadInlineTagInfo(force);
    }


    /**
     * Loads the token type mapping table.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private void loadTokenTypeInfo()
    {
        try
        {
            Properties props = new Properties();
            props.load(JavadocLexer.class.getResourceAsStream("JavadocTokenTypes.txt"));
            _tokenTypes = new HashMap((int) (props.size() * 1.3));
            _tokenTypes.putAll(props);
        }
        catch (Exception ex)
        {
            throw new RuntimeException(
                "failed loading token types file -- JavadocTokenTypes.txt");
        }
    }


    private static final long[] mk_tokenSet_0()
    {
        long[] data = { 3074458502396117376L, 384307168201960064L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_1()
    {
        long[] data = { 2L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_10()
    {
        long[] data = { 1099511627776L, 24019198012293120L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_11()
    {
        long[] data = { -272L, -1L, 131071L, 0L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_12()
    {
        long[] data = { 6003333487656960L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_13()
    {
        long[] data = { -7782220156096217086L, 64L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_14()
    {
        long[] data = { 4611686018427387904L, 10L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_15()
    {
        long[] data = { 1099511627776L, 262144L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_16()
    {
        long[] data = { 0L, 294912L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_17()
    {
        long[] data = { 2560L, 819200L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_18()
    {
        long[] data = { 1099511627776L, 168134386088148992L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_2()
    {
        long[] data = { 3074458794453901184L, 1537228672809631616L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_3()
    {
        long[] data = { 292057776128L, 1152921504606846976L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_4()
    {
        long[] data = { -18014415689343102L, 2161727821137772511L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_5()
    {
        long[] data = { -90072009727271038L, 1537228672809631707L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_6()
    {
        long[] data = { 1855425872256L, 384307168201932800L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_7()
    {
        long[] data = { 3026418949592973312L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_8()
    {
        long[] data = { -18014415689343102L, 1537228672809631707L, 0L, 0L };

        return data;
    }


    private static final long[] mk_tokenSet_9()
    {
        long[] data = { -18014415689343102L, 2305843009213628383L, 0L, 0L };

        return data;
    }
}
