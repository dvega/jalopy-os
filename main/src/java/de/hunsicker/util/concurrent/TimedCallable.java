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
package de.hunsicker.util.concurrent;

/**
 * TimedCallable runs a Callable function for a given length of time. The
 * function is run in its own thread. If the function completes in time, its
 * result is returned; otherwise the thread is interrupted and an
 * InterruptedException is thrown.
 * 
 * <p>
 * Note: TimedCallable will always return within the given time limit (modulo
 * timer inaccuracies), but whether or not the worker thread stops in a
 * timely fashion depends on the interrupt handling in the Callable
 * function's implementation.
 * </p>
 * 
 * <p>
 * This class was taken from the util.concurrent package written by Doug Lea.
 * See <a
 * href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html">http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html</a>
 * for an introduction to this package.
 * </p>
 *
 * @author Joseph Bowbeer
 * @author <a href="http://gee.cs.oswego.edu/dl">Doug Lea</a>
 * @version 1.0
 */
public class TimedCallable
    extends ThreadFactoryUser
    implements Callable
{
    //~ Instance variables ····················································

    private final Callable _function;
    private final long _millis;

    //~ Constructors ··························································

    /**
     * Creates a new TimedCallable object.
     *
     * @param function DOCUMENT ME!
     * @param millis DOCUMENT ME!
     */
    public TimedCallable(Callable function,
                         long     millis)
    {
        _function = function;
        _millis = millis;
    }

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object call()
        throws Exception
    {
        FutureResult result = new FutureResult();
        Thread thread = getThreadFactory().newThread(result.setter(_function));
        thread.start();

        try
        {
            return result.timedGet(_millis);
        }
        catch (InterruptedException ex)
        {
            /* Stop thread if we were interrupted or timed-out
               while waiting for the result. */
            thread.interrupt();
            throw ex;
        }
    }
}
