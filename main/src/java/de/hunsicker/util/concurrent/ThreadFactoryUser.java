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
 * Base class for Executors and related classes that rely on thread factories.
 * Generally intended to be used as a mixin-style abstract class, but can
 * also be used stand-alone.
 * 
 * <p>
 * This class was taken from the util.concurrent package written by Doug Lea.
 * See <a
 * href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html">http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html</a>
 * for an introduction to this package.
 * </p>
 *
 * @author <a href="http://gee.cs.oswego.edu/dl">Doug Lea</a>
 */
public class ThreadFactoryUser
{
    //~ Instance variables ····················································

    /** DOCUMENT ME! */
    protected ThreadFactory threadFactory_ = new DefaultThreadFactory();

    //~ Methods ·······························································

    /**
     * Set the factory for creating new threads. By default, new threads are
     * created without any special priority, threadgroup, or status
     * parameters. You can use a different factory to change the kind of
     * Thread class used or its construction parameters.
     *
     * @param factory the factory to use
     *
     * @return the previous factory
     */
    public synchronized ThreadFactory setThreadFactory(ThreadFactory factory)
    {
        ThreadFactory old = threadFactory_;
        threadFactory_ = factory;

        return old;
    }


    /**
     * Get the factory for creating new threads.
     *
     * @return DOCUMENT ME!
     */
    public synchronized ThreadFactory getThreadFactory()
    {
        return threadFactory_;
    }

    //~ Inner Classes ·························································

    protected static class DefaultThreadFactory
        implements ThreadFactory
    {
        public Thread newThread(Runnable command)
        {
            return new Thread(command);
        }
    }
}
