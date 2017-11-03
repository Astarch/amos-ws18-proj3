package de.pretrendr;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import de.pretrendr.boot.Application;
import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.controller.UserController;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.dataccess.UserRedisDAO;
import de.pretrendr.usermanagement.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {

	@Mock
	UserService userService;

	@Mock
	UserDAO userDAO;

	@Mock
	UserRedisDAO userRedisDAO;

	UserController userController;

	@Before
	public void init() {
		userController = new UserController(userService, userDAO, userRedisDAO);
	}

	@Test
	public void testExample() {
		// Arrange
		String testUserName = "testusername";

		// Act
		User u = userController.user(testUserName);

		// Assert
		assertEquals(testUserName, u.getUsername());

	}

}
