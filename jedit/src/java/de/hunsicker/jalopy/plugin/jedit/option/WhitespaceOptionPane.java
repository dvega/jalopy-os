/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.WhitespaceSettingsPage;


/**
 * OptionPane to integrate the Jalopy whitespace options in JEdit's global options
 * dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class WhitespaceOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new WhitespaceOptionPane object.
     */
    public WhitespaceOptionPane()
    {
        super("jalopy.printer.whitespace" /* NOI18N */);
        this.panel = new WhitespaceSettingsPage();
    }
}
