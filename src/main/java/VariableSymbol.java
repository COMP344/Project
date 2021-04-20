public class VariableSymbol extends Symbol {

    int registerIndex;
    Object value;

    public VariableSymbol(String name, SymbolType type, int registerIndex, Object value) {
        super(name, type);
        this.registerIndex = registerIndex;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getRegisterIndex() {
        return registerIndex;
    }
}
