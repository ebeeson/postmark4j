package postmark4j;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import postmark4j.data.PostmarkAddress;
import postmark4j.data.PostmarkError;
import postmark4j.data.PostmarkMessage;
import postmark4j.data.PostmarkResponse;
import postmark4j.exceptions.InternalServerErrorPostmarkException;
import postmark4j.exceptions.UnauthorizedPostmarkException;
import postmark4j.exceptions.UnknownPostmarkException;
import postmark4j.exceptions.UnprocessableEntityPostmarkException;
import postmark4j.jsonio.ISODateDeserializer;
import postmark4j.jsonio.PostmarkAddressListSerializer;
import postmark4j.jsonio.PostmarkAddressSerializer;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * The main class for sending messages.
 *
 * Includes constructors for a variety of configurations.
 *
 * Often all messages will be sent using the same "from" address, so this class supports setting a so-called
 * "default from address". If set here, messages are <em>not required</em> to specify a "from" address, though
 * if a message does specify a "from" address, it will be used instead of the default. If a "default from address"
 * is not given, then all {@link PostmarkMessage}s passed to {@link #send(postmark4j.data.PostmarkMessage...)} must
 * include a "from" address.
 *
 * If a server token isn't given, the test token "POSTMARK_API_TEST" will be used.
 *
 * By default, HTTPS will be used, though this can be overriden by passing {@code false} to an appropriate
 * constructor for the {@code https} parameter.
 *
 * @author Erik Beeson
 */
@SuppressWarnings({"NullableProblems", "UnusedDeclaration"})
public class PostmarkClient {
	private static final Logger LOGGER = LoggerFactory.getLogger(PostmarkClient.class);

	/**
	 * Often when implementing your client library or when integrating an existing library into your application you
	 * would want to send "test" emails that don't actually get delivered to the recipient. You just need to know if
	 * your data is valid. You can do that by passing the "POSTMARK_API_TEST" value as your server API token.
	 *
	 * @see <a href="http://developer.postmarkapp.com/developer-build.html#authentication-headers">http://developer.postmarkapp.com/developer-build.html#authentication-headers</a>
	 */
	public static final String TEST_SERVER_TOKEN = "POSTMARK_API_TEST";

	private static final String URL_BASE_HTTP = "http://api.postmarkapp.com";
	private static final String URL_BASE_HTTPS = "https://api.postmarkapp.com";

	private static final String URL_SEND = "/email";
	private static final String URL_BATCH = "/email/batch";
	private static final String URL_BOUNCE_TAGS = "/bounces/tags";

	private final Gson gson = new GsonBuilder()
			.registerTypeAdapter(PostmarkAddress.class, new PostmarkAddressSerializer())
			.registerTypeAdapter(PostmarkAddressListSerializer.TYPE, new PostmarkAddressListSerializer())
			.registerTypeAdapter(Date.class, new ISODateDeserializer())
			.create();

	private final String serverToken;
	private final PostmarkAddress defaultFrom;
	private final boolean https;

	private static final String version;

	static {
		String v;
		try {
			Properties properties = new Properties();
			properties.load(PostmarkClient.class.getResourceAsStream("version.properties"));
			v = properties.getProperty("version", "X");
		} catch(IOException ignored) {
			v = "X";
		}
		version = v;
	}

	public PostmarkClient(String serverToken) {
		this(serverToken, null, true);
	}

	public PostmarkClient(String serverToken, boolean https) {
		this(serverToken, null, https);
	}

	public PostmarkClient(String serverToken, PostmarkAddress defaultFrom) {
		this(serverToken, defaultFrom, true);
	}

	public PostmarkClient(String serverToken, PostmarkAddress defaultFrom, boolean https) {
		this.serverToken = serverToken != null && serverToken.trim().length() > 0 ? serverToken.trim() : TEST_SERVER_TOKEN;
		this.defaultFrom = defaultFrom;
		this.https = https;

		if(LOGGER.isInfoEnabled()) {
			LOGGER.info("Created PostmarkClient [{}], Server Token: {}", (this.https ? "https" : "http"),
					TEST_SERVER_TOKEN.equals(this.serverToken) ? TEST_SERVER_TOKEN : this.serverToken.replaceAll("[a-zA-Z0-9]", "X")
			);
		}
	}

	protected String getBaseURL() {
		return https ? URL_BASE_HTTPS : URL_BASE_HTTP;
	}

	/**
	 * Send a message.
	 *
	 * @param message The message to send.
	 * @return The response from Postmark.
	 * @throws IOException If there's a problem communicating with the Postmark server.
	 * @throws postmark4j.exceptions.InternalServerErrorPostmarkException Error at Postmark servers. See: {@link InternalServerErrorPostmarkException}.
	 * @throws postmark4j.exceptions.UnauthorizedPostmarkException Missing or incorrect API Key header.
	 * @throws postmark4j.exceptions.UnknownPostmarkException Postmark returned an HTTP Status that wasn't documented.
	 * @throws postmark4j.exceptions.UnprocessableEntityPostmarkException Something with the message is not quite right. See: {@link UnprocessableEntityPostmarkException}.
	 */
	public PostmarkResponse send(PostmarkMessage message) throws IOException, UnauthorizedPostmarkException, UnprocessableEntityPostmarkException, InternalServerErrorPostmarkException, UnknownPostmarkException {
		return send(URL_SEND, new PostmarkMessage[]{message}, PostmarkResponse.class);
	}

	/**
	 * Send messages.
	 *
	 * @param messages The messages to send.
	 * @return The responses from Postmark.
	 * @throws IOException If there's a problem communicating with the Postmark server.
	 * @throws postmark4j.exceptions.InternalServerErrorPostmarkException Error at Postmark servers. See: {@link InternalServerErrorPostmarkException}.
	 * @throws postmark4j.exceptions.UnauthorizedPostmarkException Missing or incorrect API Key header.
	 * @throws postmark4j.exceptions.UnknownPostmarkException Postmark returned an HTTP Status that wasn't documented.
	 * @throws postmark4j.exceptions.UnprocessableEntityPostmarkException Something with the message is not quite right. See: {@link UnprocessableEntityPostmarkException}.
	 */
	public PostmarkResponse[] send(PostmarkMessage... messages) throws IOException, UnauthorizedPostmarkException, UnprocessableEntityPostmarkException, InternalServerErrorPostmarkException, UnknownPostmarkException {
		return send(URL_BATCH, messages, PostmarkResponse[].class);
	}

	protected String toJson(PostmarkMessage... messages) {
		if(messages.length == 0) {
			throw new IllegalArgumentException("At least one message is required.");
		}

		for(int i = 0; i < messages.length; i++) {
			if(messages[i].getFrom() == null) {
				if(defaultFrom == null) {
					throw new IllegalArgumentException("A valid \"from\" address is required.");
				} else {
					messages[i] = new PostmarkMessage(defaultFrom, messages[i]);
				}
			}
		}

		if(LOGGER.isTraceEnabled() || LOGGER.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			for(PostmarkMessage message : messages) {
				if(sb.length() != 0) {
					sb.append(", ");
				}
				if(LOGGER.isTraceEnabled()) {
					sb.append(message.toStringVerbose());
				} else {
					sb.append(message.toString());
				}
			}
			LOGGER.debug("send: [{}]", sb.toString());
		}

		return gson.toJson((messages.length > 1) ? messages : messages[0]);
	}

	protected <T> T send(String url, PostmarkMessage[] messages, Class<T> responseClass) throws IOException, UnauthorizedPostmarkException, UnprocessableEntityPostmarkException, InternalServerErrorPostmarkException, UnknownPostmarkException {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			return send(httpClient, url, messages, responseClass);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	protected <T> T send(HttpClient httpClient, String url, PostmarkMessage[] messages, Class<T> responseClass) throws IOException, UnauthorizedPostmarkException, UnprocessableEntityPostmarkException, InternalServerErrorPostmarkException, UnknownPostmarkException {
		HttpPost method = new HttpPost(getBaseURL() + url);

		method.addHeader("Accept", "application/json");
		method.addHeader("Content-Type", "application/json; charset=UTF-8");
		method.addHeader("X-Postmark-Server-Token", serverToken);
		method.addHeader("User-Agent", "postmark4j-" + version);

		String requestJson = toJson(messages);
		LOGGER.debug("Request: {}", requestJson);
		method.setEntity(new StringEntity(requestJson, "UTF-8"));

		HttpResponse httpResponse = httpClient.execute(method);
		HttpEntity entity = httpResponse.getEntity();
		String responseJson = (entity == null ? null : EntityUtils.toString(entity));

		int statusCode = httpResponse.getStatusLine().getStatusCode();

		LOGGER.debug("Status: {}, Response: {}", statusCode, responseJson);

		if(statusCode == 200) {
			return gson.fromJson(responseJson, responseClass);
		} else if(statusCode == 401) {
			throw new UnauthorizedPostmarkException();
		} else if(statusCode == 422) {
			if(responseJson != null && responseJson.trim().length() > 0) {
				throw new UnprocessableEntityPostmarkException(gson.fromJson(responseJson, PostmarkError.class));
			} else {
				throw new UnprocessableEntityPostmarkException();
			}
		} else if(statusCode == 500) {
			throw new InternalServerErrorPostmarkException();
		} else {
			throw new UnknownPostmarkException(httpResponse.getStatusLine().getStatusCode());
		}
	}
}
