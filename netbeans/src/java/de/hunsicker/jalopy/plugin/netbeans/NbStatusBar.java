/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.netbeans;

import de.hunsicker.jalopy.plugin.StatusBar;

import org.openide.TopManager;


/**
 * The NetBeans StatusBar implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbStatusBar
    implements StatusBar
{
    //~ Static variables/initializers ----------------------------------------------------

    static final NbStatusBar INSTANCE = new NbStatusBar();

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        TopManager.getDefault().setStatusText(text);
    }
}
