package de.hunsicker.jalopy.printer;

import java.io.IOException;

import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;

import antlr.collections.AST;


/**
 * The EnumConstant printer 
 */
public class EnumConstantPrinter extends AbstractPrinter {
    /** Singleton. */
    private static final Printer INSTANCE = new EnumConstantPrinter();

    /**
     * 
     */
    public EnumConstantPrinter() {
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
     * Prints the children. If child will exceed line length a new line is printed
     *
     * @param node The node
     * @param out The node writer
     * @throws IOException If an error occurs
     */
    public void print(AST node, NodeWriter out) throws IOException {
        TestNodeWriter tester = out.testers.get();
        
        tester.reset(out);
        printChildren(node,tester);
        int lineLength =
            AbstractPrinter.settings.getInt(
                ConventionKeys.LINE_LENGTH, ConventionDefaults.LINE_LENGTH);

        if (tester.line>1 || tester.column> lineLength) {
            out.printNewline();
        }
        out.testers.release(tester);
        printChildren(node,out);

    }

}
