package postmark4j.exceptions;

/**
 * Missing or incorrect API Key header.
 *
 * @author Erik Beeson
 */
public class UnauthorizedPostmarkException extends PostmarkException {
	public UnauthorizedPostmarkException() {
		super("Missing or incorrect API Key header.");
	}
}
