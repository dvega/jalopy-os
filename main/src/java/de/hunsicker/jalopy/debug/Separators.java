/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.debug;

import java.io.File;
import java.io.FileReader;
import java.io.PushbackReader;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class Separators
{
    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv)
    {
        int line = 0;

        try
        {
            PushbackReader in = new PushbackReader(new FileReader(new File(argv[0])));
            int c = -1;

            while ((c = in.read()) > -1)
            {
                switch (c)
                {
                    case '\r' :
                        line++;

                        int cc = -1;

                        if ((cc = in.read()) > -1)
                        {
                            switch (cc)
                            {
                                case '\n' :
                                    System.err.println("line " + line + ": DOS");

                                    break;

                                default :
                                    System.err.println("line " + line + ": MAC");
                                    in.unread(cc);

                                    break;
                            }
                        }

                        break;

                    case '\n' :
                        line++;
                        System.err.println("line " + line + ": UNIX");

                        break;
                }
            }
        }
        catch (Throwable ex)
        {
            ex.printStackTrace();
        }
    }
}
