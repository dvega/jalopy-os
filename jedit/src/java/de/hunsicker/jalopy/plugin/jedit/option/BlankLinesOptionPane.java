/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.BlankLinesSettingsPage;


/**
 * OptionPane to integrate the Jalopy blank lines options in JEdit's global options
 * dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class BlankLinesOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new BlankLinesOptionPane object.
     */
    public BlankLinesOptionPane()
    {
        super("jalopy.printer.separation" /* NOI18N */);
        this.panel = new BlankLinesSettingsPage();
    }
}
