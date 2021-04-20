import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    Map<String, Symbol> table = new HashMap<>();
    SymbolTable enclosing;

    public SymbolTable(SymbolTable enclosing) {
        this.enclosing = enclosing;
    }

    void assignVariable(String name, Object value) {
        if (table.containsKey(name)) {
            ((VariableSymbol) table.get(name)).setValue(value);
        }
        if (enclosing != null) {
            enclosing.assignVariable(name, value);
        }
    }

    void defineVariable(String name, String type, int registerIndex, Object value) {
        if (!table.containsKey(name)) {
            table.put(name, SymbolFactory.makeVariableSymbol(name, type, registerIndex, value));
        } else {
            //error
        }
    }

    Object getVariable(String name) {
        if (table.containsKey(name) && table.get(name) instanceof VariableSymbol) {
            return ((VariableSymbol) table.get(name)).getValue();
        } else {
            //error
            return null;
        }
    }

    int getVariableRegisterIndex(String name) {
        if (table.containsKey(name) && table.get(name) instanceof VariableSymbol) {
            return ((VariableSymbol) table.get(name)).getRegisterIndex();
        }
        return -1;
    }

    SymbolTable ancestor(int distance) {
        SymbolTable table = this;
        for (int i = 0; i < distance; i++) {
            table = table.enclosing;
        }
        return table;
    }

    Object getVariableAt(int distance, String name) {
        return ((VariableSymbol) ancestor(distance).table.get(name)).getValue();
    }

    void assignVariableAt(int distance, String name, String type, int registerIndex, Object value) {
        ancestor(distance).table.put(name, SymbolFactory.makeVariableSymbol(name, type, registerIndex, value));
    }
}
