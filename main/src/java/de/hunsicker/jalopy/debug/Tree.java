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

import de.hunsicker.antlr.BaseAST;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.JavaNode;
import de.hunsicker.jalopy.parser.JavaRecognizer;
import de.hunsicker.jalopy.parser.TreeWalker;
import de.hunsicker.jalopy.prefs.Loggers;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public final class Tree
{
    //~ Static variables/initializers ·········································

    static boolean showTree = false;

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void doFile(File f)
        throws Exception
    {
        // If this is a directory, walk each file/dir in that directory
        if (f.isDirectory())
        {
            String[] files = f.list();

            for (int i = 0; i < files.length; i++)
            {
                doFile(new File(f, files[i]));
            }
        }
        else if ((f.getName().length() > 5) &&
                 f.getName().substring(f.getName().length() - 5).equals(".java"))
        {
            // parseFile(f.getName(), new FileInputStream(f));
            parseFile(f); // , new BufferedReader(new FileReader(f)));
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     * @param t DOCUMENT ME!
     * @param tokenNames DOCUMENT ME!
     */
    public static void doTreeAction(String   f,
                                    AST      t,
                                    String[] tokenNames)
    {
        if (t == null)
        {
            return;
        }

        ((BaseAST)t).setVerboseStringConversion(true, tokenNames);

        final ASTFrame frame = new ASTFrame("Java AST", t);
        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    frame.setVisible(false);
                    frame.dispose();
                    System.exit(0);
                }
            });
    }


    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args)
        throws Exception
    {
        Loggers.initialize(new ConsoleAppender(
                                               new PatternLayout("%-5p [%t]: %m%n"),
                                               ConsoleAppender.SYSTEM_OUT));

        try
        {
            // if we have at least one command-line argument
            if (args.length > 0)
            {
                System.err.println("Parsing...");

                // for each directory/file specified on the command line
                for (int i = 0; i < args.length; i++)
                {
                    if (args[i].equals("-showtree"))
                    {
                        showTree = true;
                    }
                    else
                    {
                        doFile(new File(args[i])); // parse it
                    }
                }
            }
            else
            {
                System.err.println("Usage: java Main [-showtree] " +
                                   "<directory or file name>");
            }
        }
        catch (Exception e)
        {
            System.err.println("exception: " + e);
            e.printStackTrace(System.err); // so we can get stack trace
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static final void parseFile(File file)
        throws Exception
    {
        try
        {
            if (file.getClass() == byte.class)
            {
                ;
            }

            String f = file.getName();

            /*
               JavaLexer lexer = new JavaLexer(new java.io.BufferedReader(new java.io.FileReader(file)));
               lexer.setFilename(f);
                           JavaParser parser = new JavaParser(lexer);
                           parser.setFilename(f);
                           parser.setASTFactory(new JavaNodeFactory());
                           long start = System.currentTimeMillis();
                           parser.parse();
                           long stop = System.currentTimeMillis();
                           System.err.println(stop - start + " ms");*/
            JavaRecognizer r = new JavaRecognizer();
            long start = System.currentTimeMillis();
            r.parse(file);

            long stop = System.currentTimeMillis();
            System.err.println(stop - start);

            AST tree = r.getAST();

            new TestWalker().walk(tree);

            /*BufferedReader r = new BufferedReader(new FileReader(file));
               // Create a scanner that reads from the input stream passed to us
               JavaLexer lexer = new JavaLexer(r);
               lexer.setFilename(f);
               // Create a parser that reads from the scanner
               JavaParser parser = new JavaParser(lexer);
               parser.setASTFactory(new JavaNodeFactory());
               parser.setFilename(f);
               // start parsing at the compilationUnit rule
               long start = System.currentTimeMillis();
               parser.parse();
               long stop = System.currentTimeMillis();
               System.err.println(stop - start + " ms");*/
            /*Lexer lexer = new JavadocLexer();
               JavadocParser parser = new JavadocParser(lexer);
               parser.setASTFactory(new NodeFactory());
               Recognizer recognizer = new JavaRecognizer(parser, lexer);
               recognizer.parse(file);
               AST tree = recognizer.getAST();*/
            // Show the tree
            doTreeAction(f, tree, r.getParser().getTokenNames());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //~ Inner Classes ·························································

    // Here's where we do the real work...
    public static class TestWalker
        extends TreeWalker
    {
        public void visit(AST node)
        {
            JavaNode n = (JavaNode)node;

            if (n.getParent() == null)
            {
                System.err.println("NO PARENT " + n);
            }

            if (n.getPreviousSibling() == null)
            {
                System.err.println("NO PREV " + n);
            }
        }
    }
}
