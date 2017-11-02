package de.pretrendr.usermanagement.dataccess;

import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.pretrendr.usermanagement.model.User;

public interface UserDAO extends CrudRepository<User, Long>, QueryDslPredicateExecutor<User> {
	List<User> findByFirstname(String firstname);
}
