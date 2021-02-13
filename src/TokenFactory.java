public class TokenFactory {

    public Token makeToken(Token.TokenType type, String lexeme, Object literal, int line, int column) {
        return new Token(type, lexeme, literal, new Position(line, column));
    }

    public Token makeEofToken(int line, int column) {
        return new Token(Token.TokenType.EOF, "", null, new Position(line, column));
    }

}
