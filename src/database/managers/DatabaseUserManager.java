package database.managers;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

import backend.datamodel.User;

/**
 * This class will connect Redis to our backend
 * 
 * @author Florian
 *
 */
public class DatabaseUserManager {
	RedisClient redisClient;
	RedisConnection<String, String> connection;
	
	/**
	 * Create a connection to Redis
	 */
	public DatabaseUserManager(){
		redisClient = new RedisClient(
			    RedisURI.create("redis://ec2-18-221-3-38.us-east-2.compute.amazonaws.com:6379"));
		connection = redisClient.connect();
	}
	
	/**
	 * Retrieve an user from the database
	 * If the user does not exist, a null object will be returned
	 * 
	 * @param user
	 * @return an user from the database
	 */
	public User getUserbyUserObject(User user){
		return getUserbyMail(user.getMail());
	}
	
	/**
	 * Retrieve an user from the database
	 * If the user does not exist, a null object will be returned
	 * 
	 * @param userMail
	 * @return an user from the database
	 */
	public User getUserbyMail(String userMail){
		String redisValue = connection.get(userMail);
		if (redisValue == null || redisValue.equals("")) {
			return null;
		}
		String[] redisData = redisValue.split("####");
		
		return new User(redisData[0],redisData[2],redisData[1],userMail);
	}
	
	/**
	 * Create a new user in the database
	 * @param newUser
	 */
	public void createUser(User newUser){
		String redisValue = newUser.getSurename() + "####" + 
				newUser.getFirstname() + "####" + 
				newUser.getPassword();
	    connection.set(newUser.getMail(), redisValue);
	}
	
	/**
	 * Close the connection to Redis
	 */
	public void closeConnection(){
		connection.close();
	    redisClient.shutdown();
	}
}

