package postmark4j.exceptions;

import postmark4j.data.PostmarkError;

/**
 * Something with the message is not quite right (either malformed JSON or incorrect fields).
 * In this case, the response body contains JSON {ErrorCode: 405, Message: "details"} with an error code
 * and an error message containing details on what went wrong.
 *
 * @author Erik Beeson
 */
public class UnprocessableEntityPostmarkException extends PostmarkException {
	protected final int errorCode;
	protected final String errorString;
	protected final String message;


	public UnprocessableEntityPostmarkException() {
		this(-1, "Unknown Error", "Empty or unexpected response.");
	}

	public UnprocessableEntityPostmarkException(PostmarkError error) {
		this(error.getErrorCode(), error.getErrorString(), error.getMessage());
	}

	public UnprocessableEntityPostmarkException(int errorCode, String errorString, String message) {
		super(errorCode + " " + errorString + ": " + message);

		this.errorCode = errorCode;
		this.errorString = errorString;
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorString() {
		return errorString;
	}

	public String getMessage() {
		return message;
	}
}
