/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is SATC. The Initial Developer of the Original Code is
 * Bogdan Mitu.
 *
 * Copyright(C) 2001-2002 Bogdan Mitu.
 */
package de.hunsicker.jalopy.swing.syntax;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;


/**
 * DOCUMENT ME!
 *
 * @author Bogdan Mitu
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @since 1.0b9
 */
final class SyntaxTextAreaUI
    extends BasicTextAreaUI
{
    //~ Instance variables ---------------------------------------------------------------

    private SyntaxEditorKit _editorKit;

    //~ Constructors ---------------------------------------------------------------------

    public SyntaxTextAreaUI()
    {
        _editorKit = new SyntaxEditorKit();
    }

    //~ Methods --------------------------------------------------------------------------

    public EditorKit getEditorKit(JTextComponent tc)
    {
        return _editorKit;
    }


    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View create(Element elem)
    {
        return _editorKit.createView(elem);
    }


    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void installUI(JComponent c)
    {
        super.installUI(c);

        JTextComponent text = (JTextComponent) c;

        if (
            (text.getDocument() == null)
            || !(text.getDocument() instanceof SyntaxDocument))
        {
            text.setDocument(_editorKit.createDefaultDocument());
        }
        else
        {
            _editorKit.installEditorKit(text.getDocument());
        }
    }
}
