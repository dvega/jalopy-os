/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.primetime.ide.Browser;

import de.hunsicker.jalopy.plugin.StatusBar;


/**
 * The JBuilder StatusBar implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbStatusBar
    implements StatusBar
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final StatusBar INSTANCE = new JbStatusBar();

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbStatusBar object.
     */
    private JbStatusBar()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the sole instance of this class.
     *
     * @return the sole instance of this class.
     */
    public static StatusBar getInstance()
    {
        return INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        Browser.getActiveBrowser().getStatusView().setText(text, false);
    }
}
