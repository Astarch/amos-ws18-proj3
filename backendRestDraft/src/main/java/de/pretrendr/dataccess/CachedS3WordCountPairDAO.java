package de.pretrendr.dataccess;

import java.util.UUID;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3WordCountPair;

public interface CachedS3WordCountPairDAO
		extends CrudRepository<CachedS3WordCountPair, UUID>, QueryDslPredicateExecutor<CachedS3WordCountPair> {

}
