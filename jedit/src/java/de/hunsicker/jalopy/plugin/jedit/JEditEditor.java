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
package de.hunsicker.jalopy.plugin.jedit;

import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.Selection;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;


/**
 * The JEdit editor implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JEditEditor
    implements Editor
{
    //~ Instance variables ····················································

    /** The 'physical' editor. */
    JEditTextArea textArea;
    ProjectFile file;

    //~ Constructors ··························································

    /**
     * Creates a new JEditEditor object.
     *
     * @param file the underlying Java source file.
     * @param textArea the underlying editor.
     */
    public JEditEditor(ProjectFile   file,
                       JEditTextArea textArea)
    {
        this.file = file;
        this.textArea = textArea;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        this.textArea.setCaretPosition(offset);
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int line,
                                 int column)
    {
        setCaretPosition(line, column);
    }


    /**
     * {@inheritDoc}
     */
    public int getCaretPosition()
    {
        return this.textArea.getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        return this.textArea.getMagicCaretPosition();
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
        return this.textArea.getBuffer().getLength();
    }


    /**
     * {@inheritDoc}
     */
    public int getLine()
    {
        return this.textArea.getCaretLine();
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        return this.textArea.getSelectedText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(int startOffset,
                             int endOffset)
    {
        Selection selection = new Selection.Range(startOffset, endOffset);
        this.textArea.setSelection(selection);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        Selection[] selection = this.textArea.getSelection();

        if (selection.length > 0)
        {
            return selection[selection.length - 1].getStart();
        }

        return getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionStart()
    {
        Selection[] selection = this.textArea.getSelection();

        if (selection.length > 0)
        {
            return selection[0].getStart();
        }

        return getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        this.textArea.setText(text);
    }


    /**
     * {@inheritDoc}
     */
    public String getText()
    {
        return this.textArea.getText();
    }


    /**
     * {@inheritDoc}
     */
    public void paste(String text)
    {
        this.textArea.setSelectedText(text);
    }


    /**
     * {@inheritDoc}
     */
    public void requestFocus()
    {
        this.textArea.requestFocus();
    }


    /**
     * {@inheritDoc}
     */
    public void selectAll()
    {
        this.textArea.selectAll();
    }
}
