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

import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Key;
import de.hunsicker.jalopy.prefs.Keys;


/**
 * A component that can be used to display/edit the Jalopy footer preferences.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class FooterPanel
    extends HeaderPanel
{
    //~ Constructors ··························································

    /**
     * Creates a new FooterPanel object.
     */
    public FooterPanel()
    {
        super();
    }


    /**
     * Creates a new FooterPanel.
     *
     * @param container the parent container.
     */
    FooterPanel(PreferencesContainer container)
    {
        super(container);
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    protected String getDefaultAfter()
    {
        return String.valueOf(Defaults.BLANK_LINES_AFTER_FOOTER);
    }

    /**
     * {@inheritDoc}
     */
    protected String[] getItemsAfter()
    {
        return new String[] { "1", "2", "3", "4", "5" };
    }

    /**
     * {@inheritDoc}
     */
    protected Key getBlankLinesAfterKey()
    {
        return Keys.BLANK_LINES_AFTER_FOOTER;
    }


    /**
     * {@inheritDoc}
     */
    protected Key getBlankLinesBeforeKey()
    {
        return Keys.BLANK_LINES_BEFORE_FOOTER;
    }


    /**
     * {@inheritDoc}
     */
    protected String getDeleteLabel()
    {
        return "Delete Footers";
    }


    /**
     * {@inheritDoc}
     */
    protected Key getKeysKey()
    {
        return Keys.FOOTER_KEYS;
    }


    /**
     * {@inheritDoc}
     */
    protected Key getSmartModeKey()
    {
        return Keys.FOOTER_SMART_MODE_LINES;
    }


    /**
     * {@inheritDoc}
     */
    protected Key getTextKey()
    {
        return Keys.FOOTER_TEXT;
    }


    /**
     * {@inheritDoc}
     */
    protected Key getUseKey()
    {
        return Keys.FOOTER;
    }


    /**
     * {@inheritDoc}
     */
    protected String getUseLabel()
    {
        return "Use Footer";
    }
}
