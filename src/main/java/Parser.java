import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import Token.TokenType;

public class Parser {
	//Not recognized
	private static class ParseError extends RunTimeException {}
    private final List<IToken> tokens;
    private int current = 0;

    Parser(List<IToken> tokens) {
        this.tokens = tokens;
    }
    
    
    private Expr expression() {
    	return equality();
    }
    
    private Expr equality() {
    	Expr expr = comparison();
    	while (match(BANG_EQUAL, EQUAL_EQUAL)) {
    		//Not sure about this cast
    		Token operator = (Token) previous();
    		Expr right = comparison();
    		expr = new Expr.Binary(operator, expr, right);
    		
    	}
		return expr;
    	
    }
    
    private Expr comparison() {
    	Expr expr = term();
    	while (match(GREATER,GREATER_EQUAL,LESS,LESS_EQUAL)) {
    		//Not sure about this cast
    		Token operator = (Token) previous();
    		Expr right = term();
    		expr = new Expr.Binary(operator, expr, right);	
    	}
		return expr;   	
    }
    
    private Expr term() {
    	Expr expr = factor();
    	
    	while (match(MINUS, PLUS)) {
    		//Not sure about this cast
    		Token operator = (Token) previous();
    		Expr right = factor();
    		expr = new Expr.Binary(operator, expr, right);
    		
    	}
    	return expr;
    }
    
    private Expr factor() {
    	Expr expr = unary();
    	
    	while (match(SLASH, STAR)) {
    		//Not sure about this cast
    		Token operator = (Token) previous();
    		Expr right = unary();
    		expr = new Expr.Binary(operator, expr, right);
    		
    	}
    	return expr;
    }
    
    private Expr unary() {

    	while (match(BANG, MINUS)) {
    		//Not sure about this cast
    		Token operator = (Token) previous();
    		Expr right = unary();
    		return new Expr.Unary(operator, right);
    		
    	}
    	return primary();
    }
    //No grouping method or consume method
    private Expr primary() {
    	if (match(FALSE)) return new Expr.Literal(false);
    	if (match(TRUE)) return new Expr.Literal(true);
    	if (match(NULL)) return new Expr.Literal(null);
    	
    	if (match(NUMBER, STRING)) {
    		return new Expr.Literal(previous().getLiteral());
    	}
    	
    	if (match(LEFT_PAREN)) {
    		Expr expr = expression();
    		consume(RIGHT_PAREN, "Expect ')' after expression");
    		//No grouping method
    		return new Expr.Grouping(expr);
    	}
    }
    
    
    private boolean match(IToken... types) {
    	for(IToken type : types) {
    		if (check(type)) {
    			advance();
    			return true;
    		}
    	}
    	
		return false;  	
    }
    
    private boolean check (IToken token) {
    	if (isAtEnd()) return false;
    	return peek().getTokenType() == token.getTokenType();
    }
    
    private IToken advance() {
    	if (!isAtEnd()) current++;
    	return previous();
    }
    
    private boolean isAtEnd() {
    	return peek().getTokenType() == Token.TokenType.EOF;
    }
    
    private IToken peek() {
    	return tokens.get(current);
    }
    
    private IToken previous() {
    	return tokens.get(current - 1);
    }
    //
    private IToken consume(IToken type, String message) {
    	if (check(type)) return advance();
    	
    	throw error(peek(),message);
    }
    // What is Lox.error
    private ParseError error(Token token, String message) {
    	Lox.error(token,message);
    	return new ParserError();
    }
    
    static void error(Token token, String message) {
    	if(token.getTokenType() == Token.TokenType.EOF) {
    		report(token.getPosition().getLineNumber(), " at end", message);
    	} else {
    		report(token.getPosition().getLineNumber(), " at '" + token.getLexeme() +"'", message);
    	}
    }
    
    private void synchronize() {
    	advance();
    	while(!isAtEnd()) {
    		if (previous().getTokenType() == Token.TokenType.SEMICOLON) return;
    	
//    	switch (peek().getTokenType()) {
//        case Token.TokenType.CLASS:
//        case Token.TokenType.FUN:
//        case Token.TokenType.VAR:
//        case Token.TokenType.FOR:
//        case Token.TokenType.IF:
//        case Token.TokenType.WHILE:
//        case Token.TokenType.PRINT:
//        case Token.TokenType.RETURN:
//         return;
//    	}
    	advance();
    	}
    }
    	
    
    
    
    
    
    
    
    
    
    
    
    
    
    //Test Purposes
    public static void main(String[] args) throws IOException {
    	String contents = "MODULE Procedures;\r\n"
    			+ "  INT x, y;\r\n"
    			+ "  \r\n"
    			+ "  PROCEDURE NofBits(INT x): INT;\r\n"
    			+ "    INT cnt, n;\r\n"
    			+ "  BEGIN cnt := 0; n := 8;\r\n"
    			+ "    REPEAT\r\n"
    			+ "      IF x.0 THEN INC cnt END ];\r\n"
    			+ "      ROR x; DEC n\r\n"
    			+ "    UNTIL n = 0;\r\n"
    			+ "    RETURN cnt\r\n"
    			+ "  END NofBits;\r\n"
    			+ "\r\n"
    			+ "  PROCEDURE Swap;\r\n"
    			+ "    INT z;\r\n"
    			+ "  BEGIN z := x; x := y; y := z\r\n"
    			+ "  END Swap;\r\n"
    			+ "\r\n"
    			+ "  PROCEDURE P(INT a);\r\n"
    			+ "  BEGIN\r\n"
    			+ "    x := a + 10\r\n"
    			+ "  END P;\r\n"
    			+ "\r\n"
    			+ "BEGIN Swap; P(y); x := NofBits(y)\r\n"
    			+ "END Procedures.\r\n"
    			+ ""
    			;

    	Scanner lexer = new Scanner(contents);
    	Parser parser = new Parser(lexer.scanTokens());
    	for(IToken token : parser.tokens) {
    		System.out.println(token.toString());
    	}
    }
    
}
