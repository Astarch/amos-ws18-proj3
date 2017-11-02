package de.pretrendr.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.User;

@RestController
public class LoginController {
	private final UserService userService;

	@Autowired
	public LoginController(final UserService userService) {
		this.userService = userService;
	}

//	@RequestMapping("/login")
//	public String login() {
//		return "Use method POST instead.";
//	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public User loginGet(@RequestParam(value = "name") String username,
			@RequestParam(value = "passwd") String password) {
		// return Dummy
		if(userService.checkCredentials(username, password)) {
			return new User(username, password, "firstname", "lastname", "email", "address", "phone");
		}
		return null;
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public User loginPost(@RequestParam(value = "name") String username,
			@RequestParam(value = "passwd") String password) {
		// return Dummy
		return new User(username, password, "firstname", "lastname", "email", "address", "phone");
	}
}
