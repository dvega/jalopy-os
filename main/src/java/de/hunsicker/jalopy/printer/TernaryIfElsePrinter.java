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

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for ternary if-else expressions (conditional operator <code>?
 * :</code>) [<code>QUESTION</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class TernaryIfElsePrinter
    extends OperatorPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new TernaryIfElsePrinter();

    //~ Constructors ··························································

    /**
     * Creates a new TernaryIfPrinter object.
     */
    private TernaryIfElsePrinter()
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
        Marker marker = out.state.markers.add();

        // print first operand
        AST secondOperand = printOperand(node.getFirstChild(), marker, out);

        printQuestionMark(node, secondOperand, marker, out);

        // print the second operand
        JavaNode colon = (JavaNode)printOperand(secondOperand, marker, out);

        printColon(colon, out);

        // print the third operand
        printOperand(colon.getNextSibling(), marker, out);

        out.state.markers.remove(marker);
    }


    /**
     * Gets the next operand of the ternary expression.
     *
     * @param node the node of the ternary expression. Either LPAREN, EXPR or
     *        an operator.
     *
     * @return the next operand of the ternary expression.
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     *
     * @since 1.b9
     */
    private JavaNode getNextOperand(AST node)
    {
        for (AST child = node; child != null; child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.LPAREN :
                    break;

                default :
                    return (JavaNode)child;
            }
        }

        throw new IllegalArgumentException("not part of operand -- " + node);
    }


    /**
     * Determines whether the given node needs parentheses to make expression
     * precedence clear.
     *
     * @param node operand node to check.
     *
     * @return <code>true</code> if the operand needs parentheses.
     *
     * @since 1.0b8
     */
    private boolean needParentheses(JavaNode node)
    {
        switch (node.getType())
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
            case JavaTokenTypes.LITERAL_instanceof :
                return true;
        }

        return false;
    }


    /**
     * Prints the colon.
     *
     * @param colon COLON node.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b9
     */
    private void printColon(JavaNode   colon,
                            NodeWriter out)
        throws IOException
    {
        if (out.mode == NodeWriter.MODE_TEST)
        {
            return;
        }

        boolean wrapLines = this.prefs.getBoolean(Keys.LINE_WRAP,
                                                  Defaults.LINE_WRAP) &&
                            (out.mode == NodeWriter.MODE_DEFAULT);
        boolean alignValue = this.prefs.getBoolean(Keys.ALIGN_TERNARY_VALUES,
                                                   Defaults.ALIGN_TERNARY_VALUES);
        int indentLength = out.getIndentLength();

        Marker marker = out.state.markers.getLast();

        if (out.column == 1) // line already wrapped, just align
        {
            out.print(out.getString((marker.column > indentLength)
                                        ? (marker.column - indentLength)
                                        : marker.column), JavaTokenTypes.WS);
            out.state.markers.add();
        }
        else if (alignValue) // force line wrap/align
        {
            out.printNewline();
            out.print(out.getString((marker.column > indentLength)
                                        ? (marker.column - indentLength)
                                        : marker.column), JavaTokenTypes.WS);
            out.state.markers.add();
        }
        else if (wrapLines) // check whether wrap/align necessary
        {
            TestNodeWriter tester = out.testers.get();

            AST thirdOp = getNextOperand(colon.getNextSibling());
            PrinterFactory.create(thirdOp).print(thirdOp, tester);

            // only wrap and align if necessary
            if ((tester.length + out.column) > this.prefs.getInt(
                                                                 Keys.LINE_LENGTH,
                                                                 Defaults.LINE_LENGTH))
            {
                out.printNewline();
                out.print(out.getString((marker.column > indentLength)
                                            ? (marker.column - indentLength)
                                            : marker.column), JavaTokenTypes.WS);
                out.state.markers.add();
            }
            else
            {
                out.print(SPACE, out.last);
            }

            out.testers.release(tester);
        }
        else // line wrapping disabled
        {
            out.print(SPACE, out.last);
        }

        out.print(COLON, JavaTokenTypes.COLON);

        if ((!printCommentsAfter(colon, NodeWriter.NEWLINE_NO,
                                 NodeWriter.NEWLINE_YES, out)) &&
            (out.column != 1))
        {
            out.print(SPACE, JavaTokenTypes.COLON);
        }
        else
        {
            // add +2 to align the third operand under the second
            out.print(out.getString(((marker.column > indentLength)
                                         ? (marker.column - indentLength)
                                         : marker.column) + 2),
                      JavaTokenTypes.WS);
        }
    }


    /**
     * Prints an operand of the ternary expression.
     *
     * @param node the first node of the ternary expression. Either a LPAREN,
     *        EXPR or an operator.
     * @param marker marker that marks the position before the first operand.
     * @param out stream to write to.
     *
     * @return the next operand of the ternary expression. Returns
     *         <code>null</code> if the third operand was printed.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalStateException DOCUMENT ME!
     *
     * @since 1.0b9
     */
    private AST printOperand(AST        node,
                             Marker     marker,
                             NodeWriter out)
        throws IOException
    {
        // does the operand already contain enclosing parentheses?
        boolean parentheses = false;

        for (AST child = node; child != null; child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.LPAREN :
                    parentheses = true;
                    PrinterFactory.create(child).print(child, out);

                    break;

                default :

                    if (parentheses || (node.getFirstChild() == child))
                    {
                        PrinterFactory.create(child).print(child, out);
                    }
                    else if (this.prefs.getBoolean(
                                                   Keys.INSERT_EXPRESSION_PARENTHESIS,
                                                   Defaults.INSERT_EXPRESSION_PARENTHESIS) &&
                             needParentheses((JavaNode)child))
                    {
                        printWithParentheses((JavaNode)child, out);
                    }
                    else
                    {
                        PrinterFactory.create(child).print(child, out);
                    }

                    for (child = child.getNextSibling();
                         child != null;
                         child = child.getNextSibling())
                    {
                        switch (child.getType())
                        {
                            case JavaTokenTypes.RPAREN :
                                PrinterFactory.create(child).print(child, out);

                                break;

                            default :
                                return child;
                        }
                    }

                    // the third operand does not have a sibling
                    return null;
            }
        }

        // should never happen if the tree was correctly build
        throw new IllegalStateException();
    }


    /**
     * Prints the question mark.
     *
     * @param node QUESTION node.
     * @param secondOperand the second operand of the ternary expression.
     * @param marker marker that marks the position before the first operand.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b9
     */
    private void printQuestionMark(AST        node,
                                   AST        secondOperand,
                                   Marker     marker,
                                   NodeWriter out)
        throws IOException
    {
        boolean alignUnderFirst = this.prefs.getBoolean(Keys.ALIGN_TERNARY_EXPRESSION,
                                                        Defaults.ALIGN_TERNARY_EXPRESSION);
        boolean wrapLines = this.prefs.getBoolean(Keys.LINE_WRAP,
                                                  Defaults.LINE_WRAP) &&
                            (out.mode == NodeWriter.MODE_DEFAULT);
        int indentLength = out.getIndentLength();
        int continuationIndent = (this.prefs.getBoolean(Keys.INDENT_CONTINUATION_IF_TERNARY,
                                                        Defaults.INDENT_CONTINUATION_IF_TERNARY))
                                     ? (out.indentSize)
                                     : 0;

        if (out.newline) // line already wrapped, just align
        {
            out.print(out.getString(((marker.column > indentLength)
                                         ? (marker.column - indentLength)
                                         : marker.column) + continuationIndent),
                      JavaTokenTypes.WS);
            marker = out.state.markers.add();
        }
        else if (alignUnderFirst) // force line wrap/align
        {
            out.printNewline();
            out.print(out.getString(((marker.column > indentLength)
                                         ? (marker.column - indentLength)
                                         : marker.column) + continuationIndent),
                      JavaTokenTypes.WS);
            marker = out.state.markers.add();
        }
        else if (wrapLines) // check whether if wrap/align necessary
        {
            TestNodeWriter tester = out.testers.get();

            AST secondOp = getNextOperand(secondOperand);
            PrinterFactory.create(secondOp).print(secondOp, tester);

            AST thirdOp = getNextOperand(secondOp);
            PrinterFactory.create(thirdOp).print(thirdOp, tester);

            // wrap and align necessary (add +3 for the colon between
            // values)
            if ((tester.length + out.column + 3) > this.prefs.getInt(
                                                                     Keys.LINE_LENGTH,
                                                                     Defaults.LINE_LENGTH))
            {
                out.printNewline();
                out.print(out.getString(((marker.column > indentLength)
                                             ? (marker.column - indentLength)
                                             : marker.column) +
                                        continuationIndent), JavaTokenTypes.WS);

                out.state.markers.add();
            }
            else // no line wrap necessary
            {
                out.print(SPACE, out.last);
                marker = out.state.markers.add();
            }

            out.testers.release(tester);
        }
        else // line wrapping disabled
        {
            out.print(SPACE, out.last);
            marker = out.state.markers.add();
        }

        out.print(QUESTION, JavaTokenTypes.QUESTION);

        if ((!printCommentsAfter(node, NodeWriter.NEWLINE_NO,
                                 NodeWriter.NEWLINE_YES, out)) &&
            (out.column != 1))
        {
            out.print(SPACE, JavaTokenTypes.COLON);
        }
        else
        {
            // add +2 to align with the second operand
            out.print(out.getString(((marker.column > indentLength)
                                         ? (marker.column - indentLength)
                                         : marker.column) + 2),
                      JavaTokenTypes.WS);
        }
    }
}
