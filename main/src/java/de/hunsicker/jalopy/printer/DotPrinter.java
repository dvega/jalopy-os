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
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;

import java.io.IOException;


/**
 * Printer for dot separated stuff (like qualified identifiers, chained method
 * calls...) [<code>DOT</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class DotPrinter
    extends OperatorPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new DotPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new DotPrinter object.
     */
    protected DotPrinter()
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
        AST rhs = printLeftHandSide(node, out);

        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            /**
             * @todo add switch to disable wrapping along dots alltogether
             */
            boolean wrapLines = this.settings.getBoolean(Keys.LINE_WRAP,
                                                      Defaults.LINE_WRAP);
            boolean forceWrappingForChainedCalls = this.settings.getBoolean(Keys.LINE_WRAP_AFTER_CHAINED_METHOD_CALL,
                                                                         Defaults.LINE_WRAP_AFTER_CHAINED_METHOD_CALL);

            if (wrapLines || forceWrappingForChainedCalls)
            {
                align(node, out);
            }
        }

        out.print(DOT, JavaTokenTypes.DOT);

        printRightHandSide(rhs, out);
    }


    /**
     * Determines the length of a single method call contained in a method
     * call chain.
     *
     * @param dot the DOT node.
     * @param call the parent of the DOT node, a METHOD_CALL node.
     * @param lastCall the last METHOD_CALL node of the chain (but the first
     *        METHOD_CALL code of the AST tree!).
     * @param testers DOCUMENT ME!
     *
     * @return the length of the chained method call.
     *
     * @throws IOException if an I/O error occurred.
     *
     * @since 1.0b8
     */
    private int getLengthOfChainedCall(AST         dot,
                                       JavaNode    call,
                                       AST         lastCall,
                                       WriterCache testers)
        throws IOException
    {
        TestNodeWriter tester = testers.get();

        if (lastCall != call)
        {
            AST elist = dot.getNextSibling();
            PrinterFactory.create(elist).print(elist, tester);
        }
        else
        {
            AST elist = lastCall.getFirstChild().getNextSibling();
            PrinterFactory.create(elist).print(elist, tester);
        }

        AST child = dot.getFirstChild();

        switch (child.getType())
        {
            case JavaTokenTypes.METHOD_CALL :

                AST next = child.getNextSibling();
                PrinterFactory.create(next).print(next, tester);

                break;

            default : // means the last node in the AST (but the first call in
                      // the chain)
                PrinterFactory.create(child).print(child, tester);

                break;
        }

        // add +1 for the dot
        int result = tester.length + 1;

        testers.release(tester);

        return result;
    }


    /**
     * Aligns the rhs node, if necessary. This is currently only implemented
     * for chained method calls like
     * <code>scrollPane.getViewport().setBackground(Color.red)</code> or
     * <code>resultSetRow[i].field[0].substring(0, 2)</code>.
     *
     * @param node a DOT node.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b7
     */
    private void align(AST        node,
                       NodeWriter out)
        throws IOException
    {
        ParenthesesScope scope = (ParenthesesScope)out.state.parenScope.getFirst();

        boolean continuationIndent = this.settings.getBoolean(
                                                    Keys.INDENT_CONTINUATION_OPERATOR,
                                                    Defaults.INDENT_CONTINUATION_OPERATOR);



        // was a chained call detected in the current scope?
        // (the detection happens in MethodCallPrinter.java)
        if (scope.chainCall != null)
        {
            JavaNode parent = ((JavaNode)node).getParent();

            switch (parent.getType())
            {
                //case JavaTokenTypes.INDEX_OP:
                case JavaTokenTypes.METHOD_CALL :
                    boolean align = this.settings.getBoolean(Keys.ALIGN_METHOD_CALL_CHAINS,
                                                          Defaults.ALIGN_METHOD_CALL_CHAINS);

                    if (parent != scope.chainCall)
                    {
                        // force wrap after each call?
                        if (this.settings.getBoolean(
                                                  Keys.LINE_WRAP_AFTER_CHAINED_METHOD_CALL,
                                                  Defaults.LINE_WRAP_AFTER_CHAINED_METHOD_CALL))
                        {
                            if (MethodCallPrinter.isOuterMethodCall(parent))
                            {
                                // simply wrap and align all chained calls
                                // under the first one
                                out.printNewline();


                                int indentLength = out.getIndentLength();

                                if (align)
                                {
                                    out.print(out.getString((scope.chainOffset > indentLength)
                                                                ? (scope.chainOffset - indentLength)
                                                                : scope.chainOffset),
                                              JavaTokenTypes.WS);
                                }
                                else if (continuationIndent)
                                {
                                    out.continuation = true;
                                    printIndentation(out);
                                }
                                else
                                {
                                    printIndentation(out);
                                }

                                return;
                            }
                        }
                        else
                        {
                            int lineLength = this.settings.getInt(Keys.LINE_LENGTH,
                                                               Defaults.LINE_LENGTH);

                            // we're already beyond the maximal line length,
                            // time to wrap
                            if (out.column > lineLength)
                            {
                                out.printNewline();


                                if (continuationIndent)
                                    out.continuation = true;

                                indent(align, scope, out);

                                return;
                            }

                            AST first = MethodCallPrinter.getLastMethodCall(parent);

                            // if this is the last node in the chain
                            if (first == parent)
                            {
                                AST c = node.getFirstChild().getNextSibling();

                                TestNodeWriter tester = out.testers.get();
                                PrinterFactory.create(c).print(c, tester);

                                // and it does not exceed the line length
                                if (out.column + tester.length < lineLength)
                                {
                                    out.testers.release(tester);

                                    // prefer wrapping along the parameters
                                    return;
                                }

                                out.testers.release(tester);
                            }

                            int length = getLengthOfChainedCall(node, parent,
                                                                first,
                                                                out.testers);

                            // if this chain element would exceed the maximal
                            // line length, perform wrapping
                            if ((out.column + length) > lineLength)
                            {
                                if (continuationIndent)
                                    out.continuation = true;

                                out.printNewline();

                                indent(align, scope, out);
                            }
                        }
                    }

                    break;
            }
        }
        else  if (this.settings.getBoolean(Keys.LINE_WRAP_BEFORE_OPERATOR, Defaults.LINE_WRAP_BEFORE_OPERATOR))
        {
            switch (((JavaNode)node).getParent().getType())
            {
                case JavaTokenTypes.DOT:
                    break;

                case JavaTokenTypes.METHOD_CALL: // last link of the chain (first in the tree)

                    int lineLength = this.settings.getInt(Keys.LINE_LENGTH,
                                                                   Defaults.LINE_LENGTH);
                    if (out.column  + 1 > lineLength)
                    {
                        out.printNewline();


                        if (continuationIndent)
                            out.continuation = true;

                        printIndentation(out);
                    }
                    else
                    {
                        AST n = node.getFirstChild();

                        switch (n.getType())
                        {
                            case JavaTokenTypes.LPAREN:

                            SEEK_FORWARD:
                            for (AST child = n; n != null; n = n.getNextSibling())
                            {
                                switch (n.getType())
                                {
                                    case JavaTokenTypes.RPAREN:
                                        n = n.getNextSibling();
                                        break SEEK_FORWARD;
                                }
                            }
                            break;
                            default:
                                n = n.getNextSibling();
                                break;

                        }


                        TestNodeWriter tester = out.testers.get();

                        PrinterFactory.create(n).print(n, tester);

                        if (out.column + 1 + tester.length > lineLength)
                        {
                            out.printNewline();

                        if (continuationIndent)
                            out.continuation = true;

                            printIndentation(out);
                        }

                        out.testers.release(tester);
                    }

                break;
            }
        }
    }


    /**
     * Prints the indenation whitespace for wrapped lines.
     *
     * @param align if <code>true</code> enough whitespace will be printed to
     *        align under the '.' of the previous chain member.
     * @param scope curent scope info.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O occured.
     *
     * @since 1.0b9
     */
    private void indent(boolean          align,
                        ParenthesesScope scope,
                        NodeWriter       out)
        throws IOException
    {
        if (align)
        {
            int indentLength = out.getIndentLength();

            out.print(out.getString(((scope.chainOffset > indentLength)
                                         ? (scope.chainOffset - indentLength)
                                         : scope.chainOffset)),
                      JavaTokenTypes.WS);
        }
        else if (out.continuation ||
                 ((!out.continuation) &&
                  this.settings.getBoolean(Keys.INDENT_CONTINUATION_OPERATOR,
                                        Defaults.INDENT_CONTINUATION_OPERATOR)))
        {
            printIndentation(out);
        }
        else
        {
            printIndentation(out);
        }
    }
}
