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

import de.hunsicker.jalopy.parser.JavaRecognizer;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.ui.ErrorDialog;
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;


/**
 * A component that can be used to display/edit the Jalopy general
 * preferences.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class GeneralPanel
    extends AbstractPreferencesPanel
{
    //~ Static variables/initializers ·········································

    private static final FileFilter FILTER_JAL = new JalopyFilter();
    private static final FileFilter FILTER_XML = new XmlFilter();
    private static final String URL_DELIMETER = "|";
    private static final String JDK_1_3 = "JDK 1.3";
    private static final String JDK_1_4 = "JDK 1.4";

    //~ Instance variables ····················································

    private JComboBox _compatComboBox;
    private JTextField _descTextField;
    private JTextField _nameTextField;

    //~ Constructors ··························································

    /**
     * Creates a new GeneralPanel object.
     */
    public GeneralPanel()
    {
        initialize();
    }


    /**
     * Creates a new GeneralPanel.
     *
     * @param container the parent container.
     */
    GeneralPanel(PreferencesContainer container)
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
        this.prefs.put(Keys.STYLE_NAME, _nameTextField.getText());
        this.prefs.put(Keys.STYLE_DESCRIPTION, _descTextField.getText());
        this.prefs.putInt(Keys.SOURCE_VERSION,
                          getSourceVersion((String)_compatComboBox.getSelectedItem()));
    }


    /**
     * Returns the corresponding integer constant for the given version
     * string.
     *
     * @param version version string.
     *
     * @return corresponding integer constant.
     *
     * @since 1.0b8
     */
    private int getSourceVersion(String version)
    {
        if (JDK_1_3.equals(version))
        {
            return JavaRecognizer.JDK_1_3;
        }
        else if (JDK_1_4.equals(version))
        {
            return JavaRecognizer.JDK_1_4;
        }

        return Defaults.SOURCE_VERSION;
    }


    /**
     * Returns the corresponding string for the given integer constant.
     *
     * @param version version constant.
     *
     * @return corresponding string.
     *
     * @since 1.0b8
     */
    private String getSourceVersion(int version)
    {
        switch (version)
        {
            case JavaRecognizer.JDK_1_3 :
                return JDK_1_3;

            case JavaRecognizer.JDK_1_4 :
            default :
                return JDK_1_4;
        }
    }


    private JPanel createGeneralPane()
    {
        JPanel conventionPanel = new JPanel();
        conventionPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                     BorderFactory.createTitledBorder("Convention"),
                                                                     BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout conventionLayout = new GridBagLayout();
        conventionPanel.setLayout(conventionLayout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.right = 10;

        JLabel nameLbl = new JLabel("Name:");
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.RELATIVE, 1, 0.0,
                                   0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        conventionLayout.setConstraints(nameLbl, c);
        conventionPanel.add(nameLbl, c);
        c.insets.right = 0;
        _nameTextField = new JTextField(this.prefs.get(Keys.STYLE_NAME,
                                                       Defaults.STYLE_NAME), 15);
        SwingHelper.setConstraints(c, 1, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        conventionLayout.setConstraints(_nameTextField, c);
        conventionPanel.add(_nameTextField);
        c.insets.right = 10;
        c.insets.top = 1;

        JLabel descLbl = new JLabel("Description:");
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.RELATIVE, 1, 0.0,
                                   0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        conventionLayout.setConstraints(descLbl, c);
        conventionPanel.add(descLbl);
        c.insets.right = 0;
        _descTextField = new JTextField(this.prefs.get(Keys.STYLE_DESCRIPTION,
                                                       Defaults.STYLE_DESCRIPTION),
                                        15);
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        conventionLayout.setConstraints(_descTextField, c);
        conventionPanel.add(_descTextField);

        JPanel compatPanel = new JPanel();
        compatPanel.setBorder(BorderFactory.createCompoundBorder(
                                                                 BorderFactory.createTitledBorder("Compliance"),
                                                                 BorderFactory.createEmptyBorder(0, 5, 5, 5)));

        GridBagLayout compatLayout = new GridBagLayout();
        compatPanel.setLayout(compatLayout);
        c.insets.left = 0;
        c.insets.top = 0;

        int version = this.prefs.getInt(Keys.SOURCE_VERSION,
                                        Defaults.SOURCE_VERSION);
        String[] items ={ JDK_1_3, JDK_1_4 };
        ComboBoxPanel compatComboBoxPanel = new ComboBoxPanel("Source compatibility: ",
                                                              items,
                                                              getSourceVersion(version));
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        compatLayout.setConstraints(compatComboBoxPanel, c);
        compatPanel.add(compatComboBoxPanel, c);
        _compatComboBox = compatComboBoxPanel.getComboBox();

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(conventionPanel, c);
        panel.add(conventionPanel);
        c.insets.top = 10;
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   0.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(compatPanel, c);
        panel.add(compatPanel);

        return panel;
    }


    private JPanel createImportExportPane()
    {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));

        JButton load = new JButton("Import...");
        load.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    final Window owner = SwingUtilities.windowForComponent(GeneralPanel.this);
                    LocationDialog dialog = null;

                    if (owner instanceof Dialog)
                    {
                        dialog = new LocationDialog((Dialog)owner,
                                                    "Import Preferences", "XXX",
                                                    LocationDialog.loadHistory(new File(
                                                                                        Preferences.getProjectSettingsDirectory(),
                                                                                        "import.dat")));
                    }
                    else
                    {
                        dialog = new LocationDialog((Frame)owner,
                                                    "Import Preferences", "XXX",
                                                    LocationDialog.loadHistory(new File(
                                                                                        Preferences.getProjectSettingsDirectory(),
                                                                                        "import.dat")));
                    }

                    dialog.addFilter(FILTER_JAL);
                    dialog.addFilter(FILTER_XML, true);
                    dialog.setVisible(true);

                    switch (dialog.getOption())
                    {
                        case JOptionPane.OK_OPTION :

                            try
                            {
                                String location = (String)dialog.getSelectedLocation();

                                if ((location == null) ||
                                    (location.trim().length() == 0))
                                {
                                    /**
                                     * @todo show error dialog;
                                     */
                                    return;
                                }

                                FileFilter filter = dialog.getFileFilter();

                                if ((filter == FILTER_JAL) ||
                                    location.endsWith(Preferences.EXTENSION_JAL))
                                {
                                    if (!location.endsWith(Preferences.EXTENSION_JAL))
                                    {
                                        location += Preferences.EXTENSION_JAL;
                                    }
                                }
                                else if ((filter == FILTER_XML) ||
                                         location.endsWith(Preferences.EXTENSION_XML))
                                {
                                    if (!location.endsWith(Preferences.EXTENSION_XML))
                                    {
                                        location += Preferences.EXTENSION_XML;
                                    }
                                }

                                if (location.startsWith("http:"))
                                {
                                    GeneralPanel.this.prefs.importPreferences(new URL(location));
                                    GeneralPanel.this.prefs.put(Keys.STYLE_LOCATION,
                                                                location);
                                }
                                else if (location.startsWith("www."))
                                {
                                    GeneralPanel.this.prefs.importPreferences(new URL("http://" + location));
                                    GeneralPanel.this.prefs.put(Keys.STYLE_LOCATION,
                                                                "http://" +
                                                                location);
                                }
                                else
                                {
                                    GeneralPanel.this.prefs.importPreferences(new File(location));
                                }

                                // update the fields
                                _nameTextField.setText(GeneralPanel.this.prefs.get(
                                                                                   Keys.STYLE_NAME,
                                                                                   Defaults.STYLE_NAME));
                                _descTextField.setText(GeneralPanel.this.prefs.get(
                                                                                   Keys.STYLE_DESCRIPTION,
                                                                                   Defaults.STYLE_DESCRIPTION));

                                int version = GeneralPanel.this.prefs.getInt(Keys.SOURCE_VERSION,
                                                                             Defaults.SOURCE_VERSION);
                                _compatComboBox.setSelectedItem(getSourceVersion(version));
                                LocationDialog.storeHistory(new File(
                                                                     Preferences.getProjectSettingsDirectory(),
                                                                     "import.dat"),
                                                            dialog.getHistoryString());

                                // clear the panel cache so the new values will be
                                // loaded the next time a panel is displayed
                                if (getContainer() != null)
                                {
                                    getContainer().clearCache();
                                }

                                // and finally store everything
                                GeneralPanel.this.prefs.flush();
                                dialog.dispose();
                            }
                            catch (Exception ex)
                            {
                                ErrorDialog d = null;

                                if (owner instanceof Dialog)
                                {
                                    d = new ErrorDialog(ex, (Dialog)owner);
                                }
                                else
                                {
                                    d = new ErrorDialog(ex, (Frame)owner);
                                }

                                /**
                                 * @todo needs custom error message
                                 */
                                d.setVisible(true);
                                d.dispose();
                            }

                            break;
                    }
                }
            });

        JButton save = new JButton("Export...");
        save.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent ev)
                {
                    Window owner = SwingUtilities.windowForComponent(GeneralPanel.this);
                    LocationDialog dialog = null;

                    if (owner instanceof Dialog)
                    {
                        dialog = new LocationDialog((Dialog)owner,
                                                    "Export Preferences", "XXX",
                                                    LocationDialog.loadHistory(new File(
                                                                                        Preferences.getProjectSettingsDirectory(),
                                                                                        "export.dat")));
                    }
                    else
                    {
                        dialog = new LocationDialog((Frame)owner,
                                                    "Export Preferences", "XXX",
                                                    LocationDialog.loadHistory(new File(
                                                                                        Preferences.getProjectSettingsDirectory(),
                                                                                        "export.dat")));
                    }

                    dialog.addFilter(FILTER_JAL);
                    dialog.addFilter(FILTER_XML, true);
                    dialog.setVisible(true);

                    switch (dialog.getOption())
                    {
                        case JOptionPane.OK_OPTION :

                            String location = (String)dialog.getSelectedLocation();

                            if ((location != null) && (location.length() > 0))
                            {
                                try
                                {
                                    LocationDialog.storeHistory(new File(
                                                                         Preferences.getProjectSettingsDirectory(),
                                                                         "export.dat"),
                                                                dialog.getHistoryString());

                                    FileFilter filter = dialog.getFileFilter();
                                    String extension = Preferences.EXTENSION_JAL;

                                    if ((filter == FILTER_JAL) ||
                                        location.endsWith(Preferences.EXTENSION_JAL))
                                    {
                                        if (!location.endsWith(Preferences.EXTENSION_JAL))
                                        {
                                            location += Preferences.EXTENSION_JAL;
                                        }
                                    }
                                    else if ((filter == FILTER_XML) ||
                                             location.endsWith(Preferences.EXTENSION_XML))
                                    {
                                        if (!location.endsWith(Preferences.EXTENSION_XML))
                                        {
                                            location += Preferences.EXTENSION_XML;
                                        }

                                        extension = Preferences.EXTENSION_XML;
                                    }

                                    // export the preferences
                                    OutputStream out = new FileOutputStream(new File(location));
                                    GeneralPanel.this.prefs.exportPreferences(out,
                                                                              extension);
                                }
                                catch (Exception ex)
                                {
                                    /**
                                     * @todo needs custom error message
                                     */
                                    ErrorDialog d = null;

                                    if (owner instanceof Dialog)
                                    {
                                        d = new ErrorDialog(ex, (Dialog)owner);
                                    }
                                    else
                                    {
                                        d = new ErrorDialog(ex, (Frame)owner);
                                    }

                                    d.setVisible(true);
                                }
                            }

                            dialog.dispose();

                            break;
                    }
                }
            });
        buttonPanel.add(load);
        buttonPanel.add(save);

        return buttonPanel;
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        c.insets.top = 10;

        JPanel conventionPanel = createGeneralPane();
        SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(conventionPanel, c);
        add(conventionPanel);

        JPanel importExportPanel = createImportExportPane();
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 0, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 1.0, GridBagConstraints.NORTHWEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(importExportPanel, c);
        add(importExportPanel);
    }

    //~ Inner Classes ·························································

    private static class AddDialog
        extends JDialog
    {
        String value;

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

            JLabel valueLabel = new JLabel(text);
            c.insets.top = 10;
            c.insets.left = 5;
            c.insets.right = 5;
            SwingHelper.setConstraints(c, 0, 0, GridBagConstraints.REMAINDER, 1,
                                       1.0, 0.0, GridBagConstraints.WEST,
                                       GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
            layout.setConstraints(valueLabel, c);
            contentPane.add(valueLabel);

            final JTextField valueTextField = new JTextField(20);
            valueLabel.setLabelFor(valueTextField);
            c.insets.top = 2;
            SwingHelper.setConstraints(c, 0, 1, 12, 1, 1.0, 1.0,
                                       GridBagConstraints.WEST,
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
                        setVisible(false);

                        String contents = valueTextField.getText();

                        if (contents.length() == 0)
                        {
                            return;
                        }

                        AddDialog.this.value = contents;
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


    /**
     * JFileChooser filter for Jalopy preference files (.jal).
     */
    private static class JalopyFilter
        extends FileFilter
    {
        public String getDescription()
        {
            return "Binary preferences (*.jal)";
        }


        public boolean accept(File f)
        {
            if (f == null)
            {
                return false;
            }

            if (f.isDirectory())
            {
                return true;
            }

            if (f.getName().endsWith(Preferences.EXTENSION_JAL))
            {
                return true;
            }

            return false;
        }
    }


    /**
     * JFileChooser filter for Jalopy preference files (.xml).
     */
    private static class XmlFilter
        extends FileFilter
    {
        public String getDescription()
        {
            return "XML preferences (*.xml)";
        }


        public boolean accept(File f)
        {
            if (f == null)
            {
                return false;
            }

            if (f.isDirectory())
            {
                return true;
            }

            if (f.getName().endsWith(Preferences.EXTENSION_XML))
            {
                return true;
            }

            return false;
        }
    }
}
