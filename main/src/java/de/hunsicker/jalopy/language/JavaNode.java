/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.plugin.Annotation;


/**
 * A node representing an element in a Java source file.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class JavaNode
    extends Node
{
    //~ Instance variables ---------------------------------------------------------------

    /** Parent node. */
    protected JavaNode parent;

    /** Previous node. */
    protected JavaNode prevSibling;
    private List _annotations = Collections.EMPTY_LIST;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JavaNode object.
     *
     * @param startLine the starting line.
     * @param startColumn the starting column offset.
     * @param endLine the ending line.
     * @param endColumn the ending column offset.
     */
    public JavaNode(
        int startLine,
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
    public JavaNode(
        int    type,
        String text)
    {
        super(type, text);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns and detaches all annotations that are attached to this node.
     *
     * @return list of attached annotations (of type {@link de.hunsicker.jalopy.plugin.Annotation &lt;Annotation&gt;}). Returns an empty list in case no annotations
     *         were found.
     *
     * @since 1.0b9
     */
    public List detachAnnotations()
    {
        try
        {
            return _annotations;
        }
        finally
        {
            if (_annotations != Collections.EMPTY_LIST)
            {
                _annotations = Collections.EMPTY_LIST;
            }
        }
    }


    /**
     * Returns the first comment that appears after this node.
     *
     * @return the first comment that appears after this node. Returns <code>null</code>
     *         if no comments appear after this node.
     *
     * @since 1.0b9
     */
    public CommonHiddenStreamToken getCommentAfter()
    {
        if (this.hiddenAfter != null)
        {
            for (
                CommonHiddenStreamToken t = this.hiddenAfter; t != null;
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
     * @return the first comment before the node. Returns <code>null</code> if this node
     *         does not have a comment before.
     *
     * @since 1.0b8
     */
    public CommonHiddenStreamToken getFirstCommentBefore()
    {
        CommonHiddenStreamToken result = null;

        for (
            CommonHiddenStreamToken t = getHiddenBefore(); t != null;
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
     *
     * @since 1.0b8
     */
    public int getSizeCommentsAfter()
    {
        if (this.hiddenAfter != null)
        {
            int result = 0;

            for (
                CommonHiddenStreamToken t = this.hiddenAfter; t != null;
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
     *
     * @since 1.0b8
     */
    public int getSizeCommentsBefore()
    {
        if (this.hiddenBefore != null)
        {
            int result = 0;

            for (
                CommonHiddenStreamToken t = this.hiddenBefore; t != null;
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
     * Attaches an annotation to this node.
     *
     * @param annotation annotation.
     *
     * @since 1.0b9
     */
    public void attachAnnotation(Annotation annotation)
    {
        if (_annotations == Collections.EMPTY_LIST)
        {
            _annotations = new ArrayList(10);
        }

        System.err.println(this + " attach " + annotation);

        _annotations.add(annotation);
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

        JavaNode t = (JavaNode) this.down;
        JavaNode n = (JavaNode) node;

        if (t != null)
        {
            while (t.getNextSibling() != null)
            {
                t = (JavaNode) t.getNextSibling();
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
        for (
            CommonHiddenStreamToken comment = getHiddenBefore(); comment != null;
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

        JavaNode n = (JavaNode) node;
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
        buf.append((this.prevSibling != null) ? (this.prevSibling.text)
                                              : "NONE");

        buf.append(", next ");
        buf.append((getNextSibling() != null) ? (getNextSibling().getText())
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


    /**
     * Updates the line information of the attached annotations. Does nothing, if no
     * annotations are attached to this node.
     *
     * @param line the new line information for the annotations.
     *
     * @since 1.0b9
     */
    public void updateAnnotations(int line)
    {
        for (int i = 0, size = _annotations.size(); i < size; i++)
        {
            Annotation annotation = (Annotation) _annotations.get(i);

            System.err.println(this + " " + annotation.getLine()  + " --> " +line);

            annotation.setLine(line);
        }
    }
}