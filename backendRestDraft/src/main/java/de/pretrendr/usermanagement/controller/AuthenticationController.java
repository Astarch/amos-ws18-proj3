package de.pretrendr.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.boot.git.GitRepositoryState;
import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.User;
import de.pretrendr.usermanagement.model.pojo.RegUser;

/*****
 * 
 * @author Tristan Schneider
 */
@RequestMapping("/auth")
@RestController
@CrossOrigin
public class AuthenticationController {
	private final UserService userService;
	private final UserDAO userDAO;
	private GitRepositoryState gitRepositoryState;

	/**
	 * Autowired constructor.
	 *
	 * @param userService
	 * @param userDAO
	 * @author Tristan Schneider
	 */
	@Autowired
	public AuthenticationController(final UserService userService, final UserDAO userDAO,
			GitRepositoryState gitRepositoryState) {
		this.userService = userService;
		this.userDAO = userDAO;
		this.gitRepositoryState = gitRepositoryState;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 * @author Tristan Schneider
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

	/**
	 * @param username
	 * @param password
	 * @return
	 * @author Tristan Schneider
	 */
	@RequestMapping(value = "/version", method = RequestMethod.GET)
	public ResponseEntity<GitRepositoryState> version() {
		return new ResponseEntity<GitRepositoryState>(gitRepositoryState, HttpStatus.OK);
	}

}
