public interface IToken {

    Enum<?> getTokenType();

    String getLexeme();

    Object getLiteral();

    int getLineNumber();

    int getColNumber();

    String toString();
}