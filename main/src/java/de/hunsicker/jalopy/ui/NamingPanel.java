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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;


/**
 * A component that can be used to display/edit the Jalopy Code Inspector
 * naming settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class NamingPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private final PatternCompiler _compiler = new Perl5Compiler();
    private JTextField _abstractClassTextField;
    private JTextField _classTextField;
    private JTextField _interfaceTextField;
    private JTextField _labelTextField;
    private JTextField _packageTextField;

    /** The data of the methods table. */
    private String[][] _methodData;

    /** The data of the variables table. */
    private String[][] _variablesData;

    //~ Constructors ··························································

    /**
     * Creates a new NamingPanel object.
     */
    public NamingPanel()
    {
        initialize();
    }


    /**
     * Creates a new NamingPanel.
     *
     * @param container the parent container.
     */
    NamingPanel(SettingsContainer container)
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
        this.settings.put(Keys.REGEXP_PACKAGE, _packageTextField.getText());
        this.settings.put(Keys.REGEXP_CLASS, _classTextField.getText());
        this.settings.put(Keys.REGEXP_CLASS_ABSTRACT,
                       _abstractClassTextField.getText());
        this.settings.put(Keys.REGEXP_INTERFACE, _interfaceTextField.getText());
        this.settings.put(Keys.REGEXP_LABEL, _labelTextField.getText());
        this.settings.put(Keys.REGEXP_FIELD_PUBLIC, _variablesData[0][1]);
        this.settings.put(Keys.REGEXP_FIELD_PROTECTED, _variablesData[0][2]);
        this.settings.put(Keys.REGEXP_FIELD_DEFAULT, _variablesData[0][3]);
        this.settings.put(Keys.REGEXP_FIELD_PRIVATE, _variablesData[0][4]);
        this.settings.put(Keys.REGEXP_FIELD_PUBLIC_STATIC, _variablesData[1][1]);
        this.settings.put(Keys.REGEXP_FIELD_PROTECTED_STATIC, _variablesData[1][2]);
        this.settings.put(Keys.REGEXP_FIELD_DEFAULT_STATIC, _variablesData[1][3]);
        this.settings.put(Keys.REGEXP_FIELD_PRIVATE_STATIC, _variablesData[1][4]);
        this.settings.put(Keys.REGEXP_FIELD_PUBLIC_STATIC_FINAL,
                       _variablesData[2][1]);
        this.settings.put(Keys.REGEXP_FIELD_PROTECTED_STATIC_FINAL,
                       _variablesData[2][2]);
        this.settings.put(Keys.REGEXP_FIELD_DEFAULT_STATIC_FINAL,
                       _variablesData[2][3]);
        this.settings.put(Keys.REGEXP_FIELD_PRIVATE_STATIC_FINAL,
                       _variablesData[2][4]);
    }


    /**
     * {@inheritDoc}
     */
    public void validateSettings()
        throws ValidationException
    {
        /*checkRegexp("Packages", _packageTextField.getText());
        checkRegexp("Classes", _classTextField.getText());
        checkRegexp("Abstract Classes", _abstractClassTextField.getText());
        checkRegexp("Interfaces", _interfaceTextField.getText());
        checkRegexp("Labels", _labelTextField.getText());*/
    }


    /**
     * Checks whether the given regexp pattern is valid, i.e. compilable.
     * Displays an error message if the pattern is invalid.
     *
     * @param name Label name of the pattern.
     * @param regexp the regexp pattern.
     *
     * @throws ValidationException if the pattern is invalid.
     */
    private void checkRegexp(String name,
                             String regexp)
        throws ValidationException
    {
        try
        {
            _compiler.compile(regexp);
        }
        catch (MalformedPatternException ex)
        {
            JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(NamingPanel.this),
                                          "\"" + regexp +
                                          "\" is no valid regular expression",
                                          "Error: Invalid regexp for " + name,
                                          JOptionPane.ERROR_MESSAGE);
            throw new ValidationException("invalid regular expression");
        }
    }


    private JPanel createGeneralPane()
    {
        JPanel generalPanel = new JPanel();
        GridBagLayout generalLayout = new GridBagLayout();
        generalPanel.setLayout(generalLayout);

        JLabel packageLabel = new JLabel("Packages:");
        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalLayout.setConstraints(packageLabel, c);
        generalPanel.add(packageLabel);
        _packageTextField = new RegexpTextField(this.settings.get(
                                                               Keys.REGEXP_PACKAGE,
                                                               Defaults.REGEXP_PACKAGE));
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_packageTextField, c);
        generalPanel.add(_packageTextField);

        JLabel classLabel = new JLabel("Classes:");
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalLayout.setConstraints(classLabel, c);
        generalPanel.add(classLabel);
        _classTextField = new RegexpTextField(this.settings.get(Keys.REGEXP_CLASS,
                                                             Defaults.REGEXP_CLASS));
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_classTextField, c);
        generalPanel.add(_classTextField);

        JLabel abstractClassLabel = new JLabel("Abstract Classes:");
        c.insets.right = 10;
        SwingHelper.setConstraints(c, 0, 2, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalLayout.setConstraints(abstractClassLabel, c);
        generalPanel.add(abstractClassLabel);
        _abstractClassTextField = new RegexpTextField(this.settings.get(
                                                                     Keys.REGEXP_CLASS_ABSTRACT,
                                                                     Defaults.REGEXP_CLASS_ABSTRACT));
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_abstractClassTextField, c);
        generalPanel.add(_abstractClassTextField);

        JLabel interfaceLabel = new JLabel("Interfaces:");
        SwingHelper.setConstraints(c, 0, 3, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalLayout.setConstraints(interfaceLabel, c);
        generalPanel.add(interfaceLabel);
        _interfaceTextField = new RegexpTextField(this.settings.get(
                                                                 Keys.REGEXP_INTERFACE,
                                                                 Defaults.REGEXP_INTERFACE));
        SwingHelper.setConstraints(c, 1, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_interfaceTextField, c);
        generalPanel.add(_interfaceTextField);

        JLabel labelLabel = new JLabel("Labels:");
        SwingHelper.setConstraints(c, 0, 4, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        generalLayout.setConstraints(labelLabel, c);
        generalPanel.add(labelLabel);
        _labelTextField = new RegexpTextField(this.settings.get(Keys.REGEXP_LABEL,
                                                             Defaults.REGEXP_LABEL));
        SwingHelper.setConstraints(c, 1, 4, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_labelTextField, c);
        generalPanel.add(_labelTextField);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.bottom = 10;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(generalPanel, c);
        panel.add(generalPanel);

        return panel;
    }


    private JPanel createMethodsPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscPanelLayout = new GridBagLayout();
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Methods"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.bottom = 10;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);

        return panel;
    }


    private JPanel createVariablesPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscPanelLayout = new GridBagLayout();
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(BorderFactory.createCompoundBorder(
                                                               BorderFactory.createTitledBorder("Fields"),
                                                               BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _variablesData = new String[3][5];
        _variablesData[0][0] = "instance";
        _variablesData[0][1] = this.settings.get(Keys.REGEXP_FIELD_PUBLIC,
                                              Defaults.REGEXP_FIELD_PUBLIC);
        _variablesData[0][2] = this.settings.get(Keys.REGEXP_FIELD_PROTECTED,
                                              Defaults.REGEXP_FIELD_PROTECTED);
        _variablesData[0][3] = this.settings.get(Keys.REGEXP_FIELD_DEFAULT,
                                              Defaults.REGEXP_FIELD_DEFAULT);
        _variablesData[0][4] = this.settings.get(Keys.REGEXP_FIELD_PRIVATE,
                                              Defaults.REGEXP_FIELD_PRIVATE);
        _variablesData[1][0] = "static";
        _variablesData[1][1] = this.settings.get(Keys.REGEXP_FIELD_PUBLIC_STATIC,
                                              Defaults.REGEXP_FIELD_PUBLIC_STATIC);
        _variablesData[1][2] = this.settings.get(Keys.REGEXP_FIELD_PROTECTED_STATIC,
                                              Defaults.REGEXP_FIELD_PROTECTED_STATIC);
        _variablesData[1][3] = this.settings.get(Keys.REGEXP_FIELD_DEFAULT_STATIC,
                                              Defaults.REGEXP_FIELD_DEFAULT_STATIC);
        _variablesData[1][4] = this.settings.get(Keys.REGEXP_FIELD_PRIVATE_STATIC,
                                              Defaults.REGEXP_FIELD_PRIVATE_STATIC);
        _variablesData[2][0] = "static final";
        _variablesData[2][1] = this.settings.get(Keys.REGEXP_FIELD_PUBLIC_STATIC_FINAL,
                                              Defaults.REGEXP_FIELD_PUBLIC_STATIC_FINAL);
        _variablesData[2][2] = this.settings.get(Keys.REGEXP_FIELD_PROTECTED_STATIC_FINAL,
                                              Defaults.REGEXP_FIELD_PROTECTED_STATIC_FINAL);
        _variablesData[2][3] = this.settings.get(Keys.REGEXP_FIELD_DEFAULT_STATIC_FINAL,
                                              Defaults.REGEXP_FIELD_DEFAULT_STATIC_FINAL);
        _variablesData[2][4] = this.settings.get(Keys.REGEXP_FIELD_PRIVATE_STATIC_FINAL,
                                              Defaults.REGEXP_FIELD_PRIVATE_STATIC_FINAL);

        String[] names =
        {
            "            ", "public", "protected", "default", "private"
        };
        TableModel model = new TableModel(_variablesData, names);
        JTable table = new JTable(model);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setDefaultEditor(String.class, new StringEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport()
                  .setBackground((Color)UIManager.get("Table.background"));

        int height = (table.getRowHeight() * (table.getRowCount() + 1)) +
                     (table.getRowMargin() * (table.getRowCount() + 1));
        scrollPane.setMinimumSize(new Dimension(200, height));
        scrollPane.setMinimumSize(new Dimension(300, height + 16));

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        miscPanelLayout.setConstraints(scrollPane, c);
        miscPanel.add(scrollPane);

        JPanel variablePanel = new JPanel();
        GridBagLayout variableLayout = new GridBagLayout();
        variablePanel.setLayout(variableLayout);
        variablePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                   BorderFactory.createTitledBorder("Local variables"),
                                                                   BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JLabel variableLabel = new JLabel("Variables:");
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        variableLayout.setConstraints(variableLabel, c);
        variablePanel.add(variableLabel);

        RegexpTextField _localVariableTextField = new RegexpTextField(this.settings.get(
                                                                                     Keys.REGEXP_LOCAL_VARIABLE,
                                                                                     Defaults.REGEXP_LOCAL_VARIABLE));
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        variableLayout.setConstraints(_localVariableTextField, c);
        variablePanel.add(_localVariableTextField);

        JPanel paramPanel = new JPanel();
        GridBagLayout paramLayout = new GridBagLayout();
        paramPanel.setLayout(paramLayout);
        paramPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Parameters"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JLabel paramLabel = new JLabel("Parameters:");
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        paramLayout.setConstraints(paramLabel, c);
        paramPanel.add(paramLabel);

        RegexpTextField _paramTextField = new RegexpTextField(this.settings.get(
                                                                             Keys.REGEXP_PARAM,
                                                                             Defaults.REGEXP_PARAM));
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        paramLayout.setConstraints(_paramTextField, c);
        paramPanel.add(_paramTextField);

        JLabel finalParamLabel = new JLabel("Final Parameters:");
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        paramLayout.setConstraints(finalParamLabel, c);
        paramPanel.add(finalParamLabel);

        RegexpTextField _finalParamTextField = new RegexpTextField(this.settings.get(
                                                                                  Keys.REGEXP_PARAM,
                                                                                  Defaults.REGEXP_PARAM));
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        paramLayout.setConstraints(_finalParamTextField, c);
        paramPanel.add(_finalParamTextField);

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
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(variablePanel, c);
        panel.add(variablePanel);
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(paramPanel, c);
        panel.add(paramPanel);

        return panel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add(createGeneralPane(), "General");
        tabbedPane.add(createVariablesPane(), "Variables");
        tabbedPane.add(createMethodsPane(), "Methods");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(tabbedPane, BorderLayout.CENTER);
    }

    //~ Inner Classes ·························································

    private static class StringEditor
        extends DefaultCellEditor
    {
        public StringEditor()
        {
            super(new RegexpTextField());

            RegexpTextField component = (RegexpTextField)getComponent();
            component.setCellEditor(this);
        }
    }


    private static class TableModel
        extends AbstractTableModel
    {
        String[] columnNames;
        String[][] data;

        public TableModel(String[][] data,
                          String[]   names)
        {
            this.data = data;
            this.columnNames = names;
        }

        public boolean isCellEditable(int row,
                                      int col)
        {
            if (col < 1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }


        public Class getColumnClass(int c)
        {
            return String.class;
        }


        public int getColumnCount()
        {
            return this.columnNames.length;
        }


        public String getColumnName(int col)
        {
            return this.columnNames[col];
        }


        public int getRowCount()
        {
            return this.data.length;
        }


        public void setValueAt(Object value,
                               int    row,
                               int    col)
        {
            this.data[row][col] = (String)value;
            fireTableCellUpdated(row, col);
        }


        public Object getValueAt(int row,
                                 int col)
        {
            return this.data[row][col];
        }
    }
}
