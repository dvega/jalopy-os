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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A text field suitable to display and edit regular expressions
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class RegexpTextField
    extends JTextField
{
    //~ Static variables/initializers ·········································

    private static final String EMPTY_STRING = "";

    //~ Instance variables ····················································

    private CellEditor _cellEditor;

    //~ Constructors ··························································

    /**
     * Creates a new RegexpTextField object.
     */
    public RegexpTextField()
    {
        this(EMPTY_STRING);
    }


    /**
     * Creates a new RegexpTextField object.
     *
     * @param pattern DOCUMENT ME!
     */
    public RegexpTextField(String pattern)
    {
        super(pattern);
        setEditable(false);
        addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent ev)
                {
                    //ev.consume();
                    getParent().requestFocus();

                    Window owner = SwingUtilities.windowForComponent(RegexpTextField.this);
                    RegexpDialog dialog = null;

                    if (owner instanceof Dialog)
                    {
                        dialog = new RegexpDialog((Dialog)owner);
                    }
                    else
                    {
                        dialog = new RegexpDialog((Frame)owner);
                    }

                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
    }

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     *
     * @param cellEditor DOCUMENT ME!
     */
    public void setCellEditor(CellEditor cellEditor)
    {
        _cellEditor = cellEditor;
    }

    //~ Inner Classes ·························································

    private class RegexpDialog
        extends JDialog
    {
        JLabel messageLabel;

        public RegexpDialog(Frame owner)
        {
            super(owner);
            initialize();
        }


        public RegexpDialog(Dialog owner)
        {
            super(owner);
            initialize();
        }

        private void initialize()
        {
            setTitle("Regular expression tester");
            setModal(true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            final Container pane = getContentPane();
            final GridBagLayout layout = new GridBagLayout();
            pane.setLayout(layout);

            JLabel patternLabel = new JLabel("Pattern:");
            GridBagConstraints c = new GridBagConstraints();
            c.insets.top = 10;
            c.insets.left = 15;
            c.insets.right = 10;
            SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(patternLabel, c);
            pane.add(patternLabel);
            c.insets.left = 0;
            c.insets.right = 15;

            final JTextField patternTextField = new JTextField(getText(), 25);
            SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(patternTextField, c);
            pane.add(patternTextField);

            JLabel testLabel = new JLabel("Test String:");
            c.insets.top = 0;
            c.insets.bottom = 10;
            c.insets.left = 15;
            c.insets.right = 10;
            SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(testLabel, c);
            pane.add(testLabel);
            c.insets.left = 0;
            c.insets.right = 15;

            final JTextField testTextField = new JTextField("", 25);
            SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(testTextField, c);
            pane.add(testTextField);
            c.insets.top = 15;
            c.insets.bottom = 15;
            c.insets.left = 20;
            c.insets.right = 20;
            messageLabel = new JLabel(" ");
            messageLabel.setFont(new Font("Courier", Font.BOLD, 14));
            SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(messageLabel, c);
            pane.add(messageLabel);

            final JButton testButton = new JButton("Test");
            c.insets.top = 0;
            c.insets.bottom = 10;
            c.insets.left = 15;
            c.insets.right = 50;
            SwingHelper.setConstraints(c, 0, 3, 1, 1, 0.0, 0.0,
                                       GridBagConstraints.WEST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(testButton, c);
            pane.add(testButton);
            testButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ev)
                    {
                        test(patternTextField.getText(),
                             testTextField.getText());
                    }
                });

            JButton applyButton = new JButton("Apply");
            c.insets.left = 10;
            c.insets.right = 5;
            SwingHelper.setConstraints(c, 4, 3, 1, 1, 1.0, 0.0,
                                       GridBagConstraints.EAST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(applyButton, c);
            pane.add(applyButton);
            applyButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ev)
                    {
                        if (test(patternTextField.getText(),
                                 testTextField.getText()))
                        {
                            setText(patternTextField.getText());
                            dispose();

                            if (_cellEditor != null)
                            {
                                _cellEditor.stopCellEditing();
                            }
                        }
                    }
                });

            JButton cancelButton = new JButton("Cancel");
            c.insets.left = 0;
            c.insets.right = 15;
            SwingHelper.setConstraints(c, 5, 3, GridBagConstraints.REMAINDER, 1,
                                       0.0, 0.0, GridBagConstraints.EAST,
                                       GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(cancelButton, c);
            pane.add(cancelButton);
            cancelButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent ev)
                    {
                        dispose();

                        if (_cellEditor != null)
                        {
                            _cellEditor.stopCellEditing();
                        }
                    }
                });
            pack();
            setLocationRelativeTo(getParent());
        }


        private boolean test(String pattern,
                             String string)
        {
            Pattern regexp = null;

            try
            {
                PatternCompiler compiler = new Perl5Compiler();
                regexp = compiler.compile(pattern);
            }
            catch (MalformedPatternException ex)
            {
                this.messageLabel.setForeground(Color.red);
                this.messageLabel.setText("ERROR: Invalid Pattern");

                return false;
            }

            PatternMatcher matcher = new Perl5Matcher();

            if (matcher.matches(string, regexp))
            {
                this.messageLabel.setForeground(Color.blue);
                this.messageLabel.setText("Pattern matches.");

                return true;
            }
            else
            {
                this.messageLabel.setForeground(Color.red);
                this.messageLabel.setText("Pattern does not match!");

                return false;
            }
        }
    }
}
