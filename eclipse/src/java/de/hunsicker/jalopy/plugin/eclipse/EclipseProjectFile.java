/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.eclipse;

import java.io.File;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.Project;
import de.hunsicker.jalopy.plugin.ProjectFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;


/**
 * The Jalopy Eclipse ProjectFile implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class EclipseProjectFile
    implements ProjectFile
{
    //~ Instance variables ---------------------------------------------------------------

    EclipseEditor editor;
    IFile file;
    IWorkbenchPage page;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new EclipseProjectFile object.
     *
     * @param editor the opened associated editor view of the file.
     */
    public EclipseProjectFile(AbstractTextEditor editor)
    {
        this.editor = new EclipseEditor(this, editor);
    }


    /**
     * Creates a new EclipseProjectFile object.
     *
     * @param file the underlying file object.
     * @param page the workbench page the file is containted in.
     */
    public EclipseProjectFile(
        IFile          file,
        IWorkbenchPage page)
    {
        this.file = file;
        this.page = page;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public Editor getEditor()
    {
        if (this.editor != null)
        {
            return this.editor;
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public String getEncoding()
    {
        /**
         * @todo implement
         */
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public File getFile()
    {
        if (this.editor != null)
        {
            return getFile(this.editor).getLocation().toFile();
        }
        else
        {
            return this.file.getLocation().toFile();
        }
    }


    /**
     * {@inheritDoc}
     */
    public String getName()
    {
        if (this.editor != null)
        {
            return this.editor.editor.getEditorInput().getName();
        }
        else
        {
            return this.file.getName();
        }
    }


    /**
     * {@inheritDoc}
     */
    public boolean isOpened()
    {
        boolean result = false;

        if (this.editor != null)
        {
            result = true;
        }
        else
        {
            Operation operation = new Operation();
            Display.getDefault().syncExec(operation);

            result = operation.result;
        }

        return result;
    }


    /**
     * {@inheritDoc}
     */
    public Project getProject()
    {
        return EclipsePlugin.getDefault();
    }


    /**
     * {@inheritDoc}
     */
    public boolean isReadOnly()
    {
        if (this.editor != null)
        {
            return getFile(this.editor).isReadOnly();
        }
        else
        {
            return this.file.isReadOnly();
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        return this.file.toString();
    }


    private IFile getFile(EclipseEditor editor)
    {
        FileEditorInput input = (FileEditorInput) editor.editor.getEditorInput();

        return input.getFile();
    }

    //~ Inner Classes --------------------------------------------------------------------

    private final class Operation
        implements Runnable
    {
        boolean result;

        public void run()
        {
            IEditorReference[] references =
                EclipseProjectFile.this.page.getEditorReferences();

            for (int i = 0; i < references.length; i++)
            {
                IEditorPart part = references[i].getEditor(true);

                if (part == null)
                {
                    this.result = false;

                    break;
                }

                /**
                 * @todo it would be better to compare file paths
                 */
                if (part.getTitle().equals(EclipseProjectFile.this.file.getName()))
                {
                    EclipseProjectFile.this.editor =
                        new EclipseEditor(
                            EclipseProjectFile.this, (AbstractTextEditor) part);

                    this.result = true;

                    break;
                }
            }
        }
    }
}
