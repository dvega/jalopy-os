/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

import de.hunsicker.antlr.ASTFactory;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.collections.AST;


/**
 * Central facility to create extended nodes.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class NodeFactory
    extends ASTFactory
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The empty string constant. */
    protected static final String EMPTY_STRING = "" /* NOI18N */.intern();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new NodeFactory object.
     */
    public NodeFactory()
    {
        this.theASTNodeType = "Node" /* NOI18N */;
        this.theASTNodeTypeClass = Node.class;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Creates a new empty Node node.
     *
     * @return newly created Node.
     */
    public AST create()
    {
        Node t = new Node();

        return t;
    }


    /**
     * Creates a new empty Node node.
     *
     * @param type information to setup the node with.
     *
     * @return newly created Node.
     */
    public AST create(int type)
    {
        AST t = create();
        t.initialize(type, EMPTY_STRING);

        return t;
    }


    /**
     * Creates a new empty Node node.
     *
     * @param type type information to setup the node with.
     * @param text text to setup the node with.
     *
     * @return newly created Node.
     */
    public AST create(
        int    type,
        String text)
    {
        AST t = create();
        t.initialize(type, text);

        return t;
    }


    /**
     * Creates a new empty Node node.
     *
     * @param node node to setup the new node with.
     *
     * @return newly created Node.
     */
    public AST create(AST node)
    {
        if (node == null)
        {
            return null;
        }

        AST t = create();
        t.initialize(node);

        return t;
    }


    /**
     * Creates a new empty Node node.
     *
     * @param tok token to setup the new node with.
     *
     * @return newly created Node.
     */
    public AST create(Token tok)
    {
        AST t = create();
        t.initialize(tok);

        return t;
    }
}
