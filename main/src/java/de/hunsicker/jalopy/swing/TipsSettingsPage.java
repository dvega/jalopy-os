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
 * Settings page for the Jalopy Code Inspector tips settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class TipsSettingsPage
    extends AbstractSettingsPage
{
    //~ Instance variables ---------------------------------------------------------------

    private JCheckBox _tip15CheckBox;
    private JCheckBox _tipAvoidThreadGroupsCheckBox;
    private JCheckBox _tipDeclareListCommentCheckBox;
    private JCheckBox _tipDontIgnoreExceptionCheckBox;
    private JCheckBox _tipDontSubstituteCheckBox;
    private JCheckBox _tipEmptyFinallyCheckBox;
    private JCheckBox _tipInterfaceTypeCheckBox;
    private JCheckBox _tipNeverExceptionCheckBox;
    private JCheckBox _tipNeverThrowableCheckBox;
    private JCheckBox _tipObeyEqualsCheckBox;
    private JCheckBox _tipOverrideEqualsCheckBox;
    private JCheckBox _tipOverrideHashCodeCheckBox;
    private JCheckBox _tipOverrideStringCheckBox;
    private JCheckBox _tipReferByInterfaceCheckBox;
    private JCheckBox _tipReplaceStructCheckBox;
    private JCheckBox _tipStringLiteralI18nCheckBox;
    private JCheckBox _tipWaitOutsideLoopCheckBox;
    private JCheckBox _tipWrongListCommentCheckBox;
    private JCheckBox _tipZeroArrayCheckBox;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new TipsSettingsPage object.
     */
    public TipsSettingsPage()
    {
        initialize();
    }


    /**
     * Creates a new TipsSettingsPage.
     *
     * @param container the parent container.
     */
    TipsSettingsPage(SettingsContainer container)
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
            ConventionKeys.TIP_STRING_LITERAL_I18N,
            _tipStringLiteralI18nCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_NEVER_THROW_EXCEPTION,
            _tipNeverExceptionCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_NEVER_THROW_THROWABLE,
            _tipNeverThrowableCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_DONT_IGNORE_EXCEPTION,
            _tipDontIgnoreExceptionCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_NEVER_WAIT_OUTSIDE_LOOP,
            _tipWaitOutsideLoopCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_AVOID_THREAD_GROUPS,
            _tipAvoidThreadGroupsCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_DECLARE_COLLECTION_COMMENT,
            _tipDeclareListCommentCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_WRONG_COLLECTION_COMMENT,
            _tipWrongListCommentCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_EMPTY_FINALLY, _tipEmptyFinallyCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_DONT_SUBSTITUTE_OBJECT_EQUALS,
            _tipDontSubstituteCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_OBEY_CONTRACT_EQUALS, _tipObeyEqualsCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_OVERRIDE_HASHCODE,
            _tipOverrideHashCodeCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_OVERRIDE_EQUALS, _tipOverrideEqualsCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_OVERRIDE_TO_STRING, _tipOverrideStringCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_INTERFACE_ONLY_FOR_TYPE,
            _tipInterfaceTypeCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_REPLACE_STRUCTURE_WITH_CLASS,
            _tipReplaceStructCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_RETURN_ZERO_ARRAY, _tipZeroArrayCheckBox.isSelected());
        this.settings.putBoolean(
            ConventionKeys.TIP_REFER_BY_INTERFACE,
            _tipReferByInterfaceCheckBox.isSelected());
    }


    private void initialize()
    {
        JPanel miscPanel = new JPanel();
        GridLayout miscPanelLayout = new GridLayout(3, 10);
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_TIPS" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        _tipDontSubstituteCheckBox =
            new JCheckBox(
                "1" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_DONT_SUBSTITUTE_OBJECT_EQUALS, false));
        _tipDontSubstituteCheckBox.setToolTipText(
            this.bundle.getString("TIP_DONT_SUBSTITUTE_OBJECT_EQUALS" /* NOI18N */));
        miscPanel.add(_tipDontSubstituteCheckBox);

        _tipObeyEqualsCheckBox =
            new JCheckBox(
                "2" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_OBEY_CONTRACT_EQUALS, false));
        _tipObeyEqualsCheckBox.setToolTipText(
            this.bundle.getString("TIP_OBEY_CONTRACT_EQUALS" /* NOI18N */));
        miscPanel.add(_tipObeyEqualsCheckBox);

        _tipOverrideHashCodeCheckBox =
            new JCheckBox(
                "3" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_OVERRIDE_HASHCODE, false));
        _tipOverrideHashCodeCheckBox.setToolTipText(
            this.bundle.getString("TIP_OVERRIDE_HASHCODE" /* NOI18N */));
        miscPanel.add(_tipOverrideHashCodeCheckBox);

        _tipOverrideEqualsCheckBox =
            new JCheckBox(
                "4" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_OVERRIDE_EQUALS, false));
        _tipOverrideEqualsCheckBox.setToolTipText(
            this.bundle.getString("TIP_OVERRIDE_EQUALS" /* NOI18N */));
        miscPanel.add(_tipOverrideEqualsCheckBox);

        _tipOverrideStringCheckBox =
            new JCheckBox(
                "5" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_OVERRIDE_TO_STRING, false));
        _tipOverrideStringCheckBox.setToolTipText(
            this.bundle.getString("TIP_OVERRIDE_TO_STRING" /* NOI18N */));
        miscPanel.add(_tipOverrideStringCheckBox);

        _tipInterfaceTypeCheckBox =
            new JCheckBox(
                "6" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_INTERFACE_ONLY_FOR_TYPE, false));
        _tipInterfaceTypeCheckBox.setToolTipText(
            this.bundle.getString("TIP_INTERFACE_ONLY_FOR_TYPE" /* NOI18N */));
        miscPanel.add(_tipInterfaceTypeCheckBox);

        _tipReplaceStructCheckBox =
            new JCheckBox(
                "7" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_REPLACE_STRUCTURE_WITH_CLASS, false));
        _tipReplaceStructCheckBox.setToolTipText(
            this.bundle.getString("TIP_REPLACE_STRUCTURE_WITH_CLASS" /* NOI18N */));
        miscPanel.add(_tipReplaceStructCheckBox);

        _tipZeroArrayCheckBox =
            new JCheckBox(
                "8" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_RETURN_ZERO_ARRAY, false));
        _tipZeroArrayCheckBox.setToolTipText(
            this.bundle.getString("TIP_RETURN_ZERO_ARRAY" /* NOI18N */));
        miscPanel.add(_tipZeroArrayCheckBox);

        _tipReferByInterfaceCheckBox =
            new JCheckBox(
                "9" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_REFER_BY_INTERFACE, false));
        _tipReferByInterfaceCheckBox.setToolTipText(
            this.bundle.getString("TIP_REFER_BY_INTERFACE" /* NOI18N */));
        miscPanel.add(_tipReferByInterfaceCheckBox);

        _tipNeverExceptionCheckBox =
            new JCheckBox(
                "10" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_NEVER_THROW_EXCEPTION, false));
        _tipNeverExceptionCheckBox.setToolTipText(
            this.bundle.getString("TIP_NEVER_THROW_EXCEPTION" /* NOI18N */));
        miscPanel.add(_tipNeverExceptionCheckBox);

        _tipNeverThrowableCheckBox =
            new JCheckBox(
                "11" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_NEVER_THROW_THROWABLE, false));
        _tipNeverThrowableCheckBox.setToolTipText(
            this.bundle.getString("TIP_NEVER_THROW_THROWABLE" /* NOI18N */));
        miscPanel.add(_tipNeverThrowableCheckBox);

        _tipDontIgnoreExceptionCheckBox =
            new JCheckBox(
                "12" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_DONT_IGNORE_EXCEPTION, false));
        _tipDontIgnoreExceptionCheckBox.setToolTipText(
            this.bundle.getString("TIP_DONT_IGNORE_EXCEPTION" /* NOI18N */));
        miscPanel.add(_tipDontIgnoreExceptionCheckBox);

        _tipWaitOutsideLoopCheckBox =
            new JCheckBox(
                "13" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_NEVER_WAIT_OUTSIDE_LOOP, false));
        _tipWaitOutsideLoopCheckBox.setToolTipText(
            this.bundle.getString("TIP_NEVER_WAIT_OUTSIDE_LOOP" /* NOI18N */));
        miscPanel.add(_tipWaitOutsideLoopCheckBox);

        _tipAvoidThreadGroupsCheckBox =
            new JCheckBox(
                "14" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_AVOID_THREAD_GROUPS, false));

        _tipAvoidThreadGroupsCheckBox.setToolTipText(
            this.bundle.getString("TIP_AVOID_THREAD_GROUPS" /* NOI18N */));
        miscPanel.add(_tipAvoidThreadGroupsCheckBox);

        _tipDeclareListCommentCheckBox =
            new JCheckBox(
                "15" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_DECLARE_COLLECTION_COMMENT, false));
        _tipDeclareListCommentCheckBox.setToolTipText(
            this.bundle.getString("TIP_DECLARE_COLLECTION_COMMENT" /* NOI18N */));
        miscPanel.add(_tipDeclareListCommentCheckBox);

        _tipWrongListCommentCheckBox =
            new JCheckBox(
                "16" /* NOI18N */,
                this.settings.getBoolean(
                    ConventionKeys.TIP_WRONG_COLLECTION_COMMENT, false));
        _tipWrongListCommentCheckBox.setToolTipText(
            this.bundle.getString("TIP_WRONG_COLLECTION_COMMENT" /* NOI18N */));
        miscPanel.add(_tipWrongListCommentCheckBox);

        _tipEmptyFinallyCheckBox =
            new JCheckBox(
                "17" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_EMPTY_FINALLY, false));
        _tipEmptyFinallyCheckBox.setToolTipText(
            this.bundle.getString("TIP_EMPTY_FINALLY" /* NOI18N */));
        miscPanel.add(_tipEmptyFinallyCheckBox);

        _tip15CheckBox = new JCheckBox("18" /* NOI18N */, false);
        _tip15CheckBox.setToolTipText(
            this.bundle.getString("TIP_AVOID_VARIABLE_SHADOWING" /* NOI18N */));
        miscPanel.add(_tip15CheckBox);

        _tipStringLiteralI18nCheckBox =
            new JCheckBox(
                "19" /* NOI18N */,
                this.settings.getBoolean(ConventionKeys.TIP_STRING_LITERAL_I18N, false));
        _tipStringLiteralI18nCheckBox.setToolTipText(
            this.bundle.getString("TIP_STRING_LITERAL_I18N" /* NOI18N */));
        miscPanel.add(_tipStringLiteralI18nCheckBox);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.bottom = 10;
        c.insets.top = 10;
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(miscPanel, c);
        add(miscPanel);
    }
}
