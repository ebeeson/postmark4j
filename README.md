# Usage
To use, `send` a `PostmarkMessage` with an instance of `PostmarkClient`.

First, create a `PostmarkClient` with your *Server Token*:

```java
PostmarkClient postmark = new PostmarkClient("YOUR_SERVER_TOKEN");
```

Or with a "defaul from address" (see the javadocs for `PostmarkClient` for more info):

```java
PostmarkClient postmark = new PostmarkClient("YOUR_SERVER_TOKEN", new PostmarkAddress("email@example.com"));
```

Or with a "default from address" with a "name":

```java
PostmarkClient postmark = new PostmarkClient("YOUR_SERVER_TOKEN", new PostmarkAddress("My Company", "email@example.com"));
```

See the other `PostmarkClient` constructors for more options.

Then create a `PostmarkMessage`:

```java
PostmarkMessage message = new PostmarkMessage("Example Subject", new PostmarkAddress("User Name", "user@example.org"));
message.setTextBody("This is an exmaple message.");
```

See the `set*` and `add*` methods on `PostmarkMessage` for more options.

Then send the message:

```java
postmark.send(message); // catch exceptions as needed
```

That's it! Check out the javadocs for more info.


# Dependencies
I hate projects with a ton of dependencies, but the reality is that better tools can be built faster when
"standing on the shoulders of giants", so here we go.

## GSON
For very clean JSON serialization/deserialization. It's the XStream of JSON (because JSON with XStream
is an extremely cludgy hack).

## HttpClient 4
For making HTTP/HTTPS requests. URLConnection just doesn't cut it.

HttpClient 4 also depends on:

* Commons Logging (or jcl-over-slf4j, because commons-logging is for wimps)
* Commons Codec (also required by postmark4j)
* httpcore (included)
* httpmime (included)

## slf4j
For logging, obviously. Built with slf4j-simple, but you'll use your own binding.

## JodaTime
Used for parsing ISO 8601 dates. This thing is over 500k, so I'd love to avoid it, but parsing ISO 8601
date/time strings ends up being non-trivial otherwise. If you don't like it, ask Wildbit to return date/times
in a more Java-friendly format (SimpleDateFormat can't parse the ISO 8601 timezone format).

## Commons Codec
HttpClient needs it, so we use it too.

## Commons IO
Because we're lazy.


# Changes
## 1.1
### New Features
* Attachments (pretty basic, doesn't verify attachement type or size yet).

## 1.0
### New Features
* Batch message sending
* JodaTime for proper parsing of ISO 8601 date/time
* Better docs
* Package reorganization
* Renamed to postmark4j


### Fixes
* Hard-code message entity as UTF-8 in PostmarkClient
