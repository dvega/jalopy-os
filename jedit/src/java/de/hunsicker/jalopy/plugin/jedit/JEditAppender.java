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
package de.hunsicker.jalopy.plugin.jedit;

import org.gjt.sp.jedit.view.message.Message;
import org.gjt.sp.jedit.view.message.MessageTab;
import org.gjt.sp.jedit.view.message.MessageType;
import org.gjt.sp.jedit.view.message.MessageView;

import de.hunsicker.jalopy.plugin.AbstractAppender;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.oro.text.regex.MatchResult;


/**
 * Appender which displays messages in a JEdit message view.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JEditAppender
    extends AbstractAppender
{
    //~ Static variables/initializers �����������������������������������������

    /** Our message category. */
    private static final MessageTab TAB_JALOPY = new MessageTab("Jalopy");

    //~ Constructors ����������������������������������������������������������

    /**
     * Creates a new JEditAppender object.
     */
    public JEditAppender()
    {
        super();
    }

    //~ Methods ���������������������������������������������������������������

    /**
     * Does the actual outputting.
     *
     * @param ev logging event.
     */
    public void append(LoggingEvent ev)
    {
        switch (ev.getLevel().toInt())
        {
            case Level.WARN_INT :
                append(ev, MessageType.WARN);

                break;

            case Level.DEBUG_INT :
                append(ev, MessageType.DEBUG);

                break;

            case Level.ERROR_INT :
            case Level.FATAL_INT :
                append(ev, MessageType.ERROR);

                break;

            case Level.INFO_INT :
            default :
                append(ev, MessageType.INFO);

                break;
        }
    }


    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        MessageView.getInstance().clear(TAB_JALOPY);
    }


    /**
     * Adds a new message to the message view.
     *
     * @param ev the log4j logging event.
     * @param type the type of the logging event.
     */
    private void append(LoggingEvent ev,
                        MessageType  type)
    {
        MatchResult result = parseMessage(ev);

        if (result == null)
        {
            MessageView.getInstance()
                       .addMessage(new Message(ev.getRenderedMessage(), type),
                                   TAB_JALOPY, false);
        }
        else
        {
            String text = result.group(POS_TEXT);
            String filename = result.group(POS_FILENAME);
            int line = 0;
            int column = 0;

            try
            {
                line = Integer.parseInt(result.group(POS_LINE));
                column = Integer.parseInt(result.group(POS_COLUMN));
            }
            catch (NumberFormatException ex)
            {
                // never happens as the regexp already validated the numbers
            }

            MessageView.getInstance()
                       .addMessage(new Message(text, new File(filename), line,
                                               column, 0, type), TAB_JALOPY,
                                   false);
        }
    }
}
