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
package de.hunsicker.ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;


/**
 * UI related helper functions.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class SwingHelper
{
    //~ Constructors ··························································

    /**
     * Creates a new SwingHelper object.
     */
    private SwingHelper()
    {
    }

    //~ Methods ·······························································

    /**
     * Sets the constraints for the given constraints object. Helper function
     * to be able to reuse a constraints object.
     *
     * @param constraints the constraints object to initialize.
     * @param gridx the initial gridx value.
     * @param gridy the initial gridy value.
     * @param gridwidth the initial gridwidth value.
     * @param gridheight the initial gridheight value.
     * @param weightx the initial weightx value.
     * @param weighty the initial weighty value.
     * @param anchor the initial anchor value.
     * @param fill the initial fill value.
     * @param insets the initial insets value.
     * @param ipadx the initial ipadx value.
     * @param ipady the initial ipady value.
     *
     * @return the initialized constraints object.
     */
    public static GridBagConstraints setConstraints(GridBagConstraints constraints,
                                                    int                gridx,
                                                    int                gridy,
                                                    int                gridwidth,
                                                    int                gridheight,
                                                    double             weightx,
                                                    double             weighty,
                                                    int                anchor,
                                                    int                fill,
                                                    Insets             insets,
                                                    int                ipadx,
                                                    int                ipady)
    {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.fill = fill;
        constraints.insets.top = insets.top;
        constraints.insets.bottom = insets.bottom;
        constraints.insets.left = insets.left;
        constraints.insets.right = insets.right;
        constraints.ipadx = ipadx;
        constraints.ipady = ipady;

        return constraints;
    }


    /**
     * Returns the frame that contains the given component.
     *
     * @param component component.
     *
     * @return frame that contains the given component or <code>null</code> if
     *         there is no parent frame.
     */
    public static Frame getOwnerFrame(Component component)
    {
        Window window = SwingUtilities.windowForComponent(component);
        Frame mother = null;

        if (window != null)
        {
            Window owner = window.getOwner();

            if ((owner != null) && owner instanceof Frame)
            {
                mother = (Frame)owner;
            }
        }

        return mother;
    }


    /**
     * Returns the visual height of the given table.
     *
     * @param table the table.
     *
     * @return the table height.
     */
    public static int getTableHeight(JTable table)
    {
        int result = 0;
        int rowHeight = 0;

        for (int i = 0, rows = table.getRowCount(); i < rows; i++)
        {
            int height = table.getRowHeight(i);
            result += height;

            if (height > rowHeight)
            {
                rowHeight = height;
            }
        }

        return result + rowHeight + (table.getRowCount() * table.getRowMargin());
    }


    /**
     * Displays the given file chooser. Utility method for avoiding of memory
     * leak in JDK 1.3 {@link javax.swing.JFileChooser#showDialog}.
     *
     * @param chooser the file chooser to display.
     * @param parent the parent window.
     * @param approveButtonText the text for the approve button.
     *
     * @return the return code of the chooser.
     */
    public static final int showJFileChooser(JFileChooser chooser,
                                             Component    parent,
                                             String       approveButtonText)
    {
        if (approveButtonText != null)
        {
            chooser.setApproveButtonText(approveButtonText);
            chooser.setDialogType(javax.swing.JFileChooser.CUSTOM_DIALOG);
        }

        Frame frame = (parent instanceof Frame) ? (Frame)parent
                                                : (Frame)javax.swing.SwingUtilities.getAncestorOfClass(java.awt.Frame.class,
                                                                                                       parent);
        String title = chooser.getDialogTitle();

        if (title == null)
        {
            title = chooser.getUI().getDialogTitle(chooser);
        }

        final JDialog dialog = new JDialog(frame, title, true);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(chooser, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        chooser.rescanCurrentDirectory();

        final int[] retValue = new int[] 
        {
            javax.swing.JFileChooser.CANCEL_OPTION
        };
        ActionListener l = new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                if (ev.getActionCommand() == JFileChooser.APPROVE_SELECTION)
                {
                    retValue[0] = JFileChooser.APPROVE_OPTION;
                }

                dialog.setVisible(false);
                dialog.dispose();
            }
        };

        chooser.addActionListener(l);
        dialog.show();

        return (retValue[0]);
    }
}
