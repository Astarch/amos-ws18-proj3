package de.pretrendr.dataccess;

import java.util.UUID;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3Object;

public interface CachedS3ObjectDAO
		extends CrudRepository<CachedS3Object, UUID>, QueryDslPredicateExecutor<CachedS3Object> {

}
