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

import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Loggers;
import de.hunsicker.ui.util.*;
import de.hunsicker.jalopy.ui.syntax.SyntaxTextArea;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.JToolBar;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import org.apache.log4j.Level;
import javax.swing.JFileChooser;

/**
 * Provides a floating preview that can be used to display a Java source file.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b8
 */
final class PreviewFrame
    extends JDialog
{
    //~ Instance variables ииииииииииииииииииииииииииииииииииииииииииииииииииии

    /** The currently displayed preferences page. */
    private AbstractPreferencesPanel _page;

    /** The associated preferences dialog. */
    private JDialog _dialog;

    /** Our text area. */
    private JTextArea _textArea;

    /** The Jalopy instance to format the preview files. */
    private Jalopy _jalopy = new Jalopy();

    /** The list of the currently running threads. */
    private List _threads = new ArrayList(3); // List of <Thread>

    /** Was the frame ever made visible? */
    private boolean _wasVisible;

    //~ Constructors ииииииииииииииииииииииииииииииииииииииииииииииииииииииииии

    /**
     * Creates a new PreviewFrame object.
     *
     * @param dialog the associated preferences dialog.
     */
    public PreviewFrame(JDialog dialog)
    {
        super(dialog);
        setTitle("Preview");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        _dialog = dialog;
        _textArea = new SyntaxTextArea();
        _textArea.setEditable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        menuBar.add(fileMenu);

        JMenuItem openFileMenuItem = new JMenuItem(new FileOpenAction());
        /*openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));*/
        fileMenu.add(openFileMenuItem);


        JMenuItem closeFileMenuItem = new JMenuItem("Close",
                                 KeyEvent.VK_C);
        fileMenu.add(closeFileMenuItem);

        setJMenuBar(menuBar);
        JScrollPane scrollPane = new JScrollPane(_textArea);
        getContentPane().add(scrollPane);
        pack();
    }

    //~ Methods иииииииииииииииииииииииииииииииииииииииииииииииииииииииииииииии


    private class FileOpenAction
        extends AbstractAction
    {
        public FileOpenAction()
        {
            super.putValue(Action.NAME, "Open");
            super.putValue(Action.SHORT_DESCRIPTION, "Open an existing Java source file");
            super.putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_O));
        }

        public void actionPerformed(ActionEvent ev)
        {
            LocationDialog dialog = new LocationDialog(PreviewFrame.this, "Open Java source file", "Test", "");
            dialog.setVisible(true);
        }
    }

    /**
     * Sets the preferences page that is currently displayed in the
     * preferences dialog.
     *
     * @param page the currently displayed preferences page.
     */
    public void setCurrentPage(AbstractPreferencesPanel page)
    {
        _page = page;
    }


    /**
     * Returns the currently active preferences page.
     *
     * @return currently displayed preferences page.
     */
    public AbstractPreferencesPanel getCurrentPage()
    {
        return _page;
    }


    /**
     * Sets the contents of the preview.
     *
     * @param text valid Java source file contents.
     */
    public synchronized void setText(String text)
    {
        if (text == null)
        {
            text = "";
        }

        synchronized (_threads)
        {
            if (_threads.size() > 0)
            {
                try
                {
                    FormatThread thread = (FormatThread)_threads.remove(0);
                    thread.interrupt();

                    if (thread.isAlive())
                    {
                        thread.join(25);
                    }
                }
                catch (InterruptedException ignored)
                {
                    ;
                }

                _jalopy.reset();
            }

            Thread thread = new FormatThread(text);
            _threads.add(thread);
            thread.start();
        }
    }


    /**
     * Makes the dialog visible.
     */
    public void show()
    {
        if (!_wasVisible)
        {
            Dimension screen = getToolkit().getScreenSize();

            int screenWidth = screen.width;
            int screenHeight = screen.height;
            int dialogWidth = _dialog.getWidth();
            int dialogHeight = _dialog.getHeight();
            int dialogX = _dialog.getX();
            int dialogY = _dialog.getY();
            int frameWidth = 600;

            if (screenWidth > (dialogWidth + frameWidth))
            {
                _dialog.setLocation((screenWidth - frameWidth - dialogWidth) / 2,
                                    dialogY);
                setSize(frameWidth, screenHeight - 30);
                setLocation(_dialog.getX() + dialogWidth, 1);
            }
            else
            {
                _dialog.setLocation(1, dialogY);
                setSize(screenWidth - dialogWidth - 2, screenHeight - 30);
                setLocation(_dialog.getX() + dialogWidth, 1);
            }

            _wasVisible = true;
        }

        super.show();
        _dialog.toFront();
    }

    //~ Inner Classes иииииииииииииииииииииииииииииииииииииииииииииииииииииииии

    private class FormatThread
        extends Thread
    {
        String text;

        public FormatThread(String text)
        {
            this.text = text;
        }

        public void run()
        {
            Level ioLevel = null;
            Level parserLevel = null;
            Level parserJavadocLevel = null;
            Level printerLevel = null;
            Level printerJavadocLevel = null;

            try
            {
                // the thread may be interrupted, but I don't want to introduce
                // inconsistencies
            }
            finally
            {
                ioLevel = Loggers.IO.getLevel();
                parserLevel = Loggers.PARSER.getLevel();
                parserJavadocLevel = Loggers.PARSER_JAVADOC.getLevel();
                printerLevel = Loggers.PRINTER.getLevel();
                printerJavadocLevel = Loggers.PRINTER_JAVADOC.getLevel();
                _page.prefs.snapshot();
            }

            try
            {
                _page.store();

                // disable logging so no messages appear and may confuse the
                // users
                Loggers.IO.setLevel(Level.FATAL);
                Loggers.PARSER.setLevel(Level.FATAL);
                Loggers.PARSER_JAVADOC.setLevel(Level.FATAL);
                Loggers.PRINTER.setLevel(Level.FATAL);
                Loggers.PRINTER_JAVADOC.setLevel(Level.FATAL);

                // enable Header/Footer template only for the Header/Footer page
                if (_page.getCategory().equals("header"))
                {
                    _page.prefs.putBoolean(Keys.FOOTER, false);
                }
                else if (_page.getCategory().equals("footer"))
                {
                    _page.prefs.putBoolean(Keys.HEADER, false);
                }
                else
                {
                    _page.prefs.putBoolean(Keys.FOOTER, false);
                    _page.prefs.putBoolean(Keys.HEADER, false);

                    if (_page.getCategory().equals("indentation"))
                    {
                        _page.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_EXTENDS,
                                               true);
                        _page.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_IMPLEMENTS,
                                               true);
                        _page.prefs.putBoolean(Keys.LINE_WRAP_BEFORE_THROWS,
                                               true);
                        _page.prefs.putBoolean(Keys.LINE_WRAP_AFTER_PARAMS_METHOD_DEF,
                                               true);
                        _page.prefs.putBoolean(Keys.ALIGN_TERNARY_EXPRESSION,
                                               true);
                        _page.prefs.putBoolean(Keys.ALIGN_TERNARY_VALUES, true);
                    }
                }

                // enable Javadoc template insertion only for Javadoc page
                if (!_page.getCategory().equals("javadoc"))
                {
                    _page.prefs.putInt(Keys.COMMENT_JAVADOC_CLASS_MASK, 0);
                    _page.prefs.putInt(Keys.COMMENT_JAVADOC_CTOR_MASK, 0);
                    _page.prefs.putInt(Keys.COMMENT_JAVADOC_METHOD_MASK, 0);
                    _page.prefs.putInt(Keys.COMMENT_JAVADOC_VARIABLE_MASK, 0);
                }

                // enable separation comments only for Separation page
                if (!_page.getPreviewFileName().equals("separationcomments"))
                {
                    _page.prefs.putBoolean(Keys.COMMENT_INSERT_SEPARATOR, false);
                }

                int wrapGuideColumn = _page.prefs.getInt(Keys.LINE_LENGTH,
                                                         Defaults.LINE_LENGTH);

                /*if (wrapGuideColumn != _textArea.getWrapGuideColumn())
                {
                    _textArea.setWrapGuideColumn(wrapGuideColumn);
                }*/
                _textArea.setTabSize(_page.prefs.getInt(Keys.INDENT_SIZE,
                                                        Defaults.INDENT_SIZE));

                _jalopy.setForce(true);

                if (this.text.length() > 0)
                {
                    _jalopy.setInput(this.text, _page.getCategory() + ".java");

                    StringBuffer buf = new StringBuffer(this.text.length());
                    _jalopy.setOutput(buf);
                    _jalopy.format();

                    int offset = _textArea.getCaretPosition();
                    String result = buf.toString();
                    _textArea.setText(result);

                    if (_textArea.getDocument().getLength() > offset)
                    {
                        _textArea.setCaretPosition(offset);
                    }
                }
                else
                {
                    _textArea.setText(this.text);
                }
            }
            catch (Throwable ignored)
            {
                ;
            }
            finally
            {
                // restore the current active settings as we want the user
                // to explicitely enable the changes (either by pressing 'OK'
                // or 'Apply')
                _page.prefs.revert();

                if (_textArea.getText().equals(""))
                {
                    _textArea.setText(this.text);
                }

                _jalopy.reset();

                Loggers.IO.setLevel(ioLevel);
                Loggers.PARSER.setLevel(parserLevel);
                Loggers.PARSER_JAVADOC.setLevel(parserJavadocLevel);
                Loggers.PRINTER.setLevel(printerLevel);
                Loggers.PRINTER_JAVADOC.setLevel(printerJavadocLevel);

                synchronized (_threads)
                {
                    _threads.remove(this);
                }
            }
        }
    }
}
