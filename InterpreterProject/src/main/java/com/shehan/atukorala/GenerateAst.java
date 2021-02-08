package com.shehan.atukorala;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
	public static void main(String[] args) throws IOException{
		if(args.length != 1) {
			System.err.println("Usage: generate_ast <output directory>");
			System.exit(64);
		}
		String outputDir = args[0];
		
		defineAst(outputDir, "Expr", Arrays.asList(
				"Assign		: Token name, Expr value",
				"Binary 	: Expr left, Token operator, Expr right",
				"Block      : List<Stmt> statements", // a block is a series of statements or declarations surrounded by curly braces. A block is itsefl a statment and can appear anywhere a statement is allowed
				"Call       : Expr callee, Token paren, List<Expr> arguments",
				"Get        : Expr object, Token name",
				"Grouping   : Expr expression",
				"Literal    : Object value",
				"Logical    : Expr left, Token operator, Expr right",
				"Set        : Expr object, Token name, Expr value",
				"Super      : Token keyword, Token method",
				"This       : Token keyword",
				"Unary		: Token operator, Expr right",
				"Variable   : Token name"
				));
	}
	
	private static void defineAst(
			String outputDir, String baseName, List<String> types)
			throws IOException {
		String path = outputDir + "/" + baseName + ".java";
		PrintWriter writer = new PrintWriter(path, "UTF-8");
		
		writer.println("package com.craftinginterpreters.lox;");
		writer.println();
		writer.println("import java.util.List;");
		writer.println();
		writer.println("abstract class " + baseName + " {");
		
		for(String type : types) {
			String className = type.split(":")[0].trim();
			String fields = type.split(":")[1].trim();
			defineType(writer, baseName, className, fields);
		}
		
		writer.println("}");
		writer.close();
		
	}
	
	private static void defineType(
			PrintWriter writer, String baseName,
			String className, String fieldList) {
		writer.println("  static class " + className + " extends " + baseName + " {");
		
		writer.println("     " + className + "(" + fieldList + ") {");
		
		String[] fields = fieldList.split(", ");
		for(String field : fields) {
			String name = field.split(" ")[1];
			writer.println("      this." + name + " = " + name + ";");
		}
		
		writer.println("     }");
		
		writer.println();
		for(String field : fields) {
			writer.println("    final " + field + ";");
		}
		
		writer.println("  }");
	}
}
