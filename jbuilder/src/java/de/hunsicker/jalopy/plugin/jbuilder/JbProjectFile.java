/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import java.io.File;
import java.util.Arrays;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.TextFileNode;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;


/**
 * The JBuilder project file implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbProjectFile
    implements ProjectFile
{
    //~ Instance variables ---------------------------------------------------------------

    /** The underlying file node. */
    TextFileNode node;

    /** The containing project. */
    private Project _project;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbProjectFile object.
     *
     * @param project the enclosing project.
     * @param node the file node which represents the project file.
     *
     * @throws NullPointerException if <code>project == null</code> or <code>node ==
     *         null</code>.
     */
    public JbProjectFile(
        Project      project,
        TextFileNode node)
    {
        if (project == null)
        {
            throw new NullPointerException();
        }

        if (node == null)
        {
            throw new NullPointerException();
        }

        _project = project;
        this.node = node;
    }

    //~ Methods --------------------------------------------------------------------------

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
