/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.WhitespaceSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer white space settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class WhitespacePanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new WhitespacePanel object.
     */
    public WhitespacePanel()
    {
        this.subPanel = new WhitespaceSettingsPage();
        this.add(this.subPanel);
    }
}
