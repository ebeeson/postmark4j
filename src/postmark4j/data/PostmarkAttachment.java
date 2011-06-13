package postmark4j.data;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Represents an attachment as defined by: http://developer.postmarkapp.com/developer-build.html#attachments
 *
 * {
 *   "Name": "readme.txt",
 *   "Content": "dGVzdCBjb250ZW50",
 *   "ContentType": "text/plain"
 * }
 *
 * @author Erik Beeson
 */
public class PostmarkAttachment {
	protected static final Logger LOGGER = LoggerFactory.getLogger(PostmarkAttachment.class);

	public static final String[] ALLOWED_EXTENSIONS = {
			"gif", "jpg", "jpeg", "png", "swf", "flv", "avi", "mpg", "mp3", "mp4", "ogv", "wav", "rm", "mov", "psd", "ai", "tif", "tiff",
			"txt", "rtf", "htm", "html", "pdf", "epub", "mobi", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "ps", "eps", "iif",
			"log", "csv", "ics", "xml"
	};

	@SerializedName("Name")
	private final String name;

	@SerializedName("Content")
	private final String content;

	@SerializedName("ContentType")
	private final String contentType;

	public PostmarkAttachment(File file, String contentType) throws IOException {
		this(file, contentType, file.getName());
	}

	public PostmarkAttachment(File file, String contentType, String name) throws IOException {
		this.name = name;
		this.content = Base64.encodeBase64String(IOUtils.toByteArray(new FileInputStream(file)));
		this.contentType = contentType;

		if(!isAcceptableExtension(file)) {
			LOGGER.warn("Attachment with unacceptable extension: {}", file);
		}
	}

	public String getName() {
		return name;
	}

	public String getContent() {
		return content;
	}

	public String getContentType() {
		return contentType;
	}

	public static boolean isAcceptableExtension(File file) {
		String name = file.getName().toLowerCase();
		for(String allowedExtension : ALLOWED_EXTENSIONS) {
			if(name.endsWith(allowedExtension)) {
				return true;
			}
		}
		return false;
	}
}
