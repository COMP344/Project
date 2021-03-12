import java.util.HashMap;
import java.util.Map;

public class VariableTable {
    final Map<String, Object> values = new HashMap<>();
    static VariableTable vt;

    private VariableTable() {}

    static VariableTable getInstance() {
        if (vt == null)
            vt = new VariableTable();

        return vt;
    }

    Object get(Token token) {
        if (values.containsKey(token.lexeme)) {
            return values.get(token.lexeme);
        } else {
            return null;
        }
        //return error
    }

    void assign(Token token, Object value) {
        if (values.containsKey(token.lexeme)) {
            values.put(token.lexeme, value);
        } else {
            return;
        }
    }

    void define(String name, Object value) {
        values.put(name, value);
    }
}
