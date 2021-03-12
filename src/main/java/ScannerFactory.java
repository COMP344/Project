public class ScannerFactory {
    public IScanner getScanner(String source) {
        return new Scanner(source);
    }
}
