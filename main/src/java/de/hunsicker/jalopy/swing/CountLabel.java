/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.swing;

import javax.swing.JLabel;

import de.hunsicker.util.ResourceBundleFactory;


/**
 * A label which displays its label text along with an integer count.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class CountLabel
    extends JLabel
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final String EMPTY_STRING = "" /* NOI18N */.intern();

    //~ Instance variables ---------------------------------------------------------------

    /** The current count. */
    private int _count;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new CountLabel object with the text 'Counter' and an initial count of
     * zero.
     */
    public CountLabel()
    {
        this(EMPTY_STRING);

        super.setText(
            ResourceBundleFactory.getBundle(
                "de.hunsicker.jalopy.swing.Bundle" /* NOI18N */).getString(
                "LBL_COUNT" /* NOI18N */));
    }


    /**
     * Creates a new CountLabel object with an initial count of zero.
     *
     * @param text the label text.
     */
    public CountLabel(String text)
    {
        this(text, 0);
    }


    /**
     * Creates a new CountLabel object.
     *
     * @param text the label text.
     * @param initialCount the initial value of the count.
     */
    public CountLabel(
        String text,
        int    initialCount)
    {
        super(text + ':' + initialCount);
        _count = initialCount;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Sets the counter to the given value.
     *
     * @param count the count to display.
     */
    public void setCount(int count)
    {
        _count = count;

        String text = getText();
        setText(text.substring(0, text.indexOf(':')).intern() + ':' + count);
    }


    /**
     * Returns the current count.
     *
     * @return the current count.
     */
    public int getCount()
    {
        return _count;
    }


    /**
     * Increases the counter.
     */
    public void increase()
    {
        setCount(++_count);
    }


    /**
     * Resets the counter.
     */
    public void reset()
    {
        _count = 0;
        setCount(0);
    }
}