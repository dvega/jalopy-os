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

import de.hunsicker.jalopy.storage.Defaults;
import de.hunsicker.jalopy.storage.Keys;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.util.ChainingRuntimeException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.StringSubstitution;
import org.apache.oro.text.regex.Substitution;
import org.apache.oro.text.regex.Util;


/**
 * Provides access to global and local environment variables (key/value
 * pairs).
 *
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 *
 * @since 1.0b8
 */
public final class Environment
{
    //~ Static variables/initializers ·········································

    /** Delimeter for the encoded variables string. */
    private static final String DELIMETER = "|";

    /** The compiler to create pattern objects. */
    private static final PatternCompiler REGEXP_COMPILER = new Perl5Compiler();

    /** The pattern to search for variables . */
    private static Pattern _variablesPattern;

    static
    {
        try
        {
            _variablesPattern = REGEXP_COMPILER.compile("\\$([a-zA-Z_][a-zA-Z0-9_.]+)\\$",
                                                        Perl5Compiler.READ_ONLY_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ChainingRuntimeException(ex);
        }
    }

    private static final Environment INSTANCE = new Environment(true);

    //~ Instance variables ····················································

    /** The pattern matcher. */
    private final PatternMatcher _matcher = new Perl5Matcher();

    /** The current environment variables. */
    private Map _variables; // Map of <String:String>

    //~ Constructors ··························································

    /**
     * Creates a new Environment object.
     *
     * @param initial DOCUMENT ME!
     */
    private Environment(boolean initial)
    {
        if (initial)
        {
            _variables = new HashMap(25);
            _variables.putAll(System.getProperties());

            Convention settings = Convention.getInstance();
            String variables = settings.get(Keys.ENVIRONMENT, Defaults.ENVIRONMENT);

            if (!"".equals(variables))
            {
                for (StringTokenizer tokens = new StringTokenizer(variables,
                                                                  DELIMETER);
                     tokens.hasMoreElements();)
                {
                    String pair = tokens.nextToken();
                    int delimOffset = pair.indexOf('^');
                    String key = pair.substring(0, delimOffset);
                    String value = pair.substring(delimOffset + 1);
                    _variables.put(key, value);
                }
            }
        }
    }

    //~ Methods ·······························································

    /**
     * Returns an instance of this class. This method is guarantueed to return
     * a unique instance for each thread that invokes it.
     *
     * @return An instance of this class.
     */
    public static Environment getInstance()
    {
        return INSTANCE;
    }


    /**
     * Returns a copy of this object.
     *
     * @return a copy of this object.
     */
    public Environment copy()
    {
        Environment environment = new Environment(false);
        environment._variables = new HashMap(INSTANCE._variables);

        return environment;
    }


    /**
     * Performs variable interpolation for the given input string. All
     * environment variable expressions
     * (<code>\$[a-zA-Z_][a-zA-Z0-9_.]+\$</code>, e.g. $fileName$ or $author$)
     * in the given string are replaced with their corresponding environment
     * value.
     *
     * @param str string to perform variable interpolation for.
     *
     * @return The input <em>str</em> with all variables interpolated.
     *
     * @see #set
     */
    public String interpolate(String str)
    {
        PatternMatcherInput input = new PatternMatcherInput(str);
        Map keys = new HashMap(10);

        // map all found variable expressions with their environment variable
        while (_matcher.contains(input, _variablesPattern))
        {
            MatchResult result = _matcher.getMatch();
            String value = (String)_variables.get(result.group(1));

            // the value has to be set in order to be substituted
            if (value != null && value.length() > 0)
                keys.put(result.group(0), value);
        }

        // and finally interpolate them
        for (Iterator i = keys.entrySet().iterator(); i.hasNext();)
        {
            Map.Entry entry = (Map.Entry)i.next();

            String key = (String)entry.getKey();
            String value = (String)entry.getValue();

            Substitution substitution = new StringSubstitution(value);
            Pattern pattern = null;

            try
            {
                /**
                 * @todo use a cache
                 */
                pattern = REGEXP_COMPILER.compile(Perl5Compiler.quotemeta(key));
            }
            catch (MalformedPatternException ex)
            {
                continue;
            }

            str = Util.substitute(_matcher, pattern, substitution, str,
                                  Util.SUBSTITUTE_ALL);
        }

        return str;
    }


    /**
     * Sets the given variable to the given value.
     *
     * @param variable variable name. Valid variable names have the form
     *        <nobr><code>[a-zA-Z_][a-zA-Z0-9_.]+</code></nobr>
     * @param value value to associate.
     *
     * @see #unset
     */
    public void set(String variable,
                    String value)
    {
        _variables.put(variable, value);
    }


    /**
     * Returns a string representation of this object. The string
     * representation consists of a list of key-value mappings in no
     * particular order.
     *
     * @return a string representation of this object.
     */
    public String toString()
    {
        return _variables.toString();
    }


    /**
     * Unsets (removes) the given variable
     *
     * @param variable variable name.
     *
     * @see #set
     */
    public void unset(String variable)
    {
        _variables.remove(variable);
    }

    //~ Inner Classes ·························································

    /**
     * Represents a local environment variable.
     */
    public static final class Variable
    {
        /** Defines the variable &quot;file&quot;. */
        public static final Variable FILE = new Variable("file");

        /** Defines the variable &quot;fileName&quot;. */
        public static final Variable FILE_NAME = new Variable("fileName");

        /** Defines the variable &quot;fileFormat&quot;. */
        public static final Variable FILE_FORMAT = new Variable("fileFormat");

        /** Defines the variable &quot;tabSize&quot;. */
        public static final Variable TAB_SIZE = new Variable("tabSize");

        /** Defines the variable &quot;convention&quot;. */
        public static final Variable CONVENTION = new Variable("convention");

        /** Defines the variable &quot;package&quot;. */
        public static final Variable PACKAGE = new Variable("package");

        /** Defines the variable &quot;paramType&quot;. */
        public static final Variable TYPE_PARAM = new Variable("paramType");

        /** Defines the variable &quot;exceptionType&quot;. */
        public static final Variable TYPE_EXCEPTION = new Variable("exceptionType");

        /** Defines the variable &quot;objectType&quot;. */
        public static final Variable TYPE_OBJECT = new Variable("objectType");
        String name;
        int hashCode;

        private Variable(String name)
        {
            this.name = name.intern();
            this.hashCode = this.name.hashCode();
        }

        /**
         * Returns the name of the variable.
         *
         * @return variable name.
         */
        public String getName()
        {
            return this.name;
        }

        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }

            if (o instanceof Environment.Variable)
            {
                return this.name == ((Environment.Variable)o).name;
            }

            return false;
        }


        public int hashCode()
        {
            return this.hashCode;
        }


        /**
         * Returns a string representation of this object.
         *
         * @return A string representation of this object.
         *
         * @see #getName
         */
        public String toString()
        {
            return this.name;
        }
    }
}
