/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


//J-
import java.lang.ClassCastException;
//J+

/**
 * Represents a Java modifier.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class ModifierType
    implements Comparable, Type
{
    //~ Static variables/initializers ----------------------------------------------------

    /** The bit value for the public modifier. */
    static final int PUBLIC_INT = 1;

    /** The bit value for the protected  modifier. */
    static final int PROTECTED_INT = 2;

    /** The bit value for the private modifier. */
    static final int PRIVATE_INT = 16;

    /** The bit value for the static modifier. */
    static final int STATIC_INT = 8;

    /** The bit value for the final modifier. */
    static final int FINAL_INT = 32;

    /** The bit value for the transient modifier. */
    static final int TRANSIENT_INT = 64;

    /** The bit value for the abstract modifier. */
    static final int ABSTRACT_INT = 128;

    /** The bit value for the native modifier. */
    static final int NATIVE_INT = 256;

    /** The bit value for the synchronized modifier. */
    static final int SYNCHRONIZED_INT = 512;

    /** The bit value for the volatile modifier. */
    static final int VOLATILE_INT = 1024;

    /** The bit value for the strictfp modifier. */
    static final int STRICTFP_INT = 64;

    /** A string represenation of the current sort order. */
    private static String _sortOrder;

    /** Holds the actual order of the method types. */
    private static List _order = new ArrayList(7); // List of <String>

    /** Represents the public modifier. */
    public static final ModifierType PUBLIC = new ModifierType("public", PUBLIC_INT);

    /** Represents the protected modifier. */
    public static final ModifierType PROTECTED =
        new ModifierType("protected", PROTECTED_INT);

    /** Represents the private modifier. */
    public static final ModifierType PRIVATE = new ModifierType("private", PRIVATE_INT);

    /** Represents the static modifier. */
    public static final ModifierType STATIC = new ModifierType("static", STATIC_INT);

    /** Represents the final modifier. */
    public static final ModifierType FINAL = new ModifierType("final", FINAL_INT);

    /** Represents the transient modifier. */
    public static final ModifierType TRANSIENT =
        new ModifierType("transient", TRANSIENT_INT);

    /** Represents the native modifier. */
    public static final ModifierType NATIVE = new ModifierType("native", NATIVE_INT);

    /** Represents the abstract modifier. */
    public static final ModifierType ABSTRACT =
        new ModifierType("abstract", ABSTRACT_INT);

    /** Represents the synchronized modifier. */
    public static final ModifierType SYNCHRONIZED =
        new ModifierType("synchronized", SYNCHRONIZED_INT);

    /** Represents the volatile modifier. */
    public static final ModifierType VOLATILE =
        new ModifierType("volatile", VOLATILE_INT);

    /** Represents the strictfp modifier. */
    public static final ModifierType STRICTFP =
        new ModifierType("strictfp", STRICTFP_INT);

    static
    {
        _order.add(PUBLIC);
        _order.add(PROTECTED);
        _order.add(PRIVATE);
        _order.add(ABSTRACT);
        _order.add(STATIC);
        _order.add(FINAL);
        _order.add(SYNCHRONIZED);
        _order.add(TRANSIENT);
        _order.add(VOLATILE);
        _order.add(NATIVE);
        _order.add(STRICTFP);

        StringBuffer buf = new StringBuffer(100);

        for (int i = 0, size = _order.size(); i < size; i++)
        {
            buf.append(((ModifierType) _order.get(i))._name);
            buf.append(',');
        }

        buf.setLength(buf.length() - 1);
        _sortOrder = buf.toString();
    }

    //~ Instance variables ---------------------------------------------------------------

    /** A user-friendly string representation of the type. */
    private final String _name;

    /** The bit value of the type. */
    private final int _key;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ModifierType object.
     *
     * @param name a string representing the modifier type.
     * @param key a bit value representing the modifier type.
     */
    private ModifierType(
        String name,
        int    key)
    {
        _name = name;
        _key = key;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the name of this type.
     *
     * @return type name.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * Sets the order to use as the natural order.
     *
     * @param str a string representing the new order. The string must consist of exactly
     *        elevene different, comma delimited strings which represents a modifier
     *        type.
     *
     * @throws IllegalArgumentException if the given string is invalid.
     */
    public static synchronized void setOrder(String str)
    {
        if ((str == null) || (str.length() == 0))
        {
            throw new IllegalArgumentException("order == " + str);
        }

        StringTokenizer tokens = new StringTokenizer(str, ",");
        List temp = new ArrayList(_order.size());
        StringBuffer buf = new StringBuffer(50);

        while (tokens.hasMoreElements())
        {
            String token = tokens.nextToken();
            ModifierType type = valueOf(token);

            if (temp.contains(type))
            {
                throw new IllegalArgumentException("invalid order string " + temp);
            }

            temp.add(type);
            buf.append(type.toString());
            buf.append(',');
        }

        if (_order.size() != temp.size())
        {
            throw new IllegalArgumentException("invalid order string " + temp);
        }

        if (
            !temp.contains(PUBLIC) || !temp.contains(PROTECTED)
            || !temp.contains(PRIVATE) || !temp.contains(STATIC) || !temp.contains(FINAL)
            || !temp.contains(ABSTRACT) || !temp.contains(SYNCHRONIZED)
            || !temp.contains(TRANSIENT) || !temp.contains(VOLATILE)
            || !temp.contains(STRICTFP) || !temp.contains(NATIVE))
        {
            throw new IllegalArgumentException("invalid order string " + temp);
        }

        _order = Collections.unmodifiableList(temp);
        buf.deleteCharAt(buf.length() - 1);
        _sortOrder = buf.toString();
    }


    /**
     * Returns a string representation of the current sort order. To encode the different
     * declaration types, use
     * <pre class="snippet">
     * public,protected,private,abstract,static,final,synchronized,transient,volatile,native,strictfp
     * </pre>
     * or any combination thereof.
     *
     * @return the string representation of the order.
     */
    public static synchronized String getOrder()
    {
        return _sortOrder;
    }


    /**
     * Compares this object with the specified object for order. Returns a negative
     * integer, zero, or a positive integer as this object is less than, equal to, or
     * greater than the specified object.
     *
     * @param other the object to be compared.
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassCastException if the specified object's type prevents it from being
     *         compared to this object.
     */
    public int compareTo(Object other)
    {
        if (other == this)
        {
            return 0;
        }

        if (other instanceof ModifierType)
        {
            int thisIndex = _order.indexOf(this);
            int otherIndex = _order.indexOf(other);

            if (thisIndex > otherIndex)
            {
                return 1;
            }
            else if (thisIndex < otherIndex)
            {
                return -1;
            }
            else
            {
                return 0;
            }
        }

        throw new ClassCastException(
            (other == null) ? "null"
                            : other.getClass().getName());
    }


    /**
     * Returns a string representation of this modifier type.
     *
     * @return a string representation of this type.
     */
    public String toString()
    {
        return _name;
    }


    /**
     * Returns the modifier type for the given name.
     *
     * @param name a modifier.
     *
     * @return the modifier type of the given name.
     *
     * @throws IllegalArgumentException if no valid modifier was given.
     */
    public static ModifierType valueOf(String name)
    {
        if ((name == null) || (name.trim().length() == 0))
        {
            throw new IllegalArgumentException("invalid modifier -- " + name);
        }

        name = name.trim();

        if (PUBLIC._name.equals(name))
        {
            return PUBLIC;
        }
        else if (PRIVATE._name.equals(name))
        {
            return PRIVATE;
        }
        else if (PROTECTED._name.equals(name))
        {
            return PROTECTED;
        }
        else if (FINAL._name.equals(name))
        {
            return FINAL;
        }
        else if (STATIC._name.equals(name))
        {
            return STATIC;
        }
        else if (ABSTRACT._name.equals(name))
        {
            return ABSTRACT;
        }
        else if (SYNCHRONIZED._name.equals(name))
        {
            return SYNCHRONIZED;
        }
        else if (TRANSIENT._name.equals(name))
        {
            return TRANSIENT;
        }
        else if (VOLATILE._name.equals(name))
        {
            return VOLATILE;
        }
        else if (STRICTFP._name.equals(name))
        {
            return STRICTFP;
        }
        else if (NATIVE._name.equals(name))
        {
            return NATIVE;
        }

        throw new IllegalArgumentException("invalid modifier -- " + name);
    }
}
