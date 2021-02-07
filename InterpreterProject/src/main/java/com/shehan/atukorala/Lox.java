package com.shehan.atukorala;

import java.awt.List;
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
	 */
	static boolean hadError = false;
	
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
		
	}
	
	private static void runPrompt() {
		
	}
	
	private static void run(String source) {
		// TODO Auto-generated method stub
		Scanner scanner = new Scanner(source);
//		List<Token> tokens = scanner.scanTokens();
		Parser parser = new Parser(tokens);
		Expr expression = parser.parse();
		
		if(hadError) return;
		
		System.out.println(new AstPrinter().print(expression));
	}
	
	static void error(int line, String message) {
		report(line, "", message);
	}
	
	private static void report(int line, String where, String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
		hadError = true;
		if(hadError) System.exit(65);
	}
	
	
	
	static void error(Token token, String message) {
		if(token.type == TokenType.EOF) {
			report(token.line, " at end ", message);
		}else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
	
	
}
