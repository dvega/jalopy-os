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
package de.hunsicker.jalopy.printer;

import java.io.IOException;


/**
 * NodeWriter can be used to &quot;test&quot; the output result for nodes. The
 * class' sole purpose is to determine the length of an AST tree (or portions
 * thereof) if printed.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class TestNodeWriter
    extends NodeWriter
{
    //~ Instance variables ····················································

    /** The length it would take to print the tree/node. */
    int length;

    //~ Constructors ··························································

    /**
     * Creates a new TestNodeWriter object.
     */
    public TestNodeWriter()
    {
        super();
        this.mode = MODE_TEST;
    }

    //~ Methods ·······························································

    /**
     * Returns the length of the testet AST.
     *
     * @return the length it would take to output.
     */
    public int getLength()
    {
        return this.length;
    }


    /**
     * {@inheritDoc}
     */
    public void close()
        throws IOException
    {
        super.close();
        reset();
    }


    /**
     * {@inheritDoc}
     */
    public void flush()
        throws IOException
    {
        super.flush();
        reset();
    }


    /**
     * {@inheritDoc}
     */
    public void print(String string,
                      int    type)
        throws IOException
    {
        if (this.newline)
        {
            int l = this.indentLevel * this.indentSize;
            this.column += l;
            this.length += l;
            this.newline = false;
        }

        int l = string.length();
        this.length += l;
        this.column += l;
        this.last = type;
    }


    /**
     * {@inheritDoc}
     */
    public void printNewline()
        throws IOException
    {
        this.newline = true;
        this.column = 1;
        this.line++;
    }


    /**
     * Resets the stream. Call this method prior reusing the stream.
     */
    public void reset()
    {
        this.length = 0;
        this.line = 1;
        this.column = 1;
    }
}
