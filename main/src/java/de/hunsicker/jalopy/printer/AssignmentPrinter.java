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
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for assignments [<code>ASSIGN</code>]. These represent either
 * assignment statements or the variable declaration assignments.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class AssignmentPrinter
    extends OperatorPrinter
{
    //~ Static variables/initializers ·········································

    static final int OFFSET_NONE = -1;

    /** Singleton. */
    private static final AssignmentPrinter INSTANCE = new AssignmentPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new AssignmentPrinter object.
     */
    private AssignmentPrinter()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns the sole instance of this class.
     *
     * @return the sole instance of this class.
     */
    public static AssignmentPrinter getInstance()
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
        print(node, false, out);
    }


    /**
     * Prints the given node.
     *
     * @param node node to print.
     * @param wrapAfterAssign if <code>true</code> the value won't be aligned
     *        after the '=', but rather the indentation level will be
     *        increased and the value will be printed like a block.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    public void print(AST        node,
                      boolean    wrapAfterAssign,
                      NodeWriter out)
        throws IOException
    {
        logIssues(node, out);
        printCommentsBefore(node, out);

        boolean wrapLines = this.prefs.getBoolean(Keys.LINE_WRAP,
                                                  Defaults.LINE_WRAP) &&
                            (out.mode == NodeWriter.MODE_DEFAULT);
        boolean preferWrapAfterLeftParen = this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_LEFT_PAREN,
                                                                 Defaults.LINE_WRAP_AFTER_LEFT_PAREN);
        boolean preferWrapAfterAssign = this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_ASSIGN,
                                                              Defaults.LINE_WRAP_AFTER_ASSIGN);
        boolean padding = this.prefs.getBoolean(Keys.PADDING_ASSIGNMENT_OPERATORS,
                                                Defaults.PADDING_ASSIGNMENT_OPERATORS);
        int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                           Defaults.LINE_LENGTH);
        boolean standardIndent = !this.prefs.getBoolean(Keys.INDENT_DEEP,
                                                        Defaults.INDENT_DEEP);

        AST expr = node.getFirstChild();

        if (isPartOfDeclaration(node))
        {
            if (wrapLines)
            {
                JavaNode parent = ((JavaNode)node).getParent();

                if ((!wrapAfterAssign) &&
                    this.prefs.getBoolean(Keys.ALIGN_VAR_ASSIGNS,
                                          Defaults.ALIGN_VAR_ASSIGNS))
                {
                    if (isNewChunk(parent, JavaTokenTypes.VARIABLE_DEF))
                    {
                        out.state.assignOffset = OFFSET_NONE;
                    }

                    alignAssignment(node, true, out);
                }

                boolean canIndent = canIndent(node);
                boolean indent = (standardIndent || wrapAfterAssign ||
                                  preferWrapAfterAssign ||
                                  preferWrapAfterLeftParen) && canIndent;

                if (indent)
                {
                    out.indent();
                }

                Marker marker = null;

                if (wrapAfterAssign) // force wrap after assign
                {
                    if (padding)
                    {
                        out.print(SPACE_ASSIGN, JavaTokenTypes.ASSIGN);
                    }
                    else
                    {
                        out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                    }

                    out.printNewline();
                    printIndentation(out);
                    marker = out.state.markers.add();
                    PrinterFactory.create(expr).print(expr, out);
                }
                else if (canIndent)
                {
                    TestNodeWriter tester = out.testers.get();
                    PrinterFactory.create(expr).print(expr, tester);

                    // prefer wrap after assign
                    if ((preferWrapAfterAssign) && ((tester.length + out.column + (padding ? 3
                                                                : 1)) > lineLength)
                        )
                    {
                        if (padding)
                        {
                            out.print(SPACE_ASSIGN, JavaTokenTypes.ASSIGN);
                        }
                        else
                        {
                            out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                        }

                        out.printNewline();
                        printIndentation(out);
                    }
                    else if (standardIndent)
                    {
                        if (out.column > this.prefs.getInt(
                                                           Keys.INDENT_SIZE_DEEP,
                                                           Defaults.INDENT_SIZE_DEEP))
                        {
                            if (padding)
                            {
                                out.print(SPACE_ASSIGN, JavaTokenTypes.ASSIGN);
                            }
                            else
                            {
                                out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                            }

                            out.state.markers.add();
                            out.printNewline();
                            printIndentation(out);
                        }
                        else
                        {
                            if (padding)
                            {
                                marker = out.state.markers.add(out.line,
                                                               out.column + 2);
                                out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                            }
                            else
                            {
                                out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                                out.state.markers.add();
                            }
                        }
                    }
                    else if (padding)
                    {
                        marker = out.state.markers.add(out.line, out.column + 2);
                        out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                    }
                    else
                    {
                        out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                        out.state.markers.add();
                    }

                    PrinterFactory.create(expr).print(expr, out);

                    if (indent)
                    {
                        out.unindent();
                    }

                    out.testers.release(tester);

                    if (marker != null)
                    {
                        out.state.markers.remove(marker);
                    }
                }
                else
                {
                    if (padding)
                    {
                        // ArrayInitializerPrinter.java wraps if the curly
                        // brace should be printed on its own line, so we try
                        // to avoid trailing whitespace
                        if (this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                                  Defaults.BRACE_NEWLINE_LEFT))
                        {
                            out.print(SPACE_ASSIGN, JavaTokenTypes.ASSIGN);
                        }
                        else
                        {
                            out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                        }
                    }
                    else
                    {
                        out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                    }

                    PrinterFactory.create(expr).print(expr, out);
                }
            }
            else // never perform wrapping
            {
                if (padding)
                {
                    out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                }
                else
                {
                    out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                }

                PrinterFactory.create(expr).print(expr, out);
            }
        }
        else // assignment expression
        {
            AST rhs = printLeftHandSide(node, out);

            if (out.mode == NodeWriter.MODE_DEFAULT)
            {
                TestNodeWriter tester = out.testers.get();
                PrinterFactory.create(rhs).print(rhs, tester);

                boolean indent = (standardIndent || wrapAfterAssign ||
                                 preferWrapAfterAssign ||
                                 preferWrapAfterLeftParen);

                if (indent)
                {
                    out.indent();
                }

                Marker marker = null;

                if (preferWrapAfterAssign &&
                    (out.mode == NodeWriter.MODE_DEFAULT) &&
                    ((out.column + (padding ? 3
                                            : 1) + tester.length) > lineLength))
                {
                    if (padding)
                    {
                        out.print(SPACE_ASSIGN, JavaTokenTypes.ASSIGN);
                    }
                    else
                    {
                        out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                    }

                    out.printNewline();
                    printIndentation(out);
                    marker = out.state.markers.add();
                }
                else
                {
                    if (this.prefs.getBoolean(Keys.ALIGN_VAR_ASSIGNS,
                                              Defaults.ALIGN_VAR_ASSIGNS))
                    {
                        JavaNode parent = ((JavaNode)node).getParent();

                        if (isNewChunk(parent, JavaTokenTypes.ASSIGN))
                        {
                            out.state.assignOffset = OFFSET_NONE;
                        }

                        if (canAlign((JavaNode)node))
                        {
                            alignAssignment(node, false, out);
                        }
                    }

                    if (padding)
                    {
                        marker = out.state.markers.add(out.line, out.column + 2);
                        out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                    }
                    else
                    {
                        out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                        marker = out.state.markers.add();
                    }

                    out.testers.release(tester);
                }

                printRightHandSide(rhs, out);

                if (indent)
                {
                    out.unindent();
                }

                if (marker != null)
                {
                    out.state.markers.remove(marker);
                }
            }
            else
            {
                if (padding)
                {
                    out.print(ASSIGN_PADDED, JavaTokenTypes.ASSIGN);
                }
                else
                {
                    out.print(ASSIGN, JavaTokenTypes.ASSIGN);
                }

                printRightHandSide(rhs, out);
            }
        }
    }


    void align(int        amount,
               NodeWriter out)
        throws IOException
    {
        out.print(out.getString(amount), JavaTokenTypes.WS);
    }


    /**
     * Checks whether the given node denotes an assignment.
     *
     * @param node EXPR node to check.
     *
     * @return <code>true</code> if the given node denotes an assignment.
     */
    private boolean isAssignment(AST node)
    {
        AST child = node.getFirstChild();

        switch (child.getType())
        {
            case JavaTokenTypes.ASSIGN :

                switch (child.getFirstChild().getType())
                {
                    case JavaTokenTypes.QUESTION :
                        return false;

                    default :
                        return true;
                }

            default :
                return false;
        }
    }


    /**
     * Determines whether the given node marks the start of a new chunk.
     *
     * @param node a VARIABLE_DEF or EXPR node.
     * @param type the node type for which the chunk state should be
     *        determined, either VARIABLE_DEF or ASSIGN.
     *
     * @return <code>true</code> if the node marks a new chunk.
     */
    private boolean isNewChunk(AST node,
                               int type)
    {
        JavaNode n = (JavaNode)node;

        // special handling of 'for' statements
        switch (n.getParent().getType())
        {
            case JavaTokenTypes.FOR_INIT :
                return true;
        }

        if (this.prefs.getBoolean(Keys.CHUNKS_BY_COMMENTS,
                                  Defaults.CHUNKS_BY_COMMENTS))
        {
            if (n.hasCommentsBefore())
            {
                return true;
            }
        }

        int maxLinesBetween = this.prefs.getInt(Keys.BLANK_LINES_KEEP_UP_TO,
                                                Defaults.BLANK_LINES_KEEP_UP_TO);

        // it does not make sense to mark chunks by blank lines if no blank
        // lines will be kept
        if (maxLinesBetween > 0)
        {
            if (this.prefs.getBoolean(Keys.CHUNKS_BY_BLANK_LINES,
                                      Defaults.CHUNKS_BY_BLANK_LINES))
            {
                JavaNode prev = n.getPreviousSibling();

                switch (type)
                {
                    // align assignment part of VARIABLE_DEF
                    case JavaTokenTypes.VARIABLE_DEF :

                        switch (prev.getType())
                        {
                            case JavaTokenTypes.VARIABLE_DEF :

                                if (maxLinesBetween > 0)
                                {
                                    if ((n.getStartLine() - n.getPreviousSibling()
                                                             .getStartLine() -
                                         1) >= maxLinesBetween)
                                    {
                                        return true;
                                    }
                                }

                                return false;
                        }

                        break;

                    case JavaTokenTypes.ASSIGN :

                        switch (prev.getType())
                        {
                            case JavaTokenTypes.EXPR :

                                if (isAssignment(prev))
                                {
                                    if (maxLinesBetween > 0)
                                    {
                                        if ((n.getStartLine() - n.getPreviousSibling()
                                                                 .getStartLine() -
                                             1) >= maxLinesBetween)
                                        {
                                            return true;
                                        }
                                    }
                                }

                                return false;

                            case JavaTokenTypes.VARIABLE_DEF :

                                if (maxLinesBetween > 0)
                                {
                                    if ((n.getStartLine() - n.getPreviousSibling()
                                                             .getStartLine() -
                                         1) > maxLinesBetween)
                                    {
                                        return true;
                                    }
                                }

                                return false;
                        }

                        break;
                }
            }
        }

        return false;
    }


    /**
     * Determines the next sibling of the given ASSIGN node.
     *
     * @param node the ASSIGN node.
     * @param parent the parent node of the ASSIGN node.
     *
     * @return the next sibling, returns <code>null</code> if no sibling could
     *         be found.
     *
     * @throws IllegalArgumentException if an unexpected <em>parent</em> type
     *         was given.
     */
    private AST getNextSibling(AST      node,
                               JavaNode parent)
    {
        /**
         * @todo are really all cases handled?
         */
        switch (parent.getType())
        {
            case JavaTokenTypes.EXPR :
            case JavaTokenTypes.VARIABLE_DEF :
                return parent.getNextSibling();

            case JavaTokenTypes.ASSIGN :
            case JavaTokenTypes.QUESTION :
            case JavaTokenTypes.EQUAL :
            case JavaTokenTypes.NOT_EQUAL :
            case JavaTokenTypes.LT :
            case JavaTokenTypes.GT :
                return getNextSibling(parent, parent.getParent());

            /*case JavaTokenTypes.LITERAL_if:
            case JavaTokenTypes.LITERAL_else:
            case JavaTokenTypes.LITERAL_for:
            case JavaTokenTypes.LITERAL_while:
            case JavaTokenTypes.LITERAL_do:
            case JavaTokenTypes.LITERAL_try:
            case JavaTokenTypes.LITERAL_catch:
            case JavaTokenTypes.LITERAL_finally:
            case JavaTokenTypes.LITERAL_synchronized:
            case JavaTokenTypes.LCURLY:
            case JavaTokenTypes.SLIST:
            case JavaTokenTypes.OBJBLOCK:
                return null;*/
            default :
                throw new IllegalArgumentException("unexpected parent node --" +
                                                   parent);
        }
    }


    /**
     * Determines whether the given assignment is part of a variable
     * declaration.
     *
     * @param node an ASSIGN node.
     *
     * @return <code>true</code> if the node is part of a declaration.
     *
     * @since 1.0b9
     */
    private boolean isPartOfDeclaration(AST node)
    {
        return ((JavaNode)node).getParent().getType() == JavaTokenTypes.VARIABLE_DEF;
    }


    /**
     * Outputs whitespace to align the assignment of the given node under
     * prior assignments or variable definitions.
     *
     * @param node the current ASSIGN node to print.
     * @param variableAssign <code>true</code> indicates that this assignment
     *        is part of a variable declaration.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void alignAssignment(AST        node,
                                 boolean    variableAssign,
                                 NodeWriter out)
        throws IOException
    {
        JavaNode parent = ((JavaNode)node).getParent();
        AST next = getNextSibling(node, parent);

        // offset already defined, succesive assignment
        if (out.state.assignOffset != OFFSET_NONE)
        {
            // align if necessary
            if (out.column < out.state.assignOffset)
            {
                align(out.state.assignOffset - out.column, out);
            }

            // delete the offset if we're not followed by a VARIABLE_DEF or
            // ASSIGN node
            if (next != null)
            {
                switch (next.getType())
                {
                    case JavaTokenTypes.VARIABLE_DEF :

                        if (isNewChunk(next, JavaTokenTypes.VARIABLE_DEF))
                        {
                            out.state.assignOffset = OFFSET_NONE;
                        }

                        break;

                    case JavaTokenTypes.EXPR :

                        if ((!isAssignment(next)) ||
                            isNewChunk(next, JavaTokenTypes.ASSIGN))
                        {
                            out.state.assignOffset = OFFSET_NONE;
                        }

                        break;

                    default :
                        out.state.assignOffset = OFFSET_NONE;

                        break;
                }
            }
        }
        else // no offset defined, this is the first VARIABLE_DEF/ASSIGN
        {
            if (next != null)
            {
                switch (next.getType())
                {
                    case JavaTokenTypes.EXPR :
                    {
                        if (variableAssign || (!isAssignment(next)))
                        {
                            out.state.assignOffset = OFFSET_NONE;

                            break;
                        }

                        //boolean lastAssign = isNewChunk(next, JavaTokenTypes.ASSIGN); // last chunk
                        int length = 0;
                        TestNodeWriter tester = out.testers.get();
SEARCH:
                        for (AST def = parent;
                             def != null;
                             def = def.getNextSibling())
                        {
                            switch (def.getType())
                            {
                                case JavaTokenTypes.EXPR :

                                    // if the next sibling is an assignment
                                    if (isAssignment(def))
                                    {
                                        tester.reset();

                                        AST rhs = def.getFirstChild()
                                                     .getFirstChild();
                                        PrinterFactory.create(rhs)
                                                      .print(rhs, tester);

                                        if (tester.length > length)
                                        {
                                            length = tester.length;
                                        }

                                        AST t = def.getNextSibling();

                                        if (t != null)
                                        {
                                            if (isNewChunk(t,
                                                           JavaTokenTypes.ASSIGN))
                                            {
                                                break SEARCH;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        // no assignment found, quit
                                        break SEARCH;
                                    }

                                    break;
                            }
                        }

                        out.state.assignOffset = length + out.getIndentLength() +
                                                 1;

                        // align if necessary
                        if (out.column < out.state.assignOffset)
                        {
                            align(out.state.assignOffset - out.column, out);
                        }

                        /*if (lastAssign)
                        {
                            out.state.assignOffset = OFFSET_NONE;
                        }*/
                        out.testers.release(tester);

                        break;
                    }

                    case JavaTokenTypes.VARIABLE_DEF :
                    {
                        if (!variableAssign)
                        {
                            out.state.assignOffset = OFFSET_NONE;

                            break;
                        }

                        int length = OFFSET_NONE;

                        //boolean lastAssign = false;
                        TestNodeWriter tester = out.testers.get();
                        boolean alignVariables = this.prefs.getBoolean(Keys.ALIGN_VAR_IDENTS,
                                                                       Defaults.ALIGN_VAR_IDENTS);
SEARCH:

                        // determine the longest VARIABLE_DEF or ASSIGN
                        for (AST def = parent;
                             def != null;
                             def = def.getNextSibling())
                        {
                            switch (def.getType())
                            {
                                case JavaTokenTypes.EXPR :

                                    // if the next sibling is an assignment
                                    if (isAssignment(def))
                                    {
                                        tester.reset();

                                        AST rhs = def.getFirstChild()
                                                     .getFirstChild();
                                        PrinterFactory.create(rhs)
                                                      .print(rhs, tester);

                                        if (tester.length > length)
                                        {
                                            length = tester.length;
                                        }

                                        if (isNewChunk(def,
                                                       JavaTokenTypes.ASSIGN))
                                        {
                                            break SEARCH;
                                        }
                                    }
                                    else
                                    {
                                        // no assignment found, quit
                                        break SEARCH;
                                    }

                                    break;

                                case JavaTokenTypes.VARIABLE_DEF :
                                    tester.reset();

                                    AST defModifier = def.getFirstChild();
                                    PrinterFactory.create(defModifier)
                                                  .print(defModifier, tester);

                                    AST defType = defModifier.getNextSibling();
                                    PrinterFactory.create(defType)
                                                  .print(defType, tester);

                                    // we have to adjust the length in case
                                    // variable alignment is performed
                                    if (alignVariables &&
                                        (out.state.variableOffset != VariableDeclarationPrinter.OFFSET_NONE))
                                    {
                                        if (out.state.variableOffset > tester.length)
                                        {
                                            tester.length = out.state.variableOffset -
                                                            out.getIndentLength() -
                                                            1;
                                        }
                                    }

                                    AST defIdent = defType.getNextSibling();
                                    PrinterFactory.create(defIdent)
                                                  .print(defIdent, tester);
                                    tester.length++; // space before identifier

                                    if (tester.length > length)
                                    {
                                        length = tester.length;
                                    }

                                    AST n = def.getNextSibling();

                                    if (n != null)
                                    {
                                        if (isNewChunk(n,
                                                       JavaTokenTypes.VARIABLE_DEF))
                                        {
                                            break SEARCH;
                                        }
                                    }

                                    break;

                                default :
                                    break SEARCH;
                            }
                        }

                        out.testers.release(tester);

                        out.state.assignOffset = length + out.getIndentLength() +
                                                 1;

                        // align if necessary
                        if (out.column < out.state.assignOffset)
                        {
                            align(out.state.assignOffset - out.column, out);
                        }

                        //if (lastAssign)
                        //out.state.assignOffset = OFFSET_NONE;
                        break;
                    }

                    default :
                        out.state.assignOffset = OFFSET_NONE;

                        break;
                }
            }
        }
    }


    /**
     * Determines whether the given assignment expression can be aligned.
     *
     * @param node ASSIGN node.
     *
     * @return <code>true</code> if the node can be aligned. ASSIGN nodes
     *         can't be aligned if they are part of a block statement
     *         expression list.
     *
     * @since 1.0b9
     */
    private boolean canAlign(JavaNode node)
    {
        JavaNode parent = node.getParent();

        /**
         * @todo parent should never be null!
         */
        if (parent != null)
        {
            switch (parent.getType())
            {
                case JavaTokenTypes.LITERAL_if :
                case JavaTokenTypes.LITERAL_else :
                case JavaTokenTypes.LITERAL_do :
                case JavaTokenTypes.LITERAL_for :
                case JavaTokenTypes.LITERAL_while :
                case JavaTokenTypes.LITERAL_switch :
                    return false;

                case JavaTokenTypes.SLIST :
                case JavaTokenTypes.OBJBLOCK :
                    return true;

                default :
                    return canAlign(parent);
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * Determines whether the given assignment allows an indenation increase
     * in case a line wrap is necessary.
     *
     * @param node an ASSIGN node.
     *
     * @return <code>true</code> if the first child of the given assignment is
     *         no ARRAY_INIT node.
     *
     * @since 1.0b9
     */
    private boolean canIndent(AST node)
    {
        AST child = node.getFirstChild();

        if (child != null)
        {
            switch (child.getType())
            {
                case JavaTokenTypes.ARRAY_INIT :
                    return false;
            }
        }

        return true;
    }
}
