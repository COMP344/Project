
public class PICLScanner{

    static File srcFile;
    static FileWriter fileWriter;
    static BufferedWriter writer;
    static FileReader fileReader;
    static BufferedReader reader;

    IScanner scanner;

    public PICLScanner(String srcFilename){
        // Check if the srcFile exists.
        try{
            srcFile = new File(srcFilename);
            if (!srcFile.canRead()) {
                System.out.println("TestFileInputStream: Cannot read srcFile \"" + srcFilename + "\"");
                return;
            } catch (IOException e){
                System.out.printl("TestFIleInputStream: Cannot open srcFile \"" + srcFilename + "\"");
                e.getMessage();
            }
        }

        IScanner scanner = new BasicScanner(srcFile);
/*
        // Scan the srcFile character per character.
        int token;


        while ( (token = scanner.getToken()) != Eof ) {
            System.out.print( "[" + token + "]" );
        }

        scanner.close();
        System.out.println("\ndone.");
    */
    }



    public long getPosition(){
        return 0;//STUB needs data from a scanner
    }

    private class NoSymbolException extends Exception{
        public NoSymbolException(String errorMessage) {
            super(errorMessage);
        }
    }
//Rewriting the Oberon file
    public int identifySymbol(String symbol, Token symbols) throws NoSymbolException{
        int symbolFound = -1;
        char ch;
        do{
            ch = scanner.getToken();
            symbol = symbol + Character.toString(ch);
            scanner.eatToken();
            if (symbols.getToken(symbol)!=null){
                symbolFound = symbols.getToken();
                break;
            }
        }while(ch != scanner.EOF || ch != ' ' || ch != '\n');
        if (symbolFound ==-1){
            throw new NoSymbolException("Could not find symbol");
        }
        return symbolFound;
    }

    //PROCEDURE Number -> String.parseInt()
    //PROCEDURE getDigit -> jva included
    //PROCEDURE Hex -> Hex Converter?

    public convertGetStar (int sym, Token symbols){
        char ch;
        do{
            ch = scanner.getToken();
            if (ch == '{') {
                while(ch!='}'){
                    scanner.eatToken();
                    ch=scanner.getToken(IScanner);
                }
            }
            scanner.eatToken();
        }while(ch <= ' ' || ch == '{');

        if(symbols.getToken(Character.toString(ch))!=null){
            sym = symbols.getToken(Character.toString(ch));
        } else {
            try {
                sym = identifySymbol(Character.toString(ch));
            } catch (NoSymbolException e){
                sym = -1;
            }
        }
        return sym;
    }
}
// These check to see the type of token

public boolean containsNumber(String word){
    return str.matches(".*\\d.*");
}

public boolean onlyContainsLetters(String word){
    return word.matches("[a-zA-Z]+");
}

public boolean onlyContainsNumbers(String word){
    return word.matches("[0-9]+");
}
public boolean onlyContainsSymbols(String word){
    boolean isOnlySymbols = true;
    for (int i = 0; i < word.length();i++){
        if ((Character.isLetter(s.charAt(i)) == true) || (Character.isLetter(s.charAt(i)) == true)){
            isOnlySymbols = false;
        }
    }
    return isOnlySymbols
}

public int classifyToken(String token){
    if(onlyContainsLetters(token)){
        //STUB fixed symbol table classes
        return Word.getToken(word);
    }
    if(onlyContainsNumbers(token)){
        //STUB fixed symbol table classes
        return (int) token;
    }
    if(onlyContainsSymbols(token)){
        //STUB fixed symbol table classes
        return Symbol.getToken(token);
    }
    //End case iterate character by character of the token
    /* Example 1) ~!A.2
    * create temp string
    * iterate character by character
    * Add character to the string
    * if the next character is the same type then add it to the string
    *       if they are symbols check them together in the symbol table, if null check the first one and restart process
    * if it isn't the same type, check first character in the symbol table and restart
    *       find way for procedure names like cost4000 to not throw an error, maybe have a dichotomy of letters/numbers vs symbols
    */

}

