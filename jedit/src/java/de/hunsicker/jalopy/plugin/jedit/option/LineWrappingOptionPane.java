/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.WrappingSettingsPage;


/**
 * OptionPane to integrate the Jalopy line wrapping options in JEdit's global options
 * dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class LineWrappingOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new LineWrappingOptionPane object.
     */
    public LineWrappingOptionPane()
    {
        super("jalopy.printer.wrapping" /* NOI18N */);
        this.panel = new WrappingSettingsPage();
    }
}
