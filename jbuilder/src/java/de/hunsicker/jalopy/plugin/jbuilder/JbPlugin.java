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
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.jbuilder.JBuilderMenu;
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
import com.borland.primetime.ide.ContextActionProvider;
import com.borland.primetime.ide.ProjectView;
import com.borland.primetime.node.Node;
import com.borland.primetime.node.ProjectListener;
import com.borland.primetime.util.VetoException;
import com.borland.primetime.vfs.Url;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.parser.ClassRepository;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;
import de.hunsicker.jalopy.plugin.StatusBar;
import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.ImportPolicy;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.ui.SettingsDialog;
import de.hunsicker.ui.ErrorDialog;

import java.awt.Event;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/**
 * The Jalopy JBuilder Plug-in. This is the OpenTools entry point.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class JbPlugin
    extends AbstractPlugin
{
    //~ Static variables/initializers ·········································

    /** The content type for Java source files. */
    private static final String CONTENT_TYPE_JAVA = "text/java";

    /** Our sole instance. */
    private static final JbPlugin INSTANCE = new JbPlugin();

    /** The currently active project. */
    private static com.borland.jbuilder.node.JBProject _curProject;

    /** Number of project files in the active project. */
    private static int _numFiles;

    /** Directory to compile classes to. */
    private static String _outputDir;

    //~ Constructors ··························································

    /**
     * Creates a new JbPlugin object.
     */
    public JbPlugin()
    {
        super(new JbAppender());
        initActions();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public Project getActiveProject()
    {
        return new JbProject(Browser.getActiveBrowser().getProjectView()
                                    .getActiveProject());
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
     * Initializes the Plug-in.
     *
     * @param major major release number for which the OpenTool is being
     *        initialized.
     * @param minor minor release number for which the OpenTool is being
     *        initialized.
     */
    public static void initOpenTool(byte major,
                                    byte minor)
    {
        if (major != PrimeTime.CURRENT_MAJOR_VERSION)
        {
            System.err.println("Could not load Jalopy OpenTool. Need PrimeTime " +
                               PrimeTime.CURRENT_MAJOR_VERSION + "." +
                               PrimeTime.CURRENT_MINOR_VERSION + ", was " +
                               major + '.' + minor);

            return;
        }

        System.out.println("Jalopy Java Source Code Formatter " +
                           Jalopy.getVersion());
        System.out.println("Copyright (c) 2001, 2002 Marco Hunsicker ");
        System.out.println("Loading from " +
                           INSTANCE.getClass().getResource("JbPlugin.class"));
        Browser.addStaticBrowserListener(new BrowserHandler());
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
        return decodeFileFormat(Browser.getActiveBrowser().getActiveProject()
                                       .getProperty("editor.general",
                                                    "line_ending.style", null));
    }


    /**
     * Determines whether the import optimization feature is enabled.
     *
     * @return <code>true</code> if the import optimization feature is
     *         enabled.
     *
     * @since 1.0b8
     */
    static boolean isImportOptimizationEnabled()
    {
        ImportPolicy importPolicy = ImportPolicy.valueOf(Convention.getInstance()
                                                                    .get(Keys.IMPORT_POLICY,
                                                                         Defaults.IMPORT_POLICY));

        if ((importPolicy == ImportPolicy.EXPAND) ||
            (importPolicy == ImportPolicy.COLLAPSE))
        {
            return true;
        }

        return false;
    }


    /**
     * Updates the class repository if Java source files have bean
     * added/removed.
     *
     * @see ClassRepository#load
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
                ClassRepository.getInstance().load(new File(_outputDir));

                // now it is save to update our counter
                _numFiles = numFiles;
            }
            catch (Throwable ex)
            {
                ErrorDialog dialog = new ErrorDialog(ex,
                                                     Browser.getActiveBrowser());
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


    /**
     * Returns all Java source files that make up the currently active
     * project.
     *
     * @return project files. Returns an empty collection if no source files
     *         could be found which means that either no active project is
     *         available or the project doesn't contains any source code
     *         files.
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
    private ActionGroup getToolsGroup()
    {
        ActionGroup toolGroup = null;
        ActionGroup[] groups = Browser.getMenuGroups();

        if ((PrimeTime.CURRENT_MAJOR_VERSION == 4) &&
            (PrimeTime.CURRENT_MINOR_VERSION > 1))
        {
            toolGroup = JBuilderMenu.GROUP_ToolsOptions;
        }
        else
        {
            // ugly workaround for a JBuilder 4.0 incompatibility
            for (int i = 0; i < groups.length; i++)
            {
                if ("Tools".equals(groups[i].getShortText()))
                {
                    toolGroup = (ActionGroup)groups[i].getAction(0);

                    break;
                }
            }
        }

        return toolGroup;
    }


    /**
     * Checks whether all libraries as defined in the project properties, are
     * valid (i.e. all library path sets contain at least one classpath
     * entry, either a directory or an archive).
     *
     * @param project the current project.
     *
     * @return <code>true</code> if the library definition is valid.
     *
     * @since 1.0b8
     */
    private boolean checkClassPath(com.borland.jbuilder.node.JBProject project)
    {
        String libraries = project.getProperty(project.PROPERTY_LIBRARIES);

        for (StringTokenizer i = new StringTokenizer(libraries, ";");
             i.hasMoreElements();)
        {
            String library = i.nextToken();
            PathSet set = PathSetManager.getLibrary(library);

            if (set.isEmpty())
            {
                JOptionPane.showMessageDialog(INSTANCE.getMainWindow(),
                                              "The library \"" + library +
                                              "\" contains no paths. This most notably\n" +
                                              "means it has been removed from disk.\n\n" +
                                              "Check the settings in the \"Required Libraries\" tab in you project properties.\n",
                                              "Error: Formatting cannot be performed",
                                              JOptionPane.ERROR_MESSAGE);

                return false;
            }
            else
            {
                Url[] urls = set.getClassPath();
                List locations = getFilePaths(urls);

                for (int j = 0, size = locations.size(); j < size; j++)
                {
                    File location = (File)locations.get(j);

                    if (!location.exists())
                    {
                        JOptionPane.showMessageDialog(INSTANCE.getMainWindow(),
                                                      "The library \"" +
                                                      library +
                                                      "\" contains an invalid path.\n" +
                                                      "\"" + location +
                                                      "\" could not be found!\n\n" +
                                                      "Check the settings in the \"Required Libraries\" tab in you project properties.\n",
                                                      "Error: Formatting cannot be performed",
                                                      JOptionPane.ERROR_MESSAGE);

                        return false;
                    }
                }
            }
        }

        return true;
    }


    /**
     * Decodes the file format string of the JBuilder editor properties as a
     * valid Jalopy file format.
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

        if (fileformat.equals("1"))
        {
            return FileFormat.DEFAULT;
        }

        if (fileformat.equals("2"))
        {
            return FileFormat.DOS;
        }

        if (fileformat.equals("3"))
        {
            return FileFormat.UNIX;
        }

        if (fileformat.equals("4"))
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
        FormatSingleAction formatSingleAction = new FormatSingleAction();
        JBuilderMenu.GROUP_ProjectBuild.add(formatSingleAction);
        EditorManager.registerContextActionProvider(formatSingleAction);
        EditorManager.getKeymap()
                     .addActionForKeyStroke(KeyStroke.getKeyStroke(
                                                                   KeyEvent.VK_F10,
                                                                   Event.CTRL_MASK | Event.SHIFT_MASK),
                                            formatSingleAction);
        EditorActions.addBindableEditorAction(formatSingleAction);
        ProjectView.registerContextActionProvider(new JbContextActionProvider());

        SettingsAction displaySettingsAction = new SettingsAction();
        getToolsGroup().add(2, displaySettingsAction);
        EditorManager.registerContextActionProvider(displaySettingsAction);
    }

    //~ Inner Classes ·························································

    private static class BrowserHandler
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
        }


        public void browserClosing(Browser browser)
            throws VetoException
        {
            this.closing = true;
        }


        public void browserOpened(Browser browser)
        {
            this.opened = true;
            browserProjectActivated(browser,
                                    browser.getProjectView().getActiveProject());
        }


        public void browserProjectActivated(final Browser                      browser,
                                            com.borland.primetime.node.Project project)
        {
            if ((project == null) || (!(this.opened)) || this.closing)
            {
                return;
            }

            _outputDir = project.getProjectPath().getFileObject() +
                         File.separator +
                         project.getProperty("sys", "OutPath", "build/classes");

            Thread updater = new UpdateThread(null,
                                              (com.borland.jbuilder.node.JBProject)project);
            updater.start();
        }


        public void browserProjectClosed(Browser                            browser,
                                         com.borland.primetime.node.Project project)
        {
            project.removeProjectListener(this.listener);
            this.projects.remove(project);

            if (this.projects.size() == 0)
            {
                unloadProject((com.borland.jbuilder.node.JBProject)project,
                              new Url[0]);
            }
        }


        private void unloadProject(com.borland.jbuilder.node.JBProject currentProject,
                                   Url[]                               newLibraries)
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
        private class UpdateThread
            extends Thread
        {
            com.borland.jbuilder.node.JBProject project;
            JDialog dialog;

            UpdateThread(JDialog                             dialog,
                         com.borland.jbuilder.node.JBProject project)
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
                locations.add(new File(_outputDir));

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

                    JOptionPane.showMessageDialog(INSTANCE.getMainWindow(),
                                                  ex.getMessage() +
                                                  "\nCheck your classpath setup via Tools->Configure Libraries... and/or\n" +
                                                  "Project->Project properties...->Required Libraries \n\n" +
                                                  "Meanwhile the import optimization feature will be disabled.\n",
                                                  "Error: Library not found",
                                                  JOptionPane.ERROR_MESSAGE);
                }

                // keep track of the current project
                _curProject = this.project;
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
     * Used to update the class repository if the user changes project
     * settings.
     */
    private static class ProjectHandler
        implements ProjectListener
    {
        public void nodeChanged(com.borland.primetime.node.Project project,
                                Node                               node)
        {
            _numFiles = 0;
        }


        public void nodeChildrenChanged(com.borland.primetime.node.Project project,
                                        Node                               parent)
        {
        }


        public void projectPropertyChanged(com.borland.primetime.node.Project project,
                                           String                             category,
                                           String                             property,
                                           String                             oldValue,
                                           String                             newValue)
        {
            //System.err.println(category + " " + property + " " + oldValue +
            //                   " " + newValue);
            if ("sys".equals(category) && "Libraries".equals(property))
            {
                List oldUrls = new ArrayList();
                List newUrls = new ArrayList();

                for (StringTokenizer tokens = new StringTokenizer(oldValue, ";");
                     tokens.hasMoreTokens();)
                {
                    oldUrls.add(tokens.nextToken());
                }

                for (StringTokenizer tokens = new StringTokenizer(newValue, ";");
                     tokens.hasMoreTokens();)
                {
                    newUrls.add(tokens.nextToken());
                }

                List removed = new ArrayList();

                for (int i = 0, size = oldUrls.size(); i < size; i++)
                {
                    String url = (String)oldUrls.get(i);

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
                    String url = (String)newUrls.get(i);

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
                ProjectPathSet paths = ((com.borland.jbuilder.node.JBProject)project).getPaths();

                for (int i = 0, size = removed.size(); i < size; i++)
                {
                    PathSet pathset = paths.getLibrary((String)removed.get(i));
                    Url[] urls = pathset.getFullClassPath();
                    Url[] temp = new Url[unload.length + urls.length];
                    System.arraycopy(unload, 0, temp, 0, unload.length);
                    System.arraycopy(urls, 0, temp, unload.length, urls.length);
                    unload = temp;
                }

                Url[] load = new Url[0];

                for (int i = 0, size = added.size(); i < size; i++)
                {
                    PathSet pathset = paths.getLibrary((String)added.get(i));
                    Url[] urls = pathset.getFullClassPath();
                    Url[] temp = new Url[load.length + urls.length];
                    System.arraycopy(load, 0, temp, 0, load.length);
                    System.arraycopy(urls, 0, temp, load.length, urls.length);
                    load = temp;
                }

                update(project, unload, load);
            }
            else if ("sys".equals(category) && "JDK".equals(property))
            {
                JDKPathSet oldpath = PathSetManager.getJDK(oldValue);
                JDKPathSet newpath = PathSetManager.getJDK(newValue);
                update(project, oldpath.getFullClassPath(),
                       newpath.getFullClassPath());
            }
            else if ("sys".equals(category) && "OutPath".equals(property))
            {
                String projectPath = project.getProjectPath().getFileObject() +
                                     File.separator;
                Url[] unload ={ new Url(new File(projectPath + oldValue)) };
                Url[] load ={ new Url(new File(projectPath + newValue)) };
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
                 * @todo show error dialog, unload all libs to disable
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

            files.add(new File(_outputDir));

            try
            {
                repository.unloadAll(files);
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
         * Issues the updating of the class repository for the given project.
         *
         * @param project project which settings have been altered.
         * @param unload urls that are to be removed from the class
         *        repository.
         * @param load urls that are to be added to the class repository.
         */
        private void update(com.borland.primetime.node.Project project,
                            Url[]                              unload,
                            Url[]                              load)
        {
            Thread updater = new UpdateThread(null, unload, load);
            updater.start();
        }

        /**
         * Used to update the class repository after changes to the classpath.
         */
        private static class UpdateThread
            extends Thread
        {
            JDialog dialog;
            Url[] load;
            Url[] unload;

            UpdateThread(JDialog dialog,
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
     * Formats the project files currently selected in the project view.
     */
    private class FormatMultiAction
        extends BrowserAction
    {
        /**
         * Creates a new FormatMultiAction object.
         */
        public FormatMultiAction()
        {
            super("project-format");
            putValue("ActionGroup", "Build");
            putValue(BrowserAction.SHORT_DESCRIPTION, "Format");
            putValue(BrowserAction.LONG_DESCRIPTION,
                     "Format the selected nodes");
        }

        public void actionPerformed(Browser browser)
        {
            if (isImportOptimizationEnabled())
            {
                if (!checkClassPath(_curProject))
                {
                    return;
                }
            }

            // we want to update all selected nodes
            performAction(Action.FORMAT_SELECTED);
        }
    }


    /**
     * Formats the currently active editor window content.
     */
    private class FormatSingleAction
        extends EditorAction
        implements EditorContextActionProvider
    {
        /**
         * Creates a new FormatSingleAction object.
         */
        public FormatSingleAction()
        {
            super("file-format");
            putValue("ActionGroup", "Build");
            putValue(BrowserAction.LONG_DESCRIPTION,
                     "Format active project node");
            putValue(UpdateAction.MNEMONIC, new Character('t'));
            putValue(UpdateAction.ACCELERATOR,
                     KeyStroke.getKeyStroke(KeyEvent.VK_F10,
                                            ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
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
            ProjectFile file = getActiveProject().getActiveFile();

            if (null == file)
            {
                putValue(BrowserAction.SHORT_DESCRIPTION, "Format");

                return false;
            }

            putValue(BrowserAction.SHORT_DESCRIPTION,
                     "Format \"" + file.getName() + "\"");

            return true;
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
         * @return the binding Object stored with this key. If there are no
         *         keys, it will return <code>null</code>.
         */
        public Object getValue(String key)
        {
            if (key.equals(BrowserAction.SHORT_DESCRIPTION))
            {
                ProjectFile file = getActiveProject().getActiveFile();

                if (file == null)
                {
                    return "Format";
                }
                else
                {
                    return "Format \"" + file.getName() + "\"";
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

            // we only want to update the active file
            performAction(Action.FORMAT_ACTIVE);
        }
    }


    /**
     * Provides the context menu action for the project view.
     */
    private class JbContextActionProvider
        implements ContextActionProvider
    {
        FormatMultiAction action = new FormatMultiAction();

        public JbContextActionProvider()
        {
        }

        public javax.swing.Action getContextAction(Browser browser,
                                                   Node[]  nodes)
        {
            return (this.action);
        }
    }


    /**
     * Displays the configuration dialog.
     */
    private class SettingsAction
        extends EditorAction
        implements EditorContextActionProvider
    {
        /**
         * Creates a new SettingsAction object.
         */
        public SettingsAction()
        {
            super("config-jalopy");
            putValue(BrowserAction.SMALL_ICON,
                     new ImageIcon(this.getClass()
                                       .getResource("/de/hunsicker/jalopy/ui/resources/Preferences16.gif")));
            putValue(BrowserAction.LONG_DESCRIPTION, "Edit Jalopy options");
            putValue(BrowserAction.SHORT_DESCRIPTION, "Jalopy Options...");
            putValue(UpdateAction.MNEMONIC, new Character('j'));
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
            SettingsDialog dlg = new SettingsDialog(Browser.getActiveBrowser());
            dlg.pack();
            dlg.setLocationRelativeTo(Browser.getActiveBrowser());
            dlg.setVisible(true);
        }
    }
}
