public class Position {

    private final int lineNumber;
    private final int columnNumber;

    protected Position(int lineNumber, int columnNumber) {
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String toString() {
        return "line: " + lineNumber + ", column: " + columnNumber;
    }
}
