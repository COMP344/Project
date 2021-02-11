import java.util.*

public class Symbol extends Token{
	
	Map<String,Integer> symbolMap = Map.ofEntries(			
			entry("!",20),
			entry("#",11),
			entry("$",32),
			entry("&",6),
			entry("(",22),
			entry(")",33),
			entry("*",1),
			entry("+",3),
			entry(",",17),
			entry("-",4),
			entry(".",16),
			entry("/",2),
			entry(":",17),
			entry("=",10),
			entry(";",36),
			entry("<=",14),
			entry(">=",12),
			entry("?",21),
			entry(":=",23),
			entry("<",13),
			entry(">",15),
			entry("~",)5
			)
	
	
	
	public int getTokenType(String symbol) {
		return symbolMap(symbol);
	}
}
