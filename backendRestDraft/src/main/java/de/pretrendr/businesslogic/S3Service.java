package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;

import de.pretrendr.ex.InvalidBucketNameException;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

public interface S3Service {

	List<CachedS3Bucket> getAllBuckets();

	List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId);

	List<CachedS3Object> getAllObjectsByBucketName(String bucketName);

	Page<CachedS3WordCountPair> getWordCountMapByBucketName(String bucketName, int number, int size);

	Page<CachedS3WordCountPair> getWordCountMapByBucketId(UUID bucketId, int number, int size);

	CachedS3Bucket updateCacheByBucket(String bucketName);

	CachedS3Bucket updateCacheByBucket(UUID bucketId);

	@Deprecated
	Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException;

	CachedS3Bucket createBucket(String bucketName) throws InvalidBucketNameException;

	boolean updateAllBuckets();

	boolean deleteBucket(String bucketName);

	boolean deleteBucket(UUID bucketId);
}
