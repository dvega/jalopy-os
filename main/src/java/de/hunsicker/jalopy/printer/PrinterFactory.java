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
package de.hunsicker.jalopy.printer;

import de.hunsicker.antlr.collections.AST;
import de.hunsicker.jalopy.parser.JavaTokenTypes;


/**
 * Central facility to create printers.
 *
 * @author <a href="http://jalopy.sf.net/contact.html">Marco Hunsicker</a>
 * @version $Revision$
 */
public final class PrinterFactory
{
    //~ Constructors ··························································

    /**
     * Creates a new PrinterFactory object.
     */
    private PrinterFactory()
    {
    }

    //~ Methods ·······························································

    /**
     * Returns a printer instance for the given node.
     *
     * @param node the node to print.
     *
     * @return The printer object for the given node.
     *
     * @throws IllegalArgumentException if no viable printer for the given
     *         node is known by the factory.
     */
    public static Printer create(AST node)
    {
        int type = node.getType();
        Printer result = null;

        switch (type)
        {
            /*case JavaTokenTypes.RBRACK:*/
            case JavaTokenTypes.RCURLY :
                result = SkipPrinter.getInstance();

                break;

            case JavaTokenTypes.COMMA :
                result = CommaPrinter.getInstance();

                break;

            case JavaTokenTypes.MODIFIERS :
                result = ModifiersPrinter.getInstance();

                break;

            case JavaTokenTypes.EMPTY_STAT :
                result = EmptyStatementPrinter.getInstance();

                break;

            case JavaTokenTypes.ASSIGN :
                result = AssignmentPrinter.getInstance();

                break;

            case JavaTokenTypes.SEMI :
                result = SemiPrinter.getInstance();

                break;

            case JavaTokenTypes.LPAREN :
                result = LeftParenthesisPrinter.getInstance();

                break;

            case JavaTokenTypes.RPAREN :
                result = RightParenthesisPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_do :
                result = DoWhilePrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_assert :
                result = AssertionPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_try :
                result = TryCatchFinallyPrinter.getInstance();

                break;

            case JavaTokenTypes.EXPR :
                result = ExpressionPrinter.getInstance();

                break;

            case JavaTokenTypes.INSTANCE_INIT :
                result = InstanceInitPrinter.getInstance();

                break;

            case JavaTokenTypes.CTOR_DEF :
                result = ConstructorDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.SUPER_CTOR_CALL :
            case JavaTokenTypes.CTOR_CALL :
                result = ConstructorCallPrinter.getInstance();

                break;

            case JavaTokenTypes.METHOD_DEF :
                result = MethodDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.VARIABLE_DEF :
                result = VariableDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.ARRAY_INIT :
                result = ArrayInitializerPrinter.getInstance();

                break;

            case JavaTokenTypes.STATIC_INIT :
                result = StaticInitPrinter.getInstance();

                break;

            case JavaTokenTypes.ARRAY_DECLARATOR :
                result = ArrayTypePrinter.getInstance();

                break;

            case JavaTokenTypes.EXTENDS_CLAUSE :
                result = ExtendsPrinter.getInstance();

                break;

            case JavaTokenTypes.IMPLEMENTS_CLAUSE :
                result = ImplementsPrinter.getInstance();

                break;

            case JavaTokenTypes.ELIST :
            case JavaTokenTypes.PARAMETERS :
                result = ParametersPrinter.getInstance();

                break;

            case JavaTokenTypes.PARAMETER_DEF :
                result = ParameterDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.TYPECAST :
                result = TypeCastPrinter.getInstance();

                break;

            case JavaTokenTypes.TYPE :
                result = TypePrinter.getInstance();

                break;

            case JavaTokenTypes.METHOD_CALL :
                result = MethodCallPrinter.getInstance();

                break;

            case JavaTokenTypes.POST_INC :
            case JavaTokenTypes.POST_DEC :
                result = PostfixOperatorPrinter.getInstance();

                break;

            case JavaTokenTypes.INDEX_OP :
                result = IndexOperatorPrinter.getInstance();

                break;

            case JavaTokenTypes.LABELED_STAT :
                result = LabelStatementPrinter.getInstance();

                break;

            case JavaTokenTypes.INTERFACE_DEF :
                result = InterfaceDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.INC :
            case JavaTokenTypes.DEC :
            case JavaTokenTypes.LNOT :
            case JavaTokenTypes.BNOT :
            case JavaTokenTypes.UNARY_MINUS :
            case JavaTokenTypes.UNARY_PLUS :
                result = PrefixOperatorPrinter.getInstance();

                break;

            // Logical operators
            case JavaTokenTypes.LOR :
            case JavaTokenTypes.LAND :
                result = LogicalOperatorPrinter.getInstance();

                break;

            // Bitwise operators
            case JavaTokenTypes.BAND :
            case JavaTokenTypes.BOR :
            case JavaTokenTypes.BXOR :
                result = BitwiseOperatorPrinter.getInstance();

                break;

            // Mathematical operators
            case JavaTokenTypes.PLUS :
            case JavaTokenTypes.MINUS :
            case JavaTokenTypes.STAR :
            case JavaTokenTypes.DIV :
            case JavaTokenTypes.MOD :
                result = MathematicalOperatorPrinter.getInstance();

                break;

            // Relational operators
            case JavaTokenTypes.EQUAL :
            case JavaTokenTypes.NOT_EQUAL :
            case JavaTokenTypes.LT :
            case JavaTokenTypes.GT :
            case JavaTokenTypes.LE :
            case JavaTokenTypes.GE :
            case JavaTokenTypes.LITERAL_instanceof :
                result = RelationalOperatorPrinter.getInstance();

                break;

            // Shift operators
            case JavaTokenTypes.SL :
            case JavaTokenTypes.SR :
            case JavaTokenTypes.BSR :
                result = ShiftOperatorPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_public :
            case JavaTokenTypes.LITERAL_private :
            case JavaTokenTypes.LITERAL_static :
            case JavaTokenTypes.LITERAL_protected :
            case JavaTokenTypes.FINAL :
            case JavaTokenTypes.LITERAL_transient :
            case JavaTokenTypes.ABSTRACT :
            case JavaTokenTypes.LITERAL_synchronized :
            case JavaTokenTypes.LITERAL_volatile :
            case JavaTokenTypes.LITERAL_native :
            case JavaTokenTypes.STRICTFP :
                result = ModifierPrinter.getInstance();

                break;

            case JavaTokenTypes.SYNBLOCK :
                result = SynchronizedPrinter.getInstance();

                break;

            case JavaTokenTypes.IDENT :
            case JavaTokenTypes.LITERAL_void :
            case JavaTokenTypes.LITERAL_boolean :
            case JavaTokenTypes.LITERAL_byte :
            case JavaTokenTypes.LITERAL_char :
            case JavaTokenTypes.LITERAL_short :
            case JavaTokenTypes.LITERAL_int :
            case JavaTokenTypes.LITERAL_float :
            case JavaTokenTypes.LITERAL_long :
            case JavaTokenTypes.LITERAL_double :
            case JavaTokenTypes.NUM_INT :
            case JavaTokenTypes.CHAR_LITERAL :
            case JavaTokenTypes.STRING_LITERAL :
            case JavaTokenTypes.NUM_FLOAT :
            case JavaTokenTypes.NUM_DOUBLE :
            case JavaTokenTypes.ESC :
            case JavaTokenTypes.HEX_DIGIT :
            case JavaTokenTypes.VOCAB :
            case JavaTokenTypes.EXPONENT :
            case JavaTokenTypes.FLOAT_SUFFIX :
            case JavaTokenTypes.LITERAL_this :
            case JavaTokenTypes.LITERAL_super :
            case JavaTokenTypes.LITERAL_true :
            case JavaTokenTypes.LITERAL_false :
            case JavaTokenTypes.LITERAL_null :
            case JavaTokenTypes.LITERAL_class :
            case JavaTokenTypes.COLON :
                result = BasicPrinter.getInstance();

                break;

            case JavaTokenTypes.NUM_LONG :
                result = LongLiteralPrinter.getInstance();

                break;

            case JavaTokenTypes.DOT :
                result = DotPrinter.getInstance();

                break;

            case JavaTokenTypes.PLUS_ASSIGN :
            case JavaTokenTypes.MINUS_ASSIGN :
            case JavaTokenTypes.STAR_ASSIGN :
            case JavaTokenTypes.DIV_ASSIGN :
            case JavaTokenTypes.MOD_ASSIGN :
            case JavaTokenTypes.BAND_ASSIGN :
            case JavaTokenTypes.BOR_ASSIGN :
            case JavaTokenTypes.BXOR_ASSIGN :
            case JavaTokenTypes.SL_ASSIGN :
            case JavaTokenTypes.SR_ASSIGN :
            case JavaTokenTypes.BSR_ASSIGN :
                result = AssignmentOperatorPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_throws :
                result = ThrowsPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_for :
                result = ForPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_if :
                result = IfElsePrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_while :
                result = WhilePrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_case :
            case JavaTokenTypes.LITERAL_default :
            case JavaTokenTypes.CASE_GROUP :
                result = CasePrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_switch :
                result = SwitchPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_break :
            case JavaTokenTypes.LITERAL_continue :
                result = FlowControlPrinter.getInstance();

                break;

            case JavaTokenTypes.QUESTION :
                result = TernaryIfElsePrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_new :
                result = CreatorPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_return :
                result = ReturnPrinter.getInstance();

                break;

            case JavaTokenTypes.LITERAL_throw :
                result = ThrowPrinter.getInstance();

                break;

            case JavaTokenTypes.JAVADOC_COMMENT :
                result = JavadocPrinter.getInstance();

                break;

            case JavaTokenTypes.SLIST :
            case JavaTokenTypes.OBJBLOCK :
                result = BlockPrinter.getInstance();

                break;

            case JavaTokenTypes.CASESLIST :
                result = CaseBlockPrinter.getInstance();

                break;

            case JavaTokenTypes.IMPORT :
                result = ImportPrinter.getInstance();

                break;

            case JavaTokenTypes.CLASS_DEF :
                result = ClassDeclarationPrinter.getInstance();

                break;

            case JavaTokenTypes.PACKAGE_DEF :
                result = PackagePrinter.getInstance();

                break;

            case JavaTokenTypes.ROOT :
                result = JavaPrinter.getInstance();

                break;

            default :
                throw new IllegalArgumentException("no viable printer for -- " +
                                                   node);
        }

        return result;
    }
}
