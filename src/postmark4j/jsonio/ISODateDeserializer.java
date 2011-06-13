package postmark4j.jsonio;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Deserializes a {@link Date} using {@link ISODateTimeFormat} from JodaTime.
 *
 * @author Erik Beeson
 */
public class ISODateDeserializer implements JsonDeserializer<Date> {
	private final DateTimeFormatter formatter = ISODateTimeFormat.dateTimeParser();

	public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		try {
			return formatter.parseDateTime(json.getAsString()).toDate();
		} catch(Exception e) {
			return null;
		}
	}
}
