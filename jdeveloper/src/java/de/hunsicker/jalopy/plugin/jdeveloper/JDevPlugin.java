/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.awt.Container;
import java.awt.Frame;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.StatusBar;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevBracesPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevCommentsPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevDummyPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevEnvironmentPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevFooterPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevGeneralPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevHeaderPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevImportsPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevIndentationPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevJavadocPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevMessagesPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevMiscPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevSeparationPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevSortingPanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevWhitespacePanel;
import de.hunsicker.jalopy.plugin.jdeveloper.swing.JDevWrappingPanel;
import de.hunsicker.swing.util.SwingHelper;
import de.hunsicker.util.ResourceBundleFactory;

import oracle.ide.AddinManager;
import oracle.ide.ContextMenu;
import oracle.ide.Ide;
import oracle.ide.IdeAction;
import oracle.ide.addin.Addin;
import oracle.ide.addin.Context;
import oracle.ide.addin.ContextMenuListener;
import oracle.ide.addin.Controller;
import oracle.ide.config.IdeSettings;
import oracle.ide.model.DirectoryFolder;
import oracle.ide.model.Element;
import oracle.ide.model.PackageFolder;
import oracle.ide.model.Workspace;
import oracle.ide.model.Workspaces;
import oracle.ide.panels.Navigable;
import oracle.jdeveloper.model.BusinessComponents;
import oracle.jdeveloper.model.EnterpriseJavaBeans;
import oracle.jdeveloper.model.JProject;
import oracle.jdeveloper.model.JavaSourceNode;
import oracle.jdeveloper.model.JavaSources;


/**
 * The Jalopy JDeveloper Extension.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class JDevPlugin
    extends AbstractPlugin
    implements Addin, ContextMenuListener, Controller
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The name for ResourceBundle lookup. */
    private static final String BUNDLE_NAME =
        "de.hunsicker.jalopy.plugin.jdeveloper.Bundle" /* NOI18N */;

    /** The id for the Format command. */
    public static final int FORMAT_CMD_ID =
        Ide.newCmd("JDevPlugin.FORMAT_CMD_ID" /* NOI18N */);

    /** The id for the Preferences command. */
    public static final int PREFERENCES_CMD_ID =
        Ide.newCmd("JDevPlugin.PREFERENCES_CMD_ID" /* NOI18N */);

    /** The Format command string */
    public static final String FORMAT_CMD =
        "de.hunsicker.jalopy.plugin.jdeveloper.Format" /* NOI18N */;

    /** The Preferences command string */
    public static final String PREFERENCES_CMD =
        "de.hunsicker.jalopy.plugin.jdeveloper.DisplayPreferences" /* NOI18N */;

    /** Context id. */
    private static final int CONTEXT_INVALID = -1;

    /** Context id. */
    private static final int CONTEXT_SOURCE = 0;

    /** Context id . */
    private static final int CONTEXT_SOURCES = 1;

    /** Context id. */
    private static final int CONTEXT_WORKSPACE = 2;

    /** Context id. */
    private static final int CONTEXT_WORKSPACES = 3;

    /** Context id. */
    private static final int CONTEXT_PACKAGE = 4;

    /** Context id. */
    private static final int CONTEXT_DIRECTORY = 5;

    /** Context id. */
    private static final int CONTEXT_PROJECT = 6;

    /** Context id. */
    private static final int CONTEXT_EJB = 7;

    /** Context id. */
    private static final int CONTEXT_BUSINESS = 8;

    //~ Instance variables ---------------------------------------------------------------

    /** The localized label strings indexed by context id. */
    private final String[] _labels;

    /** The JDeveloper project representation. */
    private JDevProject _project;

    /** Our status bar implementation. */
    private JDevStatusBar _statusBar;

    /** The <em>Format</em> menu item. */
    private JMenuItem _formatMenuItem;

    /** The current context id. */
    private int _contextId;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public JDevPlugin()
    {
        super(new JDevAppender());

        ResourceBundle bundle = ResourceBundleFactory.getBundle(BUNDLE_NAME);

        _labels = new String[9];
        _labels[0] = bundle.getString("LBL_FORMAT" /* NOI18N */);
        _labels[1] = bundle.getString("LBL_FORMAT_PROJECT" /* NOI18N */);
        _labels[2] = bundle.getString("LBL_FORMAT_WORKSPACE" /* NOI18N */);
        _labels[3] = bundle.getString("LBL_FORMAT" /* NOI18N */);
        _labels[4] = bundle.getString("LBL_FORMAT" /* NOI18N */);
        _labels[5] = bundle.getString("LBL_FORMAT" /* NOI18N */);
        _labels[6] = bundle.getString("LBL_FORMAT_PROJECT" /* NOI18N */);
        _labels[7] = bundle.getString("LBL_FORMAT" /* NOI18N */);
        _labels[8] = bundle.getString("LBL_FORMAT" /* NOI18N */);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the version information of this Addin.
     *
     * @return version information of this Addin.
     */
    public static String getVersion()
    {
        return Package.getPackage("de.hunsicker.jalopy.plugin.jdeveloper" /* NOI18N */)
                      .getImplementationVersion();
    }


    /**
     * {@inheritDoc}
     */
    public Project getActiveProject()
    {
        if (_project == null)
        {
            _project = new JDevProject();
        }

        return _project;
    }


    /**
     * {@inheritDoc}
     */
    public FileFormat getFileFormat()
    {
        return FileFormat.DEFAULT;
    }


    /**
     * {@inheritDoc}
     */
    public Frame getMainWindow()
    {
        return Ide.getMainWindow();
    }


    /**
     * {@inheritDoc}
     */
    public StatusBar getStatusBar()
    {
        if (_statusBar == null)
        {
            _statusBar = new JDevStatusBar();
        }

        return _statusBar;
    }


    /**
     * Determines whether the IDE can shutdown.
     *
     * @return <code>true</code>
     */
    public boolean canShutdown()
    {
        return true;
    }


    /**
     * Checks the state of the context sensitive actions.
     *
     * @param context the current context.
     * @param activeController the controller associated with the active view.
     */
    public void checkCommands(
        Context    context,
        Controller activeController)
    {
    }


    /**
     * Called when the user double clicks on an item that has a popup menu.
     *
     * @param context the current context
     *
     * @return <code>false</code>
     */
    public boolean handleDefaultAction(Context context)
    {
        return false;
    }


    /**
     * Called when the user interaction with the <code>View</code> triggers the execution
     * of a command.
     *
     * @param action action whose command is to be executed.
     * @param context the context of the action.
     *
     * @return <code>true</code> if the controller handles the specified command.
     */
    public boolean handleEvent(
        IdeAction action,
        Context   context)
    {
        int commandId = action.getCommandId();

        if (commandId == FORMAT_CMD_ID)
        {
            switch (_contextId)
            {
                case CONTEXT_SOURCE :
                {
                    JDevProject project = (JDevProject) getActiveProject();

                    Element[] selection = context.getSelection();

                    if (selection.length == 0)
                    {
                        performAction(Action.FORMAT_ACTIVE);
                    }
                    else
                    {
                        project.selection = selection;
                        performAction(Action.FORMAT_SELECTED);
                    }

                    return true;
                }

                case CONTEXT_SOURCES :
                case CONTEXT_WORKSPACE :
                case CONTEXT_WORKSPACES :
                case CONTEXT_PACKAGE :
                case CONTEXT_DIRECTORY :
                case CONTEXT_PROJECT :
                case CONTEXT_EJB :
                case CONTEXT_BUSINESS :

                    JDevProject project = (JDevProject) getActiveProject();
                    Element[] selection = context.getSelection();
                    project.selection = selection;

                    performAction(Action.FORMAT_SELECTED);

                    return true;
            }
        }

        return false;
    }


    /**
     * Returns the version of the IDE for which this Addin was implemented.
     *
     * @return version for which this Addin was implemented.
     */
    public float ideVersion()
    {
        return Ide.IDE_VERSION;
    }


    /**
     * Initializes the Addin.
     */
    public void initialize()
    {
        AddinManager addinManager = Ide.getAddinManager();
        String command = addinManager.getCommand(FORMAT_CMD_ID, FORMAT_CMD);

        IdeAction action =
            IdeAction.get(
                FORMAT_CMD_ID, command, null, (Integer) null, null, null, null, true);
        action.setController(this);

        _formatMenuItem = Ide.getMenubar().createMenuItem(action);

        Ide.getNavigatorManager().addContextMenuListener(this, null);
        Ide.getEditorManager().getContextMenu().addContextMenuListener(this, null);

        IdeSettings settings = Ide.getSettings();

        Navigable[] panels =
        {
            new Navigable("General", JDevGeneralPanel.class),
            new Navigable("Braces", JDevBracesPanel.class),
            new Navigable("Whitespace", JDevWhitespacePanel.class),
            new Navigable("Indentation", JDevIndentationPanel.class),
            new Navigable("Wrapping", JDevWrappingPanel.class),
            new Navigable("Separation", JDevSeparationPanel.class),
            new Navigable("Comments", JDevCommentsPanel.class),
            new Navigable("Imports", JDevImportsPanel.class),
            new Navigable("Environment", JDevEnvironmentPanel.class),
            new Navigable("Javadoc", JDevJavadocPanel.class),
            new Navigable("Header", JDevHeaderPanel.class),
            new Navigable("Footer", JDevFooterPanel.class),
            new Navigable("Sorting", JDevSortingPanel.class),
            new Navigable("Misc", JDevMiscPanel.class),
            new Navigable("Messages", JDevMessagesPanel.class)
        };

        IdeSettings.registerUI(new Navigable("Jalopy", JDevDummyPanel.class, panels));

        System.out.println(
            "Jalopy Java Source Code Formatter " /* NOI18N */ + Jalopy.getVersion());
        System.out.println("JDeveloper Extension " /* NOI18N */ + getVersion());
        System.out.println(
            "Copyright (c) 2001-2002 Marco Hunsicker. All rights reserved." /* NOI18N */);
    }


    /**
     * Called just before the context menu disappears.
     *
     * @param menu the current context menu.
     */
    public void poppingDown(ContextMenu menu)
    {
    }


    /**
     * Called just before the context menu is popping up.
     *
     * @param menu the current context menu.
     */
    public void poppingUp(ContextMenu menu)
    {
        if (menu != null)
        {
            Context context = menu.getContext();

            if (context != null)
            {
                Element element = context.getElement();

                _contextId = getContextId(element);

                if (_contextId != JDevPlugin.CONTEXT_INVALID)
                {
                    insertFormatMenuItem(menu, _contextId);

                    /*
System.err.println(element.getClass().getName());

Container m = menu.getGUI(true);
Component[] c = m.getComponents();

if (c != null)
{
    int index = menu.getIndexOfCommandId(m, 215);

    for (int i = 0; i< c.length; i++)
    {
        if (c[i] instanceof JMenuItem)
        {
            JMenuItem item = (JMenuItem)c[i];
            IdeAction action = (IdeAction)item.getAction();

            if (action != null)
            {
                System.err.println(action.getCommand());
                System.err.println(action.getCommandId());
            }
            else
                System.err.println(item);
        }
    }
}
*/
                }
            }
        }
    }


    /**
     * Called when shutting down the IDE.
     */
    public void shutdown()
    {
        Ide.getNavigatorManager().removeContextMenuListener(this);
        Ide.getEditorManager().getContextMenu().removeContextMenuListener(this);
    }


    /**
     * Gets the supervising controller.
     *
     * @return <code>null</code>.
     */
    public Controller supervisor()
    {
        return null;
    }


    /**
     * Updates the enabled status of the specified action within the specified context.
     *
     * @param action action whose command is to be executed.
     * @param context the current context.
     *
     * @return <code>true</code> if the controller handles the specified command.
     */
    public boolean update(
        IdeAction action,
        Context   context)
    {
        return true;
    }


    /**
     * Returns the version of the Addin.
     *
     * @return version of the Addin.
     */
    public float version()
    {
        return 1f;
    }


    /**
     * Determines whether the <em>Format</em> menu item should be displayed for the given
     * element.
     *
     * @param element the selected element.
     *
     * @return <code>true</code> if the <em>Format</em> menu item should be displayed.
     */
    private int getContextId(Element element)
    {
        if (element instanceof JavaSourceNode)
        {
            return JDevPlugin.CONTEXT_SOURCE;
        }

        if (element instanceof JavaSources)
        {
            return JDevPlugin.CONTEXT_SOURCES;
        }

        if (element instanceof JProject)
        {
            return JDevPlugin.CONTEXT_PROJECT;
        }

        if (element instanceof PackageFolder)
        {
            return JDevPlugin.CONTEXT_PACKAGE;
        }

        if (element instanceof DirectoryFolder)
        {
            return JDevPlugin.CONTEXT_DIRECTORY;
        }

        if (element instanceof BusinessComponents)
        {
            return JDevPlugin.CONTEXT_BUSINESS;
        }

        if (element instanceof EnterpriseJavaBeans)
        {
            return JDevPlugin.CONTEXT_EJB;
        }

        if (element instanceof Workspace)
        {
            return JDevPlugin.CONTEXT_WORKSPACE;
        }

        if (element instanceof Workspaces)
        {
            return JDevPlugin.CONTEXT_WORKSPACES;
        }

        return JDevPlugin.CONTEXT_INVALID;
    }


    /**
     * Gets the appropriate <em>Format</em> menu item for the given context.
     *
     * @param contextId an integer that indicates the current context of the view.
     *
     * @return <em>Format</em> menu item.
     */
    private JMenuItem getFormatMenuItem(int contextId)
    {
        SwingHelper.setMenuText(_formatMenuItem, _labels[contextId], true);

        return _formatMenuItem;
    }


    /**
     * Returns the index of the <em>Build</em> item in the given context menu.
     *
     * @param menu a context menu.
     *
     * @return the index of the <em>Build</em> item, returns <code>-1</code> if no such
     *         item could be found.
     */
    private int getIndexOfBuildItem(ContextMenu menu)
    {
        int result = -1;

        Container container = menu.getGUI(false);

        if (container != null)
        {
            // oracle.jdeveloper.compiler.BuildSelectedCommand - 215
            result = menu.getIndexOfCommandId(container, 215); // Build

            if (result == -1)
            {
                // oracle.jdeveloper.compiler.BuildProjectCommand - 217
                result = menu.getIndexOfCommandId(container, 217); // Build Project
            }

            if (result == -1)
            {
                // oracle.jdeveloper.compiler.BuildProjectCommand - 219
                result = menu.getIndexOfCommandId(container, 219); // Build Workspace
            }
        }

        return result;
    }


    /**
     * Inserts the <em>Format</em> menu item into the given context menu.
     *
     * @param menu a context menu.
     * @param contextId an integer that describes the current context.
     */
    private void insertFormatMenuItem(
        ContextMenu menu,
        int         contextId)
    {
        // try to add the item behind the 'Build' item
        int index = getIndexOfBuildItem(menu);

        if (index != -1)
        {
            menu.insert(getFormatMenuItem(contextId), index + 1);
        }
        else
        {
            // add the item to the end of the menu
            menu.add(getFormatMenuItem(contextId));
        }
    }
}