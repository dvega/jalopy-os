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

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component that can be used to display/edit the Jalopy indentation
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class IndentationPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _alignAssignmentsCheckBox;
    private JCheckBox _alignMethodCallChainsCheckBox;
    private JCheckBox _alignMethodDefParamsCheckBox;
    private JCheckBox _alignVariablesCheckBox;
    private JCheckBox _alignTernaryOperatorCheckBox;
    private JCheckBox _indentCaseSwitchCheckBox;
    private JCheckBox _indentContinuationCheckBox;
    private JCheckBox _indentContinuationOperatorCheckBox;
    //private JCheckBox _indentContinuationTernaryCheckBox;
    private JCheckBox _indentExtendsCheckBox;
    private JCheckBox _indentFirstColumnCheckBox;
    private JCheckBox _indentImplementsCheckBox;
    private JCheckBox _indentLabelsCheckBox;
    private JCheckBox _indentMethodCallCheckBox;
    private JCheckBox _indentParametersCheckBox;
    private JCheckBox _indentThrowsCheckBox;
    private JCheckBox _indentUsingLeadingTabsCheckBox;
    private JCheckBox _indentUsingTabsCheckBox;
    private JCheckBox _standardIndentCheckBox;
    private JComboBox _continuationIndentComboBox;
    private JComboBox _endlineIndentComboBox;
    private JComboBox _indentComboBox;
    private JComboBox _indentExtendsComboBox;
    private JComboBox _indentImplementsComboBox;
    private JComboBox _indentParametersComboBox;
    private JComboBox _indentThrowsComboBox;
    private JComboBox _leadingIndentComboBox;
    private JComboBox _tabSizeComboBox;
    private JTabbedPane _tabbedPane;

    //~ Constructors ··························································

    /**
     * Creates a new IndentationPanel object.
     */
    public IndentationPanel()
    {
        initialize();
    }


    /**
     * Creates a new IndentationPanel.
     *
     * @param container the parent container.
     */
    IndentationPanel(SettingsContainer container)
    {
        super(container);
        initialize();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public String getPreviewFileName()
    {
        switch (_tabbedPane.getSelectedIndex())
        {
            case 1 :
                return "indentationmisc";

            default :
                return super.getPreviewFileName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.put(Keys.INDENT_SIZE,
                       (String)_indentComboBox.getSelectedItem());
        this.settings.put(Keys.INDENT_SIZE_LEADING,
                       (String)_leadingIndentComboBox.getSelectedItem());
        this.settings.put(Keys.INDENT_SIZE_CONTINUATION,
                       (String)_continuationIndentComboBox.getSelectedItem());
        this.settings.put(Keys.INDENT_SIZE_COMMENT_ENDLINE,
                       (String)_endlineIndentComboBox.getSelectedItem());
        this.settings.putBoolean(Keys.INDENT_CASE_FROM_SWITCH,
                              _indentCaseSwitchCheckBox.isSelected());
        this.settings.putBoolean(Keys.INDENT_LABEL,
                              _indentLabelsCheckBox.isSelected());
        this.settings.putBoolean(Keys.INDENT_CONTINUATION_IF,
                              _indentContinuationCheckBox.isSelected());
        this.settings.putBoolean(Keys.INDENT_FIRST_COLUMN_COMMENT,
                              _indentFirstColumnCheckBox.isSelected());
        /*this.settings.putBoolean(Keys.INDENT_CONTINUATION_IF_TERNARY,
                              _indentContinuationTernaryCheckBox.isSelected());*/
        this.settings.put(Keys.INDENT_SIZE_TABS,
                       (String)_tabSizeComboBox.getSelectedItem());
        this.settings.putBoolean(Keys.INDENT_WITH_TABS,
                              _indentUsingTabsCheckBox.isSelected());
        this.settings.putBoolean(Keys.INDENT_WITH_TABS_ONLY_LEADING,
                              _indentUsingLeadingTabsCheckBox.isSelected());
        this.settings.putBoolean(Keys.ALIGN_VAR_IDENTS,
                              _alignVariablesCheckBox.isSelected());
        this.settings.putBoolean(Keys.ALIGN_VAR_ASSIGNS,
                              _alignAssignmentsCheckBox.isSelected());
        this.settings.putBoolean(Keys.ALIGN_PARAMS_METHOD_DEF,
                              _alignMethodDefParamsCheckBox.isSelected());
        this.settings.putBoolean(Keys.ALIGN_TERNARY_OPERATOR,
                              _alignTernaryOperatorCheckBox.isSelected());

        /*this.settings.putBoolean(Keys.INDENT_USE_PARAMS_METHOD_CALL,
                              _indentMethodCallCheckBox.isSelected());*/
        this.settings.putBoolean(Keys.INDENT_CONTINUATION_OPERATOR,
                              _indentContinuationOperatorCheckBox.isSelected());
        this.settings.putBoolean(Keys.INDENT_DEEP,
                              !_standardIndentCheckBox.isSelected());
        this.settings.putBoolean(Keys.ALIGN_METHOD_CALL_CHAINS,
                              _alignMethodCallChainsCheckBox.isSelected());

        if (_indentExtendsCheckBox.isSelected())
        {
            this.settings.put(Keys.INDENT_SIZE_EXTENDS,
                           (String)_indentExtendsComboBox.getSelectedItem());
        }
        else
        {
            this.settings.put(Keys.INDENT_SIZE_EXTENDS, "-1");
        }

        if (_indentImplementsCheckBox.isSelected())
        {
            this.settings.put(Keys.INDENT_SIZE_IMPLEMENTS,
                           (String)_indentImplementsComboBox.getSelectedItem());
        }
        else
        {
            this.settings.put(Keys.INDENT_SIZE_IMPLEMENTS, "-1");
        }

        if (_indentThrowsCheckBox.isSelected())
        {
            this.settings.put(Keys.INDENT_SIZE_THROWS,
                           (String)_indentThrowsComboBox.getSelectedItem());
        }
        else
        {
            this.settings.put(Keys.INDENT_SIZE_THROWS, "-1");
        }

        /*if (_indentParametersCheckBox.isSelected())
        {
            this.settings.put(Keys.INDENT_SIZE_PARAMETERS,
                           (String)_indentParametersComboBox.getSelectedItem());
        }
        else
        {
            this.settings.put(Keys.INDENT_SIZE_PARAMETERS, "-1");
        }*/
    }


    private JPanel createGeneralPane()
    {
        GridBagConstraints c = new GridBagConstraints();

        JPanel policyPanel = new JPanel();
        policyPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Indentation policy"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout policyPanelLayout = new GridBagLayout();
        policyPanel.setLayout(policyPanelLayout);

        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _standardIndentCheckBox = new JCheckBox("Standard indent",
                                                !this.settings.getBoolean(
                                                                       Keys.INDENT_DEEP,
                                                                       Defaults.INDENT_DEEP));
        _standardIndentCheckBox.addActionListener(this.trigger);
        policyPanelLayout.setConstraints(_standardIndentCheckBox, c);
        policyPanel.add(_standardIndentCheckBox);

        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);

        JCheckBox deepIndentCheckBox = new JCheckBox("Deep indent",
                                                     this.settings.getBoolean(
                                                                           Keys.INDENT_DEEP,
                                                                           Defaults.INDENT_DEEP));
        deepIndentCheckBox.addActionListener(this.trigger);
        policyPanelLayout.setConstraints(deepIndentCheckBox, c);
        policyPanel.add(deepIndentCheckBox);

        ButtonGroup policyGroup = new ButtonGroup();
        policyGroup.add(_standardIndentCheckBox);
        policyGroup.add(deepIndentCheckBox);

        JPanel indentPanel = new JPanel();
        indentPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Sizes"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout indentPanelLayout = new GridBagLayout();
        indentPanel.setLayout(indentPanelLayout);

        Object[] items ={ "2", "3", "4" };
        ComboBoxPanel indent = new NumberComboBoxPanel("General indent:", items,
                                                       this.settings.get(
                                                                      Keys.INDENT_SIZE,
                                                                      String.valueOf(Defaults.INDENT_SIZE)));
        _indentComboBox = indent.getComboBox();
        _indentComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(indent, c);
        indentPanel.add(indent);

        Object[] leadingItems ={ "0", "2", "4", "6", "8", "10" };
        ComboBoxPanel leadingIndent = new NumberComboBoxPanel("Leading indent:",
                                                              leadingItems,
                                                              this.settings.get(
                                                                             Keys.INDENT_SIZE_LEADING,
                                                                             String.valueOf(Defaults.INDENT_SIZE_LEADING)));
        _leadingIndentComboBox = leadingIndent.getComboBox();
        _leadingIndentComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(leadingIndent, c);
        indentPanel.add(leadingIndent);

        Object[] continuationItems ={ "2", "4", "6", "8", "10", "12" };
        ComboBoxPanel continuationIndent = new NumberComboBoxPanel("Continuation indent:",
                                                                   continuationItems,
                                                                   this.settings.get(
                                                                                  Keys.INDENT_SIZE_CONTINUATION,
                                                                                  String.valueOf(Defaults.INDENT_SIZE_CONTINUATION)));
        _continuationIndentComboBox = continuationIndent.getComboBox();
        _continuationIndentComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(continuationIndent, c);
        indentPanel.add(continuationIndent);

        Object[] endlineItems ={ "0", "1", "2", "3", "4" };
        ComboBoxPanel endlineIndent = new NumberComboBoxPanel("Endline comment indent:",
                                                              endlineItems,
                                                              this.settings.get(
                                                                             Keys.INDENT_SIZE_COMMENT_ENDLINE,
                                                                             String.valueOf(Defaults.INDENT_SIZE_COMMENT_ENDLINE)));
        _endlineIndentComboBox = endlineIndent.getComboBox();
        _endlineIndentComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(endlineIndent, c);
        indentPanel.add(endlineIndent);

        Object[] tabSizeItems ={ "2", "3", "4", "6", "8", "10" };
        ComboBoxPanel tabSize = new NumberComboBoxPanel("Original Tab indent:",
                                                        tabSizeItems,
                                                        this.settings.get(
                                                                       Keys.INDENT_SIZE_TABS,
                                                                       String.valueOf(Defaults.INDENT_SIZE_TABS)));
        _tabSizeComboBox = tabSize.getComboBox();
        _tabSizeComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(tabSize, c);
        indentPanel.add(tabSize);

        String[] identItems ={ "0", "2", "3", "4", "6", "8" };
        int indentExtends = this.settings.getInt(Keys.INDENT_SIZE_EXTENDS,
                                              Defaults.INDENT_SIZE_EXTENDS);
        NumberComboBoxPanelCheckBox indentExtendsComboCheckBox = new NumberComboBoxPanelCheckBox("Extends indent:",
                                                                                                 indentExtends > -1,
                                                                                                 "",
                                                                                                 identItems,
                                                                                                 (indentExtends > -1)
                                                                                                     ? String.valueOf(indentExtends)
                                                                                                     : "0");
        _indentExtendsCheckBox = indentExtendsComboCheckBox.getCheckBox();
        _indentExtendsCheckBox.addActionListener(this.trigger);
        _indentExtendsComboBox = indentExtendsComboCheckBox.getComboBoxPanel()
                                                           .getComboBox();
        _indentExtendsComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 5, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(indentExtendsComboCheckBox, c);
        indentPanel.add(indentExtendsComboCheckBox);

        int indentImplements = this.settings.getInt(Keys.INDENT_SIZE_IMPLEMENTS,
                                                 Defaults.INDENT_SIZE_IMPLEMENTS);
        NumberComboBoxPanelCheckBox indentImplementsComboCheckBox = new NumberComboBoxPanelCheckBox("Implements indent:",
                                                                                                    indentImplements > -1,
                                                                                                    "",
                                                                                                    identItems,
                                                                                                    (indentImplements > -1)
                                                                                                        ? String.valueOf(indentImplements)
                                                                                                        : "0");
        _indentImplementsCheckBox = indentImplementsComboCheckBox.getCheckBox();
        _indentImplementsCheckBox.addActionListener(this.trigger);
        _indentImplementsComboBox = indentImplementsComboCheckBox.getComboBoxPanel()
                                                                 .getComboBox();
        _indentImplementsComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 6, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(indentImplementsComboCheckBox, c);
        indentPanel.add(indentImplementsComboCheckBox);

        int indentThrows = this.settings.getInt(Keys.INDENT_SIZE_THROWS,
                                             Defaults.INDENT_SIZE_THROWS);
        NumberComboBoxPanelCheckBox indentThrowsComboCheckBox = new NumberComboBoxPanelCheckBox("Throws indent:",
                                                                                                indentThrows > -1,
                                                                                                "",
                                                                                                identItems,
                                                                                                (indentThrows > -1)
                                                                                                    ? String.valueOf(indentThrows)
                                                                                                    : "0");
        _indentThrowsCheckBox = indentThrowsComboCheckBox.getCheckBox();
        _indentThrowsComboBox = indentThrowsComboCheckBox.getComboBoxPanel()
                                                         .getComboBox();
        _indentThrowsCheckBox.addActionListener(this.trigger);
        _indentThrowsComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 7, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(indentThrowsComboCheckBox, c);
        indentPanel.add(indentThrowsComboCheckBox);

        /*int indentParams = this.settings.getInt(Keys.INDENT_SIZE_PARAMETERS,
                                             Defaults.INDENT_SIZE_PARAMETERS);
        NumberComboBoxPanelCheckBox indentParametersComboCheckBox =
        new NumberComboBoxPanelCheckBox("Parameters indent:",
                                        indentParams > -1, "", identItems,
                                        (indentParams > -1)
                                            ? String.valueOf(indentParams)
                                            : "0");
        _indentParametersCheckBox = indentParametersComboCheckBox.getCheckBox();
        _indentParametersComboBox = indentParametersComboCheckBox.getComboBoxPanel()
                                                                 .getComboBox();
        _indentParametersCheckBox.addActionListener(this.trigger);
        _indentParametersComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 8, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        indentPanelLayout.setConstraints(indentParametersComboCheckBox, c);
        indentPanel.add(indentParametersComboCheckBox);*/
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.bottom = 0;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;

        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(policyPanel, c);
        panel.add(policyPanel);

        c.insets.bottom = 0;

        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(indentPanel, c);
        panel.add(indentPanel);

        return panel;
    }


    private JPanel createMiscPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscPanelLayout = new GridBagLayout();
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Misc"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagConstraints c = new GridBagConstraints();
        _indentUsingTabsCheckBox = new JCheckBox("Use tabs to indent",
                                                 this.settings.getBoolean(
                                                                       Keys.INDENT_WITH_TABS,
                                                                       Defaults.INDENT_WITH_TABS));
        _indentUsingTabsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentUsingTabsCheckBox, c);
        miscPanel.add(_indentUsingTabsCheckBox);

        _indentUsingLeadingTabsCheckBox = new JCheckBox("Use only leading tabs",
                                                        this.settings.getBoolean(
                                                                              Keys.INDENT_WITH_TABS_ONLY_LEADING,
                                                                              Defaults.INDENT_WITH_TABS_ONLY_LEADING));
        _indentUsingLeadingTabsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentUsingLeadingTabsCheckBox, c);
        miscPanel.add(_indentUsingLeadingTabsCheckBox);

        _indentCaseSwitchCheckBox = new JCheckBox("Indent \"case\" from \"switch\"",
                                                  this.settings.getBoolean(
                                                                        Keys.INDENT_CASE_FROM_SWITCH,
                                                                        Defaults.INDENT_CASE_FROM_SWITCH));
        _indentCaseSwitchCheckBox.addActionListener(this.trigger);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentCaseSwitchCheckBox, c);
        miscPanel.add(_indentCaseSwitchCheckBox);
        _indentLabelsCheckBox = new JCheckBox("Indent labels",
                                              this.settings.getBoolean(
                                                                    Keys.INDENT_LABEL,
                                                                    Defaults.INDENT_LABEL));
        _indentLabelsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentLabelsCheckBox, c);
        miscPanel.add(_indentLabelsCheckBox);
        _indentFirstColumnCheckBox = new JCheckBox("Indent first column comments",
                                                   this.settings.getBoolean(
                                                                         Keys.INDENT_FIRST_COLUMN_COMMENT,
                                                                         Defaults.INDENT_FIRST_COLUMN_COMMENT));
        _indentFirstColumnCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentFirstColumnCheckBox, c);
        miscPanel.add(_indentFirstColumnCheckBox);

        _indentContinuationCheckBox = new JCheckBox("Continuation indent for blocks",
                                                    this.settings.getBoolean(
                                                                          Keys.INDENT_CONTINUATION_IF,
                                                                          Defaults.INDENT_CONTINUATION_IF));
        _indentContinuationCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 3, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentContinuationCheckBox, c);
        miscPanel.add(_indentContinuationCheckBox);

        _indentContinuationOperatorCheckBox = new JCheckBox("Continuation indent for operators",
                                                            this.settings.getBoolean(
                                                                                  Keys.INDENT_CONTINUATION_OPERATOR,
                                                                                  Defaults.INDENT_CONTINUATION_OPERATOR));
        _indentContinuationOperatorCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentContinuationOperatorCheckBox, c);
        miscPanel.add(_indentContinuationOperatorCheckBox);

        /*_indentContinuationTernaryCheckBox = new JCheckBox("Continuation indent for ternary \"if-else\"",
                                                           this.settings.getBoolean(
                                                                                 Keys.INDENT_CONTINUATION_IF_TERNARY,
                                                                                 Defaults.INDENT_CONTINUATION_IF_TERNARY));
        _indentContinuationTernaryCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentContinuationTernaryCheckBox, c);
        miscPanel.add(_indentContinuationTernaryCheckBox);*/

        /*_indentMethodCallCheckBox = new JCheckBox(
            "Force indentation for parameters",
            this.settings.getBoolean(Keys.INDENT_USE_PARAMS_METHOD_CALL,
                                  Defaults.INDENT_USE_PARAMS_METHOD_CALL));
        _indentMethodCallCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 5, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPanelLayout.setConstraints(_indentMethodCallCheckBox, c);
        miscPanel.add(_indentMethodCallCheckBox);*/
        JPanel alignPanel = new JPanel();
        GridBagLayout alignPanelLayout = new GridBagLayout();
        alignPanel.setLayout(alignPanelLayout);
        alignPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Align"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _alignVariablesCheckBox = new JCheckBox("Variable identifiers",
                                                this.settings.getBoolean(
                                                                      Keys.ALIGN_VAR_IDENTS,
                                                                      Defaults.ALIGN_VAR_IDENTS));
        _alignVariablesCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        alignPanelLayout.setConstraints(_alignVariablesCheckBox, c);
        alignPanel.add(_alignVariablesCheckBox);

        _alignAssignmentsCheckBox = new JCheckBox("Variable assignments",
                                                  this.settings.getBoolean(
                                                                        Keys.ALIGN_VAR_ASSIGNS,
                                                                        Defaults.ALIGN_VAR_ASSIGNS));
        _alignAssignmentsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        alignPanelLayout.setConstraints(_alignAssignmentsCheckBox, c);
        alignPanel.add(_alignAssignmentsCheckBox);

        _alignMethodDefParamsCheckBox = new JCheckBox("Method Def parameters",
                                                      this.settings.getBoolean(
                                                                            Keys.ALIGN_PARAMS_METHOD_DEF,
                                                                            Defaults.ALIGN_PARAMS_METHOD_DEF));
        _alignMethodDefParamsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        alignPanelLayout.setConstraints(_alignMethodDefParamsCheckBox, c);
        alignPanel.add(_alignMethodDefParamsCheckBox);

        _alignMethodCallChainsCheckBox = new JCheckBox("Method call chains",
                                                       this.settings.getBoolean(
                                                                             Keys.ALIGN_METHOD_CALL_CHAINS,
                                                                             Defaults.ALIGN_METHOD_CALL_CHAINS));
        _alignMethodCallChainsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        alignPanelLayout.setConstraints(_alignMethodCallChainsCheckBox, c);
        alignPanel.add(_alignMethodCallChainsCheckBox);


        _alignTernaryOperatorCheckBox       = new JCheckBox("Ternary expressions",
                                                       this.settings.getBoolean(
                                                                             Keys.ALIGN_TERNARY_OPERATOR,
                                                                             Defaults.ALIGN_TERNARY_OPERATOR));
        _alignTernaryOperatorCheckBox      .addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        alignPanelLayout.setConstraints(_alignTernaryOperatorCheckBox      , c);
        alignPanel.add(_alignTernaryOperatorCheckBox      );




        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.bottom = 10;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(alignPanel, c);
        panel.add(alignPanel);

        return panel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _tabbedPane = new JTabbedPane();
        _tabbedPane.add(createGeneralPane(), "General");
        _tabbedPane.add(createMiscPane(), "Misc");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(_tabbedPane, BorderLayout.CENTER);

        if (getContainer() != null)
        {
            _tabbedPane.addChangeListener(new ChangeListener()
                {
                    public void stateChanged(ChangeEvent ev)
                    {
                        String text = getContainer()
                                          .loadPreview(getPreviewFileName());
                        getContainer().getPreview().setText(text);
                    }
                });
        }
    }
}
