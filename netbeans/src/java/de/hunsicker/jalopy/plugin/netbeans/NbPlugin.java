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
package de.hunsicker.jalopy.plugin.netbeans;

import de.hunsicker.io.FileFormat;
import de.hunsicker.jalopy.plugin.AbstractPlugin;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.StatusBar;

import java.awt.Frame;

import org.openide.TopManager;


/**
 * The Jalopy NetBeans Plug-in.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbPlugin
    extends AbstractPlugin
{
    //~ Static variables/initializers ·········································

    /** The sole instance of this class. */
    static final NbPlugin INSTANCE = new NbPlugin();

    //~ Instance variables ····················································

    /** The current project. */
    NbProject project;

    //~ Constructors ··························································

    /**
     * Creates a new NbPlugin object.
     */
    private NbPlugin()
    {
        super(new NbAppender());
        this.project = new NbProject();
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public Project getActiveProject()
    {
        return this.project;
    }


    /**
     * {@inheritDoc}
     */
    public FileFormat getFileFormat()
    {
        /**
         * @todo implement
         */
        return FileFormat.AUTO;
    }


    /**
     * {@inheritDoc}
     */
    public Frame getMainWindow()
    {
        return TopManager.getDefault().getWindowManager().getMainWindow();
    }


    /**
     * {@inheritDoc}
     */
    public StatusBar getStatusBar()
    {
        return NbStatusBar.INSTANCE;
    }
}
