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

import de.hunsicker.jalopy.prefs.Loggers;
import de.hunsicker.jalopy.prefs.Preferences;
import de.hunsicker.ui.ErrorDialog;
import de.hunsicker.ui.util.SwingHelper;
import de.hunsicker.util.Helper;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ColorUIResource;

import org.apache.log4j.*;
import org.apache.log4j.varia.NullAppender;


/**
 * The Jalopy preferences dialog. Provides a graphical user interface to
 * display and interactively edit all preferences options.
 * 
 * <p>
 * The dialog can be used from other Java applications as usual, in which case
 * it acts like any other JDialog (i.e. as a secondary window). But it may be
 * also invoked directly from the command line, magically acting as the main
 * application window.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class PreferencesDialog
    extends JDialog
{
    //~ Static variables/initializers ·········································

    /**
     * Command line option that indicates that the preferences dialog was
     * called from within Eclipse. Used to prevent the call to System.exit
     * upon closure.
     */
    public static final String ARG_ECLIPSE = "-eclipse";

    //~ Instance variables ····················································

    /**
     * Used to store the action listeners that should be notified upon saving.
     */
    private EventListenerList _listeners;

    /** Button to save the state, don't closes the dialog. */
    private JButton _applyButton;

    /** Button to discard changes and close the dialog. */
    private JButton _cancelButton;

    /** Button to close the dialog, the options will be saved. */
    private JButton _okButton;

    /** The container that contains the tree and page views. */
    private PreferencesContainer _preferencesContainer;

    /** A frame that displays the contents of a Java source file. */
    private PreviewFrame _previewFrame;

    //~ Constructors ··························································

    /**
     * Creates a new PreferencesDialog object.
     *
     * @param owner the frame from which the dialog is displayed.
     */
    public PreferencesDialog(Frame owner)
    {
        super(owner, "Jalopy Preferences");
        initialize();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}      Overriden to dispatch the call to the top-level
     * container if invoked from the command line.
     */
    public int getHeight()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            return ((PreferencesFrame)c).getHeight();
        }
        else
        {
            return super.getHeight();
        }
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public void setLocation(int x,
                            int y)
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            ((PreferencesFrame)c).setLocation(x, y);
        }
        else
        {
            super.setLocation(x, y);
        }
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public Point getLocation()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            return ((PreferencesFrame)c).getLocation();
        }
        else
        {
            return super.getLocation();
        }
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public int getWidth()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            return ((PreferencesFrame)c).getWidth();
        }
        else
        {
            return super.getWidth();
        }
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public int getX()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            return ((PreferencesFrame)c).getX();
        }
        else
        {
            return super.getX();
        }
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public int getY()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            return ((PreferencesFrame)c).getY();
        }
        else
        {
            return super.getY();
        }
    }


    /**
     * Adds an action listener that will be notified if the user stores
     * preferences.
     *
     * @param listener listener to add.
     */
    public synchronized void addActionListener(ActionListener listener)
    {
        if (_listeners == null)
        {
            _listeners = new EventListenerList();
        }

        _listeners.add(ActionListener.class, listener);
    }


    /**
     * Displays the preferences dialog. The dialog then uses a {@link
     * javax.swing.JFrame} as its top-level container.
     *
     * @param argv command line arguments.
     */
    public static void main(String[] argv)
    {
        if ((argv.length == 0) ||
            (!argv[0].equals(PreferencesDialog.ARG_ECLIPSE)))
        {
            Loggers.initialize(new ConsoleAppender(
                                                   new PatternLayout("[%p] %m\n"),
                                                   "System.out"));

            //Loggers.initialize(new NullAppender());
        }

        initializeLookAndFeel();

        final PreferencesFrame frame = new PreferencesFrame("Jalopy Preferences",
                                                            argv);
        final PreferencesDialog dialog = new PreferencesDialog(frame);
        frame.getContentPane().add(dialog.getContentPane());
        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent ev)
                {
                    dialog._preferencesContainer.dispose();

                    if (frame.isExitOnClose)
                    {
                        System.exit(0);
                    }
                    else
                    {
                        frame.dispose();
                        dialog._previewFrame.dispose();
                    }
                }


                public void windowOpened(WindowEvent ev)
                {
                    dialog._preferencesContainer.displayPreview();
                }
            });
        frame.pack();

        // center on screen
        Toolkit kit = dialog.getToolkit();
        frame.setLocation((kit.getScreenSize().width - frame.getWidth()) / 2,
                          (kit.getScreenSize().height - frame.getHeight()) / 2);
        frame.setVisible(true);
    }


    /**
     * Removes the specified listener.
     *
     * @param listener to remove.
     */
    public synchronized void removeActionListener(ActionListener listener)
    {
        if (_listeners == null)
        {
            return;
        }

        _listeners.remove(ActionListener.class, listener);
    }


    /**
     * {@inheritDoc} Overriden to dispatch the call to the top-level container
     * if invoked from the command line.
     */
    public void toFront()
    {
        Container c = getParent();

        if (c instanceof PreferencesFrame)
        {
            ((PreferencesFrame)c).toFront();
        }
        else
        {
            super.toFront();
        }
    }


    /**
     * Initializes preferences for the Look &amp; Feel.
     */
    private static void initializeLookAndFeel()
    {
        if (UIManager.getLookAndFeel().getID().equals("Metal"))
        {
            String fontName = "Tahoma";
            int fontSize = 12;
            Font dialogPlain = new Font(fontName, Font.PLAIN, fontSize);
            Font serifPlain = new Font("Serif", Font.PLAIN, fontSize);
            Font sansSerifPlain = new Font("SansSerif", Font.PLAIN, fontSize);
            Font monospacedPlain = new Font("Monospaced", Font.PLAIN, fontSize);
            UIManager.put("Border.font", dialogPlain);
            UIManager.put("InternalFrameTitlePane.font", dialogPlain);
            UIManager.put("OptionPane.font", dialogPlain);
            UIManager.put("DesktopIcon.font", dialogPlain);
            UIManager.put("Button.font", dialogPlain);
            UIManager.put("ToggleButton.font", dialogPlain);
            UIManager.put("RadioButton.font", dialogPlain);
            UIManager.put("CheckBox.font", dialogPlain);
            UIManager.put("ColorChooser.font", dialogPlain);
            UIManager.put("ComboBox.font", dialogPlain);
            UIManager.put("Label.font", dialogPlain);
            UIManager.put("List.font", dialogPlain);
            UIManager.put("MenuBar.font", dialogPlain);
            UIManager.put("MenuItem.font", dialogPlain);
            UIManager.put("RadioButtonMenuItem.font", dialogPlain);
            UIManager.put("CheckBoxMenuItem.font", dialogPlain);
            UIManager.put("Menu.font", dialogPlain);
            UIManager.put("PopupMenu.font", dialogPlain);
            UIManager.put("OptionPane.font", dialogPlain);
            UIManager.put("Panel.font", dialogPlain);
            UIManager.put("ProgressBar.font", dialogPlain);
            UIManager.put("ScrollPane.font", dialogPlain);
            UIManager.put("Viewport.font", dialogPlain);
            UIManager.put("TabbedPane.font", dialogPlain);
            UIManager.put("Table.font", dialogPlain);
            UIManager.put("TableHeader.font", dialogPlain);
            UIManager.put("TextField.font", sansSerifPlain);
            UIManager.put("PasswordField.font", monospacedPlain);
            UIManager.put("TextArea.font", monospacedPlain);
            UIManager.put("TextPane.font", serifPlain);
            UIManager.put("EditorPane.font", serifPlain);
            UIManager.put("TitledBorder.font", dialogPlain);
            UIManager.put("ToolBar.font", dialogPlain);
            UIManager.put("ToolTip.font", sansSerifPlain);
            UIManager.put("Tree.font", dialogPlain);

            Color control = new ColorUIResource(SystemColor.control);
            Color desktop = new ColorUIResource(SystemColor.desktop);
            Color menu = new ColorUIResource(SystemColor.menu);
            Color textText = new ColorUIResource(SystemColor.textText);
            Color textInactiveText = new ColorUIResource(SystemColor.textInactiveText);
            Color textHighlight = new ColorUIResource(SystemColor.textHighlight);
            Color textHighlightText = new ColorUIResource(SystemColor.textHighlightText);
            UIManager.put("control", control);
            UIManager.put("Button.background", control);
            UIManager.put("CheckBox.background", control);
            UIManager.put("CheckBoxMenuItem.background", control);
            UIManager.put("ColorChooser.background", control);
            UIManager.put("ComboBox.background", control);
            UIManager.put("ComboBox.disabledBackground", control);
            UIManager.put("ComboBox.disabledForeground", textInactiveText);
            UIManager.put("Desktop.background", desktop);
            UIManager.put("Label.background", control);
            UIManager.put("Label.disabledForeground", textInactiveText);
            UIManager.put("Menu.background", control);
            UIManager.put("MenuBar.background", control);
            UIManager.put("MenuItem.background", control);
            UIManager.put("OptionPane.background", control);
            UIManager.put("Panel.background", control);
            UIManager.put("PopupMenu.background", control);
            UIManager.put("ProgressBar.background", control);
            UIManager.put("RadioButton.background", control);
            UIManager.put("RadioButtonMenuItem.background", control);
            UIManager.put("ScrollBar.background", control);
            UIManager.put("ScrollPane.background", control);
            UIManager.put("Slider.background", control);
            UIManager.put("SplitPane.background", control);
            UIManager.put("TabbedPane.background", control);
            UIManager.put("Table.background", Color.white);
            UIManager.put("TableHeader.background", control);
            UIManager.put("TabbedPane.selected", control);
            UIManager.put("ToggleButton.background", control);
            UIManager.put("ToolBar.background", control);
            UIManager.put("ToolBar.dockingBackground", control);
            UIManager.put("ToolBar.flotingBackground", control);
            UIManager.put("viewport.background", control);
            UIManager.put("PopupMenu.background", menu);
            UIManager.put("Menu.foreground", textText);
            UIManager.put("Menu.acceleratorForeground", textText);
            UIManager.put("Menu.acceleratorSelectionForeground",
                          textHighlightText);
            UIManager.put("Menu.selectionBackground", textHighlight);
            UIManager.put("Menu.selectionForeground", textHighlightText);
            UIManager.put("MenuItem.acceleratorForeground", textText);
            UIManager.put("MenuItem.acceleratorSelectionForeground",
                          textHighlightText);
            UIManager.put("MenuItem.selectionBackground", textHighlight);
            UIManager.put("MenuItem.selectionForeground", textHighlightText);
            UIManager.put("MenuItem.background", menu);
            UIManager.put("Tree.selectionForeground", textHighlightText);
            UIManager.put("Tree.selectionBackground", textHighlight);
            UIManager.put("Label.foreground", Color.black);
            UIManager.put("TitledBorder.titleColor", Color.black);
        }
    }


    /**
     * Initializes the UI.
     */
    private void initialize()
    {
        //setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        _previewFrame = new PreviewFrame(this);
        _preferencesContainer = new PreferencesContainer(_previewFrame);

        Container container = getParent();
        _okButton = new JButton("OK");
        _okButton.addActionListener(new OkActionHandler());

        if (container instanceof PreferencesFrame)
        {
            ((JFrame)container).getRootPane().setDefaultButton(_okButton);
        }
        else
        {
            getRootPane().setDefaultButton(_okButton);
        }

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        Container contentPane = getContentPane();
        contentPane.setLayout(layout);
        c.insets.top = 10;
        c.insets.left = 5;
        c.insets.right = 5;
        SwingHelper.setConstraints(c, 0, 0, 10, 10, 1.0, 1.0,
                                   GridBagConstraints.NORTH,
                                   GridBagConstraints.BOTH, c.insets, 0, 0);
        layout.setConstraints(_preferencesContainer, c);
        contentPane.add(_preferencesContainer);
        c.insets.bottom = 10;
        SwingHelper.setConstraints(c, 7, 11, 1, 1, 1.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        layout.setConstraints(_okButton, c);
        contentPane.add(_okButton);
        c.insets.left = 0;
        SwingHelper.setConstraints(c, 8, 11, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _applyButton = new JButton("Apply");
        _applyButton.addActionListener(new ApplyActionHandler());
        layout.setConstraints(_applyButton, c);
        contentPane.add(_applyButton);
        c.insets.right = 20;
        SwingHelper.setConstraints(c, 9, 11, 1, 1, 0.0, 0.0,
                                   GridBagConstraints.EAST,
                                   GridBagConstraints.NONE, c.insets, 0, 0);
        _cancelButton = new JButton("Cancel");
        _cancelButton.addActionListener(new CancelActionHandler());
        layout.setConstraints(_cancelButton, c);
        contentPane.add(_cancelButton);
        addWindowListener(new WindowAdapter()
            {
                public void windowOpened(WindowEvent ev)
                {
                    _preferencesContainer.displayPreview();
                }


                public void windowClosing(WindowEvent ev)
                {
                    _preferencesContainer.dispose();
                    _previewFrame.dispose();
                }
            });
    }


    /**
     * Notifies all registered listeners that the preferences will be saved.
     *
     * @param ev action event.
     */
    private void notifyListeners(ActionEvent ev)
    {
        if (_listeners == null)
        {
            return;
        }

        EventListener[] listeners = _listeners.getListeners(ActionListener.class);

        for (int i = 0; i < listeners.length; i++)
        {
            ((ActionListener)listeners[i]).actionPerformed(ev);
        }
    }


    /**
     * Stores the preferences.
     *
     * @return <code>true</code> if the preferences were stored sucessfully.
     */
    private boolean storePreferences()
    {
        try
        {
            _preferencesContainer.store();
        }
        catch (ValidationException ex)
        {
            // we displayed a message box to informa the user
            return false;
        }
        catch (Throwable ex)
        {
            final Window owner = SwingUtilities.windowForComponent(this);
            ErrorDialog dialog = null;

            if (owner instanceof Dialog)
            {
                dialog = new ErrorDialog(ex, (Dialog)owner);
            }
            else
            {
                dialog = new ErrorDialog(ex, (Frame)owner);
            }

            dialog.setVisible(true);
            dialog.dispose();

            return false;
        }

        try
        {
            // save the preferences
            Preferences.getInstance().flush();
        }
        catch (Throwable ex)
        {
            ErrorDialog dialog = new ErrorDialog(ex, this);
            dialog.setVisible(true);
            dialog.dispose();

            return false;
        }

        return true;
    }

    //~ Inner Classes ·························································

    /**
     * The sole purpose of this class lies in the fact that we want to tell
     * whether the dialog was invoked from within a Plug-in or from the
     * command line.
     */
    private static class PreferencesFrame
        extends JFrame
    {
        /**
         * Should we exit the application upon user confirmation or
         * cancelation?
         */
        boolean isExitOnClose = true;

        public PreferencesFrame(String   title,
                                String[] argv)
        {
            super(title);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setIconImage(new ImageIcon(getClass()
                                           .getResource("resources/Preferences16.gif")).getImage());

            if (argv.length == 1)
            {
                if (argv[0].equals(PreferencesDialog.ARG_ECLIPSE))
                {
                    this.isExitOnClose = false;
                }
            }
        }
    }


    /**
     * Handler for the Apply button. Stores the preferences.
     */
    private class ApplyActionHandler
        implements ActionListener
    {
        public void actionPerformed(ActionEvent ev)
        {
            if (storePreferences())
            {
                notifyListeners(ev);
            }
        }
    }


    /**
     * Handler for the Cancel button. Closes the dialog.
     */
    private class CancelActionHandler
        implements ActionListener
    {
        public void actionPerformed(ActionEvent ev)
        {
            _previewFrame.dispose();
            _preferencesContainer.dispose();
            dispose();

            Container c = getParent();

            // was the dialog invoked from the command line?
            if (c instanceof PreferencesFrame)
            {
                PreferencesFrame f = (PreferencesFrame)c;

                if (f.isExitOnClose)
                {
                    System.exit(0);
                }
                else
                {
                    f.dispose();
                }
            }
        }
    }


    /**
     * Handler for the OK button. Stores the preferences and closes the
     * dialog.
     */
    private class OkActionHandler
        implements ActionListener
    {
        public void actionPerformed(ActionEvent ev)
        {
            if (storePreferences())
            {
                notifyListeners(ev);
                _previewFrame.dispose();
                _preferencesContainer.dispose();
                dispose();

                Container c = getParent();

                // was the dialog invoked from the command line?
                if (c instanceof PreferencesFrame)
                {
                    PreferencesFrame f = (PreferencesFrame)c;

                    if (f.isExitOnClose)
                    {
                        System.exit(0);
                    }
                    else
                    {
                        f.dispose();
                    }
                }
            }
        }
    }
}
