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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


/**
 * Some I/O helper routines.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class IoHelper
{
    //~ Constructors ··························································

    /**
     * Creates a new IoHelper object.
     */
    private IoHelper()
    {
    }

    //~ Methods ·······························································

    /**
     * Deletes the given file or directory.
     *
     * @param file a file or directory.
     * @param recursive if <code>true</code> directories will be deleted
     *        recursively.
     *
     * @return <code>true</code> if the file or directory could be deleted
     *         successfully.
     */
    public static boolean delete(File    file,
                                 boolean recursive)
    {
        if (file.exists())
        {
            if (file.isFile())
            {
                return file.delete();
            }
            else
            {
                if (recursive)
                {
                    File[] files = file.listFiles();
                    boolean success = false;

                    for (int i = 0; i < files.length; i++)
                    {
                        if (files[i].isDirectory() &&
                            (files[i].list().length != 0))
                        {
                            success = delete(files[i], true);
                        }
                        else
                        {
                            success = files[i].delete();
                        }

                        if (!success)
                        {
                            return false;
                        }
                    }

                    return file.delete();
                }
                else
                {
                    return file.delete();
                }
            }
        }
        else
        {
            return false;
        }
    }


    /**
     * Deserializes an object previously written using an ObjectOutputStream.
     *
     * @param data binary array.
     *
     * @return deserialized object.
     *
     * @throws IOException if an I/O error occured.
     *
     * @see #serialize
     */
    public static Object deserialize(byte[] data)
        throws IOException
    {
        if (data.length == 0)
        {
            return null;
        }

        return deserialize(new BufferedInputStream(new ByteArrayInputStream(data)));
    }


    /**
     * Deserializes the object stored in the given stream.
     *
     * @param in an input stream.
     *
     * @return the deserialized object.
     *
     * @throws IOException if an I/O exception occured.
     */
    public static Object deserialize(InputStream in)
        throws IOException
    {
        ObjectInputStream oin = new ObjectInputStream(in);

        try
        {
            return oin.readObject();
        }
        catch (ClassNotFoundException ex)
        {
            /**
             * @todo once we only support JDK 1.4, add chained exception
             */
            throw new IOException(ex.getMessage());
        }
        finally
        {
            if (oin != null)
            {
                oin.close();
            }
        }
    }


    /**
     * Deserializes the object stored in the given file.
     *
     * @param file a file.
     *
     * @return the deserialized object.
     *
     * @throws IOException if an I/O exception occured.
     */
    public static final Object deserialize(File file)
        throws IOException
    {
        return deserialize(new BufferedInputStream(new FileInputStream(file)));
    }


    /**
     * Verifies the existence of the given directory and if that directory
     * does not yet exist, tries to create it.
     *
     * @param directory directory to check for existence.
     *
     * @return <code>true</code> if the directory already exists or was
     *         successfully created.
     *
     * @throws IllegalArgumentException if <em>directory</em> does exist but
     *         does not denote a valid directory.
     */
    public static boolean ensureDirectoryExists(File directory)
    {
        if (!directory.exists())
        {
            return directory.mkdirs();
        }
        else if (!directory.isDirectory())
        {
            throw new IllegalArgumentException("no directory -- " + directory);
        }

        return true;
    }


    /**
     * Serializes the given object to the given output stream.
     *
     * @param o a serializable object.
     * @param out the stream to write the object to.
     *
     * @throws IOException if an I/O exception occured.
     */
    public static void serialize(Object       o,
                                 OutputStream out)
        throws IOException
    {
        ObjectOutputStream oout = new ObjectOutputStream(out);

        try
        {
            oout.writeObject(o);
        }
        finally
        {
            if (oout != null)
            {
                oout.close();
            }
        }
    }


    /**
     * Serializes the given object to the given file.
     *
     * @param o a serializable object.
     * @param file the file to write the object to.
     *
     * @throws IOException if an I/O exception occured.
     */
    public static void serialize(Object o,
                                 File   file)
        throws IOException
    {
        serialize(o, new BufferedOutputStream(new FileOutputStream(file)));
    }
}
