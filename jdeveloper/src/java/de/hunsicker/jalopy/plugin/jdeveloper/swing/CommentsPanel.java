/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.CommentsSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer comments settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class CommentsPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CommentsPanel object.
     */
    public CommentsPanel()
    {
        this.subPanel = new CommentsSettingsPage();
        this.add(this.subPanel);
    }
}
