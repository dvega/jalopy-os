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
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.ui.EmptyButtonGroup;
import de.hunsicker.ui.ErrorDialog;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * A component that can be used to display/edit the Jalopy miscellaneous
 * settings.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class MiscPanel
    extends AbstractSettingsPanel
{
    //~ Instance variables ····················································

    private JCheckBox _arrayBracketsAfterIdentifierCheckBox;
    private JCheckBox _forceCheckBox;
    private JCheckBox _historyCommentCheckBox;
    private JCheckBox _historyFileCheckBox;
    private JCheckBox _insertConditionalCheckBox;
    private JCheckBox _insertParenCheckBox;
    private JCheckBox _insertTrailingNewlineCheckBox;
    private JCheckBox _insertUIDCheckBox;
    private JSlider _backupSlider;
    private JSlider _threadSlider;
    private JTextField _directoryTextField;

    //~ Constructors ··························································

    /**
     * Creates a new MiscPanel object.
     */
    public MiscPanel()
    {
        initialize();
    }


    /**
     * Creates a new MiscPanel.
     *
     * @param container the parent container.
     */
    MiscPanel(SettingsContainer container)
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
        this.settings.putBoolean(Keys.INSERT_EXPRESSION_PARENTHESIS,
                              _insertParenCheckBox.isSelected());
        this.settings.putBoolean(Keys.FORCE_FORMATTING,
                              _forceCheckBox.isSelected());
        this.settings.putBoolean(Keys.INSERT_TRAILING_NEWLINE,
                              _insertTrailingNewlineCheckBox.isSelected());
        this.settings.putBoolean(Keys.ARRAY_BRACKETS_AFTER_IDENT,
                              _arrayBracketsAfterIdentifierCheckBox.isSelected());
        this.settings.putInt(Keys.BACKUP_LEVEL, _backupSlider.getValue());
        this.settings.putInt(Keys.THREAD_COUNT, _threadSlider.getValue());

        String directoryPath = _directoryTextField.getText();

        if (directoryPath.startsWith(Convention.getProjectSettingsDirectory()
                                                .getAbsolutePath()))
        {
            // if the user specified a path relative to the default Jalopy
            // settings directory we only store the relative subdirectory
            // to make the setting portable across different platforms
            directoryPath = directoryPath.substring(Convention.getProjectSettingsDirectory()
                                                               .getAbsolutePath()
                                                               .length() + 1);
        }

        this.settings.put(Keys.BACKUP_DIRECTORY, directoryPath);
        this.settings.putBoolean(Keys.INSERT_SERIAL_UID,
                              _insertUIDCheckBox.isSelected());
        this.settings.putBoolean(Keys.INSERT_LOGGING_CONDITIONAL,
                              _insertConditionalCheckBox.isSelected());

        if (_historyCommentCheckBox.isSelected())
        {
            this.settings.put(Keys.HISTORY_POLICY,
                           History.Policy.COMMENT.toString());
        }
        else if (_historyFileCheckBox.isSelected())
        {
            this.settings.put(Keys.HISTORY_POLICY, History.Policy.FILE.toString());
        }
        else
        {
            this.settings.put(Keys.HISTORY_POLICY,
                           History.Policy.DISABLED.toString());
        }
    }


    private JPanel createBackupPanel()
    {
        JPanel backupPanel = new JPanel();
        backupPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Backup"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout backupLayout = new GridBagLayout();
        backupPanel.setLayout(backupLayout);

        GridBagConstraints c = new GridBagConstraints();
        JLabel backupLbl = new JLabel("Level:");
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        backupLayout.setConstraints(backupLbl, c);
        backupPanel.add(backupLbl);
        _backupSlider = new JSlider(JSlider.HORIZONTAL, 0, 30,
                                    this.settings.getInt(Keys.BACKUP_LEVEL,
                                                      Defaults.BACKUP_LEVEL));
        _backupSlider.setSnapToTicks(true);
        c.insets.left = 10;
        c.insets.right = 10;
        SwingHelper.setConstraints(c, 1, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        backupLayout.setConstraints(_backupSlider, c);
        backupPanel.add(_backupSlider);

        final NumberedLabel backupLevelLbl = new NumberedLabel(this.settings.getInt(
                                                                                 Keys.BACKUP_LEVEL,
                                                                                 Defaults.BACKUP_LEVEL),
                                                               "Backup ",
                                                               "Backups");
        _backupSlider.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent ev)
                {
                    JSlider source = (JSlider)ev.getSource();
                    int level = (int)source.getValue();
                    backupLevelLbl.setLevel(level);
                }
            });
        c.insets.left = 0;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        backupLayout.setConstraints(backupLevelLbl, c);
        backupPanel.add(backupLevelLbl);
        c.insets.top = 10;

        JLabel directoryLbl = new JLabel("Directory:");
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        backupLayout.setConstraints(directoryLbl, c);
        backupPanel.add(directoryLbl);

        String directoryPath = this.settings.get(Keys.BACKUP_DIRECTORY,
                                              Convention.getBackupDirectory()
                                                         .getAbsolutePath());
        File directoryFile = new File(directoryPath);

        if (!directoryFile.isAbsolute())
        {
            directoryPath = new File(Convention.getProjectSettingsDirectory(),
                                     directoryPath).getAbsolutePath();
        }

        _directoryTextField = new JTextField(directoryPath);
        _directoryTextField.setCaretPosition(1);
        _directoryTextField.setEditable(false);
        _directoryTextField.setToolTipText(_directoryTextField.getText());
        c.insets.left = 10;
        c.insets.right = 10;
        SwingHelper.setConstraints(c, 1, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        backupLayout.setConstraints(_directoryTextField, c);
        backupPanel.add(_directoryTextField);

        final JButton directoryAddBtn = new JButton("Choose...");
        c.insets.left = 0;
        c.insets.right = 0;
        SwingHelper.setConstraints(c, 2, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        backupLayout.setConstraints(directoryAddBtn, c);
        backupPanel.add(directoryAddBtn);
        directoryAddBtn.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    final Window owner = SwingUtilities.windowForComponent(MiscPanel.this);
                    LocationDialog dialog = null;
                    String backupHistory = LocationDialog.loadHistory(new File(
                                                                               Convention.getProjectSettingsDirectory(),
                                                                               "backup.dat"));

                    if ("".equals(backupHistory))
                    {
                        backupHistory = Convention.getBackupDirectory()
                                                   .getAbsolutePath();
                    }

                    if (owner instanceof Dialog)
                    {
                        dialog = new LocationDialog((Dialog)owner,
                                                    "Select backup directory",
                                                    "XXX", backupHistory);
                    }
                    else
                    {
                        dialog = new LocationDialog((Frame)owner,
                                                    "Select backup directory",
                                                    "XXX", backupHistory);
                    }

                    dialog.setFileChooserProperties(LocationDialog.DIRECTORIES_ONLY);
                    dialog.setVisible(true);

                    switch (dialog.getOption())
                    {
                        case JOptionPane.OK_OPTION :

                            try
                            {
                                String location = (String)dialog.getSelectedLocation();

                                if ((location != null) && (location.length() > 0))
                                {
                                    File file = new File(location);

                                    if (!IoHelper.ensureDirectoryExists(file))
                                    {
                                        /**
                                         * @todo display message
                                         */
                                        return;
                                    }

                                    _directoryTextField.setText(file.getAbsolutePath());
                                    _directoryTextField.setToolTipText(file.getAbsolutePath());

                                    // update url history
                                    LocationDialog.storeHistory(new File(
                                                                         Convention.getProjectSettingsDirectory(),
                                                                         "backup.dat"),
                                                                dialog.getHistoryString());
                                }

                                dialog.dispose();
                            }
                            catch (Exception ex)
                            {
                                /**
                                 * @todo needs custom error message
                                 */
                                ErrorDialog d = new ErrorDialog(ex,
                                                                (JDialog)owner);
                                d.setVisible(true);
                                d.dispose();
                            }

                            break;
                    }
                }
            });

        return backupPanel;
    }


    private JPanel createHistoryPanel()
    {
        JPanel historyPanel = new JPanel();
        historyPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                  BorderFactory.createTitledBorder("History"),
                                                                  BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        History.Policy historyPolicy = History.Policy.valueOf(this.settings.get(
                                                                             Keys.HISTORY_POLICY,
                                                                             Defaults.HISTORY_POLICY));
        GridBagLayout historyLayout = new GridBagLayout();
        historyPanel.setLayout(historyLayout);
        _historyCommentCheckBox = new JCheckBox("Use history comments",
                                                historyPolicy == History.Policy.COMMENT);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        historyLayout.setConstraints(_historyCommentCheckBox, c);
        historyPanel.add(_historyCommentCheckBox);
        _historyFileCheckBox = new JCheckBox("Use history file",
                                             historyPolicy == History.Policy.FILE);

        ButtonGroup historyCheckBoxGroup = new EmptyButtonGroup();
        historyCheckBoxGroup.add(_historyCommentCheckBox);
        historyCheckBoxGroup.add(_historyFileCheckBox);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        historyLayout.setConstraints(_historyFileCheckBox, c);
        historyPanel.add(_historyFileCheckBox);

        final JButton browseButton = new JButton("View...");
        browseButton.setEnabled(_historyFileCheckBox.isSelected());
        _historyFileCheckBox.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent e)
                {
                    if (_historyFileCheckBox.isSelected())
                    {
                        browseButton.setEnabled(true);
                    }
                    else
                    {
                        browseButton.setEnabled(false);
                    }
                }
            });
        browseButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JDialog dialog = null;
                    Window owner = SwingUtilities.windowForComponent(MiscPanel.this);

                    if (owner instanceof Dialog)
                    {
                        dialog = new JDialog((Dialog)owner, "History Viewer");
                    }
                    else
                    {
                        dialog = new JDialog((Frame)owner, "History Viewer");
                    }

                    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    dialog.getContentPane().add(new HistoryViewer());
                    dialog.setModal(true);
                    dialog.pack();
                    dialog.setLocationRelativeTo(owner);
                    dialog.setVisible(true);
                    dialog.dispose();
                }
            });
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        historyLayout.setConstraints(browseButton, c);
        historyPanel.add(browseButton);

        return historyPanel;
    }


    private JPanel createThreadPanel()
    {
        JPanel threadPanel = new JPanel();
        threadPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Threads"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout threadPanelLayout = new GridBagLayout();
        threadPanel.setLayout(threadPanelLayout);

        GridBagConstraints c = new GridBagConstraints();
        JLabel threadLabel = new JLabel("Number:");
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        threadPanelLayout.setConstraints(threadLabel, c);
        threadPanel.add(threadLabel);
        _threadSlider = new JSlider(JSlider.HORIZONTAL, 1, 8,
                                    this.settings.getInt(Keys.THREAD_COUNT,
                                                      Defaults.THREAD_COUNT));
        _threadSlider.setLabelTable(_threadSlider.createStandardLabels(1, 1));
        _threadSlider.setMajorTickSpacing(7);
        _threadSlider.setMinorTickSpacing(1);
        _threadSlider.setSnapToTicks(true);
        c.insets.left = 10;
        c.insets.top = 0;
        c.insets.right = 10;
        c.insets.bottom = 0;
        SwingHelper.setConstraints(c, 1, 0, 1, 1, 0.5, 0.0,
                                   GridBagConstraints.CENTER,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        threadPanelLayout.setConstraints(_threadSlider, c);
        threadPanel.add(_threadSlider);

        final NumberedLabel backupLevelLbl = new NumberedLabel(this.settings.getInt(
                                                                                 Keys.THREAD_COUNT,
                                                                                 Defaults.THREAD_COUNT),
                                                               "Thread ",
                                                               "Threads");
        _threadSlider.addChangeListener(new ChangeListener()
            {
                public void stateChanged(ChangeEvent ev)
                {
                    JSlider source = (JSlider)ev.getSource();
                    int level = (int)source.getValue();
                    backupLevelLbl.setLevel(level);
                }
            });
        c.insets.left = 0;
        c.insets.right = 15;
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        threadPanelLayout.setConstraints(backupLevelLbl, c);
        threadPanel.add(backupLevelLbl);

        return threadPanel;
    }


    private void initialize()
    {
        JPanel removePanel = new JPanel();
        GridBagLayout removeLayout = new GridBagLayout();
        removePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Misc"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));
        removePanel.setLayout(removeLayout);

        GridBagConstraints c = new GridBagConstraints();
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        _insertParenCheckBox = new JCheckBox("Insert expression parentheses",
                                             this.settings.getBoolean(
                                                                   Keys.INSERT_EXPRESSION_PARENTHESIS,
                                                                   Defaults.INSERT_EXPRESSION_PARENTHESIS));
        _insertParenCheckBox.addActionListener(this.trigger);
        removeLayout.setConstraints(_insertParenCheckBox, c);
        removePanel.add(_insertParenCheckBox);
        _insertUIDCheckBox = new JCheckBox("Insert serial version UID",
                                           this.settings.getBoolean(
                                                                 Keys.INSERT_SERIAL_UID,
                                                                 Defaults.INSERT_SERIAL_UID));
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_insertUIDCheckBox, c);
        removePanel.add(_insertUIDCheckBox);

        _insertConditionalCheckBox = new JCheckBox("Insert logging conditional",
                                                   this.settings.getBoolean(
                                                                         Keys.INSERT_LOGGING_CONDITIONAL,
                                                                         Defaults.INSERT_LOGGING_CONDITIONAL));
        _insertConditionalCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_insertConditionalCheckBox, c);
        removePanel.add(_insertConditionalCheckBox);

        _insertTrailingNewlineCheckBox = new JCheckBox("Insert trailing newline",
                                                       this.settings.getBoolean(
                                                                             Keys.INSERT_TRAILING_NEWLINE,
                                                                             Defaults.INSERT_TRAILING_NEWLINE));
        _insertTrailingNewlineCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_insertTrailingNewlineCheckBox, c);
        removePanel.add(_insertTrailingNewlineCheckBox);

        _arrayBracketsAfterIdentifierCheckBox = new JCheckBox("Array brackets after identifier",
                                                              this.settings.getBoolean(
                                                                                    Keys.ARRAY_BRACKETS_AFTER_IDENT,
                                                                                    Defaults.ARRAY_BRACKETS_AFTER_IDENT));
        _arrayBracketsAfterIdentifierCheckBox.addActionListener(this.trigger);
        SwingHelper.setConstraints(c, 0, 2, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_arrayBracketsAfterIdentifierCheckBox, c);
        removePanel.add(_arrayBracketsAfterIdentifierCheckBox);

        _forceCheckBox = new JCheckBox("Force formatting",
                                       this.settings.getBoolean(
                                                             Keys.FORCE_FORMATTING,
                                                             Defaults.FORCE_FORMATTING));
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        removeLayout.setConstraints(_forceCheckBox, c);
        removePanel.add(_forceCheckBox);

        c.insets.top = 0;
        c.insets.left = 0;
        c.insets.right = 0;

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(removePanel, c);
        add(removePanel);
        c.insets.top = 0;

        JPanel historyPanel = createHistoryPanel();
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(historyPanel, c);
        add(historyPanel);

        JPanel backupPanel = createBackupPanel();
        SwingHelper.setConstraints(c, 0, 2, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(backupPanel, c);
        add(backupPanel);

        JPanel threadPanel = createThreadPanel();
        SwingHelper.setConstraints(c, 0, 3, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(threadPanel, c);
        add(threadPanel);
    }

    //~ Inner Classes ·························································

    private static class NumberedLabel
        extends JLabel
    {
        String plural;
        String singular;
        int level;

        public NumberedLabel(int    level,
                             String singular,
                             String plural)
        {
            this.singular = singular;
            this.plural = plural;
            setLevel(level);
        }

        public void setLevel(int level)
        {
            this.level = level;
            super.setText((level != 1)
                              ? (level + (' ' + this.plural))
                              : (level + (' ' + this.singular)));
        }


        public void setText(String text)
        {
        }
    }
}
