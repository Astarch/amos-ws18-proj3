package de.pretrendr.usermanagement.dataccess;

import java.util.Map;

import de.pretrendr.usermanagement.model.User;

public interface UserRedisDAO {

	Map<Object, User> findAllUsers();

	void saveUser(User user);

	void updateUser(User user);

	User findUser(String id);

	void deleteUser(String id);

	User getUserbyMail(String userMail);
}