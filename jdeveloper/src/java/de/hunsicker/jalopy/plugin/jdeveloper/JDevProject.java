/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import oracle.ide.Ide;
import oracle.ide.editor.Editor;
import oracle.ide.model.DirectoryFolder;
import oracle.ide.model.Element;
import oracle.ide.model.PackageFolder;
import oracle.ide.model.Workspace;
import oracle.ide.model.Workspaces;
import oracle.jdeveloper.model.BusinessComponents;
import oracle.jdeveloper.model.EnterpriseJavaBeans;
import oracle.jdeveloper.model.JProject;
import oracle.jdeveloper.model.JavaSourceNode;
import oracle.jdeveloper.model.JavaSources;


/**
 * The JDeveloper project implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JDevProject
    implements Project
{
    //~ Instance variables ---------------------------------------------------------------

    /** The currently selected element. Set by JDevPlugin.java */
    Element[] selection;

    /** Used to build ProjectFile lists. */
    private Set _files;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JDevProject object.
     */
    public JDevProject()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        Editor editor = Ide.getEditorManager().getCurrentEditor();

        if (editor != null)
        {
            Element element = editor.getContext().getElement();

            return new JDevProjectFile(this, (JavaSourceNode) element);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getAllFiles()
    {
        // not used by the Extension
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getOpenedFiles()
    {
        // not used by the Extension
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getSelectedFiles()
    {
        if (this.selection != null)
        {
            try
            {
                List files = new ArrayList();

                for (int i = 0; i < this.selection.length; i++)
                {
                    if (this.selection[i] instanceof JavaSourceNode)
                    {
                        files.add(
                            new JDevProjectFile(this, (JavaSourceNode) this.selection[i]));
                    }
                    else if (isJavaFolder(this.selection[i]))
                    {
                        walkTree(this.selection[i]);
                    }
                }

                if (_files != null)
                {
                    files.addAll(_files);
                    _files.clear();
                    _files = null;
                }

                return files;
            }
            finally
            {
                this.selection = null;
            }
        }

        return Collections.EMPTY_LIST;
    }


    /**
     * Determines whether the given element represents a data folder that can possibly
     * contain Java sources.
     *
     * @param node a node element.
     *
     * @return <code>true</code> if the given element represents a data folder that can
     *         contain Java sources.
     */
    private boolean isJavaFolder(Element node)
    {
        if (
            node instanceof JavaSources || node instanceof JProject
            || node instanceof BusinessComponents || node instanceof EnterpriseJavaBeans
            || node instanceof PackageFolder || node instanceof DirectoryFolder
            || node instanceof Workspace || node instanceof Workspaces)
        {
            return true;
        }

        return false;
    }


    /**
     * Adds the given node to the build file list if it represents a Java source file.
     *
     * @param node a node element.
     */
    private void addToList(Element node)
    {
        if (node instanceof JavaSourceNode)
        {
            _files.add(new JDevProjectFile(this, (JavaSourceNode) node));
        }
    }


    /**
     * Walks over the children of the given node adding all nodes that represent Java
     * sourcs files to the build file list.
     *
     * @param node a node element.
     */
    private void walkChildren(Element node)
    {
        Iterator nodes = node.getChildren();

        if (nodes == null)
        {
            return;
        }

        while (nodes.hasNext())
        {
            walkNode((Element) nodes.next());
        }
    }


    /**
     * Walks over the given node adding all nodes that represent Java sourcs files to the
     * build file list.
     *
     * @param node a node element.
     */
    private void walkNode(Element node)
    {
        addToList(node);
        walkChildren(node);
    }


    /**
     * Walks over the whole tree adding all nodes that represent Java sourcs files to the
     * build file list.
     *
     * @param node a node element.
     */
    private void walkTree(Element node)
    {
        if (_files == null)
        {
            _files = new HashSet();
        }

        walkNode(node);
    }
}
