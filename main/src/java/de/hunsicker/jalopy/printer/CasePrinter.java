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
 * Printer for case blocks and case expressions.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class CasePrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Singleton. */
    private static final Printer INSTANCE = new CasePrinter();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CasePrinter object.
     */
    private CasePrinter()
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
        switch (node.getType())
        {
            case JavaTokenTypes.LITERAL_case :
            {
                printCommentsBefore(node, out);

                int offset = out.print(CASE_SPACE, JavaTokenTypes.LITERAL_case);

                trackPosition((JavaNode) node, out.line, offset, out);

                AST expr = node.getFirstChild();
                PrinterFactory.create(expr).print(expr, out);

                if (
                    this.settings.getBoolean(
                        ConventionKeys.SPACE_BEFORE_CASE_COLON,
                        ConventionDefaults.SPACE_BEFORE_CASE_COLON))
                {
                    out.print(SPACE, JavaTokenTypes.WS);
                }

                out.print(COLON, JavaTokenTypes.LITERAL_case);

                AST colon = expr.getNextSibling();

                if (
                    !printCommentsAfter(
                        colon, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO, out))
                {
                    if (this.settings.getBoolean(ConventionKeys.BRACE_NEWLINE_LEFT, ConventionDefaults.BRACE_NEWLINE_LEFT))
                        out.printNewline();
                }

                out.last = JavaTokenTypes.LITERAL_case;

                break;
            }

            case JavaTokenTypes.CASE_GROUP :
LOOP:
                for (
                    AST child = node.getFirstChild(); child != null;
                    child = child.getNextSibling())
                {
                    switch (child.getType())
                    {
                        case JavaTokenTypes.COLON :

                            if (
                                !printCommentsAfter(
                                    child, NodeWriter.NEWLINE_NO, NodeWriter.NEWLINE_NO,
                                    out))
                            {
                                if (this.settings.getBoolean(ConventionKeys.BRACE_NEWLINE_LEFT, ConventionDefaults.BRACE_NEWLINE_LEFT))
                                    out.printNewline();
                            }

                            continue LOOP;

                        case JavaTokenTypes.SLIST :

                            // don't print empty lists, as these would break
                            // our indentation
                            if (NodeHelper.isEmptyBlock(child))
                            {
                                continue LOOP;
                            }

                            break;

                        case JavaTokenTypes.LITERAL_default :

                            AST sibling = child.getNextSibling();

                            // I really don't know why, but one may use the
                            // default statement *not* as the last statement in
                            // the switch selection, so we have to check here
                            // whether another case follows, and only if not we
                            // apply the test
                            switch (sibling.getType())
                            {
                                case JavaTokenTypes.LITERAL_case :
                                case JavaTokenTypes.COLON :
                                    break;

                                default :

                                    if (NodeHelper.isEmptyBlock(sibling))
                                    {
                                        break LOOP;
                                    }
                            }

                            break;
                    }

                    PrinterFactory.create(child).print(child, out);
                }

                break;

            case JavaTokenTypes.LITERAL_default :
            {
                printCommentsBefore(node, out);

                int offset = 1;

                if (
                    this.settings.getBoolean(
                        ConventionKeys.SPACE_BEFORE_CASE_COLON, false))
                {
                    offset =
                        out.print(DEFAULT_SPACE_COLON, JavaTokenTypes.LITERAL_default);
                }
                else
                {
                    offset = out.print(DEFAULT_COLON, JavaTokenTypes.LITERAL_default);
                }

                trackPosition((JavaNode) node, out.line, offset, out);

                out.last = JavaTokenTypes.LITERAL_default;

                break;
            }
        }
    }
}
