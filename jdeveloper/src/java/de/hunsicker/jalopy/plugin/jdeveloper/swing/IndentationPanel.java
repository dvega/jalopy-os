/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.IndentationSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer indentation settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class IndentationPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new IndentationPanel object.
     */
    public IndentationPanel()
    {
        this.subPanel = new IndentationSettingsPage();
        this.add(this.subPanel);
    }
}
