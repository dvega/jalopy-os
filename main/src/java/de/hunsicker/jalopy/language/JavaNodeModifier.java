/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

import java.lang.reflect.Modifier;

import de.hunsicker.antlr.collections.AST;


/**
 * Helper class which resolves the different modifiers of a MODIFIERS node.
 * 
 * <p>
 * This class can be used to get the access level of a class, method, field...
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @todo add constant for default (friendly) access
 */
public final class JavaNodeModifier
    extends Modifier
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Unknown keyword. */
    public static final int UNKNOWN = 0;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JavaNodeModifier object.
     */
    private JavaNodeModifier()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Indicates whether the given modifiers node contains the <em>abstract</em> keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>abstract</em>
     *         keyword.
     */
    public static boolean isAbstract(AST modifiers)
    {
        return Modifier.isAbstract(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node contains the <em>final</em> keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>final</em> keyword.
     */
    public static boolean isFinal(AST modifiers)
    {
        return Modifier.isFinal(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node denotes package protected (friendly)
     * access.
     *
     * @param modifiers
     *
     * @return <code>true</code> if the node denotes package protected access.
     */
    public static boolean isFriendly(AST modifiers)
    {
        return isFriendly(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifier denotes package protected (friendly) access.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the node denotes package protected access.
     */
    public static boolean isFriendly(int modifiers)
    {
        return (modifiers & (Modifier.PUBLIC + Modifier.PROTECTED + Modifier.PRIVATE)) == 0;
    }


    /**
     * Indicates whether the given modifiers node contains the <em>private</em> keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>private</em> keyword.
     */
    public static boolean isPrivate(AST modifiers)
    {
        return Modifier.isPrivate(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node contains the <em>protected</em>
     * keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>protected</em>
     *         keyword.
     */
    public static boolean isProtected(AST modifiers)
    {
        return Modifier.isProtected(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node contains the <em>public</em> keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>public</em> keyword.
     */
    public static boolean isPublic(AST modifiers)
    {
        return Modifier.isPublic(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node contains the <em>static</em> keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>static</em> keyword.
     */
    public static boolean isStatic(AST modifiers)
    {
        return Modifier.isStatic(valueOf(modifiers));
    }


    /**
     * Indicates whether the given modifiers node contains the <em>synchronized</em>
     * keyword.
     *
     * @param modifiers MODIFIERS node to check.
     *
     * @return <code>true</code> if the given node contains the <em>synchronized</em>
     *         keyword.
     */
    public static boolean isSynchronized(AST modifiers)
    {
        return Modifier.isSynchronized(valueOf(modifiers));
    }


    /**
     * Returns the modifier mask of the given modifiers node.
     *
     * @param modifiers MODIFIERS node.
     *
     * @return modifier mask of the given modifiers.
     *
     * @throws IllegalArgumentException if <code>modifiers == null</code> or
     *         <code>modifiers.getType() != JavaTokenTypes.MODIFIERS</code>
     */
    public static int valueOf(AST modifiers)
    {
        if (modifiers.getType() != JavaTokenTypes.MODIFIERS)
        {
            modifiers = NodeHelper.getFirstChild(modifiers, JavaTokenTypes.MODIFIERS);

            if ((modifiers == null) || (modifiers.getType() != JavaTokenTypes.MODIFIERS))
            {
                throw new IllegalArgumentException(modifiers + " is no MODIFIERS type");
            }
        }

        int mod = 0;

        for (AST sib = modifiers.getFirstChild(); sib != null;
            sib = sib.getNextSibling())
        {
            switch (sib.getType())
            {
                case JavaTokenTypes.LITERAL_public :
                    mod += Modifier.PUBLIC;

                    break;

                case JavaTokenTypes.LITERAL_protected :
                    mod += Modifier.PROTECTED;

                    break;

                case JavaTokenTypes.LITERAL_private :
                    mod += Modifier.PRIVATE;

                    break;

                case JavaTokenTypes.LITERAL_static :
                    mod += Modifier.STATIC;

                    break;

                case JavaTokenTypes.ABSTRACT :
                    mod += Modifier.ABSTRACT;

                    break;

                case JavaTokenTypes.FINAL :
                    mod += Modifier.FINAL;

                    break;

                case JavaTokenTypes.LITERAL_synchronized :
                    mod += Modifier.SYNCHRONIZED;

                    break;

                case JavaTokenTypes.LITERAL_interface :
                    mod += Modifier.INTERFACE;

                    break;

                case JavaTokenTypes.LITERAL_native :
                    mod += Modifier.NATIVE;

                    break;

                case JavaTokenTypes.LITERAL_transient :
                    mod += Modifier.TRANSIENT;

                    break;

                case JavaTokenTypes.STRICTFP :
                    mod += Modifier.STRICT;

                    break;

                case JavaTokenTypes.LITERAL_volatile :
                    mod += Modifier.VOLATILE;

                    break;
            }
        }

        return mod;
    }
}
