/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.netbeans;

import javax.swing.Action;

import de.hunsicker.jalopy.swing.SettingsDialog;

import org.openide.TopManager;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;


/**
 * Action to display the Jalopy settings dialog.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class SettingsAction
    extends CallableSystemAction
{
    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;

        // If you will provide context help then use:
        // return new HelpCtx (Callable_actionAction.class);
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName()
    {
        return NbBundle.getMessage(
            SettingsAction.class, "LBL_SettingsAction" /* NOI18N */);
    }


    /**
     * DOCUMENT ME!
     */
    public void performAction()
    {
        SettingsDialog dialog =
            SettingsDialog.create(
                TopManager.getDefault().getWindowManager().getMainWindow());
        dialog.pack();
        dialog.setLocationRelativeTo(
            TopManager.getDefault().getWindowManager().getMainWindow());
        dialog.setVisible(true);
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String iconResource()
    {
        return NbBundle.getMessage(SettingsAction.class, "ICON_Settings" /* NOI18N */);
    }


    /**
     * DOCUMENT ME!
     */
    protected void initialize()
    {
        super.initialize();
        putProperty(
            Action.SHORT_DESCRIPTION,
            NbBundle.getMessage(SettingsAction.class, "HINT_SettingsAction" /* NOI18N */));
    }
}
