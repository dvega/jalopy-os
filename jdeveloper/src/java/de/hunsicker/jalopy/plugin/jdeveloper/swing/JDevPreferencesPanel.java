/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import java.awt.BorderLayout;

import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.swing.AbstractSettingsPage;
import de.hunsicker.jalopy.swing.ValidationException;
import de.hunsicker.swing.ErrorDialog;

import oracle.ide.Ide;
import oracle.ide.panels.DefaultTraversablePanel;
import oracle.ide.panels.TraversableContext;


/**
 * UI for the Jalopy Source Code Formatter settings dialog. This page is a basic page for
 * every sub-settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public abstract class JDevPreferencesPanel
    extends DefaultTraversablePanel
{
    //~ Instance variables ---------------------------------------------------------------

    AbstractSettingsPage subPanel;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevPreferencesPanel()
    {
        setLayout(new BorderLayout());
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param tc DOCUMENT ME!
     */
    public void onEntry(TraversableContext tc)
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param tc DOCUMENT ME!
     */
    public void onExit(TraversableContext tc)
    {
        if (this.subPanel != null)
        {
            try
            {
                this.subPanel.validateSettings();
            }
            catch (ValidationException ex)
            {
                return;
            }

            this.subPanel.updateSettings();
        }

        try
        {
            Convention.getInstance().flush();
        }
        catch (Throwable ex)
        {
            ErrorDialog dialog = ErrorDialog.create(Ide.getMainWindow(), ex);
            dialog.setVisible(true);
            dialog.dispose();
        }
    }
}
