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

/**
 * Provides the keys to access the Jalopy preferences.
 *
 * <p>
 * Use this class in conjunction with {@link Defaults} to access the
 * preferences:
 * </p>
 * <pre style="background:lightgrey">
 * Preferences prefs = Preferences.getInstance();
 * int numThreads = this.prefs.getInt(Keys.THREAD_COUNT,
 *                                    Defaults.THREAD_COUNT));
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class Keys
{
    //~ Static variables/initializers ·········································

    /**
     * Specifies whether method declarations parameters should be aligned
     * (<em>boolean</em>).
     */
    public static final Key ALIGN_PARAMS_METHOD_DEF = new Key("printer/alignment/ParamsMethodDef");

    /** Force alignment of ternary expression? (<em>boolean</em>) */
    public static final Key ALIGN_TERNARY_EXPRESSION = new Key("printer/alignment/ternaryExpresssion");

    /** Force alignment of ternary values? (<em>boolean</em>) */
    public static final Key ALIGN_TERNARY_VALUES = new Key("printer/alignment/ternaryValue");

    /** Force alignment of indiviual method call chains? (<em>boolean</em>) */
    public static final Key ALIGN_METHOD_CALL_CHAINS = new Key("printer/alignment/methodCallChains");

    /** Align the indiviual parts of the ternar operator? (<em>boolean</em>) */
    public static final Key ALIGN_TERNARY_OPERATOR = new Key("printer/alignment/ternaryOperator");

    /**
     * Specifies whether variable assignments should be aligned
     * (<em>boolean</em>).
     */
    public static final Key ALIGN_VAR_ASSIGNS = new Key("printer/alignment/varAssigns");

    /**
     * Specifies whether variable identifiers should be aligned
     * (<em>boolean</em>).
     */
    public static final Key ALIGN_VAR_IDENTS = new Key("printer/alignment/varIdents");

    /**
     * Should the brackets for array type be printed after the type or after
     * the identifier? (<em>boolean</em>)
     */
    public static final Key ARRAY_BRACKETS_AFTER_IDENT = new Key("printer/misc/arrayBracketsAfterIdent");

    /** The directory where file backups are to be stored (<em>String</em>). */
    public static final Key BACKUP_DIRECTORY = new Key("general/backupDirectory");

    /** Number of backup files to hold (<em>int</em>). */
    public static final Key BACKUP_LEVEL = new Key("general/backupLevel");

    /** Number of blank lines after a block (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_BLOCK = new Key("printer/blankLines/afterBlock");

    /**
     * Force the given number of blank lines after left curly braces
     * (<em>int</em>).
     */
    public static final Key BLANK_LINES_AFTER_BRACE_LEFT = new Key("printer/blankLines/afterBraceLeft");

    /** Number of blank lines after classes (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_CLASS = new Key("printer/blankLines/afterClass");

    /** Number of blank lines after declarations (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_DECLARATION = new Key("printer/blankLines/afterDeclaration");

    /** Number of blank lines after the footer (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_FOOTER = new Key("printer/blankLines/afterFooter");

    /** Number of blank lines after the header (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_HEADER = new Key("printer/blankLines/afterHeader");

    /** Number of blank lines after the last import statement (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_IMPORT = new Key("printer/blankLines/afterLastImport");

    /** Number of blank lines after interfaces (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_INTERFACE = new Key("printer/blankLines/afterInterface");

    /** Number of blank lines after methods (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_METHOD = new Key("printer/blankLines/afterMethod");

    /** Number of blank lines after the package statement (<em>int</em>). */
    public static final Key BLANK_LINES_AFTER_PACKAGE = new Key("printer/blankLines/afterPackage");

    /** Number of blank lines before a block (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_BLOCK = new Key("printer/blankLines/beforeBlock");

    /**
     * Force the given number of blank lines before right curly braces
     * (<em>int</em>).
     */
    public static final Key BLANK_LINES_BEFORE_BRACE_RIGHT = new Key("printer/blankLines/beforeBraceRight");

    /** Number of blank lines before a case block (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_CASE_BLOCK = new Key("printer/blankLines/beforeCaseBlock");

    /** Number of blank lines before Javadoc comments (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_COMMENT_JAVADOC = new Key("printer/blankLines/beforeJavadoc");

    /** Number of blank lines before multi-line comments (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_COMMENT_MULTI_LINE = new Key("printer/blankLines/beforeCommentMultiLine");

    /** Number of blank lines before single-line commenents (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE = new Key("printer/blankLines/beforeCommentSingleLine");

    /** Number of blank lines before a flow control statement (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_CONTROL = new Key("printer/blankLines/beforeControl");

    /** Number of blank lines before declarations (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_DECLARATION = new Key("printer/blankLines/beforeDeclaration");

    /** Number of blank lines before the footer (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_FOOTER = new Key("printer/blankLines/beforeFooter");

    /** Number of blank lines before the header (<em>int</em>). */
    public static final Key BLANK_LINES_BEFORE_HEADER = new Key("printer/blankLines/beforeHeader");

    /** Number of blank lines to keep up to (<em>int</em>). */
    public static final Key BLANK_LINES_KEEP_UP_TO = new Key("printer/blankLines/keepUpTo");

    /** Cuddle empty braces? (<em>boolean</em>) */
    public static final Key BRACE_EMPTY_CUDDLE = new Key("printer/braces/emptyCuddle");

    /** Insert an empty statement into empty braces? (<em>boolean</em>) */
    public static final Key BRACE_EMPTY_INSERT_STATEMENT = new Key("printer/braces/emptyInsertStatement");

    /** Insert braces around single do-while statements? (<em>boolean</em>) */
    public static final Key BRACE_INSERT_DO_WHILE = new Key("printer/braces/insertBracesDoWhile");

    /**
     * Insert unneccessary  braces around single for statements?
     * (<em>boolean</em>)
     */
    public static final Key BRACE_INSERT_FOR = new Key("printer/braces/insertBracesFor");

    /** Insert braces around single if-else statements? (<em>boolean</em>) */
    public static final Key BRACE_INSERT_IF_ELSE = new Key("printer/braces/insertBracesIfElse");

    /** Insert braces around single while statements? (<em>boolean</em>) */
    public static final Key BRACE_INSERT_WHILE = new Key("printer/braces/insertBracesWhile");

    /** Print left braces on a new line? (<em>boolean</em>) */
    public static final Key BRACE_NEWLINE_LEFT = new Key("printer/braces/leftBraceNewLine");

    /** Print right braces on a new line? (<em>boolean</em>) */
    public static final Key BRACE_NEWLINE_RIGHT = new Key("printer/braces/rightBraceNewLine");

    /** Print a newline after the last curly brace? (<em>boolean</em>) */
    public static final Key INSERT_TRAILING_NEWLINE = new Key("printer/misc/insertTrailingNewline");

    /** Remove unneccessary braces for blocks? (<em>boolean</em>) */
    public static final Key BRACE_REMOVE_BLOCK = new Key("printer/braces/removeBracesBlock");

    /** Remove braces around single do-while statements? (<em>boolean</em>) */
    public static final Key BRACE_REMOVE_DO_WHILE = new Key("printer/braces/removeBracesDoWhile");

    /**
     * Remove unneccessary  braces around single for statements?
     * (<em>boolean</em>)
     */
    public static final Key BRACE_REMOVE_FOR = new Key("printer/braces/removeBracesFor");

    /**
     * Remove unneccessary braces around single if-else statements?
     * (<em>boolean</em>)
     */
    public static final Key BRACE_REMOVE_IF_ELSE = new Key("printer/braces/removeBracesIfElse");

    /** Remove braces around single while statements? (<em>boolean</em>) */
    public static final Key BRACE_REMOVE_WHILE = new Key("printer/braces/removeBracesWhile");

    /**
     * Should class and method blocks be treated different? (<em>boolean</em>)
     */
    public static final Key BRACE_TREAT_DIFFERENT = new Key("printer/braces/treatMethodClassDifferent");

    /** Enable chunk detection by blank lines (<em>boolean</em>). */
    public static final Key CHUNKS_BY_BLANK_LINES = new Key("printer/chunks/byBlankLines");

    /** Enable chunk detection by comments (<em>boolean</em>). */
    public static final Key CHUNKS_BY_COMMENTS = new Key("printer/chunks/byComments");

    /** Directory to store class repository files (<em>String</em>). */
    public static final Key CLASS_REPOSITORY_DIRECTORY = new Key("transform/import/classRepositoryDirectory");

    /** Format multi-line comments? (<em>boolean</em>) */
    public static final Key COMMENT_FORMAT_MULTI_LINE = new Key("printer/comments/formatMultiLine");

    /**
     * Insert separator comments between the different tree portions (
     * class/interface/variable/method/constructor/initialiation
     * declarations)? (<em>boolean</em>)
     */
    public static final Key COMMENT_INSERT_SEPARATOR = new Key("printer/comments/insertSeparator");

    /**
     * Insert separator comments between the different tree portions of inner
     * classes/interfaces (a.k.a recursively)? (<em>boolean</em>)
     */
    public static final Key COMMENT_INSERT_SEPARATOR_RECURSIVE = new Key("printer/comments/insertSeparatorRecursive");

    /** Check Javadoc standard tags?  (<em>boolean</em>) */
    public static final Key COMMENT_JAVADOC_CHECK_TAG = new Key("printer/comments/javadoc/checkTags");

    /**
     * Print Javadoc comments for fields in one line, if possible?
     * (<em>boolean</em>)
     */
    public static final Key COMMENT_JAVADOC_FIELDS_SHORT = new Key("printer/comments/javadoc/fieldsShort");

    /**
     * Javadoc template for interfaces (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_INTERFACE = new Key("printer/comments/javadoc/templates/interface");

    /**
     * Javadoc template for classes (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_CLASS = new Key("printer/comments/javadoc/templates/classes");

    /**
     * Javadoc template for variables (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_VARIABLE = new Key("printer/comments/javadoc/templates/variables");

    /**
     * Javadoc template for methods, top part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_METHOD_TOP = new Key("printer/comments/javadoc/templates/methods/top");

    /**
     * Javadoc template for methods, parameter part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM = new Key("printer/comments/javadoc/templates/methods/param");

    /**
     * Javadoc template for methods, exceptions part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION = new Key("printer/comments/javadoc/templates/methods/exception");

    /**
     * Javadoc template for methods, return part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN = new Key("printer/comments/javadoc/templates/methods/return");

    /**
     * Javadoc template for methods, bottom part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM = new Key("printer/comments/javadoc/templates/methods/bottom");

    /**
     * Javadoc template for constructors, top part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_CTOR_TOP = new Key("printer/comments/javadoc/templates/constructors/top");

    /**
     * Javadoc template for constructors, exceptions part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION = new Key("printer/comments/javadoc/templates/constructors/exception");

    /**
     * Javadoc template for constructors, parameters part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM = new Key("printer/comments/javadoc/templates/constructors/param");

    /**
     * Javadoc template for constructors, bottom part (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM = new Key("printer/comments/javadoc/templates/constructors/bottom");

    /** Insert missing Javadoc comment for classes/interfaces? (<em>int</em>) */
    public static final Key COMMENT_JAVADOC_CLASS_MASK = new Key("printer/comments/javadoc/addClass");

    /** Insert missing Javadoc comment for constructors? (<em>int</em>) */
    public static final Key COMMENT_JAVADOC_CTOR_MASK = new Key("printer/comments/javadoc/addCtor");

    /** Insert missing Javadoc comment for variables? (<em>int</em>) */
    public static final Key COMMENT_JAVADOC_VARIABLE_MASK = new Key("printer/comments/javadoc/addField");

    /**
     * Insert missing Javadoc comments for inner classes too?
     * (<em>boolean</em>)
     */
    public static final Key COMMENT_JAVADOC_INNER_CLASS = new Key("printer/comments/javadoc/checkInnerClass");

    /** Insert missing Javadoc comment for methods? (<em>int</em>) */
    public static final Key COMMENT_JAVADOC_METHOD_MASK = new Key("printer/comments/javadoc/addMethod");

    /** Parse Javadoc comments or add AS IS? (<em>boolean</em>) */
    public static final Key COMMENT_JAVADOC_PARSE = new Key("printer/comments/javadoc/parseComments");

    /** Remove Javadoc comments? (<em>boolean</em>) */
    public static final Key COMMENT_JAVADOC_REMOVE = new Key("printer/comments/javadoc/remove");

    /**
     * Transform non-Javadoc comments to Javadoc comments? (<em>boolean</em>)
     */
    public static final Key COMMENT_JAVADOC_TRANSFORM = new Key("printer/comments/javadoc/transform");

    /** Remove multi-line comments? (<em>boolean</em>) */
    public static final Key COMMENT_REMOVE_MULTI_LINE = new Key("printer/comments/removeMultiLine");

    /** Remove single-line comments? (<em>boolean</em>) */
    public static final Key COMMENT_REMOVE_SINGLE_LINE = new Key("printer/comments/removeSingleLine");

    /** Insert a footer? (<em>boolean</em>) */
    public static final Key FOOTER = new Key("printer/footer/use");

    /**
     * Identify keys of the footers that are to be deleted (<em>String</em>).
     */
    public static final Key FOOTER_KEYS = new Key("printer/footer/keys");

    /** The footer text (<em>String</em>). */
    public static final Key FOOTER_TEXT = new Key("printer/footer/text");

    /**
     * Should the processing of a source file be forced although the file
     * hasn't changed? (<em>boolean</em>)
     */
    public static final Key FORCE_FORMATTING = new Key("general/forceFormatting");

    /** Insert a header? (<em>boolean</em>) */
    public static final Key HEADER = new Key("printer/header/use");

    /**
     * Number of comments before the first node (an opening curly brace) that
     * should be treated as header comments (<em>int</em>).
     *
     * @since 1.0b8
     */
    public static final Key HEADER_SMART_MODE_LINES = new Key("printer/header/smartModeLines");

    /**
     * Number of comments after the last node (a closing curly brace) that
     * should be treated as footer comments (<em>int</em>).
     *
     * @since 1.0b8
     */
    public static final Key FOOTER_SMART_MODE_LINES = new Key("printer/footer/smartModeLines");

    /**
     * Identify keys of the headers that are to be deleted (<em>String</em>).
     */
    public static final Key HEADER_KEYS = new Key("printer/header/keys");

    /** The header text (<em>String</em>). */
    public static final Key HEADER_TEXT = new Key("printer/header/text");

    /**
     * The history policy to use (<em>String</em>). Either &quot;none&quot;,
     * &quot;file&quot; or &quot;comment&quot;.
     */
    public static final Key HISTORY_POLICY = new Key("printer/history/policy");

    /** Specifies the import optimization policy (<em>String</em>). */
    public static final Key IMPORT_POLICY = new Key("transform/import/policy");

    /** Holds grouping info for distinct package names (<em>String</em>). */
    public static final Key IMPORT_GROUPING = new Key("transform/import/grouping");

    /** Default import grouping depth (<em>int</em>). */
    public static final Key IMPORT_GROUPING_DEPTH = new Key("transform/import/groupingDepth");

    /** Sort import statements? (<em>boolean</em>) */
    public static final Key IMPORT_SORT = new Key("transform/import/sort");

    /** Indent case block in switch statements? (<em>boolean</em>) */
    public static final Key INDENT_CASE_FROM_SWITCH = new Key("printer/indentation/caseFromSwitch");

    /**
     * Should continuation indentation be used for <code>if</code> blocks?
     * (<em>boolean</em>).
     */
    public static final Key INDENT_CONTINUATION_IF = new Key("printer/indentation/continationIf");

    /**
     * Should standard indentation be used to indent wrapped lines, or rather
     * the deep indentation policy? (<em>boolean</em>).
     */
    public static final Key INDENT_DEEP = new Key("printer/indentation/policyDeep");

    /**
     * Should continuation indentation be used for operators?
     * (<em>boolean</em>).
     */
    public static final Key INDENT_CONTINUATION_OPERATOR = new Key("printer/indentation/continationOperator");

    /**
     * Should continuation indentation be used for ternary "if-else"?
     * (<em>boolean</em>).
     */
    public static final Key INDENT_CONTINUATION_IF_TERNARY = new Key("printer/indentation/continationIfTernary");

    /** Indent first column comments? (<em>boolean</em>) */
    public static final Key INDENT_FIRST_COLUMN_COMMENT = new Key("printer/indentation/firstColumnComments");

    /** Indent labels? (<em>boolean</em>) */
    public static final Key INDENT_LABEL = new Key("printer/indentation/label");

    /** Amount of space to use for indentation (<em>int</em>). */
    public static final Key INDENT_SIZE = new Key("printer/indentation/general");

    /** Indentation before cuddled braces (<em>int</em>). */
    public static final Key INDENT_SIZE_BRACE_CUDDLED = new Key("printer/indentation/braceCuddled");

    /** Indentation before a left curly brace (<em>int</em>). */
    public static final Key INDENT_SIZE_BRACE_LEFT = new Key("printer/indentation/braceLeft");

    /** Indentation before a right curly brace (<em>int</em>). */
    public static final Key INDENT_SIZE_BRACE_RIGHT = new Key("printer/indentation/braceRight");

    /** Indentation after a right curly brace (<em>int</em>). */
    public static final Key INDENT_SIZE_BRACE_RIGHT_AFTER = new Key("printer/indentation/braceRightAfter");

    /** Indentation before an endline comment (<em>int</em>). */
    public static final Key INDENT_SIZE_COMMENT_ENDLINE = new Key("printer/indentation/commentEndline");

    /** Continuation indent size (<em>int</em>). */
    public static final Key INDENT_SIZE_CONTINUATION = new Key("printer/indentation/continuation");

    /**
     * Maximal amount of spaces for wrapping should be forced (<em>int</em>).
     */
    public static final Key INDENT_SIZE_DEEP = new Key("printer/indentation/deep");

    /**
     * Indentation for extends types (<em>int</em>).
     *
     * @since 1.0b7
     */
    public static final Key INDENT_SIZE_EXTENDS = new Key("printer/indentation/extends");

    /**
     * Indentation for implements types (<em>int</em>).
     *
     * @since 1.0b7
     */
    public static final Key INDENT_SIZE_IMPLEMENTS = new Key("printer/indentation/implements");

    /** Indentation before every line (<em>int</em>). */
    public static final Key INDENT_SIZE_LEADING = new Key("printer/indentation/leading");

    /** Number of spaces to assume for tabs (<em>int</em>). */
    public static final Key INDENT_SIZE_TABS = new Key("printer/indentation/tabs/size");

    /**
     * Indentation for throws types (<em>int</em>).
     *
     * @since 1.0b7
     */
    public static final Key INDENT_SIZE_THROWS = new Key("printer/indentation/throws");

    /*
     * Indentation for wrapped parameters (<em>int</em>).
     *
     * @since 1.0b8
     *
    public static final Key INDENT_SIZE_PARAMETERS =
        new Key("printer/indentation/parameter");*/
    /*
     * Use the specified parameters indentation size for parameters of method
     * calls? (<em>boolean</em>)
     *
     * @since 1.0b8
     *
    public static final Key INDENT_USE_PARAMS_METHOD_CALL =
        new Key("printer/indentation/useMethodCallParams");*/

    /** Fill gaps with tabs? (<em>boolean</em>) */
    public static final Key INDENT_WITH_TABS = new Key("printer/indentation/tabs/use");

    /** Use spaces for continuation after tabs? (<em>boolean</em>) */
    public static final Key INDENT_WITH_TABS_ONLY_LEADING = new Key("printer/indentation/tabs/useOnlyLeading");

    /**
     * Insert parenthesis around expressions to make precedence clear?
     * (<em>boolean</em>)
     */
    public static final Key INSERT_EXPRESSION_PARENTHESIS = new Key("transform/misc/insertExpressionParenthesis");

    /**
     * Insert conditional expresssion for debug logging calls?
     * (<em>boolean</em>)
     */
    public static final Key INSERT_LOGGING_CONDITIONAL = new Key("transform/misc/insertLoggingConditional");

    /**
     * Insert serial version UID for serializable classes? (<em>boolean</em>)
     */
    public static final Key INSERT_SERIAL_UID = new Key("transform/misc/insertUID");

    /** For internal use only. */
    public static final Key INTERNAL_VERSION = new Key("internal/version");

    /**
     * Custom Javadoc in-line tags definitions (<em>String</em>).
     *
     * @since 1.0b7
     */
    public static final Key COMMENT_JAVADOC_TAGS_INLINE = new Key("printer/comments/javadoc/tags/in-line");

    /**
     * Custom Javadoc standard tags definitions (<em>String</em>).
     *
     * @since 1.0b7
     */
    public static final Key COMMENT_JAVADOC_TAGS_STANDARD = new Key("printer/comments/javadoc/tags/standard");

    /** Number of characters in each line(<em>int</em>). */
    public static final Key LINE_LENGTH = new Key("printer/wrapping/lineLength");

    /** Use line wrapping? (<em>boolean</em>) */
    public static final Key LINE_WRAP = new Key("printer/wrapping/use");

    /**
     * Force wrapping/alignment of chained method? (<em>boolean</em>)
     *
     * @since 1.0b7
     */
    public static final Key LINE_WRAP_AFTER_CHAINED_METHOD_CALL = new Key("printer/wrapping/afterChainedMethodCall");

    /** Print newline after labels? (<em>boolean</em>) */
    public static final Key LINE_WRAP_AFTER_LABEL = new Key("printer/wrapping/afterLabel");

    /**
     * Prefer wrapping after assignments? (<em>boolean</em>)
     *
     * @since 1.0b9
     */
    public static final Key LINE_WRAP_AFTER_ASSIGN = new Key("printer/wrapping/afterAssign");

    /**
     * Force wrapping/alignment of successive parameters/expression if the
     * first parameter/expression was wrapped ? (<em>boolean</em>)
     *
     * @since 1.0b9
     */
    public static final Key LINE_WRAP_ALL = new Key("printer/wrapping/ifFirst");

    /**
     * Force wrapping/alignment of params for method calls? (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_PARAMS_METHOD_CALL = new Key("printer/wrapping/paramsMethodCall");

    /**
     * Force wrapping/alignment of params for method calls? (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_PARAMS_METHOD_CALL_IF_NESTED = new Key("printer/wrapping/paramsMethodCallIfCall");

    /**
     * Force wrapping/alignment of method params for method/constructor
     * declarations? (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_PARAMS_METHOD_DEF = new Key("printer/wrapping/paramsMethodDef");

    /**
     * Force alignment of extends types for class/interface declarations?
     * (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_TYPES_EXTENDS = new Key("printer/wrapping/afterExtendsTypes");

    /**
     * Force alignment of implements types for class/interface declarations?
     * (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_TYPES_IMPLEMENTS = new Key("printer/wrapping/afterImplementsTypes");

    /**
     * Force wrapping/alignment of exception types for method/ctor
     * declarations? (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_AFTER_TYPES_THROWS = new Key("printer/wrapping/afterThrowsTypes");

    /** DOCUMENT ME! */
    public static final Key LINE_WRAP_AFTER_THROWS = new Key("printer/wrapping/afterThrows");

    /**
     * Prefer line wrapping after the left parentheses of
     * parameter/expressions lists? (<em>boolean</em>)
     *
     * @since 1.0b9
     */
    public static final Key LINE_WRAP_AFTER_LEFT_PAREN = new Key("printer/wrapping/afterLeftParen");

    /**
     * Insert a newline before the right parentheses of parameter/epxression
     * lists? (<em>boolean</em>)
     *
     * @since 1.0b9
     */
    public static final Key LINE_WRAP_BEFORE_RIGHT_PAREN = new Key("printer/wrapping/beforeRightParen");

    /**
     * Force wrapping/alignment after given number of array elements
     * (<em>int</em>).
     */
    public static final Key LINE_WRAP_ARRAY_ELEMENTS = new Key("printer/wrapping/arrayElements");

    /** Force line wrapping before array initialization? (<em>boolean</em>) */
    public static final Key LINE_WRAP_BEFORE_ARRAY_INIT = new Key("printer/wrapping/beforeArrayInit");

    /** Force line wrapping before extends? (<em>boolean</em>) */
    public static final Key LINE_WRAP_BEFORE_EXTENDS = new Key("printer/wrapping/beforeExtends");

    /** Force line wrapping before implements? (<em>boolean</em>) */
    public static final Key LINE_WRAP_BEFORE_IMPLEMENTS = new Key("printer/wrapping/beforeImplements");

    /**
     * Should line wrapping be performed before or after operators?
     * (<em>boolean</em>)
     */
    public static final Key LINE_WRAP_BEFORE_OPERATOR = new Key("printer/wrapping/beforeOperator");

    /**
     * Force line wrapping before throws? (<em>boolean</em>)
     *
     * @since 1.0b7
     */
    public static final Key LINE_WRAP_BEFORE_THROWS = new Key("printer/wrapping/beforeThrows");

    /** Level of the {@link Loggers#IO} (<em>int</em>). */
    public static final Key MSG_PRIORITY_IO = new Key("messages/ioMsgPrio");

    /** Level of the {@link Loggers#PARSER} (<em>int</em>). */
    public static final Key MSG_PRIORITY_PARSER = new Key("messages/parserMsgPrio");

    /** Level of the {@link Loggers#PARSER_JAVADOC} (<em>int</em>). */
    public static final Key MSG_PRIORITY_PARSER_JAVADOC = new Key("messages/parserJavadocMsgPrio");

    /** Level of the {@link Loggers#PRINTER} (<em>int</em>). */
    public static final Key MSG_PRIORITY_PRINTER = new Key("messages/printerMsgPrio");

    /** Level of the {@link Loggers#PRINTER_JAVADOC} (<em>int</em>). */
    public static final Key MSG_PRIORITY_PRINTER_JAVADOC = new Key("messages/printerJavadocMsgPrio");

    /** Level of the {@link Loggers#TRANSFORM} (<em>int</em>). */
    public static final Key MSG_PRIORITY_TRANSFORM = new Key("messages/transformMsgPrio");

    /** Display stacktrace for errors? (<em>boolean</em>) */
    public static final Key MSG_SHOW_ERROR_STACKTRACE = new Key("messages/showErrorStackTrace");

    /** Pad assignment operators? (<em>boolean</em>) */
    public static final Key PADDING_ASSIGNMENT_OPERATORS = new Key("printer/whitespace/paddingAssignmentOperators");

    /** Pad bitwise operators? (<em>boolean</em>) */
    public static final Key PADDING_BITWISE_OPERATORS = new Key("printer/whitespace/paddingBitwiseOperators");

    /**
     * Insert spaces after the left, and before the right curly brace for
     * array initializations? (<em>boolean</em>)
     */
    public static final Key PADDING_BRACES = new Key("printer/whitespace/padddingBraces");

    /**
     * Insert spaces after the left, and before the right bracket?
     * (<em>boolean</em>)
     */
    public static final Key PADDING_BRACKETS = new Key("printer/whitespace/padddingBrackets");

    /**
     * Insert spaces after the left, and before the right type cast
     * parenthesis? (<em>boolean</em>)
     */
    public static final Key PADDING_CAST = new Key("printer/whitespace/padddingTypeCast");

    /** Pad logical operators? (<em>boolean</em>) */
    public static final Key PADDING_LOGICAL_OPERATORS = new Key("printer/whitespace/paddingLogicalOperators");

    /** Pad mathematical operators? (<em>boolean</em>) */
    public static final Key PADDING_MATH_OPERATORS = new Key("printer/whitespace/paddingMathematicalOperators");

    /**
     * Insert spaces after the left, and before the right parenthesis?
     * (<em>boolean</em>)
     */
    public static final Key PADDING_PAREN = new Key("printer/whitespace/padddingParenthesis");

    /** Pad relational operators? (<em>boolean</em>) */
    public static final Key PADDING_RELATIONAL_OPERATORS = new Key("printer/whitespace/paddingRelationalOperators");

    /** Pad shift operators? (<em>boolean</em>) */
    public static final Key PADDING_SHIFT_OPERATORS = new Key("printer/whitespace/paddingShiftOperators");

    /** Sort the different elements of a Java source file? (<em>boolean</em>) */
    public static final Key SORT = new Key("printer/sorting/use");

    /** Sort classes declarations? (<em>boolean</em>) */
    public static final Key SORT_CLASS = new Key("printer/sorting/class");

    /** Sort constructors declarations? (<em>boolean</em>) */
    public static final Key SORT_CTOR = new Key("printer/sorting/constructor");

    /** Sort interfaces declarations? (<em>boolean</em>) */
    public static final Key SORT_INTERFACE = new Key("printer/sorting/interface");

    /** Sort methods declarations? (<em>boolean</em>) */
    public static final Key SORT_METHOD = new Key("printer/sorting/method");

    /** Sort modifers? (<em>boolean</em>) */
    public static final Key SORT_MODIFIERS = new Key("printer/sorting/modifiers/use");

    /** String indicating the declaration sort order (<em>String</em>). */
    public static final Key SORT_ORDER = new Key("printer/sorting/order");

    /** String indicating the modifiers sort order (<em>String</em>). */
    public static final Key SORT_ORDER_MODIFIERS = new Key("printer/sorting/orderModifiers");

    /** Sort variable declarations? (<em>boolean</em>) */
    public static final Key SORT_VARIABLE = new Key("printer/sorting/variable");

    /**
     * String encoded environment variables (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key ENVIRONMENT = new Key("printer/environment");

    /**
     * JDK source compatibility version (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SOURCE_VERSION = new Key("general/sourceVersion");

    /** Print a space after type casting? (<em>boolean</em>) */
    public static final Key SPACE_AFTER_CAST = new Key("printer/whitespace/afterCastingParenthesis");

    /** Print a space after a comma? (<em>boolean</em>) */
    public static final Key SPACE_AFTER_COMMA = new Key("printer/whitespace/afterComma");

    /** Print a space after a semicolon? (<em>boolean</em>) */
    public static final Key SPACE_AFTER_SEMICOLON = new Key("printer/whitespace/afterSemiColon");

    /** Print a space before braces (of arrays)? (<em>boolean</em>) */
    public static final Key SPACE_BEFORE_BRACES = new Key("printer/whitespace/beforeBraces");

    /** Print a space before brackets? (<em>boolean</em>) */
    public static final Key SPACE_BEFORE_BRACKETS = new Key("printer/whitespace/beforeBrackets");

    /** Print a space before types with brackets (<em>int</em>). */
    public static final Key SPACE_BEFORE_BRACKETS_TYPES = new Key("printer/whitespace/beforeBracketsTypes");

    /** Print a space before the colon of a case block? (<em>boolean</em>) */
    public static final Key SPACE_BEFORE_CASE_COLON = new Key("printer/whitespace/beforeCaseColon");

    /**
     * Print a space before the negation of boolean expressions?
     * (<em>boolean</em>)
     */
    public static final Key SPACE_BEFORE_LOGICAL_NOT = new Key("printer/whitespace/beforeLogicalNot");

    /** Print a space before method call parenthesis? (<em>boolean</em>) */
    public static final Key SPACE_BEFORE_METHOD_CALL_PAREN = new Key("printer/whitespace/beforeMethodCallParenthesis");

    /**
     * Print a space before method definition parenthesis? (<em>boolean</em>)
     */
    public static final Key SPACE_BEFORE_METHOD_DEF_PAREN = new Key("printer/whitespace/beforeMethodDeclarationParenthesis");

    /** Print a space before java statement parenthesis? (<em>boolean</em>) */
    public static final Key SPACE_BEFORE_STATEMENT_PAREN = new Key("printer/whitespace/beforeStatementParenthesis");

    /** Strip qualification for identifiers? (<em>boolean</em>) */
    public static final Key STRIP_QUALIFICATION = new Key("parser/stripQualification");

    /** Description of the code convention (<em>String</em>). */
    public static final Key STYLE_DESCRIPTION = new Key("general/styleDescription");

    /** The location where to load the code convention from (String). */
    public static final Key STYLE_LOCATION = new Key("general/styleLocation");

    /** Name of the code convention (<em>String</em>). */
    public static final Key STYLE_NAME = new Key("general/styleName");

    /** Number of processing threads to use (<em>int</em>). */
    public static final Key THREAD_COUNT = new Key("general/threadCount");

    /**
     * The fill character to use for the separator comments (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_FILL_CHARACTER = new Key("printer/comments/separator/fillCharacter");

    /**
     * Separator text for the static variables and initalizers section
     * (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_STATIC_VAR_INIT = new Key("printer/comments/separator/staticVariableInit");

    /**
     * Separator text for the instance variables section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_INSTANCE_VAR = new Key("printer/comments/separator/instanceVariable");

    /**
     * Separator text for the instance initializers section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_INSTANCE_INIT = new Key("printer/comments/separator/instanceInit");

    /**
     * Separator text for the constructors section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_CTOR = new Key("printer/comments/separator/ctor");

    /**
     * Separator text for the methods section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_METHOD = new Key("printer/comments/separator/method");

    /**
     * Separator text for the interfaces section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_INTERFACE = new Key("printer/comments/separator/interface");

    /**
     * Separator text for the inner classes section (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key SEPARATOR_CLASS = new Key("printer/comments/separator/class");

    /** Enable the code inspector? (<em>boolean</em>) */
    public static final Key INSPECTOR = new Key("inspector/enable");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_DONT_SUBSTITUTE_OBJECT_EQUALS = new Key("inspector/tips/dontSubstituteObjectEquals");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_OBEY_CONTRACT_EQUALS = new Key("inspector/tips/obeyContractEquals");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_OVERRIDE_HASHCODE = new Key("inspector/tips/alwaysOverrideHashCode");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_OVERRIDE_EQUALS = new Key("inspector/tips/alwaysOverrideEquals");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_OVERRIDE_TO_STRING = new Key("inspector/tips/overrideToString");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_INTERFACE_ONLY_FOR_TYPE = new Key("inspector/tips/useInterfaceOnlyForTypes");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_REPLACE_STRUCTURE_WITH_CLASS = new Key("inspector/tips/replaceStructureWithClass");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_RETURN_ZERO_ARRAY = new Key("inspector/tips/neverReturnZeroArrays");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_REFER_BY_INTERFACE = new Key("inspector/tips/referToObjectsByInterface");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_ADHERE_TO_NAMING_CONVENTION = new Key("inspector/tips/");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_NEVER_THROW_EXCEPTION = new Key("inspector/tips/neverDeclareException");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_NEVER_THROW_THROWABLE = new Key("inspector/tips/neverDeclareThrowable");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_DONT_IGNORE_EXCEPTION = new Key("inspector/tips/dontIgnoreExceptions");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_NEVER_WAIT_OUTSIDE_LOOP = new Key("inspector/tips/neverInvokeWaitOutsideLoop");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_AVOID_THREAD_GROUPS = new Key("inspector/tips/avoidThreadGroups");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_VARIABLE_SHADOW = new Key("inspector/tips/avoidVariableShadowing");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_DECLARE_COLLECTION_VARIABLE_COMMENT = new Key("inspector/tips/addCommentForCollections");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_EMPTY_FINALLY = new Key("inspector/tips/neverUseEmptyFinally");

    /**
     * Perform this code inspection? (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_DECLARE_COLLECTION_COMMENT = new Key("inspector/tips/declareCollectionComment");

    /**
     * Perform this code inspection (<em>boolean</em>)
     *
     * @since 1.0b8
     */
    public static final Key TIP_WRONG_COLLECTION_COMMENT = new Key("inspector/tips/wrongCollectionComment");

    /**
     * Regexp for package names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_PACKAGE = new Key("inspector/naming/packages");

    /**
     * Regexp for class names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_CLASS = new Key("inspector/naming/classes");

    /**
     * Regexp for abstract class names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_CLASS_ABSTRACT = new Key("inspector/naming/abstractClasses");

    /**
     * Regexp for interface names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_INTERFACE = new Key("inspector/naming/interfaces");

    /**
     * Regexp for local variables names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_LOCAL_VARIABLE = new Key("inspector/naming/localVariable");

    /**
     * Regexp for method/ctor parameter names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_PARAM = new Key("inspector/naming/param");

    /**
     * Regexp for final method/ctor parameter names (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_PARAM_FINAL = new Key("inspector/naming/paramFinal");

    /**
     * Regexp for labels (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_LABEL = new Key("inspector/naming/label");

    /**
     * Regexp for public fields  (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PUBLIC = new Key("inspector/naming/fieldPublic");

    /**
     * Regexp for protected fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PROTECTED = new Key("inspector/naming/fieldProtected");

    /**
     * Regexp for package protected (default access) fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_DEFAULT = new Key("inspector/naming/fieldFriendly");

    /**
     * Regexp for private fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PRIVATE = new Key("inspector/naming/fieldPrivate");

    /**
     * Regexp for public static fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PUBLIC_STATIC = new Key("inspector/naming/fieldPublicStatic");

    /**
     * Regexp for protected static  (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PROTECTED_STATIC = new Key("inspector/naming/fieldProtectedStatic");

    /**
     * Regexp for package protected (default access) static fields
     * (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_DEFAULT_STATIC = new Key("inspector/naming/fieldFriendlyStatic");

    /**
     * Regexp for private static fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PRIVATE_STATIC = new Key("inspector/naming/fieldPrivateStatic");

    /**
     * Regexp for public static final fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PUBLIC_STATIC_FINAL = new Key("inspector/naming/fieldPublicStaticFinal");

    /**
     * Regexp for protected static final fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PROTECTED_STATIC_FINAL = new Key("inspector/naming/fieldProtectedStaticFinal");

    /**
     * Regexp for package protected (default access) static final fields
     * (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_DEFAULT_STATIC_FINAL = new Key("inspector/naming/fieldFriendlyStaticFinal");

    /**
     * Regexp for private static final fields (<em>String</em>).
     *
     * @since 1.0b8
     */
    public static final Key REGEXP_FIELD_PRIVATE_STATIC_FINAL = new Key("inspector/naming/fieldPrivateStaticFinal");

    //~ Constructors ··························································

    /**
     * Creates a new Keys object.
     */
    private Keys()
    {
    }
}
