import java.util.List;

public class ProcedureSymbol extends Symbol {

    List<Symbol> parameters;
    int lineStart;

    public ProcedureSymbol(String name, SymbolType type, List<Symbol> parameters, int lineStart) {
        super(name, type);
        this.parameters = parameters;
        this.lineStart = lineStart;
    }
}
