/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.eclipse.prefs;

import de.hunsicker.jalopy.storage.Convention;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * The page for setting code formatter options
 */
public abstract class AbstractPage
    extends PreferencePage
    implements IWorkbenchPreferencePage
{
    //~ Static variables/initializers ----------------------------------------------------

    /** DOCUMENT ME! */
    protected static final Convention settings = Convention.getInstance();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new AbstractPage object.
     */
    public AbstractPage()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param workbench DOCUMENT ME!
     */
    public void init(IWorkbench workbench)
    {
    }
}
