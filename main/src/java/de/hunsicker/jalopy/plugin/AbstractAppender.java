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

import de.hunsicker.util.ChainingRuntimeException;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;


/**
 * Skeleton implementation of an appender which outputs messages in a visual
 * component.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public abstract class AbstractAppender
    extends AppenderSkeleton
    implements VisualAppender
{
    //~ Static variables/initializers ·········································

    /** Position of the filename in the regex result. */
    public static final int POS_FILENAME = 1;

    /** Position of the lineno in the regex result. */
    public static final int POS_LINE = 2;

    /** Position of the message text in the regex result. */
    public static final int POS_TEXT = 4;

    /** Position of the message text in the regex result. */
    public static final int POS_COLUMN = 3;

    /** Name of the appender for output messages. */
    private static final String APPENDER_NAME = "JalopyAppender";

    /**
     * Regex to apply for Emacs-style messages. Messages must comply to the
     * filename:line:column:text pattern.
     */
    private static final String PATTERN = "(.+?):(\\d+):(\\d+):\\s*(.+)";

    //~ Instance variables ····················································

    /**
     * The regex to parse the messages. If messages have a format similiar to
     * Emacs messages (<code>filename:lineno:text</code>) the pattern will
     * match.
     */
    protected final Pattern regex;

    /** Matcher instance. */
    private PatternMatcher _matcher;

    //~ Constructors ··························································

    /**
     * Creates a new AbstractAppender object.
     *
     * @throws ChainingRuntimeException DOCUMENT ME!
     */
    public AbstractAppender()
    {
        this.name = APPENDER_NAME;
        this.layout = new VisualLayout();
        setThreshold(Level.DEBUG);
        _matcher = new Perl5Matcher();

        PatternCompiler compiler = new Perl5Compiler();

        try
        {
            this.regex = compiler.compile(PATTERN,
                                          Perl5Compiler.SINGLELINE_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ChainingRuntimeException(ex);
        }
    }

    //~ Methods ·······························································

    /**
     * Does the actual outputting.
     *
     * @param ev logging event.
     */
    public abstract void append(LoggingEvent ev);


    /**
     * Sets the name of the appender. Overidden so that the initial name can't
     * be changed.
     *
     * @param name appender name (ignored).
     */
    public final void setName(String name)
    {
    }


    /**
     * Releases any resources allocated within the appender.
     */
    public void close()
    {
    }


    /**
     * Parses the given message. To access the parsed information one may
     * typically use:
     * <pre style="background:lightgrey">
     * MatchResult result = parseMessage(message);
     * if (result == null)
     * {
     *     // handle plain message
     *     ...
     * }
     * else
     * {
     *     // this is an Emacs style message, you can easily access the
     *     // information
     *     String filename = result.group(POS_FILENAME);
     *     String line = result.group(POS_LINE);
     *     String column = result.group(POS_COLUMN);
     *     String text = result.group(POS_TEXT);
     *     ...
     * }
     * </pre>
     *
     * @param ev logging event.
     *
     * @return parsing result. Returns <code>null</code> if the message
     *         doesn't match the Emacs format
     *         <code>filename:line:column:text</code>.
     */
    public MatchResult parseMessage(LoggingEvent ev)
    {
        if (_matcher.matches(this.layout.format(ev), this.regex))
        {
            return _matcher.getMatch();
        }

        return null;
    }


    /**
     * Determines if the appender requires a layout. Returns <code>true</code>
     * by default.
     *
     * @return always <code>true</code>.
     */
    public boolean requiresLayout()
    {
        return true;
    }


    /**
     * This method determines if there is a sense in attempting to append.
     *
     * @return always <code>true</code>.
     */
    protected boolean checkEntryConditions()
    {
        return true;
    }
}
