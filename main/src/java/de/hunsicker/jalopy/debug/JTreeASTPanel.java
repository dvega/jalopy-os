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
package de.hunsicker.jalopy.debug;


/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/RIGHTS.html
 *
 * $Id$
 */
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class JTreeASTPanel
    extends JPanel
{
    //~ Instance variables ····················································

    JTree tree;

    //~ Constructors ··························································

    /**
     * Creates a new JTreeASTPanel object.
     *
     * @param tm DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public JTreeASTPanel(TreeModel             tm,
                         TreeSelectionListener listener)
    {
        // use a layout that will stretch tree to panel size
        setLayout(new BorderLayout());

        // Create tree
        tree = new JTree(tm);

        // Change line style
        //tree.putClientProperty("JTree.lineStyle", "Angled");
        // Add TreeSelectionListener
        if (listener != null)
        {
            tree.addTreeSelectionListener(listener);
        }

        // Put tree in a scrollable pane's viewport
        JScrollPane sp = new JScrollPane();
        sp.getViewport().add(tree);
        add(sp, BorderLayout.CENTER);

        final JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setDefaultLightWeightPopupEnabled(true);
        popupMenu.setLightWeightPopupEnabled(true);

        final Action updateAction = new UpdateAction();
        updateAction.setEnabled(true);
        popupMenu.add(updateAction);
        tree.addMouseListener(new MouseAdapter()
            {
                public void mouseReleased(MouseEvent ev)
                {
                    if (ev.isPopupTrigger())
                    {
                        java.awt.Point point = new java.awt.Point(ev.getX(),
                                                                  ev.getY());
                        SwingUtilities.convertPointToScreen(point, tree);
                        popupMenu.setInvoker(tree);
                        popupMenu.setLocation(point.x, point.y);
                        popupMenu.setVisible(true);
                    }
                }
            });
    }

    //~ Inner Classes ·························································

    private class UpdateAction
        extends AbstractAction
    {
        public UpdateAction()
        {
            super.putValue(Action.NAME, "Reload");
        }

        public void actionPerformed(ActionEvent ev)
        {
            System.err.println("Reload");
        }
    }
}
