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

import de.hunsicker.antlr.CommonASTWithHiddenTokens;
import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.collections.AST;


//J-
import java.lang.ClassCastException;
//J+

/**
 * An extended node. Stores information about the node's span.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class Node
    extends CommonASTWithHiddenTokens
    implements Comparable
{
    //~ Instance variables ····················································

    /** Node text. */
    protected String text;

    /** Column number where the node ends. */
    protected int endColumn;

    /** Line number where the node ends. */
    protected int endLine;

    /** Column number where the node starts. */
    protected int startColumn;

    /** Line number where the node starts. */
    protected int startLine;

    /** Node type. */
    protected int type;

    //~ Constructors ··························································

    /**
     * Creates a new Node object.
     */
    public Node()
    {
    }


    /**
     * Creates a new Node object.
     *
     * @param type node type
     * @param text node text.
     * @param startLine line number where the node starts.
     * @param startColumn column number where the node starts.
     * @param endLine line number where the node ends.
     * @param endColumn column number where the node ends.
     */
    public Node(int    type,
                String text,
                int    startLine,
                int    startColumn,
                int    endLine,
                int    endColumn)
    {
        this.type = type;
        this.text = text;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }


    /**
     * Creates a new Node object.
     *
     * @param startLine line number where the node starts.
     * @param startColumn column number where the node starts.
     * @param endLine line number where the node ends.
     * @param endColumn column number where the node ends.
     */
    public Node(int startLine,
                int startColumn,
                int endLine,
                int endColumn)
    {
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }


    /**
     * Creates a new Node object.
     *
     * @param type node type
     * @param text node text.
     */
    public Node(int    type,
                String text)
    {
        this.type = type;
        this.text = text;
    }


    /**
     * Creates a new Node object from the given token.
     *
     * @param tok token to initialize the node with.
     */
    public Node(Token tok)
    {
        initialize(tok);
    }


    /**
     * Creates a new Node object.
     *
     * @param text text to add.
     */
    public Node(String text)
    {
        this.text = text;
    }

    //~ Methods ·······························································

    /**
     * Sets the end column value.
     *
     * @param column new end column value.
     */
    public void setEndColumn(int column)
    {
        this.endColumn = column;
    }


    /**
     * Returns the column number where the node ends.
     *
     * @return ending column number of the node.
     */
    public int getEndColumn()
    {
        return this.endColumn;
    }


    /**
     * Sets the end line value.
     *
     * @param line new end line value.
     */
    public void setEndLine(int line)
    {
        this.endLine = line;
    }


    /**
     * Returns the line number where the node ends.
     *
     * @return ending line number of the node.
     */
    public int getEndLine()
    {
        return this.endLine;
    }


    /**
     * Sets the first hidden token that appears after the node.
     *
     * @param token a hidden token.
     */
    public void setHiddenAfter(CommonHiddenStreamToken token)
    {
        this.hiddenAfter = token;
    }


    /**
     * Sets the first hidden token that appears before the node.
     *
     * @param token a hidden token.
     */
    public void setHiddenBefore(CommonHiddenStreamToken token)
    {
        this.hiddenBefore = token;
    }


    /**
     * Indicates whether the node has location information set.
     *
     * @return <code>true</code> if the node contains location information.
     */
    public boolean isPositionKnown()
    {
        return (this.startLine + this.endLine) > 0;
    }


    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     */
    public void setStartColumn(int column)
    {
        this.startColumn = column;
    }


    /**
     * Returns the column number where the node starts.
     *
     * @return starting column number of the node.
     */
    public int getStartColumn()
    {
        return this.startColumn;
    }


    /**
     * DOCUMENT ME!
     *
     * @param line DOCUMENT ME!
     */
    public void setStartLine(int line)
    {
        this.startLine = line;
    }


    /**
     * Returns the line number where the node starts.
     *
     * @return starting line number of the node.
     */
    public int getStartLine()
    {
        return this.startLine;
    }


    /**
     * Sets the text of the node.
     *
     * @param text text to set.
     */
    public void setText(String text)
    {
        this.text = text;
    }


    /**
     * Get the token text for this node
     *
     * @return the text of the node.
     */
    public String getText()
    {
        return this.text;
    }


    /**
     * Sets the type of the node.
     *
     * @param type type to set.
     */
    public void setType(int type)
    {
        this.type = type;
    }


    /**
     * Get the token type for this node
     *
     * @return the type of the node.
     */
    public int getType()
    {
        return this.type;
    }


    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * @param o the Object to be compared
     *
     * @return a negative integer, zero, or a positive integer as this node's
     *         extent starts before, with or after the extent of the
     *         specified node.
     *
     * @throws NullPointerException if <code>object == null</code>
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    public int compareTo(Object o)
    {
        if (o == this)
        {
            return 0;
        }

        if (o == null)
        {
            throw new NullPointerException("o == null");
        }

        if (!(o instanceof Node))
        {
            throw new ClassCastException(o.getClass() +
                                         " not of type de.hunsicker.jalopy.parser.Node");
        }

        Node other = (Node)o;

        if (startsBefore(other))
        {
            return -1;
        }
        else if (endsAfter(other))
        {
            return 1;
        }

        return 0;
    }


    /**
     * Checks whether this node contains the given node.
     *
     * @param node to check.
     *
     * @return <code>true</code> if the given node is contained in this node.
     */
    public boolean contains(Node node)
    {
        return contains(node.startLine, node.startColumn) &&
               contains(node.endLine, node.endColumn);
    }


    /**
     * Checks whether this node contains the given position.
     *
     * @param startLine start line to check.
     * @param startColumn start column to check.
     * @param endLine end line to check.
     * @param endColumn end column to check.
     *
     * @return <code>true</code> if the given position is contained in this
     *         span.
     */
    public boolean contains(int startLine,
                            int startColumn,
                            int endLine,
                            int endColumn)
    {
        return contains(startLine, startColumn) && contains(endLine, endColumn);
    }


    /**
     * Checks whether this node contains the given position.
     *
     * @param line line to check.
     * @param column column to check.
     *
     * @return <code>true</code> if the given position is contained in this
     *         span.
     */
    public boolean contains(int line,
                            int column)
    {
        boolean afterStart = false;
        boolean beforeEnd = false;

        if (this.startLine < line)
        {
            afterStart = true;
        }
        else if ((this.startLine == line) && (this.startColumn <= column))
        {
            afterStart = true;
        }

        if (this.endLine > line)
        {
            beforeEnd = true;
        }
        else if ((this.endLine == line) && (this.endColumn >= column))
        {
            beforeEnd = true;
        }

        return (afterStart && beforeEnd);
    }


    /**
     * Determines whether this node starts after another one.
     *
     * @param other node to check for.
     *
     * @return <code>true</code> if this node ends after the other node.
     */
    public boolean endsAfter(Node other)
    {
        boolean result = false;

        if (this.endLine > other.endLine)
        {
            result = true;
        }
        else if ((this.endLine == other.endLine) &&
                 (this.endColumn >= other.endColumn))
        {
            result = true;
        }

        return result;
    }


    /**
     * Compares the specified object with this object for equality. Returns
     * <code>true</code> if and only if the specified object is also an Node
     * and both nodes have the same text and type. So it does actually the
     * same job as {@link de.hunsicker.antlr.BaseAST#equals(AST)}.
     *
     * @param o the object to be compared for equality with this node.
     *
     * @return <code>true</code> if the specified object is equal to this
     *         object.
     */
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o instanceof Node)
        {
            Node node = (Node)o;
            boolean result = this.text.equals(node.text) &&
                             (this.type == node.type);

            return result;
        }

        return false;
    }


    /**
     * Initializes the node with information from the given node.
     *
     * @param node node to setup the node with.
     */
    public void initialize(AST node)
    {
        Node n = (Node)node;
        this.text = n.text;
        this.type = n.getType();
        this.startLine = n.startLine;
        this.endLine = n.endLine;
        this.startColumn = n.startColumn;
        this.endColumn = n.endColumn;
        this.hiddenBefore = n.getHiddenBefore();
        this.hiddenAfter = n.getHiddenAfter();
    }


    /**
     * Initializes the node with the given information.
     *
     * @param type type to set.
     * @param text text to set.
     */
    public void initialize(int    type,
                           String text)
    {
        this.type = type;
        this.text = text;
    }


    /**
     * Initializes the node with information from the given token.
     *
     * @param tok token to setup the node with.
     */
    public void initialize(Token tok)
    {
        ExtendedToken token = (ExtendedToken)tok;

        this.text = token.text;
        this.type = token.getType();
        this.startLine = token.getLine();
        this.endLine = token.endLine;
        this.startColumn = token.getColumn();
        this.endColumn = token.endColumn;
        this.hiddenBefore = token.getHiddenBefore();
        this.hiddenAfter = token.getHiddenAfter();
    }


    /**
     * Determines whether this node starts before another one.
     *
     * @param other node to check for.
     *
     * @return <code>true</code> if this node starts before the other one.
     */
    public boolean startsBefore(Node other)
    {
        boolean result = false;

        if (this.startLine < other.startLine)
        {
            result = true;
        }
        else if ((this.startLine == other.startLine) &&
                 (this.startColumn <= other.startColumn))
        {
            result = true;
        }

        return result;
    }


    /**
     * Returns a string representation of this node.
     *
     * @return a string representation of this node.
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(50);
        buf.append(this.text);
        buf.append(' ');
        buf.append(this.startLine);
        buf.append(':');
        buf.append(this.startColumn);
        buf.append(' ');
        buf.append(this.endLine);
        buf.append(':');
        buf.append(this.endColumn);

        return buf.toString();
    }
}
