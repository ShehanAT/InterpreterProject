package com.shehan.atukorala;

import java.util.List;

import com.shehan.atukorala.Stmt.Block;
import com.shehan.atukorala.Stmt.If;

abstract class Expr {
	
	static class Unary extends Expr{
		Expr right;
		public Unary(Token operator2, Expr right) {
			// TODO Auto-generated constructor stub
		}

		public Object operator;

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	
	static class Assign extends Expr{
		final Token name;
		final Expr value;
		
		Assign(Token name, Expr value){
			this.name = name;
			this.value = value;
		}
		
		@Override 
		<R> R accept(Visitor<R> visitor) {
			return visitor.visitAssignExpr(this);
		}
	}

	static class Grouping extends Expr{
		private Expr expr1;
		Expr expression;//static class variables are public by default
		
		public Grouping(Expr expr1) {
			this.expr1 = expr1;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	public interface Visitor<T> {
		T visitLiteralExpr(Literal expr);
		T visitGroupingExpr(Grouping expr);
		T visitBinaryExpr(Binary expr);
		T visitAssignExpr(Assign expr);
		T visitCallExpr(Call expr);
		T visitGetExpr(Get expr);
		T visitLogicalExpr(Logical expr);
		T visitSetExpr(Set expr);
		T visitSuperExpr(Super expr);
		T visitUnaryExpr(Unary expr);
		T visitVariableExpr(Variable expr);
		Void visitBlockStmt(Block stmt);
		Void visitIfStmt(If stmt);
		
	}
	
	static class Super extends Expr{
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	static class Set extends Expr{

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class Logical extends Expr{

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class Get extends Expr{

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	static class Call extends Expr{

		Expr callee;
		List<Expr> arguments;
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	static class Literal extends Expr{
		private Object boolean1;
		
		public Literal(Object boolean1) {
			this.boolean1 = boolean1;
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
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
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class Variable extends Expr{
		public Variable(Token token) {
			
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	abstract <R> R accept(Visitor<R> visitor);
}
