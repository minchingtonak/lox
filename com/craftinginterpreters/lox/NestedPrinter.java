package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.ContextVisitor;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Unary;
import com.craftinginterpreters.lox.Expr.Visitor;

// TODO better way to handle having subclass of Context tham having it as a generic in the ContextVisitor interface?
class NestedPrinter implements ContextVisitor<String, PrintContext> {

    public String print(Expr expr) {
        PrintContext pc = new PrintContext();
        return expr.accept(this, pc);
    }


    @Override
    public String visitBinaryExpr(Binary expr, PrintContext context) {
        return nestPrint(context, context.indent() + expr.operator.lexeme, expr.left, expr.right);
    }


    @Override
    public String visitGroupingExpr(Grouping expr, PrintContext context) {
        return nestPrint(context, context.indent() + "g", expr.expression);
    }


    @Override
    public String visitLiteralExpr(Literal expr, PrintContext context) {
        // context.increaseIndent();
        String indent = context.increaseIndent();
        String to_return = expr.value == null ? indent + "nil\n" : indent + expr.value.toString() + "\n";
        // context.decreaseIndent();
        context.decreaseIndent();
        return to_return;
    }


    @Override
    public String visitUnaryExpr(Unary expr, PrintContext context) {
        return nestPrint(context, context.indent() + expr.operator.lexeme, expr.right);
    }


    private String nestPrint(PrintContext context, String lexeme, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append(lexeme).append(" {\n");
        context.increaseIndent();
        for (Expr expr : exprs) {
            sb.append(expr.accept(this, context));
        }
        context.decreaseIndent();
        return sb.append(context.indent()).append("}\n").toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(1),
                        new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2))),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(4),
                        new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3))));

        System.out.println(new NestedPrinter().print(expression));
    }

}
