/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is Marco Hunsicker. The Initial Developer of the Original
 * Code is Marco Hunsicker. All rights reserved.
 *
 * Copyright (c) 2002 Marco Hunsicker
 */
package de.hunsicker.jalopy.plugin.netbeans;

import java.awt.Frame;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.StatusBar;

import org.openide.TopManager;


/**
 * The Jalopy NetBeans Plug-in.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbPlugin
    extends AbstractPlugin
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The sole instance of this class. */
    static final NbPlugin INSTANCE = new NbPlugin();

    //~ Instance variables ---------------------------------------------------------------

    /** The current project. */
    NbProject project;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new NbPlugin object.
     */
    private NbPlugin()
    {
        super(new NbAppender());
        this.project = new NbProject();
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Project getActiveProject()
    {
        return this.project;
    }


    /**
     * {@inheritDoc}
     */
    public FileFormat getFileFormat()
    {
        /**
         * @todo implement
         */
        return FileFormat.AUTO;
    }


    /**
     * {@inheritDoc}
     */
    public Frame getMainWindow()
    {
        return TopManager.getDefault().getWindowManager().getMainWindow();
    }


    /**
     * {@inheritDoc}
     */
    public StatusBar getStatusBar()
    {
        return NbStatusBar.INSTANCE;
    }
}
