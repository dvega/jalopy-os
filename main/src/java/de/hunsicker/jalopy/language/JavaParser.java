// $ANTLR 2.7.2a2 (20020112-1): "src/java/de/hunsicker/jalopy/language/java.g" -> "JavaParser.java"$

package de.hunsicker.jalopy.language;

import de.hunsicker.antlr.TokenBuffer;
import de.hunsicker.antlr.TokenStreamException;
import de.hunsicker.antlr.TokenStreamIOException;
import de.hunsicker.antlr.ANTLRException;
import de.hunsicker.antlr.LLkParser;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.TokenStream;
import de.hunsicker.antlr.RecognitionException;
import de.hunsicker.antlr.NoViableAltException;
import de.hunsicker.antlr.MismatchedTokenException;
import de.hunsicker.antlr.SemanticException;
import de.hunsicker.antlr.ParserSharedInputState;
import de.hunsicker.antlr.collections.impl.BitSet;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.antlr.ASTPair;
import de.hunsicker.antlr.collections.impl.ASTArray;

import de.hunsicker.antlr.ANTLRStringBuffer;
import de.hunsicker.antlr.CommonHiddenStreamToken;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Parser for the Sun Java language. Heavily based on the public domain grammar written by
 * <a href="mailto:parrt@jguru.com">Terence Parr
 * </a> et al. See <a href="http://www.antlr.org/resources.html">
 * http://www.antlr.org/resources.html</a> for more infos.
 *
 * <p>This is an <a href="http://www.antlr.org">ANTLR</a> automated generated
 * file. <strong>DO NOT EDIT</strong> but rather change the associated grammar
 * (<code>java.g</code>) and rebuild.</p>
 *
 * @version 1.0
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @see de.hunsicker.jalopy.language.JavaLexer
 */
public final class JavaParser extends de.hunsicker.antlr.LLkParser
       implements JavaTokenTypes
, Parser {


    final static String STR_TYPE = "TYPE";
    final static String STR_MODIFIERS= "MODIFIERS";
    final static String STR_OBJBLOCK = "OBJBLOCK";
    final static String STR_EXTENDS_CLAUSE = "EXTENDS_CLAUSE";
    final static String STR_INSTANCE_INIT = "INSTANCE_INIT";
    final static String STR_PARAMETERS = "PARAMETERS";
    final static String STR_CASESLIST = "CASESLIST";
    final static String STR_EXPR = "EXPR";
    final static String STR_ELIST = "ELIST";

    /** Reported identifiers. */
    private List _unqualIdents = new NoList(100); // List of <String>

    /** Reported identifiers. */
    private List _qualIdents = new NoList(30); // List of <String>

    /** Used to build qualified identifiers. */
    private ANTLRStringBuffer _buf = new ANTLRStringBuffer(100);

    /** Used to build identifiers. */
    private List _buildList = new ArrayList(10); // List of <String>

    private References _references = new References();

    /**
     * Were the import nodes of the identifiers we stripped qualification yet
     * added to the tree?
     */
    private boolean _insert = true;

    /** Holds the imports of the identifiers we stripped. */
    private List _strippedImports = new ArrayList(); // List of <JavaNode>

    /** Qualified imports. */
    private Set _qualImports = new HashSet(20); // Set of <String>

    /** Unqualified (wildcard) imports. */
    private Set _unqualImports = new HashSet(10); // Set of <String>

    /** Logging. */
    private final Logger _logger = Logger.getLogger("de.hunsicker.jalopy.language.java");

    /** The package name of the parsed source file. */
    private String _packageName = "";

    /** Strip qualification for qualified identifiers? */
    boolean stripQualification;

    /**
     * Sets whether qualification of qualified identifiers should be stripped.
     * (not implemented yet)
     * @param strip if <code>true</code> qualification will be stripped.
     */
    public void setStripQualification(boolean strip)
    {
        // XXX currently disabled as it don't work without accessing the
        // class repository
        //this.stripQualification = strip;
    }

    /**
     * Indicates whether the qualification stripping is enabled.
     * @return <code>true</code> if the qualification stripping is enabled.
     */
    public boolean isStripQualifation()
    {
        return this.stripQualification;
    }

    /**
     * Adds the given identifier to the identifier storage.
     * @param ident identifier.
     */
    private void addIdentifier(String ident)
    {
        if (ident.indexOf('.') > -1)
        {
            // XXX implement feature: resolve qualified identifiers
            // make them unqualified
            _qualIdents.add(ident);
        }
        else
        {
            _unqualIdents.add(ident);
        }
    }

    public AST getParseTree()
    {
        // insert the import nodes of the stripped identifiers to the tree
        // if not already added
        if (this.stripQualification && _insert)
        {
            if (this.returnAST == null)
                return null;

            AST top = this.returnAST.getFirstChild();

            switch (top.getType())
            {
                case JavaTokenTypes.PACKAGE_DEF:
                case JavaTokenTypes.IMPORT:
                    for (int i = 0, size = _strippedImports.size(); i < size; i++)
                    {
                        AST tmp = top.getNextSibling();
                        JavaNode imp = (JavaNode)_strippedImports.get(i);
                        imp.setNextSibling(tmp);
                        top.setNextSibling(imp);
                    }

                    break;

                case JavaTokenTypes.CLASS_DEF:
                    break;
            }

            _insert = false;
        }

        return this.returnAST;
    }

   /**
    * Reports the given error.
    * @param ex encountered exception.
    */
   public void reportError(RecognitionException ex)
   {
      Object[] args = { getFilename(), new Integer(ex.line), new Integer(ex.column), ex.getMessage() };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, ex);
   }

    private final static Integer UNKNOWN_POSITION = new Integer(0);

   /**
    * Reports the given error.
    * @param message error message.
    */
   public void reportError(String message)
   {
      Object[] args = { getFilename(), UNKNOWN_POSITION, UNKNOWN_POSITION, message };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, null);
   }

   /**
    * Reports the given warning.
    * @param message warning message.
    */
   public void reportWarning(String message)
   {
      Object[] args = { getFilename(), UNKNOWN_POSITION,UNKNOWN_POSITION, message };
      _logger.l7dlog(Level.WARN, "PARSER_ERROR", args, null);
   }

    /**
     * Returns the package name of the parsed source file.
     * @return the package name of the parsed source file. Returns the empty
     *         String if the source file contains no package information.
     */
    public String getPackageName()
    {
        return _packageName;
    }

    /**
     * Attaches the hidden tokens from the specified compound statement to its
     * imaginary node.
     *
     * @param node a INSTANCE_INIT node.
     * @param statement a SLIST node.
     */
    private void attachStuffBeforeCompoundStatement(JavaNode node, JavaNode statement)
    {
        node.setHiddenBefore(statement.getHiddenBefore());
        statement.setHiddenBefore(null);
    }

    /**
     * Attaches the hidden tokens associated to either the modifiers or keyword to the imaginary node.
     *
     * @param node a CTOR_DEF node.
     * @param modifiers a MODIFIRES node.
     * @param keyword a IDENT node.
     */
    private void attachStuffBeforeCtor(JavaNode node, JavaNode modifiers, JavaNode keyword)
    {
        JavaNode modifier = (JavaNode)modifiers.getFirstChild();

        if (modifier != null)
        {
            node.setHiddenBefore(modifier.getHiddenBefore());
            modifier.setHiddenBefore(null);
        }
        else
        {
            if (keyword.getHiddenBefore() != null)
            {
                node.setHiddenBefore(keyword.getHiddenBefore());
                keyword.setHiddenBefore(null);
            }
        }
    }

    /**
     * Attaches the hidden tokens associated to either the modifiers or type to the imaginary node.
     *
     * @param node a METHOD_DEF or VARIABLE_DEF node.
     * @param modifiers a MODIFIERS node.
     * @param type a TYPE node.
     */
    private void attachStuffBefore(JavaNode node, JavaNode modifiers, JavaNode type)
    {
        JavaNode modifier = (JavaNode)modifiers.getFirstChild();

        if (modifier != null)
        {
            node.setHiddenBefore(modifier.getHiddenBefore());
            modifier.setHiddenBefore(null);
        }
        else
        {
            for (AST child = type; child != null; child = child.getFirstChild())
            {
                if (child.getFirstChild() == null)
                {
                    JavaNode t = (JavaNode)child;

                    if (t.getHiddenBefore() != null)
                    {
                        node.setHiddenBefore(t.getHiddenBefore());
                        t.setHiddenBefore(null);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Returns all unqualified Java identifiers referenced in the file.
     *
     * @return unqualified identifiers. Returns an empty array if no
     *         unqualified identifiers could be found.
     */
    public List getUnqualifiedIdents()
    {
        return _unqualIdents;
    }

    /**
     * Returns all qualified Java identifiers referenced in the file.
     *
     * @return qualified identifiers. Returns an empty array if no
     *         qualified identifiers could be found.
     */
    public List getQualifiedIdents()
    {
        return _qualIdents;
    }

    /**
     * Resets the parser.
     */
    public void reset()
    {
        _buildList.clear();
        _qualIdents.clear();
        _qualImports.clear();
        _strippedImports.clear();
        _references.reset();
        _unqualIdents.clear();
        _unqualImports.clear();
        _insert = true;
        _packageName = "";
        _buf.setLength(0);

        if (this.inputState != null)
            this.inputState.reset();

        setFilename(Recognizer.UNKNOWN_FILE);
        this.returnAST = null;
    }

    /**
     * Random access list that prohibits duplicates or null-values.
     */
    private static class NoList
        extends ArrayList
    {
        public NoList(int initialSize)
        {
            super(initialSize);
        }

        public boolean add(Object element)
        {
            if (element == null)
                return false;

            if (contains(element))
                return false;

            return super.add(element);
        }

        public void add(int index, Object element)
        {
            if (element == null)
                return;

            if (contains(element))
                return;

            super.add(index, element);
        }

        public Object set(int index, Object element)
        {
            if (element == null)
                return element;

            if (contains(element))
                return element;

            return super.set(index, element);
        }

        // XXX implement addAll
    }


protected JavaParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public JavaParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected JavaParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public JavaParser(TokenStream lexer) {
  this(lexer,2);
}

public JavaParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	public final void parse() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode parse_AST = null;
		
		JavaNode root = new JavaNode();
		root.setType(JavaTokenTypes.ROOT);
		root.setText(getFilename());
		currentAST.root = root;
		
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_package:
			{
				packageDefinition();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case EOF:
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case SEMI:
			case LITERAL_import:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_class:
			case LITERAL_interface:
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
			_loop4:
			do {
				if ((LA(1)==LITERAL_import)) {
					importDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop4;
				}
				
			} while (true);
			}
			{
			_loop6:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					typeDefinition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop6;
				}
				
			} while (true);
			}
			match(Token.EOF_TYPE);
			parse_AST = (JavaNode)currentAST.root;
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
		returnAST = parse_AST;
	}
	
	public final void packageDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode packageDefinition_AST = null;
		Token  p = null;
		JavaNode p_AST = null;
		Token  semi = null;
		JavaNode semi_AST = null;
		
		try {      // for error handling
			p = LT(1);
			p_AST = (JavaNode)astFactory.create(p);
			astFactory.makeASTRoot(currentAST, p_AST);
			match(LITERAL_package);
			identifierPackage();
			astFactory.addASTChild(currentAST, returnAST);
			semi = LT(1);
			semi_AST = (JavaNode)astFactory.create(semi);
			astFactory.addASTChild(currentAST, semi_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				p_AST.setType(JavaTokenTypes.PACKAGE_DEF);
			}
			packageDefinition_AST = (JavaNode)currentAST.root;
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
		returnAST = packageDefinition_AST;
	}
	
	public final void importDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode importDefinition_AST = null;
		Token  i = null;
		JavaNode i_AST = null;
		Token  semi = null;
		JavaNode semi_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = (JavaNode)astFactory.create(i);
			astFactory.makeASTRoot(currentAST, i_AST);
			match(LITERAL_import);
			identifierStar();
			astFactory.addASTChild(currentAST, returnAST);
			semi = LT(1);
			semi_AST = (JavaNode)astFactory.create(semi);
			astFactory.addASTChild(currentAST, semi_AST);
			match(SEMI);
			if ( inputState.guessing==0 ) {
				i_AST.setType(JavaTokenTypes.IMPORT);
			}
			importDefinition_AST = (JavaNode)currentAST.root;
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
		returnAST = importDefinition_AST;
	}
	
	public final void typeDefinition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode typeDefinition_AST = null;
		JavaNode m_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_class:
			case LITERAL_interface:
			{
				modifiers();
				m_AST = (JavaNode)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_class:
				{
					classDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				typeDefinition_AST = (JavaNode)currentAST.root;
				break;
			}
			case SEMI:
			{
				JavaNode tmp2_AST = null;
				tmp2_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(SEMI);
				typeDefinition_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_3);
			} else {
			  throw ex;
			}
		}
		returnAST = typeDefinition_AST;
	}
	
/**
 * Our qualified package identifier. We need this rule because all other
 * identifiers will be made unqualified (depending on the stripQualification switch).
 */
	public final void identifierPackage() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode identifierPackage_AST = null;
		Token  id1 = null;
		JavaNode id1_AST = null;
		Token  id2 = null;
		JavaNode id2_AST = null;
		
		_buf.setLength(0);
		
		
		try {      // for error handling
			id1 = LT(1);
			id1_AST = (JavaNode)astFactory.create(id1);
			astFactory.addASTChild(currentAST, id1_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				_buf.append(id1_AST.getText());
			}
			{
			_loop26:
			do {
				if ((LA(1)==DOT)) {
					JavaNode tmp3_AST = null;
					tmp3_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp3_AST);
					match(DOT);
					id2 = LT(1);
					id2_AST = (JavaNode)astFactory.create(id2);
					astFactory.addASTChild(currentAST, id2_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						
						_buf.append('.');
						_buf.append(id2_AST.getText());
						
					}
				}
				else {
					break _loop26;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				_packageName = _buf.toString();
			}
			identifierPackage_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = identifierPackage_AST;
	}
	
	public final void identifierStar() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode identifierStar_AST = null;
		Token  id1 = null;
		JavaNode id1_AST = null;
		Token  id2 = null;
		JavaNode id2_AST = null;
		
		_buf.setLength(0);
		boolean star = false;
		
		
		try {      // for error handling
			id1 = LT(1);
			id1_AST = (JavaNode)astFactory.create(id1);
			astFactory.addASTChild(currentAST, id1_AST);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				_buf.append(id1.getText());
			}
			{
			_loop29:
			do {
				if ((LA(1)==DOT) && (LA(2)==IDENT)) {
					JavaNode tmp4_AST = null;
					tmp4_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp4_AST);
					match(DOT);
					id2 = LT(1);
					id2_AST = (JavaNode)astFactory.create(id2);
					astFactory.addASTChild(currentAST, id2_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						
						_buf.append('.');
						_buf.append(id2.getText());
						
					}
				}
				else {
					break _loop29;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case DOT:
			{
				JavaNode tmp5_AST = null;
				tmp5_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp5_AST);
				match(DOT);
				JavaNode tmp6_AST = null;
				tmp6_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(STAR);
				if ( inputState.guessing==0 ) {
					star = true;
				}
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				if (star)
				_unqualImports.add(_buf.toString());
				else
				_qualImports.add(_buf.toString());
				
			}
			identifierStar_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = identifierStar_AST;
	}
	
	public final void modifiers() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode modifiers_AST = null;
		
		try {      // for error handling
			{
			_loop33:
			do {
				if ((_tokenSet_5.member(LA(1)))) {
					modifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop33;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				modifiers_AST = (JavaNode)currentAST.root;
				modifiers_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.MODIFIERS,STR_MODIFIERS)).add(modifiers_AST));
				currentAST.root = modifiers_AST;
				currentAST.child = modifiers_AST!=null &&modifiers_AST.getFirstChild()!=null ?
					modifiers_AST.getFirstChild() : modifiers_AST;
				currentAST.advanceChildToEnd();
			}
			modifiers_AST = (JavaNode)currentAST.root;
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
		returnAST = modifiers_AST;
	}
	
	public final void classDefinition(
		JavaNode modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode classDefinition_AST = null;
		Token  c = null;
		JavaNode c_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		JavaNode sc_AST = null;
		JavaNode ic_AST = null;
		JavaNode cb_AST = null;
		
		try {      // for error handling
			c = LT(1);
			c_AST = (JavaNode)astFactory.create(c);
			match(LITERAL_class);
			id = LT(1);
			id_AST = (JavaNode)astFactory.create(id);
			match(IDENT);
			superClassClause();
			sc_AST = (JavaNode)returnAST;
			implementsClause();
			ic_AST = (JavaNode)returnAST;
			classBlock();
			cb_AST = (JavaNode)returnAST;
			if ( inputState.guessing==0 ) {
				classDefinition_AST = (JavaNode)currentAST.root;
				classDefinition_AST = (JavaNode)astFactory.make( (new ASTArray(6)).add((JavaNode)astFactory.create(JavaTokenTypes.CLASS_DEF,"CLASS_DEF")).add(modifiers).add(id_AST).add(sc_AST).add(ic_AST).add(cb_AST));
				
				classDefinition_AST.setHiddenBefore(c_AST.getHiddenBefore());
				classDefinition_AST.setHiddenAfter(c_AST.getHiddenAfter());
				attachStuffBefore(classDefinition_AST, modifiers, c_AST);
				
				currentAST.root = classDefinition_AST;
				currentAST.child = classDefinition_AST!=null &&classDefinition_AST.getFirstChild()!=null ?
					classDefinition_AST.getFirstChild() : classDefinition_AST;
				currentAST.advanceChildToEnd();
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
		returnAST = classDefinition_AST;
	}
	
	public final void interfaceDefinition(
		JavaNode modifiers
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode interfaceDefinition_AST = null;
		Token  i = null;
		JavaNode i_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		JavaNode ie_AST = null;
		JavaNode cb_AST = null;
		
		try {      // for error handling
			i = LT(1);
			i_AST = (JavaNode)astFactory.create(i);
			match(LITERAL_interface);
			id = LT(1);
			id_AST = (JavaNode)astFactory.create(id);
			match(IDENT);
			interfaceExtends();
			ie_AST = (JavaNode)returnAST;
			classBlock();
			cb_AST = (JavaNode)returnAST;
			if ( inputState.guessing==0 ) {
				interfaceDefinition_AST = (JavaNode)currentAST.root;
				interfaceDefinition_AST = (JavaNode)astFactory.make( (new ASTArray(5)).add((JavaNode)astFactory.create(JavaTokenTypes.INTERFACE_DEF,"INTERFACE_DEF")).add(modifiers).add(id_AST).add(ie_AST).add(cb_AST));
				attachStuffBefore(interfaceDefinition_AST, modifiers, i_AST);
				
				currentAST.root = interfaceDefinition_AST;
				currentAST.child = interfaceDefinition_AST!=null &&interfaceDefinition_AST.getFirstChild()!=null ?
					interfaceDefinition_AST.getFirstChild() : interfaceDefinition_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_8);
			} else {
			  throw ex;
			}
		}
		returnAST = interfaceDefinition_AST;
	}
	
/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
	public final void declaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode declaration_AST = null;
		JavaNode m_AST = null;
		JavaNode t_AST = null;
		JavaNode v_AST = null;
		
		try {      // for error handling
			modifiers();
			m_AST = (JavaNode)returnAST;
			typeSpec(false);
			t_AST = (JavaNode)returnAST;
			variableDefinitions(m_AST,t_AST);
			v_AST = (JavaNode)returnAST;
			if ( inputState.guessing==0 ) {
				declaration_AST = (JavaNode)currentAST.root;
				declaration_AST = v_AST;
				currentAST.root = declaration_AST;
				currentAST.child = declaration_AST!=null &&declaration_AST.getFirstChild()!=null ?
					declaration_AST.getFirstChild() : declaration_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = declaration_AST;
	}
	
	public final void typeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode typeSpec_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				classTypeSpec(addImagNode);
				astFactory.addASTChild(currentAST, returnAST);
				typeSpec_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInTypeSpec(addImagNode);
				astFactory.addASTChild(currentAST, returnAST);
				typeSpec_AST = (JavaNode)currentAST.root;
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
		returnAST = typeSpec_AST;
	}
	
	public final void variableDefinitions(
		JavaNode mods, JavaNode t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode variableDefinitions_AST = null;
		
		try {      // for error handling
			variableDeclarator((JavaNode)getASTFactory().dupTree(mods),
                                                   (JavaNode)getASTFactory().dupTree(t));
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop65:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					variableDeclarator((JavaNode)getASTFactory().dupTree(mods),
                                                           (JavaNode)getASTFactory().dupTree(t));
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop65;
				}
				
			} while (true);
			}
			variableDefinitions_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDefinitions_AST;
	}
	
	public final void classTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode classTypeSpec_AST = null;
		JavaNode i_AST = null;
		Token  lb = null;
		JavaNode lb_AST = null;
		
		try {      // for error handling
			identifier();
			i_AST = (JavaNode)returnAST;
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop15:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (JavaNode)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop15;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				classTypeSpec_AST = (JavaNode)currentAST.root;
				
				if ( addImagNode ) {
				classTypeSpec_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.TYPE,STR_TYPE)).add(classTypeSpec_AST));
				}
				_references.defineType(i_AST.getText().intern(), classTypeSpec_AST);
				
				currentAST.root = classTypeSpec_AST;
				currentAST.child = classTypeSpec_AST!=null &&classTypeSpec_AST.getFirstChild()!=null ?
					classTypeSpec_AST.getFirstChild() : classTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			classTypeSpec_AST = (JavaNode)currentAST.root;
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
		returnAST = classTypeSpec_AST;
	}
	
	public final void builtInTypeSpec(
		boolean addImagNode
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode builtInTypeSpec_AST = null;
		Token  lb = null;
		JavaNode lb_AST = null;
		
		try {      // for error handling
			builtInType();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop18:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (JavaNode)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop18;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				builtInTypeSpec_AST = (JavaNode)currentAST.root;
				
				if ( addImagNode ) {
				builtInTypeSpec_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.TYPE,STR_TYPE)).add(builtInTypeSpec_AST));
				}
				
				currentAST.root = builtInTypeSpec_AST;
				currentAST.child = builtInTypeSpec_AST!=null &&builtInTypeSpec_AST.getFirstChild()!=null ?
					builtInTypeSpec_AST.getFirstChild() : builtInTypeSpec_AST;
				currentAST.advanceChildToEnd();
			}
			builtInTypeSpec_AST = (JavaNode)currentAST.root;
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
		returnAST = builtInTypeSpec_AST;
	}
	
	public final void identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode identifier_AST = null;
		Token  id1 = null;
		JavaNode id1_AST = null;
		Token  id2 = null;
		JavaNode id2_AST = null;
		
		_buf.setLength(0);
		
		
		try {      // for error handling
			id1 = LT(1);
			id1_AST = (JavaNode)astFactory.create(id1);
			match(IDENT);
			if ( inputState.guessing==0 ) {
				_buf.append(id1.getText());
			}
			{
			_loop23:
			do {
				if ((LA(1)==DOT)) {
					JavaNode tmp10_AST = null;
					tmp10_AST = (JavaNode)astFactory.create(LT(1));
					match(DOT);
					id2 = LT(1);
					id2_AST = (JavaNode)astFactory.create(id2);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						
						_buf.append('.');
						_buf.append(id2.getText());
						
						
					}
				}
				else {
					break _loop23;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				identifier_AST = (JavaNode)currentAST.root;
				
				identifier_AST = (JavaNode)astFactory.make( (new ASTArray(1)).add((JavaNode)astFactory.create(JavaTokenTypes.IDENT,_buf.toString())));
				String text = identifier_AST.getText().intern();
				addIdentifier(text);
				
				identifier_AST.setHiddenBefore(id1_AST.getHiddenBefore());
				identifier_AST.setHiddenAfter(id1_AST.getHiddenAfter());
				identifier_AST.startLine = id1_AST.startLine;
				identifier_AST.startColumn = id1_AST.startColumn;
				identifier_AST.endLine = id1_AST.startLine;
				identifier_AST.endColumn = id1_AST.startColumn + text.length();
				
				currentAST.root = identifier_AST;
				currentAST.child = identifier_AST!=null &&identifier_AST.getFirstChild()!=null ?
					identifier_AST.getFirstChild() : identifier_AST;
				currentAST.advanceChildToEnd();
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
		returnAST = identifier_AST;
	}
	
	public final void builtInType() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode builtInType_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_void:
			{
				JavaNode tmp11_AST = null;
				tmp11_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(LITERAL_void);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_boolean:
			{
				JavaNode tmp12_AST = null;
				tmp12_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp12_AST);
				match(LITERAL_boolean);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_byte:
			{
				JavaNode tmp13_AST = null;
				tmp13_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(LITERAL_byte);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_char:
			{
				JavaNode tmp14_AST = null;
				tmp14_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp14_AST);
				match(LITERAL_char);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_short:
			{
				JavaNode tmp15_AST = null;
				tmp15_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp15_AST);
				match(LITERAL_short);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_int:
			{
				JavaNode tmp16_AST = null;
				tmp16_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp16_AST);
				match(LITERAL_int);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_float:
			{
				JavaNode tmp17_AST = null;
				tmp17_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp17_AST);
				match(LITERAL_float);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_long:
			{
				JavaNode tmp18_AST = null;
				tmp18_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp18_AST);
				match(LITERAL_long);
				builtInType_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_double:
			{
				JavaNode tmp19_AST = null;
				tmp19_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp19_AST);
				match(LITERAL_double);
				builtInType_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_11);
			} else {
			  throw ex;
			}
		}
		returnAST = builtInType_AST;
	}
	
	public final void type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode type_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				type_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				type_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_12);
			} else {
			  throw ex;
			}
		}
		returnAST = type_AST;
	}
	
	public final void modifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode modifier_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_private:
			{
				JavaNode tmp20_AST = null;
				tmp20_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp20_AST);
				match(LITERAL_private);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_public:
			{
				JavaNode tmp21_AST = null;
				tmp21_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp21_AST);
				match(LITERAL_public);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_protected:
			{
				JavaNode tmp22_AST = null;
				tmp22_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp22_AST);
				match(LITERAL_protected);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_static:
			{
				JavaNode tmp23_AST = null;
				tmp23_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp23_AST);
				match(LITERAL_static);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_transient:
			{
				JavaNode tmp24_AST = null;
				tmp24_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp24_AST);
				match(LITERAL_transient);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case FINAL:
			{
				JavaNode tmp25_AST = null;
				tmp25_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp25_AST);
				match(FINAL);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case ABSTRACT:
			{
				JavaNode tmp26_AST = null;
				tmp26_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp26_AST);
				match(ABSTRACT);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_native:
			{
				JavaNode tmp27_AST = null;
				tmp27_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp27_AST);
				match(LITERAL_native);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_synchronized:
			{
				JavaNode tmp28_AST = null;
				tmp28_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp28_AST);
				match(LITERAL_synchronized);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_volatile:
			{
				JavaNode tmp29_AST = null;
				tmp29_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp29_AST);
				match(LITERAL_volatile);
				modifier_AST = (JavaNode)currentAST.root;
				break;
			}
			case STRICTFP:
			{
				JavaNode tmp30_AST = null;
				tmp30_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp30_AST);
				match(STRICTFP);
				modifier_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_13);
			} else {
			  throw ex;
			}
		}
		returnAST = modifier_AST;
	}
	
	public final void superClassClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode superClassClause_AST = null;
		Token  e = null;
		JavaNode e_AST = null;
		JavaNode id_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				e = LT(1);
				e_AST = (JavaNode)astFactory.create(e);
				match(LITERAL_extends);
				identifier();
				id_AST = (JavaNode)returnAST;
				break;
			}
			case LCURLY:
			case LITERAL_implements:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				superClassClause_AST = (JavaNode)currentAST.root;
				superClassClause_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.EXTENDS_CLAUSE,STR_EXTENDS_CLAUSE)).add(id_AST));
				if (e_AST != null)
				superClassClause_AST.setHiddenBefore(e_AST.getHiddenBefore());
				
				currentAST.root = superClassClause_AST;
				currentAST.child = superClassClause_AST!=null &&superClassClause_AST.getFirstChild()!=null ?
					superClassClause_AST.getFirstChild() : superClassClause_AST;
				currentAST.advanceChildToEnd();
			}
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
		returnAST = superClassClause_AST;
	}
	
	public final void implementsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode implementsClause_AST = null;
		Token  i = null;
		JavaNode i_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_implements:
			{
				i = LT(1);
				i_AST = (JavaNode)astFactory.create(i);
				match(LITERAL_implements);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop49:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						identifier();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop49;
					}
					
				} while (true);
				}
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				implementsClause_AST = (JavaNode)currentAST.root;
				implementsClause_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE")).add(implementsClause_AST));
				if (i_AST != null)
				implementsClause_AST.setHiddenBefore(i_AST.getHiddenBefore());
				
				currentAST.root = implementsClause_AST;
				currentAST.child = implementsClause_AST!=null &&implementsClause_AST.getFirstChild()!=null ?
					implementsClause_AST.getFirstChild() : implementsClause_AST;
				currentAST.advanceChildToEnd();
			}
			implementsClause_AST = (JavaNode)currentAST.root;
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
		returnAST = implementsClause_AST;
	}
	
	public final void classBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode classBlock_AST = null;
		Token  lc = null;
		JavaNode lc_AST = null;
		Token  rc = null;
		JavaNode rc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (JavaNode)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				_references.enterScope(References.SCOPE_CLASS);
			}
			{
			_loop41:
			do {
				switch ( LA(1)) {
				case LCURLY:
				case FINAL:
				case ABSTRACT:
				case STRICTFP:
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case IDENT:
				case LITERAL_private:
				case LITERAL_public:
				case LITERAL_protected:
				case LITERAL_static:
				case LITERAL_transient:
				case LITERAL_native:
				case LITERAL_synchronized:
				case LITERAL_volatile:
				case LITERAL_class:
				case LITERAL_interface:
				{
					field();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					JavaNode tmp32_AST = null;
					tmp32_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp32_AST);
					match(SEMI);
					break;
				}
				default:
				{
					break _loop41;
				}
				}
			} while (true);
			}
			rc = LT(1);
			rc_AST = (JavaNode)astFactory.create(rc);
			astFactory.addASTChild(currentAST, rc_AST);
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				_references.leaveScope();
			}
			if ( inputState.guessing==0 ) {
				lc_AST.setType(JavaTokenTypes.OBJBLOCK);
			}
			classBlock_AST = (JavaNode)currentAST.root;
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
		returnAST = classBlock_AST;
	}
	
	public final void interfaceExtends() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode interfaceExtends_AST = null;
		Token  e = null;
		JavaNode e_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_extends:
			{
				e = LT(1);
				e_AST = (JavaNode)astFactory.create(e);
				match(LITERAL_extends);
				identifier();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop45:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						identifier();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop45;
					}
					
				} while (true);
				}
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				interfaceExtends_AST = (JavaNode)currentAST.root;
				interfaceExtends_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.EXTENDS_CLAUSE,STR_EXTENDS_CLAUSE)).add(interfaceExtends_AST));
				currentAST.root = interfaceExtends_AST;
				currentAST.child = interfaceExtends_AST!=null &&interfaceExtends_AST.getFirstChild()!=null ?
					interfaceExtends_AST.getFirstChild() : interfaceExtends_AST;
				currentAST.advanceChildToEnd();
			}
			interfaceExtends_AST = (JavaNode)currentAST.root;
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
		returnAST = interfaceExtends_AST;
	}
	
	public final void field() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode field_AST = null;
		JavaNode mods_AST = null;
		JavaNode h_AST = null;
		JavaNode s_AST = null;
		JavaNode cd_AST = null;
		JavaNode id_AST = null;
		JavaNode t_AST = null;
		Token  idd = null;
		JavaNode idd_AST = null;
		Token  lp = null;
		JavaNode lp_AST = null;
		JavaNode param_AST = null;
		Token  rp = null;
		JavaNode rp_AST = null;
		JavaNode rt_AST = null;
		JavaNode tc_AST = null;
		JavaNode s2_AST = null;
		Token  semim = null;
		JavaNode semim_AST = null;
		JavaNode v_AST = null;
		Token  semi = null;
		JavaNode semi_AST = null;
		Token  stat = null;
		JavaNode stat_AST = null;
		JavaNode s3_AST = null;
		JavaNode s4_AST = null;
		
		try {      // for error handling
			if ((_tokenSet_13.member(LA(1))) && (_tokenSet_17.member(LA(2)))) {
				modifiers();
				mods_AST = (JavaNode)returnAST;
				{
				switch ( LA(1)) {
				case LITERAL_class:
				{
					classDefinition(mods_AST);
					cd_AST = (JavaNode)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (JavaNode)currentAST.root;
						field_AST = cd_AST;
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				case LITERAL_interface:
				{
					interfaceDefinition(mods_AST);
					id_AST = (JavaNode)returnAST;
					if ( inputState.guessing==0 ) {
						field_AST = (JavaNode)currentAST.root;
						field_AST = id_AST;
						currentAST.root = field_AST;
						currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
							field_AST.getFirstChild() : field_AST;
						currentAST.advanceChildToEnd();
					}
					break;
				}
				default:
					if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
						if ( inputState.guessing==0 ) {
							_references.enterScope();
						}
						ctorHead();
						h_AST = (JavaNode)returnAST;
						constructorBody();
						s_AST = (JavaNode)returnAST;
						if ( inputState.guessing==0 ) {
							_references.leaveScope();
						}
						if ( inputState.guessing==0 ) {
							field_AST = (JavaNode)currentAST.root;
							field_AST = (JavaNode)astFactory.make( (new ASTArray(4)).add((JavaNode)astFactory.create(JavaTokenTypes.CTOR_DEF,"CTOR_DEF")).add(mods_AST).add(h_AST).add(s_AST));
							attachStuffBeforeCtor(field_AST, mods_AST, h_AST);
							
							currentAST.root = field_AST;
							currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
								field_AST.getFirstChild() : field_AST;
							currentAST.advanceChildToEnd();
						}
					}
					else if (((LA(1) >= LITERAL_void && LA(1) <= IDENT)) && (_tokenSet_18.member(LA(2)))) {
						typeSpec(false);
						t_AST = (JavaNode)returnAST;
						{
						if ((LA(1)==IDENT) && (LA(2)==LPAREN)) {
							idd = LT(1);
							idd_AST = (JavaNode)astFactory.create(idd);
							match(IDENT);
							if ( inputState.guessing==0 ) {
								_references.enterScope();
							}
							lp = LT(1);
							lp_AST = (JavaNode)astFactory.create(lp);
							match(LPAREN);
							parameterDeclarationList();
							param_AST = (JavaNode)returnAST;
							rp = LT(1);
							rp_AST = (JavaNode)astFactory.create(rp);
							match(RPAREN);
							declaratorBrackets(t_AST);
							rt_AST = (JavaNode)returnAST;
							{
							switch ( LA(1)) {
							case LITERAL_throws:
							{
								throwsClause();
								tc_AST = (JavaNode)returnAST;
								break;
							}
							case LCURLY:
							case SEMI:
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
							switch ( LA(1)) {
							case LCURLY:
							{
								compoundStatement();
								s2_AST = (JavaNode)returnAST;
								break;
							}
							case SEMI:
							{
								semim = LT(1);
								semim_AST = (JavaNode)astFactory.create(semim);
								match(SEMI);
								if ( inputState.guessing==0 ) {
									_references.leaveScope();
								}
								break;
							}
							default:
							{
								throw new NoViableAltException(LT(1), getFilename());
							}
							}
							}
							if ( inputState.guessing==0 ) {
								field_AST = (JavaNode)currentAST.root;
								field_AST = (JavaNode)astFactory.make( (new ASTArray(10)).add((JavaNode)astFactory.create(JavaTokenTypes.METHOD_DEF,"METHOD_DEF")).add(mods_AST).add((JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.TYPE,STR_TYPE)).add(rt_AST))).add(idd_AST).add(lp_AST).add(param_AST).add(rp_AST).add(tc_AST).add(s2_AST).add(semim_AST));
								attachStuffBefore(field_AST, mods_AST, t_AST);
								
								currentAST.root = field_AST;
								currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
									field_AST.getFirstChild() : field_AST;
								currentAST.advanceChildToEnd();
							}
						}
						else if ((LA(1)==IDENT) && (_tokenSet_19.member(LA(2)))) {
							variableDefinitions(mods_AST,t_AST);
							v_AST = (JavaNode)returnAST;
							semi = LT(1);
							semi_AST = (JavaNode)astFactory.create(semi);
							match(SEMI);
							if ( inputState.guessing==0 ) {
								field_AST = (JavaNode)currentAST.root;
								
								field_AST = v_AST;
								field_AST.addChild(semi_AST);
								
								AST next = field_AST.getNextSibling();
								// HACK for multiple variable declaration in one statement
								//      e.g float  x, y, z;
								// the semicolon will only be added to the first statement so
								// we have to add it manually to all others
								if (next != null)
								{
								AST ssemi = NodeHelper.getFirstChild(field_AST, JavaTokenTypes.SEMI);
								
								for (AST var = next; var != null; var = var.getNextSibling())
								{
								var.addChild(astFactory.create(ssemi));
								}
								}
								
								
								currentAST.root = field_AST;
								currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
									field_AST.getFirstChild() : field_AST;
								currentAST.advanceChildToEnd();
							}
						}
						else {
							throw new NoViableAltException(LT(1), getFilename());
						}
						
						}
					}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
			}
			else if ((LA(1)==LITERAL_static) && (LA(2)==LCURLY)) {
				stat = LT(1);
				stat_AST = (JavaNode)astFactory.create(stat);
				match(LITERAL_static);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				compoundStatement();
				s3_AST = (JavaNode)returnAST;
				if ( inputState.guessing==0 ) {
					field_AST = (JavaNode)currentAST.root;
					
					field_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add(stat_AST).add(s3_AST));
					field_AST.setType(STATIC_INIT);
					
					currentAST.root = field_AST;
					currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
						field_AST.getFirstChild() : field_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else if ((LA(1)==LCURLY)) {
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				compoundStatement();
				s4_AST = (JavaNode)returnAST;
				if ( inputState.guessing==0 ) {
					field_AST = (JavaNode)currentAST.root;
					field_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.INSTANCE_INIT,STR_INSTANCE_INIT)).add(s4_AST));
					attachStuffBeforeCompoundStatement(field_AST, s4_AST);
					
					currentAST.root = field_AST;
					currentAST.child = field_AST!=null &&field_AST.getFirstChild()!=null ?
						field_AST.getFirstChild() : field_AST;
					currentAST.advanceChildToEnd();
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_20);
			} else {
			  throw ex;
			}
		}
		returnAST = field_AST;
	}
	
	public final void ctorHead() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode ctorHead_AST = null;
		
		try {      // for error handling
			JavaNode tmp34_AST = null;
			tmp34_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp34_AST);
			match(IDENT);
			JavaNode tmp35_AST = null;
			tmp35_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp35_AST);
			match(LPAREN);
			parameterDeclarationList();
			astFactory.addASTChild(currentAST, returnAST);
			JavaNode tmp36_AST = null;
			tmp36_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp36_AST);
			match(RPAREN);
			{
			switch ( LA(1)) {
			case LITERAL_throws:
			{
				throwsClause();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			ctorHead_AST = (JavaNode)currentAST.root;
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
		returnAST = ctorHead_AST;
	}
	
	public final void constructorBody() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode constructorBody_AST = null;
		Token  lc = null;
		JavaNode lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (JavaNode)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(JavaTokenTypes.SLIST);
			}
			{
			boolean synPredMatched58 = false;
			if (((_tokenSet_21.member(LA(1))) && (_tokenSet_22.member(LA(2))))) {
				int _m58 = mark();
				synPredMatched58 = true;
				inputState.guessing++;
				try {
					{
					explicitConstructorInvocation();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched58 = false;
				}
				rewind(_m58);
				inputState.guessing--;
			}
			if ( synPredMatched58 ) {
				explicitConstructorInvocation();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_23.member(LA(1))) && (_tokenSet_24.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			_loop60:
			do {
				if ((_tokenSet_25.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop60;
				}
				
			} while (true);
			}
			JavaNode tmp37_AST = null;
			tmp37_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp37_AST);
			match(RCURLY);
			constructorBody_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_20);
			} else {
			  throw ex;
			}
		}
		returnAST = constructorBody_AST;
	}
	
	public final void parameterDeclarationList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode parameterDeclarationList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FINAL:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			{
				parameterDeclaration();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop86:
				do {
					if ((LA(1)==COMMA)) {
						JavaNode tmp38_AST = null;
						tmp38_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp38_AST);
						match(COMMA);
						parameterDeclaration();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop86;
					}
					
				} while (true);
				}
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				parameterDeclarationList_AST = (JavaNode)currentAST.root;
				parameterDeclarationList_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.PARAMETERS,STR_PARAMETERS)).add(parameterDeclarationList_AST));
				currentAST.root = parameterDeclarationList_AST;
				currentAST.child = parameterDeclarationList_AST!=null &&parameterDeclarationList_AST.getFirstChild()!=null ?
					parameterDeclarationList_AST.getFirstChild() : parameterDeclarationList_AST;
				currentAST.advanceChildToEnd();
			}
			parameterDeclarationList_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclarationList_AST;
	}
	
	public final void declaratorBrackets(
		JavaNode typ
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode declaratorBrackets_AST = null;
		Token  lb = null;
		JavaNode lb_AST = null;
		
		try {      // for error handling
			if ( inputState.guessing==0 ) {
				declaratorBrackets_AST = (JavaNode)currentAST.root;
				declaratorBrackets_AST=typ;
				currentAST.root = declaratorBrackets_AST;
				currentAST.child = declaratorBrackets_AST!=null &&declaratorBrackets_AST.getFirstChild()!=null ?
					declaratorBrackets_AST.getFirstChild() : declaratorBrackets_AST;
				currentAST.advanceChildToEnd();
			}
			{
			_loop69:
			do {
				if ((LA(1)==LBRACK)) {
					lb = LT(1);
					lb_AST = (JavaNode)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
					}
					match(RBRACK);
				}
				else {
					break _loop69;
				}
				
			} while (true);
			}
			declaratorBrackets_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_27);
			} else {
			  throw ex;
			}
		}
		returnAST = declaratorBrackets_AST;
	}
	
	public final void throwsClause() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode throwsClause_AST = null;
		
		try {      // for error handling
			JavaNode tmp40_AST = null;
			tmp40_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp40_AST);
			match(LITERAL_throws);
			identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop82:
			do {
				if ((LA(1)==COMMA)) {
					JavaNode tmp41_AST = null;
					tmp41_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp41_AST);
					match(COMMA);
					identifier();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop82;
				}
				
			} while (true);
			}
			throwsClause_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_28);
			} else {
			  throw ex;
			}
		}
		returnAST = throwsClause_AST;
	}
	
	public final void compoundStatement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode compoundStatement_AST = null;
		Token  lc = null;
		JavaNode lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (JavaNode)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(JavaTokenTypes.SLIST);
			}
			{
			_loop92:
			do {
				if ((_tokenSet_25.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop92;
				}
				
			} while (true);
			}
			JavaNode tmp42_AST = null;
			tmp42_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp42_AST);
			match(RCURLY);
			if ( inputState.guessing==0 ) {
				_references.leaveScope();
			}
			compoundStatement_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_29);
			} else {
			  throw ex;
			}
		}
		returnAST = compoundStatement_AST;
	}
	
	public final void explicitConstructorInvocation() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode explicitConstructorInvocation_AST = null;
		Token  lp1 = null;
		JavaNode lp1_AST = null;
		Token  lp2 = null;
		JavaNode lp2_AST = null;
		Token  lp3 = null;
		JavaNode lp3_AST = null;
		Token t = null;
		
		try {      // for error handling
			{
			if ((LA(1)==LITERAL_this) && (LA(2)==LPAREN)) {
				JavaNode tmp43_AST = null;
				tmp43_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp43_AST);
				match(LITERAL_this);
				lp1 = LT(1);
				lp1_AST = (JavaNode)astFactory.create(lp1);
				astFactory.makeASTRoot(currentAST, lp1_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp44_AST = null;
				tmp44_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp44_AST);
				match(RPAREN);
				JavaNode tmp45_AST = null;
				tmp45_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp45_AST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp1_AST.setType(JavaTokenTypes.CTOR_CALL);
				}
			}
			else if ((LA(1)==LITERAL_super) && (LA(2)==LPAREN)) {
				JavaNode tmp46_AST = null;
				tmp46_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(LITERAL_super);
				lp2 = LT(1);
				lp2_AST = (JavaNode)astFactory.create(lp2);
				astFactory.makeASTRoot(currentAST, lp2_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp47_AST = null;
				tmp47_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp47_AST);
				match(RPAREN);
				JavaNode tmp48_AST = null;
				tmp48_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp2_AST.setType(JavaTokenTypes.SUPER_CTOR_CALL);
				}
			}
			else if ((_tokenSet_21.member(LA(1))) && (_tokenSet_22.member(LA(2)))) {
				t=primaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					if (t != null)addIdentifier(t.getText());
				}
				match(DOT);
				match(STR_supper);
				lp3 = LT(1);
				lp3_AST = (JavaNode)astFactory.create(lp3);
				astFactory.makeASTRoot(currentAST, lp3_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp51_AST = null;
				tmp51_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp51_AST);
				match(RPAREN);
				JavaNode tmp52_AST = null;
				tmp52_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp52_AST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					lp3_AST.setType(JavaTokenTypes.SUPER_CTOR_CALL);
				}
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			explicitConstructorInvocation_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_23);
			} else {
			  throw ex;
			}
		}
		returnAST = explicitConstructorInvocation_AST;
	}
	
	public final void statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode statement_AST = null;
		JavaNode decl_AST = null;
		Token  semi1 = null;
		JavaNode semi1_AST = null;
		JavaNode e_AST = null;
		Token  semi2 = null;
		JavaNode semi2_AST = null;
		JavaNode m_AST = null;
		Token  c = null;
		JavaNode c_AST = null;
		Token  bid = null;
		JavaNode bid_AST = null;
		Token  cid = null;
		JavaNode cid_AST = null;
		Token  synBlock = null;
		JavaNode synBlock_AST = null;
		Token  emptyStat = null;
		JavaNode emptyStat_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LCURLY:
			{
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				compoundStatement();
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_if:
			{
				JavaNode tmp53_AST = null;
				tmp53_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp53_AST);
				match(LITERAL_if);
				JavaNode tmp54_AST = null;
				tmp54_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp54_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp55_AST = null;
				tmp55_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp55_AST);
				match(RPAREN);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					_references.leaveScope();
				}
				{
				if ((LA(1)==LITERAL_else) && (_tokenSet_25.member(LA(2)))) {
					JavaNode tmp56_AST = null;
					tmp56_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp56_AST);
					match(LITERAL_else);
					if ( inputState.guessing==0 ) {
						_references.enterScope();
					}
					statement();
					astFactory.addASTChild(currentAST, returnAST);
					if ( inputState.guessing==0 ) {
						_references.leaveScope();
					}
				}
				else if ((_tokenSet_30.member(LA(1))) && (_tokenSet_31.member(LA(2)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_for:
			{
				JavaNode tmp57_AST = null;
				tmp57_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp57_AST);
				match(LITERAL_for);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				JavaNode tmp58_AST = null;
				tmp58_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp58_AST);
				match(LPAREN);
				forInit();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp59_AST = null;
				tmp59_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp59_AST);
				match(SEMI);
				forCond();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp60_AST = null;
				tmp60_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp60_AST);
				match(SEMI);
				forIter();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp61_AST = null;
				tmp61_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp61_AST);
				match(RPAREN);
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					_references.leaveScope();
				}
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_while:
			{
				JavaNode tmp62_AST = null;
				tmp62_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp62_AST);
				match(LITERAL_while);
				JavaNode tmp63_AST = null;
				tmp63_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp63_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp64_AST = null;
				tmp64_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp64_AST);
				match(RPAREN);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					_references.leaveScope();
				}
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_do:
			{
				JavaNode tmp65_AST = null;
				tmp65_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp65_AST);
				match(LITERAL_do);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				statement();
				astFactory.addASTChild(currentAST, returnAST);
				if ( inputState.guessing==0 ) {
					_references.leaveScope();
				}
				JavaNode tmp66_AST = null;
				tmp66_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp66_AST);
				match(LITERAL_while);
				JavaNode tmp67_AST = null;
				tmp67_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp67_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp68_AST = null;
				tmp68_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp68_AST);
				match(RPAREN);
				JavaNode tmp69_AST = null;
				tmp69_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp69_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_break:
			{
				JavaNode tmp70_AST = null;
				tmp70_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp70_AST);
				match(LITERAL_break);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					bid = LT(1);
					bid_AST = (JavaNode)astFactory.create(bid);
					astFactory.addASTChild(currentAST, bid_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						addIdentifier(bid.getText());
					}
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaNode tmp71_AST = null;
				tmp71_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp71_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_assert:
			{
				JavaNode tmp72_AST = null;
				tmp72_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp72_AST);
				match(LITERAL_assert);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COLON:
				{
					match(COLON);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaNode tmp74_AST = null;
				tmp74_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp74_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_continue:
			{
				JavaNode tmp75_AST = null;
				tmp75_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp75_AST);
				match(LITERAL_continue);
				{
				switch ( LA(1)) {
				case IDENT:
				{
					cid = LT(1);
					cid_AST = (JavaNode)astFactory.create(cid);
					astFactory.addASTChild(currentAST, cid_AST);
					match(IDENT);
					if ( inputState.guessing==0 ) {
						addIdentifier(cid.getText());
					}
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaNode tmp76_AST = null;
				tmp76_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp76_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_return:
			{
				JavaNode tmp77_AST = null;
				tmp77_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp77_AST);
				match(LITERAL_return);
				{
				switch ( LA(1)) {
				case LITERAL_void:
				case LITERAL_boolean:
				case LITERAL_byte:
				case LITERAL_char:
				case LITERAL_short:
				case LITERAL_int:
				case LITERAL_float:
				case LITERAL_long:
				case LITERAL_double:
				case IDENT:
				case LPAREN:
				case LITERAL_this:
				case LITERAL_super:
				case PLUS:
				case MINUS:
				case INC:
				case DEC:
				case BNOT:
				case LNOT:
				case LITERAL_true:
				case LITERAL_false:
				case LITERAL_null:
				case LITERAL_new:
				case NUM_INT:
				case CHAR_LITERAL:
				case STRING_LITERAL:
				case NUM_FLOAT:
				case NUM_LONG:
				case NUM_DOUBLE:
				{
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case SEMI:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				JavaNode tmp78_AST = null;
				tmp78_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp78_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_switch:
			{
				JavaNode tmp79_AST = null;
				tmp79_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp79_AST);
				match(LITERAL_switch);
				JavaNode tmp80_AST = null;
				tmp80_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp80_AST);
				match(LPAREN);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp81_AST = null;
				tmp81_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp81_AST);
				match(RPAREN);
				JavaNode tmp82_AST = null;
				tmp82_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp82_AST);
				match(LCURLY);
				if ( inputState.guessing==0 ) {
					_references.enterScope();
				}
				{
				_loop102:
				do {
					if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default)) {
						casesGroup();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop102;
					}
					
				} while (true);
				}
				if ( inputState.guessing==0 ) {
					_references.leaveScope();
				}
				JavaNode tmp83_AST = null;
				tmp83_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp83_AST);
				match(RCURLY);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_try:
			{
				tryBlock();
				astFactory.addASTChild(currentAST, returnAST);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_throw:
			{
				JavaNode tmp84_AST = null;
				tmp84_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp84_AST);
				match(LITERAL_throw);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp85_AST = null;
				tmp85_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp85_AST);
				match(SEMI);
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			case SEMI:
			{
				emptyStat = LT(1);
				emptyStat_AST = (JavaNode)astFactory.create(emptyStat);
				astFactory.addASTChild(currentAST, emptyStat_AST);
				match(SEMI);
				if ( inputState.guessing==0 ) {
					emptyStat_AST.setType(JavaTokenTypes.EMPTY_STAT);
				}
				statement_AST = (JavaNode)currentAST.root;
				break;
			}
			default:
				boolean synPredMatched95 = false;
				if (((_tokenSet_32.member(LA(1))) && (_tokenSet_33.member(LA(2))))) {
					int _m95 = mark();
					synPredMatched95 = true;
					inputState.guessing++;
					try {
						{
						declaration();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched95 = false;
					}
					rewind(_m95);
					inputState.guessing--;
				}
				if ( synPredMatched95 ) {
					declaration();
					decl_AST = (JavaNode)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					semi1 = LT(1);
					semi1_AST = (JavaNode)astFactory.create(semi1);
					match(SEMI);
					if ( inputState.guessing==0 ) {
						
						// add semicolon to the AST
						decl_AST.addChild(semi1_AST);
						
						AST next = currentAST.root.getNextSibling();
						
						// HACK for multiple variable declaration in one statement
						//      e.g float x, y, z;
						// the semicolon will only be added to the first statement so
						// we have to add it manually to all others
						if (next != null)
						{
						AST semi = NodeHelper.getFirstChild(currentAST.root, JavaTokenTypes.SEMI);
						
						for (AST var = next; var != null; var = var.getNextSibling())
						{
						var.addChild(astFactory.create(semi));
						}
						}
						
					}
					statement_AST = (JavaNode)currentAST.root;
				}
				else if ((_tokenSet_34.member(LA(1))) && (_tokenSet_35.member(LA(2)))) {
					expression();
					e_AST = (JavaNode)returnAST;
					astFactory.addASTChild(currentAST, returnAST);
					semi2 = LT(1);
					semi2_AST = (JavaNode)astFactory.create(semi2);
					match(SEMI);
					if ( inputState.guessing==0 ) {
						e_AST.addChild(semi2_AST);
					}
					statement_AST = (JavaNode)currentAST.root;
				}
				else if ((_tokenSet_36.member(LA(1))) && (_tokenSet_37.member(LA(2)))) {
					modifiers();
					m_AST = (JavaNode)returnAST;
					classDefinition(m_AST);
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (JavaNode)currentAST.root;
				}
				else if ((LA(1)==IDENT) && (LA(2)==COLON)) {
					JavaNode tmp86_AST = null;
					tmp86_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp86_AST);
					match(IDENT);
					c = LT(1);
					c_AST = (JavaNode)astFactory.create(c);
					astFactory.makeASTRoot(currentAST, c_AST);
					match(COLON);
					if ( inputState.guessing==0 ) {
						c_AST.setType(JavaTokenTypes.LABELED_STAT);
					}
					statement();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (JavaNode)currentAST.root;
				}
				else if ((LA(1)==LITERAL_synchronized) && (LA(2)==LPAREN)) {
					synBlock = LT(1);
					synBlock_AST = (JavaNode)astFactory.create(synBlock);
					astFactory.makeASTRoot(currentAST, synBlock_AST);
					match(LITERAL_synchronized);
					JavaNode tmp87_AST = null;
					tmp87_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp87_AST);
					match(LPAREN);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					JavaNode tmp88_AST = null;
					tmp88_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp88_AST);
					match(RPAREN);
					if ( inputState.guessing==0 ) {
						synBlock_AST.setType(JavaTokenTypes.SYNBLOCK);
						_references.enterScope();
					}
					compoundStatement();
					astFactory.addASTChild(currentAST, returnAST);
					statement_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = statement_AST;
	}
	
	public final void argList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode argList_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RPAREN:
			{
				if ( inputState.guessing==0 ) {
					argList_AST = (JavaNode)currentAST.root;
					argList_AST = (JavaNode)astFactory.create(JavaTokenTypes.ELIST,STR_ELIST);
					currentAST.root = argList_AST;
					currentAST.child = argList_AST!=null &&argList_AST.getFirstChild()!=null ?
						argList_AST.getFirstChild() : argList_AST;
					currentAST.advanceChildToEnd();
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			argList_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
		returnAST = argList_AST;
	}
	
	public final Token  primaryExpression() throws RecognitionException, TokenStreamException {
		Token t;
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode primaryExpression_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		Token  th = null;
		JavaNode th_AST = null;
		Token  lp = null;
		JavaNode lp_AST = null;
		JavaNode a_AST = null;
		Token  rp = null;
		JavaNode rp_AST = null;
		Token  lbt = null;
		JavaNode lbt_AST = null;
		t=null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENT:
			{
				id = LT(1);
				id_AST = (JavaNode)astFactory.create(id);
				astFactory.addASTChild(currentAST, id_AST);
				match(IDENT);
				if ( inputState.guessing==0 ) {
					t = (Token)id;
				}
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				constant();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_true:
			{
				JavaNode tmp89_AST = null;
				tmp89_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp89_AST);
				match(LITERAL_true);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_false:
			{
				JavaNode tmp90_AST = null;
				tmp90_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp90_AST);
				match(LITERAL_false);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_this:
			{
				th = LT(1);
				th_AST = (JavaNode)astFactory.create(th);
				astFactory.addASTChild(currentAST, th_AST);
				match(LITERAL_this);
				if ( inputState.guessing==0 ) {
					t = (Token)th;
				}
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_null:
			{
				JavaNode tmp91_AST = null;
				tmp91_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp91_AST);
				match(LITERAL_null);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_new:
			{
				newExpression();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LPAREN:
			{
				lp = LT(1);
				lp_AST = (JavaNode)astFactory.create(lp);
				astFactory.addASTChild(currentAST, lp_AST);
				match(LPAREN);
				assignmentExpression();
				a_AST = (JavaNode)returnAST;
				astFactory.addASTChild(currentAST, returnAST);
				rp = LT(1);
				rp_AST = (JavaNode)astFactory.create(rp);
				astFactory.addASTChild(currentAST, rp_AST);
				match(RPAREN);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_super:
			{
				JavaNode tmp92_AST = null;
				tmp92_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp92_AST);
				match(LITERAL_super);
				primaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			{
				builtInType();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop186:
				do {
					if ((LA(1)==LBRACK)) {
						lbt = LT(1);
						lbt_AST = (JavaNode)astFactory.create(lbt);
						astFactory.makeASTRoot(currentAST, lbt_AST);
						match(LBRACK);
						if ( inputState.guessing==0 ) {
							lbt_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
						}
						match(RBRACK);
					}
					else {
						break _loop186;
					}
					
				} while (true);
				}
				JavaNode tmp94_AST = null;
				tmp94_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp94_AST);
				match(DOT);
				JavaNode tmp95_AST = null;
				tmp95_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp95_AST);
				match(LITERAL_class);
				primaryExpression_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_38);
			} else {
			  throw ex;
			}
		}
		returnAST = primaryExpression_AST;
		return t;
	}
	
/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
	public final void variableDeclarator(
		JavaNode mods, JavaNode t
	) throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode variableDeclarator_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		JavaNode d_AST = null;
		JavaNode v_AST = null;
		
		try {      // for error handling
			id = LT(1);
			id_AST = (JavaNode)astFactory.create(id);
			match(IDENT);
			declaratorBrackets(t);
			d_AST = (JavaNode)returnAST;
			varInitializer();
			v_AST = (JavaNode)returnAST;
			if ( inputState.guessing==0 ) {
				variableDeclarator_AST = (JavaNode)currentAST.root;
				
				variableDeclarator_AST = (JavaNode)astFactory.make( (new ASTArray(5)).add((JavaNode)astFactory.create(JavaTokenTypes.VARIABLE_DEF,"VARIABLE_DEF")).add(mods).add((JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.TYPE,STR_TYPE)).add(d_AST))).add(id_AST).add(v_AST));
				attachStuffBefore(variableDeclarator_AST, mods, t);
				_references.defineVariable(id.getText().intern(), variableDeclarator_AST);
				
				currentAST.root = variableDeclarator_AST;
				currentAST.child = variableDeclarator_AST!=null &&variableDeclarator_AST.getFirstChild()!=null ?
					variableDeclarator_AST.getFirstChild() : variableDeclarator_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		returnAST = variableDeclarator_AST;
	}
	
	public final void varInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode varInitializer_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case ASSIGN:
			{
				JavaNode tmp96_AST = null;
				tmp96_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp96_AST);
				match(ASSIGN);
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			case COMMA:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			varInitializer_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_39);
			} else {
			  throw ex;
			}
		}
		returnAST = varInitializer_AST;
	}
	
	public final void initializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode initializer_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (JavaNode)currentAST.root;
				break;
			}
			case LCURLY:
			{
				arrayInitializer();
				astFactory.addASTChild(currentAST, returnAST);
				initializer_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_40);
			} else {
			  throw ex;
			}
		}
		returnAST = initializer_AST;
	}
	
	public final void arrayInitializer() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode arrayInitializer_AST = null;
		Token  lc = null;
		JavaNode lc_AST = null;
		
		try {      // for error handling
			lc = LT(1);
			lc_AST = (JavaNode)astFactory.create(lc);
			astFactory.makeASTRoot(currentAST, lc_AST);
			match(LCURLY);
			if ( inputState.guessing==0 ) {
				lc_AST.setType(JavaTokenTypes.ARRAY_INIT);
			}
			{
			switch ( LA(1)) {
			case LCURLY:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				initializer();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop75:
				do {
					if ((LA(1)==COMMA) && (_tokenSet_41.member(LA(2)))) {
						JavaNode tmp97_AST = null;
						tmp97_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp97_AST);
						match(COMMA);
						initializer();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop75;
					}
					
				} while (true);
				}
				{
				switch ( LA(1)) {
				case COMMA:
				{
					JavaNode tmp98_AST = null;
					tmp98_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp98_AST);
					match(COMMA);
					break;
				}
				case RCURLY:
				{
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
			case RCURLY:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			JavaNode tmp99_AST = null;
			tmp99_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp99_AST);
			match(RCURLY);
			arrayInitializer_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_38);
			} else {
			  throw ex;
			}
		}
		returnAST = arrayInitializer_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode expression_AST = null;
		
		try {      // for error handling
			assignmentExpression();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				expression_AST = (JavaNode)currentAST.root;
				expression_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.EXPR,STR_EXPR)).add(expression_AST));
				currentAST.root = expression_AST;
				currentAST.child = expression_AST!=null &&expression_AST.getFirstChild()!=null ?
					expression_AST.getFirstChild() : expression_AST;
				currentAST.advanceChildToEnd();
			}
			expression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_42);
			} else {
			  throw ex;
			}
		}
		returnAST = expression_AST;
	}
	
	public final void parameterDeclaration() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode parameterDeclaration_AST = null;
		JavaNode pm_AST = null;
		JavaNode t_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		JavaNode pd_AST = null;
		
		try {      // for error handling
			parameterModifier();
			pm_AST = (JavaNode)returnAST;
			typeSpec(false);
			t_AST = (JavaNode)returnAST;
			id = LT(1);
			id_AST = (JavaNode)astFactory.create(id);
			match(IDENT);
			declaratorBrackets(t_AST);
			pd_AST = (JavaNode)returnAST;
			if ( inputState.guessing==0 ) {
				parameterDeclaration_AST = (JavaNode)currentAST.root;
				parameterDeclaration_AST = (JavaNode)astFactory.make( (new ASTArray(4)).add((JavaNode)astFactory.create(JavaTokenTypes.PARAMETER_DEF,"PARAMETER_DEF")).add(pm_AST).add((JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.TYPE,STR_TYPE)).add(pd_AST))).add(id_AST));
				
				_references.defineVariable(id.getText().intern(), parameterDeclaration_AST);
				
				currentAST.root = parameterDeclaration_AST;
				currentAST.child = parameterDeclaration_AST!=null &&parameterDeclaration_AST.getFirstChild()!=null ?
					parameterDeclaration_AST.getFirstChild() : parameterDeclaration_AST;
				currentAST.advanceChildToEnd();
			}
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_43);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterDeclaration_AST;
	}
	
	public final void parameterModifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode parameterModifier_AST = null;
		Token  f = null;
		JavaNode f_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case FINAL:
			{
				f = LT(1);
				f_AST = (JavaNode)astFactory.create(f);
				astFactory.addASTChild(currentAST, f_AST);
				match(FINAL);
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				parameterModifier_AST = (JavaNode)currentAST.root;
				parameterModifier_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.MODIFIERS,STR_MODIFIERS)).add(f_AST));
				currentAST.root = parameterModifier_AST;
				currentAST.child = parameterModifier_AST!=null &&parameterModifier_AST.getFirstChild()!=null ?
					parameterModifier_AST.getFirstChild() : parameterModifier_AST;
				currentAST.advanceChildToEnd();
			}
			parameterModifier_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_44);
			} else {
			  throw ex;
			}
		}
		returnAST = parameterModifier_AST;
	}
	
	public final void forInit() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode forInit_AST = null;
		
		try {      // for error handling
			{
			boolean synPredMatched114 = false;
			if (((_tokenSet_32.member(LA(1))) && (_tokenSet_33.member(LA(2))))) {
				int _m114 = mark();
				synPredMatched114 = true;
				inputState.guessing++;
				try {
					{
					declaration();
					}
				}
				catch (RecognitionException pe) {
					synPredMatched114 = false;
				}
				rewind(_m114);
				inputState.guessing--;
			}
			if ( synPredMatched114 ) {
				declaration();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_34.member(LA(1))) && (_tokenSet_45.member(LA(2)))) {
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((LA(1)==SEMI)) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			if ( inputState.guessing==0 ) {
				forInit_AST = (JavaNode)currentAST.root;
				forInit_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.FOR_INIT,"FOR_INIT")).add(forInit_AST));
				currentAST.root = forInit_AST;
				currentAST.child = forInit_AST!=null &&forInit_AST.getFirstChild()!=null ?
					forInit_AST.getFirstChild() : forInit_AST;
				currentAST.advanceChildToEnd();
			}
			forInit_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = forInit_AST;
	}
	
	public final void forCond() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode forCond_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case SEMI:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				forCond_AST = (JavaNode)currentAST.root;
				forCond_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.FOR_CONDITION,"FOR_CONDITION")).add(forCond_AST));
				currentAST.root = forCond_AST;
				currentAST.child = forCond_AST!=null &&forCond_AST.getFirstChild()!=null ?
					forCond_AST.getFirstChild() : forCond_AST;
				currentAST.advanceChildToEnd();
			}
			forCond_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_4);
			} else {
			  throw ex;
			}
		}
		returnAST = forCond_AST;
	}
	
	public final void forIter() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode forIter_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				expressionList();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RPAREN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				forIter_AST = (JavaNode)currentAST.root;
				forIter_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.FOR_ITERATOR,"FOR_ITERATOR")).add(forIter_AST));
				currentAST.root = forIter_AST;
				currentAST.child = forIter_AST!=null &&forIter_AST.getFirstChild()!=null ?
					forIter_AST.getFirstChild() : forIter_AST;
				currentAST.advanceChildToEnd();
			}
			forIter_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_26);
			} else {
			  throw ex;
			}
		}
		returnAST = forIter_AST;
	}
	
	public final void casesGroup() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode casesGroup_AST = null;
		
		try {      // for error handling
			{
			int _cnt105=0;
			_loop105:
			do {
				if ((LA(1)==LITERAL_case||LA(1)==LITERAL_default) && (_tokenSet_46.member(LA(2)))) {
					aCase();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt105>=1 ) { break _loop105; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt105++;
			} while (true);
			}
			caseSList();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				casesGroup_AST = (JavaNode)currentAST.root;
				casesGroup_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.CASE_GROUP,"CASE_GROUP")).add(casesGroup_AST));
				currentAST.root = casesGroup_AST;
				currentAST.child = casesGroup_AST!=null &&casesGroup_AST.getFirstChild()!=null ?
					casesGroup_AST.getFirstChild() : casesGroup_AST;
				currentAST.advanceChildToEnd();
			}
			casesGroup_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = casesGroup_AST;
	}
	
	public final void tryBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode tryBlock_AST = null;
		
		try {      // for error handling
			JavaNode tmp100_AST = null;
			tmp100_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp100_AST);
			match(LITERAL_try);
			if ( inputState.guessing==0 ) {
				_references.enterScope();
			}
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop121:
			do {
				if ((LA(1)==LITERAL_catch)) {
					handler();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop121;
				}
				
			} while (true);
			}
			{
			switch ( LA(1)) {
			case LITERAL_finally:
			{
				finallyBlock();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LCURLY:
			case RCURLY:
			case FINAL:
			case ABSTRACT:
			case STRICTFP:
			case SEMI:
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LITERAL_private:
			case LITERAL_public:
			case LITERAL_protected:
			case LITERAL_static:
			case LITERAL_transient:
			case LITERAL_native:
			case LITERAL_synchronized:
			case LITERAL_volatile:
			case LITERAL_class:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_if:
			case LITERAL_else:
			case LITERAL_for:
			case LITERAL_while:
			case LITERAL_do:
			case LITERAL_break:
			case LITERAL_assert:
			case LITERAL_continue:
			case LITERAL_return:
			case LITERAL_switch:
			case LITERAL_throw:
			case LITERAL_case:
			case LITERAL_default:
			case LITERAL_try:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			tryBlock_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = tryBlock_AST;
	}
	
	public final void aCase() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode aCase_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_case:
			{
				JavaNode tmp101_AST = null;
				tmp101_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp101_AST);
				match(LITERAL_case);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_default:
			{
				JavaNode tmp102_AST = null;
				tmp102_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
				match(LITERAL_default);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			JavaNode tmp103_AST = null;
			tmp103_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp103_AST);
			match(COLON);
			aCase_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_48);
			} else {
			  throw ex;
			}
		}
		returnAST = aCase_AST;
	}
	
	public final void caseSList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode caseSList_AST = null;
		
		try {      // for error handling
			{
			_loop110:
			do {
				if ((_tokenSet_25.member(LA(1)))) {
					statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop110;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				caseSList_AST = (JavaNode)currentAST.root;
				caseSList_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.CASESLIST,STR_CASESLIST)).add(caseSList_AST));
				currentAST.root = caseSList_AST;
				currentAST.child = caseSList_AST!=null &&caseSList_AST.getFirstChild()!=null ?
					caseSList_AST.getFirstChild() : caseSList_AST;
				currentAST.advanceChildToEnd();
			}
			caseSList_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_47);
			} else {
			  throw ex;
			}
		}
		returnAST = caseSList_AST;
	}
	
	public final void expressionList() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode expressionList_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop128:
			do {
				if ((LA(1)==COMMA)) {
					JavaNode tmp104_AST = null;
					tmp104_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp104_AST);
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop128;
				}
				
			} while (true);
			}
			if ( inputState.guessing==0 ) {
				expressionList_AST = (JavaNode)currentAST.root;
				expressionList_AST = (JavaNode)astFactory.make( (new ASTArray(2)).add((JavaNode)astFactory.create(JavaTokenTypes.ELIST,STR_ELIST)).add(expressionList_AST));
				currentAST.root = expressionList_AST;
				currentAST.child = expressionList_AST!=null &&expressionList_AST.getFirstChild()!=null ?
					expressionList_AST.getFirstChild() : expressionList_AST;
				currentAST.advanceChildToEnd();
			}
			expressionList_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_49);
			} else {
			  throw ex;
			}
		}
		returnAST = expressionList_AST;
	}
	
	public final void handler() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode handler_AST = null;
		
		try {      // for error handling
			JavaNode tmp105_AST = null;
			tmp105_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp105_AST);
			match(LITERAL_catch);
			JavaNode tmp106_AST = null;
			tmp106_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp106_AST);
			match(LPAREN);
			if ( inputState.guessing==0 ) {
				_references.enterScope();
			}
			parameterDeclaration();
			astFactory.addASTChild(currentAST, returnAST);
			JavaNode tmp107_AST = null;
			tmp107_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp107_AST);
			match(RPAREN);
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			handler_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_50);
			} else {
			  throw ex;
			}
		}
		returnAST = handler_AST;
	}
	
	public final void finallyBlock() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode finallyBlock_AST = null;
		
		try {      // for error handling
			JavaNode tmp108_AST = null;
			tmp108_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp108_AST);
			match(LITERAL_finally);
			if ( inputState.guessing==0 ) {
				_references.enterScope();
			}
			compoundStatement();
			astFactory.addASTChild(currentAST, returnAST);
			finallyBlock_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_30);
			} else {
			  throw ex;
			}
		}
		returnAST = finallyBlock_AST;
	}
	
	public final void assignmentExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode assignmentExpression_AST = null;
		
		try {      // for error handling
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case ASSIGN:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			{
				{
				switch ( LA(1)) {
				case ASSIGN:
				{
					JavaNode tmp109_AST = null;
					tmp109_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp109_AST);
					match(ASSIGN);
					break;
				}
				case PLUS_ASSIGN:
				{
					JavaNode tmp110_AST = null;
					tmp110_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp110_AST);
					match(PLUS_ASSIGN);
					break;
				}
				case MINUS_ASSIGN:
				{
					JavaNode tmp111_AST = null;
					tmp111_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp111_AST);
					match(MINUS_ASSIGN);
					break;
				}
				case STAR_ASSIGN:
				{
					JavaNode tmp112_AST = null;
					tmp112_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp112_AST);
					match(STAR_ASSIGN);
					break;
				}
				case DIV_ASSIGN:
				{
					JavaNode tmp113_AST = null;
					tmp113_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp113_AST);
					match(DIV_ASSIGN);
					break;
				}
				case MOD_ASSIGN:
				{
					JavaNode tmp114_AST = null;
					tmp114_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp114_AST);
					match(MOD_ASSIGN);
					break;
				}
				case SR_ASSIGN:
				{
					JavaNode tmp115_AST = null;
					tmp115_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp115_AST);
					match(SR_ASSIGN);
					break;
				}
				case BSR_ASSIGN:
				{
					JavaNode tmp116_AST = null;
					tmp116_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp116_AST);
					match(BSR_ASSIGN);
					break;
				}
				case SL_ASSIGN:
				{
					JavaNode tmp117_AST = null;
					tmp117_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp117_AST);
					match(SL_ASSIGN);
					break;
				}
				case BAND_ASSIGN:
				{
					JavaNode tmp118_AST = null;
					tmp118_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp118_AST);
					match(BAND_ASSIGN);
					break;
				}
				case BXOR_ASSIGN:
				{
					JavaNode tmp119_AST = null;
					tmp119_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp119_AST);
					match(BXOR_ASSIGN);
					break;
				}
				case BOR_ASSIGN:
				{
					JavaNode tmp120_AST = null;
					tmp120_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp120_AST);
					match(BOR_ASSIGN);
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
			case SEMI:
			case RBRACK:
			case COMMA:
			case RPAREN:
			case COLON:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			assignmentExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_42);
			} else {
			  throw ex;
			}
		}
		returnAST = assignmentExpression_AST;
	}
	
	public final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode conditionalExpression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case QUESTION:
			{
				JavaNode tmp121_AST = null;
				tmp121_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp121_AST);
				match(QUESTION);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp122_AST = null;
				tmp122_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp122_AST);
				match(COLON);
				conditionalExpression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RCURLY:
			case SEMI:
			case RBRACK:
			case COMMA:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			conditionalExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_51);
			} else {
			  throw ex;
			}
		}
		returnAST = conditionalExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop136:
			do {
				if ((LA(1)==LOR)) {
					JavaNode tmp123_AST = null;
					tmp123_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp123_AST);
					match(LOR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop136;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_52);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode logicalAndExpression_AST = null;
		
		try {      // for error handling
			inclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop139:
			do {
				if ((LA(1)==LAND)) {
					JavaNode tmp124_AST = null;
					tmp124_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp124_AST);
					match(LAND);
					inclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop139;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_53);
			} else {
			  throw ex;
			}
		}
		returnAST = logicalAndExpression_AST;
	}
	
	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode inclusiveOrExpression_AST = null;
		
		try {      // for error handling
			exclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop142:
			do {
				if ((LA(1)==BOR)) {
					JavaNode tmp125_AST = null;
					tmp125_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp125_AST);
					match(BOR);
					exclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop142;
				}
				
			} while (true);
			}
			inclusiveOrExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_54);
			} else {
			  throw ex;
			}
		}
		returnAST = inclusiveOrExpression_AST;
	}
	
	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode exclusiveOrExpression_AST = null;
		
		try {      // for error handling
			andExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop145:
			do {
				if ((LA(1)==BXOR)) {
					JavaNode tmp126_AST = null;
					tmp126_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp126_AST);
					match(BXOR);
					andExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop145;
				}
				
			} while (true);
			}
			exclusiveOrExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_55);
			} else {
			  throw ex;
			}
		}
		returnAST = exclusiveOrExpression_AST;
	}
	
	public final void andExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode andExpression_AST = null;
		
		try {      // for error handling
			equalityExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop148:
			do {
				if ((LA(1)==BAND)) {
					JavaNode tmp127_AST = null;
					tmp127_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp127_AST);
					match(BAND);
					equalityExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop148;
				}
				
			} while (true);
			}
			andExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_56);
			} else {
			  throw ex;
			}
		}
		returnAST = andExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode equalityExpression_AST = null;
		
		try {      // for error handling
			relationalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop152:
			do {
				if ((LA(1)==NOT_EQUAL||LA(1)==EQUAL)) {
					{
					switch ( LA(1)) {
					case NOT_EQUAL:
					{
						JavaNode tmp128_AST = null;
						tmp128_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp128_AST);
						match(NOT_EQUAL);
						break;
					}
					case EQUAL:
					{
						JavaNode tmp129_AST = null;
						tmp129_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp129_AST);
						match(EQUAL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop152;
				}
				
			} while (true);
			}
			equalityExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_57);
			} else {
			  throw ex;
			}
		}
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode relationalExpression_AST = null;
		
		try {      // for error handling
			shiftExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case RCURLY:
			case SEMI:
			case RBRACK:
			case COMMA:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			{
				{
				_loop157:
				do {
					if (((LA(1) >= LT && LA(1) <= GE))) {
						{
						switch ( LA(1)) {
						case LT:
						{
							JavaNode tmp130_AST = null;
							tmp130_AST = (JavaNode)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp130_AST);
							match(LT);
							break;
						}
						case GT:
						{
							JavaNode tmp131_AST = null;
							tmp131_AST = (JavaNode)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp131_AST);
							match(GT);
							break;
						}
						case LE:
						{
							JavaNode tmp132_AST = null;
							tmp132_AST = (JavaNode)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp132_AST);
							match(LE);
							break;
						}
						case GE:
						{
							JavaNode tmp133_AST = null;
							tmp133_AST = (JavaNode)astFactory.create(LT(1));
							astFactory.makeASTRoot(currentAST, tmp133_AST);
							match(GE);
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						shiftExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop157;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_instanceof:
			{
				JavaNode tmp134_AST = null;
				tmp134_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp134_AST);
				match(LITERAL_instanceof);
				typeSpec(true);
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			relationalExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_58);
			} else {
			  throw ex;
			}
		}
		returnAST = relationalExpression_AST;
	}
	
	public final void shiftExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode shiftExpression_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop161:
			do {
				if (((LA(1) >= SL && LA(1) <= BSR))) {
					{
					switch ( LA(1)) {
					case SL:
					{
						JavaNode tmp135_AST = null;
						tmp135_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp135_AST);
						match(SL);
						break;
					}
					case SR:
					{
						JavaNode tmp136_AST = null;
						tmp136_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp136_AST);
						match(SR);
						break;
					}
					case BSR:
					{
						JavaNode tmp137_AST = null;
						tmp137_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp137_AST);
						match(BSR);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					additiveExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop161;
				}
				
			} while (true);
			}
			shiftExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_59);
			} else {
			  throw ex;
			}
		}
		returnAST = shiftExpression_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode additiveExpression_AST = null;
		
		try {      // for error handling
			multiplicativeExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop165:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						JavaNode tmp138_AST = null;
						tmp138_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp138_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						JavaNode tmp139_AST = null;
						tmp139_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp139_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplicativeExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop165;
				}
				
			} while (true);
			}
			additiveExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_60);
			} else {
			  throw ex;
			}
		}
		returnAST = additiveExpression_AST;
	}
	
	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode multiplicativeExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop169:
			do {
				if ((_tokenSet_61.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						JavaNode tmp140_AST = null;
						tmp140_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp140_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						JavaNode tmp141_AST = null;
						tmp141_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp141_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						JavaNode tmp142_AST = null;
						tmp142_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp142_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop169;
				}
				
			} while (true);
			}
			multiplicativeExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_62);
			} else {
			  throw ex;
			}
		}
		returnAST = multiplicativeExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode unaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INC:
			{
				JavaNode tmp143_AST = null;
				tmp143_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp143_AST);
				match(INC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case DEC:
			{
				JavaNode tmp144_AST = null;
				tmp144_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp144_AST);
				match(DEC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case MINUS:
			{
				JavaNode tmp145_AST = null;
				tmp145_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp145_AST);
				match(MINUS);
				if ( inputState.guessing==0 ) {
					tmp145_AST.setType(JavaTokenTypes.UNARY_MINUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case PLUS:
			{
				JavaNode tmp146_AST = null;
				tmp146_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp146_AST);
				match(PLUS);
				if ( inputState.guessing==0 ) {
					tmp146_AST.setType(JavaTokenTypes.UNARY_PLUS);
				}
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case BNOT:
			case LNOT:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				unaryExpressionNotPlusMinus();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode unaryExpressionNotPlusMinus_AST = null;
		Token  lpb = null;
		JavaNode lpb_AST = null;
		Token  lp = null;
		JavaNode lp_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BNOT:
			{
				JavaNode tmp147_AST = null;
				tmp147_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp147_AST);
				match(BNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (JavaNode)currentAST.root;
				break;
			}
			case LNOT:
			{
				JavaNode tmp148_AST = null;
				tmp148_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp148_AST);
				match(LNOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (JavaNode)currentAST.root;
				break;
			}
			case LITERAL_void:
			case LITERAL_boolean:
			case LITERAL_byte:
			case LITERAL_char:
			case LITERAL_short:
			case LITERAL_int:
			case LITERAL_float:
			case LITERAL_long:
			case LITERAL_double:
			case IDENT:
			case LPAREN:
			case LITERAL_this:
			case LITERAL_super:
			case LITERAL_true:
			case LITERAL_false:
			case LITERAL_null:
			case LITERAL_new:
			case NUM_INT:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NUM_FLOAT:
			case NUM_LONG:
			case NUM_DOUBLE:
			{
				{
				boolean synPredMatched174 = false;
				if (((LA(1)==LPAREN) && ((LA(2) >= LITERAL_void && LA(2) <= LITERAL_double)))) {
					int _m174 = mark();
					synPredMatched174 = true;
					inputState.guessing++;
					try {
						{
						match(LPAREN);
						builtInTypeSpec(true);
						match(RPAREN);
						unaryExpression();
						}
					}
					catch (RecognitionException pe) {
						synPredMatched174 = false;
					}
					rewind(_m174);
					inputState.guessing--;
				}
				if ( synPredMatched174 ) {
					lpb = LT(1);
					lpb_AST = (JavaNode)astFactory.create(lpb);
					astFactory.makeASTRoot(currentAST, lpb_AST);
					match(LPAREN);
					if ( inputState.guessing==0 ) {
						lpb_AST.setType(TYPECAST);
					}
					builtInTypeSpec(true);
					astFactory.addASTChild(currentAST, returnAST);
					match(RPAREN);
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					boolean synPredMatched176 = false;
					if (((LA(1)==LPAREN) && (LA(2)==IDENT))) {
						int _m176 = mark();
						synPredMatched176 = true;
						inputState.guessing++;
						try {
							{
							match(LPAREN);
							classTypeSpec(true);
							match(RPAREN);
							unaryExpressionNotPlusMinus();
							}
						}
						catch (RecognitionException pe) {
							synPredMatched176 = false;
						}
						rewind(_m176);
						inputState.guessing--;
					}
					if ( synPredMatched176 ) {
						lp = LT(1);
						lp_AST = (JavaNode)astFactory.create(lp);
						astFactory.makeASTRoot(currentAST, lp_AST);
						match(LPAREN);
						if ( inputState.guessing==0 ) {
							lp_AST.setType(TYPECAST);
						}
						classTypeSpec(true);
						astFactory.addASTChild(currentAST, returnAST);
						match(RPAREN);
						unaryExpressionNotPlusMinus();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else if ((_tokenSet_21.member(LA(1))) && (_tokenSet_64.member(LA(2)))) {
						postfixExpression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpressionNotPlusMinus_AST = (JavaNode)currentAST.root;
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
					consumeUntil(_tokenSet_63);
				} else {
				  throw ex;
				}
			}
			returnAST = unaryExpressionNotPlusMinus_AST;
		}
		
	public final void postfixExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode postfixExpression_AST = null;
		Token  id = null;
		JavaNode id_AST = null;
		Token  tt = null;
		JavaNode tt_AST = null;
		Token  lbc = null;
		JavaNode lbc_AST = null;
		Token  lb = null;
		JavaNode lb_AST = null;
		Token  lp = null;
		JavaNode lp_AST = null;
		Token  in = null;
		JavaNode in_AST = null;
		Token  de = null;
		JavaNode de_AST = null;
		Token t; _buildList.clear();
		StringBuffer buf = new StringBuffer(50);
		
		
		try {      // for error handling
			t=primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			if ( inputState.guessing==0 ) {
				
				if (t != null)
				{
				buf.append(t.getText().intern());
				}
				
			}
			{
			_loop182:
			do {
				switch ( LA(1)) {
				case DOT:
				{
					JavaNode tmp151_AST = null;
					tmp151_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp151_AST);
					match(DOT);
					if ( inputState.guessing==0 ) {
						
						if (t != null && _buildList.isEmpty())
						{
						String text = t.getText().intern();
						addIdentifier(text);
						_buildList.add(text);
						
						}
						
					}
					{
					switch ( LA(1)) {
					case IDENT:
					{
						id = LT(1);
						id_AST = (JavaNode)astFactory.create(id);
						astFactory.addASTChild(currentAST, id_AST);
						match(IDENT);
						if ( inputState.guessing==0 ) {
							_buildList.add(id.getText().intern());
							
							if (t != null)
							{
							buf.append(".".intern());
							buf.append(id.getText().intern());
							}
							
						}
						break;
					}
					case LITERAL_this:
					{
						tt = LT(1);
						tt_AST = (JavaNode)astFactory.create(tt);
						astFactory.addASTChild(currentAST, tt_AST);
						match(LITERAL_this);
						if ( inputState.guessing==0 ) {
							if (t != null) {_buf.append(".this"); buf.append(".this");}
						}
						break;
					}
					case LITERAL_class:
					{
						JavaNode tmp152_AST = null;
						tmp152_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp152_AST);
						match(LITERAL_class);
						if ( inputState.guessing==0 ) {
							if (t != null) {_buf.append(".class");buf.append(".class");}
						}
						break;
					}
					case LITERAL_new:
					{
						newExpression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case LITERAL_super:
					{
						JavaNode tmp153_AST = null;
						tmp153_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp153_AST);
						match(LITERAL_super);
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
				case LPAREN:
				{
					lp = LT(1);
					lp_AST = (JavaNode)astFactory.create(lp);
					astFactory.makeASTRoot(currentAST, lp_AST);
					match(LPAREN);
					if ( inputState.guessing==0 ) {
						lp_AST.setType(JavaTokenTypes.METHOD_CALL);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);
					}
					argList();
					astFactory.addASTChild(currentAST, returnAST);
					JavaNode tmp154_AST = null;
					tmp154_AST = (JavaNode)astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp154_AST);
					match(RPAREN);
					break;
				}
				default:
					if ((LA(1)==LBRACK) && (LA(2)==RBRACK)) {
						{
						int _cnt181=0;
						_loop181:
						do {
							if ((LA(1)==LBRACK)) {
								lbc = LT(1);
								lbc_AST = (JavaNode)astFactory.create(lbc);
								astFactory.makeASTRoot(currentAST, lbc_AST);
								match(LBRACK);
								if ( inputState.guessing==0 ) {
									lbc_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
								}
								match(RBRACK);
							}
							else {
								if ( _cnt181>=1 ) { break _loop181; } else {throw new NoViableAltException(LT(1), getFilename());}
							}
							
							_cnt181++;
						} while (true);
						}
						JavaNode tmp156_AST = null;
						tmp156_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp156_AST);
						match(DOT);
						JavaNode tmp157_AST = null;
						tmp157_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp157_AST);
						match(LITERAL_class);
					}
					else if ((LA(1)==LBRACK) && (_tokenSet_34.member(LA(2)))) {
						lb = LT(1);
						lb_AST = (JavaNode)astFactory.create(lb);
						astFactory.makeASTRoot(currentAST, lb_AST);
						match(LBRACK);
						if ( inputState.guessing==0 ) {
							lb_AST.setType(JavaTokenTypes.INDEX_OP);
						}
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						JavaNode tmp158_AST = null;
						tmp158_AST = (JavaNode)astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp158_AST);
						match(RBRACK);
					}
				else {
					break _loop182;
				}
				}
			} while (true);
			}
			{
			switch ( LA(1)) {
			case INC:
			{
				in = LT(1);
				in_AST = (JavaNode)astFactory.create(in);
				astFactory.makeASTRoot(currentAST, in_AST);
				match(INC);
				if ( inputState.guessing==0 ) {
					in_AST.setType(JavaTokenTypes.POST_INC);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);
				}
				break;
			}
			case DEC:
			{
				de = LT(1);
				de_AST = (JavaNode)astFactory.create(de);
				astFactory.makeASTRoot(currentAST, de_AST);
				match(DEC);
				if ( inputState.guessing==0 ) {
					de_AST.setType(JavaTokenTypes.POST_DEC);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);
				}
				break;
			}
			case RCURLY:
			case SEMI:
			case RBRACK:
			case STAR:
			case COMMA:
			case RPAREN:
			case ASSIGN:
			case COLON:
			case PLUS_ASSIGN:
			case MINUS_ASSIGN:
			case STAR_ASSIGN:
			case DIV_ASSIGN:
			case MOD_ASSIGN:
			case SR_ASSIGN:
			case BSR_ASSIGN:
			case SL_ASSIGN:
			case BAND_ASSIGN:
			case BXOR_ASSIGN:
			case BOR_ASSIGN:
			case QUESTION:
			case LOR:
			case LAND:
			case BOR:
			case BXOR:
			case BAND:
			case NOT_EQUAL:
			case EQUAL:
			case LT:
			case GT:
			case LE:
			case GE:
			case LITERAL_instanceof:
			case SL:
			case SR:
			case BSR:
			case PLUS:
			case MINUS:
			case DIV:
			case MOD:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			if ( inputState.guessing==0 ) {
				
				if (t != null)
				{
				_references.addReference(buf.toString().intern(), (JavaNode)currentAST.root);
				}
				
				if (!_buildList.isEmpty())
				{
				_buf.setLength(0);
				
				for (int i = 0, size = _buildList.size(); i < size; i++)
				{
				_buf.append((String)_buildList.get(i));
				_buf.append('.');
				}
				_buf.setLength(_buf.length() - 1);
				addIdentifier(_buf.toString());
				}
				
			}
			postfixExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_63);
			} else {
			  throw ex;
			}
		}
		returnAST = postfixExpression_AST;
	}
	
/** object instantiation.
 *  Trees are built as illustrated by the following input/tree pairs:
 *  <pre>
 *  new T()
 *
 *  new
 *   |
 *   T --  ELIST
 *           |
 *          arg1 -- arg2 -- .. -- argn
 *
 *  new int[]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *
 *  new int[] {1,2}
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR -- ARRAY_INIT
 *                                  |
 *                                EXPR -- EXPR
 *                                  |      |
 *                                  1      2
 *
 *  new int[3]
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *                |
 *              EXPR
 *                |
 *                3
 *
 *  new int[1][2]
 *
 *  new
 *   |
 *  int -- ARRAY_DECLARATOR
 *               |
 *         ARRAY_DECLARATOR -- EXPR
 *               |              |
 *             EXPR             1
 *               |
 *               2
 *  </pre>
 */
	public final void newExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode newExpression_AST = null;
		
		try {      // for error handling
			JavaNode tmp159_AST = null;
			tmp159_AST = (JavaNode)astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp159_AST);
			match(LITERAL_new);
			type();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LPAREN:
			{
				JavaNode tmp160_AST = null;
				tmp160_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp160_AST);
				match(LPAREN);
				argList();
				astFactory.addASTChild(currentAST, returnAST);
				JavaNode tmp161_AST = null;
				tmp161_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp161_AST);
				match(RPAREN);
				{
				switch ( LA(1)) {
				case LCURLY:
				{
					classBlock();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RCURLY:
				case SEMI:
				case LBRACK:
				case RBRACK:
				case DOT:
				case STAR:
				case COMMA:
				case LPAREN:
				case RPAREN:
				case ASSIGN:
				case COLON:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case NOT_EQUAL:
				case EQUAL:
				case LT:
				case GT:
				case LE:
				case GE:
				case LITERAL_instanceof:
				case SL:
				case SR:
				case BSR:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case INC:
				case DEC:
				{
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
			case LBRACK:
			{
				newArrayDeclarator();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case LCURLY:
				{
					arrayInitializer();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case RCURLY:
				case SEMI:
				case LBRACK:
				case RBRACK:
				case DOT:
				case STAR:
				case COMMA:
				case LPAREN:
				case RPAREN:
				case ASSIGN:
				case COLON:
				case PLUS_ASSIGN:
				case MINUS_ASSIGN:
				case STAR_ASSIGN:
				case DIV_ASSIGN:
				case MOD_ASSIGN:
				case SR_ASSIGN:
				case BSR_ASSIGN:
				case SL_ASSIGN:
				case BAND_ASSIGN:
				case BXOR_ASSIGN:
				case BOR_ASSIGN:
				case QUESTION:
				case LOR:
				case LAND:
				case BOR:
				case BXOR:
				case BAND:
				case NOT_EQUAL:
				case EQUAL:
				case LT:
				case GT:
				case LE:
				case GE:
				case LITERAL_instanceof:
				case SL:
				case SR:
				case BSR:
				case PLUS:
				case MINUS:
				case DIV:
				case MOD:
				case INC:
				case DEC:
				{
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
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			newExpression_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_38);
			} else {
			  throw ex;
			}
		}
		returnAST = newExpression_AST;
	}
	
	public final void constant() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode constant_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUM_INT:
			{
				JavaNode tmp162_AST = null;
				tmp162_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp162_AST);
				match(NUM_INT);
				constant_AST = (JavaNode)currentAST.root;
				break;
			}
			case CHAR_LITERAL:
			{
				JavaNode tmp163_AST = null;
				tmp163_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp163_AST);
				match(CHAR_LITERAL);
				constant_AST = (JavaNode)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			{
				JavaNode tmp164_AST = null;
				tmp164_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp164_AST);
				match(STRING_LITERAL);
				constant_AST = (JavaNode)currentAST.root;
				break;
			}
			case NUM_FLOAT:
			{
				JavaNode tmp165_AST = null;
				tmp165_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp165_AST);
				match(NUM_FLOAT);
				constant_AST = (JavaNode)currentAST.root;
				break;
			}
			case NUM_LONG:
			{
				JavaNode tmp166_AST = null;
				tmp166_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp166_AST);
				match(NUM_LONG);
				constant_AST = (JavaNode)currentAST.root;
				break;
			}
			case NUM_DOUBLE:
			{
				JavaNode tmp167_AST = null;
				tmp167_AST = (JavaNode)astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp167_AST);
				match(NUM_DOUBLE);
				constant_AST = (JavaNode)currentAST.root;
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
				consumeUntil(_tokenSet_38);
			} else {
			  throw ex;
			}
		}
		returnAST = constant_AST;
	}
	
	public final void newArrayDeclarator() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		JavaNode newArrayDeclarator_AST = null;
		Token  lb = null;
		JavaNode lb_AST = null;
		
		try {      // for error handling
			{
			int _cnt196=0;
			_loop196:
			do {
				if ((LA(1)==LBRACK) && (_tokenSet_65.member(LA(2)))) {
					lb = LT(1);
					lb_AST = (JavaNode)astFactory.create(lb);
					astFactory.makeASTRoot(currentAST, lb_AST);
					match(LBRACK);
					if ( inputState.guessing==0 ) {
						lb_AST.setType(JavaTokenTypes.ARRAY_DECLARATOR);
					}
					{
					switch ( LA(1)) {
					case LITERAL_void:
					case LITERAL_boolean:
					case LITERAL_byte:
					case LITERAL_char:
					case LITERAL_short:
					case LITERAL_int:
					case LITERAL_float:
					case LITERAL_long:
					case LITERAL_double:
					case IDENT:
					case LPAREN:
					case LITERAL_this:
					case LITERAL_super:
					case PLUS:
					case MINUS:
					case INC:
					case DEC:
					case BNOT:
					case LNOT:
					case LITERAL_true:
					case LITERAL_false:
					case LITERAL_null:
					case LITERAL_new:
					case NUM_INT:
					case CHAR_LITERAL:
					case STRING_LITERAL:
					case NUM_FLOAT:
					case NUM_LONG:
					case NUM_DOUBLE:
					{
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RBRACK:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					match(RBRACK);
				}
				else {
					if ( _cnt196>=1 ) { break _loop196; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt196++;
			} while (true);
			}
			newArrayDeclarator_AST = (JavaNode)currentAST.root;
		}
		catch (RecognitionException ex) {
			if (inputState.guessing==0) {
				reportError(ex);
				consume();
				consumeUntil(_tokenSet_66);
			} else {
			  throw ex;
			}
		}
		returnAST = newArrayDeclarator_AST;
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
		"MODIFIERS",
		"OBJBLOCK",
		"SLIST",
		"CTOR_DEF",
		"METHOD_DEF",
		"VARIABLE_DEF",
		"INSTANCE_INIT",
		"STATIC_INIT",
		"TYPE",
		"CLASS_DEF",
		"INTERFACE_DEF",
		"PACKAGE_DEF",
		"ARRAY_DECLARATOR",
		"EXTENDS_CLAUSE",
		"IMPLEMENTS_CLAUSE",
		"PARAMETERS",
		"PARAMETER_DEF",
		"LABELED_STAT",
		"TYPECAST",
		"INDEX_OP",
		"POST_INC",
		"POST_DEC",
		"METHOD_CALL",
		"EXPR",
		"ARRAY_INIT",
		"IMPORT",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"CASE_GROUP",
		"ELIST",
		"FOR_INIT",
		"FOR_CONDITION",
		"FOR_ITERATOR",
		"EMPTY_STAT",
		"\"final\"",
		"\"abstract\"",
		"\"strictfp\"",
		"SUPER_CTOR_CALL",
		"CTOR_CALL",
		"BOF",
		"ROOT",
		"CASESLIST",
		"BLOCK_STATEMENT",
		"SEPARATOR_COMMENT",
		"SYNBLOCK",
		"\"package\"",
		"SEMI",
		"\"import\"",
		"LBRACK",
		"RBRACK",
		"\"void\"",
		"\"boolean\"",
		"\"byte\"",
		"\"char\"",
		"\"short\"",
		"\"int\"",
		"\"float\"",
		"\"long\"",
		"\"double\"",
		"IDENT",
		"DOT",
		"STAR",
		"\"private\"",
		"\"public\"",
		"\"protected\"",
		"\"static\"",
		"\"transient\"",
		"\"native\"",
		"\"synchronized\"",
		"\"volatile\"",
		"\"class\"",
		"\"extends\"",
		"\"interface\"",
		"COMMA",
		"\"implements\"",
		"LPAREN",
		"RPAREN",
		"\"this\"",
		"\"super\"",
		"STR_supper",
		"ASSIGN",
		"\"throws\"",
		"COLON",
		"\"if\"",
		"\"else\"",
		"\"for\"",
		"\"while\"",
		"\"do\"",
		"\"break\"",
		"\"assert\"",
		"\"continue\"",
		"\"return\"",
		"\"switch\"",
		"\"throw\"",
		"\"case\"",
		"\"default\"",
		"\"try\"",
		"\"catch\"",
		"\"finally\"",
		"PLUS_ASSIGN",
		"MINUS_ASSIGN",
		"STAR_ASSIGN",
		"DIV_ASSIGN",
		"MOD_ASSIGN",
		"SR_ASSIGN",
		"BSR_ASSIGN",
		"SL_ASSIGN",
		"BAND_ASSIGN",
		"BXOR_ASSIGN",
		"BOR_ASSIGN",
		"QUESTION",
		"LOR",
		"LAND",
		"BOR",
		"BXOR",
		"BAND",
		"NOT_EQUAL",
		"EQUAL",
		"LT",
		"GT",
		"LE",
		"GE",
		"\"instanceof\"",
		"SL",
		"SR",
		"BSR",
		"PLUS",
		"MINUS",
		"DIV",
		"MOD",
		"INC",
		"DEC",
		"BNOT",
		"LNOT",
		"\"true\"",
		"\"false\"",
		"\"null\"",
		"\"new\"",
		"NUM_INT",
		"CHAR_LITERAL",
		"STRING_LITERAL",
		"NUM_FLOAT",
		"NUM_LONG",
		"NUM_DOUBLE",
		"WS",
		"SPECIAL_COMMENT",
		"SL_COMMENT",
		"COMMENT",
		"ML_COMMENT",
		"ESC",
		"HEX_DIGIT",
		"VOCAB",
		"EXPONENT",
		"FLOAT_SUFFIX"
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 36090369670119424L, 196480L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 108147963708047362L, 196480L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 36090369670119426L, 196480L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 36028797018963968L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 61572651155456L, 32640L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { -576460752303423488L, 163871L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { -540370382633303678L, 4397791903647L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { -540370382633303678L, 196511L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 324259173170675968L, 9223354444838862864L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 468374361246531968L, 9223354444840435728L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 468374361246531840L, 9223354444839911472L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 144115188075855872L, 1048576L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { -576399179652268032L, 196511L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 128L, 524288L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 128L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { -108024818405736062L, -13194224009217L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { -432283991576412160L, 1245119L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 144115188075855872L, 48L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 180143985094819840L, 33816576L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { -540370382633303680L, 196511L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { -576460752303423488L, 13631519L, 33521664L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { -432345564227567616L, 13631551L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { -540370382633303680L, 2747987459999L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { -396255194557447808L, -14844030681089L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { -540370382633303936L, 2747987459999L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 0L, 2097152L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 36028797018964096L, 103022592L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 36028797018964096L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { -540370382633303680L, 17591931436959L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { -540370382633303680L, 4397791772575L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { -396255194557447808L, -86835201L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { -576399179652268032L, 32671L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { -432283991576412160L, 32703L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { -576460752303423488L, 13631519L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { -396316767208603648L, -17592138858369L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 61572651155456L, 65408L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 61572651155456L, 65424L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 468374361246531840L, -17592014864288L, 8191L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 36028797018963968L, 262144L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 36028797018964224L, 262144L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { -576460752303423360L, 13631519L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 324259173170675968L, 136577024L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 0L, 2359296L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { -576460752303423488L, 31L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { -396316767208603648L, -17592138596225L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = { -576460752303423488L, 147849247L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 256L, 1649267441664L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { -540370382633303680L, 4397254901663L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = { 36028797018963968L, 2097152L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	private static final long[] mk_tokenSet_50() {
		long[] data = { -540370382633303680L, 17591931305887L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
	private static final long[] mk_tokenSet_51() {
		long[] data = { 324259173170675968L, 36011205003051008L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
	private static final long[] mk_tokenSet_52() {
		long[] data = { 324259173170675968L, 72040002022014976L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
	private static final long[] mk_tokenSet_53() {
		long[] data = { 324259173170675968L, 144097596059942912L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
	private static final long[] mk_tokenSet_54() {
		long[] data = { 324259173170675968L, 288212784135798784L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
	private static final long[] mk_tokenSet_55() {
		long[] data = { 324259173170675968L, 576443160287510528L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
	private static final long[] mk_tokenSet_56() {
		long[] data = { 324259173170675968L, 1152903912590934016L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
	private static final long[] mk_tokenSet_57() {
		long[] data = { 324259173170675968L, 2305825417197780992L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
	private static final long[] mk_tokenSet_58() {
		long[] data = { 324259173170675968L, 9223354444838862848L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
	private static final long[] mk_tokenSet_59() {
		long[] data = { 324259173170675968L, -17592015912960L, 15L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
	private static final long[] mk_tokenSet_60() {
		long[] data = { 324259173170675968L, -17592015912960L, 127L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
	private static final long[] mk_tokenSet_61() {
		long[] data = { 0L, 64L, 1536L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
	private static final long[] mk_tokenSet_62() {
		long[] data = { 324259173170675968L, -17592015912960L, 511L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
	private static final long[] mk_tokenSet_63() {
		long[] data = { 324259173170675968L, -17592015912896L, 2047L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
	private static final long[] mk_tokenSet_64() {
		long[] data = { -108086391056891648L, -17592002281345L, 33554431L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());
	private static final long[] mk_tokenSet_65() {
		long[] data = { -288230376151711744L, 13631519L, 33552768L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_65 = new BitSet(mk_tokenSet_65());
	private static final long[] mk_tokenSet_66() {
		long[] data = { 468374361246531968L, -17592014864288L, 8191L, 0L, 0L, 0L};
		return data;
	}
	private static final BitSet _tokenSet_66 = new BitSet(mk_tokenSet_66());
	
	}
