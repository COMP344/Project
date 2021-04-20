import java.util.List;

public class SymbolFactory {

    public static Symbol makeVariableSymbol(String name, String type, int registerIndex, Object value) {
        return switch (type) {
            case "INT" -> new VariableSymbol(name, Symbol.SymbolType.INT, registerIndex, value);
            case "SET" -> new VariableSymbol(name, Symbol.SymbolType.SET, registerIndex, value);
            case "BOOL" -> new VariableSymbol(name, Symbol.SymbolType.BOOL, registerIndex, value);
            case "CONST" -> new VariableSymbol(name, Symbol.SymbolType.CONST, 0, value);
            default -> null;
        };
    }

    public static Symbol makeProcedureSymbol(String name, List<Symbol> parameters, int lineStart) {
        return new ProcedureSymbol(name, Symbol.SymbolType.PROCEDURE, parameters, lineStart);
    }
}
