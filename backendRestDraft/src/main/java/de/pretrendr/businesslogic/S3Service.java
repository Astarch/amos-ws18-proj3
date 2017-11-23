package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

public interface S3Service {
	public List<CachedS3WordCountPair> getWordCountMapByBucket(String bucketName) throws IOException;

	public List<CachedS3Bucket> getAllBuckets();

	public List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId);

	public List<CachedS3Object> getAllObjectsByBucket(String bucketName);

	public List<CachedS3WordCountPair> getWordCountMapByBucket(UUID bucketId);

	public boolean updateCacheByBucket(String bucketName);

	public boolean updateCacheByBucket(UUID bucketId);

	void updateAllBuckets();
}
