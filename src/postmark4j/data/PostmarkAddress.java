package postmark4j.data;

/**
 * Represents an email address as defined by: http://developer.postmarkapp.com/developer-build.html#message-format
 *
 * John Doe &lt;email@example.com&gt;
 *
 * @author Erik Beeson
 */
public class PostmarkAddress {
	private final String name;
	private final String email;

	/**
	 *
	 * @param email
	 */
	public PostmarkAddress(String email) {
		this(null, email);
	}

	/**
	 *
	 * @param name
	 * @param email
	 */
	public PostmarkAddress(String name, String email) {
		this.name = ((name == null || name.trim().length() == 0) ? null : name.trim());
		this.email = ((email == null || email.trim().length() == 0) ? null : email.trim());

		if(email == null) {
			throw new IllegalArgumentException("A valid email address must be specified.");
		}
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public boolean equals(Object o) {
		if(this == o) return true;
		if(!(o instanceof PostmarkAddress)) return false;

		PostmarkAddress that = (PostmarkAddress) o;

		return !(email != null ? !email.equals(that.email) : that.email != null) &&
				!(name != null ? !name.equals(that.name) : that.name != null);

	}

	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (email != null ? email.hashCode() : 0);
		return result;
	}

	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PostmarkAddress");
		sb.append("{name='").append(name).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
