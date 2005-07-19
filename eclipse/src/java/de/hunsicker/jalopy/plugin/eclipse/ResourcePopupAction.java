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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.hunsicker.jalopy.plugin.AbstractPlugin;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.actions.ActionDelegate;


/**
 * Action to be added to the context menu of the Navigator or Packages view. Enables the
 * user to format the selected files (the files contained in the selected folders or
 * packages).
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ResourcePopupAction
    extends ActionDelegate
    implements IObjectActionDelegate
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The file extension for Java source files. */
    private static final String EXT_JAVA = ".java" /* NOI18N */;

    //~ Instance variables ---------------------------------------------------------------

    /** The page containing the part target. */
    private IWorkbenchPage _page;

    /** The part target. */
    private IWorkbenchPart _part;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ResourcePopupAction object.
     */
    public ResourcePopupAction()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
     */
    public void setActivePart(
        IAction        action,
        IWorkbenchPart targetPart)
    {
        _part = targetPart;
        _page = _part.getSite().getPage();
    }


    /**
     * @see ActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
        Shell shell = new Shell();
        if (
        !MessageDialog.openConfirm(shell,"Please Confirm", "Are you sure you wish to use jalopy to format")) {
         return;   
        }
        MessageDialog.openInformation(
            shell,
            "TestPlugin Plug-in",
            "New Action was executed.");
        
        IWorkbenchPartSite site = _part.getSite();
        ISelectionProvider provider = site.getSelectionProvider();
        StructuredSelection selection = (StructuredSelection) provider.getSelection();
        Object[] items = selection.toArray();
        Set files = new HashSet(items.length, 1.0F);

        try
        {
            for (int i = 0; i < items.length; i++)
            {
                if (items[i] instanceof IResource)
                {
                    IResource resource = (IResource) items[i];

                    switch (resource.getType())
                    {
                        case IResource.FOLDER :
                        case IResource.PROJECT :

                            IContainer folder = (IContainer) items[i];
                            getChildren(folder, files);

                            break;

                        case IResource.FILE :
                            files.add(new EclipseProjectFile((IFile) items[i], _page));

                            break;

                        default :

                            /**
                             * @todo use logger to print warning about invalid type
                             */
                            break;
                    }
                }
                else if (items[i] instanceof ICompilationUnit)
                {
                    ICompilationUnit unit = (ICompilationUnit) items[i];
                    IResource resource = unit.getCorrespondingResource();

                    if (resource != null)
                    {
                        files.add(new EclipseProjectFile((IFile) resource, _page));
                    }
                }
                else if (items[i] instanceof IPackageFragment)
                {
                    IPackageFragment fragment = (IPackageFragment) items[i];
                    addFilesFromPackage(fragment, files);
                }
                else if (items[i] instanceof IJavaProject)
                {
                    IJavaProject project = (IJavaProject) items[i];

                    try
                    {
                        IPackageFragment[] fragments = project.getPackageFragments();

                        for (int j = 0; j < fragments.length; j++)
                        {
                            switch (fragments[j].getKind())
                            {
                                case IPackageFragmentRoot.K_SOURCE :

                                    /**
                                     * @todo I'm not sure whether K_SOURCE actually means
                                     *       non-Archive (and therefore further testing
                                     *       is obsolete)
                                     */
                                    IPackageFragmentRoot root =
                                        (IPackageFragmentRoot) fragments[j].getAncestor(
                                            IJavaElement.PACKAGE_FRAGMENT_ROOT);

                                    if (!root.isArchive())
                                    {
                                        addFilesFromPackage(fragments[j], files);
                                    }

                                    break;

                                default :
                                    break;
                            }
                        }
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (CoreException ex)
        {
            ex.printStackTrace();
        }

        EclipsePlugin plugin = EclipsePlugin.getDefault();

        // update the project information
        plugin.files = files;

        // and format the selected files
        plugin.impl.performAction(AbstractPlugin.Action.FORMAT_SELECTED);
    }


    /**
     * Returns all children of the given container that denotes Java source files.
     *
     * @param resource the resource to return the children for.
     * @param files set to add all children to.
     *
     * @throws CoreException if the request failed.
     */
    private void getChildren(
        IContainer resource,
        Set        files)
      throws CoreException
    {
        IResource[] children = resource.members();

        for (int i = 0; i < children.length; i++)
        {
            IResource child = children[i];

            switch (child.getType())
            {
                case IResource.FILE :

                    if (child.getName().endsWith(EXT_JAVA))
                    {
                        files.add(new EclipseProjectFile((IFile) child, _page));
                    }

                    break;

                case IResource.FOLDER :
                    getChildren((IFolder) child, files);

                    break;

                case IResource.PROJECT :
                    getChildren((IProject) child, files);

                    break;

                case IResource.ROOT :
                    getChildren((IWorkspaceRoot) child, files);

                    break;
            }
        }
    }


    /**
     * Adds all Java source files contained in the given package fragement to the given
     * collection.
     *
     * @param fragment package fragement to search.
     * @param files collection to add all found Java source files to.
     *
     * @throws JavaModelException if the fragement does not exist or if an exception
     *         occurs while accessing its corresponding resource.
     */
    private void addFilesFromPackage(
        IPackageFragment fragment,
        Collection       files)
      throws JavaModelException
    {
        ICompilationUnit[] units = fragment.getCompilationUnits();

        for (int i = 0; i < units.length; i++)
        {
            IResource resource = units[i].getCorrespondingResource();

            if (resource != null)
            {
                files.add(new EclipseProjectFile((IFile) resource, _page));
            }
        }
    }
}
