/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.FooterSettingsPage;


/**
 * OptionPane to integrate the Jalopy footer options in JEdit's global options dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class FooterOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new FooterOptionPane object.
     */
    public FooterOptionPane()
    {
        super("jalopy.printer.footer" /* NOI18N */);
        this.panel = new FooterSettingsPage();
    }
}
