/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.message;

import java.io.File;

import javax.swing.tree.TreeCellRenderer;


/**
 * A message that can be displayed in a log page.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class Message
{
    //~ Instance variables ---------------------------------------------------------------

    /** The file this message belongs to. */
    protected File file;

    /** The message type */
    protected MessageType type;

    /** The message text. */
    protected String text;

    /** The renderer used to draw messages in the tree. */
    protected TreeCellRenderer renderer;

    /** The end offset of an issue in a line. */
    protected int endOffset;

    /** The line number where an issue was found. */
    protected int line;

    /** The start offset of an issue in a line. */
    protected int startOffset;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new Message object.
     *
     * @param text the message text.
     */
    public Message(String text)
    {
        this(text, null, 0, MessageType.INFO);
    }


    /**
     * Creates a new Message object.
     *
     * @param text the message text.
     * @param type the message type.
     */
    public Message(
        String      text,
        MessageType type)
    {
        this(text, null, 0, type);
    }


    /**
     * Creates a new Message object.
     *
     * @param text the message text.
     * @param file the file this message belongs to.
     * @param line the line where the an issue was found (1-based).
     */
    public Message(
        String text,
        File   file,
        int    line)
    {
        this(text, file, line, MessageType.INFO);
    }


    /**
     * Creates a new Message object.
     *
     * @param text the message text.
     * @param file the file this message belongs to.
     * @param line the line where the an issue was found (1-based).
     * @param type the message type.
     */
    public Message(
        String      text,
        File        file,
        int         line,
        MessageType type)
    {
        this(text, file, line, 0, 0, type);
    }


    /**
     * Creates a new Message object.
     *
     * @param text the message text.
     * @param file the file this message belongs to.
     * @param line the line where the an issue was found (1-based).
     * @param startOffset the start offset of the issue (1-based).
     * @param endOffset the end offset of the issue (1-based).
     * @param type the message type.
     */
    public Message(
        String      text,
        File        file,
        int         line,
        int         startOffset,
        int         endOffset,
        MessageType type)
    {
        if (line > 0)
        {
            StringBuffer buf = new StringBuffer(text.length() + 8);
            buf.append(line);
            buf.append(':');
            buf.append(' ');
            buf.append(text);
            this.text = buf.toString();
        }
        else
        {
            this.text = text;
        }

        if (file != null)
        {
            this.file = file.getAbsoluteFile();
        }

        this.line = line;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.type = type;
    }


    Message()
    {
        this.type = MessageType.INFO;
    }


    Message(
        String      text,
        int         line,
        MessageType type)
    {
        this(text, null, line, type);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Sets the cell renderer that does the actual drawing of the message.
     *
     * @param renderer a tree cell renderer. If none is set the default cell renderer
     *        will be used.
     */
    public void setCellRenderer(TreeCellRenderer renderer)
    {
        this.renderer = renderer;
    }


    /**
     * Returns the file this message is associated with.
     *
     * @return the associated file. Returns <code>null</code> if this is a stand-alone
     *         message.
     */
    public File getFile()
    {
        return this.file;
    }


    /**
     * Returns the line number of the document this message is related to.
     *
     * @return the line number of the document this message points to. A value
     *         <code>&lt;= 0</code> indicates that this message is not associated with a
     *         specific position of a document.
     */
    public int getLine()
    {
        return this.line;
    }


    /**
     * Returns the text of the message.
     *
     * @return the message text.
     */
    public String getText()
    {
        return this.text;
    }


    /**
     * Returns the type of the message.
     *
     * @return the message type.
     */
    public MessageType getType()
    {
        return this.type;
    }


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     *
     * @see #getText
     */
    public String toString()
    {
        return this.text;
    }
}
