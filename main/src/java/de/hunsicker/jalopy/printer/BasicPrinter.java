/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.printer;

import java.io.IOException;

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.language.JavaNode;


/**
 * Basic printer which prints out just the node's text.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class BasicPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Singleton. */
    private static final Printer INSTANCE = new BasicPrinter();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a mew BasicPrinter object.
     */
    protected BasicPrinter()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the sole instance of this class.
     *
     * @return the sole instance of this class.
     */
    public static Printer getInstance()
    {
        return INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    public void print(
        AST        node,
        NodeWriter out)
      throws IOException
    {
        printCommentsBefore(node, out);

        int offset = out.print(node.getText(), node.getType());

        trackPosition((JavaNode) node, out.line, offset, out);

        if (out.state.arrayBrackets > 0)
        {
            for (int i = 0; i < out.state.arrayBrackets; i++)
            {
                out.print(BRACKETS, node.getType());
            }

            out.state.arrayBrackets = 0;
        }

        printCommentsAfter(node, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO, out);
    }
}
