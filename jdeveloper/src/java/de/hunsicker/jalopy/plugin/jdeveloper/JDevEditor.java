/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.util.Collections;
import java.util.List;

import javax.swing.text.BadLocationException;

import de.hunsicker.jalopy.plugin.Editor;
import de.hunsicker.jalopy.plugin.ProjectFile;
import de.hunsicker.util.ChainingRuntimeException;

import oracle.ide.Ide;
import oracle.ide.editor.AbstractEditor;
import oracle.ide.model.TextNode;
import oracle.javatools.buffer.TextBuffer;
import oracle.jdeveloper.ceditor.CodeEditor;


/**
 * The JDeveloper editor implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JDevEditor
    implements Editor
{
    //~ Instance variables ---------------------------------------------------------------

    private CodeEditor _editor;
    private ProjectFile _file;
    private TextNode _node;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JDevEditor object.
     *
     * @param file the underlying Java source file.
     * @param node the attached file node.
     */
    public JDevEditor(
        ProjectFile file,
        TextNode    node)
    {
        _file = file;
        _node = node;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        getEditor().setCaretPosition(offset);
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(
        int line,
        int column)
    {
        int offset = getEditor().getLineStartOffset(line);
        getEditor().setCaretPosition(offset + column);
    }


    /**
     * {@inheritDoc}
     */
    public int getCaretPosition()
    {
        return getEditor().getCaretPosition();
    }


    /**
     * {@inheritDoc}
     */
    public int getColumn()
    {
        int currentLine = getEditor().getLineFromOffset(getCaretPosition());
        int linePosition = getEditor().getLineStartOffset(currentLine);

        return getCaretPosition() - linePosition;
    }


    /**
     * {@inheritDoc}
     */
    public ProjectFile getFile()
    {
        return _file;
    }


    /**
     * {@inheritDoc}
     */
    public int getLength()
    {
        TextBuffer buffer = _node.acquireTextBuffer();

        try
        {
            return buffer.getLength();
        }
        finally
        {
            if (buffer != null)
            {
                _node.releaseTextBuffer();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    public int getLine()
    {
        return getEditor().getLineFromOffset(getCaretPosition());
    }


    /**
     * {@inheritDoc}
     */
    public String getSelectedText()
    {
        return getEditor().getSelectedText();
    }


    /**
     * {@inheritDoc}
     */
    public void setSelection(
        int startOffset,
        int endOffset)
    {
        getEditor().setSelectionStart(startOffset);
        getEditor().setSelectionEnd(endOffset);
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionEnd()
    {
        return getEditor().getSelectionEnd();
    }


    /**
     * {@inheritDoc}
     */
    public int getSelectionStart()
    {
        return getEditor().getSelectionStart();
    }


    /**
     * {@inheritDoc}
     */
    public void setText(String text)
    {
        TextBuffer buffer = _node.acquireTextBuffer();

        try
        {
            buffer.beginEdit();
            buffer.removeToEnd(0);
            buffer.insert(0, text.toCharArray());
        }
        finally
        {
            if (buffer != null)
            {
                buffer.endEdit();
                _node.releaseTextBuffer();
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    public String getText()
    {
        try
        {
            return getEditor().getText(0, getLength());
        }
        catch (BadLocationException ex)
        {
            // should really never happen
            throw new ChainingRuntimeException(ex);
        }
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
        getEditor().replaceSelection(text);
    }


    /**
     * {@inheritDoc}
     */
    public void requestFocus()
    {
        //getEditor().requestFocus();
    }


    /**
     * {@inheritDoc}
     */
    public void selectAll()
    {
        setSelection(0, getLength());
    }


    private CodeEditor getEditor()
    {
        if (_editor == null)
        {
            List editors = Ide.getEditorManager().getAllEditors();

            for (int i = 0, size = editors.size(); i < size; i++)
            {
                AbstractEditor editor = (AbstractEditor) editors.get(i);

                if (editor instanceof CodeEditor)
                {
                    if (_node.equals(editor.getContext().getElement()))
                    {
                        _editor = (CodeEditor) editor;

                        break;
                    }
                }
            }
        }

        if (_editor != null)
        {
            return _editor;
        }
        else
        {
            throw new IllegalStateException("could not find an editor for " + _node);
        }
    }
}
