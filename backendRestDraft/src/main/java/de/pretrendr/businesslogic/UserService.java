package de.pretrendr.businesslogic;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import de.pretrendr.model.User;
import de.pretrendr.model.pojo.RegUser;

/**
 * @author Tristan Schneider
 *
 */
public interface UserService {

	/**
	 * Registers the given <b>userReg</b> in the database. The default role is
	 * <i>ROLE_USER</i>. If the given username or email are already present in the
	 * database, null is returned and the registration failed. Else, the created
	 * {@link User} is returned.
	 * 
	 * @param userReg
	 *            the user details to be registered
	 * @return iff credentials unique, then the created {@link User} is returned,
	 *         else null
	 */
	User register(RegUser userReg);

	/**
	 * Retrieves user with the give id from the database. If not found, an
	 * {@link EntityNotFoundException} will be thrown.
	 * 
	 * @param userId
	 * @return user
	 */
	User getUser(UUID userId);

	List<User> getAll();

}
