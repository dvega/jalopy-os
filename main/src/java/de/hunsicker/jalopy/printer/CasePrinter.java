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
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for case blocks and case expressions.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class CasePrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new CasePrinter();

    //~ Constructors ··························································

    /**
     * Creates a new CasePrinter object.
     */
    protected CasePrinter()
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
        switch (node.getType())
        {
            case JavaTokenTypes.LITERAL_case :
            {
                printCommentsBefore(node, out);
                out.print(CASE_SPACE, JavaTokenTypes.LITERAL_case);

                AST expr = node.getFirstChild();
                PrinterFactory.create(expr).print(expr, out);

                if (this.prefs.getBoolean(Keys.SPACE_BEFORE_CASE_COLON,
                                          Defaults.SPACE_BEFORE_CASE_COLON))
                {
                    out.print(SPACE, JavaTokenTypes.WS);
                }

                out.print(COLON, JavaTokenTypes.LITERAL_case);

                AST colon = expr.getNextSibling();

                if (!printCommentsAfter(colon, NodeWriter.NEWLINE_NO,
                                        NodeWriter.NEWLINE_YES, out))
                {
                    out.printNewline();
                }

                out.last = JavaTokenTypes.LITERAL_case;

                break;
            }

            case JavaTokenTypes.CASE_GROUP :
LOOP: 
                for (AST child = node.getFirstChild();
                     child != null;
                     child = child.getNextSibling())
                {
                    switch (child.getType())
                    {
                        case JavaTokenTypes.COLON :

                            if (!printCommentsAfter(child,
                                                    NodeWriter.NEWLINE_NO,
                                                    NodeWriter.NEWLINE_YES, out))
                            {
                                out.printNewline();
                            }

                            continue LOOP;

                        case JavaTokenTypes.SLIST :

                            // don't print empty lists, as these would break
                            // our indentation
                            if (NodeHelper.isEmptyBlock(child))
                            {
                                continue LOOP;
                            }

                            break;

                        case JavaTokenTypes.LITERAL_default :

                            AST sibling = child.getNextSibling();

                            // I really don't know why, but one may use the
                            // default statement *not* as the last statement in
                            // the switch selection, so we have to check here
                            // whether another case follows, and only if not we
                            // apply the test
                            switch (sibling.getType())
                            {
                                case JavaTokenTypes.LITERAL_case :
                                case JavaTokenTypes.COLON :
                                    break;

                                default :

                                    if (NodeHelper.isEmptyBlock(sibling))
                                    {
                                        break LOOP;
                                    }
                            }

                            break;
                    }

                    PrinterFactory.create(child).print(child, out);
                }

                break;

            case JavaTokenTypes.LITERAL_default :
            {
                printCommentsBefore(node, out);

                if (this.prefs.getBoolean(Keys.SPACE_BEFORE_CASE_COLON, false))
                {
                    out.print(DEFAULT_SPACE_COLON,
                              JavaTokenTypes.LITERAL_default);
                }
                else
                {
                    out.print(DEFAULT_COLON, JavaTokenTypes.LITERAL_default);
                }

                out.last = JavaTokenTypes.LITERAL_default;

                break;
            }
        }
    }
}
