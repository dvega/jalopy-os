/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.swing.util.SwingHelper;

import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;


/**
 * Settings page for the Jalopy Code Inspector naming settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class NamingSettingsPage
    extends AbstractSettingsPage
{
    //~ Instance variables ---------------------------------------------------------------

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

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new NamingSettingsPage object.
     */
    public NamingSettingsPage()
    {
        initialize();
    }


    /**
     * Creates a new NamingSettingsPage.
     *
     * @param container the parent container.
     */
    NamingSettingsPage(SettingsContainer container)
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
        this.settings.put(ConventionKeys.REGEXP_PACKAGE, _packageTextField.getText());
        this.settings.put(ConventionKeys.REGEXP_CLASS, _classTextField.getText());
        this.settings.put(
            ConventionKeys.REGEXP_CLASS_ABSTRACT, _abstractClassTextField.getText());
        this.settings.put(
            ConventionKeys.REGEXP_INTERFACE, _interfaceTextField.getText());
        this.settings.put(ConventionKeys.REGEXP_LABEL, _labelTextField.getText());
        this.settings.put(ConventionKeys.REGEXP_FIELD_PUBLIC, _variablesData[0][1]);
        this.settings.put(ConventionKeys.REGEXP_FIELD_PROTECTED, _variablesData[0][2]);
        this.settings.put(ConventionKeys.REGEXP_FIELD_DEFAULT, _variablesData[0][3]);
        this.settings.put(ConventionKeys.REGEXP_FIELD_PRIVATE, _variablesData[0][4]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PUBLIC_STATIC, _variablesData[1][1]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PROTECTED_STATIC, _variablesData[1][2]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_DEFAULT_STATIC, _variablesData[1][3]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PRIVATE_STATIC, _variablesData[1][4]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PUBLIC_STATIC_FINAL, _variablesData[2][1]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PROTECTED_STATIC_FINAL, _variablesData[2][2]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_DEFAULT_STATIC_FINAL, _variablesData[2][3]);
        this.settings.put(
            ConventionKeys.REGEXP_FIELD_PRIVATE_STATIC_FINAL, _variablesData[2][4]);
    }


    private JPanel createGeneralPane()
    {
        JPanel generalPanel = new JPanel();
        GridBagLayout generalLayout = new GridBagLayout();
        generalPanel.setLayout(generalLayout);

        GridBagConstraints c = new GridBagConstraints();

        JLabel packageLabel =
            new JLabel(this.bundle.getString("LBL_PACKAGES" /* NOI18N */));
        SwingHelper.setConstraints(
            c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        generalLayout.setConstraints(packageLabel, c);
        generalPanel.add(packageLabel);
        _packageTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_PACKAGE, ConventionDefaults.REGEXP_PACKAGE));
        SwingHelper.setConstraints(
            c, 1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_packageTextField, c);
        generalPanel.add(_packageTextField);

        JLabel classLabel = new JLabel(this.bundle.getString("LBL_CLASSES" /* NOI18N */));
        SwingHelper.setConstraints(
            c, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        generalLayout.setConstraints(classLabel, c);
        generalPanel.add(classLabel);
        _classTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_CLASS, ConventionDefaults.REGEXP_CLASS));
        SwingHelper.setConstraints(
            c, 1, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_classTextField, c);
        generalPanel.add(_classTextField);

        JLabel abstractClassLabel =
            new JLabel(this.bundle.getString("LBL_CLASSES_ABSTRACT" /* NOI18N */));
        c.insets.right = 10;
        SwingHelper.setConstraints(
            c, 0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        generalLayout.setConstraints(abstractClassLabel, c);
        generalPanel.add(abstractClassLabel);

        _abstractClassTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_CLASS_ABSTRACT,
                    ConventionDefaults.REGEXP_CLASS_ABSTRACT));
        c.insets.right = 0;
        SwingHelper.setConstraints(
            c, 1, 2, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_abstractClassTextField, c);
        generalPanel.add(_abstractClassTextField);

        JLabel interfaceLabel =
            new JLabel(this.bundle.getString("LBL_INTERFACES" /* NOI18N */));
        SwingHelper.setConstraints(
            c, 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        generalLayout.setConstraints(interfaceLabel, c);
        generalPanel.add(interfaceLabel);
        _interfaceTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_INTERFACE, ConventionDefaults.REGEXP_INTERFACE));
        SwingHelper.setConstraints(
            c, 1, 3, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        generalLayout.setConstraints(_interfaceTextField, c);
        generalPanel.add(_interfaceTextField);

        JLabel labelLabel = new JLabel(this.bundle.getString("LBL_LABELS" /* NOI18N */));
        SwingHelper.setConstraints(
            c, 0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        generalLayout.setConstraints(labelLabel, c);
        generalPanel.add(labelLabel);

        _labelTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_LABEL, ConventionDefaults.REGEXP_LABEL));
        SwingHelper.setConstraints(
            c, 1, 4, GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.WEST,
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
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(generalPanel, c);
        panel.add(generalPanel);

        return panel;
    }


    private JPanel createMethodsPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscPanelLayout = new GridBagLayout();
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_METHODS" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.bottom = 10;
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);

        return panel;
    }


    private JPanel createVariablesPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscPanelLayout = new GridBagLayout();
        miscPanel.setLayout(miscPanelLayout);
        miscPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_FIELDS" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        _variablesData = new String[3][5];
        _variablesData[0][0] = "instance" /* NOI18N */;
        _variablesData[0][1] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PUBLIC, ConventionDefaults.REGEXP_FIELD_PUBLIC);
        _variablesData[0][2] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PROTECTED,
                ConventionDefaults.REGEXP_FIELD_PROTECTED);
        _variablesData[0][3] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_DEFAULT,
                ConventionDefaults.REGEXP_FIELD_DEFAULT);
        _variablesData[0][4] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PRIVATE,
                ConventionDefaults.REGEXP_FIELD_PRIVATE);
        _variablesData[1][0] = "static" /* NOI18N */;
        _variablesData[1][1] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PUBLIC_STATIC,
                ConventionDefaults.REGEXP_FIELD_PUBLIC_STATIC);
        _variablesData[1][2] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PROTECTED_STATIC,
                ConventionDefaults.REGEXP_FIELD_PROTECTED_STATIC);
        _variablesData[1][3] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_DEFAULT_STATIC,
                ConventionDefaults.REGEXP_FIELD_DEFAULT_STATIC);
        _variablesData[1][4] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PRIVATE_STATIC,
                ConventionDefaults.REGEXP_FIELD_PRIVATE_STATIC);
        _variablesData[2][0] = "static final" /* NOI18N */;
        _variablesData[2][1] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PUBLIC_STATIC_FINAL,
                ConventionDefaults.REGEXP_FIELD_PUBLIC_STATIC_FINAL);
        _variablesData[2][2] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PROTECTED_STATIC_FINAL,
                ConventionDefaults.REGEXP_FIELD_PROTECTED_STATIC_FINAL);
        _variablesData[2][3] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_DEFAULT_STATIC_FINAL,
                ConventionDefaults.REGEXP_FIELD_DEFAULT_STATIC_FINAL);
        _variablesData[2][4] =
            this.settings.get(
                ConventionKeys.REGEXP_FIELD_PRIVATE_STATIC_FINAL,
                ConventionDefaults.REGEXP_FIELD_PRIVATE_STATIC_FINAL);

        String[] names =
        {
            "            " /* NOI18N */, "public" /* NOI18N */, "protected" /* NOI18N */,
            "default" /* NOI18N */, "private" /* NOI18N */
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
        scrollPane.getViewport().setBackground(
            UIManager.getColor("Table.background" /* NOI18N */));

        int height =
            (table.getRowHeight() * (table.getRowCount() + 1))
            + (table.getRowMargin() * (table.getRowCount() + 1));
        scrollPane.setMinimumSize(new Dimension(200, height));
        scrollPane.setMinimumSize(new Dimension(300, height + 16));

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 5, 5);
        miscPanelLayout.setConstraints(scrollPane, c);
        miscPanel.add(scrollPane);

        JPanel variablePanel = new JPanel();
        GridBagLayout variableLayout = new GridBagLayout();
        variablePanel.setLayout(variableLayout);
        variablePanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_LOCAL_VARS" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JLabel variableLabel = new JLabel(this.bundle.getString("LBL_VARS" /* NOI18N */));
        c.insets.right = 5;
        SwingHelper.setConstraints(
            c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        variableLayout.setConstraints(variableLabel, c);
        variablePanel.add(variableLabel);

        RegexpTextField _localVariableTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_LOCAL_VARIABLE,
                    ConventionDefaults.REGEXP_LOCAL_VARIABLE));
        c.insets.right = 0;
        SwingHelper.setConstraints(
            c, 1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        variableLayout.setConstraints(_localVariableTextField, c);
        variablePanel.add(_localVariableTextField);

        JPanel paramPanel = new JPanel();
        GridBagLayout paramLayout = new GridBagLayout();
        paramPanel.setLayout(paramLayout);
        paramPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    this.bundle.getString("BDR_PARAMS" /* NOI18N */)),
                BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        JLabel paramLabel = new JLabel(this.bundle.getString("LBL_PARAMS" /* NOI18N */));
        SwingHelper.setConstraints(
            c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        paramLayout.setConstraints(paramLabel, c);
        paramPanel.add(paramLabel);

        RegexpTextField _paramTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_PARAM, ConventionDefaults.REGEXP_PARAM));
        SwingHelper.setConstraints(
            c, 1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
            GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        paramLayout.setConstraints(_paramTextField, c);
        paramPanel.add(_paramTextField);

        JLabel finalParamLabel =
            new JLabel(this.bundle.getString("LBL_PARAMS_FINAL" /* NOI18N */));
        c.insets.right = 5;
        SwingHelper.setConstraints(
            c, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            c.insets, 0, 0);
        paramLayout.setConstraints(finalParamLabel, c);
        paramPanel.add(finalParamLabel);

        RegexpTextField _finalParamTextField =
            new RegexpTextField(
                this.settings.get(
                    ConventionKeys.REGEXP_PARAM, ConventionDefaults.REGEXP_PARAM));
        c.insets.right = 0;
        SwingHelper.setConstraints(
            c, 1, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.WEST,
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
        SwingHelper.setConstraints(
            c, 0, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(miscPanel, c);
        panel.add(miscPanel);
        c.insets.top = 0;
        SwingHelper.setConstraints(
            c, 0, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(variablePanel, c);
        panel.add(variablePanel);
        SwingHelper.setConstraints(
            c, 0, 2, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
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
        tabbedPane.add(
            createGeneralPane(), this.bundle.getString("TAB_GENERAL" /* NOI18N */));
        tabbedPane.add(
            createVariablesPane(), this.bundle.getString("TAB_VARS" /* NOI18N */));
        tabbedPane.add(
            createMethodsPane(), this.bundle.getString("TAB_METHODS" /* NOI18N */));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(tabbedPane, BorderLayout.CENTER);
    }

    //~ Inner Classes --------------------------------------------------------------------

    private static class StringEditor
        extends DefaultCellEditor
    {
        public StringEditor()
        {
            super(new RegexpTextField());

            RegexpTextField component = (RegexpTextField) getComponent();
            component.setCellEditor(this);
        }
    }


    private static class TableModel
        extends AbstractTableModel
    {
        String[] columnNames;
        String[][] data;

        public TableModel(
            String[][] data,
            String[]   names)
        {
            this.data = data;
            this.columnNames = names;
        }

        public boolean isCellEditable(
            int row,
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


        public void setValueAt(
            Object value,
            int    row,
            int    col)
        {
            this.data[row][col] = (String) value;
            fireTableCellUpdated(row, col);
        }


        public Object getValueAt(
            int row,
            int col)
        {
            return this.data[row][col];
        }
    }
}
