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

import java.io.IOException;


/**
 * Printer for assertions [<code>LITERAL_assert</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class AssertionPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers иииииииииииииииииииииииииииииииииииииииии

    /** Singleton. */
    private static final Printer INSTANCE = new AssertionPrinter();

    //~ Constructors ииииииииииииииииииииииииииииииииииииииииииииииииииииииииии

    /**
     * Creates a new AssertionPrinter object.
     */
    protected AssertionPrinter()
    {
    }

    //~ Methods иииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииии

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
        out.print(ASSERT_SPACE, JavaTokenTypes.LITERAL_assert);

        AST condition = node.getFirstChild();
        PrinterFactory.create(condition).print(condition, out);

        AST next = condition.getNextSibling();

        switch (next.getType())
        {
            // print the message expression
            case JavaTokenTypes.EXPR :
                out.print(" : ", JavaTokenTypes.COLON);
                PrinterFactory.create(next).print(next, out);

                AST semi = next.getNextSibling();
                PrinterFactory.create(semi).print(semi, out);

                break;

            // print the delimiting semi
            case JavaTokenTypes.SEMI :
                PrinterFactory.create(next).print(next, out);

                break;

            default :
                break;
        }
    }
}
