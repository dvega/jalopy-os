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
import de.hunsicker.jalopy.storage.Key;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import de.hunsicker.util.StringHelper;


/**
 * A component that can be used to display/edit the Jalopy header settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class HeaderPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private AddRemoveList _keysList;
    private JButton _addButton;
    private JButton _removeButton;
    private JCheckBox _useCheckBox;
    private JComboBox _blankLinesAfterComboBox;
    private JComboBox _blankLinesBeforeComboBox;
    private JComboBox _smartModeComboBox;
    private JTabbedPane _tabs;
    private JTextArea _textTextArea;
    private NumberComboBoxPanel _blankLinesAfterComboBoxPnl;
    private NumberComboBoxPanel _blankLinesBeforeComboBoxPnl;

    //~ Constructors ··························································

    /**
     * Creates a new HeaderPanel object.
     */
    public HeaderPanel()
    {
        initialize();
    }


    /**
     * Creates a new HeaderPanel.
     *
     * @param container the parent container.
     */
    HeaderPanel(SettingsContainer container)
    {
        super(container);
        initialize();
    }

    private final static String DELIMETER = "|";

    //~ Methods ·······························································

    private void storeText()
    {
        String text = _textTextArea.getText().trim();

        if (text.length() > 0)
        {

            String[] lines = StringHelper.split(text, "\n");

            StringBuffer buf = new StringBuffer(text.length());

            for (int i = 0; i < lines.length; i++)
            {
                buf.append(StringHelper.trimTrailing(lines[i]));
                buf.append(DELIMETER);
            }

            if (lines.length > 0)
            {
                buf.deleteCharAt(buf.length() - 1);
            }

            this.settings.put(getTextKey(), buf.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.put(getBlankLinesBeforeKey(),
                       (String)_blankLinesBeforeComboBox.getSelectedItem());
        this.settings.put(getBlankLinesAfterKey(),
                       (String)_blankLinesAfterComboBox.getSelectedItem());
        this.settings.putBoolean(getUseKey(), _useCheckBox.isSelected());
        this.settings.put(getSmartModeKey(),
                       (String)_smartModeComboBox.getSelectedItem());

        storeText();

        DefaultListModel keysListModel = (DefaultListModel)_keysList.getModel();

        if (keysListModel.size() > 0)
        {
            String[] items = new String[keysListModel.size()];
            keysListModel.copyInto(items);

            StringBuffer buf = new StringBuffer(100);

            for (int i = 0; i < items.length; i++)
            {
                buf.append(items[i]);
                buf.append(DELIMETER);
            }

            buf.deleteCharAt(buf.length() - 1);
            this.settings.put(getKeysKey(), buf.toString());
        }
        else
        {
            this.settings.put(getKeysKey(), "");
        }
    }


    /**
     * {@inheritDoc}
     */
    public void validateSettings()
        throws ValidationException
    {
        DefaultListModel keysListModel = (DefaultListModel)_keysList.getModel();

        // we need at least one identify key to make the header/footer feature
        // work
        if ((!isSmartModeEnabled()) && _useCheckBox.isSelected() &&
            (keysListModel.size() == 0))
        {
            JOptionPane.showMessageDialog(HeaderPanel.this,
                                          "You have to specify at least one identify key in the '" +
                                          getDeleteLabel() + "' panel.",
                                          "Error: Key missing",
                                          JOptionPane.ERROR_MESSAGE);
            throw new ValidationException("No key specified");
        }
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#BLANK_LINES_AFTER_HEADER
     */
    protected Key getBlankLinesAfterKey()
    {
        return Keys.BLANK_LINES_AFTER_HEADER;
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#BLANK_LINES_BEFORE_HEADER
     */
    protected Key getBlankLinesBeforeKey()
    {
        return Keys.BLANK_LINES_BEFORE_HEADER;
    }


    /**
     * Returns the label text for the identiy panel.
     *
     * @return label text.
     */
    protected String getDeleteLabel()
    {
        return "Delete Headers";
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#HEADER_KEYS
     */
    protected Key getKeysKey()
    {
        return Keys.HEADER_KEYS;
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#HEADER_SMART_MODE_LINES
     */
    protected Key getSmartModeKey()
    {
        return Keys.HEADER_SMART_MODE_LINES;
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#HEADER_TEXT
     */
    protected Key getTextKey()
    {
        return Keys.HEADER_TEXT;
    }


    /**
     * Returns the settings key to store the setting.
     *
     * @return settings key.
     *
     * @see de.hunsicker.jalopy.storage.Keys#HEADER
     */
    protected Key getUseKey()
    {
        return Keys.HEADER;
    }

    /**
     * Returns the text for the use label.
     *
     * @return text for use label.
     */
    protected String getUseLabel()
    {
        return "Use Header";
    }


    /**
     * Determines whether the SmartMode feature is enabled.
     *
     * @return <code>true</code> if the SmartMode feature is enabled.
     *
     * @since 1.0b8
     */
    private boolean isSmartModeEnabled()
    {
        try
        {
            return Integer.parseInt((String)_smartModeComboBox.getSelectedItem()) > 0;
        }
        catch (NumberFormatException neverOccurs)
        {
            return false;
        }
    }

    /**
     * Returns the default value for the BLANK_LINES_AFTER_XXX setting.
     * @return default value of the BLANK_LINES_AFTER_XXX setting.
     * @since 1.0b9
     */
    protected String getDefaultAfter()
    {
        return String.valueOf(Defaults.BLANK_LINES_AFTER_HEADER);
    }

    /**
     * Returns the default values for the combo box entries to choose the
     * value for the BLANK_LINES_AFTER_XXX settings.
     * @return the default values for the blank lines after combo box.
     * @since 1.0b9
     */
    protected String[] getItemsAfter()
    {
        return new String[] { "0", "1", "2", "3", "4", "5" };
    }

    private String loadText()
    {
        String text = this.settings.get(getTextKey(), "");
        return text.replace('|', '\n');
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _textTextArea = new JTextArea(loadText(), 7, 50);
        _textTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        _textTextArea.setForeground(new Color(0, 128, 128));
        _textTextArea.setCaretPosition(0);

        JScrollPane textScroller = new JScrollPane(_textTextArea);
        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        textPanel.setLayout(new BorderLayout());
        textPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        textPanel.add(textScroller);

        JPanel headerPanel = new JPanel();
        GridBagLayout headerLayout = new GridBagLayout();
        headerPanel.setLayout(headerLayout);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("General"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 0)));

        GridBagConstraints c = new GridBagConstraints();
        _useCheckBox = new JCheckBox(getUseLabel(),
                                     this.settings.getBoolean(getUseKey(), false));
        _useCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        headerLayout.setConstraints(_useCheckBox, c);
        headerPanel.add(_useCheckBox);

        int lines = this.settings.getInt(getSmartModeKey(),
                                      Defaults.HEADER_SMART_MODE_LINES);
        String[] lineItems ={ "0", "5", "10", "15", "20" };
        NumberComboBoxPanel smartModeComboBoxPanel = new NumberComboBoxPanel("Smart Mode:",
                                                                             lineItems,
                                                                             String.valueOf(lines));
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        headerLayout.setConstraints(smartModeComboBoxPanel, c);
        headerPanel.add(smartModeComboBoxPanel);
        _smartModeComboBox = smartModeComboBoxPanel.getComboBox();

        JPanel blankLinesPanel = new JPanel();
        blankLinesPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("Blank lines"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout blankLinesLayout = new GridBagLayout();
        blankLinesPanel.setLayout(blankLinesLayout);

        String[] items ={ "0", "1", "2", "3", "4", "5" };
        _blankLinesBeforeComboBoxPnl = new NumberComboBoxPanel("Blank lines before:",
                                                               items,
                                                               this.settings.get(
                                                                              getBlankLinesBeforeKey(),
                                                                              "0"));
        _blankLinesBeforeComboBox = _blankLinesBeforeComboBoxPnl.getComboBox();
        _blankLinesBeforeComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        blankLinesLayout.setConstraints(_blankLinesBeforeComboBoxPnl, c);
        blankLinesPanel.add(_blankLinesBeforeComboBoxPnl);
        _blankLinesAfterComboBoxPnl = new NumberComboBoxPanel("Blank lines after:",
                                                              getItemsAfter(),
                                                              this.settings.get(
                                                                             getBlankLinesAfterKey(),
                                                                             getDefaultAfter()));
        _blankLinesAfterComboBox = _blankLinesAfterComboBoxPnl.getComboBox();
        _blankLinesAfterComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        blankLinesLayout.setConstraints(_blankLinesAfterComboBoxPnl, c);
        blankLinesPanel.add(_blankLinesAfterComboBoxPnl);

        JPanel identifyPanel = new JPanel();
        identifyPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                   BorderFactory.createTitledBorder(getDeleteLabel()),
                                                                   BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout identifyLayout = new GridBagLayout();
        identifyPanel.setLayout(identifyLayout);

        String keysString = this.settings.get(getKeysKey(), "");
        List keys = Collections.EMPTY_LIST;

        if ((keysString != null) && (!keysString.trim().equals("")))
        {
            keys = new ArrayList();

            for (StringTokenizer tokens = new StringTokenizer(keysString, DELIMETER);
                 tokens.hasMoreElements();)
            {
                keys.add(tokens.nextElement());
            }
        }

        _keysList = new AddRemoveList("Add new identify key",
                                      "Enter key string:", keys);

        JScrollPane keysScrollPane = new JScrollPane(_keysList);
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        identifyLayout.setConstraints(keysScrollPane, c);
        identifyPanel.add(keysScrollPane);
        c.insets.bottom = 2;
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _addButton = _keysList.getAddButton();
        identifyLayout.setConstraints(_addButton, c);
        identifyPanel.add(_addButton);
        c.insets.left = 10;
        c.insets.right = 0;
        c.insets.bottom = 0;
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _removeButton = _keysList.getRemoveButton();
        _removeButton.setEnabled(false);
        identifyLayout.setConstraints(_removeButton, c);
        identifyPanel.add(_removeButton);

        JPanel panels = new JPanel();
        GridBagLayout panelsLayout = new GridBagLayout();
        panels.setLayout(panelsLayout);
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        panelsLayout.setConstraints(headerPanel, c);
        panels.add(headerPanel);
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        panelsLayout.setConstraints(blankLinesPanel, c);
        panels.add(blankLinesPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        panelsLayout.setConstraints(identifyPanel, c);
        panels.add(identifyPanel);
        _tabs = new JTabbedPane();
        _tabs.add(panels, "Options");
        _tabs.add(textPanel, "Text");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(_tabs, BorderLayout.CENTER);
    }
}
