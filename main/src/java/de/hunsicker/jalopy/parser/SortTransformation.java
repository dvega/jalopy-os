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
package de.hunsicker.jalopy.parser;

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Convention;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


/**
 * Transformation which sorts the nodes of a tree according to some user
 * configurable policy.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class SortTransformation
    implements Transformation
{
    //~ Static variables/initializers ·········································

    private static final String EMPTY_STRING = "";

    //~ Instance variables ····················································

    /**
     * Comparator for CLASS_DEF, INTERFACE_DEF, CTOR_DEF and METHOD_DEF nodes.
     */
    private final NodeComparator _defaultComparator = new NodeComparator();

    /** Comparator for VARIABLE_DEF nodes */
    private final VariableDefNodeComparator _variablesComparator = new VariableDefNodeComparator();

    //~ Constructors ··························································

    /**
     * Creates a new SortTransformation object.
     */
    public SortTransformation()
    {
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void apply(AST tree)
        throws TransformationException
    {
        sort(tree, _defaultComparator);
    }


    /**
     * Sorts the given tree.
     *
     * @param tree root node of the tree.
     * @param comp comparator to use.
     */
    public void sort(AST        tree,
                     Comparator comp)
    {
        if (tree == null)
        {
            return;
        }

        AST first = null;
LOOP:

        // advance to the first CLASS_DEF or INTERFACE_DEF
        for (AST child = tree.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.CLASS_DEF :
                case JavaTokenTypes.INTERFACE_DEF :
                    first = child;

                    break LOOP;
            }
        }

        for (AST declaration = first;
             declaration != null;
             declaration = declaration.getNextSibling())
        {
            sortDeclarations(declaration, comp, 1);
        }
    }


    /**
     * Determines whether the given declaration node contains the
     * <code>static</code> modifier.
     *
     * @param node declaration node.
     *
     * @return <code>true</code> if the given node contains the
     *         <code>static</code> modifier.
     *
     * @since 1.0b8
     */
    private boolean isStatic(AST node)
    {
        return JavaNodeModifier.isStatic(NodeHelper.getFirstChild(node,
                                                                  JavaTokenTypes.MODIFIERS));
    }


    /**
     * Appends <em>sibling</em> as a new sibling to <em>node</em>.
     *
     * @param node the currently last sibling.
     * @param sibling the new sibling that should be appended.
     */
    private void addChild(JavaNode node,
                          JavaNode sibling)
    {
        node.setNextSibling(sibling);
        sibling.prevSibling = node;
        sibling.setNextSibling(null); // don't leave any old pointers left
    }


    /**
     * Add all nodes of the given list as siblings to the given node.
     *
     * @param nodes nodes to add.
     * @param node node to add the nodes as siblings to.
     * @param addSeparator if <code>true</code> adds a separator comment
     *        before the first node of the siblings list.
     * @param indent current indentation length.
     * @param maxwidth the maximum line length.
     *
     * @return the last node that was added. Returns the given node if no
     *         nodes were added.
     *
     * @throws IllegalArgumentException if <em>node</em> contains an invalid
     *         node.
     */
    private JavaNode addSiblings(List     nodes,
                                 JavaNode node,
                                 boolean  addSeparator,
                                 int      indent,
                                 int      maxwidth)
    {
        JavaNode cur = node;

        if (nodes.size() > 0)
        {
            JavaNode next = (JavaNode)nodes.get(0);
            addChild((JavaNode)cur, next);
            cur = next;

            if (addSeparator)
            {
                ExtendedToken comment = new ExtendedToken(JavaTokenTypes.SEPARATOR_COMMENT,
                                                          EMPTY_STRING);

                if (next.hasCommentsBefore())
                {
                    for (CommonHiddenStreamToken tok = next.getHiddenBefore();
                         tok != null;
                         tok = tok.getHiddenBefore())
                    {
                        if (tok.getHiddenBefore() == null)
                        {
                            tok.setHiddenBefore(comment);
                            comment.setHiddenAfter(tok);

                            break;
                        }
                    }
                }
                else
                {
                    next.setHiddenBefore(comment);
                }

                Convention settings = Convention.getInstance();
                String fillCharacter = settings.get(Keys.SEPARATOR_FILL_CHARACTER,
                                                 "\u00b7");

                switch (next.getType())
                {
                    case JavaTokenTypes.VARIABLE_DEF :

                        if (isStatic(cur))
                        {
                            fillComment(comment,
                                        settings.get(
                                                  Keys.SEPARATOR_STATIC_VAR_INIT,
                                                  "Static variables/initializers"),
                                        fillCharacter, indent, maxwidth);
                        }
                        else
                        {
                            fillComment(comment,
                                        settings.get(Keys.SEPARATOR_INSTANCE_VAR,
                                                  "Instance variables"),
                                        fillCharacter, indent, maxwidth);
                        }

                        break;

                    case JavaTokenTypes.METHOD_DEF :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_METHOD, "Methods"),
                                    fillCharacter, indent, maxwidth);

                        break;

                    case JavaTokenTypes.CTOR_DEF :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_CTOR,
                                              "Constructors"), fillCharacter,
                                    indent, maxwidth);

                        break;

                    case JavaTokenTypes.CLASS_DEF :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_CLASS,
                                              "Inner classes"), fillCharacter,
                                    indent, maxwidth);

                        break;

                    case JavaTokenTypes.INTERFACE_DEF :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_INTERFACE,
                                              "Inner Interfaces"),
                                    fillCharacter, indent, maxwidth);

                        break;

                    case JavaTokenTypes.STATIC_INIT :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_STATIC_VAR_INIT,
                                              "Static variables/initializers"),
                                    fillCharacter, indent, maxwidth);

                        break;

                    case JavaTokenTypes.INSTANCE_INIT :
                        fillComment(comment,
                                    settings.get(Keys.SEPARATOR_INSTANCE_INIT,
                                              "Instance initializers"),
                                    fillCharacter, indent, maxwidth);

                        break;

                    default :
                        throw new IllegalArgumentException("unexpected type -- " +
                                                           cur);
                }
            }
        }
        else
        {
            return node;
        }

        for (int i = 1, size = nodes.size(); i < size; i++)
        {
            JavaNode next = (JavaNode)nodes.get(i);
            addChild((JavaNode)cur, next);
            cur = next;
        }

        return cur;
    }


    /**
     * Fills the given separator comment up to the given maximum size with a
     * given character.
     *
     * @param comment comment to fill.
     * @param text comment text.
     * @param character character to use.
     * @param indent current indent length.
     * @param maxwidth maximum line length.
     */
    private void fillComment(ExtendedToken comment,
                             String        text,
                             String        character,
                             int           indent,
                             int           maxwidth)
    {
        StringBuffer buf = new StringBuffer(maxwidth);
        buf.append("//~ ");
        buf.append(text);
        buf.append(' ');

        for (int i = text.length() + 4, size = maxwidth - indent - 1;
             i < size;
             i++)
        {
            buf.append(character);
        }

        comment.setText(buf.toString());
    }


    /**
     * Sorts the given tree portion.
     *
     * @param node the root node of the tree to sort. Either a CLASS_DEF or
     *        INTERFACE_DEF.
     * @param comp comparator to use for sorting.
     * @param level the current recursion level (1-based).
     *
     * @return the updated node.
     *
     * @throws IllegalArgumentException if the given node contains a child of
     *         an unknown type.
     */
    private AST sortDeclarations(AST        node,
                                 Comparator comp,
                                 int        level)
    {
        JavaNode lcurly = null;

        switch (node.getType())
        {
            case JavaTokenTypes.CLASS_DEF :
                lcurly = (JavaNode)node.getFirstChild().getNextSibling()
                                       .getNextSibling().getNextSibling()
                                       .getNextSibling();

                break;

            case JavaTokenTypes.INTERFACE_DEF :
                lcurly = (JavaNode)node.getFirstChild().getNextSibling()
                                       .getNextSibling().getNextSibling();

                break;

            default :
                return node;
        }

        switch (lcurly.getFirstChild().getType())
        {
            // empty block, nothing to do
            case JavaTokenTypes.RCURLY :
                return node;
        }

        List staticStuff = new ArrayList(3); // both variables and initializers
        List variables = new ArrayList(); // instance variables
        List initializers = new ArrayList(3); // instance initializers
        List ctors = new ArrayList(5);
        List methods = new ArrayList();
        List classes = new ArrayList(3);
        List interfaces = new ArrayList(3);
        List names = new ArrayList(); // type names of all instance variables

        AST rcurly = null; // stores the last rcurly

        // add nodes to the different lists
        for (AST child = lcurly.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.METHOD_DEF :
                    methods.add(child);

                    break;

                case JavaTokenTypes.VARIABLE_DEF :

                    if (isStatic(child))
                    {
                        staticStuff.add(child);
                    }
                    else
                    {
                        // store the name of the variable types
                        names.add(NodeHelper.getFirstChild(child,
                                                           JavaTokenTypes.IDENT)
                                            .getText());
                        variables.add(child);
                    }

                    break;

                case JavaTokenTypes.CTOR_DEF :
                    ctors.add(child);

                    break;

                case JavaTokenTypes.STATIC_INIT :
                    staticStuff.add(child);

                    break;

                case JavaTokenTypes.INSTANCE_INIT :
                    initializers.add(child);

                    break;

                case JavaTokenTypes.CLASS_DEF :
                    classes.add(sortDeclarations(child, comp, level + 1));

                    break;

                case JavaTokenTypes.INTERFACE_DEF :
                    interfaces.add(sortDeclarations(child, comp, level + 1));

                    break;

                case JavaTokenTypes.RCURLY :
                    rcurly = child;

                    break;

                case JavaTokenTypes.SEMI :

                    // it is perfectly valid to use a SEMI and totally
                    // useless, we ignore it and don't care (at least until
                    // someone rings a bell)
                    break;

                default :
                    throw new IllegalArgumentException("cannot handle node -- " +
                                                       child);
            }
        }

        Convention settings = Convention.getInstance();

        if (settings.getBoolean(Keys.SORT_VARIABLE, Defaults.SORT_VARIABLE))
        {
            // because we recursively link into inner classes in the switch we
            // have to set our type names for every level
            _variablesComparator.names = names;
            Collections.sort(variables, _variablesComparator);
            names.clear();
        }

        if (settings.getBoolean(Keys.SORT_CTOR, Defaults.SORT_CTOR))
        {
            Collections.sort(ctors, comp);
        }

        if (settings.getBoolean(Keys.SORT_METHOD, Defaults.SORT_METHOD))
        {
            Collections.sort(methods, comp);
        }

        if (settings.getBoolean(Keys.SORT_CLASS, Defaults.SORT_CLASS))
        {
            Collections.sort(classes, comp);
        }

        if (settings.getBoolean(Keys.SORT_INTERFACE, Defaults.SORT_INTERFACE))
        {
            Collections.sort(interfaces, comp);
        }

        Map nodes = new HashMap(8, 1.0f);
        nodes.put(DeclarationType.STATIC_VARIABLE_INIT.getName(), staticStuff);
        nodes.put(DeclarationType.VARIABLE.getName(), variables);
        nodes.put(DeclarationType.INIT.getName(), initializers);
        nodes.put(DeclarationType.CTOR.getName(), ctors);
        nodes.put(DeclarationType.METHOD.getName(), methods);
        nodes.put(DeclarationType.INTERFACE.getName(), interfaces);
        nodes.put(DeclarationType.CLASS.getName(), classes);

        boolean addSeparator = false;

        if (level == 1)
        {
            addSeparator = settings.getBoolean(Keys.COMMENT_INSERT_SEPARATOR,
                                            Defaults.COMMENT_INSERT_SEPARATOR);
        }
        else
        {
            addSeparator = settings.getBoolean(Keys.COMMENT_INSERT_SEPARATOR_RECURSIVE,
                                            Defaults.COMMENT_INSERT_SEPARATOR_RECURSIVE);
        }

        String sortString = settings.get(Keys.SORT_ORDER,
                                      DeclarationType.getOrder());
        int maxwidth = settings.getInt(Keys.LINE_LENGTH, Defaults.LINE_LENGTH);
        int indent = settings.getInt(Keys.INDENT_SIZE, Defaults.INDENT_SIZE);
        JavaNode tmp = new JavaNode();
        JavaNode current = tmp;

        // add the different declaration groups in the specified order
        for (StringTokenizer tokens = new StringTokenizer(sortString, ",");
             tokens.hasMoreTokens();)
        {
            current = addSiblings((List)nodes.get(tokens.nextToken()), current,
                                  addSeparator, indent * level, maxwidth);
        }

        current.setNextSibling(rcurly);

        // get the first sibling
        JavaNode sibling = (JavaNode)tmp.getNextSibling();

        // and link it into the tree
        sibling.prevSibling = lcurly;
        lcurly.setFirstChild(sibling);

        tmp.setNextSibling(null); // don't leave any old pointers set

        current.setNextSibling(rcurly);

        return node;
    }
}
