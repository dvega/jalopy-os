package de.hunsicker.jalopy.printer;

import java.io.IOException;

import antlr.collections.AST;


/**
 * TODO 
 */
public class AnnotationsPrinter extends AbstractPrinter {
    /** Singleton. */
    private static final Printer INSTANCE = new AnnotationsPrinter();

    /**
     * 
     */
    public AnnotationsPrinter() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Returns the sole instance of this class.
     *
     * @return the sole instance of this class.
     */
    public static final Printer getInstance()
    {
        return INSTANCE;
    }

    /** 
     * TODO 
     *
     * @param node
     * @param out
     * @throws IOException
     */
    public void print(AST node, NodeWriter out) throws IOException {
        // TODO Complete annotations implementation
        System.out.println("TODO Annotations " + node);
        //out.print("TODO Annotations",node.getType());

    }

}
