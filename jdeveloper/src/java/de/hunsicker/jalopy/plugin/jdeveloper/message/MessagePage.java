/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.message;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import oracle.ide.Ide;
import oracle.ide.editor.Editor;
import oracle.ide.editor.EditorManager;
import oracle.ide.layout.ViewId;
import oracle.ide.log.AbstractLogPage;
import oracle.ide.model.NodeFactory;
import oracle.ide.model.TextNode;
import oracle.ide.net.URLFactory;
import oracle.jdeveloper.ceditor.CodeEditor;


/**
 * The JDeveloper log page that displayes messages in a hierachical tree.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class MessagePage
    extends AbstractLogPage
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final String FORMAT_UNIX = "\n" /* NOI18N */;
    private static final String FORMAT_DOS = "\r\n" /* NOI18N */;
    private static final String FORMAT_MAC = "\r" /* NOI18N */;

    //~ Instance variables ---------------------------------------------------------------

    /** Our tree model. */
    private DefaultTreeModel _treeModel;

    /** The file node that was last added to the tree. */
    private FileTreeNode _curFileNode;

    /** The scroll pane that holds the tree. */
    private JScrollPane _scrollPane;

    /** The tree used to display the messages. */
    private JTree _tree;

    /** The root node of the tree. */
    private MessageTreeNode _rootNode;

    /** The path to the root node. */
    private TreePath _rootPath;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JalopyMessagePage object.
     *
     * @param id the view id.
     * @param icon the icon of the page, possibly <code>null</code>.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public MessagePage(
        ViewId id,
        Icon   icon)
    {
        super(id, icon, false);

        _rootNode =
            new MessageTreeNode(new Message("" /* NOI18N */), MessageTreeNode.Type.ROOT);
        _rootPath = new TreePath(_rootNode);
        _tree = new JTree(_rootNode);

        try
        {
            _tree.setCellRenderer(new MessageTreeCellRenderer());
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }

        _tree.setRootVisible(false);
        _treeModel = (DefaultTreeModel) _tree.getModel();
        _scrollPane = new JScrollPane(_tree);

        _tree.addTreeSelectionListener(
            new TreeSelectionListener()
            {
                public void valueChanged(TreeSelectionEvent ev)
                {
                    TreePath[] paths = ev.getPaths();

                    for (int i = 0; i < paths.length; i++)
                    {
                        if (ev.isAddedPath(i))
                        {
                            MessageTreeNode node =
                                (MessageTreeNode) paths[i].getLastPathComponent();

                            if (
                                node instanceof FileTreeNode
                                && (node.getChildCount() == 0))
                            {
                                FileTreeNode parent = (FileTreeNode) node;

                                for (
                                    int j = 0, size = parent.children.size(); j < size;
                                    j++)
                                {
                                    MessageTreeNode child =
                                        createMessageNode(
                                            (Message) parent.children.get(j));

                                    _treeModel.insertNodeInto(
                                        child, parent, parent.getChildCount());
                                }

                                parent.children.clear();
                            }
                        }
                    }
                }
            });

        _tree.addMouseListener(
            new MouseAdapter()
            {
                public void mousePressed(MouseEvent ev)
                {
                    Object n = _tree.getLastSelectedPathComponent();

                    if (n != null)
                    {
                        MessageTreeNode node = (MessageTreeNode) n;
                        final Message message = (Message) node.getUserObject();

                        if ((message != null) && (message.line > 0))
                        {
                            if (message.file.exists())
                            {
                                URL url = URLFactory.newFileURL(message.file);
                                EditorManager editorManager = Ide.getEditorManager();
                                Editor editor =
                                    editorManager.openDefaultEditorInFrame(url);

                                if (editor instanceof CodeEditor)
                                {
                                    TextNode textnode = (TextNode) NodeFactory.find(url);
                                    ((CodeEditor) editor).gotoLine(
                                        message.line, message.startOffset, true);
                                }
                            }
                        }
                    }
                }
            });

        Ide.getLogManager().addPage(this);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Get the GUI to be used to display the state of this page.
     *
     * @return the view's root graphical user interface component.
     */
    public Component getGUI()
    {
        return _scrollPane;
    }


    /**
     * Clears all messages.
     */
    public void clearAll()
    {
        super.clearAll();

        _rootNode.removeAllChildren();
        _treeModel.reload();
        _curFileNode = null;
    }


    /**
     * Logs the given message.
     *
     * @param message a message object.
     */
    public void logMsg(Object message)
    {
        addMessage((Message) message, false);
    }


    /**
     * Updates the message view.
     */
    public void update()
    {
        _treeModel.reload();
    }


    /**
     * Sets the current view.
     *
     * @param component the component to set as the current view.
     */
    protected final void setCurrentView(Component component)
    {
        _scrollPane.setViewportView(component);
    }


    /**
     * Returns the currrent view.
     *
     * @return the current view.
     */
    protected final Component getCurrentView()
    {
        JViewport viewport = _scrollPane.getViewport();

        return (viewport != null) ? viewport.getView()
                                  : null;
    }


    /**
     * Determines whether and what kind of line separator the given string contains.
     *
     * @param str string to check for line separators.
     *
     * @return the line separator contained in the given string. Returns
     *         <code>null</code> if the string does not contain a line separator.
     */
    private static String getLineSeparator(String str)
    {
        if (str.indexOf(FORMAT_DOS) > -1)
        {
            return FORMAT_DOS;
        }
        else if (str.indexOf('\n') > -1)
        {
            return FORMAT_UNIX;
        }
        else if (str.indexOf('\r') > -1)
        {
            return FORMAT_MAC;
        }
        else
        {
            return null;
        }
    }


    /**
     * Adds a message to the tree.
     *
     * @param message the message to add.
     * @param refresh if <code>true</code> the tree will be refreshed immediately.
     */
    private void addMessage(
        final Message message,
        final boolean refresh)
    {
        if (message.file != null)
        {
            // the file node that will serve as the parent for the message
            FileTreeNode parent = null;

            // check whether the parent already exists
            if (_curFileNode != null)
            {
                // if the message should be added to the current file node,
                // we can ommit further searching
                if (message.file.equals(_curFileNode.file))
                {
                    parent = _curFileNode;
                }
                else
                {
                    // so search the tree to find the file node for the
                    // message
                    for (int i = 0, size = _rootNode.getChildCount(); i < size; i++)
                    {
                        Object node = _rootNode.getChildAt(i);

                        if (node instanceof FileTreeNode)
                        {
                            FileTreeNode fileNode = (FileTreeNode) node;

                            if (message.file.equals(fileNode.file))
                            {
                                parent = fileNode;

                                break;
                            }
                        }
                    }
                }
            }

            // no parent found, create a new one
            if (parent == null)
            {
                parent = createFileNode(message, _treeModel);

                // if the user specified a custom cell renderer, use it
                if (
                    (message.renderer != null)
                    && (message.renderer != _tree.getCellRenderer()))
                {
                    _tree.setCellRenderer(message.renderer);
                }

                _treeModel.insertNodeInto(parent, _rootNode, _rootNode.getChildCount());
                _curFileNode = parent;
            }

            parent.addMessage(message, refresh);
        }
        else
        {
            // the message is not associated with a file,
            // just append a new node at the end of the tree
            MessageTreeNode child = createMessageNode(message);
            _treeModel.insertNodeInto(child, _rootNode, _rootNode.getChildCount());
        }

        if (!_tree.isExpanded(_rootPath))
        {
            _tree.expandPath(_rootPath);
        }
    }


    /**
     * Creates a new node for the given message.
     *
     * @param message the first message related to a file.
     * @param model the tree model this node will be added to.
     *
     * @return the new message node.
     */
    private static FileTreeNode createFileNode(
        Message          message,
        DefaultTreeModel model)
    {
        return new FileTreeNode(message, model);
    }


    /**
     * Creates a node for the given message.
     *
     * @param message a message.
     *
     * @return a tree node representing the given message.
     */
    private static MessageTreeNode createMessageNode(Message message)
    {
        String text = message.text;
        String lineSeparator = getLineSeparator(text);

        if (lineSeparator == null) // single-line message
        {
            return new MessageTreeNode(message, MessageTreeNode.Type.SINGLE_LINE);
        }
        else // multi-line message, split in several nodes
        {
            StringTokenizer lines = new StringTokenizer(text, lineSeparator);

            // create the message for the first line
            Message firstMessage = new Message();
            firstMessage.text = lines.nextToken();
            firstMessage.file = message.file;
            firstMessage.line = message.line;
            firstMessage.startOffset = message.startOffset;
            firstMessage.endOffset = message.endOffset;
            firstMessage.type = message.type;

            MessageTreeNode parent =
                new MessageTreeNode(firstMessage, MessageTreeNode.Type.MULTI_LINE);

            for (; lines.hasMoreElements();)
            {
                String line = lines.nextToken().replace('\t', ' ');
                MessageTreeNode child =
                    new MessageTreeNode(
                        new Message(line, message.type), MessageTreeNode.Type.SINGLE_LINE);
                parent.add(child);
            }

            return parent;
        }
    }


    /**
     * Selects the nodes below the given node.
     *
     * @param node a tree node.
     *
     * @todo extend to allow the inclusion of unvisible children
     */
    private void selectNodes(MessageTreeNode node)
    {
        if (node != _rootNode)
        {
            _tree.addSelectionPath(new TreePath(node.getPath()));
        }

        for (int i = 0, size = node.getChildCount(); i < size; i++)
        {
            MessageTreeNode child = (MessageTreeNode) node.getChildAt(i);
            TreePath path = new TreePath(child.getPath());
            _tree.addSelectionPath(path);

            if (_tree.isExpanded(path))
            {
                selectNodes(child);
            }
        }
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * A FileTreeNode acts as the parent node for all message nodes of a given file.
     */
    protected static class FileTreeNode
        extends MessageTreeNode
    {
        DefaultTreeModel model;
        File file;
        List children = new ArrayList();
        MessageType type;
        int errors;
        int messages;
        int others;
        int warnings;

        public FileTreeNode(
            Message          message,
            DefaultTreeModel model)
        {
            super(
                new Message(message.text, message.file, 0, message.type),
                MessageTreeNode.Type.FILE);
            this.file = message.file;
            this.model = model;
            this.type = message.type;
        }

        public void setParent(MutableTreeNode node)
        {
            super.setParent(node);

            if (node == null)
            {
                this.model = null;
                this.children.clear();
            }
        }


        public void addMessage(
            Message message,
            boolean refresh)
        {
            if (message.type == MessageType.INFO)
            {
                ;
            }
            else if (message.type == MessageType.ERROR)
            {
                this.errors++;
            }
            else if (message.type == MessageType.WARN)
            {
                this.warnings++;
            }
            else
            {
                this.others++;
            }

            this.messages++;

            // no children means the file node has not yet been expanded, so
            // store the message for lazy construction
            if (getChildCount() == 0)
            {
                this.children.add(message);
            }
            else
            {
                MessageTreeNode node = createMessageNode(message);
                add(node);
            }

            // refresh immediately
            if (refresh)
            {
                this.model.reload(this);
            }
        }
    }
}
