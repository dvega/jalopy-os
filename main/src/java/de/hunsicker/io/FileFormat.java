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
package de.hunsicker.io;

/**
 * Represents the file format of a platform. The file format is defined by the
 * line separator a platform uses.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class FileFormat
{
    //~ Static variables/initializers ·········································

    /** Represents the platform default file format. */
    public static final FileFormat DEFAULT = new FileFormat("DEFAULT",
                                                            System.getProperty("line.separator"));

    /** Indicates that the file format should be auto-detected. */
    public static final FileFormat AUTO = new FileFormat("AUTO",
                                                         System.getProperty("line.separator"));

    /** Represents  the DOS file format (&quot;\r\n&quot;). */
    public static final FileFormat DOS = new FileFormat("DOS", "\r\n");

    /** Represents  the Mac file format (&quot;\r&quot;). */
    public static final FileFormat MAC = new FileFormat("MAC", "\r");

    /** Represents  the Unix file format (&quot;\n&quot;). */
    public static final FileFormat UNIX = new FileFormat("UNIX", "\n");

    /** Represents an unknown or not yet determined file format. */
    public static final FileFormat UNKNOWN = new FileFormat("UNKNOWN",
                                                            System.getProperty("line.separator"));

    //~ Instance variables ····················································

    private final String _name;
    private final String _separator;

    //~ Constructors ··························································

    private FileFormat(String name,
                       String lineSeparator)
    {
        _name = name.intern();
        _separator = lineSeparator.intern();
    }

    //~ Methods ·······························································

    /**
     * Returns the characteristic line separator of the file format. Note that
     * both {@link #UNKNOWN} and {@link #AUTO} does not have distinct line
     * separators and therefore rather return the platform default separator.
     *
     * @return The line separator of the file format. Either &quot;\r\n&quot;
     *         or &quot;\n&quot; or &quot;\r&quot;
     */
    public String getLineSeparator()
    {
        return _separator;
    }


    /**
     * Returns the descriptive name of the file format.
     *
     * @return The name of the file format.
     */
    public String getName()
    {
        return _name;
    }


    /**
     * Returns the FileFormat object for the given file format string.
     *
     * @param format a valid format string. Either &quot;\r\n&quot; or
     *        &quot;DOS&quot;, &quot;\n&quot; or &quot;UNIX&quot;,
     *        &quot;\r&quot; or &quot;MAC&quot;, &quot;auto&quot; or
     *        &quot;default&quot; (case-insensitive).
     *
     * @return The FileFormat object for the given format string. Returns
     *         {@link #DEFAULT} if <em>format</em> == null or <em>format</em>
     *         does not denote a valid file format string.
     */
    public static FileFormat valueOf(String format)
    {
        String n = format.toUpperCase().intern();

        if ((DOS._name == n) || (DOS._separator == n))
        {
            return DOS;
        }
        else if ((UNIX._name == n) || (UNIX._separator == n))
        {
            return UNIX;
        }
        else if ((MAC._name == n) || (MAC._separator == n))
        {
            return MAC;
        }
        else if (AUTO._name == n)
        {
            return AUTO;
        }
        else if (UNKNOWN._name == n)
        {
            return UNKNOWN;
        }
        else
        {
            return DEFAULT;
        }
    }


    /**
     * Returns a string representation of this object.
     *
     * @return A string representation of this object.
     */
    public String toString()
    {
        return _separator;
    }
}
