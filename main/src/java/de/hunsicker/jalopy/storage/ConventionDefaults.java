/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.storage;

import de.hunsicker.jalopy.language.JavaRecognizer;

import org.apache.log4j.Level;


/**
 * Holds the default code convention values (Sun Java Coding Style).
 * 
 * <p>
 * Use this class in conjunction with {@link ConventionKeys} to access the convention
 * settings:
 * </p>
 * <pre class="snippet">
 * {@link Convention} settings = {@link Convention}.getInstance();
 * int numThreads = settings.getInt({@link ConventionKeys}.THREAD_COUNT,
 *                                  {@link ConventionDefaults}.THREAD_COUNT));
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @see de.hunsicker.jalopy.storage.Convention
 * @see de.hunsicker.jalopy.storage.ConventionKeys
 */
public final class ConventionDefaults
{
    //~ Static variables/initializers ----------------------------------------------------

    /** DOCUMENT ME! */
    public static final boolean ALIGN_PARAMS_METHOD_DEF = false;

    /** DOCUMENT ME! */
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
    public static final int BLANK_LINES_AFTER_FOOTER = 1;

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
    public static final boolean BRACE_TREAT_DIFFERENT_IF_WRAPPED = false;

    /** DOCUMENT ME! */
    public static final boolean INSERT_TRAILING_NEWLINE = true;

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
    public static final boolean COMMENT_JAVADOC_CHECK_TAGS = false;

    /** DOCUMENT ME! */
    public static final boolean COMMENT_JAVADOC_CHECK_TAGS_THROWS = false;

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
    public static final String COMMENT_JAVADOC_TEMPLATE_INTERFACE =
        "/**| * DOCUMENT ME!| *| * @author $author$| * @version \u0024Revision\u0024| */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CLASS =
        "/**| * DOCUMENT ME!| *| * @author $author$| * @version \u0024Revision\u0024| */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_VARIABLE = "/** DOCUMENT ME! */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_TOP =
        "/**| * DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_SEPARATOR = " *";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM =
        " * @param $paramType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION =
        " * @throws $exceptionType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN =
        " * @return DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM = " */";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_TOP =
        "/**| * Creates a new $objectType$ object.";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM =
        " * @param $paramType$ DOCUMENT ME!";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION =
        " * @throws $exceptionType$ DOCUMENT ME!";

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

    /** The default method a timestamp. */
    public static final String HISTORY_METHOD = History.Method.TIMESTAMP.getName();

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
    public static final String LANGUAGE = "en";

    /** DOCUMENT ME! */
    public static final String COUNTRY = "US";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TAGS_INLINE = "";

    /** DOCUMENT ME! */
    public static final String COMMENT_JAVADOC_TAGS_STANDARD = "";

    /** DOCUMENT ME! */
    public static final int LINE_LENGTH = 80;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP = true;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_PARAMS_EXCEED = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_PAREN_GROUPING = false;

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
    public static final boolean LINE_WRAP_AFTER_TYPES_EXTENDS_EXCEED = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_IMPLEMENTS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_IMPLEMENTS_EXCEED = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_THROWS = false;

    /** DOCUMENT ME! */
    public static final boolean LINE_WRAP_AFTER_TYPES_THROWS_EXCEED = false;

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
    public static final String CONVENTION_DESCRIPTION = "Sun Java Coding Convention";

    /** DOCUMENT ME! */
    public static final String CONVENTION_NAME = "Sun";

    /** DOCUMENT ME! */
    public static final int THREAD_COUNT = 1;

    /** DOCUMENT ME! */
    public static final boolean INSPECTOR = false;

    /** DOCUMENT ME! */
    public static final String REGEXP_PACKAGE = "[a-z]+(?:\\.[a-z]+)*";

    /** DOCUMENT ME! */
    public static final String REGEXP_CLASS = "[A-Z][a-zA-Z0-9]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_CLASS_ABSTRACT = "[A-Z][a-zA-Z0-9]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_INTERFACE = "[A-Z][a-zA-Z0-9]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_LABEL = "\\w+";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD = "[a-z][\\w]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_FIELD_STATIC_FINAL = "[a-zA-Z][\\w]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_METHOD = "[a-z][\\w]+";

    /** DOCUMENT ME! */
    public static final String REGEXP_LOCAL_VARIABLE = "[a-z][\\w]*";

    /** DOCUMENT ME! */
    public static final String REGEXP_PARAM = "[a-z][\\w]+";

    /** DOCUMENT ME! */
    public static final String SEPARATOR_FILL_CHARACTER = "-";

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ConventionDefaults object.
     */
    private ConventionDefaults()
    {
    }
}
