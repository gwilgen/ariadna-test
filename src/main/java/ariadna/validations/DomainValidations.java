package ariadna.validations;

import java.sql.Timestamp;

public class DomainValidations {
	
	public static boolean isValidTimestamp(String value) {
		try {
			Timestamp.valueOf(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isValidValue(String value) {
		try {
			Double.valueOf(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
