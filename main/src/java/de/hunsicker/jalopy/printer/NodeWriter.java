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

import de.hunsicker.antlr.*;
import de.hunsicker.jalopy.Environment;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.util.StringHelper;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;


/**
 * The writer to be used to print a Java AST. This class contains some basic
 * support methods to be used by printers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @author <a href="mailto:david_beutel2@yahoo.com">David Beutel</a>
 * @version $Revision$
 *
 * @see de.hunsicker.jalopy.printer.Printer
 */
public class NodeWriter
    extends Writer
{
    //~ Static variables/initializers ·········································

    /** Indicates that no indentation should be performed. */
    public static final boolean INDENT_NO = false;

    /** Indicates that indentation should be performed. */
    public static final boolean INDENT_YES = true;

    /** Print NO newline after a token. */
    public static final boolean NEWLINE_NO = false;

    /** Print a newline after a token. */
    public static final boolean NEWLINE_YES = true;

    /** Indicates that a printer is in default mode. */
    static final int MODE_DEFAULT = 1;

    /** Indicates that a printer is in testing mode. */
    static final int MODE_TEST = 2;
    private static final String LCURLY = "{";
    private static final String RCURLY = "}";
    private static final String SEMI = ";";
    private static final String TAB = "\t";
    private static final String EMPTY_STRING = "";

    //~ Instance variables ····················································

    /** The envrionment to use. */
    protected Environment environment;

    /** The user preferences that control the output style. */
    protected Preferences prefs;

    /** Used line separator. Defaults to the platform standard. */
    protected String lineSeparator;

    /** The original line separator of the file as reported by the lexer. */
    protected String originalLineSeparator;

    /** Should indenting use an added contination amount? */
    protected boolean continuation;

    /**
     * Indicates wether a trailing empty line should be inserted at the end of
     * a file.
     */
    protected boolean insertTrailingEmpty;

    /** Print left curly braces on a new line? */
    protected boolean leftBraceNewline;

    /**
     * Indicates whether we're at the beginning of a new line (<code>column ==
     * 1</code>).
     */
    protected boolean newline = true;

    /** Should tabs only be used to print leading indentation? */
    protected boolean useLeadingTabs;

    /** Should tabs be used to print indentation? */
    protected boolean useTabs;

    /** Current column position. */
    protected int column = 1;

    /** Number of spaces to use for continuation indentation. */
    protected int continuationIndentSize;

    /** Number of spaces to take for one indent. */
    protected int indentSize;

    /** Holds the type of the last printed token. */
    protected int last = JavaTokenTypes.BOF;

    /** Number of spaces to print before left curly braces. */
    protected int leftBraceIndent;

    /** Current line number. */
    protected int line = 1;

    /** Printing mode. */
    protected int mode = MODE_DEFAULT;
    CommonHiddenStreamToken pendingComment;

    /** The last EXPR node printed. */
    JavaNode expression;

    /** The found issues. */
    Map issues;

    /** The printing state. */
    PrinterState state;

    /** The original filename of the stream we output. */
    String filename = "<unknown>";
    WriterCache testers;
    boolean groupingParentheses;

    /**
     * The number of blank lines that were printed before the last EXPR node.
     */
    int blankLines;

    /** Current indent level. */
    int indentLevel;

    /** Number of spaces to use for leading indentation. */
    int leadingIndentSize;

    /** Whitespace to prepend every line. */
    private String _leadingIndentSizeString;

    /** Our right brace. */
    private String _rightBrace;

    /** Target output stream. */
    private Writer _out;

    /** Used to generate the indent string. */
    private char[] _indentChars;

    //~ Constructors ··························································

    /**
     * Creates a new NodeWriter object with the given file output format.
     *
     * @param out the output stream to write to.
     * @param filename name of the parsed file.
     * @param issues holds the issues found during a run.
     * @param lineSeparator the lineSeparator to use.
     * @param originalLineSeparator the original line separator of the file.
     */
    public NodeWriter(Writer out,
                      String filename,
                      Map    issues,
                      String lineSeparator,
                      String originalLineSeparator)
    {
        this();
        this.filename = filename;
        this.issues = issues;
        this.lineSeparator = lineSeparator;
        this.originalLineSeparator = originalLineSeparator;
        this.testers = new WriterCache();
        _out = out;
    }


    /**
     * Creates a new NodeWriter object.
     */
    protected NodeWriter()
    {
        this.state = new PrinterState(this);
        this.lineSeparator = File.separator;
        this.prefs = Preferences.getInstance();
        this.indentSize = this.prefs.getInt(Keys.INDENT_SIZE,
                                            Defaults.INDENT_SIZE);
        this.insertTrailingEmpty = this.prefs.getBoolean(Keys.INSERT_TRAILING_NEWLINE,
                                                         Defaults.INSERT_TRAILING_NEWLINE);
        this.continuationIndentSize = this.prefs.getInt(Keys.INDENT_SIZE_CONTINUATION,
                                                        Defaults.INDENT_SIZE_CONTINUATION);
        this.leftBraceNewline = this.prefs.getBoolean(Keys.BRACE_NEWLINE_LEFT,
                                                      Defaults.BRACE_NEWLINE_LEFT);
        this.leftBraceIndent = this.prefs.getInt(Keys.INDENT_SIZE_BRACE_LEFT,
                                                 Defaults.INDENT_SIZE_BRACE_LEFT);
        this.leadingIndentSize = this.prefs.getInt(Keys.INDENT_SIZE_LEADING,
                                                   Defaults.INDENT_SIZE_LEADING);
        this.useTabs = this.prefs.getBoolean(Keys.INDENT_WITH_TABS,
                                             Defaults.INDENT_WITH_TABS);
        this.useLeadingTabs = this.prefs.getBoolean(Keys.INDENT_WITH_TABS_ONLY_LEADING,
                                                    Defaults.INDENT_WITH_TABS_ONLY_LEADING);

        _indentChars = new char[150];

        for (int i = 0; i < _indentChars.length; i++)
        {
            _indentChars[i] = ' ';
        }

        if (this.leadingIndentSize > 0)
        {
            _leadingIndentSizeString = getString(this.leadingIndentSize);

            if (this.useTabs)
            {
                _leadingIndentSizeString = StringHelper.replace(_leadingIndentSizeString,
                                                                getString(this.indentSize),
                                                                TAB);
            }
        }
    }

    //~ Methods ·······························································

    /**
     * Returns the line column position of the last written character.
     *
     * @return line position of the last written character.
     */
    public int getColumn()
    {
        return this.column;
    }


    /**
     * Sets the environment to use.
     *
     * @param environment environment.
     *
     * @since 1.0b9
     */
    public void setEnvironment(Environment environment)
    {
        this.environment = environment;
    }


    /**
     * Sets the filename of the file beeing printed.
     *
     * @param filename filename of the source files beeing printed.
     */
    public void setFilename(String filename)
    {
        this.filename = filename;
    }


    /**
     * Returns the name of the parsed file.
     *
     * @return filename.
     */
    public String getFilename()
    {
        return this.filename;
    }


    /**
     * Returns the length of the current indent string.
     *
     * @return indent string length.
     */
    public int getIndentLength()
    {
        return this.indentLevel * this.indentSize;
    }


    /**
     * Sets the current indent level.
     *
     * @param level new indent level.
     */
    public void setIndentLevel(int level)
    {
        this.indentLevel = level;
    }


    /**
     * Returns the current indent level.
     *
     * @return indent level.
     */
    public int getIndentLevel()
    {
        return this.indentLevel;
    }


    /**
     * Returns the number of spaces to use for indentation.
     *
     * @return the indentation size.
     *
     * @since 1.0b8
     */
    public int getIndentSize()
    {
        return this.indentSize;
    }


    /**
     * Sets the type of the token last printed.
     *
     * @param type type of the token
     */
    public void setLast(int type)
    {
        this.last = type;
    }


    /**
     * Returns the type of the token last printed.
     *
     * @return type of the token that was printed last. Returns <code>{@link
     *         JavaTokenTypes#BOF}</code> if nothing was printed yet.
     */
    public int getLast()
    {
        return this.last;
    }


    /**
     * Returns the current line number.
     *
     * @return the current line number.
     */
    public int getLine()
    {
        return this.line;
    }


    /**
     * Sets the separator string to use for newlines.
     *
     * @param lineSeparator separator string. Either &quot;\n&quot;,
     *        &quot;\r&quot; or &quot;\r\n&quot;.
     */
    public void setLineSeparator(String lineSeparator)
    {
        this.lineSeparator = lineSeparator;
    }


    /**
     * Returns the current line separator.
     *
     * @return the current line separator.
     */
    public String getLineSeparator()
    {
        return this.lineSeparator;
    }


    /**
     * Returns a string of the given length.
     *
     * @param length length of the string to return.
     *
     * @return string with the given length.
     */
    public String getString(int length)
    {
        return generateIndentString(length);
    }


    /**
     * Sets the underlying writer to actually write to.
     *
     * @param out writer to write to.
     */
    public void setWriter(Writer out)
    {
        _out = out;
    }


    /**
     * Closes the stream, flushing it first.
     *
     * @throws IOException if an I/O error occured.
     */
    public void close()
        throws IOException
    {
        this.prefs = null;
        this.issues = null;
        this.state.dispose();
        this.state = null;
        _out.close();
    }


    /**
     * Flushes the stream.
     *
     * @throws IOException if an I/O error occured.
     */
    public void flush()
        throws IOException
    {
        _out.flush();
    }


    /**
     * Increases the current indent level one level.
     */
    public void indent()
    {
        setIndentLevel(this.indentLevel + 1);
    }


    /**
     * Outputs the given string of the given type to the underlying writer.
     *
     * @param string string to write.
     * @param type type of the string.
     *
     * @throws IOException if an I/O error occured.
     */
    public void print(String string,
                      int    type)
        throws IOException
    {
        if (this.newline)
        {
            if (leadingIndentSize > 0)
            {
                _out.write(_leadingIndentSizeString);
                this.column += leadingIndentSize;
            }

            int length = this.indentLevel * this.indentSize;

            switch (type)
            {
                case JavaTokenTypes.WS :
                {
                    if (!useTabs)
                    {
                        String s = generateIndentString(length +
                                                        string.length());
                        this.column += s.length();
                        _out.write(s);
                    }
                    else
                    {
                        if (!this.useLeadingTabs)
                        {
                            String s = generateIndentString(length +
                                                            string.length());
                            this.column += s.length();
                            s = StringHelper.replace(s,
                                                     generateIndentString(this.indentSize),
                                                     TAB);
                            _out.write(s);
                        }
                        else
                        {
                            String s = generateIndentString(length);
                            this.column += length;
                            s = StringHelper.replace(s,
                                                     generateIndentString(this.indentSize),
                                                     TAB);
                            _out.write(s);

                            this.column += string.length();
                            _out.write(string);
                        }
                    }

                    break;
                }

                default :
                {
                    String s = generateIndentString(length);
                    this.column += (length + string.length());

                    if (this.useTabs)
                    {
                        s = StringHelper.replace(s,
                                                 generateIndentString(this.indentSize),
                                                 TAB);
                    }

                    _out.write(s);
                    _out.write(string);

                    break;
                }
            }

            this.newline = false;
        }
        else
        {
            switch (type)
            {
                case JavaTokenTypes.WS :

                    if (this.useTabs && (!useLeadingTabs) &&
                        (string.length() > this.indentSize))
                    {
                        int tabCount = this.column / this.indentSize;
                        int spacesCount = this.column - 1 -
                                          (tabCount * this.indentSize);
                        this.column += string.length();

                        if (spacesCount == 0)
                        {
                            string = StringHelper.replace(string,
                                                          generateIndentString(this.indentSize),
                                                          TAB);
                            _out.write(string);
                        }
                        else
                        {
                            if (spacesCount < 0)
                            {
                                _out.write(TAB);
                            }

                            _out.write(TAB);

                            string = StringHelper.replace(string.substring(this.indentSize - spacesCount),
                                                          generateIndentString(this.indentSize),
                                                          TAB);
                            _out.write(string);
                        }

                        break;
                    }

                // fall through
                default :
                    this.column += string.length();
                    _out.write(string);

                    break;
            }
        }

        this.last = type;
    }


    /**
     * Prints the given number of blank lines.
     *
     * @param amount number of blank lines to print.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printBlankLines(int amount)
        throws IOException
    {
        for (int i = 0; i < amount; i++)
        {
            printNewline();
        }
    }


    /**
     * Outputs a closing curly brace. Prints a newline after the brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printRightBrace()
        throws IOException
    {
        printRightBrace(NEWLINE_YES);
    }


    /**
     * Outputs a right curly brace.
     *
     * @param newlineAfter <code>true</code> if a newline should be printed
     *        after the brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printRightBrace(boolean newlineAfter)
        throws IOException
    {
        printRightBrace(JavaTokenTypes.RCURLY, newlineAfter);
    }


    /**
     * Outputs a right curly brace.
     *
     * @param type the type of the brace. Either RCURLY or
     *        OBJBLOCK.
     * @param newlineAfter if <code>true</code> a newline will be printed after the
     *        brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printRightBrace(int     type,
                                boolean newlineAfter)
        throws IOException
    {
        printRightBrace(type, true, newlineAfter);
    }

    /**
     * Outputs a right curly brace.
     *
     * @param type the type of the brace. Either RCURLY or
     *        OBJBLOCK.
     * @param whitepaceBefore if <code>true</code> outputs indentation whitespace (depending on the preferences setting).
     * @param newlineAfter if <code>true</code> a newline will be printed after the
     *        brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printRightBrace(int     type,
                                boolean whitespaceBefore,
                                boolean newlineAfter)
        throws IOException
    {
        unindent();

        if (whitespaceBefore)
            print(getRightBrace(), type);
        else
            print(RCURLY, type);

        // only issue line break if not the last curly brace
        if (newlineAfter && (insertTrailingEmpty || (this.indentLevel > 0)))
        {
            printNewline();
        }
    }

    /**
     * Outputs a Java statement delimeter (<code>;</code>) followed by a
     * newline.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printEndStatement()
        throws IOException
    {
        printEndStatement(true);
    }


    /**
     * Outputs a Java statement delimeter (<code>;</code>).
     *
     * @param newlineAfter <code>true</code> prints a newline after the
     *        statement.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printEndStatement(boolean newlineAfter)
        throws IOException
    {
        print(SEMI, JavaTokenTypes.EXPR);

        if (newlineAfter)
        {
            printNewline();
        }
    }


    /**
     * Outputs a line break.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printNewline()
        throws IOException
    {
        _out.write(this.lineSeparator);
        this.newline = true;
        this.column = 1;
        this.line++;
    }


    /**
     * Outputs an opening curly brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printLeftBrace()
        throws IOException
    {
        printLeftBrace(NEWLINE_YES, NEWLINE_YES);
    }


    /**
     * Outputs an opening curly brace.
     *
     * @param newlineBefore <code>true</code> if a newline should be printed
     *        before the brace.
     * @param newlineAfter <code>true</code> if a newline should be printed
     *        after the brace.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printLeftBrace(boolean newlineBefore,
                               boolean newlineAfter)
        throws IOException
    {
        printLeftBrace(newlineBefore, newlineAfter, NodeWriter.INDENT_YES);
    }


    /**
     * Outputs an opening curly brace.
     *
     * @param newlineBefore <code>true</code> if a newline should be printed
     *        before the brace.
     * @param newlineAfter <code>true</code> if a newline should be printed
     *        after the brace.
     * @param indent if <code>true</code> the brace will be indented relative
     *        to the current indentation level.
     *
     * @throws IOException if an I/O error occured.
     */
    public void printLeftBrace(boolean newlineBefore,
                               boolean newlineAfter,
                               boolean indent)
        throws IOException
    {
        if (newlineBefore)
        {
            printNewline();
        }

        if (indent && (this.leftBraceIndent > 0))
        {
            print(generateIndentString(this.leftBraceIndent), JavaTokenTypes.WS);
        }

        print(LCURLY, JavaTokenTypes.LCURLY);

        if (newlineAfter)
        {
            printNewline();
        }

        indent();
    }


    /**
     * Decreases the current indent level one level.
     */
    public void unindent()
    {
        setIndentLevel(this.indentLevel - 1);
    }


    /**
     * Write a portion of an array of characters.
     *
     * @param cbuf array of characters.
     * @param off offset from which to start writing characters.
     * @param len number of characters to write.
     *
     * @throws IOException if an I/O error occured.
     */
    public void write(char[] cbuf,
                      int    off,
                      int    len)
        throws IOException
    {
        _out.write(cbuf, off, len);
    }


    /**
     * Returns the closing (right) curly brace to use. The actual
     * representation depends on the user preferences.
     *
     * @return the closing curly brace to use.
     */
    String getRightBrace()
    {
        if (_rightBrace == null)
        {
            StringBuffer buf = new StringBuffer(getIndentSize() + 1);
            buf.append(generateIndentString(this.prefs.getInt(
                                                              Keys.INDENT_SIZE_BRACE_RIGHT,
                                                              Defaults.INDENT_SIZE_BRACE_RIGHT)));
            buf.append(RCURLY);

            _rightBrace = buf.toString();
        }

        return _rightBrace;
    }


    /**
     * Generates a string only comprimised of spaces, with the given length.
     *
     * @param length length of the string to create.
     *
     * @return a string of the given length.
     */
    private String generateIndentString(int length)
    {
        if (length == 0)
        {
            return EMPTY_STRING;
        }

        // make sure the char buffer is big enough
        if (length > _indentChars.length)
        {
            char[] buf = new char[(int)(1.5 * length)];

            for (int i = 0;; i++)
            {
                int offset = i * _indentChars.length;

                if ((offset + _indentChars.length) <= buf.length)
                {
                    System.arraycopy(_indentChars, 0, buf, offset,
                                     _indentChars.length);
                }
                else
                {
                    System.arraycopy(_indentChars, 0, buf, offset,
                                     buf.length - offset);

                    break;
                }
            }

            _indentChars = buf;
        }

        return new String(_indentChars, 0, length).intern();
    }
}
