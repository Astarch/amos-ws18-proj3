package DatabaseTests;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

/**
 * Test for Redis: check the database connection
 * 
 * @author Florian
 *
 */
public class RedisTest {
	
	//Test for Redis (User Management)
	public static void main(String[] args) {
		//Documentation: https://redis.io/topics/quickstart
		//			     https://redislabs.com/lp/redis-java/
		
		//Create a connection to redis
		RedisClient redisClient = new RedisClient(
	    RedisURI.create("redis://ec2-18-220-147-20.us-east-2.compute.amazonaws.com:6379"));
		
		//<String, String> is the data structure; if necessary, please change
	    RedisConnection<String, String> connection = redisClient.connect();
	    System.out.println("Connected to Redis (NoSQL Database/User Management)");
	    
	    //Create a testuser in redis
	    if (connection.get("Admin") != null) {
	    	System.out.println("The user Admin already exists!");
		} else {
			String newUserID = "Admin";
			String password  = "password";
		    connection.set(newUserID, password);
		    System.out.println("Admin successfully stored in Redis");
		}
	    
		//Read the user's password from the database
	    System.out.println("Password from Admin: " + connection.get("Admin"));
	
	    //Close connection to redis
	    connection.close();
	    redisClient.shutdown();
	    System.out.println("Connection to Redis closed");
	}
}
