/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import org.gjt.sp.jedit.AbstractOptionPane;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.SwingUtilities;

import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.swing.AbstractSettingsPage;
import de.hunsicker.jalopy.swing.ValidationException;
import de.hunsicker.swing.ErrorDialog;
import de.hunsicker.swing.util.SwingHelper;


/**
 * Abstract class to make embedding a Jalopy settings panel into the jEdit Global Options
 * dialog easy.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class JalopyOptionPane
    extends AbstractOptionPane
{
    //~ Instance variables ---------------------------------------------------------------

    /** The actual settings panel to embed. */
    protected AbstractSettingsPage panel;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JalopyOptionPane object.
     *
     * @param name the name of the option pane.
     */
    protected JalopyOptionPane(String name)
    {
        super(name);
        setPreferredSize(new Dimension(450, 450));
        setMaximumSize(new Dimension(500, 500));
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Performs initialization, sets up the option's pane contents.
     */
    protected void _init()
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(this.panel, c);
        add(this.panel);
        this.initialized = true;
    }


    /**
     * Saves the options.
     */
    protected void _save()
    {
        try
        {
            this.panel.validateSettings();
            this.panel.updateSettings();
        }
        catch (ValidationException ex)
        {
            return;
        }

        // save the code convention
        try
        {
            /**
             * @todo if the user presses OK/Apply in jEdit's Global Options dialog, this
             *       results in calling this method as often as settings panel were
             *       added to the option dialog which is wasteful Implement the flush()
             *       method so that the convention will only be saved if indeed
             *       something changed
             */
            Convention.getInstance().flush();
        }
        catch (Throwable ex)
        {
            Window owner = SwingUtilities.windowForComponent(this);
            ErrorDialog dialog = ErrorDialog.create(owner, ex);
            dialog.setVisible(true);
            dialog.dispose();
        }
    }
}
