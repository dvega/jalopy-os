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

import de.hunsicker.antlr.collections.AST;

import java.util.Comparator;
import java.util.List;


/**
 * Compares two VARIABLE_DEF nodes first by accessibility, then by name.
 * Special checking is applied to avoid forward references as a result to
 * mindless sorting.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class VariableDefNodeComparator
    implements Comparator
{
    //~ Instance variables ····················································

    /** Holds all instance variable type names of a given class. */
    List names; // List of <String>

    /** Simple tree walker to search tree portions. */
    private final TreeSearcher _searcher = new TreeSearcher();

    //~ Constructors ··························································

    /**
     * Creates a new VariableDefNodeComparator object.
     */
    public VariableDefNodeComparator()
    {
    }

    //~ Methods ·······························································

    /**
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     *
     * @param o1 the first node.
     * @param o2 the second node
     *
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public int compare(Object o1,
                       Object o2)
    {
        if (o1 == o2)
        {
            return 0;
        }

        if (this.names == null)
        {
            throw new IllegalStateException("no variable type names has been set");
        }

        AST node1 = (AST)o1;
        AST node2 = (AST)o2;
        String name1 = NodeHelper.getFirstChild(node1, JavaTokenTypes.IDENT)
                                 .getText();
        String name2 = NodeHelper.getFirstChild(node2, JavaTokenTypes.IDENT)
                                 .getText();

        // first make sure we don't introduce forward references
        //
        //      private short indentSize = 4;
        //      private short currentIndent = indentSize;
        //
        AST assign = NodeHelper.getFirstChild(node2, JavaTokenTypes.ASSIGN);

        if (assign != null)
        {
            _searcher.reset();
            _searcher.name = name1;
            _searcher.walk(assign);

            // if the name of the first node is contained in the second one,
            if (_searcher.result == _searcher.FOUND)
            {
                return -1;
            }
        }

        // now check the accessibility
        int mod1 = JavaNodeModifier.valueOf(node1);
        int mod2 = JavaNodeModifier.valueOf(node2);
        int result = NodeComparator.compareModifiers(mod1, mod2);

        if (result != 0)
        {
            return result;
        }

        result = NodeComparator.compareTypes(node1, node2);

        if (result != 0)
        {
            return result;
        }

        return NodeComparator.compareNames(node1, node2);
    }

    //~ Inner Classes ·························································

    private static final class TreeSearcher
        extends TreeWalker
    {
        static final int FOUND = 1;
        String name;
        int result;

        public void reset()
        {
            super.reset();
            this.result = 0;
        }


        public void visit(AST node)
        {
            if (this.name.equals(node.getText()))
            {
                this.result = FOUND;
                stop();
            }
        }
    }
}
