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
 * Base class for infix operator printers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class InfixOperatorPrinter
    extends OperatorPrinter
{
    //~ Constructors ··························································

    /**
     * Creates a new InfixOperatorPrinter object.
     */
    protected InfixOperatorPrinter()
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
        print(node, true, out);
    }


    /**
     * Outputs the given node to the given stream.
     *
     * @param node node to print.
     * @param paddOperator if <code>true</code> a space will be printed before
     *        and after the operator.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    public void print(AST        node,
                      boolean    paddOperator,
                      NodeWriter out)
        throws IOException
    {
        boolean shouldWrap = this.prefs.getBoolean(Keys.LINE_WRAP,
                                                   Defaults.LINE_WRAP);
        boolean wrapLines = false; // actually perform line wrapping
        boolean insertLeft = false; // insert parentheses for the lhs expression
        boolean insertRight = false; // insert parentheses for the rhs expression

        AST lhs = null;
        AST next = null;

ITERATE:
        for (AST child = node.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.LPAREN :

                    // grouping parentheses already present, to simplify
                    // printing we treat this case equal to manual insertion
                    insertLeft = true;

                    break;

                default :
                    lhs = child;

                    for (AST c = child.getNextSibling();
                         c != null;
                         c = c.getNextSibling())
                    {
                        switch (c.getType())
                        {
                            case JavaTokenTypes.RPAREN :
                                break;

                            default :
                                next = c;

                                break ITERATE;
                        }
                    }

                    break;
            }
        }

        AST rhs = null;

        for (AST child = next; child != null; child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.LPAREN :
                    insertRight = true;

                    break;

                case JavaTokenTypes.RPAREN :
                    break;

                default :
                    rhs = child;

                    break;
            }
        }

        if (insertLeft && insertRight)
        {
            // only perform line wrapping for certain operators
            wrapLines = canWrap(node, out);

            print((JavaNode)node, (JavaNode)lhs, (JavaNode)rhs,
                  shouldWrap && wrapLines, paddOperator, true, true, out);
        }
        else
        {
            boolean insertExpressionParentheses = this.prefs.getBoolean(Keys.INSERT_EXPRESSION_PARENTHESIS,
                                                                        Defaults.INSERT_EXPRESSION_PARENTHESIS);

            // should we add parentheses to make precedence obvious?
            if (insertExpressionParentheses)
            {
                if (!insertLeft)
                {
                    insertLeft = getPrecedence(lhs) > getPrecedence(node);
                }

                if (!insertRight)
                {
                    insertRight = getPrecedence(rhs) > getPrecedence(node);
                }
            }

            // only perform line wrapping for certain operators
            wrapLines = canWrap(node, out);
            print((JavaNode)node, (JavaNode)lhs, (JavaNode)rhs,
                  shouldWrap && wrapLines, paddOperator, insertLeft,
                  insertRight, out);
        }
    }


    /**
     * Does the actual printing.
     *
     * @param operator operator node to print.
     * @param lhs left hand side expression.
     * @param rhs right hand side expression.
     * @param wrapLines if <code>true</code> performs line wrapping, if
     *        necessary.
     * @param paddOperator if <code>true</code> adds padding whitespace around
     *        the operator.
     * @param insertLeftParen if <code>true</code> inserts parentheses for the
     *        lhs expression.
     * @param insertRightParen if <code>true</code> inserts parentheses for
     *        the rhs expression.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void print(JavaNode   operator,
                       JavaNode   lhs,
                       JavaNode   rhs,
                       boolean    wrapLines,
                       boolean    paddOperator,
                       boolean    insertLeftParen,
                       boolean    insertRightParen,
                       NodeWriter out)
        throws IOException
    {
        switch (operator.getType())
        {
            case JavaTokenTypes.PLUS:
                System.err.println(operator + " "  + wrapLines);
            }

        if (insertLeftParen)
        {
            printWithParentheses(lhs, out);
        }
        else
        {
            PrinterFactory.create(lhs).print(lhs, out);
        }

        boolean continuationIndent = this.prefs.getBoolean(Keys.INDENT_CONTINUATION_OPERATOR,
                                                           Defaults.INDENT_CONTINUATION_OPERATOR);

        boolean wrapped = false;

        if (out.newline)
        {
            wrapped = true;

            if (continuationIndent && (!out.continuation))
            {
                printIndentation(out.continuationIndentSize, out);
            }
            else
            {
                printIndentation(out);
            }
        }

        boolean wrapBeforeOperator = this.prefs.getBoolean(Keys.LINE_WRAP_BEFORE_OPERATOR,
                                                           Defaults.LINE_WRAP_BEFORE_OPERATOR);
        boolean commentAfter = operator.hasCommentsAfter();

        // no line wrap before operator means that we maybe have to add
        // whitespace around the operator
        if (!wrapBeforeOperator)
        {
            if (paddOperator)
            {
                if (!wrapped)
                {
                    out.print(SPACE, JavaTokenTypes.WS);
                }

                out.print(operator.getText(), operator.getType());

                if (!wrapLines)
                {
                    out.print(SPACE, JavaTokenTypes.WS);
                }
            }
            else
            {
                out.print(operator.getText(), operator.getType());
            }
        }

        if (commentAfter)
        {
            printCommentsAfter(operator, NodeWriter.NEWLINE_NO,
                               NodeWriter.NEWLINE_YES, out);
            wrapped = true;

            if (continuationIndent && (!out.continuation))
            {
                printIndentation(out.continuationIndentSize, out);
            }
            else
            {
                printIndentation(out);
            }
        }
        else if (wrapLines)
        {
            if (out.state.wrap) // force wrapping for all operators
            {
                switch (operator.getType())
                {
                    case JavaTokenTypes.PLUS :
                    case JavaTokenTypes.MINUS :

                        if (!wrapped)
                        {
                            TestNodeWriter tester = out.testers.get();
                            PrinterFactory.create(rhs).print(rhs, tester);
                            wrapped = performWrap(tester.length, operator,
                                                  wrapBeforeOperator,
                                                  paddOperator, out);
                            out.testers.release(tester);
                        }

                        break;

                    default :
                        out.printNewline();

                        if (continuationIndent && (!out.continuation))
                        {
                            printIndentation(out.continuationIndentSize, out);
                        }
                        else
                        {
                            printIndentation(out);
                        }

                        wrapped = true;

                        break;
                }
            }
            else
            {
                if (!wrapped)
                {
                    TestNodeWriter tester = out.testers.get();
                    PrinterFactory.create(rhs).print(rhs, tester);
                    wrapped = performWrap(tester.length, operator,
                                          wrapBeforeOperator, paddOperator, out);
                    out.testers.release(tester);
                }
            }
        }

        if (wrapBeforeOperator)
        {
            // check whether continuation indentation is appropriate
            if (wrapped && continuationIndent)
            {
                switch (operator.getType())
                {
                    case JavaTokenTypes.LAND :
                    case JavaTokenTypes.LOR :
                        out.print(out.getString(out.continuationIndentSize),
                                  JavaTokenTypes.WS);

                        break;
                }
            }

            if (paddOperator)
            {
                if (!wrapped)
                    out.print(SPACE, JavaTokenTypes.WS);

                out.print(operator.getText(),
                          operator.getType());
                out.print(SPACE, JavaTokenTypes.WS);
            }
            else
            {
                out.print(operator.getText(), operator.getType());
            }
        }
        else
        {
            // check whether continuation indentation is appropriate
            if (wrapped && out.continuation)
            {
                switch (operator.getType())
                {
                    case JavaTokenTypes.LAND :
                    case JavaTokenTypes.LOR :
                        out.print(out.getString(out.continuationIndentSize),
                                  JavaTokenTypes.WS);

                        break;
                }
            }
        }

        if (insertRightParen)
        {
            printWithParentheses(rhs, out);
        }
        else
        {
            PrinterFactory.create(rhs).print(rhs, out);
        }

        // if the rhs expression is followed by an enline comment, we have to
        // take care that the any non-operator element that follows, will be
        // correctly indented, e.g.
        //
        // String test = "multi" + // comment 1
        //               "line"    // comment 2
        //               ;
        //
        if (rhs.hasCommentsAfter())
        {
            // 'null' means no operator follows
            if (operator.getNextSibling() == null)
            {
                //printIndentation(out);
            }
        }
    }
}
