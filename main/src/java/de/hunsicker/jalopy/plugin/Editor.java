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
package de.hunsicker.jalopy.plugin;

/**
 * Represents an editor view used to display and interactively modify the
 * contents of a Java source file.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public interface Editor
{
    //~ Methods ·······························································

    /**
     * Moves the caret to the given location.
     *
     * @param offset absolute character position.
     */
    public void setCaretPosition(int offset);


    /**
     * Moves the caret to the given location.
     *
     * @param line line number.
     * @param column column offset in the given line.
     */
    public void setCaretPosition(int line,
                                 int column);


    /**
     * Returns the current location of the caret.
     *
     * @return current caret position (absolute character position).
     */
    public int getCaretPosition();


    /**
     * Returns the column offset in the current line.
     *
     * @return current column offset.
     */
    public int getColumn();


    /**
     * Returns the file of the editor.
     *
     * @return the file that has its contents displayed in the editor.
     */
    public ProjectFile getFile();


    /**
     * Returns the number of characters in the editor document.
     *
     * @return The document length.
     *
     * @since 1.0b8
     */
    public int getLength();


    /**
     * Returns the current line number.
     *
     * @return current line.
     */
    public int getLine();


    /**
     * Returns the selected text contained in this editor.
     *
     * @return selected text. If no text is selected or the document is empty,
     *         returns <code>null</code>.
     */
    public String getSelectedText();


    /**
     * Selects the specified text.
     *
     * @param startOffset the offset you wish to start selection on (absolute
     *        character position).
     * @param endOffset the offset you wish to end selection on (absolute
     *        character position).
     */
    public void setSelection(int startOffset,
                             int endOffset);


    /**
     * Returns the selected text's end position.
     *
     * @return the selected text's end position (<code>&gt;=0</code>). Returns
     *         <code>0</code> if the document is empty,  or the position of
     *         the caret if there is no selection.
     */
    public int getSelectionEnd();


    /**
     * Returns the selected text's start position.
     *
     * @return the start position (<code>&gt;=0</code>). Returns
     *         <code>0</code> for an empty document, or the position of the
     *         caret if there is no selection.
     */
    public int getSelectionStart();


    /**
     * Sets the text of this editor to the specified text. If the text is
     * <code>null</code> or empty, has the effect of simply deleting the old
     * text.
     *
     * @param text the new text to be set.
     */
    public void setText(String text);


    /**
     * Returns the text contained in this editor.
     *
     * @return the text.
     */
    public String getText();


    /**
     * Replaces the currently selected content with new content represented by
     * the given string. If there is no selection this amounts to an insert
     * of the given text. If there is no replacement text this amounts to a
     * removal of the current selection.
     *
     * @param text the string to replace the selection with.
     */
    public void paste(String text);


    /**
     * Tries to set the focus on the receiving component.
     */
    public void requestFocus();


    /**
     * Selects the whole text of the editor.
     */
    public void selectAll();
}
