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

import com.borland.primetime.build.BuildListener;
import com.borland.primetime.build.BuildProcess;
import com.borland.primetime.build.StaticBuildListener;
import com.borland.primetime.node.Project;
import com.borland.primetime.vfs.Url;


/**
 * OpenTool class that registers a build listener to update the class
 * repository. This class has to be compiled against JBuilder 6.0 or below.
 *
 * @since 0.7.5
 */
public final class OldBuildHandler
{
    //~ Static variables/initializers ·········································

    private static BuildListener _listener;

    /** Was the build successful? */
    private static boolean _error;

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     *
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     */
    public static void initOpenTool(byte major,
                                    byte minor)
    {
        // add listener only for JBuilder 6.0 or below
        if ((major == 4) && (minor < 4))
        {
            BuildProcess.addStaticBuildListener(new StaticBuildListener()
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
                                // reset the error state
                                _error = false;
                                process.removeBuildListener(_listener);
                            }
                        }
                    }


                    public void buildStart(BuildProcess process)
                    {
                        if (_listener == null)
                        {
                            _listener = new BuildListener()
                                {
                                    public void buildFinish(BuildProcess process)
                                    {
                                    }


                                    public void buildProblem(BuildProcess process,
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


                                    public void buildMessage(BuildProcess process,
                                                             String       p1,
                                                             String       p2)
                                    {
                                    }


                                    public void buildStatus(BuildProcess process,
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
