/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.storage;

/**
 * Represents an import policy.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class ImportPolicy
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Apply no import optimization. */
    public static final ImportPolicy DISABLED =
        new ImportPolicy("ImportPolicy [disabled]");

    /** Expand on-demand import statements. */
    public static final ImportPolicy EXPAND = new ImportPolicy("ImportPolicy [expand]");

    /** Collapse single-type import statements. */
    public static final ImportPolicy COLLAPSE =
        new ImportPolicy("ImportPolicy [collapse]");

    //~ Instance variables ---------------------------------------------------------------

    /** The unique policy name. */
    private String _name;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new ImportPolicy object.
     *
     * @param name name of the policy.
     */
    private ImportPolicy(String name)
    {
        _name = name.intern();
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the ImportPolicy for the given name.
     *
     * @param name a valid policy name.
     *
     * @return the corresponding policy for the given name.
     *
     * @throws IllegalArgumentException if <em>name</em> is no valid policy name.
     */
    public static ImportPolicy valueOf(String name)
    {
        String n = name.intern();

        if (n == EXPAND._name)
        {
            return EXPAND;
        }
        else if (n == COLLAPSE._name)
        {
            return COLLAPSE;
        }
        else if (n == DISABLED._name)
        {
            return DISABLED;
        }

        throw new IllegalArgumentException("invalid policy name -- " + name);
    }


    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    public String toString()
    {
        return _name;
    }
}
