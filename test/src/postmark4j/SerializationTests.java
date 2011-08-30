package postmark4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.testng.annotations.Test;
import postmark4j.data.PostmarkAddress;
import postmark4j.data.PostmarkAttachment;
import postmark4j.data.PostmarkMessage;
import postmark4j.data.PostmarkResponse;
import postmark4j.jsonio.ISODateDeserializer;
import postmark4j.jsonio.PostmarkAddressListSerializer;
import postmark4j.jsonio.PostmarkAddressSerializer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Some basic tests to verify that data is serialized to and from JSON correctly.
 *
 * Serialized forms taken from: http://developer.postmarkapp.com/developer-build.html
 *
 * @author Erik Beeson
 */
public class SerializationTests {
	private static final String SERIALIZED_MESSAGE = "{" +
			"\"From\":\"sender@example.com\"," +
			"\"To\":\"receiver@example.com\"," +
			"\"Cc\":\"copied@example.com\"," +
			"\"Bcc\":\"blank-copied@example.com\"," +
			"\"Subject\":\"Test\"," +
			"\"Tag\":\"Invitation\"," +
			"\"HtmlBody\":\"<b>Hello</b>\"," +
			"\"TextBody\":\"Hello\"," +
			"\"ReplyTo\":\"reply@example.com\"," +
			"\"Headers\":[{\"Name\":\"CUSTOM-HEADER\",\"Value\":\"value\"}]" +
			"}";

	private static final String SERIALIZED_MESSAGE_WITH_ATTACHMENTS = "{" +
			"\"From\":\"sender@example.com\"," +
			"\"To\":\"receiver@example.com\"," +
			"\"Subject\":\"Test\"," +
			"\"HtmlBody\":\"<b>Hello</b>\"," +
			"\"TextBody\":\"Hello\"," +
			"\"Headers\":[{\"Name\":\"CUSTOM-HEADER\",\"Value\":\"value\"}]," +
			"\"Attachments\":[" +
			"{" +
			"\"Name\":\"readme.txt\"," +
			"\"Content\":\"dGVzdCBjb250ZW50\"," +
			"\"ContentType\":\"text/plain\"" +
			"}," +
			"{" +
			"\"Name\":\"report.pdf\"," +
			"\"Content\":\"dGVzdCBjb250ZW50\"," +
			"\"ContentType\":\"application/octet-stream\"" +
			"}" +
			"]" +
			"}";

	private static final String SERIALIZED_BATCH_MESSAGES = "[{" +
			"\"From\":\"sender@example.com\"," +
			"\"To\":\"receiver1@example.com\"," +
			"\"Subject\":\"Postmark test #1\"," +
			"\"HtmlBody\":\"<html><body><strong>Hello</strong> dear Postmark user.</body></html>\"" +
			"},{" +
			"\"From\":\"sender@example.com\"," +
			"\"To\":\"receiver2@example.com\"," +
			"\"Subject\":\"Postmark test #2\"," +
			"\"HtmlBody\":\"<html><body><strong>Hello</strong> dear Postmark user.</body></html>\"" +
			"}]";

	private static final String SERIALIZED_RESPONSE = "{" +
			"\"ErrorCode\":0," +
			"\"Message\":\"OK\"," +
			"\"MessageID\":\"b7bc2f4a-e38e-4336-af7d-e6c392c2f817\"," +
			"\"SubmittedAt\":\"2010-11-26T12:01:05.1794748-05:00\"," +
			"\"To\":\"receiver@example.com\"" +
			"}";

	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(PostmarkAddress.class, new PostmarkAddressSerializer())
			.registerTypeAdapter(PostmarkAddressListSerializer.TYPE, new PostmarkAddressListSerializer())
			.registerTypeAdapter(Date.class, new ISODateDeserializer())
			.disableHtmlEscaping()
			.create();

	@Test
	public void serializeMessage() {
		System.out.println("PostmarkMessageTest.test");
		PostmarkMessage message = new PostmarkMessage(new PostmarkAddress("sender@example.com"), "Test", new PostmarkAddress("receiver@example.com"));
		message.setReplyTo(new PostmarkAddress("reply@example.com"));
		message.addCc(new PostmarkAddress("copied@example.com"));
		message.addBcc(new PostmarkAddress("blank-copied@example.com"));
		message.setTag("Invitation");
		message.setHtmlBody("<b>Hello</b>");
		message.setTextBody("Hello");
		message.addHeader("CUSTOM-HEADER", "value");
		assertThat(gson.toJson(message), is(SERIALIZED_MESSAGE));
	}

	@Test
	public void serializeMessageWithAttachments() throws IOException {
		PostmarkMessage message = new PostmarkMessage(new PostmarkAddress("sender@example.com"), "Test", new PostmarkAddress("receiver@example.com"));
		message.setHtmlBody("<b>Hello</b>");
		message.setTextBody("Hello");
		message.addHeader("CUSTOM-HEADER", "value");
		message.addAttachment(new PostmarkAttachment(new File("test/attachments/readme.txt"), "text/plain"));
		message.addAttachment(new PostmarkAttachment(new File("test/attachments/report.pdf"), "application/octet-stream"));
		assertThat(gson.toJson(message), is(SERIALIZED_MESSAGE_WITH_ATTACHMENTS));
	}

	@Test
	public void serializeBatchMessages() {
		PostmarkMessage message1 = new PostmarkMessage(new PostmarkAddress("sender@example.com"), "Postmark test #1", new PostmarkAddress("receiver1@example.com"));
		message1.setHtmlBody("<html><body><strong>Hello</strong> dear Postmark user.</body></html>");
		PostmarkMessage message2 = new PostmarkMessage(new PostmarkAddress("sender@example.com"), "Postmark test #2", new PostmarkAddress("receiver2@example.com"));
		message2.setHtmlBody("<html><body><strong>Hello</strong> dear Postmark user.</body></html>");
		assertThat(gson.toJson(new PostmarkMessage[]{message1, message2}), is(SERIALIZED_BATCH_MESSAGES));
	}

	@Test
	public void deserializeResponse() {
		PostmarkResponse response = gson.fromJson(SERIALIZED_RESPONSE, PostmarkResponse.class);
		assertThat(response.getMessageId(), is("b7bc2f4a-e38e-4336-af7d-e6c392c2f817"));
		assertThat(response.getSubmittedAt(), is(new Date(1290790865179l)));
		assertThat(response.getTo(), is("receiver@example.com"));
	}
}
