/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy;

/**
 * Indicates a version mismatch between the specification versions of the Jalopy Plug-in
 * API and a concrete Plug-in implementation.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b8
 */
public final class VersionMismatchException
    extends Exception
{
    //~ Instance variables ---------------------------------------------------------------

    /** The expected version string. */
    private String _expected;

    /** The found version string. */
    private String _found;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new VersionMismatchException object.
     *
     * @param expected the expected vesion.
     * @param found the found vesion.
     */
    public VersionMismatchException(
        String expected,
        String found)
    {
        super("expected version was " + expected + ", found " + found);
        _expected = expected;
        _found = found;
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns the expected version string.
     *
     * @return The expected version.
     */
    public String getExpectedVersion()
    {
        return _expected;
    }


    /**
     * Returns the found version string.
     *
     * @return The found version.
     */
    public String getFoundVersion()
    {
        return _found;
    }
}
