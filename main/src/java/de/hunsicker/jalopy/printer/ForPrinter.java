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
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;


/**
 * Printer for <code>for</code> loops [<code>LITERAL_for</code>].
 * <pre style="background:lightgrey">
 * <strong>for </strong>(<em>initialization</em>; <em>Boolean-expression</em>; <em>step</em>;)
 * {
 *    <em>statement</em>
 * }
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ForPrinter
    extends BlockStatementPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ForPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ForPrinter object.
     */
    protected ForPrinter()
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
        super.print(node, out);

        if (this.prefs.getBoolean(Keys.SPACE_BEFORE_STATEMENT_PAREN,
                                  Defaults.SPACE_BEFORE_STATEMENT_PAREN))
        {
            out.print(FOR_SPACE, JavaTokenTypes.LITERAL_for);
        }
        else
        {
            out.print(FOR, JavaTokenTypes.LITERAL_for);
        }

        AST lparen = node.getFirstChild();
        PrinterFactory.create(lparen).print(lparen, out);

        Marker marker = out.state.markers.add();
        int mark = out.column - 1;

        /**
         * @todo print comments after semis
         */
        AST forInit = lparen.getNextSibling();
        AST firstSemi = forInit.getNextSibling();
        AST forCond = firstSemi.getNextSibling();
        AST secondSemi = forCond.getNextSibling();
        AST forIter = secondSemi.getNextSibling();

        boolean deepIndent = this.prefs.getBoolean(Keys.INDENT_DEEP,
                                                   Defaults.INDENT_DEEP);

        boolean firstWrap = false;

        // check if we should wrap the first part
        if (out.mode == NodeWriter.MODE_DEFAULT)
        {
            out.state.paramLevel++;
            out.state.parenScope.addFirst(new ParenthesesScope(out.state.paramLevel));

            if (this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_LEFT_PAREN,
                                      Defaults.LINE_WRAP_AFTER_LEFT_PAREN))
            {
                TestNodeWriter tester = out.testers.get();

                AST child = forInit.getFirstChild();

                for (AST c = child; c != null; c = c.getNextSibling())
                {
                    PrinterFactory.create(c).print(c, tester);
                }

                child = forCond.getFirstChild();

                if (child != null)
                {
                    PrinterFactory.create(child).print(child, tester);
                }

                child = forIter.getFirstChild();

                if (child != null)
                {
                    PrinterFactory.create(child).print(child, tester);
                }

                int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                                   Defaults.LINE_LENGTH);

                if ((out.column + tester.length) > lineLength)
                {
                    out.printNewline();
                    firstWrap = true;

                    /*int indentation = this.prefs.getInt(Keys.INDENT_SIZE_PARAMETERS,
                                                        Defaults.INDENT_SIZE_PARAMETERS);

                    if (indentation == -1)
                    {
                        out.print(out.getString(out.indentSize),
                                  JavaTokenTypes.WS);
                    }
                    else
                    {
                        out.print(out.getString(indentation),
                                  JavaTokenTypes.WS);
                    }*/
                    if (deepIndent)
                    {
                        out.print(out.getString(mark - out.getIndentLength()),
                                  JavaTokenTypes.WS);
                    }
                    else
                    {
                        out.print(out.getString(out.indentSize),
                                  JavaTokenTypes.WS);
                        mark = out.column - 1;
                    }
                }

                out.testers.release(tester);
            }
        }

        printForInit(forInit, out);

        boolean wrap = firstWrap;

        // check if we should wrap the second and third part
        if ((!firstWrap) &&
            this.prefs.getBoolean(Keys.LINE_WRAP, Defaults.LINE_WRAP) &&
            (out.mode == NodeWriter.MODE_DEFAULT))
        {
            TestNodeWriter tester = out.testers.get();
            printForCond(forCond, tester, -1);
            printForIter(forIter, tester, -1);

            int lineLength = this.prefs.getInt(Keys.LINE_LENGTH,
                                               Defaults.LINE_LENGTH);

            if ((tester.length + out.column) > lineLength)
            {
                wrap = true;
            }

            out.testers.release(tester);
        }

        printForCond(forCond, out, wrap ? mark
                                        : (-1));
        printForIter(forIter, out, wrap ? mark
                                        : (-1));

        if (wrap &&
            this.prefs.getBoolean(Keys.LINE_WRAP_BEFORE_RIGHT_PAREN,
                                  Defaults.LINE_WRAP_BEFORE_RIGHT_PAREN))
        {
            if (!out.newline)
            {
                out.printNewline();

                if (deepIndent)
                {
                    out.print(out.getString(mark - 1 - out.getIndentLength()),
                              JavaTokenTypes.WS);
                }
            }

            out.print(EMPTY_STRING, JavaTokenTypes.WS);
        }

        AST rparen = forIter.getNextSibling();

        PrinterFactory.create(rparen).print(rparen, out);

        out.state.markers.remove(marker);

        if (out.mode == out.MODE_DEFAULT)
        {
            out.state.paramLevel--;
            out.state.parenScope.removeFirst();
        }

        out.last = JavaTokenTypes.LITERAL_for;

        AST body = rparen.getNextSibling();

        switch (body.getType())
        {
            case JavaTokenTypes.SLIST :
                PrinterFactory.create(body).print(body, out);

                break;

            default :

                // insert braces manually
                if (this.prefs.getBoolean(Keys.BRACE_INSERT_FOR,
                                          Defaults.BRACE_INSERT_FOR))
                {
                    out.printLeftBrace(this.prefs.getBoolean(
                                                             Keys.BRACE_NEWLINE_LEFT,
                                                             Defaults.BRACE_NEWLINE_LEFT),
                                       NodeWriter.NEWLINE_YES);
                    PrinterFactory.create(body).print(body, out);
                    out.printRightBrace();
                }
                else
                {
                    out.printNewline();
                    out.indent();
                    PrinterFactory.create(body).print(body, out);
                    out.unindent();
                }
        }

        // to simplify line wrapping we always indicate braces
        out.last = JavaTokenTypes.RCURLY;
    }


    /**
     * Prints the condition part of the for loop.
     *
     * @param node the condition part node of the loop.
     * @param out stream to write to.
     * @param mark
     *
     * @throws IOException if an I/O error occured.
     */
    private void printForCond(AST        node,
                              NodeWriter out,
                              int        mark)
        throws IOException
    {
        out.print(SEMI, JavaTokenTypes.FOR_INIT);

        if (node.getFirstChild() == null)
        {
            return;
        }

        if (mark > -1)
        {
            out.printNewline();
            out.print(out.getString(mark - out.getIndentLength()),
                      JavaTokenTypes.WS);
        }
        else if ((mark == -1) &&
                 this.prefs.getBoolean(Keys.SPACE_AFTER_SEMICOLON,
                                       Defaults.SPACE_AFTER_SEMICOLON))
        {
            out.print(SPACE, JavaTokenTypes.FOR_INIT);
        }

        for (AST child = node.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            PrinterFactory.create(child).print(child, out);
        }
    }


    /**
     * Prints the initialization part of the for loop.
     *
     * @param node the initialization part node of the loop.
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printForInit(AST        node,
                              NodeWriter out)
        throws IOException
    {
        AST child = node.getFirstChild();

        if (child == null)
        {
            return;
        }

        printVariableDefs(node, out);
    }


    /**
     * Prints the iteration part of the for loop.
     *
     * @param node the iteration part node of the loop.
     * @param out stream to write to.
     * @param mark
     *
     * @throws IOException if an I/O error occured.
     */
    private void printForIter(AST        node,
                              NodeWriter out,
                              int        mark)
        throws IOException
    {
        out.print(SEMI, JavaTokenTypes.FOR_INIT);

        AST elist = node.getFirstChild();

        if (elist == null)
        {
            return;
        }

        if (mark > -1)
        {
            out.printNewline();
            out.print(out.getString(mark - out.getIndentLength()),
                      JavaTokenTypes.WS);
        }
        else if ((mark == -1) &&
                 this.prefs.getBoolean(Keys.SPACE_AFTER_SEMICOLON,
                                       Defaults.SPACE_AFTER_SEMICOLON))
        {
            out.print(SPACE, JavaTokenTypes.FOR_INIT);
        }

        boolean spaceAfterComma = this.prefs.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                        Defaults.SPACE_AFTER_SEMICOLON);
        String comma = spaceAfterComma ? COMMA_SPACE
                                       : COMMA;

        // we don't use ParametersPrinter.java because of the
        // automated added parentheses, which would lead to compile
        // errors here
        for (AST element = elist.getFirstChild();
             element != null;
             element = element.getNextSibling())
        {
            switch (element.getType())
            {
                case JavaTokenTypes.COMMA :
                    out.print(comma, JavaTokenTypes.COMMA);

                    break;

                default :
                    PrinterFactory.create(element).print(element, out);

                    break;
            }
        }
    }


    /**
     * Outputs a single variable definition of the initialization part of the
     * for loop.
     *
     * @param node a node.
     * @param out the output stream to write to.
     * @param printType
     *
     * @throws IOException if an I/O error occured.
     */
    private void printVariableDef(AST        node,
                                  NodeWriter out,
                                  boolean    printType)
        throws IOException
    {
        // the initialization part of the loop can either consist of variable
        // definitions or assignment expressions
        // for (int i = 0;;){...}  vs.  for (i = 0;;){...}
        switch (node.getType())
        {
            // one or more assignment expressions
            case JavaTokenTypes.ELIST :

                boolean spaceAfterComma = this.prefs.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                                Defaults.SPACE_AFTER_COMMA);
                String comma = spaceAfterComma ? COMMA_SPACE
                                               : COMMA;

                // don't use ParametersPrinter.java because of the added
                // parenthesis
                for (AST param = node.getFirstChild();
                     param != null;
                     param = param.getNextSibling())
                {
                    PrinterFactory.create(param).print(param, out);

                    /**
                     * @todo space after comma
                     */
                    if (param.getNextSibling() != null)
                    {
                        out.print(comma, JavaTokenTypes.COMMA);
                    }
                }

                return;
        }

        AST mod = node.getFirstChild();
        AST type = mod.getNextSibling();

        if (printType)
        {
            PrinterFactory.create(type).print(type, out);
        }

        AST ident = type.getNextSibling();

        // only insert whitespace if we haven't multiple variable defs in the
        // initialization part
        if (out.last != JavaTokenTypes.COMMA)
        {
            out.print(SPACE, out.last);
        }

        PrinterFactory.create(ident).print(ident, out);

        AST assign = ident.getNextSibling();

        if (assign != null)
        {
            // BUG FIX: if we're printing multiple variable defs but the last one
            // has no assignment then a semi appears which is bad as we always
            // output one in printForCond(), so ignore it
            switch (assign.getType())
            {
                case JavaTokenTypes.SEMI :
                    break;

                default :
                    PrinterFactory.create(assign).print(assign, out);

                    break;
            }
        }
    }


    /**
     * Outputs all variable definitions/initializations of the initialization
     * part of the for loop.
     *
     * @param node a VARIABLE_DEF node.
     * @param out the output stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printVariableDefs(AST        node,
                                   NodeWriter out)
        throws IOException
    {
        AST child = node.getFirstChild();
        boolean spaceAfterComma = this.prefs.getBoolean(Keys.SPACE_AFTER_COMMA,
                                                        Defaults.SPACE_AFTER_COMMA);
        String comma = spaceAfterComma ? COMMA_SPACE
                                       : COMMA;

        switch (child.getType())
        {
            case JavaTokenTypes.VARIABLE_DEF :
                printVariableDef(child, out, true);

                for (child = child.getNextSibling();
                     child != null;
                     child = child.getNextSibling())
                {
                    out.print(comma, JavaTokenTypes.COMMA);
                    printVariableDef(child, out, false);
                }

                break;

            case JavaTokenTypes.ELIST :

                for (AST var = child.getFirstChild();
                     var != null;
                     var = var.getNextSibling())
                {
                    switch (child.getType())
                    {
                        case JavaTokenTypes.COMMA :
                            out.print(comma, JavaTokenTypes.COMMA);

                            break;

                        default :
                            PrinterFactory.create(var).print(var, out);

                            break;
                    }
                }

                break;
        }
    }
}
