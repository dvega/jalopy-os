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
import de.hunsicker.jalopy.Environment;
import de.hunsicker.jalopy.parser.ExtendedToken;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaNodeModifier;
import de.hunsicker.jalopy.parser.JavaTokenTypes;
import de.hunsicker.jalopy.parser.JavadocTokenTypes;
import de.hunsicker.jalopy.parser.Node;
import de.hunsicker.jalopy.parser.NodeHelper;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;


/**
 * Common superclass the main declarations elements of a Java source file
 * (class, interface declaration and the like).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class BasicDeclarationPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Root node text for generated Javadoc comments. */
    static final String GENERATED_COMMENT = "<GENERATED_JAVADOC_COMMENT>";
    private static final String DELIMETER = "|";

    //~ Constructors ··························································

    /**
     * Creates a new BasicDeclarationPrinter object.
     */
    protected BasicDeclarationPrinter()
    {
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void print(AST        node,
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
     *
     * @since 1.0b8
     */
    protected void addClassComment(JavaNode node, NodeWriter out)
    {
        String t = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_CLASS,
                                  Defaults.COMMENT_JAVADOC_TEMPLATE_CLASS);
        Node text = new Node(JavadocTokenTypes.PCDATA, out.environment.interpolate(t));
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT,
                                GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT,
                                                null);
        token.setComment(comment);
        node.setHiddenBefore(token);
    }


    /**
     * Adds a Javadoc comment to the given node.
     *
     * @param node node to add a Javadoc comment.
     * @param out stream to write to.
     */
    protected void addComment(JavaNode   node,
                              NodeWriter out)
    {
        // check if the comment generation is enabled for the node type
        // and access level
        switch (node.getType())
        {
            case JavaTokenTypes.METHOD_DEF :

                if (isEnabled(this.prefs.getInt(
                                                Keys.COMMENT_JAVADOC_METHOD_MASK,
                                                Defaults.COMMENT_JAVADOC_METHOD_MASK),
                              node))
                {
                    addMethodComment(node, out);
                }

                break;

            case JavaTokenTypes.CTOR_DEF :

                if (isEnabled(this.prefs.getInt(Keys.COMMENT_JAVADOC_CTOR_MASK,
                                                Defaults.COMMENT_JAVADOC_CTOR_MASK),
                              node))
                {
                    addCtorComment(node, out);
                }

                break;

            case JavaTokenTypes.VARIABLE_DEF :

                if (isEnabled(this.prefs.getInt(
                                                Keys.COMMENT_JAVADOC_VARIABLE_MASK,
                                                Defaults.COMMENT_JAVADOC_VARIABLE_MASK),
                              node))
                {
                    addVariableComment(node);
                }

                break;

            case JavaTokenTypes.CLASS_DEF :

                if (isEnabled(this.prefs.getInt(
                                                Keys.COMMENT_JAVADOC_CLASS_MASK,
                                                Defaults.COMMENT_JAVADOC_CLASS_MASK),
                              node))
                {
                    addClassComment(node, out);
                }

                break;

            case JavaTokenTypes.INTERFACE_DEF :

                if (isEnabled(this.prefs.getInt(
                                                Keys.COMMENT_JAVADOC_CLASS_MASK,
                                                Defaults.COMMENT_JAVADOC_CLASS_MASK),
                              node))
                {
                    addInterfaceComment(node);
                }

                break;
        }
    }


    /**
     * Adds a Javadoc comment to the given INTERFACE_DEF node.
     *
     * @param node a INTERFACE_DEF node.
     *
     * @since 1.0b8
     */
    protected void addInterfaceComment(JavaNode node)
    {
        String t = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_INTERFACE,
                                  Defaults.COMMENT_JAVADOC_TEMPLATE_INTERFACE);
        Node text = new Node(JavadocTokenTypes.PCDATA, t);
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT,
                                GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT,
                                                null);
        token.setComment(comment);
        node.setHiddenBefore(token);
    }


    /**
     * Adds a Javadoc comment to the given METHOD_DEF node.
     *
     * @param node a METHOD_DEF node.
     * @param out stream to write to.
     */
    protected void addMethodComment(JavaNode   node,
                                    NodeWriter out)
    {
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT,
                                GENERATED_COMMENT);
        StringBuffer buf = new StringBuffer(150);
        String topText = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP,
                                        Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_TOP)
                                   .trim();
        buf.append(topText);
        buf.append(DELIMETER);

        AST parameters = NodeHelper.getFirstChild(node,
                                                  JavaTokenTypes.PARAMETERS);
        String bottomText = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM,
                                           Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_BOTTOM);
        String leadingSeparator = bottomText.substring(0,
                                                       bottomText.indexOf('*') +
                                                       1);

        if (parameters.getFirstChild() != null)
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            addParameters(buf, parameters,
                          this.prefs.get(
                                         Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM,
                                         Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_PARAM),
                          out.environment);
        }

        AST returnType = NodeHelper.getFirstChild(node, JavaTokenTypes.TYPE)
                                   .getFirstChild();

        if (!VOID.equals(returnType.getText()))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            buf.append(this.prefs.get(
                                      Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN,
                                      Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_RETURN));
            buf.append(DELIMETER);
        }

        AST exceptions = NodeHelper.getFirstChild(node,
                                                  JavaTokenTypes.LITERAL_throws);

        if ((exceptions != null) && (exceptions.getFirstChild() != null))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);

            List types = JavadocPrinter.getValidTypeNames(node,
                                                          JavaTokenTypes.LITERAL_throws);
            addExceptions(buf, types,
                          this.prefs.get(
                                         Keys.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION,
                                         Defaults.COMMENT_JAVADOC_TEMPLATE_METHOD_EXCEPTION),
                          out.environment);
        }

        buf.append(bottomText);

        Node text = new Node(JavadocTokenTypes.PCDATA, buf.toString());
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT,
                                                null);
        token.setComment(comment);
        node.setHiddenBefore(token);
    }


    /**
     * Adds a Javadoc comment for the given node, if necessary.
     *
     * @param node a declaration node.
     * @param out stream to write to.
     */
    void addCommentIfNeeded(JavaNode   node,
                            NodeWriter out)
    {
        if (out.mode != NodeWriter.MODE_DEFAULT)
        {
            return;
        }

        if ((!out.state.anonymousInnerClass) &&
            ((!out.state.innerClass) ||
             this.prefs.getBoolean(Keys.COMMENT_JAVADOC_INNER_CLASS,
                                   Defaults.COMMENT_JAVADOC_INNER_CLASS)))
        {
            boolean hasJavadoc = node.hasJavadocComment();

            if ((!hasJavadoc) && node.hasCommentsBefore())
            {
                /**
                 * @todo transform the existing comment
                 */

                /*if (this.prefs.getBoolean(Keys.COMMENT_JAVADOC_TRANSFORM, Defaults.COMMENT_JAVADOC_TRANSFORM))
                {
                }*/
            }
            else if (!hasJavadoc)
            {
                addComment(node, out);
            }
        }
    }


    /**
     * Determines whether the auto-generation of Javadoc comments is enabled
     * for the given node.
     *
     * @param mask int mask that encodes the auto-generation preferences for
     *        the given node.
     * @param node declaration node.
     *
     * @return <code>true</code> if the auto-generation feature is enabled for
     *         the given node.
     */
    private boolean isEnabled(int mask,
                              AST node)
    {
        /**
         * @todo we currently don't calculate friendly access but rather
         *       misuse the FINAL modifier to indicate it, so if we find it
         *       here we know we have to handle it separately
         */
        if (mask == Modifier.FINAL)
        {
            return (JavaNodeModifier.valueOf(node) == 0);
        }

        int value = JavaNodeModifier.valueOf(node);

        if (((mask & value) != 0) ||
            ((value == 0) && ((Modifier.FINAL & mask) != 0)))
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
    private void addCtorComment(JavaNode   node,
                                NodeWriter out)
    {
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT,
                                GENERATED_COMMENT);
        String topText = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP,
                                        Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_TOP)
                                   .trim();
        StringBuffer buf = new StringBuffer();

        out.environment.set(Environment.Variable.TYPE_OBJECT.getName(),
                            NodeHelper.getFirstChild(node, JavaTokenTypes.IDENT)
                                      .getText());
        buf.append(out.environment.interpolate(topText));
        out.environment.unset(Environment.Variable.TYPE_OBJECT.getName());

        buf.append(DELIMETER);

        AST parameters = NodeHelper.getFirstChild(node,
                                                  JavaTokenTypes.PARAMETERS);
        String bottomText = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM,
                                           Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_BOTTOM);
        String leadingSeparator = bottomText.substring(0,
                                                       bottomText.indexOf('*') +
                                                       1);

        if (parameters.getFirstChild() != null)
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);
            addParameters(buf, parameters,
                          this.prefs.get(
                                         Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM,
                                         Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_PARAM),
                          out.environment);
        }

        AST exceptions = NodeHelper.getFirstChild(node,
                                                  JavaTokenTypes.LITERAL_throws);

        if ((exceptions != null) && (exceptions.getFirstChild() != null))
        {
            buf.append(leadingSeparator);
            buf.append(DELIMETER);

            List types = JavadocPrinter.getValidTypeNames(node,
                                                          JavaTokenTypes.LITERAL_throws);
            addExceptions(buf, types,
                          this.prefs.get(
                                         Keys.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION,
                                         Defaults.COMMENT_JAVADOC_TEMPLATE_CTOR_EXCEPTION),
                          out.environment);
        }

        buf.append(bottomText);

        Node text = new Node(JavadocTokenTypes.PCDATA, buf.toString());
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT,
                                                null);
        token.setComment(comment);
        node.setHiddenBefore(token);
    }


    /**
     * Adds and interpolates the given expection template text for all
     * exceptions of the given node to the given buffer.
     *
     * @param buf buffer to add the interpolated text to.
     * @param types EXCEPTION node.
     * @param text exception template text
     * @param environment DOCUMENT ME!
     *
     * @since 1.0b8
     */
    private void addExceptions(StringBuffer buf,
                               List         types,
                               String       text,
                               Environment  environment)
    {
        for (int i = 0, size = types.size(); i < size; i++)
        {
            String type = (String)types.get(i);
            environment.set(Environment.Variable.TYPE_EXCEPTION.getName(), type);
            buf.append(environment.interpolate(text));
            buf.append(DELIMETER);
            environment.unset(Environment.Variable.TYPE_EXCEPTION.getName());
        }
    }


    /**
     * Adds and interpolates the given parameters template text for all
     * paramaeters of the given node to the given buffer.
     *
     * @param buf buffer to add the interpolated text to.
     * @param node PARAMETERS node.
     * @param text exception template text
     * @param environment DOCUMENT ME!
     *
     * @since 1.0b8
     */
    private void addParameters(StringBuffer buf,
                               AST          node,
                               String       text,
                               Environment  environment)
    {
        for (AST child = node.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.PARAMETER_DEF :

                    String type = NodeHelper.getFirstChild(child,
                                                           JavaTokenTypes.IDENT)
                                            .getText();
                    environment.set(Environment.Variable.TYPE_PARAM.getName(),
                                    type);
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
        String t = this.prefs.get(Keys.COMMENT_JAVADOC_TEMPLATE_VARIABLE,
                                  Defaults.COMMENT_JAVADOC_TEMPLATE_VARIABLE);
        Node text = new Node(JavadocTokenTypes.PCDATA, t);
        Node comment = new Node(JavaTokenTypes.JAVADOC_COMMENT,
                                GENERATED_COMMENT);
        comment.setFirstChild(text);

        ExtendedToken token = new ExtendedToken(JavaTokenTypes.JAVADOC_COMMENT,
                                                null);
        token.setComment(comment);
        node.setHiddenBefore(token);
    }
}
