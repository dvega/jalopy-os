/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.language.ClassRepository;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.storage.Loggers;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;


//import org.apache.tools.ant.types.selectors.FilenameSelector;

/**
 * The Jalopy Ant Plug-in. Formats Java source files according to some user configurable
 * rules. For more information about the Jalopy Java Source Code Formatter visit the
 * official homepage: <a href="http://jalopy.sf.net/">http://jalopy.sf.net/</a>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class AntPlugin
    extends Task
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The .java file extension. */
    private static final String EXT_JAVA = ".java" /* NOI18N */;
    private static final ResourceBundle BUNDLE =
        ResourceBundle.getBundle(
            "de.hunsicker.jalopy.plugin.ant.Bundle" /* NOI18N */,
            Convention.getInstance().getLocale());

    //~ Instance variables ---------------------------------------------------------------

    /** Destination directory. */
    private File _destDir;

    /** The file to format (if we're processing only one single file). */
    private File _file;

    /** The file format to use. */
    private FileFormat _fileFormat = FileFormat.AUTO;

    /** The filesets to format (if we're processing a fileset). */
    private List _filesets; // List of <FileSet>

    /** The history policy to apply. */
    private History.Method _historyMethod;

    /** Helper object to synchronize processes. */
    private final Object _lock = new Object();

    /** The level to control logging output. */
    private Level _loglevel;

    /** The classpath to init the ClassRepository with. */
    private Path _classpath;

    /** The history policy to apply. */
    private History.Policy _history;

    /** File path of the convention to use. */
    private String _convention;

    /** Encoding to use. */
    private String _encoding; // null means system default encoding

    /** Should a backup of every file be kept? */
    private boolean _backup;

    /** Indicates if the run should fail on the first error. */
    private boolean _failOnError = true;

    /** Should formatting be forced for files that are up to date? */
    private boolean _force;

    /** Should a new JVM be forked to perform formatting? */
    private boolean _fork;

    /** Did the user specified the &quot;backup&quot; attribute? */
    private boolean _isBackup = false;

    /** Did the user specified the &quot;force&quot; attribute? */
    private boolean _isForce;

    /** Did the user specified the &quot;threads&quot; attribute? */
    private boolean _isThreads;

    /** Show Javadoc related messages. */
    private boolean _javadoc = true;

    /** Number of processing threads. */
    private int _threads = 1;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new AntPlugin object.
     */
    public AntPlugin()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Sets whether a backup of an original file should be kept.
     *
     * @param backup if <code>true</code> a backup of an original file will be kept.
     */
    public void setBackup(boolean backup)
    {
        _backup = backup;
        _isBackup = true;
    }


    /**
     * Set the reference to the project classpath.
     *
     * @param reference project classpath reference.
     *
     * @since 0.5.3
     */
    public void setClasspathRef(Reference reference)
    {
        _classpath = new Path(this.project);
        _classpath.setRefid(reference);
    }


    /**
     * Sets the location of the code convention to use. This may either be an absolute
     * path to both a local file or distributed url or a path relative to the base
     * directory of the project.
     *
     * @param location absolute or relative path to load style convention from.
     *
     * @since 0.5.5
     */
    public void setConvention(String location)
    {
        File file = new File(location);

        // if the given path does not exist, the user may have specified
        // a path relative to the basedir
        if (!file.exists())
        {
            File basedir = getProject().getBaseDir();
            _convention = basedir.getAbsolutePath() + File.separator + location;
        }

        _convention = location;
    }


    /**
     * Sets the destination directory into which the Java source files should be
     * formatted.
     *
     * @param destDir destination directory.
     *
     * @see de.hunsicker.jalopy.Jalopy#setDestination
     */
    public void setDestdir(File destDir)
    {
        _destDir = destDir;
    }


    /**
     * Sets the encoding to use.
     *
     * @param encoding encoding to use.
     *
     * @see de.hunsicker.jalopy.Jalopy#setEncoding
     */
    public void setEncoding(String encoding)
    {
        _encoding = encoding;
    }


    /**
     * Sets whether an error will immediately cancel the run. Default is <em>false</em>.
     *
     * @param fail if <code>true</code> an error will lead to an immediate cancelation of
     *        the run.
     */
    public void setFailOnError(boolean fail)
    {
        _failOnError = fail;
    }


    /**
     * Sets a single source file to format.
     *
     * @param file the file to format.
     *
     * @throws IllegalArgumentException if the file does not exist.
     *
     * @see de.hunsicker.jalopy.Jalopy#setInput(File)
     */
    public void setFile(File file)
    {
        if (!file.exists())
        {
            Object[] args = { file };
            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("FILE_NOT_EXIST" /* NOI18N */), args));
        }

        _file = file;
    }


    /**
     * Sets the file format to use. Default is <em>AUTO</em>.
     *
     * @param fileFormat format to use.
     *
     * @throws IllegalArgumentException if an invalid file format was given
     *
     * @see de.hunsicker.jalopy.Jalopy#setFileFormat(String)
     */
    public void setFileFormat(String fileFormat)
    {
        String format = fileFormat.trim().toLowerCase();

        if (
            format.equals("dos" /* NOI18N */)
            || format.equals(FileFormat.DOS.getLineSeparator()))
        {
            _fileFormat = FileFormat.DOS;
        }
        else if (
            format.equals("default" /* NOI18N */)
            || format.equals(FileFormat.DEFAULT.toString()))
        {
            _fileFormat = FileFormat.DEFAULT;
        }
        else if (
            format.equals("unix" /* NOI18N */)
            || format.equals(FileFormat.UNIX.getLineSeparator()))
        {
            _fileFormat = FileFormat.UNIX;
        }
        else if (
            format.equals("mac" /* NOI18N */)
            || format.equals(FileFormat.MAC.getLineSeparator()))
        {
            _fileFormat = FileFormat.MAC;
        }
        else if (
            format.equals("auto" /* NOI18N */)
            || format.equals(FileFormat.AUTO.toString()))
        {
            _fileFormat = FileFormat.AUTO;
        }
        else
        {
            Object[] args = { fileFormat };
            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("INVALID_FILE_FORMAT" /* NOI18N */), args));
        }
    }


    /**
     * Controls whether all files should always be formatted.
     *
     * @param force if <code>true</code> all files are always formatted.
     *
     * @see de.hunsicker.jalopy.Jalopy#setForce
     */
    public void setForce(boolean force)
    {
        _force = force;
        _isForce = true;
    }


    /**
     * Sets the fork attribute.
     *
     * @param fork if <code>true</code> the task will be executed in a new process.
     *
     * @since 0.5.3
     */
    public void setFork(boolean fork)
    {
        _fork = fork;
    }


    /**
     * Sets the history policy to use.
     *
     * @param policy Either <ul><li><code>COMMENT</code> or</li> <li><code>FILE</code>
     *        or</li> <li><code>NONE</code></li> </ul>
     *
     * @throws IllegalArgumentException if an invalid history policy is specified.
     */
    public void setHistory(String policy)
    {
        policy = policy.trim().toLowerCase();

        if (policy.equals("comment" /* NOI18N */))
        {
            _history = History.Policy.COMMENT;
        }
        else if (policy.equals("file" /* NOI18N */))
        {
            _history = History.Policy.FILE;
        }
        else if (policy.equals("none" /* NOI18N */))
        {
            _history = History.Policy.DISABLED;
        }
        else
        {
            Object[] args = { policy };
            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("INVALID_HISTORY_POLICY" /* NOI18N */), args));
        }
    }


    /**
     * Sets the history method to use.
     *
     * @param method Either<ul><li><code>timestamp</code> or</li> <li><code>crc32</code>
     *        or</li> <li><code>adler32</code></li> </ul>
     *
     * @throws IllegalArgumentException if an invalid history method is specified.
     *
     * @since 0.5.5
     */
    public void setHistoryMethod(String method)
    {
        method = method.trim().toLowerCase();

        if ("timestamp" /* NOI18N */.equals(method))
        {
            _historyMethod = History.Method.TIMESTAMP;
        }
        else if ("crc32" /* NOI18N */.equals(method))
        {
            _historyMethod = History.Method.CRC32;
        }
        else if ("adler32" /* NOI18N */.equals(method))
        {
            _historyMethod = History.Method.ADLER32;
        }
        else
        {
            Object[] args = { method };

            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("INVALID_HISTORY_METHOD" /* NOI18N */), args));
        }
    }


    /**
     * Sets whether Javadoc related messages should be displayed. Default is
     * <em>true</em>.
     * 
     * <p>
     * Note that setting this switch to <code>false</code> means that <strong>no</strong>
     * Javadoc related messages will be displayed no matter what ever happens! Even if
     * formatting failed due to Javadoc parsing errors, there will be no output
     * indicating this fact.
     * </p>
     *
     * @param javadoc if <code>true</code> Javadoc messages will be displayed.
     */
    public void setJavadoc(boolean javadoc)
    {
        _javadoc = javadoc;
    }


    /**
     * Sets the level to control logging output. The valid levels are
     * 
     * <ul>
     * <li>
     * ERROR
     * </li>
     * <li>
     * WARN
     * </li>
     * <li>
     * INFO
     * </li>
     * <li>
     * DEBUG
     * </li>
     * </ul>
     * 
     * being <code>ERROR</code> the highest level and <code>DEBUG</code> the lowest
     * level.
     * 
     * <p>
     * Enabling logging at a given level also enables logging at all higher levels.
     * </p>
     *
     * @param level the logging level to use.
     *
     * @throws IllegalArgumentException if an invalid logging level has been specified.
     */
    public void setLoglevel(String level)
    {
        level = level.trim().toUpperCase();

        if (level.equals("INFO" /* NOI18N */))
        {
            _loglevel = Level.INFO;
        }
        else if (level.equals("DEBUG" /* NOI18N */))
        {
            _loglevel = Level.DEBUG;
        }
        else if (level.equals("WARN" /* NOI18N */))
        {
            _loglevel = Level.WARN;
        }
        else if (level.equals("ERROR" /* NOI18N */))
        {
            _loglevel = Level.ERROR;
        }
        else
        {
            Object[] args = { level };

            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("INVALID_LOGLEVEL" /* NOI18N */), args));
        }
    }


    /**
     * Sets the location of the code convention to use. This may either be an absolute
     * path to both a local file or distributed url or a path relative to the base
     * directory of the project.
     *
     * @param location absolute or relative path to load style convention from.
     *
     * @deprecated
     */
    public void setStyle(String location)
    {
        setConvention(location);
    }


    /**
     * Sets the number of processing threads to use.
     *
     * @param threads the thread count to use. A number between 1 and 8.
     *
     * @throws IllegalArgumentException if <code>threads &lt; 1</code> or <code>threads
     *         &gt; 8</code>
     */
    public void setThreads(String threads)
    {
        String number = threads.trim();

        try
        {
            _threads = Integer.parseInt(number);

            if ((_threads < 1) || (_threads > 8))
            {
                Object[] args = { threads };

                throw new IllegalArgumentException(
                    MessageFormat.format(
                        BUNDLE.getString("INVALID_TREAD_COUNT" /* NOI18N */), args));
            }
        }
        catch (NumberFormatException ex)
        {
            Object[] args = { threads };

            throw new IllegalArgumentException(
                MessageFormat.format(
                    BUNDLE.getString("INVALID_TREAD_COUNT" /* NOI18N */), args));
        }

        _isThreads = true;
    }


    /**
     * Adds a set of files (nested fileset attribute).
     *
     * @param set fileset to add.
     */
    public void addFileset(FileSet set)
    {
        _filesets.add(set);
    }


    /**
     * Executes the task.
     *
     * @throws BuildException if someting goes wrong with the build.
     */
    public void execute()
            throws BuildException
    {
        if (_fork)
        {
            if (
                System.getProperty(
                    "de.hunsicker.jalopy.plugin.ant.forked" /* NOI18N */,
                    "false" /* NOI18N */).equals("false" /* NOI18N */))
            {
                try
                {
                    CommandlineJava cmd = new CommandlineJava();
                    cmd.createArgument().setValue(
                        "-Dde.hunsicker.jalopy.plugin.ant.forked=true" /* NOI18N */);
                    cmd.createArgument().setValue("-classpath" /* NOI18N */);
                    cmd.createArgument().setValue(
                        System.getProperty("java.class.path" /* NOI18N */));
                    cmd.createArgument().setValue(
                        "org.apache.tools.ant.Main" /* NOI18N */);
                    cmd.createArgument().setValue("-logger" /* NOI18N */);
                    cmd.createArgument().setValue(
                        "org.apache.tools.ant.NoBannerLogger" /* NOI18N */);
                    cmd.createArgument().setValue("-f" /* NOI18N */);
                    cmd.createArgument().setValue(
                        this.project.getProperty("ant.file" /* NOI18N */));
                    cmd.createArgument().setValue(getOwningTarget().getName());

                    Execute exe = new Execute();
                    exe.setAntRun(this.project);
                    exe.setWorkingDirectory(this.project.getBaseDir());
                    exe.setCommandline(cmd.getCommandline());
                    exe.execute();

                    if (exe.getExitValue() != 0)
                    {
                        throw new BuildException(
                            BUNDLE.getString("RUN_FAILED" /* NOI18N */));
                    }
                }
                catch (Throwable ex)
                {
                    throw new BuildException(ex);
                }

                return;
            }
        }

        if (_file == null)
        {
            if (_filesets.size() == 0)
            {
                throw new BuildException(BUNDLE.getString("MISSING_SOURCE" /* NOI18N */));
            }
        }
        else
        {
            if (_file.exists())
            {
                if (_file.isDirectory())
                {
                    throw new BuildException(
                        BUNDLE.getString("USE_FILESET_FOR_DIRECTORIES" /* NOI18N */));
                }
            }
            else
            {
                Object[] args = { _file };

                throw new BuildException(
                    MessageFormat.format(
                        BUNDLE.getString("FILE_NOT_EXIST" /* NOI18N */), args));
            }
        }

        log("Jalopy Java Source Code Formatter " /* NOI18N */ + Jalopy.getVersion());

        // init the log4j logging facility
        AntAppender appender = new AntAppender();

        if (_loglevel != null)
        {
            initLoggers(appender, _loglevel);
        }
        else
        {
            Loggers.initialize(appender);
        }

        if (_classpath != null)
        {
            loadRepository(_classpath.list());
        }

        if (!_isThreads)
        {
            _threads =
                Convention.getInstance().getInt(
                    ConventionKeys.THREAD_COUNT, ConventionDefaults.THREAD_COUNT);
        }

        if (_threads == 1)
        {
            formatSingleThreaded();
        }
        else
        {
            formatMultiThreaded();
        }
    }


    /**
     * Initializes the task.
     *
     * @throws BuildException if the initialization failed.
     */
    public void init()
            throws BuildException
    {
        _filesets = new ArrayList(4);
    }


    /**
     * Determines whether either one of the given threads is still running.
     *
     * @param threads list with the worker threads.
     *
     * @return <code>true</code> if either one of the given threads is still running.
     */
    private boolean isRunning(List threads)
    {
        for (int i = 0, size = threads.size(); i < size; i++)
        {
            FormatThread thread = (FormatThread) threads.get(i);

            if (thread.running)
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Creates and initializes a new Jalopy instance.
     *
     * @return a new Jalopy instance.
     *
     * @throws BuildException if setting the convention failed.
     *
     * @since 0.5.3
     */
    private Jalopy createJalopy()
    {
        Jalopy jalopy = new Jalopy();

        if (_convention != null)
        {
            try
            {
                jalopy.setConvention(_convention);
            }
            catch (IOException ex)
            {
                throw new BuildException(ex);
            }
        }

        Convention settings = Convention.getInstance();

        try
        {
            jalopy.setEncoding(_encoding);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new BuildException(ex);
        }

        jalopy.setFileFormat(_fileFormat);
        jalopy.setInspect(
            settings.getBoolean(ConventionKeys.INSPECTOR, ConventionDefaults.INSPECTOR));

        if (_destDir != null)
        {
            jalopy.setDestination(_destDir);
        }

        if (_history != null)
        {
            jalopy.setHistoryPolicy(_history);
        }
        else
        {
            History.Policy historyPolicy =
                History.Policy.valueOf(
                    settings.get(
                        ConventionKeys.HISTORY_POLICY, ConventionDefaults.HISTORY_POLICY));
            jalopy.setHistoryPolicy(historyPolicy);
        }

        if (_historyMethod != null)
        {
            jalopy.setHistoryMethod(_historyMethod);
        }
        else
        {
            History.Method historyMethod =
                History.Method.valueOf(
                    settings.get(
                        ConventionKeys.HISTORY_METHOD, ConventionDefaults.HISTORY_METHOD));
            jalopy.setHistoryMethod(historyMethod);
        }

        if (_isBackup)
        {
            jalopy.setBackup(_backup);
        }
        else
        {
            jalopy.setBackup(
                settings.getInt(
                    ConventionKeys.BACKUP_LEVEL, ConventionDefaults.BACKUP_LEVEL) > 0);
        }

        if (_isForce)
        {
            jalopy.setForce(_force);
        }
        else
        {
            jalopy.setForce(
                settings.getBoolean(
                    ConventionKeys.FORCE_FORMATTING, ConventionDefaults.FORCE_FORMATTING));
        }

        return jalopy;
    }


    /**
     * Uses multiple threads to perform the actual work.
     *
     * @throws BuildException if something goes wrong.
     */
    private void formatMultiThreaded()
    {
        List files = new ArrayList(200);

        /**
         * @todo if we ever start relying on Ant 1.5 we can add a selector and won't have
         *       to add a Java include pattern anymore
         */

        // FilenameSelector selector = new FilenameSelector();
        // selector.setName("**/*.java");
        // build the list with all files that are to be processed
        for (int i = 0, size = _filesets.size(); i < size; i++)
        {
            FileSet fs = (FileSet) _filesets.get(i);

            //fs.addFilename(selector);
            DirectoryScanner scanner = fs.getDirectoryScanner(this.project);
            File fromDir = fs.getDir(this.project);
            String[] srcFiles = scanner.getIncludedFiles();

            for (int j = 0; j < srcFiles.length; j++)
            {
                files.add(new File(fromDir, srcFiles[j]));
            }
        }

        if ((_file != null) && _file.getName().endsWith(EXT_JAVA))
        {
            files.add(_file);
        }

        logStart(files.size());

        int numThreads = _threads;

        if (files.size() < _threads)
        {
            numThreads = files.size();
        }

        List threads = new ArrayList(numThreads);

        Jalopy jalopy = null;

        // start the worker threads
        for (int j = 0; j < numThreads; j++)
        {
            FormatThread thread = new FormatThread(files);
            threads.add(thread);
            thread.start();

            jalopy = thread.jalopy;
        }

        try
        {
            synchronized (_lock)
            {
                // wait until all worker threads have finished
                while (isRunning(threads))
                {
                    _lock.wait();
                }
            }

            boolean error = false;
            int count = 0;

            // check for errors
            for (int i = 0, size = threads.size(); i < size; i++)
            {
                FormatThread thread = (FormatThread) threads.get(i);
                count += thread.count;

                if (!error)
                {
                    error = thread.error;
                }
            }

            logEnd(count);

            if (jalopy != null)
            {
                jalopy.cleanupBackupDirectory();
            }

            if (error)
            {
                throw new BuildException(BUNDLE.getString("RUN_FAILED" /* NOI18N */));
            }
        }
        catch (Throwable ex)
        {
            throw new BuildException(ex);
        }
    }


    /**
     * Performs the actual work in the current execution thread.
     *
     * @throws BuildException if something goes wrong.
     */
    private void formatSingleThreaded()
    {
        Jalopy jalopy = createJalopy();

        boolean error = false;

        // format single file
        if ((_file != null) && _file.getName().endsWith(EXT_JAVA))
        {
            try
            {
                jalopy.setInput(_file);

                try
                {
                    jalopy.setOutput(_file);
                }
                catch (IllegalArgumentException ex)
                {
                    error = true;

                    Object[] args = { _file, _destDir };

                    Loggers.IO.l7dlog(
                        Level.ERROR, "INVALID_OUTPUT_TARGET" /* NOI18N */, args, null);

                    if (_failOnError)
                    {
                        throw new BuildException(
                            BUNDLE.getString("INVALID_OUTPUT_TARGET" /* NOI18N */));
                    }
                }

                jalopy.format();

                if (jalopy.getState() == Jalopy.State.ERROR)
                {
                    error = true;

                    if (_failOnError)
                    {
                        throw new BuildException(
                            BUNDLE.getString("UNKNOWN_ERROR" /* NOI18N */));
                    }
                }
            }
            catch (FileNotFoundException ex)
            {
                // should never happen as {@link execute()} verifies the
                // existence
                throw new BuildException(ex);
            }
        }

        try
        {
            /**
             * @todo if we ever start relying on Ant 1.5 we can add a selector and won't
             *       have to add a Java include pattern anymore
             */

            // FilenameSelector selector = new FilenameSelector();
            // selector.setName("**/*.java");
            // format filesets
            for (int i = 0, size = _filesets.size(); i < size; i++)
            {
                FileSet fs = (FileSet) _filesets.get(i);

                //fs.addFilename(selector);
                DirectoryScanner scanner = fs.getDirectoryScanner(this.project);
                File fromDir = fs.getDir(this.project);
                String[] srcFiles = scanner.getIncludedFiles();

                logStart(srcFiles.length);

                int count = 0;

                for (int j = 0; j < srcFiles.length; j++)
                {
                    File file = new File(fromDir, srcFiles[j]);
                    jalopy.setInput(file);

                    try
                    {
                        jalopy.setOutput(file);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        error = true;

                        Object[] args = { file, _destDir };

                        Loggers.IO.l7dlog(
                            Level.ERROR, "INVALID_OUTPUT_TARGET" /* NOI18N */, args, null);

                        if (_failOnError)
                        {
                            throw new BuildException(
                                BUNDLE.getString("INVALID_OUTPUT_TARGET" /* NOI18N */));
                        }
                    }

                    boolean success = jalopy.format();

                    if (jalopy.getState() == Jalopy.State.ERROR)
                    {
                        error = true;

                        if (_failOnError)
                        {
                            throw new BuildException(
                                BUNDLE.getString("UNKNOWN_ERROR" /* NOI18N */));
                        }
                    }
                    else if (success)
                    {
                        count++;
                    }
                }

                logEnd(count);
            }

            if (error)
            {
                throw new BuildException(BUNDLE.getString("RUN_FAILED" /* NOI18N */));
            }

            jalopy.cleanupBackupDirectory();
        }
        catch (FileNotFoundException ex)
        {
            // should never happen as the DirectoryScanner reports only
            // existent files
            throw new BuildException(ex);
        }
        catch (Throwable ex)
        {
            throw new BuildException(ex);
        }
    }


    /**
     * Initializes the given logger.
     *
     * @param logger logging category.
     * @param appender appender to add to the logger.
     * @param level level to set for the logger.
     */
    private void initLogger(
        Logger   logger,
        Appender appender,
        Level    level)
    {
        Object currentAppender = logger.getAppender(AntAppender.APPENDER_NAME);

        if (currentAppender == null)
        {
            logger.addAppender(appender);
            logger.setLevel(level);
        }
    }


    /**
     * Initializes all loggers with the given information.
     *
     * @param appender appender to add to all loggers.
     * @param level level to use for logging output.
     */
    private void initLoggers(
        Appender appender,
        Level    level)
    {
        Loggers.ALL.removeAllAppenders();

        ResourceBundle bundle =
            ResourceBundle.getBundle(
                "de.hunsicker.jalopy.storage.Bundle" /* NOI18N */,
                Convention.getInstance().getLocale());
        Loggers.ALL.setResourceBundle(bundle);

        initLogger(Loggers.IO, appender, level);
        initLogger(Loggers.PARSER, appender, level);
        initLogger(Loggers.PRINTER, appender, level);
        initLogger(Loggers.TRANSFORM, appender, level);

        if (_javadoc)
        {
            initLogger(Loggers.PARSER_JAVADOC, appender, level);
            initLogger(Loggers.PRINTER_JAVADOC, appender, level);
        }
    }


    /**
     * Loads the Class repository.
     *
     * @param paths locations to initialize the repository with.
     *
     * @since 0.5.3
     */
    private void loadRepository(String[] paths)
    {
        if (paths.length == 0)
        {
            return;
        }

        ClassRepository repository = ClassRepository.getInstance();

        File javaRuntime =
            new File(
                System.getProperty("java.home" /* NOI18N */) + File.separator
                + "lib" /* NOI18N */ + File.separator + "rt.jar" /* NOI18N */    );

        if (!javaRuntime.exists())
        {
            log(BUNDLE.getString("RUNTIME_NOT_FOUND" /* NOI18N */), Project.MSG_WARN);

            return;
        }

        List files = new ArrayList(paths.length);
        files.add(javaRuntime);

        for (int i = 0; i < paths.length; i++)
        {
            files.add(new File(paths[i]));
        }

        try
        {
            repository.loadAll(files);
        }
        catch (Throwable ex)
        {
            log(BUNDLE.getString("REPOSITORY_NOT_LOADED" /* NOI18N */), Project.MSG_WARN);

            // make sure the repository is empty so that import optimization
            // will be disabled
            if ((repository != null) && !repository.isEmpty())
            {
                try
                {
                    repository.unloadAll(files);
                }
                catch (Throwable e)
                {
                    log(
                        BUNDLE.getString("REPOSITORY_CLEANUP" /* NOI18N */),
                        Project.MSG_ERR);
                }
            }
        }
    }


    /**
     * Logs a message upon the process end.
     *
     * @param files the number of files that were formatted.
     *
     * @since 0.5.5
     */
    private void logEnd(int files)
    {
        if (_destDir == null)
        {
            Object[] args = { new Integer(files) };

            log(
                MessageFormat.format(
                    BUNDLE.getString("FORMATTED_FILES" /* NOI18N */), args),
                Project.MSG_INFO);
        }
        else
        {
            Object[] args = { new Integer(files), _destDir };

            log(
                MessageFormat.format(
                    BUNDLE.getString("FORMATTED_FILES_DESTINATION" /* NOI18N */), args),
                Project.MSG_INFO);
        }
    }


    /**
     * Logs a message upon the process start.
     *
     * @param files the number of files that are to be formatted.
     *
     * @since 0.5.5
     */
    private void logStart(int files)
    {
        if (_destDir == null)
        {
            Object[] args = { new Integer(files) };

            log(
                MessageFormat.format(
                    BUNDLE.getString("FORMAT_FILES" /* NOI18N */), args),
                Project.MSG_INFO);
        }
        else
        {
            Object[] args = { new Integer(files), _destDir };

            log(
                MessageFormat.format(
                    BUNDLE.getString("FORMAT_FILES_DESTINATION" /* NOI18N */), args),
                Project.MSG_INFO);
        }
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * Handles all message output.
     */
    private class AntAppender
        extends AppenderSkeleton
    {
        static final String APPENDER_NAME = "JalopyAntAppender" /* NOI18N */;

        public AntAppender()
        {
            this.name = APPENDER_NAME;
        }

        public void append(LoggingEvent ev)
        {
            switch (ev.getLevel().toInt())
            {
                case Level.DEBUG_INT :
                    log(ev.getRenderedMessage(), Project.MSG_INFO);

                    break;

                case Level.ERROR_INT :
                case Level.FATAL_INT :
                    log(ev.getRenderedMessage(), Project.MSG_ERR);

                    String[] lines = ev.getThrowableStrRep();

                    if (lines != null)
                    {
                        for (int i = 0; i < lines.length; i++)
                        {
                            log(lines[i], Project.MSG_ERR);
                        }
                    }

                    break;

                case Level.INFO_INT :
                    log(ev.getRenderedMessage(), Project.MSG_INFO);

                    break;

                case Level.WARN_INT :
                    log(ev.getRenderedMessage(), Project.MSG_WARN);

                    break;

                default :
                    break;
            }
        }


        public void close()
        {
        }


        public boolean requiresLayout()
        {
            return false;
        }
    }


    /**
     * Dedicated thread to be used on multi-processor machines.
     */
    private class FormatThread
        extends Thread
    {
        /** The used Jalopy instance. */
        Jalopy jalopy;

        /** Files to format. */
        List files; // List of <File>

        /** Error found during formatting? */
        boolean error;

        /** Is the thread currently running? */
        volatile boolean running;

        /** Number of files that were formatted. */
        int count;

        public FormatThread(final List files)
        {
            this.files = files;
            this.jalopy = createJalopy();
            this.running = true;
        }

        public void run()
        {
            File file = null;

            try
            {
                this.running = true;

                while (!this.files.isEmpty())
                {
                    synchronized (this.files)
                    {
                        if (!this.files.isEmpty())
                        {
                            file = (File) this.files.remove(0);
                        }
                        else
                        {
                            // no more files, stop processing
                            break;
                        }
                    }

                    try
                    {
                        this.jalopy.setInput(file);
                    }
                    catch (FileNotFoundException ex)
                    {
                        // should never happen as the DirectoryScanner reported
                        // the existence of the files
                        throw new BuildException(ex);
                    }

                    try
                    {
                        this.jalopy.setOutput(file);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        this.error = true;

                        Object[] args = { file, _destDir };

                        Loggers.IO.l7dlog(
                            Level.ERROR, "INVALID_OUTPUT_TARGET" /* NOI18N */, args, null);

                        if (_failOnError)
                        {
                            throw new BuildException(
                                BUNDLE.getString("INVALID_OUTPUT_TARGET" /* NOI18N */));
                        }
                    }

                    boolean success = this.jalopy.format();

                    if (jalopy.getState() == Jalopy.State.ERROR)
                    {
                        this.error = true;

                        if (_failOnError)
                        {
                            throw new BuildException(
                                BUNDLE.getString("UNKNOWN_ERROR" /* NOI18N */));
                        }
                    }
                    else if (success)
                    {
                        this.count++;
                    }
                }
            }
            finally
            {
                synchronized (_lock)
                {
                    this.running = false;
                    _lock.notify();
                }
            }
        }
    }
}
