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
import de.hunsicker.antlr.TokenStreamRecognitionException;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.io.FileFormat;
import de.hunsicker.util.ChainingRuntimeException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


/**
 * Recognizer acts as a helper class to bundle both an ANTLR parser and lexer
 * for the task of language recognition.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class Recognizer
{
    //~ Static variables/initializers ·········································

    /** Represents an unknown file. */
    public static final String UNKNOWN_FILE = "<unknown>";

    //~ Instance variables ····················································

    /** The used lexer. */
    protected Lexer lexer;

    /** The used parser. */
    protected Parser parser;

    /** Indicates that formatting finished. */
    boolean finished;

    /** Indicates that formatting currently takes place. */
    boolean running;

    //~ Constructors ··························································

    /**
     * Creates a new Recognizer object.
     *
     * @param parser the parser to use.
     * @param lexer the lexer to use.
     */
    public Recognizer(Parser parser,
                      Lexer  lexer)
    {
        this.parser = parser;
        this.lexer = lexer;
    }


    /**
     * Creates a new Recognizer object.
     */
    protected Recognizer()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns the AST tree.
     *
     * @return resulting AST tree.
     */
    public AST getAST()
    {
        return this.parser.getAST();
    }


    /**
     * Sets the current column of the lexer.
     *
     * @param column current column information.
     */
    public void setColumn(int column)
    {
        this.lexer.setColumn(column);
    }


    /**
     * Returns the current column of the lexer.
     *
     * @return current column offset.
     */
    public int getColumn()
    {
        return this.lexer.getColumn();
    }


    /**
     * Gets the file format of the parsed file as reported by the lexer
     *
     * @return The file format.
     *
     * @throws IllegalStateException if nothing has been parsed yet.
     */
    public FileFormat getFileFormat()
    {
        if (!this.finished)
        {
            throw new IllegalStateException("nothing parsed yet");
        }

        return this.lexer.getFileFormat();
    }


    /**
     * Indicates whether the recognizer is currently running.
     *
     * @return <code>true</code> if the recognizer is currently running.
     */
    public boolean isFinished()
    {
        return this.finished;
    }


    /**
     * Returns the used lexer.
     *
     * @return lexer.
     */
    public Lexer getLexer()
    {
        return this.lexer;
    }


    /**
     * Sets the current line of the lexer.
     *
     * @param line current line information.
     */
    public void setLine(int line)
    {
        this.lexer.setLine(line);
    }


    /**
     * Returns the current line of the lexer.
     *
     * @return current line number of the lexer
     */
    public int getLine()
    {
        return this.lexer.getLine();
    }


    /**
     * Returns the used parser.
     *
     * @return parser.
     */
    public Parser getParser()
    {
        return this.parser;
    }


    /**
     * Indicates whether the recognizer is currently running.
     *
     * @return <code>true</code> if the recognizer is currently running.
     */
    public boolean isRunning()
    {
        return this.running;
    }


    /**
     * Parses the given stream.
     *
     * @param in stream we read from.
     * @param filename name of the file we parse.
     *
     * @throws IllegalStateException if the parser is currently running.
     * @throws ParseException DOCUMENT ME!
     */
    public void parse(Reader in,
                      String filename)
    {
        if (this.running)
        {
            throw new IllegalStateException("parser already running");
        }

        this.finished = false;
        this.running = true;
        this.lexer.setInputBuffer(in);
        this.lexer.setFilename(filename);
        this.parser.setTokenBuffer(new TokenBuffer(this.lexer));
        this.parser.setFilename(filename);

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
     * Parses the given file.
     *
     * @param file file to parse.
     */
    public void parse(File file)
    {
        if (file.exists() && file.isFile())
        {
            BufferedReader in = null;

            try
            {
                in = new BufferedReader(new FileReader(file));
                parse(in, file.getAbsolutePath());
            }
            catch (FileNotFoundException neverOccurs)
            {
                ;
            }
            finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException ignored)
                    {
                        ;
                    }
                }
            }
        }
    }


    /**
     * Parses the given string.
     *
     * @param str to parse.
     * @param filename name of the file we parse.
     *
     * @throws IOException if an I/O error occured.
     */
    public void parse(String str,
                      String filename)
        throws IOException
    {
        BufferedReader in = new BufferedReader(new StringReader(str));
        parse(in, filename);
        in.close();
    }


    /**
     * Resets both the parser and lexer.
     *
     * @see Parser#reset
     * @see Lexer#reset
     */
    public void reset()
    {
        this.running = false;
        this.finished = false;
        this.lexer.reset();
        this.parser.reset();
    }

    //~ Inner Classes ·························································

    /**
     * Indicates an error during parsing of an input file or stream.
     */
    public static class ParseException
        extends ChainingRuntimeException
    {
        /**
         * Creates a new ParseException.
         *
         * @param cause throwable which caused the error.
         */
        public ParseException(Throwable cause)
        {
            super(cause);
        }
    }
}
