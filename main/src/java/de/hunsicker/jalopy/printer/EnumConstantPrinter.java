package de.hunsicker.jalopy.printer;

import java.io.IOException;

import antlr.collections.AST;


/**
 * TODO 
 */
public class EnumConstantPrinter extends AbstractPrinter {
    /** Singleton. */
    private static final Printer INSTANCE = new EnumConstantPrinter();

    /**
     * 
     */
    public EnumConstantPrinter() {
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
        printChildren(node,out);

    }

}
