/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.debug;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class Comment
{
    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new Comment object.
     */
    public Comment()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] argv)
      throws Exception
    {
        /*Lexer l = new JavadocLexer();
        Parser p = l.getParser();
        Recognizer r = new Recognizer(p, l);
        r.parse(new File(argv[0]));

        AST tree = p.getParseTree();
        ((BaseAST)tree).setVerboseStringConversion(true, p.getTokenNames());

        final ASTFrame frame = new ASTFrame("Java AST", tree);
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
        });*/
    }
}
