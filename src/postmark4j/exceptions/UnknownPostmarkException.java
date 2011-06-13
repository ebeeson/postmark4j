package postmark4j.exceptions;

/**
 * Represents an HTTP Status that wasn't documented.
 *
 * @author Erik Beeson
 */
public class UnknownPostmarkException extends PostmarkException {
	private final int status;

	public UnknownPostmarkException(int status) {
		super("Unexpected HTTP status: " + status);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}
