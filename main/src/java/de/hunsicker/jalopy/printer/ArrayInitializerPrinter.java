/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.printer;

import java.io.IOException;

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.language.JavaNode;
import de.hunsicker.jalopy.language.JavaTokenTypes;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;


/**
 * Printer for array initializers [<code>ARRAY_INIT</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ArrayInitializerPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Singleton. */
    private static final Printer INSTANCE = new ArrayInitializerPrinter();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ArrayInitializerPrinter object.
     */
    protected ArrayInitializerPrinter()
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
        // Options for wrapping:
        //
        //   - 0                 : wrap as needed (according to line length)
        //   - Integer.MAX_VALUE : never wrap
        //   - custom user value : wrap if more than 'value' elements
        //
        int maxElementsPerLine =
            this.settings.getInt(
                ConventionKeys.LINE_WRAP_ARRAY_ELEMENTS,
                ConventionDefaults.LINE_WRAP_ARRAY_ELEMENTS);
        boolean wrapAsNeeded = maxElementsPerLine == 0;
        int lineLength =
            this.settings.getInt(
                ConventionKeys.LINE_LENGTH, ConventionDefaults.LINE_LENGTH);
        boolean bracesPadding =
            this.settings.getBoolean(
                ConventionKeys.PADDING_BRACES, ConventionDefaults.PADDING_BRACES);
        boolean spaceAfterComma =
            this.settings.getBoolean(
                ConventionKeys.SPACE_AFTER_COMMA, ConventionDefaults.SPACE_AFTER_COMMA);
        int numElements = 0; // number of array elements
        int last = out.last;
        boolean multiArray = hasArrayChild(node);

        if (multiArray)
        {
            maxElementsPerLine = 1;
            wrapAsNeeded = false;
        }

        // force newline before the left curly brace of a new array dimension
        if (isFirstOfMultiArray((JavaNode) node))
        {
            if (!out.newline)
            {
                out.printNewline();
            }

            printIndentation(out);
        }

        JavaNode firstElement = (JavaNode) node.getFirstChild();

        // should we check whether wrapping/aligning is necessary?
        if (
            (maxElementsPerLine != Integer.MAX_VALUE)
            && (out.mode == NodeWriter.MODE_DEFAULT))
        {
            TestNodeWriter tester = out.testers.get();

            for (AST child = firstElement; child != null;
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
                if (!wrapAsNeeded || ((out.column + tester.length) > lineLength))
                {
                    if (!out.newline)
                    {
                        if (
                            (out.getIndentLength() != (out.column - 1))
                            && (this.settings.getBoolean(
                                ConventionKeys.BRACE_NEWLINE_LEFT,
                                ConventionDefaults.BRACE_NEWLINE_LEFT)
                            && (((JavaNode) node).getParent().getType() != JavaTokenTypes.ARRAY_INIT)))
                        {
                            out.printNewline();
                            printIndentation(out);
                        }

                        else
                        {
                            switch (last)
                            {
                                case JavaTokenTypes.ARRAY_DECLARATOR :

                                    if (
                                        this.settings.getBoolean(
                                            ConventionKeys.SPACE_BEFORE_BRACES,
                                            ConventionDefaults.SPACE_BEFORE_BRACES))
                                    {
                                        out.print(SPACE, out.last);
                                    }

                                    break;
                            }
                        }
                    }

                    out.print(LCURLY, JavaTokenTypes.LCURLY);

                    Marker marker = null;

                    if (
                        this.settings.getBoolean(
                            ConventionKeys.INDENT_DEEP, ConventionDefaults.INDENT_DEEP))
                    {
                        if (!out.state.markers.isMarked())
                        {
                            marker = out.state.markers.add(out.line, out.column - 2);
                            out.state.markers.add(
                                out.line,
                                out.state.markers.getLast().column + out.indentSize);
                        }
                        else
                        {
                            marker =
                                out.state.markers.add(
                                    out.line,
                                    out.state.markers.getLast().column + out.indentSize);
                        }
                    }
                    else
                    {
                        out.indent();
                    }

                    boolean wrapped = false;

                    //printCommentsAfter(node, out);
                    if (firstElement.getType() != JavaTokenTypes.ARRAY_INIT)
                    {
                        if (!multiArray || hasArrayParent(node))
                        {
                            out.printNewline();
                            printIndentation(out);
                            wrapped = true;
                        }
                    }

                    int count = 0;

                    // print the elements
                    for (
                        JavaNode child = firstElement; child != null;
                        child = (JavaNode) child.getNextSibling())
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
                                    wrapped =
                                        printCommentsAfter(
                                            child, NodeWriter.NEWLINE_NO,
                                            !isLastElement(child), out);
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
                                    PrinterFactory.create(child).print(child, tester);

                                    if (
                                        (!wrapAsNeeded && (count > maxElementsPerLine))
                                        || (wrapAsNeeded
                                        && ((out.column + tester.length) > lineLength)))
                                    {
                                        JavaNode n = (JavaNode) child;

                                        if (
                                            n.hasCommentsBefore()
                                            && (((CommonHiddenStreamToken) n
                                            .getHiddenBefore()).getLine() == n
                                            .getStartLine()))
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
                                        else if ((spaceAfterComma && (count > 1)))
                                        {
                                            out.print(SPACE, JavaTokenTypes.WS);
                                        }
                                    }
                                }

                                wrapped = false;

                                PrinterFactory.create(child).print(child, out);

                                /*switch (child.getType())
                                {
                                    case JavaTokenTypes.EXPR :

                                        if (!wrapped && !isLastElement(child))
                                        {
                                            JavaNode c = (JavaNode)child.getFirstChild();
                                        }

                                        break;
                                }*/
                                break;
                        }
                    }

                    if (marker != null)
                    {
                        out.state.markers.remove(marker);
                    }
                    else
                    {
                        out.unindent();
                    }

                    if (!out.newline)
                    {
                        out.printNewline();
                    }

                    printIndentation(out);

                    out.print(RCURLY, JavaTokenTypes.ARRAY_INIT);
                    out.testers.release(tester);

                    // we're done
                    return;
                }
            }

            out.testers.release(tester);
        }

        switch (last)
        {
            case JavaTokenTypes.ARRAY_DECLARATOR :

                if (
                    this.settings.getBoolean(
                        ConventionKeys.SPACE_BEFORE_BRACES,
                        ConventionDefaults.SPACE_BEFORE_BRACES))
                {
                    out.print(SPACE, out.last);
                }

                break;
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

        printCommentsAfter(node, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO, out);

        out.indent();

        String comma = spaceAfterComma ? COMMA_SPACE
                                       : COMMA;

        for (
            JavaNode child = (JavaNode) node.getFirstChild(); child != null;
            child = (JavaNode) child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.COMMA :
                    printCommentsBefore(child, out);

                    if (!child.hasCommentsAfter())
                    {
                        if (isLast(child))
                        {
                            out.print(COMMA, JavaTokenTypes.COMMA);
                        }
                        else
                        {
                            out.print(comma, JavaTokenTypes.COMMA);
                        }
                    }
                    else
                    {
                        out.print(COMMA, JavaTokenTypes.COMMA);
                        printCommentsAfter(
                            child, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_YES, out);
                    }

                    break;

                default :

                    switch (child.getType())
                    {
                        case JavaTokenTypes.EXPR :

                            JavaNode c = (JavaNode) child.getFirstChild();

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
     * @return <code>true</code> if the node is part of a multi-dimensional array.
     *
     * @since 1.0b8
     */
    private boolean isFirstOfMultiArray(JavaNode node)
    {
        JavaNode parent = node.getParent();

        if (
            (parent.getType() == JavaTokenTypes.ARRAY_INIT)
            && (node.getPreviousSibling() == parent))
        {
            return true;
        }

        return false;
    }


    /**
     * Determines whether the given element (either an expression or comma operator) is
     * the last element of the array initializer.
     *
     * @param node either an EXPR or COMMA node.
     *
     * @return <code>true</code> if the given node is the last element of the array
     *         initializer.
     *
     * @since 1.b09
     */
    private boolean isLast(AST node)
    {
        return node.getNextSibling().getType() == JavaTokenTypes.RCURLY;
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
     * @return <code>true</code> if the node is part of a multi-dimensional array.
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


    /**
     * Determines whether the given array initializer has another array initializer as a
     * child (which makes it part of a multi-array thingy).
     *
     * @param node ARRAY_INIT node or LITERAl_NEW.
     *
     * @return <code>true</code> if another ARRAY_INIT could be found as a child of the
     *         given initializer or creator.
     *
     * @since 1.0b9
     */
    private boolean hasArrayChild(AST node)
    {
        for (AST child = node.getFirstChild(); child != null;
            child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.ARRAY_INIT :
                    return true;

                case JavaTokenTypes.EXPR :

                    AST child1 = child.getFirstChild();

                    switch (child1.getType())
                    {
                        case JavaTokenTypes.LITERAL_new :

                            if (hasArrayChild(child1.getFirstChild()))
                            {
                                return true;
                            }
                    }

                    break;
            }
        }

        return false;
    }


    private boolean hasArrayParent(AST node)
    {
        JavaNode parent = ((JavaNode) node).getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.ARRAY_INIT :
                return true;

            case JavaTokenTypes.LITERAL_new :
                return parent.getParent().getParent().getType() == JavaTokenTypes.ARRAY_INIT;
        }

        return false;
    }
}
