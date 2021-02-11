import java.util.Map;

public class Word extends Token{
	
	private Word(){}

	protected static Map<String,Integer> wordMap = Map.ofEntries(			
			entry("BEGIN",51),
			entry("BOOL",44),
			entry("CONST",50),
			entry("DEC",29),
			entry("DO",35),
			entry("ELSE",38),
			entry("ELSIF",39),
			entry("END",37),
			entry("IF",25),
			entry("INC",28),
			entry("INT",42),
			entry("MODULE",53),
			entry("OR",7),
			entry("PROCEDURE",52),
			entry("REPEAT",27),
			entry("RETURN",41),
			entry("ROL",30),
			entry("ROR",31),
			entry("SET",43),
			entry("THEN",34),
			entry("UNTIL",40),
			entry("WHILE",26)
			)

	public static int getToken(String symbol){
			if(wordMap.get(symbol) != null){
				return wordMap.get(symbol);
			}
				return (int)symbol;
	}
}
