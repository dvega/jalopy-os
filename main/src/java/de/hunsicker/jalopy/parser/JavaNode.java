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

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.collections.AST;


/**
 * A node representing an element in a Java source file.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class JavaNode
    extends Node
{
    //~ Instance variables ····················································

    /** Parent node. */
    protected JavaNode parent;

    /** Previous node. */
    protected JavaNode prevSibling;

    //~ Constructors ··························································

    /**
     * Creates a new JavaNode object.
     *
     * @param startLine the starting line.
     * @param startColumn the starting column offset.
     * @param endLine the ending line.
     * @param endColumn the ending column offset.
     */
    public JavaNode(int startLine,
                    int startColumn,
                    int endLine,
                    int endColumn)
    {
        super(startLine, startColumn, endLine, endColumn);
    }


    /**
     * Creates a new JavaNode object.
     */
    public JavaNode()
    {
        super();
    }


    /**
     * Creates a new JavaNode object from the given token.
     *
     * @param tok token to initialize the node with.
     */
    public JavaNode(Token tok)
    {
        super(tok);
    }


    /**
     * Creates a new JavaNode object.
     *
     * @param type the type of the node.
     * @param text the text of the node.
     */
    public JavaNode(int    type,
                    String text)
    {
        super(type, text);
    }

    //~ Methods ·······························································

    /**
     * Returns the first comment that appears after this node.
     *
     * @return the first comment that appears after this node. Returns
     *         <code>null</code> if no comments appear after this node.
     *
     * @since 1.0b9
     */
    public CommonHiddenStreamToken getCommentAfter()
    {
        if (this.hiddenAfter != null)
        {
            for (CommonHiddenStreamToken t = this.hiddenAfter;
                 t != null;
                 t = t.getHiddenAfter())
            {
                switch (t.getType())
                {
                    case JavaTokenTypes.WS :
                        break;

                    default :
                        return t;
                }
            }
        }

        return null;
    }


    /**
     * Returns the first comment before this node.
     *
     * @return the first comment before the node. Returns <code>null</code> if
     *         this node does not have a comment before.
     */
    public CommonHiddenStreamToken getFirstCommentBefore()
    {
        CommonHiddenStreamToken result = null;

        for (CommonHiddenStreamToken t = getHiddenBefore();
             t != null;
             t = t.getHiddenBefore())
        {
            switch (t.getType())
            {
                case JavaTokenTypes.WS :
                    break;

                default :
                    result = t;

                    break;
            }
        }

        return result;
    }


    /**
     * Sets the parent to the given node.
     *
     * @param node a node.
     */
    public void setParent(JavaNode node)
    {
        this.parent = node;
    }


    /**
     * Returns the parent node of this node.
     *
     * @return parent node.
     */
    public JavaNode getParent()
    {
        return this.parent;
    }


    /**
     * Sets the previous sibling to the given node.
     *
     * @param node a node.
     */
    public void setPreviousSibling(JavaNode node)
    {
        this.prevSibling = node;
    }


    /**
     * Returns the previous sibling of this node.
     *
     * @return previous sibling node.
     */
    public JavaNode getPreviousSibling()
    {
        return this.prevSibling;
    }


    /**
     * Returns the number of comments that appear after this node.
     *
     * @return number of comment after this node.
     */
    public int getSizeCommentsAfter()
    {
        if (this.hiddenAfter != null)
        {
            int result = 0;

            for (CommonHiddenStreamToken t = this.hiddenAfter;
                 t != null;
                 t = t.getHiddenAfter())
            {
                switch (t.getType())
                {
                    case JavaTokenTypes.WS :
                        break;

                    default :
                        result++;

                        break;
                }
            }

            return result;
        }

        return 0;
    }


    /**
     * Returns the number of comments that appear before this node.
     *
     * @return number of comment before this node.
     */
    public int getSizeCommentsBefore()
    {
        if (this.hiddenBefore != null)
        {
            int result = 0;

            for (CommonHiddenStreamToken t = this.hiddenBefore;
                 t != null;
                 t = t.getHiddenBefore())
            {
                switch (t.getType())
                {
                    case JavaTokenTypes.WS :
                        break;

                    default :
                        result++;

                        break;
                }
            }

            return result;
        }

        return 0;
    }


    /**
     * Adds a node to the end of the child list for this node.
     *
     * @param node node to add as a new child.
     */
    public void addChild(AST node)
    {
        if (node == null)
        {
            return;
        }

        JavaNode t = (JavaNode)this.down;
        JavaNode n = (JavaNode)node;

        if (t != null)
        {
            while (t.getNextSibling() != null)
            {
                t = (JavaNode)t.getNextSibling();
            }

            t.setNextSibling(node);
            n.prevSibling = t;
        }
        else
        {
            this.down = n;
            n.prevSibling = this;
        }

        n.parent = this;
        this.endLine = n.endLine;
        this.endColumn = n.endColumn;
    }


    /**
     * Indicates whether comments appear before of this node.
     *
     * @return <code>true</code> if comments appear before this node.
     */
    public boolean hasCommentsAfter()
    {
        return getSizeCommentsAfter() > 0;
    }


    /**
     * Indicates whether comments appear after this node.
     *
     * @return <code>true</code> if comments follows this node.
     */
    public boolean hasCommentsBefore()
    {
        return getSizeCommentsBefore() > 0;
    }


    /**
     * Indicates whether this node has a Javadoc comment attached.
     *
     * @return <code>true</code> if this node has a Javadoc comment attached.
     */
    public boolean hasJavadocComment()
    {
        for (CommonHiddenStreamToken comment = getHiddenBefore();
             comment != null;
             comment = comment.getHiddenBefore())
        {
            switch (comment.getType())
            {
                case JavaTokenTypes.JAVADOC_COMMENT :
                    return true;
            }
        }

        return false;
    }


    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void initialize(AST node)
    {
        super.initialize(node);

        JavaNode n = (JavaNode)node;
        this.parent = n.parent;
        this.prevSibling = n.prevSibling;
    }


    /**
     * Returns a string representation of this node.
     *
     * @return a string representation of this node.
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(50);
        buf.append('"');
        buf.append(this.text);
        buf.append('"');

        //buf.append(super.toString());
        buf.append(", <");
        buf.append(this.type);
        buf.append(">");

        buf.append(", par ");
        buf.append((this.parent != null) ? this.parent.text
                                         : "NONE");
        buf.append(", prev ");
        buf.append((this.prevSibling != null)
                       ? (this.prevSibling.text)
                       : "NONE");

        buf.append(", next ");
        buf.append((getNextSibling() != null)
                       ? (getNextSibling().getText())
                       : "NONE");

        buf.append(", [");
        buf.append(this.startLine);
        buf.append(':');
        buf.append(this.startColumn);
        buf.append("-");
        buf.append(this.endLine);
        buf.append(':');
        buf.append(this.endColumn);
        buf.append("], ");

        buf.append(getSizeCommentsBefore());
        buf.append(",");
        buf.append(getSizeCommentsAfter());

        return buf.toString();
    }
}
