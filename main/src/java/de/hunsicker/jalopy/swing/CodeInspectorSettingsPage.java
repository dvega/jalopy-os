/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.swing.util.SwingHelper;


/**
 * Settings page for the Jalopy Code Inspector general settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class CodeInspectorSettingsPage
    extends AbstractSettingsPage
{
    //~ Instance variables ---------------------------------------------------------------

    private JCheckBox _enableCheckBox;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CodeInspectorSettingsPage object.
     */
    public CodeInspectorSettingsPage()
    {
        initialize();
    }


    /**
     * Creates a new CodeInspectorSettingsPage.
     *
     * @param container the parent container.
     */
    CodeInspectorSettingsPage(SettingsContainer container)
    {
        super(container);
        initialize();
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void updateSettings()
    {
        this.settings.putBoolean(
            ConventionKeys.INSPECTOR, _enableCheckBox.isSelected());
    }


    private void initialize()
    {
        JPanel miscPanel = new JPanel();
        GridLayout miscPanelLayout = new GridLayout(1, 1);
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_GENERAL" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        _enableCheckBox =
            new JCheckBox(
                this.bundle.getString("CHK_ENABLE_INSPECTOR" /* NOI18N */),
                this.settings.getBoolean(ConventionKeys.INSPECTOR, false));
        miscPanel.add(_enableCheckBox);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.bottom = 10;
        c.insets.top = 10;
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(miscPanel, c);
        add(miscPanel);
    }
}
