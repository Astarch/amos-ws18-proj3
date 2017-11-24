package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.pretrendr.ex.InvalidBucketNameException;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

public interface S3Service {
	List<CachedS3WordCountPair> getWordCountMapByBucket(String bucketName) throws IOException;

	List<CachedS3Bucket> getAllBuckets();

	List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId);

	List<CachedS3Object> getAllObjectsByBucketName(String bucketName);

	List<CachedS3WordCountPair> getWordCountMapByBucket(UUID bucketId);

	CachedS3Bucket updateCacheByBucket(String bucketName);

	CachedS3Bucket updateCacheByBucket(UUID bucketId);

	boolean updateAllBuckets();

	Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException;

	CachedS3Bucket createBucket(String bucketName) throws InvalidBucketNameException;

	boolean deleteBucket(String bucketName);

	boolean deleteBucket(UUID bucketId);
}
