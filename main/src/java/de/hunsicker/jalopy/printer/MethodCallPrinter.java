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
 * Printer for method calls [<code>METHOD_CALL</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class MethodCallPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new MethodCallPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new MethodCallPrinter object.
     */
    protected MethodCallPrinter()
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
     * Determines the last method call in the given method call chain.
     *
     * @param node the first method call in the method call chain.
     *
     * @return the last element in the chain.
     *
     * @since 1.0b7
     */
    public static JavaNode getLastMethodCall(JavaNode node)
    {
        JavaNode parent = node.getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.DOT :
                return getLastMethodCall(parent.getParent());

            default :
                return node;
        }
    }


    /**
     * {@inheritDoc}
     */
    public void print(AST        node,
                      NodeWriter out)
        throws IOException
    {
        // we need to keep track of the current continuation indentation state
        boolean continuation = out.continuation;

        boolean lastInChain = false;
        ParenthesesScope scope = null;

        AST first = node.getFirstChild();

        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            boolean wrapLines = this.settings.getBoolean(Keys.LINE_WRAP,
                                                      Defaults.LINE_WRAP);
            boolean forceWrappingForChainedCalls = this.settings.getBoolean(Keys.LINE_WRAP_AFTER_CHAINED_METHOD_CALL,
                                                                         Defaults.LINE_WRAP_AFTER_CHAINED_METHOD_CALL);

            if ((wrapLines || forceWrappingForChainedCalls) &&
                NodeHelper.isChained(first))
            {
                AST firstLink = NodeHelper.getFirstChainLink(node);
                scope = (ParenthesesScope)out.state.parenScope.getFirst();

                // if no chained call for the current parentheses scope exists,
                // determine the offset of the first 'dot' and store it
                if (scope.chainCall == null)
                {
                    // store the node so wrapping can be performed
                    // (it will be done in DotPrinter.java)
                    scope.chainCall = firstLink;

                    AST child = firstLink.getFirstChild();

                    switch (child.getType())
                    {
                        // means qualified name, align under the last dot
                        case JavaTokenTypes.DOT :
                        {
                            TestNodeWriter tester = out.testers.get();
                            AST identifier = child.getFirstChild();
                            PrinterFactory.create(identifier)
                                          .print(identifier, tester);
                            scope.chainOffset = out.column - 1 + tester.length;
                            out.testers.release(tester);

                            break;
                        }

                        // no qualification, standard indent
                        default :
                        {
                            scope.chainOffset = out.column - 1 + out.indentSize;

                            break;
                        }
                    }

                    if (out.newline)
                    {
                        /**
                         * @todo maybe we need to take care of the markers
                         *       here?
                         */
                        scope.chainOffset += out.getIndentLength();
                    }

                    lastInChain = true;
                }
            }
        }

        // did we print an anonymous inner class?
        boolean innerClass = false;
        Marker marker = null;

        JavaNode parent = ((JavaNode)node).getParent();

        switch (parent.getType())
        {
            // add the marker just before the method call
            default :
                marker = out.state.markers.add();

                break;

            // we can't place the marker just before the method call because
            // then we would align our parameters too deeply. Instead we have
            // to determine the length of the type cast and substract it from
            // the current column position in order to place the marker
            // *before* the type cast
            //
            //      (JDialog)SwingUtilities.getWindowAncestor(
            //              ^            this),
            //
            //      (JDialog)SwingUtilities.getWindowAncestor(
            //      ^           this),
            case JavaTokenTypes.TYPECAST :

                AST next = parent.getFirstChild();
                AST type = null;

                /**
                 * @todo handle enclosing parentheses properly
                 */
                switch (next.getType())
                {
                    case JavaTokenTypes.LPAREN :
                        type = PrinterHelper.advanceToFirstNonParen(next);

                        break;

                    default :
                        type = next;

                        break;
                }

                AST identifier = type.getFirstChild();
                int length = 0;

                switch (identifier.getType())
                {
                    case JavaTokenTypes.IDENT :
                        length = identifier.getText().length();

                        break;

                    case JavaTokenTypes.ARRAY_DECLARATOR :
                        length = identifier.getFirstChild().getText().length();

                        if (this.settings.getBoolean(
                                                  Keys.SPACE_BEFORE_BRACKETS_TYPES,
                                                  Defaults.SPACE_BEFORE_BRACKETS_TYPES))
                        {
                            length += 1;
                        }

                        break;

                    case JavaTokenTypes.LITERAL_boolean :
                        length = 7;

                        break;

                    case JavaTokenTypes.LITERAL_char :
                    case JavaTokenTypes.LITERAL_long :
                    case JavaTokenTypes.LITERAL_byte :
                        length = 4;

                        break;

                    case JavaTokenTypes.LITERAL_float :
                    case JavaTokenTypes.LITERAL_short :
                        length = 5;

                        break;

                    case JavaTokenTypes.LITERAL_int :
                        length = 3;

                        break;

                    case JavaTokenTypes.LITERAL_double :
                        length = 6;

                        break;

                    default :
                        throw new RuntimeException("unexpected TYPE, was " +
                                                   type);
                }

                if (this.settings.getBoolean(Keys.PADDING_CAST,
                                          Defaults.PADDING_CAST))
                {
                    length += 2;
                }

                if (this.settings.getBoolean(Keys.SPACE_AFTER_CAST,
                                          Defaults.SPACE_AFTER_CAST))
                {
                    length += 1;
                }

                // -3 because 1 for the usual 'step one back'
                //            2 for the parentheses
                marker = out.state.markers.add(out.line,
                                               (out.column - 3) - length);

                break;
        }

        logIssues(node, out);

        PrinterFactory.create(first).print(first, out);

        LeftParenthesisPrinter.getInstance().print(node, out);

        AST elist = first.getNextSibling();

        if (this.settings.getBoolean(Keys.SPACE_BEFORE_METHOD_CALL_PAREN,
                                  Defaults.SPACE_BEFORE_METHOD_CALL_PAREN))
        {
            out.print(SPACE, JavaTokenTypes.WS);
        }

        PrinterFactory.create(elist).print(elist, out);

        // another trick to track inner class definitions
        if (out.last == JavaTokenTypes.CLASS_DEF)
        {
            innerClass = true;
        }

        AST rparen = elist.getNextSibling();
        PrinterFactory.create(rparen).print(rparen, out);

        // for the correct blank lines behaviour: we want blank lines
        // after inner classes but not after method calls
        if (innerClass)
        {
            out.last = JavaTokenTypes.CLASS_DEF;
        }

        out.state.markers.remove(marker);

        if (lastInChain && (scope != null))
        {
            scope.chainCall = null;
        }

        // reset continuation indentation if necessary (it could have been set
        // in DotPrinter.java)
        if (out.continuation && !continuation)
        {
            out.continuation = false;
        }
    }


    /**
     * Determines the topmost parent of the given expression node (contained
     * in a chained method call).
     *
     * @param node an EXPR node.
     *
     * @return the root node of the chain.
     *
     * @since 1.0b8
     */
    static AST getChainParent(JavaNode node)
    {
        JavaNode parent = node.getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.EXPR :

            /**
             * @todo maybe more operators make sense?
             */
            case JavaTokenTypes.PLUS :

                //case JavaTokenTypes.NOT_EQUAL:
                //case JavaTokenTypes.EQUAL:
                return getChainParent(parent);

            default :
                return parent;
        }
    }


    /**
     * Determines wether the given method call is the outmost method call or
     * contained within another method call chain.
     *
     * @param node a METHOD_CALL node.
     *
     * @return <code>true</code> if the given method call is the outmost
     *         method call.
     *
     * @since 1.0b7
     */
    static boolean isOuterMethodCall(AST node)
    {
        JavaNode call = getLastMethodCall((JavaNode)node);

        if (call.getPreviousSibling().getType() == JavaTokenTypes.LPAREN)
        {
            return false;
        }

        JavaNode expr = call.getParent();

        // if the topmost parent is no ELIST we know we can savely wrap and
        // align
        return getChainParent(expr).getType() != JavaTokenTypes.ELIST;
    }
}
