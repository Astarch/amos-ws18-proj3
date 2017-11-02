package de.pretrendr.usermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.usermanagement.model.User;

@RestController
public class UserController {

	@RequestMapping("/user")
	public User greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new User("username", "password", "firstname", "lastname", "email", "address", "phone");
	}
}