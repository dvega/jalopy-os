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

import de.hunsicker.jalopy.parser.Type;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;


/**
 * A visual, kind of list-like component.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class TableList
    extends JPanel
{
    //~ Static variables/initializers ·········································

    /** List with ADD/REMOVE buttons. */
    public static final int TYPE_ADD_REMOVE = 1;

    /** List with UP/DOWN buttons. */
    public static final int TYPE_UP_DOWN = 2;

    /** DOCUMENT ME! */
    public static final int TYPE_BOTH = 3;

    //~ Instance variables ····················································

    /** Button to add items. */
    JButton addButton;

    /** Button to move an item down the list. */
    JButton downButton;

    /** Button to remove items. */
    JButton removeButton;

    /** Button to move an entry up the list. */
    JButton upButton;

    /** The used table model. */
    private DefaultTableModel _tableModel;

    /** The scroller. */
    private JScrollPane _scrollPane;

    /** Used table. */
    private JTable _table;

    /** Holds the constraints for the down action. */
    private List _downContraints; // List of <Constraint>

    /** Holds the constraints for the up action. */
    private List _upConstraints; // List of <Constraint>
    private int _type;

    //~ Constructors ··························································

    /**
     * Creates a new TableList object.
     *
     * @param model the table model.
     * @param type the type of the list.
     */
    public TableList(DefaultTableModel model,
                     int               type)
    {
        _tableModel = model;
        _table = new JTable(_tableModel);
        _table.getTableHeader().setResizingAllowed(false);
        _table.getTableHeader().setReorderingAllowed(false);
        _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _table.setCellSelectionEnabled(false);
        _table.setColumnSelectionAllowed(false);
        _table.setRowSelectionAllowed(true);
        _table.setDefaultRenderer(Type.class, new TypeRenderer());
        _table.setDefaultRenderer(Boolean.class, new BooleanRenderer());
        initializeColumnSizes(_table);
        _scrollPane = new JScrollPane(_table);
        _scrollPane.getViewport()
                   .setBackground((Color)UIManager.get("Table.background"));

        int height = SwingHelper.getTableHeight(_table);
        _scrollPane.setMinimumSize(new Dimension(300, height));
        _scrollPane.setPreferredSize(new Dimension(300, height + 17));
        _scrollPane.setMaximumSize(new Dimension(300, height + 17));

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(_scrollPane, c);
        add(_scrollPane);

        _type = type;

        switch (type)
        {
            case TYPE_ADD_REMOVE :
                initializeAddRemove();

                break;

            case TYPE_UP_DOWN :
                initializeUpDown();

                break;

            case TYPE_BOTH :
                initializeAddRemove();
                initializeUpDown();

                break;
        }
    }

    //~ Methods ·······························································

    /**
     * Returns the button which can be used to add items to the list.
     *
     * @return button to add items to the list.
     */
    public JButton getAddButton()
    {
        return this.addButton;
    }


    /**
     * Returns the button which can be used to move items down the list.
     *
     * @return button to move item down.
     */
    public JButton getDownButton()
    {
        return this.downButton;
    }


    /**
     * Sets whether or not this component is enabled.
     *
     * @param enable if <code>enable</code> this component will be made
     *        enabled (if it isn't already).
     */
    public void setEnabled(boolean enable)
    {
        if (enable)
        {
            _table.setEnabled(true);
            updateButtons(_table.getSelectedRow());
        }
        else
        {
            _table.setEnabled(false);
            this.downButton.setEnabled(false);
            this.upButton.setEnabled(false);
        }

        super.setEnabled(enable);
    }


    /**
     * Returns the button which can be used to remove items from the list.
     *
     * @return button to remove items from the the list.
     */
    public JButton getRemoveButton()
    {
        return this.removeButton;
    }


    /**
     * Returns the table.
     *
     * @return table.
     */
    public JTable getTable()
    {
        return _table;
    }


    /**
     * Returns the button which can be used to move items up the list.
     *
     * @return button to move items up.
     */
    public JButton getUpButton()
    {
        return this.upButton;
    }


    /**
     * Adds a constraint for the given types.
     *
     * @param first the first type.
     * @param second the second type.
     * @param constraint the type of the constraint ({@link Constraint#UP} or
     *        {@link Constraint#DOWN}).
     */
    public void addConstraint(Type first,
                              Type second,
                              int  constraint)
    {
        if (constraint == Constraint.UP)
        {
            if (_upConstraints == null)
            {
                _upConstraints = new ArrayList(3);
            }

            _upConstraints.add(new Constraint(first, second, constraint));
        }
        else
        {
            if (_downContraints == null)
            {
                _downContraints = new ArrayList(3);
            }

            _downContraints.add(new Constraint(first, second, constraint));
        }
    }


    /**
     * Removes the constraint for the given types.
     *
     * @param first the first type.
     * @param second the second type.
     * @param constraint the type of the constraint ({@link Constraint#UP} or
     *        {@link Constraint#DOWN}).
     */
    public void removeConstraint(Type first,
                                 Type second,
                                 int  constraint)
    {
        if (constraint == Constraint.UP)
        {
            if (_upConstraints != null)
            {
                _upConstraints.remove(new Constraint(first, second, constraint));
            }
        }
        else
        {
            if (_downContraints != null)
            {
                _downContraints.remove(new Constraint(first, second, constraint));
            }
        }
    }


    /**
     * Checks the constraints for the given types.
     *
     * @param cur current type.
     * @param prev previous type.
     *
     * @return <code>true</code> if no violation occured.
     */
    private boolean checkHighConstraint(Type cur,
                                        Type prev)
    {
        if (_upConstraints == null)
        {
            return true;
        }

        for (int i = 0, size = _upConstraints.size(); i < size; i++)
        {
            Constraint c = (Constraint)_upConstraints.get(i);

            if (c.first == cur)
            {
                if (c.second == prev)
                {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks the constraints for the given types.
     *
     * @param cur current type.
     * @param next previous type.
     *
     * @return <code>true</code> if no violation occured.
     */
    private boolean checkLowConstraint(Type cur,
                                       Type next)
    {
        if (_downContraints == null)
        {
            return true;
        }

        for (int i = 0, size = _downContraints.size(); i < size; i++)
        {
            Constraint c = (Constraint)_downContraints.get(i);

            if (c.first == cur)
            {
                if (c.second == next)
                {
                    return false;
                }
            }
        }

        return true;
    }


    private void initializeAddRemove()
    {
        this.addButton = new JButton("Add");
        this.removeButton = new JButton("Remove");
        this.addButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    _tableModel.addRow(new Object[1]);
                    _table.editCellAt(_table.getRowCount() - 1, 0);
                    _table.setRowSelectionInterval(_table.getRowCount() - 1,
                                                   _table.getRowCount() - 1);

                    DefaultCellEditor editor = (DefaultCellEditor)_table.getCellEditor(_table.getRowCount() -
                                                                                       1, 0);
                    Component comp = editor.getTableCellEditorComponent(_table,
                                                                        "",
                                                                        true,
                                                                        _table.getRowCount() -
                                                                        1, 0);
                    comp.requestFocus();
                    TableList.this.removeButton.setEnabled(true);
                }
            });

        this.removeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    // if the user is editing a value, first cancel the editing
                    TableCellEditor editor = _table.getCellEditor();

                    if (editor != null)
                    {
                        editor.cancelCellEditing();
                    }

                    int curRow = _table.getSelectionModel()
                                       .getMinSelectionIndex();

                    if (curRow > -1)
                    {
                        _tableModel.removeRow(curRow);
                    }

                    if (_tableModel.getRowCount() > 0)
                    {
                        if (curRow == 0)
                        {
                            _table.setRowSelectionInterval(0, 0);
                        }
                        else
                        {
                            _table.setRowSelectionInterval(curRow - 1,
                                                           curRow - 1);
                        }
                    }
                    else
                    {
                        TableList.this.removeButton.setEnabled(false);
                    }
                }
            });
    }


    /**
     * This method picks good column sizes.
     *
     * @param table table to initialize.
     */
    private void initializeColumnSizes(JTable table)
    {
        TableCellRenderer headerRenderer = table.getTableHeader()
                                                .getDefaultRenderer();

        for (int i = 1, size = table.getColumnModel().getColumnCount();
             i < size;
             i++)
        {
            TableColumn column = table.getColumnModel().getColumn(i);
            Component comp = headerRenderer.getTableCellRendererComponent(null,
                                                                          column.getHeaderValue(),
                                                                          false,
                                                                          false, 0, 0);
            int headerWidth = comp.getPreferredSize().width;
            column.setPreferredWidth(headerWidth);
        }
    }


    /**
     * Creates a new TableList object.
     */
    private void initializeUpDown()
    {
        this.upButton = new JButton("Up");
        this.upButton.setEnabled(false);
        this.downButton = new JButton("Down");
        this.downButton.setEnabled(false);

        this.upButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    int row = _table.getSelectedRow();

                    // we know for sure that there is a row above us as we
                    // disallow the action otherwise
                    _tableModel.moveRow(row, row, row - 1);
                    _table.setRowSelectionInterval(row - 1, row - 1);
                    _table.requestFocus();
                }
            });
        this.downButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    int row = _table.getSelectedRow();

                    // we know for sure that there is a row beyond us as we
                    // disallow the action otherwise
                    _tableModel.moveRow(row, row, row + 1);
                    _table.setRowSelectionInterval(row + 1, row + 1);
                    _table.requestFocus();
                }
            });

        // update the button states so that actions will only be allowed if
        // they make sense
        _table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent ev)
                {
                    // ignore extra messages
                    if (ev.getValueIsAdjusting())
                    {
                        return;
                    }

                    updateButtons(_table.getSelectedRow());
                }
            });
    }


    /**
     * Updates the state of the buttons.
     *
     * @param selectedRow the currently selected row, <code>-1</code> if no
     *        row is selected.
     */
    private void updateButtons(int selectedRow)
    {
        if (selectedRow == -1) // no selection
        {
            if (this.removeButton != null)
            {
                this.removeButton.setEnabled(false);
            }

            if (this.upButton != null)
            {
                this.upButton.setEnabled(false);
            }

            if (this.downButton != null)
            {
                this.downButton.setEnabled(false);
            }
        }
        else if (selectedRow == 0) // first row
        {
            if (this.removeButton != null)
            {
                this.removeButton.setEnabled(true);
            }

            if (this.upButton != null)
            {
                this.upButton.setEnabled(false);
            }

            if (this.downButton != null)
            {
                this.downButton.setEnabled(true);
            }
        }
        else if (selectedRow == (_tableModel.getRowCount() - 1)) // last row
        {
            if (this.removeButton != null)
            {
                this.removeButton.setEnabled(true);
            }

            if (this.downButton != null)
            {
                this.downButton.setEnabled(false);
            }

            if (this.upButton != null)
            {
                this.upButton.setEnabled(true);
            }
        }
        else // in-between row
        {
            if (this.removeButton != null)
            {
                this.removeButton.setEnabled(true);
            }

            if (this.upButton != null)
            {
                this.upButton.setEnabled(true);
            }

            if (this.downButton != null)
            {
                this.downButton.setEnabled(true);
            }
        }

        if ((this.upButton != null) && this.upButton.isEnabled())
        {
            int row = _table.getSelectedRow();

            // check the constraints for the up button
            if ((row != -1) && (_upConstraints != null))
            {
                Type prev = (Type)_tableModel.getValueAt(row - 1, 0);
                Type cur = (Type)_tableModel.getValueAt(row, 0);

                if (!checkHighConstraint(cur, prev))
                {
                    this.upButton.setEnabled(false);
                }
            }
        }

        if ((this.downButton != null) && this.downButton.isEnabled())
        {
            int row = _table.getSelectedRow();

            // check the constraints for the down button
            if ((row != -1) && (_downContraints != null))
            {
                Type cur = (Type)_tableModel.getValueAt(row, 0);
                Type next = (Type)_tableModel.getValueAt(row + 1, 0);

                if (!checkLowConstraint(cur, next))
                {
                    this.downButton.setEnabled(false);
                }
            }
        }
    }

    //~ Inner Classes ·························································

    /**
     * Serves as a constraint for the type entries in a table list.
     *
     * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
     * @version 1.0
     */
    static class Constraint
    {
        /** Indicates a constraint for downwards checking. */
        public static final int DOWN = 2;

        /** Indicates a constraint for upwards checking. */
        public static final int UP = 1;

        /** The first element of the constraint. */
        Type first;

        /** The second element of the constraint. */
        Type second;

        /** The type of the constraint. */
        int constraint;

        /**
         * Creates a new Constraint object.
         * 
         * <p>
         * If you want to prohibit that element <em>first</em> can ever be
         * moved above element <em>second</em> use: <code> new
         * Constraint(first, second, Constraint.UP)</code>.
         * </p>
         * 
         * <p>
         * Accordingly, if you want to prohibit that element <em>first</em>
         * can ever be moved below element <em>second</em> use: <code> new
         * Constraint(first, second, Constraint.DOWN)</code>.
         * </p>
         *
         * @param first the first element.
         * @param second the second element.
         * @param constraint the type of the constraint; either {@link #UP} or
         *        {@link #DOWN}.
         */
        public Constraint(Type first,
                          Type second,
                          int  constraint)
        {
            this.first = first;
            this.second = second;
            this.constraint = constraint;
        }
    }


    /**
     * TableCellRenderer for boolean values.
     */
    private static class BooleanRenderer
        extends JCheckBox
        implements TableCellRenderer
    {
        boolean disabled;

        public BooleanRenderer()
        {
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable  table,
                                                       Object  value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int     row,
                                                       int     column)
        {
            if (table.isEnabled())
            {
                if (isSelected)
                {
                    setForeground(table.getSelectionForeground());
                    super.setBackground(table.getSelectionBackground());
                }
                else
                {
                    setForeground(table.getForeground());
                    setBackground(table.getBackground());
                }

                if ((!disabled) && (!isEnabled()))
                {
                    this.setEnabled(true);
                }
            }
            else
            {
                if (isEnabled())
                {
                    this.setEnabled(false);
                    setBackground((Color)UIManager.get("Table.background"));
                }
            }

            if (value == null)
            {
                this.setEnabled(false);
            }

            setSelected(((value != null) && ((Boolean)value).booleanValue()));

            return this;
        }
    }


    /**
     * TableCellRenderer for Types (actually strings).
     */
    private static class TypeRenderer
        extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable  table,
                                                       Object  value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int     row,
                                                       int     column)
        {
            if (table.isEnabled())
            {
                if (isSelected)
                {
                    super.setForeground(table.getSelectionForeground());
                    super.setBackground(table.getSelectionBackground());
                }
                else
                {
                    super.setForeground(table.getForeground());
                    super.setBackground(table.getBackground());
                }
            }
            else
            {
                super.setForeground((Color)UIManager.get("textInactiveText"));
                super.setBackground((Color)UIManager.get("Table.background"));
            }

            setFont(table.getFont());
            setValue(value);

            Color back = getBackground();
            boolean colorMatch = (back != null) &&
                                 (back.equals(table.getBackground())) &&
                                 table.isOpaque();
            setOpaque(!colorMatch);

            return this;
        }
    }
}
