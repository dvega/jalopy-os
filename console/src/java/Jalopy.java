/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
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
