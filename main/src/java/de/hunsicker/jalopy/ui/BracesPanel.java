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
import de.hunsicker.ui.EmptyButtonGroup;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component that can be used to display/edit the Jalopy general brace
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class BracesPanel
    extends AbstractSettingsPanel
{
    //~ Static variables/initializers ·········································

    /** Indicates C style. */
    private static final String STYLE_C = "c";

    /** Indicates custom style. */
    private static final String STYLE_CUSTOM = "custom";

    /** Indicates old GNU style. */
    private static final String STYLE_GNU = "gnu";

    /** Indicates Sun Java style. */
    private static final String STYLE_SUN = "sun";

    //~ Instance variables ····················································

    private JCheckBox _cStyleCheckBox;
    private JCheckBox _cuddleEmptyBracesCheckBox;
    private JCheckBox _customStyleCheckBox;
    private JCheckBox _gnuStyleCheckBox;
    private JCheckBox _insertDoWhileCheckBox;
    private JCheckBox _insertEmptyStatementCheckBox;
    private JCheckBox _insertForCheckBox;
    private JCheckBox _insertIfElseCheckBox;
    private JCheckBox _insertWhileCheckBox;
    private JCheckBox _newlineLeftCheckBox;
    private JCheckBox _newlineRightCheckBox;
    private JCheckBox _removeBlockCheckBox;
    private JCheckBox _removeDoWhileCheckBox;
    private JCheckBox _removeForCheckBox;
    private JCheckBox _removeIfElseCheckBox;
    private JCheckBox _removeWhileCheckBox;
    private JCheckBox _sunStyleCheckBox;
    private JCheckBox _treatDifferentCheckBox;
    private JComboBox _cuddleEmptyBracesComboBox;
    private JComboBox _indentAfterRightBraceComboBox;
    private JComboBox _indentLeftBraceComboBox;
    private JComboBox _indentRightBraceComboBox;
    private JTabbedPane _tabbedPane;
    private NumberComboBoxPanel _indentAfterRightBraceComboBoxPnl;
    private NumberComboBoxPanel _indentLeftBraceComboBoxPnl;
    private NumberComboBoxPanel _indentRightBraceComboBoxPnl;

    //~ Constructors ··························································

    /**
     * Creates a new BracesPanel.
     */
    public BracesPanel()
    {
        initialize();
    }


    /**
     * Creates a new BracesPanel.
     *
     * @param container the parent container.
     */
    BracesPanel(SettingsContainer container)
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
                return "bracesmisc";

            default :
                return super.getPreviewFileName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.putBoolean(Keys.BRACE_EMPTY_CUDDLE,
                              _cuddleEmptyBracesCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_EMPTY_INSERT_STATEMENT,
                              _insertEmptyStatementCheckBox.isSelected());
        this.settings.put(Keys.INDENT_SIZE_BRACE_CUDDLED,
                       (String)_cuddleEmptyBracesComboBox.getSelectedItem());
        this.settings.putBoolean(Keys.BRACE_INSERT_IF_ELSE,
                              _insertIfElseCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_INSERT_FOR,
                              _insertForCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_INSERT_DO_WHILE,
                              _insertDoWhileCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_INSERT_WHILE,
                              _insertWhileCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_REMOVE_IF_ELSE,
                              _removeIfElseCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_REMOVE_FOR,
                              _removeForCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_REMOVE_DO_WHILE,
                              _removeDoWhileCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_REMOVE_WHILE,
                              _removeWhileCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_REMOVE_BLOCK,
                              _removeBlockCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_NEWLINE_LEFT,
                              _newlineLeftCheckBox.isSelected());
        this.settings.putBoolean(Keys.BRACE_NEWLINE_RIGHT,
                              _newlineRightCheckBox.isSelected());
        this.settings.put(Keys.INDENT_SIZE_BRACE_RIGHT_AFTER,
                       (String)_indentAfterRightBraceComboBox.getSelectedItem());
        this.settings.put(Keys.INDENT_SIZE_BRACE_RIGHT,
                       (String)_indentRightBraceComboBox.getSelectedItem());
        this.settings.put(Keys.INDENT_SIZE_BRACE_LEFT,
                       (String)_indentLeftBraceComboBox.getSelectedItem());
        this.settings.putBoolean(Keys.BRACE_TREAT_DIFFERENT,
                              _treatDifferentCheckBox.isSelected());
    }


    /**
     * Determines whether the current settings forms the C brace style.
     *
     * @return <code>true</code> if the the current settings form the C brace
     *         style.
     */
    private boolean isCStyle()
    {
        if (!_newlineLeftCheckBox.isSelected())
        {
            return false;
        }
        else if (!_newlineRightCheckBox.isSelected())
        {
            return false;
        }
        else if (!"0".equals(_indentLeftBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"0".equals(_indentRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"0".equals(_indentAfterRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }

        return true;
    }


    /**
     * Sets whether the custom mode should be enabled.
     *
     * @param enable if <code>true</code> this custom mode will be enabled.
     */
    private void setCustomMode(boolean enable)
    {
        _indentLeftBraceComboBoxPnl.setEnabled(enable);
        _indentRightBraceComboBoxPnl.setEnabled(enable);
        _indentAfterRightBraceComboBoxPnl.setEnabled(enable);
        _newlineLeftCheckBox.setEnabled(enable);
        _newlineRightCheckBox.setEnabled(enable);
        _treatDifferentCheckBox.setEnabled(enable);
    }


    /**
     * Determines whether the current settings forms the GNU brace style.
     *
     * @return <code>true</code> if the the current settings form the GNU
     *         brace style.
     */
    private boolean isGnuStyle()
    {
        if (!_newlineLeftCheckBox.isSelected())
        {
            return false;
        }
        else if (!_newlineRightCheckBox.isSelected())
        {
            return false;
        }
        else if (!"2".equals(_indentLeftBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"2".equals(_indentRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"0".equals(_indentAfterRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }

        return true;
    }


    /**
     * Determines whether the current settings forms the Sun brace style.
     *
     * @return <code>true</code> if the the current settings form the Sun
     *         brace style.
     */
    private boolean isSunStyle()
    {
        if (_newlineLeftCheckBox.isSelected())
        {
            return false;
        }
        else if (_newlineRightCheckBox.isSelected())
        {
            return false;
        }
        else if (!"1".equals(_indentLeftBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"0".equals(_indentRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }
        else if (!"1".equals(_indentAfterRightBraceComboBox.getSelectedItem()))
        {
            return false;
        }

        return true;
    }


    /**
     * Initializes the misc pane.
     *
     * @return the misc pane.
     */
    private JPanel createMiscPane()
    {
        Object[] items ={ "0", "1", "2", "3", "4", "5" };
        ComboBoxPanelCheckBox emptyBraces = new NumberComboBoxPanelCheckBox("Cuddle braces",
                                                                            this.settings.getBoolean(
                                                                                                  Keys.BRACE_EMPTY_CUDDLE,
                                                                                                  Defaults.BRACE_EMPTY_CUDDLE),
                                                                            "Space before:",
                                                                            items,
                                                                            this.settings.get(
                                                                                           Keys.INDENT_SIZE_BRACE_CUDDLED,
                                                                                           String.valueOf(Defaults.INDENT_SIZE_BRACE_CUDDLED)));
        _cuddleEmptyBracesCheckBox = emptyBraces.getCheckBox();
        _cuddleEmptyBracesCheckBox.addActionListener(this.trigger);
        _cuddleEmptyBracesComboBox = emptyBraces.getComboBoxPanel()
                                                .getComboBox();
        _cuddleEmptyBracesComboBox.addActionListener(this.trigger);

        JPanel emptyPanel = new JPanel();
        GridBagLayout emptyPanelLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        emptyPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Empty braces"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        emptyPanel.setLayout(emptyPanelLayout);
        _insertEmptyStatementCheckBox = new JCheckBox("Insert empty statement",
                                                      this.settings.getBoolean(
                                                                            Keys.BRACE_EMPTY_INSERT_STATEMENT,
                                                                            Defaults.BRACE_EMPTY_INSERT_STATEMENT));
        _insertEmptyStatementCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        emptyPanelLayout.setConstraints(_insertEmptyStatementCheckBox, c);
        emptyPanel.add(_insertEmptyStatementCheckBox);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        emptyPanelLayout.setConstraints(emptyBraces, c);
        emptyPanel.add(emptyBraces);

        EmptyButtonGroup emptyButtonGroup = new EmptyButtonGroup();
        emptyButtonGroup.add(_insertEmptyStatementCheckBox);
        emptyButtonGroup.add(_cuddleEmptyBracesCheckBox);

        JPanel insertBracesPanel = new JPanel();
        insertBracesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        insertBracesPanel.setBorder(BorderFactory.createTitledBorder("Insert braces"));
        _insertIfElseCheckBox = new JCheckBox("if...else",
                                              this.settings.getBoolean(
                                                                    Keys.BRACE_INSERT_IF_ELSE,
                                                                    Defaults.BRACE_INSERT_IF_ELSE));
        _insertIfElseCheckBox.addActionListener(this.trigger);
        insertBracesPanel.add(_insertIfElseCheckBox);
        _insertForCheckBox = new JCheckBox("for",
                                           this.settings.getBoolean(
                                                                 Keys.BRACE_INSERT_FOR,
                                                                 Defaults.BRACE_INSERT_FOR));
        _insertForCheckBox.addActionListener(this.trigger);
        insertBracesPanel.add(_insertForCheckBox);
        _insertWhileCheckBox = new JCheckBox("while",
                                             this.settings.getBoolean(
                                                                   Keys.BRACE_INSERT_WHILE,
                                                                   Defaults.BRACE_INSERT_WHILE));
        _insertWhileCheckBox.addActionListener(this.trigger);
        insertBracesPanel.add(_insertWhileCheckBox);
        _insertDoWhileCheckBox = new JCheckBox("do...while",
                                               this.settings.getBoolean(
                                                                     Keys.BRACE_INSERT_DO_WHILE,
                                                                     Defaults.BRACE_INSERT_DO_WHILE));
        _insertDoWhileCheckBox.addActionListener(this.trigger);
        insertBracesPanel.add(_insertDoWhileCheckBox);

        JPanel removeBracesPanel = new JPanel();
        removeBracesPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        removeBracesPanel.setBorder(BorderFactory.createTitledBorder("Remove braces"));
        _removeIfElseCheckBox = new JCheckBox("if...else",
                                              this.settings.getBoolean(
                                                                    Keys.BRACE_REMOVE_IF_ELSE,
                                                                    Defaults.BRACE_REMOVE_IF_ELSE));
        _removeIfElseCheckBox.addActionListener(this.trigger);
        removeBracesPanel.add(_removeIfElseCheckBox);
        _removeForCheckBox = new JCheckBox("for",
                                           this.settings.getBoolean(
                                                                 Keys.BRACE_REMOVE_FOR,
                                                                 Defaults.BRACE_REMOVE_FOR));
        _removeForCheckBox.addActionListener(this.trigger);
        removeBracesPanel.add(_removeForCheckBox);
        _removeWhileCheckBox = new JCheckBox("while",
                                             this.settings.getBoolean(
                                                                   Keys.BRACE_REMOVE_WHILE,
                                                                   Defaults.BRACE_REMOVE_WHILE));
        _removeWhileCheckBox.addActionListener(this.trigger);
        removeBracesPanel.add(_removeWhileCheckBox);
        _removeDoWhileCheckBox = new JCheckBox("do...while",
                                               this.settings.getBoolean(
                                                                     Keys.BRACE_REMOVE_DO_WHILE,
                                                                     Defaults.BRACE_REMOVE_DO_WHILE));
        _removeDoWhileCheckBox.addActionListener(this.trigger);
        removeBracesPanel.add(_removeDoWhileCheckBox);
        _removeBlockCheckBox = new JCheckBox("Blocks",
                                             this.settings.getBoolean(
                                                                   Keys.BRACE_REMOVE_BLOCK,
                                                                   Defaults.BRACE_REMOVE_BLOCK));
        _removeBlockCheckBox.addActionListener(this.trigger);
        removeBracesPanel.add(_removeBlockCheckBox);

        ButtonGroup ifElseButtonGroup = new EmptyButtonGroup();
        ifElseButtonGroup.add(_insertIfElseCheckBox);
        ifElseButtonGroup.add(_removeIfElseCheckBox);

        ButtonGroup doWhileButtonGroup = new EmptyButtonGroup();
        doWhileButtonGroup.add(_insertDoWhileCheckBox);
        doWhileButtonGroup.add(_removeDoWhileCheckBox);

        ButtonGroup forButtonGroup = new EmptyButtonGroup();
        forButtonGroup.add(_insertForCheckBox);
        forButtonGroup.add(_removeForCheckBox);

        ButtonGroup whileButtonGroup = new EmptyButtonGroup();
        whileButtonGroup.add(_insertWhileCheckBox);
        whileButtonGroup.add(_removeWhileCheckBox);

        GridBagLayout miscPaneLayout = new GridBagLayout();
        JPanel miscPane = new JPanel();
        miscPane.setLayout(miscPaneLayout);
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPaneLayout.setConstraints(insertBracesPanel, c);
        miscPane.add(insertBracesPanel);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPaneLayout.setConstraints(removeBracesPanel, c);
        miscPane.add(removeBracesPanel);
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscPaneLayout.setConstraints(emptyPanel, c);
        miscPane.add(emptyPanel);

        return miscPane;
    }


    /**
     * Creates the pane with the general brace style options.
     *
     * @return general brace style pane.
     */
    private JPanel createStylePane()
    {
        JPanel alignmentPanel = new JPanel();
        alignmentPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                    BorderFactory.createTitledBorder("Wrapping"),
                                                                    BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        alignmentPanel.setLayout(new BoxLayout(alignmentPanel, BoxLayout.Y_AXIS));
        _newlineLeftCheckBox = new JCheckBox("Newline before left brace",
                                             this.settings.getBoolean(
                                                                   Keys.BRACE_NEWLINE_LEFT,
                                                                   Defaults.BRACE_NEWLINE_LEFT));
        _newlineLeftCheckBox.addActionListener(this.trigger);
        alignmentPanel.add(_newlineLeftCheckBox);
        _newlineRightCheckBox = new JCheckBox("Newline after right brace",
                                              this.settings.getBoolean(
                                                                    Keys.BRACE_NEWLINE_RIGHT,
                                                                    Defaults.BRACE_NEWLINE_RIGHT));
        _newlineRightCheckBox.addActionListener(this.trigger);
        alignmentPanel.add(_newlineRightCheckBox);
        _treatDifferentCheckBox = new JCheckBox("Treat class and method blocks different",
                                                this.settings.getBoolean(
                                                                      Keys.BRACE_TREAT_DIFFERENT,
                                                                      Defaults.BRACE_TREAT_DIFFERENT));
        _treatDifferentCheckBox.addActionListener(this.trigger);
        alignmentPanel.add(_treatDifferentCheckBox);

        String[] items ={ "0", "1", "2", "3", "4", "5" };
        _indentLeftBraceComboBoxPnl = new NumberComboBoxPanel("Before left brace:",
                                                              items,
                                                              this.settings.get(
                                                                             Keys.INDENT_SIZE_BRACE_LEFT,
                                                                             String.valueOf(Defaults.INDENT_SIZE_BRACE_LEFT)));
        _indentLeftBraceComboBox = _indentLeftBraceComboBoxPnl.getComboBox();
        _indentLeftBraceComboBox.addActionListener(this.trigger);
        _indentRightBraceComboBoxPnl = new NumberComboBoxPanel("Before right brace:",
                                                               items,
                                                               this.settings.get(
                                                                              Keys.INDENT_SIZE_BRACE_RIGHT,
                                                                              String.valueOf(Defaults.INDENT_SIZE_BRACE_RIGHT)));
        _indentRightBraceComboBox = _indentRightBraceComboBoxPnl.getComboBox();
        _indentRightBraceComboBox.addActionListener(this.trigger);
        _indentAfterRightBraceComboBoxPnl = new NumberComboBoxPanel("After right brace:",
                                                                    items,
                                                                    this.settings.get(
                                                                                   Keys.INDENT_SIZE_BRACE_RIGHT_AFTER,
                                                                                   String.valueOf(Defaults.INDENT_SIZE_BRACE_RIGHT_AFTER)));
        _indentAfterRightBraceComboBox = _indentAfterRightBraceComboBoxPnl.getComboBox();
        _indentAfterRightBraceComboBox.addActionListener(this.trigger);

        JPanel stylesPanel = new JPanel();
        stylesPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Styles"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout stylesLayout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        stylesPanel.setLayout(stylesLayout);
        _cStyleCheckBox = new JCheckBox("C style");
        _cStyleCheckBox.setActionCommand(STYLE_C);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        stylesLayout.setConstraints(_cStyleCheckBox, c);
        stylesPanel.add(_cStyleCheckBox);
        _sunStyleCheckBox = new JCheckBox("Sun Java style");
        _sunStyleCheckBox.setActionCommand(STYLE_SUN);
        SwingHelper.setConstraints(c, 1, 0, 1, GridBagConstraints.REMAINDER,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        stylesLayout.setConstraints(_sunStyleCheckBox, c);
        stylesPanel.add(_sunStyleCheckBox);
        _gnuStyleCheckBox = new JCheckBox("GNU style");
        _gnuStyleCheckBox.setActionCommand(STYLE_GNU);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        stylesLayout.setConstraints(_gnuStyleCheckBox, c);
        stylesPanel.add(_gnuStyleCheckBox);
        _customStyleCheckBox = new JCheckBox("Custom style");
        _customStyleCheckBox.setActionCommand(STYLE_CUSTOM);
        SwingHelper.setConstraints(c, 1, 1, 1, GridBagConstraints.REMAINDER,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        stylesLayout.setConstraints(_customStyleCheckBox, c);
        stylesPanel.add(_customStyleCheckBox);

        if (isSunStyle())
        {
            _sunStyleCheckBox.setSelected(true);
            setCustomMode(false);
            _treatDifferentCheckBox.setEnabled(true);
        }
        else if (isCStyle())
        {
            _cStyleCheckBox.setSelected(true);
            setCustomMode(false);
        }
        else if (isGnuStyle())
        {
            _gnuStyleCheckBox.setSelected(true);
            setCustomMode(false);
            _treatDifferentCheckBox.setEnabled(false);
            _treatDifferentCheckBox.setSelected(false);
        }
        else
        {
            _customStyleCheckBox.setSelected(true);
        }

        ButtonGroup group = new ButtonGroup();
        group.add(_cStyleCheckBox);
        group.add(_sunStyleCheckBox);
        group.add(_gnuStyleCheckBox);
        group.add(_customStyleCheckBox);

        ActionListener buttonHandler = new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                if (ev.getActionCommand() == STYLE_SUN)
                {
                    setCustomMode(false);
                    _newlineLeftCheckBox.setSelected(false);
                    _newlineRightCheckBox.setSelected(false);
                    _indentLeftBraceComboBox.setSelectedItem("1");
                    _indentRightBraceComboBox.setSelectedItem("0");
                    _indentAfterRightBraceComboBox.setSelectedItem("1");
                    _treatDifferentCheckBox.setEnabled(true);
                }
                else if (ev.getActionCommand() == STYLE_C)
                {
                    setCustomMode(false);
                    _newlineLeftCheckBox.setSelected(true);
                    _newlineRightCheckBox.setSelected(true);
                    _indentLeftBraceComboBox.setSelectedItem("0");
                    _indentRightBraceComboBox.setSelectedItem("0");
                    _indentAfterRightBraceComboBox.setSelectedItem("0");
                    _treatDifferentCheckBox.setSelected(false);
                    _treatDifferentCheckBox.setEnabled(false);
                }
                else if (ev.getActionCommand() == STYLE_GNU)
                {
                    setCustomMode(false);
                    _newlineLeftCheckBox.setSelected(true);
                    _newlineRightCheckBox.setSelected(true);
                    _indentLeftBraceComboBox.setSelectedItem("2");
                    _indentRightBraceComboBox.setSelectedItem("2");
                    _indentAfterRightBraceComboBox.setSelectedItem("0");
                    _treatDifferentCheckBox.setEnabled(false);
                    _treatDifferentCheckBox.setSelected(false);
                }
                else
                {
                    setCustomMode(true);
                }
            }
        };

        _cStyleCheckBox.addActionListener(buttonHandler);
        _sunStyleCheckBox.addActionListener(buttonHandler);
        _gnuStyleCheckBox.addActionListener(buttonHandler);
        _customStyleCheckBox.addActionListener(buttonHandler);

        JPanel whitespacePanel = new JPanel();
        whitespacePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("Whitespace"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout whitespacePanelLayout = new GridBagLayout();
        whitespacePanel.setLayout(whitespacePanelLayout);
        c.insets.top = 0;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        whitespacePanelLayout.setConstraints(_indentLeftBraceComboBoxPnl, c);
        whitespacePanel.add(_indentLeftBraceComboBoxPnl);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        whitespacePanelLayout.setConstraints(_indentRightBraceComboBoxPnl, c);
        whitespacePanel.add(_indentRightBraceComboBoxPnl);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        whitespacePanelLayout.setConstraints(_indentAfterRightBraceComboBoxPnl, c);
        whitespacePanel.add(_indentAfterRightBraceComboBoxPnl);

        JPanel stylePane = new JPanel();
        GridBagLayout styleLayout = new GridBagLayout();
        stylePane.setLayout(styleLayout);
        c.insets.top = 10;
        c.insets.bottom = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        styleLayout.setConstraints(stylesPanel, c);
        stylePane.add(stylesPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        styleLayout.setConstraints(alignmentPanel, c);
        stylePane.add(alignmentPanel);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        styleLayout.setConstraints(whitespacePanel, c);
        stylePane.add(whitespacePanel);

        return stylePane;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _tabbedPane = new JTabbedPane();
        _tabbedPane.add(createStylePane(), "General");
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
