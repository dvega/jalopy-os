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
package de.hunsicker.jalopy.plugin;

import java.io.File;


/**
 * Represents a Java source file that is part of a project.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public interface ProjectFile
{
    //~ Methods ·······························································

    /**
     * Returns an editor view to modify the file. One may check if the file is
     * actually opened in the editor prior to call this method:
     * <pre style="background:lightgrey">
     * if (projectFile.isOpened())
     * {    
     *     return projectFile.getEditor();
     * }
     * else
     * {
     *     // do whatever you want ...
     * }
     * </pre>
     *
     * @return the editor to modify the contents of the file. Returns
     *         <code>null</code> if the file is currently closed.
     *
     * @see #isOpened
     */
    public Editor getEditor();


    /**
     * Returns the encoding used to read and write this file.
     *
     * @return A Java encoding name. May be <code>null</code> to indicate the
     *         platform's default encoding.
     */
    public String getEncoding();


    /**
     * Returns the underlying physical file. Note that if the application uses
     * virtual files this method should create an intermediate representation
     * but never return <code>null</code>.
     *
     * @return the physical file.
     */
    public File getFile();


    /**
     * Returns the name of the file.
     *
     * @return the file name.
     */
    public String getName();


    /**
     * Determines whether the file is currently opened. That means an editor
     * view exists.
     *
     * @return <code>true</code> if the file is currently opened, i.e. has an
     *         editor to modify its contents.
     *
     * @see #getEditor
     */
    public boolean isOpened();


    /**
     * Returns the project this file is attached to.
     *
     * @return the containing project.
     */
    public Project getProject();


    /**
     * Determines whether the file can be changed.
     *
     * @return <code>true</code> if the file can be changed by the user.
     */
    public boolean isReadOnly();
}
