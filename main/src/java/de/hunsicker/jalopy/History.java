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
package de.hunsicker.jalopy;

import de.hunsicker.io.IoHelper;
import de.hunsicker.jalopy.prefs.Preferences;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * History serves as a tracker for file modifications.
 * 
 * <p>
 * The {@link #flush} method may be used to synchronously force updates to the
 * backing store. Normal termination of the Java Virtual Machine will
 * <em>not</em> result in the loss of pending updates - an explicit flushing
 * is <em>not</em> required upon termination to ensure that pending updates
 * are made persistent.
 * </p>
 * 
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class History
{
    //~ Static variables/initializers ·········································

    private static final History INSTANCE = new History();
    private static Map _history; // Map of <String>:<History.Entry>

    //~ Constructors ··························································

    /**
     * Creates a new History object.
     */
    private History()
    {
        init();
    }

    //~ Methods ·······························································

    /**
     * Returns the sole instance of this class.
     *
     * @return class instance.
     */
    public static History getInstance()
    {
        return INSTANCE;
    }


    /**
     * Adds the given file to the history. It will only be added, if it exists
     * and indeed denotes a file (not a directory).
     *
     * @param file file to add.
     * @param packageName the package name of the file to add.
     * @param timestamp the time the file given was last processed.
     *
     * @throws IOException if an I/O error occured, which is possible because
     *         a canonical pathname will be constructed.
     */
    public synchronized void add(File   file,
                                 String packageName,
                                 long   timestamp)
        throws IOException
    {
        if (file.exists() && file.isFile())
        {
            _history.put(file.getCanonicalPath(),
                         new Entry(packageName, timestamp));
        }
    }


    /**
     * Clears the history.
     */
    public synchronized void clear()
    {
        _history.clear();
    }


    /**
     * Stores the history to the backing store.
     *
     * @throws IOException if an I/O error occured.
     */
    public synchronized void flush()
        throws IOException
    {
        File file = Preferences.getHistoryFile();
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
        ObjectOutputStream p = new ObjectOutputStream(out);

        try
        {
            p.writeObject(_history);
        }
        finally
        {
            if (p != null)
            {
                p.close();
            }
        }
    }


    /**
     * Returns the history entry for the given file.
     *
     * @param file the file to get the corresponding history entry for.
     *
     * @return the history entry for the given file or <code>null</code> if no
     *         entry for the given file exists.
     *
     * @throws IOException if an I/O error occured, which is possible because
     *         a canonical pathname will be constructed.
     */
    public synchronized Entry get(File file)
        throws IOException
    {
        return (Entry)_history.get(file.getCanonicalPath());
    }


    /**
     * Removes the given file from the history.
     *
     * @param file file to remove.
     *
     * @throws IOException if an I/O error occured, which is possible because
     *         a canonical pathname will be constructed.
     */
    public synchronized void remove(File file)
        throws IOException
    {
        _history.remove(file.getCanonicalPath());
    }


    /**
     * Initialization. Loads the history from the backing store.
     */
    private synchronized void init()
    {
        try
        {
            File file = Preferences.getHistoryFile();

            if (file.exists())
            {
                _history = (Map)IoHelper.deserialize(file);
            }
            else
            {
                _history = new HashMap();
            }
        }
        catch (Throwable ex)
        {
            _history = new HashMap();
        }

        Runtime.getRuntime().addShutdownHook(new TerminationHandler());
    }

    //~ Inner Classes ·························································

    /**
     * Represents a history policy.
     *
     * @since 1.0b8
     */
    public static final class Policy
    {
        /** Don't use the history. */
        public static final Policy DISABLED = new Policy("History.Policy [disabled]");

        /**
         * Insert a single line comment header at the top of every formatted
         * file.
         */
        public static final Policy COMMENT = new Policy("History.Policy [comment]");

        /**
         * Track file modifications in a binary file stored in the Jalopy
         * settings directory.
         */
        public static final Policy FILE = new Policy("History.Policy [file]");
        final String name;

        private Policy(String name)
        {
            this.name = name.intern();
        }

        /**
         * Returns the policy for the given name.
         *
         * @param name a valid policy name. Either &quot;none&quot;,
         *        &quot;file&quot; or &quot;comment&quot; (case-sensitive).
         *
         * @return The policy for the given name.
         *
         * @throws IllegalArgumentException if an invalid name specified.
         */
        public static Policy valueOf(String name)
        {
            String n = name.intern();

            if (FILE.name == n)
            {
                return FILE;
            }
            else if (COMMENT.name == n)
            {
                return COMMENT;
            }
            else if (DISABLED.name == n)
            {
                return DISABLED;
            }

            throw new IllegalArgumentException("no valid history policy name -- " +
                                               name);
        }


        /**
         * Returns a string representation of this object.
         *
         * @return A string representation of this object.
         */
        public String toString()
        {
            return this.name;
        }
    }


    /**
     * Represents a history entry.
     */
    public static final class Entry
        implements Serializable
    {
        /** Use serialVersionUID for interoperability. */
        static final long serialVersionUID = 7661537884776942605L;

        /** The package name of the entry. */
        String packageName;

        /** The time this entry was last processed. */
        long lastmod;

        /**
         * Creates a new entry object.
         *
         * @param packageName the package name of the entry.
         * @param time the time this entry was last processed.
         */
        public Entry(String packageName,
                     long   time)
        {
            this.packageName = packageName;
            this.lastmod = time;
        }

        /**
         * Returns the last modification time stamp.
         *
         * @return last modification time stamp.
         */
        public long getLastModification()
        {
            return this.lastmod;
        }


        /**
         * Returns the package name of the entry.
         *
         * @return the package name.
         */
        public String getPackageName()
        {
            return this.packageName;
        }


        /**
         * Returns a string representation of this entry.
         *
         * @return a string representation of this entry.
         */
        public String toString()
        {
            StringBuffer buf = new StringBuffer(30);
            buf.append('%');
            buf.append(this.lastmod);
            buf.append(':');
            buf.append(this.packageName);
            buf.append('%');

            return buf.toString();
        }
    }


    /**
     * Executed before the JVM terminates. Flushes the history to disk.
     */
    private final class TerminationHandler
        extends Thread
    {
        public synchronized void run()
        {
            try
            {
                if (_history != null)
                {
                    flush();
                }
            }
            catch (IOException ex)
            {
                /**
                 * @todo log error message
                 */
            }
        }
    }
}
