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
import de.hunsicker.ui.util.SwingHelper;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component that can be used to display/edit the Jalopy separation
 * preferences.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class SeparationPanel
    extends AbstractPreferencesPanel
{
    //~ Instance variables ····················································

    private JCheckBox _blankLinesAfterLeftCurlyCheckBox;
    private JCheckBox _blankLinesBeforeRightCurlyCheckBox;
    private JCheckBox _chunksByBlankLinesCheckBox;
    private JCheckBox _chunksByCommentsCheckBox;
    private JCheckBox _keepBlankLinesCheckBox;
    private JCheckBox _separatorCheckBox;
    private JCheckBox _separatorRecursiveCheckBox;
    private JComboBox _blankLinesAfterLeftCurlyComboBox;
    private JComboBox _blankLinesBeforeRightCurlyComboBox;
    private JComboBox _fillCharacterComboBox;
    private JComboBox _keepBlankLinesComboBox;
    private JComboBox _linesAfterBlockComboBox;
    private JComboBox _linesAfterClassComboBox;
    private JComboBox _linesAfterDeclarationComboBox;
    private JComboBox _linesAfterImportComboBox;
    private JComboBox _linesAfterInterfaceComboBox;
    private JComboBox _linesAfterMethodComboBox;
    private JComboBox _linesAfterPackageComboBox;
    private JComboBox _linesBeforeBlockComboBox;
    private JComboBox _linesBeforeCaseComboBox;
    private JComboBox _linesBeforeControlComboBox;
    private JComboBox _linesBeforeDeclarationComboBox;
    private JComboBox _linesBeforeJavadocComboBox;
    private JComboBox _linesBeforeMultiLineComboBox;
    private JComboBox _linesBeforeSingleLineComboBox;
    private JTabbedPane _tabbedPane;
    private JTextField _classTextField;
    private JTextField _constructorTextField;
    private JTextField _instanceInitTextField;
    private JTextField _instanceVarTextField;
    private JTextField _interfaceTextField;
    private JTextField _methodTextField;
    private JTextField _staticVarInitTextField;

    //~ Constructors ··························································

    /**
     * Creates a new SeparationPanel object.
     */
    public SeparationPanel()
    {
        initialize();
    }


    /**
     * Creates a new SeparationPanel.
     *
     * @param container the parent container.
     */
    SeparationPanel(PreferencesContainer container)
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
                return "separation";

            case 2 :
                return "separationcomments";

            default :
                return super.getPreviewFileName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.prefs.put(Keys.BLANK_LINES_AFTER_METHOD,
                       (String)_linesAfterMethodComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_CLASS,
                       (String)_linesAfterClassComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_INTERFACE,
                       (String)_linesAfterInterfaceComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_IMPORT,
                       (String)_linesAfterImportComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_PACKAGE,
                       (String)_linesAfterPackageComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_DECLARATION,
                       (String)_linesAfterDeclarationComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_DECLARATION,
                       (String)_linesBeforeDeclarationComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE,
                       (String)_linesBeforeSingleLineComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE,
                       (String)_linesBeforeMultiLineComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_CASE_BLOCK,
                       (String)_linesBeforeCaseComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_BLOCK,
                       (String)_linesBeforeBlockComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_AFTER_BLOCK,
                       (String)_linesAfterBlockComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_CONTROL,
                       (String)_linesBeforeControlComboBox.getSelectedItem());
        this.prefs.put(Keys.BLANK_LINES_BEFORE_COMMENT_JAVADOC,
                       (String)_linesBeforeJavadocComboBox.getSelectedItem());
        this.prefs.putBoolean(Keys.CHUNKS_BY_COMMENTS,
                              _chunksByCommentsCheckBox.isSelected());
        this.prefs.putBoolean(Keys.CHUNKS_BY_BLANK_LINES,
                              _chunksByBlankLinesCheckBox.isSelected());
        this.prefs.put(Keys.SEPARATOR_STATIC_VAR_INIT,
                       _staticVarInitTextField.getText());
        this.prefs.put(Keys.SEPARATOR_INSTANCE_VAR,
                       _instanceVarTextField.getText());
        this.prefs.put(Keys.SEPARATOR_INSTANCE_INIT,
                       _instanceInitTextField.getText());
        this.prefs.put(Keys.SEPARATOR_CTOR, _constructorTextField.getText());
        this.prefs.put(Keys.SEPARATOR_METHOD, _methodTextField.getText());
        this.prefs.put(Keys.SEPARATOR_INTERFACE, _interfaceTextField.getText());
        this.prefs.put(Keys.SEPARATOR_CLASS, _classTextField.getText());
        this.prefs.putBoolean(Keys.COMMENT_INSERT_SEPARATOR,
                              _separatorCheckBox.isSelected());
        this.prefs.putBoolean(Keys.COMMENT_INSERT_SEPARATOR_RECURSIVE,
                              _separatorRecursiveCheckBox.isSelected());
        this.prefs.put(Keys.SEPARATOR_FILL_CHARACTER,
                       (String)_fillCharacterComboBox.getSelectedItem());

        if (_blankLinesAfterLeftCurlyCheckBox.isSelected())
        {
            this.prefs.put(Keys.BLANK_LINES_AFTER_BRACE_LEFT,
                           (String)_blankLinesAfterLeftCurlyComboBox.getSelectedItem());
        }
        else
        {
            this.prefs.put(Keys.BLANK_LINES_AFTER_BRACE_LEFT, "-1");
        }

        if (_blankLinesBeforeRightCurlyCheckBox.isSelected())
        {
            this.prefs.put(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT,
                           (String)_blankLinesBeforeRightCurlyComboBox.getSelectedItem());
        }
        else
        {
            this.prefs.put(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT, "-1");
        }

        if (_keepBlankLinesCheckBox.isSelected())
        {
            this.prefs.put(Keys.BLANK_LINES_KEEP_UP_TO,
                           (String)_keepBlankLinesComboBox.getSelectedItem());
        }
        else
        {
            this.prefs.put(Keys.BLANK_LINES_KEEP_UP_TO, "0");
        }
    }


    private JPanel createBlankLinesPane()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

        GridBagLayout panelLayout = new GridBagLayout();
        panel.setLayout(panelLayout);

        Object[] items ={ "0", "1", "2", "3", "4", "5" };
        JLabel packageLabel = new JLabel("Package statement");
        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(packageLabel, c);
        panel.add(packageLabel);

        ComboBoxPanel afterPackage = new NumberComboBoxPanel("After:", items,
                                                             this.prefs.get(
                                                                            Keys.BLANK_LINES_AFTER_PACKAGE,
                                                                            String.valueOf(Defaults.BLANK_LINES_AFTER_PACKAGE)));
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterPackageComboBox = afterPackage.getComboBox();
        _linesAfterPackageComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterPackage, c);
        panel.add(afterPackage);

        JLabel importLabel = new JLabel("Last import statement");
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(importLabel, c);
        panel.add(importLabel);

        ComboBoxPanel afterImport = new NumberComboBoxPanel("After:", items,
                                                            this.prefs.get(
                                                                           Keys.BLANK_LINES_AFTER_IMPORT,
                                                                           String.valueOf(Defaults.BLANK_LINES_AFTER_IMPORT)));
        SwingHelper.setConstraints(c, 2, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterImportComboBox = afterImport.getComboBox();
        _linesAfterImportComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterImport, c);
        panel.add(afterImport);

        JLabel classLabel = new JLabel("Classes");
        SwingHelper.setConstraints(c, 0, 2, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(classLabel, c);
        panel.add(classLabel);

        ComboBoxPanel afterClass = new NumberComboBoxPanel("After:", items,
                                                           this.prefs.get(
                                                                          Keys.BLANK_LINES_AFTER_CLASS,
                                                                          String.valueOf(Defaults.BLANK_LINES_AFTER_CLASS)));
        SwingHelper.setConstraints(c, 2, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterClassComboBox = afterClass.getComboBox();
        _linesAfterClassComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterClass, c);
        panel.add(afterClass);

        JLabel interfaceLabel = new JLabel("Interfaces");
        SwingHelper.setConstraints(c, 0, 3, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(interfaceLabel, c);
        panel.add(interfaceLabel);

        ComboBoxPanel afterInterface = new NumberComboBoxPanel("After:", items,
                                                               this.prefs.get(
                                                                              Keys.BLANK_LINES_AFTER_INTERFACE,
                                                                              String.valueOf(Defaults.BLANK_LINES_AFTER_INTERFACE)));
        SwingHelper.setConstraints(c, 2, 3, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterInterfaceComboBox = afterInterface.getComboBox();
        _linesAfterInterfaceComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterInterface, c);
        panel.add(afterInterface);

        JLabel methodLabel = new JLabel("Methods");
        SwingHelper.setConstraints(c, 0, 4, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(methodLabel, c);
        panel.add(methodLabel);

        ComboBoxPanel afterMethod = new NumberComboBoxPanel("After:", items,
                                                            this.prefs.get(
                                                                           Keys.BLANK_LINES_AFTER_METHOD,
                                                                           String.valueOf(Defaults.BLANK_LINES_AFTER_METHOD)));
        SwingHelper.setConstraints(c, 2, 4, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterMethodComboBox = afterMethod.getComboBox();
        _linesAfterMethodComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterMethod, c);
        panel.add(afterMethod);

        JLabel blockBeforeLabel = new JLabel("Blocks");
        SwingHelper.setConstraints(c, 0, 5, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(blockBeforeLabel, c);
        panel.add(blockBeforeLabel);

        ComboBoxPanel beforeBlock = new NumberComboBoxPanel("Before:", items,
                                                            this.prefs.get(
                                                                           Keys.BLANK_LINES_BEFORE_BLOCK,
                                                                           String.valueOf(Defaults.BLANK_LINES_BEFORE_BLOCK)));
        SwingHelper.setConstraints(c, 1, 5, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeBlockComboBox = beforeBlock.getComboBox();
        _linesBeforeBlockComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeBlock, c);
        panel.add(beforeBlock);

        ComboBoxPanel afterBlock = new NumberComboBoxPanel("After:", items,
                                                           this.prefs.get(
                                                                          Keys.BLANK_LINES_AFTER_BLOCK,
                                                                          String.valueOf(Defaults.BLANK_LINES_AFTER_BLOCK)));
        SwingHelper.setConstraints(c, 2, 5, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterBlockComboBox = afterBlock.getComboBox();
        _linesAfterBlockComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterBlock, c);
        panel.add(afterBlock);

        JLabel declarationLabel = new JLabel("Declarations");
        SwingHelper.setConstraints(c, 0, 6, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(declarationLabel, c);
        panel.add(declarationLabel);

        ComboBoxPanel beforeDeclaration = new NumberComboBoxPanel("Before:",
                                                                  items,
                                                                  this.prefs.get(
                                                                                 Keys.BLANK_LINES_BEFORE_DECLARATION,
                                                                                 String.valueOf(Defaults.BLANK_LINES_BEFORE_DECLARATION)));
        SwingHelper.setConstraints(c, 1, 6, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeDeclarationComboBox = beforeDeclaration.getComboBox();
        _linesBeforeDeclarationComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeDeclaration, c);
        panel.add(beforeDeclaration);

        ComboBoxPanel afterDeclaration = new NumberComboBoxPanel("After:",
                                                                 items,
                                                                 this.prefs.get(
                                                                                Keys.BLANK_LINES_AFTER_DECLARATION,
                                                                                String.valueOf(Defaults.BLANK_LINES_AFTER_DECLARATION)));
        SwingHelper.setConstraints(c, 2, 6, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesAfterDeclarationComboBox = afterDeclaration.getComboBox();
        _linesAfterDeclarationComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(afterDeclaration, c);
        panel.add(afterDeclaration);

        JLabel caseLabel = new JLabel("Case blocks");
        SwingHelper.setConstraints(c, 0, 7, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(caseLabel, c);
        panel.add(caseLabel);

        ComboBoxPanel beforeCase = new NumberComboBoxPanel("Before:", items,
                                                           this.prefs.get(
                                                                          Keys.BLANK_LINES_BEFORE_CASE_BLOCK,
                                                                          String.valueOf(Defaults.BLANK_LINES_BEFORE_CASE_BLOCK)));
        SwingHelper.setConstraints(c, 1, 7, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeCaseComboBox = beforeCase.getComboBox();
        _linesBeforeCaseComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeCase, c);
        panel.add(beforeCase);

        JLabel controlLabel = new JLabel("Control statements");
        SwingHelper.setConstraints(c, 0, 8, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(controlLabel, c);
        panel.add(controlLabel);

        ComboBoxPanel beforeControlPanel = new NumberComboBoxPanel("Before:",
                                                                   items,
                                                                   this.prefs.get(
                                                                                  Keys.BLANK_LINES_BEFORE_CONTROL,
                                                                                  String.valueOf(Defaults.BLANK_LINES_BEFORE_CONTROL)));
        SwingHelper.setConstraints(c, 1, 8, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeControlComboBox = beforeControlPanel.getComboBox();
        _linesBeforeControlComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeControlPanel, c);
        panel.add(beforeControlPanel);

        JLabel singleLineLabel = new JLabel("Single-line comments");
        SwingHelper.setConstraints(c, 0, 9, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(singleLineLabel, c);
        panel.add(singleLineLabel);

        ComboBoxPanel beforeSingleLine = new NumberComboBoxPanel("Before:",
                                                                 items,
                                                                 this.prefs.get(
                                                                                Keys.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE,
                                                                                String.valueOf(Defaults.BLANK_LINES_BEFORE_COMMENT_SINGLE_LINE)));
        SwingHelper.setConstraints(c, 1, 9, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeSingleLineComboBox = beforeSingleLine.getComboBox();
        _linesBeforeSingleLineComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeSingleLine, c);
        panel.add(beforeSingleLine);

        JLabel multiLineLabel = new JLabel("Multi-line comments");
        SwingHelper.setConstraints(c, 0, 10, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(multiLineLabel, c);
        panel.add(multiLineLabel);

        ComboBoxPanel beforeMultiLine = new NumberComboBoxPanel("Before:",
                                                                items,
                                                                this.prefs.get(
                                                                               Keys.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE,
                                                                               String.valueOf(Defaults.BLANK_LINES_BEFORE_COMMENT_MULTI_LINE)));
        SwingHelper.setConstraints(c, 1, 10, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeMultiLineComboBox = beforeMultiLine.getComboBox();
        _linesBeforeMultiLineComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeMultiLine, c);
        panel.add(beforeMultiLine);

        JLabel javadocLabel = new JLabel("Javadoc comments");
        SwingHelper.setConstraints(c, 0, 11, 1, 1, 0.3, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        panelLayout.setConstraints(javadocLabel, c);
        panel.add(javadocLabel);

        ComboBoxPanel beforeJavadocPanel = new NumberComboBoxPanel("Before:",
                                                                   items,
                                                                   this.prefs.get(
                                                                                  Keys.BLANK_LINES_BEFORE_COMMENT_JAVADOC,
                                                                                  String.valueOf(Defaults.BLANK_LINES_BEFORE_COMMENT_JAVADOC)));
        SwingHelper.setConstraints(c, 1, 11, GridBagConstraints.REMAINDER, 1,
                                   0.7, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _linesBeforeJavadocComboBox = beforeJavadocPanel.getComboBox();
        _linesBeforeJavadocComboBox.addActionListener(this.trigger);
        panelLayout.setConstraints(beforeJavadocPanel, c);
        panel.add(beforeJavadocPanel);
        SwingHelper.setConstraints(c, 0, 12, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);

        Component glue = Box.createVerticalGlue();
        panelLayout.setConstraints(glue, c);
        panel.add(glue);

        return panel;
    }


    private JPanel createCommentsPane()
    {
        JPanel separatorPanel = new JPanel();
        GridBagLayout separatorPanelLayout = new GridBagLayout();
        separatorPanel.setLayout(separatorPanelLayout);
        separatorPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                    BorderFactory.createTitledBorder("General"),
                                                                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _separatorCheckBox = new JCheckBox("Add separator comments",
                                           this.prefs.getBoolean(
                                                                 Keys.COMMENT_INSERT_SEPARATOR,
                                                                 Defaults.COMMENT_INSERT_SEPARATOR));
        _separatorCheckBox.addActionListener(this.trigger);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        separatorPanelLayout.setConstraints(_separatorCheckBox, c);
        separatorPanel.add(_separatorCheckBox);
        _separatorRecursiveCheckBox = new JCheckBox("Add separator comments for inner classes",
                                                    this.prefs.getBoolean(
                                                                          Keys.COMMENT_INSERT_SEPARATOR_RECURSIVE,
                                                                          Defaults.COMMENT_INSERT_SEPARATOR_RECURSIVE));
        _separatorRecursiveCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        separatorPanelLayout.setConstraints(_separatorRecursiveCheckBox, c);
        separatorPanel.add(_separatorRecursiveCheckBox);

        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Descriptions"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout textPanelLayout = new GridBagLayout();
        textPanel.setLayout(textPanelLayout);
        c.insets.right = 10;

        JLabel staticVarInitLabel = new JLabel("Static variables/initializers:");
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(staticVarInitLabel, c);
        textPanel.add(staticVarInitLabel);
        _staticVarInitTextField = new JTextField(this.prefs.get(
                                                                Keys.SEPARATOR_STATIC_VAR_INIT,
                                                                "Static variables/initializers"),
                                                 30);
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_staticVarInitTextField, c);
        textPanel.add(_staticVarInitTextField);

        JLabel instanceVarLabel = new JLabel("Instance variables:");
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(instanceVarLabel, c);
        textPanel.add(instanceVarLabel);
        _instanceVarTextField = new JTextField(this.prefs.get(
                                                              Keys.SEPARATOR_INSTANCE_VAR,
                                                              "Instance variables"),
                                               30);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_instanceVarTextField, c);
        textPanel.add(_instanceVarTextField);

        JLabel instanceInitLabel = new JLabel("Instance initializers:");
        SwingHelper.setConstraints(c, 0, 2, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(instanceInitLabel, c);
        textPanel.add(instanceInitLabel);
        _instanceInitTextField = new JTextField(this.prefs.get(
                                                               Keys.SEPARATOR_INSTANCE_INIT,
                                                               "Instance initializers"),
                                                30);
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_instanceInitTextField, c);
        textPanel.add(_instanceInitTextField);

        JLabel constructorLabel = new JLabel("Constructors:");
        SwingHelper.setConstraints(c, 0, 3, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(constructorLabel, c);
        textPanel.add(constructorLabel);
        _constructorTextField = new JTextField(this.prefs.get(
                                                              Keys.SEPARATOR_CTOR,
                                                              "Constructors"),
                                               30);
        SwingHelper.setConstraints(c, 1, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_constructorTextField, c);
        textPanel.add(_constructorTextField);

        JLabel methodLabel = new JLabel("Methods:");
        SwingHelper.setConstraints(c, 0, 4, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(methodLabel, c);
        textPanel.add(methodLabel);
        _methodTextField = new JTextField(this.prefs.get(Keys.SEPARATOR_METHOD,
                                                         "Methods"), 30);
        SwingHelper.setConstraints(c, 1, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_methodTextField, c);
        textPanel.add(_methodTextField);

        JLabel interfaceLabel = new JLabel("Interfaces:");
        SwingHelper.setConstraints(c, 0, 5, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(interfaceLabel, c);
        textPanel.add(interfaceLabel);
        _interfaceTextField = new JTextField(this.prefs.get(
                                                            Keys.SEPARATOR_INTERFACE,
                                                            "Interfaces"), 30);
        SwingHelper.setConstraints(c, 1, 5, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_interfaceTextField, c);
        textPanel.add(_interfaceTextField);

        JLabel classLabel = new JLabel("Classes:");
        SwingHelper.setConstraints(c, 0, 6, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        textPanelLayout.setConstraints(classLabel, c);
        textPanel.add(classLabel);
        _classTextField = new JTextField(this.prefs.get(Keys.SEPARATOR_CLASS,
                                                        "Classes"), 30);
        SwingHelper.setConstraints(c, 1, 6, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        textPanelLayout.setConstraints(_classTextField, c);
        textPanel.add(_classTextField);

        JPanel characterPanel = new JPanel();
        GridBagLayout characterPanelLayout = new GridBagLayout();
        characterPanel.setLayout(characterPanelLayout);
        characterPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                    BorderFactory.createTitledBorder("Fill character"),
                                                                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        Object[] items ={ "\u00b7", ".", "-", "=", "*", "/" };
        ComboBoxPanel fillCharacterComboBoxPanel = new ComboBoxPanel("Character:",
                                                                     items,
                                                                     this.prefs.get(
                                                                                    Keys.SEPARATOR_FILL_CHARACTER,
                                                                                    "\u00b7"));
        _fillCharacterComboBox = fillCharacterComboBoxPanel.getComboBox();
        _fillCharacterComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        characterPanelLayout.setConstraints(fillCharacterComboBoxPanel, c);
        characterPanel.add(fillCharacterComboBoxPanel);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.top = 10;
        c.insets.bottom = 0;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(separatorPanel, c);
        panel.add(separatorPanel);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(textPanel, c);
        panel.add(textPanel);
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(characterPanel, c);
        panel.add(characterPanel);

        return panel;
    }


    private JPanel createMiscPane()
    {
        JPanel arrayPanel = new JPanel();
        arrayPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Misc"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout arrayLayout = new GridBagLayout();
        arrayPanel.setLayout(arrayLayout);

        String[] items ={ "0", "1", "2", "3", "4", "5" };
        int blankLinesAfterLeftCurly = this.prefs.getInt(Keys.BLANK_LINES_AFTER_BRACE_LEFT,
                                                         Defaults.BLANK_LINES_AFTER_BRACE_LEFT);
        NumberComboBoxPanelCheckBox blankLinesAfterLeftCurlyCheck = new NumberComboBoxPanelCheckBox("Blank lines after left curly brace",
                                                                                                    blankLinesAfterLeftCurly > -1,
                                                                                                    "",
                                                                                                    items,
                                                                                                    (blankLinesAfterLeftCurly > -1)
                                                                                                        ? String.valueOf(blankLinesAfterLeftCurly)
                                                                                                        : "0");
        _blankLinesAfterLeftCurlyCheckBox = blankLinesAfterLeftCurlyCheck.getCheckBox();
        _blankLinesAfterLeftCurlyCheckBox.addActionListener(this.trigger);
        _blankLinesAfterLeftCurlyComboBox = blankLinesAfterLeftCurlyCheck.getComboBoxPanel()
                                                                         .getComboBox();
        _blankLinesAfterLeftCurlyComboBox.addActionListener(this.trigger);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        arrayLayout.setConstraints(blankLinesAfterLeftCurlyCheck, c);
        arrayPanel.add(blankLinesAfterLeftCurlyCheck);

        int blankLinesBeforeRightCurly = this.prefs.getInt(Keys.BLANK_LINES_BEFORE_BRACE_RIGHT,
                                                           Defaults.BLANK_LINES_BEFORE_BRACE_RIGHT);
        NumberComboBoxPanelCheckBox blankLinesBeforeRightCurlyCheck = new NumberComboBoxPanelCheckBox("Blank lines before right curly brace",
                                                                                                      blankLinesBeforeRightCurly > -1,
                                                                                                      "",
                                                                                                      items,
                                                                                                      (blankLinesBeforeRightCurly > -1)
                                                                                                          ? String.valueOf(blankLinesBeforeRightCurly)
                                                                                                          : "0");
        _blankLinesBeforeRightCurlyCheckBox = blankLinesBeforeRightCurlyCheck.getCheckBox();
        _blankLinesBeforeRightCurlyComboBox = blankLinesBeforeRightCurlyCheck.getComboBoxPanel()
                                                                             .getComboBox();
        _blankLinesBeforeRightCurlyCheckBox.addActionListener(this.trigger);
        _blankLinesBeforeRightCurlyComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        arrayLayout.setConstraints(blankLinesBeforeRightCurlyCheck, c);
        arrayPanel.add(blankLinesBeforeRightCurlyCheck);

        int keepBlankLines = this.prefs.getInt(Keys.BLANK_LINES_KEEP_UP_TO,
                                               Defaults.BLANK_LINES_KEEP_UP_TO);
        String[] blankItems ={ "1", "2", "3", "4", "5" };
        NumberComboBoxPanelCheckBox keepBlankLinesCheck = new NumberComboBoxPanelCheckBox("Keep Blank lines up to",
                                                                                          keepBlankLines > 0,
                                                                                          "",
                                                                                          blankItems,
                                                                                          (keepBlankLines > 0)
                                                                                              ? String.valueOf(keepBlankLines)
                                                                                              : "1");
        _keepBlankLinesCheckBox = keepBlankLinesCheck.getCheckBox();
        _keepBlankLinesComboBox = keepBlankLinesCheck.getComboBoxPanel()
                                                     .getComboBox();
        _keepBlankLinesCheckBox.addActionListener(this.trigger);
        _keepBlankLinesComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        arrayLayout.setConstraints(keepBlankLinesCheck, c);
        arrayPanel.add(keepBlankLinesCheck);

        JPanel chunksPanel = new JPanel();
        chunksPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Chunks"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout chunksPanelLayout = new GridBagLayout();
        chunksPanel.setLayout(chunksPanelLayout);
        _chunksByCommentsCheckBox = new JCheckBox("By Comments",
                                                  this.prefs.getBoolean(
                                                                        Keys.CHUNKS_BY_COMMENTS,
                                                                        Defaults.CHUNKS_BY_COMMENTS));
        _chunksByCommentsCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        chunksPanelLayout.setConstraints(_chunksByCommentsCheckBox, c);
        chunksPanel.add(_chunksByCommentsCheckBox);
        _chunksByBlankLinesCheckBox = new JCheckBox("By Blank lines",
                                                    this.prefs.getBoolean(
                                                                          Keys.CHUNKS_BY_BLANK_LINES,
                                                                          Defaults.CHUNKS_BY_BLANK_LINES));
        _chunksByBlankLinesCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        chunksPanelLayout.setConstraints(_chunksByBlankLinesCheckBox, c);
        chunksPanel.add(_chunksByBlankLinesCheckBox);
        _chunksByBlankLinesCheckBox.setEnabled(_keepBlankLinesCheckBox.isSelected());

        GridBagLayout layout = new GridBagLayout();
        JPanel panel = new JPanel();
        panel.setLayout(layout);
        c.insets.top = 10;
        c.insets.bottom = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(arrayPanel, c);
        panel.add(arrayPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(chunksPanel, c);
        panel.add(chunksPanel);
        _keepBlankLinesCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    if (_keepBlankLinesCheckBox.isSelected())
                    {
                        _chunksByBlankLinesCheckBox.setEnabled(true);
                    }
                    else
                    {
                        _chunksByBlankLinesCheckBox.setEnabled(false);
                    }
                }
            });

        return panel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _tabbedPane = new JTabbedPane();
        _tabbedPane.add(createBlankLinesPane(), "Blank lines");
        _tabbedPane.add(createMiscPane(), "Misc");
        _tabbedPane.add(createCommentsPane(), "Comments");
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
