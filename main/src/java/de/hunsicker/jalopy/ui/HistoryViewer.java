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
import de.hunsicker.jalopy.storage.Convention;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


/**
 * A simple, tree-like history viewer component.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class HistoryViewer
    extends JPanel
{
    //~ Instance variables ····················································

    private DateFormat _dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                                       DateFormat.SHORT,
                                                                       Locale.getDefault());
    private DefaultTreeModel _treeModel;
    private HistoryTreeNode _rootNode;
    private JTree _tree;
    private boolean _caseSensitive;

    //~ Constructors ··························································

    /**
     * Creates a new HistoryViewer object.
     */
    public HistoryViewer()
    {
        initialize();
    }

    //~ Methods ·······························································

    /**
     * Notifies this component that it no longer has a parent component. When
     * this method is invoked, the history will be flushed.
     *
     * @see History#flush
     */
    public void removeNotify()
    {
        super.removeNotify();

        try
        {
            History.getInstance().flush();
        }
        catch (IOException ignored)
        {
            ;
        }
    }


    private int getIndex(HistoryTreeNode node,
                         HistoryTreeNode parent)
    {
        int result = 0;

        for (Enumeration childs = parent.children(); childs.hasMoreElements();)
        {
            HistoryTreeNode child = (HistoryTreeNode)childs.nextElement();
            int compare = node.compareTo(child);

            if (compare < 0)
            {
                break;
            }

            result++;
        }

        return result;
    }


    /**
     * Adds the given path to the given parent, if not already contained.
     *
     * @param parent parent to add the path to.
     * @param path the path to add.
     * @param entry history entry.
     */
    private void addAsChild(HistoryTreeNode parent,
                            String          path,
                            History.Entry   entry)
    {
        int offset = path.indexOf(File.separatorChar);

        if (offset > -1)
        {
            String ident = path.substring(0, offset);
            int index = contains(parent, ident);

            if (index > -1)
            {
                addAsChild((HistoryTreeNode)parent.getChildAt(index),
                           path.substring(offset + 1), entry);
            }
            else
            {
                HistoryTreeNode node = new HistoryTreeNode(ident);
                _treeModel.insertNodeInto(node, parent, getIndex(node, parent));
                addAsChild(node, path.substring(offset + 1), entry);
            }
        }
        else
        {
            HistoryTreeNode node = new HistoryTreeNode(path,
                                                       entry.getModification());

            if (contains(parent, node) == -1)
            {
                _treeModel.insertNodeInto(node, parent, getIndex(node, parent));
            }
        }
    }


    /**
     * Indicates whether the given parent contains the given child.
     *
     * @param parent parent to check for child.
     * @param child a node.
     *
     * @return the index of child within parent or <code>-1</code> if child is
     *         no child of parent.
     */
    private int contains(HistoryTreeNode parent,
                         HistoryTreeNode child)
    {
        return contains(parent, child.name);
    }


    /**
     * Indicates whether the given parent contains the given child.
     *
     * @param parent parent to check for child.
     * @param name the name of a node.
     *
     * @return the index of child within parent or <code>-1</code> if child is
     *         no child of parent.
     */
    private int contains(HistoryTreeNode parent,
                         String          name)
    {
        for (Enumeration i = parent.children(); i.hasMoreElements();)
        {
            HistoryTreeNode c = (HistoryTreeNode)i.nextElement();

            if (_caseSensitive)
            {
                if (c.name.equals(name))
                {
                    return parent.getIndex(c);
                }
            }
            else
            {
                if (c.name.equalsIgnoreCase(name))
                {
                    return parent.getIndex(c);
                }
            }
        }

        return -1;
    }


    /**
     * Initialization.
     */
    private void initialize()
    {
        setPreferredSize(new Dimension(350, 400));

        /**
         * @todo what OSes are actually case-aware?
         */
        _caseSensitive = File.separatorChar == '/';
        _rootNode = new HistoryTreeNode("History");
        _tree = new JTree(_rootNode);
        _tree.putClientProperty("JTree.lineStyle", "Angled");
        _tree.setScrollsOnExpand(true);
        _tree.setLargeModel(true);

        DefaultTreeCellRenderer r = new HistoryTreeCellRenderer();
        _tree.setCellRenderer(r);
        _treeModel = (DefaultTreeModel)_tree.getModel();

        JScrollPane scrollPane = new JScrollPane(_tree);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setDefaultLightWeightPopupEnabled(true);
        popupMenu.setLightWeightPopupEnabled(true);

        final Action removeAction = new RemoveItemAction();
        removeAction.setEnabled(false);
        popupMenu.add(removeAction);
        _tree.addMouseListener(new MouseAdapter()
            {
                public void mouseReleased(MouseEvent ev)
                {
                    if (ev.isPopupTrigger())
                    {
                        Object selection = _tree.getLastSelectedPathComponent();

                        if ((selection != null) && (selection != _rootNode))
                        {
                            removeAction.setEnabled(true);
                        }
                        else
                        {
                            removeAction.setEnabled(false);
                        }

                        java.awt.Point point = new java.awt.Point(ev.getX(),
                                                                  ev.getY());
                        SwingUtilities.convertPointToScreen(point, _tree);
                        popupMenu.setInvoker(_tree);
                        popupMenu.setLocation(point.x, point.y);
                        popupMenu.setVisible(true);
                    }
                }
            });

        /*try
        {
            // flush the history to reflect the latest changes
            History.getInstance().flush();
        }
        catch (IOException ignored)
        {
            ;
        }*/
        Map history = loadHistory();
        populateTree(history);
        _tree.expandRow(0);
    }


    /**
     * Loads the history from persistent storage.
     *
     * @return the history.
     */
    private Map loadHistory()
    {
        Map history = null;

        try
        {
            File file = Convention.getHistoryFile();

            if (file.exists())
            {
                history = new HashMap((Map)IoHelper.deserialize(file));
            }
            else
            {
                history = new HashMap();
            }
        }
        catch (Throwable ex)
        {
            history = new HashMap();
        }

        return history;
    }


    /**
     * Builds the history tree from the given map.
     *
     * @param history map with the history data.
     */
    private void populateTree(Map history)
    {
        for (Iterator i = history.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry)i.next();
            addAsChild(_rootNode, (String)entry.getKey(),
                       (History.Entry)entry.getValue());
        }
    }


    /**
     * Removes the given tree path.
     *
     * @param path path to remove.
     */
    private void removeChildren(TreePath path)
    {
        HistoryTreeNode node = (HistoryTreeNode)path.getLastPathComponent();
        removeChildren(node);
    }


    /**
     * Removes the given node and all child nodes.
     *
     * @param node node to remove from the tree.
     */
    private void removeChildren(HistoryTreeNode node)
    {
        if (node.isLeaf())
        {
            try
            {
                History.getInstance().remove(node.toFile());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        else
        {
            for (Enumeration i = node.children(); i.hasMoreElements();)
            {
                removeChildren((HistoryTreeNode)i.nextElement());
            }
        }
    }


    /**
     * Removes the given path from the tree.
     *
     * @param path path to remove.
     */
    private void removePath(TreePath path)
    {
        TreePath parent = path.getParentPath();

        if (parent != null)
        {
            removeChildren(path);
            _treeModel.removeNodeFromParent((HistoryTreeNode)path.getLastPathComponent());
        }
    }

    //~ Inner Classes ·························································

    private static class HistoryTreeCellRenderer
        extends DefaultTreeCellRenderer
    {
        Icon rootIcon;

        public HistoryTreeCellRenderer()
        {
            this.rootIcon = new ImageIcon(getClass()
                                              .getResource("resources/History16.gif"));
        }

        public Component getTreeCellRendererComponent(JTree   tree,
                                                      Object  value,
                                                      boolean sel,
                                                      boolean expanded,
                                                      boolean leaf,
                                                      int     row,
                                                      boolean hasFocus)
        {
            if (row == 0)
            {
                super.getTreeCellRendererComponent(tree, value, sel, expanded,
                                                   leaf, row, hasFocus);
                setIcon(this.rootIcon);

                return this;
            }
            else
            {
                return super.getTreeCellRendererComponent(tree, value, sel,
                                                          expanded, leaf, row,
                                                          hasFocus);
            }
        }
    }


    /**
     * Represents a history entry. Either a path part (just the name) or the
     * file name (name + timestamp).
     */
    private class HistoryTreeNode
        extends DefaultMutableTreeNode
        implements Comparable
    {
        String name;
        long lastmod = -1;

        public HistoryTreeNode(String name)
        {
            this.name = name;
        }


        public HistoryTreeNode(String name,
                               long   lastmod)
        {
            this.lastmod = lastmod;
            this.name = name;
        }

        public int compareTo(Object o)
        {
            if (o instanceof String)
            {
                return this.name.compareTo((String)o);
            }
            else if (o instanceof HistoryTreeNode)
            {
                return this.name.compareTo(((HistoryTreeNode)o).name);
            }
            else
            {
                throw new ClassCastException();
            }
        }


        public boolean equals(Object o)
        {
            if (o instanceof String)
            {
                if (_caseSensitive)
                {
                    return this.name.equals((String)o);
                }
                else
                {
                    return this.name.equalsIgnoreCase((String)o);
                }
            }
            else
            {
                if (_caseSensitive)
                {
                    return this.name.equals(((HistoryTreeNode)o).name);
                }
                else
                {
                    return this.name.equalsIgnoreCase(((HistoryTreeNode)o).name);
                }
            }
        }


        public int hashCode()
        {
            if (_caseSensitive)
            {
                return this.name.hashCode();
            }
            else
            {
                return this.name.toLowerCase().hashCode();
            }
        }


        public File toFile()
        {
            StringBuffer buf = new StringBuffer(50);
            Object[] p = getPath();

            // skip the first component: the root node of the tree
            for (int i = 1; i < p.length; i++)
            {
                buf.append(p[i]);
                buf.append(File.separatorChar);
            }

            buf.deleteCharAt(buf.length() - 1);

            String filename = buf.toString();

            if (filename.endsWith("]"))
            {
                filename = filename.substring(0, filename.lastIndexOf('['));
            }

            return new File(filename);
        }


        public String toString()
        {
            if (this.lastmod > -1)
            {
                StringBuffer buf = new StringBuffer(20);
                buf.append(this.name);
                buf.append(" [");
                buf.append(_dateFormatter.format(new Date(this.lastmod)));
                buf.append(']');

                return buf.toString();
            }
            else
            {
                return this.name;
            }
        }
    }


    /**
     * Action which removes selected tree items.
     */
    private class RemoveItemAction
        extends AbstractAction
    {
        public RemoveItemAction()
        {
            super.putValue(Action.NAME, "Remove");
        }

        public void actionPerformed(ActionEvent ev)
        {
            TreePath[] selections = _tree.getSelectionPaths();

            if (selections != null)
            {
                for (int i = 0; i < selections.length; i++)
                {
                    TreePath selection = selections[i];
                    removePath(selection);
                }
            }
        }
    }
}
