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
package de.hunsicker.jalopy.plugin.jedit;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import java.io.File;


/**
 * The JEdit project file implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JEditProjectFile
    implements ProjectFile
{
    //~ Instance variables ····················································

    Buffer buffer;
    Project project;

    //~ Constructors ··························································

    /**
     * Creates a new JEditProjectFile object.
     *
     * @param project the enclosing project.
     * @param buffer the file buffer.
     */
    public JEditProjectFile(Project project,
                            Buffer  buffer)
    {
        this.project = project;
        this.buffer = buffer;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public Editor getEditor()
    {
        if (isOpened())
        {
            View view = jEdit.getActiveView();
            Buffer buffer = view.getBuffer();

            if (JEditPlugin.isActiveBuffer(buffer))
            {
                return new JEditEditor(this, view.getTextArea());
            }
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        return (String)this.buffer.getProperty(Buffer.ENCODING);
    }


    /**
     * {@inheritDoc}
     */
    public File getFile()
    {
        return this.buffer.getFile();
    }


    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return getFile().getName();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOpened()
    {
        Buffer[] buffers = jEdit.getBuffers();

        for (int i = 0; i < buffers.length; i++)
        {
            if (this.buffer == buffers[i])
            {
                return true;
            }
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    public Project getProject()
    {
        return this.project;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isReadOnly()
    {
        return this.buffer.isReadOnly();
    }
}
