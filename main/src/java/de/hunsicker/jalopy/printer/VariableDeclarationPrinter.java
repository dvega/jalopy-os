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
import de.hunsicker.jalopy.language.JavaTokenTypes;
import de.hunsicker.jalopy.language.NodeHelper;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;


/**
 * Printer for variable declarations [<code>VARIABLE_DEF</code>].
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class VariableDeclarationPrinter
    extends BasicDeclarationPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    static final int OFFSET_NONE = -1;

    /** Singleton. */
    private static final Printer INSTANCE = new VariableDeclarationPrinter();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new VariableDeclarationPrinter object.
     */
    protected VariableDeclarationPrinter()
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
        JavaNode n = (JavaNode) node;

        if (!out.state.anonymousInnerClass && !n.hasJavadocComment())
        {
            if (
                (!out.state.innerClass
                || this.settings.getBoolean(
                    ConventionKeys.COMMENT_JAVADOC_INNER_CLASS,
                    ConventionDefaults.COMMENT_JAVADOC_INNER_CLASS))
                && !NodeHelper.isLocalVariable(node))
            {
                addComment(n, out);
            }
        }

        printCommentsBefore(node, out);

        int last = out.last;
        AST modifiers = node.getFirstChild();
        PrinterFactory.create(modifiers).print(modifiers, out);

        AST type = modifiers.getNextSibling();
        PrinterFactory.create(type).print(type, out);

        boolean newChunk = false;

        // align if necessary
        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            if (
                this.settings.getBoolean(
                    ConventionKeys.ALIGN_VAR_IDENTS, ConventionDefaults.ALIGN_VAR_IDENTS))
            {
                newChunk = alignVariable(node, last, out);
            }
            else
            {
                AST next = node.getNextSibling();

                if (
                    (next == null)
                    || ((NodeHelper.getFirstChild(node, JavaTokenTypes.ASSIGN) == null)
                    && (next.getType() != JavaTokenTypes.VARIABLE_DEF)))
                {
                    newChunk = true;
                }
            }
        }

        out.print(SPACE, JavaTokenTypes.WS);

        AST identifier = type.getNextSibling();
        PrinterFactory.create(identifier).print(identifier, out);

        AST assign = identifier.getNextSibling();

        if (assign != null)
        {
            // special handling for long literal string assignments which are
            // wrapped with operators
            if ((type != null) && STRING.equals(type.getFirstChild().getText()))
            {
                switch (assign.getType())
                {
                    case JavaTokenTypes.ASSIGN :

                        if (isLongStringLiteral(assign, out))
                        {
                            AssignmentPrinter.getInstance().print(assign, true, out);

                            break;
                        }

                    default :
                        PrinterFactory.create(assign).print(assign, out);

                        break;
                }
            }
            else
            {
                PrinterFactory.create(assign).print(assign, out);
            }
        }

        boolean innerClass = (out.last == JavaTokenTypes.CLASS_DEF);

        if (assign != null)
        {
            AST semi = assign.getNextSibling();

            if (semi != null)
            {
                PrinterFactory.create(semi).print(semi, out);
            }
        }

        if (newChunk)
        {
            out.state.assignOffset = AssignmentPrinter.OFFSET_NONE;
        }

        // if we've printed an anonymous inner class we don't adjust the type
        // to get the right behaviour for the newline handling
        if (innerClass)
        {
            out.last = JavaTokenTypes.RCURLY;
        }
        else
        {
            out.last = JavaTokenTypes.VARIABLE_DEF;
        }
    }


    /**
     * Indicates whether the given node denotes the start of a new chunk.
     *
     * @param node a VARIABLE_DEF node.
     * @param last the type of the last node printed.
     *
     * @return <code>true</code> if the node starts a new chunk.
     */
    boolean isNewChunk(
        AST node,
        int last)
    {
        JavaNode n = (JavaNode) node;

        if (
            this.settings.getBoolean(
                ConventionKeys.CHUNKS_BY_COMMENTS, ConventionDefaults.CHUNKS_BY_COMMENTS))
        {
            if (n.hasCommentsBefore())
            {
                return true;
            }
        }

        switch (last)
        {
            case JavaTokenTypes.VARIABLE_DEF :

                int maxLinesBetween =
                    this.settings.getInt(
                        ConventionKeys.BLANK_LINES_KEEP_UP_TO,
                        ConventionDefaults.BLANK_LINES_KEEP_UP_TO);

                // it does not make sense to mark chunks by blank lines if no
                // blank lines should be retained
                if (maxLinesBetween > 0)
                {
                    if (
                        this.settings.getBoolean(
                            ConventionKeys.CHUNKS_BY_BLANK_LINES,
                            ConventionDefaults.CHUNKS_BY_BLANK_LINES))
                    {
                        if (
                            (n.getStartLine() - n.getPreviousSibling().getStartLine() - 1) > maxLinesBetween)
                        {
                            return true;
                        }
                    }

                    return false;
                }
        }

        return false;
    }


    /**
     * Determines whether the node needs special wrapping (for long string literal
     * assignments).
     *
     * @param node node to check.
     * @param out stream to write to.
     *
     * @return <code>true</code> if the node needs special wrapping.
     *
     * @throws IOException if an I/O error occured.
     */
    private boolean isLongStringLiteral(
        AST        node,
        NodeWriter out)
      throws IOException
    {
        boolean result = false;
        boolean possible = false;
        int lineLength =
            this.settings.getInt(
                ConventionKeys.LINE_LENGTH, ConventionDefaults.LINE_LENGTH);
LOOP: 
        for (AST child = node.getFirstChild(); child != null;
            child = child.getFirstChild())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.PLUS :
                    possible = true;

                    break;

                case JavaTokenTypes.STRING_LITERAL :

                    if (
                        possible
                        && ((out.column + child.getText().length()) > lineLength))
                    {
                        result = true;
                    }

                    break LOOP;
            }
        }

        return result;
    }


    /**
     * Outputs whitespace to align the identifier of the given VARIABLE_DEF under prior
     * VARIABLE_DEF nodes.
     *
     * @param node the current VARIABLE_DEF to print.
     * @param last the type of the last node printed.
     * @param out stream to write to.
     *
     * @return <code>true</code> if the node was aligned.
     *
     * @throws IOException if an I/O error occured.
     */
    private boolean alignVariable(
        AST        node,
        int        last,
        NodeWriter out)
      throws IOException
    {
        boolean result = false;

        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            AST next = node.getNextSibling();

            if (
                (last != JavaTokenTypes.VARIABLE_DEF)
                || (out.state.variableOffset == OFFSET_NONE))
            {
                // first VARIABLE_DEF in a row, so we check for further
                // variables
                if (next != null)
                {
                    out.state.variableOffset = OFFSET_NONE;
                    result = true;

                    switch (next.getType())
                    {
                        case JavaTokenTypes.VARIABLE_DEF :

                            TestNodeWriter tester = out.testers.get();
                            int length = OFFSET_NONE;
SEARCH: 

                            // determine the longest VARIABLE_DEF
                            for (AST def = node; def != null;
                                def = def.getNextSibling())
                            {
                                switch (def.getType())
                                {
                                    case JavaTokenTypes.VARIABLE_DEF :
                                        tester.reset();

                                        AST defModifier = def.getFirstChild();
                                        AST defType = defModifier.getNextSibling();
                                        PrinterFactory.create(defModifier).print(
                                            defModifier, tester);
                                        PrinterFactory.create(defType).print(
                                            defType, tester);

                                        /**
                                         * @todo add new max. length setting
                                         */
                                        if (tester.length > length)
                                        {
                                            length = tester.length;
                                        }

                                        if (
                                            isNewChunk(
                                                (JavaNode) def.getNextSibling(),
                                                JavaTokenTypes.VARIABLE_DEF))
                                        {
                                            break SEARCH;
                                        }

                                        break;

                                    default :
                                        break SEARCH;
                                }
                            }

                            out.testers.release(tester);

                            // set the state variable, now the following
                            // VARIABLE_DEFs can be aligned
                            out.state.variableOffset = length + out.getIndentLength() + 1;

                            break;

                        default :
                            break;
                    }
                }
            }

            if (out.state.variableOffset != OFFSET_NONE)
            {
                // check if this variable needs to be indented
                if (out.state.variableOffset > out.column)
                {
                    out.print(
                        out.getString(out.state.variableOffset - out.column),
                        JavaTokenTypes.WS);
                }

                // check if following variables needs to be indented
                switch (next.getType())
                {
                    case JavaTokenTypes.VARIABLE_DEF :

                        if (isNewChunk((JavaNode) next, last))
                        {
                            out.state.variableOffset = OFFSET_NONE;
                            result = true;
                        }

                        break;

                    default :
                        out.state.variableOffset = OFFSET_NONE;
                        result = true;

                        break;
                }
            }
        }

        return result;
    }
}
