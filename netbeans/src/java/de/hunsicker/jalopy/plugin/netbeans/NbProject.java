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

import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;


/**
 * The NetBeans Project implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbProject
    implements Project
{
    //~ Instance variables ····················································

    /** The currently accessible nodes. */
    Node[] nodes;

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public ProjectFile getActiveFile()
    {
        if ((this.nodes != null) && (this.nodes.length == 1))
        {
            return new NbProjectFile(this, this.nodes[0]);
        }
        else
        {
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    public Collection getAllFiles()
    {
        return Collections.EMPTY_LIST;
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
        List files = new ArrayList(nodes.length);

        for (int i = 0; i < this.nodes.length; i++)
        {
            DataFolder folder = (DataFolder)nodes[i].getCookie(DataFolder.class);

            if (folder == null)
            {
                if (NbHelper.isJavaFile((DataObject)nodes[i].getCookie(DataObject.class)))
                {
                    files.add(new NbProjectFile(this, nodes[i]));
                }
            }
            else
            {
                // it's a folder kind of thing, add all children (recursive)
                for (Enumeration children = folder.children(true);
                     children.hasMoreElements();)
                {
                    DataObject obj = (DataObject)children.nextElement();

                    if (NbHelper.isJavaFile(obj))
                    {
                        NbProjectFile file = new NbProjectFile(this,
                                                               obj.getNodeDelegate());

                        // we check if the file is already contained in the list
                        // because a user may select both a folder and a
                        // contained file or a folder and a subfolder
                        if (!files.contains(file))
                        {
                            files.add(file);
                        }
                    }
                }
            }
        }

        return files;
    }
}
