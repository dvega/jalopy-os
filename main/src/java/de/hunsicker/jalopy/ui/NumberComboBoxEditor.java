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
package de.hunsicker.jalopy.ui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;


/**
 * ComboBoxEditor which only allows whole numbers as user input.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
class NumberComboBoxEditor
    extends BasicComboBoxEditor
{
    //~ Constructors ··························································

    /**
     * Creates a new NumberComboBoxEditor.
     *
     * @param value the initial value to display.
     * @param columns number of columns to use.
     */
    public NumberComboBoxEditor(final int value,
                                int       columns)
    {
        final NumberTextField textField = new NumberTextField(value, columns);
        textField.addFocusListener(new FocusAdapter()
            {
                public void focusLost(FocusEvent ev)
                {
                    String test = textField.getText();

                    // if the field contains no valid number, we use a default value
                    if ((test == null) || test.trim().equals(""))
                    {
                        textField.setText(String.valueOf(value));
                    }
                }
            });
        this.editor = textField;
    }

    //~ Inner Classes ·························································

    /**
     * TextField used to display and edit the user value.
     */
    private static class NumberTextField
        extends JTextField
    {
        final int initialValue;
        NumberFormat integerFormatter;

        /**
         * Creates a new NumberTextField object.
         *
         * @param value
         * @param columns
         */
        public NumberTextField(int value,
                               int columns)
        {
            super(columns);

            NumberDocument document = (NumberDocument)getDocument();
            this.initialValue = value;
            document.initialValue = value;
            this.integerFormatter = NumberFormat.getNumberInstance();
            this.integerFormatter.setParseIntegerOnly(true);
            setValue(value);
        }

        public void setValue(int value)
        {
            setText(integerFormatter.format(value));
        }


        public int getValue()
        {
            try
            {
                return integerFormatter.parse(getText()).intValue();
            }
            catch (ParseException ex)
            {
                return this.initialValue;
            }
        }


        protected Document createDefaultModel()
        {
            return new NumberDocument();
        }

        private static class NumberDocument
            extends PlainDocument
        {
            int initialValue;

            public NumberDocument()
            {
            }

            public void insertString(int          offs,
                                     String       str,
                                     AttributeSet atts)
                throws BadLocationException
            {
                char[] source = str.toCharArray();
                char[] result = new char[source.length];
                int j = 0;

                for (int i = 0; i < result.length; i++)
                {
                    if (Character.isDigit(source[i]))
                    {
                        result[j++] = source[i];
                    }
                    else if (offs == 0)
                    {
                        super.insertString(offs,
                                           String.valueOf(this.initialValue),
                                           atts);

                        return;
                    }
                }

                super.insertString(offs, new String(result, 0, j), atts);
            }
        }
    }
}
