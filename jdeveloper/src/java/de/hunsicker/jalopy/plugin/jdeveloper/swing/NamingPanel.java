/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.NamingSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Code Inspector naming settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class NamingPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new NamingPanel object.
     */
    public NamingPanel()
    {
        this.subPanel = new NamingSettingsPage();
        this.add(this.subPanel);
    }
}
