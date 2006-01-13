/*
 * Copyright (c) 2002, Marco Hunsicker. All rights reserved.
 *
 * The contents of this file are subject to the Common Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://jalopy.sf.net/license-cpl.html
 *
 * Copyright (c) 2001-2002 Marco Hunsicker
 */
package de.hunsicker.jalopy.plugin.eclipse;

import java.awt.Frame;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;
import de.hunsicker.jalopy.plugin.StatusBar;
import de.hunsicker.jalopy.swing.ProgressMonitor;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The Jalopy Eclipse Plug-in.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class EclipsePlugin
    extends AbstractUIPlugin
    implements Project
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The Plug-in identifier ({@value}). */
    public static final String ID = "de.hunsicker.jalopy.plugin.eclipse" /* NOI18N */;

    /** The I-Beam text cusor. */
    protected static Cursor _ibeamCursor;

    /** The sole object instance. */
    protected static EclipsePlugin plugin;

    //~ Instance variables ---------------------------------------------------------------

    /** The currently selected project files. */
    Collection files = Collections.EMPTY_LIST; // List of <ProjectFile>

    /** The Jalopy Plug-in implementation. */
    PluginImpl impl;

    /** The currently opened project file. */
    ProjectFile file;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new EclipsePlugin object.
     *
     * @param descriptor the Plug-in descriptor.
     */
    public EclipsePlugin()
    {
        plugin = this;
    }
    public EclipsePlugin(IPluginDescriptor descriptor)
    {
        super(descriptor);
        files = Collections.EMPTY_LIST;
        plugin = this;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the shared plugin instance.
     *
     * @return the sole instance of this class.
     */
    public static EclipsePlugin getDefault()
    {
        return plugin;
    }


    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        return this.file;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getAllFiles()
    {
        return this.files;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getOpenedFiles()
    {
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public Collection getSelectedFiles()
    {
        return this.files;
    }

    /**
     * Called on stop of this plugin 
     *
     * @param context
     * @throws Exception If an error occurs
     */
    public void stop(BundleContext context) throws Exception{
        this.impl = null;
        this.file = null;
        this.files.clear();
        this.files = null;
        EclipsePlugin.plugin = null;
        _ibeamCursor.dispose();
        super.stop(context);
    }

    /**
     * Called on start of this plugin
     *
     * @throws Exception If an error occurs
     */
    public void start(BundleContext context)
      throws Exception
    {
        super.start(context);
        this.impl = new PluginImpl();
        _ibeamCursor =
            new Cursor(
                getActiveWorkbenchWindow().getShell().getDisplay(), SWT.CURSOR_IBEAM);
    }


    protected IWorkbenchWindow getActiveWorkbenchWindow()
    {
        IWorkbenchWindow window = getWorkbench().getActiveWorkbenchWindow();

        if (window == null)
        {
            final Visibility visibility = new Visibility();
            IWorkbenchWindow[] windows = plugin.getWorkbench().getWorkbenchWindows();

            for (int i = 0; i < windows.length; i++)
            {
                window = windows[i];

                final Shell shell = window.getShell();

                if (shell != null)
                {
                    Display.getDefault().syncExec(
                        new Runnable()
                        {
                            public void run()
                            {
                                if (shell.isVisible())
                                {
                                    visibility.visible = true;
                                }
                            }
                        });

                    if (visibility.visible)
                    {
                        return window;
                    }
                }
            }
        }

        return window;
    }


    protected IStatusLineManager getStatusLine()
    {
        IWorkbenchWindow window = plugin.getActiveWorkbenchWindow();
        IWorkbenchPage[] pages = window.getPages();

        for (int i = 0; i < pages.length; i++)
        {
            IViewReference[] references = pages[i].getViewReferences();

            for (int j = 0; j < references.length; j++)
            {
                IViewPart view = references[j].getView(true);

                if (view == null)
                {
                    continue;
                }

                IActionBars bars = view.getViewSite().getActionBars();
                IStatusLineManager statusLineManager = bars.getStatusLineManager();

                if (statusLineManager instanceof SubStatusLineManager)
                {
                    statusLineManager =
                        (IStatusLineManager) ((SubStatusLineManager) statusLineManager)
                        .getParent();
                }

                return statusLineManager;
            }
        }

        return null;
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * Implements the Jalopy Plug-in contract.
     */
    static class PluginImpl
        extends AbstractPlugin
    {
        Control activeControl;
        Cursor waitCursor;
        Shell activeShell;
        StatusBar statusBar;

        /**
         * Creates a new PluginImpl object.
         */
        public PluginImpl()
        {
            super(new EclipseAppender());
            this.statusBar = new EclipseStatusBar();
        }

        public Project getActiveProject()
        {
            return plugin;
        }


        public FileFormat getFileFormat()
        {
            /**
             * @todo implement
             */
            return FileFormat.AUTO;
        }


        public Frame getMainWindow()
        {
            return null;
        }


        public StatusBar getStatusBar()
        {
            return this.statusBar;
        }


        protected ProgressMonitor createProgressMonitor()
        {
            final Monitor monitor = new Monitor();

            try
            {
                executeSynchron(
                    new Runnable()
                    {
                        public void run()
                        {
                            monitor.monitor =
                                new ProgressMonitorImpl(
                                    plugin.getActiveWorkbenchWindow().getShell());
                        }
                    });
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return monitor.monitor;
        }


        protected void displayError(
            final Throwable error,
            Frame           parent)
        {
            try
            {
                executeSynchron(
                    new Runnable()
                    {
                        public void run()
                        {
                            IWorkbenchWindow window = plugin.getActiveWorkbenchWindow();
                            Shell shell = window.getShell();
                            InternalErrorDialog.openError(
                                shell, "Internal Error",
                                (error.getMessage() != null) ? error.getMessage()
                                                             : error.getClass().getName(),
                                error);
                        }
                    });
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        protected void executeAsynchron(Runnable operation)
        {
            Display.getDefault().asyncExec(operation);
        }


        protected void executeSynchron(Runnable operation)
          throws InterruptedException, InvocationTargetException
        {
            Display.getDefault().syncExec(operation);
        }


        protected void hideWaitCursor()
        {
            if (this.waitCursor != null)
            {
                try
                {
                    executeSynchron(
                        new Runnable()
                        {
                            public void run()
                            {
                                PluginImpl.this.activeControl.setCursor(_ibeamCursor);

                                Shell[] shells =
                                    PluginImpl.this.activeShell.getDisplay().getShells();

                                for (int i = 0; i < shells.length; i++)
                                {
                                    shells[i].setCursor(null);
                                }

                                PluginImpl.this.waitCursor.dispose();
                                PluginImpl.this.activeShell = null;
                                PluginImpl.this.activeControl = null;
                            }
                        });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }


        protected void showWaitCursor()
        {
            IWorkbenchWindow window = plugin.getActiveWorkbenchWindow();
            this.activeShell = window.getShell();
            this.waitCursor = new Cursor(this.activeShell.getDisplay(), SWT.CURSOR_WAIT);

            try
            {
                executeSynchron(
                    new Runnable()
                    {
                        public void run()
                        {
                            PluginImpl.this.activeControl =
                                PluginImpl.this.activeShell.getDisplay().getCursorControl();
                            PluginImpl.this.activeControl.setCursor(
                                PluginImpl.this.waitCursor);

                            Shell[] shells =
                                PluginImpl.this.activeShell.getDisplay().getShells();

                            for (int i = 0; i < shells.length; i++)
                            {
                                shells[i].setCursor(PluginImpl.this.waitCursor);
                            }
                        }
                    });
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        private static class Monitor
        {
            public ProgressMonitor monitor;
        }
    }


    /**
     * Provides access to the Eclipse status bar.
     */
    private static class EclipseStatusBar
        implements StatusBar
    {
        public void setText(final String message)
        {
            Display.getDefault().syncExec(
                new Runnable()
                {
                    public void run()
                    {
                        IStatusLineManager statusLine = plugin.getStatusLine();

                        if (statusLine != null)
                        {
                            statusLine.setMessage(message);
                            statusLine.update(true);
                        }
                    }
                });
        }
    }


    private static class ProgressMonitorImpl
        implements ProgressMonitor
    {
        IProgressMonitor monitor;
        ProgressMonitorDialog dialog;
        Shell shell;
        int units;

        public ProgressMonitorImpl(Composite parent)
        {
            this.shell = parent.getShell();
        }

        public void setCanceled(boolean state)
        {
        }


        public boolean isCanceled()
        {
            if (this.monitor != null)
            {
                return this.monitor.isCanceled();
            }

            return false;
        }


        public void setProgress(final int units)
        {
            if (this.monitor != null)
            {
                try
                {
                    plugin.impl.executeSynchron(
                        new Runnable()
                        {
                            public void run()
                            {
                                ProgressMonitorImpl.this.monitor.worked(units);
                            }
                        });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }


        public int getProgress()
        {
            return this.units;
        }


        public void setText(final String text)
        {
            if (this.monitor != null)
            {
                try
                {
                    plugin.impl.executeSynchron(
                        new Runnable()
                        {
                            public void run()
                            {
                                ProgressMonitorImpl.this.monitor.subTask(text);
                            }
                        });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }


        public void begin(
            final String text,
            final int    aUnits)
        {
            try
            {
                plugin.impl.executeSynchron(
                    new Runnable()
                    {
                        public void run()
                        {
                            ProgressMonitorImpl.this.dialog =
                                new ProgressMonitorDialog(ProgressMonitorImpl.this.shell);

                            /**
                             * @todo causes error if Cancel button pressed
                             */

                            // ProgressMonitorImpl.this.dialog.setCancelable(true);
                            ProgressMonitorImpl.this.monitor =
                                ProgressMonitorImpl.this.dialog.getProgressMonitor();
                            ProgressMonitorImpl.this.dialog.open();
                            ProgressMonitorImpl.this.monitor.beginTask(text, aUnits);
                        }
                    });
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        public void done()
        {
            if (this.dialog != null)
            {
                try
                {
                    plugin.impl.executeSynchron(
                        new Runnable()
                        {
                            public void run()
                            {
                                ProgressMonitorImpl.this.dialog.close();
                                ProgressMonitorImpl.this.dialog = null;
                                ProgressMonitorImpl.this.monitor = null;
                                ProgressMonitorImpl.this.shell = null;
                            }
                        });
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }


    private static class Visibility
    {
        boolean visible;
    }
}
