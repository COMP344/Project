import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TestParser {

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: TestScanner [path to file] ");
            System.exit(64);
        } else {
            runScanner(args[0]);
        }

    }

    private static void runScanner(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        String source = new String(bytes);

        ScannerFactory scannerFactory = new ScannerFactory();
        IScanner scanner = scannerFactory.getScanner(source);
        List<IToken> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Decl> declarations;

        declarations = parser.parse();

        AstPrinter printer = new AstPrinter();

        for (Decl declaration : declarations) {
            printer.printDecl(declaration);
        }

        System.out.println("PEACE! :)");

    }
}
