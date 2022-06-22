package ariadna.validations;

/**
 * Specific type of RuntimeException to catch custom behavior
 *
 */
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String msg) {
		super(msg);
	}
	
}
