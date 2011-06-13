package postmark4j.data;

import com.google.gson.annotations.SerializedName;

/**
 * Represents an error response as defined by: http://developer.postmarkapp.com/developer-build.html#http-response-codes
 *
 * {ErrorCode: 405, Message: "details"}
 *
 * @author Erik Beeson
 */
public class PostmarkError {
	@SerializedName("ErrorCode")
	private int errorCode;

	@SerializedName("Message")
	private String message;

	public int getErrorCode() {
		return errorCode;
	}

	public String getMessage() {
		return message;
	}

	public String getErrorString() {
		return ErrorCodes.getErrorString(errorCode);
	}

	private enum ErrorCodes {
		/**
		 * You requested a bounce by ID, but we could not find an entry in our database.
		 */
		BOUNCE_NOT_FOUND(407, "Bounce not found"),

		/**
		 * You provided bad arguments as a bounces filter.
		 */
		BOUNCE_QUERY_EXCEPTION(408, "Bounce query exception"),

		/**
		 * You tried to send to a recipient that has been marked as inactive. Inactive recipients are ones that have generated a hard bounce or a spam complaint.
		 */
		INACTIVE_RECIPIENT(406, "Inactive recipient"),

		/**
		 * The JSON input you provided is syntactically correct, but still not the one we expect.
		 */
		INCOMPATIBLE_JSON(403, "Incompatible JSON"),

		/**
		 * Validation failed for the email request JSON data that you provided.
		 */
		INVALID_EMAIL_REQUEST(300, "Invalid email request"),

		/**
		 * The JSON input you provided is syntactically incorrect.
		 */
		INVALID_JSON(402, "Invalid JSON"),

		/**
		 * Your HTTP request does not have the Accept and Content-Type headers set to application/json.
		 */
		JSON_REQUIRED(409, "JSON required"),

		/**
		 * Your request did not submit the correct API token in the X-Postmark-Server-Token header.
		 */
		BAD_OR_MISSING_API_TOKEN(0, "Bad or missing API token"),

		/**
		 * You are trying to send email with a From address that does not have a corresponding confirmed sender signature.
		 */
		SENDER_SIGNATURE_NOT_CONFIRMED(401, "Sender signature not confirmed"),

		/**
		 * You are trying to send email with a From address that does not have a sender signature.
		 */
		SENDER_SIGNATURE_NOT_FOUND(400, "Sender signature not found"),

		/**
		 * You ran out of credits.
		 */
		NOT_ALLOWED_TO_SEND(405, "Not allowed to send"),

		/**
		 * None of the above, unexpected error code.
		 */
		UNEXPECTED_ERROR("Unexpected error");

		private final Integer errorCode;
		private final String errorString;

		private ErrorCodes(String errorString) {
			this(null, errorString);
		}

		private ErrorCodes(Integer errorCode, String errorString) {
			this.errorCode = errorCode;
			this.errorString = errorString;
		}

		public Integer getErrorCode() {
			return errorCode;
		}

		public String getErrorString() {
			return errorString;
		}

		public static String getErrorString(int errorCode) {
			for(ErrorCodes value : values()) {
				if(value.getErrorCode() == errorCode) {
					return value.getErrorString();
				}
			}
			return UNEXPECTED_ERROR.getErrorString();
		}
	}
}
