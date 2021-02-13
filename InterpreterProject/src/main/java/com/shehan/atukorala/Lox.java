package com.shehan.atukorala;

import java.util.List;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.shehan.atukorala.Scanner;

public class Lox {
	/**
	 * Lox type		Java representation
	 * --------------------------------
	 * Any Lox value   |   Object 
	 * nil             |   null 
	 * Boolean         |   Boolean 
	 * number          |   Double 
	 * string          |   String 
	 * 
	 * 
	 * Lox is an imperaive language, and mutation comes with the territory.
	 * 
	 * Lexical scope(or the less commonly head static scope) is a specific style of scoping where the text of the program itself shows where a 
	 * scope begins and ends. In Lox, as in most modern languages, variables are lexically scoped. When you see an expression that 
	 * uses some variable, you can figure out which variable declaration it refers to just by statically reading the code. 
	 */
	private static final Interpreter interpreter = new Interpreter();
	static boolean hadError = false;
	static boolean hadRuntimeError = false;
	
	public static void main(String[] args) throws IOException{
		if(args.length > 1) {
			System.out.println("Usage: jlox [script]");
			System.exit(64);
		}else if(args.length == 1) {
			runFile(args[0]);
		}else {
			runPrompt();
		}
	}
	
	public static void runFile(String path) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(path));
		run(new String(bytes, Charset.defaultCharset()));
		
//		if (hadError) System.exit(70);
		if(hadRuntimeError) System.exit(70);
	}
	
	private static void runPrompt() {
		
	}
	
	private static void run(String source) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(source);
		List<Token> tokens = scanner.scanTokens();
		Parser parser = new Parser(tokens);
		List<Stmt> statements = parser.parse();
		
//		if(hadError) return;
		Resolver resolver = new Resolver(interpreter);
		resolver.resolve(statements);
		
		// Stop if there was a resolution error. 
		if(hadError) return;
		
		interpreter.interpret(statements);
	}
	
	static void error(int line, String message) {
		report(line, "", message);
	}
	
	private static void report(int line, String where, String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
		if(hadError) System.exit(65);
	}
	
	static void runtimeError(RuntimeError error) {
		System.err.println(error.getMessage() + 
				"\n[line " + error.token.line + "]");
		hadRuntimeError = true;
	}
	
	
	
	static void error(Token token, String message) {
		if(token.type == TokenType.EOF) {
			report(token.line, " at end ", message);
		}else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
	
	
}
