package de.pretrendr.usermanagement.dataccess;

import java.util.UUID;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.usermanagement.model.Role;

public interface RoleDAO extends CrudRepository<Role, UUID>, QueryDslPredicateExecutor<Role> {

}
