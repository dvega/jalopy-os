/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.BracesSettingsPage;


/**
 * OptionPane to integrate the Jalopy braces options in JEdit's global options dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class BracesOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new BracesOptionPane object.
     */
    public BracesOptionPane()
    {
        super("jalopy.printer.braces" /* NOI18N */);
        this.panel = new BracesSettingsPage();
    }
}
