package de.pretrendr.usermanagement.businesslogic;

public interface UserService {
	boolean checkCredentials(String username, String password);
}
