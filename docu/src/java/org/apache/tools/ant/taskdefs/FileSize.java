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
/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2002 The Apache Software Foundation.  All rights
 * reserved.
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
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Ant", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.text.*;
import java.util.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Property;


/**
 * Determines the size of a file and sets a Project property.  Will not
 * override values set by the command line or parent projects.
 * 
 * @author Marco Hunsicker
 */
public class FileSize
    extends Property
{
    //~ Instance variables ····················································

    /** DOCUMENT ME! */
    protected String style = "kb";

    /** DOCUMENT ME! */
    protected boolean failOnError = true;

    /** DOCUMENT ME! */
    protected boolean quiet;

    //~ Constructors ··························································

    /**
     * Creates a new FileSize object.
     */
    public FileSize()
    {
        super();
    }


    /**
     * Creates a new FileSize object.
     * 
     * @param userProperty DOCUMENT ME!
     */
    public FileSize(boolean userProperty)
    {
        super(userProperty);
    }

    //~ Methods ·······························································

    /**
     * DOCUMENT ME!
     * 
     * @param fail DOCUMENT ME!
     */
    public void setFailOnError(boolean fail)
    {
        this.failOnError = fail;
    }


    /**
     * DOCUMENT ME!
     * 
     * @param style DOCUMENT ME!
     */
    public void setFormat(String style)
    {
        if (style != null)
        {
            String value = style.toLowerCase();

            if ("kb".equals(value) || "bytes".equals(value) || 
                "mb".equals(value))
            {
                ;
            }
            else
            {
                throw new IllegalArgumentException(
                      "invalid style attribute -- " + style);
            }

            this.style = value;
        }
        else if (!quiet)
        {
            log("Empty style attribute found. Will use default -- " + 
                this.style, Project.MSG_WARN);
        }
    }


    /**
     * DOCUMENT ME!
     * 
     * @param quiet DOCUMENT ME!
     */
    public void setQuiet(boolean quiet)
    {
        this.quiet = quiet;
    }


    /**
     * DOCUMENT ME!
     * 
     * @throws BuildException DOCUMENT ME!
     */
    public void execute()
        throws BuildException
    {
        if ((this.name == null) || (this.file == null))
        {
            throw new BuildException("You must specify both a name and a file", 
                                     location);
        }

        if (!this.file.exists())
        {
            if (this.failOnError)
            {
                throw new BuildException("File not found -- " + this.file, 
                                         location);
            }
            else
            {
                // we set the value to a default
                this.value = "???";
                addProperty(this.name, this.value);

                if (!quiet)
                {
                    log("Could not determine size for file -- " + this.file, 
                        Project.MSG_WARN);
                }

                return;
            }
        }

        try
        {
            if ("kb".equals(this.style))
            {
                this.value = format(this.file.length() / 1024.0, 0);
            }
            else if ("mb".equals(this.style))
            {
                this.value = format(this.file.length() / 1024.0 / 1024.0, 2);
            }
            else
            {
                this.value = format((double)this.file.length(), 0);
            }
        }
        catch (Throwable ex)
        {
            throw new BuildException(ex);
        }

        addProperty(this.name, this.value);
    }


    private String format(double size, 
                          int    digits)
        throws NumberFormatException
    {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(digits);

        return formatter.format(size);
    }
}