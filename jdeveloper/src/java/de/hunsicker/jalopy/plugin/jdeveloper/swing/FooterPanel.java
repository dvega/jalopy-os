/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.FooterSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer footer settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class FooterPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new FooterPanel object.
     */
    public FooterPanel()
    {
        this.subPanel = new FooterSettingsPage();
        this.add(this.subPanel);
    }
}
