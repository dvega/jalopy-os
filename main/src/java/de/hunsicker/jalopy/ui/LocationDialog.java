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
import de.hunsicker.ui.util.SwingHelper;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;


/**
 * Custom dialog used to select a local or distributed location. The dialog
 * manages a history.
 */
class LocationDialog
    extends JDialog
{
    //~ Static variables/initializers ·········································

    /** DOCUMENT ME! */
    public static final int ACCEPT_ALL = 8;

    /** DOCUMENT ME! */
    public static final int DIRECTORIES_ONLY = 4;

    /** DOCUMENT ME! */
    public static final int FILES_AND_DIRECTORIES = 2;

    /** DOCUMENT ME! */
    public static final int FILES_ONLY = 1;

    /** DOCUMENT ME! */
    public static final int SELECTION_MULTI = 16;

    /** The empty string array. */
    protected static final String[] EMPTY_STRING_ARRAY = new String[0];

    /** Delimeter string for tokenizing the history entries. */
    protected static final String URL_DELIMETER = "|";

    //~ Instance variables ····················································

    /** The currently active file filter. */
    protected FileFilter fileFilter;

    /** The combo box which provides the history items. */
    protected JComboBox locationComboBox;

    /** List with file filters. */
    protected List filters = Collections.EMPTY_LIST; // List of <FileFilter>

    /** Size of the history (Number of items in the combo box). */
    protected int maxHistoryItems = 10;

    /** Holds the state after the dialog was closed. */
    protected int state;

    /** Indicates wether this dialog was destroyed. */
    private boolean _disposed;

    /** The properties mask for the file chooser. */
    private int _chooserProperties;

    //~ Constructors ··························································

    /**
     * Creates a new LocationDialog object.
     *
     * @param owner the <code>Frame</code> from which the dialog is displayed.
     * @param title the string to display in the title bar.
     * @param text the text to display above the combo box (not yet
     *        implemented)
     * @param history the delimeted location history.
     *
     * @todo implement the addition of a custom text message.
     */
    public LocationDialog(Frame  owner,
                          String title,
                          String text,
                          String history)
    {
        super(owner);
        initialize(title, text, history);
        setLocationRelativeTo(owner);
    }


    /**
     * Creates a new LocationDialog object.
     *
     * @param owner the <code>Dialog</code> from which the dialog is
     *        displayed.
     * @param title the string to display in the title bar.
     * @param text the text to display above the combo box (not yet
     *        implemented)
     * @param history the delimeted location history.
     */
    public LocationDialog(Dialog owner,
                          String title,
                          String text,
                          String history)
    {
        super(owner);
        initialize(title, text, history);
        setLocationRelativeTo(owner);
    }

    //~ Methods ·······························································

    /**
     * Loads the history from the given file.
     *
     * @param file the history file.
     *
     * @return the url history stored in the given file. Returns the empty
     *         string if the given file did not exist or an error occurred
     *         during the reading process.
     *
     * @since 1.0b8
     */
    public static String loadHistory(File file)
    {
        if (file.exists() && file.isFile())
        {
            try
            {
                return (String)IoHelper.deserialize(file);
            }
            catch (IOException ignored)
            {
                ;
            }
        }

        return "";
    }


    /**
     * Stores the given history urls in the given file.
     *
     * @param file file to store the history urls.
     * @param urls string with the history urls.
     *
     * @since 1.0b8
     */
    public static void storeHistory(File   file,
                                    String urls)
    {
        ObjectOutputStream out = null;

        try
        {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            out.writeObject(urls);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException ex)
                {
                    ;
                }
            }
        }
    }


    /**
     * Sets up the file chooser with the given properties mask.
     *
     * @param mask properties mask.
     */
    public void setFileChooserProperties(int mask)
    {
        _chooserProperties = mask;
    }


    /**
     * Returns the last selected file filter of the file chooser.
     *
     * @return last selected file filter. Returns <code>null</code> if no file
     *         chooser has been displayed.
     */
    public FileFilter getFileFilter()
    {
        return this.fileFilter;
    }


    /**
     * Gets a delimeted history string, encoding the locations.
     *
     * @return delimeted string encoding the loations.
     */
    public String getHistoryString()
    {
        return getHistoryString(getLocations(), getSelectedLocation());
    }


    /**
     * Returns the locations of the dialog.
     *
     * @return locations.
     */
    public Object[] getLocations()
    {
        Object[] result = new Object[this.locationComboBox.getItemCount()];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = this.locationComboBox.getItemAt(i);
        }

        return result;
    }


    /**
     * Returns the user selected option.
     *
     * @return user selected option. Either <ul><li>{@link
     *         JOptionPane#OK_OPTION} or</li> <li>{@link
     *         JOptionPane#CANCEL_OPTION}</li> </ul>
     */
    public int getOption()
    {
        return this.state;
    }


    /**
     * Returns the selection location.
     *
     * @return selected location.
     */
    public Object getSelectedLocation()
    {
        return this.locationComboBox.getSelectedItem();
    }


    /**
     * Adds a FileFilter to the FileChooser.
     *
     * @param filter filter to add.
     */
    public void addFilter(FileFilter filter)
    {
        addFilter(filter, false);
    }


    /**
     * Adds a new FileFilter to the FileChooser.
     *
     * @param filter filter to add.
     * @param standard if <code>true</code> the filter will be added as the
     *        new standard filter.
     */
    public void addFilter(FileFilter filter,
                          boolean    standard)
    {
        if (this.filters.isEmpty())
        {
            this.filters = new ArrayList(4);
        }

        if (standard)
        {
            this.filters.add(0, filter);
        }
        else
        {
            this.filters.add(filter);
        }
    }


    /**
     * Release system resources.
     */
    public void dispose()
    {
        super.dispose();

        if (!_disposed)
        {
            this.locationComboBox = null;
            this.fileFilter = null;
            this.filters.clear();
            this.filters = null;
            _disposed = true;
        }
    }


    /**
     * Creates a mew FileChooser. The file chooser only allows single files to
     * be selected
     *
     * @return FileChooser.
     */
    protected JFileChooser createFileChooser()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(getTitle());
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setMultiSelectionEnabled(false);
        setupFileChooser(chooser, _chooserProperties);
        setCurrentDirectory(chooser);

        for (int i = LocationDialog.this.filters.size() - 1; i >= 0; i--)
        {
            if (i == 0)
            {
                chooser.setFileFilter((FileFilter)LocationDialog.this.filters.get(i));
            }
            else
            {
                chooser.addChoosableFileFilter((FileFilter)LocationDialog.this.filters.get(i));
            }
        }

        return chooser;
    }


    /**
     * Returns the panel which holds the buttons.
     *
     * @return the panel containing the buttons.
     */
    private JPanel getButtonPanel()
    {
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    LocationDialog.this.state = JOptionPane.OK_OPTION;
                    setVisible(false);
                }
            });
        getRootPane().setDefaultButton(okButton);
        SwingHelper.setConstraints(c, 0, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(okButton, c);
        panel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    LocationDialog.this.state = JOptionPane.CANCEL_OPTION;
                    setVisible(false);
                }
            });
        c.insets.left = 5;
        SwingHelper.setConstraints(c, 1, 0, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(cancelButton, c);
        panel.add(cancelButton);

        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    JFileChooser chooser = createFileChooser();

                    switch (SwingHelper.showJFileChooser(chooser,
                                                         LocationDialog.this,
                                                         "OK"))
                    {
                        case JFileChooser.APPROVE_OPTION :

                            File file = chooser.getSelectedFile();
                            Object item = file.getAbsolutePath();
                            LocationDialog.this.locationComboBox.removeItem(item);
                            LocationDialog.this.locationComboBox.insertItemAt(item, 0);
                            LocationDialog.this.locationComboBox.setSelectedIndex(0);

                            int items = LocationDialog.this.locationComboBox.getItemCount();

                            if (items > LocationDialog.this.maxHistoryItems)
                            {
                                LocationDialog.this.locationComboBox.removeItemAt(items -
                                                                                  1);
                            }

                            break;
                    }

                    LocationDialog.this.fileFilter = chooser.getFileFilter();
                }
            });
        SwingHelper.setConstraints(c, 2, 0, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(browseButton, c);
        panel.add(browseButton);

        return panel;
    }


    /**
     * Sets the current directory for the given file chooser according to the
     * state of the combobox with the history entries.
     *
     * @param chooser file chooser to set the directory for.
     */
    private void setCurrentDirectory(JFileChooser chooser)
    {
        String item = (String)LocationDialog.this.locationComboBox.getSelectedItem();

        if (item != null)
        {
            File file = new File(item);

            if (file.exists())
            {
                setCurrentDirectory(chooser, file);
            }
            else
            {
                String[] urls = getHistoryUrls(LocationDialog.this.locationComboBox);

                for (int i = 0; i < urls.length; i++)
                {
                    file = new File(urls[i]);

                    if (file.exists())
                    {
                        setCurrentDirectory(chooser, file);

                        break;
                    }
                }
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param chooser DOCUMENT ME!
     * @param file DOCUMENT ME!
     */
    private void setCurrentDirectory(JFileChooser chooser,
                                     File         file)
    {
        if (!file.isDirectory())
        {
            chooser.setCurrentDirectory(file.getParentFile());
        }
        else
        {
            chooser.setCurrentDirectory(file);
        }
    }


    /**
     * Gets a delimeted history string, encoding the given information.
     *
     * @param urls urls to store.
     * @param selectedUrl currently user selected url (saved on the front of
     *        the list)
     *
     * @return delimeted string encoding the urls.
     */
    private String getHistoryString(Object[] urls,
                                    Object   selectedUrl)
    {
        StringBuffer buf = new StringBuffer(300);
        buf.append(selectedUrl);
        buf.append(URL_DELIMETER);

        int maxItems = 10;

        for (int i = 0; (i < urls.length) && (i < maxItems); i++)
        {
            if (urls[i] == selectedUrl)
            {
                maxItems--;

                continue;
            }

            buf.append(urls[i]);
            buf.append(URL_DELIMETER);
        }

        buf.deleteCharAt(buf.length() - 1);

        return buf.toString();
    }


    /**
     * DOCUMENT ME!
     *
     * @param history DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String[] getHistoryUrls(String history)
    {
        List result = new ArrayList();

        for (StringTokenizer tokens = new StringTokenizer(history,
                                                          URL_DELIMETER);
             tokens.hasMoreElements();)
        {
            String token = tokens.nextToken().trim();

            if (token.length() > 0)
            {
                result.add(token);
            }
        }

        return (String[])result.toArray(EMPTY_STRING_ARRAY);
    }


    /**
     * DOCUMENT ME!
     *
     * @param combo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private String[] getHistoryUrls(JComboBox combo)
    {
        String[] result = new String[combo.getItemCount()];

        for (int i = 0; i < result.length; i++)
        {
            result[i] = (String)combo.getItemAt(i);
        }

        return result;
    }


    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param text DOCUMENT ME!
     * @param history DOCUMENT ME!
     */
    private void initialize(String title,
                            String text,
                            String history)
    {
        setTitle(title);
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new Dimension(350, 80));

        Container contentPane = getContentPane();
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);

        GridBagConstraints c = new GridBagConstraints();
        JLabel label = new JLabel("Open:");
        c.insets.top = 5;
        c.insets.left = 5;
        SwingHelper.setConstraints(c, 0, 1, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(label, c);
        contentPane.add(label);

        String[] urls = getHistoryUrls(history);
        this.locationComboBox = new JComboBox(urls);
        this.locationComboBox.setEditable(true);

        if (urls.length > 0)
        {
            this.locationComboBox.setToolTipText(urls[0]);
        }

        this.locationComboBox.addItemListener(new ItemListener()
            {
                public void itemStateChanged(ItemEvent ev)
                {
                    switch (ev.getStateChange())
                    {
                        case ItemEvent.SELECTED :
                            LocationDialog.this.locationComboBox.setToolTipText((String)LocationDialog.this.locationComboBox.getSelectedItem());

                            break;
                    }
                }
            });
        c.insets.left = 10;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 1, 1, GridBagConstraints.REMAINDER, 1,
                                   1.0, 0.0, GridBagConstraints.WEST,
                                   GridBagConstraints.HORIZONTAL, c.insets, 0, 0);
        layout.setConstraints(this.locationComboBox, c);
        contentPane.add(this.locationComboBox);
        c.insets.left = 5;
        c.insets.right = 5;
        c.insets.top = 10;
        c.insets.bottom = 5;

        JPanel buttonPanel = getButtonPanel();
        SwingHelper.setConstraints(c, 1, 2, GridBagConstraints.REMAINDER, 1,
                                   0.0, 0.0, GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(buttonPanel, c);
        contentPane.add(buttonPanel);
        pack();
    }


    /**
     * Sets up the given file chooser.
     *
     * @param chooser file chooser to setup.
     * @param mask properties mask.
     */
    private void setupFileChooser(JFileChooser chooser,
                                  int          mask)
    {
        switch (mask)
        {
            case FILES_ONLY :
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            case FILES_AND_DIRECTORIES :
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

                break;

            case DIRECTORIES_ONLY :
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                break;

            case SELECTION_MULTI :
                chooser.setMultiSelectionEnabled(true);

                break;

            case ACCEPT_ALL :
                chooser.setAcceptAllFileFilterUsed(true);

                break;
        }
    }
}
