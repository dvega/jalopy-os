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
 * Holds some state values during the printing process (mostly used to
 * implement line wrapping and aligning).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class PrinterState
{
    //~ Instance variables ····················································

    LinkedList parenScope;

    /** An array we use to hold the arguments for String formatting. */
    final Object[] args = new Object[6];

    /** Our markers. */
    Markers markers;

    /** Indicates that the printer currently prints an anonymous inner class. */
    boolean anonymousInnerClass;

    /**
     * Indicates whether we should print the left curly brace of method/ctor
     * declations in C-style (nno matter what the current brace options say).
     */
    boolean extraWrap;

    /** Indicates that the printer currently prints an inner class. */
    boolean innerClass;

    /**
     * Indicates whether operators should be wrapped as needed, or wrapping
     * should be forced after each operator.
     */
    boolean wrap;
    int arrayBrackets;

    /**
     * Holds the column offset of the rightmost assignment for assignment
     * aligning.
     */
    int assignOffset = AssignmentPrinter.OFFSET_NONE;

    /** Stores the nesting level for parameter/expression lists. */
    int paramLevel;

    /**
     * Holds the column offset of the rightmost identifier for parameter
     * aligning.
     */
    int paramOffset = ParametersPrinter.OFFSET_NONE;

    /**
     * Holds the column offset of the rightmost variable definition for
     * variable aligning.
     */
    int variableOffset = VariableDeclarationPrinter.OFFSET_NONE;

    //~ Constructors ··························································

    /**
     * Creates a new PrinterState object.
     *
     * @param writer the NodeWriter to associate.
     */
    PrinterState(NodeWriter writer)
    {
        if (writer.mode == NodeWriter.MODE_DEFAULT)
        {
            this.markers = new Markers(writer);
            this.parenScope = new LinkedList();
            this.parenScope.addFirst(new ParenthesesScope(0));
        }
    }

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     */
    public void dispose()
    {
        if (this.parenScope != null)
        {
            this.parenScope.clear();
            this.parenScope = null;
            this.markers = null;
        }
    }
}
