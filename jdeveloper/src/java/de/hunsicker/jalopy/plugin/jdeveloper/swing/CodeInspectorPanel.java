/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import de.hunsicker.jalopy.swing.CodeInspectorSettingsPage;


/**
 * UI for the Jalopy Source Code Formatter Code Inspector general settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class CodeInspectorPanel
    extends PreferencesPanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CodeInspectorPanel object.
     */
    public CodeInspectorPanel()
    {
        this.subPanel = new CodeInspectorSettingsPage();
        this.add(this.subPanel);
    }
}
