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
import de.hunsicker.jalopy.Environment;
import de.hunsicker.jalopy.History;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Key;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Printer for a Java AST.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JavaPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** The empty string array. */
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** Singleton. */
    private static final Printer INSTANCE = new JavaPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new JavaPrinter object.
     */
    protected JavaPrinter()
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
        out.environment.set(Environment.Variable.CONVENTION.getName(),
                            this.prefs.get(Keys.STYLE_NAME, Defaults.STYLE_NAME));

        try
        {
            History.Policy historyPolicy = History.Policy.valueOf(this.prefs.get(
                                                                                 Keys.HISTORY_POLICY,
                                                                                 Defaults.HISTORY_POLICY));
            boolean useCommentHistory = (historyPolicy == History.Policy.COMMENT);
            boolean useHeader = this.prefs.getBoolean(Keys.HEADER, false);

            if (useHeader || useCommentHistory)
            {
                removeHeader(node);
            }

            if (useHeader)
            {
                printHeader(out);
            }

            boolean useFooter = this.prefs.getBoolean(Keys.FOOTER,
                                                      Defaults.FOOTER);

            if (useFooter)
            {
                removeFooter(node);
            }

            for (AST child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                PrinterFactory.create(child).print(child, out);
            }

            if (useFooter)
            {
                printFooter(out);
            }
        }
        finally
        {
            out.environment.unset(Environment.Variable.CONVENTION.getName());
        }
    }


    /**
     * Returns the identify keys as stored in the user preferences.
     *
     * @param key user preferences key.
     *
     * @return identify keys. If no keys are stored, an empty array will be
     *         returned.
     */
    private String[] getKeys(Key key)
    {
        List keys = new ArrayList();
        String str = this.prefs.get(key, EMPTY_STRING);
        String delim = "|";

        for (StringTokenizer tokens = new StringTokenizer(str, delim);
             tokens.hasMoreElements();)
        {
            keys.add(tokens.nextElement());
        }

        return (String[])keys.toArray(EMPTY_STRING_ARRAY);
    }


    /**
     * Returns the last node of the Java AST.
     *
     * @param root root node the AST
     *
     * @return the last node of the AST.
     *
     * @since 1.0b9
     */
    private JavaNode getLastElement(AST root)
    {
        for (AST declaration = root.getFirstChild();
             declaration != null;
             declaration = declaration.getNextSibling())
        {
            // last top-level declaration
            if (declaration.getNextSibling() == null)
            {
                switch (declaration.getType())
                {
                    case JavaTokenTypes.CLASS_DEF :
                    case JavaTokenTypes.INTERFACE_DEF :

                        AST block = NodeHelper.getFirstChild(declaration,
                                                             JavaTokenTypes.OBJBLOCK);

                        for (AST element = block.getFirstChild();
                             element != null;
                             element = element.getNextSibling())
                        {
                            // last RCURLY
                            if (element.getNextSibling() == null)
                            {
                                switch (element.getType())
                                {
                                    case JavaTokenTypes.RCURLY :
                                        return (JavaNode)element;
                                }
                            }
                        }

                        break;

                    case JavaTokenTypes.SEMI :
                        return (JavaNode)declaration;

                    case JavaTokenTypes.PACKAGE_DEF :
                    case JavaTokenTypes.IMPORT :

                        /**
                         * @todo implement
                         */
                        return (JavaNode)declaration;
                }
            }
        }

        throw new IllegalStateException("invalid AST -- " + root);
    }


    /**
     * Returns the text (as individual lines) as stored in the user
     * preferences.
     *
     * @param text header text.
     *
     * @return lines of the header or footer.
     */
    private String[] getLines(String text)
    {
        // we cannot use StringTokenizer because then empty lines would
        // disappear
        return tokenize(text, "\n");
    }


    /**
     * Prints the footer.
     *
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printFooter(NodeWriter out)
        throws IOException
    {
        String text = out.environment.interpolate(this.prefs.get(
                                                                 Keys.FOOTER_TEXT,
                                                                 EMPTY_STRING));
        String[] footer = getLines(text);

        if (footer.length > 0)
        {
            switch (out.last)
            {
                case JavaTokenTypes.RCURLY :
                case JavaTokenTypes.OBJBLOCK :
                case JavaTokenTypes.CLASS_DEF :
                case JavaTokenTypes.INTERFACE_DEF :

                    // print one extra after the last curly brace of a file
                    out.printNewline();

                    break;
            }

            out.printBlankLines(this.prefs.getInt(
                                                  Keys.BLANK_LINES_BEFORE_FOOTER,
                                                  Defaults.BLANK_LINES_BEFORE_FOOTER));

            for (int i = 0; i < footer.length; i++)
            {
                out.print(footer[i], JavaTokenTypes.ML_COMMENT);

                if (i < (footer.length - 1))
                {
                    out.printNewline();
                }
            }

            out.printBlankLines(this.prefs.getInt(
                                                  Keys.BLANK_LINES_AFTER_FOOTER,
                                                  Defaults.BLANK_LINES_AFTER_FOOTER));
        }
    }


    /**
     * Prints the header.
     *
     * @param out stream to write to.
     *
     * @throws IOException if an I/O error occured.
     */
    private void printHeader(NodeWriter out)
        throws IOException
    {
        String text = out.environment.interpolate(this.prefs.get(
                                                                 Keys.HEADER_TEXT,
                                                                 EMPTY_STRING));
        String[] header = getLines(text);

        if (header.length > 0)
        {
            out.printBlankLines(this.prefs.getInt(
                                                  Keys.BLANK_LINES_BEFORE_HEADER,
                                                  Defaults.BLANK_LINES_BEFORE_HEADER));

            for (int i = 0; i < header.length; i++)
            {
                out.print(header[i], JavaTokenTypes.ML_COMMENT);
                out.printNewline();
            }

            out.printBlankLines(this.prefs.getInt(
                                                  Keys.BLANK_LINES_AFTER_HEADER,
                                                  Defaults.BLANK_LINES_AFTER_HEADER));
            out.last = JavaTokenTypes.ML_COMMENT;
        }
    }


    /**
     * Removes the footer.
     *
     * @param root the root node of the Java AST.
     *
     * @since 1.0b8
     */
    private void removeFooter(AST root)
    {
        JavaNode rcurly = getLastElement(root);

        if (rcurly.hasCommentsAfter())
        {
            String[] keys = getKeys(Keys.FOOTER_KEYS);
            int count = 0;
            int smartModeLines = this.prefs.getInt(Keys.FOOTER_SMART_MODE_LINES, 0);
            boolean smartMode = smartModeLines > 0;

            for (CommonHiddenStreamToken comment = rcurly.getHiddenAfter();
                 comment != null;
                 comment = comment.getHiddenAfter())
            {
                switch (comment.getType())
                {
                    case JavaTokenTypes.SL_COMMENT :
                    case JavaTokenTypes.SPECIAL_COMMENT :
                    case JavaTokenTypes.SEPARATOR_COMMENT :

                        if (smartMode && (count < smartModeLines))
                        {
                            removeFooterComment(comment, rcurly);
                        }

                        count++;

                        break;

                    default :

                        for (int j = 0; j < keys.length; j++)
                        {
                            if (comment.getText().indexOf(keys[j]) > -1)
                            {
                                removeFooterComment(comment, rcurly);
                            }
                        }

                        break;
                }
            }
        }
    }


    private void removeFooterComment(CommonHiddenStreamToken comment,
                                     JavaNode                node)
    {
        CommonHiddenStreamToken before = comment.getHiddenBefore();
        CommonHiddenStreamToken after = comment.getHiddenAfter();

        if (after != null)
        {
            after.setHiddenBefore(before);

            if (before != null)
            {
                before.setHiddenAfter(after);
            }
            else
            {
                // we've just removed the first comment after the RCURLY so add
                // the following as the new starting one
                node.setHiddenAfter(after);
            }
        }
        else if ((before != null) && (comment != node.getHiddenAfter()))
        {
            before.setHiddenAfter(after);

            if (after != null)
            {
                after.setHiddenBefore(before);
            }
        }
        else
        {
            // it was the first comment
            node.setHiddenAfter(null);
        }

        comment.setHiddenBefore(null);
    }


    /**
     * Removes the header. This method removes both our 'magic' header and the
     * user header, if any.
     *
     * @param node the root node of the tree.
     *
     * @since 1.0b8
     */
    private void removeHeader(AST node)
    {
        History.Policy historyPolicy = History.Policy.valueOf(this.prefs.get(
                                                                             Keys.HISTORY_POLICY,
                                                                             Defaults.HISTORY_POLICY));
        JavaNode first = (JavaNode)node.getFirstChild();
        String[] keys = getKeys(Keys.HEADER_KEYS);
        int smartModeLines = this.prefs.getInt(Keys.HEADER_SMART_MODE_LINES,
                                               Defaults.HEADER_SMART_MODE_LINES);
        boolean smartMode = (smartModeLines > 0);
        int line = 0;

        for (CommonHiddenStreamToken token = first.getHiddenBefore();
             token != null;
             token = token.getHiddenBefore())
        {
            if (token.getHiddenBefore() == null)
            {
                for (CommonHiddenStreamToken comment = token;
                     (comment != null) && (line <= smartModeLines);
                     comment = comment.getHiddenAfter())
                {
                    switch (comment.getType())
                    {
                        case JavaTokenTypes.ML_COMMENT :
                        case JavaTokenTypes.JAVADOC_COMMENT :
KEY_SEARCH: 
                            for (int j = 0; j < keys.length; j++)
                            {
                                if (comment.getText().indexOf(keys[j]) > -1)
                                {
                                    removeHeaderComment(comment, first);

                                    break KEY_SEARCH;
                                }
                            }

                            break;

                        case JavaTokenTypes.SL_COMMENT :

                            /**
                             * @todo this isn't really fool-proof?
                             */
                            if (comment.getText().indexOf('%') > -1)
                            {
                                removeHeaderComment(comment, first);
                            }
                            else if (smartMode && (line < smartModeLines))
                            {
                                removeHeaderComment(comment, first);
                            }

                            break;
                    }

                    line++;
                }

                break;
            }
        }
    }


    private void removeHeaderComment(CommonHiddenStreamToken comment,
                                     JavaNode                node)
    {
        CommonHiddenStreamToken before = comment.getHiddenBefore();
        CommonHiddenStreamToken after = comment.getHiddenAfter();

        if (after != null)
        {
            after.setHiddenBefore(before);

            if (before != null)
            {
                before.setHiddenAfter(after);
            }
        }
        else if (before != null)
        {
            before.setHiddenAfter(after);

            if (after != null)
            {
                after.setHiddenBefore(before);
            }
        }
        else
        {
            // if this is the first comment
            if (comment == node.getHiddenBefore())
            {
                node.setHiddenBefore(null);
            }
        }
    }


    /**
     * Breaks the given string into tokens.
     *
     * @param str string to break into token.
     * @param delim the delimeter.
     *
     * @return array with the tokens.
     *
     * @todo move into StringHelper
     */
    private String[] tokenize(String str,
                              String delim)
    {
        int startOffset = 0;
        int endOffset = -1;
        int sepLength = delim.length();
        List lines = new ArrayList(15);

        while ((endOffset = str.indexOf(delim, startOffset)) > -1)
        {
            lines.add(str.substring(startOffset, endOffset));
            startOffset = endOffset + sepLength;
        }

        if (startOffset > 0)
        {
            lines.add(str.substring(startOffset));
        }
        else
        {
            lines.add(str);
        }

        return (String[])lines.toArray(EMPTY_STRING_ARRAY);
    }
}
