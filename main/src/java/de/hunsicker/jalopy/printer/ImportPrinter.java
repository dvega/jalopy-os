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
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.util.StringHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Printer for import declarations (<code>IMPORT</code>).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ImportPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Singleton. */
    private static final Printer INSTANCE = new ImportPrinter();

    //~ Constructors ··························································

    /**
     * Creates a new ImportPrinter object.
     */
    protected ImportPrinter()
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
        printCommentsBefore(node, NodeWriter.NEWLINE_YES, out);
        out.print(IMPORT_SPACE, JavaTokenTypes.LITERAL_import);

        AST identifier = node.getFirstChild();
        String name = NodeHelper.getDottedName(identifier);
        out.print(name, JavaTokenTypes.LITERAL_import);

        AST semi = identifier.getNextSibling();
        PrinterFactory.create(semi).print(semi, out);

        AST next = node.getNextSibling();

        if (next != null)
        {
            switch (next.getType())
            {
                case JavaTokenTypes.IMPORT :

                    // grouping of the declarations only makes sense if
                    // sorting is enabled
                    if (this.settings.getBoolean(Keys.IMPORT_SORT,
                                              Defaults.IMPORT_SORT))
                    {
                        String nextName = NodeHelper.getDottedName(next.getFirstChild());
                        int depth = getImportDepth(name);
                        int offset = StringHelper.indexOf('.', name, depth);

                        // the declaration has a package name equal/greater
                        // than the given package depth
                        if (offset > -1)
                        {
                            String nextPart = name.substring(0, offset);

                            // so only issue extra newline if next declaration
                            // starts with a different one
                            if (!nextName.startsWith(nextPart + '.'))
                            {
                                printNewline(next, out);
                            }
                        }

                        // no package name found; if the next declaration
                        // contains one, we have to issue an extra newline
                        else if (depth > 0)
                        {
                            int dots = StringHelper.occurs('.', name);

                            if (dots > 0)
                            {
                                String nextPart = name.substring(0,
                                                                 name.lastIndexOf('.'));

                                if ((!nextName.startsWith(nextPart + '.')) ||
                                    (StringHelper.occurs('.', nextName) != dots))
                                {
                                    printNewline(next, out);
                                }
                            }
                            else if (nextName.indexOf('.') > -1)
                            {
                                printNewline(next, out);
                            }
                        }
                    }

                    break;
            }
        }

        out.last = JavaTokenTypes.IMPORT;
    }


    /**
     * Returns the import depth for the given import declaration. This integer
     * indicates the number of packages/subpackages that are to group
     * together.
     *
     * @param declaration declaration to check.
     *
     * @return the import depth for the given import declaration.
     */
    private int getImportDepth(String declaration)
    {
        int defaultGroupingDepth = this.settings.getInt(Keys.IMPORT_GROUPING_DEPTH,
                                                     Defaults.IMPORT_GROUPING_DEPTH);

        // '0' means no grouping at all
        if (defaultGroupingDepth > 0)
        {
            String info = this.settings.get(Keys.IMPORT_GROUPING,
                                         Defaults.IMPORT_GROUPING);

            if (info.length() > 0)
            {
                Map values = decodeGroupingInfo(info);

                for (Iterator i = values.entrySet().iterator(); i.hasNext();)
                {
                    Map.Entry entry = (Map.Entry)i.next();

                    if (declaration.startsWith((String)entry.getKey()))
                    {
                        return new Integer((String)entry.getValue()).intValue();
                    }
                }
            }
        }

        return defaultGroupingDepth;
    }


    private Map decodeGroupingInfo(String info)
    {
        Map result = new HashMap(15);

        for (StringTokenizer tokens = new StringTokenizer(info, "|");
             tokens.hasMoreElements();)
        {
            String pair = tokens.nextToken();
            int delimOffset = pair.indexOf(':');
            String name = pair.substring(0, delimOffset);
            String depth = pair.substring(delimOffset + 1);
            result.put(name, depth);
        }

        return result;
    }


    private void printNewline(AST        node,
                              NodeWriter out)
        throws IOException
    {
        JavaNode n = (JavaNode)node;
        CommonHiddenStreamToken comment = n.getHiddenBefore();

        if ((comment == null) || (node.getType() != JavaTokenTypes.IMPORT))
        {
            out.printNewline();
        }
        else
        {
            switch (comment.getType())
            {
                case JavaTokenTypes.SL_COMMENT :

                    if (this.settings.getInt(
                                          Keys.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE,
                                          Defaults.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE) <= 0)
                    {
                        out.printNewline();
                    }

                    break;

                case JavaTokenTypes.JAVADOC_COMMENT :

                    if (this.settings.getInt(
                                          Keys.BLANK_LINES_BEFORE_COMMENT_JAVADOC,
                                          Defaults.BLANK_LINES_BEFORE_COMMENT_JAVADOC) <= 0)
                    {
                        out.printNewline();
                    }

                    break;

                case JavaTokenTypes.ML_COMMENT :

                    if (this.settings.getInt(
                                          Keys.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE,
                                          Defaults.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE) <= 0)
                    {
                        out.printNewline();
                    }

                    break;

                // we always print a newline for all other comments so nothing
                // to do here
                default :
                    break;
            }
        }
    }
}
