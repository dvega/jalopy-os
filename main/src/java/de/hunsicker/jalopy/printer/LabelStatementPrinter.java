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
 * Printer for labels.
 * <pre style="background:lightgrey">
 * <strong>LABEL1:</strong>
 * <em>outer-iteration</em>
 * {
 *     <em>inner-iteration</em>
 *     {
 *         // ...
 *         break;
 *         // ...
 *         continue;
 *         // ...
 *         continue LABEL1;
 *         // ...
 *         break LABEL1;
 *     }
 * }
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class LabelStatementPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new LabelStatementPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new LabelStatementPrinter object.
     */
    protected LabelStatementPrinter()
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
        boolean indentLabel = this.settings.getBoolean(Keys.INDENT_LABEL,
                                                    Defaults.INDENT_LABEL);
        boolean lineWrapLabel = this.settings.getBoolean(Keys.LINE_WRAP_AFTER_LABEL,
                                                      Defaults.LINE_WRAP_AFTER_LABEL);
        AST identifier = node.getFirstChild();
        AST body = identifier.getNextSibling();

        boolean newlineAfter = false;
        boolean commentAfter = false;

        if (lineWrapLabel && (body.getType() != JavaTokenTypes.SLIST))
        {
            //out.printNewline();
            newlineAfter = true;
        }

        // use the current indentation
        if (indentLabel)
        {
            printCommentsBefore(node, out);
            logIssues(node, out);
            PrinterFactory.create(identifier).print(identifier, out);
            out.print(COLON_SPACE, JavaTokenTypes.LABELED_STAT);
            commentAfter = printCommentsAfter(node, NodeWriter.NEWLINE_NO,
                                              newlineAfter, out);
        }
        else
        {
            // we want the label statement at the beginning of a line,
            // i.e. we use no indentation at all
            int oldLevel = out.getIndentLevel();
            out.setIndentLevel(0);
            printCommentsBefore(node, out);
            logIssues(node, out);
            PrinterFactory.create(identifier).print(identifier, out);
            out.print(COLON_SPACE, JavaTokenTypes.LABELED_STAT);

            if (!printCommentsAfter(node, NodeWriter.NEWLINE_NO, newlineAfter,
                                    out))
            {
                // calculate the space between the printed label and the
                // beginning of the loop statement
                int diff = out.getIndentLength() -
                           identifier.getText().length() - 2;

                if (diff > 1)
                {
                    // we must print some whitespace between our label and the 
                    // block
                    out.print(out.getString(diff), JavaTokenTypes.WS);
                }
            }
            else
            {
                commentAfter = true;
            }

            out.setIndentLevel(oldLevel);
        }

        if (newlineAfter && (!commentAfter))
        {
            out.printNewline();
        }

        PrinterFactory.create(body).print(body, out);
    }
}
