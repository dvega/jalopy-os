/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.MiscSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class JDevMiscPanel
    extends JDevPreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevMiscPanel()
    {
        this.subPanel = new MiscSettingsPage();
        this.add(this.subPanel);
    }
}
