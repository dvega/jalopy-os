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

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;

import de.hunsicker.antlr.CharScanner;
import de.hunsicker.jalopy.language.JavaLexer;


/**
 * DOCUMENT ME!
 *
 * @author Bogdan Mitu
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @since 1.0b9
 */
final class SyntaxEditorKit
    extends DefaultEditorKit
{
    //~ Constructors ---------------------------------------------------------------------

    public SyntaxEditorKit()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    public Document createDefaultDocument()
    {
        Document doc = new SyntaxDocument();
        installEditorKit(doc);

        return doc;
    }


    /**
     * DOCUMENT ME!
     *
     * @param elem DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public View createView(Element elem)
    {
        SyntaxView view = new SyntaxView(elem);

        return view;
    }


    /**
     * Installs this editor kit into a document.
     *
     * @param doc DOCUMENT ME!
     */
    public void installEditorKit(Document doc)
    {
        SyntaxStream stream = new SyntaxStream(doc);
        CharScanner lexer = new JavaLexer(stream);

        doc.putProperty("stream" /* NOI18N */, stream);
        doc.putProperty("lexer" /* NOI18N */, lexer);
    }
}
