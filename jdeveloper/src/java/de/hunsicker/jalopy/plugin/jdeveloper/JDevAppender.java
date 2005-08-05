/*
 * Copyright (c) 2001-2003, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 */
package de.hunsicker.jalopy.plugin.jdeveloper;

import java.io.File;
import java.util.regex.Matcher;

import de.hunsicker.jalopy.plugin.AbstractAppender;
import de.hunsicker.jalopy.plugin.jdeveloper.message.Message;
import de.hunsicker.jalopy.plugin.jdeveloper.message.MessagePage;
import de.hunsicker.jalopy.plugin.jdeveloper.message.MessageType;

import oracle.ide.log.LogManager;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

import oracle.ide.layout.ViewId;
import oracle.ide.log.LogWindow;


/**
 * Appender which displays messages in a JDeveloper log page.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
final class JDevAppender
    extends AbstractAppender
{
    //~ Instance variables ---------------------------------------------------------------

    private MessagePage _page;

    //~ Constructors ---------------------------------------------------------------------

    /**
     * Creates a new JDevAppender object.
     */
    public JDevAppender()
    {
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Does the actual outputting.
     *
     * @param ev logging event.
     */
    public void append(LoggingEvent ev)
    {
        switch (ev.getLevel().toInt())
        {
            case Priority.WARN_INT :
                append(ev, MessageType.WARN);

                break;

            case Priority.DEBUG_INT :
                append(ev, MessageType.DEBUG);

                break;

            case Priority.ERROR_INT :
            case Priority.FATAL_INT :
                append(ev, MessageType.ERROR);

                break;

            case Priority.INFO_INT :default :
                append(ev, MessageType.INFO);

                break;
        }
    }


    /**
     * {@inheritDoc}
     */
    public void clear()
    {
        if (_page != null)
        {
            _page.clearAll();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void done()
    {
        if (_page != null)
        {
            _page.update();
        }
    }


    /**
     * Does the actual outputting.
     *
     * @param ev logging event.
     * @param type the message type.
     */
    private void append(
        LoggingEvent ev,
        MessageType  type)
    {
        Matcher result = parseMessage(ev);

        if (_page == null)
        {
            _page =
                new MessagePage(
                    new ViewId("JalopyPage" /* NOI18N */, "Jalopy" /* NOI18N */), null);
        }

        LogWindow window = LogManager.getIdeLogWindow();

        if (!window.isVisible())
        {
            window.show();
        }

        if (result == null)
        {
            _page.log(new Message(ev.getRenderedMessage(), type));
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
                // never happens as the regex already validated the numbers
            }

            _page.log(new Message(text, new File(filename), line, column, 0, type));
        }
    }
}
