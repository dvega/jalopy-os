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
package de.hunsicker.util;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;


/**
 * Resembles the JDK 1.4 exception chaining facility.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ChainingRuntimeException
    extends RuntimeException
{
    //~ Instance variables ····················································

    /** Causing throwable. */
    protected Throwable cause;

    //~ Constructors ··························································

    /**
     * Creates a new ChainingRuntimeException object.
     */
    public ChainingRuntimeException()
    {
    }


    /**
     * Creates a new ChainingRuntimeException object.
     *
     * @param message error message.
     */
    public ChainingRuntimeException(String message)
    {
        super(message);
    }


    /**
     * Creates a new ChainingRuntimeException object.
     *
     * @param message error message.
     * @param cause throwable which caused the error.
     */
    public ChainingRuntimeException(String    message,
                                    Throwable cause)
    {
        super(message);
        this.cause = cause;
    }


    /**
     * Creates a new ChainingRuntimeException object.
     *
     * @param cause throwable which caused the error.
     */
    public ChainingRuntimeException(Throwable cause)
    {
        super((cause == null) ? ""
                              : cause.getLocalizedMessage());
        this.cause = cause;
    }

    //~ Methods ·······························································

    /**
     * Returns the causing throwable.
     *
     * @return throwable which caused the exception.
     */
    public Throwable getCause()
    {
        return ((this.cause == null) ? this
                                     : this.cause);
    }


    /**
     * Prints this <code>Throwable</code> (the cause if available) and its
     * backtrace to the specified print writer.
     *
     * @param writer writer to use for output.
     */
    public void printStackTrace(PrintWriter writer)
    {
        if (this.cause != null)
        {
            String msg = super.getLocalizedMessage();
            writer.println((msg != null) ? msg
                                         : "Exception occured");
            writer.print("Nested Exception is: ");
            this.cause.printStackTrace(writer);
        }
        else
        {
            super.printStackTrace(writer);
        }
    }


    /**
     * Prints this <code>Throwable</code> (the cause if available) and its
     * backtrace to the specified print writer.
     *
     * @param s stream to use for output.
     */
    public void printStackTrace(PrintStream s)
    {
        printStackTrace(new PrintWriter(new OutputStreamWriter(s)));
    }


    /**
     * Prints this <code>Throwable</code> (the cause if available) and its
     * backtrace to the standard error stream.
     */
    public void printStackTrace()
    {
        printStackTrace(System.err);
    }
}
