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
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;

import java.io.IOException;


/**
 * Printer for method declarations (<code>METHOD_DEF</code>).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class MethodDeclarationPrinter
    extends BasicDeclarationPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new MethodDeclarationPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new MethodDeclarationPrinter object.
     */
    protected MethodDeclarationPrinter()
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
        JavaNode n = (JavaNode)node;

        addCommentIfNeeded(n, out);

        // trigger printing of blank lines/comments
        super.print(node, out);

        AST modifiers = node.getFirstChild();

        if (isPartOfInterface(n))
        {
            PrinterHelper.removeAbstractModifier(modifiers);
        }

        PrinterFactory.create(modifiers).print(modifiers, out);

        AST type = modifiers.getNextSibling();
        PrinterFactory.create(type).print(type, out);
        out.print(SPACE, JavaTokenTypes.WS);

        AST identifier = type.getNextSibling();
        PrinterFactory.create(identifier).print(identifier, out);

        // print parameters
        if (this.settings.getBoolean(Keys.SPACE_BEFORE_METHOD_DEF_PAREN,
                                  Defaults.SPACE_BEFORE_METHOD_DEF_PAREN))
        {
            out.print(SPACE, JavaTokenTypes.WS);
        }

        // set the marker needed by ThrowsPrinter.java
        Marker marker = out.state.markers.add(out.line, out.column);

        AST lparen = identifier.getNextSibling();
        PrinterFactory.create(lparen).print(lparen, out);

        AST parameters = lparen.getNextSibling();
        PrinterFactory.create(parameters).print(parameters, out);

        AST rparen = parameters.getNextSibling();
        PrinterFactory.create(rparen).print(rparen, out);

        for (AST child = rparen.getNextSibling();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.SLIST :
                case JavaTokenTypes.SEMI :
                    out.state.markers.remove(marker);

                    break;
            }

            PrinterFactory.create(child).print(child, out);
        }

        out.last = JavaTokenTypes.METHOD_DEF;
    }


    /**
     * Determines whether the given node is part of an interface declaration.
     *
     * @param node a METHOD_DEF node.
     *
     * @return <code>true</code> if the given node is part of an interface
     *         declaration.
     *
     * @since 1.0b8
     */
    private boolean isPartOfInterface(JavaNode node)
    {
        if (node.getParent().getParent().getType() == JavaTokenTypes.INTERFACE_DEF)
        {
            return true;
        }

        return false;
    }
}
