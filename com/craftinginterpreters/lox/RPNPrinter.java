package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Assign;
import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Ternary;
import com.craftinginterpreters.lox.Expr.Unary;
import com.craftinginterpreters.lox.Expr.Variable;
import com.craftinginterpreters.lox.Expr.Visitor;

class RPNPrinter implements Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }


    @Override
    public String visitAssignExpr(Assign expr) {
        return RPNFormat("= " + expr.name.lexeme, expr.value);
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return RPNFormat("var " + expr.name.lexeme);
    }

    @Override
    public String visitTernaryExpr(Ternary expr) {
        return RPNFormat("?:", expr.condition, expr.if_true, expr.if_false);
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return RPNFormat(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return expr.expression.accept(this); // Don't print anything for a grouping, just recurse
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        return expr.value == null ? "nil" : expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return RPNFormat(expr.operator.lexeme, expr.right);
    }

    private String RPNFormat(String lexeme, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        for (Expr expr : exprs) {
            sb.append(expr.accept(this));
            sb.append(" ");
        }

        return sb.append(lexeme).toString();
    }

    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(1),
                        new Token(TokenType.PLUS, "+", null, 1), new Expr.Literal(2))),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(new Expr.Binary(new Expr.Literal(4),
                        new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(3))));

        System.out.println(new RPNPrinter().print(expression));
    }
}
