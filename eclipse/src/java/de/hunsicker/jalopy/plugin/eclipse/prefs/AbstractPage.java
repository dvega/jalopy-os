/*
 * Copyright (c) 2002, Marco Hunsicker. All rights reserved.
 *
 * The contents of this file are subject to the Common Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.eclipse.org/
 *
 * Copyright (c) 2001-2002 Marco Hunsicker
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
