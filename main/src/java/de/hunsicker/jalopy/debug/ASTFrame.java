/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.debug;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/* ANTLR Translator Generator
 * Project led by Terence Parr at http://www.jGuru.com
 * Software rights: http://www.antlr.org/RIGHTS.html
 *
 * $Id$
 */
import antlr.ASTFactory;
import antlr.CommonAST;
import antlr.collections.AST;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ASTFrame
    extends JFrame
{
    //~ Static variables/initializers ----------------------------------------------------

    static final int HEIGHT = 300;

    // The initial width and height of the frame
    static final int WIDTH = 200;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ASTFrame object.
     *
     * @param lab DOCUMENT ME!
     * @param r DOCUMENT ME!
     */
    public ASTFrame(
        String lab,
        AST    r)
    {
        super(lab);

        // Create the TreeSelectionListener
        JTreeASTPanel tp = new JTreeASTPanel(new JTreeASTModel(r), null);
        Container content = getContentPane();
        content.add(tp, BorderLayout.CENTER);
        addWindowListener(
            new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    Frame f = (Frame) e.getSource();
                    f.setVisible(false);
                    f.dispose();

                    // System.exit(0);
                }
            });
        setSize(WIDTH, HEIGHT);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args)
    {
        // Create the tree nodes
        ASTFactory factory = new ASTFactory();
        CommonAST r = (CommonAST) factory.create(0, "ROOT");
        r.addChild(factory.create(0, "C1"));
        r.addChild(factory.create(0, "C2"));
        r.addChild(factory.create(0, "C3"));

        ASTFrame frame = new ASTFrame("AST JTree Example", r);
        frame.setVisible(true);
    }

    //~ Inner Classes --------------------------------------------------------------------

    class MyTreeSelectionListener
        implements TreeSelectionListener
    {
        public void valueChanged(TreeSelectionEvent event)
        {
            TreePath path = event.getPath();
            System.out.println("Selected: " + path.getLastPathComponent());

            Object[] elements = path.getPath();

            for (int i = 0; i < elements.length; i++)
            {
                System.out.print("->" + elements[i]);
            }

            System.out.println();
        }
    }
}
