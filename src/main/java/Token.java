public class Token implements IToken{

    enum TokenType{

        AST, SLASH, PLUS, MINUS, NOT, AND, OR, EQL, NEQ, GEQ, LSS, LEQ,
        GTR, PERIOD, COMMA, COLON, OP, QUERY, LPAREN, BECOMES, IDENT, IF,
        WHILE, REPEAT, INC, DEC, ROL, ROR, NUMBER, RPAREN, THEN, DO,
        SEMICOLON, END, ELSE, ELSIF, UNTIL, RETURN, INT, SET, BOOL,
        CONST, BEGIN, PROCED, MODULE, EOF, HEX

    }

    TokenType type;

    String lexeme;

    Object literal;

    Position position;

    public Token(TokenType type, String lexeme, Object literal, Position position) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.position = position;
    }

    @Override
    public TokenType getTokenType() {
        return this.type;
    }

    @Override
    public String getLexeme() {
        return this.lexeme;
    }

    @Override
    public Object getLiteral() {
        return this.literal;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    public String toString() {
        return type + " " + lexeme + " " + (literal != null ? literal + " " : "") + position;
    }
}
