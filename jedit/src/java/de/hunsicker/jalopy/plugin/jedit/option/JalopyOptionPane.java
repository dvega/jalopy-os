/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Neither the name of the Jalopy project nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
 * COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * $Id$
 */
package de.hunsicker.jalopy.plugin.jedit.option;

import org.gjt.sp.jedit.AbstractOptionPane;

import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.ui.AbstractSettingsPanel;
import de.hunsicker.jalopy.ui.ValidationException;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;


/**
 * Abstract class to make embedding a Jalopy settings panel into the jEdit
 * Global Options dialog easy.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
abstract class JalopyOptionPane
    extends AbstractOptionPane
{
    //~ Instance variables ····················································

    /** The actual settings panel to embed. */
    protected AbstractSettingsPanel panel;

    //~ Constructors ··························································

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

    //~ Methods ·······························································

    /**
     * Performs initialization, sets up the option's pane contents.
     */
    protected void _init()
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
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
            this.panel.store();
        }
        catch (ValidationException ex)
        {
            return;
        }

        // save the code convention
        try
        {
            /**
             * @todo if the user presses OK/Apply in jEdit's Global Options
             *       dialog, this results in calling this method as often as
             *       settings panel were added to the option dialog which is
             *       wasteful Implement the flush() method so that the
             *       convention will only be saved if indeed something
             *       changed
             */
            Convention.getInstance().flush();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
