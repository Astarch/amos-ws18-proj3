package de.pretrendr.usermanagement.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.QUser;
import de.pretrendr.usermanagement.model.User;

/**
 * @author Tristan Schneider
 *
 */
@Service
@ComponentScan("de.pretrendr")
public class UserServiceImpl implements UserService {
	private final UserDAO userDAO;

	@Autowired
	public UserServiceImpl(final UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @author Tristan Schneider
	 */
	@Override
	public boolean checkCredentials(String username, String password) {
		User user = userDAO.findOne(QUser.user.username.eq(username));
		return user != null && user.getPassword().equals(password);
	}
}
