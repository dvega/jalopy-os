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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;


/**
 * A component that can be used to display/edit the Jalopy whitespace
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class WhitespacePanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _assignmentOperatorsCheckBox;
    private JCheckBox _bangCheckBox;
    private JCheckBox _bitwiseOperatorsCheckBox;
    private JCheckBox _bracesCheckBox;
    private JCheckBox _bracketsCheckBox;
    private JCheckBox _bracketsTypesCheckBox;
    private JCheckBox _caseColonCheckBox;
    private JCheckBox _castCheckBox;
    private JCheckBox _commaCheckBox;
    private JCheckBox _logicalOperatorsCheckBox;
    private JCheckBox _mathematicalOperatorsCheckBox;
    private JCheckBox _methodCallCheckBox;
    private JCheckBox _methodDefCheckBox;
    private JCheckBox _paddingBracesCheckBox;
    private JCheckBox _paddingBracketsCheckBox;
    private JCheckBox _paddingCastCheckBox;
    private JCheckBox _paddingParenCheckBox;
    private JCheckBox _relationalOperatorsCheckBox;
    private JCheckBox _semicolonCheckBox;
    private JCheckBox _shiftOperatorsCheckBox;
    private JCheckBox _statementCheckBox;

    //~ Constructors ··························································

    /**
     * Creates a new WhitespacePanel object.
     */
    public WhitespacePanel()
    {
        initialize();
    }


    /**
     * Creates a new WhitespacePanel.
     *
     * @param container the parent container.
     */
    WhitespacePanel(SettingsContainer container)
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
        this.settings.putBoolean(Keys.SPACE_AFTER_COMMA,
                              _commaCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_AFTER_SEMICOLON,
                              _semicolonCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_AFTER_CAST, _castCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_METHOD_CALL_PAREN,
                              _methodCallCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_METHOD_DEF_PAREN,
                              _methodDefCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_STATEMENT_PAREN,
                              _statementCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_CASE_COLON,
                              _caseColonCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_BRACKETS_TYPES,
                              _bracketsTypesCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_BRACKETS,
                              _bracketsCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_BRACES,
                              _bracesCheckBox.isSelected());
        this.settings.putBoolean(Keys.SPACE_BEFORE_LOGICAL_NOT,
                              _bangCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_BRACKETS,
                              _paddingBracketsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_BRACES,
                              _paddingBracesCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_PAREN,
                              _paddingParenCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_CAST,
                              _paddingCastCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_MATH_OPERATORS,
                              _mathematicalOperatorsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_LOGICAL_OPERATORS,
                              _logicalOperatorsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_RELATIONAL_OPERATORS,
                              _relationalOperatorsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_ASSIGNMENT_OPERATORS,
                              _assignmentOperatorsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_SHIFT_OPERATORS,
                              _shiftOperatorsCheckBox.isSelected());
        this.settings.putBoolean(Keys.PADDING_BITWISE_OPERATORS,
                              _bitwiseOperatorsCheckBox.isSelected());
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        JPanel beforePanel = new JPanel();
        GridBagLayout beforeLayout = new GridBagLayout();
        beforePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Space before"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 0)));
        beforePanel.setLayout(beforeLayout);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _methodDefCheckBox = new JCheckBox("Method Def parentheses",
                                           this.settings.getBoolean(
                                                                 Keys.SPACE_BEFORE_METHOD_DEF_PAREN,
                                                                 Defaults.SPACE_BEFORE_METHOD_DEF_PAREN));
        _methodDefCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_methodDefCheckBox, c);
        beforePanel.add(_methodDefCheckBox);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _bracesCheckBox = new JCheckBox("Braces",
                                        this.settings.getBoolean(
                                                              Keys.SPACE_BEFORE_BRACES,
                                                              Defaults.SPACE_BEFORE_BRACES));
        _bracesCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_bracesCheckBox, c);
        beforePanel.add(_bracesCheckBox);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _methodCallCheckBox = new JCheckBox("Method Call parentheses",
                                            this.settings.getBoolean(
                                                                  Keys.SPACE_BEFORE_METHOD_CALL_PAREN,
                                                                  Defaults.SPACE_BEFORE_METHOD_CALL_PAREN));
        _methodCallCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_methodCallCheckBox, c);
        beforePanel.add(_methodCallCheckBox);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _bracketsCheckBox = new JCheckBox("Brackets",
                                          this.settings.getBoolean(
                                                                Keys.SPACE_BEFORE_BRACKETS,
                                                                Defaults.SPACE_BEFORE_BRACKETS));
        _bracketsCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_bracketsCheckBox, c);
        beforePanel.add(_bracketsCheckBox);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _statementCheckBox = new JCheckBox("Statement parentheses",
                                           this.settings.getBoolean(
                                                                 Keys.SPACE_BEFORE_STATEMENT_PAREN,
                                                                 Defaults.SPACE_BEFORE_STATEMENT_PAREN));
        _statementCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_statementCheckBox, c);
        beforePanel.add(_statementCheckBox);
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _bracketsTypesCheckBox = new JCheckBox("Brackets in types",
                                               this.settings.getBoolean(
                                                                     Keys.SPACE_BEFORE_BRACKETS_TYPES,
                                                                     Defaults.SPACE_BEFORE_BRACKETS_TYPES));
        _bracketsTypesCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_bracketsTypesCheckBox, c);
        beforePanel.add(_bracketsTypesCheckBox);
        SwingHelper.setConstraints(c, 0, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _caseColonCheckBox = new JCheckBox("Case colon",
                                           this.settings.getBoolean(
                                                                 Keys.SPACE_BEFORE_CASE_COLON,
                                                                 Defaults.SPACE_BEFORE_CASE_COLON));
        _caseColonCheckBox.addActionListener(this.trigger);
        beforeLayout.setConstraints(_caseColonCheckBox, c);
        beforePanel.add(_caseColonCheckBox);

        JPanel afterPanel = new JPanel();
        afterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        afterPanel.setBorder(BorderFactory.createTitledBorder("Space after"));
        _commaCheckBox = new JCheckBox("Comma",
                                       this.settings.getBoolean(
                                                             Keys.SPACE_AFTER_COMMA,
                                                             Defaults.SPACE_AFTER_COMMA));
        _commaCheckBox.addActionListener(this.trigger);
        afterPanel.add(_commaCheckBox);
        _semicolonCheckBox = new JCheckBox("Semicolon",
                                           this.settings.getBoolean(
                                                                 Keys.SPACE_AFTER_SEMICOLON,
                                                                 Defaults.SPACE_AFTER_SEMICOLON));
        _semicolonCheckBox.addActionListener(this.trigger);
        afterPanel.add(_semicolonCheckBox);
        _castCheckBox = new JCheckBox("Type Cast",
                                      this.settings.getBoolean(
                                                            Keys.SPACE_AFTER_CAST,
                                                            Defaults.SPACE_AFTER_CAST));
        _castCheckBox.addActionListener(this.trigger);
        afterPanel.add(_castCheckBox);
        _bangCheckBox = new JCheckBox("Negation",
                                      this.settings.getBoolean(
                                                            Keys.SPACE_BEFORE_LOGICAL_NOT,
                                                            Defaults.SPACE_BEFORE_LOGICAL_NOT));
        _bangCheckBox.addActionListener(this.trigger);
        afterPanel.add(_bangCheckBox);

        GridBagLayout aroundLayout = new GridBagLayout();
        JPanel aroundPanel = new JPanel();
        aroundPanel.setLayout(aroundLayout);
        aroundPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Spaces around"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _assignmentOperatorsCheckBox = new JCheckBox("Assignment Operators",
                                                     this.settings.getBoolean(
                                                                           Keys.PADDING_ASSIGNMENT_OPERATORS,
                                                                           Defaults.PADDING_ASSIGNMENT_OPERATORS));
        _assignmentOperatorsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        aroundLayout.setConstraints(_assignmentOperatorsCheckBox, c);
        aroundPanel.add(_assignmentOperatorsCheckBox);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _bitwiseOperatorsCheckBox = new JCheckBox("Bitwise Operators",
                                                  this.settings.getBoolean(
                                                                        Keys.PADDING_BITWISE_OPERATORS,
                                                                        Defaults.PADDING_BITWISE_OPERATORS));
        _bitwiseOperatorsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_bitwiseOperatorsCheckBox, c);
        aroundPanel.add(_bitwiseOperatorsCheckBox);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _logicalOperatorsCheckBox = new JCheckBox("Logical Operators",
                                                  this.settings.getBoolean(
                                                                        Keys.PADDING_LOGICAL_OPERATORS,
                                                                        Defaults.PADDING_LOGICAL_OPERATORS));
        _logicalOperatorsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_logicalOperatorsCheckBox, c);
        aroundPanel.add(_logicalOperatorsCheckBox);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _mathematicalOperatorsCheckBox = new JCheckBox("Mathematical Operators",
                                                       this.settings.getBoolean(
                                                                             Keys.PADDING_MATH_OPERATORS,
                                                                             Defaults.PADDING_MATH_OPERATORS));
        _mathematicalOperatorsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_mathematicalOperatorsCheckBox, c);
        aroundPanel.add(_mathematicalOperatorsCheckBox);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _relationalOperatorsCheckBox = new JCheckBox("Relational Operators",
                                                     this.settings.getBoolean(
                                                                           Keys.PADDING_RELATIONAL_OPERATORS,
                                                                           Defaults.PADDING_RELATIONAL_OPERATORS));
        _relationalOperatorsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_relationalOperatorsCheckBox, c);
        aroundPanel.add(_relationalOperatorsCheckBox);
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _shiftOperatorsCheckBox = new JCheckBox("Shift Operators",
                                                this.settings.getBoolean(
                                                                      Keys.PADDING_SHIFT_OPERATORS,
                                                                      Defaults.PADDING_SHIFT_OPERATORS));
        _shiftOperatorsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_shiftOperatorsCheckBox, c);
        aroundPanel.add(_shiftOperatorsCheckBox);
        SwingHelper.setConstraints(c, 0, 3, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _paddingBracesCheckBox = new JCheckBox("Braces",
                                               this.settings.getBoolean(
                                                                     Keys.PADDING_BRACES,
                                                                     Defaults.PADDING_BRACES));
        _paddingBracesCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_paddingBracesCheckBox, c);
        aroundPanel.add(_paddingBracesCheckBox);
        SwingHelper.setConstraints(c, 1, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _paddingBracketsCheckBox = new JCheckBox("Brackets",
                                                 this.settings.getBoolean(
                                                                       Keys.PADDING_BRACKETS,
                                                                       Defaults.PADDING_BRACKETS));
        _paddingBracketsCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_paddingBracketsCheckBox, c);
        aroundPanel.add(_paddingBracketsCheckBox);
        SwingHelper.setConstraints(c, 0, 4, GridBagConstraints.RELATIVE, 1, 1.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _paddingParenCheckBox = new JCheckBox("Parentheses",
                                              this.settings.getBoolean(
                                                                    Keys.PADDING_PAREN,
                                                                    Defaults.PADDING_PAREN));
        _paddingParenCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_paddingParenCheckBox, c);
        aroundPanel.add(_paddingParenCheckBox);
        SwingHelper.setConstraints(c, 1, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _paddingCastCheckBox = new JCheckBox("Type Cast Parentheses",
                                             this.settings.getBoolean(
                                                                   Keys.PADDING_CAST,
                                                                   Defaults.PADDING_CAST));
        _paddingCastCheckBox.addActionListener(this.trigger);
        aroundLayout.setConstraints(_paddingCastCheckBox, c);
        aroundPanel.add(_paddingCastCheckBox);

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(beforePanel, c);
        add(beforePanel);
        c.insets.top = 0;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(afterPanel, c);
        add(afterPanel);
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(aroundPanel, c);
        add(aroundPanel);
    }
}
