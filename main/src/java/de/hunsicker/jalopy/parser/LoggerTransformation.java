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

import de.hunsicker.antlr.ASTFactory;
import de.hunsicker.antlr.collections.AST;

import java.util.ArrayList;
import java.util.List;


/**
 * Transformation which checks debug logging statements and adds an enclosing
 * boolean expression if not yet present.
 * 
 * <p>
 * The current implementation only works for the Jakarta Log4J logging toolkit
 * and is somewhat weak in that every method call with a name of
 * &quot;debug&quot; will be recognized as a logging call.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class LoggerTransformation
    extends TreeWalker
    implements Transformation
{
    //~ Static variables/initializers ·········································

    private static final String DEBUG = "debug";
    private static final String LEVEL_DEBUG = "Level.DEBUG";
    private static final String LOCALIZED_LOG = "l7dlog";
    private static final String PRIORITY_DEBUG = "Priority.DEBUG";
    private static final ASTFactory AST_FACTORY = new JavaNodeFactory();

    //~ Instance variables ····················································

    private List _calls = new ArrayList(50); // List of <JavaNode>

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void apply(AST tree)
        throws TransformationException
    {
        try
        {
            walk(tree);

            for (int i = 0, size = _calls.size(); i < size; i++)
            {
                AST node = (AST)_calls.get(i);
                AST name = node.getFirstChild();

                switch (name.getType())
                {
                    case JavaTokenTypes.DOT :

                        AST firstPart = name.getFirstChild();
                        AST lastPart = firstPart.getNextSibling();
                        String methodName = lastPart.getText();

                        if (isDebugCall(node, methodName))
                        {
                            JavaNode expr = ((JavaNode)node).getParent();

                            if (!isEnclosed(expr))
                            {
                                addConditional(expr, firstPart);
                            }
                        }

                        break;
                }
            }
        }
        finally
        {
            _calls.clear();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void visit(AST node)
    {
        switch (node.getType())
        {
            case JavaTokenTypes.METHOD_CALL :

                // we can't apply the transformation during the tree
                // walking, because we alter the nodes in a way that
                // affects the walk so we simply store all method call
                // nodes to defer the actual work after the walk has
                // finished
                _calls.add(node);

                break;
        }
    }


    /**
     * Determines whether the given node represents a debug call.
     *
     * @param node METHOD_CALL node to check.
     * @param name name of the method call.
     *
     * @return <code>true</code> if the given node represents a method call.
     */
    private boolean isDebugCall(AST    node,
                                String name)
    {
        /**
         * @todo we need a Symbol Table to query the type of the ref var
         */
        if (DEBUG.equals(name))
        {
            JavaNode n = (JavaNode)node;

            switch (n.getType())
            {
                case JavaTokenTypes.ASSIGN :
                case JavaTokenTypes.EXPR :
                    return false;
            }

            AST params = NodeHelper.getFirstChild(node, JavaTokenTypes.ELIST);
            AST expr = params.getFirstChild();

            // we need at least one parameter
            if (expr == null)
            {
                return false;
            }

            return true;
        }
        else if (LOCALIZED_LOG.equals(name))
        {
            AST params = NodeHelper.getFirstChild(node, JavaTokenTypes.ELIST);
            AST expr = params.getFirstChild();

            if (expr != null)
            {
                AST param = expr.getFirstChild();

                switch (param.getType())
                {
                    case JavaTokenTypes.DOT :

                        String n = NodeHelper.getDottedName(param);

                        if (LEVEL_DEBUG.equals(n) || PRIORITY_DEBUG.equals(n))
                        {
                            return true;
                        }

                        break;
                }
            }
        }

        return false;
    }


    /**
     * Determines whether the given expression (denoting a method call) is
     * enclosed with a conditional expression.
     *
     * @param expr EXPR node to check.
     *
     * @return <code>true</code> if an enclosing conditional expression could
     *         be found.
     */
    private boolean isEnclosed(JavaNode expr)
    {
        JavaNode parent = expr.getParent();

        switch (parent.getType())
        {
            case JavaTokenTypes.LITERAL_if :
                return true;

            case JavaTokenTypes.SLIST :
                return isEnclosed(parent);

            default :
                return false;
        }
    }


    /**
     * Adds a conditional expression for the given debug method call.
     *
     * @param expr our method call node to add conditional for.
     * @param name name of the debug method call.
     */
    private void addConditional(JavaNode expr,
                                AST      name)
    {
        JavaNode cond = createConditional(name);
        JavaNode parent = expr.getParent();
        JavaNode prev = expr.getPreviousSibling();
        JavaNode next = (JavaNode)expr.getNextSibling();
        cond.setParent(parent);
        cond.setPreviousSibling(prev);

        if (parent == prev)
        {
            prev.setFirstChild(cond);
        }
        else
        {
            prev.setNextSibling(cond);
        }

        JavaNode lparen = (JavaNode)cond.getFirstChild().getNextSibling()
                                        .getNextSibling();
        lparen.setNextSibling(expr);
        expr.setParent(cond);
        expr.setPreviousSibling(lparen);
        expr.setNextSibling(null);

        if (next != null)
        {
            cond.setNextSibling(next);
            next.setPreviousSibling(cond);
        }
    }


    /**
     * Creates the enclosing conditional expression.
     *
     * @param name name name of the debug method call.
     *
     * @return enclosing expression.
     */
    private JavaNode createConditional(AST name)
    {
        AST qualifiedName = AST_FACTORY.create(JavaTokenTypes.DOT);
        qualifiedName.addChild(AST_FACTORY.dupTree(name));

        /**
         * @todo make name configurable
         */
        AST methodName = AST_FACTORY.create(JavaTokenTypes.IDENT,
                                            "isDebugEnabled");
        qualifiedName.addChild(methodName);

        AST methodCall = AST_FACTORY.create(JavaTokenTypes.METHOD_CALL);
        methodCall.addChild(qualifiedName);
        methodCall.addChild(AST_FACTORY.create(JavaTokenTypes.ELIST));
        methodCall.addChild(AST_FACTORY.create(JavaTokenTypes.RPAREN));

        AST expr = AST_FACTORY.create(JavaTokenTypes.EXPR);
        expr.addChild(methodCall);

        AST ifNode = AST_FACTORY.create(JavaTokenTypes.LITERAL_if);
        ifNode.addChild(AST_FACTORY.create(JavaTokenTypes.LPAREN));
        ifNode.addChild(expr);
        ifNode.addChild(AST_FACTORY.create(JavaTokenTypes.RPAREN));

        return (JavaNode)ifNode;
    }
}
