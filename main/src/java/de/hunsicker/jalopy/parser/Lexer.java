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

import de.hunsicker.antlr.TokenStream;
import de.hunsicker.io.FileFormat;

import java.io.Reader;


/**
 * Common interface for ANTLR lexers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public interface Lexer
    extends TokenStream
{
    //~ Methods ·······························································

    /**
     * Sets the start column of the position where parsing starts.
     *
     * @param column start column.
     */
    public void setColumn(int column);


    /**
     * Returns the current column.
     *
     * @return current column offset.
     */
    public int getColumn();


    /**
     * Returns the file format of the input stream.
     *
     * @return The detected file format of the input stream.
     */
    public FileFormat getFileFormat();


    /**
     * Sets the filename we parse.
     *
     * @param file filename to parse.
     */
    public void setFilename(String file);


    /**
     * Returns the name of the file.
     *
     * @return filename.
     */
    public String getFilename();


    /**
     * Sets the input source to use.
     *
     * @param in input source to use.
     */
    public void setInputBuffer(Reader in);


    /**
     * Sets the line number of the position where parsing starts.
     *
     * @param line line number.
     */
    public void setLine(int line);


    /**
     * Returns the current line.
     *
     * @return current line number.
     */
    public int getLine();


    /**
     * Returns the corresponding parser for this lexer.
     *
     * @return corresponding parser.
     */
    public Parser getParser();


    /**
     * Resets the lexer state.
     */
    public void reset();
}
