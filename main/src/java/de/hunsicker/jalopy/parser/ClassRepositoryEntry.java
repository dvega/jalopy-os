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
package de.hunsicker.jalopy.parser;

import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Preferences;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Set;


/**
 * Represents a repository entry for a given Java library. An entry consists
 * of meta information and the actual data stored in a set.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @see ClassRepository
 */
public class ClassRepositoryEntry
{
    //~ Instance variables ····················································

    /** The entry info. */
    Info info;

    /** The actual data. */
    Set data = Collections.EMPTY_SET; // Set of <String>

    //~ Constructors ··························································

    /**
     * Creates a new ClassRepositoryEntry object.
     *
     * @param info the entry information.
     * @param data set with the actual data.
     */
    public ClassRepositoryEntry(Info info,
                                Set  data)
    {
        this.data = data;
        this.info = info;
    }


    /**
     * Creates a new ClassRepositoryEntry object.
     *
     * @param location the location of the original source.
     * @param lastModified
     * @param data contents.
     */
    public ClassRepositoryEntry(File location,
                                long lastModified,
                                Set  data)
    {
        this(new Info(location), data);
    }


    /**
     * Sole constructor.
     */
    ClassRepositoryEntry()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns the entry information for the given repository entry file.
     *
     * @param file repository entry file (those ending with
     *        <code>.jdb</code>).
     *
     * @return entry information.
     *
     * @throws IOException if an I/O error occured.
     */
    public static Info getInfo(File file)
        throws IOException
    {
        ObjectInputStream in = null;

        try
        {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));

            return (Info)in.readObject();
        }
        catch (ClassNotFoundException neverOccurs)
        {
            return null;
        }
        finally
        {
            in.close();
        }
    }


    /**
     * Sets the data of the entry.
     *
     * @param data data.
     */
    public void setData(Set data)
    {
        this.data = data;
    }


    /**
     * Returns the current data.
     *
     * @return data.
     */
    public Set getData()
    {
        return this.data;
    }


    /**
     * Returns the entry information.
     *
     * @return entry information.
     */
    public Info getInfo()
    {
        return this.info;
    }


    /**
     * Indicates whether some other object is 'equal to' this one.
     *
     * @param o the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the <em>o</em>
     *         argument.
     */
    public boolean equals(Object o)
    {
        if (!(o instanceof ClassRepositoryEntry))
        {
            return false;
        }

        return this.data.equals(((ClassRepositoryEntry)o).data);
    }


    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    public int hashCode()
    {
        return this.data.hashCode();
    }


    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString()
    {
        return "ClassRepositoryEntry: " + this.info.getLocation();
    }

    //~ Inner Classes ·························································

    /**
     * Provides information about a repository entry.
     */
    public static final class Info
        implements Serializable,
                   Comparable
    {
        /** Use serialVersionUID for interoperability. */
        static final long serialVersionUID = 6093443653626639672L;

        /** The filename of the entry. */
        transient String filename;

        /** The location of the original source. */
        transient String location;

        /** Indicates whether the entry was loaded into memory. */
        transient boolean loaded;

        /**
         * Creates a new Info object.
         *
         * @param location location of the source for the entry (either file
         *        or directory are valid).
         *
         * @throws IllegalArgumentException if <em>location</em> does not
         *         denote an existing archive or directory.
         */
        public Info(File location)
        {
            if (!location.exists())
            {
                throw new IllegalArgumentException("location does not exist -- " +
                                                   location);
            }

            this.location = location.getAbsolutePath();

            if ((!location.isDirectory()) && (!this.location.endsWith(".jar")) &&
                (!this.location.endsWith(".zip")))
            {
                throw new IllegalArgumentException(location +
                                                   " does denote archive or directory");
            }

            ClassRepositoryEntry.Info info = ClassRepository.getInstance()
                                                            .get(location);

            if (info == null)
            {
                if (location.isDirectory())
                {
                    this.filename = String.valueOf(System.currentTimeMillis() +
                                                   ClassRepository.EXT_REPOSITORY);
                }
                else
                {
                    this.filename = genFilename(location.getName()
                                                        .substring(0,
                                                                   location.getName()
                                                                           .lastIndexOf('.')) +
                                                ClassRepository.EXT_REPOSITORY);
                }
            }
            else
            {
                this.filename = info.getFilename();
            }
        }

        /**
         * Returns the filename under which this entry is stored.
         *
         * @return the filename of the entry.
         */
        public String getFilename()
        {
            return this.filename;
        }


        public void setLoaded(boolean loaded)
        {
            this.loaded = loaded;
        }


        public boolean isLoaded()
        {
            return this.loaded;
        }


        /**
         * Returns the original location of the entry's data.
         *
         * @return the original location.
         */
        public File getLocation()
        {
            return new File(this.location);
        }


        /**
         * Determines whether this entry can be refreshed.
         *
         * @return <code>true</code> if the entry can be refreshed.
         */
        public boolean isRefreshable()
        {
            return new File(this.location).exists();
        }


        /**
         * Compares this object with the specified object for order.
         *
         * @param o the object to be compared.
         *
         * @return a negative integer, zero, or a positive integer as this
         *         object is less than, equal to, or greater than the
         *         specified object.
         *
         * @throws ClassCastException if the specified object's type prevents
         *         it from being compared to this object.
         */
        public int compareTo(Object o)
        {
            /**
             * @todo maybe we should do locale dependent sorting here
             */
            if (o instanceof Info)
            {
                return this.location.compareToIgnoreCase(((Info)o).location);
            }
            else if (o instanceof String)
            {
                return this.location.compareToIgnoreCase((String)o);
            }

            throw new ClassCastException(o.getClass().getName());
        }


        public boolean equals(Object o)
        {
            if (o instanceof Info)
            {
                return this.location.equals(((Info)o).location);
            }
            else if (o instanceof String)
            {
                return this.location.equals((String)o);
            }

            return false;
        }


        public int hashCode()
        {
            return this.location.hashCode();
        }


        /**
         * Returns a string representation of the object.
         *
         * @return a string representation of the object.
         */
        public String toString()
        {
            return this.location + " [" + this.filename + "]";
        }


        private String genFilename(String filename)
        {
            File file = new File(Preferences.getInstance()
                                            .get(Keys.CLASS_REPOSITORY_DIRECTORY,
                                                 Preferences.getRepositoryDirectory()
                                                            .getAbsolutePath()) +
                                 filename);

            if (file.exists())
            {
                int paren = filename.indexOf('(');

                if (paren > -1)
                {
                    String number = filename.substring(paren + 1,
                                                       filename.lastIndexOf(')'));

                    try
                    {
                        int n = Integer.parseInt(number);
                        filename = filename.substring(0,
                                                      filename.lastIndexOf('(')) +
                                   "(" + (++n) + ")" +
                                   ClassRepository.EXT_REPOSITORY;
                    }
                    catch (Exception ex)
                    {
                        throw new RuntimeException("error creating filename for " +
                                                   filename);
                    }
                }
                else
                {
                    filename = filename.substring(0, filename.lastIndexOf('.')) +
                               "(1)" + ClassRepository.EXT_REPOSITORY;
                }

                filename = genFilename(filename);
            }

            return filename;
        }


        private void readObject(ObjectInputStream in)
            throws IOException, 
                   ClassNotFoundException
        {
            in.defaultReadObject();
            this.location = (String)in.readObject();
            this.filename = (String)in.readObject();
        }


        /**
         * DOCUMENT ME!
         *
         * @todo document serial data
         */
        private void writeObject(ObjectOutputStream out)
            throws IOException
        {
            out.defaultWriteObject();
            out.writeObject(this.location);
            out.writeObject(this.filename);
        }
    }
}
