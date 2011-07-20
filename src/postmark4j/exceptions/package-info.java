/**
 * Exceptions related to a Postmark request. All extend a base {@link postmark4j.exceptions.PostmarkException} to
 * make it easy to "catch all" Postmark related exceptions.
 *
 * Also thrown by some classes is an unchecked {@link java.lang.IllegalArgumentException} when incorrect
 * parameters are given.
 */
package postmark4j.exceptions;