/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

/**
 * A class that wraps some application specific annotation data. This class can be used
 * to track the position information for things like debugger breakpoints, erroneous
 * lines, and so on.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b9
 */
public final class Position
{
    //~ Instance variables ---------------------------------------------------------------

    int line = 1;
    int column = 1;

    //~ Constructors ---------------------------------------------------------------------

    Position(int line, int column)
    {
        this.line = line;
        this.column = column;
    }

    //~ Methods --------------------------------------------------------------------------


    public int getColumn()
    {

        return this.column;
        }

    public int getLine()
    {
        return this.line;
    }

    public String toString()
    {
        return this.line + ":" + this.column;
    }
}
