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

/**
 * Common interface for objects that monitor the progress of an operation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public interface ProgressMonitor
{
    //~ Methods ·······························································

    /**
     * Sets the canceled state to the given value.
     *
     * @param state <code>true</code> to request a cancelation of the
     *        monitored operation.
     */
    public void setCanceled(boolean state);


    /**
     * Indicates whether the user canceled the monitored operation.
     *
     * @return <code>true</code> if the user canceled the monitored operation.
     */
    public boolean isCanceled();


    /**
     * Sets amount of work units done to <em>units</em>.
     *
     * @param units the amount of work units done.
     */
    public void setProgress(int units);


    /**
     * Returns the amount of work units done.
     *
     * @return the amount of work units done.
     */
    public int getProgress();


    /**
     * Sets the description to be displayed.
     *
     * @param text description to be displayed.
     */
    public void setText(String text);


    /**
     * Begins the monitoring of an operation.
     *
     * @param text description to be displayed.
     * @param units amount of work units to be done.
     */
    public void begin(String text,
                      int    units);


    /**
     * Notifies that the worker is done. Indicates that either the operation
     * is completed or the user canceled it.
     *
     * @see #setCanceled
     */
    public void done();
}
