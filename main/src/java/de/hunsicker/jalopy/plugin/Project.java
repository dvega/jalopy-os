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

import java.util.Collection;


/**
 * Provides access to the Java source files that make up a user's project.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public interface Project
{
    //~ Methods ·······························································

    /**
     * Returns the currently active Java source file. <em>Active</em> means
     * that the file has an {@link Editor} opened and this editor has the
     * focus.
     *
     * @return currently active file. Returns <code>null</code> if there is no
     *         active file or if the active file is no Java source file.
     */
    public ProjectFile getActiveFile();


    /**
     * Returns all Java source files that make up a project.
     *
     * @return the files of the project. Returns an empty list if no files
     *         exist. This method never returns <code>null</code>.
     */
    public Collection getAllFiles();


    /**
     * Returns the Java source files that are currently opened (i.e.
     * <code>{@link ProjectFile#getEditor()} != null</code>).
     *
     * @return currently opened files. Returns an empty list if no files are
     *         opened. This method never returns <code>null</code>.
     */
    public Collection getOpenedFiles();


    /**
     * Returns the Java source files that are currently selected.
     *
     * @return currently selected files. Returns an empty list if no files are
     *         selected. This method never returns <code>null</code>.
     */
    public Collection getSelectedFiles();
}
