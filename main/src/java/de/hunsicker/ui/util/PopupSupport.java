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
package de.hunsicker.ui.util;

import java.awt.AWTEvent;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;


/**
 * Helper class which adds popup menu support for {@link
 * javax.swing.text.JTextComponent text components}.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class PopupSupport
{
    //~ Static variables/initializers ·········································

    private static final Comparator COMPARATOR = new PartialStringComparator();

    //~ Instance variables ····················································

    /** The copy action. */
    private Action _copy;

    /* The cut action. */
    private Action _cut;

    /** The delete action. */
    private Action _delete;

    /** The paste action. */
    private Action _paste;

    /** The select all action. */
    private Action _selectAll;

    /** The focus event interceptor. */
    private FocusInterceptor _interceptor;

    /** The popup menu to display. */
    private JPopupMenu _menu;

    /** Holds a list of all text components that have popup support. */
    private List _registeredComponents; // List of <ListenerSupport>

    /**
     * List with the package names for which popup support should be enabled.
     */
    private final List _supported; // List of <String>

    //~ Constructors ··························································

    /**
     * Creates a new PopupSuppport object. The popup menu support is initially
     * enabled. The popup support will only be added for components of the
     * <code>javax.swing</code> hierachy.
     */
    public PopupSupport()
    {
        this(true);
    }


    /**
     * Creates a new PopupSuppport object. The popup menu support is initially
     * enabled for the given hierachies or classes.
     *
     * @param supported supported package hierarchies or classes.
     */
    public PopupSupport(List supported)
    {
        this(true, supported);
    }


    /**
     * Creates a new PopupSuppport object.
     *
     * @param enable if <code>true</code> the popup menu support will be
     *        initially enabled.
     * @param supported supported package hierarchies or classes.
     */
    public PopupSupport(boolean    enable,
                        final List supported)
    {
        if (enable)
        {
            setEnabled(true);
        }

        _supported = new ArrayList(supported);
        Collections.sort(_supported);
    }


    /**
     * Creates a new PopupSuppport object.
     *
     * @param enable if <code>true</code> the popup menu support will be
     *        initially enabled.
     */
    public PopupSupport(boolean enable)
    {
        if (enable)
        {
            setEnabled(true);
        }

        _supported = new ArrayList(3);
        _supported.add("javax.");
    }

    //~ Methods ·······························································

    /**
     * Sets the status of the popup menu support.
     *
     * @param enable if <code>true</code> the popup menu support will be
     *        enabled.
     */
    public void setEnabled(boolean enable)
    {
        if (enable)
        {
            if (_interceptor == null)
            {
                _interceptor = new FocusInterceptor();
                Toolkit.getDefaultToolkit()
                       .addAWTEventListener(_interceptor,
                                            AWTEvent.FOCUS_EVENT_MASK);
            }
        }
        else
        {
            Toolkit.getDefaultToolkit().removeAWTEventListener(_interceptor);
            _interceptor = null;

            // if no text component have ever got the focus no listeners were
            // added, so we have to check
            if (_registeredComponents != null)
            {
                for (int i = 0, size = _registeredComponents.size();
                     i < size;
                     i++)
                {
                    ListenerSupport support = (ListenerSupport)_registeredComponents.get(i);
                    support.remove();
                }
            }

            _registeredComponents = null;
            _copy = null;
            _cut = null;
            _selectAll = null;
            _delete = null;
            _menu = null;
            _paste = null;
        }
    }


    /**
     * Adds a default popup menu for the given component.
     *
     * @param component component to add the popup menu to.
     */
    public void addSupport(JTextComponent component)
    {
        if (component == null)
        {
            return;
        }

        if (_registeredComponents == null)
        {
            _registeredComponents = new ArrayList();
        }

        if (!_registeredComponents.contains(new ListenerSupport(component)))
        {
            ListenerSupport support = new ListenerSupport(component,
                                                          new MouseHandler(),
                                                          new KeyHandler(),
                                                          new CaretHandler(),
                                                          new DocumentHandler());
            _registeredComponents.add(support);
        }
        else
        {
            updateSelectAllAction(component.getDocument());
            updateDeleteAction(component);
        }
    }


    /**
     * Indicates whether the system clipboard contains text content.
     *
     * @return <code>true</code> if the system clipboard contains no text
     *         content.
     */
    protected boolean isClipboardEmpty()
    {
        Transferable data = Toolkit.getDefaultToolkit().getSystemClipboard()
                                   .getContents(this);

        if ((data == null) ||
            (!data.isDataFlavorSupported(DataFlavor.stringFlavor)))
        {
            return true;
        }

        return false;
    }


    /**
     * Returns the popup menu for the given component. The default
     * implementation returns the same default popup menu for all components.
     * 
     * <p>
     * This popup menu consists of five actions:
     * </p>
     * <pre>
     * +------------+
     * | Copy       |
     * | Cut        |
     * | Paste      |
     * | Delete     |
     * +------------+
     * | Select all |
     * +------------+
     * </pre>
     *
     * @param component component to return the popup menu for.
     *
     * @return the popup menu for the given component.
     */
    protected JPopupMenu getPopup(JTextComponent component)
    {
        if (_menu == null)
        {
            _menu = new JPopupMenu();

            Action[] actions = component.getActions();

            for (int i = 0; i < actions.length; i++)
            {
                Object value = actions[i].getValue(Action.NAME);

                if (value.equals(DefaultEditorKit.cutAction))
                {
                    _cut = actions[i];
                    updateCopyCutAction(component);
                }
                else if (value.equals(DefaultEditorKit.copyAction))
                {
                    _copy = actions[i];
                    updateCopyCutAction(component);
                }
                else if (value.equals(DefaultEditorKit.pasteAction))
                {
                    _paste = actions[i];
                    updatePasteAction(component);
                }
                else if (value.equals(DefaultEditorKit.selectAllAction))
                {
                    _selectAll = actions[i];
                    updateSelectAllAction(component.getDocument());
                }
            }

            if (_cut != null)
            {
                JMenuItem item = new JMenuItem(_cut);
                item.setText("Cut");
                _menu.add(item);
            }

            if (_copy != null)
            {
                JMenuItem item = new JMenuItem(_copy);
                item.setText("Copy");
                _menu.add(item);
            }

            if (_paste != null)
            {
                JMenuItem item = new JMenuItem(_paste);
                item.setText("Paste");
                _menu.add(item);
            }

            _delete = new DeleteAction();
            updateDeleteAction(component);
            _menu.add(_delete);

            if (_selectAll != null)
            {
                _menu.add(new JPopupMenu.Separator());

                JMenuItem item = new JMenuItem(_selectAll);
                item.setText("Select all");
                _menu.add(item);
            }
        }

        return _menu;
    }


    /**
     * Indicates whether a text selection exists.
     *
     * @param start start offset of the text's selection start.
     * @param end end offset of the text's selection end.
     *
     * @return <code>true</code> if <code>start &gt; end</code>.
     */
    protected boolean isTextSelected(int start,
                                     int end)
    {
        if (start == end)
        {
            return false;
        }

        return true;
    }


    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    private void updateCopyCutAction(JTextComponent component)
    {
        int startOffset = component.getSelectionStart();
        int stopOffset = component.getSelectionEnd();

        if (isTextSelected(startOffset, stopOffset))
        {
            if (_copy != null)
            {
                _copy.setEnabled(true);
            }

            if ((_cut != null) && component.isEditable())
            {
                _cut.setEnabled(true);
            }
        }
        else
        {
            if (_copy != null)
            {
                _copy.setEnabled(false);
            }

            if (_cut != null)
            {
                _cut.setEnabled(false);
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param document DOCUMENT ME!
     */
    private void updateDeleteAction(Document document)
    {
        if (document.getLength() > 0)
        {
            if (_delete != null)
            {
                _delete.setEnabled(true);
            }
        }
        else
        {
            if (_delete != null)
            {
                _delete.setEnabled(false);
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    private void updateDeleteAction(JTextComponent component)
    {
        if ((component.getDocument().getLength() > 0) && component.isEditable())
        {
            if (_delete != null)
            {
                _delete.setEnabled(true);
            }
        }
        else
        {
            if (_delete != null)
            {
                _delete.setEnabled(false);
            }
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    private void updatePasteAction(JTextComponent component)
    {
        if (_paste != null)
        {
            if ((!isClipboardEmpty()) && component.isEditable())
            {
                _paste.setEnabled(true);
            }
            else
            {
                _paste.setEnabled(false);
            }
        }
    }


    /**
     * Updates the state of the select-all text action depending on the state
     * of the given document.
     *
     * @param document document of a text component.
     */
    private void updateSelectAllAction(Document document)
    {
        if (document.getLength() > 0)
        {
            if (_selectAll != null)
            {
                _selectAll.setEnabled(true);
            }
        }
        else
        {
            if (_selectAll != null)
            {
                _selectAll.setEnabled(false);
            }
        }
    }

    //~ Inner Classes ·························································

    /**
     * Helper class to bundle a component and associated listeners so we are
     * able to correctly remove listenes after we're done.
     */
    private static class ListenerSupport
    {
        CaretListener caretHandler;
        DocumentListener documentHandler;
        JTextComponent component;
        KeyListener keyHandler;
        MouseListener mouseHandler;

        /**
         * Creates a new ListenerSupport object.
         *
         * @param component the component to add popup support to.
         */
        public ListenerSupport(JTextComponent component)
        {
            this.component = component;
        }


        /**
         * Creates a new ListenerSupport object.
         *
         * @param component the component to add popup support to.
         * @param mouseListener mouse listener to add.
         * @param keyListener key listener to add.
         * @param caretListener caret listener to add.
         * @param documentListener document listener to add.
         */
        public ListenerSupport(JTextComponent   component,
                               MouseListener    mouseListener,
                               KeyListener      keyListener,
                               CaretListener    caretListener,
                               DocumentListener documentListener)
        {
            this(component);
            this.mouseHandler = mouseListener;
            this.keyHandler = keyListener;
            this.caretHandler = caretListener;
            this.documentHandler = documentListener;
            add();
        }

        public void add()
        {
            this.component.addMouseListener(this.mouseHandler);
            this.component.addKeyListener(this.keyHandler);
            this.component.getDocument()
                          .addDocumentListener(this.documentHandler);
            this.component.addCaretListener(this.caretHandler);
        }


        public boolean equals(Object o)
        {
            if (o instanceof JTextComponent)
            {
                return this.component.equals(o);
            }
            else if (o instanceof ListenerSupport)
            {
                return this.component.equals(((ListenerSupport)o).component);
            }

            return false;
        }


        public int hashCode()
        {
            return this.component.hashCode();
        }


        public void remove()
        {
            this.component.removeMouseListener(this.mouseHandler);
            this.component.removeKeyListener(this.keyHandler);
            this.component.getDocument()
                          .removeDocumentListener(this.documentHandler);
            this.component.removeCaretListener(this.caretHandler);
        }
    }


    private static class PartialStringComparator
        implements Comparator
    {
        public int compare(Object o1,
                           Object o2)
        {
            String s1 = (String)o1;
            String s2 = (String)o1;

            if (s2.startsWith(s1))
            {
                return 0;
            }

            return s1.compareTo(s2);
        }
    }


    /**
     * Handler to update the state of the actions for caret events.
     */
    private class CaretHandler
        implements CaretListener
    {
        /**
         * Creates a new CaretHandler object.
         */
        public CaretHandler()
        {
        }

        public void caretUpdate(CaretEvent ev)
        {
            JTextComponent component = (JTextComponent)ev.getSource();
            updatePasteAction(component);
            updateCopyCutAction(component); // ,ev.getDot(), ev.getMark()
        }
    }


    /**
     * Actions which depending on the selection state of a text component
     * either deletes the selection or the whole text.
     */
    private static class DeleteAction
        extends TextAction
    {
        /**
         * Creates a new DeleteAction object.
         */
        public DeleteAction()
        {
            super("clear-action");
            putValue(Action.NAME, "Delete");
            this.setEnabled(false);
        }

        public void actionPerformed(ActionEvent ev)
        {
            JTextComponent target = getTextComponent(ev);

            if (target == null)
            {
                return;
            }

            Caret caret = target.getCaret();
            int curPos = caret.getDot();
            int markPos = caret.getMark();

            if (curPos != markPos)
            {
                try
                {
                    int span = markPos - curPos;

                    if (span < 0)
                    {
                        span = (span * -1);
                        curPos = markPos;
                    }

                    Document document = target.getDocument();
                    document.remove(curPos, span);
                }
                catch (Exception neverOccurs)
                {
                    ;
                }
            }
            else
            {
                target.setText("");
            }
        }
    }


    /**
     * Handler which updates the state of the action for document events.
     */
    private class DocumentHandler
        implements DocumentListener
    {
        public void changedUpdate(DocumentEvent ev)
        {
            updateSelectAllAction(ev.getDocument());
            updateDeleteAction(ev.getDocument());
        }


        public void insertUpdate(DocumentEvent ev)
        {
            updateSelectAllAction(ev.getDocument());
            updateDeleteAction(ev.getDocument());
        }


        public void removeUpdate(DocumentEvent ev)
        {
            updateSelectAllAction(ev.getDocument());
            updateDeleteAction(ev.getDocument());
        }
    }


    /**
     * Handler which 'spies' on the AWT event dispatching thread and
     * intercepts focus events in order to add a popup menu to a text
     * component which just gained the input focus.
     */
    private class FocusInterceptor
        implements AWTEventListener
    {
        public void eventDispatched(AWTEvent ev)
        {
            if (ev.getID() == FocusEvent.FOCUS_GAINED)
            {
                if (ev.getSource() instanceof JTextComponent)
                {
                    if (Collections.binarySearch(_supported,
                                                 ev.getSource().getClass()
                                                   .getName(), COMPARATOR) > -1)
                    {
                        addSupport((JTextComponent)ev.getSource());
                    }
                }
            }
        }
    }


    /**
     * Handler which updates the state of the action for keyboard events.
     */
    private class KeyHandler
        extends KeyAdapter
    {
        public void keyPressed(KeyEvent ev)
        {
            if (ev.isShiftDown() && (ev.getKeyCode() == KeyEvent.VK_F10))
            {
                JTextComponent component = (JTextComponent)ev.getSource();

                if (component.isShowing())
                {
                    try
                    {
                        Rectangle r = component.modelToView(component.getCaretPosition());
                        getPopup(component).show(component, r.x, r.y);
                    }
                    catch (BadLocationException ignored)
                    {
                        ;
                    }
                }
            }
        }
    }


    /**
     * Handler which updates the state of the actions for mouse events.
     */
    private class MouseHandler
        extends MouseAdapter
    {
        public void mousePressed(MouseEvent ev)
        {
            ((JComponent)ev.getSource()).requestFocus();
        }


        public void mouseReleased(MouseEvent ev)
        {
            if (ev.isPopupTrigger())
            {
                JTextComponent component = (JTextComponent)ev.getSource();
                getPopup(component).show(component, ev.getX(), ev.getY());
            }
        }
    }
}
