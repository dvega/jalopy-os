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

import de.hunsicker.jalopy.parser.DeclarationType;
import de.hunsicker.jalopy.parser.ModifierType;
import de.hunsicker.jalopy.parser.Type;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;


/**
 * A component that can be used to display/edit the Jalopy sorting
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class SortPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _sortCheckBox;
    private JCheckBox _sortModifiersCheckBox;
    private JTabbedPane _tabbedPane;

    /** The data stored in the table. */
    private List _data; // List of <List<DeclarationType>>
    private List _modifiersData; // List of <List<ModifiersType>>
    private TableList _table;

    //~ Constructors ··························································

    /**
     * Creates a new SortPanel object.
     */
    public SortPanel()
    {
        initialize();
    }


    /**
     * Creates a new SortPanel.
     *
     * @param container the parent container.
     */
    SortPanel(SettingsContainer container)
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
                return "sortingmodifiers";

            default :
                return super.getPreviewFileName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void store()
    {
        this.settings.putBoolean(Keys.SORT, _sortCheckBox.isSelected());
        this.settings.putBoolean(Keys.SORT_MODIFIERS,
                              _sortModifiersCheckBox.isSelected());

        String declarationOrder = getSortString();
        DeclarationType.setOrder(declarationOrder);
        this.settings.put(Keys.SORT_ORDER, declarationOrder);

        String modifierOrder = getModifierSortString();
        ModifierType.setOrder(modifierOrder);
        this.settings.put(Keys.SORT_ORDER_MODIFIERS, modifierOrder);
    }


    private String getModifierSortString()
    {
        StringBuffer buf = new StringBuffer(100);

        for (Iterator i = _modifiersData.iterator(); i.hasNext();)
        {
            List rowData = (List)i.next();
            ModifierType type = (ModifierType)rowData.get(0);
            buf.append(type);
            buf.append(',');
        }

        // remove the last comma
        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }


    private String getSortString()
    {
        StringBuffer buf = new StringBuffer(50);

        for (Iterator i = _data.iterator(); i.hasNext();)
        {
            List rowData = (List)i.next();
            DeclarationType type = (DeclarationType)rowData.get(0);
            buf.append(type);
            buf.append(',');

            if (type == DeclarationType.METHOD)
            {
                this.settings.putBoolean(Keys.SORT_METHOD,
                                      ((Boolean)rowData.get(1)).booleanValue());
            }
            else if (type == DeclarationType.CTOR)
            {
                this.settings.putBoolean(Keys.SORT_CTOR,
                                      ((Boolean)rowData.get(1)).booleanValue());
            }
            else if (type == DeclarationType.CLASS)
            {
                this.settings.putBoolean(Keys.SORT_CLASS,
                                      ((Boolean)rowData.get(1)).booleanValue());
            }
            else if (type == DeclarationType.VARIABLE)
            {
                this.settings.putBoolean(Keys.SORT_VARIABLE,
                                      ((Boolean)rowData.get(1)).booleanValue());
            }
            else if (type == DeclarationType.INTERFACE)
            {
                this.settings.putBoolean(Keys.SORT_INTERFACE,
                                      ((Boolean)rowData.get(1)).booleanValue());
            }
        }

        // remove the last comma
        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }


    private JPanel createDeclarationPane()
    {
        StringTokenizer tokens = new StringTokenizer(this.settings.get(
                                                                    Keys.SORT_ORDER,
                                                                    DeclarationType.getOrder()),
                                                     ",");
        Object[][] data = new Object[7][2];

        for (int i = 0; tokens.hasMoreTokens(); i++)
        {
            String token = tokens.nextToken();

            if (DeclarationType.valueOf(token) == DeclarationType.VARIABLE)
            {
                data[i][0] = DeclarationType.VARIABLE;
                data[i][1] = new Boolean(this.settings.getBoolean(
                                                               Keys.SORT_VARIABLE,
                                                               Defaults.SORT_VARIABLE));
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.INIT)
            {
                data[i][0] = DeclarationType.INIT;
                data[i][1] = null;
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.CTOR)
            {
                data[i][0] = DeclarationType.CTOR;
                data[i][1] = new Boolean(this.settings.getBoolean(Keys.SORT_CTOR,
                                                               Defaults.SORT_CTOR));
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.METHOD)
            {
                data[i][0] = DeclarationType.METHOD;
                data[i][1] = new Boolean(this.settings.getBoolean(
                                                               Keys.SORT_METHOD,
                                                               Defaults.SORT_METHOD));
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.STATIC_VARIABLE_INIT)
            {
                data[i][0] = DeclarationType.STATIC_VARIABLE_INIT;
                data[i][1] = null;
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.INTERFACE)
            {
                data[i][0] = DeclarationType.INTERFACE;
                data[i][1] = new Boolean(this.settings.getBoolean(
                                                               Keys.SORT_INTERFACE,
                                                               Defaults.SORT_INTERFACE));
            }
            else if (DeclarationType.valueOf(token) == DeclarationType.CLASS)
            {
                data[i][0] = DeclarationType.CLASS;
                data[i][1] = new Boolean(this.settings.getBoolean(Keys.SORT_CLASS,
                                                               Defaults.SORT_CLASS));
            }
        }

        JPanel general = new JPanel();
        general.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        general.setBorder(BorderFactory.createCompoundBorder(
                                                             BorderFactory.createTitledBorder("General"),
                                                             BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _sortCheckBox = new JCheckBox("Sort class elements",
                                      this.settings.getBoolean(Keys.SORT,
                                                            Defaults.SORT));
        _sortCheckBox.addActionListener(this.trigger);
        general.add(_sortCheckBox);
        _sortCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    refresh();
                }
            });

        JPanel typesPanel = new JPanel();
        typesPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Ordering"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 0)));

        GridBagLayout typesLayout = new GridBagLayout();
        typesPanel.setLayout(typesLayout);

        Object[] columnNames ={ "Type", "Sort" };
        DefaultTableModel d = new DataModel(data, columnNames);
        _table = new TableList(d, TableList.TYPE_UP_DOWN);
        _table.addConstraint(DeclarationType.INIT, DeclarationType.VARIABLE,
                             TableList.Constraint.UP);
        _table.addConstraint(DeclarationType.VARIABLE, DeclarationType.INIT,
                             TableList.Constraint.DOWN);
        _data = d.getDataVector();

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        typesLayout.setConstraints(_table, c);
        typesPanel.add(_table);
        c.insets.top = 10;
        c.insets.bottom = 2;
        c.insets.left = 10;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        typesLayout.setConstraints(_table.getUpButton(), c);
        typesPanel.add(_table.getUpButton());
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        typesLayout.setConstraints(_table.getDownButton(), c);
        typesPanel.add(_table.getDownButton());

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(general, c);
        panel.add(general);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(typesPanel, c);
        panel.add(typesPanel);
        refresh();

        return panel;
    }


    private JPanel createModifierPane()
    {
        StringTokenizer tokens = new StringTokenizer(this.settings.get(
                                                                    Keys.SORT_ORDER_MODIFIERS,
                                                                    ModifierType.getOrder()),
                                                     ",");
        Object[][] data = new Object[11][1];

        for (int i = 0; tokens.hasMoreTokens(); i++)
        {
            String token = tokens.nextToken();

            if (ModifierType.valueOf(token) == ModifierType.PUBLIC)
            {
                data[i][0] = ModifierType.PUBLIC;
            }
            else if (ModifierType.valueOf(token) == ModifierType.PROTECTED)
            {
                data[i][0] = ModifierType.PROTECTED;
            }
            else if (ModifierType.valueOf(token) == ModifierType.PRIVATE)
            {
                data[i][0] = ModifierType.PRIVATE;
            }
            else if (ModifierType.valueOf(token) == ModifierType.STATIC)
            {
                data[i][0] = ModifierType.STATIC;
            }
            else if (ModifierType.valueOf(token) == ModifierType.FINAL)
            {
                data[i][0] = ModifierType.FINAL;
            }
            else if (ModifierType.valueOf(token) == ModifierType.ABSTRACT)
            {
                data[i][0] = ModifierType.ABSTRACT;
            }
            else if (ModifierType.valueOf(token) == ModifierType.NATIVE)
            {
                data[i][0] = ModifierType.NATIVE;
            }
            else if (ModifierType.valueOf(token) == ModifierType.TRANSIENT)
            {
                data[i][0] = ModifierType.TRANSIENT;
            }
            else if (ModifierType.valueOf(token) == ModifierType.SYNCHRONIZED)
            {
                data[i][0] = ModifierType.SYNCHRONIZED;
            }
            else if (ModifierType.valueOf(token) == ModifierType.VOLATILE)
            {
                data[i][0] = ModifierType.VOLATILE;
            }
            else if (ModifierType.valueOf(token) == ModifierType.STRICTFP)
            {
                data[i][0] = ModifierType.STRICTFP;
            }
        }

        JPanel general = new JPanel();
        general.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        general.setBorder(BorderFactory.createCompoundBorder(
                                                             BorderFactory.createTitledBorder("General"),
                                                             BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        _sortModifiersCheckBox = new JCheckBox("Sort modifiers",
                                               this.settings.getBoolean(
                                                                     Keys.SORT_MODIFIERS,
                                                                     Defaults.SORT_MODIFIERS));
        _sortModifiersCheckBox.addActionListener(this.trigger);
        general.add(_sortModifiersCheckBox);
        _sortModifiersCheckBox.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    refresh();
                }
            });

        JPanel typesPanel = new JPanel();
        typesPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createTitledBorder("Ordering"),
                                                                BorderFactory.createEmptyBorder(0, 5, 5, 0)));

        GridBagLayout typesLayout = new GridBagLayout();
        typesPanel.setLayout(typesLayout);

        Object[] columnNames ={ "Type" };
        DefaultTableModel d = new DataModel(data, columnNames);
        TableList modifiersTable = new TableList(d, TableList.TYPE_UP_DOWN);
        JTable table = modifiersTable.getTable();
        d.addTableModelListener(new TableModelListener()
            {
                public void tableChanged(TableModelEvent ev)
                {
                    trigger.actionPerformed(null);
                }
            });
        _modifiersData = d.getDataVector();

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        typesLayout.setConstraints(modifiersTable, c);
        typesPanel.add(modifiersTable);
        c.insets.top = 10;
        c.insets.bottom = 2;
        c.insets.left = 10;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);

        JButton upButton = modifiersTable.getUpButton();
        typesLayout.setConstraints(upButton, c);
        typesPanel.add(upButton);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);

        JButton downButton = modifiersTable.getDownButton();
        typesLayout.setConstraints(downButton, c);
        typesPanel.add(downButton);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(general, c);
        panel.add(general);
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(typesPanel, c);
        panel.add(typesPanel);

        return panel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _tabbedPane = new JTabbedPane();
        _tabbedPane.add(createDeclarationPane(), "Declarations");
        _tabbedPane.add(createModifierPane(), "Modifiers");
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


    /**
     * Sets the state (enabled/disabled) of the panels according to the
     * selection state of the sorting checkbox.
     */
    private void refresh()
    {
        if (_sortCheckBox.isSelected())
        {
            _table.setEnabled(true);
        }
        else
        {
            _table.setEnabled(false);
        }
    }

    //~ Inner Classes ·························································

    /**
     * Holds the data do be display in the table.
     */
    private static class DataModel
        extends DefaultTableModel
    {
        static final int TYPE_CLASS = 1;
        static final int TYPE_CTOR = 3;
        static final int TYPE_IFC = 2;
        static final int TYPE_INIT = 6;
        static final int TYPE_METHOD = 5;
        static final int TYPE_VAR = 4;

        public DataModel(Object[][] data,
                         Object[]   columnNames)
        {
            super(data, columnNames);
        }

        public boolean isCellEditable(int row,
                                      int col)
        {
            if (getValueAt(row, col) == null)
            {
                return false;
            }

            if (col < 1)
            {
                return false;
            }
            else
            {
                return true;
            }
        }


        public Class getColumnClass(int columnIndex)
        {
            switch (columnIndex)
            {
                case 0 :
                    return Type.class;

                case 1 :
                    return Boolean.class;

                default :
                    return String.class;
            }
        }
    }
}
