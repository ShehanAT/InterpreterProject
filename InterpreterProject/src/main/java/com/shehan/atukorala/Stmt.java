package com.shehan.atukorala;

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
 }
 
 static class Class extends Stmt{
	 
 }
 
 static class Expression extends Stmt{
	 
 }
 
 static class Function extends Stmt{
	 
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
 }
 
 static class Var extends Stmt{
	 
 }
 
 static class While extends Stmt{
	 Expr condition;
	 Expr body;
 }
 
}
