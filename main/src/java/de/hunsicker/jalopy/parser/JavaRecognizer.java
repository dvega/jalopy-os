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
package de.hunsicker.jalopy.parser;

import de.hunsicker.antlr.RecognitionException;
import de.hunsicker.antlr.TokenBuffer;
import de.hunsicker.antlr.TokenStreamException;
import de.hunsicker.antlr.TokenStreamHiddenTokenFilter;
import de.hunsicker.antlr.TokenStreamRecognitionException;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Loggers;
import de.hunsicker.jalopy.storage.Convention;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Level;


/**
 * The Java-specific recognizer.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class JavaRecognizer
    extends Recognizer
{
    //~ Static variables/initializers ·········································

    /** Delimeter for encoded tag string. */
    private static final String DELIMETER = "|";

    /** Indicates JDK version 1.3. */
    public static final int JDK_1_3 = JavaLexer.JDK_1_3;

    /** Indicates JDK version 1.4. */
    public static final int JDK_1_4 = JavaLexer.JDK_1_4;

    //~ Instance variables ····················································

    /** The code convention. */
    private Convention _settings;

    /** Resolves wildcard imports. */
    private Transformation _importTrans;

    /**
     * Checks whether debug logging calls are enclosing with a boolean
     * expression .
     */
    private Transformation _loggingTransformation;

    /** Inserts a serial version UID for serializable classes. */
    private Transformation _serialTrans;

    /** Sorts the AST tree. */
    private Transformation _sortTrans;

    /** Were the transformations applied to the generated AST? */
    private boolean _transformed;

    //~ Constructors ··························································

    /**
     * Creates a new JavaRecognizer object.
     */
    public JavaRecognizer()
    {
        _settings = Convention.getInstance();

        JavaLexer l = new JavaLexer();
        this.lexer = l;

        JavaParser p = (JavaParser)l.getParser();
        this.parser = p;
        _importTrans = new ImportTransformation(p.getQualifiedIdents(),
                                                p.getUnqualifiedIdents());
        _sortTrans = new SortTransformation();
        _serialTrans = new SerializableTransformation();
        _loggingTransformation = new LoggerTransformation();
    }

    //~ Methods ·······························································

    /**
     * Returns the AST tree. Note that every call to this method triggers the
     * AST transformations, which could be quite expensive. So make sure to
     * avoid unnecessary calls.
     *
     * <p>
     * As we don't use checked exceptions to indicate runtime failures, one
     * may check successful execution of the transformations prior to perform
     * further processing:
     * <pre style="background:lightgrey">
     * if (myJalopyInstance.getState() == Jalopy.State.ERROR)
     * {
     *     // transformation failed, errors were already issued; perform
     *     // any custom error handling code you need
     * }
     * else
     * {
     *     // perform further logic
     * }
     * </pre>
     * </p>
     *
     * @return resulting AST tree.
     *
     * @throws IllegalStateException if the parser is still running or wasn't
     *         started yet.
     *
     * @see de.hunsicker.jalopy.Jalopy#getState
     */
    public AST getAST()
    {
        if (!this.finished)
        {
            throw new IllegalStateException("parser not started or still running");
        }

        if (!_transformed)
        {
            transform();
            _transformed = true;
        }

        return super.getAST();
    }


    /**
     * Returns the package name of the parsed source file.
     *
     * @return the package name of the parsed source file. Returns the empty
     *         String if the source file contains no package information.
     *
     * @throws IllegalStateException if the parser is still running or wasn't
     *         started yet.
     */
    public String getPackageName()
    {
        if (!this.finished)
        {
            throw new IllegalStateException("parser not started or still running");
        }

        return ((JavaParser)this.parser).getPackageName();
    }


    /**
     * {@inheritDoc}
     */
    public void parse(Reader in,
                      String filename)
    {
        if (this.running)
        {
            throw new IllegalStateException("parser currently running");
        }

        this.finished = false;
        this.running = true;
        _transformed = false;

        // update the code convention prior to parsing
        JavaParser parser = (JavaParser)this.parser;
        parser.stripQualification = _settings.getBoolean(Keys.STRIP_QUALIFICATION,
                                                      Defaults.STRIP_QUALIFICATION);

        JavaLexer lexer = (JavaLexer)this.lexer;
        lexer.setTabSize(_settings.getInt(Keys.INDENT_SIZE_TABS,
                                       Defaults.INDENT_SIZE_TABS));
        lexer.sourceVersion = _settings.getInt(Keys.SOURCE_VERSION,
                                            Defaults.SOURCE_VERSION);
        lexer.parseJavadocComments = _settings.getBoolean(Keys.COMMENT_JAVADOC_PARSE,
                                                       Defaults.COMMENT_JAVADOC_PARSE);
        lexer.removeJavadocComments = _settings.getBoolean(Keys.COMMENT_JAVADOC_REMOVE,
                                                        Defaults.COMMENT_JAVADOC_REMOVE);
        lexer.removeSLComments = _settings.getBoolean(Keys.COMMENT_REMOVE_SINGLE_LINE,
                                                   Defaults.COMMENT_REMOVE_SINGLE_LINE);
        lexer.removeMLComments = _settings.getBoolean(Keys.COMMENT_REMOVE_MULTI_LINE,
                                                   Defaults.COMMENT_REMOVE_MULTI_LINE);
        lexer.formatMLComments = _settings.getBoolean(Keys.COMMENT_FORMAT_MULTI_LINE,
                                                   Defaults.COMMENT_FORMAT_MULTI_LINE);

        JavadocParser javadocParser = lexer.getJavadocParser();
        javadocParser.setCustomStandardTags(decodeTags(_settings.get(
                                                                  Keys.COMMENT_JAVADOC_TAGS_STANDARD,
                                                                  Defaults.COMMENT_JAVADOC_TAGS_STANDARD)));
        javadocParser.setCustomInlineTags(decodeTags(_settings.get(
                                                                Keys.COMMENT_JAVADOC_TAGS_INLINE,
                                                                Defaults.COMMENT_JAVADOC_TAGS_INLINE)));

        this.lexer.setInputBuffer(in);

        TokenStreamHiddenTokenFilter filter = new TokenStreamHiddenTokenFilter(this.lexer);
        filter.discard(JavaTokenTypes.WS);
        filter.discard(JavaTokenTypes.SEPARATOR_COMMENT);
        filter.hide(JavaTokenTypes.JAVADOC_COMMENT);
        filter.hide(JavaTokenTypes.ML_COMMENT);
        filter.hide(JavaTokenTypes.SPECIAL_COMMENT);
        filter.hide(JavaTokenTypes.SL_COMMENT);

        this.lexer.setFilename(filename);
        this.parser.setFilename(filename);
        this.parser.setTokenBuffer(new TokenBuffer(filter));

        try
        {
            this.parser.parse();
        }

        // the parser/lexer should never throw any checked exception as we
        // intercept them and print logging messages prior to attempt
        // further parsing; so simply wrap all checked exceptions for the
        // case one changes the error handling in the grammar...
        catch (RecognitionException ex)
        {
            throw new ParseException(ex);
        }
        catch (TokenStreamRecognitionException ex)
        {
            throw new ParseException(ex);
        }
        catch (TokenStreamException ex)
        {
            throw new ParseException(ex);
        }
        finally
        {
            this.finished = true;
            this.running = false;
        }
    }


    /**
     * Decodes the given encoded tags string.
     *
     * @param tags encoded tags string.
     *
     * @return collection of the tags.
     *
     * @since 1.0b7
     */
    private Collection decodeTags(String tags)
    {
        List result = new ArrayList();

        for (StringTokenizer i = new StringTokenizer(tags, DELIMETER);
             i.hasMoreElements();)
        {
            result.add(i.nextToken());
        }

        return result;
    }


    /**
     * Applies the registered transformations to the AST.
     */
    private void transform()
    {
        AST tree = this.parser.getAST();

        if (tree != null)
        {
            try
            {
                _importTrans.apply(tree);

                if (_settings.getBoolean(Keys.INSERT_SERIAL_UID,
                                      Defaults.INSERT_SERIAL_UID))
                {
                    _serialTrans.apply(tree);
                }

                if (_settings.getBoolean(Keys.SORT, Defaults.SORT))
                {
                    _sortTrans.apply(tree);
                }

                if (_settings.getBoolean(Keys.INSERT_LOGGING_CONDITIONAL,
                                      Defaults.INSERT_LOGGING_CONDITIONAL))
                {
                    _loggingTransformation.apply(tree);
                }
            }
            catch (TransformationException ex)
            {
                Object[] args ={ this.parser.getFilename() };
                Loggers.IO.l7dlog(Level.ERROR, "TRANS_ERROR", args, ex);
            }
        }
    }
}
