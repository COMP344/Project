
public class Number extends Token{
	
	//https://www.baeldung.com/java-check-string-number
	public static boolean isNumber(String token) {
		
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public int getToken(String symbol){		
		if(isNumber(symbol)) {
			return symbol;
		}
		else {
		//Future error code
			return null;
		}
	}
}
