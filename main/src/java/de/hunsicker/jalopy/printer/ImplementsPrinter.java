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
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;

import java.io.IOException;


/**
 * Printer for implements clauses (<code>IMPLEMENTS_CLAUSE</code>).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ImplementsPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ImplementsPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ImplementsPrinter object.
     */
    protected ImplementsPrinter()
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
        AST first = node.getFirstChild();

        if (first != null)
        {
            boolean newlineBefore = this.settings.getBoolean(Keys.LINE_WRAP_BEFORE_IMPLEMENTS,
                                                          Defaults.LINE_WRAP_BEFORE_IMPLEMENTS);
            boolean wrapLines = this.settings.getBoolean(Keys.LINE_WRAP,
                                                      Defaults.LINE_WRAP) &&
                                (out.mode == NodeWriter.MODE_DEFAULT);
            int deepIndentSize = this.settings.getInt(Keys.INDENT_SIZE_DEEP,
                                               Defaults.INDENT_SIZE_DEEP);
            int lineLength = this.settings.getInt(Keys.LINE_LENGTH,
                                               Defaults.LINE_LENGTH);

            boolean indentDeep = this.settings.getBoolean(Keys.INDENT_DEEP, Defaults.INDENT_DEEP);

            out.indent();

            if (out.newline || newlineBefore ||
                (wrapLines &&
                 (((out.column + 1) > deepIndentSize) ||
                  ((out.column + 11 + first.getText().length()) > lineLength))))
            {
                if (!out.newline)
                {
                    out.printNewline();
                }

                int indentSize = this.settings.getInt(Keys.INDENT_SIZE_IMPLEMENTS,
                                                   Defaults.INDENT_SIZE_IMPLEMENTS);

                if (indentSize > -1) // use custom indentation
                {
                    out.print(out.getString(indentSize), JavaTokenTypes.WS);
                    out.print(IMPLEMENTS_SPACE, JavaTokenTypes.LITERAL_extends);
                }
                else
                {
                    out.print(IMPLEMENTS_SPACE, JavaTokenTypes.LITERAL_extends);
                }
            }
            else
            {
                out.print(SPACE_IMPLEMENTS_SPACE,
                          JavaTokenTypes.LITERAL_extends);
            }

            Marker marker = out.state.markers.add();
            boolean forceWrapping = this.settings.getBoolean(Keys.LINE_WRAP_AFTER_TYPES_IMPLEMENTS,
                                                          Defaults.LINE_WRAP_AFTER_TYPES_IMPLEMENTS);
            TestNodeWriter tester = null;

            if (wrapLines)
            {
                tester = out.testers.get();
            }

            boolean wrapAfterType = false;

            String comma = this.settings.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                 Defaults.SPACE_AFTER_COMMA)
                               ? COMMA_SPACE
                               : COMMA;

            for (AST child = first;
                 child != null;
                 child = child.getNextSibling())
            {
                PrinterFactory.create(child).print(child, out);

                AST next = child.getNextSibling();

                if (next != null)
                {
                    if (forceWrapping)
                    {
                        out.print(COMMA, JavaTokenTypes.COMMA);
                        out.printNewline();

                        if (!indentDeep && !wrapAfterType)
                        {
                            wrapAfterType = true;
                            out.indent();
                        }

                        printIndentation(out);
                        /*out.print(out.getString(marker.column - out.getIndentLength()),
                                  JavaTokenTypes.WS);*/
                    }
                    else if (wrapLines)
                    {
                        PrinterFactory.create(next).print(next, tester);

                        if ((tester.length + out.column) > lineLength)
                        {
                            out.print(COMMA, JavaTokenTypes.COMMA);
                            out.printNewline();

                            if (!indentDeep && !wrapAfterType)
                            {
                                wrapAfterType = true;
                                out.indent();
                            }

                            printIndentation(out);
                            /*out.print(out.getString(marker.column - out.getIndentLength()),
                                      JavaTokenTypes.WS);*/

                        }
                        else
                        {
                            out.print(comma, JavaTokenTypes.COMMA);
                        }

                        tester.reset();
                    }
                    else
                    {
                        out.print(comma, JavaTokenTypes.COMMA);
                    }
                }
            }

            if (wrapLines)
            {
                out.testers.release(tester);
            }

            out.state.markers.remove(marker);

            out.unindent();

            if (wrapAfterType)
                out.unindent();
        }
    }
}
