package BackendTest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import databaseConnectors.RedisConnector;
import datamodelBackend.User;

public class TestUserDatabase {
	static RedisConnector redisConnector;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		redisConnector = new RedisConnector();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		redisConnector.closeConnection();
	}

	@Test
	public void createAndGetNewUser() {
		String userMail = "" + java.util.UUID.randomUUID() + "@web.com";
		User newUser = new User("Musterman", "password","Max",userMail);
		redisConnector.createUser(newUser);
		
		User getUser = redisConnector.getUserbyMail(userMail);
		equals(userMail.equals(getUser.getMail()));
	}
	
	@Test
	public void userNotExist() {
		equals(redisConnector.getUserbyMail("userNotExist")==null);
	}
	
	@Test
	public void mailIsEmpty() {
		equals(redisConnector.getUserbyMail("userNotExist")==null);
	}
}
