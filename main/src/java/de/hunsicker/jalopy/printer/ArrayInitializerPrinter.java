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

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;

import java.io.IOException;


/**
 * Printer for array initializers [<code>ARRAY_INIT</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ArrayInitializerPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ArrayInitializerPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ArrayInitializerPrinter object.
     */
    protected ArrayInitializerPrinter()
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
        // Options for wrapping:
        //
        //   - 0                 : wrap as needed (according to line length)
        //   - Integer.MAX_VALUE : never wrap
        //   - custom user value : wrap if more than 'value' elements
        //
        int maxElementsPerLine = this.settings.getInt(Keys.LINE_WRAP_ARRAY_ELEMENTS,
                                                   Defaults.LINE_WRAP_ARRAY_ELEMENTS);
        boolean wrapAsNeeded = maxElementsPerLine == 0;
        int lineLength = this.settings.getInt(Keys.LINE_LENGTH,
                                           Defaults.LINE_LENGTH);
        boolean bracesPadding = this.settings.getBoolean(Keys.PADDING_BRACES,
                                                      Defaults.PADDING_BRACES);
        boolean spaceAfterComma = this.settings.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                        Defaults.SPACE_AFTER_COMMA);
        int numElements = 0; // number of array elements

        boolean multiArray = hasArrayChild(node);

        if (multiArray)
        {
            maxElementsPerLine = 1;
            wrapAsNeeded = false;
        }

        // force newline before the left curly brace of a new array dimension
        if (isFirstOfMultiArray((JavaNode)node))
        {
            if (!out.newline)
                out.printNewline();

            printIndentation(out);
        }

        JavaNode firstElement = (JavaNode)node.getFirstChild();

        // should we check whether wrapping/aligning is necessary?
        if ((maxElementsPerLine != Integer.MAX_VALUE) &&
            (out.mode == NodeWriter.MODE_DEFAULT))
        {
            TestNodeWriter tester = out.testers.get();

            for (AST child = firstElement;
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.COMMA :
                        break;

                    default :
                        numElements++;

                        break;
                }

                PrinterFactory.create(child).print(child, tester);

                if (wrapAsNeeded && (tester.length > lineLength))
                {
                    break;
                }
            }

            // wrap elements
            if (numElements > maxElementsPerLine)
            {
                // but only if the elements don't fit in one line
                if ((!wrapAsNeeded) ||
                    ((out.column + tester.length) > lineLength))
                {
                    if ((!out.newline) &&
                        (
                         (this.settings.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                                Defaults.BRACE_NEWLINE_LEFT) &&
                          (((JavaNode)node).getParent().getType() != JavaTokenTypes.ARRAY_INIT))))
                    {
                        out.printNewline();
                        printIndentation(out);
                    }

                    out.print(LCURLY, JavaTokenTypes.LCURLY);

                    Marker marker = null;

                    if (this.settings.getBoolean(Keys.INDENT_DEEP, Defaults.INDENT_DEEP))
                        marker = out.state.markers.add(out.line, out.state.markers.getLast().column + out.indentSize);
                    else
                        out.indent();

                    //printCommentsAfter(node, out);
                    if (firstElement.getType() != JavaTokenTypes.ARRAY_INIT)
                    {
                        if (!multiArray ||  hasArrayParent(node))
                        {
                            out.printNewline();
                            printIndentation(out);
                        }
                    }

                    int count = 0;
                    boolean wrapped = false;

                    // print the elements
                    for (JavaNode child = firstElement;
                         child != null;
                         child = (JavaNode)child.getNextSibling())
                    {
                        switch (child.getType())
                        {
                            case JavaTokenTypes.COMMA :

                                if (!child.hasCommentsAfter())
                                {
                                    out.print(COMMA, JavaTokenTypes.COMMA);
                                }
                                else
                                {
                                    out.print(COMMA, JavaTokenTypes.COMMA);
                                    wrapped = printCommentsAfter(child,
                                                                 NodeWriter.NEWLINE_NO,
                                                                 !isLastElement(child),
                                                                 out);
                                    count = 0;
                                }

                                break;

                            case JavaTokenTypes.RCURLY :
                                break;

                            default :
                                count++;

                                if (!wrapped)
                                {
                                    // check if we exceed the line length
                                    tester.reset();
                                    PrinterFactory.create(child)
                                                  .print(child, tester);

                                    if (((!wrapAsNeeded) &&
                                         (count > maxElementsPerLine)) ||
                                        (wrapAsNeeded &&
                                         ((out.column + tester.length) > lineLength)))
                                    {
                                        JavaNode n = (JavaNode)child;

                                        if (n.hasCommentsBefore() &&
                                            (((CommonHiddenStreamToken)n.getHiddenBefore()).getLine() == n.getStartLine()))
                                        {
                                            ;
                                        }
                                        else
                                        {
                                            out.printNewline();
                                            printIndentation(out);

                                            wrapped = true;
                                            count = 1;

                                            int blankLines = getOriginalBlankLines(child);

                                            if (blankLines > 0)
                                            {
                                                out.printBlankLines(blankLines);
                                            }
                                        }
                                    }
                                    else
                                    {
                                        int blankLines = getOriginalBlankLines(child);

                                        if (blankLines > 0)
                                        {
                                            out.printNewline();
                                            printIndentation(out);

                                            wrapped = true;
                                            count = 1;
                                            out.printBlankLines(blankLines);
                                        }
                                        else if (spaceAfterComma && (count > 1))
                                        {
                                            out.print(SPACE, JavaTokenTypes.WS);
                                        }
                                    }
                                }

                                wrapped = false;

                                PrinterFactory.create(child).print(child, out);

                                switch (child.getType())
                                {
                                    case JavaTokenTypes.EXPR :

                                        if ((!wrapped) && (!isLastElement(child)))
                                        {
                                            JavaNode c = (JavaNode)child.getFirstChild();
                                        }

                                        break;
                                }

                                break;
                        }
                    }

                    if (marker != null)
                        out.state.markers.remove(marker);
                    else
                        out.unindent();

                    if (!out.newline)
                        out.printNewline();
                    printIndentation(out);

                    out.print(RCURLY, JavaTokenTypes.ARRAY_INIT);
                    out.testers.release(tester);

                    // we're done
                    return;
                }
            }

            out.testers.release(tester);
        }

        // print everything on one line
        if (bracesPadding)
        {
            out.print(LCURLY_SPACE, JavaTokenTypes.LCURLY);
        }
        else
        {
            out.print(LCURLY, JavaTokenTypes.LCURLY);
        }

        printCommentsAfter(node, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO,
                           out);

        out.indent();

        String comma = spaceAfterComma ? COMMA_SPACE
                                       : COMMA;

        for (JavaNode child = (JavaNode)node.getFirstChild();
             child != null;
             child = (JavaNode)child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.COMMA :
                    printCommentsBefore(child, out);

                    if (!child.hasCommentsAfter())
                    {
                        out.print(comma, JavaTokenTypes.COMMA);
                    }
                    else
                    {
                        out.print(COMMA, JavaTokenTypes.COMMA);
                        printCommentsAfter(child, NodeWriter.NEWLINE_NO,
                                           NodeWriter.NEWLINE_YES, out);
                    }

                    break;

                default :

                    switch (child.getType())
                    {
                        case JavaTokenTypes.EXPR :

                            JavaNode c = (JavaNode)child.getFirstChild();

                            if (c.hasCommentsBefore())
                            {
                                out.printNewline();
                                printIndentation(out);
                                bracesPadding = false;
                            }

                            PrinterFactory.create(child).print(child, out);

                            if (c.hasCommentsAfter())
                            {
                                bracesPadding = false;
                            }

                            break;

                        default :
                            PrinterFactory.create(child).print(child, out);

                            if (child.hasCommentsAfter())
                            {
                                bracesPadding = false;
                            }

                            break;
                    }

                    break;
            }
        }

        out.unindent();

        if (bracesPadding)
        {
            out.print(RCURLY_SPACE, JavaTokenTypes.RCURLY);
        }
        else
        {
            out.print(RCURLY, JavaTokenTypes.RCURLY);
        }
    }


    /**
     * Determines whether the given node is part of a multi-dimensional array.
     *
     * @param node an element of an array initializer.
     *
     * @return <code>true</code> if the node is part of a multi-dimensional
     *         array.
     *
     * @since 1.0b8
     */
    private boolean isFirstOfMultiArray(JavaNode node)
    {
        JavaNode parent = node.getParent();

        if ((parent.getType() == JavaTokenTypes.ARRAY_INIT) &&
            (node.getPreviousSibling() == parent))
        {
            return true;
        }

        return false;
    }


    /**
     * Determines whether the given node is the last array element.
     *
     * @param node an element of an array initializer.
     *
     * @return <code>true</code> if the node is the last element of the array.
     *
     * @since 1.0b8
     */
    private boolean isLastElement(JavaNode node)
    {
        AST next = node.getNextSibling();

        if ((next == null) || (next.getType() == JavaTokenTypes.RCURLY))
        {
            return true;
        }

        return false;
    }


    /**
     * Determines whether the given node is part of a multi-dimensional array.
     *
     * @param node an element of an array initializer.
     *
     * @return <code>true</code> if the node is part of a multi-dimensional
     *         array.
     *
     * @since 1.0b8
     */
    private boolean isMultiArray(AST node)
    {
        AST first = node.getFirstChild();

        if ((first != null) && (first.getType() == JavaTokenTypes.ARRAY_INIT))
        {
            return true;
        }

        /*JavaNode n = (JavaNode)node;

        return n.getParent().getType() == JavaTokenTypes.ARRAY_INIT;*/

            return false;
    }

    private boolean hasArrayParent(AST node)
    {
        JavaNode parent =  ((JavaNode)node).getParent();
        switch (parent.getType())
        {
            case JavaTokenTypes.ARRAY_INIT:
                return true;

            case JavaTokenTypes.LITERAL_new:
                return parent.getParent().getParent().getType() == JavaTokenTypes.ARRAY_INIT;

        }

        return false;
    }

    /**
     * Determines whether the given array initializer has another array initializer as a child (which makes it part of a multi-array thingy).
     * @param node ARRAY_INIT node or LITERAl_NEW.
     * @return <code>true</code> if another ARRAY_INIT could be found as a child of the given initializer or creator.
     * @since 1.0b9
     */
    private boolean hasArrayChild(AST node)
    {
        for (AST child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.ARRAY_INIT:
                    return true;

                case JavaTokenTypes.EXPR:
                    AST child1 = child.getFirstChild();

                    switch (child1.getType())
                    {
                        case JavaTokenTypes.LITERAL_new:
                            if( hasArrayChild(child1.getFirstChild()))
                                return true;
                    }
                    break;
            }
        }

        return false;
    }
}
