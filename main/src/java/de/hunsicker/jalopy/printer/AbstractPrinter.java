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
package de.hunsicker.jalopy.printer;

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.ExtendedToken;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.parser.Node;
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Loggers;
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.util.StringHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Skeleton implementation of the printer interface. Provides comment printing
 * and blank lines support.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class AbstractPrinter
    implements Printer
{
    //~ Static variables/initializers ·········································

    static final String ASSERT_SPACE = "assert ";
    static final String ASSIGN = "=";
    static final String ASSIGN_PADDED = " = ";
    static final String SPACE_ASSIGN = " =";
    static final String BRACES = "{}";
    static final String BRACKETS = "[]";
    static final String BRACKET_LEFT = "[";
    static final String BRACKET_LEFT_SPACE = "[ ";
    static final String BRACKET_RIGHT = "]";
    static final String CASE_SPACE = "case ";
    static final String CATCH = "catch";
    static final String CLASS_SPACE = "class ";
    static final String COMMA = ",";
    static final String COMMA_SPACE = ", ";
    static final String COLON = ":";
    static final String COLON_SPACE = ": ";
    static final String DEFAULT_COLON = "default:";
    static final String DEFAULT_SPACE_COLON = "default :";
    static final String DO = "do";
    static final String DOT = ".";
    static final String ELSE = "else";
    static final String EMPTY_STRING = "";
    static final String EXTENDS_SPACE = "extends ";
    static final String FINALLY = "finally";
    static final String FOR = "for";
    static final String FOR_SPACE = "for ";
    static final String IF = "if";
    static final String IF_SPACE = "if ";
    static final String IMPLEMENTS_SPACE = "implements ";
    static final String IMPORT_SPACE = "import ";
    static final String INTERFACE_SPACE = "interface ";
    static final String L = "L";
    static final String LCURLY = "{";
    static final String LCURLY_SPACE = "{ ";
    static final String LPAREN = "(";
    static final String LPAREN_SPACE = "( ";
    static final String NEW_SPACE = "new ";
    static final String PACKAGE_SPACE = "package ";
    static final String PARENTHESES = "()";
    static final String QUESTION_SPACE = "? ";
    static final String RCURLY = "}";
    static final String RCURLY_SPACE = " }";
    static final String RETURN = "return";
    static final String RPAREN = ")";
    static final String SEMI = ";";
    static final String SPACE = " ";
    static final String SPACE_BRACKET_RIGHT = " ]";
    static final String SPACE_BRACKETS = " []";
    static final String SPACE_COLON_SPACE = " : ";
    static final String SPACE_EXTENDS_SPACE = " extends ";
    static final String SPACE_LCURLY = " {";
    static final String SPACE_IMPLEMENTS_SPACE = " implements ";
    static final String SPACE_QUESTION = " ?";
    static final String SPACE_QUESTION_SPACE = " ? ";
    static final String SPACE_RPAREN = " )";
    static final String SPACE_THROWS_SPACE = " throws ";
    static final String STATIC = "static";
    static final String STRING = "String";
    static final String SUPER = "super";
    static final String SWITCH = "switch";
    static final String SYNCHRONIZED = "synchronized";
    static final String THIS = "this";
    static final String THROW_SPACE = "throw ";
    static final String THROWS_SPACE = "throws ";
    static final String TRY = "try";
    static final String VOID = "void";
    static final String WHILE = "while";
    static final String WHILE_SPACE = "while ";
    static final String QUESTION = "?";
    static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** The user preferences. */
    protected static final Preferences prefs = Preferences.getInstance();

    /** Indicates that <strong>no</strong> whitespace should be printed. */
    private static final boolean WHITESPACE_NO = false;

    /** Indicates that whitespace should be printed. */
    private static final boolean WHITESPACE_YES = true;

    //~ Instance variables ····················································

    /** Helper object that holds arguments for message formatting. */
    private final Object[] _args = new Object[2];

    //~ Constructors ··························································

    /**
     * Creates a new AbstractPrinter object.
     */
    protected AbstractPrinter()
    {
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void print(AST        node,
                      NodeWriter out)
        throws IOException
    {
        out.print(node.getText(), node.getType());
    }


    /**
     * Outputs indentation whitespace according to the current marker offset.
     * If no marker is set, this method does nothing.
     *
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    protected void printIndentation(NodeWriter out)
        throws IOException
    {
        printIndentation(0, out);
    }


    /**
     * Outputs indentation whitespace according to the current marker offset.
     * If no marker is set, this method does nothing.
     *
     * @param diff DOCUMENT ME!
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    protected void printIndentation(int        diff,
                                    NodeWriter out)
        throws IOException
    {
        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            if (!this.prefs.getBoolean(Keys.INDENT_DEEP, Defaults.INDENT_DEEP))
            {
                /**
                 * @todo implement custom indentation
                 */
                out.print(out.getString((out.state.paramLevel * out.indentSize) +
                                        diff), JavaTokenTypes.WS);
            }

            else if (out.state.markers.isMarked())
            {
                int indentLength = out.getIndentLength();
                Marker marker = out.state.markers.getLast();

                /*int offset = diff;

                if (out.state.paramLevel > 0)
                {
                    //offset += ((out.state.paramLevel - 1) * out.indentSize);
                    offset += out.indentSize;
                }*/

                //offset += out.state.paramLevel * out.indentSize;
                int length = (marker.column > indentLength)
                                 ? (marker.column - indentLength)
                                 : marker.column;

                if ((length + diff) >= 0)
                {
                    out.print(out.getString(length + diff), JavaTokenTypes.WS);
                }
                else
                {
                    out.print(out.getString(length), JavaTokenTypes.WS);
                }
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @since 1.0b9
     */
    final boolean isChildOf(JavaNode node,
                            JavaNode child)
    {
        JavaNode parent = child.getParent();

        if (parent != node)
        {
            switch (parent.getType())
            {
                case JavaTokenTypes.OBJBLOCK :
                case JavaTokenTypes.SLIST :
                case JavaTokenTypes.LCURLY :
                case JavaTokenTypes.LITERAL_if :
                case JavaTokenTypes.LITERAL_for :
                case JavaTokenTypes.LITERAL_while :
                case JavaTokenTypes.LITERAL_do :
                case JavaTokenTypes.SYNBLOCK :
                case JavaTokenTypes.LITERAL_try :
                case JavaTokenTypes.LITERAL_catch :
                case JavaTokenTypes.LITERAL_finally :
                case JavaTokenTypes.LITERAL_static :
                case JavaTokenTypes.PARAMETERS :
                case JavaTokenTypes.MODIFIERS :
                case JavaTokenTypes.TYPE :
                    break;

                default :
                    return isChildOf(node, parent);
            }
        }
        else
        {
            return true;
        }

        return false;
    }


    /**
     * Determines the number of blank lines before the given node as found in
     * the parsed source file.
     *
     * @param node a node.
     *
     * @return number of blank lines before the given node as found in the
     *         parsed source file.
     */
    int getOriginalBlankLines(JavaNode node)
    {
        int keepLinesUpTo = this.prefs.getInt(Keys.BLANK_LINES_KEEP_UP_TO,
                                              Defaults.BLANK_LINES_KEEP_UP_TO);

        if (keepLinesUpTo <= 0)
        {
            // forget about original blank lines
            return 0;
        }

        /**
         * @todo it is currently not possible to determine the original blank
         *       lines for the class declarations because of the sorting
         *       transformation; we need to implement the preserving logic
         *       via stream splitting and deal with hidden tokens
         */
        switch (node.getPreviousSibling().getType())
        {
            case JavaTokenTypes.METHOD_DEF :
            case JavaTokenTypes.CTOR_DEF :
            case JavaTokenTypes.CLASS_DEF :
            case JavaTokenTypes.INTERFACE_DEF :
            case JavaTokenTypes.STATIC_INIT :
            case JavaTokenTypes.INSTANCE_INIT :
            case JavaTokenTypes.IMPORT :
            case JavaTokenTypes.PACKAGE_DEF :
            case JavaTokenTypes.ROOT :
                return 0;

            case JavaTokenTypes.VARIABLE_DEF :

                if (!isLocalVariable(node))
                {
                    return 0;
                }
        }

        switch (node.getType())
        {
            case JavaTokenTypes.EXPR :

                for (JavaNode child = (JavaNode)node.getFirstChild();
                     child != null;
                     child = (JavaNode)child.getFirstChild())
                {
                    if (child.hasCommentsBefore())
                    {
                        CommonHiddenStreamToken t = child.getFirstCommentBefore();

                        if (t != null)
                        {
                            int l = t.getLine() -
                                    node.getPreviousSibling().getEndLine() - 1;

                            if (l > keepLinesUpTo)
                            {
                                return keepLinesUpTo;
                            }

                            return Math.max(l, 0);
                        }
                    }
                }

                break;

            case JavaTokenTypes.VARIABLE_DEF :

                if (!isLocalVariable(node))
                {
                    return 0;
                }

                break;

            case JavaTokenTypes.RCURLY :

                int blankLinesBeforeRcurly = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT,
                                                               Defaults.BLANK_LINES_BEFORE_BRACE_RIGHT);

                if (blankLinesBeforeRcurly != -1)
                {
                    return blankLinesBeforeRcurly;
                }
        }

        JavaNode prev = node.getPreviousSibling();
        int result = 0;

        /**
         * @todo currently import nodes have no prevSibling after
         *       transformation
         */
        if (prev != null)
        {
            if (!node.hasCommentsBefore())
            {
                if (node.getParent().getType() == JavaTokenTypes.EXPR)
                {
                    /*result = node.getStartLine() -
                            node.getParent().getPreviousSibling()
                                .getEndLine() - 1;*/
                }
                else
                {
                    result = node.getStartLine() - prev.getEndLine() - 1;
                }
            }
            else
            {
                if (node.getParent().getType() == JavaTokenTypes.EXPR)
                {
                    /*result = node.getHiddenBefore().getLine() -
                             node.getParent().getPreviousSibling()
                                 .getEndLine() - 1;*/
                }
                else
                {
                    result = node.getHiddenBefore().getLine() -
                             prev.getEndLine() - 1;
                }
            }
        }

        if (result > keepLinesUpTo)
        {
            return keepLinesUpTo;
        }

        return result;
    }


    /**
     * Outputs all found issues for the given node.
     *
     * @param node a node.
     * @param out stream to write to.
     */
    void logIssues(AST        node,
                   NodeWriter out)
    {
        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            if (out.issues.containsKey(node))
            {
                Object value = out.issues.get(node);
                _args[0] = out.filename;
                _args[1] = String.valueOf(out.line);

                StringBuffer buf = new StringBuffer(250);

                if (value instanceof List)
                {
                    List issues = (List)value;

                    for (int i = 0, size = issues.size(); i < size; i++)
                    {
                        JavaNode n = (JavaNode)node;
                        buf.append(out.filename);
                        buf.append(':');
                        buf.append(out.line);
                        buf.append(':');
                        buf.append((String)issues.get(i));

                        Loggers.IO.warn(buf.toString());
                        buf.setLength(0);
                    }
                }
                else
                {
                    buf.append(out.filename);
                    buf.append(':');
                    buf.append(out.line);
                    buf.append(':');
                    buf.append((String)value);

                    Loggers.IO.warn(buf.toString());
                }
            }
        }
    }


    /**
     * Outputs a number of blank lines before the given node.
     *
     * @param node node to print blank lines for.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    void printBlankLinesBefore(JavaNode   node,
                               NodeWriter out)
        throws IOException
    {
        int blankLines = getBlankLinesBefore(node, out);
        out.printBlankLines(blankLines);

        switch (node.getType())
        {
            case JavaTokenTypes.EXPR :

                // we need to store this information in order to be able to
                // print the right amout of blank lines before the node. This is
                // because we print out blank lines for the EXPR node
                // and afterwards may have to print blanks lines for a child
                // (because of an associated comment)
                out.blankLines = blankLines;
                out.expression = node;

                break;
        }
    }


    /**
     * Prints all comments after the given node. The handling of newlines
     * before and after depends on the type of the given node.
     *
     * @param node node that have its comments printed.
     * @param out stream to write to.
     *
     * @return <code>true</code> if comments were printed.
     *
     * @throws IOException if an I/O error occured.
     *
     * @see #printCommentsAfter(AST, boolean, boolean, NodeWriter)
     */
    boolean printCommentsAfter(AST        node,
                               NodeWriter out)
        throws IOException
    {
        if (out.mode != NodeWriter.MODE_DEFAULT)
        {
            return false;
        }

        JavaNode n = (JavaNode)node;

        if (n.getHiddenAfter() == null)
        {
            return false;
        }

        boolean result = false;

        switch (node.getType())
        {
            // these nodes can have endline comments (before their block),
            // therefore the comment printing depends on the current brace mode
            case JavaTokenTypes.LITERAL_if :
            case JavaTokenTypes.LITERAL_else :
            case JavaTokenTypes.LITERAL_for :
            case JavaTokenTypes.LITERAL_while :
            case JavaTokenTypes.LITERAL_try :
            case JavaTokenTypes.LITERAL_catch :
            case JavaTokenTypes.LITERAL_finally :
            case JavaTokenTypes.LITERAL_switch :
            case JavaTokenTypes.LITERAL_do :
            case JavaTokenTypes.LITERAL_synchronized :
            case JavaTokenTypes.METHOD_DEF :
            case JavaTokenTypes.CLASS_DEF :
            case JavaTokenTypes.INTERFACE_DEF :
            case JavaTokenTypes.CTOR_DEF :
            case JavaTokenTypes.INSTANCE_INIT :
            case JavaTokenTypes.STATIC_INIT :

                /**
                 * @todo does this stuff still work in 1.0b9?
                 */
                if (this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                          Defaults.BRACE_NEWLINE_LEFT))
                {
                    // print directly after, newlines are
                    // handled by BlockPrinter.java
                    result = printCommentsAfter(node, NodeWriter.NEWLINE_NO,
                                                NodeWriter.NEWLINE_NO, out);
                }
                else
                {
                    /*List comments = ((JavaNode)node).getCommentsAfter();
                    JavaNode child = (JavaNode)NodeHelper.getFirstChild(node,
                                                                        JavaTokenTypes.SLIST);

                    if (child != null)
                    {
                        if (child.hasCommentsAfter())
                        {
                            result = printCommentsAfter(node, out,
                                                        NodeWriter.NEWLINE_NO,
                                                        NodeWriter.NEWLINE_YES);
                        }
                        else
                        {
                            // we can't print the comment directly because than
                            // we would comment out the following open curly
                            // brace. Therefore we just store the comment;
                            // BlockPrinter.java will output it
                            AST comment = (AST)comments.remove(0);
                            out.setPendingComment(comment.getText());
                            result = false;
                        }
                    }*/
                }

                break;

            default :
                result = printCommentsAfter(node, NodeWriter.NEWLINE_NO,
                                            NodeWriter.NEWLINE_NO, out);

                break;
        }

        return result;
    }


    /**
     * Prints all comments after the given node.
     *
     * @param node node that have its comments printed.
     * @param newlineBefore if <code>true</code>, a line break will be added
     *        before each comment.
     * @param newlineAfter if <code>true</code>, a line break will be added
     *        after each comment.
     * @param out stream to write to.
     *
     * @return <code>true</code> if comments were printed.
     *
     * @throws IOException if an I/O error occured.
     */
    boolean printCommentsAfter(AST        node,
                               boolean    newlineBefore,
                               boolean    newlineAfter,
                               NodeWriter out)
        throws IOException
    {
        if (out.mode == NodeWriter.MODE_TEST)
        {
            return false;
        }

        JavaNode n = (JavaNode)node;

        if (n.getHiddenAfter() == null)
        {
            return false;
        }

        // if this is not the last right curly brace
        if ((node.getType() != JavaTokenTypes.RCURLY) ||
            (out.getIndentLevel() != 0))
        {
            // store the position where the first comment starts
            int offset = out.column - 1 +
                         this.prefs.getInt(Keys.INDENT_SIZE_COMMENT_ENDLINE,
                                           Defaults.INDENT_SIZE_COMMENT_ENDLINE);
            CommonHiddenStreamToken firstComment = (CommonHiddenStreamToken)n.getHiddenAfter();

            // if we have more than one comment
            if (firstComment.getHiddenAfter() != null)
            {
                // print the first comment directly after the node
                printCommentAfter(n, firstComment, newlineBefore,
                                  NodeWriter.NEWLINE_YES, WHITESPACE_YES, out);

                int indentLength = out.getIndentLength();

                // and align all other under the first
                for (CommonHiddenStreamToken comment = firstComment.getHiddenAfter();
                     comment != null;
                     comment = comment.getHiddenAfter())
                {
                    if (offset < indentLength)
                    {
                        int indentLevel = out.getIndentLevel();
                        out.setIndentLevel(0);
                        out.print(out.getString(offset), JavaTokenTypes.WS);
                        out.setIndentLevel(indentLevel);
                    }
                    else
                    {
                        out.print(out.getString(offset - indentLength),
                                  JavaTokenTypes.WS);
                    }

                    printCommentAfter(n, comment, newlineBefore, newlineAfter,
                                      WHITESPACE_NO, out);
                }
            }
            else
            {
                printCommentAfter(n, firstComment, newlineBefore, newlineAfter,
                                  WHITESPACE_YES, out);
            }
        }
        else
        {
            CommonHiddenStreamToken firstComment = n.getHiddenAfter();

            // is the first comment an endline comment?
            boolean endlineComment = firstComment.getLine() == n.getEndLine();

            if (!endlineComment)
            {
                out.printNewline();
                out.printNewline();
            }

            printCommentAfter(node, firstComment, NodeWriter.NEWLINE_NO,
                              NodeWriter.NEWLINE_YES,
                              endlineComment ? WHITESPACE_YES
                                             : WHITESPACE_NO, out);

            // the Java Language Specification requires every single-line
            // comment to be terminated by an end-of-line sequence, for the
            // sake of simplicity we print one for all comment types
            for (CommonHiddenStreamToken comment = firstComment.getHiddenAfter();
                 comment != null;
                 comment = comment.getHiddenAfter())
            {
                printCommentAfter(n, comment, newlineBefore,
                                  NodeWriter.NEWLINE_YES, WHITESPACE_NO, out);
            }
        }

        out.last = node.getType();

        return true;
    }


    /**
     * Prints all comments before the given node.
     *
     * @param node node that have its comments printed.
     * @param out stream to write to.
     *
     * @return <code>true</code> if comments were printed.
     *
     * @throws IOException if an I/O error occured.
     */
    boolean printCommentsBefore(AST        node,
                                NodeWriter out)
        throws IOException
    {
        return printCommentsBefore(node, true, out);
    }


    /**
     * Prints all comments before the given node.
     *
     * @param node node that have its comments printed.
     * @param newlineBefore if <code>true</code> a line break will be printed
     *        before the comment.
     * @param out stream to write to.
     *
     * @return <code>true</code> if comments were printed.
     *
     * @throws IOException if an I/O error occured.
     * @throws RuntimeException DOCUMENT ME!
     */
    boolean printCommentsBefore(AST        node,
                                boolean    newlineBefore,
                                NodeWriter out)
        throws IOException
    {
        if (out.mode == NodeWriter.MODE_TEST)
        {
            return false;
        }

        JavaNode n = (JavaNode)node;

        if (!n.hasCommentsBefore())
        {
            // no comments, only print blank lines
            printBlankLinesBefore(n, out);

            return false;
        }

        int linesToKeep = this.prefs.getInt(Keys.BLANK_LINES_KEEP_UP_TO,
                                            Defaults.BLANK_LINES_KEEP_UP_TO);
        boolean keepLines = linesToKeep > -1;

        CommonHiddenStreamToken previousComment = null;

        // the first hidden token is actually the last token found, so we
        // have to traverse the chain first
        for (CommonHiddenStreamToken token = ((JavaNode)node).getHiddenBefore();
             token != null;
             token = token.getHiddenBefore())
        {
            if (token.getHiddenBefore() == null)
            {
                for (CommonHiddenStreamToken comment = token;
                     comment != null;
                     comment = comment.getHiddenAfter())
                {
                    switch (comment.getType())
                    {
                        case JavaTokenTypes.SL_COMMENT :
                        case JavaTokenTypes.ML_COMMENT :
                        case JavaTokenTypes.JAVADOC_COMMENT :
                        case JavaTokenTypes.SPECIAL_COMMENT :
                        case JavaTokenTypes.SEPARATOR_COMMENT :

                            if (n.getStartLine() != comment.getLine())
                            {
                                switch (out.last)
                                {
                                    case JavaTokenTypes.SEPARATOR_COMMENT :
                                        break;

                                    default :

                                        /*if (keepLines && (previousComment != null))
                                        {
                                            printBlankLinesBetweenComments(comment,
                                                                           previousComment,
                                                                           linesToKeep,
                                                                           out);
                                        }*/
                                        break;
                                }

                                printCommentBefore(n, comment, comment == token,
                                                   newlineBefore,
                                                   NodeWriter.NEWLINE_YES, out);
                            }
                            else // print in same line
                            {
                                printCommentBefore(n, comment, comment == token,
                                                   newlineBefore,
                                                   NodeWriter.NEWLINE_NO, out);
                                out.print(SPACE, out.last);

                                // change the column offset as we don't
                                // want line wrapping occuring because of the
                                // comment
                                out.column -= (comment.getText().length() - 1);
                            }

                            break;

                        default :
                            throw new RuntimeException("" + comment);
                    }

                    previousComment = comment;
                }

                return true;
            }
        }

        return false;
    }


    /**
     * Returns the number of blank lines that should be printed before the
     * given node.
     *
     * @param node a node.
     * @param out stream to write to.
     *
     * @return the number of blank lines to be printed before the node.
     */
    private int getBlankLinesBefore(JavaNode   node,
                                    NodeWriter out)
    {
        int result = 0;

        if (canHaveBlankLinesBefore(node))
        {
            // first set the value as specified in the preferences
            switch (node.getType())
            {
                case JavaTokenTypes.ASSIGN :
                    break;

                case JavaTokenTypes.EXPR :

                    switch (node.getParent().getType())
                    {
                        case JavaTokenTypes.SLIST :
                        case JavaTokenTypes.CASESLIST :
                            break;

                        default :
                            return 0;
                    }

                    break;

                case JavaTokenTypes.VARIABLE_DEF :

                    switch (node.getParent().getType())
                    {
                        case JavaTokenTypes.FOR_INIT :
                            return 0;
                    }

                    switch (out.last)
                    {
                        case JavaTokenTypes.VARIABLE_DEF :
                            break;

                        case JavaTokenTypes.METHOD_DEF :
                        case JavaTokenTypes.CTOR_DEF :
                        case JavaTokenTypes.CLASS_DEF :
                        case JavaTokenTypes.INTERFACE_DEF :
                        case JavaTokenTypes.INSTANCE_INIT :
                        case JavaTokenTypes.STATIC_INIT :
                            result = 1;

                            break;

                        default :
                            result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_DECLARATION,
                                                       Defaults.BLANK_LINES_BEFORE_DECLARATION);

                            break;
                    }

                    break;

                case JavaTokenTypes.RCURLY :

                    if (!node.hasCommentsBefore())
                    {
                        switch (out.last)
                        {
                            case JavaTokenTypes.RCURLY :
                            case JavaTokenTypes.CLASS_DEF :
                            case JavaTokenTypes.INTERFACE_DEF :
                            case JavaTokenTypes.METHOD_DEF :
                            case JavaTokenTypes.CTOR_DEF :
                            case JavaTokenTypes.INSTANCE_INIT :
                            case JavaTokenTypes.STATIC_INIT :
                                return this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT,
                                                         Defaults.BLANK_LINES_BEFORE_BRACE_RIGHT);

                            default :
                                result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT,
                                                           Defaults.BLANK_LINES_BEFORE_BRACE_RIGHT);

                                break;
                        }
                    }

                    break;

                case JavaTokenTypes.LITERAL_if :

                    switch (out.last)
                    {
                        case JavaTokenTypes.LITERAL_else :
                        case JavaTokenTypes.LABELED_STAT :
                            break;

                        default :
                            result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BLOCK,
                                                       Defaults.BLANK_LINES_BEFORE_BLOCK);

                            break;
                    }

                    break;

                // blocks
                case JavaTokenTypes.LITERAL_while :

                    if (node.getParent().getType() == JavaTokenTypes.LITERAL_do)
                    {
                        break;
                    }

                case JavaTokenTypes.LITERAL_for :
                case JavaTokenTypes.LITERAL_try :
                case JavaTokenTypes.LITERAL_switch :
                case JavaTokenTypes.SYNBLOCK :
                case JavaTokenTypes.LITERAL_do :

                    switch (out.last)
                    {
                        case JavaTokenTypes.LABELED_STAT :
                            break;

                        default :
                            result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BLOCK,
                                                       Defaults.BLANK_LINES_BEFORE_BLOCK);

                            break;
                    }

                    break;

                case JavaTokenTypes.LITERAL_case :
                case JavaTokenTypes.LITERAL_default :
                    result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_CASE_BLOCK,
                                               Defaults.BLANK_LINES_BEFORE_CASE_BLOCK);

                    break;

                case JavaTokenTypes.LITERAL_return :
                case JavaTokenTypes.LITERAL_break :
                case JavaTokenTypes.LITERAL_continue :
                    result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_CONTROL,
                                               Defaults.BLANK_LINES_BEFORE_CONTROL);

                    break;

                case JavaTokenTypes.SLIST :

                    switch (node.getParent().getType())
                    {
                        case JavaTokenTypes.LITERAL_if :
                        case JavaTokenTypes.LITERAL_for :
                        case JavaTokenTypes.LITERAL_do :
                        case JavaTokenTypes.LITERAL_while :
                        case JavaTokenTypes.LITERAL_try :
                        case JavaTokenTypes.SYNBLOCK :
                            break;

                        default :

                            switch (node.getPreviousSibling().getType())
                            {
                                case JavaTokenTypes.INSTANCE_INIT :
                                    break;

                                default : // means freestanding block
                                    result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BLOCK,
                                                               Defaults.BLANK_LINES_BEFORE_BLOCK);

                                    break;
                            }

                            break;
                    }

                    break;

                case JavaTokenTypes.LITERAL_else :
                case JavaTokenTypes.LITERAL_catch :
                case JavaTokenTypes.LITERAL_finally :

                    if (!this.prefs.getBoolean(Keys.BRACE_NEWLINE_RIGHT,
                                               Defaults.BRACE_NEWLINE_RIGHT))
                    {
                        result--;
                    }

                    break;

                case JavaTokenTypes.SEMI :
                case JavaTokenTypes.IMPORT :
                case JavaTokenTypes.LABELED_STAT :
                case JavaTokenTypes.ARRAY_INIT :
                case JavaTokenTypes.CTOR_CALL :
                case JavaTokenTypes.SUPER_CTOR_CALL :
                case JavaTokenTypes.LITERAL_throw :
                case JavaTokenTypes.EMPTY_STAT :
                case JavaTokenTypes.LITERAL_assert :
                case JavaTokenTypes.PACKAGE_DEF :
                    break;

                case JavaTokenTypes.METHOD_DEF :
                {
                    JavaNode prev = node.getPreviousSibling();

                    switch (prev.getType())
                    {
                        case JavaTokenTypes.CTOR_DEF :
                        case JavaTokenTypes.CLASS_DEF :
                        case JavaTokenTypes.INTERFACE_DEF :
                        case JavaTokenTypes.VARIABLE_DEF :
                        case JavaTokenTypes.INSTANCE_INIT :
                        case JavaTokenTypes.STATIC_INIT :
                            result = 1;

                            break;

                        case JavaTokenTypes.METHOD_DEF :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_METHOD,
                                                       Defaults.BLANK_LINES_AFTER_METHOD);

                            break;

                        default :
                            break;
                    }

                    break;
                }

                case JavaTokenTypes.CTOR_DEF :
                {
                    JavaNode prev = node.getPreviousSibling();

                    switch (prev.getType())
                    {
                        case JavaTokenTypes.METHOD_DEF :
                        case JavaTokenTypes.CLASS_DEF :
                        case JavaTokenTypes.INTERFACE_DEF :
                        case JavaTokenTypes.INSTANCE_INIT :
                        case JavaTokenTypes.STATIC_INIT :
                        case JavaTokenTypes.VARIABLE_DEF :
                            result = 1;

                            break;

                        case JavaTokenTypes.CTOR_DEF :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_METHOD,
                                                       Defaults.BLANK_LINES_AFTER_METHOD);

                            break;

                        default :
                            break;
                    }

                    break;
                }

                case JavaTokenTypes.CLASS_DEF :
                {
                    JavaNode prev = node.getPreviousSibling();

                    switch (prev.getType())
                    {
                        case JavaTokenTypes.METHOD_DEF :
                        case JavaTokenTypes.CTOR_DEF :
                        case JavaTokenTypes.INSTANCE_INIT :
                        case JavaTokenTypes.STATIC_INIT :
                        case JavaTokenTypes.VARIABLE_DEF :
                            result = 1;

                            break;

                        case JavaTokenTypes.INTERFACE_DEF :

                            if (out.indentLevel > 0)
                            {
                                result = 1;
                            }
                            else
                            {
                                result = 2;
                            }

                            break;

                        case JavaTokenTypes.CLASS_DEF :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_CLASS,
                                                       Defaults.BLANK_LINES_AFTER_CLASS);

                            /**
                             * @todo see NodeWriter.printCloseBrace(int,
                             *       boolean)
                             */
                            if (out.indentLevel == 0)
                            {
                                result++;
                            }

                            break;

                        case JavaTokenTypes.IMPORT :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_IMPORT,
                                                       Defaults.BLANK_LINES_AFTER_IMPORT);

                            break;

                        default :
                            break;
                    }

                    break;
                }

                case JavaTokenTypes.INTERFACE_DEF :
                {
                    JavaNode prev = node.getPreviousSibling();

                    switch (prev.getType())
                    {
                        case JavaTokenTypes.METHOD_DEF :
                        case JavaTokenTypes.CTOR_DEF :
                        case JavaTokenTypes.INSTANCE_INIT :
                        case JavaTokenTypes.STATIC_INIT :
                        case JavaTokenTypes.VARIABLE_DEF :
                            result = 1;

                            break;

                        case JavaTokenTypes.CLASS_DEF :

                            if (out.indentLevel > 0)
                            {
                                result = 1;
                            }
                            else
                            {
                                result = 2;
                            }

                            break;

                        case JavaTokenTypes.INTERFACE_DEF :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_INTERFACE,
                                                       Defaults.BLANK_LINES_AFTER_INTERFACE);

                            /**
                             * @todo see NodeWriter.printCloseBrace(int,
                             *       boolean)
                             */
                            if (out.indentLevel == 0)
                            {
                                result++;
                            }

                            break;

                        case JavaTokenTypes.IMPORT :
                            result = this.prefs.getInt(Keys.BLANK_LINES_AFTER_IMPORT,
                                                       Defaults.BLANK_LINES_AFTER_IMPORT);

                            break;

                        default :
                            break;
                    }

                    break;
                }

                case JavaTokenTypes.INSTANCE_INIT :
                    result = 1;

                    break;

                case JavaTokenTypes.STATIC_INIT :
                    result = 1;

                    break;

                default :

                    /**
                     * @todo do we need to determine whether this node is part
                     *       of an imaginary node and thus the blank line
                     *       setting was already determined?
                     */
                    return 0;
            }
        }
        else
        {
            return 0;
        }

        // override if necessary (according to the last node printed)
        switch (out.last)
        {
            case JavaTokenTypes.INSTANCE_INIT :
            case JavaTokenTypes.STATIC_INIT :
            case JavaTokenTypes.RCURLY :

                switch (node.getType())
                {
                    case JavaTokenTypes.RCURLY :
                    case JavaTokenTypes.LITERAL_else :
                    case JavaTokenTypes.LITERAL_catch :
                    case JavaTokenTypes.LITERAL_finally :
                    case JavaTokenTypes.LITERAL_case :
                    case JavaTokenTypes.LITERAL_default :
                        break;

                    case JavaTokenTypes.LITERAL_while :

                        switch (node.getParent().getType())
                        {
                            case JavaTokenTypes.LITERAL_do :
                                result = 0;

                                break;
                        }

                        break;

                    default :

                        int blankLinesAfterBlock = this.prefs.getInt(Keys.BLANK_LINES_AFTER_BLOCK,
                                                                     Defaults.BLANK_LINES_AFTER_BLOCK);

                        if (blankLinesAfterBlock > result)
                        {
                            result = blankLinesAfterBlock;
                        }

                        /*
                           if (!this.prefs.getBoolean(Keys.BRACE_NEWLINE_RIGHT,
                                                      Defaults.BRACE_NEWLINE_RIGHT))
                           {
                               switch (node.getPreviousSibling().getType())
                               {
                                   case JavaTokenTypes.CLASS_DEF:
                                   case JavaTokenTypes.INTERFACE_DEF:
                                   break;
                                   default:
                                   result++;
                                   break;
                               }
                           }
                         */
                        break;
                }

                break;

            case JavaTokenTypes.LITERAL_case :
            case JavaTokenTypes.LITERAL_default :

                switch (node.getType())
                {
                    case JavaTokenTypes.LITERAL_case :
                    case JavaTokenTypes.LITERAL_default :
                    case JavaTokenTypes.LITERAL_return :
                    case JavaTokenTypes.LITERAL_break :
                        result = 0;

                        break;
                }

                break;

            case JavaTokenTypes.VARIABLE_DEF :

                switch (node.getType())
                {
                    case JavaTokenTypes.VARIABLE_DEF :
                        break;

                    case JavaTokenTypes.METHOD_DEF :
                    case JavaTokenTypes.CTOR_DEF :
                    case JavaTokenTypes.INSTANCE_INIT :
                    case JavaTokenTypes.STATIC_INIT :
                    case JavaTokenTypes.CLASS_DEF :
                    case JavaTokenTypes.INTERFACE_DEF :
                    case JavaTokenTypes.RCURLY :
                        break;

                    default :

                        int blankLinesAfterDeclaration = this.prefs.getInt(Keys.BLANK_LINES_AFTER_DECLARATION,
                                                                           Defaults.BLANK_LINES_AFTER_DECLARATION);

                        if (blankLinesAfterDeclaration > result)
                        {
                            result = blankLinesAfterDeclaration;
                        }

                        break;
                }

                break;

            case JavaTokenTypes.LITERAL_while :
            case JavaTokenTypes.LITERAL_if :
            case JavaTokenTypes.LITERAL_for :
            case JavaTokenTypes.LITERAL_do :
            case JavaTokenTypes.LCURLY :

                int blankLinesAfterOpenCurly = this.prefs.getInt(Keys.BLANK_LINES_AFTER_BRACE_LEFT,
                                                                 Defaults.BLANK_LINES_AFTER_BRACE_LEFT);

                if (blankLinesAfterOpenCurly > -1)
                {
                    // force number of blank lines after open curly braces
                    // (and block statements without braces printed)
                    result = blankLinesAfterOpenCurly;
                }

                break;

            case JavaTokenTypes.BOF :
                break;
        }

        // and keep the original blank lines
        switch (node.getType())
        {
            case JavaTokenTypes.CLASS_DEF :
            case JavaTokenTypes.INTERFACE_DEF :
            case JavaTokenTypes.METHOD_DEF :
            case JavaTokenTypes.CTOR_DEF :
            case JavaTokenTypes.INSTANCE_INIT :
            case JavaTokenTypes.STATIC_INIT :
            case JavaTokenTypes.PACKAGE_DEF :
                break;

            default :

                int original = getOriginalBlankLines(node);

                if (original > result)
                {
                    result = original;
                }

                break;
        }

        return result;
    }


    /**
     * Returns the number of blank lines that should be printed before a node
     * because of the given comment (the first comment of the given node).
     *
     * @param comment the first comment of a node.
     * @param previous DOCUMENT ME!
     * @param out stream to write to.
     *
     * @return the number of blank lines to be printed before the given
     *         comment.
     */
    private int getCommentBlankLines(CommonHiddenStreamToken comment,
                                     JavaNode                previous,
                                     NodeWriter              out)
    {
        switch (out.last)
        {
            case JavaTokenTypes.BOF :
                return 0;

            case JavaTokenTypes.LCURLY :

                int blankLinesAfterOpenCurly = this.prefs.getInt(Keys.BLANK_LINES_AFTER_BRACE_LEFT,
                                                                 Defaults.BLANK_LINES_AFTER_BRACE_LEFT);

                if (blankLinesAfterOpenCurly > -1)
                {
                    // force number of blank lines after open curly braces
                    return blankLinesAfterOpenCurly;
                }

                break;
        }

        int result = 0;

        switch (comment.getType())
        {
            case JavaTokenTypes.SL_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.SL_COMMENT :
                        result = 0;

                        break;

                    default :
                        result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE,
                                                   Defaults.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE);

                        break;
                }

                break;

            case JavaTokenTypes.SPECIAL_COMMENT :
                result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE,
                                           Defaults.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE);

                break;

            case JavaTokenTypes.ML_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.ML_COMMENT :
                        result = 0;

                        break;

                    default :
                        result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE,
                                                   Defaults.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE);

                        break;
                }

                break;

            case JavaTokenTypes.JAVADOC_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.JAVADOC_COMMENT :
                        result = 0;

                        break;

                    default :
                        result = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_COMMENT_JAVADOC,
                                                   Defaults.BLANK_LINES_BEFORE_COMMENT_JAVADOC);

                        break;
                }

                break;

            case JavaTokenTypes.SEPARATOR_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.RCURLY :
                        result = 2;

                        break;

                    default :
                        result = 1;

                        break;
                }

                break;
        }

        switch (out.last)
        {
            case JavaTokenTypes.PACKAGE_DEF :

                int linesAfterPackage = this.prefs.getInt(Keys.BLANK_LINES_AFTER_PACKAGE,
                                                          Defaults.BLANK_LINES_AFTER_PACKAGE);

                if (result > linesAfterPackage)
                {
                    result -= linesAfterPackage;
                }
                else
                {
                    result = 0;
                }

                break;
        }

        int keepLinesUpTo = this.prefs.getInt(Keys.BLANK_LINES_KEEP_UP_TO,
                                              Defaults.BLANK_LINES_KEEP_UP_TO);

        int l = comment.getLine() - previous.getEndLine() - 1;

        if (l > keepLinesUpTo)
        {
            return keepLinesUpTo;
        }

        return Math.max(result, l);
    }


    /**
     * Determines whether the given VARIABLE_DEF node denotes a local variable
     * (those immediately contained by a method, constructor or initializer
     * block).
     *
     * @param node VARIABLE_DEF node.
     *
     * @return <code>true</code> if the given node denotes a local variable.
     */
    private boolean isLocalVariable(JavaNode node)
    {
        switch (node.getParent().getType())
        {
            case JavaTokenTypes.OBJBLOCK :
                return false;

            default :
                return true;
        }
    }


    /**
     * Determines whether the given node can have blank before.
     *
     * @param node node to check.
     *
     * @return <code>true</code> if the given node can have blank lines
     *         before.
     */
    private boolean canHaveBlankLinesBefore(JavaNode node)
    {
        /*switch (node.getType())
        {
            case JavaTokenTypes.EXPR:
            case JavaTokenTypes.LITERAL_if:
            case JavaTokenTypes.LITERAL_for:
            case JavaTokenTypes.LITERAL_while:
            case JavaTokenTypes.LITERAL_do:
            case JavaTokenTypes.LITERAL_try:
            case JavaTokenTypes.LITERAL_catch:
            case JavaTokenTypes.LITERAL_finally:
                return true;
        }*/
        JavaNode parent = node.getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.SLIST :
            case JavaTokenTypes.OBJBLOCK :
            case JavaTokenTypes.LCURLY :
            case JavaTokenTypes.ROOT :
                return true;

            case JavaTokenTypes.MODIFIERS :
                return parent.getFirstChild() == node;

            case JavaTokenTypes.EXPR :
            case JavaTokenTypes.METHOD_CALL :
            case JavaTokenTypes.IMPORT :
            case JavaTokenTypes.CLASS_DEF :
            case JavaTokenTypes.INTERFACE_DEF :
            case JavaTokenTypes.PACKAGE_DEF :
                return false;

            default :
                return canHaveBlankLinesBefore(parent);
        }
    }


    /**
     * Outputs a number of blank lines before the given comment (according to
     * the user preferences).
     *
     * @param node node to print blank lines for.
     * @param comment the first comment of the given node.
     * @param first <code>true</code> indicates that <em>comment</em> is the
     *        first comment (in case of multiple comments in a row)
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printBlankLinesBefore(JavaNode                node,
                                       CommonHiddenStreamToken comment,
                                       boolean                 first,
                                       NodeWriter              out)
        throws IOException
    {
        int blankLines = 0;
        int blankLinesForComment = 0;

        switch (comment.getType())
        {
            case JavaTokenTypes.SL_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.BOF :
                        break;

                    case JavaTokenTypes.JAVADOC_COMMENT :
                    case JavaTokenTypes.ML_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :

                    //case JavaTokenTypes.SEMI:
                    case JavaTokenTypes.RCURLY :
                    case JavaTokenTypes.CLASS_DEF :
                    case JavaTokenTypes.INTERFACE_DEF :
                    case JavaTokenTypes.VARIABLE_DEF :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = 1;

                        break;

                    default :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;
                }

                break;

            case JavaTokenTypes.JAVADOC_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.JAVADOC_COMMENT :
                    case JavaTokenTypes.BOF :
                        break;

                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.ML_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :

                    //case JavaTokenTypes.SEMI:
                    case JavaTokenTypes.RCURLY :
                    case JavaTokenTypes.CLASS_DEF :
                    case JavaTokenTypes.INTERFACE_DEF :
                    case JavaTokenTypes.VARIABLE_DEF :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        //blankLinesForComment = 1;
                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;

                    default :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;
                }

                break;

            case JavaTokenTypes.ML_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.ML_COMMENT :
                    case JavaTokenTypes.BOF :
                        break;

                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.JAVADOC_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = 1;

                        break;

                    default :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;
                }

                break;

            case JavaTokenTypes.SEPARATOR_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.ML_COMMENT :
                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.JAVADOC_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = 1;

                        break;

                    case JavaTokenTypes.BOF :
                        break;

                    default :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;
                }

                break;

            case JavaTokenTypes.SPECIAL_COMMENT :

                switch (out.last)
                {
                    case JavaTokenTypes.ML_COMMENT :
                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.JAVADOC_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = 1;

                        break;

                    case JavaTokenTypes.BOF :
                        break;

                    default :

                        if (first)
                        {
                            blankLines = getBlankLinesBefore(node, out);
                        }

                        blankLinesForComment = getCommentBlankLines(comment,
                                                                    node.getPreviousSibling(),
                                                                    out);

                        break;
                }

                break;
        }

        if (canHaveBlankLinesBefore(node))
        {
            out.printBlankLines(Math.max(blankLines, blankLinesForComment));
        }
        else
        {
            if (out.expression != null)
            {
                if (isChildOf(out.expression, node))
                {
                    out.printBlankLines(blankLinesForComment - out.blankLines);
                }
            }
            else
            {
                out.printBlankLines(Math.max(blankLines, blankLinesForComment));
            }
        }
    }


    /**
     * Prints the original amout of blank lines between the two given
     * comments.
     *
     * @param comment the comment.
     * @param previousComment the previous comment.
     * @param keepLines the number of blank lines to keep.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printBlankLinesBetweenComments(CommonHiddenStreamToken comment,
                                                CommonHiddenStreamToken previousComment,
                                                int                     keepLines,
                                                NodeWriter              out)
        throws IOException
    {
        int lines = 0;

        switch (previousComment.getType())
        {
            case JavaTokenTypes.SPECIAL_COMMENT :
                break;

            default :
                lines = comment.getLine() - previousComment.getLine() - 1;

                break;
        }

        if (lines > keepLines)
        {
            out.printBlankLines(keepLines);
        }
        else if (lines > 0)
        {
            out.printBlankLines(lines);
        }
    }


    /**
     * Prints the given comment after the specified node.
     *
     * @param node the node the comment is attached to.
     * @param comment the comment to print.
     * @param newlineBefore if <code>true</code> a newline will be printed
     *        before the comment.
     * @param newlineAfter if <code>true</code> a newline will be printed
     *        after the comment.
     * @param whitespaceBefore if <code>true</code> the comment will be
     *        indented.
     * @param out the stream to write to.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void printCommentAfter(AST                     node,
                                   CommonHiddenStreamToken comment,
                                   boolean                 newlineBefore,
                                   boolean                 newlineAfter,
                                   boolean                 whitespaceBefore,
                                   NodeWriter              out)
        throws IOException
    {
        int last = out.last;
        boolean restore = false;
        int indent = 0;

        if ((comment.getColumn() == 1) &&
            (!this.prefs.getBoolean(Keys.INDENT_FIRST_COLUMN_COMMENT,
                                    Defaults.INDENT_FIRST_COLUMN_COMMENT)))
        {
            // we should retain first column comments, so we have to
            // change the indentation level
            restore = true;
            indent = out.getIndentLevel();
            out.setIndentLevel(0);
        }

        if (whitespaceBefore)
        {
            out.print(out.getString(this.prefs.getInt(
                                                      Keys.INDENT_SIZE_COMMENT_ENDLINE,
                                                      Defaults.INDENT_SIZE_COMMENT_ENDLINE)),
                      JavaTokenTypes.WS);
        }

        switch (comment.getType())
        {
            case JavaTokenTypes.JAVADOC_COMMENT :

                // Javadoc endline comment may indicate bad
                // formatted input source, issue an error here

                /**
                 * @todo allow Javadoc endline comment if it is the first
                 *       comment, and only throw error otherwise
                 */
                throw new IllegalArgumentException("no valid endline comment -- " +
                                                   comment + "(attached to " +
                                                   node + ")");

            case JavaTokenTypes.SL_COMMENT :
                out.print(comment.getText(), comment.getType());

                break;

            case JavaTokenTypes.ML_COMMENT :
                printMultiLineComment(comment, JavaTokenTypes.ML_COMMENT, out);

                break;

            case JavaTokenTypes.SPECIAL_COMMENT :
                printSpecialComment(comment, out);

                break;

            case JavaTokenTypes.SEPARATOR_COMMENT :
                out.print(comment.getText(), comment.getType());

                break;

            default :
                throw new IllegalArgumentException("invalid type -- " + comment);
        }

        if (restore)
        {
            // restore current indentation level
            out.setIndentLevel(indent);
        }

        if (newlineAfter || shouldForceNewline(node, comment))
        {
            out.printNewline();
        }

        out.last = last;
    }


    /**
     * Prints the given comment before the specified node.
     *
     * @param node node the comment belongs to.
     * @param comment the comment to print.
     * @param first DOCUMENT ME!
     * @param newlineBefore if <code>true</code>, a line break will be added
     *        before each comment, if appropriate.
     * @param newlineAfter if <code>true</code>, a line break will be added
     *        after each comment, if appropriate.
     * @param out the stream to print to.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void printCommentBefore(JavaNode                node,
                                    CommonHiddenStreamToken comment,
                                    boolean                 first,
                                    boolean                 newlineBefore,
                                    boolean                 newlineAfter,
                                    NodeWriter              out)
        throws IOException
    {
        printBlankLinesBefore(node, comment, first, out);

        boolean retain = false;
        int indent = 0;

        if ((comment.getType() == JavaTokenTypes.SPECIAL_COMMENT) ||
            ((comment.getColumn() == 1) &&
             (!this.prefs.getBoolean(Keys.INDENT_FIRST_COLUMN_COMMENT,
                                     Defaults.INDENT_FIRST_COLUMN_COMMENT))))
        {
            // we should retain indentation, so we have to change the
            // indentation level
            indent = out.getIndentLevel();
            out.setIndentLevel(0);
            retain = true;
        }

        int type = comment.getType();

        switch (type)
        {
            case JavaTokenTypes.SL_COMMENT :
                out.print(comment.getText(), type);

                break;

            case JavaTokenTypes.JAVADOC_COMMENT :

                ExtendedToken t = (ExtendedToken)comment;
                Node c = t.getComment();

                if (c != null)
                {
                    ((JavadocPrinter)PrinterFactory.create(c)).print(node, c,
                                                                     out);
                }
                else
                {
                    printMultiLineComment(comment,
                                          JavaTokenTypes.JAVADOC_COMMENT, out);
                }

                break;

            case JavaTokenTypes.ML_COMMENT :
                printMultiLineComment(comment, JavaTokenTypes.ML_COMMENT, out);

                break;

            case JavaTokenTypes.SPECIAL_COMMENT :
                printSpecialComment(comment, out);

                break;

            case JavaTokenTypes.SEPARATOR_COMMENT :
                out.print(comment.getText(), comment.getType());

                // if the node the separator comment belongs to, contains
                // no other comments we want a newline between the two
                if (comment.getHiddenAfter() == null)
                {
                    out.printNewline();
                }

                break;

            default :
                throw new IllegalArgumentException("invalid type -- " + comment);
        }

        if (retain)
        {
            // restore the indentation level
            out.setIndentLevel(indent);
        }

        if (newlineAfter)
        {
            out.printNewline();
        }
    }


    /**
     * Prints the given multi line comment.
     *
     * @param comment the comment token.
     * @param type the type of the comment.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    private void printMultiLineComment(CommonHiddenStreamToken comment,
                                       int                     type,
                                       NodeWriter              out)
        throws IOException
    {
        String[] lines = null;
        boolean format = this.prefs.getBoolean(Keys.COMMENT_FORMAT_MULTI_LINE,
                                               Defaults.COMMENT_FORMAT_MULTI_LINE);

        // we always split the comment into several lines in order to indent
        // the text correctly (and the proper eol characters are used)
        if (format)
        {
            lines = StringHelper.wrapStringToArray(comment.getText(),
                                                   Integer.MAX_VALUE,
                                                   out.lineSeparator, false,
                                                   StringHelper.TRIM_LEADING);
        }
        else
        {
            lines = split(comment.getText(), out.originalLineSeparator);
        }

        int lastLine = lines.length - 1;

        for (int i = 0; i < lines.length; i++)
        {
            if (lines[i].length() > 0)
            {
                out.print(lines[i], type);
            }

            if (i < lastLine)
            {
                out.printNewline();
            }
        }
    }


    /**
     * Prints the given multi line comment.
     *
     * @param comment the comment token.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    private void printSpecialComment(CommonHiddenStreamToken comment,
                                     NodeWriter              out)
        throws IOException
    {
        String[] lines = split(comment.getText(), out.originalLineSeparator);

        int lastLine = lines.length - 1;

        for (int i = 0; i < lines.length; i++)
        {
            if (lines[i].length() > 0)
            {
                out.print(lines[i], JavaTokenTypes.SPECIAL_COMMENT);
            }

            if (i < lastLine)
            {
                out.printNewline();
            }
        }
    }


    /**
     * Determines wether the given comment makes a line break necessary after
     * the given node.
     *
     * @param node a node.
     * @param comment a comment.
     *
     * @return <code>true</code> if the given comment makes a line break
     *         necessary after the given node.
     */
    private boolean shouldForceNewline(AST                     node,
                                       CommonHiddenStreamToken comment)
    {
        switch (comment.getType())
        {
            case JavaTokenTypes.SL_COMMENT :
            case JavaTokenTypes.SPECIAL_COMMENT :
            case JavaTokenTypes.SEPARATOR_COMMENT :

                if (this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                          Defaults.BRACE_NEWLINE_LEFT))
                {
                    return (!NodeHelper.isBlockNext(node));
                }

                return true;

            default :
                return false;
        }
    }


    /**
     * Returns the individual lines of the given multi-line comment.
     *
     * @param str a multi-line comment.
     * @param delim the line separator.
     *
     * @return the individual lines of the comment.
     *
     * @todo move this into de.hunsicker.util.StringHelper
     */
    private String[] split(String str,
                           String delim)
    {
        int startOffset = 0;
        int endOffset = -1;
        int sepLength = delim.length();
        List lines = new ArrayList(15);

        while ((endOffset = str.indexOf(delim, startOffset)) > -1)
        {
            lines.add(str.substring(startOffset, endOffset));
            startOffset = endOffset + sepLength;
        }

        if (startOffset > 0)
        {
            lines.add(str.substring(startOffset));
        }
        else
        {
            lines.add(str);
        }

        return (String[])lines.toArray(EMPTY_STRING_ARRAY);
    }
}
