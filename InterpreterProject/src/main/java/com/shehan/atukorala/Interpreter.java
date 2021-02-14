package com.shehan.atukorala;

import static com.shehan.atukorala.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shehan.atukorala.Expr.Call;
import com.shehan.atukorala.Expr.Get;
import com.shehan.atukorala.Expr.Logical;
import com.shehan.atukorala.Expr.Set;
import com.shehan.atukorala.Expr.Super;
import com.shehan.atukorala.Stmt.Class;
import com.shehan.atukorala.Stmt.Function;
import com.shehan.atukorala.Return;
import com.shehan.atukorala.Stmt.While;


public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void>{
	final Environment globals = new Environment();
	private Environment environment = globals;
	private final Map<Expr, Integer> locals = new HashMap<Expr, Integer>();
	
	Interpreter(){
		globals.define("clock", new LoxCallable() {
			 
			public int arity() { return 0; }
			
			// since these methods are implements and not extended from interface the @Override annotation is not required 
			public Object call(Interpreter interpreter, List<Object> arguments) {
				return (double)System.currentTimeMillis() / 1000.00;
			}
			
			@Override 
			public String toString() { return "<native fn>"; } 
		});
	}
	
	public Object visitLiteralExpr(Expr.Literal expr) {
		return expr.value;
	}
	
	public Object visitGroupingExpr(Expr.Grouping expr) {
		return evaluate(expr.expression);
	}
	
	private Object evaluate(Expr expr) {
		return expr.accept(this);
	}
	
	public Void visitExpressionStmt(Stmt.Expression stmt) {
		evaluate(stmt.expression);
		return null;
	}
	
	public Void visitIfStmt(Stmt.If stmt) {
		if(isTruthy(evaluate(stmt.condition))) {
			execute(stmt.thenBranch);
		}else if(stmt.elseBranch != null) {
			execute(stmt.elseBranch);
		}
		
		return null;
	}
	
	public Void visitPrintStmt(Stmt.Print stmt) {
		Object value = evaluate(stmt.expression);
		System.out.println(stringify(value));
		return null;
	}
	
	public Void visitReturnStmt(Stmt.Return stmt) {
		Object value = null;
		if(stmt.value != null) value = evaluate(stmt.value);
		
		throw new Return(value);
	}
	
	public Void visitVarStmt(Stmt.Var stmt) {
		Object value = null;
		if(stmt.initializer != null) {
			value = evaluate(stmt.initializer);
		}
		
		environment.define(stmt.name.lexeme, value);
		return null;
	}
	
	public Void visitBlockStmt(Stmt.Block stmt) {
		executeBlock(stmt.statements, new Environment(environment));
		return null;
	}
	
	public Object visitAssignExpr(Expr.Assign expr) {
		Object value = evaluate(expr.value);

		Integer distance = locals.get(expr);
		if(distance != null) {
			environment.assignAt(distance, expr.name, value);
		}else {
			globals.assign(expr.name, value);
		}
		return value;
	}
	
	void interpret(List<Stmt> statements) {
		try {
			for(Stmt statement : statements) {
				execute(statement);
			}
		}catch(RuntimeError error) {
			Lox.runtimeError(error);
		}
	}
	
	private void execute(Stmt stmt) {
		stmt.accept(this);
	}
	
	void executeBlock(List<Stmt> statements,
						Environment environment) {
		Environment previous = this.environment;
		try {
			this.environment = environment;
			
			for(Stmt statement : statements) {
				execute(statement);
			}
		}finally {
			this.environment = previous;
		}
		
	}
	
	void resolve(Expr expr, int depth) {
		locals.put(expr, depth);
	}
	
	public Object visitUnaryExpr(Expr.Unary expr) {
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case BANG:
			return !isTruthy(right);
		case MINUS:
			return -(Double)right;
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
	
	 
	public Object visitVariableExpr(Expr.Variable expr) {
		return lookUpVariable(expr.name, expr);
	}
	
	private Object lookUpVariable(Token name, Expr expr) {
		Integer distance = locals.get(expr);
		if(distance != null) {
			return environment.getAt(distance ,name.lexeme);
		}else {
			return globals.get(name);
		}
	}
	
	private boolean isTruthy(Object object) {
		if(object == null) return false;
		if(object instanceof Boolean) return (Boolean)object;
		return true;
	}
	
	private void checkNumberOperand(Token operator, Object operand) {
		if(operand instanceof Double) return;
		throw new RuntimeError(operator, "Operand must be a number");
	}
	
	private void checkNumberOperands(Token operator, Object left, Object right) {
		if(left instanceof Double && right instanceof Double) return;
		throw new RuntimeError(operator, "Operands must be numbers");
	}
	
	void interpret(Expr expression) {
		try {
			Object value = evaluate(expression);
			System.out.println(stringify(value));
		}catch(RuntimeError error) {
			Lox.runtimeError(error);
		}
	}
	
	
	public Object visitBinaryExpr(Expr.Binary expr) {
		Object left = evaluate(expr.left);
		Object right = evaluate(expr.right);
		
		switch(expr.operator.type) {
		case MINUS: 
			checkNumberOperands(expr.operator, left, right);
			return (Double)left - (Double)right;
		case PLUS:
			if(left instanceof Double && right instanceof Double) {
				return (Double)left + (Double)right;
			}
			
			if(left instanceof String && right instanceof String) {
				return (String)left + (String)right;
			}
			throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
		case GREATER:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left > (Double)right;
		case GREATER_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left >= (Double)right;
		case LESS:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left < (Double)right;
		case LESS_EQUAL:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left <= (Double)right;
		case SLASH:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left / (Double)right;
		case STAR:
			checkNumberOperands(expr.operator, left, right);
			return (Double)left * (Double)right;
		
		}
		
		// Unreachable
		return null;
	}
	
	private boolean isEqual(Object object1, Object object2) {
		if(object1 == object2) {
			return true;
		}
		return false;
	}
	
	private String stringify(Object object) {
		if(object == null) return "nil";
		
		if(object instanceof Double) {
			String text = object.toString();
			if(text.endsWith(".0")) {
				text = text.substring(0, text.length() - 2);
			}
			return text;
		}
		
		return object.toString();
	}

	public Void visitClassStmt(Class stmt) {
		Object superclass = null;
		if(stmt.superclass != null) {
			superclass = evaluate(stmt.superclass);
			if(!(superclass instanceof LoxClass)) {
				throw new RuntimeError(stmt.superclass.name, 
						"Superclass must be a class.");
			}
		}
		environment.define(stmt.name.lexeme, null);
		
		if(stmt.superclass != null) {
			environment = new Environment(environment);
			environment.define("super", superclass);
		}
		
		Map<String, LoxFunction> methods = new HashMap<String, LoxFunction>();
		for(Stmt.Function method : stmt.methods) {
			LoxFunction function = new LoxFunction(method, environment,
					method.name.lexeme.equals("init"));
			methods.put(method.name.lexeme, function);
		}
		
		LoxClass klass = new LoxClass(stmt.name.lexeme, (LoxClass)superclass, methods);
		environment.assign(stmt.name, klass);
		return null;
	}

	public Void visitFunctionStmt(Stmt.Function stmt) {
		LoxFunction function = new LoxFunction(stmt, environment, false);
		environment.define(stmt.name.lexeme, function);
		return null;
	}

	public Void visitWhileStmt(While stmt) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visitCallExpr(Call expr) {
		Object callee = evaluate(expr.callee);
		
		List<Object> arguments = new ArrayList<Object>();
		for(Expr argument : expr.arguments) {
			arguments.add(evaluate(argument));
		}
		
		if(!(callee instanceof LoxCallable)) {
			throw new RuntimeError(expr.paren, 
					"Can only call functions and classes.");
		}
		
		LoxCallable function = (LoxCallable)callee;
		if(arguments.size() != function.arity()) {
			throw new RuntimeError(expr.paren, "Expected " + 
				function.arity() + " arguments but got " + 
				arguments.size() + ".");
		}
		
		return function.call(this, arguments);
	}

	public Object visitGetExpr(Get expr) {
		Object object = evaluate(expr.object);
		if(object instanceof LoxInstance) {
			return ((LoxInstance) object).get(expr.name);
		}
		
		throw new RuntimeError(expr.name, 
				"Only instances have properties");
	}

	public Object visitLogicalExpr(Logical expr) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object visitSetExpr(Set expr) {
		Object object = evaluate(expr.object);
		
		if(!(object instanceof LoxInstance)) {
			throw new RuntimeError(expr.name, 
									"Only instances have fields.");
		}
		
		Object value = evaluate(expr.value);
		((LoxInstance)object).set(expr.name, value);
		return value;
	}
	
	public Object visitThisExpr(Expr.This expr) {
		return lookUpVariable(expr.keyword, expr);
	}

	public Object visitSuperExpr(Super expr) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
