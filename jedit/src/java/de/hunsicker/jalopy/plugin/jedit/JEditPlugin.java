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
package de.hunsicker.jalopy.plugin.jedit;

import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.EBPlugin;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.OptionGroup;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.gui.OptionsDialog;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.msg.BufferUpdate;
import org.gjt.sp.jedit.msg.EditPaneUpdate;
import org.gjt.sp.jedit.msg.ViewUpdate;
import org.gjt.sp.jedit.view.message.MessageView;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.StatusBar;
import de.hunsicker.jalopy.plugin.jedit.option.BracesOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.CommentsOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.EnvironmentOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.FooterOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.GeneralOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.HeaderOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.ImportsOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.IndentationOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.JavadocOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.LineWrappingOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.MessagesOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.MiscOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.SeparationOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.SortOptionPane;
import de.hunsicker.jalopy.plugin.jedit.option.WhitespaceOptionPane;
import de.hunsicker.jalopy.ui.SettingsDialog;

import java.awt.Frame;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 * The Jalopy jEdit Plug-in.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class JEditPlugin
    extends EBPlugin
{
    //~ Static variables/initializers ·········································

    /** Our format menu item. */
    private static JMenuItem _formatItem;

    /** The actual plug-in implementation. */
    private static PluginImpl _instance;

    //~ Instance variables ····················································

    /** The menu which holds our menu items. */
    private JMenu _menu;

    //~ Constructors ··························································

    /**
     * Creates a new jEditPlugin object.
     */
    public JEditPlugin()
    {
    }

    //~ Methods ·······························································

    /**
     * Creates the menu items.
     *
     * @param menuItems holds the menus/menu items created so far.
     */
    public void createMenuItems(Vector menuItems)
    {
        _menu = GUIUtilities.loadMenu("jalopy-menu");

        // we store the "Format current Buffer" menu item, because we want to
        // be able to enable/disable this item according to the state of the
        // view
        _formatItem = _menu.getItem(0);
        menuItems.addElement(_menu);
    }


    /**
     * Displays the Jalopy settings dialog.
     *
     * @param view current view.
     */
    public static synchronized void displaySettingsDialog(View view)
    {
        if (_instance == null)
        {
            _instance = new PluginImpl();
            _instance.statusBar = new JEditStatusBar(view.getStatus());
        }

        _instance.statusBar.statusBar = view.getStatus();

        SettingsDialog dialog = new SettingsDialog(_instance.getMainWindow());
        dialog.pack();
        dialog.setLocationRelativeTo(_instance.getMainWindow());
        dialog.setVisible(true);
    }


    /**
     * Formats the active buffer. Invoked if the user selects the
     * corresponding menu item.
     *
     * @param view current view.
     */
    public static synchronized void formatActive(View view)
    {
        if (_instance == null)
        {
            _instance = new PluginImpl();
            _instance.statusBar = new JEditStatusBar(view.getStatus());
        }

        _instance.statusBar.statusBar = view.getStatus();
        _instance.formatActive();
    }


    /**
     * Formats the open buffers. Invoked if the user selects the corresponding
     * menu item.
     *
     * @param view current view.
     */
    public static synchronized void formatOpen(View view)
    {
        if (_instance == null)
        {
            _instance = new PluginImpl();
            _instance.statusBar = new JEditStatusBar(view.getStatus());
        }

        _instance.statusBar.statusBar = view.getStatus();
        _instance.formatOpen();
    }


    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static synchronized JavaNode parse(View view)
    {
        if (_instance == null)
        {
            _instance = new PluginImpl();
            _instance.statusBar = new JEditStatusBar(view.getStatus());
        }

        if (view != null)
        {
            String text = view.getTextArea().getText();
            Jalopy jalopy = _instance.getEngine();
            jalopy.setInput(text, view.getBuffer().getFile().getAbsolutePath());

            return jalopy.parse();
        }

        return null;
    }


    /**
     * Creates the option panes for the global options dialog.
     *
     * @param dialog global options dialog.
     */
    public void createOptionPanes(OptionsDialog dialog)
    {
        OptionGroup grp = new OptionGroup("jalopy");
        grp.addOptionPane(new GeneralOptionPane());
        grp.addOptionPane(new BracesOptionPane());
        grp.addOptionPane(new WhitespaceOptionPane());
        grp.addOptionPane(new IndentationOptionPane());
        grp.addOptionPane(new LineWrappingOptionPane());
        grp.addOptionPane(new SeparationOptionPane());
        grp.addOptionPane(new CommentsOptionPane());
        grp.addOptionPane(new ImportsOptionPane());
        grp.addOptionPane(new EnvironmentOptionPane());
        grp.addOptionPane(new JavadocOptionPane());
        grp.addOptionPane(new HeaderOptionPane());
        grp.addOptionPane(new FooterOptionPane());
        grp.addOptionPane(new SortOptionPane());
        grp.addOptionPane(new MiscOptionPane());
        grp.addOptionPane(new MessagesOptionPane());
        dialog.addOptionGroup(grp);
    }


    /**
     * Handles a message sent on the EditBus. Updates the state of the Jalopy
     * menu item according to the message content.
     *
     * @param message the message.
     */
    public void handleMessage(EBMessage message)
    {
        if (_formatItem == null)
        {
            return;
        }

        if (message instanceof EditPaneUpdate)
        {
            EditPaneUpdate update = (EditPaneUpdate)message;
            Object what = update.getWhat();

            if (update.getEditPane() != null)
            {
                if ((what == EditPaneUpdate.BUFFER_CHANGED) ||
                    (what == EditPaneUpdate.CREATED))
                {
                    Buffer buffer = update.getEditPane().getBuffer();

                    if (isActiveBuffer(buffer))
                    {
                        if (isSourceFile(buffer))
                        {
                            if (!_formatItem.isEnabled())
                            {
                                _formatItem.setEnabled(true);
                            }
                        }
                        else
                        {
                            if (_formatItem.isEnabled())
                            {
                                _formatItem.setEnabled(false);
                            }
                        }
                    }
                }
            }
        }
        else if (message instanceof ViewUpdate)
        {
            ViewUpdate update = (ViewUpdate)message;
            Object what = update.getWhat();

            if (what == ViewUpdate.CREATED)
            {
                Buffer buffer = update.getView().getBuffer();

                if (isActiveBuffer(buffer))
                {
                    if (isSourceFile(buffer))
                    {
                        if (!_formatItem.isEnabled())
                        {
                            _formatItem.setEnabled(true);
                        }
                    }
                    else
                    {
                        if (_formatItem.isEnabled())
                        {
                            _formatItem.setEnabled(false);
                        }
                    }
                }
            }
        }
        else if (message instanceof BufferUpdate)
        {
            BufferUpdate update = (BufferUpdate)message;
            Object what = update.getWhat();

            if (update.getBuffer() != null)
            {
                if (what == BufferUpdate.MODE_CHANGED)
                {
                    Buffer buffer = update.getBuffer();

                    if (isActiveBuffer(buffer))
                    {
                        if (isSourceFile(buffer))
                        {
                            if (!_formatItem.isEnabled())
                            {
                                _formatItem.setEnabled(true);
                            }
                        }
                        else
                        {
                            if (_formatItem.isEnabled())
                            {
                                _formatItem.setEnabled(false);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Initializes the Plug-in.
     */
    public void start()
    {
        /**
         * @todo maybe the shortcut creation could be placed in the .props
         *       file, but how?
         */
        Object shortcut = jEdit.getProperty("jalopy.format.shortcut", "");

        // register a shortcut (Ctrl+Shift+F10)
        if ("".equals(shortcut))
        {
            jEdit.setProperty("jalopy.format.shortcut", "CS+F10");
        }
    }


    /**
     * Determines whether the given buffer is the currently active one.
     *
     * @param buffer buffer to have its state checked.
     *
     * @return <code>true</code> if the given buffer is currently active.
     */
    static boolean isActiveBuffer(Buffer buffer)
    {
        View[] views = jEdit.getViews();

        for (int i = 0; i < views.length; i++)
        {
            if (buffer == views[i].getBuffer())
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Determines whether the given buffer contains a Java source file.
     *
     * @param buffer jEdit buffer.
     *
     * @return <code>true</code> if the given buffer edits a Java source file.
     */
    private static boolean isSourceFile(Buffer buffer)
    {
        if ((!buffer.isReadOnly()) &&
            buffer.getMode().getName().equalsIgnoreCase("java"))
        {
            return true;
        }

        return false;
    }

    //~ Inner Classes ·························································

    /**
     * The actual Plug-in implementation.
     */
    private static class PluginImpl
        extends AbstractPlugin
    {
        /** The current status bar. */
        JEditStatusBar statusBar;

        /** The used project. */
        Project project;

        /**
         * Creates a new PluginImpl object.
         */
        public PluginImpl()
        {
            super(new JEditAppender());
        }

        public Project getActiveProject()
        {
            if (this.project == null)
            {
                this.project = new JEditProject();
            }

            return this.project;
        }


        public Jalopy getEngine()
        {
            return getJalopy();
        }


        public FileFormat getFileFormat()
        {
            /**
             * @todo there is a bug(?) in jEdit's text area whereas inserting
             *       text with DOS file format results in displaying EOF
             *       characters, so we always use UNIX format and let jEdit
             *       handle the specified file format upon file saving
             */
            return FileFormat.UNIX;

            /*
               String lineSeparator = (String)jEdit.getFirstBuffer().getProperty(Buffer.LINESEP);
               //String lineSeparator = (String)jEdit.getProperties().get("buffer.lineSeparator");
               if (lineSeparator == null)
               {
                   return Jalopy.FORMAT_AUTO;
               }
               if (lineSeparator.equals("\r\n"))
               {
                   return Jalopy.FORMAT_DOS;
               }
               if (lineSeparator.equals("\n"))
               {
                   return Jalopy.FORMAT_UNIX;
               }
               if (lineSeparator.equals("\r"))
               {
                   return Jalopy.FORMAT_MAC;
               }
               return Jalopy.FORMAT_AUTO;
             */
        }


        public Frame getMainWindow()
        {
            return jEdit.getActiveView();
        }


        public StatusBar getStatusBar()
        {
            return this.statusBar;
        }


        /**
         * {@inheritDoc}
         */
        public void afterEnd()
        {
            super.afterEnd();

            MessageView.getInstance().update();
        }


        /**
         * Formats the currently active buffer.
         */
        public void formatActive()
        {
            /**
             * @todo maybe this check will become obsolete one day, if jEdit
             *       makes use of the Java Action framework
             */

            // only perform the action if the format menu item is enabled
            if (_formatItem.isEnabled())
            {
                performAction(Action.FORMAT_ACTIVE);
            }
        }


        /**
         * Formats the currently open buffers.
         */
        public void formatOpen()
        {
            performAction(Action.FORMAT_OPEN);
        }
    }
}
