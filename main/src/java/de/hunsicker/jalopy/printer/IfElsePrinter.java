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
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for if-else statements [<code>LITERAL_if</code>,
 * <code>LITERAL_else</code>].
 * <pre style="background:lightgrey">
 * <strong>if</strong> (<em>Boolean-expression</em>)
 * {
 *     <em>statement</em>
 * }
 * <strong>else if</strong> (<em>Boolean-expression</em>)
 * {
 *     <em>statement</em>
 * }
 * <strong>else</strong>
 * {
 *    <em>statement</em>
 * }
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class IfElsePrinter
    extends BlockStatementPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new IfElsePrinter();

    //~ Constructors ··························································

    /**
     * Creates a new IfElsePrinter object.
     */
    protected IfElsePrinter()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns the sole instance of this class.
     *
     * @return the sole instance of this class.
     */
    public static final Printer getInstance()
    {
        return INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    public void print(AST        node,
                      NodeWriter out)
        throws IOException
    {
        printCommentsBefore(node, out);

        // print space between else and if:
        // if { ... } else if { ... }
        switch (out.last)
        {
            case JavaTokenTypes.LITERAL_else :
                out.print(SPACE, JavaTokenTypes.LITERAL_if);

                break;
        }

        boolean spaceBefore = this.prefs.getBoolean(Keys.SPACE_BEFORE_STATEMENT_PAREN,
                                                    Defaults.SPACE_BEFORE_STATEMENT_PAREN);

        if (spaceBefore)
        {
            out.print(IF_SPACE, JavaTokenTypes.LITERAL_if);
        }
        else
        {
            out.print(IF, JavaTokenTypes.LITERAL_if);
        }

        AST lparen = node.getFirstChild();

        boolean insertBraces = this.prefs.getBoolean(Keys.BRACE_INSERT_IF_ELSE,
                                                     Defaults.BRACE_INSERT_IF_ELSE);

        JavaNode rparen = printExpressionList(lparen, insertBraces, out);
        AST body = rparen.getNextSibling();

        boolean leftBraceNewline = this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                                         Defaults.BRACE_NEWLINE_LEFT);
        boolean rightBraceNewline = this.prefs.getBoolean(Keys.BRACE_NEWLINE_RIGHT,
                                                          Defaults.BRACE_NEWLINE_RIGHT);
        boolean hasBraces = body.getType() == JavaTokenTypes.SLIST;

        out.last = JavaTokenTypes.LITERAL_if;

        JavaNode next = (JavaNode)body.getNextSibling();

        if (hasBraces)
        {
            PrinterFactory.create(body).print(body, out);
        }
        else // no braces, single statement
        {
            if (insertBraces) // insert braces manually
            {
                if (out.pendingComment == null)
                {
                    out.printLeftBrace(leftBraceNewline && (!out.newline),
                                       NodeWriter.NEWLINE_YES,
                                       (!leftBraceNewline) && (!out.newline));
                }
                else
                {
                    out.printLeftBrace(NodeWriter.NEWLINE_NO,
                                       NodeWriter.NEWLINE_NO);
                    rparen.setHiddenAfter(out.pendingComment);
                    printCommentsAfter(rparen, NodeWriter.NEWLINE_NO,
                                       NodeWriter.NEWLINE_YES, out);
                    out.pendingComment = null;
                }

                PrinterFactory.create(body).print(body, out);
                out.printRightBrace(rightBraceNewline || (next == null));
            }
            else
            {
                out.printNewline();
                out.indent();
                PrinterFactory.create(body).print(body, out);
                out.unindent();
            }
        }

        // print the else-if or else part
        if (next != null)
        {
            printCommentsBefore(next, out);

            if ((!out.newline) && (out.last == JavaTokenTypes.RCURLY))
            {
                out.print(out.getString(this.prefs.getInt(
                                                          Keys.INDENT_SIZE_BRACE_RIGHT_AFTER,
                                                          Defaults.INDENT_SIZE_BRACE_RIGHT_AFTER)),
                          JavaTokenTypes.WS);
            }

            out.print(ELSE, JavaTokenTypes.LITERAL_else);

            JavaNode block = (JavaNode)next.getNextSibling();

            switch (block.getType())
            {
                case JavaTokenTypes.SLIST : // block

                    // print the endline comment for the else
                    printCommentsAfter(next, NodeWriter.NEWLINE_NO,
                                       !leftBraceNewline, out);

                    // either we print a LITERAL_if or LITERAL_else but
                    // we don't care as both will lead to the same result
                    out.last = JavaTokenTypes.LITERAL_if;
                    PrinterFactory.create(block).print(block, out);

                    break;

                case JavaTokenTypes.LITERAL_if : // the else-if 'if'
                    out.last = JavaTokenTypes.LITERAL_else;
                    print(block, out);

                    break;

                default : // single expression

                    if (insertBraces)
                    {
                        out.pendingComment = next.getCommentAfter();

                        if (out.pendingComment == null)
                        {
                            printCommentsAfter(next, NodeWriter.NEWLINE_NO,
                                               !leftBraceNewline, out);
                            out.printLeftBrace(rightBraceNewline,
                                               NodeWriter.NEWLINE_NO,
                                               (!rightBraceNewline) &&
                                               (!out.newline));
                            out.printNewline();
                        }
                        else
                        {
                            out.printLeftBrace(NodeWriter.NEWLINE_NO,
                                               NodeWriter.NEWLINE_NO);
                            printCommentsAfter(next, NodeWriter.NEWLINE_NO,
                                               NodeWriter.NEWLINE_YES, out);
                        }

                        PrinterFactory.create(block).print(block, out);
                        out.printRightBrace();
                    }
                    else
                    {
                        printCommentsAfter(next, NodeWriter.NEWLINE_NO,
                                           NodeWriter.NEWLINE_NO, out);
                        out.printNewline();
                        out.indent();
                        PrinterFactory.create(block).print(block, out);
                        out.unindent();
                    }
            }
        }

        // do as if always braces printed for the correct blank lines behaviour
        out.last = JavaTokenTypes.RCURLY;
    }


    /**
     * Determines if an endline comment will be printed before the given else
     * node.
     *
     * @param elseNode else node to check for endline comments before.
     *
     * @return <code>true</code> if an endline comment will be printed before
     *         the given else node.
     */
    private boolean isCommentBefore(JavaNode elseNode)
    {
        JavaNode slist = elseNode.getPreviousSibling();
        JavaNode rcurly = null;

        for (AST child = slist.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.RCURLY :

                    if (child.getNextSibling() == null)
                    {
                        rcurly = (JavaNode)child;
                    }

                    break;
            }
        }

        if (rcurly != null)
        {
            return rcurly.hasCommentsAfter();
        }
        else
        {
            return false;
        }
    }
}
