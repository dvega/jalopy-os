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
/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is SATC. The Initial Developer of the Original Code is
 * Bogdan Mitu.
 *
 * Copyright(C) 2001-2002 Bogdan Mitu.
 */
package de.hunsicker.jalopy.ui.syntax;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Segment;


/**
 * Class to provide InputStream functionality from a portion of a Swing
 * Document.
 */
final class SyntaxStream
    extends InputStream
{
    //~ Instance variables ····················································

    private Document doc;
    private Segment segment;
    private int index; // index into array of the segment
    private int p0; // start position
    private int p1; // end position
    private int pos; // pos in document

    //~ Constructors ··························································

    /**
     * Creates a new document stream from a Swing document.
     *
     * @param doc DOCUMENT ME!
     */
    public SyntaxStream(Document doc)
    {
        this();
        setDocument(doc);
    }


    /**
     * Creates a new document stream with no document attached.
     */
    public SyntaxStream()
    {
        this.segment = new Segment();
    }

    //~ Methods ·······························································

    /**
     * Attaches this input stream to a document.
     *
     * @param doc DOCUMENT ME!
     */
    public void setDocument(Object doc)
    {
        this.doc = (Document)doc;
        setRange(0, this.doc.getLength());
    }


    /**
     * Restrict the stream to a range of the document.
     *
     * @param p0 DOCUMENT ME!
     * @param p1 DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public void setRange(int p0,
                         int p1)
    {
        this.p0 = p0;
        this.p1 = Math.min(doc.getLength(), p1);

        pos = p0;

        try
        {
            loadSegment();
        }
        catch (IOException ioe)
        {
            throw new Error("unexpected: " + ioe);
        }
    }


    /**
     * Reads the next byte of data from this input stream. The value  byte is
     * returned as an <code>int</code> in the range  <code>0</code> to
     * <code>255</code>. If no byte is available  because the end of the
     * stream has been reached, the value  <code>-1</code> is returned. This
     * method blocks until input data  is available, the end of the stream is
     * detected, or an exception  is thrown.
     * 
     * <p>
     * A subclass must provide an implementation of this method.
     * </p>
     *
     * @return the next byte of data, or <code>-1</code> if the end of the
     *         stream is reached.
     *
     * @exception IOException if an I/O error occurs.
     */
    public int read()
        throws IOException
    {
        if (index >= (segment.offset + segment.count))
        {
            if (pos >= p1)
            {
                // no more data
                return -1;
            }

            loadSegment();
        }

        pos++;

        return segment.array[index++];
    }


    /**
     * Reset the document stream.  The stream is positioned at the begining of
     * the document and the range of the stream is set to cover the whole
     * document.
     */
    public void reset()
    {
        if (doc != null)
        {
            setRange(0, doc.getLength());
        }
    }


    private void loadSegment()
        throws IOException
    {
        try
        {
            //int n = Math.min(1024, p1 - pos);
            doc.getText(pos, p1 - pos, segment);

//          pos += n;
            index = segment.offset;
        }
        catch (BadLocationException e)
        {
            throw new IOException("Bad location");
        }
    }
}
