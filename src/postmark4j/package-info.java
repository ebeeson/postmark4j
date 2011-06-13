/**
 *  Postmark4j: a (almost) complete Java library for accessing the <a href="http://postmarkapp.com/">Postmark</a>
 *  transactional mail service.
 *
 *  The main entry point is {@link PostmarkClient} which should be configured with a Server Token, and optionally a
 *  "default from address" (see the {@link PostmarkClient} docs for more info).
 *
 *  Messages are sent by calling {@link PostmarkClient#send(postmark4j.data.PostmarkMessage...)} with one or more
 *  {@link postmark4j.data.PostmarkMessage}s.
 */
package postmark4j;