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
import de.hunsicker.util.StringHelper;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;


/**
 * A simple dialog which can be used to display errors.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ErrorDialog
    extends JDialog
{
    //~ Static variables/initializers ·········································

    /** Indicates that the error message is displayed. */
    private static final int DETAILS_HIDE = 2;

    /** Indicates that the detailed error stacktrace is displayed. */
    private static final int DETAILS_SHOW = 1;

    //~ Instance variables ····················································

    /** Holds the component with the detailed stacktrace. */
    private JScrollPane _details;

    /** Is the error message or the stacktrace displayed? */
    private int _status = DETAILS_HIDE;

    //~ Constructors ··························································

    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex causing throwable.
     * @param parent parent frame.
     */
    public ErrorDialog(Throwable ex,
                       Frame     parent)
    {
        this(ex, "Error", parent, true);
    }


    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex causing throwable.
     * @param parent parent dialog.
     */
    public ErrorDialog(Throwable ex,
                       Dialog    parent)
    {
        this(ex, parent, true);
    }


    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex causing throwable.
     * @param parent the parent dialog
     * @param modal if <code>true</code> the dialog will become a modal
     *        dialog.
     */
    public ErrorDialog(final Throwable ex,
                       final Dialog    parent,
                       boolean         modal)
    {
        super(parent, modal);
        init(ex, "Error", parent);
    }


    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex causing throwable..
     * @param parent parent frame.
     * @param modal if <code>true</code> the dialog will become a modal
     *        dialog.
     */
    public ErrorDialog(final Throwable ex,
                       final Frame     parent,
                       boolean         modal)
    {
        super(parent, modal);
        init(ex, "Error", parent);
    }


    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public ErrorDialog(final Throwable ex,
                       String          title,
                       final Dialog    parent,
                       boolean         modal)
    {
        super(parent, modal);
        init(ex, title, parent);
    }


    /**
     * Creates a new ErrorDialog object.
     *
     * @param ex DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param modal DOCUMENT ME!
     */
    public ErrorDialog(final Throwable ex,
                       String          title,
                       final Frame     parent,
                       boolean         modal)
    {
        super(parent, modal);
        init(ex, title, parent);
    }

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     *
     * @param ex DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param parent DOCUMENT ME!
     */
    private void init(final Throwable ex,
                      String          title,
                      final Component parent)
    {
        setTitle(title);
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final Container pane = getContentPane();
        final GridBagLayout layout = new GridBagLayout();
        pane.setLayout(layout);

        StringBuffer buf = new StringBuffer();

        if (ex instanceof RuntimeException)
        {
            buf.append("An unexpected ");
            buf.append(ex.getClass().getName());
            buf.append(" has occured:");
        }
        else
        {
            buf.append(ex.getClass().getName() + ':');
        }

        final JPanel messagePanel = new JPanel();
        GridBagLayout messageLayout = new GridBagLayout();
        messagePanel.setLayout(messageLayout);

        JLabel message = new JLabel(buf.toString());
        final GridBagConstraints c = new GridBagConstraints();
        c.insets.right = 15;

        JLabel icon = new JLabel((Icon)UIManager.get("OptionPane.errorIcon"));
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.RELATIVE, 1, 0.0,
                                   0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.VERTICAL, c.insets, 0, 0);
        messageLayout.setConstraints(icon, c);
        messagePanel.add(icon);
        c.insets.left = 0;
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        messageLayout.setConstraints(message, c);
        messagePanel.add(message);

        String[] lines = StringHelper.wrapStringToArray((ex.getMessage() == null)
                                                            ? ""
                                                            : ex.getMessage(),
                                                        55, "\r\n", true,
                                                        StringHelper.TRIM_ALL);

        if ((lines.length == 1) && (lines[0].length() == 0))
        {
            lines = new String[] { "<no further information available>" };
        }

        for (int i = 0; i < lines.length; i++)
        {
            JLabel line = new JLabel(lines[i]);
            SwingHelper.setConstraints(c, 1, i + 1,
                                       GridBagConstraints.REMAINDER, 1, 1.0,
                                       0.0, GridBagConstraints.NORTHWEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            messageLayout.setConstraints(line, c);
            messagePanel.add(line);
        }

        c.insets.left = 10;
        c.insets.right = 10;
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 4,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(messagePanel, c);
        pane.add(messagePanel);
        c.insets.top = 10;
        c.insets.left = 50;
        c.insets.bottom = 15;
        c.insets.right = 15;

        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    dispose();
                }
            });
        getRootPane().setDefaultButton(okBtn);
        SwingHelper.setConstraints(c, 3, 4, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(okBtn, c);
        pane.add(okBtn);

        final JButton toggleBtn = new JButton("Show details");
        toggleBtn.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    setVisible(false);

                    switch (_status)
                    {
                        // show stacktrace, remove error message
                        case DETAILS_HIDE :
                            toggleBtn.setText("Hide details");
                            _status = DETAILS_SHOW;

                            if (_details == null)
                            {
                                StringWriter stringWriter = new StringWriter();
                                PrintWriter out = new PrintWriter(new BufferedWriter(stringWriter));
                                ex.printStackTrace(out);
                                out.close();

                                JTextArea textArea = new JTextArea(stringWriter.toString(),
                                                                   10, 40);
                                _details = new JScrollPane(textArea);
                                textArea.setCaretPosition(1);

                                Dimension size = new Dimension(400, 170);
                                _details.setMinimumSize(size);
                                _details.setPreferredSize(size);
                            }

                            pane.remove(messagePanel);
                            c.insets.top = 10;
                            c.insets.left = 10;
                            c.insets.bottom = 10;
                            c.insets.right = 10;
                            SwingHelper.setConstraints(c, 0, 0,
                                                       GridBagConstraints.REMAINDER, 4,
                                                       1.0, 1.0,
                                                       GridBagConstraints.NORTHWEST,
                                                       GridBagConstraints.BOTH,
                                                       c.insets, 0, 0);
                            layout.setConstraints(_details, c);
                            pane.add(_details);

                            break;

                        // show error message, remove stacktrace
                        case DETAILS_SHOW :
                            toggleBtn.setText("Show details");
                            _status = DETAILS_HIDE;
                            pane.remove(_details);
                            c.insets.top = 10;
                            c.insets.left = 10;
                            c.insets.bottom = 10;
                            c.insets.right = 10;
                            SwingHelper.setConstraints(c, 0, 0,
                                                       GridBagConstraints.REMAINDER, 4,
                                                       1.0, 1.0,
                                                       GridBagConstraints.NORTHWEST,
                                                       GridBagConstraints.BOTH,
                                                       c.insets, 0, 0);
                            layout.setConstraints(messagePanel, c);
                            pane.add(messagePanel);

                            break;
                    }

                    pack();
                    setLocationRelativeTo(parent);
                    setVisible(true);
                }
            });
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.bottom = 15;
        c.insets.right = 50;
        SwingHelper.setConstraints(c, 0, 4, GridBagConstraints.RELATIVE, 1, 0.0,
                                   0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(toggleBtn, c);
        pane.add(toggleBtn);
        pack();
        setLocationRelativeTo(parent);
    }
}
