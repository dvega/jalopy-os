/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.netbeans;

import de.hunsicker.jalopy.storage.Loggers;

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
    //~ Static variables/initializers ----------------------------------------------------

    /** The .java file extension. */
    static final String EXTENSION_JAVA = "java";

    //~ Methods --------------------------------------------------------------------------

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


    /**
     * Indicates whether the given data object represents a Java source file.
     *
     * @param obj a data object.
     *
     * @return <code>true</code> if the given object represents a Java source file.
     */
    static boolean isJavaFile(DataObject obj)
    {
        String name = obj.getNodeDelegate().getClass().getName();

        /**
         * @todo this is ugly, but we can't format org.netbeans.modules.form.FormDataNode
         *       nodes because of their guarded sections
         */
        if (
            "org.netbeans.modules.java.JavaNode" /* NOI18N */.equals(name)
            || "org.netbeans.modules.web.core.jsploader.WebLookNode" /* NOI18N */.equals(
                name))
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
