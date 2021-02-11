
public class Number extends Token{
	
	private Number(){}

	//https://www.baeldung.com/java-check-string-number
	private boolean isNumber(String token) {
		
	    try {
	        double d = Double.parseDouble(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	
	public static int getToken(String symbol){		
		if(this.isNumber(symbol)) {
			return symbol;
		}
		else {
		//Future error code, magic number for now
			return -2;
		}
	}
}
