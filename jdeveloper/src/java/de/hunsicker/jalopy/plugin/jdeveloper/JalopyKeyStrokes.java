/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.util.HashSet;
import java.util.Set;

import oracle.ide.IdeAction;
import oracle.ide.keyboard.XMLKeyStrokeContext;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public final class JalopyKeyStrokes
    extends XMLKeyStrokeContext
{
    //~ Static variables/initializers ----------------------------------------------------

    /** DOCUMENT ME! */
    public static final String KEY_SETTINGS =
        "de.hunsicker.jalopy.plugin.jdeveloper.JalopyKeyStrokes" /* NOI18N */;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JalopyKeyStrokes object.
     */
    public JalopyKeyStrokes()
    {
        super(KEY_SETTINGS);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param global DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getAllActions(boolean global)
    {
        if (global)
        {
            Set actions = new HashSet(5);

            actions.add(IdeAction.find(JDevPlugin.FORMAT_CMD_ID));

            return actions;
        }

        return null;
    }
}
