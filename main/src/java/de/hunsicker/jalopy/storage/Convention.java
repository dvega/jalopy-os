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
package de.hunsicker.jalopy.storage;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;

import org.jdom.input.SAXBuilder;

import org.jdom.output.XMLOutputter;

import de.hunsicker.io.Copy;
import de.hunsicker.io.ExtensionFilter;
import de.hunsicker.io.IoHelper;
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.parser.DeclarationType;
import de.hunsicker.util.ChainingRuntimeException;
import de.hunsicker.util.StringHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.oro.text.perl.Perl5Util;


//J- needed only as a workaround for a Javadoc bug
import java.lang.Long;
import java.lang.Object;
import java.lang.NullPointerException;
//J+

/**
 * Represents a source code convention: the settings that describe
 * the style Java source files should look like.
 *
 * <p>To ensure type-safety, valid key access, two accompanying classes are provided:</p>
 *
 * <pre style="background:lightgrey">
 * {@link Convention} settings = {@link Convention}.getInstance();
 * int numThreads = settings.getInt({@link Keys}.THREAD_COUNT,
 *                                  {@link Defaults}.THREAD_COUNT));
 * </pre>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @author <a href="http://jalopy.sf.net/contact.html">Roman Sarychev</a>
 * @version $Revision$
 *
 * @see de.hunsicker.jalopy.storage.Keys
 * @see de.hunsicker.jalopy.storage.Defaults
 */
public class Convention
{
    //~ Static variables/initializers ·········································

    /**
     * The file extension for Jalopy binary code convention files
     * (&quot;.jal&quot;).
     */
    public static final String EXTENSION_JAL = ".jal";

    /**
     * The file extension for Jalopy XML code convention files (&quot;.xml&quot;).
     */
    public static final String EXTENSION_XML = ".xml";

    /**
     * The file extension for Jalopy local binary code convention files
     * (&quot;.dat&quot;).
     */
    public static final String EXTENSION_DAT = ".dat";

    /** The filename of project files. */
    private static final String FILENAME_PROJECT = "project.dat";

    /** The filename of the code convention files. */
    private static final String FILENAME_PREFERENCES = "preferences.dat";

    /** The filename of the code convention settings files. */
    private static final String FILENAME_SETTINGS = "settings.xml";

    /** The name of the repository directories. */
    private static final String NAME_REPOSITORY = "repository";

    /** The filename of the history files. */
    private static final String FILENAME_HISTORY = "history.dat";

    /** The name of the backup directories. */
    private static final String NAME_BACKUP = "bak";

    /** The empty map, effectively means the default values. */
    private static final Map EMPTY_MAP = new HashMap(); // Map of <Key>:<String>

    /** The current version number. */
    private static final String VERSION = "5";

    /** Object to synchronize processes on. */
    private static final Object _lock = new Object();

    /** The sole instance of this class. */
    private static Convention INSTANCE;

    /**
     * The empty code convention, used if no code convention could be loaded from
     * persistent storage. This either means no code convention were ever stored
     * or something went wrong during the loading process. In either way
     * the build-in defaults will be used.
     */
    private static final Convention EMPTY_PREFERENCES = new Convention(EMPTY_MAP);

    /** Our default project. */
    private static final Project DEFAULT_PROJECT = new Project("default",
                                                               "The Jalopy default project space.");

    /** The default project. */
    private static Project _project = DEFAULT_PROJECT;

    /** Base settings directory. */
    private static File _settingsDirectory;

    /** Bbackup directory. */
    private static File _backupDirectory;

    /** Class type repository directory. */
    private static File _repositoryDirectory;

    /** The local code convention file. */
    private static File _settingsFile;

    /** The active project settings directory. */
    private static File _projectSettingsDirectory;

    /** The active project history file. */
    private static File _historyFile;

    static
    {
        _settingsDirectory = new File(System.getProperty("user.home") +
                                      File.separator + ".jalopy");

        Project project = loadProject();
        _project = project;
        setDirectories(project);

        File settingsFile = null;
        InputStream in = null;

        try
        {
            settingsFile = getSettingsFile();

            // first load the system code convention file
            if (settingsFile.exists())
            {
                in = new FileInputStream(settingsFile);
                INSTANCE = readFromStream(in);
            }
            else
            {
                INSTANCE = EMPTY_PREFERENCES;
            }

            String location = INSTANCE.get(Keys.STYLE_LOCATION, "");

            // if the user specified a distributed location to load
            // code convention from, try to sync
            if (location.startsWith("http"))
            {
                try
                {
                    importSettings(new URL(location));

                    // update the location, so we keep synchronizing further on
                    INSTANCE.put(Keys.STYLE_LOCATION, location);
                    INSTANCE.flush();
                }
                catch (IOException ex)
                {
                    Object[] args ={ location };
                    Loggers.IO.l7dlog(Level.WARN, "PREF_COULD_NOT_CONNECT",
                                      args, null);
                }
            }
            else
            {
                sync(INSTANCE);
            }
        }
        catch (Throwable ex)
        {
            Object[] args ={ settingsFile };
            Loggers.IO.l7dlog(Level.WARN, "PREF_ERROR_LOADING", args, ex);

            // actually means the build-in defaults will be used
            INSTANCE = EMPTY_PREFERENCES;
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch (IOException ignored)
                {
                    ;
                }
            }
        }

        if (project.getName().equals(DEFAULT_PROJECT.getName()))
        {
            // update to our new
            if ((!_projectSettingsDirectory.exists()) ||
                new File(_settingsDirectory, "preferences.jal").exists())
            {
                if (IoHelper.ensureDirectoryExists(_projectSettingsDirectory))
                {
                    File[] files = _settingsDirectory.listFiles();

                    for (int i = 0; i < files.length; i++)
                    {
                        if (files[i].getName().endsWith("preferences.jal"))
                        {
                            try
                            {
                                Copy.file(files[i],
                                          new File(_projectSettingsDirectory,
                                                   FILENAME_PREFERENCES));
                                files[i].delete();
                            }
                            catch (IOException ex)
                            {
                                ;
                            }
                        }
                        else if (!files[i].getName().equals(FILENAME_PROJECT))
                        {
                            if (files[i].isDirectory() &&
                                (files[i].getName().equals(NAME_BACKUP) || files[i].getName()
                                                                                   .equals(NAME_REPOSITORY)))
                            {
                                IoHelper.delete(files[i], true);
                            }
                        }
                    }

                    try
                    {
                        IoHelper.serialize(project,
                                           new File(_projectSettingsDirectory,
                                                    FILENAME_PROJECT));
                    }
                    catch (IOException ex)
                    {
                        ;
                    }
                }
            }
        }
    }

    //~ Instance variables ····················································

    /** Holds the last snapshot. */
    private Map _snapshot; // Map of <Key>:<String>

    /** The map which holds the actual values. */
    private Map _values = EMPTY_MAP; // Map of <Key>:<String>

    //~ Constructors ··························································

    /**
     * Creates a new code convention object.
     *
     * @param values the actual code convention setttings.
     */
    private Convention(Map values)
    {
        if (!values.isEmpty())
        {
            Iterator keys = values.keySet().iterator();

            Object key = keys.next();

            if (key instanceof de.hunsicker.jalopy.prefs.Key)
            {
                Map t = new HashMap(values.size());
                for (Iterator i = values.entrySet().iterator();i.hasNext();)
                {
                    Map.Entry entry = (Map.Entry)i.next();
                    t.put(new Key(entry.getKey().toString()), entry.getValue());
                }
                values = t;
            }
        }

        _values = values;
    }

    //~ Methods ·······························································

    /**
     * Returns the current project's backup directory path.
     *
     * @return backup directory.
     *
     * @since 1.0b8
     */
    public static File getBackupDirectory()
    {
        return _backupDirectory;
    }


    /**
     * Returns the default project.
     *
     * @return the default project.
     *
     * @since 1.0b8
     */
    public static Project getDefaultProject()
    {
        return DEFAULT_PROJECT;
    }


    /**
     * Returns the history backing store file.
     *
     * @return history backing store file.
     */
    public static File getHistoryFile()
    {
        return _historyFile;
    }


    /**
     * Returns the sole instance of this object.
     *
     * @return the sole instance of this object.
     */
    public static Convention getInstance()
    {
        return INSTANCE;
    }


    /**
     * Returns the local code convention file.
     *
     * @return local code convention file.
     *
     * @since 1.0b8
     */
    public static File getSettingsFile()
    {
        return _settingsFile;
    }


    /**
     * Sets the currently active project.
     *
     * @param project the new active project.
     *
     * @return <code>true</code> if setting the project was successful.
     *
     * @since 1.0b8
     */
    public static boolean setProject(Project project)
    {
        synchronized (_lock)
        {
            try
            {
                INSTANCE.snapshot();

                File activeFile = getSettingsFile();

                // change all directories to point to the new project directory
                setDirectories(project);
                storeProject(project);

                File file = getSettingsFile();

                if (file.exists())
                {
                    importSettings(file);
                }
                else if (activeFile.exists())
                {
                    importSettings(activeFile);
                }

                _project = project;

                return true;
            }
            catch (IOException ex)
            {
                _project = DEFAULT_PROJECT;
                setDirectories(_project);
                INSTANCE.revert();

                return false;
            }
        }
    }


    /**
     * Returns the current project settings directory.
     *
     * @return settings directory.
     *
     * @since 1.0b8
     */
    public static File getProjectSettingsDirectory()
    {
        return _projectSettingsDirectory;
    }


    /**
     * Returns the current project's class repository directory.
     *
     * @return class repository directory.
     *
     * @since 1.0b8
     */
    public static File getRepositoryDirectory()
    {
        return _repositoryDirectory;
    }


    /**
     * Returns the base settings directory.
     *
     * @return settings directory.
     *
     * @since 1.0b8
     */
    public static File getSettingsDirectory()
    {
        return _settingsDirectory;
    }


    /**
     * Adds a new project. Adding a project means that the settings of the
     * currently active project will be duplicated to the new project
     * settings directory.
     *
     * @param project the project information.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    public static void addProject(Project project)
        throws IOException
    {
        synchronized (_lock)
        {
            File projectDirectory = new File(getSettingsDirectory(),
                                             project.getName());
            File activeProjectDirectory = new File(getSettingsDirectory(),
                                                   _project.getName());

            try
            {
                if (activeProjectDirectory.exists())
                {
                    File[] files = activeProjectDirectory.listFiles(new ExtensionFilter(EXTENSION_DAT));

                    // copy the settings files from the active project directory
                    // into the new one
                    for (int i = 0; i < files.length; i++)
                    {
                        if (files[i].isFile())
                        {
                            Copy.file(files[i],
                                      new File(projectDirectory,
                                               files[i].getName()));
                        }
                    }
                }

                if (IoHelper.ensureDirectoryExists(projectDirectory))
                {
                    IoHelper.serialize(project,
                                       new File(projectDirectory,
                                                FILENAME_PROJECT));
                }
            }
            catch (IOException ex)
            {
                IoHelper.delete(projectDirectory, true);
                throw ex;
            }
        }
    }


    /**
     * Imports the code convention from the specified input stream.
     *
     * @param in the input stream from which to read the code convention.
     * @param extension file extension indicating the format of the saved
     *        code convention.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalArgumentException if an invalid extension was specified.
     */
    public static void importSettings(InputStream in,
                                         String      extension)
        throws IOException
    {
        if (EXTENSION_DAT.equals(extension) || EXTENSION_JAL.equals(extension))
        {
            INSTANCE._values = (Map)IoHelper.deserialize(in);
            sync(INSTANCE);
        }
        else if (EXTENSION_XML.equals(extension))
        {
            Reader isr = null;

            try
            {
                String encoding = System.getProperty("file.encoding");
                isr = new InputStreamReader(new BufferedInputStream(in),
                                            encoding);

                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(isr);
                INSTANCE._values = new HashMap();
                convertXmlToMap(INSTANCE._values, document.getRootElement());
                sync(INSTANCE);
            }
            catch (JDOMException ex)
            {
                throw new IOException(ex.getMessage());
            }
            finally
            {
                if (isr != null)
                {
                    try
                    {
                        isr.close();
                    }
                    catch (IOException ignored)
                    {
                        ;
                    }
                }
            }
        }
        else
        {
            throw new IllegalArgumentException("invalid extension -- " +
                                               extension);
        }
    }


    /**
     * Imports the code convention from the specified url.
     *
     * @param url url to import the code convention from.
     *
     * @throws IOException an I/O error occured.
     * @throws ChainingRuntimeException DOCUMENT ME!
     */
    public static void importSettings(URL url)
        throws IOException
    {
        InputStream in = null;

        try
        {
            in = url.openStream();
            importSettings(in, getExtension(url));
        }
        catch (MalformedURLException ex)
        {
            throw new ChainingRuntimeException(
                                               "Could not load code convention from the given url -- " +
                                               url, ex);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ignored)
            {
                ;
            }
        }
    }


    /**
     * Imports the code convention from the given file.
     *
     * @param file code convention file.
     *
     * @throws IOException if an I/O error occured.
     */
    public static void importSettings(File file)
        throws IOException
    {
        InputStream in = null;

        try
        {
            if (!file.exists())
            {
                // if no explicit path was given
                if (file.getAbsolutePath().indexOf(File.separatorChar) < 0)
                {
                    file = new File(System.getProperty("user.dir") +
                                    File.separator + file);

                    if (file.exists()) // first search the current directory
                    {
                        in = new FileInputStream(file);
                    }
                    else // then the user's home directory
                    {
                        file = new File(System.getProperty("user.home") +
                                        File.separator + file);

                        if (file.exists())
                        {
                            in = new FileInputStream(file);
                        }
                        else // and finally the Jalopy .jar
                        {
                            in = Convention.class.getResourceAsStream(file.getAbsolutePath());

                            if (in == null)
                            {
                                throw new FileNotFoundException("file not found -- " +
                                                                file.getAbsolutePath());
                            }
                        }
                    }
                }
                else
                {
                    throw new FileNotFoundException(file.getAbsolutePath());
                }
            }
            else if (file.isFile())
            {
                in = new FileInputStream(file);
            }
            else
            {
                throw new IllegalArgumentException("no valid file -- " +
                                                   file.getAbsolutePath());
            }

            importSettings(in, getExtension(file));
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ignored)
            {
                ;
            }
        }
    }


    /**
     * Removes the given project.
     *
     * @param project a project.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    public static void removeProject(Project project)
        throws IOException
    {
        synchronized (_lock)
        {
            File projectDirectory = new File(getSettingsDirectory(),
                                             project.getName());
            IoHelper.delete(projectDirectory, true);
        }
    }


    /**
     * Returns the boolean value associated with the given key.
     *
     * <p>
     * This implementation invokes {@link
     * #get(Key,String) <tt>get(key,
     * null)</tt>}. If the return value is non-null, it is compared with
     * <tt>"true"</tt> using {@link String#equalsIgnoreCase(String)}. If the
     * comparison returns <tt>true</tt>, this invocation returns
     * <tt>true</tt>. Otherwise, the original return value is compared with
     * <tt>"false"</tt>, again using {@link String#equalsIgnoreCase(String)}.
     * If the comparison returns <tt>true</tt>, this invocation returns
     * <tt>false</tt>. Otherwise, this invocation returns <tt>def</tt>.
     * </p>
     *
     * @param key key whose associated value is to be returned as a boolean.
     * @param def the value to be returned in the event that this preference
     *        node has no value associated with <tt>key</tt> or the
     *        associated value cannot be interpreted as a boolean.
     *
     * @return the boolean value represented by the string associated with
     *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
     *         associated value does not exist or cannot be interpreted as a
     *         boolean.
     */
    public boolean getBoolean(Key     key,
                              boolean def)
    {
        boolean result = def;
        String value = get(key, null);

        if (value != null)
        {
            if (value.equalsIgnoreCase("true"))
            {
                result = true;
            }
            else if (value.equalsIgnoreCase("false"))
            {
                result = false;
            }
        }

        return result;
    }


    /**
     * Returns the int value represented by the string associated with the
     * specified key in this preference node. The string is converted to an
     * integer as by {@link Integer#parseInt(String)}. Returns the specified
     * default if there is no value associated with the key, the backing
     * store is inaccessible, or if {@link Integer#parseInt(String)} would
     * throw a {@link NumberFormatException} if the associated value were
     * passed. This method is intended for use in conjunction with {@link
     * #putInt}.
     *
     * @param key key whose associated value is to be returned as an int.
     * @param def the value to be returned in the event that this preference
     *        node has no value associated with <tt>key</tt> or the
     *        associated value cannot be interpreted as an int, or the
     *        backing store is inaccessible.
     *
     * @return the int value represented by the string associated with
     *         <tt>key</tt> in this preference node, or <tt>def</tt> if the
     *         associated value does not exist or cannot be interpreted as an
     *         int.
     *
     * @see #putInt(Key,int)
     * @see #get(Key,String)
     */
    public int getInt(Key key,
                      int def)
    {
        int result = def;

        try
        {
            String value = get(key, null);

            if (value != null)
            {
                result = Integer.parseInt(value);
            }
        }
        catch (NumberFormatException ex)
        {
            ;
        }

        return result;
    }


    /*public boolean equals(Object obj)
       {
           if (obj instanceof Convention)
           {
               return _values.equals((Convention)obj);
           }

           return false;
       }*/

    /**
     * Exports the code convention to the given file. The file extension
     * determines the format in which the code convention will be written.
     *
     * @param file file to export the code convention to.
     *
     * @throws IOException if writing to the specified output stream failed.
     */
    public void exportSettings(File file)
        throws IOException
    {
        exportSettings(new FileOutputStream(file), getExtension(file));
    }


    /**
     * Emits the code convention in a format indicated by the given extension. If
     * no extension is given, the default format will be used (the binary
     * <code>.jal</code> format).
     *
     * @param out the output stream on which to emit the code convention.
     * @param extension output format to use. Either {@link #EXTENSION_JAL} or
     *        {@link #EXTENSION_XML}.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalArgumentException if <em>extension</em> is no valid file
     *         extension.
     */
    public void exportSettings(OutputStream out,
                                  String       extension)
        throws IOException
    {
        _values.put(Keys.INTERNAL_VERSION, VERSION);

        if (extension == null)
        {
            extension = EXTENSION_JAL;
        }

        if (EXTENSION_DAT.equals(extension) || EXTENSION_JAL.equals(extension))
        {
            IoHelper.serialize(_values, new BufferedOutputStream(out));
        }
        else if (EXTENSION_XML.equals(extension))
        {
            try
            {
                String encoding = System.getProperty("file.encoding");
                XMLOutputter outputter = new XMLOutputter("    ", true,
                                                          encoding);
                outputter.output(convertMapToXml(_values),
                                 new BufferedOutputStream(out));
            }
            finally
            {
                out.close();
            }
        }
        else
        {
            throw new IllegalArgumentException("invalid file extension -- " +
                                               extension);
        }
    }


    /**
     * Flushes the code convention to the persistent store.
     *
     * @throws IOException if an I/O error occured.
     */
    public void flush()
        throws IOException
    {
        File directory = getProjectSettingsDirectory();

        if (!IoHelper.ensureDirectoryExists(directory))
        {
            throw new IOException("could not create settings directory -- " +
                                  directory);
        }

        _values.put(Keys.INTERNAL_VERSION, VERSION);

        // write the values to disk
        IoHelper.serialize(_values, getSettingsFile());

        // update the project file in the current project directory
        storeProject(_project);
    }


    /**
     * Returns the value associated with the given key.
     *
     * <p>
     * This implementation first checks to see if <tt>key</tt> is
     * <tt>null</tt> throwing a <tt>NullPointerException</tt> if this is the
     * case.
     * </p>
     *
     * @param key key whose associated value is to be returned.
     * @param def the value to be returned in the event that this preference
     *        node has no value associated with <tt>key</tt>.
     *
     * @return the value associated with <tt>key</tt>, or <tt>def</tt> if no
     *         value is associated with <tt>key</tt>.
     *
     * @throws NullPointerException if key is<tt>null</tt>.  (A <tt>null</tt>
     *         default <i>is</i> permitted.)
     */
    public String get(Key    key,
                      String def)
    {
        if (key == null)
        {
            throw new NullPointerException("null no valid key");
        }

        String result = null;

        try
        {
            result = (String)_values.get(key);
        }
        catch (Exception ignored)
        {
            ;
        }

        return ((result == null) ? def
                                 : result);
    }


    /**
     * Implements the <tt>put</tt> method as per the specification in {@link
     * Convention#put(Key,String)}.
     *
     * @param key key with which the specified value is to be associated.
     * @param value value to be associated with the specified key.
     *
     * @throws NullPointerException if key or value is<tt>null</tt>.
     */
    public void put(Key    key,
                    String value)
    {
        if ((key == null) || (value == null))
        {
            throw new NullPointerException(key + ", " + value);
        }

        _values.put(key, value);
    }


    /**
     * Implements the <tt>putBoolean</tt> method as per the specification in
     * {@link Convention#putBoolean(Key,boolean)}.
     *
     * <p>
     * This implementation translates <tt>value</tt> to a string with {@link
     * String#valueOf(boolean)} and invokes {@link #put(Key,String)} on the
     * result.
     * </p>
     *
     * @param key key with which the string form of value is to be associated.
     * @param value value whose string form is to be associated with key.
     */
    public void putBoolean(Key     key,
                           boolean value)
    {
        put(key, String.valueOf(value));
    }


    /**
     * Implements the <tt>putInt</tt> method as per the specification in
     * {@link Convention#putInt(Key,int)}.
     *
     * <p>
     * This implementation translates <tt>value</tt> to a string with {@link
     * Integer#toString(int)} and invokes {@link #put(Key,String)} on the
     * result.
     * </p>
     *
     * @param key key with which the string form of value is to be associated.
     * @param value value whose string form is to be associated with key.
     */
    public void putInt(Key key,
                       int value)
    {
        put(key, Integer.toString(value));
    }


    /**
     * Reverts the code convention to the state of the last snapshot. If no
     * snapshots exists, the call will be ignored.
     *
     * @see #snapshot
     * @since 1.0b8
     */
    public void revert()
    {
        synchronized (_lock)
        {
            if (_snapshot != null)
            {
                _values.clear();
                _values.putAll(_snapshot);
                _snapshot = null;
            }
        }
    }


    /**
     * Creates an internal snapshot of the current code convention.  You can use
     * {@link #revert} at any time to revert the code convention to the state of
     * the last snapshot.
     *
     * @see #revert
     * @since 1.0b8
     */
    public void snapshot()
    {
        synchronized (_lock)
        {
            _snapshot = new HashMap(_values);
        }
    }


    /**
     * Returns a string representation of this object.
     *
     * @return a string representation of this object.
     */
    public String toString()
    {
        return _values.toString();
    }


    /**
     * DOCUMENT ME!
     *
     * @param project DOCUMENT ME!
     */
    private static void setDirectories(Project project)
    {
        _projectSettingsDirectory = new File(_settingsDirectory,
                                             project.getName());
        _backupDirectory = new File(_projectSettingsDirectory, NAME_BACKUP);
        _repositoryDirectory = new File(_projectSettingsDirectory,
                                        NAME_REPOSITORY);
        _settingsFile = new File(_projectSettingsDirectory,
                                    FILENAME_PREFERENCES);
        _historyFile = new File(_projectSettingsDirectory, FILENAME_HISTORY);
    }


    /**
     * Returns the file extension of the given string, denoting a file path.
     *
     * @param location string denoting a file path.
     *
     * @return file extension of the given file.
     *
     * @throws IOException if the given location does not denote a valid
     *         code convention file.
     * @throws IllegalArgumentException if <em>location</em> does not have a
     *         valid extension (Either {@link #EXTENSION_JAL} or {@link
     *         #EXTENSION_XML}).
     */
    private static String getExtension(String location)
        throws IOException
    {
        int offset = location.lastIndexOf('.');

        if (offset > -1)
        {
            String extension = location.substring(offset);

            if ((extension == null) && (!EXTENSION_JAL.equals(extension)) &&
                EXTENSION_DAT.equals(extension) &&
                (!(EXTENSION_XML.equals(extension))))
            {
                throw new IOException("no valid location given -- " + location);
            }

            return extension;
        }

        throw new IllegalArgumentException("invalid file extension -- " +
                                           location);
    }


    /**
     * Returns the file extension of the file.
     *
     * @param file a file.
     *
     * @return file extension of the given file.
     *
     * @throws IOException if the given file does not denote a valid
     *         code convention file.
     */
    private static String getExtension(File file)
        throws IOException
    {
        return getExtension(file.getName());
    }


    /**
     * Returns the file extension of the url.
     *
     * @param url an url.
     *
     * @return file extension of the given url.
     *
     * @throws IOException if the given url does not denote a valid
     *         code convention file.
     */
    private static String getExtension(URL url)
        throws IOException
    {
        return getExtension(url.getFile());
    }


    /**
     * Returns the history policy for the given string.
     *
     * @param policy string indidcating the old integer-based history.
     *
     * @return History policy for the given string.
     *
     * @since 1.0b8
     */
    private static History.Policy getHistoryPolicy(String policy)
    {
        if ("1".equals(policy))
        {
            return History.Policy.COMMENT;
        }
        else if ("2".equals(policy))
        {
            return History.Policy.FILE;
        }
        else
        {
            return History.Policy.DISABLED;
        }
    }


    /**
     * Converts a JDOM tree to a Map represenation.
     *
     * @param map the map to hold the values.
     * @param element root element of the JDOM tree.
     */
    private static void convertXmlToMap(Map     map,
                                        Element element)
    {
        List children = element.getChildren();

        if (children.size() == 0)
        {
            StringBuffer path = new StringBuffer();
            String value = element.getText();

            while (true)
            {
                if (path.length() > 0)
                {
                    path.insert(0, '/');
                }

                path.insert(0, element.getName());
                element = element.getParent();

                if ((element == null) || element.getName().equals("jalopy"))
                {
                    break;
                }
            }

            map.put(new Key(new String(path)), value);

            return;
        }

        for (int i = 0, i_len = children.size(); i < i_len; i++)
        {
            Element childElement = (Element)children.get(i);
            convertXmlToMap(map, childElement);
        }
    }


    /**
     * Loads the active project from persistent storage.
     *
     * @return the currently active project.
     *
     * @since 1.0b8
     */
    private static Project loadProject()
    {
        try
        {
            File file = new File(getSettingsDirectory(), FILENAME_PROJECT);

            if (file.exists())
            {
                Project project = (Project)IoHelper.deserialize(file);

                return project;
            }
            else
            {
                return DEFAULT_PROJECT;
            }
        }
        catch (Throwable ex)
        {
            return DEFAULT_PROJECT;
        }
    }


    /**
     * Reads the code convention from the given stream.
     *
     * @param in stream to read the code convention from.
     *
     * @return the code convention just read.
     *
     * @throws IOException if an I/O error occured.
     * @throws ClassNotFoundException if a class could not be found.
     */
    private static Convention readFromStream(InputStream in)
        throws IOException,
               ClassNotFoundException
    {
        return new Convention((Map)IoHelper.deserialize(new BufferedInputStream(in)));
    }


    /**
     * Renames the given code convention key.
     *
     * @param oldName the old key name.
     * @param newKey the new key name.
     *
     * @since 1.0b6
     */
    private static void renameKey(String oldName,
                                  Key    newKey)
    {
        Object value = INSTANCE._values.remove(oldName);
        INSTANCE._values.put(newKey, value);
    }


    /**
     * Stores the given project (it will be serialized to disk).
     *
     * @param project project to made persistent.
     *
     * @throws IOException if an I/O error occured.
     *
     * @since 1.0b8
     */
    private static void storeProject(Project project)
        throws IOException
    {
        File file = new File(getProjectSettingsDirectory(), FILENAME_PROJECT);
        IoHelper.serialize(project, file);

        if (!project.getName().equals(DEFAULT_PROJECT.getName()))
        {
            // don't forget to keep track of the active project
            IoHelper.serialize(project,
                               new File(getSettingsDirectory(),
                                        FILENAME_PROJECT));
        }
        else
        {
            File f = new File(getSettingsDirectory(), FILENAME_PROJECT);
            f.delete();
        }
    }


    /**
     * Updates the given code convention to be compatible with the current
     * (latest) version.
     *
     * @param settings code convention settings to synchronize.
     * @param version version number of the given code convention settings.
     *
     * @since 1.0b8
     */
    private static void sync(Convention settings,
                             int         version)
    {
        switch (version)
        {
            case -1 : // before 1.0b6
                sync0To1(settings);

                break;

            case 1 : // 1.0b6
                sync1To2(settings);
                sync(settings, 2);

                break;

            case 2 : // 1.0b7
                sync2To3(settings);
                sync(settings, 3);

                break;

            case 3 : // 1.0b8
                sync3To4(settings);
                sync(settings, 4);

                break;

            case 4: // 1.0b9
                sync4To5(settings);
        }
    }


    /**
     * Updates the given code convention settings to the the current code convention format.
     *
     * @param settings code convention settings to synchronize against the latest version.
     *
     * @since 1.0b6
     */
    private static void sync(Convention settings)
    {
        int version = settings.getInt(Keys.INTERNAL_VERSION, -1);
        sync(settings, version);
        INSTANCE.put(Keys.INTERNAL_VERSION, VERSION);
    }


    /**
     * Changes the code convention settings format from an old, unsupported format (prior
     * to 1.0b6) to the new format (all values will be lost).
     *
     * @param settings code convention settings to update.
     *
     * @since 1.0b6
     */
    private static void sync0To1(Convention settings)
    {
        INSTANCE = EMPTY_PREFERENCES;
        INSTANCE._values = EMPTY_MAP;
    }


    /**
     * Changes the code convention settings format from version 1 to version 2.
     *
     * @param settings code convention settings to update.
     *
     * @since 1.0b7
     */
    private static void sync1To2(Convention settings)
    {
        renameKey("printer/alignment/throwsTypes",
                  Keys.LINE_WRAP_AFTER_TYPES_THROWS);
        renameKey("printer/alignment/implementsTypes",
                  Keys.LINE_WRAP_AFTER_TYPES_IMPLEMENTS);
        renameKey("printer/alignment/extendsTypes",
                  Keys.LINE_WRAP_AFTER_TYPES_EXTENDS);

        Object collapse = INSTANCE._values.get("transform/import/collapse");

        if (collapse != null)
        {
            INSTANCE._values.remove("transform/import/collapse");

            if ("true".equals(collapse))
            {
                INSTANCE.putInt(Keys.IMPORT_POLICY, 2);
            }
        }

        Object expand = INSTANCE._values.get("transform/import/expand");

        if (expand != null)
        {
            INSTANCE._values.remove("transform/import/expand");

            if ("true".equals(expand))
            {
                INSTANCE.putInt(Keys.IMPORT_POLICY, 1);
            }
        }
    }


    /**
     * Changes the code convention settings format from version 2 to version 3.
     *
     * @param settings code convention settings to update.
     *
     * @since 1.0b8
     */
    private static void sync2To3(Convention settings)
    {
        int historyPolicy = INSTANCE.getInt(Keys.HISTORY_POLICY, 0);

        switch (historyPolicy)
        {
            case -1 :
                INSTANCE.putInt(Keys.HISTORY_POLICY, 0);

                break;
        }
    }

    /**
     * Changes the code convention settings format from version 4 to version 5.
     *
     * @param settings code convention settings to update.
     *
     * @since 1.0b9
     */
    private static void sync4To5(Convention settings)
    {
        String header = settings.get(Keys.HEADER_TEXT, "").trim();
        String[] lines = StringHelper.split(header, "\n");

        StringBuffer buf = new StringBuffer(header.length());

        for (int i = 0; i < lines.length; i++)
        {
            buf.append(StringHelper.trimTrailing(lines[i]));
            buf.append('|');
        }

        if (lines.length > 0)
        {
            buf.deleteCharAt(buf.length() - 1);
        }

        settings.put(Keys.HEADER_TEXT, buf.toString());


        String footer = settings.get(Keys.FOOTER_TEXT, "").trim();
        lines = StringHelper.split(footer, "\n");

        buf = new StringBuffer(footer.length());

        for (int i = 0; i < lines.length; i++)
        {
            buf.append(StringHelper.trimTrailing(lines[i]));
            buf.append('|');
        }

        if (lines.length > 0)
        {
            buf.deleteCharAt(buf.length() - 1);
        }

        settings.put(Keys.FOOTER_TEXT, buf.toString());
    }


    /**
     * Changes the code convention settings format from version 3 to version 4.
     *
     * @param settings code convention settings to update.
     *
     * @since 1.0b8
     */
    private static void sync3To4(Convention settings)
    {
        Map values = new HashMap(settings._values.size());

        // make sure we don't use String based keys anymore
        for (Iterator i = settings._values.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry)i.next();

            if (entry.getKey() instanceof String)
            {
                Key key = new Key((String)entry.getKey());
                values.put(key, entry.getValue());
            }
            else
            {
                values.put(entry.getKey(), entry.getValue());
            }
        }

        INSTANCE._values = values;
        settings._values.remove(new Key("printer/comments/javadoc/templateDescription"));
        settings._values.remove(new Key("printer/comments/javadoc/templateParam"));
        settings._values.remove(new Key("printer/comments/javadoc/templateReturn"));
        settings._values.remove(new Key("printer/comments/javadoc/templateThrows"));
        settings._values.remove(new Key("internal/urls/import"));
        settings._values.remove(new Key("internal/urls/backup"));
        settings._values.remove(new Key("internal/urls/export"));
        settings._values.remove(new Key("internal/styleLastPanel"));
        settings._values.remove(new Key("internal/styleLastPanelClass"));
        settings._values.remove(new Key("internal/styleLastPanelTitle"));
        settings._values.remove(new Key("printer/comments/removeSeparator"));
        settings._values.remove(new Key("messages/showIoMsg"));
        settings._values.remove(new Key("messages/showParserMsg"));
        settings._values.remove(new Key("messages/showParserJavadocMsg"));
        settings._values.remove(new Key("messages/showTransformMsg"));
        settings._values.remove(new Key("messages/showPrinterJavadocMsg"));
        settings._values.remove(new Key("messages/showPrinterMsg"));

        String sortOrder = INSTANCE.get(Keys.SORT_ORDER,
                                        DeclarationType.getOrder());
        Perl5Util regexp = new Perl5Util();
        sortOrder = regexp.substitute("s/Class/Classes/", sortOrder);
        sortOrder = regexp.substitute("s/Interface/Interfaces/", sortOrder);
        sortOrder = regexp.substitute("s/Method/Methods/", sortOrder);
        sortOrder = regexp.substitute("s/Constructor/Constructors/", sortOrder);
        sortOrder = regexp.substitute("s/Initializer/Instance Initializers/",
                                      sortOrder);
        sortOrder = regexp.substitute("s/Variable/Instance Variables/",
                                      sortOrder);
        sortOrder = "Static Variables/Initializers," + sortOrder;
        INSTANCE.put(Keys.SORT_ORDER, sortOrder);
        INSTANCE.put(Keys.HISTORY_POLICY,
                     getHistoryPolicy(INSTANCE.get(Keys.HISTORY_POLICY, "0"))
                         .toString());

        int importPolicy = INSTANCE.getInt(Keys.IMPORT_POLICY, 0);

        switch (importPolicy)
        {
            case 1 : // EXPAND
                INSTANCE.put(Keys.IMPORT_POLICY, ImportPolicy.EXPAND.toString());

                break;

            case 2 : // COLLAPPSE
                INSTANCE.put(Keys.IMPORT_POLICY,
                             ImportPolicy.COLLAPSE.toString());

                break;

            case 0 : // DISABLED

            // fall through
            default :
                INSTANCE.put(Keys.IMPORT_POLICY,
                             ImportPolicy.DISABLED.toString());

                break;
        }

        String backupDirectory = INSTANCE.get(Keys.BACKUP_DIRECTORY, "").trim();

        if (backupDirectory.endsWith(".jalopy/bak") ||
            backupDirectory.endsWith(".jalopy\\bak"))
        {
            INSTANCE.put(Keys.BACKUP_DIRECTORY, NAME_BACKUP);
        }
    }


    /**
     * Converts the given map into an XML representation.
     *
     * @param map map to convert.
     *
     * @return root element of the resulting XML document.
     */
    private Element convertMapToXml(Map map)
    {
        Element root = new Element("jalopy");

        for (Iterator it = map.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry entry = (Map.Entry)it.next();
            Key key = (Key)entry.getKey();
            Object value = entry.getValue();
            List pathList = splitPath(key.toString());
            Element go = root;

            for (int i = 0, i_len = pathList.size(); i < i_len; i++)
            {
                String elName = (String)pathList.get(i);
                Element child = go.getChild(elName);

                if (child == null)
                {
                    child = new Element(elName);
                    go.addContent(child);
                }

                go = child;
            }

            go.setText(value.toString());
        }

        return root;
    }


    /**
     * Splits the given XPath fragment into several parts.
     *
     * @param strXPath XPath fragment (e.g printer/indentation/general)
     *
     * @return list with splitted parts of the given path.
     */
    private List splitPath(String strXPath)
    {
        strXPath = "/" + strXPath;

        List result = new ArrayList();

        for (int i = 0, i_len = strXPath.length(); i < (i_len - 1); i++)
        {
            char ch = strXPath.charAt(i);

            if (ch == '/')
            {
                StringBuffer sb = new StringBuffer();

                for (int j = i + 1; j < i_len; j++)
                {
                    char varCh = strXPath.charAt(j);

                    if (varCh != '/')
                    {
                        sb.append(varCh);

                        continue;
                    }

                    break;
                }

                if (sb.length() == 0)
                {
                    continue;
                }

                result.add(new String(sb));
            }
        }

        return result;
    }
}
