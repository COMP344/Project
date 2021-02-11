package Example;
// TestIncrementalScanner.java

import java.io.*;

// The best way to build a scanner incrementally is by using a strategy and
// template method patterns.

// Strategy Pattern
interface IScanner {
    int getToken();
    void close();
}

// Template method pattern
abstract class Scanner implements IScanner {
    private   FileInputStream srcStream;
    private   File srcFile;
    protected int  ch;

    public Scanner(File srcFile) {
        open(srcFile);
        read();   // Prime the scanner.
    }

    private void open(File srcFile) {
        try {
            srcStream = new FileInputStream(srcFile);
        } catch (IOException e) { }
    }
    public void close() {
        try {
            srcStream.close();
        } catch (IOException e) { }
    }

    // Tokens:
    protected static final int Eof = -1;
    protected static final int InvalidToken = 0;
    protected static final int BinaryNumber = 1;
    protected static final int Operator     = 2;

    protected void read() {
        try {
            ch = srcStream.read();
        } catch (IOException e) { }
    }

    public int getToken() {
        switch (ch) {
            case Eof:
                return ch;
            case '0':
            case '1':
                return scanBinaryNumber();
            case '+':
            case '-':
            case '*':
            case '/':
                return scanOperator();
            default:
                read();
                break;
        }
        return InvalidToken;
    }
    abstract protected int scanBinaryNumber();
    abstract protected int scanOperator();
}

// Basic implementation of the template method.
class BasicScanner extends Scanner {
    public BasicScanner(File srcFile) {
        super(srcFile);
    }
    protected int scanBinaryNumber() {
        read();
        while (ch == '0' || ch == '1') {
            read();
        }
        return BinaryNumber;
    }
    protected int scanOperator() {
        read();
        return Operator;
    }
}

public class TestIncrementalScanner {
    private static int Eof = -1;

    public static void main(String args[]) throws IOException {

        // Check the number of arguments.
        if ( args.length != 1 ) {
            System.out.println("Usage: Scanner <src>" );
            return;
        }

        // Check if the srcFile exists.
        String srcFilename = args[0];
        File   srcFile = new File(srcFilename);
        if (!srcFile.canRead()) {
            System.out.println("TestFileInputStream: Cannot open srcFile '" + srcFilename + "'");
            return;
        }

        IScanner scanner = new BasicScanner(srcFile);

        // Scan the srcFile character per character.
        int token;

        while ( (token = scanner.getToken()) != Eof ) {
            System.out.print( "[" + token + "]" );
        }

        scanner.close();
        System.out.println("\ndone.");
    }
}

// Output:
// [0][1][2][1][2][1][2][1][2][1][0]
// done.
