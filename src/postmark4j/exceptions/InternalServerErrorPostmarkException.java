package postmark4j.exceptions;

/**
 * As you may guess, this one is our fault :( (we promise we will do our best so this does not happen!).
 * Unfortunately, in this case you can be sure that the message is lost. We are notified in such cases,
 * so rest assured that someone is already looking into the issue.
 *
 * @author Erik Beeson
 */
public class InternalServerErrorPostmarkException extends PostmarkException {
	public InternalServerErrorPostmarkException() {
		super("Internal Server Error at Postmark");
	}
}
