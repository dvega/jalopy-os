/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.ProjectSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter project settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ProjectPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ProjectPanel object.
     */
    public ProjectPanel()
    {
        this.subPanel = new ProjectSettingsPage();
        this.add(this.subPanel);
    }
}
