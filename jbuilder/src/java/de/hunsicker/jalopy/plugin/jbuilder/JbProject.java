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

import com.borland.jbuilder.node.JavaFileNode;
import com.borland.jbuilder.node.PackageNode;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.ProjectView;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.Project;

import de.hunsicker.jalopy.plugin.ProjectFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The JBuilder project implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbProject
    implements de.hunsicker.jalopy.plugin.Project
{
    //~ Instance variables ····················································

    /** Used to build ProjectFile arrays. */
    private List _projectFiles; // List of <ProjectFile>

    /** The underlying JBuilder project representation. */
    private Project _project;

    //~ Constructors ··························································

    /**
     * Creates a new JbProject object.
     *
     * @param project the JBuilder representation of a project.
     */
    public JbProject(Project project)
    {
        _project = project;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        Node node = Browser.getActiveBrowser().getActiveNode();

        if (node instanceof JavaFileNode)
        {
            return new JbProjectFile(this, (JavaFileNode)node);
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
                files.add(new JbProjectFile(this, (JavaFileNode)nodes[i]));
            }
        }

        return files;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getSelectedFiles()
    {
        Node[] nodes = Browser.getActiveBrowser().getProjectView()
                              .getSelectedNodes(_project);

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
            _projectFiles.add(new JbProjectFile(this, (JavaFileNode)node));
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
     * Walks over the whole tree. Adds all java source file nodes to the
     * ProjectFile list.
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
