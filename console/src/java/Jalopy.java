/*
 * Jalopy Java Source Code Formatter
 *
 * Copyright (c) 2002, Marco Hunsicker. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */
import de.hunsicker.jalopy.plugin.console.ConsolePlugin;


/**
 * Shorthand for {@link de.hunsicker.jalopy.plugin.console.ConsolePlugin}.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class Jalopy
{
    //~ Methods --------------------------------------------------------------------------

    /**
     * Command line entry point. Delegates to {@link
     * de.hunsicker.jalopy.plugin.console.ConsolePlugin#main}.
     *
     * @param argv command line arguments. Type <code>java Jalopy -h</code> for details
     *        about the valid options.
     */
    public static void main(String[] argv)
    {
        ConsolePlugin.main(argv);
    }
}
