/*
 *                 Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 *
 * The Original Code is Marco Hunsicker. The Initial Developer of the Original
 * Code is Marco Hunsicker. All rights reserved.
 *
 * Copyright (c) 2002 Marco Hunsicker
 */
package de.hunsicker.jalopy.plugin.netbeans;

import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.JEditorPane;

import org.openide.TopManager;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.LocalFileSystem;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataShadow;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;
import org.openide.util.actions.SystemAction;


/**
 * The action to format nodes or children thereof.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class FormatAction
    extends CookieAction
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Cookies for which to enable the action. */
    private static final Class[] COOKIE_CLASSES =
        new Class[] { EditorCookie.class, DataFolder.class, DataFolder.FolderNode.class };

    //~ Instance variables ---------------------------------------------------------------

    /** The name of the action to display in 'Build' menu. */
    private String _name;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new FormatAction object.
     */
    public FormatAction()
    {
        // we have to register popups here, because otherwise users would have
        // to open the "Build" menu first in order to see the popup menus
        registerPopups();
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Get a help context for the action.
     *
     * @return the help context for this action.
     */
    public HelpCtx getHelpCtx()
    {
        return HelpCtx.DEFAULT_HELP;

        // If you will provide context help then use:
        // return new HelpCtx (FormatAction.class);
    }


    /**
     * Get a human presentable name of the action.
     *
     * @return the name of the action.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * Get the cookies that this action requires.
     *
     * @return a list of cookies.
     */
    protected java.lang.Class[] cookieClasses()
    {
        return COOKIE_CLASSES;
    }


    /**
     * Indicates whether the action should be enabled based on the currently activated
     * nodes. The action will only be enabled for Java data objects (representing Java
     * source files) and data folders.
     *
     * @param nodes the set of activated nodes.
     *
     * @return <code>true</code> if the action should be enabled.
     */
    protected boolean enable(Node[] nodes)
    {
        /**
         * @todo this is not the proposed way to implement this logic, it should be
         *       sufficient to specify a cookie and mode to go, as outlined in the
         *       Javadocs, but I don't know how to achieve enabling the node only for
         *       Java source files and folders this way, therefore this hack. Plus I
         *       gain context information so I can change the name of the menu item
         *       depending on the state. Nice!
         */
        for (int i = 0; i < nodes.length; i++)
        {
            DataObject obj = (DataObject) nodes[i].getCookie(DataObject.class);

            if (obj == null)
            {
                continue;
            }

            // if there is at least one Java source file or folder,
            // we know there is a sense in formatting
            if (NbHelper.isFolder(nodes[i]))
            {
                DataFolder folder = null;

                if (obj instanceof DataShadow)
                {
                    folder = (DataFolder) ((DataShadow) obj).getOriginal();
                }
                else
                {
                    folder = (DataFolder) obj;
                }

                try
                {
                    /**
                     * @todo can it ever happen that a LocalFileSystem is part of a
                     *       MultiFileSystem? If so, we have to add extra checking code
                     *       here
                     */
                    if (
                        folder.getPrimaryFile().getFileSystem() instanceof LocalFileSystem)
                    {
                        _name = "&Format All";

                        return true;
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            else if (NbHelper.isJavaFile(obj))
            {
                _name = "&Format";

                return true;
            }
        }

        _name = "&Format";

        return false;
    }


    /**
     * Initializes the action.
     */
    protected void initialize()
    {
        super.initialize();
        putProperty(
            Action.SHORT_DESCRIPTION,
            NbBundle.getMessage(FormatAction.class, "HINT_FormatAction"));
    }


    /**
     * Get the mode of the action (how strict it should be about cookie support).
     *
     * @return{@link CookieAction#MODE_SOME}.
     */
    protected int mode()
    {
        return MODE_ANY;
    }


    /**
     * Performs the action based on the currently activated nodes.
     *
     * @param nodes current activated nodes, may be empty but not <code>null</code>.
     */
    protected void performAction(org.openide.nodes.Node[] nodes)
    {
        NbPlugin.INSTANCE.project.nodes = nodes;

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try
        {
            // set the context class loader for the ImportTransformation
            // feature to work
            Thread.currentThread().setContextClassLoader(
                TopManager.getDefault().currentClassLoader());

            if (nodes.length == 1)
            {
                DataObject obj = (DataObject) nodes[0].getCookie(DataObject.class);

                if (NbHelper.isJavaFile(obj))
                {
                    // we have to check if the node has an editor opened as
                    // Action.FORMAT_ACTIVE relies on an opened view
                    if (isOpened(nodes[0]))
                    {
                        NbPlugin.INSTANCE.performAction(NbPlugin.Action.FORMAT_ACTIVE);
                    }
                    else
                    {
                        NbPlugin.INSTANCE.performAction(NbPlugin.Action.FORMAT_SELECTED);
                    }
                }
                else if (NbHelper.isFolder(nodes[0]))
                {
                    NbPlugin.INSTANCE.performAction(NbPlugin.Action.FORMAT_SELECTED);
                }
            }
            else
            {
                NbPlugin.INSTANCE.performAction(NbPlugin.Action.FORMAT_SELECTED);
            }
        }
        finally
        {
            Thread.currentThread().setContextClassLoader(loader);
        }
    }


    /**
     * Determines whether the given Java source file node has an opened editor pane.
     *
     * @param node a Java source file node.
     *
     * @return <code>true</code> if the node has an opened editor pane.
     */
    private boolean isOpened(Node node)
    {
        EditorCookie cookie = (EditorCookie) node.getCookie(EditorCookie.class);

        /**
         * @todo maybe this check is obsolete?
         */
        if (cookie == null)
        {
            return false;
        }

        JEditorPane[] panes = cookie.getOpenedPanes();

        return ((panes != null) && (panes.length > 0));
    }


    private void registerPopups()
    {
        /**
         * @todo this code is ugly and subject to change but until the Looks API is in
         *       place, I really like this better than the SystemAction way to integrate
         *       items into the Tools submenu
         */
        TopManager manager = TopManager.getDefault();

        DataLoader loader = manager.getLoaderPool().firstProducerOf(DataFolder.class);
        SystemAction[] actions = loader.getActions();

SEARCH: 
        for (int i = 0; i < actions.length; i++)
        {
            if (actions[i] != null) // null means separator
            {
                // add after the BuildAll action
                if (actions[i] instanceof org.openide.actions.BuildAllAction)
                {
                    SystemAction[] result = new SystemAction[actions.length + 2];
                    System.arraycopy(actions, 0, result, 0, i + 1);
                    result[i + 2] = this; // add the action
                    result[i + 3] = null; // and a separator
                    System.arraycopy(
                        actions, i + 2, result, i + 4, actions.length - i - 2);
                    loader.setActions(result);

                    break SEARCH;
                }
            }
        }

        // add our action to the popup menu of Java source file nodes
        for (
            Enumeration loaders = manager.getLoaderPool().allLoaders();
            loaders.hasMoreElements();)
        {
            loader = (DataLoader) loaders.nextElement();

            String name = loader.getClass().getName();

            /**
             * @todo it would be cool to be able to format FormDataNodes
             *       (org.netbeans.modules.form.FormDataLoader) as well, but I don't
             *       know how to achieve that as form views have guarded sections
             */
            if (name.equals("org.netbeans.modules.java.JavaDataLoader"))
            {
                actions = loader.getActions();
SEARCH_JAVA_FILE: 
                for (int i = 0; i < actions.length; i++)
                {
                    if (actions[i] != null) // null means separator
                    {
                        // add after the Execute action
                        if (actions[i] instanceof org.openide.actions.ExecuteAction)
                        {
                            SystemAction[] result = new SystemAction[actions.length + 2];
                            System.arraycopy(actions, 0, result, 0, i + 1);
                            result[i + 2] = this; // add the action
                            result[i + 3] = null; // and a separator
                            System.arraycopy(
                                actions, i + 2, result, i + 4, actions.length - i - 2);
                            loader.setActions(result);

                            break SEARCH_JAVA_FILE;
                        }
                    }
                }
            }
        }
    }
}
