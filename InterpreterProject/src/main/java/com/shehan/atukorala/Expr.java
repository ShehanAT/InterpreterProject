package com.shehan.atukorala;

abstract class Expr {
	static class Unary extends Expr{

		public Unary(Token operator2, Expr right) {
			// TODO Auto-generated constructor stub
		}

		public Object operator;

	}

	static class Grouping extends Expr{
		private Expr expr1;
		
		public Grouping(Expr expr1) {
			this.expr1 = expr1;
		}
	}

	public interface Visitor<T> {
		Object visitLiteralExpr(Literal expr);
		Object visitGroupingExpr(Grouping expr);
		Object visitBinaryExpr(Binary expr);
	}

	static class Literal extends Expr{
		private Object boolean1;
		
		public Literal(Object boolean1) {
			this.boolean1 = boolean1;
		}
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
	
	static class Variable extends Expr{
		public Variable(Token token) {
			
		}
	}
}
