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
package de.hunsicker.jalopy.parser;

import de.hunsicker.antlr.CommonHiddenStreamToken;
import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.prefs.Defaults;
import de.hunsicker.jalopy.prefs.Keys;
import de.hunsicker.jalopy.prefs.Preferences;

import java.io.File;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.oro.text.PatternCache;
import org.apache.oro.text.PatternCacheLRU;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * Inspects a Java AST for convention violations and possible code weaknesses.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b8
 */
public class CodeInspector
    extends TreeWalker
{
    //~ Static variables/initializers ·········································

    /** Type names for which a base type should be favored. */
    private static final List _favorableTypes = new ArrayList(); // List of <String>

    /** Collection type names. */
    private static final List _collectionTypes = new ArrayList(); // List of <String>

    /** The cache with the regexpes. */
    private static final PatternCache _patterns = new PatternCacheLRU(30);

    /** The Jalopy preferences. */
    private static final Preferences _prefs = Preferences.getInstance();
    private static final ResourceBundle _bundle = ResourceBundle.getBundle("de.hunsicker.jalopy.Bundle");
    private static final String STR_boolean = "boolean";
    private static final String STR_Object = "Object";
    private static final String STR_equals = "equals";
    private static final String STR_javalangObject = "java.lang.Object";
    private static final String STR_String = "String";
    private static final String STR_toString = "toString";
    private static final String STR_void = "void";
    private static final String STR_finalize = "finalize";
    private static final String STR_int = "int";
    private static final String STR_hashCode = "hashCode";
    private static final String STR_wait = "wait";
    private static final String STR_Exception = "Exception";
    private static final String STR_Throwable = "Throwable";

    static
    {
        _favorableTypes.add("HashMap");
        _favorableTypes.add("Hashtable");
        _favorableTypes.add("Vector");
        _favorableTypes.add("TreeMap");
        _favorableTypes.add("HashSet");
        _favorableTypes.add("IdentityHashMap");
        _favorableTypes.add("ArrayList");
        _favorableTypes.add("WeakHashMap");
        _favorableTypes.add("java.util.HashMap");
        _favorableTypes.add("java.util.Hashtable");
        _favorableTypes.add("java.util.Vector");
        _favorableTypes.add("java.util.TreeMap");
        _favorableTypes.add("java.util.HashSet");
        _favorableTypes.add("java.util.IdentityHashMap");
        _favorableTypes.add("java.util.ArrayList");
        _favorableTypes.add("java.util.WeakHashMap");
        _collectionTypes.add("Collection");
        _collectionTypes.add("Set");
        _collectionTypes.add("List");
        _collectionTypes.add("SortedSet");
        _collectionTypes.add("Map");
        _collectionTypes.add("SortedMap");
        _collectionTypes.add("HashMap");
        _collectionTypes.add("Hashtable");
        _collectionTypes.add("Vector");
        _collectionTypes.add("TreeMap");
        _collectionTypes.add("HashSet");
        _collectionTypes.add("IdentityHashMap");
        _collectionTypes.add("ArrayList");
        _collectionTypes.add("WeakHashMap");
        _collectionTypes.add("java.util.HashMap");
        _collectionTypes.add("java.util.Hashtable");
        _collectionTypes.add("java.util.Vector");
        _collectionTypes.add("java.util.TreeMap");
        _collectionTypes.add("java.util.HashSet");
        _collectionTypes.add("java.util.IdentityHashMap");
        _collectionTypes.add("java.util.ArrayList");
        _collectionTypes.add("java.util.WeakHashMap");
    }

    private static final String STR_RETURN_ZERO_ARRAY = "RETURN_ZERO_ARRAY";
    private static final String STR_REFER_BY_INTERFACE = "REFER_BY_INTERFACE";
    private static final String STR_VARIABLE_SHADOW = "VARIABLE_SHADOW";
    private static final String STR_DONT_IGNORE_EXCEPTION = "DONT_IGNORE_EXCEPTION";
    private static final String STR_REPLACE_STRUCTURE_WITH_CLASS = "REPLACE_STRUCTURE_WITH_CLASS";
    private static final String STR_OVERRIDE_HASHCODE = "OVERRIDE_HASHCODE";
    private static final String STR_OVERRIDE_EQUALS = "OVERRIDE_EQUALS";
    private static final String STR_OVERRIDE_TO_STRING = "OVERRIDE_TO_STRING";
    private static final String STR_ADHERE_TO_NAMING_CONVENTION = "ADHERE_TO_NAMING_CONVENTION";
    private static final String STR_DECLARE_COLLECTION_COMMENT = "DECLARE_COLLECTION_COMMENT";
    private static final String STR_WRONG_COLLECTION_COMMENT = "WRONG_COLLECTION_COMMENT";
    private static final String STR_OBEY_CONTRACT_EQUALS = "OBEY_CONTRACT_EQUALS";
    private static final String STR_EMPTY_FINALLY = "EMPTY_FINALLY";
    private static final String STR_INTERFACE_ONLY_FOR_TYPE = "INTERFACE_ONLY_FOR_TYPE";
    private static final String STR_NEVER_WAIT_OUTSIDE_LOOP = "NEVER_WAIT_OUTSIDE_LOOP";
    private static final String STR_DONT_SUBSTITUTE_OBJECT_EQUALS = "DONT_SUBSTITUTE_OBJECT_EQUALS";
    private static final String STR_NEVER_THROW_EXCEPTION = "NEVER_THROW_EXCEPTION";
    private static final String STR_NEVER_THROW_THROWABLE = "NEVER_THROW_THROWABLE";
    private static final String STR_NAMING_CONVENTION = "NAMING_CONVENTION";
    private static final String STR_FIELD_STATIC_FINAL_PUBLIC = "public static final field";
    private static final String STR_FIELD_STATIC_FINAL_PROTECTED = "protected static final field";
    private static final String STR_FIELD_STATIC_FINAL_DEFAULT = "static final field";
    private static final String STR_FIELD_STATIC_FINAL_PRIVATE = "private static final field";
    private static final String STR_FIELD_STATIC_PUBLIC = "public static field";
    private static final String STR_FIELD_STATIC_PROTECTED = "protected static field";
    private static final String STR_FIELD_STATIC_DEFAULT = "static field";
    private static final String STR_FIELD_STATIC_PRIVATE = "private static field";
    private static final String STR_FIELD_PUBLIC = "public field";
    private static final String STR_FIELD_PROTECTED = "protected field";
    private static final String STR_FIELD_DEFAULT = "field";
    private static final String STR_FIELD_PRIVATE = "private field";
    private static final String STR_EMPTY = "";
    private static final String STR_LOCAL_VARIABLE = "local variable";

    //~ Instance variables ····················································

    /**
     * Holds the issues found during inspection. Maps one node to either
     * exaclty one issue or a list of the resource keys of several issues.
     */
    private Map _issues; // Map of <JavaNode>:<Object>

    /** The pattern matcher. */
    private final PatternMatcher _matcher = new Perl5Matcher();

    /** The file currently beeing processed. */
    private String _file;

    /** Array used to take the arguments for the message formatter. */
    private final String[] _args = new String[3];

    //~ Constructors ··························································

    /**
     * Creates a new CodeInspector object.
     *
     * @param issues map to hold the found issues.
     */
    public CodeInspector(Map issues)
    {
        _issues = issues;
    }

    //~ Methods ·······························································

    /**
     * Inspects the given Java AST for code convention violations and coding
     * weaknesses.
     *
     * @param tree root node of the AST.
     * @param file the file that is inspected.
     */
    public void inspect(AST  tree,
                        File file)
    {
        _file = file.getAbsolutePath();
        walk(tree);
    }


    /**
     * {@inheritDoc}
     */
    public void visit(AST node)
    {
        switch (node.getType())
        {
            case JavaTokenTypes.VARIABLE_DEF :

                AST type = NodeHelper.getFirstChild(node, JavaTokenTypes.TYPE);
                String returnType = type.getFirstChild().getText();

                if (_prefs.getBoolean(Keys.TIP_REFER_BY_INTERFACE, false) &&
                    _favorableTypes.contains(returnType))
                {
                    addIssue(node, STR_REFER_BY_INTERFACE, _args);
                }

                checkCollectionReturnType((JavaNode)node, returnType);

                String name = type.getNextSibling().getText();
                checkVariableName(node, name);

                break;

            case JavaTokenTypes.METHOD_CALL :
                checkMethodCall(node);

                break;

            case JavaTokenTypes.ASSIGN :
                checkAssignment(node);

                break;

            case JavaTokenTypes.LITERAL_catch :
                checkCatch(node);

                break;

            case JavaTokenTypes.LITERAL_finally :
                checkFinally(node);

                break;

            case JavaTokenTypes.LABELED_STAT :
                checkLabelName(node);

                break;

            case JavaTokenTypes.METHOD_DEF :

                Method method = new Method(node);
                checkObjectEquals(node, method);
                checkReturnType(node, method);
                checkParameters(node, method);
                checkThrows(node, method);

                if (!method.isAbstract())
                {
                    checkArrayReturnType(node, method);
                }

                break;

            case JavaTokenTypes.CTOR_DEF :

                Constructor ctor = new Constructor(node);
                checkParameters(node, ctor);
                checkThrows(node, ctor);

                break;

            case JavaTokenTypes.CLASS_DEF :
                checkClass(node);
                checkClassName(node);

                break;

            case JavaTokenTypes.INTERFACE_DEF :
                checkInterface(node);
                checkInterfaceName(node);

                break;

            case JavaTokenTypes.PACKAGE_DEF :
                checkPackageName(node);

                break;
        }
    }


    /**
     * Indicates whether the given object represents the "public boolean
     * equals(java.lang.Object o)" method.
     *
     * @param method the method object.
     *
     * @return <code>true</code> if the given method type represents the
     *         equals method.
     */
    private boolean isEqualsMethod(Method method)
    {
        if (method.getParameterCount() != 1)
        {
            return false;
        }

        if ((method.modifierMask != Modifier.PUBLIC) &&
            (!Modifier.isPublic(method.modifierMask)))
        {
            return false;
        }

        if (!STR_boolean.equals(method.returnType))
        {
            return false;
        }

        if (!STR_equals.equals(method.name))
        {
            return false;
        }

        Parameter parameter = (Parameter)method.parameters.get(0);

        if (parameter.modifierMask != 0)
        {
            return false;
        }

        if ((!(STR_Object.equals(parameter.typeName))) &&
            (STR_javalangObject.equals(parameter.typeName)))
        {
            return false;
        }

        return true;
    }


    /**
     * Indicates whether the given object represents the "protected void
     * finalize()" method.
     *
     * @param method the method object.
     *
     * @return <code>true</code> if the given method type represents the
     *         finalize method.
     */
    private boolean isFinalizeMethod(Method method)
    {
        if (method.getParameterCount() != 0)
        {
            return false;
        }

        if ((method.modifierMask != Modifier.PROTECTED) &&
            (!Modifier.isProtected(method.modifierMask)))
        {
            return false;
        }

        if (!STR_void.equals(method.returnType))
        {
            return false;
        }

        if (!STR_finalize.equals(method.name))
        {
            return false;
        }

        return true;
    }


    /**
     * Indicates whether the given object represents the  "public int
     * hashCode()" method.
     *
     * @param method the method object.
     *
     * @return <code>true</code> if the given method type represents the
     *         hashCode method.
     */
    private boolean isHashCodeMethod(Method method)
    {
        if (method.getParameterCount() != 0)
        {
            return false;
        }

        if ((method.modifierMask != Modifier.PUBLIC) &&
            (!Modifier.isPublic(method.modifierMask)))
        {
            return false;
        }

        if (!STR_int.equals(method.returnType))
        {
            return false;
        }

        if (!STR_hashCode.equals(method.name))
        {
            return false;
        }

        return true;
    }


    /**
     * Searches the tree way back to determine the OBJBLOCK parent of the
     * given node.
     *
     * @param node a node of the tree below an OBJBLOCK node.
     *
     * @return the OBJBLOCK parent of the given node.
     */
    private JavaNode getObjectBlock(JavaNode node)
    {
        JavaNode parent = node.parent;

        switch (node.getType())
        {
            case JavaTokenTypes.OBJBLOCK :
                return node;

            default :
                return getObjectBlock(parent);
        }
    }


    /**
     * Indicates whether the given object represents a substituted "public
     * boolean equals(java.lang.Object o)" method (e.g. "public boolean
     * equals(MyObject o)").
     *
     * @param method the method object.
     *
     * @return <code>true</code> if the given method type represents a
     *         substituted equals method.
     */
    private boolean isSubstituteEqualsMethod(Method method)
    {
        if (method.getParameterCount() != 1)
        {
            return false;
        }

        if ((method.modifierMask != Modifier.PUBLIC) &&
            (!Modifier.isPublic(method.modifierMask)))
        {
            return false;
        }

        if (!STR_boolean.equals(method.returnType))
        {
            return false;
        }

        if (!STR_equals.equals(method.name))
        {
            return false;
        }

        Parameter parameter = (Parameter)method.parameters.get(0);

        if (parameter.modifierMask != 0)
        {
            return false;
        }

        if ((STR_Object.equals(parameter.typeName)) ||
            (STR_javalangObject.equals(parameter.typeName)))
        {
            return false;
        }

        return true;
    }


    /**
     * Indicates whether the given object represents the "public String
     * toString()" method.
     *
     * @param method the method object.
     *
     * @return <code>true</code> if the given method type represents the
     *         toString method.
     */
    private boolean isToStringMethod(Method method)
    {
        if (method.getParameterCount() != 0)
        {
            return false;
        }

        if ((method.modifierMask != Modifier.PUBLIC) &&
            (!Modifier.isPublic(method.modifierMask)))
        {
            return false;
        }

        if (!STR_String.equals(method.returnType))
        {
            return false;
        }

        if (!STR_toString.equals(method.name))
        {
            return false;
        }

        return true;
    }


    /**
     * Adds the given tip for the given node.
     *
     * @param node node to add a tip for.
     * @param resourceKey resource key of the tip.
     * @param args DOCUMENT ME!
     */
    private void addIssue(AST      node,
                          String   resourceKey,
                          Object[] args)
    {
        if (!_issues.containsKey(node))
        {
            _issues.put(node,
                        MessageFormat.format(_bundle.getString(resourceKey),
                                             args));
        }
        else
        {
            Object value = _issues.get(node);

            if (value instanceof List)
            {
                List issuesForNode = (List)value;
                issuesForNode.add(MessageFormat.format(_bundle.getString(resourceKey),
                                                       args));
            }
            else
            {
                List issuesForNode = new ArrayList(4);
                issuesForNode.add(value);
                issuesForNode.add(MessageFormat.format(_bundle.getString(resourceKey),
                                                       args));
                _issues.put(node, issuesForNode);
            }
        }
    }


    /**
     * Checks whether, in case the method has an array return type, the given
     * method returns always an array and not <code>null</code>.
     *
     * @param node the METHOD_DEF node.
     * @param method the method object.
     */
    private void checkArrayReturnType(AST    node,
                                      Method method)
    {
        if (!_prefs.getBoolean(Keys.TIP_RETURN_ZERO_ARRAY, false))
        {
            return;
        }

        String returnType = method.returnType;

        if (returnType.indexOf('[') > -1) // it's an array thingy
        {
            TreeWalker walker = new TreeWalker()
            {
                public void visit(AST node)
                {
                    switch (node.getType())
                    {
                        case JavaTokenTypes.LITERAL_return :

                            AST child = node.getFirstChild();
                            int retType = 0;

                            switch (child.getType())
                            {
                                case JavaTokenTypes.EXPR :
                                    retType = child.getFirstChild().getType();

                                    break;

                                case JavaTokenTypes.LPAREN :

                                    AST expr = NodeHelper.advanceToFirstNonParen(child);

                                    switch (expr.getType())
                                    {
                                        case JavaTokenTypes.EXPR :
                                            retType = expr.getFirstChild()
                                                          .getType();

                                            break;
                                    }

                                    break;
                            }

                            switch (retType)
                            {
                                case JavaTokenTypes.LITERAL_null :

                                    if (_prefs.getBoolean(
                                                          Keys.TIP_RETURN_ZERO_ARRAY,
                                                          false))
                                    {
                                        addIssue(node, STR_RETURN_ZERO_ARRAY,
                                                 _args);
                                    }

                                    stop();

                                    break;
                            }

                            break;
                    }
                }
            };

            walker.walk(NodeHelper.getFirstChild(node, JavaTokenTypes.SLIST));
        }
    }


    private void checkAssignment(AST node)
    {
        if (!_prefs.getBoolean(Keys.TIP_VARIABLE_SHADOW, false))
        {
            return;
        }

        AST lhs = node.getFirstChild();

        switch (lhs.getType())
        {
            case JavaTokenTypes.IDENT :

                AST rhs = lhs.getNextSibling();

                if (rhs != null)
                {
                    switch (rhs.getType())
                    {
                        case JavaTokenTypes.IDENT :

                            // possible shadowing found,
                            if (lhs.getText().equals(rhs.getText()))
                            {
                                JavaNode objBlock = getObjectBlock((JavaNode)node);
                                boolean foundReference = false;

                                for (AST child = objBlock.getFirstChild();
                                     child != null;
                                     child = child.getNextSibling())
                                {
                                    switch (child.getType())
                                    {
                                        case JavaTokenTypes.VARIABLE_DEF :

                                            String name = NodeHelper.getFirstChild(child,
                                                                                   JavaTokenTypes.IDENT)
                                                                    .getText();

                                            if (name.equals(lhs.getText()))
                                            {
                                                _args[2] = "this." + name +
                                                           " = " + name;

                                                if (_prefs.getBoolean(
                                                                      Keys.TIP_VARIABLE_SHADOW,
                                                                      false))
                                                {
                                                    addIssue(node,
                                                             STR_VARIABLE_SHADOW,
                                                             _args);
                                                }

                                                foundReference = true;
                                            }

                                            break;
                                    }
                                }

                                if (!foundReference)
                                {
                                    /**
                                     * @todo
                                     */

                                    // 1. if inner class and non-static, search parents
                                    // 2. if anonymous inner class, search parents
                                    // 3. if dereived class, print message about possible shadowing
                                }
                            }

                            break;
                    }
                }

                break;
        }
    }


    /**
     * Checks the given catch clause is not empty.
     *
     * @param node a LITERAL_catch node.
     */
    private void checkCatch(AST node)
    {
        if (!_prefs.getBoolean(Keys.TIP_DONT_IGNORE_EXCEPTION, false))
        {
            return;
        }

        JavaNode next = (JavaNode)node.getFirstChild().getNextSibling()
                                      .getFirstChild();

        if (next.getType() == JavaTokenTypes.RCURLY)
        {
            // if the user does not document why the catch is ignored
            if ((!next.hasCommentsBefore()) &&
                _prefs.getBoolean(Keys.TIP_DONT_IGNORE_EXCEPTION, false))
            {
                addIssue(node, STR_DONT_IGNORE_EXCEPTION, _args);
            }
        }
    }


    /**
     * Checks whether the given class represents a structure and in case
     * equals is overriden that hashCode is also overriden (or vice versa).
     * Also checks whether the toSting method is overriden.
     *
     * @param node a CLASS_DEF node.
     */
    private void checkClass(AST node)
    {
        boolean checkReplaceStructure = _prefs.getBoolean(Keys.TIP_REPLACE_STRUCTURE_WITH_CLASS,
                                                          false);
        boolean checkOverrideHashCode = _prefs.getBoolean(Keys.TIP_OVERRIDE_HASHCODE,
                                                          false);
        boolean checkOverrideEquals = _prefs.getBoolean(Keys.TIP_OVERRIDE_EQUALS,
                                                        false);
        boolean checkOverrideToString = _prefs.getBoolean(Keys.TIP_OVERRIDE_TO_STRING,
                                                          false);

        if ((!checkReplaceStructure) && (!checkOverrideHashCode) &&
            (!checkOverrideEquals) && (!checkOverrideHashCode))
        {
            return;
        }

        AST classModifierMask = NodeHelper.getFirstChild(node,
                                                         JavaTokenTypes.MODIFIERS);
        boolean isPublicClass = Modifier.isPublic(JavaNodeModifier.valueOf(classModifierMask));
        AST body = NodeHelper.getFirstChild(node, JavaTokenTypes.OBJBLOCK);
        boolean violate = isPublicClass;
        boolean foundEquals = false;
        AST equalsNode = null;
        boolean foundHashCode = false;
        AST hashCodeNode = null;
        boolean foundToString = false;

        for (AST child = body.getFirstChild();
             child != null;
             child = child.getNextSibling())
        {
            switch (child.getType())
            {
                case JavaTokenTypes.LCURLY :
                case JavaTokenTypes.RCURLY :
                    break;

                case JavaTokenTypes.VARIABLE_DEF :

                    if (checkReplaceStructure && isPublicClass)
                    {
                        int vaiableModifierMask = JavaNodeModifier.valueOf(child.getFirstChild());

                        if (!Modifier.isPublic(vaiableModifierMask))
                        {
                            violate = false;
                        }
                    }

                    break;

                case JavaTokenTypes.METHOD_DEF :
                    violate = false;

                    Method method = new Method(child);

                    if (checkOverrideEquals && (!foundEquals) &&
                        isEqualsMethod(method))
                    {
                        equalsNode = child;
                        foundEquals = true;
                    }

                    if (checkOverrideHashCode && (!foundHashCode) &&
                        isHashCodeMethod(method))
                    {
                        hashCodeNode = child;
                        foundHashCode = true;
                    }

                    if (checkOverrideToString && (!foundToString) &&
                        isToStringMethod(method))
                    {
                        foundToString = true;
                    }

                    break;

                default :
                    violate = false;

                    break;
            }
        }

        if (checkReplaceStructure && isPublicClass && violate)
        {
            if (_prefs.getBoolean(Keys.TIP_REPLACE_STRUCTURE_WITH_CLASS, false))
            {
                addIssue(node, STR_REPLACE_STRUCTURE_WITH_CLASS, _args);
            }
        }

        if (foundEquals && (!foundHashCode))
        {
            if (_prefs.getBoolean(Keys.TIP_OVERRIDE_HASHCODE, false))
            {
                addIssue(equalsNode, STR_OVERRIDE_HASHCODE, _args);
            }
        }

        if (foundHashCode && (!foundEquals))
        {
            if (_prefs.getBoolean(Keys.TIP_OVERRIDE_EQUALS, false))
            {
                addIssue(hashCodeNode, STR_OVERRIDE_EQUALS, _args);
            }
        }

        if (!foundToString)
        {
            if (_prefs.getBoolean(Keys.TIP_OVERRIDE_TO_STRING, false))
            {
                addIssue(node, STR_OVERRIDE_TO_STRING, _args);
            }
        }
    }


    /**
     * Checks whether the given class name adheres to the naming convention.
     *
     * @param node a CLASS_DEF node.
     */
    private void checkClassName(AST node)
    {
        String name = NodeHelper.getFirstChild(node, JavaTokenTypes.IDENT)
                                .getText();

        if (!JavaNodeModifier.isAbstract(node))
        {
            Pattern pattern = _patterns.getPattern(_prefs.get(
                                                              Keys.REGEXP_CLASS,
                                                              Defaults.REGEXP_CLASS));

            if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                (!_matcher.matches(name, pattern)))
            {
                if (_prefs.getBoolean(Keys.TIP_ADHERE_TO_NAMING_CONVENTION,
                                      false))
                {
                    _args[0] = "Class";
                    _args[1] = name;
                    _args[2] = pattern.getPattern();
                    addIssue(node, STR_NAMING_CONVENTION, _args);
                }
            }
        }
        else
        {
            Pattern pattern = _patterns.getPattern(_prefs.get(
                                                              Keys.REGEXP_CLASS_ABSTRACT,
                                                              Defaults.REGEXP_CLASS_ABSTRACT));

            if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                (!_matcher.matches(name, pattern)))
            {
                if (_prefs.getBoolean(Keys.TIP_ADHERE_TO_NAMING_CONVENTION,
                                      false))
                {
                    _args[0] = "Abstract class";
                    _args[1] = name;
                    _args[2] = pattern.getPattern();
                    addIssue(node, STR_NAMING_CONVENTION, _args);
                }
            }
        }
    }


    /**
     * Checks, in case the variable declares a collection type, whether an
     * endline comment states the type of the elements
     *
     * @param node a VARIABLE_DEF node.
     * @param returnType the returnType of the variable declaration.
     */
    private void checkCollectionReturnType(JavaNode node,
                                           String   returnType)
    {
        if (!NodeHelper.isLocalVariable(node))
        {
            if (_collectionTypes.contains(returnType))
            {
                if ((!node.hasCommentsAfter()) &&
                    _prefs.getBoolean(Keys.TIP_DECLARE_COLLECTION_COMMENT,
                                      false))
                {
                    addIssue(node, STR_DECLARE_COLLECTION_COMMENT, _args);
                }
                else if (_prefs.getBoolean(Keys.TIP_WRONG_COLLECTION_COMMENT,
                                           false))
                {
                    CommonHiddenStreamToken comment = (CommonHiddenStreamToken)node.getHiddenAfter();

                    switch (comment.getType())
                    {
                        case JavaTokenTypes.SL_COMMENT :

                            String text = comment.getText();

                            if ((text.indexOf('<') == -1) ||
                                (text.indexOf('>') == -1))
                            {
                                addIssue(node, STR_WRONG_COLLECTION_COMMENT,
                                         _args);
                            }

                            break;

                        default :

                            /**
                             * @todo print warning about wrong comment type
                             */
                            break;
                    }
                }
            }
        }
    }


    /**
     * Checks whether method throws an exception (which would violate the
     * equals contract).
     *
     * @param node the METHOD_DEF node.
     * @param method the method object.
     */
    private void checkEqualsReturnType(AST    node,
                                       Method method)
    {
        TreeWalker walker = new TreeWalker()
        {
            public void visit(AST node)
            {
                switch (node.getType())
                {
                    case JavaTokenTypes.LITERAL_throw :

                        if (_prefs.getBoolean(Keys.TIP_OBEY_CONTRACT_EQUALS,
                                              false))
                        {
                            addIssue(node, STR_OBEY_CONTRACT_EQUALS, _args);
                        }

                        stop();

                        break;
                }
            }
        };

        walker.walk(NodeHelper.getFirstChild(node, JavaTokenTypes.SLIST));
    }


    /**
     * Checks the given finally clause whether is it not empty.
     *
     * @param node a LITERAL_finally node.
     */
    private void checkFinally(AST node)
    {
        if (!_prefs.getBoolean(Keys.TIP_EMPTY_FINALLY, false))
        {
            return;
        }

        if ((node.getFirstChild().getFirstChild().getType() == JavaTokenTypes.RCURLY) &&
            _prefs.getBoolean(Keys.TIP_EMPTY_FINALLY, false))
        {
            addIssue(node, STR_EMPTY_FINALLY, _args);
        }
    }


    /**
     * Checks whether the given interface is misused to export constants.
     *
     * @param node a INTERFACE_DEF node.
     */
    private void checkInterface(AST node)
    {
        if (_prefs.getBoolean(Keys.TIP_INTERFACE_ONLY_FOR_TYPE, false))
        {
            AST body = NodeHelper.getFirstChild(node, JavaTokenTypes.OBJBLOCK);
            boolean violate = true;
SEARCH: 
            for (AST child = body.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.LCURLY :

                        switch (child.getNextSibling().getType())
                        {
                            // we've found a "tagging interface", it's ok
                            case JavaTokenTypes.RCURLY :
                                violate = false;

                                break SEARCH;
                        }

                        break;

                    case JavaTokenTypes.RCURLY :
                        break;

                    case JavaTokenTypes.VARIABLE_DEF :

                        // fields defined in interfaces are automatically 'static'
                        // and 'final', so no further checking here
                        break;

                    default :
                        violate = false;

                        break SEARCH;
                }
            }

            if (violate)
            {
                addIssue(node, STR_INTERFACE_ONLY_FOR_TYPE, _args);
            }
        }
    }


    /**
     * Checks whether the given interface name adheres to the naming
     * convention.
     *
     * @param node a INTERFACE_DEF node.
     */
    private void checkInterfaceName(AST node)
    {
        String name = NodeHelper.getFirstChild(node, JavaTokenTypes.IDENT)
                                .getText();
        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                          Keys.REGEXP_INTERFACE,
                                                          Defaults.REGEXP_INTERFACE));

        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
            (!_matcher.matches(name, pattern)))
        {
            _args[0] = "Interface";
            _args[1] = name;
            _args[2] = pattern.getPattern();
            addIssue(node, STR_NAMING_CONVENTION, _args);
        }
    }


    /**
     * Checks whether the given label adheres to the naming convention.
     *
     * @param node a LABELED_STAT node.
     */
    private void checkLabelName(AST node)
    {
        String name = node.getFirstChild().getText();
        Pattern pattern = _patterns.getPattern(_prefs.get(Keys.REGEXP_LABEL,
                                                          Defaults.REGEXP_LABEL));

        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
            (!_matcher.matches(name, pattern)))
        {
            _args[0] = "Label";
            _args[1] = name;
            _args[2] = pattern.getPattern();
            addIssue(node, STR_NAMING_CONVENTION, _args);
        }
    }


    /**
     * Checks, in case the method call represents the Object.wait() method,
     * whether  it is invoked from within a loop.
     *
     * @param node a METHOD_CALL node.
     */
    private void checkMethodCall(AST node)
    {
        if (!_prefs.getBoolean(Keys.TIP_NEVER_WAIT_OUTSIDE_LOOP, false))
        {
            return;
        }

        AST child = node.getFirstChild();
        String name = null;

        switch (child.getType())
        {
            case JavaTokenTypes.IDENT :
                name = child.getText();

                break;

            case JavaTokenTypes.DOT :

                if (NodeHelper.isChained(child))
                {
                    name = NodeHelper.getFirstChainLink(node).getNextSibling()
                                     .getText();
                }
                else
                {
                    name = child.getFirstChild().getNextSibling().getText();
                }

                break;
        }

        if (STR_wait.equals(name))
        {
            JavaNode n = (JavaNode)node;
            JavaNode expr = n.getParent();

            switch (expr.getParent().getType())
            {
                case JavaTokenTypes.SLIST :

                    switch (expr.getParent().getParent().getType())
                    {
                        case JavaTokenTypes.LITERAL_while :
                        case JavaTokenTypes.LITERAL_for :
                            break;

                        default :
                            addIssue(n, STR_NEVER_WAIT_OUTSIDE_LOOP, _args);

                            break;
                    }

                    break;

                case JavaTokenTypes.LITERAL_while :
                case JavaTokenTypes.LITERAL_for :
                    break;

                default :
                    addIssue(n, STR_NEVER_WAIT_OUTSIDE_LOOP, _args);

                    break;
            }
        }
    }


    /**
     * Checks, in case the given method node represents an overriden equals
     * method, whether it substitutes object.
     *
     * @param node a METHOD_DEF node.
     * @param method the method object.
     */
    private void checkObjectEquals(AST    node,
                                   Method method)
    {
        if (!_prefs.getBoolean(Keys.TIP_DONT_SUBSTITUTE_OBJECT_EQUALS, false))
        {
            return;
        }

        if (isSubstituteEqualsMethod(method))
        {
            addIssue(node, STR_DONT_SUBSTITUTE_OBJECT_EQUALS, _args);
        }
        else if (isEqualsMethod(method))
        {
            checkEqualsReturnType(node, method);
        }
    }


    /**
     * Checks whether the given package name adheres to the naming convention.
     *
     * @param node a PACKAGE_DEF node.
     */
    private void checkPackageName(AST node)
    {
        String name = NodeHelper.getDottedName(node.getFirstChild());
        Pattern pattern = _patterns.getPattern(_prefs.get(Keys.REGEXP_PACKAGE,
                                                          Defaults.REGEXP_PACKAGE));

        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
            (!_matcher.matches(name, pattern)))
        {
            _args[0] = "Package";
            _args[1] = name;
            _args[2] = pattern.getPattern();
            addIssue(node, STR_NAMING_CONVENTION, _args);
        }
    }


    /**
     * Checks whether all given parameters represents interface (base) types.
     *
     * @param node a CTOR_DEF or METHOD_DEF node.
     * @param type object representing a CTOR_DEF or METHOD_DEF.
     */
    private void checkParameters(AST         node,
                                 Constructor type)
    {
        if (!_prefs.getBoolean(Keys.TIP_REFER_BY_INTERFACE, false))
        {
            return;
        }

        /**
         * @todo add check for naming convention
         */
        for (int i = 0, size = type.parameters.size(); i < size; i++)
        {
            Parameter parameter = (Parameter)type.parameters.get(i);

            if (_favorableTypes.contains(parameter.typeName))
            {
                addIssue(node, STR_REFER_BY_INTERFACE, _args);
            }
        }
    }


    /**
     * Checks whether the given return type represents an interface (base)
     * type.
     *
     * @param node a METHOD_DEF node.
     * @param method the method object.
     */
    private void checkReturnType(AST    node,
                                 Method method)
    {
        if (!_prefs.getBoolean(Keys.TIP_REFER_BY_INTERFACE, false))
        {
            return;
        }

        if (method.hasReturnType())
        {
            String returnType = method.returnType;

            if (_favorableTypes.contains(returnType))
            {
                addIssue(node, STR_REFER_BY_INTERFACE, _args);
            }
        }
    }


    /**
     * Checks whether the given constructor or method node does throw a
     * superclass exception.
     *
     * @param node a CTOR_DEF or METHOD_DEF node.
     * @param type the constructor or method object.
     */
    private void checkThrows(AST         node,
                             Constructor type)
    {
        boolean checkThrowException = _prefs.getBoolean(Keys.TIP_NEVER_THROW_EXCEPTION,
                                                        false);
        boolean checkThrowThrowable = _prefs.getBoolean(Keys.TIP_NEVER_THROW_THROWABLE,
                                                        false);

        if ((!checkThrowException) && (!checkThrowThrowable))
        {
            return;
        }

        if (type.hasExceptions())
        {
            switch (node.getType())
            {
                case JavaTokenTypes.METHOD_DEF :

                    // user overrides finalizer, this is OK
                    if (isFinalizeMethod((Method)type))
                    {
                        return;
                    }
            }

            List exceptions = type.exceptions;

            for (int i = 0, size = exceptions.size(); i < size; i++)
            {
                String name = (String)exceptions.get(i);

                if (checkThrowException && STR_Exception.equals(name))
                {
                    addIssue(node, STR_NEVER_THROW_EXCEPTION, _args);
                }
                else if (checkThrowThrowable && STR_Throwable.equals(name))
                {
                    addIssue(node, STR_NEVER_THROW_THROWABLE, _args);
                }
            }
        }
    }


    /**
     * Checks the given variable name (either a field or local variable) for
     * naming violations.
     *
     * @param node a VARIABLE_DEF node.
     * @param name the name of the variable.
     */
    private void checkVariableName(AST    node,
                                   String name)
    {
        if (NodeHelper.isLocalVariable(node))
        {
            Pattern pattern = _patterns.getPattern(_prefs.get(
                                                              Keys.REGEXP_LOCAL_VARIABLE,
                                                              Defaults.REGEXP_LOCAL_VARIABLE));

            if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                (!_matcher.matches(name, pattern)))
            {
                _args[0] = STR_LOCAL_VARIABLE;
                _args[1] = name;
                _args[2] = pattern.getPattern();
                addIssue(node, STR_NAMING_CONVENTION, _args);
            }
        }
        else
        {
            AST modifiers = NodeHelper.getFirstChild(node,
                                                     JavaTokenTypes.MODIFIERS);
            int modifierMask = JavaNodeModifier.valueOf(modifiers);

            if (Modifier.isStatic(modifierMask))
            {
                if (Modifier.isFinal(modifierMask)) // static final fields
                {
                    if (Modifier.isPublic(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PUBLIC_STATIC_FINAL,
                                                                          Defaults.REGEXP_FIELD_PUBLIC_STATIC_FINAL));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_FINAL_PUBLIC;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else if (Modifier.isProtected(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PROTECTED_STATIC_FINAL,
                                                                          Defaults.REGEXP_FIELD_PROTECTED_STATIC_FINAL));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_FINAL_PROTECTED;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else if (Modifier.isPrivate(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PRIVATE_STATIC_FINAL,
                                                                          Defaults.REGEXP_FIELD_PRIVATE_STATIC_FINAL));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_FINAL_PRIVATE;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_DEFAULT_STATIC_FINAL,
                                                                          Defaults.REGEXP_FIELD_DEFAULT_STATIC_FINAL));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_FINAL_DEFAULT;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                }
                else // static fields
                {
                    if (Modifier.isPublic(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PUBLIC_STATIC,
                                                                          Defaults.REGEXP_FIELD_PUBLIC_STATIC));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_PUBLIC;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else if (Modifier.isProtected(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PROTECTED_STATIC,
                                                                          Defaults.REGEXP_FIELD_PROTECTED_STATIC));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_PROTECTED;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else if (Modifier.isPrivate(modifierMask))
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_PRIVATE_STATIC,
                                                                          Defaults.REGEXP_FIELD_PRIVATE_STATIC));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_PRIVATE;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                    else
                    {
                        Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                          Keys.REGEXP_FIELD_DEFAULT_STATIC,
                                                                          Defaults.REGEXP_FIELD_DEFAULT_STATIC));

                        if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                            (!_matcher.matches(name, pattern)))
                        {
                            _args[0] = STR_FIELD_STATIC_DEFAULT;
                            _args[1] = name;
                            _args[2] = pattern.getPattern();
                            addIssue(node, STR_NAMING_CONVENTION, _args);
                        }
                    }
                }
            }
            else // instance fields
            {
                if (Modifier.isPublic(modifierMask))
                {
                    Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                      Keys.REGEXP_FIELD_PUBLIC,
                                                                      Defaults.REGEXP_FIELD_PUBLIC));

                    if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                        (!_matcher.matches(name, pattern)))
                    {
                        _args[0] = STR_FIELD_PUBLIC;
                        _args[1] = name;
                        _args[2] = pattern.getPattern();
                        addIssue(node, STR_NAMING_CONVENTION, _args);
                    }
                }
                else if (Modifier.isProtected(modifierMask))
                {
                    Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                      Keys.REGEXP_FIELD_PROTECTED,
                                                                      Defaults.REGEXP_FIELD_PROTECTED));

                    if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                        (!_matcher.matches(name, pattern)))
                    {
                        _args[0] = STR_FIELD_PROTECTED;
                        _args[1] = name;
                        _args[2] = pattern.getPattern();
                        addIssue(node, STR_NAMING_CONVENTION, _args);
                    }
                }
                else if (Modifier.isPrivate(modifierMask))
                {
                    Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                      Keys.REGEXP_FIELD_PRIVATE,
                                                                      Defaults.REGEXP_FIELD_PRIVATE));

                    if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                        (!_matcher.matches(name, pattern)))
                    {
                        _args[0] = STR_FIELD_PRIVATE;
                        _args[1] = name;
                        _args[2] = pattern.getPattern();
                        addIssue(node, STR_NAMING_CONVENTION, _args);
                    }
                }
                else
                {
                    Pattern pattern = _patterns.getPattern(_prefs.get(
                                                                      Keys.REGEXP_FIELD_DEFAULT,
                                                                      Defaults.REGEXP_FIELD_DEFAULT));

                    if ((!pattern.getPattern().equals(STR_EMPTY)) &&
                        (!_matcher.matches(name, pattern)))
                    {
                        _args[0] = STR_FIELD_DEFAULT;
                        _args[1] = name;
                        _args[2] = pattern.getPattern();
                        addIssue(node, STR_NAMING_CONVENTION, _args);
                    }
                }
            }
        }
    }

    //~ Inner Classes ·························································

    /**
     * Represents a CTOR_DEF node.
     */
    private static class Constructor
    {
        public static final String TYPE_VOID = "void";
        List exceptions = Collections.EMPTY_LIST; // List of <String>
        List parameters = Collections.EMPTY_LIST; // List of <Parameter>
        String name;
        int modifierMask;

        public Constructor(AST node)
        {
            for (AST child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.MODIFIERS :
                        this.modifierMask = JavaNodeModifier.valueOf(child);

                        break;

                    case JavaTokenTypes.IDENT :
                        this.name = child.getText();

                        break;

                    case JavaTokenTypes.PARAMETERS :

                        AST parameter = child.getFirstChild();

                        if (parameter != null)
                        {
                            this.parameters = new ArrayList(5);

                            for (AST pchild = parameter;
                                 pchild != null;
                                 pchild = pchild.getNextSibling())
                            {
                                this.parameters.add(new Parameter(pchild));
                            }
                        }

                        break;

                    case JavaTokenTypes.LITERAL_throws :
                        this.exceptions = new ArrayList(4);

                        for (AST exception = child.getFirstChild();
                             exception != null;
                             exception = exception.getNextSibling())
                        {
                            exceptions.add(exception.getText());
                        }

                        break;
                }
            }
        }


        private Constructor()
        {
        }

        public List getExceptions()
        {
            return this.exceptions;
        }


        public String getName()
        {
            return this.name;
        }


        public int getParameterCount()
        {
            return this.parameters.size();
        }


        public List getParameters()
        {
            return this.parameters;
        }


        public boolean hasExceptions()
        {
            return !this.exceptions.isEmpty();
        }


        public boolean hasParameters()
        {
            return !this.parameters.isEmpty();
        }
    }


    /**
     * Represents a PARAMETER node.
     */
    private static final class Parameter
    {
        String name;
        String typeName;
        int modifierMask;

        public Parameter(AST node)
        {
            for (AST child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.MODIFIERS :
                        this.modifierMask = JavaNodeModifier.valueOf(child);

                        break;

                    case JavaTokenTypes.TYPE :
                        this.typeName = child.getFirstChild().getText();

                        break;

                    case JavaTokenTypes.IDENT :
                        this.name = child.getText();

                        break;
                }
            }
        }

        public int getModifierMask()
        {
            return this.modifierMask;
        }


        public String getName()
        {
            return this.name;
        }


        public String getTypeName()
        {
            return this.typeName;
        }


        public String toString()
        {
            StringBuffer buf = new StringBuffer();
            buf.append(Modifier.toString(this.modifierMask));
            buf.append(' ');
            buf.append(typeName);
            buf.append(' ');
            buf.append(name);

            return buf.toString().trim();
        }
    }


    /**
     * Represents a METHOD_DEF node.
     */
    private static final class Method
        extends Constructor
    {
        String returnType;
        boolean isAbstract = true;

        public Method(AST node)
        {
            for (AST child = node.getFirstChild();
                 child != null;
                 child = child.getNextSibling())
            {
                switch (child.getType())
                {
                    case JavaTokenTypes.MODIFIERS :
                        this.modifierMask = JavaNodeModifier.valueOf(child);

                        break;

                    case JavaTokenTypes.TYPE :
                        this.returnType = child.getFirstChild().getText();

                        break;

                    case JavaTokenTypes.IDENT :
                        this.name = child.getText();

                        break;

                    case JavaTokenTypes.PARAMETERS :

                        AST parameter = child.getFirstChild();

                        if (parameter != null)
                        {
                            this.parameters = new ArrayList(5);

                            for (AST pchild = parameter;
                                 pchild != null;
                                 pchild = pchild.getNextSibling())
                            {
                                this.parameters.add(new Parameter(pchild));
                            }
                        }

                        break;

                    case JavaTokenTypes.LITERAL_throws :
                        this.exceptions = new ArrayList(4);

                        for (AST exception = child.getFirstChild();
                             exception != null;
                             exception = exception.getNextSibling())
                        {
                            exceptions.add(exception.getText());
                        }

                        break;

                    case JavaTokenTypes.SLIST :
                        this.isAbstract = false;

                        break;
                }
            }
        }

        public boolean isAbstract()
        {
            return this.isAbstract;
        }


        public String getReturnType()
        {
            return this.returnType;
        }


        public boolean hasReturnType()
        {
            return !TYPE_VOID.equals(this.returnType);
        }
    }
}
