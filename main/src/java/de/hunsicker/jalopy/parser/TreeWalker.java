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
package de.hunsicker.jalopy.parser;

import de.hunsicker.antlr.collections.AST;


/**
 * Helper class to make implementing tree walkers easy.
 * 
 * <p>
 * To implement a class that walks over all nodes simply dereive from
 * TreeWalker and implement the callback method {@link #visit}.
 * </p>
 * <pre style="background:lightgrey">
 * class MyWalker
 *     extends TreeWalker
 * {
 *     // called for every node
 *     public void visit(AST node)
 *     {
 *         System.out.println("node visited: " &#043; node);
 *     }
 * }
 * </pre>
 * 
 * <p>
 * If you want to control which nodes will be actually visited, overwrite
 * {@link #walkNode} too.
 * </p>
 * <pre style="background:lightgrey">
 * // Visits only IMPORT nodes and quits after the last IMPORT node found
 * protected void walkNode(AST node)
 * {
 *     switch (node.getType())
 *     {
 *         case JavaTokenTypes.ROOT:
 *             // skip to next child
 *             walkNode(node.getFirstChild());
 *             break;
 *         case JavaTokenTypes.PACKAGE_DEF:
 *             // skip to next child
 *             walkNode(node.getNextSibling());
 *             break;
 *      
 *        case JavaTokenTypes.IMPORT:
 *             // only visit root node, DON'T walk over it's children
 *             visit(node);
 *             // continue with the next node
 *             walkNode(node.getNextSibling());
 *             break;
 *         case JavaTokenTypes.CLASS_DEF:
 *             // quit after the first non-IMPORT node
 *             return;
 *     }
 * }
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public abstract class TreeWalker
{
    //~ Instance variables ····················································

    /** Indicates a stop. */
    protected boolean stop;

    //~ Constructors ··························································

    /**
     * Creates a new TreeWalker object.
     */
    protected TreeWalker()
    {
    }

    //~ Methods ·······························································

    /**
     * Resets the walker.
     */
    public void reset()
    {
        this.stop = false;
    }


    /**
     * Callback method that can be called for a node found. Overwrite to
     * perform whatever action you want take place for a node.
     * 
     * <p>
     * In the default implementation, this method will be called for every
     * node of the tree.
     * </p>
     *
     * @param node a node of the tree.
     */
    public abstract void visit(AST node);


    /**
     * Starts the walking with the root node of the tree or node portion.
     *
     * @param tree the tree to walk over.
     */
    public void walk(AST tree)
    {
        walkNode(tree);
    }


    /**
     * Stops the walking. You have to reset the walker prior reusing it.
     *
     * @see #reset
     */
    protected void stop()
    {
        this.stop = true;
    }


    /**
     * Iterates over all children of the given node.
     *
     * @param node node to iterate over.
     */
    protected void walkChildren(AST node)
    {
        if (!this.stop)
        {
            for (AST child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                walkNode(child);
            }
        }
    }


    /**
     * Walks over the given node. Links in children. Called for every single
     * node of the tree. The default implemention simply calls
     * <pre style="background:lightgrey">
     * visit(node);
     * walkChildren();
     * </pre>
     * to visit the current node and continue walking over all children of the
     * node.
     *
     * @param node a node of the tree.
     */
    protected void walkNode(AST node)
    {
        if (!this.stop)
        {
            visit(node);
            walkChildren(node);
        }
    }
}
