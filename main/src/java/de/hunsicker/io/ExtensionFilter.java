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
package de.hunsicker.io;

import java.io.File;
import java.io.FilenameFilter;


/**
 * ExtensionFilter implements a FilenameFilter driven by a file extension.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class ExtensionFilter
    implements FilenameFilter
{
    //~ Instance variables ····················································

    /** The extension we use. */
    private String _ext;

    //~ Constructors ··························································

    /**
     * Creates a new Extension filter object.
     *
     * @param ext the extension string we filter.
     *
     * @throws IllegalArgumentException if <code>ext == null</code> or
     *         <code>ext.length() == 0</code>
     */
    public ExtensionFilter(String ext)
    {
        if ((ext == null) || (ext.length() == 0))
        {
            throw new IllegalArgumentException("invalid extension -- " + ext);
        }

        _ext = ext.trim();

        if (!_ext.startsWith("."))
        {
            _ext = '.' + _ext;
        }
    }

    //~ Methods ·······························································

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir the directory in which the file was found.
     * @param name the name of the file.
     *
     * @return <code>true</code> if the given file was accepted.
     */
    public boolean accept(File   dir,
                          String name)
    {
        File file = new File(dir, name);

        if (file.isDirectory() || name.endsWith(_ext))
        {
            return true;
        }

        return false;
    }
}
