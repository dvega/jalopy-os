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

import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.ui.EmptyButtonGroup;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component that can be used to display/edit the Jalopy wrapping
 * preferences.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class LineWrappingPanel
    extends AbstractPreferencesPanel
{
    //~ Instance variables ····················································

    private JCheckBox _alignExpressionCheckBox;
    private JCheckBox _alignMethodCallParamsCheckBox;
    private JCheckBox _alignMethodCallParamsIfNestedCheckBox;
    private JCheckBox _alignParamsCheckBox;
    private JCheckBox _alignValuesCheckBox;
    private JCheckBox _wrapAfterAssignCheckBox;
    private JCheckBox _wrapAfterChainedCallCheckBox;
    private JCheckBox _wrapAfterCheckBox;
    private JCheckBox _wrapAfterExtendsCheckBox;
    private JCheckBox _wrapAfterImplementsCheckBox;
    private JCheckBox _wrapAfterLeftParenCheckBox;
    private JCheckBox _wrapAfterThrowsTypesCheckBox;
    private JCheckBox _wrapAllIfFirstCheckBox;
    private JCheckBox _wrapArraysCheckBox;
    private JCheckBox _wrapAsNeededCheckBox;
    private JCheckBox _wrapBeforeCheckBox;
    private JCheckBox _wrapBeforeExtendsCheckBox;
    private JCheckBox _wrapBeforeImplementsCheckBox;
    private JCheckBox _wrapBeforeRightParenCheckBox;
    private JCheckBox _wrapBeforeThrowsCheckBox;
    private JCheckBox _wrapLabelsCheckBox;
    private JCheckBox _wrapLinesCheckBox;
    private JComboBox _arraysElementsComboBox;
    private JComboBox _indentDeepComboBox;
    private JComboBox _lineLengthComboBox;
    private JTabbedPane _tabbedPane;
    private JCheckBox _wrapAfterThrowsCheckBox;


    //~ Constructors ··························································

    /**
     * Creates a new LineWrappingPanel object.
     */
    public LineWrappingPanel()
    {
        initialize();
    }


    /**
     * Creates a new LineWrappingPanel.
     *
     * @param container the parent container.
     */
    LineWrappingPanel(PreferencesContainer container)
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
                return "wrappingmisc";

            default :
                return super.getPreviewFileName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.prefs.putBoolean(Keys.LINE_WRAP, _wrapLinesCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_OPERATOR,
                              _wrapBeforeCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_LABEL,
                              _wrapLabelsCheckBox.isSelected());
        this.prefs.put(Keys.LINE_LENGTH,
                       (String)_lineLengthComboBox.getSelectedItem());
        this.prefs.putBoolean(Keys.ALIGN_TERNARY_EXPRESSION,
                              _alignExpressionCheckBox.isSelected());
        this.prefs.putBoolean(Keys.ALIGN_TERNARY_VALUES,
                              _alignValuesCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_THROWS,
                              _wrapBeforeThrowsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_THROWS,
                              _wrapAfterThrowsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_TYPES_THROWS,
                              _wrapAfterThrowsTypesCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_TYPES_IMPLEMENTS,
                              _wrapAfterImplementsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_TYPES_EXTENDS,
                              _wrapAfterExtendsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_PARAMS_METHOD_CALL,
                              _alignMethodCallParamsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_ASSIGN,
                              _wrapAfterAssignCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_ALL,
                              _wrapAllIfFirstCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_LEFT_PAREN,
                              _wrapAfterLeftParenCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_RIGHT_PAREN,
                              _wrapBeforeRightParenCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_PARAMS_METHOD_CALL_IF_NESTED,
                              _alignMethodCallParamsIfNestedCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_PARAMS_METHOD_DEF,
                              _alignParamsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_EXTENDS,
                              _wrapBeforeExtendsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_IMPLEMENTS,
                              _wrapBeforeImplementsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.LINE_WRAP_AFTER_CHAINED_METHOD_CALL,
                              _wrapAfterChainedCallCheckBox.isSelected());

        this.prefs.put(Keys.INDENT_SIZE_DEEP,
                       (String)_indentDeepComboBox.getSelectedItem());

        if (_wrapAsNeededCheckBox.isSelected())
        {
            this.prefs.put(Keys.LINE_WRAP_ARRAY_ELEMENTS, "0");
        }
        else if (_wrapArraysCheckBox.isSelected())
        {
            this.prefs.put(Keys.LINE_WRAP_ARRAY_ELEMENTS,
                           (String)_arraysElementsComboBox.getSelectedItem());
        }
        else
        {
            this.prefs.putInt(Keys.LINE_WRAP_ARRAY_ELEMENTS, Integer.MAX_VALUE);
        }
    }


    private String getWrapValue(int value)
    {
        switch (value)
        {
            case 0 : // wrap as needed
            case Integer.MAX_VALUE : // never wrap
                return "1";

            default :
                return String.valueOf(value);
        }
    }


    private JPanel createGeneralPane()
    {
        JPanel generalPanel = new JPanel();
        GridBagLayout generalPanelLayout = new GridBagLayout();
        generalPanel.setLayout(generalPanelLayout);
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                  BorderFactory.createTitledBorder("General"),
                                                                  BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagConstraints c = new GridBagConstraints();
        _wrapLinesCheckBox = new JCheckBox("Wrap lines",
                                           this.prefs.getBoolean(
                                                                 Keys.LINE_WRAP,
                                                                 Defaults.LINE_WRAP));
        _wrapLinesCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 10, 0);
        generalPanelLayout.setConstraints(_wrapLinesCheckBox, c);
        generalPanel.add(_wrapLinesCheckBox);

        String[] lengths ={ "70", "79", "80", "90", "100" };
        ComboBoxPanel lineLengthComboBoxPanel = new NumberComboBoxPanel("Line length:",
                                                                        lengths,
                                                                        this.prefs.get(
                                                                                       Keys.LINE_LENGTH,
                                                                                       String.valueOf(Defaults.LINE_LENGTH)));
        _lineLengthComboBox = lineLengthComboBoxPanel.getComboBox();
        _lineLengthComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalPanelLayout.setConstraints(lineLengthComboBoxPanel, c);
        generalPanel.add(lineLengthComboBoxPanel);

        Object[] deepIndentSizeItems ={ "50", "55", "60", "65", "70", "75" };
        ComboBoxPanel deepIndent = new NumberComboBoxPanel("Deep indent:",
                                                           deepIndentSizeItems,
                                                           this.prefs.get(
                                                                          Keys.INDENT_SIZE_DEEP,
                                                                          String.valueOf(Defaults.INDENT_SIZE_DEEP)));
        _indentDeepComboBox = deepIndent.getComboBox();
        _indentDeepComboBox.addActionListener(this.trigger);
        c.insets.left = 10;
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalPanelLayout.setConstraints(deepIndent, c);
        generalPanel.add(deepIndent);

        JPanel wrapPolicyPanel = new JPanel();
        GridBagLayout wrapPolicyPanelLayout = new GridBagLayout();
        wrapPolicyPanel.setLayout(wrapPolicyPanelLayout);
        wrapPolicyPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("Wrap policy"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        _wrapAfterLeftParenCheckBox = new JCheckBox("Wrap after left parenthesis",
                                                    this.prefs.getBoolean(
                                                                          Keys.LINE_WRAP_AFTER_LEFT_PAREN,
                                                                          Defaults.LINE_WRAP_AFTER_LEFT_PAREN));
        _wrapAfterLeftParenCheckBox.addActionListener(this.trigger);

        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapAfterLeftParenCheckBox, c);
        wrapPolicyPanel.add(_wrapAfterLeftParenCheckBox);

        _wrapBeforeRightParenCheckBox = new JCheckBox("Wrap before right parenthesis",
                                                      this.prefs.getBoolean(
                                                                            Keys.LINE_WRAP_BEFORE_RIGHT_PAREN,
                                                                            Defaults.LINE_WRAP_BEFORE_RIGHT_PAREN));
        _wrapBeforeRightParenCheckBox.addActionListener(this.trigger);

        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapBeforeRightParenCheckBox, c);
        wrapPolicyPanel.add(_wrapBeforeRightParenCheckBox);

        _wrapAfterAssignCheckBox = new JCheckBox("Wrap after assignments",
                                                 this.prefs.getBoolean(
                                                                       Keys.LINE_WRAP_AFTER_ASSIGN,
                                                                       Defaults.LINE_WRAP_AFTER_ASSIGN));
        _wrapAfterAssignCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapAfterAssignCheckBox, c);
        wrapPolicyPanel.add(_wrapAfterAssignCheckBox);

        _wrapAllIfFirstCheckBox = new JCheckBox("Wrap all if first wrapped",
                                                this.prefs.getBoolean(
                                                                      Keys.LINE_WRAP_ALL,
                                                                      Defaults.LINE_WRAP_ALL));
        _wrapAllIfFirstCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapAllIfFirstCheckBox, c);
        wrapPolicyPanel.add(_wrapAllIfFirstCheckBox);

        _wrapBeforeCheckBox = new JCheckBox("Wrap before operators",
                                            this.prefs.getBoolean(
                                                                  Keys.LINE_WRAP_BEFORE_OPERATOR,
                                                                  Defaults.LINE_WRAP_BEFORE_OPERATOR));
        _wrapBeforeCheckBox.addActionListener(this.trigger);

        SwingHelper.setConstraints(c, 0, 2, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapBeforeCheckBox, c);
        wrapPolicyPanel.add(_wrapBeforeCheckBox);

        _wrapAfterCheckBox = new JCheckBox("Wrap after operators",
                                           !this.prefs.getBoolean(
                                                                  Keys.LINE_WRAP_BEFORE_OPERATOR,
                                                                  Defaults.LINE_WRAP_BEFORE_OPERATOR));
        _wrapAfterCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapPolicyPanelLayout.setConstraints(_wrapAfterCheckBox, c);
        wrapPolicyPanel.add(_wrapAfterCheckBox);

        ButtonGroup operatorButtonGroup = new ButtonGroup();
        operatorButtonGroup.add(_wrapBeforeCheckBox);
        operatorButtonGroup.add(_wrapAfterCheckBox);

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
        layout.setConstraints(generalPanel, c);
        panel.add(generalPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(wrapPolicyPanel, c);
        panel.add(wrapPolicyPanel);

        return panel;
    }


    private JPanel createMiscPane()
    {
        GridBagConstraints c = new GridBagConstraints();
        JPanel wrapAlwaysPanel = new JPanel();
        GridBagLayout wrapAlwaysPanelLayout = new GridBagLayout();
        wrapAlwaysPanel.setLayout(wrapAlwaysPanelLayout);
        wrapAlwaysPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("Wrap always"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _wrapBeforeExtendsCheckBox = new JCheckBox("Before extends keyword",
                                                   this.prefs.getBoolean(
                                                                         Keys.LINE_WRAP_BEFORE_EXTENDS,
                                                                         Defaults.LINE_WRAP_BEFORE_EXTENDS));
        _wrapBeforeExtendsCheckBox.addActionListener(this.trigger);
        c.insets.left = 0;
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapBeforeExtendsCheckBox, c);
        wrapAlwaysPanel.add(_wrapBeforeExtendsCheckBox);
        _wrapAfterExtendsCheckBox = new JCheckBox("After extends types",
                                                  this.prefs.getBoolean(
                                                                        Keys.LINE_WRAP_AFTER_TYPES_EXTENDS,
                                                                        Defaults.LINE_WRAP_AFTER_TYPES_EXTENDS));
        _wrapAfterExtendsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapAfterExtendsCheckBox, c);
        wrapAlwaysPanel.add(_wrapAfterExtendsCheckBox);
        _wrapBeforeImplementsCheckBox = new JCheckBox("Before implements keyword",
                                                      this.prefs.getBoolean(
                                                                            Keys.LINE_WRAP_BEFORE_IMPLEMENTS,
                                                                            Defaults.LINE_WRAP_BEFORE_IMPLEMENTS));
        _wrapBeforeImplementsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapBeforeImplementsCheckBox, c);
        wrapAlwaysPanel.add(_wrapBeforeImplementsCheckBox);
        _wrapAfterImplementsCheckBox = new JCheckBox("After implements types",
                                                     this.prefs.getBoolean(
                                                                           Keys.LINE_WRAP_AFTER_TYPES_IMPLEMENTS,
                                                                           Defaults.LINE_WRAP_AFTER_TYPES_IMPLEMENTS));
        _wrapAfterImplementsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapAfterImplementsCheckBox, c);
        wrapAlwaysPanel.add(_wrapAfterImplementsCheckBox);
        _wrapBeforeThrowsCheckBox = new JCheckBox("Before throws keyword",
                                                  this.prefs.getBoolean(
                                                                        Keys.LINE_WRAP_BEFORE_THROWS,
                                                                        Defaults.LINE_WRAP_BEFORE_THROWS));
        _wrapBeforeThrowsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapBeforeThrowsCheckBox, c);
        wrapAlwaysPanel.add(_wrapBeforeThrowsCheckBox);

        _wrapAfterThrowsTypesCheckBox = new JCheckBox("After throws types",
                                                 this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_TYPES_THROWS,
                                                                       Defaults.LINE_WRAP_AFTER_TYPES_THROWS));
        _wrapAfterThrowsTypesCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapAfterThrowsTypesCheckBox, c);
        wrapAlwaysPanel.add(_wrapAfterThrowsTypesCheckBox);

        _wrapAfterThrowsCheckBox = new JCheckBox("After throws clause",
                                                 this.prefs.getBoolean(Keys.LINE_WRAP_AFTER_THROWS,
                                                                       Defaults.LINE_WRAP_AFTER_THROWS));
        _wrapAfterThrowsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 3, 1, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapAfterThrowsCheckBox, c);
        wrapAlwaysPanel.add(_wrapAfterThrowsCheckBox);

        _alignParamsCheckBox = new JCheckBox("Method Def parameters",
                                             this.prefs.getBoolean(
                                                                   Keys.LINE_WRAP_AFTER_PARAMS_METHOD_DEF,
                                                                   Defaults.LINE_WRAP_AFTER_PARAMS_METHOD_DEF));
        _alignParamsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 3, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_alignParamsCheckBox, c);
        wrapAlwaysPanel.add(_alignParamsCheckBox);

        _alignMethodCallParamsCheckBox = new JCheckBox("Method Call parameters",
                                                       this.prefs.getBoolean(
                                                                             Keys.LINE_WRAP_AFTER_PARAMS_METHOD_CALL,
                                                                             Defaults.LINE_WRAP_AFTER_PARAMS_METHOD_CALL));
        _alignMethodCallParamsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 4, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_alignMethodCallParamsCheckBox, c);
        wrapAlwaysPanel.add(_alignMethodCallParamsCheckBox);
        _alignMethodCallParamsIfNestedCheckBox = new JCheckBox("Method Call parameters if nested",
                                                               (!_alignMethodCallParamsCheckBox.isSelected()) &&
                                                               this.prefs.getBoolean(
                                                                                     Keys.LINE_WRAP_AFTER_PARAMS_METHOD_CALL_IF_NESTED,
                                                                                     Defaults.LINE_WRAP_AFTER_PARAMS_METHOD_CALL_IF_NESTED));

        EmptyButtonGroup group = new EmptyButtonGroup();
        group.add(_alignMethodCallParamsCheckBox);
        group.add(_alignMethodCallParamsIfNestedCheckBox);
        _alignMethodCallParamsIfNestedCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_alignMethodCallParamsIfNestedCheckBox, c);
        wrapAlwaysPanel.add(_alignMethodCallParamsIfNestedCheckBox);

        _wrapAfterChainedCallCheckBox = new JCheckBox("Chained Method Calls",
                                                      this.prefs.getBoolean(
                                                                            Keys.LINE_WRAP_AFTER_CHAINED_METHOD_CALL,
                                                                            Defaults.LINE_WRAP_AFTER_CHAINED_METHOD_CALL));
        _wrapAfterChainedCallCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 5, 1, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapAfterChainedCallCheckBox, c);
        wrapAlwaysPanel.add(_wrapAfterChainedCallCheckBox);

        _wrapLabelsCheckBox = new JCheckBox("Labels",
                                            this.prefs.getBoolean(
                                                                  Keys.LINE_WRAP_AFTER_LABEL,
                                                                  Defaults.LINE_WRAP_AFTER_LABEL));
        _wrapLabelsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 5, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_wrapLabelsCheckBox, c);
        wrapAlwaysPanel.add(_wrapLabelsCheckBox);

        _alignExpressionCheckBox = new JCheckBox("Ternary \"if-else\" expression",
                                                 this.prefs.getBoolean(
                                                                       Keys.ALIGN_TERNARY_EXPRESSION,
                                                                       Defaults.ALIGN_TERNARY_EXPRESSION));
        _alignExpressionCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 6, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_alignExpressionCheckBox, c);
        wrapAlwaysPanel.add(_alignExpressionCheckBox);

        _alignValuesCheckBox = new JCheckBox("Ternary \"if-else\" values",
                                             this.prefs.getBoolean(
                                                                   Keys.ALIGN_TERNARY_VALUES,
                                                                   Defaults.ALIGN_TERNARY_VALUES));
        _alignValuesCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 6, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        wrapAlwaysPanelLayout.setConstraints(_alignValuesCheckBox, c);
        wrapAlwaysPanel.add(_alignValuesCheckBox);

        JPanel arraysPanel = new JPanel();
        GridBagLayout arraysPanelLayout = new GridBagLayout();
        arraysPanel.setLayout(arraysPanelLayout);
        arraysPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Arrays"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        String[] items ={ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
        int arrayElements = this.prefs.getInt(Keys.LINE_WRAP_ARRAY_ELEMENTS,
                                              Defaults.LINE_WRAP_ARRAY_ELEMENTS);

        _wrapAsNeededCheckBox = new JCheckBox("Wrap as needed",
                                              arrayElements == 0);
        _wrapAsNeededCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        arraysPanelLayout.setConstraints(_wrapAsNeededCheckBox, c);
        arraysPanel.add(_wrapAsNeededCheckBox);

        NumberComboBoxPanelCheckBox arrayElementsCheckPanel = new NumberComboBoxPanelCheckBox("Wrap after element",
                                                                                              (arrayElements < Integer.MAX_VALUE) &&
                                                                                              (arrayElements > 0),
                                                                                              "Number:",
                                                                                              items,
                                                                                              getWrapValue(arrayElements));
        _wrapArraysCheckBox = arrayElementsCheckPanel.getCheckBox();
        _wrapArraysCheckBox.addActionListener(this.trigger);
        _arraysElementsComboBox = arrayElementsCheckPanel.getComboBoxPanel()
                                                         .getComboBox();
        _arraysElementsComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        arraysPanelLayout.setConstraints(arrayElementsCheckPanel, c);
        arraysPanel.add(arrayElementsCheckPanel);

        EmptyButtonGroup arrayButtonGroup = new EmptyButtonGroup();
        arrayButtonGroup.add(_wrapAsNeededCheckBox);
        arrayButtonGroup.add(_wrapArraysCheckBox);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.bottom = 10;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;

        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(wrapAlwaysPanel, c);
        panel.add(wrapAlwaysPanel);

        c.insets.top = 0;

        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(arraysPanel, c);
        panel.add(arraysPanel);

        return panel;
    }


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
                        String text = getContainer().loadPreview(getPreviewFileName());
                        getContainer().getPreview().setText(text);
                    }
                });
        }
    }
}
