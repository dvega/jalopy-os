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

import de.hunsicker.io.IoHelper;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.Project;
import de.hunsicker.ui.ErrorDialog;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * A component that can be used to switch between different coding
 * conventions.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ProjectPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private AddRemoveList _projectsList;
    private JButton _addButton;
    private JButton _removeButton;

    //~ Constructors ··························································

    /**
     * Creates a new ProjectPanel object.
     */
    public ProjectPanel()
    {
        initialize();
    }


    /**
     * Creates a new ProjectPanel.
     *
     * @param container the parent container.
     */
    ProjectPanel(SettingsContainer container)
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
    }


    private void setActive(ProjectListEntry entry)
    {
        DefaultListModel model = (DefaultListModel)_projectsList.getModel();

        for (int i = 0, size = model.size(); i < size; i++)
        {
            ProjectListEntry e = (ProjectListEntry)model.get(i);

            if (e.active)
            {
                e.active = false;

                break;
            }
        }

        entry.active = true;
        _projectsList.repaint();
    }


    private Project getActiveProject()
    {
        try
        {
            File file = new File(Convention.getSettingsDirectory(),
                                 "project.dat");

            if (file.exists())
            {
                return (Project)IoHelper.deserialize(file);
            }
            else
            {
                return Convention.getDefaultProject();
            }
        }
        catch (IOException ex)
        {
            return Convention.getDefaultProject();
        }
    }


    private Collection getProjectEntries()
    {
        File directory = Convention.getSettingsDirectory();
        File[] files = directory.listFiles();
        Project activeProject = getActiveProject();
        List projects = new ArrayList(6);

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                File file = new File(files[i], "project.dat");

                try
                {

                    if (file.exists())
                    {
                        Project project = (Project)IoHelper.deserialize(file);
                        ProjectListEntry entry = new ProjectListEntry(project.getName(),
                                                                      project.getDescription(),
                                                                      project.equals(activeProject),
                                                                      project.equals(Convention.getDefaultProject()));
                        projects.add(entry);
                    }
                }
                catch (IOException ex)
                {
                    // should only fail between incompatible versions, just
                    // remove the file for now
                    file.delete();
                }
            }
        }

        if (projects.isEmpty())
        {
            Project defaultProject = Convention.getDefaultProject();
            projects.add(new ProjectListEntry(defaultProject.getName(),
                                              defaultProject.getDescription(),
                                              true, true));
        }
        else
        {
            boolean active = false; // is a project marked as active?
            boolean standard = false; // is the default project contained?

            for (int i = 0, size = projects.size(); i < size; i++)
            {
                ProjectListEntry e = (ProjectListEntry)projects.get(i);

                if (e.active && e.standard)
                {
                    active = true;
                }

                if (e.standard)
                {
                    standard = true;
                }
            }

            if (!standard)
            {
                // determine whether the default project is the active one
                if ((!active) && standard)
                {
                    active = true;
                }

                Project defaultProject = Convention.getDefaultProject();
                projects.add(new ProjectListEntry(defaultProject.getName(),
                                                  defaultProject.getDescription(),
                                                  active, true));
            }
        }

        return projects;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        Collection projects = getProjectEntries();
        _projectsList = new ProjectList("Add new project", null, projects);
        _projectsList.setCellRenderer(new ProjectListCellRenderer());

        JScrollPane keysScrollPane = new JScrollPane(_projectsList);
        SwingHelper.setConstraints(c, 0, 0, 8, 8, 1.0, 1.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(keysScrollPane, c);
        add(keysScrollPane);
        c.insets.bottom = 2;
        c.insets.top = 10;
        c.insets.left = 10;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 9, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _addButton = _projectsList.getAddButton();
        layout.setConstraints(_addButton, c);
        add(_addButton);
        c.insets.left = 10;
        c.insets.right = 0;
        c.insets.bottom = 0;
        c.insets.top = 0;
        SwingHelper.setConstraints(c, 9, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _removeButton = _projectsList.getRemoveButton();
        _removeButton.setEnabled(false);
        _removeButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    ProjectListEntry entry = (ProjectListEntry)_projectsList.getSelectedValue();

                    if (entry != null)
                    {
                        try
                        {
                            Convention.removeProject(new Project(entry.name,
                                                                  entry.description));
                        }
                        catch (IOException ex)
                        {
                            JOptionPane.showMessageDialog(ProjectPanel.this,
                                                          "Could not remove the project.",
                                                          "Error: Project removal failed",
                                                          JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
        layout.setConstraints(_removeButton, c);
        add(_removeButton);

        final JButton activateButton = new JButton("Activate");
        activateButton.setEnabled(false);
        c.insets.top = 15;
        SwingHelper.setConstraints(c, 9, 3, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(activateButton, c);
        add(activateButton);
        activateButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    ProjectListEntry entry = (ProjectListEntry)_projectsList.getSelectedValue();

                    if (entry != null)
                    {
                        try
                        {
                            Convention.setProject(new Project(entry.name,
                                                               entry.description));
                            setActive(entry);

                            if (getContainer() != null)
                            {
                                // clear the panel cache so the new values will
                                // be populated
                                getContainer().clearCache();
                            }

                            activateButton.setEnabled(false);
                            _removeButton.setEnabled(false);
                        }
                        catch (Throwable ex)
                        {
                            /**
                             * @todo show error dialog
                             */
                            ex.printStackTrace();
                        }
                    }
                }
            });
        _projectsList.addListSelectionListener(new ListSelectionListener()
            {
                public void valueChanged(ListSelectionEvent ev)
                {
                    if (ev.getValueIsAdjusting())
                    {
                        return;
                    }

                    ProjectListEntry entry = (ProjectListEntry)_projectsList.getSelectedValue();

                    if (entry != null)
                    {
                        if (entry.standard)
                        {
                            _projectsList.removeButton.setEnabled(false);
                        }
                        else
                        {
                            _projectsList.removeButton.setEnabled(true);
                        }

                        if (entry.active)
                        {
                            activateButton.setEnabled(false);
                            _projectsList.removeButton.setEnabled(false);
                        }
                        else
                        {
                            activateButton.setEnabled(true);
                        }
                    }
                    else
                    {
                        _projectsList.removeButton.setEnabled(false);
                        activateButton.setEnabled(false);
                    }
                }
            });
    }

    //~ Inner Classes ·························································

    private static class ProjectList
        extends AddRemoveList
    {
        public ProjectList(String     title,
                           String     text,
                           Collection data)
        {
            super(title, text, data);

            EventListener[] listeners = this.listenerList.getListeners(ListSelectionListener.class);

            for (int i = 0; i < listeners.length; i++)
            {
                this.listenerList.remove(ListSelectionListener.class,
                                         listeners[i]);
            }
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

                JLabel nameLabel = new JLabel("Name:");
                c.insets.top = 10;
                c.insets.left = 5;
                c.insets.right = 5;
                SwingHelper.setConstraints(c, 0, 0,
                                           GridBagConstraints.REMAINDER, 1, 1.0,
                                           0.0, GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(nameLabel, c);
                contentPane.add(nameLabel);

                final JTextField nameTextField = new JTextField(20);
                nameLabel.setLabelFor(nameTextField);
                c.insets.top = 2;
                SwingHelper.setConstraints(c, 0, 1, 12, 1, 1.0, 0.0,
                                           GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(nameTextField, c);
                contentPane.add(nameTextField);

                JLabel descriptionLabel = new JLabel("Description:");
                c.insets.top = 10;
                c.insets.left = 5;
                c.insets.right = 5;
                SwingHelper.setConstraints(c, 0, 2,
                                           GridBagConstraints.REMAINDER, 1, 1.0,
                                           0.0, GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);
                layout.setConstraints(descriptionLabel, c);
                contentPane.add(descriptionLabel);

                final JTextArea descriptionTextArea = new JTextArea(3, 20);
                descriptionTextArea.setLineWrap(true);
                descriptionLabel.setLabelFor(descriptionTextArea);
                c.insets.top = 2;
                SwingHelper.setConstraints(c, 0, 3, 12, 1, 1.0, 1.0,
                                           GridBagConstraints.NORTHWEST,
                                           GridBagConstraints.HORIZONTAL,
                                           c.insets, 0, 0);

                JScrollPane descriptionSrollPane = new JScrollPane(descriptionTextArea);
                layout.setConstraints(descriptionSrollPane, c);
                contentPane.add(descriptionSrollPane);

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
                            String name = nameTextField.getText().trim();
                            String description = descriptionTextArea.getText()
                                                                    .trim();
                            ProjectListEntry entry = new ProjectListEntry(name,
                                                                          description,
                                                                          false);

                            if (listModel.contains(entry))
                            {
                                JOptionPane.showMessageDialog(AddDialog.this,
                                                              "A project with the given name already exists!",
                                                              "Error: Project already defined",
                                                              JOptionPane.ERROR_MESSAGE);

                                return;
                            }
                            else if (name.trim().equals(""))
                            {
                                JOptionPane.showMessageDialog(AddDialog.this,
                                                              "Invalid project name given: \"" +
                                                              name + "\"",
                                                              "Error: Invalid project name",
                                                              JOptionPane.ERROR_MESSAGE);

                                return;
                            }
                            else if (description.length() > 256)
                            {
                                JOptionPane.showMessageDialog(AddDialog.this,
                                                              "Project description must be no longer than 256 characters!",
                                                              "Error: Project description too long",
                                                              JOptionPane.ERROR_MESSAGE);

                                return;
                            }

                            try
                            {
                                Convention.addProject(new Project(name,
                                                                   description));

                                Object selValue = getSelectedValue();
                                listModel.add(0, entry);
                                setSelectedValue(selValue, false);
                            }
                            catch (IOException ex)
                            {
                                ErrorDialog dialog = new ErrorDialog(ex,
                                                                     AddDialog.this);
                                dialog.setVisible(true);
                                dialog.dispose();
                            }
                            catch (IllegalArgumentException ex)
                            {
                                JOptionPane.showMessageDialog(AddDialog.this,
                                                              ex.getMessage(),
                                                              "Error: Invalid project name",
                                                              JOptionPane.ERROR_MESSAGE);

                                return;
                            }

                            dispose();
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


    private static class ProjectListCellRenderer
        extends DefaultListCellRenderer
    {
        public Component getListCellRendererComponent(JList   list,
                                                      Object  value,
                                                      int     index,
                                                      boolean isSelected,
                                                      boolean cellHasFocus)
        {
            ProjectListEntry entry = (ProjectListEntry)value;
            JLabel component = (JLabel)super.getListCellRendererComponent(list,
                                                                          value,
                                                                          index,
                                                                          isSelected,
                                                                          cellHasFocus);

            if (entry.active)
            {
                Font font = getFont().deriveFont(Font.BOLD);
                component.setFont(font);
            }

            return component;
        }
    }


    private static class ProjectListEntry
        implements Comparable
    {
        final String description;
        final String name;
        boolean active;
        final boolean standard;

        public ProjectListEntry(String  name,
                                String  description,
                                boolean active)
        {
            this(name, description, false, false);
        }


        ProjectListEntry(String  name,
                         String  description,
                         boolean active,
                         boolean standard)
        {
            this.name = name;
            this.description = description;
            this.standard = standard;
            this.active = active;
        }

        public int compareTo(Object o)
        {
            if (o instanceof ProjectListEntry)
            {
                return this.name.compareTo(((ProjectListEntry)o).name);
            }

            return 0;
        }


        public boolean equals(Object o)
        {
            if (o instanceof ProjectListEntry)
            {
                return this.name.equals(((ProjectListEntry)o).name);
            }

            return false;
        }


        public int hashCode()
        {
            return this.name.hashCode();
        }


        public String toString()
        {
            return this.name;
        }
    }
}
