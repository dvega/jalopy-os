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
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for the exception specification of methods and constructors
 * [<code>LITERAL_throws</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ThrowsPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ThrowsPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ThrowsPrinter object.
     */
    protected ThrowsPrinter()
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
        out.indent();

        AST firstClause = node.getFirstChild();

        boolean newlineAfterThrows = this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_THROWS,
                                                           Defaults.LINE_WRAP_AFTER_THROWS);

        // null means empty imaginary node
        if (firstClause != null)
        {
            Marker marker = null;

            boolean wrapLines = this.prefs.getBoolean(Keys.LINE_WRAP,
                                                      Defaults.LINE_WRAP) &&
                                (out.mode == NodeWriter.MODE_DEFAULT);
            int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                               Defaults.LINE_LENGTH);
            int deepIndent = this.prefs.getInt(Keys.INDENT_SIZE_DEEP,
                                               Defaults.INDENT_SIZE_DEEP);
            int indentLength = out.getIndentLength();

            if ((out.mode == NodeWriter.MODE_DEFAULT) &&
                (out.newline || this.prefs.getBoolean(
                                                      Keys.LINE_WRAP_BEFORE_THROWS,
                                                      Defaults.LINE_WRAP_BEFORE_THROWS) ||
                 (wrapLines && exceedsBarriers(firstClause, lineLength,
                                               deepIndent, out))))
            {
                if (!out.newline)
                {
                    out.printNewline();
                }

                if (newlineAfterThrows)
                {
                    out.state.extraWrap = true;
                }

                int indentSize = this.prefs.getInt(Keys.INDENT_SIZE_THROWS,
                                                   Defaults.INDENT_SIZE_THROWS);

                if (indentSize > -1) // use custom indentation
                {
                    out.print(out.getString(indentSize), JavaTokenTypes.WS);
                    out.print(THROWS_SPACE, JavaTokenTypes.LITERAL_throws);
                    marker = out.state.markers.add();
                }
                else
                {
                    if (this.prefs.getBoolean(Keys.INDENT_DEEP,
                                              Defaults.INDENT_DEEP) &&
                        canAlign(firstClause, lineLength, deepIndent, out))
                    {
                        marker = out.state.markers.getLast();

                        // shift the throws to the left so that the
                        // exception type(s) align with the param expressions
                        out.print(out.getString((marker.column - indentLength) -
                                                7), JavaTokenTypes.WS);
                        out.print(THROWS_SPACE, JavaTokenTypes.LITERAL_throws);
                    }
                    else // use standard indentation
                    {
                        out.print(THROWS_SPACE, JavaTokenTypes.LITERAL_throws);
                        marker = out.state.markers.add();
                    }
                }
            }
            else // print directly after parameters
            {
                out.print(SPACE_THROWS_SPACE, JavaTokenTypes.LITERAL_throws);
                marker = out.state.markers.add();
            }

            String indentation = out.getString(marker.column - indentLength);
            boolean spaceAfterComma = this.prefs.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                            Defaults.SPACE_AFTER_COMMA);
            boolean forceWrapping = this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_TYPES_THROWS,
                                                          Defaults.LINE_WRAP_AFTER_TYPES_THROWS);
            TestNodeWriter tester = null;

            if (wrapLines)
            {
                tester = out.testers.get();
            }

            for (AST child = firstClause;
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.COMMA :
                        out.print(COMMA, JavaTokenTypes.COMMA);

                        if (spaceAfterComma)
                        {
                            out.print(SPACE, JavaTokenTypes.WS);
                        }

                        if (forceWrapping)
                        {
                            out.printNewline();
                            printIndentation(out);

                            //out.print(indentation, JavaTokenTypes.WS);
                            if (newlineAfterThrows)
                            {
                                out.state.extraWrap = true;
                            }
                        }
                        else if (wrapLines)
                        {
                            AST next = child.getNextSibling();

                            if (next != null)
                            {
                                PrinterFactory.create(next).print(next, tester);

                                if ((tester.length + out.column) > lineLength)
                                {
                                    out.printNewline();

                                    //out.print(indentation, JavaTokenTypes.WS);
                                    printIndentation(out);

                                    if (newlineAfterThrows)
                                    {
                                        out.state.extraWrap = true;
                                    }
                                }

                                tester.reset();
                            }
                        }

                        break;

                    default :
                        PrinterFactory.create(child).print(child, out);

                        break;
                }
            }

            if (tester != null)
            {
                out.testers.release(tester);
            }
        }

        out.unindent();
    }


    /**
     * Determines whether the given node (denoting the first type name of the
     * throws clause) can be aligned with the parameter list of the method
     * declaration.
     *
     * @param node the first type.
     * @param lineLength the maximum line length.
     * @param deepIndent deep indent setting.
     * @param out stream to write to.
     *
     * @return <code>true</code> if the type name can be aligned.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b7
     */
    private boolean canAlign(AST        node,
                             int        lineLength,
                             int        deepIndent,
                             NodeWriter out)
        throws IOException
    {
        TestNodeWriter tester = out.testers.get();
        PrinterFactory.create(node).print(node, tester);

        Marker marker = out.state.markers.getLast();

        if (((marker.column - 7) < deepIndent) &&
            ((marker.column + tester.length) < lineLength) &&
            ((marker.column - out.getIndentLength() - 7) > 0))
        {
            out.testers.release(tester);

            return true;
        }

        out.testers.release(tester);

        return false;
    }


    /**
     * Determines whether the given node (denoting the first type name of the
     * throws clause) would exceed one of the wrapping barriers without
     * wrapping.
     *
     * @param node a type.
     * @param lineLength the maximal line length.
     * @param deepIndent the deepIndent size.
     * @param out stream to write to.
     *
     * @return <code>true</code> if the node would exceed either one of the
     *         barriers.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b7
     */
    private boolean exceedsBarriers(AST        node,
                                    int        lineLength,
                                    int        deepIndent,
                                    NodeWriter out)
        throws IOException
    {
        if ((out.column + 1) > deepIndent)
        {
            return true;
        }

        TestNodeWriter tester = out.testers.get();
        PrinterFactory.create(node).print(node, tester);

        if ((out.column + 7 + tester.length) > lineLength)
        {
            out.testers.release(tester);

            return true;
        }

        out.testers.release(tester);

        return false;
    }
}
