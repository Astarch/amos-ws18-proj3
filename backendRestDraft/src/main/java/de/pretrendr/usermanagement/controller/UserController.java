package de.pretrendr.usermanagement.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.dataccess.UserRedisDAO;
import de.pretrendr.usermanagement.model.User;

/**
 * @author Tristan Schneider
 */
@RestController
public class UserController {

	private final UserService userService;
	private final UserDAO userDAO;
	private final UserRedisDAO userRedisDAO;

	/**
	 * @param userService
	 * @param userDAO
	 * @param userRedisDAO
	 * @author Tristan Schneider
	 */
	@Autowired
	public UserController(UserService userService, UserDAO userDAO, UserRedisDAO userRedisDAO) {
		this.userService = userService;
		this.userDAO = userDAO;
		this.userRedisDAO = userRedisDAO;
	}

	/**
	 * @param id
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping("/user")
	public User user(@RequestParam(value = "name") UUID id) {
		return userDAO.findOne(id);
	}

	/**
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping("/users")
	public Iterable<User> users() {
		return userDAO.findAll();
	}
}