/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing.syntax;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.Highlighter;
import javax.swing.JTextArea;


/**
 * A text area for displaying Java source files. Provides Syntax-highlighting.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b9
 */
public final class SyntaxTextArea
    extends JTextArea
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new SyntaxTextArea object.
     */
    public SyntaxTextArea()
    {
        super(new SyntaxDocument());
        setSelectionColor(new Color(83, 109, 165));
        setSelectedTextColor(Color.white);
        setFont(new Font("Monospaced" /* NOI18N */, Font.PLAIN, 12));
        setUI(new SyntaxTextAreaUI());
    }
}
