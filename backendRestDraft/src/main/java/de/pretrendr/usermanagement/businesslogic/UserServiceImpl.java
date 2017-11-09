package de.pretrendr.usermanagement.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import de.pretrendr.usermanagement.dataccess.RoleDAO;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.QRole;
import de.pretrendr.usermanagement.model.QUser;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;
import de.pretrendr.usermanagement.model.pojo.RegUser;

/**
 * @author Tristan Schneider
 *
 */
@Service
@ComponentScan("de.pretrendr")
public class UserServiceImpl implements UserService {
	private final UserDAO userDAO;
	private final RoleDAO roleDAO;

	@Autowired
	public UserServiceImpl(final UserDAO userDAO, final RoleDAO roleDAO) {
		this.userDAO = userDAO;
		this.roleDAO = roleDAO;
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

	/**
	 * {@inheritDoc}
	 * 
	 * @author Tristan Schneider
	 */
	@Override
	public User register(RegUser userReg) {
		User user = userDAO
				.findOne(QUser.user.username.eq(userReg.getUsername()).or(QUser.user.email.eq(userReg.getEmail())));
		if (user != null) {
			return null;
		} else {
			user = new User(userReg.getUsername(), userReg.getPassword(), userReg.getFirstname(), userReg.getLastname(),
					userReg.getEmail(), userReg.getAddress(), userReg.getPhone());
			Role userRole = roleDAO.findOne(QRole.role1.role.eq("USER"));
			user.getRoles().add(userRole);
			return userDAO.save(user);
		}
	}
}
