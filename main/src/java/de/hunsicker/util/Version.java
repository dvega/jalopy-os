/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.util;

/**
 * A class that represents version information. Version numbering follows the &quot;Dewey
 * Decimal&quot; syntax that consists of positive decimal integers separated by periods
 * &quot;.&quot;
 */
public class Version
{
    //~ Instance variables ---------------------------------------------------------------

    private final String _name;
    private final boolean _beta;
    private final byte _major;
    private final byte _micro;
    private final byte _minor;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new Version object.
     *
     * @param name DOCUMENT ME!
     * @param number DOCUMENT ME!
     */
    public Version(
        String name,
        int    number)
    {
        this(name, number, 0);
    }


    /**
     * Creates a new Version object.
     *
     * @param name DOCUMENT ME!
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     */
    public Version(
        String name,
        int    major,
        int    minor)
    {
        this(name, major, minor, 0);
    }


    /**
     * Creates a new Version object.
     *
     * @param name DOCUMENT ME!
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     * @param beta DOCUMENT ME!
     */
    public Version(
        String  name,
        int     major,
        int     minor,
        boolean beta)
    {
        this(name, major, minor, 0, beta);
    }


    /**
     * Creates a new Version object.
     *
     * @param name DOCUMENT ME!
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     * @param micro DOCUMENT ME!
     */
    public Version(
        String name,
        int    major,
        int    minor,
        int    micro)
    {
        this(name, major, minor, micro, false);
    }


    /**
     * Creates a new Version object.
     *
     * @param name DOCUMENT ME!
     * @param major DOCUMENT ME!
     * @param minor DOCUMENT ME!
     * @param micro DOCUMENT ME!
     * @param beta DOCUMENT ME!
     */
    public Version(
        String  name,
        int     major,
        int     minor,
        int     micro,
        boolean beta)
    {
        _name = name;
        _major = (byte) major;
        _minor = (byte) minor;
        _micro = (byte) micro;
        _beta = beta;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Indicates whether this version represents a beta state.
     *
     * @return <code>true</code> if this version represents a beta state.
     */
    public boolean isBeta()
    {
        return _beta;
    }


    /**
     * Returns the major version number.
     *
     * @return major version number
     */
    public byte getMajorNumber()
    {
        return _major;
    }


    /**
     * Returns the micro version number.
     *
     * @return micro version number
     */
    public byte getMicroNumber()
    {
        return _micro;
    }


    /**
     * Returns the minor version number.
     *
     * @return minor version number
     */
    public byte getMinorNumber()
    {
        return _minor;
    }


    /**
     * Returns the version name.
     *
     * @return version name.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o instanceof Version)
        {
            Version other = (Version) o;

            return (_minor == other._minor) && (_major == other._major)
            && (_beta == other._beta) && (_micro == other._micro)
            && _name.equals(other._name);
        }

        return false;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode()
    {
        int result = 17;
        result = (result * 37) + _major;
        result = (result * 37) + _minor;
        result = (result * 37) + _micro;
        result = (result * 37) + (_beta ? 1
                                        : 2);

        return _name.hashCode() + result;
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString()
    {
        StringBuffer buf = new StringBuffer(10);
        buf.append(_major);
        buf.append('.');
        buf.append(_minor);

        if (_beta)
        {
            buf.append('b');
        }
        else
        {
            buf.append('.');
        }

        buf.append(_micro);

        return buf.toString();
    }
}
