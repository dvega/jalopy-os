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
package de.hunsicker.jalopy.prefs;

import java.io.Serializable;


/**
 * Represents a project to associate a set of preferences with.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b8
 */
public final class Project
    implements Serializable
{
    //~ Static variables/initializers ·········································

    static final long serialVersionUID = -4874682073931915199L;

    //~ Instance variables ····················································

    /** The project name. */
    private final String _name;

    /** The project description. */
    private String _description;

    //~ Constructors ··························································

    /**
     * Creates a new Project object.
     *
     * @param name the project name. The name must not contain one of the
     *        following characters: <code>\ / :  ? " ' &lt; &gt; |</code>.
     * @param description the project description.
     */
    public Project(String name,
                   String description)
    {
        setDescription(description);
        validate(name, '\\');
        validate(name, '/');
        validate(name, ':');
        validate(name, '*');
        validate(name, '?');
        validate(name, '"');
        validate(name, '\'');
        validate(name, '<');
        validate(name, '>');
        validate(name, '|');
        _name = name;
    }

    //~ Methods ·······························································

    /**
     * Sets the project description.
     *
     * @param description new description.
     *
     * @throws IllegalArgumentException if the given description exceeds the
     *         maximum length of 256 characters.
     */
    public void setDescription(String description)
    {
        if (description.length() > 256)
        {
            throw new IllegalArgumentException("description exceeds maximum of 256 -- " +
                                               description.length());
        }

        _description = description;
    }


    /**
     * Returns the project description.
     *
     * @return project description.
     */
    public String getDescription()
    {
        return _description;
    }


    /**
     * Returns the project name.
     *
     * @return project name.
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
        if (o instanceof Project)
        {
            return _name.equals(((Project)o)._name);
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
        return _name.hashCode();
    }


    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param character DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void validate(String name,
                          char   character)
    {
        if (name.indexOf(character) > -1)
        {
            throw new IllegalArgumentException("invalid character found -- " +
                                               character);
        }
    }
}
