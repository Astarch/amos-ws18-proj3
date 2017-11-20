package de.pretrendr.dataccess;

import java.util.UUID;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.User;

/**
 * @author Tristan Schneider
 */
public interface UserDAO extends CrudRepository<User, UUID>, QueryDslPredicateExecutor<User> {
}
