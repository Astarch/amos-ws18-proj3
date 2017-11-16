package de.pretrendr;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.pretrendr.boot.Application;
import de.pretrendr.usermanagement.businesslogic.UserService;
import de.pretrendr.usermanagement.controller.UserController;
import de.pretrendr.usermanagement.dataccess.UserDAO;
import de.pretrendr.usermanagement.model.Role;
import de.pretrendr.usermanagement.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {

	@Mock
	UserDAO userDAO;
	@Autowired
	UserService userService;

	/**
	 * Object under test.
	 */
	UserController userController;

	@Before
	public void init() {
		userController = new UserController(userService, userDAO);
	}

	@Test
	public void testGetUser() {
		// Arrange
		UUID userId = UUID.randomUUID();
		User user = new User(userId, "username", "password", "firstname", "lastname", "email@test.de", "address",
				"phone", Sets.newHashSet(new Role(UUID.randomUUID(), "USER")));
		Mockito.when(userDAO.findOne(userId)).thenReturn(user);

		// Act
		ResponseEntity<User> responseEntity = userController.user(userId);

		// Assert
		testUserValues(user, responseEntity.getBody());
	}

	private void testUserValues(User expected, User actual) {
		assertEquals(expected.getAddress(), actual.getAddress());
		assertEquals(expected.getEmail(), actual.getEmail());
		assertEquals(expected.getFirstname(), actual.getFirstname());
		assertEquals(expected.getId(), actual.getId());
		assertEquals(expected.getLastname(), actual.getLastname());
		assertEquals(expected.getPassword(), actual.getPassword());
		assertEquals(expected.getPhone(), actual.getPhone());
		assertEquals(expected.getRoles(), actual.getRoles());
		assertEquals(expected.getUsername(), actual.getUsername());
	}

	@Test
	public void testGetAllUsers() {
		// Arrange
		UUID userId1 = UUID.randomUUID();
		UUID userId2 = UUID.randomUUID();
		User user1 = new User(userId1, "username1", "password1", "firstname1", "lastname1", "email@test.de1",
				"address1", "phone1", Sets.newHashSet(new Role(UUID.randomUUID(), "USER")));
		User user2 = new User(userId2, "username2", "password2", "firstname2", "lastname2", "email@test.de2",
				"address2", "phone2", Sets.newHashSet(new Role(UUID.randomUUID(), "ADMIN")));
		List<User> list = Lists.newArrayList(user1, user2);
		Mockito.when(userDAO.findAll()).thenReturn(list);

		// Act
		ResponseEntity<Iterable<User>> responseEntity = userController.users();

		// Assert
		Iterator<User> iter = responseEntity.getBody().iterator();
		int i = 0;
		while (iter.hasNext()) {
			User actual = iter.next();
			testUserValues(list.get(i), actual);
			i++;
		}
		assertEquals(list.size(), i);
	}
}
