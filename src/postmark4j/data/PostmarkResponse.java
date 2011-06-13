package postmark4j.data;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 * http://developer.postmarkapp.com/developer-build.html#success-response
 * 
 * {
 *   "ErrorCode" : 0,
 *   "Message" : "OK",
 *   "MessageID" : "b7bc2f4a-e38e-4336-af7d-e6c392c2f817",
 *   "SubmittedAt" : "2010-11-26T12:01:05.1794748-05:00",
 *   "To" : "receiver@example.com"
 * }
 *
 * @author Erik Beeson
 */
public class PostmarkResponse {
	@SerializedName("MessageID")
	protected String messageId;

	// The time the request was received by Postmark.
	@SerializedName("SubmittedAt")
	protected Date submittedAt;

	// The recipient of the submitted request.
	@SerializedName("To")
	protected String to;

	public String getMessageId() {
		return messageId;
	}

	public Date getSubmittedAt() {
		return submittedAt;
	}

	public String getTo() {
		return to;
	}

	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof PostmarkResponse)) return false;

		PostmarkResponse response = (PostmarkResponse) o;

		return !(messageId != null ? !messageId.equals(response.messageId) : response.messageId != null) &&
				!(submittedAt != null ? !submittedAt.equals(response.submittedAt) : response.submittedAt != null) &&
				!(to != null ? !to.equals(response.to) : response.to != null);

	}

	public int hashCode() {
		int result = messageId != null ? messageId.hashCode() : 0;
		result = 31 * result + (submittedAt != null ? submittedAt.hashCode() : 0);
		result = 31 * result + (to != null ? to.hashCode() : 0);
		return result;
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PostmarkResponse");
		sb.append("{messageId='").append(messageId).append('\'');
		sb.append(", submittedAt=").append(submittedAt);
		sb.append(", to='").append(to).append('\'');
		sb.append('}');
		return sb.toString();
	}

}
