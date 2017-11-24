package de.pretrendr.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3Object.CachedS3ObjectId;

public interface CachedS3ObjectDAO
		extends CrudRepository<CachedS3Object, CachedS3ObjectId>, QueryDslPredicateExecutor<CachedS3Object> {

}