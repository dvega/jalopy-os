/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.netbeans;

import java.util.Collections;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyledDocument;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;

import org.openide.text.*;
import org.openide.text.Line;


/**
 * The NetBeans Editor implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class NbEditor
    implements Editor
{
    //~ Instance variables ---------------------------------------------------------------

    /** The editor view. */
    JEditorPane pane;

    /** The underlying Java source file. */
    ProjectFile file;

    /** Provides per-line access to the editor contents. */
    Line.Set lines;

    /** The editor document. */
    StyledDocument document;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates new NbEditor object.
     *
     * @param file the underlying Java source file.
     * @param pane the physical editor pane.
     * @param lines set with the actual editor lines.
     */
    public NbEditor(
        ProjectFile file,
        JEditorPane pane,
        Line.Set    lines)
    {
        this.file = file;
        this.pane = pane;
        this.lines = lines;
        this.document = (StyledDocument) pane.getDocument();
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        this.pane.setCaretPosition(offset);
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(
        int line,
        int column)
    {
        try
        {
            int offset =
                (NbDocument.findLineOffset(this.document, line - 1) + column) - 1;
            setCaretPosition(offset);
        }
        catch (Throwable ex)
        {
            ;
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getCaretPosition()
    {
        return this.pane.getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        try
        {
            return NbDocument.findLineColumn(this.document, getCaretPosition()) + 1;
        }
        catch (Throwable ex)
        {
            return 1;
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
        try
        {
            return NbDocument.findLineNumber(this.document, getCaretPosition()) + 1;
        }
        catch (Throwable ex)
        {
            return 1;
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getLineCount()
    {
        return this.lines.getLines().size();
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        return this.pane.getSelectedText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(
        int startOffset,
        int endOffset)
    {
        this.pane.setSelectionStart(startOffset);
        this.pane.setSelectionEnd(endOffset);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        return this.pane.getSelectionEnd();
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionStart()
    {
        return this.pane.getSelectionStart();
    }


    /**
     * {@inheritDoc}
     */
    public void setText(final String text)
    {
        final AttributeSet attributes =
            this.document.getDefaultRootElement().getAttributes();

        /*NbDocument.runAtomic(
            this.document,
            new Runnable()
            {
                public void run()
                {
                    try
                    {
                        NbDocument.unmarkGuarded(document, 0, document.getLength());

                        document.remove(0, document.getLength());
                        document.insertString(0, text, attributes);
                    }
                    catch (Throwable ex)
                    {
                        System.out.println(ex.getClass());
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });*/
        this.pane.setText(text);
    }


    /**
     * {@inheritDoc}
     */
    public String getText()
    {
        return this.pane.getText();
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
        return Collections.EMPTY_LIST;
    }


    /**
     * {@inheritDoc}
     */
    public void paste(String text)
    {
        this.pane.replaceSelection(text);
    }


    /**
     * {@inheritDoc}
     */
    public void requestFocus()
    {
        this.pane.requestFocus();
    }


    /**
     * {@inheritDoc}
     */
    public void selectAll()
    {
        this.pane.selectAll();
    }
}
