package com.shehan.atukorala;

import static com.shehan.atukorala.TokenType.*;
public class Interpreter implements Expr.Visitor<Object>{

	@Override 
	public Object visitLiteralExpr(Expr.Literal expr) {
		return expr.value;
	}
	
	@Override
	public Object visitGroupingExpr(Expr.Grouping expr) {
		return evaluate(expr.expression);
	}
	
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	@Override 
	public Object visitUnaryExpr(Expr.Unary expr) {
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case BANG:
			return !isTruthy(right);
		case MINUS:
			return -(double)right;
		}
		/**
		 * In JavaScript, strings are truthy but empty strings are not. Arrays are truthy but empty array are... also truthy. The number 0 is falsey, but the string "0" is truthy.
		 * 
		 * In Python, empty strings are falsey like JS, but other empty sequences are falsey too. 
		 * 
		 * In PHP, both the number 0 and the string "0" are falsey. Most other non-empty string are truthy. 
		 * 
		 * In Lox, false and nil are falsey and everything else is truthy
		 */
		// Unreachable
		return null;
	}
	
	private boolean isTruthy(Object object) {
		if(object == null) return false;
		if(object instanceof Boolean) return (boolean)object;
		return true;
	}
	
	@Override 
	public Object visitBinaryExpr(Expr.Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS: 
			return (double)left - (double)right;
		case PLUS:
			if(left instanceof Double && right instanceof Double) {
				return (double)left + (double)right;
			}
			
			if(left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}
			
			break;
		case SLASH:
			return (double)left / (double)right;
		case STAR:
			return (double)left * (double)right;
		
		}
		
		// Unreachable
		return null;
	}
	
}
