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
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.ui.util.PopupSupport;
import de.hunsicker.util.Helper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * The main container that is used to display the preferences pages.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class PreferencesContainer
    extends JPanel
{
    //~ Instance variables ····················································

    /** Holds the currently displayed preferences page. */
    private AbstractPreferencesPanel _curPreferencesPanel;

    /** Used to display the title of the current preferences page. */
    private JLabel _titleLabel;

    /** Panel to add the active preferences page. */
    private JPanel _preferencesPanel;

    /** The used tree to select the different preferences pages. */
    private JTree _tree;

    /** Holds all property pages that have been displayed. */
    private Map _panels = new HashMap(10); // Map of <String>:<AbstractPreferencesPanel>

    /** Associates preview file names and their contents. */
    private Map _previews = new HashMap(); // Map of <String>:<String>

    /** Helper class to add popup menu support for text components. */
    private PopupSupport _popupSupport;

    /** The Jalopy preferences. */
    private Preferences _prefs;

    /** Displays a preview. */
    private PreviewFrame _previewFrame;

    /** Was this object destroyed? */
    private boolean _disposed;

    //~ Constructors ··························································

    /**
     * Creates a new PreferencesContainer object.
     *
     * @param dialog frame to display a preview file
     */
    public PreferencesContainer(PreviewFrame dialog)
    {
        _previewFrame = dialog;
        initialize();
    }

    //~ Methods ·······························································

    /**
     * Clears the panel cache.
     */
    public void clearCache()
    {
        _panels.clear();
    }


    /**
     * Releases allocated resources.
     */
    public void dispose()
    {
        if (!_disposed)
        {
            trackPanel();
            _popupSupport.setEnabled(false);
            _popupSupport = null;
            clearCache();
            _disposed = true;
        }
    }


    /**
     * Stores the settings of all contained preferences pages.
     *
     * @throws ValidationException if invalid settings were found.
     */
    public void store()
        throws ValidationException
    {
        for (Iterator i = _panels.values().iterator(); i.hasNext();)
        {
            AbstractPreferencesPanel page = (AbstractPreferencesPanel)i.next();
            page.validateSettings();
            page.store();
        }
    }


    /**
     * Creates the different nodes to select the preferences pages.
     *
     * @return the root node of the created node tree.
     */
    protected DefaultMutableTreeNode createNodes()
    {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("ROOT");
        DefaultMutableTreeNode general = new PreferencesNode(new PreferencesNodeInfo("general",
                                                                                     "General",
                                                                                     "de.hunsicker.jalopy.ui.GeneralPanel"));
        DefaultMutableTreeNode printer = new PreferencesNode(new PreferencesNodeInfo("printer",
                                                                                     "Printer",
                                                                                     "de.hunsicker.jalopy.ui.DummyPanel"));
        DefaultMutableTreeNode braces = new PreferencesNode(new PreferencesNodeInfo("braces",
                                                                                    "Braces",
                                                                                    "de.hunsicker.jalopy.ui.BracesPanel"));
        DefaultMutableTreeNode header = new PreferencesNode(new PreferencesNodeInfo("header",
                                                                                    "Header",
                                                                                    "de.hunsicker.jalopy.ui.HeaderPanel"));
        DefaultMutableTreeNode footer = new PreferencesNode(new PreferencesNodeInfo("footer",
                                                                                    "Footer",
                                                                                    "de.hunsicker.jalopy.ui.FooterPanel"));
        DefaultMutableTreeNode imports = new PreferencesNode(new PreferencesNodeInfo("imports",
                                                                                     "Imports",
                                                                                     "de.hunsicker.jalopy.ui.ImportsPanel"));
        DefaultMutableTreeNode indentation = new PreferencesNode(new PreferencesNodeInfo("indentation",
                                                                                         "Indentation",
                                                                                         "de.hunsicker.jalopy.ui.IndentationPanel"));
        DefaultMutableTreeNode separation = new PreferencesNode(new PreferencesNodeInfo("separation",
                                                                                        "Separation",
                                                                                        "de.hunsicker.jalopy.ui.SeparationPanel"));
        DefaultMutableTreeNode javadoc = new PreferencesNode(new PreferencesNodeInfo("javadoc",
                                                                                     "Javadoc",
                                                                                     "de.hunsicker.jalopy.ui.JavadocPanel"));
        DefaultMutableTreeNode messages = new PreferencesNode(new PreferencesNodeInfo("messages",
                                                                                      "Messages",
                                                                                      "de.hunsicker.jalopy.ui.MessagesPanel"));
        DefaultMutableTreeNode whitespace = new PreferencesNode(new PreferencesNodeInfo("whitespace",
                                                                                        "Whitespace",
                                                                                        "de.hunsicker.jalopy.ui.WhitespacePanel"));
        DefaultMutableTreeNode wrapping = new PreferencesNode(new PreferencesNodeInfo("wrapping",
                                                                                      "Wrapping",
                                                                                      "de.hunsicker.jalopy.ui.LineWrappingPanel"));
        DefaultMutableTreeNode comments = new PreferencesNode(new PreferencesNodeInfo("comments",
                                                                                      "Comments",
                                                                                      "de.hunsicker.jalopy.ui.CommentsPanel"));
        DefaultMutableTreeNode sort = new PreferencesNode(new PreferencesNodeInfo("sorting",
                                                                                  "Sorting",
                                                                                  "de.hunsicker.jalopy.ui.SortPanel"));
        DefaultMutableTreeNode misc = new PreferencesNode(new PreferencesNodeInfo("misc",
                                                                                  "Misc",
                                                                                  "de.hunsicker.jalopy.ui.MiscPanel"));
        DefaultMutableTreeNode environment = new PreferencesNode(new PreferencesNodeInfo("environment",
                                                                                         "Environment",
                                                                                         "de.hunsicker.jalopy.ui.EnvironmentPanel"));
        DefaultMutableTreeNode inspector = new PreferencesNode(new PreferencesNodeInfo("inspector",
                                                                                       "Code Inspector",
                                                                                       "de.hunsicker.jalopy.ui.DummyPanel"));
        DefaultMutableTreeNode naming = new PreferencesNode(new PreferencesNodeInfo("naming",
                                                                                    "Naming",
                                                                                    "de.hunsicker.jalopy.ui.NamingPanel"));
        DefaultMutableTreeNode tips = new PreferencesNode(new PreferencesNodeInfo("tips",
                                                                                  "Tips",
                                                                                  "de.hunsicker.jalopy.ui.TipsPanel"));
        DefaultMutableTreeNode projects = new PreferencesNode(new PreferencesNodeInfo("projects",
                                                                                      "Projects",
                                                                                      "de.hunsicker.jalopy.ui.ProjectPanel"));
        root.add(general);
        root.add(projects);
        root.add(printer);
        printer.add(braces);
        printer.add(whitespace);
        printer.add(indentation);
        printer.add(wrapping);
        printer.add(separation);
        printer.add(comments);
        printer.add(imports);
        printer.add(environment);
        printer.add(javadoc);
        printer.add(header);
        printer.add(footer);
        printer.add(sort);
        printer.add(misc);
        //root.add(inspector);
        inspector.add(naming);
        inspector.add(tips);
        root.add(messages);

        return root;
    }


    AbstractPreferencesPanel getCurrentPage()
    {
        return _curPreferencesPanel;
    }


    /**
     * Returns a handle to the preview frame.
     *
     * @return the preview frame.
     *
     * @since 1.0b8
     */
    PreviewFrame getPreview()
    {
        return _previewFrame;
    }


    void displayPreview()
    {
        displayPreview((PreferencesNode)_tree.getLastSelectedPathComponent());
    }


    /**
     * Loads the preview file with the given name.
     *
     * @param name the name of the preview file (without the file extension).
     *
     * @return the contents of the specified preview file or <code>null</code>
     *         if no preview file with the given extension exists.
     *
     * @since 1.0b8
     */
    String loadPreview(String name)
    {
        if (_previews.containsKey(name))
        {
            return (String)_previews.get(name);
        }

        Reader in = null;
        InputStream s = null;
        BufferedWriter out = null;

        try
        {
            s = getClass()
                    .getResourceAsStream("resources/" + name + ".java.tpl");

            if (s != null)
            {
                in = new InputStreamReader(s);

                char[] buf = new char[1024];
                CharArrayWriter buffer = new CharArrayWriter(1000);
                out = new BufferedWriter(buffer);

                for (int i = 0, len = 0;
                     (len = in.read(buf, 0, 1024)) != -1;
                     i++)
                {
                    out.write(buf, 0, len);
                }

                out.flush();

                String contents = buffer.toString();
                _previews.put(name, contents);

                return contents;
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                if (s != null)
                {
                    s.close();
                }
            }
            catch (IOException ignored)
            {
                ;
            }

            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ignored)
            {
                ;
            }

            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (IOException ignored)
            {
                ;
            }
        }

        return null;
    }


    private PreferencesNodeInfo getLast()
    {
        File file = new File(Preferences.getProjectSettingsDirectory(),
                             "page.dat");

        if (file.exists() && file.isFile())
        {
            try
            {
                return (PreferencesNodeInfo)IoHelper.deserialize(file);
            }
            catch (Throwable ignored)
            {
                ;
            }
        }

        return new PreferencesNodeInfo("general", "General",
                                       "de.hunsicker.jalopy.ui.GeneralPanel");
    }


    private String getTitle(DefaultMutableTreeNode node)
    {
        TreeNode[] path = node.getPath();
        StringBuffer buf = new StringBuffer(30);

        for (int i = 0; i < path.length; i++)
        {
            if (path[i].getParent() != null)
            {
                buf.append(path[i]);
                buf.append(" \u00B7 "); // middle dot
            }
        }

        buf.setLength(buf.length() - 3);

        return buf.toString();
    }


    private void displayPreview(PreferencesNode node)
    {
        if (!((DefaultMutableTreeNode)node.getParent()).isRoot())
        {
            PreferencesNode parent = (PreferencesNode)node.getParent();

            if (parent.getInfo().key.equals("printer"))
            {
                _previewFrame.setCurrentPage(_curPreferencesPanel);

                String text = loadPreview(_curPreferencesPanel.getPreviewFileName());

                if (text != null)
                {
                    _previewFrame.setText(text);

                    if (!_previewFrame.isVisible())
                    {
                        _previewFrame.setVisible(true);
                    }
                }
                else
                {
                    _previewFrame.setText("");
                }
            }
            else
            {
                _previewFrame.setVisible(false);
            }
        }
        else if (_previewFrame.isVisible())
        {
            _previewFrame.setVisible(false);
        }
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        _prefs = Preferences.getInstance();

        List supported = new ArrayList(3);
        supported.add("javax.");
        supported.add("de.hunsicker.");
        _popupSupport = new PopupSupport(supported);

        JPanel spacer = new JPanel();
        Dimension space = new Dimension(5, 5);
        spacer.setMaximumSize(space);
        spacer.setMinimumSize(space);
        spacer.setPreferredSize(space);

        PreferencesNodeInfo info = getLast();
        _titleLabel = new JLabel(info.title);
        _titleLabel.setFont(new Font(_titleLabel.getFont().getName(), Font.BOLD,
                                     _titleLabel.getFont().getSize()));
        _titleLabel.setBackground(Color.white);
        _titleLabel.setForeground(Color.black);

        JPanel titlePanel = new JPanel();
        titlePanel.add(_titleLabel);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(Color.white);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
                                                                BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                                                Color.white),
                                                                BorderFactory.createMatteBorder(0, 0, 1, 0,
                                                                                                new Color(
                                                                                                          132,
                                                                                                          130,
                                                                                                          132))));

        // load the default preferences page
        _curPreferencesPanel = loadPanel(info);
        _panels.put(info.key, _curPreferencesPanel);
        _preferencesPanel = new JPanel();
        _preferencesPanel.setLayout(new BorderLayout());
        _preferencesPanel.add(titlePanel, BorderLayout.NORTH);
        _preferencesPanel.add(_curPreferencesPanel, BorderLayout.CENTER);

        // init the tree view
        DefaultMutableTreeNode root = createNodes();
        _tree = new JTree(root);
        _tree.setRootVisible(false);
        _tree.setShowsRootHandles(true);
        _tree.putClientProperty("JTree.lineStyle", "Angled");
        _tree.setBorder(BorderFactory.createCompoundBorder(_tree.getBorder(),
                                                           BorderFactory.createEmptyBorder(5, 0, 5, 5)));

        DefaultTreeCellRenderer r = (DefaultTreeCellRenderer)_tree.getCellRenderer();
        r.setClosedIcon(null);
        r.setOpenIcon(null);
        r.setLeafIcon(null);

        // restore selection of the last session
        for (Enumeration childs = root.preorderEnumeration();
             childs.hasMoreElements();)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)childs.nextElement();
            TreePath path = new TreePath(node.getPath());
            _tree.expandPath(path);

            if (node.getUserObject().equals(info.title))
            {
                _tree.setSelectionPath(path);
                _titleLabel.setText(getTitle(node));
            }
        }

        JScrollPane treeView = new JScrollPane();
        treeView.setPreferredSize(new Dimension(130, 400));
        treeView.getViewport().add(_tree, null);
        _tree.addTreeSelectionListener(new TreeSelectionHandler());

        JPanel propertyView = new JPanel();
        propertyView.setLayout(new BorderLayout());
        propertyView.setPreferredSize(new Dimension(430, 450));
        propertyView.add(spacer, BorderLayout.WEST);
        propertyView.add(_preferencesPanel, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(treeView, BorderLayout.WEST);
        add(propertyView, BorderLayout.CENTER);
    }


    private AbstractPreferencesPanel loadPanel(PreferencesNodeInfo info)
    {
        try
        {
            Class[] params ={ PreferencesContainer.class };
            Constructor c = info.getPanelClass().getDeclaredConstructor(params);
            Object[] args ={ this };
            AbstractPreferencesPanel panel = (AbstractPreferencesPanel)c.newInstance(args);
            panel.setTitle(info.title);
            panel.setCategory(info.key);

            return panel;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return null;
    }


    /**
     * Tracks the currently displayed panel.
     */
    private void trackPanel()
    {
        File directory = Preferences.getProjectSettingsDirectory();
        File file = new File(directory, "page.dat");

        if (IoHelper.ensureDirectoryExists(directory))
        {
            try
            {
                PreferencesNodeInfo info = new PreferencesNodeInfo(_curPreferencesPanel.getCategory(),
                                                                   _curPreferencesPanel.getTitle(),
                                                                   _curPreferencesPanel.getClass()
                                                                                       .getName());
                IoHelper.serialize(info, file);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                file.delete();
            }
        }
    }

    //~ Inner Classes ·························································

    /**
     * Our customized tree node.
     */
    private static class PreferencesNode
        extends DefaultMutableTreeNode
    {
        /**
         * Creates a new PreferencesNode object.
         *
         * @param userObj the node information
         */
        public PreferencesNode(Object userObj)
        {
            super(userObj);
        }

        /**
         * Returns the node information.
         *
         * @return node information object.
         */
        public PreferencesNodeInfo getInfo()
        {
            return (PreferencesNodeInfo)getUserObject();
        }
    }


    /**
     * Helper class which provides the necessary information for every
     * property node. This is the user object for the JTree.
     */
    private static class PreferencesNodeInfo
        implements Serializable
    {
        /** Use serialVersionUID for interoperability. */
        static final long serialVersionUID = 4496045479306791488L;
        transient Class panelClass;
        String className;
        String key;
        String title;
        int hashCode;

        public PreferencesNodeInfo(String key,
                                   String title,
                                   String className)
        {
            this.key = key;
            this.className = className;
            this.title = title;
            this.hashCode = title.hashCode();
        }

        /**
         * Returns the class to load for the preferences page.
         *
         * @return the class to load for the preferences page.
         */
        public Class getPanelClass()
        {
            if (this.panelClass == null)
            {
                try
                {
                    this.panelClass = Helper.loadClass(this.className, this);
                }
                catch (ClassNotFoundException ex)
                {
                    ex.printStackTrace();
                }
            }

            return this.panelClass;
        }


        public boolean equals(Object o)
        {
            if (o == this)
            {
                return true;
            }

            // we only compare titles so that we can easily search the tree
            if (o instanceof String)
            {
                return this.title.equals(o);
            }

            return false;
        }


        public int hashCode()
        {
            return this.hashCode;
        }


        public String toString()
        {
            return this.title;
        }
    }


    /**
     * Handler for tree selection events. Displays the selected preferences
     * page.
     */
    private class TreeSelectionHandler
        implements TreeSelectionListener
    {
        boolean armed = true;

        public void valueChanged(TreeSelectionEvent ev)
        {
            if (this.armed)
            {
                PreferencesNode node = (PreferencesNode)_tree.getLastSelectedPathComponent();

                // do nothing if no node is selected
                if (node == null)
                {
                    return;
                }

                try
                {
                    _curPreferencesPanel.validateSettings();
                }
                catch (ValidationException ex)
                {
                    // we don't want the selection to change, so revert to the
                    // old path
                    this.armed = false;
                    _tree.setSelectionPath(ev.getOldLeadSelectionPath());

                    return;
                }

                // update the title
                String title = getTitle(node);
                _titleLabel.setText(title);
                _preferencesPanel.remove(_curPreferencesPanel);

                PreferencesNodeInfo info = node.getInfo();

                if (_panels.containsKey(info.key))
                {
                    // load panel from cache
                    _curPreferencesPanel = (AbstractPreferencesPanel)_panels.get(info.key);
                }
                else
                {
                    _curPreferencesPanel = loadPanel(info);

                    // update cache
                    _panels.put(info.key, _curPreferencesPanel);
                }

                displayPreview(node);
                _preferencesPanel.add(_curPreferencesPanel, BorderLayout.CENTER);
                _preferencesPanel.repaint();
            }
            else
            {
                this.armed = true;
            }
        }
    }
}
