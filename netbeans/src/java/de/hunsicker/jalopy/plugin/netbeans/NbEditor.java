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

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;

import javax.swing.JEditorPane;
import javax.swing.text.StyledDocument;

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
    //~ Instance variables ····················································

    /** The editor view. */
    JEditorPane pane;

    /** The underlying Java source file. */
    ProjectFile file;

    /** Provides per-line access to the editor contents. */
    Line.Set lines;

    /** The editor document. */
    StyledDocument document;

    //~ Constructors ··························································

    /**
     * Creates new NbEditor object.
     *
     * @param file the underlying Java source file.
     * @param pane the physical editor pane.
     * @param lines set with the actual editor lines.
     */
    public NbEditor(ProjectFile file,
                    JEditorPane pane,
                    Line.Set    lines)
    {
        this.file = file;
        this.pane = pane;
        this.lines = lines;
        this.document = (StyledDocument)pane.getDocument();
    }

    //~ Methods ·······························································

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
    public void setCaretPosition(int line,
                                 int column)
    {
        throw new UnsupportedOperationException();
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
        /**
         * @todo how can this implemented with reasonable performance?
         */

        /*
           try
           {
               return NbDocument.findLineColumn(this.document, getCaretPosition());
           }
           catch (Exception ex)
           {
               return 1;
           }
        */
        throw new UnsupportedOperationException();
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
        /**
         * @todo how can this implemented with reasonable performance?
         */

        /*
           try
           {
               return NbDocument.findLineNumber(this.document, getCaretPosition());
           }
           catch (Exception ex)
           {
               return 1;
           }
        */
        throw new UnsupportedOperationException();
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
    public void setSelection(int startOffset,
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
    public void setText(String text)
    {
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
