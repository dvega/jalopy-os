// $ANTLR 2.7.4: "java.doc.g" -> "InternalJavadocParser.java"$

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
			_loop3:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop3;
				}
				
			} while (true);
			}
			{
			_loop5:
			do {
				if ((LA(1)==TAG)) {
					standard_tag();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop5;
				}
				
			} while (true);
			}
			{
			_loop7:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop7;
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
			_loop176:
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
					break _loop176;
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
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
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
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(COMMENT);
				text_AST = (AST)currentAST.root;
				break;
			}
			case PCDATA:
			{
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(PCDATA);
				text_AST = (AST)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched18 = false;
				if (((LA(1)==LCURLY))) {
					int _m18 = mark();
					synPredMatched18 = true;
					inputState.guessing++;
					try {
						{
						match(LCURLY);
						match(TAG);
						}
					}
					catch (RecognitionException pe) {
						synPredMatched18 = false;
					}
					rewind(_m18);
					inputState.guessing--;
				}
				if ( synPredMatched18 ) {
					inline_tag();
					astFactory.addASTChild(currentAST, returnAST);
					text_AST = (AST)currentAST.root;
				}
				else if ((LA(1)==LCURLY)) {
					AST tmp7_AST = null;
					tmp7_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp7_AST);
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
				AST tmp8_AST = null;
				tmp8_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
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
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp9_AST);
			match(OADDRESS);
			{
			_loop30:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp10_AST = null;
					tmp10_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp10_AST);
					match(PCDATA);
				}
				else {
					break _loop30;
				}
				
			} while (true);
			}
			AST tmp11_AST = null;
			tmp11_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp11_AST);
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
			AST tmp12_AST = null;
			tmp12_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp12_AST);
			match(OH1);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp13_AST = null;
			tmp13_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp13_AST);
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
			AST tmp14_AST = null;
			tmp14_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp14_AST);
			match(OH2);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp15_AST = null;
			tmp15_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp15_AST);
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
			AST tmp16_AST = null;
			tmp16_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp16_AST);
			match(OH3);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp17_AST = null;
			tmp17_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp17_AST);
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
			AST tmp18_AST = null;
			tmp18_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp18_AST);
			match(OH4);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp19_AST = null;
			tmp19_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp19_AST);
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
			AST tmp20_AST = null;
			tmp20_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp20_AST);
			match(OH5);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp21_AST = null;
			tmp21_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp21_AST);
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
			AST tmp22_AST = null;
			tmp22_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp22_AST);
			match(OH6);
			heading_content();
			astFactory.addASTChild(currentAST, returnAST);
			AST tmp23_AST = null;
			tmp23_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp23_AST);
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
			AST tmp24_AST = null;
			tmp24_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp24_AST);
			match(OPARA);
			{
			_loop33:
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
					break _loop33;
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
			AST tmp26_AST = null;
			tmp26_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp26_AST);
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
			AST tmp27_AST = null;
			tmp27_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp27_AST);
			match(ODIV);
			{
			_loop76:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop76;
				}
				
			} while (true);
			}
			AST tmp28_AST = null;
			tmp28_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp28_AST);
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
			AST tmp29_AST = null;
			tmp29_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp29_AST);
			match(OCENTER);
			{
			_loop79:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop79;
				}
				
			} while (true);
			}
			AST tmp30_AST = null;
			tmp30_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp30_AST);
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
			AST tmp31_AST = null;
			tmp31_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp31_AST);
			match(OBQUOTE);
			{
			_loop82:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop82;
				}
				
			} while (true);
			}
			AST tmp32_AST = null;
			tmp32_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp32_AST);
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
			AST tmp33_AST = null;
			tmp33_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp33_AST);
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
			_loop87:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp34_AST = null;
					tmp34_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp34_AST);
					match(PCDATA);
				}
				else {
					break _loop87;
				}
				
			} while (true);
			}
			{
			int _cnt89=0;
			_loop89:
			do {
				if ((LA(1)==O_TR)) {
					tr();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt89>=1 ) { break _loop89; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt89++;
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
			AST tmp36_AST = null;
			tmp36_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp36_AST);
			match(OTTYPE);
			{
			int _cnt113=0;
			_loop113:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt113>=1 ) { break _loop113; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt113++;
			} while (true);
			}
			AST tmp37_AST = null;
			tmp37_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
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
			AST tmp38_AST = null;
			tmp38_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp38_AST);
			match(OITALIC);
			{
			int _cnt116=0;
			_loop116:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt116>=1 ) { break _loop116; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt116++;
			} while (true);
			}
			AST tmp39_AST = null;
			tmp39_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp39_AST);
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
			AST tmp40_AST = null;
			tmp40_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp40_AST);
			match(OBOLD);
			{
			int _cnt119=0;
			_loop119:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt119>=1 ) { break _loop119; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt119++;
			} while (true);
			}
			AST tmp41_AST = null;
			tmp41_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp41_AST);
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
			AST tmp42_AST = null;
			tmp42_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp42_AST);
			match(OUNDER);
			{
			int _cnt125=0;
			_loop125:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt125>=1 ) { break _loop125; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt125++;
			} while (true);
			}
			AST tmp43_AST = null;
			tmp43_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp43_AST);
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
			AST tmp44_AST = null;
			tmp44_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp44_AST);
			match(OSTRIKE);
			{
			int _cnt128=0;
			_loop128:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt128>=1 ) { break _loop128; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt128++;
			} while (true);
			}
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp45_AST);
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
			AST tmp46_AST = null;
			tmp46_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp46_AST);
			match(OBIG);
			{
			int _cnt131=0;
			_loop131:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt131>=1 ) { break _loop131; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt131++;
			} while (true);
			}
			AST tmp47_AST = null;
			tmp47_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp47_AST);
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
			AST tmp48_AST = null;
			tmp48_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp48_AST);
			match(OSMALL);
			{
			int _cnt134=0;
			_loop134:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt134>=1 ) { break _loop134; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt134++;
			} while (true);
			}
			AST tmp49_AST = null;
			tmp49_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp49_AST);
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
			AST tmp50_AST = null;
			tmp50_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp50_AST);
			match(OSUB);
			{
			int _cnt137=0;
			_loop137:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt137>=1 ) { break _loop137; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt137++;
			} while (true);
			}
			AST tmp51_AST = null;
			tmp51_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp51_AST);
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
			AST tmp52_AST = null;
			tmp52_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp52_AST);
			match(OSUP);
			{
			int _cnt140=0;
			_loop140:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt140>=1 ) { break _loop140; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt140++;
			} while (true);
			}
			AST tmp53_AST = null;
			tmp53_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp53_AST);
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
			AST tmp54_AST = null;
			tmp54_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp54_AST);
			match(OEM);
			{
			int _cnt143=0;
			_loop143:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt143>=1 ) { break _loop143; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt143++;
			} while (true);
			}
			AST tmp55_AST = null;
			tmp55_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp55_AST);
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
			AST tmp56_AST = null;
			tmp56_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp56_AST);
			match(OSTRONG);
			{
			int _cnt146=0;
			_loop146:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt146>=1 ) { break _loop146; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt146++;
			} while (true);
			}
			AST tmp57_AST = null;
			tmp57_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp57_AST);
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
			AST tmp58_AST = null;
			tmp58_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp58_AST);
			match(ODFN);
			{
			int _cnt149=0;
			_loop149:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt149>=1 ) { break _loop149; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt149++;
			} while (true);
			}
			AST tmp59_AST = null;
			tmp59_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp59_AST);
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
			AST tmp60_AST = null;
			tmp60_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp60_AST);
			match(OCODE);
			{
			int _cnt122=0;
			_loop122:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt122>=1 ) { break _loop122; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt122++;
			} while (true);
			}
			AST tmp61_AST = null;
			tmp61_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp61_AST);
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
			AST tmp62_AST = null;
			tmp62_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp62_AST);
			match(OSAMP);
			{
			int _cnt152=0;
			_loop152:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt152>=1 ) { break _loop152; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt152++;
			} while (true);
			}
			AST tmp63_AST = null;
			tmp63_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp63_AST);
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
			AST tmp64_AST = null;
			tmp64_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp64_AST);
			match(OKBD);
			{
			int _cnt155=0;
			_loop155:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt155>=1 ) { break _loop155; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt155++;
			} while (true);
			}
			AST tmp65_AST = null;
			tmp65_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp65_AST);
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
			AST tmp66_AST = null;
			tmp66_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp66_AST);
			match(OVAR);
			{
			int _cnt158=0;
			_loop158:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt158>=1 ) { break _loop158; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt158++;
			} while (true);
			}
			AST tmp67_AST = null;
			tmp67_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp67_AST);
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
			AST tmp68_AST = null;
			tmp68_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp68_AST);
			match(OCITE);
			{
			int _cnt161=0;
			_loop161:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt161>=1 ) { break _loop161; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt161++;
			} while (true);
			}
			AST tmp69_AST = null;
			tmp69_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp69_AST);
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
			AST tmp70_AST = null;
			tmp70_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp70_AST);
			match(OACRO);
			{
			int _cnt164=0;
			_loop164:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt164>=1 ) { break _loop164; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt164++;
			} while (true);
			}
			AST tmp71_AST = null;
			tmp71_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp71_AST);
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
				AST tmp72_AST = null;
				tmp72_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp72_AST);
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
				AST tmp73_AST = null;
				tmp73_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp73_AST);
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
			AST tmp74_AST = null;
			tmp74_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp74_AST);
			match(OANCHOR);
			{
			_loop168:
			do {
				if ((_tokenSet_11.member(LA(1)))) {
					anchor_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop168;
				}
				
			} while (true);
			}
			AST tmp75_AST = null;
			tmp75_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp75_AST);
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
			AST tmp76_AST = null;
			tmp76_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp76_AST);
			match(OFONT);
			{
			_loop172:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop172;
				}
				
			} while (true);
			}
			AST tmp77_AST = null;
			tmp77_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp77_AST);
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
			AST tmp78_AST = null;
			tmp78_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp78_AST);
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
			_loop179:
			do {
				if ((_tokenSet_12.member(LA(1)))) {
					AST tmp80_AST = null;
					tmp80_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp80_AST);
					matchNot(RCURLY);
				}
				else {
					break _loop179;
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
			_loop27:
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
					break _loop27;
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
			AST tmp82_AST = null;
			tmp82_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp82_AST);
			match(OULIST);
			{
			_loop38:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp83_AST = null;
					tmp83_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp83_AST);
					match(PCDATA);
				}
				else {
					break _loop38;
				}
				
			} while (true);
			}
			{
			int _cnt40=0;
			_loop40:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt40>=1 ) { break _loop40; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt40++;
			} while (true);
			}
			AST tmp84_AST = null;
			tmp84_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp84_AST);
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
			AST tmp85_AST = null;
			tmp85_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp85_AST);
			match(OOLIST);
			{
			_loop43:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp86_AST = null;
					tmp86_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp86_AST);
					match(PCDATA);
				}
				else {
					break _loop43;
				}
				
			} while (true);
			}
			{
			int _cnt45=0;
			_loop45:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt45>=1 ) { break _loop45; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt45++;
			} while (true);
			}
			AST tmp87_AST = null;
			tmp87_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp87_AST);
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
			AST tmp88_AST = null;
			tmp88_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp88_AST);
			match(ODLIST);
			{
			_loop48:
			do {
				if ((LA(1)==PCDATA)) {
					AST tmp89_AST = null;
					tmp89_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp89_AST);
					match(PCDATA);
				}
				else {
					break _loop48;
				}
				
			} while (true);
			}
			{
			int _cnt50=0;
			_loop50:
			do {
				if ((LA(1)==ODTERM||LA(1)==ODDEF)) {
					def_list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt50>=1 ) { break _loop50; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt50++;
			} while (true);
			}
			AST tmp90_AST = null;
			tmp90_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp90_AST);
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
			AST tmp91_AST = null;
			tmp91_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp91_AST);
			match(OLITEM);
			{
			int _cnt53=0;
			_loop53:
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
					if ( _cnt53>=1 ) { break _loop53; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt53++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CLITEM:
			{
				AST tmp92_AST = null;
				tmp92_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp92_AST);
				match(CLITEM);
				{
				_loop56:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp93_AST = null;
						tmp93_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp93_AST);
						match(PCDATA);
					}
					else {
						break _loop56;
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
			AST tmp94_AST = null;
			tmp94_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp94_AST);
			match(ODTERM);
			{
			int _cnt60=0;
			_loop60:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt60>=1 ) { break _loop60; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt60++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDTERM:
			{
				AST tmp95_AST = null;
				tmp95_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp95_AST);
				match(CDTERM);
				{
				_loop63:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp96_AST = null;
						tmp96_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp96_AST);
						match(PCDATA);
					}
					else {
						break _loop63;
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
			AST tmp97_AST = null;
			tmp97_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp97_AST);
			match(ODDEF);
			{
			int _cnt66=0;
			_loop66:
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
					if ( _cnt66>=1 ) { break _loop66; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt66++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDDEF:
			{
				AST tmp98_AST = null;
				tmp98_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp98_AST);
				match(CDDEF);
				{
				_loop69:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp99_AST = null;
						tmp99_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp99_AST);
						match(PCDATA);
					}
					else {
						break _loop69;
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
			AST tmp100_AST = null;
			tmp100_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp100_AST);
			match(ODIR);
			{
			int _cnt72=0;
			_loop72:
			do {
				if ((LA(1)==OLITEM)) {
					list_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt72>=1 ) { break _loop72; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt72++;
			} while (true);
			}
			{
			switch ( LA(1)) {
			case CDIR:
			{
				AST tmp101_AST = null;
				tmp101_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp101_AST);
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
			AST tmp102_AST = null;
			tmp102_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp102_AST);
			match(OCAP);
			{
			_loop92:
			do {
				if ((_tokenSet_3.member(LA(1)))) {
					text();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop92;
				}
				
			} while (true);
			}
			AST tmp103_AST = null;
			tmp103_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp103_AST);
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
			AST tmp104_AST = null;
			tmp104_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp104_AST);
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
				_loop96:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp105_AST = null;
						tmp105_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp105_AST);
						match(PCDATA);
					}
					else {
						break _loop96;
					}
					
				} while (true);
				}
				break;
			}
			case COMMENT:
			{
				AST tmp106_AST = null;
				tmp106_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp106_AST);
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
			_loop98:
			do {
				if ((LA(1)==OTH||LA(1)==OTD)) {
					th_or_td();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop98;
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
					_loop102:
					do {
						if ((LA(1)==PCDATA)) {
							AST tmp108_AST = null;
							tmp108_AST = astFactory.create(LT(1));
							astFactory.addASTChild(currentAST, tmp108_AST);
							match(PCDATA);
						}
						else {
							break _loop102;
						}
						
					} while (true);
					}
					break;
				}
				case COMMENT:
				{
					AST tmp109_AST = null;
					tmp109_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp109_AST);
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
				AST tmp110_AST = null;
				tmp110_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp110_AST);
				match(OTH);
				break;
			}
			case OTD:
			{
				AST tmp111_AST = null;
				tmp111_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp111_AST);
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
			_loop106:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					body_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop106;
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
				_loop110:
				do {
					if ((LA(1)==PCDATA)) {
						AST tmp114_AST = null;
						tmp114_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp114_AST);
						match(PCDATA);
					}
					else {
						break _loop110;
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
				AST tmp115_AST = null;
				tmp115_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp115_AST);
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
		"LCLETTER"
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 6148917039151972736L, 1633305464859055360L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 6148917606087663488L, 1633305464860704512L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 3710851744128L, 1633305464859000832L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 6124895561943351296L, 54528L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 6148917606087655808L, 1633305464859055360L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -36028814198825086L, 4035225266123833279L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { -180144002274680958L, 1633305464860704695L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 6052837899185946624L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { -36028814198825086L, 1633305464860704695L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { -36028814198825086L, 4611686018427256767L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 2199023255552L, 48038396024586240L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { -272L, -1L, 262143L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 12006666975313920L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 2882303761517117442L, 129L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { -9223372036854775808L, 20L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 2199023255552L, 524288L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 0L, 589824L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 2560L, 1638400L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 2199023255552L, 624499148328009728L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	
	}
