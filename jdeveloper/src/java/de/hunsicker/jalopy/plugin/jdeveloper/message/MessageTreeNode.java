/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.message;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * A custom tree node for displaying in the message page.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class MessageTreeNode
    extends DefaultMutableTreeNode
{
    //~ Instance variables ---------------------------------------------------------------

    /** The type of the node. */
    Type type;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new MessageTreeNode object.
     *
     * @param message the message object of the node.
     * @param type of the tree node.
     */
    public MessageTreeNode(
        Message message,
        Type    type)
    {
        super(message);
        this.type = type;
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * Used to distinguish between certain node types.
     */
    public static final class Type
    {
        /** Indicates the root node of the tree. */
        static final Type ROOT = new Type("root");

        /**
         * Indicates the parent node for file based messages where several messages
         * belongs to one parent message.
         */
        public static final Type FILE = new Type("file");

        /**
         * Indicates the parent node for multi-line messages where several single-line
         * messages form a multi-line message.
         */
        public static final Type MULTI_LINE = new Type("multi");

        /** Indicates a single-line message. */
        public static final Type SINGLE_LINE = new Type("single");
        String name;

        private Type(String name)
        {
            this.name = name;
        }
    }
}
