/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper.message;

/**
 * A MessageType defines the type of a message. This way one can distinguish between
 * certain messages and apply certain behaviour for each type.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public class MessageType
{
    //~ Static variables/initializers ----------------------------------------------------

    /** Represents an error message. */
    public static final MessageType ERROR = new MessageType("error" /* NOI18N */);

    /** Represents a warning message. */
    public static final MessageType WARN = new MessageType("warn" /* NOI18N */);

    /** Represents an informational message (this should be the default). */
    public static final MessageType INFO = new MessageType("info" /* NOI18N */);

    /** Represents a debugging message. */
    public static final MessageType DEBUG = new MessageType("debug" /* NOI18N */);

    //~ Instance variables ---------------------------------------------------------------

    private String _name;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new MessageType object.
     *
     * @param name the name of the type.
     */
    protected MessageType(String name)
    {
        _name = name;
    }
}
