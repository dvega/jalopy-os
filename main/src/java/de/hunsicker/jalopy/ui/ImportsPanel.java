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
import de.hunsicker.jalopy.storage.ImportPolicy;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.EmptyButtonGroup;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.oro.text.perl.Perl5Util;


/**
 * A component that can be used to display/edit the Jalopy package import
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ImportsPanel
    extends AbstractSettingsPanel
{
    //~ Static variables/initializers ·········································

    /** The delimeter for the value pair of an entry. */
    private static final String DELIMETER_ENTRY_PAIR = ":";

    /** The delimeter for the different entries. */
    private static final String DELIMETER_ENTRY = "|";
    private static final String STAR = "*";

    //~ Instance variables ····················································

    /** The grouping entries. */
    private DefaultTableModel _tableModel;

    /** Enables/disables import statements collapsing. */
    private JCheckBox _collapseCheckBox;

    /** Enables/disables import statements expanding. */
    private JCheckBox _expandCheckBox;

    /** Enables/disables the grouping functionality. */
    private JCheckBox _sortImportsCheckBox;

    /** Specifies the grouping depth. */
    private JComboBox _groupingDepthComboBox;

    /** The table which displays the grouping entries. */
    private JTable _table;
    private boolean _selectionAllowed = true;

    //~ Constructors ··························································

    /**
     * Creates a new ImportsPanel.
     */
    public ImportsPanel()
    {
        initialize();
    }


    /**
     * Creates a new ImportsPanel.
     *
     * @param container the parent container.
     */
    ImportsPanel(SettingsContainer container)
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
        if (_expandCheckBox.isSelected())
        {
            this.settings.put(Keys.IMPORT_POLICY, ImportPolicy.EXPAND.toString());
        }
        else if (_collapseCheckBox.isSelected())
        {
            this.settings.put(Keys.IMPORT_POLICY, ImportPolicy.COLLAPSE.toString());
        }
        else
        {
            this.settings.put(Keys.IMPORT_POLICY, ImportPolicy.DISABLED.toString());
        }

        this.settings.putBoolean(Keys.IMPORT_SORT,
                              _sortImportsCheckBox.isSelected());
        this.settings.put(Keys.IMPORT_GROUPING_DEPTH,
                       (String)_groupingDepthComboBox.getSelectedItem());

        List values = new ArrayList(_tableModel.getRowCount());

        for (int i = 0; i < _tableModel.getRowCount(); i++)
        {
            String name = (String)_tableModel.getValueAt(i, 0);

            // if the user added a column but never inserted values
            // we ignore the row
            if ((name == null) || (name.length() == 0))
            {
                continue;
            }

            Integer depth = (Integer)_tableModel.getValueAt(i, 1);

            // the user specified only a name
            if (depth == null)
            {
                continue;
            }

            ListEntry entry = new ListEntry(name, depth.toString());
            values.add(entry);
        }

        this.settings.put(Keys.IMPORT_GROUPING, encodeGroupingInfo(values));
    }


    /**
     * {@inheritDoc}
     */
    public void validateSettings()
        throws ValidationException
    {
        if (_table.isEditing())
        {
            DefaultCellEditor cellEditor = (DefaultCellEditor)_table.getCellEditor();

            if (!cellEditor.stopCellEditing())
            {
                Component cell = cellEditor.getComponent();
                cell.requestFocus();

                int column = _table.getEditingColumn();

                JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this),
                                              "Finish editing " +
                                              ((column == 0)
                                                   ? "package name"
                                                   : "grouping depth") +
                                              " in column " + (column + 1) +
                                              ", row " +
                                              (_table.getEditingRow() + 1),
                                              "Error: Finish cell editing",
                                              JOptionPane.ERROR_MESSAGE);

                throw new ValidationException("missing data");
            }
        }
    }


    private JPanel createGroupingPane()
    {
        JPanel sortPanel = new JPanel();
        GridBagLayout sortLayout = new GridBagLayout();
        sortPanel.setLayout(sortLayout);
        sortPanel.setBorder(BorderFactory.createTitledBorder("Sorting"));
        _sortImportsCheckBox = new JCheckBox("Sort imports",
                                             this.settings.getBoolean(
                                                                   Keys.IMPORT_SORT,
                                                                   Defaults.IMPORT_SORT));
        _sortImportsCheckBox.addActionListener(this.trigger);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 0;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        sortLayout.setConstraints(_sortImportsCheckBox, c);
        sortPanel.add(_sortImportsCheckBox);

        Object[] depths ={ "0", "1", "2", "3", "4", "5" };
        NumberComboBoxPanel groupingDepthPanel = new NumberComboBoxPanel("Default grouping depth:",
                                                                         depths,
                                                                         String.valueOf(this.settings.getInt(
                                                                                                          Keys.IMPORT_GROUPING_DEPTH,
                                                                                                          Defaults.IMPORT_GROUPING_DEPTH)));
        _groupingDepthComboBox = groupingDepthPanel.getComboBox();
        _groupingDepthComboBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        sortLayout.setConstraints(groupingDepthPanel, c);
        sortPanel.add(groupingDepthPanel);

        List info = decodeGroupingInfo(this.settings.get(Keys.IMPORT_GROUPING,
                                                      Defaults.IMPORT_GROUPING));
        int rows = info.size();
        Object[][] data = new Object[rows][2];
        int index = 0;

        for (int i = 0, size = info.size(); i < size; i++)
        {
            ListEntry entry = (ListEntry)info.get(i);

            data[index][0] = entry.name;
            data[index][1] = new Integer(entry.depth);
            index++;
        }

        Object[] columnNames ={ "Package", "Depth" };
        _tableModel = new DataModel(data, columnNames);
        _tableModel.addTableModelListener(new TableModelListener()
            {
                public void tableChanged(TableModelEvent ev)
                {
                    // we only handle the DELETE event here, because otherwise
                    // validateSettings() would be called before cell editing
                    // is finished, resulting in error messages
                    if (ev.getType() == TableModelEvent.DELETE)
                    {
                        trigger.actionPerformed(null);
                    }
                }
            });

        final TableList tableList = new TableList(_tableModel,
                                                  TableList.TYPE_BOTH);
        _table = tableList.getTable();
        _table.setCellSelectionEnabled(true);
        _table.setColumnSelectionAllowed(true);
        _table.setRowSelectionAllowed(true);
        _table.setDefaultRenderer(Object.class, new BasicTableCellRenderer());
        _table.setDefaultRenderer(Integer.class,
                                  new BasicNumberTableCellRenderer());
        _table.setDefaultEditor(String.class, new StringEditor(tableList));
        _table.setDefaultEditor(Integer.class, new IntegerEditor());
        _table.setEnabled(_sortImportsCheckBox.isSelected());

        /*_table.addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent ev)
            {
                if (_table.getEditingRow() == -1)
                {
                    //System.err.println(ev);
                    //_table.clearSelection();
                }
            }
            }

        );*/
        _sortImportsCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // invoked first as the selection listener changes the
                    // state of the buttons too
                    _table.clearSelection();

                    if (_sortImportsCheckBox.isSelected())
                    {
                        _table.setEnabled(true);
                        tableList.addButton.setEnabled(true);
                        _groupingDepthComboBox.setEnabled(true);
                    }
                    else
                    {
                        _table.setEnabled(false);
                        tableList.addButton.setEnabled(false);
                        tableList.removeButton.setEnabled(false);
                        _groupingDepthComboBox.setEnabled(false);
                    }
                }
            });

        CellEditorListener cellListener = new CellEditorListener()
        {
            public void editingCanceled(ChangeEvent ev)
            {
                if (_table.isEditing())
                {
                    return;
                }

                tableList.addButton.setEnabled(true);
                tableList.removeButton.setEnabled(true);
            }


            public void editingStopped(ChangeEvent ev)
            {
                if (_table.isEditing())
                {
                    return;
                }

                tableList.addButton.setEnabled(true);
                tableList.removeButton.setEnabled(true);
            }
        };

        _table.getDefaultEditor(String.class)
              .addCellEditorListener(cellListener);
        _table.getDefaultEditor(Integer.class)
              .addCellEditorListener(cellListener);

        ListSelectionListener selectionListener = new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent ev)
            {
                // ignore extra messages
                if (ev.getValueIsAdjusting())
                {
                    return;
                }

                ListSelectionModel model = (ListSelectionModel)ev.getSource();
                int selectedRow = model.getMinSelectionIndex();

                if (selectedRow > -1)
                {
                    String name = (String)_tableModel.getValueAt(selectedRow, 0);

                    if (STAR.equals(name))
                    {
                        tableList.removeButton.setEnabled(false);
                    }
                }
            }
        };

        updateSelectionModel(selectionListener);

        JPanel tablePanel = new JPanel();
        GridBagLayout tableLayout = new GridBagLayout();
        tablePanel.setLayout(tableLayout);
        c.insets.left = 0;
        c.insets.right = 0;
        c.insets.top = 10;
        c.insets.bottom = 5;
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.NORTH,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        tableLayout.setConstraints(tableList, c);
        tablePanel.add(tableList);

        c.insets.bottom = 2;
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        tableLayout.setConstraints(tableList.addButton, c);
        tablePanel.add(tableList.addButton);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        tableList.removeButton.setEnabled(false);
        tableLayout.setConstraints(tableList.removeButton, c);
        tablePanel.add(tableList.removeButton);

        c.insets.top = 10;
        c.insets.bottom = 2;
        SwingHelper.setConstraints(c, 9, 3, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        tableList.upButton.setEnabled(false);
        tableLayout.setConstraints(tableList.upButton, c);
        tablePanel.add(tableList.upButton);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 4, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        tableList.downButton.setEnabled(false);
        tableLayout.setConstraints(tableList.downButton, c);
        tablePanel.add(tableList.downButton);

        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 0;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        sortLayout.setConstraints(tablePanel, c);
        sortPanel.add(tablePanel);

        return sortPanel;
    }


    private JPanel createMiscPane()
    {
        JPanel miscPanel = new JPanel();
        GridBagLayout miscLayout = new GridBagLayout();
        miscPanel.setLayout(miscLayout);
        miscPanel.setBorder(BorderFactory.createTitledBorder("Optimize"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 0;
        c.insets.bottom = 0;

        ImportPolicy importPolicy = ImportPolicy.valueOf(this.settings.get(
                                                                        Keys.IMPORT_POLICY,
                                                                        Defaults.IMPORT_POLICY));
        _expandCheckBox = new JCheckBox("Expand on-demand imports",
                                        importPolicy == ImportPolicy.EXPAND);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscLayout.setConstraints(_expandCheckBox, c);
        miscPanel.add(_expandCheckBox);
        c.insets.bottom = 5;
        _collapseCheckBox = new JCheckBox("Collapse single-type imports",
                                          importPolicy == ImportPolicy.COLLAPSE);
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        miscLayout.setConstraints(_collapseCheckBox, c);
        miscPanel.add(_collapseCheckBox);

        ButtonGroup group = new EmptyButtonGroup();
        group.add(_expandCheckBox);
        group.add(_collapseCheckBox);

        return miscPanel;
    }


    /**
     * Decodes the grouping info found in the given string.
     *
     * @param info string with encoded grouping info.
     *
     * @return a list with the grouping info entries.
     */
    private List decodeGroupingInfo(String info)
    {
        List result = new ArrayList();

        for (StringTokenizer tokens = new StringTokenizer(info, DELIMETER_ENTRY);
             tokens.hasMoreElements();)
        {
            String pair = tokens.nextToken();
            int delimOffset = pair.indexOf(':');

            String name = pair.substring(0, delimOffset);
            String depth = pair.substring(delimOffset + 1);

            result.add(new ListEntry(name, depth));
        }

        return result;
    }


    private String encodeGroupingInfo(List info)
    {
        StringBuffer result = new StringBuffer(60);

        for (int i = 0, size = info.size(); i < size; i++)
        {
            ListEntry entry = (ListEntry)info.get(i);
            result.append(entry.name);
            result.append(DELIMETER_ENTRY_PAIR);
            result.append(entry.depth);
            result.append(DELIMETER_ENTRY);
        }

        if (result.length() > 1)
        {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        c.insets.left = 0;
        c.insets.right = 0;
        c.insets.top = 10;
        c.insets.bottom = 10;

        JPanel sortPanel = createGroupingPane();
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.2, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(sortPanel, c);
        add(sortPanel);

        JPanel miscPanel = createMiscPane();
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(miscPanel, c);
        add(miscPanel);
    }


    /**
     * Updates the selection model of the table list with a custom model.
     *
     * @param selectionListener custom selection listener.
     */
    private void updateSelectionModel(ListSelectionListener selectionListener)
    {
        CellSelectionModel model = new CellSelectionModel();

        DefaultListSelectionModel listSelectionModel = (DefaultListSelectionModel)_table.getSelectionModel();

        _table.setSelectionModel(model);

        EventListener[] listeners = (EventListener[])listSelectionModel.getListeners(ListSelectionListener.class)
                                                                       .clone();

        for (int i = 0; i < listeners.length; i++)
        {
            listSelectionModel.removeListSelectionListener((ListSelectionListener)listeners[i]);
        }

        // add the custom listener first, so it will be notified last
        model.addListSelectionListener(selectionListener);

        for (int i = 0; i < listeners.length; i++)
        {
            model.addListSelectionListener((ListSelectionListener)listeners[i]);
        }
    }

    //~ Inner Classes ·························································

    /**
     * Holds the data to be displayed in the table.
     */
    private static class DataModel
        extends DefaultTableModel
    {
        public DataModel(Object[][] data,
                         Object[]   columnNames)
        {
            super(data, columnNames);
        }

        public Class getColumnClass(int columnIndex)
        {
            switch (columnIndex)
            {
                case 1 :
                    return Integer.class;

                case 0 :
                    return String.class;

                default :
                    throw new IllegalArgumentException("invalid column -- " +
                                                       columnIndex);
            }
        }
    }


    private static final class ListEntry
    {
        String depth;
        String name;

        public ListEntry(String name,
                         String depth)
        {
            this.name = name;
            this.depth = depth;
        }
    }


    private class CellSelectionModel
        extends DefaultListSelectionModel
    {
        public CellSelectionModel()
        {
            setSelectionMode(SINGLE_SELECTION);
        }

        public void setAnchorSelectionIndex(int index)
        {
            if (_selectionAllowed)
            {
                super.setAnchorSelectionIndex(index);
            }
        }


        public void setLeadSelectionIndex(int index)
        {
            if (_selectionAllowed)
            {
                super.setLeadSelectionIndex(index);
            }
        }


        public void setSelectionInterval(int index0,
                                         int index1)
        {
            if (_selectionAllowed)
            {
                super.setSelectionInterval(index0, index1);
            }
        }


        public void addSelectionInterval(int index0,
                                         int index1)
        {
            if (_selectionAllowed)
            {
                super.addSelectionInterval(index0, index1);
            }
        }
    }


    /**
     * Table cell editor for the grouping depth values. Performs input
     * checking.
     */
    private class IntegerEditor
        extends DefaultCellEditor
    {
        public IntegerEditor()
        {
            super(new JTextField());
            ((JTextField)getComponent()).setHorizontalAlignment(JTextField.RIGHT);
        }

        public boolean isCellEditable(EventObject ev)
        {
            boolean editable = super.isCellEditable(ev);

            if (editable)
            {
                // disable editing for the '*' row
                if (STAR.equals(_table.getValueAt(_table.getSelectedRow(), 0)))
                {
                    return false;
                }
            }

            return editable;
        }


        public Object getCellEditorValue()
        {
            String value = (String)super.getCellEditorValue();

            if ((value == null) || (value.trim().length() == 0))
            {
                return "";
            }

            return new Integer((String)value);
        }


        public Component getTableCellEditorComponent(JTable  table,
                                                     Object  value,
                                                     boolean isSelected,
                                                     int     row,
                                                     int     column)
        {
            ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));

            return super.getTableCellEditorComponent(table, value, isSelected,
                                                     row, column);
        }


        public boolean stopCellEditing()
        {
            String s = (String)super.getCellEditorValue();

            // disallow empty strings
            if ("".equals(s))
            {
                ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.red));

                return false;
            }

            try
            {
                Integer value = new Integer(s);

                if (value.intValue() < 1)
                {
                    ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.red));

                    return false;
                }
            }
            catch (NumberFormatException ex)
            {
                ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.red));

                return false;
            }

            boolean successful = super.stopCellEditing();

            if (successful)
            {
                // update the preview
                ImportsPanel.this.trigger.actionPerformed(null);
            }

            return successful;
        }
    }


    /**
     * Table cell editor for the import declaration names. Performs input
     * checking.
     */
    private class StringEditor
        extends DefaultCellEditor
    {
        TableList tableList;
        boolean add;
        boolean down;
        boolean remove;
        boolean up;

        public StringEditor(TableList tableList)
        {
            super(new JTextField());
            this.tableList = tableList;
        }

        public boolean isCellEditable(EventObject ev)
        {
            boolean editable = super.isCellEditable(ev);

            if (editable)
            {
                this.up = this.tableList.upButton.isEnabled();
                this.down = this.tableList.downButton.isEnabled();
                this.add = this.tableList.addButton.isEnabled();
                this.remove = this.tableList.removeButton.isEnabled();

                // disable editing for the '*' row
                if (STAR.equals(_table.getValueAt(_table.getSelectedRow(), 0)))
                {
                    return false;
                }
            }

            return editable;
        }


        public Object getCellEditorValue()
        {
            return ((String)super.getCellEditorValue()).trim();
        }


        public Component getTableCellEditorComponent(JTable  table,
                                                     Object  value,
                                                     boolean isSelected,
                                                     int     row,
                                                     int     column)
        {
            ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.black));

            return super.getTableCellEditorComponent(table, value, isSelected,
                                                     row, column);
        }


        public boolean stopCellEditing()
        {
            String s = (String)super.getCellEditorValue();
            Perl5Util regex = new Perl5Util();

            // only allow package/type names
            if (!regex.match("m/^[a-zA-Z]+(?:.[a-zA-Z]+)*$|\\*/s", s))
            {
                ((JComponent)getComponent()).setBorder(BorderFactory.createLineBorder(Color.red));

                this.tableList.upButton.setEnabled(false);
                this.tableList.downButton.setEnabled(false);
                this.tableList.addButton.setEnabled(false);
                this.tableList.removeButton.setEnabled(false);

                _selectionAllowed = false;

                return false;
            }

            _selectionAllowed = true;
            this.tableList.upButton.setEnabled(this.up);
            this.tableList.downButton.setEnabled(this.down);
            this.tableList.addButton.setEnabled(this.add);

            this.tableList.removeButton.setEnabled(this.remove);

            int row = _table.getEditingRow();
            int column = _table.getEditingColumn() + 1;
            boolean successful = super.stopCellEditing();

            if (successful)
            {
                // switch to the grouping depth cell to force valid data input
                if (_table.getValueAt(row, column) == null)
                {
                    _table.editCellAt(row, column);

                    DefaultCellEditor cellEditor = (DefaultCellEditor)_table.getCellEditor();
                    Component cell = (Component)cellEditor.getComponent();
                    cell.requestFocus();
                }
                else
                {
                    // update the preview
                    ImportsPanel.this.trigger.actionPerformed(null);
                }
            }

            return successful;
        }
    }
}
