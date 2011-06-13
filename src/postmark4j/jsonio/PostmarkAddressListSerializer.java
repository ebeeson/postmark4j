package postmark4j.jsonio;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import postmark4j.data.PostmarkAddress;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

/**
 * Custom serializer for a {@link List} of {@link PostmarkAddress}es since Postmark expects
 * multiple addresses as a single String but we prefer to represent them as a {@link List}.
 *
 * @author Erik Beeson
 */
public class PostmarkAddressListSerializer implements JsonSerializer<List<PostmarkAddress>> {
	public static final Type TYPE = new TypeToken<List<PostmarkAddress>>() {
	}.getType();

	public JsonElement serialize(List<PostmarkAddress> src, Type typeOfSrc, JsonSerializationContext context) {
		StringBuilder builder = new StringBuilder();
		for(Iterator<PostmarkAddress> iterator = src.iterator(); iterator.hasNext(); ) {
			builder.append(context.serialize(iterator.next(), PostmarkAddress.class).getAsString());
			if(iterator.hasNext()) {
				builder.append(", ");
			}
		}
		if(builder.length() > 0) {
			return new JsonPrimitive(builder.toString());
		} else {
			return new JsonNull();
		}
	}
}
