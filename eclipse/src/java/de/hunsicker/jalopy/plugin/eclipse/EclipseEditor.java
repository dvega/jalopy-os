/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.eclipse;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;


/**
 * The Eclipse editor implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class EclipseEditor
    implements Editor
{
    //~ Instance variables ---------------------------------------------------------------

    AbstractTextEditor editor;
    IDocument document;
    ProjectFile file;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new EclipseEditor object.
     *
     * @param file the corresponding project file.
     * @param editor the Eclipse editor object.
     */
    public EclipseEditor(
        ProjectFile        file,
        AbstractTextEditor editor)
    {
        this.editor = editor;

        IDocumentProvider provider = editor.getDocumentProvider();
        this.document = provider.getDocument(this.editor.getEditorInput());
        this.file = file;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        if ((offset < 0) || (offset > getLength()))
        {
            throw new IllegalArgumentException("invalid range -- offset " + offset);
        }

        this.editor.setHighlightRange(offset, 0, true);
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(
        int line,
        int column)
    {
        if ((line < 1) || (column < 1))
        {
            throw new IllegalArgumentException(
                "invalid range -- line " + line + ", column " + column);
        }

        try
        {
            int offset = (this.document.getLineOffset(line - 1) + column) - 1;
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
        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

        return selection.getOffset();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        int result = 1;

        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

        try
        {
            result =
                selection.getOffset()
                - this.document.getLineOffset(selection.getStartLine()) + 1;
        }
        catch (BadLocationException ignored)
        {
            ;
        }

        return result;
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
        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

        return selection.getStartLine() + 1;
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

        return selection.getText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(
        int startOffset,
        int endOffset)
    {
        if ((startOffset < 0) || (endOffset < 0) || (endOffset < startOffset))
        {
            throw new IllegalArgumentException(
                "invalid range -- startOffset " + startOffset + ", endOffset "
                + endOffset);
        }

        ISelection selection =
            new TextSelection(this.document, startOffset, endOffset - startOffset);
        this.editor.getSelectionProvider().setSelection(selection);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

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
        ITextSelection selection =
            (ITextSelection) this.editor.getSelectionProvider().getSelection();

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
    public void attachAnnotations(List annotations)
    {
    }


    /**
     * {@inheritDoc}
     */
    public List detachAnnotations()
    {
        /*
        IDocumentProvider provider = editor.getDocumentProvider();
        IAnnotationModel annotations = provider.getAnnotationModel(this.editor);
        System.err.println(annotations);
        annotations = provider.getAnnotationModel(this.document);
        System.err.println(annotations);

        if (annotations != null)
        {
            for (Iterator i = annotations.getAnnotationIterator(); i.hasNext();)
            {
                System.err.println(i.next());
            }
        }
        */
        return Collections.EMPTY_LIST;
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
