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
    private int begin_column = 1;

    TokenFactory tokenFactory;

    private static final Map<String, String> SYMBOLS = new HashMap<>();

    static{
        SYMBOLS.put("!", "OP");
        SYMBOLS.put("#", "NEQ");
        SYMBOLS.put("&", "AND");
        SYMBOLS.put("(", "LPAREN");
        SYMBOLS.put(")", "RPAREN");
        SYMBOLS.put("*", "AST");
        SYMBOLS.put("+", "PLUS");
        SYMBOLS.put(",", "COMMA");
        SYMBOLS.put("-", "MINUS");
        SYMBOLS.put(".", "PERIOD");
        SYMBOLS.put("/", "SLASH");
        SYMBOLS.put(":", "COLON");
        SYMBOLS.put(":=", "BECOMES");
        SYMBOLS.put(";", "SEMICOLON");
        SYMBOLS.put("<", "LSS");
        SYMBOLS.put("<=", "LEQ");
        SYMBOLS.put("=", "EQL");
        SYMBOLS.put(">", "GTR");
        SYMBOLS.put(">=", "GEQ");
        SYMBOLS.put("?", "QUERY");
        SYMBOLS.put("~", "NOT");
        SYMBOLS.put("$", "HEX");
    }

    Scanner(String source) {
        this.source = source;
        tokenFactory = new TokenFactory();
    }

    @Override
    public List<IToken> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(tokenFactory.makeEofToken(line, column));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        begin_column = column - 1;

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
            case '<' -> addToken(match('=') ? "LEQ" : "LSS");
            case '>' -> addToken(match('=') ? "GEQ" : "GTR");
            case ':' -> addToken(match('=') ? "BECOMES" : "COLON");
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
            double value = Character.digit(firstHex, 16) * 16 + Character.digit(secondHex, 16);
            addToken("NUMBER", value);
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
        addToken("NUMBER", d);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private void scanTextToken() {
        while (isAlpha(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        addToken(text);
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

    private void addToken(String type) {
        addToken(type, null);
    }

    private void addToken(String type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(tokenFactory.makeToken(type, text, literal, line, begin_column));
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
