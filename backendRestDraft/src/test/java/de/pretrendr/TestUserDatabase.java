package de.pretrendr;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import de.pretrendr.boot.Application;
import de.pretrendr.usermanagement.dataccess.UserRedisDAO;
import de.pretrendr.usermanagement.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
// @ActiveProfiles("test")
@WebAppConfiguration
public class TestUserDatabase {
	@Autowired
	UserRedisDAO redisConnector;

	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// redisConnector = new RedisConnector();
	// }

	// @AfterClass
	// public static void tearDownAfterClass() throws Exception {
	// redisConnector.closeConnection();
	// }

	@Test
	public void createAndGetNewUser() {
		String userMail = "" + java.util.UUID.randomUUID() + "@web.com";
		User newUser = new User("Musterman", "password", "Max", "Mustermann", userMail, "address", "+49 123 phone");
		redisConnector.saveUser(newUser);

		User getUser = redisConnector.getUserbyMail(userMail);
		equals(userMail.equals(getUser.getEmail()));
	}

	@Test
	public void userNotExist() {
		equals(redisConnector.getUserbyMail("userNotExist") == null);
	}

	@Test
	public void mailIsEmpty() {
		equals(redisConnector.getUserbyMail("userNotExist") == null);
	}
}
