/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hunsicker.io.Copy;
import de.hunsicker.io.FileBackup;
import de.hunsicker.io.FileFormat;
import de.hunsicker.io.IoHelper;
import de.hunsicker.jalopy.language.CodeInspector;
import de.hunsicker.jalopy.language.JavaNode;
import de.hunsicker.jalopy.language.JavaRecognizer;
import de.hunsicker.jalopy.language.JavaTokenTypes;
import de.hunsicker.jalopy.printer.NodeWriter;
import de.hunsicker.jalopy.printer.PrinterFactory;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.Environment;
import de.hunsicker.jalopy.storage.History;
import de.hunsicker.jalopy.storage.Loggers;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MatchResult;


/**
 * <p>
 * The bean-like interface to Jalopy.
 * </p>
 *
 * <p>
 * <strong>Sample Usage</strong>
 * </p>
 *
 * <p>
 * <pre class="snippet">
 * // create a new Jalopy instance with the currently active code convention settings
 * Jalopy jalopy = new Jalopy();
 *
 * File file = ...;
 *
 * // specify input and output target
 * jalopy.setInput(file);
 * jalopy.setOutput(file);
 *
 * // format and overwrite the given input file
 * jalopy.format();
 *
 * if (jalopy.getState() == Jalopy.State.OK)
 *     System.out.println(file + " successfully formatted");
 * else if (jalopy.getState() == Jalopy.State.WARN)
 *     System.out.println(file + " formatted with warnings");
 * else if (jalopy.getState() == Jalopy.State.ERROR)
 *     System.out.println(file + " could not be formatted");
 *
 * // setup a destination directory
 * File destination = ...;
 *
 * jalopy.setDestination(destination);
 * jalopy.setInput(file);
 * jalopy.setOutput(file);
 *
 * // format the given input file and write the output to the given destination,
 * // the package structure will be retained automatically
 * jalopy.format();
 *
 * ...
 * </pre>
 * </p>
 *
 * <p>
 * <strong>Thread safety</strong>
 * </p>
 *
 * <p>
 * This class is <em>thread-hostile</em>, it is not safe for concurrent use by multiple
 * threads even if all method invocations are surrounded by external synchronisation.
 * You should rather create one instance of this class per thread.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class Jalopy
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The empty byte array. */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /** Indicates a file input. */
    private static final int FILE_INPUT = 1;

    /** Indicates a file output. */
    private static final int FILE_OUTPUT = 2;

    /** Indicates the illegal mode where no processing can take place. */
    private static final int ILLEGAL = 0;

    /** Indicates a reader input. */
    private static final int READER_INPUT = 16;

    /** Indicates a string input. */
    private static final int STRING_INPUT = 4;

    /** Indicates a string output. */
    private static final int STRING_OUTPUT = 8;

    /** Indicates a writer output. */
    private static final int WRITER_OUTPUT = 32;

    /** The version information. */
    private static String _version;

    /** Major version number. */
    private static byte _majorVersion;

    /** Minor version number. */
    private static byte _minorVersion;

    /** Micro version number. */
    private static byte _microVersion;

    /** Indicates whether this version is a beta version. */
    private static boolean _isBeta;

    /** Indicates file to file mode. */
    private static final int FILE_FILE = FILE_INPUT | FILE_OUTPUT;

    /** Indicates file to string mode. */
    private static final int FILE_STRING = FILE_INPUT | STRING_OUTPUT;

    /** Indicates file to writer mode. */
    private static final int FILE_WRITER = FILE_INPUT | WRITER_OUTPUT;

    /** Indicates string to file mode. */
    private static final int STRING_FILE = STRING_INPUT | FILE_OUTPUT;

    /** Indicates string to string mode. */
    private static final int STRING_STRING = STRING_INPUT | STRING_OUTPUT;

    /** Indicates string to writer mode. */
    private static final int STRING_WRITER = STRING_INPUT | WRITER_OUTPUT;

    /** Indicates reader to file mode. */
    private static final int READER_FILE = READER_INPUT | FILE_OUTPUT;

    /** Indicates reader to string mode. */
    private static final int READER_STRING = READER_INPUT | STRING_OUTPUT;

    /** Indicates reader to writer mode. */
    private static final int READER_WRITER = READER_INPUT | WRITER_OUTPUT;

    static
    {
        setVersion(loadVersionString());
    }

    //~ Instance variables ---------------------------------------------------------------

    /** The code inspector. */
    private CodeInspector _inspector;

    /** Default backup directory. */
    private File _backupDir;

    /** The created backup file. */
    private File _backupFile;

    /** Destination directory to copy all formatted files into. */
    private File _destination;

    /** Input source file. */
    private File _inputFile;

    /** Output target file. */
    private File _outputFile;

    /** File format of the input stream. */
    private FileFormat _inputFileFormat;

    /** File format of the output stream. */
    private FileFormat _outputFileFormat;

    /** The last generated Java AST. */
    private JavaNode _tree;

    /** Our recognizer. */
    private final JavaRecognizer _recognizer;

    /**
     * Holds the issues found during inspection. Maps one node to either exactly one
     * issue or a list of issues.
     */
    private Map _issues; // Map of <JavaNode>:<Object>

    /** What history policy should be used? */
    private History.Policy _historyPolicy = History.Policy.DISABLED;

    /** Input source reader. */
    private Reader _inputReader;

    /** Appender which <em>spies</em> for logging events. */
    private final SpyAppender _spy;

    /** Run status. */
    private State _state = State.UNDEFINED;

    /**
     * The encoding to use for formatting. If <code>null</code> the platform's default
     * encoding will be used.
     */
    private String _encoding;

    /** The contents of the input source if specified as a STRING_INPUT. */
    private String _inputString;

    /** The package name of the current input source. */
    private String _packageName;

    /** Output target string. */
    private StringBuffer _outputString;

    /** Holds the result stream if we write to STRING_OUTPUT. */
    private StringWriter _outputStringBuffer;

    /** Output writer. */
    private Writer _outputWriter;

    /** Helper array to hold parameters used to format localized messages. */
    private Object[] _args = new Object[5];

    /** Should formatting be forced for files that are up to date? */
    private boolean _force;

    /** Don't delete backup files. */
    private boolean _holdBackup;

    /** Is the code inspector enabled. */
    private boolean _inspect;

    /** Number of backups to hold. */
    private int _backupLevel;

    /** I/O mode. */
    private int _mode;

    /** Used to update the modification date of output files. */
    private long _now;

    /** Holds the number of milliseconds used for parsing. */
    private long _timeParsing;

    /** Holds the number of milliseconds used for printing. */
    private long _timePrinting;

    /** Holds the number of milliseconds used for transforming. */
    private long _timeTransforming;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new Jalopy object.
     */
    public Jalopy()
    {
        initConventionDefaults();
        _issues = new HashMap(30);
        _recognizer = new JavaRecognizer();
        _inspector = new CodeInspector(_issues);
        _spy = new SpyAppender();
        Loggers.ALL.addAppender(_spy);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Indicates whether this version is a beta release.
     *
     * @return <code>true</code> if this version is a beta release.
     */
    public static boolean isBeta()
    {
        return _isBeta;
    }


    /**
     * Sets the code convention to be loaded from the given file (either a qualified file
     * path or single file name).
     *
     * @param file the code convention file.
     *
     * @throws IOException if no code convention could be loaded from the given file.
     */
    public static void setConvention(File file)
      throws IOException
    {
        Convention.getInstance().importSettings(file);
    }


    /**
     * Sets the code convention to be loaded from the given url.
     *
     * @param url url.
     *
     * @throws IOException if no code convention could be loaded from the given url.
     */
    public static void setConvention(URL url)
      throws IOException
    {
        Convention.getInstance().importSettings(url);
    }


    /**
     * Sets the code convention to be loaded from the given location string.
     *
     * @param location location. Either a local file pathname or a pointer to a
     *        distributed resource accessible via the HTTP protocol (that is starting
     *        with &quot;<code>http://</code>&quot; or &quot;<code>www.</code>&quot;).
     *
     * @throws IOException if no code convention could be loaded from the given location.
     */
    public static void setConvention(String location)
      throws IOException
    {
        if (
            location.startsWith("http://" /* NOI18N */)
            || location.startsWith("www." /* NOI18N */))
        {
            setConvention(new URL(location));
        }
        else
        {
            setConvention(new File(location));
        }
    }


    /**
     * Sets the encoding that controls how Jalopy interprets text files containing
     * characters beyond the ASCII character set.
     *
     * @param encoding a valid encoding name. For a list of valid encoding names refer to
     *        <a
     *        href="http://java.sun.com/products/jdk/1.4/docs/guide/intl/encoding.doc.html">Supported
     *        Encodings</a>. Note that <code>null</code> is permitted and indicates the
     *        platform's default encoding.
     *
     * @throws IllegalArgumentException if an invalid encoding was specified.
     */
    public void setEncoding(String encoding)
    {
        if (encoding != null)
        {
            try
            {
                new String(EMPTY_BYTE_ARRAY, encoding);
            }
            catch (UnsupportedEncodingException ex)
            {
                throw new IllegalArgumentException(
                    "invalid encoding specified -- " + encoding);
            }
        }

        _encoding = encoding;
    }


    /**
     * Sets the file format of the output stream. The file format controls what end of
     * line character is used for the output files.
     *
     * @param format file format to use.
     */
    public void setFileFormat(FileFormat format)
    {
        _outputFileFormat = format;
    }


    /**
     * Sets the file format of the output stream. The file format controls what end of
     * line character is used for the output files.
     *
     * @param format string representation of the file format to use.
     *
     * @see de.hunsicker.io.FileFormat#valueOf
     */
    public void setFileFormat(String format)
    {
        _outputFileFormat = FileFormat.valueOf(format);
    }


    /**
     * Specifies whether all files should be formatted no matter what the state of a file
     * is.
     *
     * <p>
     * Defaults to <code>false</code>, which means that a source file will be only
     * formatted if it hasn't ever been formatted before or if it has been modified
     * since the last time it was processed.
     * </p>
     *
     * @param force if <code>true</code> all files are always formatted.
     */
    public void setForce(boolean force)
    {
        _force = force;
    }


    /**
     * Sets the history policy to use.
     *
     * @param policy history policy.
     */
    public void setHistoryPolicy(History.Policy policy)
    {
        _historyPolicy = policy;
    }


    /**
     * Enables or disables the code inspector during formatting runs. You can always
     * perform inspection using {@link #inspect}.
     *
     * @param enabled if <code>true</code> the code inspector will be enabled.
     *
     * @since 1.0b8
     */
    public void setInspect(boolean enabled)
    {
        _inspect = enabled;
    }


    /**
     * Determines wether the code inspector is enabled during formatting runs.
     *
     * @return <code>true</code> if the code inspector is enabled.
     *
     * @since 1.0b8
     */
    public boolean isInspect()
    {
        return _inspect;
    }


    /**
     * Returns the major version number.
     *
     * @return major version.
     */
    public static byte getMajorVersion()
    {
        return _majorVersion;
    }


    /**
     * Returns the micro version number.
     *
     * @return micro version.
     */
    public static byte getMicroVersion()
    {
        return _microVersion;
    }


    /**
     * Returns the minor version number.
     *
     * @return minor version.
     */
    public static byte getMinorVersion()
    {
        return _minorVersion;
    }


    /**
     * Returns the version number.
     *
     * @return the current version number.
     */
    public static String getVersion()
    {
        return _version;
    }


    /**
     * Sets the output target to use.
     *
     * @param output writer to use as output target.
     */
    public void setOutput(Writer output)
    {
        _outputWriter = output;
        _mode += WRITER_OUTPUT;
    }


    /**
     * Returns a string with the elapsed times for the different profiling categories.
     * Purely a diagnostic method only useful during developing.
     *
     * @return string with rudimentary profiling information.
     */
    public String getProfileTimes()
    {
        long whole = _timeParsing + _timeTransforming + _timePrinting;

        if (whole > 0)
        {
            StringBuffer buf = new StringBuffer(100);
            buf.append(_timeParsing);
            buf.append('(');
            buf.append((_timeParsing * 100) / whole);
            buf.append("%) ");
            buf.append(_timeTransforming);
            buf.append('(');
            buf.append((_timeTransforming * 100) / whole);
            buf.append("%) ");
            buf.append(_timePrinting);
            buf.append('(');
            buf.append((_timePrinting * 100) / whole);
            buf.append("%)");

            return buf.toString();
        }

        return "";
    }


    /**
     * Returns the current state info.
     *
     * @return The current state.
     */
    public State getState()
    {
        return _state;
    }

    /**
     * Returns the used Java recognizer.
     *
     * @return the recognizer that is used for the language processing.
     *
     * @since 1.0b9
     */
    public JavaRecognizer getRecognizer()
    {
        return _recognizer;
    }

    /**
     * Checks whether the specification version of the given Plug-in is compatible with
     * the Jalopy Plug-in API spec version.
     *
     * @param packageName the package name of a Plug-in as specified in the Jar Manifest, e.g.
     *        &quot;de.hunsicker.jalopy.plugin.ant&quot;.
     *
     * @throws VersionMismatchException if the Plug-in with the given package name is not
     *         compatible with the Plug-in API version of the Jalopy runtime.
     *
     * @since 1.0b8
     */
    public static void checkCompatibility(String packageName)
      throws VersionMismatchException
    {
        /*// make sure the Plug-in package is known to the class loader
        // (necessary for those Plug-ins that don't make use of the
        // Plug-in API like the Ant and Console Plug-in)
        if (Package.getPackage("de.hunsicker.jalopy.plugin") == null)
        {
            try
            {
                // load a class from the Plug-in package so it becomes known
                // to the classloader
                Helper.loadClass("de.hunsicker.jalopy.plugin.StatusBar",
                                 Jalopy.class);
            }
            catch (Throwable ex)
            {
                return;
            }
        }

        Package runtimePackage = Package.getPackage(
                                         "de.hunsicker.jalopy.plugin");

        if (runtimePackage == null) // not loaded from jar
        {
            return;
        }

        Package pluginPackage = Package.getPackage(packageName);

        if (pluginPackage == null) // not loaded from jar
        {
            return;
        }

        if (!pluginPackage.isCompatibleWith(
                     runtimePackage.getSpecificationVersion()))
        {
            throw new VersionMismatchException(runtimePackage.getSpecificationVersion(),
                                               pluginPackage.getSpecificationVersion());
        }*/
    }


    /**
     * Sets whether to hold a backup copy of an input file. Defaults to
     * <code>true</code>.
     *
     * <p>
     * This switch only takes action if you specify the same file for both input and
     * output.
     * </p>
     *
     * <p>
     * Note that you can specify how many backups should be retained, in case you want a
     * history. See {@link #setBackupLevel} for further information.
     * </p>
     *
     * @param backup if <code>true</code> the backup of an input file will not be deleted
     *        after the run.
     *
     * @see #setBackupLevel
     * @see #setInput(File)
     * @see #setOutput(File)
     */
    public void setBackup(boolean backup)
    {
        _holdBackup = backup;
    }


    /**
     * Sets the directory where backup files will be stored.
     *
     * @param directory path to an existing directory.
     *
     * @throws IllegalArgumentException if the given file does not denote a valid
     *         directory.
     *
     * @see #setBackup
     */
    public void setBackupDirectory(File directory)
    {
        if (!directory.isAbsolute())
        {
            directory =
                new File(Convention.getProjectSettingsDirectory(), directory.getPath());
        }

        IoHelper.ensureDirectoryExists(directory.getAbsoluteFile());

        if (!directory.exists() || !directory.isDirectory())
        {
            throw new IllegalArgumentException("invalid directory -- " + directory);
        }

        _backupDir = directory.getAbsoluteFile();
    }


    /**
     * Sets the directory where backup files will be stored. Invokes {@link
     * #setBackupDirectory(File)} with <code>newFile(directory)</code>.
     *
     * @param directory path to an existing directory.
     *
     * @see #setBackup
     * @see #setBackupDirectory
     */
    public void setBackupDirectory(String directory)
    {
        setBackupDirectory(new File(directory));
    }


    /**
     * Returns the directory where file backups will be stored.
     *
     * @return the backup directory.
     *
     * @see #setBackup
     */
    public File getBackupDirectory()
    {
        return _backupDir;
    }


    /**
     * Sets the number of backups to hold. A value of <code>0</code> means to hold no
     * backup at all (same as {@link #setBackup setBackup(false)}). The default is
     * <code>1</code>.
     *
     * @param level number of backups to hold.
     *
     * @throws IllegalArgumentException if <code><em>level</em> &lt; 0</code>
     *
     * @see #setBackup
     */
    public void setBackupLevel(int level)
    {
        if (level < 0)
        {
            throw new IllegalArgumentException("level has to be >= 0");
        }

        _backupLevel = level;

        if (level == 0)
        {
            setBackup(false);
        }
    }


    /**
     * Sets the destination directory to create all formatting output into. This setting
     * then lasts until you either specify another directory or {@link #reset} was
     * called (which results in deleting the destination, files are overwritten now on).
     *
     * <p>
     * If the given destination does not exist, it will be created.
     * </p>
     *
     * <p>
     * Only applies if a file output target was specified.
     * </p>
     *
     * @param destination destination directory.
     *
     * @throws IllegalArgumentException if <em>destination</em> is <code>null</code> or
     *         does not denote a directory.
     * @throws RuntimeException if the destination directory could not be created.
     *
     * @see #setOutput(File)
     */
    public void setDestination(File destination)
    {
        if ((destination == null) || (destination.exists() && !destination.isDirectory()))
        {
            throw new IllegalArgumentException("no valid directory -- " + destination);
        }

        if (!destination.exists())
        {
            if (!destination.mkdirs())
            {
                throw new RuntimeException(
                    "could not create destination directory -- " + destination);
            }

            Object[] args = { destination };
            Loggers.IO.l7dlog(
                Level.INFO, "FILE_DESTINATION_CREATED" /* NOI18N */, args, null);
        }

        _destination = destination;
    }


    /**
     * Sets the input source to use.
     *
     * @param input string to use as input source.
     * @param path path of the file that is to be processed.
     *
     * @throws NullPointerException if <code><em>input</em> == null</code> or <code><em>path</em> ==
     *         null</code>
     */
    public void setInput(
        String input,
        String path)
    {
        if (input == null)
        {
            throw new NullPointerException();
        }

        if (path == null)
        {
            throw new NullPointerException();
        }

        _inputFile = new File(path);
        _inputString = input;
        _inputReader = new BufferedReader(new StringReader(input));

        if (!hasInput())
        {
            _mode += STRING_INPUT;
        }
    }


    /**
     * Sets the input source to use.
     *
     * @param input stream to use as input source.
     * @param path path of file that is to be processed.
     *
     * @throws IllegalArgumentException if <code><em>path</em> == null</code> or if <em>path</em>
     *         does not denote a valid, i.e. existing file or the system input stream.
     */
    public void setInput(
        InputStream input,
        String      path)
    {
        // ignore empty input
        if (input == null)
        {
            /**
             * @todo Loggers.IO.l7dlog(Level.INFO, "", _args, null);
             */
            return;
        }

        if (path == null)
        {
            throw new IllegalArgumentException("no path given");
        }

        File file = new File(path);

        if ((!file.exists() || !file.isFile()) && (System.in != input))
        {
            throw new IllegalArgumentException("invalid path given -- " + path);
        }

        _inputFile = new File(path);
        _inputReader = new BufferedReader(new InputStreamReader(input));

        if (!hasInput())
        {
            _mode += READER_INPUT;
        }
    }


    /**
     * Sets the input source to use.
     *
     * @param input reader to use as input source.
     * @param path path of file that is to be processed.
     *
     * @throws IllegalArgumentException if <code><em>path</em> == null</code> or if <em>path</em>
     *         does not denote a valid, i.e. existing file.
     */
    public void setInput(
        Reader input,
        String path)
    {
        if (path == null)
        {
            throw new IllegalArgumentException("no path given");
        }

        // ignore empty input
        if (input == null)
        {
            /**
             * @todo Loggers.IO.l7dlog(Level.INFO, "", _args, null);
             */
            return;
        }

        File file = new File(path);

        if (!file.exists() || !file.isFile())
        {
            throw new IllegalArgumentException("invalid path given -- " + path);
        }

        _inputFile = new File(path);
        _inputReader = input;

        if (!hasInput())
        {
            _mode += READER_INPUT;
        }
    }


    /**
     * Sets the input source to use.
     *
     * @param input file to use as input source.
     *
     * @throws FileNotFoundException if the specified source file does not exist.
     *
     * @see #setInput(Reader, String)
     * @see #setInput(String, String)
     */
    public void setInput(File input)
      throws FileNotFoundException
    {
        _inputReader = new BufferedReader(new FileReader(input));
        _inputFile = input.getAbsoluteFile();

        if (!hasInput())
        {
            _mode += FILE_INPUT;
        }
    }


    /**
     * Sets the output target to use.
     *
     * @param output file to use as output target.
     */
    public void setOutput(File output)
    {
        // we don't create our output writer here, because it is possible that
        // both input and output file are equal which means we have to make
        // a backup copy prior to the processing, but we create the copy
        // only after parsing was successful to avoid unnecessary I/O activity.
        // So we delegate the creation of the stream to the {@link #parse}
        // method
        _outputFile = output;
        _mode += FILE_OUTPUT;
    }


    /**
     * Sets the output target to use.
     *
     * @param output buffer to use as output target.
     */
    public void setOutput(StringBuffer output)
    {
        _outputString = output;
        _outputStringBuffer = new StringWriter();
        _outputWriter = new BufferedWriter(_outputStringBuffer);
        _mode += STRING_OUTPUT;
    }


    /**
     * Cleans up the backup directory. All empty directories will be deleted. Only takes
     * affect if no backup copies should be kept.
     *
     * @see #setBackup
     * @since 1.0b9
     */
    public void cleanupBackupDirectory()
    {
        if (!_holdBackup)
        {
            cleanupDirectory(_backupDir);
        }
    }


    /**
     * Formats the (via {@link #setInput(File)}) specified input source and writes the
     * formatted result to the specified target.
     *
     * <p>
     * Formatting a file means that {@link #parse parsing}, {@link #inspect inspecting}
     * and printing will be performed in sequence depending on the current state. Thus
     * the parsing and/or inspection phase may be skipped.
     * </p>
     *
     * <p>
     * It is safe to call this method multiple times after you've first constructed an
     * instance: just set new input/output targets and go with it. But remember that
     * this class is thread-hostile: accessing the class concurrently from multiple
     * threads will lead to unsuspected results.
     * </p>
     *
     * @see #setInput(File)
     * @see #setOutput(File)
     * @see #parse
     * @see #inspect
     */
    public void format()
    {
        JavaNode tree = null;

        try
        {
            // only process the file if necessary
            if (!isDirty())
            {
                _args[0] = _inputFile;
                Loggers.IO.l7dlog(
                    Level.INFO, "FILE_FOUND_HISTORY" /* NOI18N */, _args, null);
                _state = State.OK;
                cleanup();

                return;
            }

            if ((_state != State.PARSED) || (_state != State.INSPECTED))
            {
                tree = parse();

                if (_state == State.ERROR)
                {
                    cleanup();

                    return;
                }
                else
                {
                    /**
                     * @todo we need to reset the line info for the recognizer!!!
                     */
                }
            }
            else
            {
                tree = _tree;
            }
        }
        catch (Throwable ex)
        {
            _state = State.ERROR;
            _args[0] = _inputFile;
            _args[1] =
                (ex.getMessage() == null) ? ex.getClass().getName()
                                          : ex.getMessage();
            Loggers.IO.l7dlog(Level.ERROR, "UNKNOWN_ERROR" /* NOI18N */, _args, ex);
        }

        format(tree, _packageName, _inputFileFormat, false);
    }


    /*
     * Formats the given Java AST and writes the result to the specified
     * target.
     *
     * @param tree root node of the JavaAST that is to be formatted.
     *
     * @since 1.0b8
     * @see #setOutput(File)
     * @see #parse
     * @see #inspect
     *
    public void format(JavaNode tree)
    {
        format(tree, true);
    }*/

    /**
     * Inspects the (via {@link #setInput(File)}) specified input source for code
     * convention violations and coding weaknesses. If no parsing was performed yet, the
     * input source will first be parsed.
     *
     * @see #setInput(File)
     * @see #parse
     * @since 1.0b8
     */
    public void inspect()
    {
        JavaNode tree = null;

        if (_state != State.PARSED)
        {
            try
            {
                tree = parse();

                if (_state == State.ERROR)
                {
                    return;
                }
            }
            catch (Throwable ex)
            {
                _state = State.ERROR;
                _args[0] = _inputFile;
                _args[1] =
                    (ex.getMessage() == null) ? ex.getClass().getName()
                                              : ex.getMessage();
                Loggers.IO.l7dlog(Level.ERROR, "UNKNOWN_ERROR" /* NOI18N */, _args, ex);
            }
        }
        else
        {
            tree = _tree;
        }

        inspect(tree);
    }


    /**
     * Inspects the given Java AST for code convention violations and coding weaknesses.
     *
     * @param tree root node of the Java AST that is to be inspected.
     *
     * @see #parse
     * @since 1.0b8
     */
    public void inspect(JavaNode tree)
    {
        long start = 0;

        if (Loggers.IO.isDebugEnabled())
        {
            start = System.currentTimeMillis();
            _args[0] = _inputFile;
            Loggers.IO.l7dlog(Level.DEBUG, "FILE_INSPECT" /* NOI18N */, _args, null);
        }

        _inspector.inspect(tree, (_outputFile != null) ? _outputFile
                                                       : _inputFile);

        if (Loggers.IO.isDebugEnabled())
        {
            long stop = System.currentTimeMillis();
            Loggers.IO.debug(_inputFile + ":0:0:inspecting took " + (stop - start));
        }

        if (_state != State.ERROR)
        {
            _state = State.INSPECTED;
        }
    }


    /**
     * Parses the (via {@link #setInput(File)}) specified input source. You should always
     * check the state after parsing, to be sure the input source could be successfully
     * parsed.
     *
     * @return The root node of the created Java AST. May or may not return
     *         <code>null</code> if the input source could not be successfully parsed
     *         (i.e. always use {@link #getState} to check for success).
     *
     * @see #setInput(File)
     * @see #getState
     * @since 1.0b8
     */
    public JavaNode parse()
    {
        long start = 0;
        _state = State.RUNNING;

        if (Loggers.IO.isDebugEnabled())
        {
            start = System.currentTimeMillis();
        }

        try
        {
            switch (_mode)
            {
                case FILE_INPUT :
                case FILE_FILE :
                case FILE_STRING :
                case FILE_WRITER :
                    _args[0] = _inputFile;
                    Loggers.IO.l7dlog(
                        Level.INFO, "FILE_PARSE" /* NOI18N */, _args, null);
                    _recognizer.parse(_inputFile);

                    break;

                case STRING_INPUT :
                case READER_INPUT :
                case STRING_FILE :
                case READER_FILE :
                case STRING_STRING :
                case STRING_WRITER :
                case READER_STRING :
                case READER_WRITER :
                    _args[0] = _inputFile;
                    Loggers.IO.l7dlog(
                        Level.INFO, "FILE_PARSE" /* NOI18N */, _args, null);
                    _recognizer.parse(_inputReader, _inputFile.getAbsolutePath());

                    break;

                default :
                    throw new IllegalStateException("no input source specified");
            }

            if (_state == State.ERROR)
            {
                return null;
            }

            if (Loggers.IO.isDebugEnabled())
            {
                long stop = System.currentTimeMillis();
                Loggers.IO.debug(
                    _inputFile.getAbsolutePath() + ":0:0:parsing took " + (stop - start));
                _timeParsing += (stop - start);
            }

            if (_state != State.ERROR)
            {
                _state = State.PARSED;
            }

            JavaNode tree = null;

            if (Loggers.IO.isDebugEnabled())
            {
                Loggers.IO.debug(
                    ((_outputFile != null) ? _outputFile
                                           : _inputFile) + ":0:0:transform");
                start = System.currentTimeMillis();
                tree = (JavaNode) _recognizer.getParseTree();

                long stop = System.currentTimeMillis();
                _timeTransforming += (stop - start);
                Loggers.IO.debug(
                    ((_outputFile != null) ? _outputFile
                                           : _inputFile) + ":0:0:transforming took "
                    + (stop - start));
            }
            else
            {
                tree = (JavaNode) _recognizer.getParseTree();
            }

            _tree = tree;
            _inputFileFormat = _recognizer.getFileFormat();
            _packageName = _recognizer.getPackageName();

            return tree;
        }
        finally
        {
            cleanupRecognizer();
        }
    }


    /**
     * Resets this instance.
     *
     * <p>
     * Note that this method is not meant to be invoked after every call of {@link
     * #format}, but rather serves as a way to reset this instance to exactly the state
     * directly after the object creation.
     * </p>
     */
    public void reset()
    {
        cleanup();
        initConventionDefaults();
    }


    /**
     * Resets the profiling timers.
     */
    void resetTimers()
    {
        _timeParsing = 0;
        _timePrinting = 0;
        _timeTransforming = 0;
    }


    /**
     * Sets the current version information.
     *
     * @param version string with encoded version information.
     *
     * @throws IllegalArgumentException if <em>version</em> is not a valid version
     *         string.
     *
     * @since 1.0b8
     */
    private static void setVersion(String version)
    {
        _version = version;

        Perl5Util regexp = new Perl5Util();

        /**
         * @todo user Matcher.matches
         */
        if (regexp.match("m/(\\d).(\\d)(?:.(\\d+))?(b\\d+)?/", version))
        {
            MatchResult result = regexp.getMatch();
            _majorVersion = (byte) Integer.parseInt(result.group(1));
            _minorVersion = (byte) Integer.parseInt(result.group(2));

            String betaString = result.group(4);

            if (betaString != null)
            {
                _isBeta = true;
                _microVersion = (byte) Integer.parseInt(result.group(3));
            }
            else
            {
                String microString = result.group(3);

                if (microString != null)
                {
                    if (microString.indexOf('b') > -1)
                    {
                        _isBeta = true;
                    }
                    else
                    {
                        _microVersion = (byte) Integer.parseInt(result.group(3));
                    }
                }
            }
        }
        else
        {
            throw new IllegalArgumentException(
                "invalid version information -- " + version);
        }
    }


    /**
     * Cleans up the given directory. All empty subdirectories will be removed.
     *
     * @param directory directory to cleanup.
     *
     * @since 1.0b9
     */
    private void cleanupDirectory(File directory)
    {
        File[] files = directory.listFiles();

        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isDirectory())
                {
                    if (files[i].list().length > 0)
                    {
                        cleanupDirectory(files[i]);
                    }
                    else
                    {
                        if (files[i].delete())
                        {
                            File parent = directory.getParentFile();

                            if (!directory.equals(_backupDir))
                            {
                                cleanupDirectory(parent);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Formats the given Java AST and writes the result to the specified target.
     *
     * @param tree root node of the JavaAST that is to be formatted.
     * @param packageName the package name of the tree.
     * @param format the detected file format of the Java source file represented by the
     *        tree.
     * @param check if <code>true</code> the method checks if the source file of the
     *        given tree actually needs reformatting and omitts further processing if
     *        so.
     *
     * @since 1.0b8
     */
    private void format(
        JavaNode   tree,
        String     packageName,
        FileFormat format,
        boolean    check)
    {
        String defaultEncoding = null;

        try
        {
            _args[0] = _inputFile;

            // only process the file if necessary
            if (check && !isDirty())
            {
                Loggers.IO.l7dlog(
                    Level.INFO, "FILE_FOUND_HISTORY" /* NOI18N */, _args, null);
                _state = State.OK;

                return;
            }

            if (_encoding != null)
            {
                // store the current default encoding
                defaultEncoding = System.getProperty("file.encoding" /* NOI18N */);

                // now set the encoding to use by Jalopy
                System.setProperty("file.encoding" /* NOI18N */, _encoding);
            }

            if (_inspect)
            {
                inspect(tree);
            }

            // it seems ok to print the AST
            print(tree, packageName, format);

            if (_state == State.ERROR)
            {
                // don't forget to restore the original file, if needed
                restore(_inputFile, _backupFile);

                return;
            }

            if (_outputStringBuffer != null)
            {
                _outputString.setLength(0);
                _outputString.append(_outputStringBuffer.toString());
            }

            if (_outputFile != null)
            {
                // we have to release the file locks prior to changing the
                // timestamp
                _inputReader.close();
                _outputWriter.close();

                // update the timestamp of the file with our 'magic' stamp
                _outputFile.setLastModified(_now);
            }

            // delete the backup if the user don't want backup copies
            if (!_holdBackup && (_backupFile != null))
            {
                _backupFile.delete();

                if (Loggers.IO.isDebugEnabled())
                {
                    _args[0] = _inputFile;
                    _args[1] = _backupFile;
                    Loggers.IO.l7dlog(
                        Level.DEBUG, "FILE_BACKUP_REMOVE" /* NOI18N */, _args, null);
                }
            }

            // update the status information if necessary
            if (
                (_state == State.PARSED) || (_state == State.INSPECTED)
                || (_state == State.RUNNING))
            {
                // no error or warnings occured, all ok
                _state = State.OK;
            }
        }
        catch (Throwable ex)
        {
            _state = State.ERROR;
            _args[0] = _inputFile;
            _args[1] =
                (ex.getMessage() == null) ? ex.getClass().getName()
                                          : ex.getMessage();
            Loggers.IO.l7dlog(Level.ERROR, "UNKNOWN_ERROR" /* NOI18N */, _args, ex);
            restore(_inputFile, _backupFile);
        }
        finally
        {
            if (defaultEncoding != null)
            {
                // restore the default encoding
                System.setProperty("file.encoding" /* NOI18N */, defaultEncoding);
            }

            cleanup();
        }
    }


    /**
     * Determines whether an input source was already set.
     *
     * @return <code>true</code> if an input source was already set.
     *
     * @since 1.0b8
     */
    private boolean hasInput()
    {
        switch (_mode)
        {
            case STRING_INPUT :
            case FILE_INPUT :
            case READER_INPUT :
            case FILE_FILE :
            case FILE_STRING :
            case FILE_WRITER :
            case STRING_FILE :
            case STRING_STRING :
            case STRING_WRITER :
            case READER_FILE :
            case READER_STRING :
            case READER_WRITER :
                return true;
        }

        return false;
    }


    /**
     * Determines whether an output source was already set.
     *
     * @return <code>true</code> if an output source was already set.
     *
     * @since 1.0b8
     */
    private boolean hasOutput()
    {
        switch (_mode)
        {
            case STRING_OUTPUT :
            case FILE_OUTPUT :
            case WRITER_OUTPUT :
            case FILE_FILE :
            case FILE_STRING :
            case FILE_WRITER :
            case STRING_FILE :
            case STRING_STRING :
            case STRING_WRITER :
            case READER_FILE :
            case READER_STRING :
            case READER_WRITER :
                return true;
        }

        return false;
    }


    /**
     * Extracts the version information out of the package manifest.
     *
     * @return The found version string.
     *
     * @since 1.0b8
     */
    private static String loadVersionString()
    {
        return "1.0b9";

        /*Package pkg = Package.getPackage("de.hunsicker.jalopy");


        if (pkg == null)
        {
            throw new RuntimeException(
                    "could not find package de.hunsicker.jalopy");
        }

        String version = pkg.getImplementationVersion();

        if (version == null)
        {
            throw new RuntimeException(
                    "no implementation version string found in package manifest");
        }

        return version;*/
    }


    /**
     * Returns the destination file for the given target. If the directory where the file
     * should reside does not exist it will be created.
     *
     * @param destination destination directory.
     * @param packageName package name of the file.
     * @param filename filename of the file.
     *
     * @return the destination file.
     *
     * @throws IOException if the target directory could not be created.
     */
    private File getDestinationFile(
        File   destination,
        String packageName,
        String filename)
      throws IOException
    {
        StringBuffer buf = new StringBuffer(90);
        buf.append(destination);
        buf.append(File.separator);
        buf.append(packageName.replace('.', File.separatorChar));

        File test = new File(buf.toString());

        if (!test.exists())
        {
            if (!test.mkdirs())
            {
                throw new IOException("could not create target directory -- " + buf);
            }
            else
            {
                if (Loggers.IO.isDebugEnabled())
                {
                    Loggers.IO.debug("directory " + test + " created");
                }
            }
        }

        buf.append(File.separator);
        buf.append(filename);

        return new File(buf.toString());
    }


    /**
     * Indicates whether the input file is <em>dirty</em>. <em>Dirty</em> means that the
     * file needs to be formatted.
     *
     * <p>
     * Use {@link #setForce setForce(true)} to always force a formatting of the file.
     * </p>
     *
     * @return <code>true</code> if the input file is dirty. If the user specified an
     *         input stream or input string, this method always returns
     *         <code>true</code> as we cannot determine the last modification of the
     *         input source in such cases.
     *
     * @throws IOException if an I/O error occured.
     */
    private boolean isDirty()
      throws IOException
    {
        if (_force) // the user forces formatting
        {
            return true;
        }

        // no input file means we're processing a non-file input. This
        // generally the case if we're format an editor view in a graphical
        // application. As the user may revert the formatting, we assume
        // processing is always necessary
        if ((_mode & FILE_INPUT) == 0)
        {
            // but only if the input is not empty
            return _inputReader != null;
        }

        // it doesn't make much sense to format a non-existing or empty file
        if (!_inputFile.exists() || (_inputFile.length() == 0))
        {
            return false;
        }

        if (_historyPolicy == History.Policy.FILE)
        {
            History.Entry entry = History.getInstance().get(_inputFile);

            if (entry != null)
            {
                // the input file is up-to-date
                if (entry.getModification() >= _inputFile.lastModified())
                {
                    if (_destination != null)
                    {
                        copyInputToOutput(
                            _inputFile, _destination, entry.getPackageName(),
                            entry.getModification());
                    }

                    return false;
                }
            }

            return true;
        }
        else if (_historyPolicy == History.Policy.COMMENT)
        {
            BufferedReader in = null;

            try
            {
                in = new BufferedReader(new FileReader(_inputFile));

                String line = in.readLine().trim();
                in.close();

                // we only check the very first line
                if (
                    line.startsWith("// %") && line.endsWith("%")
                    && (line.indexOf("modified") == -1))
                {
                    int start = line.indexOf('%') + 1;
                    int stop = line.indexOf(':');
                    long lastmod = Long.parseLong(line.substring(start, stop));

                    // the input file is up-to-date
                    if (lastmod >= _inputFile.lastModified())
                    {
                        if (_destination != null)
                        {
                            String packageName =
                                line.substring(stop + 1, line.length() - 1);
                            copyInputToOutput(
                                _inputFile, _destination, packageName, lastmod);
                        }

                        return false;
                    }
                }

                return true;
            }
            finally
            {
                if (in != null)
                {
                    in.close();
                }
            }
        }
        else
        {
            return true;
        }
    }


    /**
     * Returns the line separator for the given file format.
     *
     * @param fileFormat the user specified file format.
     * @param detectedFileFormat the detected file format.
     *
     * @return line separator. Either one of &quot;\n&quot;, &quot;\r\n&quot; or
     *         &quot;\r&quot;.
     */
    private String getLineSeparator(
        FileFormat fileFormat,
        FileFormat detectedFileFormat)
    {
        if (fileFormat == FileFormat.AUTO)
        {
            return detectedFileFormat.toString();
        }

        return fileFormat.getLineSeparator();
    }


    /**
     * Sets the local macro variables.
     *
     * @param environment DOCUMENT ME!
     * @param file file that is to be printed.
     * @param packageName package name of the file.
     * @param fileFormat fileFormat to use.
     * @param indentSize number of spaces to use for indentation.
     *
     * @since 1.0b8
     */
    private void setLocalVariables(
        Environment environment,
        File        file,
        String      packageName,
        String      fileFormat,
        int         indentSize)
    {
        environment.set(Environment.Variable.FILE_NAME.getName(), file.getName());
        environment.set(Environment.Variable.FILE.getName(), file.getAbsolutePath());
        environment.set(
            Environment.Variable.PACKAGE.getName(),
            "".equals(packageName) ? "default package"
                                   : packageName);
        environment.set(Environment.Variable.FILE_FORMAT.getName(), fileFormat);
        environment.set(
            Environment.Variable.TAB_SIZE.getName(), String.valueOf(indentSize));
    }


    /**
     * Determines whether the given file can be modified.
     *
     * @param file file to test.
     *
     * @return <code>true</code> if the given file exists and can modified or if the file
     *         does <strong>not</strong> exist.
     */
    private boolean isWritable(File file)
    {
        if (file != null)
        {
            // we cannot determine whether the file can be modified, so we
            // have to assume true
            if (!file.exists())
            {
                return true;
            }
            else
            {
                return file.canWrite();
            }
        }

        return false;
    }


    /**
     * Outputs the comment history header to the given stream (if set via {@link
     * #setInput(File)}).
     *
     * @param packageName the package name of the Java source file.
     * @param out stream to write to.
     *
     * @throws IOException if the input source could not be added to the history.
     */
    private void addCommentHistoryEntry(
        String     packageName,
        NodeWriter out)
      throws IOException
    {
        if ((_historyPolicy == History.Policy.COMMENT) && (_inputFile != null))
        {
            StringBuffer buf = new StringBuffer(40);
            buf.append("// %");
            buf.append(_now);
            buf.append(':');
            buf.append(packageName);
            buf.append('%');
            out.print(buf.toString(), JavaTokenTypes.SL_COMMENT);
            out.printNewline();
        }
    }


    /**
     * Adds the last processed input source to the file history (if set via {@link
     * #setInput(File)}).
     *
     * @param packageName the package name of the Java source file.
     *
     * @throws IOException if the input source could not be added to the history.
     */
    private void addFileHistoryEntry(String packageName)
      throws IOException
    {
        if ((_historyPolicy == History.Policy.FILE) && (_inputFile != null))
        {
            History.getInstance().add(_inputFile, packageName, _now);
        }
    }


    /**
     * Performs needed cleanup. Resets the recognizer and frees ressources.
     */
    private void cleanup()
    {
        try
        {
            if (_inputReader != null)
            {
                _inputReader.close();
                _inputReader = null;
            }
        }
        catch (IOException ignored)
        {
            ;
        }

        try
        {
            if (_outputWriter != null)
            {
                _outputWriter.close();
                _outputWriter = null;
            }
        }
        catch (IOException ignored)
        {
            ;
        }

        _mode = ILLEGAL;
        _issues.clear();
        _inputFile = null;
        _inputString = null;
        _outputStringBuffer = null;
        _outputString = null;
        _outputFile = null;
        _backupFile = null;
        _packageName = null;
        _inputFileFormat = null;
        _tree = null;

        cleanupRecognizer();
    }


    /**
     * Resets the Java recognizer.
     *
     * @since 1.0b8
     */
    private void cleanupRecognizer()
    {
        _recognizer.reset();
    }


    /**
     * In case the given input file is up-to-date but the user specified a certain output
     * destination target, this method copies the input file into the destination
     * directory.
     *
     * @param inputFile the input file for which formatting is not necessary.
     * @param destination the directory to copy all output into.
     * @param packageName package name of the input file.
     * @param lastmod the time the input file was last formatted.
     *
     * @throws IOException if an I/O error occured.
     */
    private void copyInputToOutput(
        File   inputFile,
        File   destination,
        String packageName,
        long   lastmod)
      throws IOException
    {
        File file = getDestinationFile(destination, packageName, inputFile.getName());

        if (!file.exists() || (file.lastModified() != lastmod))
        {
            Copy.file(inputFile, file, true);
            file.setLastModified(lastmod);
            _args[0] = inputFile;
            _args[1] = file.getAbsolutePath();
            Loggers.IO.l7dlog(Level.INFO, "FILE_COPY" /* NOI18N */, _args, null);
        }
    }


    /**
     * Creates depending on the type of the input source a backup file.
     *
     * @param packageName packageName of the file currently being parsed.
     *
     * @return the backup file if both input and target source are files and equal.
     *         Returns <code>null</code> for all other cases.
     *
     * @throws IOException if an I/O error occured.
     */
    private File createBackup(String packageName)
      throws IOException
    {
        switch (_mode)
        {
            case FILE_FILE :

                if (_inputFile.equals(_outputFile))
                {
                    IoHelper.ensureDirectoryExists(_backupDir);

                    File directory =
                        new File(
                            _backupDir + File.separator
                            + packageName.replace('.', File.separatorChar));
                    File backupFile =
                        FileBackup.create(_inputFile, directory, _backupLevel);

                    if (Loggers.IO.isDebugEnabled())
                    {
                        _args[1] = backupFile;
                        Loggers.IO.l7dlog(
                            Level.DEBUG, "FILE_COPY" /* NOI18N */, _args, null);
                    }

                    return backupFile;
                }

                break;

            case STRING_STRING :
            case STRING_FILE :
            case STRING_WRITER :

                if (_inputFile.exists())
                {
                    IoHelper.ensureDirectoryExists(_backupDir);

                    File directory =
                        new File(
                            _backupDir + File.separator
                            + packageName.replace('.', File.separatorChar));
                    String filename = _inputFile.getName();
                    File backupFile =
                        FileBackup.create(
                            _inputString, filename, directory, _backupLevel);

                    if (Loggers.IO.isDebugEnabled())
                    {
                        _args[1] = backupFile;
                        Loggers.IO.l7dlog(
                            Level.DEBUG, "FILE_COPY" /* NOI18N */, _args, null);
                    }

                    return backupFile;
                }

                break;

            case READER_STRING :
            case READER_FILE :
            case READER_WRITER :

                /**
                 * @todo implement
                 */
                break;
        }

        return null;
    }


    /**
     * Initializes the default startup values.
     */
    private void initConventionDefaults()
    {
        _backupDir = Convention.getBackupDirectory();
        _backupLevel = ConventionDefaults.BACKUP_LEVEL;
        _holdBackup = false;
        _state = State.UNDEFINED;
        _outputFileFormat = FileFormat.UNKNOWN;
        _destination = null; // all files are overwritten
        _encoding = null; // use platform default encoding
    }


    /**
     * Prints the generated AST to the given output source. The tree will only be printed
     * if no errors we're found during parsing.
     *
     * @param tree root node of the Java AST.
     * @param packageName the package name of the tree.
     * @param format the detected file format of the Java source file represented by the
     *        tree.
     *
     * @throws IOException if an I/O error occured.
     * @throws IllegalStateException if either one of input source or output target was
     *         not specified.
     */
    private void print(
        JavaNode   tree,
        String     packageName,
        FileFormat format)
      throws IOException
    {
        if (_state == State.ERROR)
        {
            return;
        }

        switch (_mode)
        {
            case FILE_FILE :

                // only create a backup if input and output file are equal
                if (_destination == null)
                {
                    _backupFile = createBackup(packageName);
                }
                else
                {
                    // create the target file
                    _outputFile =
                        getDestinationFile(
                            _destination, packageName, _outputFile.getName());
                }

                if (!isWritable(_outputFile))
                {
                    _args[0] = _outputFile.getAbsolutePath();
                    _outputFile = null;
                    Loggers.IO.l7dlog(
                        Level.WARN, "FILE_NO_WRITE" /* NOI18N */, _args, null);

                    return;
                }

                _outputWriter = new BufferedWriter(new FileWriter(_outputFile));

                break;

            case FILE_STRING :
            case FILE_WRITER :
                _backupFile = createBackup(packageName);

                break;

            case STRING_FILE :
            case READER_FILE :

                if (_destination != null)
                {
                    // specify the target file
                    _outputFile =
                        getDestinationFile(
                            _destination, packageName, _outputFile.getName());
                }

                if (!isWritable(_outputFile))
                {
                    _args[0] = _outputFile.getAbsolutePath();
                    _outputFile = null;
                    Loggers.IO.l7dlog(
                        Level.WARN, "FILE_NO_WRITE" /* NOI18N */, _args, null);

                    return;
                }

                _outputWriter = new BufferedWriter(new FileWriter(_outputFile));

            // fall through
            case STRING_STRING :
            case STRING_WRITER :
            case READER_STRING :
            case READER_WRITER :
                _backupFile = createBackup(packageName);

                break;

            default :
                throw new IllegalStateException(
                    "both input source and output target has to be specified");
        }

        _now = System.currentTimeMillis();

        NodeWriter out =
            new NodeWriter(
                _outputWriter, _inputFile.getAbsolutePath(), _issues,
                getLineSeparator(_outputFileFormat, format), format.toString());

        out.setAnnotation(_recognizer.hasAnnotations());

        Environment environment = Environment.getInstance().copy();
        setLocalVariables(
            environment, _inputFile, packageName, _outputFileFormat.getName(),
            out.getIndentSize());
        out.setEnvironment(environment);
        addCommentHistoryEntry(packageName, out);

        try
        {
            if (Loggers.IO.isDebugEnabled())
            {
                Loggers.IO.debug(
                    ((_outputFile != null) ? _outputFile
                                           : _inputFile) + ":0:0:print");

                long start = System.currentTimeMillis();
                PrinterFactory.create(tree).print(tree, out);

                long stop = System.currentTimeMillis();
                _timePrinting += (stop - start);
                Loggers.IO.debug(
                    ((_outputFile != null) ? _outputFile
                                           : _inputFile) + ":0:0:printing took "
                    + (stop - start));
            }
            else
            {
                PrinterFactory.create(tree).print(tree, out);
            }

            addFileHistoryEntry(packageName);
        }
        finally
        {
            unsetLocalVariables(environment);

            if (out != null)
            {
                out.close();
            }
        }
    }


    /**
     * Restores the original file.
     *
     * @param original original file
     * @param backup backup of the original file.
     */
    private void restore(
        File original,
        File backup)
    {
        // check if we're in FILE_FILE mode (both source and target are files)
        if ((original != null) && (backup != null))
        {
            _args[0] = original.getAbsolutePath();
            _args[1] = backup.getAbsolutePath();
            Loggers.IO.l7dlog(Level.INFO, "FILE_RESTORE" /* NOI18N */, _args, null);

            try
            {
                Copy.file(backup, original, true);
                original.setLastModified(backup.lastModified());
                backup.delete();
            }
            catch (IOException ex)
            {
                Loggers.IO.l7dlog(
                    Level.FATAL, "FILE_RESTORE_ERROR" /* NOI18N */, _args, ex);
            }
        }
    }


    /**
     * Unsets the local macro variables.
     *
     * @param environment DOCUMENT ME!
     *
     * @since 1.0b8
     */
    private void unsetLocalVariables(Environment environment)
    {
        environment.unset(Environment.Variable.FILE_NAME.getName());
        environment.unset(Environment.Variable.FILE.getName());
        environment.unset(Environment.Variable.PACKAGE.getName());
        environment.unset(Environment.Variable.FILE_FORMAT.getName());
        environment.unset(Environment.Variable.TAB_SIZE.getName());
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * Represents a Jalopy run state. You may want to use {@link Jalopy#getState()} to
     * query the engine about its current state.
     *
     * @since 1.0b8
     */
    public static final class State
    {
        /** Indicates a finished run without any warnings or errors. */
        public static final State OK = new State("Jalopy.State [ok]" /* NOI18N */);

        /** Indicates a finished run which produced warnings. */
        public static final State WARN = new State("Jalopy.State [warn]" /* NOI18N */);

        /** Indicates a finished run, that failed. */
        public static final State ERROR = new State("Jalopy.State [error]" /* NOI18N */);

        /** Indicates a successful parse phase. */
        public static final State PARSED =
            new State("Jalopy.State [parsed]" /* NOI18N */);

        /** Indicates a successful inspection phase. */
        public static final State INSPECTED =
            new State("Jalopy.State [inspected]" /* NOI18N */);

        /** Indicates the running state (no phase yet finished). */
        public static final State RUNNING =
            new State("Jalopy.State [running]" /* NOI18N */);

        /** Indicates the undefined state (a run was not yet startet). */
        public static final State UNDEFINED =
            new State("Jalopy.State [undefined]" /* NOI18N */);

        /** The name of the state. */
        final String name;

        /**
         * Creates a new State object.
         *
         * @param name name of the state.
         */
        private State(String name)
        {
            this.name = name;
        }

        /**
         * Returns a string representation of this state.
         *
         * @return a string representation of this state.
         */
        public String toString()
        {
            return this.name;
        }
    }


    /**
     * Detects whether and what kind of messages were produced during a run. Updates the
     * state info accordingly.
     */
    private final class SpyAppender
        extends AppenderSkeleton
    {
        public SpyAppender()
        {
            this.name = "JalopySpyAppender" /* NOI18N */;
        }

        public void append(LoggingEvent ev)
        {
            switch (ev.getLevel().toInt())
            {
                case Level.WARN_INT :

                    if (_state != State.ERROR)
                    {
                        _state = State.WARN;
                    }

                    break;

                case Level.ERROR_INT :
                case Level.FATAL_INT :
                    _state = State.ERROR;

                    break;
            }
        }


        public void close()
        {
        }


        public synchronized void doAppend(LoggingEvent ev)
        {
            append(ev);
        }


        public boolean requiresLayout()
        {
            return false;
        }


        protected boolean checkEntryConditions()
        {
            return true;
        }
    }
}
