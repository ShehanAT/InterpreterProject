package com.shehan.atukorala;

import java.util.HashMap;
import java.util.Map;

public class Environment {
	final Environment enclosing;
	private final Map<String, Object> values = new HashMap<String, Object>();
	
	Environment(){//no args constructor is for the global scope's environment 
		enclosing = null;
	}
	
	Environment(Environment enclosing){
		this.enclosing = enclosing;
	}
	
	Object get(Token name) {
		if(values.containsKey(name.lexeme)) {
			return values.get(name.lexeme);
		}
		
		//if the variable isn't found in this environment, then we simply try the enclosing one.
		//That in turn does the same thing recursively, so this will ultimately walk the entire chain.
		//If we read an environment with no enclosing one and still don't find the variable, the we give up and report an error as before.
		if(enclosing != null) {
			return enclosing.get(name);
		}
		
		throw new RuntimeError(name, 
				"Undefined variable '" + name.lexeme + "'.");
	}
	
	void assign(Token name, Object value) {
		if(values.containsKey(name.lexeme)) {
			values.put(name.lexeme, value);
			return;
		}
		
		if(enclosing != null) {
			enclosing.assign(name, value);
			return;
		}
		
		throw new RuntimeError(name,
				"Undefined variable '" + name.lexeme + "'.");
	}
	
	void define(String name, Object value) {
		values.put(name, value);
	}
	
	Object getAt(int distance, String name) {
		return ancestor(distance).values.get(name);
	}
	
	void assignAt(int distance, Token name, Object value) {
		ancestor(distance).values.put(name.lexeme, value);
	}
	
	Environment ancestor(int distance) {
		Environment environment = this;
		for(int i = 0; i < distance; i++) {
			environment = environment.enclosing;
		}
		
		return environment;
	}
	
}
