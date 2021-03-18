import java.util.HashMap;
import java.util.Map;

public class TokenFactory {

    private static final Map<String, Token.TokenType> KEYWORDS = new HashMap<>();

    static {
        KEYWORDS.put("BEGIN", Token.TokenType.BEGIN);
        KEYWORDS.put("BOOL", Token.TokenType.BOOL);
        KEYWORDS.put("CONST", Token.TokenType.CONST);
        KEYWORDS.put("DEC", Token.TokenType.DEC);
        KEYWORDS.put("DO", Token.TokenType.DO);
        KEYWORDS.put("ELSE", Token.TokenType.ELSE);
        KEYWORDS.put("ELSIF", Token.TokenType.ELSIF);
        KEYWORDS.put("END", Token.TokenType.END);
        KEYWORDS.put("IF", Token.TokenType.IF);
        KEYWORDS.put("INC", Token.TokenType.INC);
        KEYWORDS.put("INT", Token.TokenType.INT);
        KEYWORDS.put("MODULE", Token.TokenType.MODULE);
        KEYWORDS.put("OR", Token.TokenType.OR);
        KEYWORDS.put("PROCEDURE", Token.TokenType.PROCED);
        KEYWORDS.put("REPEAT", Token.TokenType.REPEAT);
        KEYWORDS.put("RETURN", Token.TokenType.RETURN);
        KEYWORDS.put("ROL", Token.TokenType.ROL);
        KEYWORDS.put("ROR", Token.TokenType.ROR);
        KEYWORDS.put("SET", Token.TokenType.SET);
        KEYWORDS.put("THEN", Token.TokenType.THEN);
        KEYWORDS.put("UNTIL", Token.TokenType.UNTIL);
        KEYWORDS.put("WHILE", Token.TokenType.WHILE);
        KEYWORDS.put("OP", Token.TokenType.OP);
        KEYWORDS.put("NEQ", Token.TokenType.NEQ);
        KEYWORDS.put("AND", Token.TokenType.AND);
        KEYWORDS.put("RPAREN", Token.TokenType.RPAREN);
        KEYWORDS.put("LPAREN", Token.TokenType.LPAREN);
        KEYWORDS.put("AST", Token.TokenType.AST);
        KEYWORDS.put("PLUS", Token.TokenType.PLUS);
        KEYWORDS.put("COMMA", Token.TokenType.COMMA);
        KEYWORDS.put("MINUS", Token.TokenType.MINUS);
        KEYWORDS.put("PERIOD", Token.TokenType.PERIOD);
        KEYWORDS.put("SLASH", Token.TokenType.SLASH);
        KEYWORDS.put("COLON", Token.TokenType.COLON);
        KEYWORDS.put("BECOMES", Token.TokenType.BECOMES);
        KEYWORDS.put("SEMICOLON", Token.TokenType.SEMICOLON);
        KEYWORDS.put("LSS", Token.TokenType.LSS);
        KEYWORDS.put("LEQ", Token.TokenType.LEQ);
        KEYWORDS.put("EQL", Token.TokenType.EQL);
        KEYWORDS.put("GTR", Token.TokenType.GTR);
        KEYWORDS.put("GEQ", Token.TokenType.GEQ);
        KEYWORDS.put("QUERY", Token.TokenType.QUERY);
        KEYWORDS.put("NOT", Token.TokenType.NOT);
        KEYWORDS.put("HEX", Token.TokenType.HEX);
        KEYWORDS.put("NUMBER", Token.TokenType.NUMBER);
    }

    public Token makeEofToken(int line, int column) {
        return new Token(Token.TokenType.EOF, "", null, line, column);
    }

    public Token makeToken(String type, String lexeme, Object literal, int line, int column) {
        return new Token(KEYWORDS.getOrDefault(type, Token.TokenType.IDENT), lexeme, literal, line, column);
    }
}

