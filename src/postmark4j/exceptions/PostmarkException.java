package postmark4j.exceptions;

/**
 * @author Erik Beeson
 */
public abstract class PostmarkException extends Exception {
	public PostmarkException(String s) {
		super(s);
	}
}
