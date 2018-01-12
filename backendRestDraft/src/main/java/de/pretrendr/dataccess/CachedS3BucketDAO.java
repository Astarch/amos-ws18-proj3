package de.pretrendr.dataccess;

import java.util.UUID;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3Bucket;

public interface CachedS3BucketDAO
		extends CrudRepository<CachedS3Bucket, UUID>, QueryDslPredicateExecutor<CachedS3Bucket> {

}
