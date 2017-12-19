package de.pretrendr.dataccess;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.GdeltCsvCache;

public interface GdeltCsvCacheDAO extends CrudRepository<GdeltCsvCache, String>, QueryDslPredicateExecutor<GdeltCsvCache> {

}
