import java.util.List;

public class TestParser {
    private final List<IToken> tokens;
    private int current;

    TestParser(List<IToken> tokens) {
        this.tokens = tokens;
    }
}
