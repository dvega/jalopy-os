/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.debug;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/RIGHTS.html
 *
 * $Id$
 */
import antlr.collections.AST;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JTreeASTModel
    implements TreeModel
{
    //~ Instance variables ---------------------------------------------------------------

    AST root = null;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JTreeASTModel object.
     *
     * @param t DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public JTreeASTModel(AST t)
    {
        if (t == null)
        {
            throw new IllegalArgumentException("root is null");
        }

        root = t;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public Object getChild(
        Object parent,
        int    index)
    {
        if (parent == null)
        {
            return null;
        }

        AST p = (AST) parent;
        AST c = p.getFirstChild();

        if (c == null)
        {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        int i = 0;

        while ((c != null) && (i < index))
        {
            c = c.getNextSibling();
            i++;
        }

        return c;
    }


    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public int getChildCount(Object parent)
    {
        if (parent == null)
        {
            throw new IllegalArgumentException("root is null");
        }

        AST p = (AST) parent;
        AST c = p.getFirstChild();
        int i = 0;

        while (c != null)
        {
            c = c.getNextSibling();
            i++;
        }

        return i;
    }


    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     * @throws java.util.NoSuchElementException DOCUMENT ME!
     */
    public int getIndexOfChild(
        Object parent,
        Object child)
    {
        if ((parent == null) || (child == null))
        {
            throw new IllegalArgumentException("root or child is null");
        }

        AST p = (AST) parent;
        AST c = p.getFirstChild();

        if (c == null)
        {
            throw new ArrayIndexOutOfBoundsException("node has no children");
        }

        int i = 0;

        while ((c != null) && (c != child))
        {
            c = c.getNextSibling();
            i++;
        }

        if (c == child)
        {
            return i;
        }

        throw new java.util.NoSuchElementException("node is not a child");
    }


    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public boolean isLeaf(Object node)
    {
        if (node == null)
        {
            throw new IllegalArgumentException("node is null");
        }

        AST t = (AST) node;

        return t.getFirstChild() == null;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getRoot()
    {
        return root;
    }


    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void addTreeModelListener(TreeModelListener l)
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param l DOCUMENT ME!
     */
    public void removeTreeModelListener(TreeModelListener l)
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     */
    public void valueForPathChanged(
        TreePath path,
        Object   newValue)
    {
        System.out.println("heh, who is calling this mystery method?");
    }
}
