/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.swing;

import oracle.ide.panels.DefaultTraversablePanel;
import oracle.ide.panels.TraversableContext;


/**
 * UI for the Jalopy Source Code Formatter settings. This panel is the main panel and
 * does not contain any settings details. It is implemented in order to supply an empty
 * page when the user clicks on the Jalopy settings node in the Tools->Settings menu.
 * All other settings are displayed in separate tables.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Frank Klomp</a>
 * @version $Revision$
 */
public class JDevDummyPanel
    extends DefaultTraversablePanel
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevDummyPanel()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param tc DOCUMENT ME!
     */
    public void onEntry(TraversableContext tc)
    {
        // init on creation of preference container
    }


    /**
     * DOCUMENT ME!
     *
     * @param tc DOCUMENT ME!
     */
    public void onExit(TraversableContext tc)
    {
    }
}
