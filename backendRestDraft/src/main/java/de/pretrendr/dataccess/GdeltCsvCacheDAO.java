package de.pretrendr.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3WordCountPair;
import de.pretrendr.model.GdeltCsvCache;
import de.pretrendr.model.CachedS3WordCountPair.CachedS3WordCountPairId;

public interface GdeltCsvCacheDAO
		extends CrudRepository<GdeltCsvCache, String>, QueryDslPredicateExecutor<GdeltCsvCache>,
		PagingAndSortingRepository<GdeltCsvCache, String> {

}
