package de.pretrendr.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import de.pretrendr.model.CachedS3WordCountPair;
import de.pretrendr.model.CachedS3WordCountPair.CachedS3WordCountPairId;

/**
 * JPA DAO for {@link CachedS3WordCountPair}, managed by Spring.
 * 
 * @author Tristan Schneider
 *
 */
public interface CachedS3WordCountPairDAO extends CrudRepository<CachedS3WordCountPair, CachedS3WordCountPairId>,
		QueryDslPredicateExecutor<CachedS3WordCountPair>,
		PagingAndSortingRepository<CachedS3WordCountPair, CachedS3WordCountPairId> {

}