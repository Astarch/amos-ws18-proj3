package de.pretrendr.usermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.model.User;

@RestController
public class LoginController {
	@RequestMapping("/login")
	public String login() {
		return "Use method POST instead.";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public User loginPost(@RequestParam(value = "name") String userName, @RequestParam(value = "passwd") String password) {
		// return Dummy
		return new User(userName, password, "firstname", "lastname", "email", "address", "phone");
	}
}
