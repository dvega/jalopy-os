/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.message;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.TreeCellRenderer;


/**
 * The default renderer for MessageTreeNode objects.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class MessageTreeCellRenderer
    extends JLabel
    implements TreeCellRenderer
{
    //~ Static variables/initializers ----------------------------------------------------

    static Font plainFont;
    static Font boldFont;
    static final ImageIcon ICON_ERROR =
        new ImageIcon(
            MessageTreeCellRenderer.class.getResource(
                "/oracle/jdeveloper/compiler/images/comperror.gif" /* NOI18N */));
    static final ImageIcon ICON_WARN =
        new ImageIcon(
            MessageTreeCellRenderer.class.getResource(
                "/oracle/jdeveloper/compiler/images/compwarning.gif" /* NOI18N */));
    static final ImageIcon ICON_DEBUG =
        new ImageIcon(
            MessageTreeCellRenderer.class.getResource(
                "/oracle/jdevimpl/runner/debug/images/debug.gif" /* NOI18N */));
    static final ImageIcon ICON_INFO =
        new ImageIcon(
            MessageTreeCellRenderer.class.getResource(
                "/oracle/ide/resource/images/filenode.gif" /* NOI18N */));
    static final String DEFAULT_PATTERN =
        "{0} ({1,choice,0#0 messages|1#1 message|1<{1,number,integer} messages}, {2,choice,0#0 warnings|1#1 warning|1<{2,number,integer} warnings}, {3,choice,0#0 errors|1#1 error|1<{3,number,integer} errors})";

    static
    {
        plainFont = UIManager.getFont("Tree.font" /* NOI18N */);
        boldFont = plainFont.deriveFont(Font.BOLD);
    }

    //~ Instance variables ---------------------------------------------------------------

    /** The pattern to use for file nodes. */
    protected String pattern = DEFAULT_PATTERN;

    /** Helper object to store the argument for the text formating. */
    private final Object[] _args = new Object[4];

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new MessageTreeCellRenderer object.
     */
    public MessageTreeCellRenderer()
    {
        setOpaque(true);
    }


    /**
     * Creates a new MessageTreeCellRenderer object.
     *
     * @param pattern the message pattern to use for file nodes.
     */
    public MessageTreeCellRenderer(String pattern)
    {
        this();
        this.pattern = pattern;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param tree DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param sel DOCUMENT ME!
     * @param expanded DOCUMENT ME!
     * @param leaf DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param focus DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTreeCellRendererComponent(
        JTree   tree,
        Object  value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int     row,
        boolean focus)
    {
        MessageTreeNode node = (MessageTreeNode) value;
        Message message = (Message) node.getUserObject();

        if (sel)
        {
            setBackground(UIManager.getColor("Tree.selectionBackground" /* NOI18N */));
            setForeground(UIManager.getColor("Tree.selectionForeground" /* NOI18N */));
        }
        else
        {
            setBackground(UIManager.getColor("Tree.textBackground" /* NOI18N */));
            setForeground(UIManager.getColor("Tree.textForeground" /* NOI18N */));
        }

        if (node.type == MessageTreeNode.Type.FILE) // parent
        {
            MessagePage.FileTreeNode n = (MessagePage.FileTreeNode) node;
            setFont(boldFont);

            _args[0] = n.file;
            _args[1] = new Integer(n.messages);
            _args[2] = new Integer(n.warnings);
            _args[3] = new Integer(n.errors);

            String text = MessageFormat.format(this.pattern, _args);
            setText(text);
            message.text = text;

            if (n.errors > 0)
            {
                setForeground(Color.red);
                setIcon(ICON_ERROR);
            }
            else if (n.warnings > 0)
            {
                setForeground(Color.blue);
                setIcon(ICON_WARN);
            }
            else if (n.others > 0)
            {
                setIcon(ICON_DEBUG);
            }
            else
            {
                setIcon(ICON_INFO);
            }
        }
        else if (node.type == MessageTreeNode.Type.MULTI_LINE) // multi-line
        {
            setFont(boldFont);
            setText(message.text);
            setColorAndIcon(this, message);
        }
        else // single-line
        {
            setFont(plainFont);
            setText(message.text);
            setColorAndIcon(this, message);
        }

        return this;
    }


    /**
     * Sets the color and icon of the component according to the given message type.
     *
     * @param component the renderer component.
     * @param message the message.
     */
    protected void setColorAndIcon(
        JLabel  component,
        Message message)
    {
        if (message.type == MessageType.INFO)
        {
            component.setForeground(Color.black);
            component.setIcon(ICON_INFO);
        }
        else if (message.type == MessageType.WARN)
        {
            component.setForeground(Color.blue);
            component.setIcon(ICON_WARN);
        }
        else if (message.type == MessageType.ERROR)
        {
            component.setForeground(Color.red);
            component.setIcon(ICON_ERROR);
        }
        else if (message.type == MessageType.DEBUG)
        {
            component.setForeground(Color.black);
            component.setIcon(ICON_DEBUG);
        }
    }
}
