package de.pretrendr.ex;

/**
 * If an email is given by a request, but its validation failed, which indicates
 * that the given email might be wrong or contain typos, this Exception should
 * be throws.
 * 
 * @author Tristan Schneider
 *
 */
public class EmailNotValidException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
