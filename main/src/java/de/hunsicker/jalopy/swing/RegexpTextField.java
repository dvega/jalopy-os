/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing;

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

import de.hunsicker.swing.util.SwingHelper;
import de.hunsicker.util.ResourceBundleFactory;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A text field suitable for displaying and editing regular expressions
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class RegexpTextField
    extends JTextField
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final String EMPTY_STRING = "" /* NOI18N */.intern();

    /** The name for ResourceBundle lookup. */
    private static final String BUNDLE_NAME =
        "de.hunsicker.jalopy.swing.Bundle" /* NOI18N */;

    //~ Instance variables ---------------------------------------------------------------

    private CellEditor _cellEditor;

    //~ Constructors ---------------------------------------------------------------------

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
        addMouseListener(
            new MouseAdapter()
            {
                public void mousePressed(MouseEvent ev)
                {
                    ev.consume();
                    getParent().requestFocus();

                    Window owner =
                        SwingUtilities.windowForComponent(RegexpTextField.this);
                    RegexpDialog dialog = null;

                    if (owner instanceof Dialog)
                    {
                        dialog = new RegexpDialog((Dialog) owner);
                    }
                    else
                    {
                        dialog = new RegexpDialog((Frame) owner);
                    }

                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param cellEditor DOCUMENT ME!
     */
    public void setCellEditor(CellEditor cellEditor)
    {
        _cellEditor = cellEditor;
    }

    //~ Inner Classes --------------------------------------------------------------------

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
            setTitle(
                ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                    "TLE_REGEXP_TESTER" /* NOI18N */));
            setModal(true);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            final Container pane = getContentPane();
            final GridBagLayout layout = new GridBagLayout();
            pane.setLayout(layout);

            GridBagConstraints c = new GridBagConstraints();

            JLabel patternLabel =
                new JLabel(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "LBL_PATTERN" /* NOI18N */));
            c.insets.top = 10;
            c.insets.left = 15;
            c.insets.right = 10;
            SwingHelper.setConstraints(
                c, 0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(patternLabel, c);
            pane.add(patternLabel);
            c.insets.left = 0;
            c.insets.right = 15;

            final JTextField patternTextField = new JTextField(getText(), 25);
            SwingHelper.setConstraints(
                c, 1, 0, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0,
                0);
            layout.setConstraints(patternTextField, c);
            pane.add(patternTextField);

            JLabel testLabel =
                new JLabel(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "LBL_STRING" /* NOI18N */));
            c.insets.top = 0;
            c.insets.bottom = 10;
            c.insets.left = 15;
            c.insets.right = 10;
            SwingHelper.setConstraints(
                c, 0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(testLabel, c);
            pane.add(testLabel);
            c.insets.left = 0;
            c.insets.right = 15;

            final JTextField testTextField = new JTextField(EMPTY_STRING, 25);
            SwingHelper.setConstraints(
                c, 1, 1, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, c.insets, 0,
                0);
            layout.setConstraints(testTextField, c);
            pane.add(testTextField);

            messageLabel = new JLabel(" " /* NOI18N */);
            messageLabel.setFont(new Font("Courier" /* NOI18N */, Font.BOLD, 14));
            c.insets.top = 15;
            c.insets.bottom = 15;
            c.insets.left = 20;
            c.insets.right = 20;
            SwingHelper.setConstraints(
                c, 0, 2, GridBagConstraints.REMAINDER, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(messageLabel, c);
            pane.add(messageLabel);

            final JButton testButton =
                SwingHelper.createButton(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "BTN_TEST" /* NOI18N */));
            c.insets.top = 0;
            c.insets.bottom = 10;
            c.insets.left = 15;
            c.insets.right = 50;
            SwingHelper.setConstraints(
                c, 0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(testButton, c);
            pane.add(testButton);
            testButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent ev)
                    {
                        if (!test(patternTextField.getText(), testTextField.getText()))
                        {
                            testTextField.requestFocus();
                        }
                    }
                });

            JButton applyButton =
                SwingHelper.createButton(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "BTN_APPLY" /* NOI18N */));
            c.insets.left = 10;
            c.insets.right = 5;
            SwingHelper.setConstraints(
                c, 4, 3, 1, 1, 1.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(applyButton, c);
            pane.add(applyButton);

            applyButton.addActionListener(
                new ActionListener()
                {
                    public void actionPerformed(ActionEvent ev)
                    {
                        if (test(patternTextField.getText(), testTextField.getText()))
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

            JButton cancelButton =
                SwingHelper.createButton(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "BTN_CANCEL" /* NOI18N */));
            c.insets.left = 0;
            c.insets.right = 15;
            SwingHelper.setConstraints(
                c, 5, 3, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, c.insets, 0, 0);
            layout.setConstraints(cancelButton, c);
            pane.add(cancelButton);

            cancelButton.addActionListener(
                new ActionListener()
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


        /**
         * Performs regular expression testing for the given pattern/string.
         *
         * @param pattern DOCUMENT ME!
         * @param string DOCUMENT ME!
         *
         * @return <code>true</code> if the given pattern matches the given string.
         */
        private boolean test(
            String pattern,
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
                this.messageLabel.setText(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "LBL_INVALID_PATTERN" /* NOI18N */));

                return false;
            }

            PatternMatcher matcher = new Perl5Matcher();

            if (matcher.matches(string, regexp))
            {
                this.messageLabel.setForeground(Color.blue);
                this.messageLabel.setText(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "LBL_PATTERN_MATCHES" /* NOI18N */));

                return true;
            }
            else
            {
                this.messageLabel.setForeground(Color.red);
                this.messageLabel.setText(
                    ResourceBundleFactory.getBundle(BUNDLE_NAME).getString(
                        "LBL_PATTERN_DOES_NOT_MATCH" /* NOI18N */));

                return false;
            }
        }
    }
}
