/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.borland.jbuilder.JBuilderMenu;
import com.borland.jbuilder.node.JBProject;
import com.borland.jbuilder.node.JavaFileNode;
import com.borland.jbuilder.node.PackageNode;
import com.borland.jbuilder.paths.JDKPathSet;
import com.borland.jbuilder.paths.PathSet;
import com.borland.jbuilder.paths.PathSetManager;
import com.borland.jbuilder.paths.ProjectPathSet;
import com.borland.primetime.PrimeTime;
import com.borland.primetime.actions.ActionGroup;
import com.borland.primetime.actions.UpdateAction;
import com.borland.primetime.editor.EditorAction;
import com.borland.primetime.editor.EditorActions;
import com.borland.primetime.editor.EditorContextActionProvider;
import com.borland.primetime.editor.EditorManager;
import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.ide.BrowserAction;
import com.borland.primetime.ide.BrowserAdapter;
import com.borland.primetime.ide.ContentManager;
import com.borland.primetime.ide.ContextActionProvider;
import com.borland.primetime.ide.ProjectView;
import com.borland.primetime.node.FolderNode;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.ProjectListener;
import com.borland.primetime.util.VetoException;
import com.borland.primetime.vfs.Url;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.language.ClassRepository;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;
import de.hunsicker.jalopy.plugin.StatusBar;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.jalopy.storage.ImportPolicy;
import de.hunsicker.jalopy.storage.Loggers;
import de.hunsicker.jalopy.swing.SettingsDialog;
import de.hunsicker.swing.ErrorDialog;
import de.hunsicker.util.StringHelper;


/**
 * The Jalopy JBuilder Plug-in. This is the OpenTools entry point.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class JbPlugin
    extends AbstractPlugin
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The content type for Java source files. */
    private static final String CONTENT_TYPE_JAVA = "text/java" /* NOI18N */;

    /** A bundle that holds localized message strings. */
    private static final ResourceBundle BUNDLE =
        ResourceBundle.getBundle(
            "de.hunsicker.jalopy.plugin.jbuilder.Bundle" /* NOI18N */,
            Convention.getInstance().getLocale());

    /** Our sole instance. */
    private static JbPlugin _instance;

    /** The currently active project. */
    private static JBProject _curProject;

    /** Number of project files in the active project. */
    private static int _numFiles;

    /** Directory to compile classes to. */
    private static File _outputDir;

    /** Helper array. */
    private static final Object[] _args = new Object[1];

    /** Our log4j appender. */
    private static JbAppender _appender;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbPlugin object.
     */
    private JbPlugin()
    {
        super(_appender);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Project getActiveProject()
    {
        return new JbProject(
            Browser.getActiveBrowser().getProjectView().getActiveProject());
    }


    /**
     * Returns the active status bar.
     *
     * @return the active status bar.
     */
    public StatusBar getStatusBar()
    {
        return JbStatusBar.getInstance();
    }


    /**
     * Returns the version information of this OpenTool.
     *
     * @return version information of this OpenTool.
     */
    public static String getVersion()
    {
        return Package.getPackage("de.hunsicker.jalopy.plugin.jbuilder" /* NOI18N */)
                      .getImplementationVersion();
    }


    /**
     * {@inheritDoc}
     */
    public void afterEnd()
    {
        super.afterEnd();

        if (getLastAction() == Action.FORMAT_ACTIVE)
        {
            Browser browser = Browser.getActiveBrowser();
            EditorPane editor = EditorManager.getEditor(browser.getActiveNode());

            if (editor != null)
            {
                editor.endUndoGroup();
            }
        }
    }


    /**
     * Initializes this OpenTool.
     *
     * @param major major release number for which this OpenTool is being initialized.
     * @param minor minor release number for which this OpenTool is being initialized.
     */
    public static void initOpenTool(
        byte major,
        byte minor)
    {
        if (major != PrimeTime.CURRENT_MAJOR_VERSION)
        {
            Object[] args =
            {
                String.valueOf(PrimeTime.CURRENT_MAJOR_VERSION),
                String.valueOf(PrimeTime.CURRENT_MINOR_VERSION), String.valueOf(major),
                String.valueOf(minor)
            };

            System.err.println(
                MessageFormat.format(
                    BUNDLE.getString("MSG_ERROR_LOADING" /* NOI18N */), args));

            return;
        }

        _appender = new JbAppender();
        Loggers.initialize(_appender);

        FormatSingleAction formatSingleAction = new FormatSingleAction();

        JBuilderMenu.GROUP_ProjectBuild.add(formatSingleAction);
        EditorManager.registerContextActionProvider(formatSingleAction);
        EditorManager.getKeymap().addActionForKeyStroke(
            KeyStroke.getKeyStroke(KeyEvent.VK_F10, Event.CTRL_MASK | Event.SHIFT_MASK),
            formatSingleAction);
        EditorActions.addBindableEditorAction(formatSingleAction);

        ProjectView.registerContextActionProvider(new ProjectContextActionProvider());

        SettingsAction displaySettingsAction = new SettingsAction();
        getToolsGroup().add(2, displaySettingsAction);

        EditorManager.registerContextActionProvider(displaySettingsAction);

        ContentManager.registerContextActionProvider(
            new ContentContextActionProvider(formatSingleAction));

        Browser.addStaticBrowserListener(new BrowserHandler());

        System.out.println(
            "Jalopy Java Source Code Formatter " /* NOI18N */ + Jalopy.getVersion());
        System.out.println("JBuilder OpenTool " /* NOI18N */ + getVersion());
        System.out.println(
            "Copyright (c) 2001-2002 Marco Hunsicker. All rights reserved." /* NOI18N */);
    }


    /**
     * {@inheritDoc}
     */
    public Frame getMainWindow()
    {
        return Browser.getActiveBrowser();
    }


    /**
     * {@inheritDoc}
     */
    protected FileFormat getFileFormat()
    {
        return decodeFileFormat(
            Browser.getActiveBrowser().getActiveProject().getProperty(
                "editor.general" /* NOI18N */, "line_ending.style" /* NOI18N */, null));
    }


    /**
     * Determines whether the import optimization feature is enabled.
     *
     * @return <code>true</code> if the import optimization feature is enabled.
     *
     * @since 1.0b8
     */
    static boolean isImportOptimizationEnabled()
    {
        ImportPolicy importPolicy =
            ImportPolicy.valueOf(
                Convention.getInstance().get(
                    ConventionKeys.IMPORT_POLICY, ConventionDefaults.IMPORT_POLICY));

        if (
            (importPolicy == ImportPolicy.EXPAND)
            || (importPolicy == ImportPolicy.COLLAPSE))
        {
            return true;
        }

        return false;
    }


    /**
     * Updates the class repository if Java source files have bean added/removed. Used by
     * the build handlers.
     */
    static void updateRepository()
    {
        int numFiles = getProjectFiles().size();

        // works for both additions/removals
        if (numFiles != _numFiles)
        {
            try
            {
                // trigger the update of the repository
                ClassRepository.getInstance().load(_outputDir);

                // now it is save to update our counter
                _numFiles = numFiles;
            }
            catch (Throwable ex)
            {
                ErrorDialog dialog = ErrorDialog.create(Browser.getActiveBrowser(), ex);
                dialog.setVisible(true);
            }
        }
    }


    private static List getFilePaths(Url[] paths)
    {
        if ((paths == null) || (paths.length == 0))
        {
            return Collections.EMPTY_LIST;
        }

        List locations = new ArrayList(paths.length);

        for (int i = 0; i < paths.length; i++)
        {
            locations.add(paths[i].getFileObject());
        }

        return locations;
    }


    private static JbPlugin getInstance()
    {
        if (_instance == null)
        {
            _instance = new JbPlugin();
        }

        return _instance;
    }


    /**
     * Returns all Java source files that make up the currently active project.
     *
     * @return project files. Returns an empty collection if no source files could be
     *         found which means that either no active project is available or the
     *         project doesn't contains any source code files.
     */
    private static Collection getProjectFiles()
    {
        if (_curProject == null)
        {
            return Collections.EMPTY_LIST;
        }

        return new JbProject(_curProject).getAllFiles();
    }


    /**
     * Returns the ActionGroup for the <code>Tools</code> menu item.
     *
     * @return the ActionGroup for the <code>Tools</code> menu item.
     */
    private static ActionGroup getToolsGroup()
    {
        ActionGroup toolGroup = null;
        ActionGroup[] groups = Browser.getMenuGroups();

        if (
            (PrimeTime.CURRENT_MAJOR_VERSION == 4)
            && (PrimeTime.CURRENT_MINOR_VERSION > 1))
        {
            toolGroup = JBuilderMenu.GROUP_ToolsOptions;
        }
        else
        {
            // ugly workaround for a JBuilder 4.0 incompatibility
            for (int i = 0; i < groups.length; i++)
            {
                if ("Tools" /* NOI18N */.equals(groups[i].getShortText()))
                {
                    toolGroup = (ActionGroup) groups[i].getAction(0);

                    break;
                }
            }
        }

        return toolGroup;
    }


    /**
     * Checks whether all libraries as defined in the project properties, are valid (i.e.
     * all library path sets contain at least one classpath entry, either a directory or
     * an archive).
     *
     * @param project the current project.
     *
     * @return <code>true</code> if the library definition is valid.
     *
     * @since 1.0b8
     */
    private static boolean checkClassPath(JBProject project)
    {
        String libraries = project.getProperty(project.PROPERTY_LIBRARIES);

        for (
            StringTokenizer i = new StringTokenizer(libraries, ";" /* NOI18N */);
            i.hasMoreElements();)
        {
            String library = i.nextToken();
            PathSet set = PathSetManager.getLibrary(library);

            if (set.isEmpty())
            {
                Object[] args =
                { library, BUNDLE.getString("TAB_REQUIRED_LIBRARIES" /* NOI18N */) };
                JOptionPane.showMessageDialog(
                    Browser.getActiveBrowser(),
                    MessageFormat.format(
                        BUNDLE.getString("MSG_ERROR_EMPTY_LIBRARY" /* NOI18N */), args),
                    BUNDLE.getString("TLE_FORMATTING_IMPOSSIBLE" /* NOI18N */),
                    JOptionPane.ERROR_MESSAGE);

                return false;
            }
            else
            {
                Url[] urls = set.getClassPath();
                List locations = getFilePaths(urls);

                for (int j = 0, size = locations.size(); j < size; j++)
                {
                    File location = (File) locations.get(j);

                    Object[] args =
                    {
                        library, location,
                        BUNDLE.getString("TAB_REQUIRED_LIBRARIES" /* NOI18N */)
                    };

                    if (!location.exists())
                    {
                        JOptionPane.showMessageDialog(
                            Browser.getActiveBrowser(),
                            MessageFormat.format(
                                BUNDLE.getString("MSG_ERROR_INVALID_PATH" /* NOI18N */),
                                args),
                            BUNDLE.getString("TLE_FORMATTING_IMPOSSIBLE" /* NOI18N */),
                            JOptionPane.ERROR_MESSAGE);

                        return false;
                    }
                }
            }
        }

        return true;
    }


    /**
     * Decodes the file format string of the JBuilder editor properties as a valid Jalopy
     * file format.
     *
     * @param fileformat the JBuilder editor line ending style property
     *        (<code>editor.general.line_ending.style</code>)
     *
     * @return file format.
     */
    private FileFormat decodeFileFormat(String fileformat)
    {
        if (fileformat == null)
        {
            return FileFormat.AUTO;
        }

        if (fileformat.equals("1" /* NOI18N */))
        {
            return FileFormat.DEFAULT;
        }

        if (fileformat.equals("2" /* NOI18N */))
        {
            return FileFormat.DOS;
        }

        if (fileformat.equals("3" /* NOI18N */))
        {
            return FileFormat.UNIX;
        }

        if (fileformat.equals("4" /* NOI18N */))
        {
            return FileFormat.MAC;
        }

        return FileFormat.DEFAULT;
    }


    /**
     * Initializes our menu and context actions.
     */
    private void initActions()
    {
    }

    //~ Inner Classes --------------------------------------------------------------------

    private static final class BrowserHandler
        extends BrowserAdapter
    {
        /** Holds the already monitored projects. */
        List projects = new ArrayList(); // List of <com.borland.primetime.node.Project>
        ProjectListener listener = new ProjectHandler();

        /** Is the browser currently closing? */
        boolean closing;

        /** Is the browser already visible? */
        boolean opened;

        public void browserClosed(Browser browser)
        {
            this.closing = false;
            this.opened = false;
        }


        public void browserClosing(Browser browser)
          throws VetoException
        {
            this.closing = true;
        }


        public void browserOpened(Browser browser)
        {
            this.opened = true;
            browserProjectActivated(browser, browser.getProjectView().getActiveProject());
        }


        public void browserProjectActivated(
            final Browser                      browser,
            com.borland.primetime.node.Project project)
        {
            if ((project == null) || (!(this.opened)) || this.closing)
            {
                return;
            }

            String path =
                project.getProperty(
                    "sys" /* NOI18N */, "OutPath" /* NOI18N */,
                    "build/classes" /* NOI18N */);

            path = StringHelper.replace(path, "%S", ";");
            path = StringHelper.replace(path, "%|", ":");
            _outputDir = new File(path);

            if (!_outputDir.exists())
            {
                _outputDir = new File(project.getProjectPath().getFileObject(), path);
            }

            // keep track of the current project
            _curProject = (JBProject) project;

            if (isImportOptimizationEnabled())
            {
                Thread updater = new UpdateThreadZwei(null, _curProject);
                updater.start();
            }
        }


        public void browserProjectClosed(
            Browser                            browser,
            com.borland.primetime.node.Project project)
        {
            project.removeProjectListener(this.listener);
            this.projects.remove(project);

            if (this.projects.size() == 0)
            {
                unloadProject((JBProject) project, new Url[0]);
            }
        }


        private void unloadProject(
            JBProject currentProject,
            Url[]     newLibraries)
        {
            if (currentProject == null)
            {
                return;
            }

            ClassRepository repository = ClassRepository.getInstance();

            if (repository.isEmpty())
            {
                return;
            }

            ProjectPathSet paths = currentProject.getPaths();
            Url[] urls = paths.getFullClassPath();
            Arrays.sort(urls);

            List locations = new ArrayList(urls.length);

            for (int i = 0; i < urls.length; i++)
            {
                // no need to unload
                if (Arrays.binarySearch(newLibraries, urls[i]) > -1)
                {
                    continue;
                }

                locations.add(urls[i].getFileObject());
            }

            try
            {
                repository.unloadAll(locations);
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        /**
         * Used to update the class repository for the active project.
         */
        private final class UpdateThreadZwei
            extends Thread
        {
            JBProject project;
            JDialog dialog;

            UpdateThreadZwei(
                JDialog   dialog,
                JBProject project)
            {
                this.dialog = dialog;
                this.project = project;
            }

            public void run()
            {
                ProjectPathSet paths = this.project.getPaths();
                Url[] urls = paths.getFullClassPath();

                // unload the libraries not needed anymore
                unloadProject(_curProject, urls);

                ClassRepository repository = ClassRepository.getInstance();
                List locations = getFilePaths(urls);
                locations.add(_outputDir);

                try
                {
                    repository.loadAll(locations);
                }
                catch (IOException ex)
                {
                    try
                    {
                        // this may not be necessary, but just to be sure
                        repository.unloadAll(locations);
                    }
                    catch (IOException ignored)
                    {
                        ;
                    }

                    Object[] args = { ex.getMessage() };

                    JOptionPane.showMessageDialog(
                        Browser.getActiveBrowser(),
                        MessageFormat.format(
                            BUNDLE.getString("MSG_ERROR_LIBRARY_NOT_FOUND" /* NOI18N */),
                            args), BUNDLE.getString("TLE_LIBRARY_NOT_FOUND" /* NOI18N */),
                        JOptionPane.ERROR_MESSAGE);
                }

                _numFiles = getProjectFiles().size();

                if (!BrowserHandler.this.projects.contains(this.project))
                {
                    this.project.addProjectListener(BrowserHandler.this.listener);
                    BrowserHandler.this.projects.add(this.project);
                }
            }
        }
    }


    /**
     * Provides the context menu action for the Content Pane.
     */
    private static final class ContentContextActionProvider
        implements ContextActionProvider
    {
        javax.swing.Action action;

        public ContentContextActionProvider(javax.swing.Action action)
        {
            this.action = action;
        }

        public javax.swing.Action getContextAction(
            Browser browser,
            Node[]  nodes)
        {
            javax.swing.Action result = null;

            if (nodes.length == 1)
            {
                if (nodes[0] instanceof JavaFileNode)
                {
                    result = this.action;
                }
            }

            return result;
        }
    }


    /**
     * Formats the project files currently selected in the project view.
     */
    private static final class FormatMultiAction
        extends BrowserAction
    {
        /**
         * Creates a new FormatMultiAction object.
         */
        public FormatMultiAction()
        {
            super("project-format" /* NOI18N */);

            putValue("ActionGroup" /* NOI18N */, "Build" /* NOI18N */);
            putValue(
                BrowserAction.SHORT_DESCRIPTION,
                BUNDLE.getString("MNE_FORMAT" /* NOI18N */));
            putValue(
                BrowserAction.LONG_DESCRIPTION,
                BUNDLE.getString("MNE_FORMAT_FILES_DESCRIPTION" /* NOI18N */));
        }

        public void actionPerformed(Browser browser)
        {
            Node node = browser.getProjectView().getSelectedNode();

            if (node instanceof JBProject)
            {
                switch (JOptionPane.showConfirmDialog(
                    Browser.getActiveBrowser(),
                    BUNDLE.getString("MSG_CONFIRM_FORMAT_PROJECT" /* NOI18N */),
                    BUNDLE.getString("TLE_CONFIRM_FORMAT_PROJECT" /* NOI18N */),
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE))
                {
                    case JOptionPane.CANCEL_OPTION :
                    case JOptionPane.CLOSED_OPTION :
                        return;
                }
            }

            if (isImportOptimizationEnabled())
            {
                if (!checkClassPath(_curProject))
                {
                    return;
                }

                if (ClassRepository.getInstance().isEmpty())
                {
                    /*Thread updater =  new UpdateThreadZwei(null, _curProject);
                    updater.start();*/
                }
            }

            // we want to update all selected nodes
            getInstance().performAction(Action.FORMAT_SELECTED);
        }
    }


    /**
     * Formats the currently active editor window content.
     */
    private static final class FormatSingleAction
        extends EditorAction
        implements EditorContextActionProvider
    {
        /**
         * Creates a new FormatSingleAction object.
         */
        public FormatSingleAction()
        {
            super("file-format" /* NOI18N */);
            putValue("ActionGroup" /* NOI18N */, "Build" /* NOI18N */);
            putValue(
                BrowserAction.LONG_DESCRIPTION,
                BUNDLE.getString("MNE_FORMAT_FILE_DESCRIPTION" /* NOI18N */));
            putValue(UpdateAction.MNEMONIC, new Character('t'));
            putValue(
                UpdateAction.ACCELERATOR,
                KeyStroke.getKeyStroke(
                    KeyEvent.VK_F10, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        }

        public javax.swing.Action getContextAction(EditorPane editor)
        {
            if (CONTENT_TYPE_JAVA.equals(editor.getContentType()))
            {
                return this;
            }
            else
            {
                return null;
            }
        }


        /**
         * Determines whether this action is enabled.
         *
         * @return <code>true</code> if the action is enabled.
         */
        public boolean isEnabled()
        {
            ProjectFile file = getInstance().getActiveProject().getActiveFile();

            if (file == null)
            {
                putValue(
                    BrowserAction.SHORT_DESCRIPTION,
                    BUNDLE.getString("MNE_FORMAT" /* NOI18N */));

                return false;
            }
            else
            {
                _args[0] = file.getName();

                putValue(
                    BrowserAction.SHORT_DESCRIPTION,
                    MessageFormat.format(
                        BUNDLE.getString("MNE_FORMAT_FILE" /* NOI18N */), _args));

                return true;
            }
        }


        public int getPriority()
        {
            return Integer.MAX_VALUE;
        }


        /**
         * Gets one of this object's properties using the associated key.
         *
         * @param key a string containing the specified key.
         *
         * @return the binding Object stored with this key. If there are no keys, it will
         *         return <code>null</code>.
         */
        public Object getValue(String key)
        {
            if (key.equals(BrowserAction.SHORT_DESCRIPTION))
            {
                ProjectFile file = getInstance().getActiveProject().getActiveFile();

                if (file == null)
                {
                    return BUNDLE.getString("MNE_FORMAT" /* NOI18N */);
                }
                else
                {
                    _args[0] = file.getName();

                    return MessageFormat.format(
                        BUNDLE.getString("MNE_FORMAT_FILE" /* NOI18N */), _args);
                }
            }

            return super.getValue(key);
        }


        /**
         * Invoked when an action occurs.
         *
         * @param ev the event which caused the action.
         */
        public void actionPerformed(ActionEvent ev)
        {
            EditorManager.getEditor(Browser.getActiveBrowser().getActiveNode())
                         .startUndoGroup();

            if (isImportOptimizationEnabled())
            {
                if (!checkClassPath(_curProject))
                {
                    return;
                }

                if (ClassRepository.getInstance().isEmpty())
                {
                    ;
                }
            }

            // we only want to update the active file
            getInstance().performAction(Action.FORMAT_ACTIVE);
        }
    }


    /**
     * Provides the context menu action for the project view.
     */
    private static final class ProjectContextActionProvider
        implements ContextActionProvider
    {
        FormatMultiAction action;

        public javax.swing.Action getContextAction(
            Browser browser,
            Node[]  nodes)
        {
            javax.swing.Action result = null;

            if (nodes.length == 1) // format one file or folder
            {
                if (nodes[0] instanceof JavaFileNode)
                {
                    _args[0] = nodes[0].getDisplayName();
                    result = getContextAction();
                    result.putValue(
                        BrowserAction.SHORT_DESCRIPTION,
                        MessageFormat.format(
                            BUNDLE.getString("MNE_FORMAT_FILE" /* NOI18N */), _args));
                    result.putValue(
                        BrowserAction.LONG_DESCRIPTION,
                        BUNDLE.getString("MNE_FORMAT_FILE_DESCRIPTION" /* NOI18N */));
                }
                else if (
                    nodes[0] instanceof PackageNode || nodes[0] instanceof FolderNode)
                {
                    result = getContextAction();
                    result.putValue(
                        BrowserAction.SHORT_DESCRIPTION,
                        BUNDLE.getString("MNE_FORMAT" /* NOI18N */));
                    result.putValue(
                        BrowserAction.LONG_DESCRIPTION,
                        BUNDLE.getString("MNE_FORMAT_FOLDER_DESCRIPTION" /* NOI18N */));
                }
                else if (nodes[0] instanceof JBProject)
                {
                    _args[0] = nodes[0].getDisplayName();

                    result = getContextAction();
                    result.putValue(
                        BrowserAction.SHORT_DESCRIPTION,
                        MessageFormat.format(
                            BUNDLE.getString("MNE_FORMAT_PROJECT" /* NOI18N */), _args));
                    result.putValue(
                        BrowserAction.LONG_DESCRIPTION,
                        BUNDLE.getString("MNE_FORMAT_PROJECT_DESCRIPTION" /* NOI18N */));
                }
            }
            else if (nodes.length == 0)
            {
                // nothing selected, nothing to do
            }
            else // format selected nodes
            {
                result = getContextAction();
                result.putValue(
                    BrowserAction.SHORT_DESCRIPTION,
                    BUNDLE.getString("MNE_FORMAT" /* NOI18N */));
                result.putValue(
                    BrowserAction.LONG_DESCRIPTION,
                    BUNDLE.getString("MNE_FORMAT_FILES_DESCRIPTION" /* NOI18N */));
            }

            return result;
        }


        private javax.swing.Action getContextAction()
        {
            if (this.action == null)
            {
                this.action = new FormatMultiAction();
            }

            return this.action;
        }
    }


    /**
     * Used to update the class repository if the user changes project settings.
     */
    private static final class ProjectHandler
        implements ProjectListener
    {
        public void nodeChanged(
            com.borland.primetime.node.Project project,
            Node                               node)
        {
            _numFiles = 0;
        }


        public void nodeChildrenChanged(
            com.borland.primetime.node.Project project,
            Node                               parent)
        {
        }


        public void projectPropertyChanged(
            com.borland.primetime.node.Project project,
            String                             category,
            String                             property,
            String                             oldValue,
            String                             newValue)
        {
            if (
                isImportOptimizationEnabled() && "sys" /* NOI18N */.equals(category)
                && "Libraries" /* NOI18N */.equals(property))
            {
                List oldUrls = new ArrayList();
                List newUrls = new ArrayList();

                for (
                    StringTokenizer tokens =
                        new StringTokenizer(oldValue, ";" /* NOI18N */);
                    tokens.hasMoreTokens();)
                {
                    oldUrls.add(tokens.nextToken());
                }

                for (
                    StringTokenizer tokens =
                        new StringTokenizer(newValue, ";" /* NOI18N */);
                    tokens.hasMoreTokens();)
                {
                    newUrls.add(tokens.nextToken());
                }

                List removed = new ArrayList();

                for (int i = 0, size = oldUrls.size(); i < size; i++)
                {
                    String url = (String) oldUrls.get(i);

                    if (newUrls.contains(url))
                    {
                        continue;
                    }
                    else
                    {
                        removed.add(url);
                    }
                }

                List added = new ArrayList();

                ClassRepository repository = ClassRepository.getInstance();

                for (int i = 0, size = newUrls.size(); i < size; i++)
                {
                    String url = (String) newUrls.get(i);

                    if ((!repository.isEmpty()) && oldUrls.contains(url))
                    {
                        continue;
                    }
                    else
                    {
                        added.add(url);
                    }
                }

                Url[] unload = new Url[0];
                ProjectPathSet paths = ((JBProject) project).getPaths();

                for (int i = 0, size = removed.size(); i < size; i++)
                {
                    PathSet pathset = paths.getLibrary((String) removed.get(i));
                    Url[] urls = pathset.getFullClassPath();
                    Url[] temp = new Url[unload.length + urls.length];
                    System.arraycopy(unload, 0, temp, 0, unload.length);
                    System.arraycopy(urls, 0, temp, unload.length, urls.length);
                    unload = temp;
                }

                Url[] load = new Url[0];

                for (int i = 0, size = added.size(); i < size; i++)
                {
                    PathSet pathset = paths.getLibrary((String) added.get(i));
                    Url[] urls = pathset.getFullClassPath();
                    Url[] temp = new Url[load.length + urls.length];
                    System.arraycopy(load, 0, temp, 0, load.length);
                    System.arraycopy(urls, 0, temp, load.length, urls.length);
                    load = temp;
                }

                update(project, unload, load);
            }
            else if (
                "sys" /* NOI18N */.equals(category)
                && "JDK" /* NOI18N */.equals(property))
            {
                JDKPathSet oldpath = PathSetManager.getJDK(oldValue);
                JDKPathSet newpath = PathSetManager.getJDK(newValue);
                update(project, oldpath.getFullClassPath(), newpath.getFullClassPath());
            }
            else if (
                "sys" /* NOI18N */.equals(category)
                && "OutPath" /* NOI18N */.equals(property))
            {
                String projectPath =
                    project.getProjectPath().getFileObject() + File.separator;
                Url[] unload = { new Url(new File(projectPath + oldValue)) };
                Url[] load = { new Url(new File(projectPath + newValue)) };
                update(project, unload, load);
            }
        }


        /**
         * Loads the given libraries into the class repository.
         *
         * @param locations libraries to load.
         */
        private static void load(Url[] locations)
        {
            ClassRepository repository = ClassRepository.getInstance();
            List files = new ArrayList(locations.length);

            for (int j = 0; j < locations.length; j++)
            {
                File location = locations[j].getFileObject();

                if (location.exists())
                {
                    files.add(location);
                }
            }

            try
            {
                repository.loadAll(files);
            }
            catch (Exception ex)
            {
                /**
                 * @todo log error
                 */
                ex.printStackTrace();
            }
        }


        /**
         * Unloads the given libraries from the class repository.
         *
         * @param locations libraries to unload.
         */
        private static void unload(Url[] locations)
        {
            ClassRepository repository = ClassRepository.getInstance();

            if (repository.isEmpty())
            {
                return; // nothing to do
            }

            List files = new ArrayList(locations.length);

            for (int j = 0; j < locations.length; j++)
            {
                File location = locations[j].getFileObject();

                if (location.exists())
                {
                    files.add(location);
                }
            }

            files.add(_outputDir);

            try
            {
                repository.unloadAll(files);
            }
            catch (Exception ex)
            {
                /**
                 * @todo show error dialog, unload all libs to disable
                 */
                ex.printStackTrace();
            }
        }


        /**
         * Issues the updating of the class repository for the given project.
         *
         * @param project project which settings have been altered.
         * @param unload urls that are to be removed from the class repository.
         * @param load urls that are to be added to the class repository.
         */
        private void update(
            com.borland.primetime.node.Project project,
            Url[]                              unload,
            Url[]                              load)
        {
            Thread updater = new UpdateThreadDrei(null, unload, load);
            updater.start();
        }

        /**
         * Used to update the class repository after changes to the classpath.
         */
        private static class UpdateThreadDrei
            extends Thread
        {
            JDialog dialog;
            Url[] load;
            Url[] unload;

            UpdateThreadDrei(
                JDialog dialog,
                Url[]   unload,
                Url[]   load)
            {
                this.dialog = dialog;
                this.load = load;
                this.unload = unload;
            }

            public void run()
            {
                unload(this.unload);
                load(this.load);
            }
        }
    }


    /**
     * Displays the configuration dialog.
     */
    private static final class SettingsAction
        extends EditorAction
        implements EditorContextActionProvider
    {
        public SettingsAction()
        {
            super("config-jalopy" /* NOI18N */);

            putValue(
                BrowserAction.SMALL_ICON,
                new ImageIcon(
                    this.getClass().getResource(
                        "/de/hunsicker/jalopy/swing/resources/Preferences16.gif" /* NOI18N */)));
            putValue(
                BrowserAction.LONG_DESCRIPTION,
                BUNDLE.getString("MNE_OPTIONS_DESCRIPTION" /* NOI18N */));
            putValue(
                BrowserAction.SHORT_DESCRIPTION,
                BUNDLE.getString("MNE_OPTIONS" /* NOI18N */));
            putValue(UpdateAction.MNEMONIC, new Character('j'));
        }

        public javax.swing.Action getContextAction(EditorPane editor)
        {
            // only add the action for Java source files
            if (CONTENT_TYPE_JAVA.equals(editor.getContentType()))
            {
                return this;
            }
            else
            {
                return null;
            }
        }


        public int getPriority()
        {
            return 5;
        }


        /**
         * Invoked when an action occurs.
         *
         * @param ev the event which caused the action.
         */
        public void actionPerformed(ActionEvent ev)
        {
            SettingsDialog dialog =
                SettingsDialog.create(
                    Browser.getActiveBrowser(),
                    BUNDLE.getString("TLE_OPTIONS" /* NOI18N */));
            dialog.pack();
            dialog.setLocationRelativeTo(Browser.getActiveBrowser());
            dialog.setVisible(true);
        }
    }
}
