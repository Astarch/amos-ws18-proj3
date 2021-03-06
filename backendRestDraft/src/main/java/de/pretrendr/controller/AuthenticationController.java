package de.pretrendr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.businesslogic.UserService;
import de.pretrendr.dataccess.UserDAO;
import de.pretrendr.model.User;
import de.pretrendr.model.pojo.RegUser;

/*****
 * 
 * @author Tristan Schneider
 */
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	private final UserService userService;
	private final UserDAO userDAO;

	/**
	 * Autowired constructor.
	 * 
	 * @param userService
	 *            autowired UserService
	 * @param userDAO
	 *            autowired UserDAO
	 */
	@Autowired
	public AuthenticationController(final UserService userService, final UserDAO userDAO) {
		this.userService = userService;
		this.userDAO = userDAO;
	}

	/**
	 * Creates a user based on the information provided in the given {@link RegUser}
	 * POJO.
	 * 
	 * @param userReg
	 *            POJO containing user information like username, password, email
	 *            etc
	 * @return {@link ResponseEntity}
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<User> register(@RequestBody final RegUser userReg) {
		User user = userService.register(userReg);
		if (user != null) {
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.FOUND);
		}
	}
}
