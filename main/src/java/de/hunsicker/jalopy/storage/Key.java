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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * A key for storing a value in a code convention.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @see de.hunsicker.jalopy.storage.Convention
 * @see de.hunsicker.jalopy.storage.Keys
 * @since 1.0b8
 */
public final class Key
    implements Serializable
{
    //~ Static variables/initializers ·········································

    /** Use serialVersionUID for interoperability. */
    static final long serialVersionUID = -7320495354745545260L;

    //~ Instance variables ····················································

    /** Our name. */
    private transient String _name;

    /** Pre-computed hash code value. */
    private transient int _hashCode;

    //~ Constructors ··························································

    /**
     * Creates a new Key object.
     *
     * @param name the name of the key.
     */
    Key(String name)
    {
        _name = name.intern();
        _hashCode = _name.hashCode();
    }

    //~ Methods ·······························································

    /**
     * Indicates whether some other object is &quot;equal to&quot; this one.
     *
     * @param o the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the obj
     *         argument.
     */
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }

        return _name == ((Key)o)._name;
    }


    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    public int hashCode()
    {
        return _hashCode;
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


    /**
     * Deserializes a key from the given stream.
     *
     * @param in stream to read the object from.
     *
     * @throws IOException if an I/O error occured.
     * @throws ClassNotFoundException if a class that should be read could not
     *         be found (Should never happen actually).
     */
    private void readObject(ObjectInputStream in)
        throws IOException,
               ClassNotFoundException
    {
        in.defaultReadObject();

        // that's why we have to provide custom serialization: we want to be
        // able to compare two keys by identity
        _name = ((String)in.readObject()).intern();
        _hashCode = in.readInt();
    }


    /**
     * Serializes this instance.
     *
     * @param out stream to write the object to.
     *
     * @throws IOException if an I/O error occured.
     *
     * @serialData Emits the name of the key, followed by its pre-computed
     *             hash code value.
     */
    private void writeObject(ObjectOutputStream out)
        throws IOException
    {
        out.defaultWriteObject();
        out.writeObject(_name);
        out.writeInt(_hashCode);
    }
}
