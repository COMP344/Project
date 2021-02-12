import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner implements IScanner {

    private final String source;
    private final List<IToken> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    Position position;

    private static final Map<String, Token.TokenType> KEYWORDS = new HashMap<>();
    private static final Map<String, Token.TokenType> SYMBOLS = new HashMap<>();

    static{
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

        SYMBOLS.put("!", Token.TokenType.OP);
        SYMBOLS.put("#", Token.TokenType.NEQ);
        SYMBOLS.put("&", Token.TokenType.AND);
        SYMBOLS.put("(", Token.TokenType.RPAREN);
        SYMBOLS.put(")", Token.TokenType.LPAREN);
        SYMBOLS.put("*", Token.TokenType.AST);
        SYMBOLS.put("+", Token.TokenType.PLUS);
        SYMBOLS.put(",", Token.TokenType.COMMA);
        SYMBOLS.put("-", Token.TokenType.MINUS);
        SYMBOLS.put(".", Token.TokenType.PERIOD);
        SYMBOLS.put("/", Token.TokenType.SLASH);
        SYMBOLS.put(":", Token.TokenType.COLON);
        SYMBOLS.put(":=", Token.TokenType.BECOMES);
        SYMBOLS.put(";", Token.TokenType.SEMICOLON);
        SYMBOLS.put("<", Token.TokenType.LSS);
        SYMBOLS.put("<=", Token.TokenType.LEQ);
        SYMBOLS.put("=", Token.TokenType.EQL);
        SYMBOLS.put(">", Token.TokenType.GTR);
        SYMBOLS.put(">=", Token.TokenType.GEQ);
        SYMBOLS.put("?", Token.TokenType.QUERY);
        SYMBOLS.put("~", Token.TokenType.NOT);
        SYMBOLS.put("$", Token.TokenType.HEX);
    }

    Scanner(String source) {
        this.source = source;
    }

    @Override
    public List<IToken> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            position = new Position(line, column);
            scanToken();
        }
        tokens.add(new Token(Token.TokenType.EOF, "", null, position));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        position = new Position(line, column - 1);

        if (isSymbol(c)) {
            scanSymbol();
        } else if (isNumber(c)) {
            scanNumber();
        } else if (isWhitespace(c)) {
            skipWhitespace();
        } else if (isAlpha(c)) {
            scanTextToken();
        } else {
            //Throw error
            System.out.println("ERROR");
        }
    }

    private boolean isSymbol(char c) {
        return SYMBOLS.containsKey("" + c);
    }

    private void scanSymbol() {
       char c = source.charAt(start);
        switch (c) {
            case '$' -> scanHexadecimal();
            case '<' -> addToken(match('=') ? Token.TokenType.LEQ : Token.TokenType.LSS);
            case '>' -> addToken(match('=') ? Token.TokenType.GEQ : Token.TokenType.GTR);
            case ':' -> addToken(match('=') ? Token.TokenType.BECOMES : Token.TokenType.COLON);
            default -> addToken(SYMBOLS.get("" + c));
        }
    }

    private boolean isHexadecimal(char c) {
        return c >= '0' && c <= 'F';
    }

    private void scanHexadecimal() {
        char firstHex = advance();
        char secondHex = advance();
        if (isHexadecimal(firstHex) && isHexadecimal(secondHex)) {
            int value = Character.digit(firstHex, 16) * 16 + Character.digit(secondHex, 16);
            addToken(Token.TokenType.NUMBER, value);
        }
    }

    private boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    private void scanNumber() {
        while (isNumber(peek())) {
            advance();
        }

        if (peek() == '.' && isNumber(peekNext())) {
            advance();
            while (isNumber(peek())) {
                advance();
            }
        }

        double d = Double.parseDouble(source.substring(start, current));
        addToken(Token.TokenType.NUMBER, d);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private void scanTextToken() {
        while (isAlpha(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        addToken(KEYWORDS.getOrDefault(text, Token.TokenType.IDENT));
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private void skipWhitespace() {
        while (isWhitespace(peek())) {
            advance();
        }
    }

    private char advance() {
        if (peek() == '\n') {
            line++;
            column = 1;
        } else {
            column++;
        }
        current++;
        return source.charAt(current - 1);
    }

    private void addToken(Token.TokenType type) {
        addToken(type, null);
    }

    private void addToken(Token.TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, position));
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        advance();
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    boolean isAtEnd() {
        return current >= source.length();
    }
}
