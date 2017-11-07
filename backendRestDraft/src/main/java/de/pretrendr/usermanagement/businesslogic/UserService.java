package de.pretrendr.usermanagement.businesslogic;

/**
 * @author Tristan Schneider
 *
 */
public interface UserService {
	/**
	 * Finds the user with the given username, compares the given password and
	 * return true iff matched, else false.
	 * 
	 * @param username
	 *            unique username
	 * @param password
	 *            password
	 * @return true iff matched, else false
	 */
	boolean checkCredentials(String username, String password);
}
