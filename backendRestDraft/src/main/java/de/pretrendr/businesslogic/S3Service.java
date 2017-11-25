package de.pretrendr.businesslogic;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;

import de.pretrendr.ex.InvalidBucketNameException;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

/**
 * Service handling all business logic concerning S3 interaction and caching.
 */
public interface S3Service {

	/**
	 * Retrieves all {@link CachedS3Bucket}s from the database. No specific order or
	 * limits.
	 * 
	 * @return List of all {@link CachedS3Bucket}s in database.
	 */
	List<CachedS3Bucket> getAllBuckets();

	/**
	 * Retrieves all {@link CachedS3Object}s belonging to a {@link CachedS3Bucket}
	 * with the given <i>bucketId</b>
	 * 
	 * @param bucketId
	 *            id of the parent {@link CachedS3Bucket}
	 * @return list of all {@link CachedS3Object} belonging to the given
	 *         {@link CachedS3Bucket}
	 * @throws EntityNotFoundException
	 *             will be thrown, if there is no {@link CachedS3Bucket} with the
	 *             given id
	 */
	List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId) throws EntityNotFoundException;

	/**
	 * Retrieves all {@link CachedS3Object}s belonging to a {@link CachedS3Bucket}
	 * with the given <i>bucketName</b>
	 * 
	 * @param bucketName
	 *            name of the parent {@link CachedS3Bucket}
	 * @return list of all {@link CachedS3Object} belonging to the given
	 *         {@link CachedS3Bucket}
	 * @throws EntityNotFoundException
	 *             will be thrown, if there is no {@link CachedS3Bucket} with the
	 *             given name
	 */
	List<CachedS3Object> getAllObjectsByBucketName(String bucketName) throws EntityNotFoundException;

	/**
	 * Retrieves all word-count-data of a {@link CachedS3Bucket} with the given
	 * name. The results are paged using Spring {@link Page}. Use <i>number</i> to
	 * specify the page number, and <i>size</i> to specify the items-per-page.
	 * 
	 * @param bucketName
	 *            name of the parent {@link CachedS3Bucket}
	 * @param number
	 *            number of mape, beginning with zero
	 * @param size
	 *            items per page
	 * @return {@link Page} containing a partion of the result set, based on
	 *         parameters <i>number</i> and <i>size</i>.
	 * @throws EntityNotFoundException
	 *             will be thrown, if there is no {@link CachedS3Bucket} with the
	 *             given name
	 */
	Page<CachedS3WordCountPair> getWordCountMapByBucketName(String bucketName, int number, int size)
			throws EntityNotFoundException;

	/**
	 * Retrieves all word-count-data of a {@link CachedS3Bucket} with the given id.
	 * The results are paged using Spring {@link Page}. Use <i>number</i> to specify
	 * the page number, and <i>size</i> to specify the items-per-page.
	 * 
	 * @param bucketId
	 *            id of the parent {@link CachedS3Bucket}
	 * @param number
	 *            number of mape, beginning with zero
	 * @param size
	 *            items per page
	 * @return {@link Page} containing a partion of the result set, based on
	 *         parameters <i>number</i> and <i>size</i>.
	 * @throws EntityNotFoundException
	 *             will be thrown, if there is no {@link CachedS3Bucket} with the
	 *             given id
	 */
	Page<CachedS3WordCountPair> getWordCountMapByBucketId(UUID bucketId, int number, int size);

	/**
	 * Updates cached information of a {@link CachedS3Bucket} with the given name.
	 * This will also cascade down to {@link CachedS3Object} and
	 * {@link CachedS3WordCountPair}. This will force an update, even if the objects
	 * modification date has not changed since last cache update.
	 * 
	 * @param bucketName
	 *            name of the {@link CachedS3Bucket}
	 * @return the updated {@link CachedS3Bucket}
	 */
	CachedS3Bucket updateCacheByBucket(String bucketName);

	/**
	 * Updates cached information of a {@link CachedS3Bucket} with the given id.
	 * This will also cascade down to {@link CachedS3Object} and
	 * {@link CachedS3WordCountPair}. This will force an update, even if the objects
	 * modification date has not changed since last cache update.
	 * 
	 * @param bucketId
	 *            id of the {@link CachedS3Bucket}
	 * @return the updated {@link CachedS3Bucket}
	 */
	CachedS3Bucket updateCacheByBucket(UUID bucketId);

	@Deprecated
	Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException;

	/**
	 * Creates a {@link CachedS3Bucket} with the given name. The created Entity will
	 * be returned.
	 * 
	 * @param bucketName
	 *            desired name
	 * @return created {@link CachedS3Bucket}
	 * @throws InvalidBucketNameException
	 *             will be thrown if the given name is not valid concerning AmazonS3
	 *             naming restrictions.
	 */
	CachedS3Bucket createBucket(String bucketName) throws InvalidBucketNameException;

	/**
	 * This will update the cached information of all {@link CachedS3Bucket}. This
	 * will not force an update, so unchanged objects will not be reloaded. Word
	 * count will only be redone, of one or more of the objects have changed.
	 * 
	 * @return true iff successful, else false
	 */
	boolean updateAllBuckets();

	/**
	 * Deletes {@link CachedS3Bucket} based on the given name.
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @return true iff success, else false
	 * @throws EntityNotFoundException
	 *             will be thrown there is no {@link CachedS3Bucket} with the given
	 *             name
	 */
	boolean deleteBucket(String bucketName) throws EntityNotFoundException;

	/**
	 * Deletes {@link CachedS3Bucket} based on the given id.
	 * 
	 * @param bucketId
	 *            id of the bucket
	 * @return true iff success, else false
	 * @throws EntityNotFoundException
	 *             will be thrown there is no {@link CachedS3Bucket} with the given
	 *             id
	 */
	boolean deleteBucket(UUID bucketId);
}
