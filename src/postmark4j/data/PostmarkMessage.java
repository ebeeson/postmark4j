package postmark4j.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a message as defined by: http://developer.postmarkapp.com/developer-build.html#message-format
 *
 * {
 *   "From" : "sender@example.com",
 *   "To" : "receiver@example.com",
 *   "Cc" : "copied@example.com",
 *   "Bcc": "blank-copied@example.com",
 *   "Subject" : "Test",
 *   "Tag" : "Invitation",
 *   "HtmlBody" : "<b>Hello</b>",
 *   "TextBody" : "Hello",
 *   "ReplyTo" : "reply@example.com",
 *   "Headers" : [{ "Name" : "CUSTOM-HEADER", "Value" : "value" }]
 * }
 *
 * @author Erik Beeson
 */
public class PostmarkMessage {
	@SerializedName("From")
	private final PostmarkAddress from;

	@SerializedName("To")
	private final List<PostmarkAddress> to = new ArrayList<PostmarkAddress>();

	@SerializedName("Cc")
	private final List<PostmarkAddress> cc = new ArrayList<PostmarkAddress>();

	@SerializedName("Bcc")
	private final List<PostmarkAddress> bcc = new ArrayList<PostmarkAddress>();

	@SerializedName("Subject")
	private final String subject;

	@SerializedName("Tag")
	private String tag;

	@SerializedName("HtmlBody")
	private String htmlBody;

	@SerializedName("TextBody")
	private String textBody;

	@SerializedName("ReplyTo")
	private PostmarkAddress replyTo;

	@SerializedName("Headers")
	private List<Header> headers;

	@SerializedName("Attachments")
	private List<PostmarkAttachment> attachments;

	public PostmarkMessage(String subject, PostmarkAddress... to) {
		this(null, subject, to);
	}

	public PostmarkMessage(PostmarkAddress from, String subject, PostmarkAddress... to) {
		if(to == null || to.length == 0) {
			throw new IllegalArgumentException("At least one \"to\" address is required.");
		}

		if(subject == null || subject.trim().length() == 0) {
			throw new IllegalArgumentException("A \"subject\" is required.");
		}

		this.from = from;
		Collections.addAll(this.to, to);
		this.subject = subject.trim();
	}

	/**
	 * Copy constructor that sets the {@code from} field.
	 *
	 * @param from
	 * @param message The message to copy data from.
	 */
	public PostmarkMessage(PostmarkAddress from, PostmarkMessage message) {
		this.from = from;
		this.to.addAll(message.to);
		this.subject = message.subject;
		this.replyTo = message.replyTo;
		this.cc.addAll(message.cc);
		this.bcc.addAll(message.bcc);
		this.htmlBody = message.htmlBody;
		this.textBody = message.textBody;
		this.tag = message.tag;
	}

	public PostmarkAddress getFrom() {
		return from;
	}

	public List<PostmarkAddress> getTo() {
		return to;
	}

	public List<PostmarkAddress> getCc() {
		return cc;
	}

	public List<PostmarkAddress> getBcc() {
		return bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void addTo(String email) {
		addTo(new PostmarkAddress(email));
	}

	public void addTo(String name, String email) {
		addTo(new PostmarkAddress(name, email));
	}

	public void addTo(PostmarkAddress... to) {
		checkAddressCount(to.length);
		Collections.addAll(this.to, to);
	}

	public void addCc(String email) {
		addCc(new PostmarkAddress(email));
	}

	public void addCc(String name, String email) {
		addCc(new PostmarkAddress(name, email));
	}

	public void addCc(PostmarkAddress... cc) {
		checkAddressCount(cc.length);
		Collections.addAll(this.cc, cc);
	}

	public void addBcc(String email) {
		addBcc(new PostmarkAddress(email));
	}

	public void addBcc(String name, String email) {
		addBcc(new PostmarkAddress(name, email));
	}

	public void addBcc(PostmarkAddress... bcc) {
		checkAddressCount(bcc.length);
		Collections.addAll(this.bcc, bcc);
	}

	private void checkAddressCount(int size) {
		if(this.to.size() + this.cc.size() + this.bcc.size() + size > 20) {
			throw new IllegalArgumentException("Maximum of 20 address in To, CC, and BCC has been exceeded.");
		}
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}

	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(String textBody) {
		this.textBody = textBody;
	}

	public PostmarkAddress getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(PostmarkAddress replyTo) {
		this.replyTo = replyTo;
	}

	public List<Header> getHeaders() {
		return headers;
	}

	public synchronized void addHeader(String name, String value) {
		if(headers == null) {
			headers = new ArrayList<Header>();
		}
		headers.add(new Header(name, value));
	}

	public synchronized void addAttachment(PostmarkAttachment... attachments) {
		if(this.attachments == null) {
			this.attachments = new ArrayList<PostmarkAttachment>();
		}

		Collections.addAll(this.attachments, attachments);
	}

	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof PostmarkMessage)) return false;

		PostmarkMessage message = (PostmarkMessage) o;

		return !(cc != null ? !cc.equals(message.cc) : message.cc != null) &&
				!(from != null ? !from.equals(message.from) : message.from != null) &&
				!(headers != null ? !headers.equals(message.headers) : message.headers != null) &&
				!(htmlBody != null ? !htmlBody.equals(message.htmlBody) : message.htmlBody != null) &&
				!(replyTo != null ? !replyTo.equals(message.replyTo) : message.replyTo != null) &&
				!(subject != null ? !subject.equals(message.subject) : message.subject != null) &&
				!(tag != null ? !tag.equals(message.tag) : message.tag != null) &&
				!(textBody != null ? !textBody.equals(message.textBody) : message.textBody != null) &&
				!(to != null ? !to.equals(message.to) : message.to != null);
	}

	public int hashCode() {
		int result = from != null ? from.hashCode() : 0;
		result = 31 * result + (to != null ? to.hashCode() : 0);
		result = 31 * result + (subject != null ? subject.hashCode() : 0);
		result = 31 * result + (replyTo != null ? replyTo.hashCode() : 0);
		result = 31 * result + (cc != null ? cc.hashCode() : 0);
		result = 31 * result + (htmlBody != null ? htmlBody.hashCode() : 0);
		result = 31 * result + (textBody != null ? textBody.hashCode() : 0);
		result = 31 * result + (tag != null ? tag.hashCode() : 0);
		result = 31 * result + (headers != null ? headers.hashCode() : 0);
		return result;
	}

	public String toString() {
		return toString(false);
	}

	public String toStringVerbose() {
		return toString(true);
	}

	private String toString(boolean verbose) {
		final StringBuilder sb = new StringBuilder();
		sb.append("PostmarkMessage");
		sb.append("{from='").append(from).append('\'');
		sb.append(", to='").append(to).append('\'');
		sb.append(", subject='").append(subject).append('\'');
		sb.append(", replyTo='").append(replyTo).append('\'');
		sb.append(", cc='").append(cc).append('\'');
		sb.append(", bcc='").append(bcc).append('\'');

		if(verbose) {
			sb.append(", htmlBody='").append(htmlBody).append('\'');
			sb.append(", textBody='").append(textBody).append('\'');
		} else {
			sb.append(", htmlBody.length=").append(htmlBody == null ? "null" : htmlBody.length());
			sb.append(", textBody.length=").append(textBody == null ? "null" : textBody.length());
		}

		sb.append(", tag='").append(tag).append('\'');
		sb.append(", headers=").append(headers);
		sb.append('}');
		return sb.toString();
	}

	public static class Header {
		@SerializedName("Name")
		private final String name;

		@SerializedName("Value")
		private final String value;

		public Header(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}

		public String toString() {
			return name + " : " + value;
		}

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;

			Header that = (Header) o;

			return !(name != null ? !name.equals(that.name) : that.name != null) &&
					!(value != null ? !value.equals(that.value) : that.value != null);

		}

		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (value != null ? value.hashCode() : 0);
			return result;
		}
	}
}


