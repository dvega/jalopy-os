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
package de.hunsicker.jalopy.plugin.netbeans;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import java.io.File;

import javax.swing.JEditorPane;

import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;


/**
 * The NetBeans ProjectFile implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbProjectFile
    implements ProjectFile
{
    //~ Instance variables ····················································

    /** The physical file. */
    File file;
    FileObject fileObj;

    /** The node object representing this file. */
    Node node;

    /** The project this file is contained in. */
    Project project;

    //~ Constructors ··························································

    /**
     * Creates a new NbProjectFile object.
     *
     * @param project the underlying NetBeans project.
     * @param node the file node.
     */
    public NbProjectFile(Project project,
                         Node    node)
    {
        this.project = project;
        this.node = node;

        DataObject obj = (DataObject)this.node.getCookie(DataObject.class);
        this.fileObj = obj.getPrimaryFile();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public Editor getEditor()
    {
        EditorCookie cookie = (EditorCookie)this.node.getCookie(EditorCookie.class);

        /**
         * @todo maybe this check is obsolete?
         */
        if (cookie == null)
        {
            return null;
        }

        JEditorPane[] panes = cookie.getOpenedPanes();

        if (panes == null)
        {
            return null;
        }

        return new NbEditor(this, panes[0], cookie.getLineSet());
    }


    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        return (String)this.fileObj.getAttribute("Content-Encoding");
    }


    /**
     * {@inheritDoc}
     */
    public File getFile()
    {
        if (this.file == null)
        {
            this.file = FileUtil.toFile(this.fileObj);
        }

        return this.file;
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
        EditorCookie cookie = (EditorCookie)this.node.getCookie(EditorCookie.class);

        /**
         * @todo maybe this check is obsolete?
         */
        if (cookie == null)
        {
            return false;
        }

        JEditorPane[] panes = cookie.getOpenedPanes();

        return ((panes != null) && (panes.length > 0));
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
        return !getFile().canWrite();
    }


    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o instanceof ProjectFile)
        {
            return getFile().equals((File)((ProjectFile)o).getFile());
        }

        return false;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return getFile().toString();
    }
}
