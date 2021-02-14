package com.shehan.atukorala;

import java.util.List;

import com.shehan.atukorala.Stmt.Block;
import com.shehan.atukorala.Stmt.Class;
import com.shehan.atukorala.Stmt.If;

abstract class Expr {
	
	static class Unary extends Expr{
		Expr right;
		Token operator;
		
		public Unary(Token operator, Expr right) {
			this.right = right;
			this.operator = operator;
		}

		

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
		Void visitClassStmt(Class stmt);
		
	}
	
	static class Super extends Expr{
		Token keyword;
		Token method;
		
		Super(Token keyword, Token method){
			this.keyword = keyword;
			this.method = method;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	static class Set extends Expr{
		Expr object;
		Token name;
		Expr value;
		
		Set(Expr object, Token name, Expr value){
			
		}
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class Logical extends Expr{
		final Expr left;
		final Token operator;
		final Expr right;
		
		Logical(Expr left, Token operator, Expr right){
			this.left = left;
			this.operator = operator;
			this.right = right;

		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class Get extends Expr{
		Expr expr; 
		Token name;
		Expr object;
		
		Get(Expr expr, Token name){
			this.expr = expr;
			this.name = name;
		}
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	static class Call extends Expr{

		Expr callee;
		Token paren;
		List<Expr> arguments;
		
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
		Call(Expr callee, Token token, List<Expr> arguments){
			this.callee = callee;
			this.paren = paren;
			this.arguments = arguments;
		}
		
	}
	
	static class Literal extends Expr{
		private Object boolean1;
		Object value;
		
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
		Token name;
		
		public Variable(Token token) {
			
		}

		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static class This extends Expr{
		Token keyword;
		
		This(Token keyword){
			this.keyword = keyword;
		}
		@Override
		<R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	abstract <R> R accept(Visitor<R> visitor);
}
