/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.borland.jbuilder.node.JavaFileNode;
import com.borland.jbuilder.node.PackageNode;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.ProjectView;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.Project;

import de.hunsicker.jalopy.plugin.ProjectFile;


/**
 * The JBuilder project implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbProject
    implements de.hunsicker.jalopy.plugin.Project
{
    //~ Instance variables ---------------------------------------------------------------

    /** Used to build ProjectFile arrays. */
    private List _projectFiles; // List of <ProjectFile>

    /** The underlying JBuilder project representation. */
    private Project _project;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbProject object.
     *
     * @param project the JBuilder representation of a project.
     */
    public JbProject(Project project)
    {
        _project = project;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        Node node = Browser.getActiveBrowser().getActiveNode();

        if (node instanceof JavaFileNode)
        {
            return new JbProjectFile(this, (JavaFileNode) node);
        }
        else
        {
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    public Collection getAllFiles()
    {
        ProjectView pv = Browser.getActiveBrowser().getProjectView();
        Project proj = pv.getActiveProject();
        walkTree(proj);

        List files = new ArrayList(_projectFiles);
        _projectFiles.clear();

        return files;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getOpenedFiles()
    {
        Node[] nodes = Browser.getActiveBrowser().getOpenNodes(_project);
        List files = new ArrayList(nodes.length);

        for (int i = 0; i < nodes.length; i++)
        {
            if (nodes[i] instanceof JavaFileNode)
            {
                files.add(new JbProjectFile(this, (JavaFileNode) nodes[i]));
            }
        }

        return files;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getSelectedFiles()
    {
        Node[] nodes =
            Browser.getActiveBrowser().getProjectView().getSelectedNodes(_project);

        for (int i = 0; i < nodes.length; i++)
        {
            walkTree(nodes[i]);
        }

        List files = new ArrayList(_projectFiles);
        _projectFiles.clear();

        return files;
    }


    /**
     * Adds the given node to the ProjectFile list.
     *
     * @param node node to add to the list.
     */
    private void addToList(Node node)
    {
        if (node instanceof JavaFileNode)
        {
            _projectFiles.add(new JbProjectFile(this, (JavaFileNode) node));
        }
    }


    /**
     * Walks over all children of the given node.
     *
     * @param node node to walk.
     */
    private void walkChildren(Node node)
    {
        Node[] nodes = node.getDisplayChildren();

        for (int i = 0; i < nodes.length; i++)
        {
            walkNode(nodes[i]);
        }
    }


    /**
     * Walks over the given node. Adds the node to the ProjectFile list.
     *
     * @param node node to walk.
     */
    private void walkNode(Node node)
    {
        if (node instanceof PackageNode)
        {
            /**
             * @todo is there a kind of 'special' node in the
             *       Professional/Enterprise versions?
             */
            if ("<Project Source>".equals(node.getDisplayName()))
            {
                // ignore children of this node to avoid duplication
                return;
            }
        }

        addToList(node);
        walkChildren(node);
    }


    /**
     * Walks over the whole tree. Adds all Java source file nodes to the ProjectFile
     * list.
     *
     * @param root root node of the tree.
     */
    private void walkTree(Node root)
    {
        if (_projectFiles == null)
        {
            _projectFiles = new ArrayList();
        }

        walkNode(root);
    }
}
