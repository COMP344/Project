import java.util.List;

public class Parser {
    private final List<IToken> tokens;
    private int current;

    Parser(List<IToken> tokens) {
        this.tokens = tokens;
    }
}
