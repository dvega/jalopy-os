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

import de.hunsicker.antlr.*;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Common superclass for the printers that handle statement blocks.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class BlockStatementPrinter
    extends AbstractPrinter
{
    //~ Constructors ··························································

    /**
     * Creates a new BlockStatementPrinter object.
     */
    protected BlockStatementPrinter()
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
        // special handling if we encounter a label
        if (out.last == JavaTokenTypes.LABELED_STAT)
        {
            // if no newline will be printed after labels
            if (!this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_LABEL,
                                       Defaults.LINE_WRAP_AFTER_LABEL))
            {
                // and we find comments before we have to issue one extra so
                // that the right amount of blank lines is printed
                if (((JavaNode)node).hasCommentsBefore())
                {
                    out.printNewline();
                }
            }
        }

        printCommentsBefore(node, out);
    }

    /**
     * Prints the expression list starting with the given node; the left
     * parenthesis.
     *
     * @param lparen a LPAREN node.
     * @param insertBraces DOCUMENT ME!
     * @param out stream to write to.
     *
     * @return the RPAREN node of the expression list.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b9
     */
    JavaNode printExpressionList(AST        lparen,
                                 boolean    insertBraces,
                                 NodeWriter out)
        throws IOException
    {
        PrinterFactory.create(lparen).print(lparen, out);

        Marker marker = out.state.markers.add();
        TestNodeWriter tester = null;
        AST expr = lparen.getNextSibling();

        boolean wrapped = false; // was line wrapping performed?

        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            out.state.expressionList = true;
            out.state.paramLevel++;
            out.state.parenScope.addFirst(new ParenthesesScope(out.state.paramLevel));

            int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                               Defaults.LINE_LENGTH);

            if (this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_LEFT_PAREN,
                                      Defaults.LINE_WRAP_AFTER_LEFT_PAREN))
            {
                if (!out.newline)
                {
                    tester = out.testers.get();
                    PrinterFactory.create(expr).print(expr, tester);

                    if ((out.column + tester.length) > lineLength)
                    {
                        out.printNewline();
                        printIndentation(out);
                        wrapped = true;

                        if (this.prefs.getBoolean(Keys.LINE_WRAP_ALL,
                                                  Defaults.LINE_WRAP_ALL))
                        {
                            out.state.wrap = true;
                        }
                    }

                    out.testers.release(tester);
                }
                else
                {
                    printIndentation(out);
                    wrapped = true;

                    if (this.prefs.getBoolean(Keys.LINE_WRAP_ALL,
                                              Defaults.LINE_WRAP_ALL))
                    {
                        out.state.wrap = true;
                    }
                }

                tester = out.testers.get();
                PrinterFactory.create(expr).print(expr, tester);
            }

            if (tester == null)
            {
                tester = out.testers.get();
                PrinterFactory.create(expr).print(expr, tester);
            }

            if ((!wrapped) && ((tester.length + out.column) > lineLength))
            {
                if (this.prefs.getBoolean(Keys.LINE_WRAP_ALL,
                                          Defaults.LINE_WRAP_ALL))
                {
                    out.state.wrap = true;
                }

                wrapped = true;
            }

            out.testers.release(tester);
        }

        // use continuation indentation within the parentheses?
        out.continuation = this.prefs.getBoolean(Keys.INDENT_CONTINUATION_IF,
                                                 Defaults.INDENT_CONTINUATION_IF);

        PrinterFactory.create(expr).print(expr, out);

        out.continuation = false;
        out.state.wrap = false;

        if (wrapped &&
            this.prefs.getBoolean(Keys.LINE_WRAP_BEFORE_RIGHT_PAREN,
                                  Defaults.LINE_WRAP_BEFORE_RIGHT_PAREN))
        {
            if (!out.newline)
            {
                out.printNewline();
            }

            if (this.prefs.getBoolean(Keys.INDENT_DEEP, Defaults.INDENT_DEEP))
            {
                printIndentation(-1, out);
            }
            else
            {
                out.print(EMPTY_STRING, JavaTokenTypes.WS);
            }
        }

        JavaNode rparen = (JavaNode)expr.getNextSibling();

        AST body = rparen.getNextSibling();

        boolean hasBraces = (body.getType() == JavaTokenTypes.SLIST);
        boolean leftBraceNewline = this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                                         Defaults.BRACE_NEWLINE_LEFT);

        CommonHiddenStreamToken pendingComment = null;

        if ((!hasBraces) && insertBraces && (!leftBraceNewline))
        {
            out.pendingComment = rparen.getCommentAfter();

            if (out.pendingComment != null)
            {
                rparen.setHiddenAfter(null);
            }
        }

        PrinterFactory.create(rparen).print(rparen, out);

        if (out.mode == out.MODE_DEFAULT)
        {
            out.state.expressionList = false;
            out.state.paramLevel--;
            out.state.parenScope.removeFirst();
        }

        out.state.markers.remove(marker);

        return rparen;
    }
}
