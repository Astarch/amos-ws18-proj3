package de.pretrendr.usermanagement.dataccess;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import de.pretrendr.usermanagement.model.User;

@Repository
public class UserRedisDAOImpl implements UserRedisDAO {

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations hashOperations;

	@Autowired
	public UserRedisDAOImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public Map<Object, User> findAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public User findUser(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUserbyMail(String userMail) {
		String redisValue = (String) hashOperations.get("USER", userMail);
		if (redisValue == null || redisValue.equals("")) {
			return null;
		}
		String[] redisData = redisValue.split("####");

		return new User(redisData[0], redisData[2], redisData[1], userMail, null, null, null);
	}
}