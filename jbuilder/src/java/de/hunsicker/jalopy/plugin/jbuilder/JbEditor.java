/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jbuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.borland.jbuilder.debugger.BreakpointTreeModel;
import com.borland.jbuilder.node.JBProject;
import com.borland.primetime.editor.BookmarkManager;
import com.borland.primetime.editor.EditorDocument;
import com.borland.primetime.editor.EditorManager;
import com.borland.primetime.editor.EditorPane;
import com.borland.primetime.editor.Gutter;
import com.borland.primetime.editor.LineMark;
import com.borland.primetime.ide.Browser;
import com.borland.primetime.node.FileNode;
import com.borland.primetime.viewer.NodeViewMap;

import de.hunsicker.jalopy.language.Annotation;
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
    //~ Static variables/initializers ----------------------------------------------------

    private static final String MARK_BREAKPOINT =
        "com.borland.jbuilder.debugger.BreakpointMark" /* NOI18N */;
    private static final String MARK_BOOK =
        "com.borland.primetime.editor.BookmarkManager$EditorMark" /* NOI18N */;

    //~ Instance variables ---------------------------------------------------------------

    EditorPane pane;
    FileNode node;
    ProjectFile file;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JbEditor object.
     *
     * @param file the underlying Java source file.
     * @param node the attached file node.
     */
    public JbEditor(
        ProjectFile file,
        FileNode    node)
    {
        this.file = file;
        this.node = node;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(int offset)
    {
        if (offset < getLength())
        {
            getEditorPane().gotoOffset(offset);
        }
    }


    /**
     * {@inheritDoc}
     */
    public void setCaretPosition(
        int line,
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
    public void setSelection(
        int startOffset,
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
    public void attachAnnotations(List annotations)
    {
        EditorPane pane = getEditorPane();
        EditorDocument document = (EditorDocument) pane.getDocument();
        Gutter gutter = NodeViewMap.getView(pane).getGutter();
        BreakpointTreeModel model = getBreakpointTreeModel();

        for (int i = 0, size = annotations.size(); i < size; i++)
        {
            Annotation annotation = (Annotation) annotations.get(i);

            String classname = (String) annotation.getData();

            if (MARK_BREAKPOINT.equals(classname))
            {
                model.toggleBreakpoint(gutter, annotation.getLine());
            }
            else if (MARK_BOOK.equals(classname))
            {
                BookmarkManager.toggleBookmark(annotation.getLine(), pane);
            }
        }

        annotations.clear();
    }


    /**
     * {@inheritDoc}
     */
    public List detachAnnotations()
    {
        List result = Collections.EMPTY_LIST;

        EditorPane pane = getEditorPane();
        EditorDocument document = (EditorDocument) pane.getDocument();
        Gutter gutter = NodeViewMap.getView(pane).getGutter();
        BreakpointTreeModel model = getBreakpointTreeModel();

        for (int i = 0, lines = pane.getLineCount(); i < lines; i++)
        {
            LineMark[] marks = document.getLineMarks(i);

            if (marks.length > 0)
            {
                if (result == Collections.EMPTY_LIST)
                {
                    result = new ArrayList();
                }

                for (int j = 0; j < marks.length; j++)
                {
                    String classname = marks[j].getClass().getName();

                    if (MARK_BREAKPOINT.equals(classname))
                    {
                        model.toggleBreakpoint(gutter, i + 1);
                        result.add(new Annotation(i + 1, MARK_BREAKPOINT));
                    }
                    else if (MARK_BOOK.equals(classname))
                    {
                        //BookmarkManager.toggleBookmark(i + 1, pane);
                        result.add(new Annotation(i + 1, MARK_BOOK));
                    }
                }
            }
        }

        BookmarkManager.clearBookmarks(Browser.getActiveBrowser(), pane);

        return result;
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


    /**
     * Returns the BreakpointTreeModel for the active browser.
     *
     * @return BreakpointTreeModel
     *
     * @since 0.7.5
     */
    private BreakpointTreeModel getBreakpointTreeModel()
    {
        JBProject project =
            (JBProject) Browser.getActiveBrowser().getProjectView().getActiveProject();

        return project.getDebugInfoManager().getBreakpointTreeModel();
    }


    /**
     * Returns the JBuilder EditorPane for this editor.
     *
     * @return the underlying JBuilder EditorPane for this editor.
     */
    private EditorPane getEditorPane()
    {
        if (this.pane == null)
        {
            this.pane = EditorManager.getEditor(this.node);
        }

        return this.pane;
    }
}
