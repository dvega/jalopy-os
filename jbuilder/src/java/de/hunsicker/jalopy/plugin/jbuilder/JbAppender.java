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
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.BrowserIcons;
import com.borland.primetime.ide.Message;
import com.borland.primetime.ide.MessageCategory;
import com.borland.primetime.ide.MessageView;
import com.borland.primetime.ide.NodeViewer;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;
import com.borland.primetime.viewer.TextViewerComponent;

import de.hunsicker.jalopy.plugin.AbstractAppender;
import de.hunsicker.util.StringHelper;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.oro.text.regex.MatchResult;


/**
 * Appender which displays messages in a JBuilder message view.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbAppender
    extends AbstractAppender
{
    //~ Static variables/initializers ·········································

    /** Our custom message category. */
    public static final MessageCategory CATEGORY = new MessageCategory("Jalopy");

    /** Indicates that no line information is available. */
    private static final int NOT_AVAILABLE = 0;

    //~ Instance variables ····················································

    /** The root node of the message tree. */
    private Message _root;

    /** The current parent to add messages to. */
    private ParentMessage _parent;

    //~ Constructors ··························································

    /**
     * Creates a new JbAppender object.
     */
    public JbAppender()
    {
        super();
    }

    //~ Methods ·······························································

    /**
     * Does the actual outputting.
     *
     * @param ev logging event.
     */
    public void append(LoggingEvent ev)
    {
        if (!checkEntryConditions())
        {
            return;
        }

        final MessageView view = Browser.getActiveBrowser().getMessageView();

        if (_root == null)
        {
            _root = new Message("Jalopy messages");
            view.addMessage(CATEGORY, _root);
        }

        // check for Emacs-style messages
        MatchResult result = parseMessage(ev);

        // parsing failed, so we issue a standard-message
        if (result == null)
        {
            Message msg = new Message(this.layout.format(ev));
            view.addMessage(CATEGORY, _root, msg);

            return; // we're done
        }

        String filename = result.group(POS_FILENAME);
        int lineno = NOT_AVAILABLE;

        try
        {
            lineno = Integer.parseInt(result.group(POS_LINE));
        }
        catch (NumberFormatException neverOccurs)
        {
            ;
        }

        String text = result.group(POS_TEXT);

        // if the message belongs to the same file, add as new sibling
        if ((_parent != null) && filename.equals(_parent.filename))
        {
            FileMessage msg = new FileMessage(filename, lineno, text,
                                              ev.getLevel().toInt());
            _parent.add(msg, msg.level);
            view.revalidate();
            view.repaint();
        }
        else
        {
            // create the parent and add as new sibling
            ParentMessage parentMsg = new ParentMessage(filename);
            view.addMessage(CATEGORY, _root, parentMsg);
            _parent = parentMsg;

            FileMessage msg = new FileMessage(filename, lineno, text,
                                              ev.getLevel().toInt());
            _parent.add(msg, msg.level);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        Browser.getActiveBrowser().getMessageView().clearMessages(CATEGORY);
        _parent = null;
        _root = null;
    }

    //~ Inner Classes ·························································

    /**
     * Custom message which let us interact with the current editor pane.
     */
    private static class FileMessage
        extends Message
    {
        /** File which caused the message. */
        public String filename;

        /** Level of the message. */
        public int level;

        /**
         * Holds the sucessive lines of the message (if this is a multiline
         * message).
         */
        private List _messages = Collections.EMPTY_LIST; // List of <Message>

        /** Line number where an error/warning occured. */
        private int _line;

        /**
         * Creates a new FileMessage object.
         *
         * @param filename filename of the source file which caused the
         *        message.
         * @param lineno line number where the message origins.
         * @param text message text.
         * @param level message level.
         */
        public FileMessage(String filename,
                           int    lineno,
                           String text,
                           int    level)
        {
            this.filename = filename;
            this.level = level;
            setIcon(BrowserIcons.ICON_FILETEXT);
            _line = lineno;

            StringBuffer buf = new StringBuffer(text.length());
            Color color = Color.black;

            switch (level)
            {
                case Level.ERROR_INT :
                case Level.FATAL_INT :
                    color = Color.red;

                    break;

                case Level.WARN_INT :
                    color = Color.blue;

                    break;

                default :
                    break;
            }

            setForeground(color);

            // is this a multiline message?
            if (text.indexOf('\n') > -1)
            {
                setLazyFetchChildren(true);
                _messages = new ArrayList();

                StringTokenizer tokens = new StringTokenizer(text, "\n");
                buf.append(lineno);
                buf.append(':');
                buf.append(tokens.nextToken());
                setText(buf.toString());

                while (tokens.hasMoreElements())
                {
                    String line = StringHelper.replace(tokens.nextToken(), "\t",
                                                       "  ");
                    Message message = new Message(line);
                    _messages.add(message);
                    message.setForeground(color);
                }
            }
            else
            {
                buf.append(lineno);
                buf.append(':');
                buf.append(text);
                setText(buf.toString());
            }
        }

        public void fetchChildren(Browser browser)
        {
            MessageView view = browser.getMessageView();

            for (int i = 0, size = _messages.size(); i < size; i++)
            {
                view.addMessage(CATEGORY, this, (Message)_messages.get(i));
            }
        }


        /**
         * Triggered if the user clicks on the item. Jumps to the line in the
         * editor pane where an error/warning occured. If the file is not
         * currently displayed, it will be opened.
         *
         * @param browser the Browser owning the MessageView.
         */
        public void selectAction(Browser browser)
        {
            /**
             * @todo I'd rather wanted to override messageAction() but it
             *       doesn't work for multiple line messages as of JBuilder
             *       5.0 (it only expands the parent node, but don't trigger
             *       the message action)
             */

            // if there is no line number given, there is nothing to do
            if (_line <= NOT_AVAILABLE)
            {
                return;
            }

            EditorPane pane = getEditor(browser);

            if (pane != null)
            {
                int totalLines = pane.getLineCount();

                if (_line > totalLines)
                {
                    _line = totalLines;
                }

                pane.gotoLine(_line, false);
                pane.requestFocus();
                pane.setHighlight(_line);

                /**
                 * @todo if we don't add our listener the highlight will only
                 *       be removed if the user collapses the message which
                 *       caused the line to be highlighted. Is this a bug or
                 *       a feature?
                 */
                pane.addCaretListener(new HighlightHandler(_line));
            }
        }


        /**
         * Gets the editor pane for the given node.
         *
         * @param browser browser to display the node in.
         *
         * @return editor pane for the selected file or <code>null</code> if
         *         no editor pane could be located/created.
         */
        private EditorPane getEditor(Browser browser)
        {
            Url url = new Url(new File(this.filename));
            Project project = browser.getActiveProject();
            Node node = project.findNode(url);

            if (node == null)
            {
                browser.doOpen(url, browser.getActiveProject(), false);
                node = browser.getActiveNode();
            }

            // make sure the node is the active node
            else if (!node.equals(browser.getActiveNode()))
            {
                try
                {
                    browser.setActiveNode(node, true);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                    return null;
                }
            }

            NodeViewer viewer = browser.getActiveViewer(node);
            TextViewerComponent component = (TextViewerComponent)viewer.getViewerComponent();

            return (component.getMainView().getEditor());
        }


        /**
         * Gets an appropriate icon for the given logging event.
         *
         * @param level of the logging event.
         *
         * @return icon to display in the message view.
         */
        private Icon getIcon(int level)
        {
            switch (level)
            {
                case Level.INFO_INT :
                    return BrowserIcons.ICON_TIPOFTHEDAY;

                case Level.ERROR_INT :
                case Level.FATAL_INT :
                    return BrowserIcons.ICON_ERROR;

                case Level.WARN_INT :
                    return BrowserIcons.ICON_WARNING;

                default :
                    break;
            }

            return BrowserIcons.ICON_UNKNOWN;
        }

        /**
         * Clears the highlight if the user selects another line.
         */
        private static class HighlightHandler
            implements CaretListener
        {
            int line;

            public HighlightHandler(int line)
            {
                this.line = line;
            }

            public void caretUpdate(CaretEvent ev)
            {
                EditorPane pane = (EditorPane)ev.getSource();

                if (this.line != pane.getLineNumber(ev.getDot()))
                {
                    pane.clearHighlight();
                    pane.removeCaretListener(this);
                }
            }
        }
    }


    /**
     * Acts as the root for all messages belonging to one file. Children are
     * lazy loaded when the user clicks on it. Displays the number of the
     * different messages in its text (along with the file name).
     */
    private static class ParentMessage
        extends Message
    {
        private static final String INFO = " ({0} info(s), {1} warning(s), {2} error(s))";

        /** File which caused the messages. */
        public String filename;

        /** Holds the children. */
        private List _messages; // List of <Message>

        /** Used to format the message text. */
        private MessageFormat _formatter;

        /** Number of error messages. */
        private int _errors;

        /** Number of info messages. */
        private int _infos;

        /** Number of warning messages. */
        private int _warnings;

        /**
         * Creates a new ParentMessage object.
         *
         * @param filename filename of the source file which caused the
         *        messages.
         */
        public ParentMessage(String filename)
        {
            this.filename = filename;
            _messages = new ArrayList();
            _formatter = new MessageFormat(filename + INFO);

            Object[] args ={ new Integer(0), new Integer(0), new Integer(0) };
            setText(_formatter.format(args));
            setLazyFetchChildren(true);
            setCellRenderer(new CellRenderer());
        }

        public void add(Message message,
                        int     level)
        {
            _messages.add(message);

            switch (level)
            {
                case Level.ERROR_INT :
                case Level.FATAL_INT :
                    _errors++;

                    break;

                case Level.WARN_INT :
                    _warnings++;

                    break;

                case Level.INFO_INT :
                case Level.DEBUG_INT :
                    _infos++;

                    break;

                default :
                    break;
            }
        }


        public void fetchChildren(Browser browser)
        {
            MessageView view = browser.getMessageView();

            for (int i = 0, size = _messages.size(); i < size; i++)
            {
                view.addMessage(CATEGORY, this, (Message)_messages.get(i));
            }
        }

        /**
         * Renderer for the messages.
         */
        private class CellRenderer
            extends JLabel
            implements TreeCellRenderer
        {
            public CellRenderer()
            {
                setOpaque(true);
            }

            public Component getTreeCellRendererComponent(JTree   tree,
                                                          Object  value,
                                                          boolean sel,
                                                          boolean expanded,
                                                          boolean leaf,
                                                          int     row,
                                                          boolean focus)
            {
                if (sel)
                {
                    this.setBackground(UIManager.getColor("Tree.selectionBackground"));
                    this.setForeground(UIManager.getColor("Tree.selectionForeground"));
                }
                else
                {
                    if (_errors > 0)
                    {
                        this.setForeground(Color.red);
                    }
                    else if (_warnings > 0)
                    {
                        this.setForeground(Color.blue);
                    }
                    else
                    {
                        this.setForeground(UIManager.getColor("Tree.textForeground"));
                    }

                    this.setBackground(UIManager.getColor("Tree.textBackground"));
                }

                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
                Object nodeValue = node.getUserObject();

                if (nodeValue == null)
                {
                    this.setIcon(null);
                    this.setText(null);
                }

                this.setFont(UIManager.getFont("Tree.font"));

                Object[] args =
                {
                    new Integer(_infos), new Integer(_warnings),
                    new Integer(_errors)
                };
                this.setText(_formatter.format(args));
                this.setIcon(null);

                return this;
            }
        }
    }
}
