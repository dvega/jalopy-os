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

import de.hunsicker.jalopy.plugin.AbstractAppender;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import org.apache.oro.text.regex.MatchResult;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;


/**
 * Appender which displays warning and error messages in an Eclipse task view.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class EclipseAppender
    extends AbstractAppender
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final String ID_MARKER = EclipsePlugin.ID + ".JalopyProblemMarker";

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new EclipseAppender object.
     */
    public EclipseAppender()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the marker severity for the given logging event.
     *
     * @param ev logging event.
     *
     * @return the marker severity for the given logging event.
     */
    public int getSeverity(LoggingEvent ev)
    {
        switch (ev.getLevel().toInt())
        {
            case Level.ERROR_INT :
            case Level.FATAL_INT :
                return IMarker.SEVERITY_ERROR;

            case Level.WARN_INT :
                return IMarker.SEVERITY_WARNING;

            default :
                return IMarker.SEVERITY_INFO;
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void append(LoggingEvent ev)
    {
        // check for Emacs-style messages
        MatchResult result = parseMessage(ev);

        // parsing failed, so we issue a standard message
        if (result == null)
        {
            /**
             * @todo where should the message go?
             */
            return; // we're done
        }

        String filename = result.group(POS_FILENAME);
        int lineno = 0;

        try
        {
            lineno = Integer.parseInt(result.group(POS_LINE));
        }
        catch (NumberFormatException neverOccurs)
        {
            ;
        }

        String text = result.group(POS_TEXT);
        IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
        IPath path = new Path(filename);
        IFile file = workspace.getFileForLocation(path);

        try
        {
            int severity = getSeverity(ev);

            switch (severity)
            {
                case IMarker.SEVERITY_ERROR :
                case IMarker.SEVERITY_WARNING :

                    IMarker marker = file.createMarker(ID_MARKER);
                    marker.setAttribute(IMarker.SEVERITY, severity);
                    marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
                    marker.setAttribute(IMarker.MESSAGE, text);

                    if (lineno > 0)
                    {
                        marker.setAttribute(IMarker.LINE_NUMBER, new Integer(lineno));
                    }

                    break;

                default :

                    /**
                     * @todo where should the message go?
                     */
                    break;
            }
        }
        catch (CoreException ex)
        {
            ex.printStackTrace();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        try
        {
            ResourcesPlugin.getWorkspace().getRoot().deleteMarkers(
                ID_MARKER, true, IResource.DEPTH_INFINITE);
        }
        catch (CoreException ignored)
        {
            ;
        }
    }
}
