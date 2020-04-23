package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.ContextVisitor;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Unary;

class PrettyPrinter implements ContextVisitor<String, PrintContext> {

    public String print(Expr expr) {
        PrintContext context = new PrintContext(1);
        return expr.accept(this, context);
    }


    @Override
    public String visitBinaryExpr(Binary expr, PrintContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("(operator) ").append(expr.operator.lexeme);
        return prettyPrint(context, "BinaryExpr", new String[] {sb.toString()},
                new String[] {"(left)", "(right)"}, new Expr[] {expr.left, expr.right});
    }

    @Override
    public String visitGroupingExpr(Grouping expr, PrintContext context) {
        return prettyPrint(context, "Grouping", new String[] {}, new String[] {"(expr)"},
                new Expr[] {expr.expression});
    }

    @Override
    public String visitLiteralExpr(Literal expr, PrintContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("Literal: ").append(expr.value == null ? "nil" : expr.value.toString())
                .append("\n");
        return sb.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr, PrintContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("(operator) ").append(expr.operator.lexeme);
        return prettyPrint(context, "UnaryExpr", new String[] {sb.toString()},
                new String[] {"(expr)"}, new Expr[] {expr.right});
    }


    private String prettyPrint(PrintContext context, String name, String[] info, String[] fields,
            Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append(name + ":\n");
        context.increaseIndent();
        for (String i : info) {
            sb.append(context.indent()).append(i).append("\n");
        }

        for (int i = 0; i < fields.length; ++i) {
            sb.append(context.indent()).append(fields[i] + " ");
            context.increaseIndent();
            sb.append(exprs[i].accept(this, context));
            context.decreaseIndent();
        }

        context.decreaseIndent();
        return sb.toString();
    }

    public static void main(String[] args) {
        Expr expression =
                new Expr.Binary(
                        new Expr.Grouping(new Expr.Binary(
                                new Expr.Binary(
                                        new Expr.Grouping(new Expr.Binary(new Expr.Literal(1),
                                                new Token(TokenType.PLUS, "+", null, 1),
                                                new Expr.Literal(2))),
                                        new Token(TokenType.STAR, "*", null, 1),
                                        new Expr.Grouping(new Expr.Binary(new Expr.Literal(4),
                                                new Token(TokenType.MINUS, "-", null, 1),
                                                new Expr.Unary(
                                                        new Token(TokenType.MINUS, "-", null, 1),
                                                        new Expr.Literal(3))))),
                                new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2))),
                        new Token(TokenType.STAR, "*", null, 1),
                        new Expr.Grouping(
                                new Expr.Binary(new Expr.Literal(4),
                                        new Token(TokenType.MINUS, "-", null, 1),
                                        new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1),
                                                new Expr.Binary(
                                                        new Expr.Grouping(new Expr.Binary(
                                                                new Expr.Literal(1),
                                                                new Token(
                                                                        TokenType.PLUS, "+", null,
                                                                        1),
                                                                new Expr.Literal(2))),
                                                        new Token(TokenType.STAR, "*", null, 1),
                                                        new Expr.Grouping(new Expr.Binary(
                                                                new Expr.Literal(4),
                                                                new Token(TokenType.MINUS, "-",
                                                                        null, 1),
                                                                new Expr.Unary(
                                                                        new Token(TokenType.MINUS,
                                                                                "-", null, 1),
                                                                        new Expr.Literal(3)))))))));

        System.out.println(new PrettyPrinter().print(expression));
    }
}
