/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.BlankLinesSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer blank lines settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class BlankLinesPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new BlankLinesPanel object.
     */
    public BlankLinesPanel()
    {
        this.subPanel = new BlankLinesSettingsPage();
        this.add(this.subPanel);
    }
}
