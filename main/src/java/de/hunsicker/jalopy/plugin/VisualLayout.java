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
package de.hunsicker.jalopy.plugin;

import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.util.StringHelper;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;


/**
 * A custom Log4J layout which reformats muliple line messages and takes care
 * of throwable information.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class VisualLayout
    extends Layout
{
    //~ Static variables/initializers ·········································

    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final int MAX_LINE_LENGTH = 100;

    //~ Instance variables ····················································

    private Convention _settings = Convention.getInstance();

    //~ Constructors ··························································

    /**
     * Creates a new VisualLayout object.
     */
    public VisualLayout()
    {
    }

    //~ Methods ·······························································

    /**
     * Activate the options that were previously set with calls to option
     * setters. Actaally does nothing as the option setters are deprecated
     * and no longer used.
     */
    public void activateOptions()
    {
    }


    /**
     * Returns the log statement. Adds throwable information if available and
     * enabled in the code convention. Multiple line messages will be
     * reformatted to fit into to given maximal line length.
     *
     * @param event the logging event.
     *
     * @return the formatted message.
     */
    public String format(LoggingEvent event)
    {
        if (ignoresThrowable())
        {
            String message = event.getRenderedMessage();

            // max. line length exeeded, perform reformatting
            if (message.length() > MAX_LINE_LENGTH)
            {
                return StringHelper.wrapString(message, MAX_LINE_LENGTH, true);
            }
            else
            {
                return message;
            }
        }
        else
        {
            StringBuffer buf = new StringBuffer(100);

            if (event.getThrowableStrRep() != null)
            {
                String[] lines = event.getThrowableStrRep();
                String message = event.getRenderedMessage();

                // first the message
                buf.append(message);
                buf.append('\n');

                // append the stacktrace (if available)
                for (int i = 0; i < lines.length; i++)
                {
                    buf.append(lines[i]);
                    buf.append('\n');
                }

                // remove the last separator
                buf.setLength(buf.length() - 1);
            }
            else
            {
                buf.append(event.getRenderedMessage());
            }

            return buf.toString();
        }
    }


    /**
     * Indicates whether throwable information should be included in the
     * output.
     *
     * @return <code>true</code> if throwable information should be included
     *         in the output.
     */
    public boolean ignoresThrowable()
    {
        return !_settings.getBoolean(Keys.MSG_SHOW_ERROR_STACKTRACE,
                                  Defaults.MSG_SHOW_ERROR_STACKTRACE);
    }
}
