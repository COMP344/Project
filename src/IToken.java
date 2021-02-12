public interface IToken {

    Enum<?> getTokenType();

    String getLexeme();

    Object getLiteral();

    Position getPosition();

    String toString();
}