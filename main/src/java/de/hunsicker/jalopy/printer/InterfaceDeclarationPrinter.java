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
import de.hunsicker.jalopy.language.JavaNodeModifier;
import de.hunsicker.jalopy.language.JavaTokenTypes;


/**
 * Printer for interface declarations (<code>INTERFACE_DEF</code>).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class InterfaceDeclarationPrinter
    extends BasicDeclarationPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Singleton. */
    private static final Printer INSTANCE = new InterfaceDeclarationPrinter();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new InterfaceDeclarationPrinter object.
     */
    protected InterfaceDeclarationPrinter()
    {
    }

    //~ Methods --------------------------------------------------------------------------

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
    public void print(
        AST        node,
        NodeWriter out)
      throws IOException
    {
        out.state.innerClass = (out.getIndentLength() > 0);

        JavaNode n = (JavaNode) node;

        addCommentIfNeeded(n, out);

        super.print(node, out);

        AST modifiers = node.getFirstChild();

        if (JavaNodeModifier.isAbstract(modifiers))
        {
            PrinterHelper.removeAbstractModifier(modifiers);
        }

        PrinterFactory.create(modifiers).print(modifiers, out);

        AST keyword = modifiers.getNextSibling();

        printCommentsBefore(keyword, NodeWriter.NEWLINE_NO, out);

        out.print(INTERFACE_SPACE, JavaTokenTypes.LITERAL_interface);

        if (printCommentsAfter(keyword, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO, out))
        {
            if (!out.newline)
                out.print(SPACE, JavaTokenTypes.WS);
        }

        AST identifier = keyword.getNextSibling();
        PrinterFactory.create(identifier).print(identifier, out);

        AST extendsClause = identifier.getNextSibling();
        PrinterFactory.create(extendsClause).print(extendsClause, out);

        out.state.extendsWrappedBefore = false;

        printCommentsAfter(node, out);

        AST body = extendsClause.getNextSibling();
        PrinterFactory.create(body).print(body, out);

        out.state.innerClass = false;
        out.last = JavaTokenTypes.INTERFACE_DEF;
    }
}
