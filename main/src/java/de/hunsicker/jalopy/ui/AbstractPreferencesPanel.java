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

import de.hunsicker.jalopy.prefs.Preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;


/**
 * Base class to implement the different preferences pages.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public abstract class AbstractPreferencesPanel
    extends JPanel
{
    //~ Instance variables ····················································

    /** Listener used to trigger an update of the preview frame. */
    protected final ActionListener trigger = new UpdateTrigger();

    /** The preferences to display/edit. */
    protected Preferences prefs;

    /**
     * Our container. May be null if the preferences pages are directly
     * embedded into a Java appplication.
     */
    private PreferencesContainer _container;

    /** The category of the page. */
    private String _category;

    /** The title to display in title bar. */
    private String _title;

    //~ Constructors ··························································

    /**
     * Creates a new AbstractPreferencesPanel.
     */
    public AbstractPreferencesPanel()
    {
        this.prefs = Preferences.getInstance();
    }


    /**
     * Creates a new AbstractPreferencesPanel.
     *
     * @param container the parent container.
     */
    AbstractPreferencesPanel(PreferencesContainer container)
    {
        this();
        _container = container;
    }

    //~ Methods ·······························································

    /**
     * Sets the name of the category.
     *
     * @param category name of the category.
     */
    public void setCategory(String category)
    {
        _category = category;
    }


    /**
     * Returns the category.
     *
     * @return The category.
     */
    public String getCategory()
    {
        return _category;
    }


    /**
     * Returns the parent container.
     *
     * @return The parent container.
     */
    public PreferencesContainer getContainer()
    {
        return _container;
    }


    /**
     * Sets the preferences to display/edit.
     *
     * @param preferences preferences to display/edit.
     */
    public void setPreferences(Preferences preferences)
    {
        this.prefs = preferences;
    }


    /**
     * Returns the current preferences.
     *
     * @return The current preferences.
     */
    public Preferences getPreferences()
    {
        return this.prefs;
    }


    /**
     * Sets the title of the panel.
     *
     * @param title the title.
     */
    public void setTitle(String title)
    {
        _title = title;
    }


    /**
     * Returns the title of the panel.
     *
     * @return The title of the panel.
     */
    public String getTitle()
    {
        return _title;
    }


    /**
     * Stores the settings.
     */
    public abstract void store();


    /**
     * Returns the file name of the preview file to use for this page.
     * Normally the file name is equalivalent to the category name, but pages
     * that uses tabbed panes may want to override this method to provide
     * different files for their different panes.
     *
     * @return the file name (no path, without extension) of the preview file
     *         to use. Returns <code>null</code> if the page does not have a
     *         preview file associated.
     *
     * @since 1.0b8
     */
    public String getPreviewFileName()
    {
        return _category;
    }


    /**
     * Validates the page's preferences settings. Pages that needs their input
     * validated should override to provide the actually needed
     * implementation.
     * 
     * <p>
     * In case of any violation the implementation should simply display an
     * error message and throw an Exception to inform the caller about the
     * the invalid input.
     * </p>
     *
     * @throws ValidationException if the current settings are not valid.
     *
     * @since 1.0b8
     */
    public void validateSettings()
        throws ValidationException
    {
    }

    //~ Inner Classes ·························································

    private final class UpdateTrigger
        implements ActionListener
    {
        Timer timer;

        public synchronized void actionPerformed(ActionEvent ev)
        {
            if (_container != null)
            {
                if (this.timer == null)
                {
                    this.timer = new Timer(20,
                                           new ActionListener()
                        {
                            public void actionPerformed(ActionEvent event)
                            {
                                UpdateTrigger.this.timer.stop();

                                PreviewFrame preview = _container.getPreview();
                                String filename = getPreviewFileName();
                                String text = _container.loadPreview(filename);

                                if (text != null)
                                {
                                    try
                                    {
                                        preview.getCurrentPage()
                                               .validateSettings();
                                        preview.setText(text);
                                    }
                                    catch (ValidationException ex)
                                    {
                                        return;
                                    }
                                    catch (Throwable ex)
                                    {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });
                    this.timer.start();
                }
                else
                {
                    this.timer.restart();
                }
            }
        }
    }
}
