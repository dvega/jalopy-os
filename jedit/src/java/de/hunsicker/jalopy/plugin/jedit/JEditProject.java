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

import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import java.util.Collection;
import java.util.Collections;


/**
 * The JEdit project implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JEditProject
    implements Project
{
    //~ Constructors ··························································

    /**
     * Creates a new JEditProject object.
     */
    public JEditProject()
    {
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        View view = jEdit.getActiveView();

        if (view != null)
        {
            Buffer buffer = view.getBuffer();

            return new JEditProjectFile(this, buffer);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getAllFiles()
    {
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getOpenedFiles()
    {
        /*Buffer[] buffers = jEdit.getBuffers();
        List result = new ArrayList(buffers.length);

        for (int i = 0; i < buffers.length; i++)
        {
            result.add(new JEditProjectFile(this, buffers[i]));
        }

        return result;*/
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getSelectedFiles()
    {
        return Collections.EMPTY_LIST;
    }
}
