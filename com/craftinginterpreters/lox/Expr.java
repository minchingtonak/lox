package com.craftinginterpreters.lox;

import java.util.List;

abstract class Expr {
  interface Visitor<R> {
    R visitBinaryExpr(Binary expr);
    R visitGroupingExpr(Grouping expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
  }
  interface ContextVisitor<R, ContextType> {
     R visitBinaryExpr(Binary expr, ContextType context);
     R visitGroupingExpr(Grouping expr, ContextType context);
     R visitLiteralExpr(Literal expr, ContextType context);
     R visitUnaryExpr(Unary expr, ContextType context);
  }
  static class Binary extends Expr {
    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }

    @Override
    <R, T, C> R accept(ContextVisitor<R, C> visitor, C context) {
      return visitor.visitBinaryExpr(this, context);
    }

    final Expr left;
    final Token operator;
    final Expr right;
  }
  static class Grouping extends Expr {
    Grouping(Expr expression) {
      this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }

    @Override
    <R, T, C> R accept(ContextVisitor<R, C> visitor, C context) {
      return visitor.visitGroupingExpr(this, context);
    }

    final Expr expression;
  }
  static class Literal extends Expr {
    Literal(Object value) {
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }

    @Override
    <R, T, C> R accept(ContextVisitor<R, C> visitor, C context) {
      return visitor.visitLiteralExpr(this, context);
    }

    final Object value;
  }
  static class Unary extends Expr {
    Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }

    @Override
    <R, T, C> R accept(ContextVisitor<R, C> visitor, C context) {
      return visitor.visitUnaryExpr(this, context);
    }

    final Token operator;
    final Expr right;
  }

  abstract <R> R accept(Visitor<R> visitor);
  abstract <R, T, C> R accept(ContextVisitor<R, C> visitor, C context);
}
