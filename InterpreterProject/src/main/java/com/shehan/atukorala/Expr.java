package com.shehan.atukorala;

abstract class Expr {
	public class Unary {

		public Object operator;

	}

	public class Grouping {

	}

	public interface Visitor<T> {
		Object visitLiteralExpr(Literal expr);
		Object visitGroupingExpr(Grouping expr);
		Object visitBinaryExpr(Binary expr);
	}

	public class Literal {

	}

	static class Binary extends Expr{
		Binary(Expr left, Token operator, Expr right){
			this.left = left;
			this.operator = operator;
			this.right = right;
		}
		
		final Expr left;
		final Token operator;
		final Expr right;
	}
}
