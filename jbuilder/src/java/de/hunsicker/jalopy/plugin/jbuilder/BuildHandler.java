/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.primetime.build.BuildListener;
import com.borland.primetime.build.BuildProcess;
import com.borland.primetime.build.StaticBuildListener;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;


/**
 * OpenTool class that registers a build listener to update the class repository. This
 * class has to be compiled against JBuilder 7.0 or above.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 0.7.5
 */
public final class BuildHandler
{
    //~ Static variables/initializers ----------------------------------------------------

    private static BuildListener _listener;

    /** Was the build successful? */
    private static boolean _error;

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     */
    public static void initOpenTool(
        byte major,
        byte minor)
    {
        // add listener only for JBuilder 7.0 or above
        if (((major == 4) && (minor > 3)) || (major > 4))
        {
            BuildProcess.addStaticBuildListener(
                new StaticBuildListener()
                {
                    public void buildFinish(BuildProcess process)
                    {
                        if (!_error)
                        {
                            try
                            {
                                if (JbPlugin.isImportOptimizationEnabled())
                                {
                                    JbPlugin.updateRepository();
                                }
                            }
                            finally
                            {
                                process.removeBuildListener(_listener);

                                // reset the error state
                                _error = false;
                            }
                        }
                    }


                    public void buildStart(BuildProcess process)
                    {
                        if (_listener == null)
                        {
                            _listener =
                                new BuildListener()
                                    {
                                        public void buildFinish(BuildProcess process)
                                        {
                                        }


                                        public void buildProblem(
                                            BuildProcess process,
                                            Project      project,
                                            Url          url,
                                            boolean      error,
                                            String       message,
                                            int          line,
                                            int          column,
                                            String       ref)
                                        {
                                            _error = error;
                                        }


                                        public void buildMessage(
                                            BuildProcess process,
                                            String       p1,
                                            String       p2)
                                        {
                                        }


                                        public void buildStatus(
                                            BuildProcess process,
                                            String       message,
                                            boolean      p2)
                                        {
                                        }


                                        public void buildStart(BuildProcess process)
                                        {
                                        }
                                    };
                        }

                        if (JbPlugin.isImportOptimizationEnabled())
                        {
                            // we need to add a listener to be informed if the build failed
                            process.addBuildListener(_listener);
                        }
                    }
                });
        }
    }
}
