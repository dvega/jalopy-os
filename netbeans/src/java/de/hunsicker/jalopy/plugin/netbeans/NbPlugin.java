/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
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
