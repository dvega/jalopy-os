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

import org.openide.loaders.DataObject;
import org.openide.nodes.Node;


/**
 * Some helper stuff.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbHelper
{
    //~ Static variables/initializers ·········································

    /** The .java file extension. */
    static final String EXTENSION_JAVA = "java";

    //~ Methods ·······························································

    /**
     * Determines whether the given node represents a folder.
     *
     * @param node a node.
     *
     * @return <code>true</code> if the node represents a folder.
     *
     * @since 1.0b8
     */
    static boolean isFolder(Node node)
    {
        return (node.getCookie(org.openide.loaders.DataFolder.class) != null);
    }


    /*static boolean isRootFolder(DataFolder folder)
    {
        return folder.getFolder() == null;
    }*/

    /**
     * Indicates whether the given data object represents a Java source file.
     *
     * @param obj a data object.
     *
     * @return <code>true</code> if the given object represents a Java source
     *         file.
     */
    static boolean isJavaFile(DataObject obj)
    {
        /**
         * @todo this is ugly, but we want to prevent formatting
         *       org.netbeans.modules.form.FormDataNode nodes
         */
        if (obj.getNodeDelegate().getClass().getName()
               .equals("org.netbeans.modules.java.JavaNode"))
        {
            if (EXTENSION_JAVA.equals(obj.getPrimaryFile().getExt()))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Determines whether the given node represents a project.
     *
     * @param node a node.
     *
     * @return <code>true</code> if the node represents a project.
     *
     * @since 1.0b8
     */
    static boolean isProject(Node node)
    {
        return (node.getCookie(org.openide.cookies.ProjectCookie.class) != null);
    }
}
