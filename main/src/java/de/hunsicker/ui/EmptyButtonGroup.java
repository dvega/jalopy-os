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
package de.hunsicker.ui;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;


/**
 * Extends the standard button group to allow empty groups. Again creating a
 * set of buttons with the same <em>EmptyButtonGroup</em> object means that
 * turning 'on' one of those buttons turns 'off' all other buttons in the
 * group. The difference between <em>EmptyButtonGroup</em> and
 * <em>ButtonGroup</em> lies in the fact that the currently selected button
 * can be deselected which results - in an empty group.
 * 
 * <p>
 * Note that the original documentation for <em>ButtonGroup</em> (as of JDK
 * 1.3) is wrong in that the initial state of the group does depend on the
 * state of the added buttons (They claim 'Initially, all buttons in the
 * group are unselected').
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class EmptyButtonGroup
    extends ButtonGroup
{
    //~ Instance variables ····················································

    /**
     * The currently selected button. May be <code>null</code> if no button is
     * selected.
     */
    protected ButtonModel selection;

    //~ Constructors ··························································

    /**
     * Creates a new EmptyButtonGroup object.
     */
    public EmptyButtonGroup()
    {
    }

    //~ Methods ·······························································

    /**
     * Sets the selected state of the given button model.
     *
     * @param model model that has its state changed.
     * @param select if <code>true</code> selects the given model.
     */
    public void setSelected(ButtonModel model,
                            boolean     select)
    {
        if (select)
        {
            ButtonModel oldSelection = this.selection;

            // select the button
            this.selection = model;

            // and change the old selection if any
            if (oldSelection != null)
            {
                oldSelection.setSelected(false);
            }
        }
        else
        {
            // unselect the button if find a selected one
            // (now the group is empty again)
            if (this.selection == model)
            {
                this.selection = null;
            }
        }
    }


    /**
     * Indicates whether the given button model is selected.
     *
     * @param model model to check.
     *
     * @return <code>true</code> if the given model is selected.
     */
    public boolean isSelected(ButtonModel model)
    {
        return (model == this.selection);
    }


    /**
     * Returns the selected button model.
     *
     * @return the selected button model.
     */
    public ButtonModel getSelection()
    {
        return this.selection;
    }


    /**
     * Adds the given button to the group. If the button is selected and the
     * group not already contains aselected button, the button will retain
     * its selection.
     *
     * @param button button to add.
     */
    public void add(AbstractButton button)
    {
        if (button == null)
        {
            return;
        }

        this.buttons.add(button);

        if ((this.selection == null) && button.isSelected())
        {
            this.selection = button.getModel();
        }

        button.getModel().setGroup(this);
    }
}
