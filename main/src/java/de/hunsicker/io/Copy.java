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
package de.hunsicker.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Helper to copy files or directories.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class Copy
{
    //~ Constructors ··························································

    /**
     * Creates a new Copy object.
     */
    private Copy()
    {
    }

    //~ Methods ·······························································

    /**
     * Copies the given directory to the new location.
     *
     * @param source the directory to copy.
     * @param destination the destination directory.
     *
     * @return <code>true</code> if the operation ended upon success.
     *
     * @throws IOException if an I/O error occured.
     * @throws NullPointerException if<code>source == null</code>
     * @throws IllegalArgumentException if <em>source</em> does not exist or
     *         does not denote a directory.
     */
    public static boolean directory(File source,
                                    File destination)
        throws IOException
    {
        if (source == null)
        {
            throw new NullPointerException();
        }

        if (!source.exists())
        {
            throw new IllegalArgumentException("source not found -- " + source);
        }

        if (!source.isDirectory())
        {
            throw new IllegalArgumentException("source no directory -- " +
                                               source);
        }

        File[] files = source.listFiles();
        boolean success = true;

        for (int i = 0; i < files.length; i++)
        {
            File file = files[i];

            if (files[i].isFile())
            {
                success = file(files[i],
                               new File(destination, files[i].getName()));
            }
            else
            {
                success = directory(files[i],
                                    new File(destination, files[i].getName()));
            }

            if (!success)
            {
                return false;
            }
        }

        return success;
    }


    /**
     * Copies the given file to <em>dest</em>. Calls {@link
     * #file(File,File,boolean)}.
     *
     * @param source source file.
     * @param destination destination file.
     *
     * @return Returns <code>true</code> if the file was sucessfully copied.
     *
     * @throws IOException if an I/O error occured.
     */
    public static boolean file(String source,
                               String destination)
        throws IOException
    {
        return file(new File(source), new File(destination), false);
    }


    /**
     * Copies the given file to <em>dest</em>. Calls {@link
     * #file(File,File,boolean)}.
     *
     * @param source source file.
     * @param destination destination file.
     *
     * @return Returns <code>true</code> if the file was sucessfully copied
     *
     * @throws IOException if an I/O error occured.
     */
    public static boolean file(File source,
                               File destination)
        throws IOException
    {
        return file(source, destination, false);
    }


    /**
     * Copies the given file to <em>dest</em>.
     *
     * @param source source file.
     * @param destination destination file.
     * @param overwrite if <code>true</code> the destination file will be
     *        always overwritten if it already exists; if <code>false</code>
     *        it will only be overwritten if the source file is newer than
     *        the destination file.
     *
     * @return Returns <code>true</code> if the file was sucessfully copied
     *
     * @throws IOException if an I/O error occured.
     * @throws NullPointerException if <code>source == null</code>
     * @throws IllegalArgumentException if <em>source</em> does not exist or
     *         does not denote a file.
     */
    public static boolean file(File    source,
                               File    destination,
                               boolean overwrite)
        throws IOException
    {
        if (source == null)
        {
            throw new NullPointerException();
        }

        if (!source.exists())
        {
            throw new IllegalArgumentException("source not found -- " + source);
        }

        if (!source.isFile())
        {
            throw new IllegalArgumentException("source no file -- " + source);
        }

        if (overwrite || (destination.lastModified() < source.lastModified()))
        {
            // ensure that parent directory of destination file exists
            File parent = destination.getAbsoluteFile().getParentFile();

            if ((parent != null) && (!parent.exists()))
            {
                parent.mkdirs();
            }

            InputStream in = new BufferedInputStream(new FileInputStream(source));
            OutputStream out = new BufferedOutputStream(new FileOutputStream(destination));
            byte[] buffer = new byte[8 * 1024];
            int count = 0;

            try
            {
                do
                {
                    out.write(buffer, 0, count);
                    count = in.read(buffer, 0, buffer.length);
                }
                while (count != -1);
            }
            finally
            {
                if (in != null)
                {
                    in.close();
                }

                if (out != null)
                {
                    out.close();
                }
            }

            return true;
        }

        return false;
    }
}
