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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;


/**
 * Handles backup generation for files.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class FileBackup
{
    //~ Static variables/initializers ·········································

    /**
     * Make numbered backups of every file by appending ~REVISION~ to the
     * file. The revision count gets increased with every new backup.
     */
    public static final int NUMBERED = 2;

    /**
     * Make simple backups of every file. Filenames are not numbered and just
     * end in a suffix that signifies a backup file, i.e. no revisions are
     * available as every backup file overwrites the last backup written.
     */
    public static final int SIMPLE = 1;

    //~ Constructors ··························································

    /**
     * Creates a new FileBackup object.
     */
    private FileBackup()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns the new backup file for the given file. Uses a backup level of
     * <code>5</code>.
     *
     * @param file the file to back up.
     * @param directory the directory to copy the backup to. If the directory
     *        doesn't exist, it will be created.
     *
     * @return the created backup file.
     *
     * @throws IOException if an I/O error occured.
     */
    public static synchronized File create(File file,
                                           File directory)
        throws IOException
    {
        return create(file, directory, NUMBERED, null, 5);
    }


    /**
     * Returns the name of the new backup file for the given file..
     *
     * @param file the file to back up.
     * @param directory the directory to copy the backup to. If the directory
     *        doesn't exist, it will be created.
     * @param backupLevel number of revisions to hold.
     *
     * @return the created backup file.
     *
     * @throws IOException if an I/O error occured.
     */
    public static synchronized File create(File file,
                                           File directory,
                                           int  backupLevel)
        throws IOException
    {
        return create(file, directory, NUMBERED, null, backupLevel);
    }


    /**
     * Creates and returns the new backup file for the given content.
     *
     * @param content content to write to a backup file.
     * @param filename the filename of the backup file.
     * @param directory the directory to create the backup in. If the
     *        directory doesn't exist, it will be created.
     * @param backupLevel number of revisions to hold.
     *
     * @return the created backup file.
     *
     * @throws IOException if an I/O error occured.
     */
    public static synchronized File create(String content,
                                           String filename,
                                           File   directory,
                                           int    backupLevel)
        throws IOException
    {
        // ensure that target directory exists
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new IOException("could not create target directory " +
                                      directory);
            }
        }

        int highestBackup = getLatestRevision(filename, directory);
        File backup = new File(directory + File.separator +
                               getVersionName(filename, highestBackup + 1));
        Writer out = null;

        try
        {
            out = new BufferedWriter(new FileWriter(backup));
            out.write(content);
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }

        removeObsoleteRevisions(highestBackup + 1, backupLevel, filename,
                                directory);

        return backup;
    }


    /**
     * Returns the new backup file for the given file.
     *
     * @param file the file to back up.
     * @param directory the directory to copy the backup to. If the directory
     *        doesn't exist, it will be created.
     * @param backupType the type of the backup. Either {@link #SIMPLE} or
     *        {@link #NUMBERED}.
     * @param suffix the suffix to designate a backup file, ignored for the
     *        {@link #NUMBERED} backup type.
     * @param backupLevel number of revisions to hold, ignored for the  {@link
     *        #SIMPLE} backup type.
     *
     * @return the created backup file.
     *
     * @throws IOException if an I/O error occured.
     */
    public static synchronized File create(File   file,
                                           File   directory,
                                           int    backupType,
                                           String suffix,
                                           int    backupLevel)
        throws IOException
    {
        // ensure that target directory exists
        if (!directory.exists())
        {
            if (!directory.mkdirs())
            {
                throw new IOException("could not create target directory " +
                                      directory);
            }
        }

        File backup = null;

        switch (backupType)
        {
            case SIMPLE :

                if (suffix == null)
                {
                    suffix = ".bak";
                }
                else if (!suffix.startsWith("."))
                {
                    suffix = "." + suffix;
                }

                backup = new File(file.getAbsolutePath() + suffix);
                copy(file, backup);

                return backup;

            case NUMBERED :

                int highestBackup = getLatestRevision(file.getName(), directory);
                backup = new File(directory + File.separator +
                                  getVersionName(file, highestBackup + 1));
                copy(file, backup);
                removeObsoleteRevisions(highestBackup + 1, backupLevel,
                                        file.getName(), directory);

                break;
        }

        return backup;
    }


    /**
     * Returns the revision number of the latest revision found for the given
     * filename.
     *
     * @param filename filename to check for revision numbers.
     * @param dir directory to search.
     *
     * @return revision number of the latest backup; returns <code>0</code> if
     *         no backup could be found.
     */
    private static int getLatestRevision(String filename,
                                         File   dir)
    {
        if (dir == null)
        {
            return 0;
        }

        File[] files = dir.listFiles();
        int result = 0;

        for (int i = 0; i < files.length; i++)
        {
            String name = files[i].getName();

            if (name.startsWith(filename))
            {
                int revision = getRevision(name);

                if (revision > result)
                {
                    result = revision;
                }
            }
        }

        return result;
    }


    /**
     * Checks whether the given string is a number.
     *
     * @param str string to check.
     *
     * @return <code>true</code> if <em>str</em> represents a number.
     */
    private static boolean isNumber(String str)
    {
        if (str == null)
        {
            return false;
        }

        int letters = 0;

        for (int i = 0, size = str.length(); i < size; i++)
        {
            if ((str.charAt(i) < 48) || (str.charAt(i) > 57))
            {
                letters++;
            }
        }

        if ((letters > 1) || ((letters == 1) && (str.length() == 1)))
        {
            return false;
        }

        return true;
    }


    /**
     * Returns the revision number of the filename.
     *
     * @param filename filename to check for a revision number.
     *
     * @return the found revision number; returns <code>0</code> if no
     *         revision number could be found.
     */
    private static int getRevision(String filename)
    {
        int startOffset = filename.indexOf('~');
        int endOffset = filename.indexOf('~', startOffset + 1);

        while ((startOffset < endOffset) && (startOffset > -1) &&
               (endOffset > -1))
        {
            String result = filename.substring(startOffset + 1, endOffset);
            startOffset = filename.indexOf('~', endOffset);
            endOffset = filename.indexOf('~', startOffset + 1);

            if (isNumber(result))
            {
                int revision = Integer.parseInt(result);

                if (revision > 0)
                {
                    return revision;
                }
            }
        }

        return 0;
    }


    /**
     * Concatenates the filename plus the revision string.
     *
     * @param file the file whose name to use.
     * @param revision the revision number.
     *
     * @return the resulting string.
     */
    private static String getVersionName(File file,
                                         int  revision)
    {
        return getVersionName(file.getName(), revision);
    }


    /**
     * Concatenates the filename plus the revision string.
     *
     * @param filename the filename  to use.
     * @param revision the revision number.
     *
     * @return the resulting string.
     */
    private static String getVersionName(String filename,
                                         int    revision)
    {
        StringBuffer buf = new StringBuffer(15);
        buf.append(filename);
        buf.append('~');
        buf.append(revision);
        buf.append('~');

        return buf.toString();
    }


    /**
     * Copies the given source file to the given destination.
     *
     * @param source source file.
     * @param target destination target file.
     *
     * @throws IOException if an I/O error occured.
     */
    private static void copy(File source,
                             File target)
        throws IOException
    {
        InputStream in = null;
        OutputStream out = null;

        try
        {
            in = new BufferedInputStream(new FileInputStream(source));
            out = new BufferedOutputStream(new FileOutputStream(target));

            byte[] buffer = new byte[8 * 1024];
            int count = 0;

            do
            {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            }
            while (count != -1);

            out.close();
            target.setLastModified(source.lastModified());
        }
        finally
        {
            in.close();
            out.close();
        }
    }


    /**
     * Removes all obsolete revisions for the given filename in the given
     * directory.
     *
     * @param currentRevision revision number of the latest revision.
     * @param backupLevel number of revisions to hold.
     * @param filename filename to check for revision numbers.
     * @param directory directory which holds the backups.
     */
    private static void removeObsoleteRevisions(int    currentRevision,
                                                int    backupLevel,
                                                String filename,
                                                File   directory)
    {
        File[] files = directory.listFiles();

        if (backupLevel > 0)
        {
            // delete old revisions
            for (int i = 0; i < files.length; i++)
            {
                String name = files[i].getName();

                if (name.startsWith(filename))
                {
                    if (getRevision(name) <= (currentRevision - backupLevel))
                    {
                        files[i].delete();
                    }
                }
            }
        }
    }
}
