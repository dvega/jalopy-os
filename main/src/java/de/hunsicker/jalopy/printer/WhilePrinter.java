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
 * Printer for while loops <code>LITERAL_while</code>.
 * <pre style="background:lightgrey">
 * <strong>while</strong> (<em>Boolean-expression</em>)
 * {
 *     <em>statement</em>
 * }
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class WhilePrinter
    extends BlockStatementPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new WhilePrinter();

    //~ Constructors ··························································

    /**
     * Creates a new WhilePrinter object.
     */
    protected WhilePrinter()
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
        super.print(node, out);

        if (this.settings.getBoolean(Keys.SPACE_BEFORE_STATEMENT_PAREN,
                                  Defaults.SPACE_BEFORE_STATEMENT_PAREN))
        {
            out.print(WHILE_SPACE, JavaTokenTypes.LITERAL_while);
        }
        else
        {
            out.print(WHILE, JavaTokenTypes.LITERAL_while);
        }

        boolean insertBraces = this.settings.getBoolean(Keys.BRACE_INSERT_WHILE,
                                                     Defaults.BRACE_INSERT_WHILE);

        AST lparen = node.getFirstChild();
        AST rparen = printExpressionList(lparen, insertBraces, out);
        AST body = rparen.getNextSibling();

        switch (body.getType())
        {
            case JavaTokenTypes.SLIST :
                out.last = JavaTokenTypes.LITERAL_while;
                PrinterFactory.create(body).print(body, out);

                break;

            default :

                // insert braces manually
                if (insertBraces)
                {
                    out.printLeftBrace(this.settings.getBoolean(
                                                             Keys.BRACE_NEWLINE_LEFT,
                                                             Defaults.BRACE_NEWLINE_LEFT),
                                       NodeWriter.NEWLINE_YES);
                    PrinterFactory.create(body).print(body, out);
                    out.printRightBrace();
                }
                else
                {
                    out.printNewline();
                    out.indent();
                    PrinterFactory.create(body).print(body, out);
                    out.unindent();
                }
        }

        // do as if braces always printed for the correct blank lines behaviour
        out.last = JavaTokenTypes.RCURLY;
    }
}
