package de.pretrendr.usermanagement.businesslogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.pretrendr.usermanagement.dataccess.UserDAO;
//import de.pretrendr.usermanagement.dataccess.UserDAO;
//import de.pretrendr.usermanagement.model.QUser;
import de.pretrendr.usermanagement.model.User;

@Service
public class UserServiceImpl implements UserService {
//	private final UserDAO userDAO;
//
//	@Autowired
//	public UserServiceImpl(final UserDAO userDAO) {
//		this.userDAO = userDAO;
//	}

	@Override
	public boolean checkCredentials(String username, String password) {
		User user = new User(username, password, "John", "Doe", "john.doe@jd.com", "Generic Street 123, Dummytown",
				"+123/123123123");
		// User user = userDAO.findOne(QUser.user.username.eq(username));
		return user != null && user.getPassword().equals(password);
	}
}
