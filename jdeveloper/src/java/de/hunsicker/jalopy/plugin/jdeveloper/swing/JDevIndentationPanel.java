/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.IndentationSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Indentation settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class JDevIndentationPanel
    extends JDevPreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevIndentationPanel()
    {
        this.subPanel = new IndentationSettingsPage();
        this.add(this.subPanel);
    }
}
