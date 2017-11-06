// package de.pretrendr.usermanagement.controller;
//
// import javax.servlet.http.HttpSession;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
//
// import de.pretrendr.usermanagement.businesslogic.UserService;
// import de.pretrendr.usermanagement.dataccess.UserDAO;
// import de.pretrendr.usermanagement.model.User;
//
/// **
// * @author Tristan Schneider
// */
// @RequestMapping("/auth")
// @RestController
// public class AuthenticationController {
// private final UserService userService;
// private final UserDAO userDAO;
//
// /**
// * Autowired constructor.
// *
// * @param userService
// * @param userDAO
// * @author Tristan Schneider
// */
// @Autowired
// public AuthenticationController(final UserService userService, final UserDAO
// userDAO) {
// this.userService = userService;
// this.userDAO = userDAO;
// }
//
// /**
// * @param username
// * @param password
// * @return
// * @author Tristan Schneider
// */
// @RequestMapping(value = "/login", method = RequestMethod.GET)
// public ResponseEntity<User> loginPost(@RequestParam(value = "name") String
// username,
// @RequestParam(value = "passwd") String password) {
// // return Dummy
// return new ResponseEntity<User>(
// new User(username, password, "firstname", "lastname", "email", "address",
// "phone"), HttpStatus.OK);
// }
//
// @RequestMapping(value = "/logout", method = RequestMethod.GET)
// public ResponseEntity<Boolean> logout(HttpSession session) {
// // return Dummy
// session.invalidate();
// return new ResponseEntity<Boolean>(true, HttpStatus.OK);
// }
// }
