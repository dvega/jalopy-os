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

import de.hunsicker.ui.util.SwingHelper;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * A list component which implements the logic to add and remove items to and
 * from the list.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class AddRemoveList
    extends JList
{
    //~ Instance variables ····················································

    /** The data model of the list. */
    protected DefaultListModel listModel;

    /** The button to add items to the list. */
    protected JButton addButton;

    /** The button to remove items from the list. */
    protected JButton removeButton;

    /** The text to use for the default add dialog. */
    protected String text;

    /** The title to use for the default add dialog. */
    protected String title;

    //~ Constructors ··························································

    /**
     * Creates a new AddRemoveList object.
     *
     * @param title the title of the add dialog.
     * @param text the text of the add dialog description.
     * @param data initial data to display.
     */
    public AddRemoveList(String     title,
                         String     text,
                         Collection data)
    {
        this.listModel = new DefaultListModel();
        this.title = title;
        this.text = text;

        for (Iterator i = data.iterator(); i.hasNext();)
        {
            this.listModel.addElement(i.next());
        }

        setModel(this.listModel);
        initialize();
    }


    /**
     * Creates a new AddRemoveList object.
     *
     * @param title the title of the add dialog.
     * @param data initial data to display.
     */
    public AddRemoveList(String   title,
                         Object[] data)
    {
        this.title = title;
        this.listModel = new DefaultListModel();

        for (int i = 0; i < data.length; i++)
        {
            this.listModel.addElement(data[i]);
        }

        setModel(this.listModel);
        initialize();
    }

    //~ Methods ·······························································

    /**
     * Returns the button to add items.
     *
     * @return add button.
     */
    public JButton getAddButton()
    {
        return this.addButton;
    }


    /**
     * DOCUMENT ME!
     *
     * @param enable DOCUMENT ME!
     */
    public void setEnabled(boolean enable)
    {
        super.setEnabled(enable);

        if (!enable)
        {
            clearSelection();
        }
    }


    /**
     * Returns the button to remove items.
     *
     * @return remove button.
     */
    public JButton getRemoveButton()
    {
        return this.removeButton;
    }


    /**
     * Returns the values of the list.
     *
     * @return the list's values.
     */
    public Object[] getValues()
    {
        return this.listModel.toArray();
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] toArray()
    {
        return this.listModel.toArray();
    }


    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected JDialog getAddDialog(Dialog owner)
    {
        return new AddDialog(owner, this.title, this.text);
    }


    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected JDialog getAddDialog(Frame owner)
    {
        return new AddDialog(owner, this.title, this.text);
    }


    private void initialize()
    {
        this.addButton = new JButton("Add...");
        this.addButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JDialog dialog = null;
                    Window owner = SwingUtilities.windowForComponent(AddRemoveList.this);

                    if (owner instanceof Dialog)
                    {
                        dialog = getAddDialog((Dialog)owner);
                    }
                    else
                    {
                        dialog = getAddDialog((Frame)owner);
                    }

                    dialog.pack();
                    dialog.setLocationRelativeTo(owner);
                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
        this.removeButton = new JButton("Remove");
        this.removeButton.setEnabled(false);
        this.removeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    int index = getSelectedIndex();

                    if (index > -1)
                    {
                        AddRemoveList.this.listModel.remove(index);
                    }

                    if (AddRemoveList.this.listModel.isEmpty())
                    {
                        AddRemoveList.this.removeButton.setEnabled(false);
                    }
                    else
                    {
                        if (index >= AddRemoveList.this.listModel.getSize())
                        {
                            // the last entry was deleted, selector preceding entry
                            setSelectedIndex(index - 1);
                        }
                        else
                        {
                            // select next entry
                            setSelectedIndex(index);
                        }
                    }
                }
            });
        addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent ev)
                {
                    if (ev.getValueIsAdjusting())
                    {
                        return;
                    }

                    if (ev.getFirstIndex() > -1)
                    {
                        AddRemoveList.this.removeButton.setEnabled(true);
                    }
                    else
                    {
                        AddRemoveList.this.removeButton.setEnabled(false);
                    }
                }
            });
    }

    //~ Inner Classes ·························································

    private class AddDialog
        extends JDialog
    {
        public AddDialog(Frame  owner,
                         String title,
                         String text)
        {
            super(owner);
            init(title, text);
        }


        public AddDialog(Dialog owner,
                         String title,
                         String text)
        {
            super(owner);
            init(title, text);
        }

        private void init(String title,
                          String text)
        {
            setTitle(title);
            setModal(true);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            Container contentPane = getContentPane();
            GridBagLayout layout = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();
            contentPane.setLayout(layout);

            JLabel valueLabel = new JLabel(text);
            c.insets.top = 10;
            c.insets.left = 5;
            c.insets.right = 5;
            SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(valueLabel, c);
            contentPane.add(valueLabel);

            final JTextField valueTextField = new JTextField(20);
            valueLabel.setLabelFor(valueTextField);
            c.insets.top = 2;
            SwingHelper.setConstraints(c, 0, 1, 12, 1, 1.0, 1.0,
                                       GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(valueTextField, c);
            contentPane.add(valueTextField);

            final JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        setVisible(false);
                        dispose();
                    }
                });

            JButton okButton = new JButton("OK");
            okButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        String value = valueTextField.getText();

                        if (AddRemoveList.this.listModel.contains(value))
                        {
                            return;
                        }

                        try
                        {
                            AddRemoveList.this.listModel.add(0, value);
                        }
                        catch (RuntimeException ex)
                        {
                            if (AddRemoveList.this.listModel.contains(value))
                            {
                                AddRemoveList.this.listModel.remove(0);
                            }

                            return;
                        }

                        setSelectedValue(value, false);
                        dispose();
                    }
                });
            getRootPane().setDefaultButton(okButton);
            c.insets.top = 15;
            c.insets.bottom = 5;
            SwingHelper.setConstraints(c, 9, 2, 1, 1, 1.0, 0.0,
                                       GridBagConstraints.EAST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(okButton, c);
            contentPane.add(okButton);
            c.insets.left = 0;
            SwingHelper.setConstraints(c, 11, 2, GridBagConstraints.REMAINDER, 1,
                                       0.0, 0.0, GridBagConstraints.WEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(cancelButton, c);
            contentPane.add(cancelButton);
        }
    }
}
