/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.EnvironmentSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer environment settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class EnvironmentPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new EnvironmentPanel object.
     */
    public EnvironmentPanel()
    {
        this.subPanel = new EnvironmentSettingsPage();
        this.add(this.subPanel);
    }
}
