/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.ImportsSettingsPage;


/**
 * OptionPane to integrate the Jalopy imports options in JEdit's global options dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ImportsOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ImportsOptionPane object.
     */
    public ImportsOptionPane()
    {
        super("jalopy.printer.imports" /* NOI18N */);
        this.panel = new ImportsSettingsPage();
    }
}
