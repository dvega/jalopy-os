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
package de.hunsicker.jalopy.plugin.jbuilder;

import com.borland.primetime.editor.EditorManager;
import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.node.FileNode;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;


/**
 * The JBuilder editor implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JbEditor
    implements Editor
{
    //~ Instance variables ····················································

    FileNode node;
    ProjectFile file;

    //~ Constructors ··························································

    /**
     * Creates a new JbEditor object.
     *
     * @param file the underlying Java source file.
     * @param node the attached file node.
     */
    public JbEditor(ProjectFile file,
                    FileNode    node)
    {
        this.file = file;
        this.node = node;
    }

    //~ Methods ·······························································

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        int line = getEditorPane().getLineNumber(offset);

        if (line <= getEditorPane().getLineCount())
        {
            getEditorPane().setCaretPosition(line, 1);
        }
        else
        {
            getEditorPane().setCaretPosition(1, 1);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int line,
                                 int column)
    {
        getEditorPane().gotoPosition(line, column);
    }


    /**
     * {@inheritDoc}
     */
    public int getCaretPosition()
    {
        return getEditorPane().getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        return getEditorPane().getColumnNumber(getCaretPosition());
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
        return getEditorPane().getDocument().getLength();
    }


    /**
     * {@inheritDoc}
     */
    public int getLine()
    {
        return getEditorPane().getLineNumber(getCaretPosition());
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        return getEditorPane().getSelectedText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(int startOffset,
                             int endOffset)
    {
        getEditorPane().setSelectionStart(startOffset);
        getEditorPane().setSelectionEnd(endOffset);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        return getEditorPane().getSelectionEnd();
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionStart()
    {
        return getEditorPane().getSelectionStart();
    }


    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        getEditorPane().setText(text);
    }


    /**
     * {@inheritDoc}
     */
    public String getText()
    {
        return getEditorPane().getText();
    }


    /**
     * {@inheritDoc}
     */
    public void paste(String text)
    {
        getEditorPane().replaceSelection(text);
    }


    /**
     * {@inheritDoc}
     */
    public void requestFocus()
    {
        getEditorPane().requestFocus();
    }


    /**
     * {@inheritDoc}
     */
    public void selectAll()
    {
        getEditorPane().selectAll();
    }


    private EditorPane getEditorPane()
    {
        return EditorManager.getEditor(this.node);
    }
}
