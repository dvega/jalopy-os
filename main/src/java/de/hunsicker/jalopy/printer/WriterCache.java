/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
package de.hunsicker.jalopy.printer;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple cache to avoid continually creating and destroying new TestNodeWriter
 * objects.
 *
 * @since 1.0b9
 */
final class WriterCache
{
    //~ Instance variables ---------------------------------------------------------------

    /** The cached writers. */
    private final List _writers = new ArrayList();
    private final String _originalLineSeparator;

    //~ Constructors ---------------------------------------------------------------------

    public WriterCache(NodeWriter writer)
    {
        _originalLineSeparator = writer.originalLineSeparator;

        TestNodeWriter tester = new TestNodeWriter();
        tester.originalLineSeparator = _originalLineSeparator;
        _writers.add(tester);
    }

    //~ Methods --------------------------------------------------------------------------

    /**
     * Returns a TestNodeWriter object. If the cache is not empty, an element from the
     * cache will be returned; otherwise a new object will be created.
     *
     * @return a TestNodeWriter object.
     */
    public TestNodeWriter get()
    {
        synchronized (this)
        {
            if (_writers.size() > 0)
            {
                return (TestNodeWriter) _writers.remove(0);
            }
        }

        TestNodeWriter tester = new TestNodeWriter();
        tester.originalLineSeparator = _originalLineSeparator;

        return tester;
    }


    /**
     * Releases the given writer and adds it to the cache.
     *
     * @param writer the writer object that should be cached.
     */
    public synchronized void release(TestNodeWriter writer)
    {
        writer.reset();
        _writers.add(writer);
    }
}
