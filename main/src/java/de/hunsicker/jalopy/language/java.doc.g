header
{
package de.hunsicker.jalopy.language;
}

{
import de.hunsicker.antlr.CommonAST;
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
}

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
class JavadocParser extends Parser;
options {
    genHashLines = true;
    importVocab = Common;
    exportVocab = Javadoc;
    k = 1;
    buildAST = true;
    classHeaderSuffix = "Parser";
    defaultErrorHandler = true;
    codeGenMakeSwitchThreshold = 2;
    codeGenBitsetTestThreshold = 3;
}

tokens {
    OTH; CTH; OTD; CTD; TAG_CUSTOM; TAG_AUTHOR; TAG_DEPRECATED; TAG_EXCEPTION;
    TAG_THROWS; TAG_PARAM; TAG_RETURN; TAG_SEE; TAG_SINCE; TAG_SERIAL;
    TAG_SERIAL_DATA; TAG_SERIAL_FIELD; TAG_VERSION; TAG_INLINE_CUSTOM;
    TAG_INLINE_DOCROOT; TAG_INLINE_INHERITDOC; TAG_INLINE_LINK;
    TAG_INLINE_LINKPLAIN; TAG_INLINE_VALUE; TAG_TODO;
}
{
    /** Logging. */
    private final Logger _logger = Logger.getLogger("de.hunsicker.jalopy.language.javadoc");

    /** Holds all valid tag names. */
    private Set _standardTags = new HashSet(); // Set of <String>

    /** Holds all valid inline tag names. */
    private Set _inlineTags = new HashSet(); // Set of <String>

    /** The token types for the parser/lexer. */
    private Map _tokenTypes; // Map of <String>

    /** The corresponding lexer. */
    private JavadocLexer _lexer;

    /** Starting line of the comment in the source file. */
    private int _startLine;

    /** Starting column of the comment in the source file. */
    private int _startColumn;

    /** Last modification stamp of the property file for inline Javadoc tags. */
    private long _inlineStamp;

    /** Last modification stamp of the property file for standard Javadoc tags. */
    private long _standardStamp;

    /** The empty Javadoc comment. */
    public final static Node EMPTY_JAVADOC_COMMENT = new Node(JavadocTokenTypes.JAVADOC_COMMENT, "<JAVADOC_COMMENT>");

    /** Indicates a standard Javadoc tag. */
    private final static String TYPE_STANDARD = "TAG_";

    /** Indicates an inline Javadoc tag. */
    private final static String TYPE_INLINE = "TAG_INLINE_";

    /** File to load the standard Javadoc tag definitions from. */
    //private final static File STANDARD_TAGS = new File(System.getProperty("user.home")+ File.separator + ".jalopy" + File.separator + "standard.tags");

    /** File to load the inline Javadoc tag definitions from. */
    //private final static File INLINE_TAGS = new File(System.getProperty("user.home")+ File.separator + ".jalopy" + File.separator + "inline.tags");

    {
        loadTokenTypeInfo();
        loadTagInfo(true);
    }

    /**
     * Sets the corresponding Javadoc lexer for the parser.
     * @param lexer corresponding Javadoc lexer.
     */
    public void setLexer(JavadocLexer lexer)
    {
        _lexer = lexer;
    }

    /**
     * Loads the token type mapping table.
     */
    private void loadTokenTypeInfo()
    {
        try
        {
            Properties props = new Properties();
            props.load(JavadocLexer.class.getResourceAsStream("JavadocTokenTypes.txt"));
            _tokenTypes = new HashMap((int)(props.size() * 1.3));
            _tokenTypes.putAll(props);
        }
        catch (Exception ex)
        {
            throw new RuntimeException("failed loading token types file -- JavadocTokenTypes.txt");
        }
    }

    /**
     * Loads the properties from the given file.
     * @param file property file.
     */
    private Properties loadProperties(File file)
        throws IOException
    {
        Properties props = new Properties();

        if (file.exists())
        {
            InputStream in  = null;

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
     * Loads the custom tag info from the given tag definition. The found tags
     * will be added to the given set.
     *
     * @param file the tag definition file.
     * @param tags set to add the tags to.
     */
    private void loadCustomTagInfo(File file, Set tags)
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
            _standardTags.add("@version");
            _standardTags.add("@todo");
            //loadCustomTagInfo(STANDARD_TAGS, _standardTags);
            //_standardStamp = STANDARD_TAGS.lastModified();
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
     * Sets the custom Javadoc standard tags to recognize.
     *
     * @param tags tags.
     */
    public void setCustomStandardTags(Collection tags)
    {
        _standardTags.addAll(tags);
    }

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
     * Tries to determine and set the correct type for the given tag node.
     * @param tag tag AST to set the correct type for.
     * @param type the type of the tag. Either {@link #TYPE_STANDARD} or
     *        {@link #TYPE_INLINE}
     */
    private void setTagType(AST tag, String type)
    {
        String text = tag.getText();
        String name = getTag(text, type);

        if (name == null) // invalid tag
        {
            Object[] args = { getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()), text };
            _logger.l7dlog(Level.ERROR, "TAG_INVALID", args, null);
        }
        else
        {
            if (name != text) // mispelled tag
            {
                // correct the tag name
                tag.setText(name);

                Object[] args = { getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()), text, name};
                _logger.l7dlog(Level.WARN, "TAG_MISSPELLED_NAME", args, null);
            }

            String t = (String)_tokenTypes.get(type +
                                               name.substring(1).toUpperCase());

            // not found in the mapping table means custom tag
            if (t == null)
            {
                if (type == TYPE_STANDARD)
                    tag.setType(JavadocTokenTypes.TAG_CUSTOM);
                else
                    tag.setType(JavadocTokenTypes.TAG_INLINE_CUSTOM);
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
     * Returns the matching tag for the given text, if any.
     *
     * @param text text to match against.
     * @param type the type of the tag. Either {@link #TYPE_STANDARD} or
     *        {@link #TYPE_INLINE}
     *
     * @return matching Javadoc tag. Returns <code>null</code> to indicate that
     * no valid tag could be found for the given text. If the given text
     * represents a valid tag the given text reference will be returned.
     */
    String getTag(String text, String type)
    {
        Set tags = null;

        if (type == TYPE_STANDARD)
            tags = _standardTags;
        else
            tags = _inlineTags;

        if (tags.contains(text))
        {
            return text;
        }

        Lcs lcs = new Lcs();

        for (Iterator i = tags.iterator(); i.hasNext();)
        {
            String tag = (String)i.next();

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
     * Returns the root node of the AST.
     *
     * @return root node of the AST.
     */
    public AST getAST()
    {
        // can be null for empty comments. The empty node indicates that this
        // comment can be savely ignored. It is up to the caller to handle
        // it appropriate
        if (this.returnAST == null)
        {
          return EMPTY_JAVADOC_COMMENT;
        }

        Node current = (Node)this.returnAST;
        Node root = new Node(_startLine, _startColumn,
                                           current.endLine, current.endColumn);
        root.setType(JavadocTokenTypes.JAVADOC_COMMENT);
        root.setText(JAVADOC_COMMENT);
        root.setFirstChild(this.returnAST);

        return root;
    }

    private final static String JAVADOC_COMMENT = "JAVADOC_COMMENT";

   /**
    * Reports the given error.
    *
    * @param ex encountered exception.
    */
   public void reportError(RecognitionException ex)
   {
      Object args[]  = { getFilename(), new Integer(ex.line), new Integer(ex.column), ex.getMessage() };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, ex);
   }

   /**
    * Reports the given error.
    *
    * @param message error message.
    */
   public void reportError(String message)
   {
      Object args[]  = { getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()), message };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, null);
   }

   /**
    * Reports the given warning.
    *
    * @param message warning message.
    */
   public void reportWarning(String message)
   {
      Object args[]  = { getFilename(), new Integer(_lexer.getLine()), new Integer(_lexer.getColumn()), message };
      _logger.l7dlog(Level.WARN, message, args, null);
   }

    /**
     * Handler for recoverable errors. If the error can't be handled it will be
     * rethrown.
     *
     * @param ex the input violation exception.
     */
    public void handleRecoverableError(RecognitionException ex)
    {
        if (ex instanceof de.hunsicker.antlr.MismatchedTokenException)
        {
            MismatchedTokenException mtex = (MismatchedTokenException)ex;

            if (mtex.token != null)
            {
                switch (mtex.expecting)
                {
                    // missing closing </p> tag
                    case JavadocTokenTypes.CPARA:

                        System.err.println("[WARN] ambigious missing </p> tag around line " + mtex.token.getLine() + " ");

                        // we add the found token manually
                        ASTPair currentAST = new ASTPair();
                        AST tmp2_AST = this.astFactory.create(mtex.token);
                        this.astFactory.makeASTRoot(currentAST, tmp2_AST);
                        this.astFactory.addASTChild(currentAST, this.returnAST);

                        // we also have to increase the line counter
                        _lexer.setLine(_lexer.getLine() + 1);

                        // now the parsing can go on
                        break;

                    default:
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
}

parse
    :  {
            // Uncomment to check the tag definition files for update
            //loadTagInfo(false);
            _startLine = _lexer.getLine();
            _startColumn = _lexer.getColumn();
       }

      JAVADOC_OPEN! ( body_content )* ( standard_tag )* JAVADOC_CLOSE!
    ;

body_content
    :   body_tag  | text
    ;

body_tag
    :   block | address | heading
    ;

heading
    :   h1 | h2 | h3 | h4 | h5 | h6
    ;

block
    :   paragraph | list | preformatted | div |
        center | blockquote | HR | table
    ;

font:   teletype | italic | bold | underline | strike |
        big | small | subscript | superscript
    ;

phrase
    :   emphasize | strong | definition | code | sample |
        keyboard | variable | citation | acronym
    ;

special
    :   anchor | IMG | font_dfn | BR
    ;

text_tag
    :   font | phrase | special
    ;

text
    :   ( LCURLY TAG ) => inline_tag | LCURLY | RCURLY | text_tag | COMMENT | PCDATA
    ;

/* BLOCK ELEMENTS */

h1
    :   OH1^ heading_content CH1
    ;

h2
    :   OH2^ heading_content CH2
    ;

h3
    :   OH3^ heading_content CH3
    ;

h4
    :   OH4^ heading_content CH4
    ;

h5
    :   OH5^ heading_content CH5
    ;

h6
    :   OH6^ heading_content CH6
    ;

heading_content
    :   (block | text)*
    ;

address
    :   OADDRESS (PCDATA)* CADDRESS
    ;

// NOTE:  according to the standard, paragraphs can't contain block elements
// like HR.  Netscape may insert these elements into paragraphs.
// We adhere strictly here.

paragraph
    :   OPARA^
        (
            /*  Rule body_content may also be just plain text because HTML is
                so loose.  When parse puts body_content in a loop, ANTLR
                doesn't know whether you want it to match all the text as part
                of this paragraph (in the case where the </p> is missing) or
                if the body rule should scarf it.  This is analogous to the
                dangling-else clause.  I shut off the warning.
            */
            options {
                generateAmbigWarnings=false;
            }
        :   text | list | div | center | blockquote | table | preformatted
        )*
        (CPARA!)?
    ;
    exception // for rule
    catch [RecognitionException ex]
    {
        handleRecoverableError(ex);
    }


list
    :   unordered_list
    |   ordered_list
    |   def_list
    ;

unordered_list
    :   OULIST^ (PCDATA)* (list_item)+ CULIST
    ;

ordered_list
    :   OOLIST^ (PCDATA)* (list_item)+ COLIST
    ;

def_list
    :   ODLIST^ (PCDATA)* (def_list_item)+ CDLIST
    ;

list_item
    :   OLITEM^ ( text | block )+ (CLITEM (PCDATA)*)?
    ;

def_list_item
    :   dt | dd
    ;

dt
    :   ODTERM^ (text)+ (CDTERM (PCDATA)*)?
    ;

dd
    :   ODDEF^ (text | block)+ (CDDEF (PCDATA)*)?
    ;

dir
    :   ODIR^ (list_item)+ (CDIR)?
    ;

div
    :   ODIV^ (body_content)* CDIV     //semi-revised
    ;

center
    :   OCENTER^ (body_content)* CCENTER //semi-revised
    ;

blockquote
    :   OBQUOTE^ (body_content)* CBQUOTE
    ;

preformatted
    : PRE^
    ;

table
    :   OTABLE^ (caption)? (PCDATA)* (tr)+ CTABLE!
    ;

caption
    :   OCAP^ (text)* CCAP
    ;

tr
    :   O_TR^ ((PCDATA)* | COMMENT) (th_or_td)* (C_TR! ((PCDATA)* | COMMENT))?
    ;

th_or_td
    :   (OTH^ | OTD^) (body_content)* ((CTH! | CTD!) (PCDATA)*)?
    ;

/* TEXT ELEMENTS */

/* font style */

teletype
    :   OTTYPE^ ( text )+ CTTYPE
    ;

italic
    :   OITALIC^ ( text )+ CITALIC
    ;

bold:   OBOLD^ ( text )+ CBOLD
    ;

code:   OCODE^ ( text )+ CCODE
    ;

underline
    :   OUNDER^ ( text )+ CUNDER
    ;

strike
    :   OSTRIKE^ ( text )+ CSTRIKE
    ;

big :   OBIG^ ( text )+ CBIG
    ;

small
    :   OSMALL^ ( text )+ CSMALL
    ;

subscript
    :   OSUB^ ( text )+ CSUB
    ;

superscript
    :   OSUP^ ( text )+ CSUP
    ;

/* phrase elements */

emphasize
    :   OEM^ ( text )+ CEM
    ;

strong
    :   OSTRONG^ ( text )+ CSTRONG
    ;

definition
    :   ODFN^ ( text )+ CDFN
    ;

sample
    :   OSAMP^ ( text )+ CSAMP
    ;

keyboard
    :   OKBD^ ( text )+ CKBD
    ;

variable
    :   OVAR^ ( text )+ CVAR
    ;

citation
    :   OCITE^ ( text )+ CCITE
    ;

acronym
    :   OACRO^ ( text )+ CACRO
    ;

/* special text level elements */
anchor
    :   OANCHOR^ ( anchor_content )*  CANCHOR
    ;

anchor_content
    :   font | phrase | PCDATA
    ;

//not w3-no blocks allowed; www.microsoft.com uses
font_dfn
    :   OFONT^ ( text )* CFONT
    ;

standard_tag
   :    (
            tag:TAG^ { setTagType(#tag, TYPE_STANDARD); } (text | block)*
          | AT {#standard_tag.setType(JavadocTokenTypes.AT); }
        )

        /*{
            switch (tag_AST.getType())
            {
                case JavadocTokenTypes.TAG_PARAM:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing parameter-name and description", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_AUTHOR:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing name-text", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_THROWS :
                case JavadocTokenTypes.TAG_EXCEPTION:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing class-name and description", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_RETURN:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing description", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_SEE:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing reference", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_SINCE:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing since-text", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_VERSION:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing version-text", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_SERIAL_DATA:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing data-description", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;
                case JavadocTokenTypes.TAG_SERIAL_FIELD:
                    if (tag_AST.getFirstChild() == null)
                        throw new SemanticException("missing field-name, field-type and field-description", getFilename(), _lexer.getLine(), _lexer.getColumn());
                    break;

                //case JavadocTokenTypes.TAG_DEPRECATED:
                //case JavadocTokenTypes.TAG_SERIAL:
                //case JavadocTokenTypes.TAG_CUSTOM:
            }
        }*/
   ;

inline_tag
    :   LCURLY! tag:TAG^ { setTagType(#tag, TYPE_INLINE); }
        (~RCURLY)* RCURLY!
    ;


////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////////////////////////////////////////

{
import de.hunsicker.util.Lcs;
import java.io.StringReader;
import de.hunsicker.io.FileFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
}

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
class JavadocLexer extends Lexer;
options {
    k = 4;
    genHashLines = true;
    exportVocab = Javadoc;
    charVocabulary = '\u0003'..'\uFFFE';
    codeGenMakeSwitchThreshold = 2;
    codeGenBitsetTestThreshold = 3;
    caseSensitive = false;
    classHeaderSuffix = "Lexer";
    defaultErrorHandler = true;
}

{
    /** Logging. */
    private Logger _logger = Logger.getLogger("de.hunsicker.jalopy.language.javadoc");

    /** Our undefined token constant. */
    final static int UNDEFINED_TOKEN = -10;

    /** The detected file format. */
    private FileFormat _fileFormat = FileFormat.UNKNOWN;

    /** Marker to track position (line/column) information. */
    private Position _mark;

    /** Current position in the stream. */
    private Position _position = new Position(1, 1);

    /** Holds the string to use as a replacement for tab characters. */
    private String _tabString;

    /** Corresponding Javadoc parser. */
    private JavadocParser _parser;

    /**
     * Creates a new JavadocLexer object. Use {@link #setInputBuffer(Reader)}
     * to set up the input buffer.
     */
    public JavadocLexer()
    {
        this(new StringReader(""));
        _parser = new JavadocParser(this);
        _parser.setASTFactory(new NodeFactory());
        _parser.setLexer(this);
    }

    public Parser getParser()
    {
        return _parser;
    }

    /**
     * Reports a fatal error.
     */
    public void panic()
    {
        if (this.inputState != null)
        {
            Object[] args = { getFilename(), new Integer(getLine()), new Integer(getColumn()), "JavadocLexer: panic" };
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

   /**
    * Reports a fatal error.
    * @param message the error message.
    */
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
      Object args[] = { getFilename(), new Integer(getLine()), new Integer(getColumn()), ex.getMessage() };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR" , args, ex);
   }

   /**
    * Reports the given error.
    *
    * @param message error message.
    */
   public void reportError(String message)
   {
      Object args[]  = { getFilename(), new Integer(getLine()), new Integer(getColumn()), message };
      _logger.l7dlog(Level.ERROR, "PARSER_ERROR", args, null);
   }

   /**
    * Reports the given warning.
    *
    * @param message warning message.
    */
   public void reportWarning(String message)
   {
      Object args[]  = { getFilename(), new Integer(getLine()), new Integer(getColumn()), message };
      _logger.l7dlog(Level.WARN, "PARSER_ERROR", args, null);
   }

    /**
     * Returns the detected file format.
     *
     * @return The detected file format.
     */
    public FileFormat getFileFormat()
    {
        return _fileFormat;
    }

    /**
     * Sets the class to use for tokens.
     *
     * @param clazz a qualified class name.
     */
    public void setTokenObjectClass(String clazz)
    {
        super.setTokenObjectClass("de.hunsicker.jalopy.language.ExtendedToken");
    }

    /**
     * Returns the current token position.
     *
     * @return current position.
     */
    private Position getPosition()
    {
        _position.line = getLine();
        _position.column = getColumn();
        return _position;
    }

    /**
     * Sets the current token position.
     *
     * @param pos position to set.
     */
    private void setPosition(Position pos)
    {
        setColumn(pos.column);
        setLine(pos.line);
    }

    /**
     * Holds position information.
     */
    private static class Position
        implements Comparable
    {
        int line;
        int column;

        public Position(int line, int column)
        {
            this.line = line;
            this.column = column;
        }

        public int compareTo(Object o)
        {
            if (o == this)
                return 0;

            Position other = (Position)o;

            if (this.line > other.line)
                return -1;
            else if (this.line < other.line)
            {
                return 1;
            }
            else
            {
                if (this.column > other.column)
                    return -1;
                else if (this.column < other.column)
                    return 1;
            }

            return 0;
        }

        public String toString()
        {
            StringBuffer buf = new StringBuffer(20);
            buf.append("[");
            buf.append("line=");
            buf.append(this.line);
            buf.append(",col=");
            buf.append(this.column);
            buf.append("]");

            return buf.toString();
        }
    }

    /**
     * Sets the input buffer to use.
     * @param buf buffer to read from.
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
     * Resets the lexer. Remember that you have to set up the input buffer
     * before start parsing again.
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
        _tabString = null;
        _fileFormat = FileFormat.UNKNOWN;
        _mark = null;
    }

    /**
     * Creates a token of the given tpye.
     *
     * @param t type of the token.
     */
    protected Token makeToken(int t)
    {

        // if we find a mark, we use this position as the end of the token
        // look at newline() for background information
        if (_mark != null)
        {
            Position cur = getPosition();
            setPosition(_mark);

            ExtendedToken tok = new ExtendedToken(t, inputState.tokenStartLine,
            inputState.tokenStartColumn, inputState.line, inputState.column);
            _mark = null;

            setPosition(cur);

            return tok;
        }
        else
        {
            return new ExtendedToken(t, inputState.tokenStartLine,
                                     inputState.tokenStartColumn, inputState.line,
                                     inputState.column);
        }
    }

    /** Amount of spaces used to replace a tab. */
    private int _tabSize = 4;

    /**
     * Returns the current tab size.
     * @return current tab size;
     */
    public int getTabSize()
    {
        return _tabSize;
    }

    /**
     * Sets the tab size to use.
     * @param size tab size to use.
     */
    public void setTabSize(int size)
    {
        _tabSize = size;
    }

    /**
     * Replaces the tab char last read into the text buffer with an
     * equivalent number of spaces. Note that we assume you know what you do,
     * we don't check if indeed the tab char were read!
     *
     * @throws CharStreamException if an I/O error occured.
     */
    private void replaceTab()
        throws CharStreamException
    {
        if (_tabString == null)
        {
            int tabSize = getTabSize(); // makes for faster array access
            StringBuffer indent = new StringBuffer(tabSize);

            for (int i = 0; i < tabSize; i++)
            {
                indent.append(' ');
            }

            _tabString = indent.toString();
        }

        // remove the tab char from the buffer
        this.text.setLength(text.length() - 1);

        // and insert the spaces
        this.text.append(_tabString);
    }

    /**
     * Replaces the newline chars last read into the text buffer with a
     * single space. Note that we assume you know what you do; we don't check
     * if indeed newline chars were read!
     *
     * @param length length of the newline chars (1 or 2).
     * @throws CharStreamException if an I/O error occured.
     */
    private void replaceNewline(int length)
            throws CharStreamException
    {

        // remove the newline chars
        this.text.setLength(text.length() - length);
        newline();

        // only add a space if the next character is not already one
        if (LA(1) != ' ')
        {
            if (this.text.length() > 0)
            {
                // only add space if necessary (depending on the last char)
                switch (this.text.charAt(this.text.length() - 1))
                {
                    case ' ':
                    case '-':
                    case '(':
                    case '[':
                    case '{':
                        break;
                    default:
                        this.text.append(' ');
                }
            }
            else
            {
                this.text.append(' ');
            }
        }
    }

   /**
    * Inserts a newline. Skips all leading whitespace until the last space
    * before the first word.
    *
    * @see #makeToken
    */
    public void newline()
    {
        newline(true);
    }

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
    public void newline(boolean skipAllLeadingWhitespace)
    {
        // because we manually advance the stream position in
        // skipLeadingSpaceAndAsterix() we store the current position in order
        // to allow our tokens to be created with the correct size
        _mark = getPosition();
        super.newline();

        try
        {
            skipLeadingSpaceAndAsterix(skipAllLeadingWhitespace);
        }
        catch (CharStreamException ignored)
        {
            // really an I/O problem, so it will show up on the next rule
        }
    }

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
    private void skipLeadingSpaceAndAsterix(boolean skipAllLeadingWhitespace)
        throws CharStreamException
    {
        try
        {
            this.saveConsumedInput = false;

            int next = LA(1);

            boolean newline = false;

            while (next != EOF_CHAR)
            {
                switch (next)
                {
                    case '\n':
                    case '\r':
                        consume();

                        if (!newline)
                        {
                            setLine(getLine() + 1);
                            newline = true;
                        }

                        setColumn(1);
                        next = LA(1);
                        break;

                    case '\t':
                    case ' ':

                        // only eat up until the last space
                        switch (LA(2))
                        {
                           case ' ':
                           case '\t':
                               consume();
                           case '*':
                              break;

                           default:
                              return;
                        }

                        consume();
                        next = LA(1);
                        break;

                    case '*':

                        if (skipAllLeadingWhitespace)
                        {
                            // only allow if we don't encounter the closing delim
                            if (LA(2) != '/')
                            {
                                consume();
                                next = LA(1);
                                break;
                            }
                            else if (LA(2) == ' ')
                            {
                                consume();
                                consume();
                                return;
                            }
                            else
                                return;
                        }
                        else
                        {
                            switch (LA(2))
                            {
                                case '\r':
                                    if (LA(3) == '\n')
                                    {
                                        consume();
                                        return;
                                    }
                                case '\n':
                                case ' ':
                                    consume();
                                    consume();

                                    return;
                            }

                            // only allow if we don't encounter the closing delim
                            if (LA(2) != '/')
                            {
                                consume();
                                next = LA(1);

                                break;
                            }
                            else
                                return;
                        }

                    default:
                        this.text.append(' ');
                        return;
                }
            }
        }
        finally
        {
            this.saveConsumedInput = true;
        }
    }

    /**
     * Reads pending characters until whitespace is found (either '\r', '\n',
     * '\t', '\f', ' ').
     *
     * @return string of the characters read.
     */
    private void consumeUntilWhitespace()
    {
        try
        {
            for (int type = LA(1);; type = LA(1))
            {
                switch (type)
                {
                    case ' ':
                    case '\r':
                    case '\n':
                    case '\t':
                    case EOF_CHAR:
                        return;
                    default:
                        consume();
                }
            }
        }
        catch (CharStreamException ignored)
        {
        }
    }
}

/* headings */

OH1
    :   "<h1" (WS ATTR)? '>'
    ;

CH1
    :   "</h1>"
    ;

OH2
    :   "<h2" (WS ATTR)?'>'
    ;

CH2
    :   "</h2>"
    ;

OH3
    :   "<h3" (WS ATTR)? '>'
    ;

CH3
    :   "</h3>"
    ;

OH4
    :   "<h4" (WS ATTR)? '>'
    ;

CH4
    :   "</h4>"
    ;

OH5
    :   "<h5" (WS ATTR)? '>'
    ;

CH5
    :   "</h5>"
    ;

OH6
    :   "<h6" (WS ATTR)? '>'
    ;

CH6
    :   "</h6>"
    ;

/*  STRUCTURAL tags */
OADDRESS
    :   "<address" (WS ATTR)? '>' (NEWLINE!)?
    ;

CADDRESS
    :   "</address>"
    ;

OPARA
    :   "<p" (WS ATTR)? '>' (NEWLINE!)?
    ;

CPARA
    :   "</p>"  //it's optional
    ;

/*UNORDERED LIST*/
OULIST
    :   "<ul" (WS ATTR)? '>' (NEWLINE!)?
    ;

CULIST
    :   "</ul>"
    ;

        /*ORDERED LIST*/
OOLIST
    :   "<ol" (WS ATTR)? '>' (NEWLINE!)?
    ;

COLIST
    :   "</ol>"
    ;

        /*LIST ITEM*/

OLITEM
    :   "<li" (WS ATTR)? '>' (NEWLINE!)?
    ;

CLITEM
    :   "</li>"
    ;

        /*DEFINITION LIST*/

ODLIST
    :   "<dl" (WS ATTR)? '>' (NEWLINE!)?
    ;

CDLIST
    :   "</dl>"
    ;

ODTERM
    :   "<dt"  (WS ATTR)? '>' (NEWLINE!)?
    ;

CDTERM
    :   "</dt>"
    ;

ODDEF
    :   "<dd" (WS ATTR)? '>' (NEWLINE!)?
    ;

CDDEF
    :   "</dd>"
    ;

ODIR:   "<dir" (WS ATTR)? '>' (NEWLINE!)?
    ;

CDIR_OR_CDIV
    :   "</di"
        (   'r' {$setType(JavadocTokenTypes.CDIR);}
        |   'v' {$setType(JavadocTokenTypes.CDIV);}
        )
        '>'
    ;

ODIV
    :   "<div" (WS ATTR)? '>' (NEWLINE!)?
    ;

OCENTER
    :   "<center" (WS ATTR)? '>' (NEWLINE!)?
    ;

CCENTER
    :   "</center>"
    ;

OBQUOTE
    :   "<blockquote" (WS ATTR)? '>' (NEWLINE!)?
    ;

CBQUOTE
    :   "</blockquote>"
    ;

//this is block element and thus can't be nested inside of
//other block elements, ex: paragraphs.
//Netscape appears to generate bad HTML vis-a-vis the standard.

HR
    :   "<hr" (WS (ATTR)*)? '>'
    ;


OTABLE
    :   "<table" (WS (ATTR)*)? '>' (NEWLINE!)?
    ;

CTABLE
    :   "</table>"
    ;

OCAP
    :   "<caption" (WS (ATTR)*)? '>' (NEWLINE!)?
    ;

CCAP
    :   "</caption>"
    ;

O_TR
    :   "<tr" (WS (ATTR)*)? '>' (NEWLINE!)?
    ;

C_TR
    :   "</tr>"
    ;

O_TH_OR_TD
    :   ("<th" {$setType(JavadocTokenTypes.OTH);}
        | "<td") {$setType(JavadocTokenTypes.OTD);}
        (WS (ATTR)*)? '>' (NEWLINE!)?
    ;

C_TH_OR_TD
    :
      "</th>" {$setType(JavadocTokenTypes.CTH);}
    | "</td>" {$setType(JavadocTokenTypes.CTD);}
    ;

/* PCDATA-LEVEL ELEMENTS */
/* font style elemens*/
OTTYPE
    :   "<tt" (WS ATTR)? '>' (NEWLINE!)?
    ;

CTTYPE
    :   "</tt>"
    ;

OCODE
    : "<code" (WS ATTR)? '>' (NEWLINE!)?
    ;

CCODE
    : "</code>"
    ;

OITALIC
    :   "<i" (WS ATTR)? '>' (NEWLINE!)?
    ;

CITALIC
    :   "</i>"
    ;

OBOLD
    :   "<b" (WS ATTR)? '>' (NEWLINE!)?
    ;

CBOLD
    :   "</b>"
    ;

OUNDER
    :   "<u" (WS ATTR)? '>' (NEWLINE!)?
    ;

CUNDER
    :   "</u>"
    ;

// Left-factor <strike></strike> and <strong></strong> to reduce lookahead
OSTRIKE_OR_OSTRONG
    :   (
            "<str"
            (   "ike" {$setType(JavadocTokenTypes.OSTRIKE);}
            |   "ong" {$setType(JavadocTokenTypes.OSTRONG);}
            )
            | "<s" {$setType(JavadocTokenTypes.OSTRIKE); $setText("<strike");}
        )
        (WS ATTR)? '>' (NEWLINE!)?
    ;

CSTRIKE_OR_CSTRONG
    :   (
            "</st"
            (   "rike" {$setType(JavadocTokenTypes.CSTRIKE);}
            |   "rong" {$setType(JavadocTokenTypes.CSTRONG);}
            )
            | "</s" {$setType(JavadocTokenTypes.CSTRIKE); $setText("</strike");}
        )
        '>'
    ;

OBIG
    :   "<big" (WS ATTR)? '>' (NEWLINE!)?
    ;

CBIG
    :   "</big>"
    ;

OSMALL
    :   "<small" (WS ATTR)? '>' (NEWLINE!)?
    ;

CSMALL
    :   "</small>"
    ;

OSUB:   "<sub" (WS ATTR)? '>' (NEWLINE!)?
    ;

OSUP
    :   "<sup" (WS ATTR)? '>' (NEWLINE!)?
    ;

CSUB_OR_CSUP
    :   "</su"
        (   'b' {$setType(JavadocTokenTypes.CSUB);}
        |   'p' {$setType(JavadocTokenTypes.CSUP);}
        )
        '>'
    ;

/*      phrase elements*/
OEM
    :   "<em" (WS ATTR)? '>' (NEWLINE!)?
    ;

CEM
    :   "</em>"
    ;

ODFN
    :   "<dfn" (WS ATTR)? '>' (NEWLINE!)?
    ;

CDFN
    :   "</dfn>"
    ;

OSAMP
    :   "<samp" (WS ATTR)? '>' (NEWLINE!)?
    ;

CSAMP
    :   "</samp>"
    ;

OKBD
    :   "<kbd" (WS ATTR)? '>' (NEWLINE!)?
    ;

CKBD
    :   "</kbd>"
    ;

OVAR
    :   "<var" (WS ATTR)? '>' (NEWLINE!)?
    ;

CVAR
    :   "</var>"
    ;

OCITE
    :   "<cite" (WS ATTR)? '>' (NEWLINE!)?
    ;

CCITE
    :   "</cite>"
    ;

OACRO
    :   "<acronym" (WS ATTR)? '>' (NEWLINE!)?
    ;

CACRO
    :   "</acronym>"
    ;

/* special text level elements*/
OANCHOR
    :   "<a" WS (ATTR)+ '>' (NEWLINE!)?
    ;

CANCHOR
    :   "</a>"
    ;

IMG
    :   "<img" WS (ATTR)+ '>'
    ;

OFONT
    :   "<font" WS (ATTR)+ '>' (NEWLINE!)?
    ;

CFONT
    :   "</font>"
    ;


BR
    :   "<br" (WS ATTR)? ('/')? '>'
    ;

STAR
    :   '*' { $setType(Token.SKIP); }
    ;

TAG
  : AT (LCLETTER)+
  ;

AT
    :   '@'
    ;

JAVADOC_OPEN
    :   "/**" (NEWLINE! { skipLeadingSpaceAndAsterix(true); })?
    ;

JAVADOC_CLOSE
    :   "*/"
    ;

RCURLY
    :   '}'
    ;

LCURLY
    :   '{'
    ;

PRE
    :   "<pre" (WS ATTR)? '>'
        (
            options {
                generateAmbigWarnings = false;
                greedy = false;
            }
        :   "\r\n" {newline(false);} // Evil DOS
        |   '\r'   {newline(false);} // Macintosh
        |   '\n'   {newline(false);} // Unix (the right way)
        |   .
        )*

        "</pre>"
    ;

/* MISC STUFF */
PCDATA
    :   (
            /* See comment in WS.  Language for combining any flavor
             * newline is ambiguous.  Shutting off the warning.
             */
            options {
                generateAmbigWarnings = false;
            }
        :

        '\r''\n'
                  {
                     // we remove the newline chars because we will calculate
                     // newlines at printing time
                     replaceNewline(2);

                     if (_fileFormat == FileFormat.UNKNOWN)
                        _fileFormat = FileFormat.DOS;
                  }
        |   '\r'
                  {  // we remove the newline chars because we will calculate
                     // newlines at printing time
                     replaceNewline(1);

                     if (_fileFormat == FileFormat.UNKNOWN)
                        _fileFormat = FileFormat.MAC;
                  }
        |   '\n'
                  { // we remove the newline chars because we will calculate
                    // newlines at printing time
                    replaceNewline(1);

                    if (_fileFormat == FileFormat.UNKNOWN)
                        _fileFormat = FileFormat.UNIX;
                  }
        | '\t'    {  replaceTab();
                  }
        | { LA(2) != '*' || (LA(2) == '*' && LA(3) != '*') }? '/' // allow slash
        | ~('*'|'<'|'{'|'@'|'}'|'\n'|'\r'|'/')
        )+

        {
            String t = $getText;

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
    ;

// multiple-line comments
protected
COMMENT_DATA
    :   (       /* '\r' '\n' can be matched in one alternative or by matching
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
                        {!(LA(2)=='-' && LA(3)=='>')}? '-' // allow '-' if not "-->"
                |       "\r\n"                  {newline();}
                |       '\r'                    {newline();}
                |       '\n'                    {newline();}
                |       ~('-'|'\n'|'\r')
                )*
    ;


COMMENT
    :   "<!--" c:COMMENT_DATA "-->" (WS)?
    ;

/*
 * PROTECTED LEXER RULES
 */
protected
WS
    :   (
            /*  '\r' '\n' can be matched in one alternative or by matching
                '\r' in one iteration and '\n' in another.  I am trying to
                handle any flavor of newline that comes in, but the language
                that allows both "\r\n" and "\r" and "\n" to all be valid
                newline is ambiguous.  Consequently, the resulting grammar
                must be ambiguous.  I'm shutting this warning off.
             */
            options {
                generateAmbigWarnings=false;
            }
        :   ' '
        |   '\t'   { replaceTab(); } // replace tab with space
        |   "\r\n"! { newline();} // Evil DOS
        |   '\r'!   { newline();} // Macintosh
        |   '\n'!   { newline();} // Unix (the right way)
        )+
    ;

protected
NEWLINE
    :   (
            /*  '\r' '\n' can be matched in one alternative or by matching
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
            "\r\n"! {newline();} // Evil DOS
        |   '\r'!   {newline();} // Macintosh
        |   '\n'!   {newline();} // Unix (the right way)
        )
        { $setType(Token.SKIP); }
    ;

protected
ATTR
    :   // TODO: -INT% is in fact not allowed
        WORD (WS)? ('=' (WS)? (('-')? INT ('%')? | STRING | HEXNUM | WORD) (WS)?)?
    ;

// don't need uppercase for case-insen.
// the '.' is for words like "image.gif"
protected
WORD
    :   (   LCLETTER
        |   '.'
        )

        (
            /*  In reality, a WORD must be followed by whitespace, '=', or
                what can follow an ATTR such as '>'.  In writing this grammar,
                however, we just list all the possibilities as optional
                elements.  This is loose, allowing the case where nothing is
                matched after a WORD and then the (ATTR)* loop means the
                grammar would allow "widthheight" as WORD WORD or WORD, hence,
                an ambiguity.  Naturally, ANTLR will consume the input as soon
                as possible, combing "widthheight" into one WORD.

                I am shutting off the ambiguity here because ANTLR does the
                right thing.  The exit path is ambiguous with ever
                alternative.  The only solution would be to write an unnatural
                grammar (lots of extra productions) that laid out the
                possibilities explicitly, preventing the bogus WORD followed
                immediately by WORD without whitespace etc...
             */
            options {
                generateAmbigWarnings=false;
            }
        :   LCLETTER
        |   DIGIT
        |   '.'
        |   ':'
        |   '/'
        |   '@'
        )+
    ;

protected
STRING
    :   '"' (~'"')* '"'
    |   '\'' (~'\'')* '\''
    ;

protected
SPECIAL
    :   '<' | '~'
    ;

protected
HEXNUM
    :   '#' HEXINT
    ;

protected
INT :   (DIGIT)+
    ;

protected
HEXINT
    :   (
            /*  Technically, HEXINT cannot be followed by a..f, but due to our
                loose grammar, the whitespace that normally would follow this
                rule is optional.  ANTLR reports that #4FACE could parse as
                HEXINT "#4" followed by WORD "FACE", which is clearly bogus.
                ANTLR does the right thing by consuming a much input as
                possible here.  I shut the warning off.
             */
             options {
                generateAmbigWarnings=false;
            }
        :   HEXDIGIT
        )+
    ;

protected
DIGIT
    :   '0'..'9'
    ;

protected
HEXDIGIT
    :   '0'..'9'
    |   'a'..'f'
    ;

protected
LCLETTER
    :   'a'..'z' | '\u00DF' .. '\u00FF'
    ;   