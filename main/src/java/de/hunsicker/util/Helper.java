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

/**
 * A few asorted helper routines.
 *
 * @author $author$
 * @version $Revision$
 */
public final class Helper
{
    //~ Constructors ··························································

    private Helper()
    {
    }

    //~ Methods ·······························································

    /**
     * Loads the class with the specified name. This method first attempts  to
     * load the class with the current context classloader and only if the
     * search failed, it tries to load the class with the class loader of the
     * given object.
     *
     * @param name the name of the class.
     * @param o the object which classloader should be used.
     *
     * @return the result {@link java.lang.Class} object.
     *
     * @throws ClassNotFoundException if the class was not found.
     */
    public static Class loadClass(final String name,
                                  final Object o)
        throws ClassNotFoundException
    {
        return loadClass(name, o.getClass().getClassLoader());
    }


    /**
     * Loads the class with the specified name. This method first attempts  to
     * load the class with the current context classloader and only if the
     * search failed, it tries to load the class with the given class loader.
     *
     * @param name the name of the class.
     * @param loader the classloader to load the class.
     *
     * @return the result {@link java.lang.Class} object.
     *
     * @throws ClassNotFoundException if the class was not found.
     */
    private static Class loadClass(final String      name,
                                   final ClassLoader loader)
        throws ClassNotFoundException
    {
        ClassLoader l = Thread.currentThread().getContextClassLoader();

        if (l != null)
        {
            try
            {
                return l.loadClass(name);
            }
            catch (ClassNotFoundException ignored)
            {
                // fall back to the given classloader
            }
        }

        return loader.loadClass(name);
    }
}
