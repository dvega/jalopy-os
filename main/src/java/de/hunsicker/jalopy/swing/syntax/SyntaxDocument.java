/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing.syntax;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;


/**
 * DOCUMENT ME!
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @since 1.0b9
 */
final class SyntaxDocument
    extends PlainDocument
{
    //~ Constructors ---------------------------------------------------------------------

    public SyntaxDocument()
    {
        super();
    }


    /**
     * Creates a new SyntaxDocument object.
     *
     * @param content DOCUMENT ME!
     */
    public SyntaxDocument(Content content)
    {
        super(content);
    }

    //~ Methods --------------------------------------------------------------------------

    protected Element createLeafElement(
        Element      parent,
        AttributeSet a,
        int          p0,
        int          p1)
    {
        return new LeafElement(parent, a, p0, p1);
    }

    //~ Inner Classes --------------------------------------------------------------------

    final class LeafElement
        extends PlainDocument.LeafElement
    {
        boolean comment;

        public LeafElement(
            Element      parent,
            AttributeSet a,
            int          offs0,
            int          offs1)
        {
            super(parent, a, offs0, offs1);
        }
    }
}
