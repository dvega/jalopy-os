/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import de.hunsicker.jalopy.swing.CodeInspectorSettingsPage;


/**
 * OptionPane to integrate the Jalopy Code Inspector settings in JEdit's global options
 * dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class CodeInspectorOptionPane
    extends JalopyOptionPane
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CodeInspectorOptionPane object.
     */
    public CodeInspectorOptionPane()
    {
        super("jalopy.inspector.general" /* NOI18N */);
        this.panel = new CodeInspectorSettingsPage();
    }
}
