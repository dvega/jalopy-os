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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * A component that combines a combo box and a descriptive label.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class ComboBoxPanel
    extends JPanel
{
    //~ Instance variables ····················································

    /** The combo box. */
    private JComboBox _combo;

    /** The label. */
    private JLabel _label;

    //~ Constructors ··························································

    /**
     * Creates a new ComboBoxPanel.
     *
     * @param text the label text.
     * @param items the items to display in the checkbox.
     * @param item the item to initially select. If the item does not exist,
     *        it will be added to the list.
     */
    public ComboBoxPanel(String   text,
                         Object[] items,
                         Object   item)
    {
        this(text, items);

        boolean found = false;

        for (int i = 0; i < items.length; i++)
        {
            if (items[i].equals(item))
            {
                found = true;

                break;
            }
        }

        if (!found)
        {
            _combo.addItem(item);
        }

        _combo.setSelectedItem(item);
    }


    /**
     * Creates a new ComboBoxPanel.
     *
     * @param text the label text.
     * @param items the items to display in the checkbox.
     */
    public ComboBoxPanel(String   text,
                         Object[] items)
    {
        init(text, items);
    }

    //~ Methods ·······························································

    /**
     * Sets the combo box to use.
     *
     * @param comboBox combo box to use.
     */
    public void setComboBox(JComboBox comboBox)
    {
        _combo = comboBox;
    }


    /**
     * Returns the used combo box.
     *
     * @return the used combo box.
     */
    public JComboBox getComboBox()
    {
        return _combo;
    }


    /**
     * Enables the component so that items can be selected.
     *
     * @param enable if <code>true</code> items will be selectable and values
     *        can be typed in the editor (if it is editable).
     */
    public void setEnabled(boolean enable)
    {
        super.setEnabled(enable);
        _combo.setEnabled(enable);
        _label.setEnabled(enable);
    }


    /**
     * Initializes the UI:
     *
     * @param text the label text.
     * @param items the items to display in the checkbox.
     */
    private void init(String   text,
                      Object[] items)
    {
        setLayout(new BorderLayout());
        _label = new JLabel(text);
        _label.setLabelFor(_combo);
        _label.setForeground(Color.black);
        add(_label, BorderLayout.WEST);
        add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        _combo = new JComboBox(items);
        add(_combo, BorderLayout.EAST);
    }
}
