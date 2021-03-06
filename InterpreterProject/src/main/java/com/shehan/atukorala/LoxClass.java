package com.shehan.atukorala;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable{
	final String name;
	LoxClass superclass;
	private final Map<String, LoxFunction> methods;
	
	LoxClass(String name, LoxClass superclass, Map<String, LoxFunction> methods){
		this.name = name;
		this.superclass = superclass;
		this.methods = methods;
	}
	
	LoxFunction findMethod(String name) {
		if(methods.containsKey(name)) {
			return methods.get(name);
		}
		
		return null;
	}

	public int arity() {
		LoxFunction initializer = findMethod("init");
		if(initializer == null) return 0;
		return initializer.arity();
	}

	public Object call(Interpreter interpreter, List<Object> arguments) {
		LoxInstance instance = new LoxInstance(this);
		LoxFunction initializer = findMethod("init");
		if(initializer != null) {
			initializer.bind(instance).call(interpreter, arguments);
		}
		
		return instance;
	}
	
	@Override 
	public String toString() {
		return name;
	}
}
