package com.shehan.atukorala;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.shehan.atukorala.Expr.Assign;
import com.shehan.atukorala.Expr.Binary;
import com.shehan.atukorala.Expr.Call;
import com.shehan.atukorala.Expr.Get;
import com.shehan.atukorala.Expr.Grouping;
import com.shehan.atukorala.Expr.Literal;
import com.shehan.atukorala.Expr.Logical;
import com.shehan.atukorala.Expr.Set;
import com.shehan.atukorala.Expr.Super;
import com.shehan.atukorala.Expr.Unary;
import com.shehan.atukorala.Expr.Variable;
import com.shehan.atukorala.Stmt.Block;
import com.shehan.atukorala.Stmt.Class;
import com.shehan.atukorala.Stmt.Expression;
import com.shehan.atukorala.Stmt.Function;
import com.shehan.atukorala.Stmt.If;
import com.shehan.atukorala.Stmt.Print;
import com.shehan.atukorala.Stmt.Return;
import com.shehan.atukorala.Stmt.Var;
import com.shehan.atukorala.Stmt.While;

public class Resolver implements Expr.Visitor<Void>, Stmt.Visitor<Void>{
	
	private final Interpreter interpreter;
	private final Stack<Map<String, Boolean>> scopes = new Stack<Map<String, Boolean>>();
	private FunctionType currentFunction = FunctionType.NONE;
	
	Resolver(Interpreter interpreter){
		this.interpreter = interpreter;
	}

	public Void visitBlockStmt(Stmt.Block stmt) {
		beginScope();
		resolve(stmt.statements);
		endScope();
		return null;
	}
	
	private enum FunctionType {
		NONE,
		FUNCTION,
		INITIALIZER,
		METHOD
	}
	
	private enum ClassType {
		NONE,
		CLASS
	}
	
	private ClassType currentClass = ClassType.NONE;

	
	void resolve(List<Stmt> statements) {
		for(Stmt statement : statements) {
			resolve(statement);
		}
	}
	
	private void resolve(Stmt stmt) {
		stmt.accept(this);
	}
	
	private void resolve(Expr expr) {
		expr.accept(this);
	}
	
	private void resolveFunction(Stmt.Function function, FunctionType type) {
		FunctionType enclosingFunction = currentFunction;
		currentFunction = type;
		
		beginScope();
		for(Token param : function.params) {
			declare(param);
			define(param);
		}
		resolve(function.body);
		endScope();
		currentFunction = enclosingFunction;
	}
	
	private void beginScope() {
		scopes.push(new HashMap<String, Boolean>());
	}
	
	private void endScope() {
		scopes.pop();
	}
	
	private void declare(Token name){
		if(scopes.isEmpty()) return;
		
		Map<String, Boolean> scope = scopes.peek();
		if(scope.containsKey(name.lexeme)) {
			Lox.error(name,
					"Already variable with this name in this scope.");	
		}
		
		scope.put(name.lexeme, false);
	}
	
	private void define(Token name) {
		if(scopes.isEmpty()) return;
		scopes.peek().put(name.lexeme, true);
	}
	
	private void resolveLocal(Expr expr, Token name) {
		for(int i = scopes.size() - 1; i >= 0; i--) {
			if(scopes.get(i).containsKey(name.lexeme)) {
				interpreter.resolve(expr, scopes.size() - 1 - i);
				return;
			}
		}
	}
	
	
	
	public Void visitLiteralExpr(Literal expr) {
		return null;
	}

	public Void visitGroupingExpr(Grouping expr) {
		resolve(expr.expression);
		return null;
	}

	public Void visitBinaryExpr(Expr.Binary expr) {
		resolve(expr.left);
		resolve(expr.right);
		return null;
	}

	public Void visitAssignExpr(Expr.Assign expr) {
		resolve(expr.value);
		resolveLocal(expr, expr.name);
		return null;
	}

	public Void visitCallExpr(Call expr) {
		resolve(expr.callee);
		
		for(Expr argument : expr.arguments) {
			resolve(argument);
		}
		
		return null;
	}

	public Void visitGetExpr(Get expr) {
		resolve(expr.object);
		return null;
	}

	public Void visitLogicalExpr(Expr.Logical expr) {
		resolve(expr.left);
		resolve(expr.right);
		return null;
	}

	public Void visitSetExpr(Set expr) {
		resolve(expr.value);
		resolve(expr.object);
		return null;
	}

	public Void visitThisExpr(Expr.This expr) {
		if(currentClass == ClassType.NONE) {
			Lox.error(expr.keyword, 
					"Can't use 'this' outside of a class");
			return null;
		}
		resolveLocal(expr, expr.keyword);
		return null;
	}
	
	public Void visitSuperExpr(Super expr) {
		// TODO Auto-generated method stub
		return null;
	}

	public Void visitUnaryExpr(Unary expr) {
		resolve(expr.right);
		return null;
	}
	
	 
	public Void visitClassStmt(Stmt.Class stmt) {
		ClassType enclosingClass = currentClass;
		currentClass = ClassType.CLASS;
		
		declare(stmt.name);
		define(stmt.name);
		
		if(stmt.superclass != null) {
			resolve(stmt.superclass);
		}
		
		beginScope();
		scopes.peek().put("this", true);
		
		for(Stmt.Function method : stmt.methods) {
			FunctionType declaration = FunctionType.METHOD;
			if(method.name.lexeme.equals("init")) {
				declaration = FunctionType.INITIALIZER;
			}
			
			resolveFunction(method, declaration);
		}
		
		endScope();
		
		currentClass = enclosingClass;
		return null;
	}

	public Void visitVarStmt(Stmt.Var stmt) {
		declare(stmt.name);
		if(stmt.initializer != null) {
			resolve(stmt.initializer);
		}
		define(stmt.name);
		return null;
	}
	
	public Void visitVariableExpr(Expr.Variable expr) {
		if(!scopes.isEmpty() &&
			scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
			Lox.error(expr.name, 
				"Can't read local variable in its own initializer");
		}
		
		resolveLocal(expr, expr.name);
		return null;
	}
	

	public Void visitExpressionStmt(Expression stmt) {
		resolve(stmt.expression);
		return null;
	}

	public Void visitFunctionStmt(Function stmt) {
		declare(stmt.name);
		define(stmt.name);
		
		resolveFunction(stmt, FunctionType.FUNCTION);
		return null;
	}

	public Void visitIfStmt(Stmt.If stmt) {
		resolve(stmt.condition);
		resolve(stmt.thenBranch);
		if(stmt.elseBranch != null) resolve(stmt.elseBranch);
		return null;
	}

	public Void visitPrintStmt(Print stmt) {
		resolve(stmt.expression);
		return null;
	}

	public Void visitReturnStmt(Return stmt) {
		if(currentFunction == FunctionType.NONE) {
			Lox.error(stmt.keyword, "Can't return from top-level code.");
		}
		
		if(stmt.value != null) {
			if(currentFunction == FunctionType.INITIALIZER) {
				Lox.error(stmt.keyword, 
						"Can't return a value from an initializer.");
			}
			
			resolve(stmt.value);
		}
		
		return null;
	}

	public Void visitWhileStmt(While stmt) {
		resolve(stmt.condition);
		resolve(stmt.body);
		return null;
	}

}
