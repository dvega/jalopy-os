/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
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
    //~ Instance variables ---------------------------------------------------------------

    JTree tree;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JTreeASTPanel object.
     *
     * @param tm DOCUMENT ME!
     * @param listener DOCUMENT ME!
     */
    public JTreeASTPanel(
        TreeModel             tm,
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
        tree.addMouseListener(
            new MouseAdapter()
            {
                public void mouseReleased(MouseEvent ev)
                {
                    if (ev.isPopupTrigger())
                    {
                        java.awt.Point point = new java.awt.Point(ev.getX(), ev.getY());
                        SwingUtilities.convertPointToScreen(point, tree);
                        popupMenu.setInvoker(tree);
                        popupMenu.setLocation(point.x, point.y);
                        popupMenu.setVisible(true);
                    }
                }
            });
    }

    //~ Inner Classes --------------------------------------------------------------------

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
