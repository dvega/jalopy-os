/*
 * Copyright (c) 2002, Marco Hunsicker. All rights reserved.
 *
 * The contents of this file are subject to the Common Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://jalopy.sf.net/license-cpl.html
 *
 * Copyright (c) 2001-2002 Marco Hunsicker
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
