// $ANTLR 2.7.2a2 (20020112-1): "src/java/de/hunsicker/jalopy/language/java.g" -> "JavaLexer.java"$

package de.hunsicker.jalopy.language;

import java.io.InputStream;
import de.hunsicker.antlr.TokenStreamException;
import de.hunsicker.antlr.TokenStreamIOException;
import de.hunsicker.antlr.TokenStreamRecognitionException;
import de.hunsicker.antlr.CharStreamException;
import de.hunsicker.antlr.CharStreamIOException;
import de.hunsicker.antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import de.hunsicker.antlr.CharScanner;
import de.hunsicker.antlr.InputBuffer;
import de.hunsicker.antlr.ByteBuffer;
import de.hunsicker.antlr.CharBuffer;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.CommonToken;
import de.hunsicker.antlr.RecognitionException;
import de.hunsicker.antlr.NoViableAltForCharException;
import de.hunsicker.antlr.MismatchedCharException;
import de.hunsicker.antlr.TokenStream;
import de.hunsicker.antlr.ANTLRHashString;
import de.hunsicker.antlr.LexerSharedInputState;
import de.hunsicker.antlr.collections.impl.BitSet;
import de.hunsicker.antlr.SemanticException;

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.util.StringHelper;
import de.hunsicker.io.FileFormat;
import de.hunsicker.util.Version;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Token lexer for the Java parser. Heavily based on the public domain
 * grammar file written by <a href="mailto:parrt@jguru.com">Terence Parr
 * </a> et al. See <a href="http://www.antlr.org/resources.html">
 * http://www.antlr.org/resources.html</a> for more infos.
 *
 * <p>Note that this parser relies on a patched version of ANTLR 2.7.2. It
 * currently won't work with any other version.</p>
 *
 * <p>This is an <a href="http://www.antlr.org">ANTLR</a> automated generated
 * file. <strong>DO NOT EDIT</strong> but rather change the associated grammar
 * (<code>java.g</code>) and rebuild.</p>
 *
 * @version $revision$
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @see de.hunsicker.jalopy.language.JavaParser
 */
public final class JavaLexer extends de.hunsicker.antlr.CharScanner implements JavaTokenTypes, TokenStream
, Lexer {

    /** Indicates JDK version 1.3. */
    public final static int JDK_1_3 = 13;

    /** Indicates JDK version 1.4. */
    public final static int JDK_1_4 = 14;

    private final static String SPACE = " ";

    /** The empty string array. */
    private final static String[] EMPTY_STRING_ARRAY = new String[0];

    private final static String EMPTY_STRING = "";

    /** The detected file format. */
    private FileFormat _fileFormat = FileFormat.UNKNOWN;

    /** The file separator for the file format. */
    private String _lineSeparator = System.getProperty("line.separator");

    /** The Javadoc recognizer. */
    private Recognizer _recognizer;

    /** Logging. */
    private Logger _logger = Logger.getLogger("de.hunsicker.jalopy.language.java");

    /** Should Javadoc comments be parsed or added AS IS? */
    boolean parseJavadocComments;

    /** Specifies the Java release version to be compatible with. */
    int sourceVersion = JDK_1_4;

    /** Should Javadoc comments be ignored? */
    boolean removeJavadocComments;

    /** Should multi-line comments be formatted? */
    boolean formatMLComments;

    /** Should single-line comments be ignored? */
    boolean removeSLComments;

    /** Should multi-line comments be ignored? */
    boolean removeMLComments;

    /** The use Java parser. */
    private JavaParser _parser;

    /** The used Javadoc parser. */
    private JavadocParser _javadocParser;

    /**
     * Creates a new JavaLexer object. Use {@link #setInputBuffer(Reader)} to
     * set up the input buffer.
     */
    public JavaLexer()
    {
        this(new StringReader(""));

        JavadocLexer lexer = new JavadocLexer();
        _javadocParser = (JavadocParser)lexer.getParser();
        _recognizer = new Recognizer(_javadocParser, lexer);

        _parser = new JavaParser(this);
        _parser.setASTFactory(new JavaNodeFactory());
    }

    /**
     * Returns the internal parser for Javadoc comments.
     *
     * @return the internal parser for Javadoc comments.
     */
    public JavadocParser getJavadocParser()
    {
        return _javadocParser;
    }

    /**
     * {@inheritDoc}
     */
    public Parser getParser()
    {
        return _parser;
    }

    /**
     * Sets whether multi-line comments should be removed during processing.
     *
     * @param remove if <code>true</code> multi-line comments will be removed during
     *        processing.
     */
    public void setRemoveMLComments(boolean remove)
    {
        this.removeMLComments = remove;
    }

    /**
     * Sets whether multi-line comments should be formatted.
     *
     * @param format if <code>true</code> multi-line comments will be formatted.
     */
    public void setFormatMLComments(boolean format)
    {
        this.formatMLComments = format;
    }

    /**
     * Indicates whether multi-line comments should be formatted.
     *
     * @return <code>true</code> if multi-line comments should be formatted.
     */
    public boolean isFormatMLComments()
    {
        return this.formatMLComments;
    }

    /**
     * Indicates whether multi-line comments should be removed during processing.
     *
     * @return <code>true</code> if multi-line comments should be removed during
     *         processing.
     */
    public boolean isRemoveMLComments()
    {
        return this.removeMLComments;
    }

    /**
     * Sets whether single-line comments should be removed during processing.
     *
     * @param remove if <code>true</code> single-line comments will be removed during
     *        processing.
     */
    public void setRemoveSLComments(boolean remove)
    {
        this.removeSLComments = remove;
    }

    /**
     * Indicates whether single-line comments should be removed during processing.
     *
     * @return <code>true</code> if single-line comments should be removed during
     *         processing.
     */
    public boolean isRemoveSLComments()
    {
        return this.removeSLComments;
    }

    /**
     * Sets whether Javadoc comments should be removed during processing.
     *
     * @param remove if <code>true</code> Javadoc comments will be removed during
     *        processing.
     */
    public void setRemoveJavadocComments(boolean remove)
    {
        this.removeJavadocComments = remove;
    }

    /**
     * Indicates whether Javadoc comments should be removed during processing.
     *
     * @return <code>true</code> if Javadoc comments should be removed during
     *         processing.
     */
    public boolean isRemoveJavadocComments()
    {
        return this.removeJavadocComments;
    }

    /**
     * Sets whether Javadoc comments should be parsed during processing.
     *
     * @param parse if <code>true</code> Javadoc comments will be parsed during
     *        processing.
     */
    public void setParseJavadocComments(boolean parse)
    {
        this.parseJavadocComments = parse;
    }

    /**
     * Indicates whether Javadoc comments will be parsed during processing.
     *
     * @return <code>true</code> if Javadoc comments will be parsed during
     *         processing.
     */
    public boolean isParseJavadocComments()
    {
        return this.parseJavadocComments;
    }

    /**
     * Sets the source compatiblity to the given release version.
     *
     * @param version Java JDK version constant.
     */
    public void setCompatibility(int version)
    {
        this.sourceVersion = version;
    }

    /**
     * Gets the current source compatiblity version.
     *
     * @return compatiblity version.
     */
    public int getCompatibility()
    {
        return this.sourceVersion;
    }

    /**
     * Test the token type against the literals table.
     *
     * @param ttype recognized token type.
     *
     * @return token type.
     */
    public int testLiteralsTable(int ttype)
    {
        this.hashString.setBuffer(text.getBuffer(), text.length());
        Integer literalsIndex = (Integer)literals.get(hashString);

        if (literalsIndex != null)
        {
            ttype = literalsIndex.intValue();

            switch (ttype)
            {
                case JavaTokenTypes.LITERAL_assert:
                    switch (this.sourceVersion)
                    {
                        case JDK_1_3:
                            ttype = JavaTokenTypes.IDENT;
                            break;
                    }
                    break;
            }
        }

        return ttype;
    }

    /**
     * Test the text passed in against the literals table.
     *
     * @param text recognized token text.
     * @param ttype recognized token text type.
     *
     * @return token type.
     */
    public int testLiteralsTable(String text, int ttype)
    {
        ANTLRHashString s = new ANTLRHashString(text, this);
        Integer literalsIndex = (Integer)literals.get(s);

        if (literalsIndex != null)
        {
            ttype = literalsIndex.intValue();

            switch (ttype)
            {
                case JavaTokenTypes.LITERAL_assert:
                    switch (this.sourceVersion)
                    {
                        case JDK_1_3:
                            ttype = JavaTokenTypes.IDENT;
                            break;
                    }
                    break;
            }
        }

        return ttype;
    }

    public void panic()
    {
        if (this.inputState != null)
        {
            Object[] args = { getFilename(), new Integer(getLine()), new Integer(getColumn()), "JavaLexer: panic" };
            _logger.l7dlog(Level.FATAL, "PARSER_ERROR", args, null);
        }
        else
        {
            if (_logger == null)
                _logger = Logger.getLogger("de.hunsicker.jalopy.language.java");

            Object[] args = { "???", new Integer(0), new Integer(0), "JavaLexer: panic" };
            _logger.l7dlog(Level.FATAL, "PARSER_ERROR", args, null);
        }
    }

    public void panic(String message)
    {
        if (this.inputState != null)
        {
            Object[] args = { getFilename(), new Integer(getLine()), new Integer(getColumn()), message };
            _logger.l7dlog(Level.FATAL, "PARSER_ERROR", args, null);
        }
        else
        {
            if (_logger == null)
                _logger = Logger.getLogger("de.hunsicker.jalopy.language.java");

            Object[] args = { "???", new Integer(0), new Integer(0), message };
            _logger.l7dlog(Level.FATAL, "PARSER_ERROR", args, null);
        }
   }

   /**
    * Reports the given error.
    *
    * @param ex exception which caused the error.
    */
   public void reportError(RecognitionException ex)
   {
      Object[] args = { getFilename(), new Integer(getLine()),new Integer(getColumn()), ex.getMessage() };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, ex);
   }

   /**
    * Reports the given error.
    *
    * @param message error message.
    */
   public void reportError(String message)
   {
       Object[] args = { getFilename(), new Integer(getLine()), new Integer(getColumn()), message };
       _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, null);
   }

   /**
    * Reports the given warning.
    *
    * @param message warning message.
    */
   public void reportWarning(String message)
   {
       Object[] args = { getFilename(), new Integer(getLine()),new Integer(getColumn()), message };
       _logger.l7dlog(Level.WARN, "PARSER_ERROR", args, null);
   }

    /**
     * Creates a token of the given tpye.
     *
     * @param t type of the token.
     */
    protected Token makeToken(int t)
    {
        return new ExtendedToken(t, this.inputState.tokenStartLine,
                                 this.inputState.tokenStartColumn,
                                 this.inputState.line,
                                 this.inputState.column);
    }

    /**
     * Sets the class to use for tokens.
     *
     * @param clazz a qualified class name.
     *
     * @throws IllegalArgumentException if the class is not derived from
     * {@link de.hunsicker.jalopy.language.ExtendedToken}.
     */
    public void setTokenObjectClass(String clazz)
    {

        // necessary because our ctor calls this method with the default ANTLR
        // token object class during instantiation. If the ANTLR guys ever
        // change the class name, instantiating our lexer will fail until we've
        // changed our method too
        if (clazz.equals("de.hunsicker.antlr.CommonToken"))
        {
            clazz = "de.hunsicker.jalopy.language.ExtendedToken";
        }

        super.setTokenObjectClass(clazz);

        Object instance = null;

        try
        {
            instance = this.tokenObjectClass.newInstance();
        }
        catch (Exception ex)
        {
            panic("" + ex);
            return;
        }

        if (!(instance instanceof de.hunsicker.jalopy.language.ExtendedToken))
        {
            throw new IllegalArgumentException("your TokenObject class must extend de.hunsicker.jalopy.language.ExtendedToken");
        }
    }

    /**
     * Returns the detected file format.
     *
     * @return file format.
     */
    public FileFormat getFileFormat()
    {
        return _fileFormat;
    }

    /**
     * Sets the input buffer to use.
     * @param buf buffer.
     */
    public void setInputBuffer(InputBuffer buf)
    {
        if (this.inputState != null)
            this.inputState.setInputBuffer(buf);
    }

    /**
     * Sets the input buffer to use.
     *
     * @param in reader to read from.
     */
    public void setInputBuffer(Reader in)
    {
        setInputBuffer(new CharBuffer(in));
    }

    /**
     * Resets the lexer.
     *
     * <p>You have to re-initialize the input buffer before you can use the
     * lexer again.</p>
     *
     * @see #setInputBuffer
     */
    public void reset()
    {
        if (this.inputState != null)
        {
            this.inputState.reset();
        }

        setFilename(Recognizer.UNKNOWN_FILE);
    }


    /**
     * Returns the individual lines of the given multi-line comment.
     *
     * @param str a multi-line comment.
     * @param beginOffset the column offset of the line where the comment
     *         starts.
     * @param separator the line separator.
     *
     * @return the individual lines of the comment.
     */
    private String[] split(String str,
                               int    beginOffset,
                               String separator)
    {
        List lines = new ArrayList();
        int sepOffset = -1;
        int sepLength = separator.length();

        while ((sepOffset = str.indexOf(separator)) > -1)
        {
            String line = str.substring(0, sepOffset);
            str = str.substring(sepOffset + sepLength);

            int charOffset = StringHelper.indexOfNonWhitespace(line);

            if (charOffset > beginOffset)
            {
                line = line.substring(beginOffset);
            }
            else if (charOffset > -1)
            {
                line = line.substring(charOffset);
            }
            else
            {
                line = EMPTY_STRING;
            }

            lines.add(line);
        }

        int charOffset = StringHelper.indexOfNonWhitespace(str);

        if (charOffset > beginOffset)
        {
            str = str.substring(beginOffset);
        }
        else if (charOffset > -1)
        {
            str = str.substring(charOffset);
        }
        else
        {
            str = EMPTY_STRING;
        }

        lines.add(str);

        return (String[])lines.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Removes the leading whitespace from each line of the given multi-line
     * comment.
     *
     * @param comment a multi-line comment.
     * @param column the column offset of the line where the comment starts.
     * @param lineSeparator the line separator.
     * @return comment without leading whitespace.
     */
    private String removeLeadingWhitespace(String comment, int column,
                                           String lineSeparator)
    {
        String[] lines = split(comment, column, lineSeparator);
        StringBuffer buf = new StringBuffer(comment.length());

        for (int i = 0, size = lines.length; i < size; i++)
        {
            buf.append(lines[i]);
            buf.append(_lineSeparator);
        }

        buf.setLength(buf.length() - _lineSeparator.length());

        return buf.toString();
    }
public JavaLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public JavaLexer(Reader in) {
	this(new CharBuffer(in));
}
public JavaLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public JavaLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(true);
	literals = new Hashtable();
	literals.put(new ANTLRHashString("byte", this), new Integer(61));
	literals.put(new ANTLRHashString("public", this), new Integer(72));
	literals.put(new ANTLRHashString("case", this), new Integer(103));
	literals.put(new ANTLRHashString("short", this), new Integer(63));
	literals.put(new ANTLRHashString("break", this), new Integer(97));
	literals.put(new ANTLRHashString("while", this), new Integer(95));
	literals.put(new ANTLRHashString("new", this), new Integer(146));
	literals.put(new ANTLRHashString("instanceof", this), new Integer(131));
	literals.put(new ANTLRHashString("implements", this), new Integer(83));
	literals.put(new ANTLRHashString("synchronized", this), new Integer(77));
	literals.put(new ANTLRHashString("float", this), new Integer(65));
	literals.put(new ANTLRHashString("package", this), new Integer(54));
	literals.put(new ANTLRHashString("return", this), new Integer(100));
	literals.put(new ANTLRHashString("throw", this), new Integer(102));
	literals.put(new ANTLRHashString("null", this), new Integer(145));
	literals.put(new ANTLRHashString("protected", this), new Integer(73));
	literals.put(new ANTLRHashString("class", this), new Integer(79));
	literals.put(new ANTLRHashString("throws", this), new Integer(90));
	literals.put(new ANTLRHashString("do", this), new Integer(96));
	literals.put(new ANTLRHashString("strictfp", this), new Integer(45));
	literals.put(new ANTLRHashString("super", this), new Integer(87));
	literals.put(new ANTLRHashString("transient", this), new Integer(75));
	literals.put(new ANTLRHashString("native", this), new Integer(76));
	literals.put(new ANTLRHashString("interface", this), new Integer(81));
	literals.put(new ANTLRHashString("final", this), new Integer(43));
	literals.put(new ANTLRHashString("if", this), new Integer(92));
	literals.put(new ANTLRHashString("double", this), new Integer(67));
	literals.put(new ANTLRHashString("volatile", this), new Integer(78));
	literals.put(new ANTLRHashString("assert", this), new Integer(98));
	literals.put(new ANTLRHashString("catch", this), new Integer(106));
	literals.put(new ANTLRHashString("try", this), new Integer(105));
	literals.put(new ANTLRHashString("int", this), new Integer(64));
	literals.put(new ANTLRHashString("for", this), new Integer(94));
	literals.put(new ANTLRHashString("extends", this), new Integer(80));
	literals.put(new ANTLRHashString("boolean", this), new Integer(60));
	literals.put(new ANTLRHashString("char", this), new Integer(62));
	literals.put(new ANTLRHashString("private", this), new Integer(71));
	literals.put(new ANTLRHashString("default", this), new Integer(104));
	literals.put(new ANTLRHashString("false", this), new Integer(144));
	literals.put(new ANTLRHashString("this", this), new Integer(86));
	literals.put(new ANTLRHashString("static", this), new Integer(74));
	literals.put(new ANTLRHashString("abstract", this), new Integer(44));
	literals.put(new ANTLRHashString("continue", this), new Integer(99));
	literals.put(new ANTLRHashString("finally", this), new Integer(107));
	literals.put(new ANTLRHashString("else", this), new Integer(93));
	literals.put(new ANTLRHashString("import", this), new Integer(56));
	literals.put(new ANTLRHashString("void", this), new Integer(59));
	literals.put(new ANTLRHashString("switch", this), new Integer(101));
	literals.put(new ANTLRHashString("true", this), new Integer(143));
	literals.put(new ANTLRHashString("long", this), new Integer(66));
}

public Token nextToken() throws TokenStreamException {
	Token theRetToken=null;
tryAgain:
	for (;;) {
		Token _token = null;
		int _ttype = Token.INVALID_TYPE;
		resetText();
		try {   // for char stream error handling
			try {   // for lexical error handling
				switch ( LA(1)) {
				case '?':
				{
					mQUESTION(true);
					theRetToken=_returnToken;
					break;
				}
				case '(':
				{
					mLPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case ')':
				{
					mRPAREN(true);
					theRetToken=_returnToken;
					break;
				}
				case '[':
				{
					mLBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case ']':
				{
					mRBRACK(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case '}':
				{
					mRCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case ':':
				{
					mCOLON(true);
					theRetToken=_returnToken;
					break;
				}
				case ',':
				{
					mCOMMA(true);
					theRetToken=_returnToken;
					break;
				}
				case '~':
				{
					mBNOT(true);
					theRetToken=_returnToken;
					break;
				}
				case ';':
				{
					mSEMI(true);
					theRetToken=_returnToken;
					break;
				}
				case '\t':  case '\n':  case '\u000c':  case '\r':
				case ' ':
				{
					mWS(true);
					theRetToken=_returnToken;
					break;
				}
				case '\'':
				{
					mCHAR_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '"':
				{
					mSTRING_LITERAL(true);
					theRetToken=_returnToken;
					break;
				}
				case '$':  case 'A':  case 'B':  case 'C':
				case 'D':  case 'E':  case 'F':  case 'G':
				case 'H':  case 'I':  case 'J':  case 'K':
				case 'L':  case 'M':  case 'N':  case 'O':
				case 'P':  case 'Q':  case 'R':  case 'S':
				case 'T':  case 'U':  case 'V':  case 'W':
				case 'X':  case 'Y':  case 'Z':  case '_':
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':  case '\u00c0':  case '\u00c1':
				case '\u00c2':  case '\u00c3':  case '\u00c4':  case '\u00c5':
				case '\u00c6':  case '\u00c7':  case '\u00c8':  case '\u00c9':
				case '\u00ca':  case '\u00cb':  case '\u00cc':  case '\u00cd':
				case '\u00ce':  case '\u00cf':  case '\u00d0':  case '\u00d1':
				case '\u00d2':  case '\u00d3':  case '\u00d4':  case '\u00d5':
				case '\u00d6':  case '\u00d8':  case '\u00d9':  case '\u00da':
				case '\u00db':  case '\u00dc':  case '\u00dd':  case '\u00de':
				case '\u00df':  case '\u00e0':  case '\u00e1':  case '\u00e2':
				case '\u00e3':  case '\u00e4':  case '\u00e5':  case '\u00e6':
				case '\u00e7':  case '\u00e8':  case '\u00e9':  case '\u00ea':
				case '\u00eb':  case '\u00ec':  case '\u00ed':  case '\u00ee':
				case '\u00ef':  case '\u00f0':  case '\u00f1':  case '\u00f2':
				case '\u00f3':  case '\u00f4':  case '\u00f5':  case '\u00f6':
				case '\u00f8':  case '\u00f9':  case '\u00fa':  case '\u00fb':
				case '\u00fc':  case '\u00fd':  case '\u00fe':  case '\u00ff':
				{
					mIDENT(true);
					theRetToken=_returnToken;
					break;
				}
				case '.':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':
				{
					mNUM_INT(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='>') && (LA(4)=='=')) {
						mBSR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='=')) {
						mSR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>') && (LA(3)=='>')) {
						mBSR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<') && (LA(3)=='=')) {
						mSL_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=') && (LA(2)=='=')) {
						mEQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!') && (LA(2)=='=')) {
						mNOT_EQUAL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='=')) {
						mDIV_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='=')) {
						mPLUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+') && (LA(2)=='+')) {
						mINC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='=')) {
						mMINUS_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-') && (LA(2)=='-')) {
						mDEC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='=')) {
						mSTAR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%') && (LA(2)=='=')) {
						mMOD_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='>')) {
						mSR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>') && (LA(2)=='=')) {
						mGE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='<')) {
						mSL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='=')) {
						mLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^') && (LA(2)=='=')) {
						mBXOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='=')) {
						mBOR_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|') && (LA(2)=='|')) {
						mLOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='=')) {
						mBAND_ASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&') && (LA(2)=='&')) {
						mLAND(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='/')) {
						mCOMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='*')) {
						mML_COMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='=')) {
						mASSIGN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='!')) {
						mLNOT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/')) {
						mDIV(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='+')) {
						mPLUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='-')) {
						mMINUS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*')) {
						mSTAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='%')) {
						mMOD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='>')) {
						mGT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<')) {
						mLT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='^')) {
						mBXOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='|')) {
						mBOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='&')) {
						mBAND(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_returnToken.setType(_ttype);
				return _returnToken;
			}
			catch (RecognitionException e) {
				reportError(e);
				consume();
			}
		}
		catch (CharStreamException cse) {
			if ( cse instanceof CharStreamIOException ) {
				throw new TokenStreamIOException(((CharStreamIOException)cse).io);
			}
			else {
				throw new TokenStreamException(cse.getMessage());
			}
		}
	}
}

	public final void mQUESTION(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = QUESTION;
		int _saveIndex;
		
		try {      // for error handling
			match('?');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LPAREN;
		int _saveIndex;
		
		try {      // for error handling
			match('(');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRPAREN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RPAREN;
		int _saveIndex;
		
		try {      // for error handling
			match(')');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LBRACK;
		int _saveIndex;
		
		try {      // for error handling
			match('[');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRBRACK(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RBRACK;
		int _saveIndex;
		
		try {      // for error handling
			match(']');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LCURLY;
		int _saveIndex;
		
		try {      // for error handling
			match('{');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mRCURLY(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = RCURLY;
		int _saveIndex;
		
		try {      // for error handling
			match('}');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLON(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLON;
		int _saveIndex;
		
		try {      // for error handling
			match(':');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMA;
		int _saveIndex;
		
		try {      // for error handling
			match(',');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match('=');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mEQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EQUAL;
		int _saveIndex;
		
		try {      // for error handling
			match("==");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LNOT;
		int _saveIndex;
		
		try {      // for error handling
			match('!');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBNOT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BNOT;
		int _saveIndex;
		
		try {      // for error handling
			match('~');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNOT_EQUAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NOT_EQUAL;
		int _saveIndex;
		
		try {      // for error handling
			match("!=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIV;
		int _saveIndex;
		
		try {      // for error handling
			match('/');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDIV_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIV_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("/=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS;
		int _saveIndex;
		
		try {      // for error handling
			match('+');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPLUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PLUS_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("+=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mINC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INC;
		int _saveIndex;
		
		try {      // for error handling
			match("++");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS;
		int _saveIndex;
		
		try {      // for error handling
			match('-');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMINUS_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MINUS_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("-=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mDEC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DEC;
		int _saveIndex;
		
		try {      // for error handling
			match("--");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR;
		int _saveIndex;
		
		try {      // for error handling
			match('*');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTAR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STAR_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("*=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMOD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MOD;
		int _saveIndex;
		
		try {      // for error handling
			match('%');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mMOD_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = MOD_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("%=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SR;
		int _saveIndex;
		
		try {      // for error handling
			match(">>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SR_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match(">>=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBSR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BSR;
		int _saveIndex;
		
		try {      // for error handling
			match(">>>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBSR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BSR_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match(">>>=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GE;
		int _saveIndex;
		
		try {      // for error handling
			match(">=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mGT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = GT;
		int _saveIndex;
		
		try {      // for error handling
			match(">");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL;
		int _saveIndex;
		
		try {      // for error handling
			match("<<");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSL_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("<<=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LE;
		int _saveIndex;
		
		try {      // for error handling
			match("<=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LT;
		int _saveIndex;
		
		try {      // for error handling
			match('<');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR;
		int _saveIndex;
		
		try {      // for error handling
			match('^');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBXOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BXOR_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("^=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR;
		int _saveIndex;
		
		try {      // for error handling
			match('|');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBOR_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BOR_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("|=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LOR;
		int _saveIndex;
		
		try {      // for error handling
			match("||");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND;
		int _saveIndex;
		
		try {      // for error handling
			match('&');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBAND_ASSIGN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BAND_ASSIGN;
		int _saveIndex;
		
		try {      // for error handling
			match("&=");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mLAND(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LAND;
		int _saveIndex;
		
		try {      // for error handling
			match("&&");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSEMI(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEMI;
		int _saveIndex;
		
		try {      // for error handling
			match(';');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		try {      // for error handling
			{
			int _cnt246=0;
			_loop246:
			do {
				switch ( LA(1)) {
				case ' ':
				{
					match(' ');
					break;
				}
				case '\t':
				{
					match('\t');
					break;
				}
				case '\u000c':
				{
					match('\f');
					break;
				}
				case '\n':  case '\r':
				{
					{
					if ((LA(1)=='\r') && (LA(2)=='\n')) {
						match("\r\n");
						if (_fileFormat == FileFormat.UNKNOWN)
						_fileFormat = FileFormat.DOS;
						_lineSeparator = "\r\n";
						
					}
					else if ((LA(1)=='\n')) {
						match('\n');
						if (_fileFormat == FileFormat.UNKNOWN)
						_fileFormat = FileFormat.UNIX;
						_lineSeparator = "\n";
						
					}
					else if ((LA(1)=='\r')) {
						match('\r');
						if (_fileFormat == FileFormat.UNKNOWN)
						_fileFormat = FileFormat.MAC;
						_lineSeparator = "\r";
						
					}
					else {
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					
					}
					newline();
					break;
				}
				default:
				{
					if ( _cnt246>=1 ) { break _loop246; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				_cnt246++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSPECIAL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SPECIAL_COMMENT;
		int _saveIndex;
		int column = getColumn()-1;
		
		try {      // for error handling
			match("//J-");
			{
			_loop250:
			do {
				// nongreedy exit test
				if ((LA(1)=='/') && (LA(2)=='/') && (LA(3)=='J') && (LA(4)=='+')) break _loop250;
				if ((LA(1)=='\r') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
					match('\r');
					{
					if ((LA(1)=='\n') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
						match('\n');
					}
					else if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
					}
					else {
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					
					}
					newline();
				}
				else if ((LA(1)=='\n') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
					match('\n');
					newline();
				}
				else if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
					matchNot(EOF_CHAR);
				}
				else {
					break _loop250;
				}
				
			} while (true);
			}
			match("//J+");
			
			String t = new String(text.getBuffer(),_begin,text.length()-_begin);
			Token tok = new ExtendedToken(JavaTokenTypes.SPECIAL_COMMENT, StringHelper.leftPad(t, t.length()+column));
			
			_token = tok;
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSEPARATOR_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SEPARATOR_COMMENT;
		int _saveIndex;
		
		try {      // for error handling
			match("//~");
			{
			_loop254:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					{
					match(_tokenSet_1);
					}
					if (LA(1) == EOF_CHAR) break;
				}
				else {
					break _loop254;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case '\n':
			{
				_saveIndex=text.length();
				match('\n');
				text.setLength(_saveIndex);
				break;
			}
			case '\r':
			{
				_saveIndex=text.length();
				match('\r');
				text.setLength(_saveIndex);
				{
				if ((LA(1)=='\n')) {
					_saveIndex=text.length();
					match('\n');
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			default:
				{
				}
			}
			}
			newline();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSL_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SL_COMMENT;
		int _saveIndex;
		
		try {      // for error handling
			match("//");
			{
			_loop260:
			do {
				if ((_tokenSet_1.member(LA(1)))) {
					{
					match(_tokenSet_1);
					}
					if (LA(1) == EOF_CHAR) break;
				}
				else {
					break _loop260;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case '\n':
			{
				_saveIndex=text.length();
				match('\n');
				text.setLength(_saveIndex);
				break;
			}
			case '\r':
			{
				_saveIndex=text.length();
				match('\r');
				text.setLength(_saveIndex);
				{
				if ((LA(1)=='\n')) {
					_saveIndex=text.length();
					match('\n');
					text.setLength(_saveIndex);
				}
				else {
				}
				
				}
				break;
			}
			default:
				{
				}
			}
			}
			newline();
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMENT;
		int _saveIndex;
		Token spec=null;
		Token sep=null;
		Token sl=null;
		
		try {      // for error handling
			{
			if ((LA(1)=='/') && (LA(2)=='/') && (LA(3)=='J') && (LA(4)=='-')) {
				mSPECIAL_COMMENT(true);
				spec=_returnToken;
				_token = spec;
			}
			else if ((LA(1)=='/') && (LA(2)=='/') && (LA(3)=='~')) {
				mSEPARATOR_COMMENT(true);
				sep=_returnToken;
				_token = sep;
			}
			else if ((LA(1)=='/') && (LA(2)=='/')) {
				mSL_COMMENT(true);
				sl=_returnToken;
				_token = sl;
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mML_COMMENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ML_COMMENT;
		int _saveIndex;
		
		// we need the line info for the Javadoc parser
		int line = getLine();
		int column = getColumn();
		boolean javadoc = false;
		
		
		try {      // for error handling
			match("/*");
			if (LA(1)=='*') javadoc = true;
			{
			_loop268:
			do {
				if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0000' && LA(4) <= '\ufffe'))) {
					match('\r');
					match('\n');
					newline();
				}
				else if (((LA(1)=='*') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe')))&&( LA(2)!='/' )) {
					match('*');
				}
				else if ((LA(1)=='\r') && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0000' && LA(3) <= '\ufffe'))) {
					match('\r');
					newline();
				}
				else if ((LA(1)=='\n')) {
					match('\n');
					newline();
				}
				else if ((_tokenSet_2.member(LA(1)))) {
					{
					match(_tokenSet_2);
					}
				}
				else {
					break _loop268;
				}
				
			} while (true);
			}
			match("*/");
			
			
			// we found a Javadoc comment
			if (javadoc)
			{
			if (!this.removeJavadocComments)
			{
			// only parse Javadoc comments if the user likes this
			// feature as much as I do (and explictly enabled it)
			if (this.parseJavadocComments)
			{
			try
			{
			String t = new String(text.getBuffer(),_begin,text.length()-_begin);
			_recognizer.setLine(line);
			_recognizer.setColumn(column);
			_recognizer.parse(t, getFilename());
			Node comment = (Node)_recognizer.getParseTree();
			
			// ignore empty comments
			if (comment != JavadocParser.EMPTY_JAVADOC_COMMENT)
			{
			ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, t);
			token.comment = comment;
			_token = token;
			}
			}
			catch (IOException ex)
			{
			throw new TokenStreamIOException(ex);
			}
			}
			else
			{
			// XXX only if not in tab mode
			// replace tabs
			
			String t = new String(text.getBuffer(),_begin,text.length()-_begin);
			
			if (t.indexOf('\t') > -1)
			{
			t = StringHelper.replace(t, "\t", StringHelper.repeat(SPACE, getTabSize()));
			}
			
			t = removeLeadingWhitespace(t, column -1, _lineSeparator);
			
			text.setLength(_begin); text.append(t);
			_ttype = JavaTokenTypes.JAVADOC_COMMENT;
			}
			}
			else
			{
			_ttype = Token.SKIP;
			}
			}
			else
			{
			if (!this.removeMLComments)
			{
			String t = new String(text.getBuffer(),_begin,text.length()-_begin);
			
			// replace tabs
			if (t.indexOf('\t') > -1)
			{
			t = StringHelper.replace(t, "\t", StringHelper.repeat(SPACE, getTabSize()));
			}
			
			// in case we don't format multi-line comments, we have
			// to remove the leading whitespace for each line
			if (!this.formatMLComments)
			{
			t = removeLeadingWhitespace(t, column -1, _lineSeparator);
			}
			
			text.setLength(_begin); text.append(t);
			}
			else
			_ttype = Token.SKIP;
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCHAR_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CHAR_LITERAL;
		int _saveIndex;
		
		try {      // for error handling
			match('\'');
			{
			if ((LA(1)=='\\')) {
				mESC(false);
			}
			else if ((_tokenSet_3.member(LA(1)))) {
				matchNot('\'');
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			match('\'');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mESC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ESC;
		int _saveIndex;
		
		try {      // for error handling
			match('\\');
			{
			switch ( LA(1)) {
			case 'n':
			{
				match('n');
				break;
			}
			case 'r':
			{
				match('r');
				break;
			}
			case 't':
			{
				match('t');
				break;
			}
			case 'b':
			{
				match('b');
				break;
			}
			case 'f':
			{
				match('f');
				break;
			}
			case '"':
			{
				match('"');
				break;
			}
			case '\'':
			{
				match('\'');
				break;
			}
			case '\\':
			{
				match('\\');
				break;
			}
			case 'u':
			{
				{
				int _cnt278=0;
				_loop278:
				do {
					if ((LA(1)=='u')) {
						match('u');
					}
					else {
						if ( _cnt278>=1 ) { break _loop278; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
					}
					
					_cnt278++;
				} while (true);
				}
				mHEX_DIGIT(false);
				mHEX_DIGIT(false);
				mHEX_DIGIT(false);
				mHEX_DIGIT(false);
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			{
				{
				matchRange('0','3');
				}
				{
				if (((LA(1) >= '0' && LA(1) <= '7')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe'))) {
					{
					matchRange('0','7');
					}
					{
					if (((LA(1) >= '0' && LA(1) <= '7')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe'))) {
						matchRange('0','7');
					}
					else if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe'))) {
					}
					else {
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					
					}
				}
				else if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe'))) {
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
				break;
			}
			case '4':  case '5':  case '6':  case '7':
			{
				{
				matchRange('4','7');
				}
				{
				if (((LA(1) >= '0' && LA(1) <= '9')) && ((LA(2) >= '\u0000' && LA(2) <= '\ufffe'))) {
					{
					matchRange('0','9');
					}
				}
				else if (((LA(1) >= '\u0000' && LA(1) <= '\ufffe'))) {
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mSTRING_LITERAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING_LITERAL;
		int _saveIndex;
		
		try {      // for error handling
			match('"');
			{
			_loop274:
			do {
				if ((LA(1)=='\\')) {
					mESC(false);
				}
				else if ((_tokenSet_5.member(LA(1)))) {
					{
					match(_tokenSet_5);
					}
				}
				else {
					break _loop274;
				}
				
			} while (true);
			}
			match('"');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEX_DIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEX_DIGIT;
		int _saveIndex;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':
			{
				matchRange('A','F');
				break;
			}
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':
			{
				matchRange('a','f');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mVOCAB(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = VOCAB;
		int _saveIndex;
		
		try {      // for error handling
			matchRange('\3','\377');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mIDENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IDENT;
		int _saveIndex;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':
			{
				matchRange('a','z');
				break;
			}
			case 'A':  case 'B':  case 'C':  case 'D':
			case 'E':  case 'F':  case 'G':  case 'H':
			case 'I':  case 'J':  case 'K':  case 'L':
			case 'M':  case 'N':  case 'O':  case 'P':
			case 'Q':  case 'R':  case 'S':  case 'T':
			case 'U':  case 'V':  case 'W':  case 'X':
			case 'Y':  case 'Z':
			{
				matchRange('A','Z');
				break;
			}
			case '_':
			{
				match('_');
				break;
			}
			case '$':
			{
				match('$');
				break;
			}
			case '\u00c0':  case '\u00c1':  case '\u00c2':  case '\u00c3':
			case '\u00c4':  case '\u00c5':  case '\u00c6':  case '\u00c7':
			case '\u00c8':  case '\u00c9':  case '\u00ca':  case '\u00cb':
			case '\u00cc':  case '\u00cd':  case '\u00ce':  case '\u00cf':
			case '\u00d0':  case '\u00d1':  case '\u00d2':  case '\u00d3':
			case '\u00d4':  case '\u00d5':  case '\u00d6':
			{
				matchRange('\u00C0','\u00D6');
				break;
			}
			case '\u00d8':  case '\u00d9':  case '\u00da':  case '\u00db':
			case '\u00dc':  case '\u00dd':  case '\u00de':  case '\u00df':
			case '\u00e0':  case '\u00e1':  case '\u00e2':  case '\u00e3':
			case '\u00e4':  case '\u00e5':  case '\u00e6':  case '\u00e7':
			case '\u00e8':  case '\u00e9':  case '\u00ea':  case '\u00eb':
			case '\u00ec':  case '\u00ed':  case '\u00ee':  case '\u00ef':
			case '\u00f0':  case '\u00f1':  case '\u00f2':  case '\u00f3':
			case '\u00f4':  case '\u00f5':  case '\u00f6':
			{
				matchRange('\u00D8','\u00F6');
				break;
			}
			case '\u00f8':  case '\u00f9':  case '\u00fa':  case '\u00fb':
			case '\u00fc':  case '\u00fd':  case '\u00fe':  case '\u00ff':
			{
				matchRange('\u00F8','\u00FF');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			_loop292:
			do {
				switch ( LA(1)) {
				case 'a':  case 'b':  case 'c':  case 'd':
				case 'e':  case 'f':  case 'g':  case 'h':
				case 'i':  case 'j':  case 'k':  case 'l':
				case 'm':  case 'n':  case 'o':  case 'p':
				case 'q':  case 'r':  case 's':  case 't':
				case 'u':  case 'v':  case 'w':  case 'x':
				case 'y':  case 'z':
				{
					matchRange('a','z');
					break;
				}
				case 'A':  case 'B':  case 'C':  case 'D':
				case 'E':  case 'F':  case 'G':  case 'H':
				case 'I':  case 'J':  case 'K':  case 'L':
				case 'M':  case 'N':  case 'O':  case 'P':
				case 'Q':  case 'R':  case 'S':  case 'T':
				case 'U':  case 'V':  case 'W':  case 'X':
				case 'Y':  case 'Z':
				{
					matchRange('A','Z');
					break;
				}
				case '_':
				{
					match('_');
					break;
				}
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					matchRange('0','9');
					break;
				}
				case '$':
				{
					match('$');
					break;
				}
				case '\u00c0':  case '\u00c1':  case '\u00c2':  case '\u00c3':
				case '\u00c4':  case '\u00c5':  case '\u00c6':  case '\u00c7':
				case '\u00c8':  case '\u00c9':  case '\u00ca':  case '\u00cb':
				case '\u00cc':  case '\u00cd':  case '\u00ce':  case '\u00cf':
				case '\u00d0':  case '\u00d1':  case '\u00d2':  case '\u00d3':
				case '\u00d4':  case '\u00d5':  case '\u00d6':
				{
					matchRange('\u00C0','\u00D6');
					break;
				}
				case '\u00d8':  case '\u00d9':  case '\u00da':  case '\u00db':
				case '\u00dc':  case '\u00dd':  case '\u00de':  case '\u00df':
				case '\u00e0':  case '\u00e1':  case '\u00e2':  case '\u00e3':
				case '\u00e4':  case '\u00e5':  case '\u00e6':  case '\u00e7':
				case '\u00e8':  case '\u00e9':  case '\u00ea':  case '\u00eb':
				case '\u00ec':  case '\u00ed':  case '\u00ee':  case '\u00ef':
				case '\u00f0':  case '\u00f1':  case '\u00f2':  case '\u00f3':
				case '\u00f4':  case '\u00f5':  case '\u00f6':
				{
					matchRange('\u00D8','\u00F6');
					break;
				}
				case '\u00f8':  case '\u00f9':  case '\u00fa':  case '\u00fb':
				case '\u00fc':  case '\u00fd':  case '\u00fe':  case '\u00ff':
				{
					matchRange('\u00F8','\u00FF');
					break;
				}
				default:
				{
					break _loop292;
				}
				}
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		_ttype = testLiteralsTable(_ttype);
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mNUM_INT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NUM_INT;
		int _saveIndex;
		Token f1=null;
		Token f2=null;
		Token f3=null;
		Token f4=null;
		boolean isDecimal=false; Token t=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case '.':
			{
				match('.');
				_ttype = JavaTokenTypes.DOT;
				{
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					{
					int _cnt296=0;
					_loop296:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							matchRange('0','9');
						}
						else {
							if ( _cnt296>=1 ) { break _loop296; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
						}
						
						_cnt296++;
					} while (true);
					}
					{
					if ((LA(1)=='E'||LA(1)=='e')) {
						mEXPONENT(false);
					}
					else {
					}
					
					}
					{
					if ((_tokenSet_6.member(LA(1)))) {
						mFLOAT_SUFFIX(true);
						f1=_returnToken;
						t=f1;
					}
					else {
					}
					
					}
					
					if (t != null && t.getText().toUpperCase().indexOf('D')>=0) {
					_ttype = JavaTokenTypes.NUM_DOUBLE;
					}
					else {
					_ttype = JavaTokenTypes.NUM_FLOAT;
					}
					
				}
				else {
				}
				
				}
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				{
				switch ( LA(1)) {
				case '0':
				{
					match('0');
					isDecimal = true;
					{
					switch ( LA(1)) {
					case 'X':  case 'x':
					{
						{
						switch ( LA(1)) {
						case 'x':
						{
							match('x');
							break;
						}
						case 'X':
						{
							match('X');
							break;
						}
						default:
						{
							throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
						}
						}
						}
						{
						int _cnt303=0;
						_loop303:
						do {
							if ((_tokenSet_7.member(LA(1)))) {
								mHEX_DIGIT(false);
							}
							else {
								if ( _cnt303>=1 ) { break _loop303; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt303++;
						} while (true);
						}
						break;
					}
					case '0':  case '1':  case '2':  case '3':
					case '4':  case '5':  case '6':  case '7':
					{
						{
						int _cnt305=0;
						_loop305:
						do {
							if (((LA(1) >= '0' && LA(1) <= '7'))) {
								matchRange('0','7');
							}
							else {
								if ( _cnt305>=1 ) { break _loop305; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
							}
							
							_cnt305++;
						} while (true);
						}
						break;
					}
					default:
						{
						}
					}
					}
					break;
				}
				case '1':  case '2':  case '3':  case '4':
				case '5':  case '6':  case '7':  case '8':
				case '9':
				{
					{
					matchRange('1','9');
					}
					{
					_loop308:
					do {
						if (((LA(1) >= '0' && LA(1) <= '9'))) {
							matchRange('0','9');
						}
						else {
							break _loop308;
						}
						
					} while (true);
					}
					isDecimal=true;
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				{
				if ((LA(1)=='L'||LA(1)=='l')) {
					{
					switch ( LA(1)) {
					case 'l':
					{
						match('l');
						break;
					}
					case 'L':
					{
						match('L');
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					_ttype = JavaTokenTypes.NUM_LONG;
				}
				else if (((_tokenSet_8.member(LA(1))))&&(isDecimal)) {
					{
					switch ( LA(1)) {
					case '.':
					{
						match('.');
						{
						_loop313:
						do {
							if (((LA(1) >= '0' && LA(1) <= '9'))) {
								matchRange('0','9');
							}
							else {
								break _loop313;
							}
							
						} while (true);
						}
						{
						if ((LA(1)=='E'||LA(1)=='e')) {
							mEXPONENT(false);
						}
						else {
						}
						
						}
						{
						if ((_tokenSet_6.member(LA(1)))) {
							mFLOAT_SUFFIX(true);
							f2=_returnToken;
							t=f2;
						}
						else {
						}
						
						}
						break;
					}
					case 'E':  case 'e':
					{
						mEXPONENT(false);
						{
						if ((_tokenSet_6.member(LA(1)))) {
							mFLOAT_SUFFIX(true);
							f3=_returnToken;
							t=f3;
						}
						else {
						}
						
						}
						break;
					}
					case 'D':  case 'F':  case 'd':  case 'f':
					{
						mFLOAT_SUFFIX(true);
						f4=_returnToken;
						t=f4;
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					
					if (t != null && t.getText().toUpperCase() .indexOf('D') >= 0) {
					_ttype = JavaTokenTypes.NUM_DOUBLE;
					}
					else {
					_ttype = JavaTokenTypes.NUM_FLOAT;
					}
					
				}
				else {
				}
				
				}
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEXPONENT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EXPONENT;
		int _saveIndex;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case 'e':
			{
				match('e');
				break;
			}
			case 'E':
			{
				match('E');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			switch ( LA(1)) {
			case '+':
			{
				match('+');
				break;
			}
			case '-':
			{
				match('-');
				break;
			}
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			int _cnt321=0;
			_loop321:
			do {
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					matchRange('0','9');
				}
				else {
					if ( _cnt321>=1 ) { break _loop321; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt321++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mFLOAT_SUFFIX(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = FLOAT_SUFFIX;
		int _saveIndex;
		
		try {      // for error handling
			switch ( LA(1)) {
			case 'f':
			{
				match('f');
				break;
			}
			case 'F':
			{
				match('F');
				break;
			}
			case 'd':
			{
				match('d');
				break;
			}
			case 'D':
			{
				match('D');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_0);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[1025];
		for (int i = 0; i<=1024; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[2048];
		data[0]=-9217L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[2048];
		data[0]=-4398046520321L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[2048];
		data[0]=-549755813889L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[2048];
		for (int i = 0; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[2048];
		data[0]=-17179869185L;
		data[1]=-268435457L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		for (int i = 1024; i<=2047; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[1025];
		data[0]=0L;
		data[1]=343597383760L;
		for (int i = 2; i<=1024; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[1025];
		data[0]=287948901175001088L;
		data[1]=541165879422L;
		for (int i = 2; i<=1024; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[1025];
		data[0]=70368744177664L;
		data[1]=481036337264L;
		for (int i = 2; i<=1024; i++) { data[i]=0L; }
		return data;
	}
	private static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	
	}
