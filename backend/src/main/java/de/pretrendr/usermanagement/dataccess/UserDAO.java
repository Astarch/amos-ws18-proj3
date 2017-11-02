package de.pretrendr.usermanagement.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import de.pretrendr.usermanagement.model.User;

@Repository
public interface UserDAO extends QueryDslPredicateExecutor<User> {

}
