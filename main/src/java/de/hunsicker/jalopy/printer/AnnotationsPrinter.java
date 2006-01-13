package de.hunsicker.jalopy.printer;

import java.io.IOException;

import antlr.collections.AST;


/**
 * The annotation printer
 */
public class AnnotationsPrinter extends AbstractPrinter {
    /** Singleton. */
    private static final Printer INSTANCE = new AnnotationsPrinter();

    /**
     * Single instance printer
     */
    public AnnotationsPrinter() {
        super();
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

    }

}
