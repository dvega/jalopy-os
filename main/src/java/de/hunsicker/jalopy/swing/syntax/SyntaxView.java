/*
 * Copyright (c) 2001-2002, Marco Hunsicker. All rights reserved.
 *
 * This software is distributable under the BSD license. See the terms of the BSD license
 * in the documentation provided with this software.
 */
/*
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is SATC. The Initial Developer of the Original Code is
 * Bogdan Mitu.
 *
 * Copyright(C) 2001-2002 Bogdan Mitu.
 */
package de.hunsicker.jalopy.swing.syntax;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;

import de.hunsicker.antlr.CharScanner;
import de.hunsicker.antlr.CharStreamException;
import de.hunsicker.antlr.Token;
import de.hunsicker.antlr.TokenStreamException;
import de.hunsicker.antlr.TokenStreamIOException;
import de.hunsicker.jalopy.language.JavaLexer;
import de.hunsicker.jalopy.language.JavaTokenTypes;
import de.hunsicker.util.StringHelper;


/**
 * A View for simple multi-line text with support for syntax highlight. When a line is to
 * be rendered, the document lexer is called to break the line into tokens, and each
 * token is rendered according to its type.
 *
 * @author Bogdan Mitu
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 *
 * @since 1.0b9
 */
final class SyntaxView
    extends PlainView
{
    //~ Static variables/initializers ----------------------------------------------------

    private static final Color COLOR_KEYWORD = Color.blue;
    private static final Color COLOR_COMMENT = new Color(0, 128, 128);
    private static final Color COLOR_NUMBER = Color.red;
    private static final Color COLOR_LITERAL = Color.gray;
    private static final Color COLOR_OPERATOR = new Color(0, 0, 128);

    //~ Instance variables ---------------------------------------------------------------

    /** DOCUMENT ME! */
    protected int tabBase;
    private DrawLineAction drawLineAction = new DrawLineAction();
    private Segment lineSegment = new Segment();

    /** Make this global to avoid creating a new one for each line parsed. */
    private Segment tokenSegment = new Segment();

    //~ Constructors ---------------------------------------------------------------------

    public SyntaxView(Element elem)
    {
        super(elem);
    }

    //~ Methods --------------------------------------------------------------------------

    public int getTabSize()
    {
        return ((JTextArea) getContainer()).getTabSize();
    }


    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param p0 DOCUMENT ME!
     * @param p1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws BadLocationException DOCUMENT ME!
     */
    public int drawUnselectedText(
        Graphics g,
        int      x,
        int      y,
        int      p0,
        int      p1)
      throws BadLocationException
    {
        SyntaxDocument document = (SyntaxDocument) getDocument();
        Element root = document.getDefaultRootElement();

        int lineIndex = root.getElementIndex(p0);
        SyntaxDocument.LeafElement line =
            (SyntaxDocument.LeafElement) root.getElement(lineIndex);

        // no need to tokenize commnent
        if (line.comment)
        {
            return drawCommentUnselectedText(g, x, y, p0, p1);
        }

        int result = 0;

        try
        {
            document.getText(p0, p1 - p0, lineSegment);
            drawLineAction.initialize(g, x, y, lineSegment, p0);

            SyntaxStream stream =
                (SyntaxStream) document.getProperty("stream" /* NOI18N */);
            stream.setRange(p0, p1);

            JavaLexer lexer = (JavaLexer) document.getProperty("lexer" /* NOI18N */);
            lexer.getInputState().reset();
            lexer.setLine(lineIndex);

            if (isCommentNext(lexer))
            {
                return drawCommentUnselectedText(
                    stream, document, lexer, lineIndex, g, x, y, p0, p1);
            }

            /**
             * @todo the call nextToken() may fail if we draw a partial line, i.e. part
             *       of a selection
             */
            for (
                Token token = lexer.nextToken(); token.getType() != Token.EOF_TYPE;
                token = lexer.nextToken())
            {
                drawLineAction.action(token);
                result = drawLineAction.x;

                if (isCommentNext(lexer))
                {
                    return drawCommentUnselectedText(
                        stream, document, lexer, lineIndex, g, x, y, p0, p1);
                }
            }
        }
        catch (TokenStreamIOException ex)
        {
            ;
        }
        catch (TokenStreamException ex)
        {
            ;
        }
        catch (RuntimeException ex)
        {
            System.err.println(ex.getClass().getName());
            ex.printStackTrace();
        }
        catch (Exception ex)
        {
            System.err.println(ex.getClass().getName());
            ex.printStackTrace();
        }

        return result;
    }


    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param tabOffset DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float nextTabStop(
        float x,
        int   tabOffset)
    {
        int tabSize = getTabSize() * this.metrics.charWidth('m');

        if (tabSize == 0)
        {
            return x;
        }

        int ntabs = (((int) x) - this.tabBase) / tabSize;

        return this.tabBase + ((ntabs + 1) * tabSize);
    }


    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param a DOCUMENT ME!
     */
    public void paint(
        Graphics g,
        Shape    a)
    {
        Rectangle alloc = (Rectangle) a;
        this.tabBase = alloc.x;
        super.paint(g, a);
    }


    /**
     * Determines whether the next token in the stream represents a multi-line comment.
     *
     * @param lexer the token lexer.
     *
     * @return <code>true</code> if the next token represents a multi-line comment.
     *
     * @throws CharStreamException DOCUMENT ME!
     */
    private boolean isCommentNext(CharScanner lexer)
      throws CharStreamException
    {
        switch (lexer.LA(1))
        {
            case '/' :

                switch (lexer.LA(2))
                {
                    case '*' :
                        return true;
                }

                break;
        }

        return false;
    }


    /**
     * Renders the given range in the model as unselected comment text.
     *
     * @param g the graphics context
     * @param x the starting X coordinate >= 0
     * @param y the starting Y coordinate >= 0
     * @param p0 the beginning position in the model >= 0
     * @param p1 the ending position in the model >= 0
     *
     * @return the X location of the end of the range >= 0
     *
     * @exception BadLocationException if the range is invalid
     */
    private int drawCommentUnselectedText(
        Graphics g,
        int      x,
        int      y,
        int      p0,
        int      p1)
      throws BadLocationException
    {
        g.setColor(COLOR_COMMENT);

        Document document = getDocument();
        document.getText(p0, p1 - p0, lineSegment);

        int ret = Utilities.drawTabbedText(lineSegment, x, y, g, this, p0);

        return ret;
    }


    private int drawCommentUnselectedText(
        SyntaxStream   stream,
        SyntaxDocument document,
        JavaLexer      lexer,
        int            lineIndex,
        Graphics       g,
        int            x,
        int            y,
        int            p0,
        int            p1)
      throws CharStreamException, BadLocationException
    {
        stream.setRange(p0, document.getLength());
        lexer.getInputState().reset();
        lexer.setLine(lineIndex);
        lexer.getInputBuffer().mark();
        lexer.consume();

        int l = lexer.getLine();

        int c = JavaLexer.EOF_CHAR;
SEEK_FORWARD: 
        while ((c = lexer.LA(1)) != JavaLexer.EOF_CHAR)
        {
            lexer.consume();

            switch (c)
            {
                case '*' :

                    switch (lexer.LA(1))
                    {
                        case '/' :
                            lexer.consume();
                            lexer.consume();

                            break SEEK_FORWARD;
                    }

                    break;
            }
        }

        String comment = lexer.getInputBuffer().getMarkedChars();
        int lineBreaks = StringHelper.occurs('\n', comment);
        lexer.getInputBuffer().commit();

        Element root = document.getDefaultRootElement();

        for (int i = l, size = i + lineBreaks; i <= size; i++)
        {
            SyntaxDocument.LeafElement el =
                (SyntaxDocument.LeafElement) root.getElement(i);
            el.comment = true;
        }

        return drawCommentUnselectedText(g, x, y, p0, p1);
    }

    //~ Inner Classes --------------------------------------------------------------------

    final class DrawLineAction
    {
        Graphics g;
        int docOffset;
        int x;
        int y;

        public void action(Token token)
        {
            switch (token.getType())
            {
                // keywords
                case JavaTokenTypes.LITERAL_void :
                case JavaTokenTypes.LITERAL_boolean :
                case JavaTokenTypes.LITERAL_byte :
                case JavaTokenTypes.LITERAL_char :
                case JavaTokenTypes.LITERAL_short :
                case JavaTokenTypes.LITERAL_int :
                case JavaTokenTypes.LITERAL_float :
                case JavaTokenTypes.LITERAL_long :
                case JavaTokenTypes.LITERAL_double :
                case JavaTokenTypes.LITERAL_package :
                case JavaTokenTypes.LITERAL_import :
                case JavaTokenTypes.LITERAL_private :
                case JavaTokenTypes.LITERAL_public :
                case JavaTokenTypes.LITERAL_protected :
                case JavaTokenTypes.LITERAL_static :
                case JavaTokenTypes.LITERAL_transient :
                case JavaTokenTypes.LITERAL_native :
                case JavaTokenTypes.LITERAL_synchronized :
                case JavaTokenTypes.LITERAL_volatile :
                case JavaTokenTypes.LITERAL_class :
                case JavaTokenTypes.LITERAL_extends :
                case JavaTokenTypes.LITERAL_implements :
                case JavaTokenTypes.LITERAL_interface :
                case JavaTokenTypes.LITERAL_this :
                case JavaTokenTypes.LITERAL_super :
                case JavaTokenTypes.LITERAL_throws :
                case JavaTokenTypes.LITERAL_if :
                case JavaTokenTypes.LITERAL_else :
                case JavaTokenTypes.LITERAL_for :
                case JavaTokenTypes.LITERAL_while :
                case JavaTokenTypes.LITERAL_do :
                case JavaTokenTypes.LITERAL_break :
                case JavaTokenTypes.LITERAL_assert :
                case JavaTokenTypes.LITERAL_continue :
                case JavaTokenTypes.LITERAL_return :
                case JavaTokenTypes.LITERAL_switch :
                case JavaTokenTypes.LITERAL_throw :
                case JavaTokenTypes.LITERAL_case :
                case JavaTokenTypes.LITERAL_default :
                case JavaTokenTypes.LITERAL_try :
                case JavaTokenTypes.LITERAL_catch :
                case JavaTokenTypes.LITERAL_finally :
                case JavaTokenTypes.LITERAL_instanceof :
                case JavaTokenTypes.LITERAL_true :
                case JavaTokenTypes.LITERAL_false :
                case JavaTokenTypes.LITERAL_null :
                case JavaTokenTypes.LITERAL_new :
                    this.g.setColor(COLOR_KEYWORD);

                    break;

                case JavaTokenTypes.SL_COMMENT :
                case JavaTokenTypes.ML_COMMENT :
                case JavaTokenTypes.JAVADOC_COMMENT :
                case JavaTokenTypes.SEPARATOR_COMMENT :
                case JavaTokenTypes.SPECIAL_COMMENT :
                    this.g.setColor(COLOR_COMMENT);

                    break;

                case JavaTokenTypes.NUM_INT :
                case JavaTokenTypes.NUM_FLOAT :
                case JavaTokenTypes.NUM_DOUBLE :
                case JavaTokenTypes.NUM_LONG :
                case JavaTokenTypes.FLOAT_SUFFIX :
                    this.g.setColor(COLOR_NUMBER);

                    break;

                case JavaTokenTypes.STRING_LITERAL :
                    this.g.setColor(COLOR_LITERAL);

                    break;

                case JavaTokenTypes.DOT :
                case JavaTokenTypes.QUESTION :
                case JavaTokenTypes.COLON :
                case JavaTokenTypes.ASSIGN :
                case JavaTokenTypes.EQUAL :
                case JavaTokenTypes.LNOT :
                case JavaTokenTypes.BNOT :
                case JavaTokenTypes.NOT_EQUAL :
                case JavaTokenTypes.DIV :
                case JavaTokenTypes.DIV_ASSIGN :
                case JavaTokenTypes.PLUS :
                case JavaTokenTypes.PLUS_ASSIGN :
                case JavaTokenTypes.INC :
                case JavaTokenTypes.MINUS :
                case JavaTokenTypes.MINUS_ASSIGN :
                case JavaTokenTypes.DEC :
                case JavaTokenTypes.STAR :
                case JavaTokenTypes.STAR_ASSIGN :
                case JavaTokenTypes.MOD :
                case JavaTokenTypes.MOD_ASSIGN :
                case JavaTokenTypes.SR :
                case JavaTokenTypes.SR_ASSIGN :
                case JavaTokenTypes.BSR :
                case JavaTokenTypes.BSR_ASSIGN :
                case JavaTokenTypes.GE :
                case JavaTokenTypes.GT :
                case JavaTokenTypes.SL :
                case JavaTokenTypes.SL_ASSIGN :
                case JavaTokenTypes.LE :
                case JavaTokenTypes.LT :
                case JavaTokenTypes.BXOR :
                case JavaTokenTypes.BXOR_ASSIGN :
                case JavaTokenTypes.BOR :
                case JavaTokenTypes.BOR_ASSIGN :
                case JavaTokenTypes.LOR :
                case JavaTokenTypes.LAND :
                    this.g.setColor(COLOR_OPERATOR);

                    break;

                default :
                    this.g.setColor(Color.black);

                    break;
            }

            try
            {
                tokenSegment.count = token.getText().length();
                this.x =
                    Utilities.drawTabbedText(
                        tokenSegment, this.x, this.y, this.g, SyntaxView.this, docOffset);
                docOffset += tokenSegment.count;
                tokenSegment.offset += tokenSegment.count;
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        public void initialize(
            Graphics g,
            int      x,
            int      y,
            Segment  lineSegment,
            int      startOffset)
        {
            this.g = g;
            this.x = x;
            this.y = y;
            tokenSegment = lineSegment;
            docOffset = startOffset;
        }
    }
}
