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


/**
 * Some common helpers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class PrinterHelper
{
    //~ Constructors ··························································

    /**
     * Creates a new PrinterHelper object.
     */
    private PrinterHelper()
    {
    }

    //~ Methods ·······························································

    /**
     * Advance to the first non-LPAREN node for the rare case where both
     * operands are enclosed by several parenthesis groups, e.g. ((LA(4) >=
     * '\u0003')), so we skip unnecessary parenthesis
     *
     * @param lparen the first LPAREN child of an expression node.
     *
     * @return the first non parentheses ast or <code>null</code> if no such
     *         node exists.
     *
     * @throws IllegalArgumentException if <code>lparen.getType() !=
     *         JavaTokenTypes.LPAREN</code>
     */
    public static AST advanceToFirstNonParen(AST lparen)
    {
        if (lparen.getType() != JavaTokenTypes.LPAREN)
        {
            throw new IllegalArgumentException(lparen + " no LPAREN");
        }

LOOP: 
        for (AST next = lparen.getNextSibling();
             next != null;
             next = next.getNextSibling())
        {
            switch (next.getType())
            {
                case JavaTokenTypes.LPAREN :

                    continue LOOP;

                default :
                    return next;
            }
        }

        return null;
    }


    /**
     * Removes the <code>abstract</code> modifier from the given modifiers
     * list, if found.
     *
     * @param node a MODIFIERS node.
     *
     * @throws IllegalArgumentException if <code>node.getType() !=
     *         JavaTokenTypes.MODIFIERS</code>.
     */
    public static void removeAbstractModifier(AST node)
    {
        switch (node.getType())
        {
            case JavaTokenTypes.MODIFIERS :
                break;

            default :
                throw new IllegalArgumentException("MODIFIERS node expected, was " +
                                                   node);
        }

        /*for (AST modifier = node.getFirstChild();
             modifier != null;
             modifier = modifier.getNextSibling())
        {
            String name = modifier.getText();

            if ("abstract".equals(name))
            {
                JavaNode current = (JavaNode)modifier;
                JavaNode parent = current.getParent();
                JavaNode previous = current.getPreviousSibling();
                JavaNode next = (JavaNode)current.getNextSibling();

                if (parent == previous)
                {
                    parent.setFirstChild(next);
                }
                else
                {
                    previous.setNextSibling(next);
                }

                if (next != null)
                {
                    next.setPreviousSibling(previous);
                }

                current.setParent(null);
                current.setPreviousSibling(null);
                current.setFirstChild(null);
                current.setNextSibling(null);

                break;
            }
        }*/
    }


    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @since 1.0b9
     */
    static boolean isMultipleExpression(AST node)
    {
        switch (node.getType())
        {
            case JavaTokenTypes.EXPR :

                switch (node.getFirstChild().getType())
                {
                    case JavaTokenTypes.LAND :
                    case JavaTokenTypes.LOR :
                    case JavaTokenTypes.LT :
                    case JavaTokenTypes.GT :
                    case JavaTokenTypes.LE :
                    case JavaTokenTypes.GE :
                    case JavaTokenTypes.EQUAL :
                    case JavaTokenTypes.NOT_EQUAL :
                        return isMultipleExpression(node.getFirstChild()
                                                        .getFirstChild());
                }

                break;

            default :

                switch (node.getType())
                {
                    case JavaTokenTypes.LAND :
                    case JavaTokenTypes.LOR :
                    case JavaTokenTypes.LT :
                    case JavaTokenTypes.GT :
                    case JavaTokenTypes.LE :
                    case JavaTokenTypes.GE :
                    case JavaTokenTypes.EQUAL :
                    case JavaTokenTypes.NOT_EQUAL :

                        JavaNode parent = ((JavaNode)node).getParent();

                        switch (parent.getType())
                        {
                            case JavaTokenTypes.LAND :
                            case JavaTokenTypes.LOR :
                            case JavaTokenTypes.LT :
                            case JavaTokenTypes.GT :
                            case JavaTokenTypes.LE :
                            case JavaTokenTypes.GE :
                            case JavaTokenTypes.EQUAL :
                            case JavaTokenTypes.NOT_EQUAL :
                                return true;
                        }
                }

                break;
        }

        return false;
    }
}
