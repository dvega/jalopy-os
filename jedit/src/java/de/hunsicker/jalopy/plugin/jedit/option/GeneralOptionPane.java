/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.GeneralSettingsPage;


/**
 * OptionPane to integrate the Jalopy general options in JEdit's global options dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class GeneralOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new GeneralOptionPane object.
     */
    public GeneralOptionPane()
    {
        super("jalopy.general" /* NOI18N */);
        this.panel = new GeneralSettingsPage();
    }
}
