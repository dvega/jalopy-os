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
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Base class for operator printers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class OperatorPrinter
    extends AbstractPrinter
{
    //~ Constructors ··························································

    /**
     * Creates a new OperatorPrinter object.
     */
    protected OperatorPrinter()
    {
    }

    //~ Methods ·······························································

    /**
     * Performs a line wrap, if necessary.
     *
     * @param length the length of the text that will be printed.
     * @param node the operator node
     * @param wrapBefore <code>true</code> indicates that the line wrap is
     *        performed before the operator, <code>false</code> means
     *        operator the operator.
     * @param paddOperator <code>true</code> indicates that operator padding
     *        is enabled.
     * @param out stream to write to.
     *
     * @return <code>true</code> if a line wrap was performed.
     *
     * @throws IOException if an I/O exception occured.
     */
    protected boolean performWrap(int        length,
                                  AST        node,
                                  boolean    wrapBefore,
                                  boolean    paddOperator,
                                  NodeWriter out)
        throws IOException
    {
        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            /**
             * @todo respect operator length if wrapped after
             */
            int offset = out.column + length;

            int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                               Defaults.LINE_LENGTH);

            if ((out.column >= lineLength) || (offset > lineLength))
            {
                out.printNewline();

                if (this.prefs.getBoolean(Keys.INDENT_CONTINUATION_OPERATOR,
                                          Defaults.INDENT_CONTINUATION_OPERATOR))
                {
                    printIndentation(out.continuationIndentSize, out);
                }
                else
                {
                    printIndentation(out);
                }

                return true;
            }
        }

        if (paddOperator)
        {
            out.print(SPACE, JavaTokenTypes.WS);
        }

        return false;
    }


    /**
     * Prints the left-hand side of the given assignment.
     *
     * @param node an ASSIGN node.
     * @param out stream to write to.
     *
     * @return the first node of the right-hand side for the assignment.
     *
     * @throws IOException if an I/O occured.
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @since 1.0b9
     */
    protected AST printLeftHandSide(AST        node,
                                    NodeWriter out)
        throws IOException
    {
        AST first = node.getFirstChild();

        switch (first.getType())
        {
            case JavaTokenTypes.LPAREN :

                int count = 0; // number of enclosing parentheses

                for (AST child = first;
                     child != null;
                     child = child.getNextSibling())
                {
                    PrinterFactory.create(child).print(child, out);

                    switch (child.getType())
                    {
                        case JavaTokenTypes.LPAREN :
                            count++;

                            break;

                        case JavaTokenTypes.RPAREN :
                            count--;

                            // we've printed the last right parenthesis
                            if (count == 0)
                            {
                                return child.getNextSibling();
                            }

                            break;
                    }
                }

                throw new IllegalStateException("missing closing parenthesis");

            default :
                PrinterFactory.create(first).print(first, out);

                return first.getNextSibling();
        }
    }


    /**
     * Prints the right-hand side of an assignment.
     *
     * @param node the first node of the right-hand side.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O occured.
     *
     * @since 1.0b9
     */
    protected void printRightHandSide(AST        node,
                                      NodeWriter out)
        throws IOException
    {
        for (AST child = node; child != null; child = child.getNextSibling())
        {
            PrinterFactory.create(child).print(child, out);
        }
    }


    /**
     * Indicates whether the given operator node is at a lower level that some
     * containing expression statement, i.e. the given node has at least one
     * child that is itself an operator node.
     *
     * @param operator the operator node to check.
     *
     * @return <code>true</code> if the given node is a lower level node.
     */
    static boolean isLowerLevel(AST operator)
    {
        for (AST child = operator.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.BXOR_ASSIGN :
                case JavaTokenTypes.BAND_ASSIGN :
                case JavaTokenTypes.BSR_ASSIGN :
                case JavaTokenTypes.SR_ASSIGN :
                case JavaTokenTypes.SL_ASSIGN :
                case JavaTokenTypes.MINUS_ASSIGN :
                case JavaTokenTypes.PLUS_ASSIGN :
                case JavaTokenTypes.MOD_ASSIGN :
                case JavaTokenTypes.DIV_ASSIGN :
                case JavaTokenTypes.STAR_ASSIGN :
                case JavaTokenTypes.ASSIGN :
                case JavaTokenTypes.COLON :
                case JavaTokenTypes.QUESTION :
                case JavaTokenTypes.LOR :
                case JavaTokenTypes.LAND :
                case JavaTokenTypes.BOR :
                case JavaTokenTypes.BXOR :
                case JavaTokenTypes.BAND :
                case JavaTokenTypes.NOT_EQUAL :
                case JavaTokenTypes.EQUAL :
                case JavaTokenTypes.GE :
                case JavaTokenTypes.LE :
                case JavaTokenTypes.GT :
                case JavaTokenTypes.LT :
                case JavaTokenTypes.SR :
                case JavaTokenTypes.SL :
                case JavaTokenTypes.MINUS :
                case JavaTokenTypes.PLUS :
                case JavaTokenTypes.MOD :
                case JavaTokenTypes.DIV :
                case JavaTokenTypes.STAR :
                case JavaTokenTypes.LNOT :
                case JavaTokenTypes.BNOT :
                case JavaTokenTypes.UNARY_MINUS :
                case JavaTokenTypes.UNARY_PLUS :
                case JavaTokenTypes.DEC :
                case JavaTokenTypes.INC :
                    return true;
            }
        }

        return false;
    }


    /**
     * Returns the precedence level of the given node as an int value. Higher
     * values means a higher precedence level.
     *
     * @param node a node.
     *
     * @return the precedence level of the given node. If the given node does
     *         not denote an operator node, this method always returns
     *         <code>0</code>.
     *
     * @since 1.0b9
     */
    int getPrecedence(AST node)
    {
        int result = 0;

        // the following switch is clearly modeled
        // after the Java operator precedences table:
        //
        // lowest
        // (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
        // (12)  ?:
        // (11)  ||
        // (10)  &&
        // ( 9)  |
        // ( 8)  ^
        // ( 7)  &
        // ( 6)  == !=
        // ( 5)  < <= > >=
        // ( 4)  << >>
        // ( 3)  +(binary) -(binary)
        // ( 2)  * / %
        // ( 1)  ++ -- +(unary) -(unary) ~ ! (type)
        // highest
        switch (node.getType())
        {
            case JavaTokenTypes.BOR_ASSIGN :
                result = 1;

                break;

            case JavaTokenTypes.BXOR_ASSIGN :
                result = 2;

                break;

            case JavaTokenTypes.BAND_ASSIGN :
                result = 3;

                break;

            case JavaTokenTypes.BSR_ASSIGN :
                result = 4;

                break;

            case JavaTokenTypes.SR_ASSIGN :
                result = 5;

                break;

            case JavaTokenTypes.SL_ASSIGN :
                result = 6;

                break;

            case JavaTokenTypes.MINUS_ASSIGN :
                result = 7;

                break;

            case JavaTokenTypes.PLUS_ASSIGN :
                result = 8;

                break;

            case JavaTokenTypes.MOD_ASSIGN :
                result = 9;

                break;

            case JavaTokenTypes.DIV_ASSIGN :
                result = 10;

                break;

            case JavaTokenTypes.STAR_ASSIGN :
                result = 11;

                break;

            case JavaTokenTypes.ASSIGN :
                result = 12;

                break;

            case JavaTokenTypes.COLON :
                result = 13;

                break;

            case JavaTokenTypes.QUESTION :
                result = 14;

                break;

            case JavaTokenTypes.LOR :
                result = 15;

                break;

            case JavaTokenTypes.LAND :
                result = 16;

                break;

            case JavaTokenTypes.BOR :
                result = 17;

                break;

            case JavaTokenTypes.BXOR :
                result = 18;

                break;

            case JavaTokenTypes.BAND :
                result = 19;

                break;

            case JavaTokenTypes.NOT_EQUAL :
                result = 20;

                break;

            case JavaTokenTypes.EQUAL :
                result = 21;

                break;

            case JavaTokenTypes.GE :
                result = 22;

                break;

            case JavaTokenTypes.LE :
                result = 23;

                break;

            case JavaTokenTypes.GT :
                result = 24;

                break;

            case JavaTokenTypes.LT :
                result = 25;

                break;

            case JavaTokenTypes.SR :
                result = 26;

                break;

            case JavaTokenTypes.SL :
                result = 27;

                break;

            case JavaTokenTypes.MINUS :
                result = 28;

                break;

            case JavaTokenTypes.PLUS :
                result = 29;

                break;

            case JavaTokenTypes.MOD :
                result = 30;

                break;

            case JavaTokenTypes.DIV :
                result = 31;

                break;

            case JavaTokenTypes.STAR :
                result = 32;

                break;

            case JavaTokenTypes.LNOT :
                result = 33;

                break;

            case JavaTokenTypes.BNOT :
                result = 34;

                break;

            /*case JavaTokenTypes.UNARY_MINUS:
                result = 35;
                break;
            case JavaTokenTypes.UNARY_PLUS:
                result = 36;
                break;
            case JavaTokenTypes.DEC :
                result = 37;
                break;
            case JavaTokenTypes.INC :
                result = 38;
                break;*/
        }

        return result;
    }


    /**
     * Indicates whether the given operator may cause a line wrap.
     *
     * @param operator operator node.
     * @param out stream to write to.
     *
     * @return <code>true</code> if the given operator can cause a line wrap.
     */
    static boolean canWrap(AST        operator,
                           NodeWriter out)
    {
        if (out.mode != NodeWriter.MODE_DEFAULT)
        {
            return false;
        }

        boolean isOutMost = out.state.paramLevel < 2;

        if (isOutMost || isLowerLevel(operator))
        {
            switch (operator.getType())
            {
                // Mathematical operators
                case JavaTokenTypes.PLUS :
                case JavaTokenTypes.MINUS :

                // Logical operators
                case JavaTokenTypes.LAND :
                case JavaTokenTypes.LOR :

                // Bitwise operators
                case JavaTokenTypes.BAND :
                case JavaTokenTypes.BOR :
                case JavaTokenTypes.BXOR :

                // Prefix operators
                case JavaTokenTypes.BNOT :
                    return true;
            }
        }

        return false;
    }


    boolean isTopMost(JavaNode node)
    {
        JavaNode parent = node.getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.EXPR :
                return true;
        }

        return false;
    }


    /**
     * Prints the given operator node and adds enclosing parentheses.
     *
     * @param node an operator node.
     * @param out stream to write to.
     *
     * @return <code>true</code> if an endline comment was printed behind the
     *         closing parenthesis.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    boolean printWithParentheses(JavaNode   node,
                                 NodeWriter out)
        throws IOException
    {
        JavaNode n = NodeHelper.getLastChild(node);

        if ((n == null) && n.hasCommentsAfter())
        {
            LeftParenthesisPrinter.getInstance().print(out);

            if (!isTopMost(node))
            {
                Marker marker = out.state.markers.add();
                out.state.paramLevel++;
                PrinterFactory.create(node).print(node, out);
                out.state.paramLevel--;
                out.state.markers.remove(marker);
            }
            else
            {
                PrinterFactory.create(node).print(node, out);
            }

            RightParenthesisPrinter.getInstance().print(out);

            return false;
        }
        else
        {
            // the last node contains an endline comment, so we have to
            // print the closing parenthesis before the comment
            CommonHiddenStreamToken t = n.getHiddenAfter();
            n.setHiddenAfter(null);

            LeftParenthesisPrinter.getInstance().print(out);

            if (!isTopMost(node))
            {
                Marker marker = out.state.markers.add();
                out.state.paramLevel++;
                PrinterFactory.create(node).print(node, out);
                out.state.paramLevel--;
                out.state.markers.remove(marker);
                RightParenthesisPrinter.getInstance().print(out);
            }
            else
            {
                PrinterFactory.create(node).print(node, out);
            }

            n.setHiddenAfter(t);
            printCommentsAfter(n, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO,
                               out);

            return true;
        }
    }
}
