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

import de.hunsicker.antlr.CommonHiddenStreamToken;


/**
 * An extended token. Stores information about the token's extent.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ExtendedToken
    extends CommonHiddenStreamToken
{
    //~ Instance variables ииииииииииииииииииииииииииииииииииииииииииииииииииии

    /** Ending column. */
    final int endColumn;

    /** Ending line. */
    final int endLine;

    /** The associated Javadoc comment. */
    Node comment;

    /** Token text. */
    String text;

    //~ Constructors ииииииииииииииииииииииииииииииииииииииииииииииииииииииииии

    /**
     * Creates a new ExtentedToken object.
     */
    public ExtendedToken()
    {
        this.endLine = 0;
        this.endColumn = 0;
    }


    /**
     * Creates a new ExtentedToken object.
     *
     * @param type type of the token.
     * @param text text of the token.
     */
    public ExtendedToken(int    type,
                         String text)
    {
        this();
        this.text = text;
        setType(type);
    }


    /**
     * Creates a new ExtendedToken object.
     *
     * @param type
     * @param text
     * @param startLine
     * @param startColumn
     * @param endLine
     * @param endColumn
     */
    public ExtendedToken(int    type,
                         String text,
                         int    startLine,
                         int    startColumn,
                         int    endLine,
                         int    endColumn)
    {
        this.text = text;
        this.line = startLine;
        this.col = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
        setType(type);
    }


    /**
     * Creates a new ExtendedToken object.
     *
     * @param type
     * @param startLine
     * @param startColumn
     * @param endLine
     * @param endColumn
     */
    public ExtendedToken(int type,
                         int startLine,
                         int startColumn,
                         int endLine,
                         int endColumn)
    {
        this.line = startLine;
        this.col = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
        setType(type);
    }


    /**
     * Creates a new ExtentedToken object.
     *
     * @param text the text of the token
     */
    public ExtendedToken(String text)
    {
        this();
        this.text = text;
    }

    //~ Methods иииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииии

    /**
     * Returns the starting column of the token.
     *
     * @return token starting column.
     */
    public int getColumn()
    {
        return this.col;
    }


    /**
     * DOCUMENT ME!
     *
     * @param comment DOCUMENT ME!
     */
    public void setComment(Node comment)
    {
        this.comment = comment;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getComment()
    {
        return this.comment;
    }


    /**
     * Returns the token's start column
     *
     * @return the column where the token ends.
     */
    public int getEndColumn()
    {
        return this.endColumn;
    }


    /**
     * Returns the token's end line.
     *
     * @return the line where the token ends.
     */
    public int getEndLine()
    {
        return this.endLine;
    }


    /**
     * Returns the starting line of the token.
     *
     * @return token starting line.
     */
    public int getLine()
    {
        return this.line;
    }


    /**
     * Sets the text of the token.
     *
     * @param text text of the token.
     */
    public void setText(String text)
    {
        this.text = text;
    }


    /**
     * Returns the text of the token.
     *
     * @return token text.
     */
    public String getText()
    {
        return this.text;
    }


    /**
     * Returns a string representation of the object.
     *
     * @return string representation.
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(30);
        buf.append('[');
        buf.append("\"");
        buf.append(this.text);
        buf.append("\"");
        buf.append(',');
        buf.append('<');
        buf.append(getType());
        buf.append('>');
        buf.append(' ');
        buf.append('[');
        buf.append(this.line);
        buf.append(':');
        buf.append(this.col);
        buf.append('-');
        buf.append(this.endLine);
        buf.append(':');
        buf.append(this.endColumn);
        buf.append(']');
        buf.append(']');

        return buf.toString();
    }
}
