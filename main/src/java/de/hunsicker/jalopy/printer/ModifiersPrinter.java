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
import de.hunsicker.jalopy.parser.ModifierType;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Printer for the imaginary modifiers node (<code>MODIFIERS</code>).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class ModifiersPrinter
    extends AbstractPrinter
{
    //~ Static variables/initializers ·········································

    /** Comparator to sort modifiers. */
    private static final Comparator COMP_MODIFIERS = new ModifiersComparator();

    /** Singleton. */
    private static final Printer INSTANCE = new ModifiersPrinter();

    //~ Constructors ··························································

    /**
     * Creates new ModifiersPrinter object.
     */
    protected ModifiersPrinter()
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
        if (this.settings.getBoolean(Keys.SORT_MODIFIERS, Defaults.SORT_MODIFIERS))
        {
            JavaNode firstModifier = (JavaNode)node.getFirstChild();

            if (firstModifier != null)
            {
                CommonHiddenStreamToken firstComment = firstModifier.getHiddenBefore();
                firstModifier.setHiddenBefore(null);

                List modifiers = new ArrayList(5);

                for (AST modifier = firstModifier;
                     modifier != null;
                     modifier = modifier.getNextSibling())
                {
                    modifiers.add(modifier);
                }

                Collections.sort(modifiers, COMP_MODIFIERS);

                firstModifier = (JavaNode)modifiers.get(0);
                firstModifier.setHiddenBefore(firstComment);

                for (int i = 0, size = modifiers.size(); i < size; i++)
                {
                    AST modifier = (AST)modifiers.get(i);
                    PrinterFactory.create(modifier).print(modifier, out);
                }
            }
        }
        else
        {
            for (AST modifier = node.getFirstChild();
                 modifier != null;
                 modifier = modifier.getNextSibling())
            {
                PrinterFactory.create(modifier).print(modifier, out);
            }
        }
    }

    //~ Inner Classes ·························································

    private static final class ModifiersComparator
        implements Comparator
    {
        public int compare(Object o1,
                           Object o2)
        {
            AST node1 = (AST)o1;
            AST node2 = (AST)o2;

            ModifierType modifier1 = ModifierType.valueOf(node1.getText());
            ModifierType modifier2 = ModifierType.valueOf(node2.getText());

            return modifier1.compareTo(modifier2);
        }


        public boolean equals(Object o)
        {
            if (o instanceof ModifiersComparator)
            {
                return true;
            }

            return false;
        }


        public int hashCode()
        {
            return super.hashCode();
        }
    }
}
