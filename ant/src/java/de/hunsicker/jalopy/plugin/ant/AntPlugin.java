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
package de.hunsicker.jalopy.plugin.ant;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.parser.ClassRepository;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Loggers;
import de.hunsicker.jalopy.storage.Convention;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;


//import org.apache.tools.ant.types.selectors.FilenameSelector;

/**
 * The Jalopy Ant Plug-in. Formats Java source files according to some user
 * configurable rules. For more information about the Jalopy Java Source Code
 * Formatter visit the official homepage: <a
 * href="http://jalopy.sf.net/">http://jalopy.sf.net/</a>
 *
 * @version $Revision$
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 */
public class AntPlugin
    extends Task
{
    //~ Static variables/initializers ·········································

    /** The .java file extension. */
    private static final String EXT_JAVA = ".java";

    /** Text to display if a run produced errors. */
    private static final String FAIL_MSG = "Run failed, messages should have been provided.";

    /** Did the user specified the &quot;history&quot; attribute? */
    private static final int HISTORY_UNSPECIFIED = -1;

    //~ Instance variables ····················································

    /** Destination directory. */
    private File _destDir;

    /** The file to format (if we're processing only a single file). */
    private File _file;

    /** The file format to use. */
    private FileFormat _fileFormat = FileFormat.AUTO;

    /** The filesets to format (if we're processing a fileset). */
    private List _filesets; // List of <FileSet>

    /** Helper object to synchronize processes. */
    private final Object _lock = new Object();

    /** The level to control logging output. */
    private Level _loglevel;

    /** The classpath to init the ClassRepository with. */
    private Path _classpath;

    /** The history policy to apply. */
    private History.Policy _history;

    /** Encoding to use. */
    private String _encoding; // null means system default encoding

    /** File path of the convention to use. */
    private String _convention;

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

    //~ Constructors ··························································

    /**
     * Creates a new AntPlugin object.
     */
    public AntPlugin()
    {
    }

    //~ Methods ·······························································

    /**
     * Sets whether a backup of an original file should be kept.
     *
     * @param backup if <code>true</code> a backup of an original file will be
     *        kept.
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
     * Sets the destination directory into which the Java source files should
     * be formatted.
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
     * Sets whether an error will immediately cancel the run. Default is
     * <em>false</em>.
     *
     * @param fail if <code>true</code> an error will lead to an immediate
     *        cancelation of the run.
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
            throw new IllegalArgumentException(file + " does not exist");
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

        if (format.equals("dos") ||
            format.equals(FileFormat.DOS.getLineSeparator()))
        {
            _fileFormat = FileFormat.DOS;
        }
        else if (format.equals("default") ||
                 format.equals(FileFormat.DEFAULT.toString()))
        {
            _fileFormat = FileFormat.DEFAULT;
        }
        else if (format.equals("unix") ||
                 format.equals(FileFormat.UNIX.getLineSeparator()))
        {
            _fileFormat = FileFormat.UNIX;
        }
        else if (format.equals("mac") ||
                 format.equals(FileFormat.MAC.getLineSeparator()))
        {
            _fileFormat = FileFormat.MAC;
        }
        else if (format.equals("auto") ||
                 format.equals(FileFormat.AUTO.toString()))
        {
            _fileFormat = FileFormat.AUTO;
        }
        else
        {
            throw new IllegalArgumentException("invalid file format specified");
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
     * @param fork if <code>true</code> the task will be executed in a new
     *        process.
     * @since 1.0b8
     */
    public void setFork(boolean fork)
    {
        _fork = fork;
    }


    /**
     * Sets the history policy to use.
     *
     * @param policy Either <ul><li><code>COMMENT</code> or</li>
     *        <li><code>FILE</code> or</li> <li><code>NONE</code></li> </ul>
     *
     * @throws IllegalArgumentException if an invalid history policy is
     *         specified.
     */
    public void setHistory(String policy)
    {
        policy = policy.trim().toLowerCase();

        if ("comment".equals(policy))
        {
            _history = History.Policy.COMMENT;
        }
        else if ("file".equals(policy))
        {
            _history = History.Policy.FILE;
        }
        else if ("none".equals(policy))
        {
            _history = History.Policy.DISABLED;
        }
        else
        {
            throw new IllegalArgumentException("invalid history policy -- " +
                                               policy);
        }
    }


    /**
     * Sets whether Javadoc related messages should be displayed. Default is
     * <em>true</em>.
     *
     * <p>
     * Note that setting this switch to <code>false</code> means that
     * <strong>no</strong> Javadoc related messages will be displayed no
     * matter what ever happens! Even if formatting failed due to Javadoc
     * parsing errors, there will be no output indicating this fact.
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
     * FATAL
     * </li>
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
     * being <code>FATAL</code> the highest level and <code>DEBUG</code> the
     * lowest level.
     *
     * <p>
     * Enabling logging at a given level also enables logging at all higher
     * levels.
     * </p>
     *
     * @param level the logging level to use.
     *
     * @throws IllegalArgumentException if an invalid logging level has been
     *         specified.
     */
    public void setLoglevel(String level)
    {
        level = level.trim().toUpperCase();

        if ("INFO".equals(level))
        {
            _loglevel = Level.INFO;
        }
        else if ("DEBUG".equals(level))
        {
            _loglevel = Level.DEBUG;
        }
        else if ("WARN".equals(level))
        {
            _loglevel = Level.WARN;
        }
        else if ("ERROR".equals(level))
        {
            _loglevel = Level.ERROR;
        }
        else
        {
            throw new IllegalArgumentException(
                  "invalid loglevel specified -- " + level);
        }
    }

    /**
     * Sets the location of the code convention to use. This may either be an absolute path to
     * both a local file or distributed url or a path relative to the base
     * directory of the project.
     *
     * @param location absolute or relative path to load style convention
     *        from.
     * @deprecated
     */
    public void setStyle(String location)
    {
        setConvention(location);
    }


    /**
     * Sets the location of the code convention to use. This may either be an absolute path to
     * both a local file or distributed url or a path relative to the base
     * directory of the project.
     *
     * @param location absolute or relative path to load style convention
     *        from.
     * @since 1.0b9
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
     * Sets the number of processing threads to use.
     *
     * @param threads the thread count to use. A number between 1 and 8.
     *
     * @throws IllegalArgumentException if <code>threads &lt; 1</code> or
     *         <code>threads &gt; 8</code>
     */
    public void setThreads(String threads)
    {
        String number = threads.trim();

        try
        {
            _threads = Integer.parseInt(number);

            if ((_threads < 1) || (_threads > 8))
            {
                throw new IllegalArgumentException("invalid thread count -- " +
                                                   number);
            }
        }
        catch (NumberFormatException ex)
        {
            throw new IllegalArgumentException("invalid thread count -- " +
                                               number);
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
            if (System.getProperty("de.hunsicker.jalopy.plugin.ant.forked",
                                   "false").equals("false"))
            {
                try
                {
                    CommandlineJava cmd = new CommandlineJava();
                    cmd.createArgument()
                       .setValue("-Dde.hunsicker.jalopy.plugin.ant.forked=true");
                    cmd.createArgument().setValue("-classpath");
                    cmd.createArgument()
                       .setValue(System.getProperty("java.class.path"));
                    cmd.createArgument().setValue("org.apache.tools.ant.Main");
                    cmd.createArgument().setValue("-logger");
                    cmd.createArgument()
                       .setValue("org.apache.tools.ant.NoBannerLogger");
                    cmd.createArgument().setValue("-f");
                    cmd.createArgument()
                       .setValue(this.project.getProperty("ant.file"));
                    cmd.createArgument().setValue(getOwningTarget().getName());

                    Execute exe = new Execute();
                    exe.setAntRun(this.project);
                    exe.setWorkingDirectory(this.project.getBaseDir());
                    exe.setCommandline(cmd.getCommandline());
                    exe.execute();

                    if (exe.getExitValue() != 0)
                    {
                        throw new BuildException(FAIL_MSG);
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
                throw new BuildException(
                      "Specify at least one source - a file or a fileset.");
            }
        }
        else
        {
            if (_file.exists())
            {
                if (_file.isDirectory())
                {
                    throw new BuildException(
                          "Use a fileset to format directories.");
                }
            }
            else
            {
                throw new BuildException("File does not exist -- " + _file);
            }
        }

        log("Jalopy Java Source Code Formatter " + Jalopy.getVersion());

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
            _threads = Convention.getInstance()
                                  .getInt(Keys.THREAD_COUNT,
                                          Defaults.THREAD_COUNT);
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
     * @return <code>true</code> if either one of the given threads is still
     *         running.
     */
    private boolean isRunning(List threads)
    {
        for (int i = 0, size = threads.size(); i < size; i++)
        {
            FormatThread thread = (FormatThread)threads.get(i);

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
        jalopy.setEncoding(_encoding);
        jalopy.setFileFormat(_fileFormat);
        jalopy.setInspect(settings.getBoolean(Keys.INSPECTOR, Defaults.INSPECTOR));

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
            History.Policy historyPolicy = History.Policy.valueOf(settings.get(
                                                                        Keys.HISTORY_POLICY,
                                                                        Defaults.HISTORY_POLICY));
            jalopy.setHistoryPolicy(historyPolicy);
        }

        if (_isBackup)
        {
            jalopy.setBackup(_backup);
        }
        else
        {
            jalopy.setBackup(
                  settings.getInt(Keys.BACKUP_LEVEL, Defaults.BACKUP_LEVEL) > 0);
        }

        if (_isForce)
        {
            jalopy.setForce(_force);
        }
        else
        {
            jalopy.setForce(settings.getBoolean(Keys.FORCE_FORMATTING,
                                             Defaults.FORCE_FORMATTING));
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
         * @todo if we ever start relying on Ant 1.5 we can add a selector and
         *       won't have to add a Java include pattern anymore
         */

        // FilenameSelector selector = new FilenameSelector();
        // selector.setName("**/*.java");
        // build the list with all files that are to be processed
        for (int i = 0, size = _filesets.size(); i < size; i++)
        {
            FileSet fs = (FileSet)_filesets.get(i);

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

        log("Formatting " + files.size() + " source files " +
            ((_destDir == null) ? ""
                                : ("to " + _destDir)), Project.MSG_INFO);

        int numThreads = _threads;

        if (files.size() < _threads)
        {
            numThreads = files.size();
        }

        List threads = new ArrayList(numThreads);

        // start the worker threads
        for (int j = 0; j < numThreads; j++)
        {
            Thread thread = new FormatThread(files);
            threads.add(thread);
            thread.start();
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
                FormatThread thread = (FormatThread)threads.get(i);
                count += thread.count;

                if (!error)
                {
                    error = thread.isError;
                }
            }

            log(count + " source files formatted " +
                ((_destDir == null) ? ""
                                    : ("to " + _destDir)), Project.MSG_INFO);

            if (error)
            {
                throw new BuildException(FAIL_MSG);
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
                jalopy.setOutput(_file);
                jalopy.format();

                if (jalopy.getState() == Jalopy.State.ERROR)
                {
                    error = true;

                    if (_failOnError)
                    {
                        throw new BuildException("Error formatting " +
                                                 _file);
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
             * @todo if we ever start relying on Ant 1.5 we can add a selector
             *       and won't have to add a Java include pattern anymore
             */

            // FilenameSelector selector = new FilenameSelector();
            // selector.setName("**/*.java");
            // format filesets
            for (int i = 0, size = _filesets.size(); i < size; i++)
            {
                FileSet fs = (FileSet)_filesets.get(i);

                //fs.addFilename(selector);
                DirectoryScanner scanner = fs.getDirectoryScanner(this.project);
                File fromDir = fs.getDir(this.project);
                String[] srcFiles = scanner.getIncludedFiles();
                log("Formatting " + srcFiles.length + " source files " +
                    ((_destDir == null) ? ("in " + fromDir)
                                        : ("to " + _destDir)),
                    Project.MSG_INFO);

                int count = 0;

                for (int j = 0; j < srcFiles.length; j++)
                {
                    File file = new File(fromDir, srcFiles[j]);
                    jalopy.setInput(file);
                    jalopy.setOutput(file);
                    jalopy.format();

                    if (jalopy.getState() == Jalopy.State.ERROR)
                    {
                        error = true;

                        if (_failOnError)
                        {
                            throw new BuildException("Error formatting " +
                                                     file);
                        }
                    }
                    else
                    {
                        count++;
                    }
                }

                log(count + " source files formatted " +
                    ((_destDir == null) ? ("in " + fromDir)
                                        : ("to " + _destDir)),
                    Project.MSG_INFO);
            }

            if (error)
            {
                throw new BuildException(FAIL_MSG);
            }
        }
        catch (FileNotFoundException ex)
        {
            // should never happen as the DirectoryScanner reports only
            // existent files
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
    private void initLogger(Logger   logger,
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
    private void initLoggers(Appender appender,
                             Level    level)
    {
        Loggers.ALL.removeAllAppenders();

        ResourceBundle bundle = ResourceBundle.getBundle(
                                      "de.hunsicker.jalopy.Bundle");
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
        File javaRuntime = new File(System.getProperty("java.home") +
                                    File.separator + "lib" + File.separator +
                                    "rt.jar");

        if (!javaRuntime.exists())
        {
            log("Could not find the Java runtime library (rt.jar), import optimization feature will be disabled.",
                Project.MSG_WARN);

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
            log("Could not load the import repository, import otimization feature will be disabled.",
                Project.MSG_WARN);
            ex.printStackTrace();

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
                    log("Error cleaning up repository", Project.MSG_ERR);
                }
            }

            System.err.println(repository.isEmpty());
        }
    }

    //~ Inner Classes ·························································

    /**
     * Handles all message output.
     */
    private class AntAppender
        extends AppenderSkeleton
    {
        static final String APPENDER_NAME = "JalopyAntAppender";

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


    private static class ForkLogger
        extends DefaultLogger
    {
        /*public void buildStarted(BuildEvent event) {
        }
        public void buildFinished(BuildEvent event) {
        }

        public void targetStarted(BuildEvent event) {
        }

        public void targetFinished(BuildEvent event) {
        }

        public void taskStarted(BuildEvent event) {}
        public void taskFinished(BuildEvent event) {}*/
        /*public void messageLogged(BuildEvent event) {

            PrintStream logTo = event.getPriority() == Project.MSG_ERR ? err : out;

            // Filter out messages based on priority
            if (event.getPriority() <= msgOutputLevel) {

                // Print the message
                logTo.println(event.getMessage());
            }
        }*/
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
        boolean isError;

        /** Is the thread currently running? */
        volatile boolean running;

        /** Number of files that were formatted. */
        int count;

        public FormatThread(final List files)
        {
            this.files = files;
            this.jalopy = createJalopy();
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
                            file = (File)this.files.remove(0);
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

                    this.jalopy.setOutput(file);
                    this.jalopy.format();

                    if (jalopy.getState() == Jalopy.State.ERROR)
                    {
                        this.isError = true;

                        if (_failOnError)
                        {
                            throw new BuildException("Error formatting " +
                                                     file);
                        }
                    }
                    else
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