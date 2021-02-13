package com.shehan.atukorala;

import java.util.List;

public class Stmt {
	
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
	 Expr statements;
	 List<Stmt> blocks;
	 
	 Block(List<Stmt> blocks){
		 this.blocks = blocks;
	 }
 }
 
 static class Class extends Stmt{
	 Token name;
	 List<Stmt.Function> methods;
	 
	 Class(Token name, List<Stmt.Function> methods){
		 this.name = name;
		 this.methods = methods;
	 }
 }
 
 static class Expression extends Stmt{
	 Expr increment;
	 
	 Expression(Expr increment){
		 this.increment = increment;
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
 }
 
 static class Return extends Stmt{
	Expr value; 
	Token keyword;
	
	Return(Token keyword, Expr value){
		this.value = value;
		this.keyword = keyword;
	}
 }
 
 static class Var extends Stmt{
	 
 }
 
 static class While extends Stmt{
	 Expr condition;
	 Stmt body;
	 
	 While(Expr condition, Stmt body){
		 this.condition = condition;
		 this.body = body;
	 }
	 
 }
 
 public void accept(Resolver resolver) {
	 
 }
 
}
