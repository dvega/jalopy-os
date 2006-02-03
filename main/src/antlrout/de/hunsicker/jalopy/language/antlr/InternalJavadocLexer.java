// $ANTLR 2.7.5 (20050128): "java.doc.g" -> "InternalJavadocLexer.java"$

package de.hunsicker.jalopy.language.antlr;

import java.io.InputStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.CharStreamException;
import antlr.CharStreamIOException;
import antlr.ANTLRException;
import java.io.Reader;
import java.util.Hashtable;
import antlr.CharScanner;
import antlr.InputBuffer;
import antlr.ByteBuffer;
import antlr.CharBuffer;
import antlr.Token;
import antlr.CommonToken;
import antlr.RecognitionException;
import antlr.NoViableAltForCharException;
import antlr.MismatchedCharException;
import antlr.TokenStream;
import antlr.ANTLRHashString;
import antlr.LexerSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.SemanticException;

import de.hunsicker.util.Lcs;
import java.io.StringReader;
import de.hunsicker.io.FileFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Token lexer for the Javadoc parser.
 *
 * <p>This lexer has limited build-in error recovery which relies on the
 * generated token types mapping table (<code>JavadocTokenTypes.txt</code>).
 * Therefore it is a <strong>necessity to copy this file after every build into
 * </strong> the directory where the classfile comes to reside.</p>
 *
 * <p>I strongly encourage you to automate this process as part of your
 * <a href="http://jakarta.apache.org/ant/">Ant</a> build script or whatever
 * build tool you use.</p>
 *
 * <p><strong>Sample Usage:</strong></p>
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
 * <p>This is an <a href="http://www.antlr.org">ANTLR</a> automated generated
 * file. <strong>DO NOT EDIT</strong> but rather change the associated grammar
 * (<code>java.doc.g</code>) and rebuild.</p>
 *
 * @version 1.0
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @see de.hunsicker.jalopy.language.JavadocParser
 * @see de.hunsicker.jalopy.language.Recognizer
 */
public abstract class InternalJavadocLexer extends antlr.CharScanner implements JavadocTokenTypes, TokenStream
 {

    /** The detected file format. */
    protected FileFormat _fileFormat = FileFormat.UNKNOWN;

    /**
     * Replaces the tab char last read into the text buffer with an
     * equivalent number of spaces. Note that we assume you know what you do,
     * we don't check if indeed the tab char were read!
     *
     * @throws CharStreamException if an I/O error occured.
     */
     protected abstract void replaceTab() throws CharStreamException;

    /**
     * Skips leading spaces and asterix.
     *
     * @param skipAllLeadingWhitespace if <code>true</code>, all leading
     *        whitespace until the last space before the first word will be
     *        removed; if <code>false</code> only whitespace until and
     *        inclusive a leading asterix will be removed.
     * @throws CharStreamException if an I/O error occured.
     *
     * @see #newline(boolean)
     */
    protected abstract void skipLeadingSpaceAndAsterix(boolean skipAllLeadingWhitespace)
        throws CharStreamException;

    /**
     * Inserts a newline.
     *
     * @param skipAllLeadingWhitespace if <code>true</code>, all leading
     *        whitespace until the last space before the first word will be
     *        removed; if <code>false</code> only whitespace until and
     *        inclusive a leading asterix will be removed.
     *
     * @see #makeToken
     */
    public abstract void newline(boolean skipAllLeadingWhitespace);

    /**
     * Replaces the newline chars last read into the text buffer with a
     * single space. Note that we assume you know what you do; we don't check
     * if indeed newline chars were read!
     *
     * @param length length of the newline chars (1 or 2).
     * @throws CharStreamException if an I/O error occured.
     */
    protected abstract void replaceNewline(int length) throws CharStreamException;

    /**
     * To satisfy antlr
     * 
     * @param ex
     * @param set
     */
	public void recover(RecognitionException ex, BitSet set) {
        
    }

public InternalJavadocLexer(InputStream in) {
	this(new ByteBuffer(in));
}
public InternalJavadocLexer(Reader in) {
	this(new CharBuffer(in));
}
public InternalJavadocLexer(InputBuffer ib) {
	this(new LexerSharedInputState(ib));
}
public InternalJavadocLexer(LexerSharedInputState state) {
	super(state);
	caseSensitiveLiterals = true;
	setCaseSensitive(false);
	literals = new Hashtable();
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
				case '}':
				{
					mRCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				case '{':
				{
					mLCURLY(true);
					theRetToken=_returnToken;
					break;
				}
				default:
					if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='1') && (_tokenSet_0.member(LA(4)))) {
						mOH1(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='1')) {
						mCH1(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='2') && (_tokenSet_0.member(LA(4)))) {
						mOH2(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='2')) {
						mCH2(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='3') && (_tokenSet_0.member(LA(4)))) {
						mOH3(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='3')) {
						mCH3(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='4') && (_tokenSet_0.member(LA(4)))) {
						mOH4(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='4')) {
						mCH4(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='5') && (_tokenSet_0.member(LA(4)))) {
						mOH5(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='5')) {
						mCH5(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='6') && (_tokenSet_0.member(LA(4)))) {
						mOH6(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='h') && (LA(4)=='6')) {
						mCH6(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='a') && (LA(4)=='d')) {
						mCADDRESS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='u') && (LA(4)=='l')) {
						mCULIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='d') && (LA(4)=='l')) {
						mCDLIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='d') && (LA(4)=='t')) {
						mCDTERM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='d') && (LA(4)=='d')) {
						mCDDEF(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='i') && (LA(4)=='r')) {
						mODIR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='d') && (LA(4)=='i')) {
						mCDIR_OR_CDIV(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='i') && (LA(4)=='v')) {
						mODIV(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='c') && (LA(4)=='e')) {
						mCCENTER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='b') && (LA(4)=='l')) {
						mCBQUOTE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='a')) {
						mCTABLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='c') && (LA(4)=='a')) {
						mCCAP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='r')) {
						mC_TR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='d'||LA(4)=='h')) {
						mC_TH_OR_TD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='t')) {
						mCTTYPE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='c') && (LA(4)=='o')) {
						mCCODE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='b') && (LA(4)=='>')) {
						mCBOLD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='u') && (LA(4)=='>')) {
						mCUNDER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='>'||LA(4)=='t')) {
						mCSTRIKE_OR_CSTRONG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='b') && (LA(4)=='i')) {
						mCBIG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='m')) {
						mCSMALL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='s') && (LA(3)=='u') && (LA(4)=='b')) {
						mOSUB(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='s') && (LA(3)=='u') && (LA(4)=='p')) {
						mOSUP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='u')) {
						mCSUB_OR_CSUP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='d') && (LA(4)=='f')) {
						mCDFN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='a')) {
						mCSAMP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='c') && (LA(4)=='i')) {
						mCCITE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='a') && (LA(4)=='c')) {
						mCACRO(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='a') && (LA(4)=='>')) {
						mCANCHOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='a') && (LA(3)=='d')) {
						mOADDRESS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='p') && (_tokenSet_0.member(LA(3))) && (true)) {
						mOPARA(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='p')) {
						mCPARA(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='u') && (LA(3)=='l')) {
						mOULIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='o') && (LA(3)=='l')) {
						mOOLIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='o')) {
						mCOLIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='l') && (LA(3)=='i')) {
						mOLITEM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='l')) {
						mCLITEM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='l')) {
						mODLIST(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='t')) {
						mODTERM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='d')) {
						mODDEF(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='c') && (LA(3)=='e')) {
						mOCENTER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='b') && (LA(3)=='l')) {
						mOBQUOTE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='h') && (LA(3)=='r')) {
						mHR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='a')) {
						mOTABLE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='c') && (LA(3)=='a')) {
						mOCAP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='r')) {
						mO_TR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='d'||LA(3)=='h')) {
						mO_TH_OR_TD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='t')) {
						mOTTYPE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='c') && (LA(3)=='o')) {
						mOCODE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='i') && (_tokenSet_0.member(LA(3))) && (true)) {
						mOITALIC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='i')) {
						mCITALIC(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='b') && (_tokenSet_0.member(LA(3))) && (true)) {
						mOBOLD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='u') && (_tokenSet_0.member(LA(3))) && (true)) {
						mOUNDER(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='s') && (_tokenSet_1.member(LA(3))) && (true)) {
						mOSTRIKE_OR_OSTRONG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='b') && (LA(3)=='i')) {
						mOBIG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='s') && (LA(3)=='m')) {
						mOSMALL(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='e') && (LA(3)=='m')) {
						mOEM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='e')) {
						mCEM(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='d') && (LA(3)=='f')) {
						mODFN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='s') && (LA(3)=='a')) {
						mOSAMP(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='k') && (LA(3)=='b')) {
						mOKBD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='k')) {
						mCKBD(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='v') && (LA(3)=='a')) {
						mOVAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='v')) {
						mCVAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='c') && (LA(3)=='i')) {
						mOCITE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='a') && (LA(3)=='c')) {
						mOACRO(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='a') && (_tokenSet_2.member(LA(3)))) {
						mOANCHOR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='i') && (LA(3)=='m')) {
						mIMG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='f') && (LA(3)=='o')) {
						mOFONT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='f')) {
						mCFONT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='b') && (LA(3)=='r')) {
						mBR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='p') && (LA(3)=='r')) {
						mPRE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (_tokenSet_3.member(LA(2))) && (_tokenSet_4.member(LA(3))) && (true)) {
						mTYPEDCLASS(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='/') && (LA(2)=='*')) {
						mJAVADOC_OPEN(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (LA(2)=='/')) {
						mJAVADOC_CLOSE(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='@') && (_tokenSet_3.member(LA(2)))) {
						mTAG(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='<') && (LA(2)=='!')) {
						mCOMMENT(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='*') && (true)) {
						mSTAR(true);
						theRetToken=_returnToken;
					}
					else if ((LA(1)=='@') && (true)) {
						mAT(true);
						theRetToken=_returnToken;
					}
					else if ((_tokenSet_5.member(LA(1))) && (true)) {
						mPCDATA(true);
						theRetToken=_returnToken;
					}
				else {
					if (LA(1)==EOF_CHAR) {uponEOF(); _returnToken = makeToken(Token.EOF_TYPE);}
				else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				if ( _returnToken==null ) continue tryAgain; // found SKIP token
				_ttype = _returnToken.getType();
				_ttype = testLiteralsTable(_ttype);
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

	public final void mOH1(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH1;
		int _saveIndex;
		
		try {      // for error handling
			match("<h1");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mWS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WS;
		int _saveIndex;
		
		try {      // for error handling
			{
			int _cnt840=0;
			_loop840:
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
					replaceTab();
					break;
				}
				case '\n':
				{
					_saveIndex=text.length();
					match('\n');
					text.setLength(_saveIndex);
					newline();
					break;
				}
				default:
					if ((LA(1)=='\r') && (LA(2)=='\n') && (true) && (true)) {
						_saveIndex=text.length();
						match("\r\n");
						text.setLength(_saveIndex);
						newline();
					}
					else if ((LA(1)=='\r') && (true) && (true) && (true)) {
						_saveIndex=text.length();
						match('\r');
						text.setLength(_saveIndex);
						newline();
					}
				else {
					if ( _cnt840>=1 ) { break _loop840; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				_cnt840++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_7);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mATTR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ATTR;
		int _saveIndex;
		
		try {      // for error handling
			mWORD(false);
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				break;
			}
			case '.':  case '/':  case '=':  case '>':
			case 'a':  case 'b':  case 'c':  case 'd':
			case 'e':  case 'f':  case 'g':  case 'h':
			case 'i':  case 'j':  case 'k':  case 'l':
			case 'm':  case 'n':  case 'o':  case 'p':
			case 'q':  case 'r':  case 's':  case 't':
			case 'u':  case 'v':  case 'w':  case 'x':
			case 'y':  case 'z':  case '\u00df':  case '\u00e0':
			case '\u00e1':  case '\u00e2':  case '\u00e3':  case '\u00e4':
			case '\u00e5':  case '\u00e6':  case '\u00e7':  case '\u00e8':
			case '\u00e9':  case '\u00ea':  case '\u00eb':  case '\u00ec':
			case '\u00ed':  case '\u00ee':  case '\u00ef':  case '\u00f0':
			case '\u00f1':  case '\u00f2':  case '\u00f3':  case '\u00f4':
			case '\u00f5':  case '\u00f6':  case '\u00f7':  case '\u00f8':
			case '\u00f9':  case '\u00fa':  case '\u00fb':  case '\u00fc':
			case '\u00fd':  case '\u00fe':  case '\u00ff':
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
			switch ( LA(1)) {
			case '=':
			{
				match('=');
				{
				switch ( LA(1)) {
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '"':  case '#':  case '\'':  case '-':
				case '.':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':  case 'a':
				case 'b':  case 'c':  case 'd':  case 'e':
				case 'f':  case 'g':  case 'h':  case 'i':
				case 'j':  case 'k':  case 'l':  case 'm':
				case 'n':  case 'o':  case 'p':  case 'q':
				case 'r':  case 's':  case 't':  case 'u':
				case 'v':  case 'w':  case 'x':  case 'y':
				case 'z':  case '\u00df':  case '\u00e0':  case '\u00e1':
				case '\u00e2':  case '\u00e3':  case '\u00e4':  case '\u00e5':
				case '\u00e6':  case '\u00e7':  case '\u00e8':  case '\u00e9':
				case '\u00ea':  case '\u00eb':  case '\u00ec':  case '\u00ed':
				case '\u00ee':  case '\u00ef':  case '\u00f0':  case '\u00f1':
				case '\u00f2':  case '\u00f3':  case '\u00f4':  case '\u00f5':
				case '\u00f6':  case '\u00f7':  case '\u00f8':  case '\u00f9':
				case '\u00fa':  case '\u00fb':  case '\u00fc':  case '\u00fd':
				case '\u00fe':  case '\u00ff':
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
				switch ( LA(1)) {
				case '-':  case '0':  case '1':  case '2':
				case '3':  case '4':  case '5':  case '6':
				case '7':  case '8':  case '9':
				{
					{
					switch ( LA(1)) {
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
					mINT(false);
					{
					switch ( LA(1)) {
					case '%':
					{
						match('%');
						break;
					}
					case '\t':  case '\n':  case '\r':  case ' ':
					case '.':  case '/':  case '>':  case 'a':
					case 'b':  case 'c':  case 'd':  case 'e':
					case 'f':  case 'g':  case 'h':  case 'i':
					case 'j':  case 'k':  case 'l':  case 'm':
					case 'n':  case 'o':  case 'p':  case 'q':
					case 'r':  case 's':  case 't':  case 'u':
					case 'v':  case 'w':  case 'x':  case 'y':
					case 'z':  case '\u00df':  case '\u00e0':  case '\u00e1':
					case '\u00e2':  case '\u00e3':  case '\u00e4':  case '\u00e5':
					case '\u00e6':  case '\u00e7':  case '\u00e8':  case '\u00e9':
					case '\u00ea':  case '\u00eb':  case '\u00ec':  case '\u00ed':
					case '\u00ee':  case '\u00ef':  case '\u00f0':  case '\u00f1':
					case '\u00f2':  case '\u00f3':  case '\u00f4':  case '\u00f5':
					case '\u00f6':  case '\u00f7':  case '\u00f8':  case '\u00f9':
					case '\u00fa':  case '\u00fb':  case '\u00fc':  case '\u00fd':
					case '\u00fe':  case '\u00ff':
					{
						break;
					}
					default:
					{
						throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
					}
					}
					}
					break;
				}
				case '"':  case '\'':
				{
					mSTRING(false);
					break;
				}
				case '#':
				{
					mHEXNUM(false);
					break;
				}
				case '.':  case 'a':  case 'b':  case 'c':
				case 'd':  case 'e':  case 'f':  case 'g':
				case 'h':  case 'i':  case 'j':  case 'k':
				case 'l':  case 'm':  case 'n':  case 'o':
				case 'p':  case 'q':  case 'r':  case 's':
				case 't':  case 'u':  case 'v':  case 'w':
				case 'x':  case 'y':  case 'z':  case '\u00df':
				case '\u00e0':  case '\u00e1':  case '\u00e2':  case '\u00e3':
				case '\u00e4':  case '\u00e5':  case '\u00e6':  case '\u00e7':
				case '\u00e8':  case '\u00e9':  case '\u00ea':  case '\u00eb':
				case '\u00ec':  case '\u00ed':  case '\u00ee':  case '\u00ef':
				case '\u00f0':  case '\u00f1':  case '\u00f2':  case '\u00f3':
				case '\u00f4':  case '\u00f5':  case '\u00f6':  case '\u00f7':
				case '\u00f8':  case '\u00f9':  case '\u00fa':  case '\u00fb':
				case '\u00fc':  case '\u00fd':  case '\u00fe':  case '\u00ff':
				{
					mWORD(false);
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
				case '\t':  case '\n':  case '\r':  case ' ':
				{
					mWS(false);
					break;
				}
				case '.':  case '/':  case '>':  case 'a':
				case 'b':  case 'c':  case 'd':  case 'e':
				case 'f':  case 'g':  case 'h':  case 'i':
				case 'j':  case 'k':  case 'l':  case 'm':
				case 'n':  case 'o':  case 'p':  case 'q':
				case 'r':  case 's':  case 't':  case 'u':
				case 'v':  case 'w':  case 'x':  case 'y':
				case 'z':  case '\u00df':  case '\u00e0':  case '\u00e1':
				case '\u00e2':  case '\u00e3':  case '\u00e4':  case '\u00e5':
				case '\u00e6':  case '\u00e7':  case '\u00e8':  case '\u00e9':
				case '\u00ea':  case '\u00eb':  case '\u00ec':  case '\u00ed':
				case '\u00ee':  case '\u00ef':  case '\u00f0':  case '\u00f1':
				case '\u00f2':  case '\u00f3':  case '\u00f4':  case '\u00f5':
				case '\u00f6':  case '\u00f7':  case '\u00f8':  case '\u00f9':
				case '\u00fa':  case '\u00fb':  case '\u00fc':  case '\u00fd':
				case '\u00fe':  case '\u00ff':
				{
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
				break;
			}
			case '.':  case '/':  case '>':  case 'a':
			case 'b':  case 'c':  case 'd':  case 'e':
			case 'f':  case 'g':  case 'h':  case 'i':
			case 'j':  case 'k':  case 'l':  case 'm':
			case 'n':  case 'o':  case 'p':  case 'q':
			case 'r':  case 's':  case 't':  case 'u':
			case 'v':  case 'w':  case 'x':  case 'y':
			case 'z':  case '\u00df':  case '\u00e0':  case '\u00e1':
			case '\u00e2':  case '\u00e3':  case '\u00e4':  case '\u00e5':
			case '\u00e6':  case '\u00e7':  case '\u00e8':  case '\u00e9':
			case '\u00ea':  case '\u00eb':  case '\u00ec':  case '\u00ed':
			case '\u00ee':  case '\u00ef':  case '\u00f0':  case '\u00f1':
			case '\u00f2':  case '\u00f3':  case '\u00f4':  case '\u00f5':
			case '\u00f6':  case '\u00f7':  case '\u00f8':  case '\u00f9':
			case '\u00fa':  case '\u00fb':  case '\u00fc':  case '\u00fd':
			case '\u00fe':  case '\u00ff':
			{
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
			recover(ex,_tokenSet_8);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH1(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH1;
		int _saveIndex;
		
		try {      // for error handling
			match("</h1>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOH2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH2;
		int _saveIndex;
		
		try {      // for error handling
			match("<h2");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH2(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH2;
		int _saveIndex;
		
		try {      // for error handling
			match("</h2>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOH3(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH3;
		int _saveIndex;
		
		try {      // for error handling
			match("<h3");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH3(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH3;
		int _saveIndex;
		
		try {      // for error handling
			match("</h3>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOH4(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH4;
		int _saveIndex;
		
		try {      // for error handling
			match("<h4");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH4(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH4;
		int _saveIndex;
		
		try {      // for error handling
			match("</h4>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOH5(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH5;
		int _saveIndex;
		
		try {      // for error handling
			match("<h5");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH5(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH5;
		int _saveIndex;
		
		try {      // for error handling
			match("</h5>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOH6(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OH6;
		int _saveIndex;
		
		try {      // for error handling
			match("<h6");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCH6(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CH6;
		int _saveIndex;
		
		try {      // for error handling
			match("</h6>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOADDRESS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OADDRESS;
		int _saveIndex;
		
		try {      // for error handling
			match("<address");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mNEWLINE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = NEWLINE;
		int _saveIndex;
		
		try {      // for error handling
			{
			if ((LA(1)=='\r') && (LA(2)=='\n')) {
				_saveIndex=text.length();
				match("\r\n");
				text.setLength(_saveIndex);
				newline();
			}
			else if ((LA(1)=='\r') && (true)) {
				_saveIndex=text.length();
				match('\r');
				text.setLength(_saveIndex);
				newline();
			}
			else if ((LA(1)=='\n')) {
				_saveIndex=text.length();
				match('\n');
				text.setLength(_saveIndex);
				newline();
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			_ttype = Token.SKIP;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCADDRESS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CADDRESS;
		int _saveIndex;
		
		try {      // for error handling
			match("</address>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOPARA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OPARA;
		int _saveIndex;
		
		try {      // for error handling
			match("<p");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCPARA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CPARA;
		int _saveIndex;
		
		try {      // for error handling
			match("</p>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOULIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OULIST;
		int _saveIndex;
		
		try {      // for error handling
			match("<ul");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCULIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CULIST;
		int _saveIndex;
		
		try {      // for error handling
			match("</ul>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOOLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OOLIST;
		int _saveIndex;
		
		try {      // for error handling
			match("<ol");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCOLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COLIST;
		int _saveIndex;
		
		try {      // for error handling
			match("</ol>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOLITEM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OLITEM;
		int _saveIndex;
		
		try {      // for error handling
			match("<li");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCLITEM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CLITEM;
		int _saveIndex;
		
		try {      // for error handling
			match("</li>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODLIST;
		int _saveIndex;
		
		try {      // for error handling
			match("<dl");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCDLIST(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CDLIST;
		int _saveIndex;
		
		try {      // for error handling
			match("</dl>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODTERM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODTERM;
		int _saveIndex;
		
		try {      // for error handling
			match("<dt");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCDTERM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CDTERM;
		int _saveIndex;
		
		try {      // for error handling
			match("</dt>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODDEF(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODDEF;
		int _saveIndex;
		
		try {      // for error handling
			match("<dd");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCDDEF(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CDDEF;
		int _saveIndex;
		
		try {      // for error handling
			match("</dd>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODIR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODIR;
		int _saveIndex;
		
		try {      // for error handling
			match("<dir");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCDIR_OR_CDIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CDIR_OR_CDIV;
		int _saveIndex;
		
		try {      // for error handling
			match("</di");
			{
			switch ( LA(1)) {
			case 'r':
			{
				match('r');
				_ttype = JavadocTokenTypes.CDIR;
				break;
			}
			case 'v':
			{
				match('v');
				_ttype = JavadocTokenTypes.CDIV;
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODIV(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODIV;
		int _saveIndex;
		
		try {      // for error handling
			match("<div");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOCENTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCENTER;
		int _saveIndex;
		
		try {      // for error handling
			match("<center");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCCENTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CCENTER;
		int _saveIndex;
		
		try {      // for error handling
			match("</center>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOBQUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OBQUOTE;
		int _saveIndex;
		
		try {      // for error handling
			match("<blockquote");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCBQUOTE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CBQUOTE;
		int _saveIndex;
		
		try {      // for error handling
			match("</blockquote>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mHR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HR;
		int _saveIndex;
		
		try {      // for error handling
			match("<hr");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				{
				_loop698:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						mATTR(false);
					}
					else {
						break _loop698;
					}
					
				} while (true);
				}
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOTABLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OTABLE;
		int _saveIndex;
		
		try {      // for error handling
			match("<table");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				{
				_loop702:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						mATTR(false);
					}
					else {
						break _loop702;
					}
					
				} while (true);
				}
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCTABLE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CTABLE;
		int _saveIndex;
		
		try {      // for error handling
			match("</table>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOCAP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCAP;
		int _saveIndex;
		
		try {      // for error handling
			match("<caption");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				{
				_loop708:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						mATTR(false);
					}
					else {
						break _loop708;
					}
					
				} while (true);
				}
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCCAP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CCAP;
		int _saveIndex;
		
		try {      // for error handling
			match("</caption>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mO_TR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = O_TR;
		int _saveIndex;
		
		try {      // for error handling
			match("<tr");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				{
				_loop714:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						mATTR(false);
					}
					else {
						break _loop714;
					}
					
				} while (true);
				}
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mC_TR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = C_TR;
		int _saveIndex;
		
		try {      // for error handling
			match("</tr>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mO_TH_OR_TD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = O_TH_OR_TD;
		int _saveIndex;
		
		try {      // for error handling
			{
			if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='h')) {
				match("<th");
				_ttype = JavadocTokenTypes.OTH;
			}
			else if ((LA(1)=='<') && (LA(2)=='t') && (LA(3)=='d')) {
				match("<td");
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			_ttype = JavadocTokenTypes.OTD;
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				{
				_loop721:
				do {
					if ((_tokenSet_9.member(LA(1)))) {
						mATTR(false);
					}
					else {
						break _loop721;
					}
					
				} while (true);
				}
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mC_TH_OR_TD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = C_TH_OR_TD;
		int _saveIndex;
		
		try {      // for error handling
			if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='h')) {
				match("</th>");
				_ttype = JavadocTokenTypes.CTH;
			}
			else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='t') && (LA(4)=='d')) {
				match("</td>");
				_ttype = JavadocTokenTypes.CTD;
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOTTYPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OTTYPE;
		int _saveIndex;
		
		try {      // for error handling
			match("<tt");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCTTYPE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CTTYPE;
		int _saveIndex;
		
		try {      // for error handling
			match("</tt>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOCODE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCODE;
		int _saveIndex;
		
		try {      // for error handling
			match("<code");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCCODE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CCODE;
		int _saveIndex;
		
		try {      // for error handling
			match("</code>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOITALIC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OITALIC;
		int _saveIndex;
		
		try {      // for error handling
			match("<i");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCITALIC(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CITALIC;
		int _saveIndex;
		
		try {      // for error handling
			match("</i>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOBOLD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OBOLD;
		int _saveIndex;
		
		try {      // for error handling
			match("<b");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCBOLD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CBOLD;
		int _saveIndex;
		
		try {      // for error handling
			match("</b>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOUNDER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OUNDER;
		int _saveIndex;
		
		try {      // for error handling
			match("<u");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCUNDER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CUNDER;
		int _saveIndex;
		
		try {      // for error handling
			match("</u>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOSTRIKE_OR_OSTRONG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OSTRIKE_OR_OSTRONG;
		int _saveIndex;
		
		try {      // for error handling
			{
			if ((LA(1)=='<') && (LA(2)=='s') && (LA(3)=='t')) {
				match("<str");
				{
				switch ( LA(1)) {
				case 'i':
				{
					match("ike");
					_ttype = JavadocTokenTypes.OSTRIKE;
					break;
				}
				case 'o':
				{
					match("ong");
					_ttype = JavadocTokenTypes.OSTRONG;
					break;
				}
				default:
				{
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				}
				}
			}
			else if ((LA(1)=='<') && (LA(2)=='s') && (_tokenSet_0.member(LA(3)))) {
				match("<s");
				_ttype = JavadocTokenTypes.OSTRIKE; text.setLength(_begin); text.append("<strike");
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCSTRIKE_OR_CSTRONG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CSTRIKE_OR_CSTRONG;
		int _saveIndex;
		
		try {      // for error handling
			{
			if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='t')) {
				match("</st");
				{
				if ((LA(1)=='r') && (LA(2)=='i')) {
					match("rike");
					_ttype = JavadocTokenTypes.CSTRIKE;
				}
				else if ((LA(1)=='r') && (LA(2)=='o')) {
					match("rong");
					_ttype = JavadocTokenTypes.CSTRONG;
				}
				else {
					throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
				}
				
				}
			}
			else if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='s') && (LA(4)=='>')) {
				match("</s");
				_ttype = JavadocTokenTypes.CSTRIKE; text.setLength(_begin); text.append("</strike");
			}
			else {
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOBIG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OBIG;
		int _saveIndex;
		
		try {      // for error handling
			match("<big");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCBIG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CBIG;
		int _saveIndex;
		
		try {      // for error handling
			match("</big>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOSMALL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OSMALL;
		int _saveIndex;
		
		try {      // for error handling
			match("<small");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCSMALL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CSMALL;
		int _saveIndex;
		
		try {      // for error handling
			match("</small>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOSUB(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OSUB;
		int _saveIndex;
		
		try {      // for error handling
			match("<sub");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOSUP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OSUP;
		int _saveIndex;
		
		try {      // for error handling
			match("<sup");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCSUB_OR_CSUP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CSUB_OR_CSUP;
		int _saveIndex;
		
		try {      // for error handling
			match("</su");
			{
			switch ( LA(1)) {
			case 'b':
			{
				match('b');
				_ttype = JavadocTokenTypes.CSUB;
				break;
			}
			case 'p':
			{
				match('p');
				_ttype = JavadocTokenTypes.CSUP;
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOEM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OEM;
		int _saveIndex;
		
		try {      // for error handling
			match("<em");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCEM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CEM;
		int _saveIndex;
		
		try {      // for error handling
			match("</em>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mODFN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = ODFN;
		int _saveIndex;
		
		try {      // for error handling
			match("<dfn");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCDFN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CDFN;
		int _saveIndex;
		
		try {      // for error handling
			match("</dfn>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOSAMP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OSAMP;
		int _saveIndex;
		
		try {      // for error handling
			match("<samp");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCSAMP(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CSAMP;
		int _saveIndex;
		
		try {      // for error handling
			match("</samp>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOKBD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OKBD;
		int _saveIndex;
		
		try {      // for error handling
			match("<kbd");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCKBD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CKBD;
		int _saveIndex;
		
		try {      // for error handling
			match("</kbd>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOVAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OVAR;
		int _saveIndex;
		
		try {      // for error handling
			match("<var");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCVAR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CVAR;
		int _saveIndex;
		
		try {      // for error handling
			match("</var>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOCITE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OCITE;
		int _saveIndex;
		
		try {      // for error handling
			match("<cite");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCCITE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CCITE;
		int _saveIndex;
		
		try {      // for error handling
			match("</cite>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOACRO(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OACRO;
		int _saveIndex;
		
		try {      // for error handling
			match("<acronym");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCACRO(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CACRO;
		int _saveIndex;
		
		try {      // for error handling
			match("</acronym>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOANCHOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OANCHOR;
		int _saveIndex;
		
		try {      // for error handling
			match("<a");
			mWS(false);
			{
			int _cnt798=0;
			_loop798:
			do {
				if ((_tokenSet_9.member(LA(1)))) {
					mATTR(false);
				}
				else {
					if ( _cnt798>=1 ) { break _loop798; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt798++;
			} while (true);
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCANCHOR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CANCHOR;
		int _saveIndex;
		
		try {      // for error handling
			match("</a>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mIMG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = IMG;
		int _saveIndex;
		
		try {      // for error handling
			match("<img");
			mWS(false);
			{
			int _cnt803=0;
			_loop803:
			do {
				if ((_tokenSet_9.member(LA(1)))) {
					mATTR(false);
				}
				else {
					if ( _cnt803>=1 ) { break _loop803; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt803++;
			} while (true);
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mOFONT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = OFONT;
		int _saveIndex;
		
		try {      // for error handling
			match("<font");
			mWS(false);
			{
			int _cnt806=0;
			_loop806:
			do {
				if ((_tokenSet_9.member(LA(1)))) {
					mATTR(false);
				}
				else {
					if ( _cnt806>=1 ) { break _loop806; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt806++;
			} while (true);
			}
			match('>');
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mCFONT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = CFONT;
		int _saveIndex;
		
		try {      // for error handling
			match("</font>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mBR(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = BR;
		int _saveIndex;
		
		try {      // for error handling
			match("<br");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '/':  case '>':
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
			switch ( LA(1)) {
			case '/':
			{
				match('/');
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
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
			_ttype = Token.SKIP;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mAT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = AT;
		int _saveIndex;
		
		try {      // for error handling
			match('@');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_3);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mJAVADOC_OPEN(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVADOC_OPEN;
		int _saveIndex;
		
		try {      // for error handling
			match("/**");
			{
			if ((LA(1)=='\n'||LA(1)=='\r')) {
				_saveIndex=text.length();
				mNEWLINE(false);
				text.setLength(_saveIndex);
				skipLeadingSpaceAndAsterix(true);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mJAVADOC_CLOSE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = JAVADOC_CLOSE;
		int _saveIndex;
		
		try {      // for error handling
			match("*/");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
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
			recover(ex,_tokenSet_6);
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
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPRE(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PRE;
		int _saveIndex;
		
		try {      // for error handling
			match("<pre");
			{
			switch ( LA(1)) {
			case '\t':  case '\n':  case '\r':  case ' ':
			{
				mWS(false);
				mATTR(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
			{
			_loop822:
			do {
				// nongreedy exit test
				if ((LA(1)=='<') && (LA(2)=='/') && (LA(3)=='p') && (LA(4)=='r')) break _loop822;
				if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					match("\r\n");
					newline(false);
				}
				else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					match('\r');
					newline(false);
				}
				else if ((LA(1)=='\n') && ((LA(2) >= '\u0003' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					match('\n');
					newline(false);
				}
				else if (((LA(1) >= '\u0003' && LA(1) <= '\ufffe')) && ((LA(2) >= '\u0003' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					matchNot(EOF_CHAR);
				}
				else {
					break _loop822;
				}
				
			} while (true);
			}
			match("</pre>");
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTYPEDCLASS(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TYPEDCLASS;
		int _saveIndex;
		
		try {      // for error handling
			match("<");
			mLCLETTER(false);
			{
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				mDIGIT(false);
				break;
			}
			case '>':
			{
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			match('>');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mLCLETTER(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = LCLETTER;
		int _saveIndex;
		
		try {      // for error handling
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
			case '\u00df':  case '\u00e0':  case '\u00e1':  case '\u00e2':
			case '\u00e3':  case '\u00e4':  case '\u00e5':  case '\u00e6':
			case '\u00e7':  case '\u00e8':  case '\u00e9':  case '\u00ea':
			case '\u00eb':  case '\u00ec':  case '\u00ed':  case '\u00ee':
			case '\u00ef':  case '\u00f0':  case '\u00f1':  case '\u00f2':
			case '\u00f3':  case '\u00f4':  case '\u00f5':  case '\u00f6':
			case '\u00f7':  case '\u00f8':  case '\u00f9':  case '\u00fa':
			case '\u00fb':  case '\u00fc':  case '\u00fd':  case '\u00fe':
			case '\u00ff':
			{
				matchRange('\u00DF','\u00FF');
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
			recover(ex,_tokenSet_10);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = DIGIT;
		int _saveIndex;
		
		try {      // for error handling
			matchRange('0','9');
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_11);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mPCDATA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = PCDATA;
		int _saveIndex;
		
		try {      // for error handling
			{
			int _cnt828=0;
			_loop828:
			do {
				if ((LA(1)=='\r') && (LA(2)=='\n') && (true) && (true)) {
					match('\r');
					match('\n');
					
					// we remove the newline chars because we will calculate
					// newlines at printing time
					replaceNewline(2);
					
					if (_fileFormat == FileFormat.UNKNOWN)
					_fileFormat = FileFormat.DOS;
					
				}
				else if ((_tokenSet_3.member(LA(1))) && (LA(2)=='@')) {
					mLCLETTER(false);
					match('@');
					// Allow email address
					
				}
				else if ((LA(1)=='\r') && (true) && (true) && (true)) {
					match('\r');
					// we remove the newline chars because we will calculate
					// newlines at printing time
					replaceNewline(1);
					
					if (_fileFormat == FileFormat.UNKNOWN)
					_fileFormat = FileFormat.MAC;
					
				}
				else if ((LA(1)=='\n')) {
					match('\n');
					// we remove the newline chars because we will calculate
					// newlines at printing time
					replaceNewline(1);
					
					if (_fileFormat == FileFormat.UNKNOWN)
					_fileFormat = FileFormat.UNIX;
					
				}
				else if ((LA(1)=='\t') && (true) && (true) && (true)) {
					match('\t');
					replaceTab();
					
				}
				else if (((LA(1)=='/'))&&( LA(2) != '*' || (LA(2) == '*' && LA(3) != '*') )) {
					match('/');
				}
				else if ((_tokenSet_12.member(LA(1))) && (true) && (true) && (true)) {
					{
					match(_tokenSet_12);
					}
				}
				else {
					if ( _cnt828>=1 ) { break _loop828; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt828++;
			} while (true);
			}
			
			String t = new String(text.getBuffer(),_begin,text.length()-_begin);
			
			if (t != null)
			{
			t = t.trim();
			
			// remove trailing delimeter
			// XXX is this necessary?
			if (t.endsWith("*/"))
			{
			t = t.substring(0, t.length() - 2).trim();
			}
			
			// skip empty nodes
			if (t.length() == 0)
			{
			_token = null;
			_createToken = false;
			}
			}
			
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	public final void mTAG(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = TAG;
		int _saveIndex;
		
		try {      // for error handling
			mAT(false);
			{
			int _cnt831=0;
			_loop831:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					mLCLETTER(false);
				}
				else {
					if ( _cnt831>=1 ) { break _loop831; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt831++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mCOMMENT_DATA(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = COMMENT_DATA;
		int _saveIndex;
		
		try {      // for error handling
			{
			_loop835:
			do {
				if (((LA(1)=='-') && ((LA(2) >= '\u0003' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe')))&&(!(LA(2)=='-' && LA(3)=='>'))) {
					match('-');
				}
				else if ((LA(1)=='\r') && (LA(2)=='\n') && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					match("\r\n");
					newline();
				}
				else if ((LA(1)=='\r') && ((LA(2) >= '\u0003' && LA(2) <= '\ufffe')) && ((LA(3) >= '\u0003' && LA(3) <= '\ufffe')) && ((LA(4) >= '\u0003' && LA(4) <= '\ufffe'))) {
					match('\r');
					newline();
				}
				else if ((LA(1)=='\n')) {
					match('\n');
					newline();
				}
				else if ((_tokenSet_13.member(LA(1)))) {
					{
					match(_tokenSet_13);
					}
				}
				else {
					break _loop835;
				}
				
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_14);
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
		Token c=null;
		
		try {      // for error handling
			match("<!--");
			mCOMMENT_DATA(true);
			c=_returnToken;
			match("-->");
			{
			if ((_tokenSet_2.member(LA(1)))) {
				mWS(false);
			}
			else {
			}
			
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mWORD(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = WORD;
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
			case 'y':  case 'z':  case '\u00df':  case '\u00e0':
			case '\u00e1':  case '\u00e2':  case '\u00e3':  case '\u00e4':
			case '\u00e5':  case '\u00e6':  case '\u00e7':  case '\u00e8':
			case '\u00e9':  case '\u00ea':  case '\u00eb':  case '\u00ec':
			case '\u00ed':  case '\u00ee':  case '\u00ef':  case '\u00f0':
			case '\u00f1':  case '\u00f2':  case '\u00f3':  case '\u00f4':
			case '\u00f5':  case '\u00f6':  case '\u00f7':  case '\u00f8':
			case '\u00f9':  case '\u00fa':  case '\u00fb':  case '\u00fc':
			case '\u00fd':  case '\u00fe':  case '\u00ff':
			{
				mLCLETTER(false);
				break;
			}
			case '.':
			{
				match('.');
				break;
			}
			default:
			{
				throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());
			}
			}
			}
			{
			int _cnt854=0;
			_loop854:
			do {
				switch ( LA(1)) {
				case '0':  case '1':  case '2':  case '3':
				case '4':  case '5':  case '6':  case '7':
				case '8':  case '9':
				{
					mDIGIT(false);
					break;
				}
				case ':':
				{
					match(':');
					break;
				}
				case '@':
				{
					match('@');
					break;
				}
				default:
					if ((_tokenSet_3.member(LA(1))) && (_tokenSet_10.member(LA(2))) && (true) && (true)) {
						mLCLETTER(false);
					}
					else if ((LA(1)=='.') && (_tokenSet_10.member(LA(2))) && (true) && (true)) {
						match('.');
					}
					else if ((LA(1)=='/') && (_tokenSet_10.member(LA(2))) && (true) && (true)) {
						match('/');
					}
				else {
					if ( _cnt854>=1 ) { break _loop854; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				}
				_cnt854++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_15);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mINT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = INT;
		int _saveIndex;
		
		try {      // for error handling
			{
			int _cnt864=0;
			_loop864:
			do {
				if (((LA(1) >= '0' && LA(1) <= '9'))) {
					mDIGIT(false);
				}
				else {
					if ( _cnt864>=1 ) { break _loop864; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt864++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_16);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSTRING(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = STRING;
		int _saveIndex;
		
		try {      // for error handling
			switch ( LA(1)) {
			case '"':
			{
				match('"');
				{
				_loop857:
				do {
					if ((_tokenSet_17.member(LA(1)))) {
						matchNot('"');
					}
					else {
						break _loop857;
					}
					
				} while (true);
				}
				match('"');
				break;
			}
			case '\'':
			{
				match('\'');
				{
				_loop859:
				do {
					if ((_tokenSet_18.member(LA(1)))) {
						matchNot('\'');
					}
					else {
						break _loop859;
					}
					
				} while (true);
				}
				match('\'');
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
			recover(ex,_tokenSet_19);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXNUM(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXNUM;
		int _saveIndex;
		
		try {      // for error handling
			match('#');
			mHEXINT(false);
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mSPECIAL(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = SPECIAL;
		int _saveIndex;
		
		try {      // for error handling
			switch ( LA(1)) {
			case '<':
			{
				match('<');
				break;
			}
			case '~':
			{
				match('~');
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
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXINT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXINT;
		int _saveIndex;
		
		try {      // for error handling
			{
			int _cnt867=0;
			_loop867:
			do {
				if ((_tokenSet_20.member(LA(1))) && (_tokenSet_21.member(LA(2))) && (true) && (true)) {
					mHEXDIGIT(false);
				}
				else {
					if ( _cnt867>=1 ) { break _loop867; } else {throw new NoViableAltForCharException((char)LA(1), getFilename(), getLine(), getColumn());}
				}
				
				_cnt867++;
			} while (true);
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_19);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mHEXDIGIT(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = HEXDIGIT;
		int _saveIndex;
		
		try {      // for error handling
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
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
		catch (RecognitionException ex) {
			reportError(ex);
			recover(ex,_tokenSet_21);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	protected final void mEMAILSTART(boolean _createToken) throws RecognitionException, CharStreamException, TokenStreamException {
		int _ttype; Token _token=null; int _begin=text.length();
		_ttype = EMAILSTART;
		int _saveIndex;
		
		try {      // for error handling
			switch ( LA(1)) {
			case '0':  case '1':  case '2':  case '3':
			case '4':  case '5':  case '6':  case '7':
			case '8':  case '9':
			{
				matchRange('0','9');
				break;
			}
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
			case '\u00df':  case '\u00e0':  case '\u00e1':  case '\u00e2':
			case '\u00e3':  case '\u00e4':  case '\u00e5':  case '\u00e6':
			case '\u00e7':  case '\u00e8':  case '\u00e9':  case '\u00ea':
			case '\u00eb':  case '\u00ec':  case '\u00ed':  case '\u00ee':
			case '\u00ef':  case '\u00f0':  case '\u00f1':  case '\u00f2':
			case '\u00f3':  case '\u00f4':  case '\u00f5':  case '\u00f6':
			case '\u00f7':  case '\u00f8':  case '\u00f9':  case '\u00fa':
			case '\u00fb':  case '\u00fc':  case '\u00fd':  case '\u00fe':
			case '\u00ff':
			{
				matchRange('\u00DF','\u00FF');
				break;
			}
			case '_':
			{
				match('_');
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
			recover(ex,_tokenSet_6);
		}
		if ( _createToken && _token==null && _ttype!=Token.SKIP ) {
			_token = makeToken(_ttype);
			_token.setText(new String(text.getBuffer(), _begin, text.length()-_begin));
		}
		_returnToken = _token;
	}
	
	
	private static final long[] mk_tokenSet_0() {
		long[] data = new long[1025];
		data[0]=4611686022722364928L;
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = new long[1025];
		data[0]=4611686022722364928L;
		data[1]=4503599627370496L;
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = new long[1025];
		data[0]=4294977024L;
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = new long[1025];
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = new long[1025];
		data[0]=4899634919602388992L;
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = new long[2048];
		data[0]=-1152925902653358088L;
		data[1]=-2882303761517117442L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = new long[1025];
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = new long[1025];
		data[0]=7205724820716126208L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = new long[1025];
		data[0]=4611897124659920896L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = new long[1025];
		data[0]=70368744177664L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = new long[1025];
		data[0]=7493919415495304704L;
		data[1]=576460743713488897L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = new long[1025];
		data[0]=7493919552934258176L;
		data[1]=576460743713488897L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = new long[2048];
		data[0]=-1153066640141722632L;
		data[1]=-2882303761517117442L;
		for (int i = 2; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = new long[2048];
		data[0]=-35184372098056L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = new long[1025];
		data[0]=35184372088832L;
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = new long[1025];
		data[0]=6917740138168591872L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = new long[1025];
		data[0]=4611897266393851392L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = new long[2048];
		data[0]=-17179869192L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = new long[2048];
		data[0]=-549755813896L;
		for (int i = 1; i<=1022; i++) { data[i]=-1L; }
		data[1023]=9223372036854775807L;
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = new long[1025];
		data[0]=4611897128954897920L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = new long[1025];
		data[0]=287948901175001088L;
		data[1]=541165879296L;
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = new long[1025];
		data[0]=4899846030129899008L;
		data[1]=576460743713488896L;
		data[3]=-2147483648L;
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	
	}
