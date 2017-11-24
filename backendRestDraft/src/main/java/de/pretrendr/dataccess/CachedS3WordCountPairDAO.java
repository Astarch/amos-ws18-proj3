package de.pretrendr.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3WordCountPair;
import de.pretrendr.model.CachedS3WordCountPair.CachedS3WordCountPairId;

public interface CachedS3WordCountPairDAO extends CrudRepository<CachedS3WordCountPair, CachedS3WordCountPairId>,
		QueryDslPredicateExecutor<CachedS3WordCountPair> {

}