package com.shehan.atukorala;

import java.util.List;

import com.shehan.atukorala.Expr.Variable;

abstract class Stmt {
	
 interface Visitor<T>{
	 T visitBlockStmt(Block stmt);
	 T visitClassStmt(Class stmt);
	 T visitExpressionStmt(Expression stmt);
	 T visitFunctionStmt(Function stmt);
	 T visitIfStmt(If stmt);
	 T visitPrintStmt(Print stmt);
	 T visitReturnStmt(Return stmt);
	 T visitVarStmt(Var stmt);
	 T visitWhileStmt(While stmt);
 }
 
 static class Block extends Stmt{
	 List<Stmt> statements;;
	 
	 Block(List<Stmt> statements){
		 this.statements = statements;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class Class extends Stmt{
	 Token name;
	 List<Stmt.Function> methods;
	 Expr.Variable superclass;
	 
	 Class(Token name, Expr.Variable superclass, List<Stmt.Function> methods){
		 this.name = name;
		 this.methods = methods;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class Expression extends Stmt{
	 Expr expression;
 
	 Expression(Expr expression){
		 this.expression = expression;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class Function extends Stmt{
	 Token name;
	 List<Token> params;
	 List<Stmt> body;
	 
	 Function(Token name, List<Token> params, List<Stmt> body){
		 this.name = name;
		 this.params = params;
		 this.body = body;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class If extends Stmt{
	 final Expr condition;
	 final Stmt thenBranch;
	 final Stmt elseBranch;
	 
	 If(Expr condition, Stmt thenBranch, Stmt elseBranch){
		 this.condition = condition;
		 this.thenBranch = thenBranch;
		 this.elseBranch = elseBranch;
	 }
	 
	
	 <R> R accept(Visitor<R> visitor) {
		 return visitor.visitIfStmt(this);
	 }
 }
 
 static class Print extends Stmt{
	 Expr expression;
	 
	 Print(Expr expression){
		 this.expression = expression;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class Return extends Stmt{
	Expr value; 
	Token keyword;
	
	Return(Token keyword, Expr value){
		this.value = value;
		this.keyword = keyword;
	}

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class Var extends Stmt{
	 Expr initializer;
	 Token name;
	 
	 Var(Token name, Expr initializer){
		 this.initializer = initializer;
		 this.name = name;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
 }
 
 static class While extends Stmt{
	 Expr condition;
	 Stmt body;
	 
	 While(Expr condition, Stmt body){
		 this.condition = condition;
		 this.body = body;
	 }

	@Override
	<A> A accept(Visitor<A> visitor) {
		// TODO Auto-generated method stub
		return null;
	}
	 
 }
 //replacing 'T' generic placeholder with 'A' to see if specific characters matter, verdict: no
 abstract <A> A accept(Visitor<A> visitor);
}
