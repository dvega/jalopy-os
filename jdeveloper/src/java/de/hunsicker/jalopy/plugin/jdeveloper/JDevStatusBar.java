/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import de.hunsicker.jalopy.plugin.StatusBar;

import oracle.ide.Ide;


/**
 * The JDeveloper StatusBar implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JDevStatusBar
    implements StatusBar
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbStatusBar object.
     */
    public JDevStatusBar()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        Ide.getStatusBar().setText(text);
    }
}
