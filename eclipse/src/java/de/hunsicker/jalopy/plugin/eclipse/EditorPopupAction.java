/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All Rights Reserved.
 *
 * The contents of this file are subject to the Common Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.eclipse.org/
 *
 * $Id$
 */
package de.hunsicker.jalopy.plugin.eclipse;

import de.hunsicker.jalopy.plugin.AbstractPlugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionDelegate;
import org.eclipse.ui.texteditor.AbstractTextEditor;


/**
 * Action to be added to the context menu of the Java editor.
 * 
 * @version $Revision$
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 */
public class EditorPopupAction
    extends ActionDelegate
    implements IEditorActionDelegate
{
    //~ Instance variables ····················································

    /** The editor target. */
    private IEditorPart _part;

    //~ Constructors ··························································

    /**
     * Create a new EditorPopupAction object.
     */
    public EditorPopupAction()
    {
    }

    //~ Methods ·······························································

    /**
     * @see IEditorActionDelegate#setActiveEditor(IAction, IEditorPart)
     */
    public void setActiveEditor(IAction     action, 
                                IEditorPart targetEditor)
    {
        _part = targetEditor;
    }


    /**
     * @see ActionDelegate#run(IAction)
     */
    public void run(IAction action)
    {
        EclipsePlugin plugin = EclipsePlugin.getDefault();


        // update the project information
        plugin.file = new EclipseProjectFile((AbstractTextEditor)_part);


        // and format the current file
        plugin.impl.performAction(AbstractPlugin.Action.FORMAT_ACTIVE);
    }
}