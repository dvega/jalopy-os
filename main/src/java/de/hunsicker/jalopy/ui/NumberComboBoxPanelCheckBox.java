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

/**
 * DOCUMENT ME!
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class NumberComboBoxPanelCheckBox
    extends ComboBoxPanelCheckBox
{
    //~ Constructors ··························································

    /**
     * Creates a new ComboBoxPanelCheckBox.
     *
     * @param checkText the text of the panel label.
     * @param comboText the text of the check box label.
     * @param items the items to display in the check box.
     */
    public NumberComboBoxPanelCheckBox(String   checkText,
                                       String   comboText,
                                       Object[] items)
    {
        this(checkText, false, comboText, items, null);
    }


    /**
     * Creates a new ComboBoxPanelCheckBox.
     *
     * @param checkText the text of the check box.
     * @param selected a boolean value indicating the initial selection state.
     *        If <code>true</code> the check box is selected.
     * @param comboText the text of the combo box panel.
     * @param items the items to display in the combo box panel.
     * @param item the item to initially select in the combo box panel.
     */
    public NumberComboBoxPanelCheckBox(String   checkText,
                                       boolean  selected,
                                       String   comboText,
                                       Object[] items,
                                       Object   item)
    {
        super(checkText, selected, comboText, items, item);
        setComboBoxPanel(new NumberComboBoxPanel(comboText, items, item));
    }
}
