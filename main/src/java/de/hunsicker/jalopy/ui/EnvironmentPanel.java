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

import de.hunsicker.jalopy.storage.Environment;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.ui.util.SwingHelper;
import de.hunsicker.util.ChainingRuntimeException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * A component that can be used to display/edit the Jalopy environment
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class EnvironmentPanel
    extends AbstractSettingsPanel
{
    //~ Static variables/initializers ·········································

    /** Used to track user actions (additions/removals). */
    private static Map _changes = new HashMap(); // Map of <ListEntry>:<Object>

    /** Indicates an addition to the list. */
    private static final Object ACTION_ADD = new Object();

    /** Indicates a removal from the list. */
    private static final Object ACTION_REMOVE = new Object();
    private static final char DELIM_PAIR = '^';
    private static final String DELIM_VAR = "|";
    private static final String EMPTY_STRING = "";

    /** The pattern to validate variables . */
    private static Pattern _variablesPattern;

    /** The pattern matcher. */
    private static final PatternMatcher _matcher = new Perl5Matcher();

    static
    {
        PatternCompiler compiler = new Perl5Compiler();

        try
        {
            _variablesPattern = compiler.compile("[a-zA-Z_][a-zA-Z0-9_]*",
                                                 Perl5Compiler.READ_ONLY_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ChainingRuntimeException(ex);
        }
    }

    //~ Instance variables ····················································

    private AddRemoveList _variablesList;
    private JButton _addButton;
    private JButton _removeButton;
    private JTabbedPane _tabs;

    //~ Constructors ··························································

    /**
     * Creates a new EnvironmentPanel object.
     */
    public EnvironmentPanel()
    {
        initialize();
    }


    /**
     * Creates a new EnvironmentPanel.
     *
     * @param container the parent container.
     */
    EnvironmentPanel(SettingsContainer container)
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
        DefaultListModel keysListModel = (DefaultListModel)_variablesList.getModel();

        if (keysListModel.size() > 0)
        {
            ListEntry[] items = new ListEntry[keysListModel.size()];
            keysListModel.copyInto(items);

            StringBuffer buf = new StringBuffer(100);

            for (int i = 0; i < items.length; i++)
            {
                buf.append(items[i].variable);
                buf.append(DELIM_PAIR);
                buf.append(items[i].value);
                buf.append(DELIM_VAR);
            }

            buf.deleteCharAt(buf.length() - 1);
            this.settings.put(Keys.ENVIRONMENT, buf.toString());
        }
        else
        {
            this.settings.put(Keys.ENVIRONMENT, EMPTY_STRING);
        }

        Environment env = Environment.getInstance();

        // update the environment so the changes will be available
        // immediately (important only for the IDE Plug-ins)
        for (Iterator i = _changes.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry)i.next();
            ListEntry e = (ListEntry)entry.getKey();
            Object action = entry.getValue();

            if (action == ACTION_REMOVE)
            {
                env.unset(e.variable);
            }
            else
            {
                env.set(e.variable, e.value);
            }
        }

        _changes.clear();
    }


    /**
     * Creates the pane with the system environment variables.
     *
     * @return pane with system environment variables.
     *
     * @since 1.0b8
     */
    private JPanel createSystemPane()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        List variables = new ArrayList();

        for (Iterator i = System.getProperties().entrySet().iterator();
             i.hasNext();)
        {
            Map.Entry entry = (Map.Entry)i.next();
            variables.add(new ListEntry((String)entry.getKey(),
                                        (String)entry.getValue()));
        }

        Collections.sort(variables);

        EnvironmentList envList = new EnvironmentList(EMPTY_STRING, null,
                                                      variables);
        JScrollPane envListScrollPane = new JScrollPane(envList);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 1.0,
                                   GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(envListScrollPane, c);
        panel.add(envListScrollPane);

        return panel;
    }


    /**
     * Creates the pane with the user environment variables.
     *
     * @return pane with user environment variables.
     *
     * @since 1.0b8
     */
    private JPanel createUserPane()
    {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        String variablesString = this.settings.get(Keys.ENVIRONMENT,
                                                Defaults.ENVIRONMENT);
        List variables = Collections.EMPTY_LIST;

        if ((variablesString != null) &&
            (!variablesString.trim().equals(EMPTY_STRING)))
        {
            variables = new ArrayList();

            for (StringTokenizer tokens = new StringTokenizer(variablesString,
                                                              DELIM_VAR);
                 tokens.hasMoreElements();)
            {
                String v = tokens.nextToken();
                int offset = v.indexOf(DELIM_PAIR);
                String variable = v.substring(0, offset);
                String value = v.substring(offset + 1);
                variables.add(new ListEntry(variable, value));
            }
        }

        _variablesList = new EnvironmentList("Add new environment variable",
                                             null, variables);

        JScrollPane keysScrollPane = new JScrollPane(_variablesList);
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(keysScrollPane, c);
        panel.add(keysScrollPane);
        c.insets.bottom = 2;
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _addButton = _variablesList.getAddButton();
        layout.setConstraints(_addButton, c);
        panel.add(_addButton);
        c.insets.left = 10;
        c.insets.right = 0;
        c.insets.bottom = 0;
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _removeButton = _variablesList.getRemoveButton();
        _removeButton.setEnabled(false);
        layout.setConstraints(_removeButton, c);
        _removeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    _changes.put(_variablesList.getSelectedValue(),
                                 ACTION_REMOVE);
                }
            });
        panel.add(_removeButton);

        return panel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _tabs = new JTabbedPane();

        JPanel userPanel = createUserPane();
        _tabs.add(userPanel, "User");

        JPanel systemPane = createSystemPane();
        _tabs.add(systemPane, "System");
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(_tabs, BorderLayout.CENTER);
    }

    //~ Inner Classes ·························································

    private static class EnvironmentList
        extends AddRemoveList
    {
        public EnvironmentList(String     title,
                               String     text,
                               Collection data)
        {
            super(title, text, data);
        }

        protected JDialog getAddDialog(Frame owner)
        {
            return new AddDialog(owner, this.title, this.text);
        }


        protected JDialog getAddDialog(Dialog owner)
        {
            return new AddDialog(owner, this.title, this.text);
        }

        private class AddDialog
            extends JDialog
        {
            public AddDialog(Frame  owner,
                             String title,
                             String text)
            {
                super(owner);
                initialize(title, text);
            }


            public AddDialog(Dialog owner,
                             String title,
                             String text)
            {
                super(owner);
                initialize(title, text);
            }

            private void initialize(String title,
                                    String text)
            {
                setTitle(title);
                setModal(true);
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

                Container contentPane = getContentPane();
                GridBagLayout layout = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();
                contentPane.setLayout(layout);

                JLabel variableLabel = new JLabel("Variable:");
                c.insets.top = 10;
                c.insets.left = 5;
                c.insets.right = 5;
                SwingHelper.setConstraints(c, 0, 0,
                                           GridBagConstraints.REMAINDER, 1, 1.0,
                                           0.0, GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(variableLabel, c);
                contentPane.add(variableLabel);

                final JTextField variableTextField = new JTextField(20);
                variableLabel.setLabelFor(variableTextField);
                c.insets.top = 2;
                SwingHelper.setConstraints(c, 0, 1, 12, 1, 1.0, 0.0,
                                           GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(variableTextField, c);
                contentPane.add(variableTextField);

                JLabel valueLabel = new JLabel("Value:");
                c.insets.top = 10;
                c.insets.left = 5;
                c.insets.right = 5;
                SwingHelper.setConstraints(c, 0, 2,
                                           GridBagConstraints.REMAINDER, 1, 1.0,
                                           0.0, GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(valueLabel, c);
                contentPane.add(valueLabel);

                final JTextField valueTextField = new JTextField(20);
                valueLabel.setLabelFor(valueTextField);
                c.insets.top = 2;
                SwingHelper.setConstraints(c, 0, 3, 12, 1, 1.0, 1.0,
                                           GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
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
                            String variable = variableTextField.getText().trim();
                            String value = valueTextField.getText().trim();
                            ListEntry entry = new ListEntry(variable, value);

                            if (listModel.contains(entry))
                            {
                                /**
                                 * @todo show dialog
                                 */
                                return;
                            }

                            if (!_matcher.matches(variable, _variablesPattern))
                            {
                                JOptionPane.showMessageDialog(AddDialog.this,
                                                              "\"" + variable +
                                                              "\" is no valid variable name.\nValid names have the form " +
                                                              _variablesPattern.getPattern() +
                                                              "\n",
                                                              "Error: Invalid variable name",
                                                              JOptionPane.ERROR_MESSAGE);

                                return;
                            }

                            listModel.add(0, entry);
                            setSelectedIndex(0);
                            dispose();
                            _changes.put(entry, ACTION_ADD);
                        }
                    });
                getRootPane().setDefaultButton(okButton);
                c.insets.top = 15;
                c.insets.bottom = 5;
                SwingHelper.setConstraints(c, 9, 4, 1, 1, 1.0, 0.0,
                                           GridBagConstraints.EAST,
                                           GridBagConstraints.NONE, c.insets, 0, 0);
                layout.setConstraints(okButton, c);
                contentPane.add(okButton);
                c.insets.left = 0;
                SwingHelper.setConstraints(c, 11, 4,
                                           GridBagConstraints.REMAINDER, 1, 0.0,
                                           0.0, GridBagConstraints.WEST,
                                           GridBagConstraints.NONE, c.insets, 0, 0);
                layout.setConstraints(cancelButton, c);
                contentPane.add(cancelButton);
            }
        }
    }


    /**
     * Represents an entry in the list: a key/value pair.
     */
    private static class ListEntry
        implements Comparable
    {
        public String value;
        public String variable;

        public ListEntry(String variable,
                         String value)
        {
            this.variable = variable;
            this.value = value;
        }

        public int compareTo(Object o)
        {
            if (o instanceof ListEntry)
            {
                return this.variable.compareTo(((ListEntry)o).variable);
            }

            return 0;
        }


        public boolean equals(Object o)
        {
            if (o instanceof ListEntry)
            {
                return this.variable.equals(((ListEntry)o).variable);
            }

            return false;
        }


        public int hashCode()
        {
            return this.variable.hashCode();
        }


        public String toString()
        {
            StringBuffer buf = new StringBuffer(30);
            buf.append(this.variable);
            buf.append(' ');
            buf.append('=');
            buf.append(' ');
            buf.append(this.value);

            return buf.toString();
        }
    }
}
