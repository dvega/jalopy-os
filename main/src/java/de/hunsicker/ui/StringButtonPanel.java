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
package de.hunsicker.ui;

import de.hunsicker.ui.util.SwingHelper;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * A component to display and change a string value. It consists of a text
 * field with a label to the left and a button to the right to change the
 * value of the text field.
 */
public abstract class StringButtonPanel
    extends JPanel
{
    //~ Instance variables ····················································

    /** DOCUMENT ME! */
    protected JButton button;

    /** DOCUMENT ME! */
    protected JLabel label;

    /** DOCUMENT ME! */
    protected JTextField textField;

    //~ Constructors ··························································

    /**
     * Creates a new StringButtonPanel.
     *
     * @param label the label text.
     * @param value the initial value of the text field.
     */
    public StringButtonPanel(String label,
                             String value)
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        this.label = new JLabel(label);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(this.label, c);
        add(this.label);
        this.textField = new JTextField(value);
        this.textField.setEditable(false);
        c.insets.left = 10;
        SwingHelper.setConstraints(c, 1, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(this.textField, c);
        add(this.textField);
        this.button = new JButton("Change...");
        this.button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    changePressed();
                }
            });
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(this.button, c);
        add(this.button);
    }

    //~ Methods ·······························································

    /**
     * Returns the text field to display edit the value.
     *
     * @return the used text field.
     */
    public JTextField getTextComponent()
    {
        return this.textField;
    }


    /**
     * Called when the users presses the change button.
     */
    public abstract void changePressed();
}
