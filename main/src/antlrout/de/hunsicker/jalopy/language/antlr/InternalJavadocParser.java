// $ANTLR 2.7.2: "java.doc.g" -> "InternalJavadocParser.java"$

package de.hunsicker.jalopy.language.antlr;

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

import antlr.CommonAST;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import de.hunsicker.util.Lcs;
import java.io.FileInputStream;
import java.io.File;
import java.util.Collection;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;

/**
 * Parser for Javadoc comments.
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
 * @see de.hunsicker.jalopy.language.JavadocLexer
 * @see de.hunsicker.jalopy.language.Recognizer
 */
public abstract class InternalJavadocParser extends antlr.LLkParser       implements JavadocTokenTypes
 {

	/**
	 * Abstracted out DEV_1_5ing
     * @param ex
     */
    protected abstract void handleRecoverableError(RecognitionException ex) ;
    protected abstract void setTagType(AST tag, String type);

    /** Indicates an inline Javadoc tag. */
    protected final static String TYPE_INLINE = "TAG_INLINE_";

    /** Indicates a standard Javadoc tag. */
    protected final static String TYPE_STANDARD = "TAG_";
    
    /**
     * To satisfy antlr
     * 
     * @param ex
     * @param set
     */
	public void recover(RecognitionException ex, BitSet set) {
        
    }


protected InternalJavadocParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public InternalJavadocParser(TokenBuffer tokenBuf) {
  this(tokenBuf,1);
}

protected InternalJavadocParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public InternalJavadocParser(TokenStream lexer) {
  this(lexer,1);
}

public InternalJavadocParser(ParserSharedInputState state) {
  super(state,1);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void internalParse() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST internalParse_AST = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				
				// Uncomment to check the tag definition files for update
				//loadTagInfo(false);
				//_startLine = _lexer.getLine();
				//_startColumn = _lexer.getColumn();
				
			}
			match(JAVADOC_OPEN);
			{
			_loop452:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop452;
				}
				
			} while (true);
			}
			{
			_loop454:
			do {
				if ((LA(1)==TAG)) {
					standard_tag();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop454;
				}
				
			} while (true);
			}
			{
			_loop456:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop456;
				}
				
			} while (true);
			}
			match(JAVADOC_CLOSE);
			internalParse_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		returnAST = internalParse_AST;
	}
	
	public final void body_content() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST body_content_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case HR:
			case OH1:
			case OH2:
			case OH3:
			case OH4:
			case OH5:
			case OH6:
			case OADDRESS:
			case OPARA:
			case OULIST:
			case OOLIST:
			case ODLIST:
			case ODIV:
			case OCENTER:
			case OBQUOTE:
			case PRE:
			case OTABLE:
			{
				body_tag();
				astFactory.addASTChild(currentAST, returnAST);
				body_content_AST = (AST)currentAST.root;
				break;
			}
			case LCURLY:
			case RCURLY:
			case IMG:
			case BR:
			case COMMENT:
			case PCDATA:
			case OTTYPE:
			case OITALIC:
			case OBOLD:
			case OCODE:
			case OUNDER:
			case OSTRIKE:
			case OBIG:
			case OSMALL:
			case OSUB:
			case OSUP:
			case OEM:
			case OSTRONG:
			case ODFN:
			case OSAMP:
			case OKBD:
			case OVAR:
			case OCITE:
			case OACRO:
			case TYPEDCLASS:
			case OANCHOR:
			case OFONT:
			{
				text();
				astFactory.addASTChild(currentAST, returnAST);
				body_content_AST = (AST)currentAST.root;
				break;
			}
			case AT:
			{
				AST tmp3_AST = null;
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
				match(AT);
				body_content_AST = (AST)currentAST.root;
				break;
			}
			case TAG_OR_AT:
			{
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
				match(TAG_OR_AT);
				body_content_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = body_content_AST;
	}
	
	public final void standard_tag() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST standard_tag_AST = null;
		Token  tag = null;
		AST tag_AST = null;
		
		try {      // for error handling
			{
			tag = LT(1);
			tag_AST = astFactory.create(tag);
			astFactory.makeASTRoot(currentAST, tag_AST);
			match(TAG);
			if ( inputState.guessing==0 ) {
				setTagType(tag_AST, TYPE_STANDARD);
			}
			{
			_loop625:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_4.member(LA(1)))) {
					block();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop625;
				}
				
			} while (true);
			}
			}
			standard_tag_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_5);
			} else {
			  throw ex;
			}
		}
		returnAST = standard_tag_AST;
	}
	
	public final void body_tag() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST body_tag_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case HR:
			case OPARA:
			case OULIST:
			case OOLIST:
			case ODLIST:
			case ODIV:
			case OCENTER:
			case OBQUOTE:
			case PRE:
			case OTABLE:
			{
				block();
				astFactory.addASTChild(currentAST, returnAST);
				body_tag_AST = (AST)currentAST.root;
				break;
			}
			case OADDRESS:
			{
				address();
				astFactory.addASTChild(currentAST, returnAST);
				body_tag_AST = (AST)currentAST.root;
				break;
			}
			case OH1:
			case OH2:
			case OH3:
			case OH4:
			case OH5:
			case OH6:
			{
				heading();
				astFactory.addASTChild(currentAST, returnAST);
				body_tag_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = body_tag_AST;
	}
	
	public final void text() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST text_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case RCURLY:
			{
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(RCURLY);
				text_AST = (AST)currentAST.root;
				break;
			}
			case IMG:
			case BR:
			case OTTYPE:
			case OITALIC:
			case OBOLD:
			case OCODE:
			case OUNDER:
			case OSTRIKE:
			case OBIG:
			case OSMALL:
			case OSUB:
			case OSUP:
			case OEM:
			case OSTRONG:
			case ODFN:
			case OSAMP:
			case OKBD:
			case OVAR:
			case OCITE:
			case OACRO:
			case TYPEDCLASS:
			case OANCHOR:
			case OFONT:
			{
				text_tag();
				astFactory.addASTChild(currentAST, returnAST);
				text_AST = (AST)currentAST.root;
				break;
			}
			case COMMENT:
			{
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(COMMENT);
				text_AST = (AST)currentAST.root;
				break;
			}
			case PCDATA:
			{
				AST tmp7_AST = null;
				tmp7_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(PCDATA);
				text_AST = (AST)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched467 = false;
				if (((LA(1)==LCURLY))) {
					int _m467 = mark();
					synPredMatched467 = true;
					inputState.guessing++;
					try {
						{
						match(LCURLY);
						match(TAG);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched467 = false;
					}
					rewind(_m467);
					inputState.guessing--;
				}
				if ( synPredMatched467 ) {
					inline_tag();
					astFactory.addASTChild(currentAST, returnAST);
					text_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==LCURLY)) {
					AST tmp8_AST = null;
					tmp8_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp8_AST);
					match(LCURLY);
					text_AST = (AST)currentAST.root;
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = text_AST;
	}
	
	public final void block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST block_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OPARA:
			{
				paragraph();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case OULIST:
			case OOLIST:
			case ODLIST:
			{
				list();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case PRE:
			{
				preformatted();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case ODIV:
			{
				div();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case OCENTER:
			{
				center();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case OBQUOTE:
			{
				blockquote();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			case HR:
			{
				AST tmp9_AST = null;
				tmp9_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(HR);
				block_AST = (AST)currentAST.root;
				break;
			}
			case OTABLE:
			{
				table();
				astFactory.addASTChild(currentAST, returnAST);
				block_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_7);
			} else {
			  throw ex;
			}
		}
		returnAST = block_AST;
	}
	
	public final void address() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST address_AST = null;
		
		try {      // for error handling
			AST tmp10_AST = null;
			tmp10_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp10_AST);
			match(OADDRESS);
			{
			_loop479:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp11_AST = null;
					tmp11_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp11_AST);
					match(PCDATA);
				}
				else {
					break _loop479;
				}
				
			} while (true);
			}
			AST tmp12_AST = null;
			tmp12_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp12_AST);
			match(CADDRESS);
			address_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = address_AST;
	}
	
	public final void heading() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST heading_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OH1:
			{
				h1();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			case OH2:
			{
				h2();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			case OH3:
			{
				h3();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			case OH4:
			{
				h4();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			case OH5:
			{
				h5();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			case OH6:
			{
				h6();
				astFactory.addASTChild(currentAST, returnAST);
				heading_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = heading_AST;
	}
	
	public final void h1() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h1_AST = null;
		
		try {      // for error handling
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp13_AST);
			match(OH1);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp14_AST);
			match(CH1);
			h1_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h1_AST;
	}
	
	public final void h2() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h2_AST = null;
		
		try {      // for error handling
			AST tmp15_AST = null;
			tmp15_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp15_AST);
			match(OH2);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp16_AST = null;
			tmp16_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp16_AST);
			match(CH2);
			h2_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h2_AST;
	}
	
	public final void h3() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h3_AST = null;
		
		try {      // for error handling
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp17_AST);
			match(OH3);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp18_AST = null;
			tmp18_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp18_AST);
			match(CH3);
			h3_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h3_AST;
	}
	
	public final void h4() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h4_AST = null;
		
		try {      // for error handling
			AST tmp19_AST = null;
			tmp19_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp19_AST);
			match(OH4);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp20_AST = null;
			tmp20_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp20_AST);
			match(CH4);
			h4_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h4_AST;
	}
	
	public final void h5() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h5_AST = null;
		
		try {      // for error handling
			AST tmp21_AST = null;
			tmp21_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp21_AST);
			match(OH5);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp22_AST = null;
			tmp22_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp22_AST);
			match(CH5);
			h5_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h5_AST;
	}
	
	public final void h6() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST h6_AST = null;
		
		try {      // for error handling
			AST tmp23_AST = null;
			tmp23_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp23_AST);
			match(OH6);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp24_AST = null;
			tmp24_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp24_AST);
			match(CH6);
			h6_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_2);
			} else {
			  throw ex;
			}
		}
		returnAST = h6_AST;
	}
	
	public final void paragraph() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST paragraph_AST = null;
		
		try {      // for error handling
			AST tmp25_AST = null;
			tmp25_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp25_AST);
			match(OPARA);
			{
			_loop482:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_8.member(LA(1)))) {
					list();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==ODIV)) {
					div();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==OCENTER)) {
					center();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==OBQUOTE)) {
					blockquote();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==OTABLE)) {
					table();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((LA(1)==PRE)) {
					preformatted();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop482;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CPARA:
			{
				match(CPARA);
				break;
			}
			case EOF:
			case LCURLY:
			case RCURLY:
			case OTH:
			case CTH:
			case OTD:
			case CTD:
			case JAVADOC_CLOSE:
			case AT:
			case TAG_OR_AT:
			case HR:
			case IMG:
			case BR:
			case TAG:
			case COMMENT:
			case PCDATA:
			case OH1:
			case CH1:
			case OH2:
			case CH2:
			case OH3:
			case CH3:
			case OH4:
			case CH4:
			case OH5:
			case CH5:
			case OH6:
			case CH6:
			case OADDRESS:
			case OPARA:
			case OULIST:
			case CULIST:
			case OOLIST:
			case COLIST:
			case ODLIST:
			case CDLIST:
			case OLITEM:
			case CLITEM:
			case ODTERM:
			case ODDEF:
			case CDDEF:
			case CDIR:
			case ODIV:
			case CDIV:
			case OCENTER:
			case CCENTER:
			case OBQUOTE:
			case CBQUOTE:
			case PRE:
			case OTABLE:
			case CTABLE:
			case O_TR:
			case C_TR:
			case OTTYPE:
			case OITALIC:
			case OBOLD:
			case OCODE:
			case OUNDER:
			case OSTRIKE:
			case OBIG:
			case OSMALL:
			case OSUB:
			case OSUP:
			case OEM:
			case OSTRONG:
			case ODFN:
			case OSAMP:
			case OKBD:
			case OVAR:
			case OCITE:
			case OACRO:
			case TYPEDCLASS:
			case OANCHOR:
			case OFONT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			paragraph_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				
				handleRecoverableError(ex);
				
			} else {
				throw ex;
			}
		}
		returnAST = paragraph_AST;
	}
	
	public final void list() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST list_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OULIST:
			{
				unordered_list();
				astFactory.addASTChild(currentAST, returnAST);
				list_AST = (AST)currentAST.root;
				break;
			}
			case OOLIST:
			{
				ordered_list();
				astFactory.addASTChild(currentAST, returnAST);
				list_AST = (AST)currentAST.root;
				break;
			}
			case ODLIST:
			{
				def_list();
				astFactory.addASTChild(currentAST, returnAST);
				list_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = list_AST;
	}
	
	public final void preformatted() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST preformatted_AST = null;
		
		try {      // for error handling
			AST tmp27_AST = null;
			tmp27_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp27_AST);
			match(PRE);
			preformatted_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = preformatted_AST;
	}
	
	public final void div() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST div_AST = null;
		
		try {      // for error handling
			AST tmp28_AST = null;
			tmp28_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp28_AST);
			match(ODIV);
			{
			_loop525:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop525;
				}
				
			} while (true);
			}
			AST tmp29_AST = null;
			tmp29_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp29_AST);
			match(CDIV);
			div_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = div_AST;
	}
	
	public final void center() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST center_AST = null;
		
		try {      // for error handling
			AST tmp30_AST = null;
			tmp30_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp30_AST);
			match(OCENTER);
			{
			_loop528:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop528;
				}
				
			} while (true);
			}
			AST tmp31_AST = null;
			tmp31_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp31_AST);
			match(CCENTER);
			center_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = center_AST;
	}
	
	public final void blockquote() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST blockquote_AST = null;
		
		try {      // for error handling
			AST tmp32_AST = null;
			tmp32_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp32_AST);
			match(OBQUOTE);
			{
			_loop531:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop531;
				}
				
			} while (true);
			}
			AST tmp33_AST = null;
			tmp33_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp33_AST);
			match(CBQUOTE);
			blockquote_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = blockquote_AST;
	}
	
	public final void table() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST table_AST = null;
		
		try {      // for error handling
			AST tmp34_AST = null;
			tmp34_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp34_AST);
			match(OTABLE);
			{
			switch ( LA(1)) {
			case OCAP:
			{
				caption();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case PCDATA:
			case O_TR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop536:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp35_AST = null;
					tmp35_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp35_AST);
					match(PCDATA);
				}
				else {
					break _loop536;
				}
				
			} while (true);
			}
			{
			int _cnt538=0;
			_loop538:
			do {
				if ((LA(1)==O_TR)) {
					tr();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt538>=1 ) { break _loop538; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt538++;
			} while (true);
			}
			match(CTABLE);
			table_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = table_AST;
	}
	
	public final void font() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST font_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OTTYPE:
			{
				teletype();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OITALIC:
			{
				italic();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OBOLD:
			{
				bold();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OUNDER:
			{
				underline();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OSTRIKE:
			{
				strike();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OBIG:
			{
				big();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OSMALL:
			{
				small();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OSUB:
			{
				subscript();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			case OSUP:
			{
				superscript();
				astFactory.addASTChild(currentAST, returnAST);
				font_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = font_AST;
	}
	
	public final void teletype() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST teletype_AST = null;
		
		try {      // for error handling
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp37_AST);
			match(OTTYPE);
			{
			int _cnt562=0;
			_loop562:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt562>=1 ) { break _loop562; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt562++;
			} while (true);
			}
			AST tmp38_AST = null;
			tmp38_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp38_AST);
			match(CTTYPE);
			teletype_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = teletype_AST;
	}
	
	public final void italic() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST italic_AST = null;
		
		try {      // for error handling
			AST tmp39_AST = null;
			tmp39_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp39_AST);
			match(OITALIC);
			{
			int _cnt565=0;
			_loop565:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt565>=1 ) { break _loop565; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt565++;
			} while (true);
			}
			AST tmp40_AST = null;
			tmp40_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp40_AST);
			match(CITALIC);
			italic_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = italic_AST;
	}
	
	public final void bold() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST bold_AST = null;
		
		try {      // for error handling
			AST tmp41_AST = null;
			tmp41_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp41_AST);
			match(OBOLD);
			{
			int _cnt568=0;
			_loop568:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt568>=1 ) { break _loop568; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt568++;
			} while (true);
			}
			AST tmp42_AST = null;
			tmp42_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp42_AST);
			match(CBOLD);
			bold_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = bold_AST;
	}
	
	public final void underline() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST underline_AST = null;
		
		try {      // for error handling
			AST tmp43_AST = null;
			tmp43_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp43_AST);
			match(OUNDER);
			{
			int _cnt574=0;
			_loop574:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt574>=1 ) { break _loop574; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt574++;
			} while (true);
			}
			AST tmp44_AST = null;
			tmp44_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp44_AST);
			match(CUNDER);
			underline_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = underline_AST;
	}
	
	public final void strike() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST strike_AST = null;
		
		try {      // for error handling
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp45_AST);
			match(OSTRIKE);
			{
			int _cnt577=0;
			_loop577:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt577>=1 ) { break _loop577; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt577++;
			} while (true);
			}
			AST tmp46_AST = null;
			tmp46_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp46_AST);
			match(CSTRIKE);
			strike_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = strike_AST;
	}
	
	public final void big() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST big_AST = null;
		
		try {      // for error handling
			AST tmp47_AST = null;
			tmp47_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp47_AST);
			match(OBIG);
			{
			int _cnt580=0;
			_loop580:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt580>=1 ) { break _loop580; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt580++;
			} while (true);
			}
			AST tmp48_AST = null;
			tmp48_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp48_AST);
			match(CBIG);
			big_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = big_AST;
	}
	
	public final void small() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST small_AST = null;
		
		try {      // for error handling
			AST tmp49_AST = null;
			tmp49_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp49_AST);
			match(OSMALL);
			{
			int _cnt583=0;
			_loop583:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt583>=1 ) { break _loop583; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt583++;
			} while (true);
			}
			AST tmp50_AST = null;
			tmp50_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp50_AST);
			match(CSMALL);
			small_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = small_AST;
	}
	
	public final void subscript() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST subscript_AST = null;
		
		try {      // for error handling
			AST tmp51_AST = null;
			tmp51_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp51_AST);
			match(OSUB);
			{
			int _cnt586=0;
			_loop586:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt586>=1 ) { break _loop586; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt586++;
			} while (true);
			}
			AST tmp52_AST = null;
			tmp52_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp52_AST);
			match(CSUB);
			subscript_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = subscript_AST;
	}
	
	public final void superscript() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST superscript_AST = null;
		
		try {      // for error handling
			AST tmp53_AST = null;
			tmp53_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp53_AST);
			match(OSUP);
			{
			int _cnt589=0;
			_loop589:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt589>=1 ) { break _loop589; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt589++;
			} while (true);
			}
			AST tmp54_AST = null;
			tmp54_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp54_AST);
			match(CSUP);
			superscript_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = superscript_AST;
	}
	
	public final void phrase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST phrase_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OEM:
			{
				emphasize();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OSTRONG:
			{
				strong();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case ODFN:
			{
				definition();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OCODE:
			{
				code();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OSAMP:
			{
				sample();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OKBD:
			{
				keyboard();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OVAR:
			{
				variable();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OCITE:
			{
				citation();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			case OACRO:
			{
				acronym();
				astFactory.addASTChild(currentAST, returnAST);
				phrase_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = phrase_AST;
	}
	
	public final void emphasize() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST emphasize_AST = null;
		
		try {      // for error handling
			AST tmp55_AST = null;
			tmp55_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp55_AST);
			match(OEM);
			{
			int _cnt592=0;
			_loop592:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt592>=1 ) { break _loop592; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt592++;
			} while (true);
			}
			AST tmp56_AST = null;
			tmp56_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp56_AST);
			match(CEM);
			emphasize_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = emphasize_AST;
	}
	
	public final void strong() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST strong_AST = null;
		
		try {      // for error handling
			AST tmp57_AST = null;
			tmp57_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp57_AST);
			match(OSTRONG);
			{
			int _cnt595=0;
			_loop595:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt595>=1 ) { break _loop595; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt595++;
			} while (true);
			}
			AST tmp58_AST = null;
			tmp58_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp58_AST);
			match(CSTRONG);
			strong_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = strong_AST;
	}
	
	public final void definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST definition_AST = null;
		
		try {      // for error handling
			AST tmp59_AST = null;
			tmp59_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp59_AST);
			match(ODFN);
			{
			int _cnt598=0;
			_loop598:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt598>=1 ) { break _loop598; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt598++;
			} while (true);
			}
			AST tmp60_AST = null;
			tmp60_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp60_AST);
			match(CDFN);
			definition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = definition_AST;
	}
	
	public final void code() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST code_AST = null;
		
		try {      // for error handling
			AST tmp61_AST = null;
			tmp61_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp61_AST);
			match(OCODE);
			{
			int _cnt571=0;
			_loop571:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt571>=1 ) { break _loop571; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt571++;
			} while (true);
			}
			AST tmp62_AST = null;
			tmp62_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp62_AST);
			match(CCODE);
			code_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = code_AST;
	}
	
	public final void sample() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST sample_AST = null;
		
		try {      // for error handling
			AST tmp63_AST = null;
			tmp63_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp63_AST);
			match(OSAMP);
			{
			int _cnt601=0;
			_loop601:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt601>=1 ) { break _loop601; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt601++;
			} while (true);
			}
			AST tmp64_AST = null;
			tmp64_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp64_AST);
			match(CSAMP);
			sample_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = sample_AST;
	}
	
	public final void keyboard() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST keyboard_AST = null;
		
		try {      // for error handling
			AST tmp65_AST = null;
			tmp65_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp65_AST);
			match(OKBD);
			{
			int _cnt604=0;
			_loop604:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt604>=1 ) { break _loop604; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt604++;
			} while (true);
			}
			AST tmp66_AST = null;
			tmp66_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp66_AST);
			match(CKBD);
			keyboard_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = keyboard_AST;
	}
	
	public final void variable() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST variable_AST = null;
		
		try {      // for error handling
			AST tmp67_AST = null;
			tmp67_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp67_AST);
			match(OVAR);
			{
			int _cnt607=0;
			_loop607:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt607>=1 ) { break _loop607; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt607++;
			} while (true);
			}
			AST tmp68_AST = null;
			tmp68_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp68_AST);
			match(CVAR);
			variable_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = variable_AST;
	}
	
	public final void citation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST citation_AST = null;
		
		try {      // for error handling
			AST tmp69_AST = null;
			tmp69_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp69_AST);
			match(OCITE);
			{
			int _cnt610=0;
			_loop610:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt610>=1 ) { break _loop610; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt610++;
			} while (true);
			}
			AST tmp70_AST = null;
			tmp70_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp70_AST);
			match(CCITE);
			citation_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = citation_AST;
	}
	
	public final void acronym() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST acronym_AST = null;
		
		try {      // for error handling
			AST tmp71_AST = null;
			tmp71_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp71_AST);
			match(OACRO);
			{
			int _cnt613=0;
			_loop613:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt613>=1 ) { break _loop613; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt613++;
			} while (true);
			}
			AST tmp72_AST = null;
			tmp72_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp72_AST);
			match(CACRO);
			acronym_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_10);
			} else {
			  throw ex;
			}
		}
		returnAST = acronym_AST;
	}
	
	public final void special() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST special_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OANCHOR:
			{
				anchor();
				astFactory.addASTChild(currentAST, returnAST);
				special_AST = (AST)currentAST.root;
				break;
			}
			case IMG:
			{
				AST tmp73_AST = null;
				tmp73_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
				match(IMG);
				special_AST = (AST)currentAST.root;
				break;
			}
			case OFONT:
			{
				font_dfn();
				astFactory.addASTChild(currentAST, returnAST);
				special_AST = (AST)currentAST.root;
				break;
			}
			case BR:
			{
				AST tmp74_AST = null;
				tmp74_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
				match(BR);
				special_AST = (AST)currentAST.root;
				break;
			}
			case TYPEDCLASS:
			{
				typedclass();
				astFactory.addASTChild(currentAST, returnAST);
				special_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = special_AST;
	}
	
	public final void anchor() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST anchor_AST = null;
		
		try {      // for error handling
			AST tmp75_AST = null;
			tmp75_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp75_AST);
			match(OANCHOR);
			{
			_loop617:
			do {
				if ((_tokenSet_11.member(LA(1)))) {
					anchor_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop617;
				}
				
			} while (true);
			}
			AST tmp76_AST = null;
			tmp76_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp76_AST);
			match(CANCHOR);
			anchor_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = anchor_AST;
	}
	
	public final void font_dfn() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST font_dfn_AST = null;
		
		try {      // for error handling
			AST tmp77_AST = null;
			tmp77_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp77_AST);
			match(OFONT);
			{
			_loop621:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop621;
				}
				
			} while (true);
			}
			AST tmp78_AST = null;
			tmp78_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp78_AST);
			match(CFONT);
			font_dfn_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = font_dfn_AST;
	}
	
	public final void typedclass() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST typedclass_AST = null;
		
		try {      // for error handling
			AST tmp79_AST = null;
			tmp79_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp79_AST);
			match(TYPEDCLASS);
			typedclass_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = typedclass_AST;
	}
	
	public final void text_tag() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST text_tag_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OTTYPE:
			case OITALIC:
			case OBOLD:
			case OUNDER:
			case OSTRIKE:
			case OBIG:
			case OSMALL:
			case OSUB:
			case OSUP:
			{
				font();
				astFactory.addASTChild(currentAST, returnAST);
				text_tag_AST = (AST)currentAST.root;
				break;
			}
			case OCODE:
			case OEM:
			case OSTRONG:
			case ODFN:
			case OSAMP:
			case OKBD:
			case OVAR:
			case OCITE:
			case OACRO:
			{
				phrase();
				astFactory.addASTChild(currentAST, returnAST);
				text_tag_AST = (AST)currentAST.root;
				break;
			}
			case IMG:
			case BR:
			case TYPEDCLASS:
			case OANCHOR:
			case OFONT:
			{
				special();
				astFactory.addASTChild(currentAST, returnAST);
				text_tag_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = text_tag_AST;
	}
	
	public final void inline_tag() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inline_tag_AST = null;
		Token  tag = null;
		AST tag_AST = null;
		
		try {      // for error handling
			match(LCURLY);
			tag = LT(1);
			tag_AST = astFactory.create(tag);
			astFactory.makeASTRoot(currentAST, tag_AST);
			match(TAG);
			if ( inputState.guessing==0 ) {
				setTagType(tag_AST, TYPE_INLINE);
			}
			{
			_loop628:
			do {
				if ((_tokenSet_12.member(LA(1)))) {
					AST tmp81_AST = null;
					tmp81_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp81_AST);
					matchNot(RCURLY);
				}
				else {
					break _loop628;
				}
				
			} while (true);
			}
			match(RCURLY);
			inline_tag_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_6);
			} else {
			  throw ex;
			}
		}
		returnAST = inline_tag_AST;
	}
	
	public final void heading_content() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST heading_content_AST = null;
		
		try {      // for error handling
			{
			_loop476:
			do {
				switch ( LA(1)) {
				case HR:
				case OPARA:
				case OULIST:
				case OOLIST:
				case ODLIST:
				case ODIV:
				case OCENTER:
				case OBQUOTE:
				case PRE:
				case OTABLE:
				{
					block();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LCURLY:
				case RCURLY:
				case IMG:
				case BR:
				case COMMENT:
				case PCDATA:
				case OTTYPE:
				case OITALIC:
				case OBOLD:
				case OCODE:
				case OUNDER:
				case OSTRIKE:
				case OBIG:
				case OSMALL:
				case OSUB:
				case OSUP:
				case OEM:
				case OSTRONG:
				case ODFN:
				case OSAMP:
				case OKBD:
				case OVAR:
				case OCITE:
				case OACRO:
				case TYPEDCLASS:
				case OANCHOR:
				case OFONT:
				{
					text();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					break _loop476;
				}
				}
			} while (true);
			}
			heading_content_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = heading_content_AST;
	}
	
	public final void unordered_list() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unordered_list_AST = null;
		
		try {      // for error handling
			AST tmp83_AST = null;
			tmp83_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp83_AST);
			match(OULIST);
			{
			_loop487:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp84_AST = null;
					tmp84_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp84_AST);
					match(PCDATA);
				}
				else {
					break _loop487;
				}
				
			} while (true);
			}
			{
			int _cnt489=0;
			_loop489:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt489>=1 ) { break _loop489; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt489++;
			} while (true);
			}
			AST tmp85_AST = null;
			tmp85_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp85_AST);
			match(CULIST);
			unordered_list_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = unordered_list_AST;
	}
	
	public final void ordered_list() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST ordered_list_AST = null;
		
		try {      // for error handling
			AST tmp86_AST = null;
			tmp86_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp86_AST);
			match(OOLIST);
			{
			_loop492:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp87_AST = null;
					tmp87_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
					match(PCDATA);
				}
				else {
					break _loop492;
				}
				
			} while (true);
			}
			{
			int _cnt494=0;
			_loop494:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt494>=1 ) { break _loop494; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt494++;
			} while (true);
			}
			AST tmp88_AST = null;
			tmp88_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp88_AST);
			match(COLIST);
			ordered_list_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = ordered_list_AST;
	}
	
	public final void def_list() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST def_list_AST = null;
		
		try {      // for error handling
			AST tmp89_AST = null;
			tmp89_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp89_AST);
			match(ODLIST);
			{
			_loop497:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp90_AST = null;
					tmp90_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp90_AST);
					match(PCDATA);
				}
				else {
					break _loop497;
				}
				
			} while (true);
			}
			{
			int _cnt499=0;
			_loop499:
			do {
				if ((LA(1)==ODTERM||LA(1)==ODDEF)) {
					def_list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt499>=1 ) { break _loop499; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt499++;
			} while (true);
			}
			AST tmp91_AST = null;
			tmp91_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp91_AST);
			match(CDLIST);
			def_list_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_9);
			} else {
			  throw ex;
			}
		}
		returnAST = def_list_AST;
	}
	
	public final void list_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST list_item_AST = null;
		
		try {      // for error handling
			AST tmp92_AST = null;
			tmp92_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp92_AST);
			match(OLITEM);
			{
			int _cnt502=0;
			_loop502:
			do {
				switch ( LA(1)) {
				case LCURLY:
				case RCURLY:
				case IMG:
				case BR:
				case COMMENT:
				case PCDATA:
				case OTTYPE:
				case OITALIC:
				case OBOLD:
				case OCODE:
				case OUNDER:
				case OSTRIKE:
				case OBIG:
				case OSMALL:
				case OSUB:
				case OSUP:
				case OEM:
				case OSTRONG:
				case ODFN:
				case OSAMP:
				case OKBD:
				case OVAR:
				case OCITE:
				case OACRO:
				case TYPEDCLASS:
				case OANCHOR:
				case OFONT:
				{
					text();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case HR:
				case OPARA:
				case OULIST:
				case OOLIST:
				case ODLIST:
				case ODIV:
				case OCENTER:
				case OBQUOTE:
				case PRE:
				case OTABLE:
				{
					block();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					if ( _cnt502>=1 ) { break _loop502; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt502++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CLITEM:
			{
				AST tmp93_AST = null;
				tmp93_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp93_AST);
				match(CLITEM);
				{
				_loop505:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp94_AST = null;
						tmp94_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp94_AST);
						match(PCDATA);
					}
					else {
						break _loop505;
					}
					
				} while (true);
				}
				break;
			}
			case EOF:
			case CULIST:
			case COLIST:
			case OLITEM:
			case CDIR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			list_item_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_14);
			} else {
			  throw ex;
			}
		}
		returnAST = list_item_AST;
	}
	
	public final void def_list_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST def_list_item_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case ODTERM:
			{
				dt();
				astFactory.addASTChild(currentAST, returnAST);
				def_list_item_AST = (AST)currentAST.root;
				break;
			}
			case ODDEF:
			{
				dd();
				astFactory.addASTChild(currentAST, returnAST);
				def_list_item_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
		returnAST = def_list_item_AST;
	}
	
	public final void dt() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dt_AST = null;
		
		try {      // for error handling
			AST tmp95_AST = null;
			tmp95_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp95_AST);
			match(ODTERM);
			{
			int _cnt509=0;
			_loop509:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt509>=1 ) { break _loop509; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt509++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDTERM:
			{
				AST tmp96_AST = null;
				tmp96_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp96_AST);
				match(CDTERM);
				{
				_loop512:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp97_AST = null;
						tmp97_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp97_AST);
						match(PCDATA);
					}
					else {
						break _loop512;
					}
					
				} while (true);
				}
				break;
			}
			case CDLIST:
			case ODTERM:
			case ODDEF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dt_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
		returnAST = dt_AST;
	}
	
	public final void dd() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dd_AST = null;
		
		try {      // for error handling
			AST tmp98_AST = null;
			tmp98_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp98_AST);
			match(ODDEF);
			{
			int _cnt515=0;
			_loop515:
			do {
				switch ( LA(1)) {
				case LCURLY:
				case RCURLY:
				case IMG:
				case BR:
				case COMMENT:
				case PCDATA:
				case OTTYPE:
				case OITALIC:
				case OBOLD:
				case OCODE:
				case OUNDER:
				case OSTRIKE:
				case OBIG:
				case OSMALL:
				case OSUB:
				case OSUP:
				case OEM:
				case OSTRONG:
				case ODFN:
				case OSAMP:
				case OKBD:
				case OVAR:
				case OCITE:
				case OACRO:
				case TYPEDCLASS:
				case OANCHOR:
				case OFONT:
				{
					text();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case HR:
				case OPARA:
				case OULIST:
				case OOLIST:
				case ODLIST:
				case ODIV:
				case OCENTER:
				case OBQUOTE:
				case PRE:
				case OTABLE:
				{
					block();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					if ( _cnt515>=1 ) { break _loop515; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt515++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDDEF:
			{
				AST tmp99_AST = null;
				tmp99_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp99_AST);
				match(CDDEF);
				{
				_loop518:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp100_AST = null;
						tmp100_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp100_AST);
						match(PCDATA);
					}
					else {
						break _loop518;
					}
					
				} while (true);
				}
				break;
			}
			case CDLIST:
			case ODTERM:
			case ODDEF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dd_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_15);
			} else {
			  throw ex;
			}
		}
		returnAST = dd_AST;
	}
	
	public final void dir() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dir_AST = null;
		
		try {      // for error handling
			AST tmp101_AST = null;
			tmp101_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp101_AST);
			match(ODIR);
			{
			int _cnt521=0;
			_loop521:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt521>=1 ) { break _loop521; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt521++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDIR:
			{
				AST tmp102_AST = null;
				tmp102_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
				match(CDIR);
				break;
			}
			case EOF:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dir_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_1);
			} else {
			  throw ex;
			}
		}
		returnAST = dir_AST;
	}
	
	public final void caption() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST caption_AST = null;
		
		try {      // for error handling
			AST tmp103_AST = null;
			tmp103_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp103_AST);
			match(OCAP);
			{
			_loop541:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop541;
				}
				
			} while (true);
			}
			AST tmp104_AST = null;
			tmp104_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp104_AST);
			match(CCAP);
			caption_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_16);
			} else {
			  throw ex;
			}
		}
		returnAST = caption_AST;
	}
	
	public final void tr() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST tr_AST = null;
		
		try {      // for error handling
			AST tmp105_AST = null;
			tmp105_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp105_AST);
			match(O_TR);
			{
			switch ( LA(1)) {
			case OTH:
			case OTD:
			case PCDATA:
			case CTABLE:
			case O_TR:
			case C_TR:
			{
				{
				_loop545:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp106_AST = null;
						tmp106_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp106_AST);
						match(PCDATA);
					}
					else {
						break _loop545;
					}
					
				} while (true);
				}
				break;
			}
			case COMMENT:
			{
				AST tmp107_AST = null;
				tmp107_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp107_AST);
				match(COMMENT);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop547:
			do {
				if ((LA(1)==OTH||LA(1)==OTD)) {
					th_or_td();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop547;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case C_TR:
			{
				match(C_TR);
				{
				switch ( LA(1)) {
				case PCDATA:
				case CTABLE:
				case O_TR:
				{
					{
					_loop551:
					do {
						if ((LA(1)==PCDATA)) {
							AST tmp109_AST = null;
							tmp109_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp109_AST);
							match(PCDATA);
						}
						else {
							break _loop551;
						}
						
					} while (true);
					}
					break;
				}
				case COMMENT:
				{
					AST tmp110_AST = null;
					tmp110_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp110_AST);
					match(COMMENT);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case CTABLE:
			case O_TR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tr_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_17);
			} else {
			  throw ex;
			}
		}
		returnAST = tr_AST;
	}
	
	public final void th_or_td() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST th_or_td_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case OTH:
			{
				AST tmp111_AST = null;
				tmp111_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp111_AST);
				match(OTH);
				break;
			}
			case OTD:
			{
				AST tmp112_AST = null;
				tmp112_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp112_AST);
				match(OTD);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop555:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop555;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CTH:
			case CTD:
			{
				{
				switch ( LA(1)) {
				case CTH:
				{
					match(CTH);
					break;
				}
				case CTD:
				{
					match(CTD);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				{
				_loop559:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp115_AST = null;
						tmp115_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp115_AST);
						match(PCDATA);
					}
					else {
						break _loop559;
					}
					
				} while (true);
				}
				break;
			}
			case OTH:
			case OTD:
			case CTABLE:
			case O_TR:
			case C_TR:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			th_or_td_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_18);
			} else {
			  throw ex;
			}
		}
		returnAST = th_or_td_AST;
	}
	
	public final void anchor_content() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST anchor_content_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case OTTYPE:
			case OITALIC:
			case OBOLD:
			case OUNDER:
			case OSTRIKE:
			case OBIG:
			case OSMALL:
			case OSUB:
			case OSUP:
			{
				font();
				astFactory.addASTChild(currentAST, returnAST);
				anchor_content_AST = (AST)currentAST.root;
				break;
			}
			case OCODE:
			case OEM:
			case OSTRONG:
			case ODFN:
			case OSAMP:
			case OKBD:
			case OVAR:
			case OCITE:
			case OACRO:
			{
				phrase();
				astFactory.addASTChild(currentAST, returnAST);
				anchor_content_AST = (AST)currentAST.root;
				break;
			}
			case PCDATA:
			{
				AST tmp116_AST = null;
				tmp116_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp116_AST);
				match(PCDATA);
				anchor_content_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_19);
			} else {
			  throw ex;
			}
		}
		returnAST = anchor_content_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"<4>",
		"<5>",
		"JAVADOC_COMMENT",
		"LCURLY",
		"RCURLY",
		"OTH",
		"CTH",
		"OTD",
		"CTD",
		"TAG_CUSTOM",
		"TAG_AUTHOR",
		"TAG_DEPRECATED",
		"TAG_EXCEPTION",
		"TAG_THROWS",
		"TAG_PARAM",
		"TAG_RETURN",
		"TAG_SEE",
		"TAG_SINCE",
		"TAG_SERIAL",
		"TAG_SERIAL_DATA",
		"TAG_SERIAL_FIELD",
		"TAG_VERSION",
		"TAG_INLINE_CUSTOM",
		"TAG_INLINE_DOCROOT",
		"TAG_INLINE_INHERITDOC",
		"TAG_INLINE_LINK",
		"TAG_INLINE_LINKPLAIN",
		"TAG_INLINE_VALUE",
		"TAG_TODO",
		"JAVADOC_OPEN",
		"JAVADOC_CLOSE",
		"AT",
		"TAG_OR_AT",
		"HR",
		"IMG",
		"BR",
		"TAG",
		"COMMENT",
		"PCDATA",
		"OH1",
		"CH1",
		"OH2",
		"CH2",
		"OH3",
		"CH3",
		"OH4",
		"CH4",
		"OH5",
		"CH5",
		"OH6",
		"CH6",
		"OADDRESS",
		"CADDRESS",
		"OPARA",
		"CPARA",
		"OULIST",
		"CULIST",
		"OOLIST",
		"COLIST",
		"ODLIST",
		"CDLIST",
		"OLITEM",
		"CLITEM",
		"ODTERM",
		"CDTERM",
		"ODDEF",
		"CDDEF",
		"ODIR",
		"CDIR",
		"ODIV",
		"CDIV",
		"OCENTER",
		"CCENTER",
		"OBQUOTE",
		"CBQUOTE",
		"PRE",
		"OTABLE",
		"CTABLE",
		"OCAP",
		"CCAP",
		"O_TR",
		"C_TR",
		"OTTYPE",
		"CTTYPE",
		"OITALIC",
		"CITALIC",
		"OBOLD",
		"CBOLD",
		"OCODE",
		"CCODE",
		"OUNDER",
		"CUNDER",
		"OSTRIKE",
		"CSTRIKE",
		"OBIG",
		"CBIG",
		"OSMALL",
		"CSMALL",
		"OSUB",
		"CSUB",
		"OSUP",
		"CSUP",
		"OEM",
		"CEM",
		"OSTRONG",
		"CSTRONG",
		"ODFN",
		"CDFN",
		"OSAMP",
		"CSAMP",
		"OKBD",
		"CKBD",
		"OVAR",
		"CVAR",
		"OCITE",
		"CCITE",
		"OACRO",
		"CACRO",
		"TYPEDCLASS",
		"OANCHOR",
		"CANCHOR",
		"OFONT",
		"CFONT",
		"CDIR_OR_CDIV",
		"O_TH_OR_TD",
		"C_TH_OR_TD",
		"OSTRIKE_OR_OSTRONG",
		"CSTRIKE_OR_CSTRONG",
		"CSUB_OR_CSUP",
		"STAR",
		"COMMENT_DATA",
		"WS",
		"NEWLINE",
		"ATTR",
		"WORD",
		"STRING",
		"SPECIAL",
		"HEXNUM",
		"INT",
		"HEXINT",
		"DIGIT",
		"HEXDIGIT",
		"EMAILSTART",
		"LCLETTER"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { -6148909961045868160L, 3266610929718110720L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { -6148908844354363520L, 3266610929721409024L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 7421703487872L, 3266610929718001664L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { -6196952949822849024L, 109056L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { -6148908844354371200L, 3266610929718110720L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -72057611217789054L, 8070450532247666559L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { -360287987369500798L, 3266610929721409391L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { -6341068275337658368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { -72057611217789054L, 3266610929721409391L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { -72057611217789054L, 9223372036854513535L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 4398046511104L, 96076792049172480L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { -272L, -1L, 1048575L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 24013333950627840L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 5764607523034234882L, 258L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 0L, 41L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 4398046511104L, 1048576L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 0L, 1179648L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 2560L, 3276800L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 4398046511104L, 1248998296656019456L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	
	}
