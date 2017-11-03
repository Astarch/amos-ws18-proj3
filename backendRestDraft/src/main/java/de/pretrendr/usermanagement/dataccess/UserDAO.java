package de.pretrendr.usermanagement.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.usermanagement.model.User;

public interface UserDAO extends CrudRepository<User, Long>, QueryDslPredicateExecutor<User> {
}
