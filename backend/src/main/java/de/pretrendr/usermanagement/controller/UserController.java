package de.pretrendr.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.User;

@RestController
public class UserController {

	private final UserService userService;
	private final UserDAO userDAO;

	@Autowired
	public UserController(UserService userService, UserDAO userDAO) {
		this.userService = userService;
		this.userDAO = userDAO;
	}

	@RequestMapping("/user")
	public User user(@RequestParam(value = "name", defaultValue = "World") String name) {

		return new User("username", "password", "firstname", "lastname", "email", "address", "phone");
	}

	@RequestMapping("/users")
	public Iterable<User> users() {
		return userDAO.findAll();
	}
}