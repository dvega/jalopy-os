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
 * Printer for array types [<code>ARRAY_DECLARATOR</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ArrayTypePrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ArrayTypePrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ArrayTypePrinter object.
     */
    protected ArrayTypePrinter()
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
        AST child = node.getFirstChild();

        boolean bracketsAfterIdentifier = this.settings.getBoolean(Keys.ARRAY_BRACKETS_AFTER_IDENT,
                                                                Defaults.ARRAY_BRACKETS_AFTER_IDENT);

        if (child != null)
        {
            for (; child != null; child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.EXPR :

                        if (this.settings.getBoolean(Keys.PADDING_BRACKETS,
                                                  Defaults.PADDING_BRACKETS))
                        {
                            out.print(BRACKET_LEFT_SPACE, JavaTokenTypes.LBRACK);
                            PrinterFactory.create(child.getFirstChild())
                                          .print(child.getFirstChild(), out);
                            out.print(SPACE_BRACKET_RIGHT,
                                      JavaTokenTypes.RBRACK);
                        }
                        else
                        {
                            out.print(BRACKET_LEFT, JavaTokenTypes.LBRACK);
                            PrinterFactory.create(child.getFirstChild())
                                          .print(child.getFirstChild(), out);
                            out.print(BRACKET_RIGHT, JavaTokenTypes.RBRACK);
                        }

                        break;

                    case JavaTokenTypes.ARRAY_DECLARATOR :
                        this.print(child, out);

                        if (child.getNextSibling() == null)
                        {
                            if (bracketsAfterIdentifier &&
                                canMoveBrackets(node))
                            {
                                out.state.arrayBrackets++;
                            }
                            else
                            {
                                out.print(BRACKETS, JavaTokenTypes.RBRACK);
                            }
                        }

                        break;

                    default :
                        PrinterFactory.create(child).print(child, out);

                        if (bracketsAfterIdentifier && canMoveBrackets(node))
                        {
                            out.state.arrayBrackets++;
                        }
                        else
                        {
                            if (this.settings.getBoolean(
                                                      Keys.SPACE_BEFORE_BRACKETS_TYPES,
                                                      Defaults.SPACE_BEFORE_BRACKETS_TYPES))
                            {
                                out.print(SPACE_BRACKETS,
                                          JavaTokenTypes.ARRAY_DECLARATOR);
                            }
                            else
                            {
                                out.print(BRACKETS, JavaTokenTypes.RBRACK);
                            }
                        }

                        break;
                }
            }
        }
        else // followed by an ARRAY_INIT
        {
            if (this.settings.getBoolean(Keys.SPACE_BEFORE_BRACKETS_TYPES,
                                      Defaults.SPACE_BEFORE_BRACKETS_TYPES))
            {
                out.print(SPACE_BRACKETS, JavaTokenTypes.ARRAY_DECLARATOR);
            }
            else
            {
                out.print(BRACKETS, JavaTokenTypes.RBRACK);
            }

            if (this.settings.getBoolean(Keys.SPACE_BEFORE_BRACES,
                                      Defaults.SPACE_BEFORE_BRACES))
            {
                out.print(SPACE, out.last);
            }
        }

        out.last = JavaTokenTypes.ARRAY_DECLARATOR;
    }


    /**
     * Determines whether the brackets for the given array type can be moved
     * behind the identifier.
     *
     * @param node a ARRAY_DECLARATOR node.
     *
     * @return <code>true</code> if the brackets can be moved.
     *
     * @since 1.0b9
     */
    private boolean canMoveBrackets(AST node)
    {
        JavaNode parent = ((JavaNode)node).getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.TYPE :

                switch (parent.getParent().getType())
                {
                    case JavaTokenTypes.METHOD_DEF :
                    case JavaTokenTypes.TYPECAST :
                    case JavaTokenTypes.LITERAL_instanceof :
                        return false;
                }

                break;

            case JavaTokenTypes.TYPECAST :
            case JavaTokenTypes.LITERAL_new :
                return false;

            case JavaTokenTypes.ARRAY_DECLARATOR :
                return canMoveBrackets(parent);
        }

        AST next = node.getNextSibling();

        if (next != null)
        {
            switch (next.getType())
            {
                case JavaTokenTypes.LITERAL_class :
                    return false;
            }
        }

        return true;
    }
}
