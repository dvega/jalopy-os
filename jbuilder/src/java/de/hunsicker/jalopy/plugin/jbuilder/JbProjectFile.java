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
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.TextFileNode;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import java.io.File;
import java.util.Arrays;


/**
 * The JBuilder project file implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbProjectFile
    implements ProjectFile
{
    //~ Instance variables ····················································

    /** The underlying file node. */
    TextFileNode node;

    /** The containing project. */
    private Project _project;

    //~ Constructors ··························································

    /**
     * Creates a new JbProjectFile object.
     *
     * @param project the enclosing project.
     * @param node the file node which represents the project file.
     *
     * @throws NullPointerException if <code>project == null</code> or
     *         <code>node == null</code>.
     */
    public JbProjectFile(Project      project,
                         TextFileNode node)
    {
        if (project == null)
        {
            throw new NullPointerException("project == null");
        }

        if (node == null)
        {
            throw new NullPointerException("node == null");
        }

        _project = project;
        this.node = node;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public Editor getEditor()
    {
        return new JbEditor(this, this.node);
    }


    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        return this.node.getEncoding();
    }


    /**
     * {@inheritDoc}
     */
    public File getFile()
    {
        File file = this.node.getUrl().getFileObject();

        if (this.node.isReadOnly())
        {
            file.setReadOnly();
        }

        return file;
    }


    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        return this.node.getDisplayName();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOpened()
    {
        Node[] nodes = Browser.getActiveBrowser().getOpenNodes();
        Arrays.sort(nodes);

        return (Arrays.binarySearch(nodes, this.node) > -1);
    }


    /**
     * {@inheritDoc}
     */
    public Project getProject()
    {
        return _project;
    }


    /**
     * {@inheritDoc}
     */
    public boolean isReadOnly()
    {
        return this.node.isReadOnly();
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return this.node.toString();
    }
}
