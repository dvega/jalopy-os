/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.List;

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.storage.Loggers;
import de.hunsicker.util.Helper;

import org.apache.log4j.Level;


/**
 * Transformation which adds a SerialVersionUID field for serializable classes if no such
 * field is present already.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class SerializableTransformation
    extends TreeWalker
    implements Transformation
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final String SERIAL_VERSION_UID = "serialVersionUID";
    private static final String SERIALIZABLE = "Serializable";
    private static final String EXTERNALIZABLE = "Externalizable";

    //~ Instance variables ---------------------------------------------------------------

    /** The current class definition node info. */
    private ClassDefInfo _curClassDef;

    /** Holds info about every class definition found in the unit. */
    private List _classDefs = new ArrayList(5); // List of <ClassDefInfo>

    /** Holds the package name of the compilation unit. */
    private String _packageName;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new SerializableTransformation object.
     */
    public SerializableTransformation()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    public void apply(AST tree)
      throws TransformationException
    {
        try
        {
            walkNode(tree);

            for (int i = 0, size = _classDefs.size(); i < size; i++)
            {
                ClassDefInfo info = (ClassDefInfo) _classDefs.get(i);

                if (info.serializable && !info.serialver)
                {
                    try
                    {
                        Class clazz = Helper.loadClass(info.name, this);
                        ObjectStreamClass oclazz = ObjectStreamClass.lookup(clazz);
                        long serialVersionUID = oclazz.getSerialVersionUID();
                        insertSerialVer(tree, info, serialVersionUID);
                    }
                    catch (ClassNotFoundException ex)
                    {
                        Object[] args = { tree.getText(), info.name };
                        Loggers.TRANSFORM.l7dlog(
                            Level.WARN, "TRANS_SERIALIZABLE_CLASS_NOT_FOUND", args, ex);
                    }
                }
            }
        }
        finally
        {
            cleanup();
        }
    }


    /**
     * Callback method that will be called for VARIABLE_DEF nodes. Checks whether the
     * given definition is the SerialVersionUID field.
     *
     * @param node an class definition node of the tree.
     */
    public void visit(AST node)
    {
LOOP: 
        for (AST child = node.getFirstChild(); child != null;
            child = child.getNextSibling())
        {
            switch (child.getType())
            {
                // the first IDENT is our field name
                case JavaTokenTypes.IDENT :

                    if (child.getText().equals(SERIAL_VERSION_UID))
                    {
                        _curClassDef.serialver = true;
                    }

                    break LOOP;
            }
        }
    }


    /**
     * Only visits VARIABLE_DEF nodes.
     *
     * @param node a node of the tree.
     */
    protected void walkNode(AST node)
    {
        if (node != null)
        {
            switch (node.getType())
            {
                case JavaTokenTypes.ROOT :
                case JavaTokenTypes.OBJBLOCK :
                    walkNode(node.getFirstChild());

                    break;

                case JavaTokenTypes.PACKAGE_DEF :
                    _packageName = JavaNodeHelper.getDottedName(node.getFirstChild());

                // fall through
                case JavaTokenTypes.METHOD_DEF :
                case JavaTokenTypes.CTOR_DEF :
                case JavaTokenTypes.INSTANCE_INIT :
                case JavaTokenTypes.INTERFACE_DEF :
                case JavaTokenTypes.IMPORT :
                case JavaTokenTypes.STATIC_INIT :
                case JavaTokenTypes.MODIFIERS :
                case JavaTokenTypes.IDENT :
                case JavaTokenTypes.IMPLEMENTS_CLAUSE :
                case JavaTokenTypes.EXTENDS_CLAUSE :
                case JavaTokenTypes.LCURLY :
                    walkNode(node.getNextSibling());

                    break;

                case JavaTokenTypes.CLASS_DEF :
                    _curClassDef = addToClassList(node);

                    // only process the tree if we know we are serializable
                    if (_curClassDef.serializable)
                    {
                        walkNode(node.getFirstChild());
                    }

                    // next CLASS_DEF if any
                    walkNode(node.getNextSibling());

                    break;

                case JavaTokenTypes.VARIABLE_DEF :

                    // only check for SerialVersionUID if the class is indeed
                    // serializable and we haven't found the field yet
                    if (_curClassDef.serializable && !_curClassDef.serialver)
                    {
                        visit(node);
                    }

                    walkNode(node.getNextSibling());

                    break;

                default :
                    break;
            }
        }
    }


    /**
     * Indicates if the given class represented by the given node (really a CLASS_DEF
     * AST) is serializable.
     *
     * @param node node to check.
     *
     * @return <code>true</code> if the class is serializable.
     */
    private static boolean isClassSerializable(AST node)
    {
        AST clauses =
            JavaNodeHelper.getFirstChild(node, JavaTokenTypes.IMPLEMENTS_CLAUSE);

        for (
            AST clause = clauses.getFirstChild(); clause != null;
            clause = clause.getNextSibling())
        {
            String name = clause.getText();

            if (SERIALIZABLE.equals(name) || EXTERNALIZABLE.equals(name))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Adds the given node to the list of found CLASS_DEF AST nodes.
     *
     * @param node node to add.
     *
     * @return the info object added to the list.
     */
    private ClassDefInfo addToClassList(AST node)
    {
        int count = _classDefs.size();
        String ident = null;
        ident =
            _packageName + "."
            + JavaNodeHelper.getFirstChild(node, JavaTokenTypes.IDENT).getText();

        if (count > 0)
        {
            for (int i = 0, size = _classDefs.size(); i < size; i++)
            {
                ClassDefInfo previousInfo = (ClassDefInfo) _classDefs.get(i);

                if (previousInfo.node.contains((JavaNode) node))
                {
                    ident =
                        previousInfo.name + "$"
                        + JavaNodeHelper.getFirstChild(node, JavaTokenTypes.IDENT)
                                        .getText();

                    break;
                }
            }
        }

        ClassDefInfo info = new ClassDefInfo(node, ident);
        _classDefs.add(info);

        return info;
    }


    /**
     * DOCUMENT ME!
     */
    private void cleanup()
    {
        _classDefs.clear();
        _curClassDef = null;
        _packageName = null;
        _curClassDef = null;
    }


    /**
     * Inserts a serialVersionUID field for the given class definition.
     *
     * @param tree
     * @param info class definition info.
     * @param serialverUID the serial version identifier for the class.
     */
    private void insertSerialVer(
        AST          tree,
        ClassDefInfo info,
        long         serialverUID)
    {
        // the VARIABLE_DEF
        JavaNode serialver = new JavaNode(Integer.MAX_VALUE, 1, Integer.MAX_VALUE, 1);
        serialver.setType(JavaTokenTypes.VARIABLE_DEF);
        serialver.setText("VARIABLE_DEF");

        // the MODIFIERS
        AST modifiers = new JavaNode(JavaTokenTypes.MODIFIERS, "MODIFIERS");
        AST privatemod = new JavaNode(JavaTokenTypes.LITERAL_private, "private");
        AST finalmod = new JavaNode(JavaTokenTypes.FINAL, "final");
        AST staticmod = new JavaNode(JavaTokenTypes.LITERAL_static, "static");
        modifiers.addChild(privatemod);
        modifiers.addChild(finalmod);
        modifiers.addChild(staticmod);

        // the TYPE
        AST type = new JavaNode(JavaTokenTypes.TYPE, "TYPE");
        AST typevalue = new JavaNode(JavaTokenTypes.LITERAL_long, "long");
        type.addChild(typevalue);

        // the IDENT
        AST ident = new JavaNode(JavaTokenTypes.IDENT, "serialVersionUID");

        // the ASSIGN
        AST assign = new JavaNode(JavaTokenTypes.ASSIGN, "=");
        AST expr = new JavaNode(JavaTokenTypes.EXPR, "EXPR");
        AST assignvalue = new JavaNode(JavaTokenTypes.NUM_LONG, serialverUID + "L");
        expr.addChild(assignvalue);
        assign.addChild(expr);

        // the SEMI
        JavaNode semi = new JavaNode(JavaTokenTypes.SEMI, ";");
        serialver.addChild(modifiers);
        serialver.addChild(type);
        serialver.addChild(ident);
        serialver.addChild(assign);
        serialver.addChild(semi);

        // add a Javadoc comment
        Node description = new Node();
        description.setType(JavadocTokenTypes.PCDATA);
        description.setText("Use serialVersionUID for interoperability.");

        Node comment =
            new Node(
                JavaTokenTypes.JAVADOC_COMMENT,
                "/** Use serialVersionUID for interoperability. */");
        comment.addChild(description);

        ExtendedToken token =
            new ExtendedToken(
                JavaTokenTypes.JAVADOC_COMMENT,
                "/** Use serialVersionUID for interoperability. */");
        token.comment = comment;
        serialver.setHiddenBefore(token);

        JavaNode objblock =
            (JavaNode) JavaNodeHelper.getFirstChild(info.node, JavaTokenTypes.OBJBLOCK);
        JavaNode lcurly = (JavaNode) objblock.getFirstChild();
        JavaNode next = (JavaNode) lcurly.getNextSibling();
        lcurly.setNextSibling(serialver);
        serialver.setParent(objblock);
        serialver.setPreviousSibling(lcurly);
        serialver.setNextSibling(next);

        if (next != null)
        {
            next.setPreviousSibling(serialver);
        }

        Object[] args = { tree.getText(), info.name };
        Loggers.TRANSFORM.l7dlog(Level.INFO, "TRANS_SERIALIZABLE_ADD", args, null);
    }

    //~ Inner Classes --------------------------------------------------------------------

    /**
     * Helper class that holds information about a CLASS_DEF node.
     */
    private static final class ClassDefInfo
    {
        public JavaNode node;
        public String name;
        public boolean serializable;
        public boolean serialver;

        /**
         * Creates a new ClassDefInfo object.
         *
         * @param node
         * @param name
         */
        public ClassDefInfo(
            AST    node,
            String name)
        {
            this.node = (JavaNode) node;
            this.name = name;
            this.serializable = isClassSerializable(node);
        }

        public String toString()
        {
            StringBuffer buf = new StringBuffer();
            buf.append('[');
            buf.append(this.node);
            buf.append(", name=");
            buf.append(this.name);
            buf.append(", hasField=");
            buf.append(this.serialver);
            buf.append(']');

            return buf.toString();
        }
    }
}
