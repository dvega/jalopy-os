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

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;


/**
 * The Eclipse editor implementation.
 * 
 * @version $Revision$
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 */
final class EclipseEditor
    implements Editor
{
    //~ Instance variables ····················································

    AbstractTextEditor editor;
    IDocument document;
    ProjectFile file;

    //~ Constructors ··························································

    /**
     * Creates a new EclipseEditor object.
     * 
     * @param file the corresponding project file.
     * @param editor the Eclipse editor object.
     */
    public EclipseEditor(ProjectFile        file, 
                         AbstractTextEditor editor)
    {
        this.editor = editor;

        IDocumentProvider provider = editor.getDocumentProvider();
        this.document = provider.getDocument(editor.getEditorInput());
        this.file = file;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        //setSelection(offset, offset);
        this.editor.setHighlightRange(offset, 0, true); 
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int line, 
                                 int column)
    {
        try
        {
            int offset = this.document.getLineOffset(line);
            //setSelection(offset, offset + column);
            this.editor.setHighlightRange(offset, 0, true); 
        }
        catch (BadLocationException ignored)
        {
            ;
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getCaretPosition()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        return selection.getOffset();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        try
        {
            return this.document.getLineOffset(selection.getStartLine()) - 
                   selection.getOffset();
        }
        catch (BadLocationException ex)
        {
            return 0;
        }
    }


    /**
     * {@inheritDoc}
     */
    public ProjectFile getFile()
    {
        return this.file;
    }


    /**
     * {@inheritDoc}
     */
    public int getLength()
    {
        return this.document.getLength();
    }


    /**
     * {@inheritDoc}
     */
    public int getLine()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        return selection.getStartLine();
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        return selection.getText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(int startOffset, 
                             int endOffset)
    {
        if ((startOffset < 0) || (endOffset < 0) || 
            (endOffset < startOffset))
        {
            throw new IllegalArgumentException("invalid range -- start " + 
                                               startOffset + ", end " + 
                                               endOffset);
        }

        ISelection selection = new TextSelection(this.document, startOffset, 
                                                 endOffset - startOffset);
        this.editor.getSelectionProvider().setSelection(selection);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        if (selection.isEmpty())
        {
            return selection.getOffset();
        }
        else if (this.document.getLength() == 0)
        {
            return 0;
        }
        else
        {
            return selection.getOffset() + selection.getText().length();
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionStart()
    {
        ITextSelection selection = (ITextSelection)this.editor.getSelectionProvider()
                                                              .getSelection();

        return selection.getOffset();
    }


    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        this.document.set(text);
    }


    /**
     * {@inheritDoc}
     */
    public String getText()
    {
        return this.document.get();
    }


    /**
     * {@inheritDoc}
     */
    public void paste(String text)
    {
        throw new RuntimeException("not yet implemented");
    }


    /**
     * {@inheritDoc}
     */
    public void requestFocus()
    {
        this.editor.setFocus();
    }


    /**
     * {@inheritDoc}
     */
    public void selectAll()
    {
        setSelection(0, this.document.getLength());
    }
}