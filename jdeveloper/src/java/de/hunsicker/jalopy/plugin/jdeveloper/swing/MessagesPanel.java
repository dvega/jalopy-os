/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.MessagesSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter messages settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class MessagesPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new MessagesPanel object.
     */
    public MessagesPanel()
    {
        this.subPanel = new MessagesSettingsPage();
        this.add(this.subPanel);
    }
}
