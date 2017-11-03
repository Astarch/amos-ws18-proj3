package de.pretrendr.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.QUser;
import de.pretrendr.usermanagement.model.User;

/**
 * @author Tristan Schneider
 */
@RestController
public class LoginController {
	private final UserService userService;
	private final UserDAO userDAO;

	/**
	 * Autowired constructor.
	 * 
	 * @param userService
	 * @param userDAO
	 * @author Tristan Schneider
	 */
	@Autowired
	public LoginController(final UserService userService, final UserDAO userDAO) {
		this.userService = userService;
		this.userDAO = userDAO;
	}

	// @RequestMapping("/login")
	// public String login() {
	// return "Use method POST instead.";
	// }

	/**
	 * @param username
	 * @param password
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public User loginGet(@RequestParam(value = "name") String username,
			@RequestParam(value = "passwd") String password) {
		// return Dummy
		User user = null;
		if (userService.checkCredentials(username, password)) {
			user = userDAO.findOne(QUser.user.username.eq(username).and(QUser.user.password.eq(password)));
		}
		return user;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public User loginPost(@RequestParam(value = "name") String username,
			@RequestParam(value = "passwd") String password) {
		// return Dummy
		return new User(username, password, "firstname", "lastname", "email", "address", "phone");
	}
}
