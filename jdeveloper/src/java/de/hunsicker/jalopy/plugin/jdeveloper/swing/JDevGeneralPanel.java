/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.GeneralSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter General settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class JDevGeneralPanel
    extends JDevPreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevGeneralPanel()
    {
        this.subPanel = new GeneralSettingsPage();
        this.add(this.subPanel);
    }
}
