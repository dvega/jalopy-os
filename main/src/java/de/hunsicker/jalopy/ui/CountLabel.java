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
package de.hunsicker.jalopy.ui;

import javax.swing.JLabel;


/**
 * A label which displays its label text along with an integer count.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class CountLabel
    extends JLabel
{
    //~ Instance variables ····················································

    /** The current count. */
    private int _count;

    //~ Constructors ··························································

    /**
     * Creates a new CountLabel object with the text 'Counter' and an initial
     * count of zero.
     */
    public CountLabel()
    {
        this("Count", 0);
    }


    /**
     * Creates a new CountLabel object with an initial count of zero.
     *
     * @param text the label text.
     */
    public CountLabel(String text)
    {
        this(text, 0);
    }


    /**
     * Creates a new CountLabel object.
     *
     * @param text the label text.
     * @param initialCount the initial value of the count.
     */
    public CountLabel(String text,
                      int    initialCount)
    {
        super(text + ':' + initialCount);
        _count = initialCount;
    }

    //~ Methods ·······························································

    /**
     * Sets the counter to the given value.
     *
     * @param count the count to display.
     */
    public void setCount(int count)
    {
        _count = count;

        String text = getText();
        setText(text.substring(0, text.indexOf(':')).intern() + ':' + count);
    }


    /**
     * Returns the current count.
     *
     * @return the current count.
     */
    public int getCount()
    {
        return _count;
    }


    /**
     * Increases the counter.
     */
    public void increase()
    {
        setCount(++_count);
    }


    /**
     * Resets the counter.
     */
    public void reset()
    {
        _count = 0;
        setCount(0);
    }
}
