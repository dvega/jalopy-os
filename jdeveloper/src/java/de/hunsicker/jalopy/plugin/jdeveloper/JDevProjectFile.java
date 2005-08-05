/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.io.File;
import java.util.List;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import oracle.ide.editor.EditorManager;
import oracle.ide.model.TextNode;


/**
 * The JDeveloper project file implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JDevProjectFile
    implements ProjectFile
{
    //~ Instance variables ---------------------------------------------------------------

    /** The used editor. */
    private Editor _editor;

    /** The containing project. */
    private Project _project;

    /** The underlying file node. */
    private TextNode _node;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JDevProjectFile object.
     *
     * @param project the enclosing project.
     * @param node the file node which represents the project file.
     *
     * @throws NullPointerException if <code>project == null</code> or <code>node ==
     *         null</code>.
     */
    public JDevProjectFile(
        Project  project,
        TextNode node)
    {
        if ((project == null) || (node == null))
        {
            throw new NullPointerException();
        }

        _project = project;
        _node = node;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Editor getEditor()
    {
        if (isOpened())
        {
            return new JDevEditor(this, _node);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        /**
         * @todo is it possible to get the encoding?
         */
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public File getFile()
    {
        /**
         * @todo JDeveloper is able to handle non-local files
         */
        File file = new File(_node.getURL().getFile());

        if (_node.isReadOnly())
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
        return _node.getLongLabel();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOpened()
    {
        // broken in 9.0.3!
        // return _node.isOpen();
        boolean result = false;

        List editors = EditorManager.getEditorManager().getAllEditors();

        for (int i = 0, size = editors.size(); i < size; i++)
        {
            oracle.ide.editor.Editor editor = (oracle.ide.editor.Editor) editors.get(i);

            if (_node.equals(editor.getContext().getDocument()))
            {
                result = true;

                break;
            }
        }

        return result;
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
        return _node.isReadOnly();
    }


    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return _node.toString();
    }
}
