/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.SeparationSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Printer separation settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class SeparationPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new SeparationPanel object.
     */
    public SeparationPanel()
    {
        this.subPanel = new SeparationSettingsPage();
        this.add(this.subPanel);
    }
}
