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
package de.hunsicker.jalopy.storage;

/**
 * Represents an import policy.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class ImportPolicy
{
    //~ Static variables/initializers �����������������������������������������

    /** Apply no import optimization. */
    public static final ImportPolicy DISABLED = new ImportPolicy("ImportPolicy [disabled]");

    /** Expand on-demand import statements. */
    public static final ImportPolicy EXPAND = new ImportPolicy("ImportPolicy [expand]");

    /** Collapse single-type import statements. */
    public static final ImportPolicy COLLAPSE = new ImportPolicy("ImportPolicy [collapse]");

    //~ Instance variables ����������������������������������������������������

    /** The unique policy name. */
    private String _name;

    //~ Constructors ����������������������������������������������������������

    /**
     * Creates a new ImportPolicy object.
     *
     * @param name name of the policy.
     */
    private ImportPolicy(String name)
    {
        _name = name.intern();
    }

    //~ Methods ���������������������������������������������������������������

    /**
     * Returns the ImportPolicy for the given name.
     *
     * @param name a valid policy name.
     *
     * @return the corresponding policy for the given name.
     *
     * @throws IllegalArgumentException if <em>name</em> is no valid policy
     *         name.
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