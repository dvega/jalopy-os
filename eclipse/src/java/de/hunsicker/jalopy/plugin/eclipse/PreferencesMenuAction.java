/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.eclipse;

import de.hunsicker.jalopy.swing.SettingsDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * Action to display the Jalopy preferences dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class PreferencesMenuAction
    implements IWorkbenchWindowActionDelegate
{
    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void dispose()
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param window DOCUMENT ME!
     */
    public void init(IWorkbenchWindow window)
    {
    }


    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void run(IAction action)
    {
        String[] argv = { SettingsDialog.ARG_ECLIPSE };
        SettingsDialog.main(argv);
    }


    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     * @param selection DOCUMENT ME!
     */
    public void selectionChanged(
        IAction    action,
        ISelection selection)
    {
    }
}
