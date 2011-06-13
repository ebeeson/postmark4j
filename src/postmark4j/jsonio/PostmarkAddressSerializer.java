package postmark4j.jsonio;

import com.google.gson.*;
import postmark4j.data.PostmarkAddress;

import java.lang.reflect.Type;

/**
 * Custom serializer for {@link PostmarkAddress} since Postmark expects Strings
 * but we prefer to represent them as Objects.
 *
 * @author Erik Beeson
 */
public class PostmarkAddressSerializer implements JsonSerializer<PostmarkAddress> {
	public JsonElement serialize(PostmarkAddress address, Type typeOfSrc, JsonSerializationContext context) {
		if(address == null) {
			return new JsonNull();
		} else {
			if(address.getName() != null) {
				return new JsonPrimitive(address.getName() + " <" + address.getEmail() + ">");
			} else {
				return new JsonPrimitive(address.getEmail());
			}
		}
	}
}
