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
package de.hunsicker.jalopy.ui;

import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * A component that can be used to display/edit the Jalopy comments
 * settings
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class CommentsPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _formatMultiLineCheckBox;
    private JCheckBox _removeJavadocCheckBox;
    private JCheckBox _removeMultiCheckBox;
    private JCheckBox _removeSingleCheckBox;

    //~ Constructors ··························································

    /**
     * Creates a new CommentsPanel object.
     */
    public CommentsPanel()
    {
        initialize();
    }


    /**
     * Creates a new CommentsPanel.
     *
     * @param container the parent container.
     */
    CommentsPanel(SettingsContainer container)
    {
        super(container);
        initialize();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.putBoolean(Keys.COMMENT_REMOVE_SINGLE_LINE,
                              _removeSingleCheckBox.isSelected());
        this.settings.putBoolean(Keys.COMMENT_REMOVE_MULTI_LINE,
                              _removeMultiCheckBox.isSelected());
        this.settings.putBoolean(Keys.COMMENT_JAVADOC_REMOVE,
                              _removeJavadocCheckBox.isSelected());
        this.settings.putBoolean(Keys.COMMENT_FORMAT_MULTI_LINE,
                              _formatMultiLineCheckBox.isSelected());
    }


    private void initialize()
    {
        JPanel removePanel = new JPanel();
        GridBagLayout removeLayout = new GridBagLayout();
        removePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Remove"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 0)));
        removePanel.setLayout(removeLayout);

        GridBagConstraints c = new GridBagConstraints();
        _removeSingleCheckBox = new JCheckBox("Single Line comments",
                                              this.settings.getBoolean(
                                                                    Keys.COMMENT_REMOVE_SINGLE_LINE,
                                                                    Defaults.COMMENT_REMOVE_SINGLE_LINE));
        _removeSingleCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_removeSingleCheckBox, c);
        removePanel.add(_removeSingleCheckBox);
        _removeMultiCheckBox = new JCheckBox("Multi Line comments",
                                             this.settings.getBoolean(
                                                                   Keys.COMMENT_REMOVE_MULTI_LINE,
                                                                   Defaults.COMMENT_REMOVE_MULTI_LINE));
        _removeMultiCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_removeMultiCheckBox, c);
        removePanel.add(_removeMultiCheckBox);
        _removeJavadocCheckBox = new JCheckBox("Javadoc comments",
                                               this.settings.getBoolean(
                                                                     Keys.COMMENT_JAVADOC_REMOVE,
                                                                     Defaults.COMMENT_JAVADOC_REMOVE));
        _removeJavadocCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_removeJavadocCheckBox, c);
        removePanel.add(_removeJavadocCheckBox);

        JPanel formatPanel = new JPanel();
        GridBagLayout formatPanelLayout = new GridBagLayout();
        formatPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Format"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 0)));
        formatPanel.setLayout(formatPanelLayout);
        _formatMultiLineCheckBox = new JCheckBox("Multi Line comments",
                                                 this.settings.getBoolean(
                                                                       Keys.COMMENT_FORMAT_MULTI_LINE,
                                                                       Defaults.COMMENT_FORMAT_MULTI_LINE));
        _formatMultiLineCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        formatPanelLayout.setConstraints(_formatMultiLineCheckBox, c);
        formatPanel.add(_formatMultiLineCheckBox);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        c.insets.top = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(removePanel, c);
        add(removePanel);
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(formatPanel, c);
        add(formatPanel);
    }
}
