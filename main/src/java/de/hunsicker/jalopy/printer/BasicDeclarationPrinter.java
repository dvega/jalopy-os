/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.printer;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import de.hunsicker.jalopy.storage.Loggers;
import org.apache.log4j.Level;

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.jalopy.language.ExtendedToken;
import de.hunsicker.jalopy.language.JavaNode;
import de.hunsicker.jalopy.language.JavaNodeModifier;
import de.hunsicker.jalopy.language.JavaTokenTypes;
import de.hunsicker.jalopy.language.JavadocTokenTypes;
import de.hunsicker.jalopy.language.Node;
import de.hunsicker.jalopy.language.NodeHelper;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.jalopy.storage.Environment;


/**
 * Common superclass the main declarations elements of a Java source file (class,
 * interface declaration and the like).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class BasicDeclarationPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Root node text for generated Javadoc comments. */
    static final String GENERATED_COMMENT = "<GENERATED_JAVADOC_COMMENT>" /* NOI18N */;
    private static final String DELIMETER = "|" /* NOI18N */;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new BasicDeclarationPrinter object.
     */
    protected BasicDeclarationPrinter()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void print(
        AST        node,
        NodeWriter out)
      throws IOException
    {
        printCommentsBefore(node, out);
        logIssues(node, out);
    }


    /**
     * Adds a Javadoc comment to the given CLASS_DEF node.
     *
     * @param node a CLASS_DEF node.
     * @param out stream to write to.
     *
     * @since 1.0b8
     */
    protected void addClassComment(
        JavaNode   node,
        NodeWriter out)
    {
        String t =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CLASS,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_CLASS);
        Node text = new Node(JavadocTokenTypes.PCDATA, out.environment.interpolate(t));
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT, GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, null);
        token.setComment(comment);
        addComment(node, token);
    }


    /**
     * Adds a Javadoc comment to the given node.
     *
     * @param node node to add a Javadoc comment.
     * @param out stream to write to.
     */
    protected void addComment(
        JavaNode   node,
        NodeWriter out)
    {
        // check if the comment generation is enabled for the node type
        // and access level
        switch (node.getType())
        {
            case JavaTokenTypes.METHOD_DEF :

                if (
                    isEnabled(
                        this.settings.getInt(
                            ConventionKeys.COMMENT_JAVADOC_METHOD_MASK,
                            ConventionDefaults.COMMENT_JAVADOC_METHOD_MASK), node))
                {
                    addMethodComment(node, out);
                }

                break;

            case JavaTokenTypes.CTOR_DEF :

                if (
                    isEnabled(
                        this.settings.getInt(
                            ConventionKeys.COMMENT_JAVADOC_CTOR_MASK,
                            ConventionDefaults.COMMENT_JAVADOC_CTOR_MASK), node))
                {
                    addCtorComment(node, out);
                }

                break;

            case JavaTokenTypes.VARIABLE_DEF :

                if (
                    isEnabled(
                        this.settings.getInt(
                            ConventionKeys.COMMENT_JAVADOC_VARIABLE_MASK,
                            ConventionDefaults.COMMENT_JAVADOC_VARIABLE_MASK), node))
                {
                    addVariableComment(node);
                }

                break;

            case JavaTokenTypes.CLASS_DEF :

                if (
                    isEnabled(
                        this.settings.getInt(
                            ConventionKeys.COMMENT_JAVADOC_CLASS_MASK,
                            ConventionDefaults.COMMENT_JAVADOC_CLASS_MASK), node))
                {
                    addClassComment(node, out);
                }

                break;

            case JavaTokenTypes.INTERFACE_DEF :

                if (
                    isEnabled(
                        this.settings.getInt(
                            ConventionKeys.COMMENT_JAVADOC_CLASS_MASK,
                            ConventionDefaults.COMMENT_JAVADOC_CLASS_MASK), node))
                {
                    addInterfaceComment(node);
                }

                break;
        }
    }

    /**
     * Adds the generated comment to the given node.
     * @param node a declaration node.
     * @param comment the generated node.
     * @since 1.0b9
     */
    private void addComment(JavaNode node, ExtendedToken comment)
    {
        CommonHiddenStreamToken c = node.getFirstCommentBefore();

        if (c == null)
        {
            node.setHiddenBefore(comment);
        }
        else
        {
            for (; c != null; c = c.getHiddenAfter())
            {
                if (c.getHiddenAfter() == null)
                {
                    c.setHiddenAfter(comment);
                    break;
                }
            }
        }
    }

    /**
     * Adds a Javadoc comment to the given INTERFACE_DEF node.
     *
     * @param node a INTERFACE_DEF node.
     * @param out stream to write to.
     *
     * @since 1.0b8
     */
    protected void addInterfaceComment(JavaNode node)
    {
        String t =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_INTERFACE,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_INTERFACE);
        Node text = new Node(JavadocTokenTypes.PCDATA, t);
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT, GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, null);
        token.setComment(comment);
        addComment(node, token);
    }


    /**
     * Adds a Javadoc comment to the given METHOD_DEF node.
     *
     * @param node a METHOD_DEF node.
     * @param out stream to write to.
     */
    protected void addMethodComment(
        JavaNode   node,
        NodeWriter out)
    {
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT, GENERATED_COMMENT);
        StringBuffer buf = new StringBuffer(150);
        String topText =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP).trim();
        buf.append(topText);
        buf.append(DELIMETER);

        AST parameters = NodeHelper.getFirstChild(node, JavaTokenTypes.PARAMETERS);
        String bottomText =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM);
        String leadingSeparator = bottomText.substring(0, bottomText.indexOf('*') + 1);

        if (parameters.getFirstChild() != null)
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            addParameters(
                buf, parameters,
                this.settings.get(
                    ConventionKeys.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM,
                    ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM),
                out.environment);
        }

        AST returnType =
            NodeHelper.getFirstChild(node, JavaTokenTypes.TYPE).getFirstChild();

        if (!VOID.equals(returnType.getText()))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            buf.append(
                this.settings.get(
                    ConventionKeys.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN,
                    ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN));
            buf.append(DELIMETER);
        }

        AST exceptions = NodeHelper.getFirstChild(node, JavaTokenTypes.LITERAL_throws);

        if ((exceptions != null) && (exceptions.getFirstChild() != null))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);

            List types =
                JavadocPrinter.getValidTypeNames(node, JavaTokenTypes.LITERAL_throws);
            addExceptions(
                buf, types,
                this.settings.get(
                    ConventionKeys.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION,
                    ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION),
                out.environment);
        }

        buf.append(bottomText);

        Node text = new Node(JavadocTokenTypes.PCDATA, buf.toString());
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, null);
        token.setComment(comment);
        addComment(node, token);
    }


    /**
     * Adds a Javadoc comment for the given node, if necessary.
     *
     * @param node a declaration node.
     * @param out stream to write to.
     */
    void addCommentIfNeeded(
        JavaNode   node,
        NodeWriter out)
    {
        if (out.mode != NodeWriter.MODE_DEFAULT)
        {
            return;
        }

        if (
            !out.state.anonymousInnerClass
            && (!out.state.innerClass
            || this.settings.getBoolean(
                ConventionKeys.COMMENT_JAVADOC_INNER_CLASS,
                ConventionDefaults.COMMENT_JAVADOC_INNER_CLASS)))
        {
            boolean hasJavadoc = node.hasJavadocComment();

            if (!hasJavadoc && node.hasCommentsBefore())
            {
                CommonHiddenStreamToken comment = node.getFirstCommentBefore();

                if (comment.getHiddenAfter() == null)
                {
                    switch (comment.getType())
                    {
                        case JavaTokenTypes.SEPARATOR_COMMENT:
                            addComment(node, out);
                            break;

                        case JavaTokenTypes.ML_COMMENT:
                        case JavaTokenTypes.SL_COMMENT:
                            /**
                             * @todo transform the existing comment
                             */

                            /*
                              if (this.settings.getBoolean(ConventionKeys.COMMENT_JAVADOC_TRANSFORM,
                                                           ConventionDefaults.COMMENT_JAVADOC_TRANSFORM))
                            {
                            }
                            */
                            break;
                        case JavaTokenTypes.SPECIAL_COMMENT:

                            break;
                    }
                }
            }
            else if (!hasJavadoc)
            {
                addComment(node, out);
            }
        }
    }


    /**
     * Determines whether the auto-generation of Javadoc comments is enabled for the
     * given node.
     *
     * @param mask int mask that encodes the auto-generation settings for the given node.
     * @param node declaration node.
     *
     * @return <code>true</code> if the auto-generation feature is enabled for the given
     *         node.
     */
    private boolean isEnabled(
        int mask,
        AST node)
    {
        /**
         * @todo we currently don't calculate friendly access but rather misuse the FINAL
         *       modifier to indicate it, so if we find it here we know we have to
         *       handle it separately
         */
        if (mask == Modifier.FINAL)
        {
            return (JavaNodeModifier.valueOf(node) == 0);
        }

        int value = JavaNodeModifier.valueOf(node);

        if (((mask & value) != 0) || ((value == 0) && ((Modifier.FINAL & mask) != 0)))
        {
            return true;
        }

        return false;
    }


    /**
     * Adds a Javadoc comment to the given CTOR_DEF node.
     *
     * @param node a CTOR_DEF node.
     * @param out stream to write to.
     */
    private void addCtorComment(
        JavaNode   node,
        NodeWriter out)
    {
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT, GENERATED_COMMENT);
        String topText =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP).trim();
        StringBuffer buf = new StringBuffer();

        out.environment.set(
            Environment.Variable.TYPE_OBJECT.getName(),
            NodeHelper.getFirstChild(node, JavaTokenTypes.IDENT).getText());
        buf.append(out.environment.interpolate(topText));
        out.environment.unset(Environment.Variable.TYPE_OBJECT.getName());

        buf.append(DELIMETER);

        AST parameters = NodeHelper.getFirstChild(node, JavaTokenTypes.PARAMETERS);
        String bottomText =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM);
        String leadingSeparator = bottomText.substring(0, bottomText.indexOf('*') + 1);

        if (parameters.getFirstChild() != null)
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            addParameters(
                buf, parameters,
                this.settings.get(
                    ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM,
                    ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM),
                out.environment);
        }

        AST exceptions = NodeHelper.getFirstChild(node, JavaTokenTypes.LITERAL_throws);

        if ((exceptions != null) && (exceptions.getFirstChild() != null))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);

            List types =
                JavadocPrinter.getValidTypeNames(node, JavaTokenTypes.LITERAL_throws);
            addExceptions(
                buf, types,
                this.settings.get(
                    ConventionKeys.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION,
                    ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION),
                out.environment);
        }

        buf.append(bottomText);

        Node text = new Node(JavadocTokenTypes.PCDATA, buf.toString());
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, null);
        token.setComment(comment);
        addComment(node, token);
    }


    /**
     * Adds and interpolates the given expection template text for all exceptions of the
     * given node to the given buffer.
     *
     * @param buf buffer to add the interpolated text to.
     * @param types EXCEPTION node.
     * @param text exception template text
     * @param environment DOCUMENT ME!
     *
     * @since 1.0b8
     */
    private void addExceptions(
        StringBuffer buf,
        List         types,
        String       text,
        Environment  environment)
    {
        for (int i = 0, size = types.size(); i < size; i++)
        {
            String type = (String) types.get(i);
            environment.set(Environment.Variable.TYPE_EXCEPTION.getName(), type);
            buf.append(environment.interpolate(text));
            buf.append(DELIMETER);
            environment.unset(Environment.Variable.TYPE_EXCEPTION.getName());
        }
    }


    /**
     * Adds and interpolates the given parameters template text for all paramaeters of
     * the given node to the given buffer.
     *
     * @param buf buffer to add the interpolated text to.
     * @param node PARAMETERS node.
     * @param text exception template text
     * @param environment DOCUMENT ME!
     *
     * @since 1.0b8
     */
    private void addParameters(
        StringBuffer buf,
        AST          node,
        String       text,
        Environment  environment)
    {
        for (AST child = node.getFirstChild(); child != null;
            child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.PARAMETER_DEF :

                    String type =
                        NodeHelper.getFirstChild(child, JavaTokenTypes.IDENT).getText();
                    environment.set(Environment.Variable.TYPE_PARAM.getName(), type);
                    buf.append(environment.interpolate(text));
                    buf.append(DELIMETER);
                    environment.unset(Environment.Variable.TYPE_PARAM.getName());

                    break;
            }
        }
    }


    /**
     * Adds a Javadoc comment to the given VARIABLE_DEF node.
     *
     * @param node a VARIABLE_DEF node.
     */
    private void addVariableComment(JavaNode node)
    {
        String t =
            this.settings.get(
                ConventionKeys.COMMENT_JAVADOC_TEMPLATE_VARIABLE,
                ConventionDefaults.COMMENT_JAVADOC_TEMPLATE_VARIABLE);
        Node text = new Node(JavadocTokenTypes.PCDATA, t);
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT, GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT, null);
        token.setComment(comment);
        addComment(node, token);
    }
}
