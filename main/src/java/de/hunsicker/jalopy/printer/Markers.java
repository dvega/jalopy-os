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

import java.util.LinkedList;


/**
 * Manages a set of markers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class Markers
{
    //~ Instance variables ····················································

    /** The current number of markers. */
    int count;

    /** Holds the markers. */
    private LinkedList _marks; // LinkedList of <Marker>

    /** The writer for which all markers are set. */
    private NodeWriter _writer;

    //~ Constructors ··························································

    /**
     * Creates a new Markers object.
     *
     * @param writer writer for which all markers will be set.
     */
    public Markers(NodeWriter writer)
    {
        _writer = writer;
        _marks = new LinkedList();
    }

    //~ Methods ·······························································

    /**
     * Returns the last marker. Equal to {@link #getMark(int) getMark(count()
     * - 1)}.
     *
     * @return the last marker set.
     */
    public Marker getLast()
    {
        return get(_marks.size() - 1);
    }


    /**
     * Indicates whether any markers are set.
     *
     * @return <code>true</code> if there is at least one marker.
     */
    public boolean isMarked()
    {
        return this.count > 0;
    }


    /**
     * Adds a new marker.
     *
     * @return the added marker.
     */
    public Marker add()
    {
        return add(_writer.line, _writer.column - 1);
    }


    /**
     * Adds a marker for the given position.
     *
     * @param line line of the marker
     * @param column column of the marker.
     *
     * @return the created marker.
     */
    public Marker add(int line,
                      int column)
    {
        Marker m = new Marker(line, column);
        _marks.add(m);
        this.count++;

        return m;
    }


    /**
     * Returns the number of markers set.
     *
     * @return number of markers set.
     */
    public int count()
    {
        return this.count;
    }


    /**
     * Returns the marker with the given index.
     *
     * @param index the index of the mark (&gt;=0).
     *
     * @return the marker with the given index.
     */
    public Marker get(int index)
    {
        return (Marker)_marks.get(index);
    }


    /**
     * Removes the given mark. Removes all marks that were set after the given
     * mark, too.
     *
     * @param mark mark to remove.
     */
    public void remove(Marker mark)
    {
        int index = _marks.indexOf(mark);

        // remove the given marker and all markers that were set after this
        // marker
        for (int i = index, size = _marks.size(); i < size; i++)
        {
            _marks.removeLast();
            this.count--;
        }
    }


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    public String toString()
    {
        return _marks.toString();
    }
}
