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
package de.hunsicker.jalopy.prefs;

import de.hunsicker.jalopy.History;
import de.hunsicker.jalopy.parser.JavaRecognizer;

import org.apache.log4j.Level;


/**
 * Holds the preferences defaults.
 *
 * <p>
 * Use this class in conjunction with {@link Key} to access the preferences:
 * </p>
 * <pre style="background:lightgrey">
 * Preferences prefs = Preferences.getInstance();
 * int numThreads = prefs.getInt(Keys.THREAD_COUNT,
 * Defaults.THREAD_COUNT));
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class Defaults
{
    //~ Static variables/initializers ·········································

    /** DOCUMENT ME! */
    public static final boolean ALIGN_PARAMS_METHOD_DEF = false;

    public static final boolean ALIGN_TERNARY_OPERATOR = true;

    /** DOCUMENT ME! */
    public static final boolean ALIGN_TERNARY_EXPRESSION = false;

    /** DOCUMENT ME! */
    public static final boolean ALIGN_TERNARY_VALUES = false;

    /** DOCUMENT ME! */
    public static final boolean ALIGN_VAR_ASSIGNS = false;

    /** DOCUMENT ME! */
    public static final boolean ALIGN_VAR_IDENTS = false;

    /** DOCUMENT ME! */
    public static final boolean ALIGN_METHOD_CALL_CHAINS = true;

    /** DOCUMENT ME! */
    public static final boolean ARRAY_BRACKETS_AFTER_IDENT = false;

    /** DOCUMENT ME! */
    public static final int BACKUP_LEVEL = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_BLOCK = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_BRACE_LEFT = 0;

    /** DOCUMENT ME! */
    public static final String ENVIRONMENT = "";

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_CLASS = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_DECLARATION = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_FOOTER = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_HEADER = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_IMPORT = 2;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_INTERFACE = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_METHOD = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_AFTER_PACKAGE = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_BLOCK = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_BRACE_RIGHT = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_CASE_BLOCK = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_COMMENT_JAVADOC = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_COMMENT_MULTI_LINE = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_CONTROL = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_DECLARATION = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_FOOTER = 1;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_BEFORE_HEADER = 0;

    /** DOCUMENT ME! */
    public static final int BLANK_LINES_KEEP_UP_TO = 1;

    /** DOCUMENT ME! */
    public static final boolean BRACE_EMPTY_CUDDLE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_EMPTY_INSERT_STATEMENT = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_INSERT_DO_WHILE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_INSERT_FOR = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_INSERT_IF_ELSE = true;

    /** DOCUMENT ME! */
    public static final boolean BRACE_INSERT_WHILE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_NEWLINE_LEFT = false;

    /** DOCUMENT ME! */
    public static final boolean INSERT_TRAILING_NEWLINE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_NEWLINE_RIGHT = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_REMOVE_BLOCK = true;

    /** DOCUMENT ME! */
    public static final boolean BRACE_REMOVE_DO_WHILE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_REMOVE_FOR = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_REMOVE_IF_ELSE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_REMOVE_WHILE = false;

    /** DOCUMENT ME! */
    public static final boolean BRACE_TREAT_DIFFERENT = false;

    /** DOCUMENT ME! */
    public static final boolean CHUNKS_BY_BLANK_LINES = true;

    /** DOCUMENT ME! */
    public static final int HEADER_SMART_MODE_LINES = 0;

    /** DOCUMENT ME! */
    public static final boolean CHUNKS_BY_COMMENTS = true;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_FORMAT_MULTI_LINE = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_INSERT_SEPARATOR = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_INSERT_SEPARATOR_RECURSIVE = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_FIELDS_SHORT = true;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_CHECK_TAG = false;

    /** DOCUMENT ME! */
    public static final int COMMENT_JAVADOC_CLASS_MASK = 0;

    /** DOCUMENT ME! */
    public static final int COMMENT_JAVADOC_CTOR_MASK = 0;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_INNER_CLASS = false;

    /** DOCUMENT ME! */
    public static final int COMMENT_JAVADOC_METHOD_MASK = 0;

    /** DOCUMENT ME! */
    public static final int COMMENT_JAVADOC_VARIABLE_MASK = 0;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_PARSE = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_REMOVE = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_TRANSFORM = false;

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_INTERFACE = "/**| * DOCUMENT ME!| *| * @author $author$| * @version \u0024Revision\u0024| */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CLASS = "/**| * DOCUMENT ME!| *| * @author $author$| * @version \u0024Revision\u0024| */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_VARIABLE = "/** DOCUMENT ME! */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_TOP = "/**| * DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_SEPARATOR = " *";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM = " * @param $paramType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION = " * @throws $exceptionType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN = " * @return DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM = " */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_TOP = "/**| * Creates a new $objectType$ object.";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM = " * @param $paramType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION = " * @throws $exceptionType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM = " */";

    /** DOCUMENT ME! */
    public static final boolean COMMENT_REMOVE_MULTI_LINE = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_REMOVE_SINGLE_LINE = false;

    /** DOCUMENT ME! */
    public static final int SOURCE_VERSION = JavaRecognizer.JDK_1_4;

    /** DOCUMENT ME! */
    public static final boolean FOOTER = false;

    /** DOCUMENT ME! */
    public static final boolean FORCE_FORMATTING = false;

    /** DOCUMENT ME! */
    public static final boolean HEADER = false;

    /** DOCUMENT ME! */
    public static final String HISTORY_POLICY = History.Policy.DISABLED.toString();

    /** DOCUMENT ME! */
    public static final String IMPORT_POLICY = ImportPolicy.DISABLED.toString();

    /** DOCUMENT ME! */
    public static final String IMPORT_GROUPING = "*:0|gnu:2|java:2|javax:2";

    /** DOCUMENT ME! */
    public static final int IMPORT_GROUPING_DEPTH = 3;

    /** DOCUMENT ME! */
    public static final boolean IMPORT_SORT = true;

    /** DOCUMENT ME! */
    public static final boolean INDENT_CASE_FROM_SWITCH = false;

    /** DOCUMENT ME! */
    public static final boolean INDENT_CONTINUATION_IF = true;

    /** DOCUMENT ME! */
    public static final boolean INDENT_CONTINUATION_OPERATOR = false;

    /** DOCUMENT ME! */
    public static final boolean INDENT_CONTINUATION_IF_TERNARY = false;

    /** DOCUMENT ME! */
    public static final boolean INDENT_FIRST_COLUMN_COMMENT = true;

    /** DOCUMENT ME! */
    public static final boolean INDENT_LABEL = false;

    /** DOCUMENT ME! */
    public static final boolean INDENT_DEEP = false;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE = 4;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_BRACE_CUDDLED = 1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_BRACE_LEFT = 1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_BRACE_RIGHT = 0;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_BRACE_RIGHT_AFTER = 1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_COMMENT_ENDLINE = 1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_CONTINUATION = 4;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_DEEP = 55;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_EXTENDS = -1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_IMPLEMENTS = -1;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_LEADING = 0;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_TABS = 8;

    /** DOCUMENT ME! */
    public static final int INDENT_SIZE_THROWS = -1;

    /** DOCUMENT ME! */
    public static final boolean INDENT_WITH_TABS = false;

    /** DOCUMENT ME! */
    public static final boolean INDENT_WITH_TABS_ONLY_LEADING = false;

    /** DOCUMENT ME! */
    public static final boolean INSERT_EXPRESSION_PARENTHESIS = true;

    /** DOCUMENT ME! */
    public static final boolean INSERT_LOGGING_CONDITIONAL = false;

    /** DOCUMENT ME! */
    public static final boolean INSERT_SERIAL_UID = false;

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TAGS_INLINE = "";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TAGS_STANDARD = "";

    /** DOCUMENT ME! */
    public static final int LINE_LENGTH = 80;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP = true;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_ALL = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_CHAINED_METHOD_CALL = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_ASSIGN = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_LABEL = true;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_PARAMS_METHOD_CALL = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_PARAMS_METHOD_CALL_IF_NESTED = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_PARAMS_METHOD_DEF = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_EXTENDS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_IMPLEMENTS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_THROWS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_THROWS = false;

    /** DOCUMENT ME! */
    public static final int LINE_WRAP_ARRAY_ELEMENTS = 0;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_ARRAY_INIT = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_EXTENDS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_IMPLEMENTS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_OPERATOR = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_THROWS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_LEFT_PAREN = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_BEFORE_RIGHT_PAREN = false;

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_IO = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_PARSER = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_PARSER_JAVADOC = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_PRINTER = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_PRINTER_JAVADOC = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final int MSG_PRIORITY_TRANSFORM = Level.WARN.toInt();

    /** DOCUMENT ME! */
    public static final boolean MSG_SHOW_ERROR_STACKTRACE = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_ASSIGNMENT_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_BITWISE_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_BRACES = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_BRACKETS = false;

    /** DOCUMENT ME! */
    public static final boolean PADDING_CAST = false;

    /** DOCUMENT ME! */
    public static final boolean PADDING_LOGICAL_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_MATH_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_PAREN = false;

    /** DOCUMENT ME! */
    public static final boolean PADDING_RELATIONAL_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean PADDING_SHIFT_OPERATORS = true;

    /** DOCUMENT ME! */
    public static final boolean SORT = true;

    /** DOCUMENT ME! */
    public static final boolean SORT_CLASS = false;

    /** DOCUMENT ME! */
    public static final boolean SORT_CTOR = false;

    /** DOCUMENT ME! */
    public static final boolean SORT_INTERFACE = false;

    /** DOCUMENT ME! */
    public static final boolean SORT_METHOD = false;

    /** DOCUMENT ME! */
    public static final boolean SORT_MODIFIERS = false;

    /** DOCUMENT ME! */
    public static final boolean SORT_VARIABLE = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_AFTER_CAST = true;

    /** DOCUMENT ME! */
    public static final boolean SPACE_AFTER_COMMA = true;

    /** DOCUMENT ME! */
    public static final boolean SPACE_AFTER_SEMICOLON = true;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_BRACES = true;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_BRACKETS = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_BRACKETS_TYPES = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_CASE_COLON = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_LOGICAL_NOT = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_METHOD_CALL_PAREN = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_METHOD_DEF_PAREN = false;

    /** DOCUMENT ME! */
    public static final boolean SPACE_BEFORE_STATEMENT_PAREN = true;

    /** DOCUMENT ME! */
    public static final boolean STRIP_QUALIFICATION = false;

    /** DOCUMENT ME! */
    public static final String STYLE_DESCRIPTION = "Sun Java Coding Convention";

    /** DOCUMENT ME! */
    public static final String STYLE_NAME = "Sun";

    /** DOCUMENT ME! */
    public static final int THREAD_COUNT = 1;

    /** DOCUMENT ME! */
    public static final boolean INSPECTOR = false;

    /** DOCUMENT ME! */
    public static final String REGEXP_PACKAGE = "[a-z]+(?:\\.[a-z]+)*";

    /** DOCUMENT ME! */
    public static final String REGEXP_CLASS = "[A-Z][a-zA-Z0-9]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_CLASS_ABSTRACT = "[A-Z][a-zA-Z0-9]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_INTERFACE = "[A-Z][a-zA-Z0-9]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_LABEL = "[a-zA-Z0-9_]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PUBLIC = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PROTECTED = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_DEFAULT = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PRIVATE = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PUBLIC_STATIC = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PROTECTED_STATIC = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_DEFAULT_STATIC = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PRIVATE_STATIC = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PUBLIC_STATIC_FINAL = "[a-zA-Z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PROTECTED_STATIC_FINAL = "[a-zA-Z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_DEFAULT_STATIC_FINAL = "[a-zA-Z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_PRIVATE_STATIC_FINAL = "[a-zA-Z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_LOCAL_VARIABLE = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_PARAM = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_PARAM_FINAL = "[a-z][\\w]*";

    //~ Constructors ··························································

    /**
     * Creates a new Defaults object.
     */
    private Defaults()
    {
    }
}
