header
{
package de.hunsicker.jalopy.language;
}
{
import de.hunsicker.antlr.ANTLRStringBuffer;
import de.hunsicker.antlr.CommonHiddenStreamToken;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
}

/**
 * Parser for the Sun Java language. Heavily based on the public domain grammar written by
 * <a href="mailto:parrt@jguru.com">Terence Parr
 * </a> et al. See <a href="http://www.antlr.org/resources.html">
 * http://www.antlr.org/resources.html</a> for more info.
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
class JavaParser extends Parser;
options {
        k = 2;                           // two token lookahead
        exportVocab=Java;                // Call its vocabulary "Java"
        codeGenMakeSwitchThreshold = 2;  // Some optimizations
        codeGenBitsetTestThreshold = 3;
        defaultErrorHandler = true;      // Generate parser error handlers
        buildAST = true;
        classHeaderSuffix = "Parser";
        importVocab = Common;
        ASTLabelType = JavaNode;
        useTokenPrefix = true;
}

tokens {
        MODIFIERS; OBJBLOCK; SLIST; CTOR_DEF; METHOD_DEF; VARIABLE_DEF;
        INSTANCE_INIT; STATIC_INIT; TYPE; CLASS_DEF; INTERFACE_DEF;
        PACKAGE_DEF; ARRAY_DECLARATOR; EXTENDS_CLAUSE; IMPLEMENTS_CLAUSE;
        PARAMETERS; PARAMETER_DEF; LABELED_STAT; TYPECAST; INDEX_OP;
        POST_INC; POST_DEC; METHOD_CALL; EXPR; ARRAY_INIT;
        IMPORT; UNARY_MINUS; UNARY_PLUS; CASE_GROUP; ELIST; FOR_INIT; FOR_CONDITION;
        FOR_ITERATOR; EMPTY_STAT; FINAL="final"; ABSTRACT="abstract";
        STRICTFP="strictfp"; SUPER_CTOR_CALL; CTOR_CALL;
        BOF; ROOT; CASESLIST; BLOCK_STATEMENT; SEPARATOR_COMMENT; SYNBLOCK;
}
{

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

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
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
    private final static class NoList
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

}

// Compilation Unit: In Java, this is a single file.  This is the start
//   rule for this parser
parse
        {
            JavaNode root = new JavaNode();
            root.setType(JavaTokenTypes.ROOT);
            root.setText(getFilename());
            currentAST.root = root;
        }

        :       // A compilation unit starts with an optional package definition
                (       packageDefinition
                |       /* nothing */
                )

                // Next we have a series of zero or more import statements
                ( importDefinition )*

                // Wrapping things up with any number of class or interface
                //    definitions
                ( typeDefinition )*

                EOF!
        ;


// Package statement: "package" followed by an identifier.
packageDefinition
        options {defaultErrorHandler = true;} // let ANTLR handle errors
        :       p:"package"^ identifierPackage semi:SEMI
                { #p.setType(JavaTokenTypes.PACKAGE_DEF); }
        ;


// Import statement: "import" followed by a package or class name
importDefinition
        options {defaultErrorHandler = true;}
        :       i:"import"^ identifierStar semi:SEMI
                { #i.setType(JavaTokenTypes.IMPORT); }
        ;

// A type definition in a file is either a class or interface definition.
typeDefinition
        options {defaultErrorHandler = true;}
        :       m:modifiers!
                ( classDefinition[#m]
                | interfaceDefinition[#m]
                )
        |       SEMI
        ;

/** A declaration is the creation of a reference or primitive-type variable
 *  Create a separate Type/Var tree for each var in the var list.
 */
declaration!
        :       m:modifiers t:typeSpec[false] v:variableDefinitions[#m,#t]
                {#declaration = #v;}
        ;

// A type specification is a type name with possible brackets afterwards
//   (which would make it an array type).
typeSpec[boolean addImagNode]
        : classTypeSpec[addImagNode]
        | builtInTypeSpec[addImagNode]

        ;

// A class type specification is a class type with possible brackets afterwards
//   (which would make it an array type).
classTypeSpec[boolean addImagNode]
        :       i:identifier (lb:LBRACK^ {#lb.setType(JavaTokenTypes.ARRAY_DECLARATOR);} RBRACK!)*
                {
                        if ( addImagNode ) {
                                #classTypeSpec = #(#[JavaTokenTypes.TYPE,STR_TYPE], #classTypeSpec);
                        }
                   _references.defineType(#i.getText().intern(), #classTypeSpec);
                }
        ;

// A builtin type specification is a builtin type with possible brackets
// afterwards (which would make it an array type).
builtInTypeSpec[boolean addImagNode]
        :       builtInType (lb:LBRACK^ {#lb.setType(JavaTokenTypes.ARRAY_DECLARATOR);} RBRACK!)*
                {
                        if ( addImagNode ) {
                                #builtInTypeSpec = #(#[JavaTokenTypes.TYPE,STR_TYPE], #builtInTypeSpec);
                        }
                }
        ;

// A type name. which is either a (possibly qualified) class name or
//   a primitive (builtin) type
type
        :       identifier
        |       builtInType
        ;

// The primitive types.
builtInType
        :   "void"
        |   "boolean"
        |   "byte"
        |   "char"
        |   "short"
        |   "int"
        |   "float"
        |   "long"
        |   "double"
        ;

// A (possibly-qualified) java identifier.  We start with the first IDENT
//   and expand its name by adding dots and following IDENTS
identifier!
        {
            _buf.setLength(0);
        }

        :       id1:IDENT  { _buf.append(id1.getText()); }
                ( DOT^ id2:IDENT
                  {
                      _buf.append('.');
                      _buf.append(id2.getText());

                  }
                )*

        {
            #identifier = #([JavaTokenTypes.IDENT, _buf.toString()]);
            String text = #identifier.getText().intern();
            addIdentifier(text);

            #identifier.setHiddenBefore(#id1.getHiddenBefore());
            #identifier.setHiddenAfter(#id1.getHiddenAfter());
            #identifier.startLine = #id1.startLine;
            #identifier.startColumn = #id1.startColumn;
            #identifier.endLine = #id1.startLine;
            #identifier.endColumn = #id1.startColumn + text.length();
        }
        ;

/**
 * Our qualified package identifier. We need this rule because all other
 * identifiers will be made unqualified (depending on the stripQualification switch).
 */
identifierPackage
        {
            _buf.setLength(0);
        }
        : id1:IDENT { _buf.append(#id1.getText()); }
          ( DOT^
            id2:IDENT {
                        _buf.append('.');
                        _buf.append(#id2.getText());
                     }
          )*

        { _packageName = _buf.toString(); }
    ;

identifierStar
        {
            _buf.setLength(0);
            boolean star = false;
        }
        :       id1:IDENT { _buf.append(id1.getText());}
                ( DOT^ id2:IDENT {
                                    _buf.append('.');
                                    _buf.append(id2.getText());
                                  }
                )*
                ( DOT^ STAR  { star = true;} )?

        {
            if (star)
                _unqualImports.add(_buf.toString());
            else
                _qualImports.add(_buf.toString());
        }
        ;

// A list of zero or more modifiers.  We could have used (modifier)* in
//   place of a call to modifiers, but I thought it was a good idea to keep
//   this rule separate so they can easily be collected in a Vector if
//   someone so desires
modifiers
        :       ( modifier )*
                {#modifiers = #([JavaTokenTypes.MODIFIERS, STR_MODIFIERS], #modifiers);}
        ;

// modifiers for Java classes, interfaces, class/instance vars and methods
modifier
        :       "private"
        |       "public"
        |       "protected"
        |       "static"
        |       "transient"
        |       "final"
        |       "abstract"
        |       "native"
//      |       "threadsafe"            // is this a keyword???
        |       "synchronized"
//      |       "const"                 // reserved word, but not valid
        |       "volatile"
        |       "strictfp"
        ;

// Definition of a Java class
classDefinition![JavaNode modifiers]
        :       c:"class"! id:IDENT!
                // it _might_ have a superclass...
                sc:superClassClause
                // it might implement some interfaces...
                ic:implementsClause
                // now parse the body of the class
                cb:classBlock
                { #classDefinition = #(#[JavaTokenTypes.CLASS_DEF,"CLASS_DEF"],
                                                           modifiers,c,id,sc,ic,cb);
                  attachStuffBefore(#classDefinition, modifiers, #c);
                }

        ;

superClassClause!
        :       ( e:"extends"! id:identifier )?
                { #superClassClause = #(#[JavaTokenTypes.EXTENDS_CLAUSE,STR_EXTENDS_CLAUSE],id);
                  if (#e != null)
                  #superClassClause.setHiddenBefore(#e.getHiddenBefore());
                }
        ;

// Definition of a Java Interface
interfaceDefinition![JavaNode modifiers]
        :       i:"interface"! id:IDENT!
                // it might extend some other interfaces
                ie:interfaceExtends
                // now parse the body of the interface (looks like a class...)
                cb:classBlock
                { #interfaceDefinition = #(#[JavaTokenTypes.INTERFACE_DEF,"INTERFACE_DEF"],
                                                                        modifiers,i,id,ie,cb);
                  attachStuffBefore(#interfaceDefinition, modifiers, #i);
                }
        ;


// This is the body of a class.  You can have fields and extra semicolons,
// That's about it (until you see what a field is...)
classBlock
        :
                lc:LCURLY^
                {_references.enterScope(References.SCOPE_CLASS);}
                        ( field | SEMI )*

                rc:RCURLY{_references.leaveScope();}
                { #lc.setType(JavaTokenTypes.OBJBLOCK);}
                //{#classBlock = #([JavaTokenTypes.OBJBLOCK, STR_OBJBLOCK], #classBlock);}
        ;

// An interface can extend several other interfaces...
interfaceExtends
        :       (
                e:"extends"!
                identifier ( COMMA! identifier )*
                )?
                {#interfaceExtends = #(#[JavaTokenTypes.EXTENDS_CLAUSE,STR_EXTENDS_CLAUSE],
                                                        #interfaceExtends);}
        ;

// A class can implement several interfaces...
implementsClause
        :       (
                        i:"implements"! identifier ( COMMA! identifier )*
                )?
                { #implementsClause = #(#[JavaTokenTypes.IMPLEMENTS_CLAUSE,"IMPLEMENTS_CLAUSE"],
                                                                 #implementsClause);
                  if (#i != null)
                                   #implementsClause.setHiddenBefore(#i.getHiddenBefore());
                                                                 }
        ;

// Now the various things that can be defined inside a class or interface...
// Note that not all of these are really valid in an interface (constructors,
//   for example), and if this grammar were used for a compiler there would
//   need to be some semantic checks to make sure we're doing the right thing...
field!
        :       // method, constructor, or variable declaration
                mods:modifiers
                (       { _references.enterScope(); }h:ctorHead s:constructorBody // constructor
                        { _references.leaveScope(); }
                        {#field = #(#[JavaTokenTypes.CTOR_DEF,"CTOR_DEF"], mods, h, s);
                        attachStuffBeforeCtor(#field, #mods, #h);
                        }


                |       cd:classDefinition[#mods]       // inner class
                        {#field = #cd;}

                |       id:interfaceDefinition[#mods]   // inner interface
                        {#field = #id;}

                |       t:typeSpec[false]  // method or variable declaration(s)
                        (       idd:IDENT  // the name of the method

                                { _references.enterScope(); }

                                // parse the formal parameter declarations.
                                lp:LPAREN param:parameterDeclarationList rp:RPAREN

                                rt:declaratorBrackets[#t]

                                // get the list of exceptions that this method is
                                // declared to throw
                                (tc:throwsClause)?

                                ( s2:compoundStatement | semim:SEMI { _references.leaveScope(); } )
                                { #field = #(#[JavaTokenTypes.METHOD_DEF,"METHOD_DEF"],
                                                     mods,
                                                         #(#[JavaTokenTypes.TYPE,STR_TYPE],rt),
                                                         idd,
                                                         lp,
                                                         param,
                                                         rp,
                                                         tc,
                                                         s2,
                                                         semim);
                                    attachStuffBefore(#field, #mods, #t);
                                }
                        |       v:variableDefinitions[#mods,#t] semi:SEMI!
                                {
                                    #field = #v;
                                    #field.addChild(#semi);

                                    AST next = #field.getNextSibling();
                                    // HACK for multiple variable declaration in one statement
                                    //      e.g float  x, y, z;
                                    // the semicolon will only be added to the first statement so
                                    // we have to add it manually to all others
                                    if (next != null)
                                    {
                                        AST ssemi = JavaNodeHelper.getFirstChild(#field, JavaTokenTypes.SEMI);

                                        for (AST var = next; var != null; var = var.getNextSibling())
                                        {
                                            var.addChild(astFactory.create(ssemi));
                                        }
                                    }

                                }
                        )
                )

    // "static { ... }" class initializer
        |       stat:"static"^ { _references.enterScope(); } s3:compoundStatement
                {
                  #field = #(#stat, s3);
                  #field.setType(JavaTokenTypes.STATIC_INIT);
                }

    // "{ ... }" instance initializer
        |       { _references.enterScope(); } s4:compoundStatement
                { #field = #(#[JavaTokenTypes.INSTANCE_INIT,STR_INSTANCE_INIT], s4);
                    attachStuffBeforeCompoundStatement(#field, #s4);
                }
        ;

constructorBody
    :   lc:LCURLY^ {#lc.setType(JavaTokenTypes.SLIST);}
                // Predicate might be slow but only checked once per constructor def
                // not for general methods.
                (       (explicitConstructorInvocation) => explicitConstructorInvocation
                |
                )
        (statement)*
        RCURLY
    ;

explicitConstructorInvocation
{ Token t = null; }
    :   (       options {
                                // this/super can begin a primaryExpression too; with finite
                                // lookahead ANTLR will think the 3rd alternative conflicts
                                // with 1, 2.  I am shutting off warning since ANTLR resolves
                                // the nondeterminism by correctly matching alts 1 or 2 when
                                // it sees this( or super(
                                generateAmbigWarnings=false;
                        }
                :       "this" lp1:LPAREN^ argList RPAREN SEMI
                        {#lp1.setType(JavaTokenTypes.CTOR_CALL);}

            |   "super" lp2:LPAREN^ argList RPAREN SEMI
                        {#lp2.setType(JavaTokenTypes.SUPER_CTOR_CALL);}

                        // (new Outer()).super()  (create enclosing instance)
                |       t=primaryExpression { if (t != null)addIdentifier(t.getText()); } DOT! STR_supper! lp3:LPAREN^ argList RPAREN SEMI
                        {#lp3.setType(JavaTokenTypes.SUPER_CTOR_CALL);}
                )
    ;

variableDefinitions[JavaNode mods, JavaNode t]
        :       variableDeclarator[(JavaNode)getASTFactory().dupTree(mods),
                                                   (JavaNode)getASTFactory().dupTree(t)]
                (       COMMA!
                        variableDeclarator[(JavaNode)getASTFactory().dupTree(mods),
                                                           (JavaNode)getASTFactory().dupTree(t)]
                )*
        ;

/** Declaration of a variable.  This can be a class/instance variable,
 *   or a local variable in a method
 * It can also include possible initialization.
 */
variableDeclarator![JavaNode mods, JavaNode t]
        :       id:IDENT d:declaratorBrackets[t] v:varInitializer
                {
                  #variableDeclarator = #(#[JavaTokenTypes.VARIABLE_DEF,"VARIABLE_DEF"], mods, #(#[JavaTokenTypes.TYPE,STR_TYPE],d), id, v);
                  attachStuffBefore(#variableDeclarator, mods, t);
                  _references.defineVariable(id.getText().intern(), #variableDeclarator);
                }
        ;

declaratorBrackets[JavaNode typ]
        :       {#declaratorBrackets=typ;}
                (lb:LBRACK^ {#lb.setType(JavaTokenTypes.ARRAY_DECLARATOR);} RBRACK!)*
        ;

varInitializer
        :       ( ASSIGN^ initializer )?
        ;

// This is an initializer used to set up an array.
arrayInitializer
        :       lc:LCURLY^ {#lc.setType(JavaTokenTypes.ARRAY_INIT);}
                        (       initializer
                                (
                                        // CONFLICT: does a COMMA after an initializer start a new
                                        //           initializer or start the option ',' at end?
                                        //           ANTLR generates proper code by matching
                                        //                       the comma as soon as possible.
                                        options {
                                                warnWhenFollowAmbig = false;
                                        }
                                :
                                        COMMA initializer
                                )*
                                (COMMA)?
                        )?
                RCURLY
        ;


// The two "things" that can initialize an array element are an expression
//   and another (nested) array initializer.
initializer
        :       expression
        |       arrayInitializer
        ;

// This is the header of a method.  It includes the name and parameters
//   for the method.
//   This also watches for a list of exception classes in a "throws" clause.
ctorHead
        :       IDENT  // the name of the method

                // parse the formal parameter declarations.
                LPAREN parameterDeclarationList RPAREN

                // get the list of exceptions that this method is declared to throw
                (throwsClause)?
        ;

// This is a list of exception classes that the method is declared to throw
throwsClause
        :       "throws"^ identifier ( COMMA identifier )*
        ;


// A list of formal parameters
parameterDeclarationList
        :       ( parameterDeclaration ( COMMA parameterDeclaration )* )?
                {#parameterDeclarationList = #(#[JavaTokenTypes.PARAMETERS,STR_PARAMETERS],
                                                                        #parameterDeclarationList);}
        ;

// A formal parameter.
parameterDeclaration!
        :       pm:parameterModifier t:typeSpec[false] id:IDENT
                pd:declaratorBrackets[#t]
                {#parameterDeclaration = #(#[JavaTokenTypes.PARAMETER_DEF,"PARAMETER_DEF"],
                                         pm, #([JavaTokenTypes.TYPE,STR_TYPE],pd), id);

                    _references.defineVariable(id.getText().intern(), #parameterDeclaration);
                }
        ;

parameterModifier
        :       (f:"final")?
                {#parameterModifier = #(#[JavaTokenTypes.MODIFIERS,STR_MODIFIERS], f);}
        ;

// Compound statement.  This is used in many contexts:
//   Inside a class definition prefixed with "static":
//      it is a class initializer
//   Inside a class definition without "static":
//      it is an instance initializer
//   As the body of a method
//   As a completely indepdent braced block of code inside a method
//      it starts a new scope for variable definitions

compoundStatement
        :       lc:LCURLY^ {#lc.setType(JavaTokenTypes.SLIST); }
                        // include the (possibly-empty) list of statements
                        (statement)*
                RCURLY
                { _references.leaveScope(); }
        ;


statement
        // A list of statements in curly braces -- start a new scope!
        :       { _references.enterScope(); }compoundStatement

        // declarations are ambiguous with "ID DOT" relative to expression
        // statements.  Must backtrack to be sure.  Could use a semantic
        // predicate to test symbol table to see what the type was coming
        // up, but that's pretty hard without a symbol table ;)
        |       (declaration)=> decl:declaration semi1:SEMI!
                {
                    // add semicolon to the AST
                    #decl.addChild(#semi1);

                    AST next = currentAST.root.getNextSibling();

                    // HACK for multiple variable declaration in one statement
                    //      e.g float x, y, z;
                    // the semicolon will only be added to the first statement so
                    // we have to add it manually to all others
                    if (next != null)
                    {
                        AST semi = JavaNodeHelper.getFirstChild(currentAST.root, JavaTokenTypes.SEMI);

                        for (AST var = next; var != null; var = var.getNextSibling())
                        {
                            var.addChild(astFactory.create(semi));
                        }
                    }
                }

        // An expression statement.  This could be a method call,
        // assignment statement, or any other expression evaluated for
        // side-effects.
        |       e:expression semi2:SEMI!
                {#e.addChild(#semi2);}

        // class definition
        |       m:modifiers! classDefinition[#m]

        // Attach a label to the front of a statement
        |       IDENT c:COLON^ {#c.setType(JavaTokenTypes.LABELED_STAT);} statement

        // If-else statement
        |       "if"^ LPAREN expression RPAREN { _references.enterScope(); } statement
                { _references.leaveScope(); }
                (
                        // CONFLICT: the old "dangling-else" problem...
                        //           ANTLR generates proper code matching
                        //                       as soon as possible.  Hush warning.
                        options {
                                warnWhenFollowAmbig = false;
                        }
                :
                        "else" { _references.enterScope(); } statement { _references.leaveScope(); }
                )?

        // For statement
        |       "for"^  { _references.enterScope(); }
                        LPAREN
                                forInit SEMI   // initializer
                                forCond SEMI   // condition test
                                forIter         // updater
                        RPAREN
                        statement                     // statement to loop over
                        { _references.leaveScope(); }

        // While statement
        |       "while"^ LPAREN expression RPAREN { _references.enterScope(); } statement { _references.leaveScope(); }

        // do-while statement
        |       "do"^ { _references.enterScope(); } statement { _references.leaveScope(); }
                "while" LPAREN expression RPAREN SEMI

        // get out of a loop (or switch)
        |       "break"^ (bid:IDENT { addIdentifier(bid.getText()); })? SEMI

        // assert statement
        |       "assert"^ expression (COLON! expression)? SEMI

        // do next iteration of a loop
        |       "continue"^ (cid:IDENT { addIdentifier(cid.getText()); })? SEMI

        // Return an expression
        |       "return"^ (expression)? SEMI

        // switch/case statement
        |       "switch"^ LPAREN expression RPAREN LCURLY {_references.enterScope(); }
                        ( casesGroup )*
                { _references.leaveScope(); }
                RCURLY

        // exception try-catch block
        |       tryBlock

        // throw an exception
        |       "throw"^ expression SEMI

        // synchronize a statement
        |       synBlock:"synchronized"^ LPAREN expression RPAREN
                { #synBlock.setType(JavaTokenTypes.SYNBLOCK);
                _references.enterScope(); }
                compoundStatement


        // empty statement
        |       emptyStat:SEMI {#emptyStat.setType(JavaTokenTypes.EMPTY_STAT);}
        ;


casesGroup
        :       (       // CONFLICT: to which case group do the statements bind?
                        //           ANTLR generates proper code: it groups the
                        //           many "case"/"default" labels together then
                        //           follows them with the statements
                        options {
                                warnWhenFollowAmbig = false;
                        }
                        :
                        aCase
                )+
                caseSList
                {#casesGroup = #([JavaTokenTypes.CASE_GROUP, "CASE_GROUP"], #casesGroup);}
        ;

aCase
        :       ("case"^ expression | "default") COLON
        ;

caseSList
        :       (statement)*
                {#caseSList = #(#[JavaTokenTypes.CASESLIST,STR_CASESLIST],#caseSList);}
        ;

// The initializer for a for loop
forInit
                // if it looks like a declaration, it is
        :       (       (declaration)=> declaration
                // otherwise it could be an expression list...
                |       expressionList
                )?
                {#forInit = #(#[JavaTokenTypes.FOR_INIT,"FOR_INIT"],#forInit);}
        ;

forCond
        :       (expression)?
                {#forCond = #(#[JavaTokenTypes.FOR_CONDITION,"FOR_CONDITION"],#forCond);}
        ;

forIter
        :       (expressionList)?
                {#forIter = #(#[JavaTokenTypes.FOR_ITERATOR,"FOR_ITERATOR"],#forIter);}
        ;

// an exception handler try/catch block
tryBlock
        :       "try"^ { _references.enterScope(); } compoundStatement
                (handler)*
                ( finallyBlock )?
        ;


// an exception handler
handler
        :       "catch"^ LPAREN { _references.enterScope(); } parameterDeclaration RPAREN compoundStatement
        ;


finallyBlock
        : "finally"^ { _references.enterScope(); } compoundStatement
        ;


// expressions
// Note that most of these expressions follow the pattern
//   thisLevelExpression :
//       nextHigherPrecedenceExpression
//           (OPERATOR nextHigherPrecedenceExpression)*
// which is a standard recursive definition for a parsing an expression.
// The operators in java have the following precedences:
//    lowest  (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
//            (12)  ?:
//            (11)  ||
//            (10)  &&
//            ( 9)  |
//            ( 8)  ^
//            ( 7)  &
//            ( 6)  == !=
//            ( 5)  < <= > >=
//            ( 4)  << >>
//            ( 3)  +(binary) -(binary)
//            ( 2)  * / %
//            ( 1)  ++ -- +(unary) -(unary)  ~  !  (type)
//                  []   () (method call)  . (dot -- identifier qualification)
//                  new   ()  (explicit parenthesis)
//
// the last two are not usually on a precedence chart; I put them in
// to point out that new has a higher precedence than '.', so you
// can validy use
//     new Frame().show()
//
// Note that the above precedence levels map to the rules below...
// Once you have a precedence chart, writing the appropriate rules as below
//   is usually very straightfoward



// the mother of all expressions
expression
        :       assignmentExpression
                {#expression = #(#[JavaTokenTypes.EXPR,STR_EXPR],#expression);}
        ;


// This is a list of expressions.
expressionList
        :       expression (COMMA expression)*
                {#expressionList = #(#[JavaTokenTypes.ELIST,STR_ELIST], expressionList);}
        ;


// assignment expression (level 13)
assignmentExpression
        :       conditionalExpression
                (       (       ASSIGN^
            |   PLUS_ASSIGN^
            |   MINUS_ASSIGN^
            |   STAR_ASSIGN^
            |   DIV_ASSIGN^
            |   MOD_ASSIGN^
            |   SR_ASSIGN^
            |   BSR_ASSIGN^
            |   SL_ASSIGN^
            |   BAND_ASSIGN^
            |   BXOR_ASSIGN^
            |   BOR_ASSIGN^
            )
                        assignmentExpression
                )?
        ;


// conditional test (level 12)
conditionalExpression
        :       logicalOrExpression
                ( QUESTION^ assignmentExpression COLON conditionalExpression )?
        ;


// logical or (||)  (level 11)
logicalOrExpression
        :       logicalAndExpression (LOR^ logicalAndExpression)*
        ;


// logical and (&&)  (level 10)
logicalAndExpression
        :       inclusiveOrExpression (LAND^ inclusiveOrExpression)*
        ;


// bitwise or non-short-circuiting or (|)  (level 9)
inclusiveOrExpression
        :       exclusiveOrExpression (BOR^ exclusiveOrExpression)*
        ;


// exclusive or (^)  (level 8)
exclusiveOrExpression
        :       andExpression (BXOR^ andExpression)*
        ;


// bitwise or non-short-circuiting and (&)  (level 7)
andExpression
        :       equalityExpression (BAND^ equalityExpression)*
        ;


// equality/inequality (==/!=) (level 6)
equalityExpression
        :       relationalExpression ((NOT_EQUAL^ | EQUAL^) relationalExpression)*
        ;


// boolean relational expressions (level 5)
relationalExpression
        :       shiftExpression
                (       (       (       LT^
                                |       GT^
                                |       LE^
                                |       GE^
                                )
                                shiftExpression
                        )*
                |       "instanceof"^ typeSpec[true]
                )
        ;


// bit shift expressions (level 4)
shiftExpression
        :       additiveExpression ((SL^ | SR^ | BSR^) additiveExpression)*
        ;


// binary addition/subtraction (level 3)
additiveExpression
        :       multiplicativeExpression ((PLUS^ | MINUS^) multiplicativeExpression)*
        ;


// multiplication/division/modulo (level 2)
multiplicativeExpression
        :       unaryExpression ((STAR^ | DIV^ | MOD^ ) unaryExpression)*
        ;

unaryExpression
        :       INC^ unaryExpression
        |       DEC^ unaryExpression
        |       MINUS^ {#MINUS.setType(JavaTokenTypes.UNARY_MINUS);} unaryExpression
        |       PLUS^  {#PLUS.setType(JavaTokenTypes.UNARY_PLUS);} unaryExpression
        |       unaryExpressionNotPlusMinus
        ;

unaryExpressionNotPlusMinus
        :       BNOT^ unaryExpression
        |       LNOT^ unaryExpression

        |       (       // subrule allows option to shut off warnings
                        options {
                                // "(int" ambig with postfixExpr due to lack of sequence
                                // info in linear approximate LL(k).  It's ok.  Shut up.
                                generateAmbigWarnings=false;
                        }
                :       // If typecast is built in type, must be numeric operand
                        // Also, no reason to backtrack if type keyword like int, float...
                        (LPAREN builtInTypeSpec[true] RPAREN unaryExpression)=>
                        lpb:LPAREN^ {#lpb.setType(JavaTokenTypes.TYPECAST);} builtInTypeSpec[true] RPAREN!
                        unaryExpression

                        // Have to backtrack to see if operator follows.  If no operator
                        // follows, it's a typecast.  No semantic checking needed to parse.
                        // if it _looks_ like a cast, it _is_ a cast; else it's a "(expr)"
                |       (LPAREN classTypeSpec[true] RPAREN unaryExpressionNotPlusMinus)=>
                        lp:LPAREN^ {#lp.setType(JavaTokenTypes.TYPECAST);} classTypeSpec[true] RPAREN!
                        unaryExpressionNotPlusMinus
                |       postfixExpression
                )
        ;


// qualified names, array expressions, method invocation, post inc/dec
postfixExpression
{Token t; _buildList.clear();
 StringBuffer buf = new StringBuffer(50);
 }
        :      t=primaryExpression // start with a primary
                {
                  if (t != null)
                  {
                     buf.append(t.getText().intern());
                  }
                }

                (
                        // qualified id (id.id.id.id...) -- build the name
                        DOT^
                        {
                            if (t != null && _buildList.isEmpty())
                            {
                                String text = t.getText().intern();
                                addIdentifier(text);
                                _buildList.add(text);

                            }
                        }
                                ( id:IDENT { _buildList.add(id.getText().intern());

                                             if (t != null)
                                             {
                                                buf.append(".".intern());
                                                buf.append(id.getText().intern());
                                             }
                                           }
                                | tt:"this" { if (t != null) {_buf.append(".this"); buf.append(".this");} }
                                | "class" { if (t != null) {_buf.append(".class");buf.append(".class");} }
                                | newExpression
                                | "super" // ClassName.super.field
                                )
                        // the above line needs a semantic check to make sure "class"
                        // is the _last_ qualifier.

                        // allow ClassName[].class
                |       ( lbc:LBRACK^ {#lbc.setType(JavaTokenTypes.ARRAY_DECLARATOR);} RBRACK! )+
                        DOT^ "class"

                        // an array indexing operation
                |       lb:LBRACK^ {#lb.setType(JavaTokenTypes.INDEX_OP);} expression RBRACK

                        // method invocation
                        // The next line is not strictly proper; it allows x(3)(4) or
                        //  x[2](4) which are not valid in Java.  If this grammar were used
                        //  to validate a Java program a semantic check would be needed, or
                        //   this rule would get really ugly...
                        // It also allows ctor invocation like super(3) which is now
                        // handled by the explicit constructor rule, but it would
                        // be hard to syntactically prevent ctor calls here
                |       lp:LPAREN^ {#lp.setType(JavaTokenTypes.METHOD_CALL);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);}
                                argList
                        RPAREN
                )*

                // possibly add on a post-increment or post-decrement.
                // allows INC/DEC on too much, but semantics can check
                (       in:INC^ {#in.setType(JavaTokenTypes.POST_INC);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);}
                |       de:DEC^ {#de.setType(JavaTokenTypes.POST_DEC);if (!_buildList.isEmpty())_buildList.remove(_buildList.size() - 1);}
                |       // nothing
                )

                {
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
        ;

// the basic element of an expression
primaryExpression  returns [Token t]
{t=null;}
        :       id:IDENT { t = (Token)id; }
        |       constant
        |       "true"
        |       "false"
        |       th:"this" { t = (Token)th; }
        |       "null"
        |       newExpression
        |       lp:LPAREN a:assignmentExpression rp:RPAREN
        |       "super"
                // look for int.class and int[].class
        |       builtInType
                ( lbt:LBRACK^ {#lbt.setType(JavaTokenTypes.ARRAY_DECLARATOR);} RBRACK! )*
                DOT^ "class"
        ;


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
newExpression
        :       "new"^ type
                (       LPAREN argList RPAREN (classBlock)?

                        //java 1.1
                        // Note: This will allow bad constructs like
                        //    new int[4][][3] {exp,exp}.
                        //    There needs to be a semantic check here...
                        // to make sure:
                        //   a) [ expr ] and [ ] are not mixed
                        //   b) [ expr ] and an init are not used together

                |       newArrayDeclarator (arrayInitializer)?
                )
        ;

argList
        :       (       expressionList
                |       /*nothing*/
                        {#argList = #[JavaTokenTypes.ELIST,STR_ELIST];}
                )
        ;

newArrayDeclarator
        :       (
                        // CONFLICT:
                        // newExpression is a primaryExpression which can be
                        // followed by an array index reference.  This is ok,
                        // as the generated code will stay in this loop as
                        // long as it sees an LBRACK (proper behavior)
                        options {
                                warnWhenFollowAmbig = false;
                        }
                :
                        lb:LBRACK^ {#lb.setType(JavaTokenTypes.ARRAY_DECLARATOR);}
                                (expression)?
                        RBRACK!
                )+
        ;

constant
        :       NUM_INT
        |       CHAR_LITERAL
        |       STRING_LITERAL
        |       NUM_FLOAT
        |       NUM_LONG
        |       NUM_DOUBLE
        ;


//----------------------------------------------------------------------------
// The Java scanner
//----------------------------------------------------------------------------
{
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.util.StringHelper;
import de.hunsicker.io.FileFormat;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
}

/**
 * Token lexer for the Java parser. Heavily based on the public domain
 * grammar file written by <a href="mailto:parrt@jguru.com">Terence Parr
 * </a> et al. See <a href="http://www.antlr.org/resources.html">
 * http://www.antlr.org/resources.html</a> for more info.
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
class JavaLexer extends Lexer;

options {
        exportVocab=Java;      // call the vocabulary "Java"
        testLiterals=false;    // don't automatically test for literals
        k=4;                   // four characters of lookahead
        charVocabulary='\u0000'..'\uFFFE';
        // without inlining some bitset tests, couldn't do unicode;
        // I need to make ANTLR generate smaller bitsets; see
        // bottom of JavaLexer.java
        codeGenBitsetTestThreshold=3;
        classHeaderSuffix = "Lexer";
        defaultErrorHandler = true;
        useTokenPrefix = true;
}
{
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
     * Returns the index within this string of the first occurrence of any of the line
     * separator characters (quot;\nquot;, quot;\r\nquot; or quot;\rquot;).
     *
     * @param str a string.
     *
     * @return the index of the first character of a newline string; otherwise
     *         <code>-1</code> is returned.
     */
    private void getNextSeparator(SeparatorInfo result, String str)
    {
        int offset = offset = str.indexOf("\r\n" /* NOI18N */); // DOS

        if (offset > -1)
        {
            result.offset = offset;
            result.length = 2;
        }
        else
        {
            result.length = 1;

            offset = str.indexOf('\n'); // UNIX

            if (offset > -1)
            {
                result.offset = offset;
            }
            else
            {
                offset = str.indexOf('\r'); // MAC
                result.offset = offset;
            }
        }
    }

    private final static class SeparatorInfo
    {
        int length = 1;
        int offset = -1;
    }

    /**
     * Removes the leading whitespace from each line of the given multi-line comment.
     *
     * @param comment a multi-line comment.
     * @param column the column offset of the line where the comment starts.
     * @param lineSeparator the line separator.
     *
     * @return comment without leading whitespace.
     */
    private String removeLeadingWhitespace(
        String comment,
        int    column,
        String lineSeparator)
    {
        String[] lines = split(comment, column);
        StringBuffer buf = new StringBuffer(comment.length());

        for (int i = 0, size = lines.length; i < size; i++)
        {
            buf.append(lines[i]);
            buf.append(_lineSeparator);
        }

        buf.setLength(buf.length() - _lineSeparator.length());

        return buf.toString();
    }


    /**
     * Returns the individual lines of the given multi-line comment.
     *
     * @param str a multi-line comment.
     * @param beginOffset the column offset of the line where the comment starts.
     *
     * @return the individual lines of the comment.
     */
    private String[] split(
        String str,
        int    beginOffset)
    {
        List lines = new ArrayList(15);

        SeparatorInfo info = new SeparatorInfo();

        for (getNextSeparator(info, str); info.offset > -1; getNextSeparator(info, str))
        {
            String line = str.substring(0, info.offset);
            str = str.substring(info.offset + info.length);

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

        return (String[]) lines.toArray(EMPTY_STRING_ARRAY);
    }
}


// OPERATORS
QUESTION                :       '?'             ;
LPAREN                  :       '('             ;
RPAREN                  :       ')'             ;
LBRACK                  :       '['             ;
RBRACK                  :       ']'             ;
LCURLY                  :       '{'             ;
RCURLY                  :       '}'             ;
COLON                   :       ':'             ;
COMMA                   :       ','             ;
//DOT                   :       '.'             ;
ASSIGN                  :       '='             ;
EQUAL                   :       "=="            ;
LNOT                    :       '!'             ;
BNOT                    :       '~'             ;
NOT_EQUAL               :       "!="    ;
DIV                             :       '/'             ;
DIV_ASSIGN              :       "/="    ;
PLUS                    :       '+'             ;
PLUS_ASSIGN             :       "+="    ;
INC                             :       "++"    ;
MINUS                   :       '-'             ;
MINUS_ASSIGN    :       "-="    ;
DEC                             :       "--"    ;
STAR                    :       '*'             ;
STAR_ASSIGN             :       "*="    ;
MOD                             :       '%'             ;
MOD_ASSIGN              :       "%="    ;
SR                              :       ">>"    ;
SR_ASSIGN               :       ">>="   ;
BSR                             :       ">>>"   ;
BSR_ASSIGN              :       ">>>="  ;
GE                              :       ">="    ;
GT                              :       ">"             ;
SL                              :       "<<"    ;
SL_ASSIGN               :       "<<="   ;
LE                              :       "<="    ;
LT                              :       '<'             ;
BXOR                    :       '^'             ;
BXOR_ASSIGN             :       "^="    ;
BOR                             :       '|'             ;
BOR_ASSIGN              :       "|="    ;
LOR                             :       "||"    ;
BAND                    :       '&'             ;
BAND_ASSIGN             :       "&="    ;
LAND                    :       "&&"    ;
SEMI                    :       ';'             ;


// Whitespace -- ignored
WS      :       (       ' '
                |       '\t'
                |       '\f'
                        // handle newlines
                |       (       options {generateAmbigWarnings=false;}
                        :       "\r\n"  // Evil DOS
                                { if (_fileFormat == FileFormat.UNKNOWN)
                                    _fileFormat = FileFormat.DOS;
                                    _lineSeparator = "\r\n";
                                }
                        |       '\n'    // Unix (the right way)
                                { if (_fileFormat == FileFormat.UNKNOWN)
                                    _fileFormat = FileFormat.UNIX;
                                    _lineSeparator = "\n";
                                }
                        |       '\r'    // Macintosh
                                { if (_fileFormat == FileFormat.UNKNOWN)
                                    _fileFormat = FileFormat.MAC;
                                    _lineSeparator = "\r";
                                }
                        )
                        { newline(); }
                )+
                //{ $setType(Token.SKIP); }
        ;

protected SPECIAL_COMMENT
{ int column = getColumn()-1; }
:

"//J-"

(
        options {
            generateAmbigWarnings=false;
            greedy=false;
        }
    :   '\r' ('\n')? {newline();}
    |   '\n'         {newline();}
    |   .
    )*


  "//J+"

  {
    String t = $getText;
    Token tok = new ExtendedToken(JavaTokenTypes.SPECIAL_COMMENT, StringHelper.leftPad(t, t.length()+column));

    $setToken(tok);
  }
;

protected SEPARATOR_COMMENT
: "//~" (~('\n'|'\r') {if (LA(1) == EOF_CHAR) break;} )*
        ( '\n'! | '\r'!('\n'!)? )?
        {newline();}
;

protected SL_COMMENT
: "//" (~('\n'|'\r') {if (LA(1) == EOF_CHAR) break;} )*
        ( '\n'! | '\r'!('\n'!)? )?
        {newline();}
;

COMMENT
:
    (
        options {
            // ANTLR does it right by consuming input as soon as possible
            generateAmbigWarnings=false;
        }:

        spec:SPECIAL_COMMENT {$setToken(spec);} |
        sep:SEPARATOR_COMMENT {$setToken(sep); } |
        sl:SL_COMMENT {$setToken(sl); }
    )
;


// multiple-line comments
ML_COMMENT
{
    // we need the line info for the Javadoc parser
    int line = getLine();
    int column = getColumn();
    boolean javadoc = false;
}
        :       "/*" { if (LA(1)=='*') javadoc = true; }

                (       /*      '\r' '\n' can be matched in one alternative or by matching
                                '\r' in one iteration and '\n' in another.  I am trying to
                                handle any flavor of newline that comes in, but the language
                                that allows both "\r\n" and "\r" and "\n" to all be valid
                                newline is ambiguous.  Consequently, the resulting grammar
                                must be ambiguous.  I'm shutting this warning off.
                         */
                        options {
                                generateAmbigWarnings=false;
                        }
                :
                        { LA(2)!='/' }? '*'
                |       '\r' '\n'               {newline();}
                |       '\r'                    {newline();}
                |       '\n'                    {newline();}
                |       ~('*'|'\n'|'\r')
                )*
                "*/"
        {

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
                            String t = $getText;
                            _recognizer.setLine(line);
                            _recognizer.setColumn(column);
                            _recognizer.parse(t, getFilename());
                            Node comment = (Node)_recognizer.getParseTree();

                            // ignore empty comments
                            if (comment != JavadocParser.EMPTY_JAVADOC_COMMENT)
                            {
                                ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, t);
                                token.comment = comment;
                                $setToken(token);
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

                        String t = $getText;

                        if (t.indexOf('\t') > -1)
                        {
                            t = StringHelper.replace(t, "\t", StringHelper.repeat(SPACE, getTabSize()));
                        }

                        t = removeLeadingWhitespace(t, column -1, _lineSeparator);

                        $setText(t);
                        $setType(JavaTokenTypes.JAVADOC_COMMENT);
                    }
                }
                else
                {
                    $setType(Token.SKIP);
                }
            }
            else
            {
                if (!this.removeMLComments)
                {
                    String t = $getText;

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

                    $setText(t);
                }
                else
                    $setType(Token.SKIP);
            }
        }
        ;


// character literals
CHAR_LITERAL
        :       '\'' ( ESC | ~'\'' ) '\''
        ;

// string literals
STRING_LITERAL
        :       '"' (ESC|~('"'|'\\'))* '"'
        ;


// escape sequence -- note that this is protected; it can only be called
//   from another lexer rule -- it will not ever directly return a token to
//   the parser
// There are various ambiguities hushed in this rule.  The optional
// '0'...'9' digit matches should be matched here rather than letting
// them go back to STRING_LITERAL to be matched.  ANTLR does the
// right thing by matching immediately; hence, it's ok to shut off
// the FOLLOW ambig warnings.
protected
ESC
        :       '\\'
                (       'n'
                |       'r'
                |       't'
                |       'b'
                |       'f'
                |       '"'
                |       '\''
                |       '\\'
                |       ('u')+ HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
                |       ('0'..'3')
                        (
                                options {
                                        warnWhenFollowAmbig = false;
                                }
                        :       ('0'..'7')
                                (
                                        options {
                                                warnWhenFollowAmbig = false;
                                        }
                                :       '0'..'7'
                                )?
                        )?
                |       ('4'..'7')
                        (
                                options {
                                        warnWhenFollowAmbig = false;
                                }
                        :       ('0'..'9')
                        )?
                )
        ;


// hexadecimal digit (again, note it's protected!)
protected
HEX_DIGIT
        :       ('0'..'9'|'A'..'F'|'a'..'f')
        ;


// a dummy rule to force vocabulary to be all characters (except special
//   ones that ANTLR uses internally (0 to 2)
protected
VOCAB
        :       '\3'..'\377'
        ;


// an identifier.  Note that testLiterals is set to true!  This means
// that after we match the rule, we look in the literals table to see
// if it's a literal or really an identifer
IDENT
        options {testLiterals=true;}
        :       ('a'..'z'|'A'..'Z'|'_'|'$'|'\u00C0'..'\u00D6'|'\u00D8'..'\u00F6'|'\u00F8'..'\u00FF')
                ('a'..'z'|'A'..'Z'|'_'|'0'..'9'|'$'|'\u00C0'..'\u00D6'|'\u00D8'..'\u00F6'|'\u00F8'..'\u00FF')*
        ;


// a numeric literal
NUM_INT
        {boolean isDecimal=false; Token t=null;}
    :   '.' {_ttype = JavaTokenTypes.DOT;}
            (   ('0'..'9')+ (EXPONENT)? (f1:FLOAT_SUFFIX {t=f1;})?
                {
                                if (t != null && t.getText().toUpperCase().indexOf('D')>=0) {
                        _ttype = JavaTokenTypes.NUM_DOUBLE;
                                }
                                else {
                        _ttype = JavaTokenTypes.NUM_FLOAT;
                                }
                                }
            )?

        |       (       '0' {isDecimal = true;} // special case for just '0'
                        (       ('x'|'X')
                                (                                                                                       // hex
                                        // the 'e'|'E' and float suffix stuff look
                                        // like hex digits, hence the (...)+ doesn't
                                        // know when to stop: ambig.  ANTLR resolves
                                        // it correctly by matching immediately.  It
                                        // is therefor ok to hush warning.
                                        options {
                                                warnWhenFollowAmbig=false;
                                        }
                                :       HEX_DIGIT
                                )+
                        |       ('0'..'7')+                                                                     // octal
                        )?
                |       ('1'..'9') ('0'..'9')*  {isDecimal=true;}               // non-zero decimal
                )
                (       ('l'|'L') { _ttype = JavaTokenTypes.NUM_LONG; }

                // only check to see if it's a float if looks like decimal so far
                |       {isDecimal}?
            (   '.' ('0'..'9')* (EXPONENT)? (f2:FLOAT_SUFFIX {t=f2;})?
            |   EXPONENT (f3:FLOAT_SUFFIX {t=f3;})?
            |   f4:FLOAT_SUFFIX {t=f4;}
            )
            {
                        if (t != null && t.getText().toUpperCase() .indexOf('D') >= 0) {
                _ttype = JavaTokenTypes.NUM_DOUBLE;
                        }
            else {
                _ttype = JavaTokenTypes.NUM_FLOAT;
                        }
                        }
        )?
        ;


// a couple protected methods to assist in matching floating point numbers
protected
EXPONENT
        :       ('e'|'E') ('+'|'-')? ('0'..'9')+
        ;


protected
FLOAT_SUFFIX
        :       'f'|'F'|'d'|'D'
        ;

