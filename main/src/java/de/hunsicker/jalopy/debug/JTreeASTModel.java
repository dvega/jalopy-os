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
package de.hunsicker.jalopy.debug;


/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/RIGHTS.html
 *
 * $Id$
 */
import de.hunsicker.antlr.collections.AST;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JTreeASTModel
    implements TreeModel
{
    //~ Instance variables ····················································

    AST root = null;

    //~ Constructors ··························································

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

    //~ Methods ·······························································

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
    public Object getChild(Object parent,
                           int    index)
    {
        if (parent == null)
        {
            return null;
        }

        AST p = (AST)parent;
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

        AST p = (AST)parent;
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
    public int getIndexOfChild(Object parent,
                               Object child)
    {
        if ((parent == null) || (child == null))
        {
            throw new IllegalArgumentException("root or child is null");
        }

        AST p = (AST)parent;
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

        AST t = (AST)node;

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
    public void valueForPathChanged(TreePath path,
                                    Object   newValue)
    {
        System.out.println("heh, who is calling this mystery method?");
    }
}
