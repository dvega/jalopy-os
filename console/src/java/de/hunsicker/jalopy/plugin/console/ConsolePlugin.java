/*
 * Jalopy Java Source Code Formatter
 *
 * Copyright (C) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * $Id$
 */
package de.hunsicker.jalopy.plugin.console;

import de.hunsicker.io.DirScanner;
import de.hunsicker.io.ExtensionFilter;
import de.hunsicker.io.FileFormat;
import de.hunsicker.io.Filters;
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.VersionMismatchException;
import de.hunsicker.jalopy.parser.ClassRepository;
import de.hunsicker.jalopy.parser.ClassRepositoryEntry;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Loggers;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.util.StringHelper;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.oro.io.Perl5FilenameFilter;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.Perl5Compiler;


/**
 * The console Plug-in provides a powerful command line interface for the
 * Jalopy engine.
 *
 * @version $Revision$
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 */
public final class ConsolePlugin
{
    //~ Static variables/initializers ·········································

    /** Indicates that we use a file as input source. */
    private static final int IO_FILE = 1;

    /** Indicates that we use standard input (System.in) as input source. */
    private static final int IO_SYSTEM = 2;

    /** Indicates that no file backups should be stored. */
    private static final int NOBACKUP = -10;

    /** Used to store a single argument string. */
    private static String _argString;

    /** Used as a monitor to synchronize processes. */
    private static final Object _lock = new Object();

    /** Logging. */
    private static final Logger _logger = Loggers.IO;

    /** Holds the number of files processed. */
    private static volatile int _numFiles;

    /** Scanner to search for Java source files. */
    private static DirScanner _scanner = new DirScanner();

    /** Number of running formatting threads. */
    private static int _threadCount;

    /** Number of parser threads to use. */
    private static int _threads = 1;
    private static final int OPT_FORCE = 4444;

    /** Holds the longoptions to use. */
    private static final LongOpt[] LONG_OPTIONS =
    {
        new LongOpt("dest", LongOpt.REQUIRED_ARGUMENT, null, 'd'),
        new LongOpt("encoding", LongOpt.REQUIRED_ARGUMENT, null, 'e'),
        new LongOpt("format", LongOpt.REQUIRED_ARGUMENT, null, 'f'),
        new LongOpt("force", LongOpt.NO_ARGUMENT, null, OPT_FORCE),
        new LongOpt("log", LongOpt.REQUIRED_ARGUMENT, null, 'l'),
        new LongOpt("nobackup", LongOpt.NO_ARGUMENT, null, NOBACKUP),
        new LongOpt("thread", LongOpt.REQUIRED_ARGUMENT, null, 't'),
        new LongOpt("recursive", LongOpt.OPTIONAL_ARGUMENT, null, 'r'),
        new LongOpt("style", LongOpt.REQUIRED_ARGUMENT, null, 's'),
        new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v'),
        new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h'),
        new LongOpt("disclaimer", LongOpt.NO_ARGUMENT, null, 'c')
    };

    //~ Instance variables ····················································

    /** Holds the name of the destination directory, if any. */
    private File _destDir;

    /** The file format we use to write files. */
    private FileFormat _fileFormat = FileFormat.AUTO;

    /** The encoding to use to read files. */
    private String _encoding;

    /** File to use load code convention from. */
    private String _convention;

    /** Should formatting be forced? */
    private boolean _force;

    /** Indicates whether no backups should be kept. */
    private boolean _noBackup;

    /** Exit code of the run. */
    private int _exitCode;

    /** I/O mode to use. Either {@link #IO_FILE} or {@link #IO_SYSTEM}. */
    private int _ioMode = IO_FILE;

    //~ Constructors ··························································

    /**
     * Creates a new ConsolePlugin object.
     */
    private ConsolePlugin()
    {
    }

    //~ Methods ·······························································

    /**
     * Starts Jalopy from the command line.
     *
     * @param argv command line arguments. Type
     */
    public static void main(String[] argv)
    {
        long start = System.currentTimeMillis();
        ConsolePlugin console = new ConsolePlugin();
        console.initializeLogging();
        console.parseArgs(argv);

        if ((console._ioMode != IO_SYSTEM) && !console.scan()) // no files found
        {
            if (_argString != null)
            {
                Object[] args = { _argString };
                System.out.println(MessageFormat.format(console.getResourceBundle()
                                                               .getString("NON_MATCHING_EXPRESSION"),
                                                        args));
                System.exit(1);
            }
        }
        else
        {
            try
            {
                Jalopy.checkCompatibility(StringHelper.getPackageName(
                                                console.getClass().getName()));
            }
            catch (VersionMismatchException ex)
            {
                Object[] args = { ex.getExpectedVersion(), ex.getFoundVersion() };
                System.out.println(MessageFormat.format(console.getResourceBundle()
                                                               .getString("VERSION_MISMATCH"),
                                                        args));
                System.exit(1);
            }

            if (_threads == 1)
            {
                console.format();
            }
            else
            {
                _threadCount = _threads;

                // span the formatting threads
                for (int i = 0; i < _threads; i++)
                {
                    new FormatThread(console).start();
                }

                try
                {
                    // wait until all formatting threads finished
                    synchronized (_lock)
                    {
                        while (_threadCount > 0)
                        {
                            _lock.wait();
                        }
                    }
                }
                catch (InterruptedException ignored)
                {
                    ;
                }
            }

            Object[] args =
            {
                new Integer(console._numFiles), new Integer(console._numFiles),
                console.getRuntime(System.currentTimeMillis() - start)
            };
            _logger.l7dlog(Level.INFO, "RUN_INFO", args, null);
        }
    }


    /**
     * Returns the exit code.
     *
     * @return exit code.
     */
    public int getExitCode()
    {
        return _exitCode;
    }


    /**
     * Sets the exit code of the program.
     *
     * @param code exit code to use.
     */
    protected void setExitCode(int code)
    {
        _exitCode = code;
    }


    /**
     * Returns an array of objects describing the long command line options
     * (those options normally of the form <code>--option</code>).
     *
     * @return objects containing all the necessary info about the long
     *         command line options.
     */
    protected LongOpt[] getLongOptions()
    {
        return LONG_OPTIONS;
    }


    /**
     * Returns a string containing a list of the short-option characters for
     * short command line options.
     *
     * @return <code>d:e:f:l:t:hcvr::s:</code>
     */
    protected String getOptString()
    {
        return "d:e:f:l:t:hcvr::s:";
    }


    /**
     * Returns the resource bundle were localized strings are stored.
     *
     * @return resource bundle to use.
     */
    protected ResourceBundle getResourceBundle()
    {
        return ResourceBundle.getBundle(getClass().getName());
    }


    /**
     * Displays the copyright header on standard output.
     */
    protected void displayCopyright()
    {
        ResourceBundle b = getResourceBundle();
        System.out.println(b.getString("PROGRAM_NAME") + " " +
                           Jalopy.getVersion() + ", Copyright (c) " +
                           b.getString("COPYRIGHT_YEAR") + " " +
                           b.getString("AUTHOR.1"));
        System.out.println(b.getString("PROGRAM_NAME") +
                           " comes with ABSOLUTELY NO WARRANTY");
        System.out.println("Type 'java " + b.getString("PROGRAM_CMD") +
                           " -c' to show the disclaimer");
        System.out.println();
    }


    /**
     * Displays help information on standard output and exits.
     */
    protected void displayHelp()
    {
        displayCopyright();
        System.out.println();
        displayUsage();
        System.exit(getExitCode());
    }


    /**
     * Displays a hint how to invoke the usage help on standard output and
     * exits.
     */
    protected void displayHint()
    {
        ResourceBundle b = getResourceBundle();
        Object[] args = { "'java " + b.getString("PROGRAM_CMD") + " -h'" };
        System.out.println(MessageFormat.format(b.getString("HINT"), args));
        System.out.println();
        System.exit(getExitCode());
    }


    /**
     * Displays the software licence on standard output and exits.
     */
    protected void displayLicence()
    {
        ResourceBundle b = getResourceBundle();
        System.out.println(
              " Jalopy Java Source Code Formatter Console Plug-in");
        System.out.println(" Copyright (c) " +
                           b.getString("COPYRIGHT_YEAR") + " " +
                           b.getString("AUTHOR.1"));
        System.out.println();
        System.out.println(
              " This program is free software; you can redistribute it and/or modify\n" +
              " it under the terms of the GNU General Public License as published by\n" +
              " the Free Software Foundation; either version 2 of the License, or\n" +
              " (at your option) any later version.\n\n" +
              " This program is distributed in the hope that it will be useful,\n" +
              " but WITHOUT ANY WARRANTY; without even the implied warranty of\n" +
              " MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\n" +
              " GNU General Public License for more details.\n\n" +
              " You should have received a copy of the GNU General Public License\n" +
              " along with this program; if not, write to the Free Software Foundation,\n" +
              " Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.");
        System.out.println();
        System.exit(getExitCode());
    }


    /**
     * Displays the usage help on standard output and exits.
     */
    protected void displayUsage()
    {
        ResourceBundle b = getResourceBundle();
        System.out.println("Usage: java " + b.getString("PROGRAM_CMD") +
                           " [-options] [args...]");
        System.out.println();
        System.out.println(
              "   or  java -jar jalopy-<version>.jar [-options] [args...]");
        System.out.println();
        System.out.println("where options include:");
        System.out.println(
              "  -c, --disclaimer        print software disclaimer");
        System.out.println(
              "  -d, --dest=DIR          use DIR as base output directory");
        System.out.println(
              "  -e, --encoding=WORD     assume WORD as encoding of input files where WORD");
        System.out.println(
              "                          describes one of the JDK supported encodings");
        System.out.println("                          (defaults to " +
                           System.getProperty("file.encoding") +
                           " if omitted)");
        System.out.println(
              "  -f, --format=WORD       use WORD as output file format where WORD can be");
        System.out.println(
              "                          either UNIX, DOS, MAC, AUTO (the default) or DEFAULT");
        System.out.println("                          (all case-insensitive)");
        System.out.println(
              "      --force             force formatting even if file up-to-date");
        System.out.println("  -h, --help              display this help");
        System.out.println(
              "      --nobackup          indicates that no backups should be kept");
        System.out.println(
              "  -r, --recursive{=NUM}   recurse into directories, up to NUM levels");
        System.out.println(
              "                          if NUM is omitted, recurses indefinitely");
        System.out.println(
              "  -c, --convention=FILE        use FILE as code convention file");
        System.out.println(
              "  -t, --thread=NUM        use NUM processing threads");
        System.out.println(
              "  -v, --version           print product version and exit");
        System.out.println();
        System.exit(getExitCode());
    }


    /**
     * Displays the software version on standard output and exits.
     */
    protected void displayVersion()
    {
        displayCopyright();
        System.out.println();
        System.exit(getExitCode());
    }


    /**
     * Parses the given command line arguments and decodes the options.
     *
     * @param argv command line arguments.
     */
    protected void parseArgs(String[] argv)
    {
        _scanner = new DirScanner();
        _scanner.setFilterPolicy(Filters.POLICY_STRICT);

        Getopt g = new Getopt("Jalopy", argv, getOptString(), getLongOptions());
        int c = -1;

        while ((c = g.getopt()) != -1)
        {
            switch (c)
            {
                case 'd' :
                    _destDir = new File(g.getOptarg());

                    if (!_destDir.exists())
                    {
                        if (!_destDir.mkdirs())
                        {
                            Object[] args = { _destDir };
                            System.out.println(MessageFormat.format(
                                                     getResourceBundle()
                                                         .getString("ERROR_CREATING_DESTINATION_DIRECTORY"),
                                                     args));
                            System.exit(1);
                        }
                    }
                    else if (_destDir.isFile())
                    {
                        Object[] args = { _destDir };
                        System.out.println(MessageFormat.format(
                                                 getResourceBundle()
                                                     .getString("INVALID_DESTINATION_DIRECTORY"),
                                                 args));
                        System.exit(1);
                    }

                    break;

                case 's' :
                    _convention = g.getOptarg();

                    break;

                case 'e' :

                    if (isValidEncoding(g.getOptarg()))
                    {
                        _encoding = g.getOptarg();
                    }
                    else
                    {
                        Object[] args = { g.getOptarg() };
                        System.out.println(MessageFormat.format(
                                                 getResourceBundle()
                                                     .getString("INVALID_ENCODING"),
                                                 args));
                        System.exit(1);
                    }

                    break;

                case 'f' :

                    String format = g.getOptarg().trim().toLowerCase();

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
                        Object[] args = { format };
                        System.out.println(MessageFormat.format(
                                                 getResourceBundle()
                                                     .getString("INVALID_FILE_FORMAT"),
                                                 args));
                        setExitCode(1);
                        displayHint();
                    }

                    break;

                case OPT_FORCE :
                    _force = true;

                    break;

                case 'c' :
                    displayLicence();

                    break;

                case 'h' :
                    displayHelp();

                    break;

                case 'l' :

                    /**
                     * @todo implement file logging
                     */
                    break;

                case 'r' :

                    String level = g.getOptarg();

                    if (level == null)
                    {
                        // recurse indefinitely
                        _scanner.setMaxLevels(Integer.MAX_VALUE);
                    }
                    else
                    {
                        try
                        {
                            int number = Integer.parseInt(level);

                            if (number > 0)
                            {
                                _scanner.setMaxLevels(number);
                            }
                            else
                            {
                                Object[] args = { level };
                                System.out.println(MessageFormat.format(
                                                         getResourceBundle()
                                                             .getString("INVALID_RECURSION_NUMBER"),
                                                         args));
                                setExitCode(1);
                                displayHint();
                            }
                        }
                        catch (NumberFormatException ex)
                        {
                            Object[] args = { level };
                            System.out.println(MessageFormat.format(
                                                     getResourceBundle()
                                                         .getString("INVALID_RECURSION_LEVEL"),
                                                     args));
                            setExitCode(1);
                            displayHint();
                        }
                    }

                    break;

                case 't' :

                    try
                    {
                        int threads = Integer.parseInt(g.getOptarg());

                        if (threads > 0)
                        {
                            _threads = threads;
                        }
                        else
                        {
                            Object[] args = { new Integer(threads) };
                            System.out.println(MessageFormat.format(
                                                     getResourceBundle()
                                                         .getString("INVALID_THREAD_NUMBER"),
                                                     args));
                            setExitCode(1);
                            displayHint();
                        }
                    }
                    catch (NumberFormatException ex)
                    {
                        Object[] args = { g.getOptarg() };
                        System.out.println(MessageFormat.format(
                                                 getResourceBundle()
                                                     .getString("INVALID_THREAD_NUMBER"),
                                                 args));
                        setExitCode(1);
                        displayHint();
                    }

                    break;

                case 'v' :
                    displayVersion();

                    break;

                case NOBACKUP :
                    _noBackup = true;

                    break;

                case '?' :
                    setExitCode(1);
                    displayUsage();

                    break;
            }
        }

        PatternCompiler compiler = new Perl5Compiler();

        // handle the non-option args - our targets (files, dirs
        // and filter expressions)
        if ((argv.length > 0) && (g.getOptind() != argv.length))
        {
            List targets = new ArrayList(5);

            for (int i = g.getOptind(); i < argv.length; i++)
            {
                File target = new File(argv[i]);

                if (target.exists())
                {
                    targets.add(target);
                }
                else // check for pattern expression
                {
                    try
                    {
                        _argString = argv[i];

                        if (argv[i].indexOf(File.separatorChar) > -1)
                        {
                            String path = argv[i].substring(0,
                                                            argv[i].lastIndexOf(
                                                                  File.separatorChar) + 1);
                            target = new File(path);

                            if (target.exists())
                            {
                                String pattern = argv[i].substring(
                                                       path.length());

                                _logger.debug("Add filter " + pattern);
                                compiler.compile(argv[i]);
                                _scanner.addFilter(
                                      new Perl5FilenameFilter(pattern));
                                targets.add(target);

                                continue;
                            }
                        }
                        else
                        {
                            compiler.compile(argv[i]);
                            _logger.debug("Add filter " + argv[i]);
                            _scanner.addFilter(
                                  new Perl5FilenameFilter(argv[i]));


                            // no path was given, so use current directory
                            targets.add(new File(".").getAbsolutePath());

                            continue;
                        }
                    }
                    catch (Exception ex)
                    {
                        Object[] args = { ex.getMessage(), argv[i] };
                        System.out.println(MessageFormat.format(
                                                 getResourceBundle()
                                                     .getString("MALFORMED_EXPRESSION"),
                                                 args));
                        System.exit(1);
                    }

                    Object[] args = { argv[i] };
                    System.out.println(MessageFormat.format(
                                             getResourceBundle()
                                                 .getString("INPUT_SOURCE_NOT_EXIST"),
                                             args));
                }
            }

            _scanner.setTargets(targets);
        }
        else
        {
            try
            {
                if (System.in.available() == 0)
                {
                    displayUsage();
                }
            }
            catch (IOException ignored)
            {
                ;
            }

            System.err.println(getResourceBundle()
                                   .getString("LISTEN_ON_STDIN"));


            // no non-option string given; listen on STD_IN, write
            // to STD_OUT, messages goes to standard STD_ERR
            _ioMode = IO_SYSTEM;
            _numFiles = 1;
        }
    }


    /**
     * Gets a formatted string with runtime info for the given time.
     *
     * @param time processing time.
     *
     * @return formatted string.
     */
    private String getRuntime(long time)
    {
        /**
         * @todo use NumberFormat?
         */
        StringBuffer buf = new StringBuffer(8);
        long hour = time / 3600000;
        time = time - (hour * 3600000);

        long min = time / 60000;
        time = time - (min * 60000);

        long sec = time / 1000;
        time = time - (sec * 1000);

        long msec = time;

        if (hour > 0)
        {
            if (hour < 10)
            {
                buf.append('0');
            }

            buf.append(hour);
            buf.append(':');
        }

        if (min > 0)
        {
            if (min < 10)
            {
                buf.append('0');
            }

            buf.append(min);

            if (hour == 0)
            {
                buf.append(':');
            }
        }

        if ((hour == 0) && (sec > 0))
        {
            if ((min > 0) && (sec < 10))
            {
                buf.append('0');
            }

            buf.append(sec);

            if ((min == 0) && (sec < 2))
            {
                buf.append('.');
            }
        }

        if ((min == 0) && (sec < 2))
        {
            if (msec < 100)
            {
                buf.append('0');
            }

            if (msec < 10)
            {
                buf.append('0');
            }

            buf.append(msec);
        }

        if (hour > 0)
        {
            buf.append(" hours");
        }
        else if (min > 0)
        {
            buf.append(" min");
        }
        else if (sec > 0)
        {
            buf.append(" sec");
        }
        else
        {
            buf.append(" msec");
        }

        return buf.toString();
    }


    /**
     * Indicates whether the given encoding is supported on the used platform.
     *
     * @param enc encoding name.
     *
     * @return <code>true</code> if <em>enc</em> is a valid encoding.
     */
    private boolean isValidEncoding(String enc)
    {
        try
        {
            new String(new byte[0], enc);

            return true;
        }
        catch (UnsupportedEncodingException ex)
        {
            return false;
        }
    }


    /**
     * Does the actual formatting.
     */
    private void format()
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
                /**
                 * @todo use logger
                 */
                ex.printStackTrace();

                return;
            }
        }

        jalopy.setFileFormat(_fileFormat);

        if (_encoding != null)
        {
            // this is safe, we checked the encoding on startup
            jalopy.setEncoding(_encoding);
        }

        if (_ioMode == IO_SYSTEM)
        {
            jalopy.setForce(true);

            Writer out = new BufferedWriter(new OutputStreamWriter(System.out));

            try
            {
                jalopy.setInput(System.in, "System.in");
                jalopy.setOutput(out);
                jalopy.format();

                if (jalopy.getState() == Jalopy.State.ERROR)
                {
                    System.exit(1);
                }
            }
            finally
            {
                try
                {
                    if (System.in != null)
                    {
                        System.in.close();
                    }
                }
                catch (IOException ignored)
                {
                    ;
                }

                try
                {
                    if (out != null)
                    {
                        out.close();
                    }
                }
                catch (IOException ignored)
                {
                    ;
                }
            }
        }
        else
        {
            Convention settings = Convention.getInstance();
            int backupLevel = settings.getInt(Keys.BACKUP_LEVEL,
                                           Defaults.BACKUP_LEVEL);

            if (_noBackup)
            {
                jalopy.setBackup(false);
            }
            else
            {
                // if backup level == 0, no backups are kept
                jalopy.setBackup(backupLevel > 0);
                jalopy.setBackupLevel(backupLevel);
            }

            jalopy.setInspect(settings.getBoolean(Keys.INSPECTOR,
                                               Defaults.INSPECTOR));
            jalopy.setHistoryPolicy(History.Policy.valueOf(settings.get(
                                                                 Keys.HISTORY_POLICY,
                                                                 Defaults.HISTORY_POLICY)));

            if (_force)
            {
                jalopy.setForce(_force);
            }
            else
            {
                jalopy.setForce(settings.getBoolean(Keys.FORCE_FORMATTING,
                                                 Defaults.FORCE_FORMATTING));
            }

            jalopy.setBackupDirectory(settings.get(Keys.BACKUP_DIRECTORY,
                                                Convention.getBackupDirectory()
                                                           .getAbsolutePath()));

            if (_destDir != null)
            {
                jalopy.setDestination(_destDir);
            }

            while (!_scanner.isFinished() || !_scanner.isEmpty())
            {
                File source = null;

                try
                {
                    // blocking take
                    source = _scanner.take();
                    jalopy.setInput(source);
                    jalopy.setOutput(source);
                    jalopy.format();

                    if (jalopy.getState() == Jalopy.State.ERROR)
                    {
                        /**
                         * @todo user configurable FAIL_ON_ERROR
                         */
                        System.exit(1);
                    }

                    _numFiles++;
                }
                catch (IOException ex)
                {
                    /**
                     * @todo use logger
                     */
                    ex.printStackTrace();
                }
                catch (InterruptedException ex)
                {
                    return;
                }
                catch (Exception ex)
                {
                    /**
                     * @todo use logger
                     */
                    ex.printStackTrace();
                }
            }
        }

        if (_logger.isDebugEnabled())
        {
            _logger.debug(jalopy.getProfileTimes());
        }
    }


    /**
     * Program initialization.
     */
    private void initializeLogging()
    {
        Loggers.initialize(
              new ConsoleAppender(new PatternLayout("[%p] %m\n"),
                                  (_ioMode == IO_FILE)
                                      ? "System.out"
                                      : "System.err"));
    }


    /**
     * Loads the repository
     */
    private void loadRepository()
    {
        ClassRepository repository = ClassRepository.getInstance();
        ClassRepositoryEntry.Info[] infos = repository.getInfo();
        List files = new ArrayList(infos.length);

        for (int i = 0; i < infos.length; i++)
        {
            files.add(infos[i].getLocation());
        }

        try
        {
            repository.loadAll(files);
        }
        catch (Exception ex)
        {
            // make sure the repository is empty so that import transformation
            // will be disabled
            if ((repository != null) && !repository.isEmpty())
            {
                try
                {
                    repository.unloadAll(files);
                }
                catch (Exception e)
                {
                    _logger.error("Error cleaning up repository", e);
                }

                _logger.warn("Could not load the import repository", ex);
            }
        }
    }


    /**
     * Scans the given targets. This method must be called
     * <strong>AFTER</strong> the parsing of the command line arguments.
     *
     * @return <code>true</code> if any matching file(s) could be found.
     *
     * @see #parseArgs
     */
    private boolean scan()
    {
        _scanner.addFilter(new ExtensionFilter(".java"));


        /**
         * @todo start async if multi-threaded
         */
        _scanner.run();

        return !_scanner.isEmpty();
    }

    //~ Inner Classes ·························································

    private static class FormatThread
        extends Thread
    {
        ConsolePlugin console;

        public FormatThread(ConsolePlugin console)
        {
            this.console = console;
        }

        public void run()
        {
            try
            {
                console.format();
            }
            finally
            {
                synchronized (_lock)
                {
                    _threadCount--;
                    _lock.notify();
                }
            }
        }
    }
}