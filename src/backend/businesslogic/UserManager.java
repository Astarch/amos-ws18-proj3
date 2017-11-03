package backend.businesslogic;

import backend.datamodel.User;
import database.managers.DatabaseUserManager;

public class UserManager {
	
	//Example for Joud und Lara
	public static void main(String[] args){
		//create a connector to Redis
		DatabaseUserManager databaseUserManager = new DatabaseUserManager();
		
		//create test data
		//email = key in Redis
		String email = "" + java.util.UUID.randomUUID() + "@web.com";
		User newUser = new User("surename", "password","firsname",email);
		
		//create an user in Redis
		databaseUserManager.createUser(newUser);
		
		//get an user from Redis
		User getUser = databaseUserManager.getUserbyMail(email);
		
		System.out.println(getUser.toString());
		
		//close connection to Redis
		databaseUserManager.closeConnection();
		
	}
}
