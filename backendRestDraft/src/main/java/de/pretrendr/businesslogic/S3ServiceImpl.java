package de.pretrendr.businesslogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Maps;

import de.pretrendr.dataccess.CachedS3BucketDAO;
import de.pretrendr.dataccess.CachedS3ObjectDAO;
import de.pretrendr.dataccess.CachedS3WordCountPairDAO;
import de.pretrendr.ex.InvalidBucketNameException;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;
import de.pretrendr.model.QCachedS3Bucket;
import de.pretrendr.model.QCachedS3WordCountPair;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@Getter
@Setter
public class S3ServiceImpl implements S3Service {
	@Autowired
	final private AmazonS3 s3;

	private CachedS3BucketDAO cachedS3BucketDAO;
	private CachedS3ObjectDAO cachedS3ObjectDAO;
	private CachedS3WordCountPairDAO cachedS3WordCountPairDAO;

	private final static String ERR_MSG_404_BUCKET = "Bucket '{1}' could not be found.";

	@Autowired
	public S3ServiceImpl(AmazonS3 s3, CachedS3BucketDAO cachedS3BucketDAO, CachedS3ObjectDAO cachedS3ObjectDAO,
			CachedS3WordCountPairDAO cachedS3WordCountPairDAO) {
		this.s3 = s3;
		this.cachedS3BucketDAO = cachedS3BucketDAO;
		this.cachedS3ObjectDAO = cachedS3ObjectDAO;
		this.cachedS3WordCountPairDAO = cachedS3WordCountPairDAO;

	}

	@Override
	public List<CachedS3Bucket> getAllBuckets() {
		return Lists.newArrayList(cachedS3BucketDAO.findAll());
	}

	@Override
	public Page<CachedS3WordCountPair> getWordCount(UUID bucketId, int number, int size) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketId));
		}
		Page<CachedS3WordCountPair> wordCountPairs = cachedS3WordCountPairDAO.findAll(
				QCachedS3WordCountPair.cachedS3WordCountPair.bucket.id.eq(bucketId),
				new PageRequest(number, size, new Sort(Sort.Direction.DESC, "count")));
		return wordCountPairs;
	}

	@Override
	public List<CachedS3Object> getAllObjects(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketId));
		}
		return Lists.newArrayList(bucket.getObjects());
	}

	@Override
	public CachedS3Bucket updateCache(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketId));
		}
		updateCache(bucket, true);
		return bucket;
	}

	@Transactional
	private boolean updateCache(CachedS3Bucket bucket, boolean forceUpdate) {
		if (bucket.isStillAvailable() || forceUpdate) {
			log.debug(MessageFormat.format("CacheUpdate: {0}: Updating with forceUpdate={1}", bucket.getName(),
					forceUpdate));

			boolean doWordCount = false;
			Map<String, Integer> wordCounts = Maps.newHashMap();

			List<CachedS3Object> unvisitedObjects = Lists.newArrayList(bucket.getObjects());

			try {
				ObjectListing ol = s3.listObjects(bucket.getName());
				log.debug(MessageFormat.format("CacheUpdate: {0}: Found bucket in Amazon S3.", bucket.getName()));
				List<S3ObjectSummary> objects = ol.getObjectSummaries();
				log.debug(MessageFormat.format("CacheUpdate: {0}: Checking {1} objects now.", bucket.getName(),
						ol.getObjectSummaries().size()));
				for (S3ObjectSummary os : objects) {
					CachedS3Object cachedS3Object = null;
					try {
						cachedS3Object = bucket.getObjects().stream().filter(o -> o.getName().equals(os.getKey()))
								.findFirst().get();
						unvisitedObjects.remove(cachedS3Object);
						if (forceUpdate || cachedS3Object.getLastModified() == null
								|| new DateTime(os.getLastModified()).isAfter(cachedS3Object.getLastModified())) {
							log.debug(MessageFormat.format("CacheUpdate: {0}: Object {1} exists and will be updated.",
									bucket.getName(), os.getKey()));
							cachedS3Object.setLastModified(DateTime.now());
							cachedS3Object = cachedS3ObjectDAO.save(cachedS3Object);
							doWordCount = true;
						} else {
							log.debug(
									MessageFormat.format("CacheUpdate: {0}: Object {1} exists and is still up-to-date.",
											bucket.getName(), os.getKey()));
						}
					} catch (Exception e) {
						log.debug(MessageFormat.format("CacheUpdate: {0}: Object {1} does not exist. Will be created.",
								bucket.getName(), os.getKey()));
						cachedS3Object = new CachedS3Object(bucket, os.getKey(), DateTime.now(), DateTime.now());
						cachedS3Object = cachedS3ObjectDAO.save(cachedS3Object);
						bucket.getObjects().add(cachedS3Object);
						doWordCount = true;
					}
				}
				bucket.getObjects().removeAll(unvisitedObjects);
				log.debug(MessageFormat.format(
						"CacheUpdate: {0}: This leads to a total of {2} objects in cache. (Outdated and removed: {1})",
						bucket.getName(), unvisitedObjects.size(), bucket.getObjects().size()));
				cachedS3ObjectDAO.delete(unvisitedObjects);

				if (doWordCount) {
					int count = 0;
					log.debug(MessageFormat.format("CacheUpdate: {0}: Starting with wordcount.", bucket.getName()));
					for (S3ObjectSummary os : objects) {
						try {
							log.debug(MessageFormat.format("CacheUpdate: {0}: Parsing file {1}.", bucket.getName(),
									os.getKey()));
							S3Object s3Object = s3.getObject(bucket.getName(), os.getKey());
							S3ObjectInputStream inputStream = s3Object.getObjectContent();
							File file = new File(os.getKey());
							FileUtils.copyInputStreamToFile(inputStream, file);
							try (BufferedReader br = new BufferedReader(new FileReader(file))) {
								String line;
								int innercount = 0;
								while ((line = br.readLine()) != null) {
									String[] wordAndCount = line.split("\t");
									if (wordAndCount.length == 2) {
										wordCounts.put(wordAndCount[0], Integer.parseInt(wordAndCount[1]));
										innercount++;
									} else {
										log.debug(MessageFormat.format(
												"CacheUpdate: {0}: Could not parse S3 entry:'{1}' in bucket:'{2}' file:'{3}'",
												bucket.getName(), line, bucket.getName(), os.getKey()));
									}
								}
								count += innercount;
								log.debug(MessageFormat.format(
										"CacheUpdate: {0}: Parsing file {1} resulted in {2} pairs. {3} in total",
										bucket.getName(), os.getKey(), innercount, count));
							}

							FileUtils.forceDelete(file);
						} catch (IOException e) {
							log.error(MessageFormat.format(
									"CacheUpdate: {0}: Could not work with file while updating cache of object {1}",
									bucket.getName(), os.getKey()));
						}
					}
					bucket.getWordCount().clear();
					for (String word : wordCounts.keySet()) {
						bucket.getWordCount().add(new CachedS3WordCountPair(bucket, word, wordCounts.get(word)));
					}
					bucket.setStillAvailable(true);
					bucket.setLastModified(DateTime.now());
					log.debug(MessageFormat.format("CacheUpdate: {0}: All files parsed. {1} pairs in total.",
							bucket.getName(), count));
				}
			} catch (AmazonS3Exception s3e) {
				log.debug(MessageFormat.format("CacheUpdate: {0}: Bucket is no longer available on Amazon S3.",
						bucket.getName()));
				bucket.setStillAvailable(false);
				bucket.setLastModified(DateTime.now());
			}
			log.debug(MessageFormat.format("CacheUpdate: {0}: Updating database.", bucket.getName()));
			cachedS3BucketDAO.save(bucket);
			log.info(MessageFormat.format("CacheUpdate: {0}: Bucket is now up-to-date.", bucket.getName()));
		} else {
			log.info(MessageFormat.format(
					"CacheUpdate: {0}: Bucket is skipped, because it was not available on Amazon S3 during last cache update.",
					bucket.getName()));
		}

		return true;
	}

	@Override
	@Transactional
	public boolean updateAllBuckets() {
		boolean result = true;
		Iterable<CachedS3Bucket> buckets = cachedS3BucketDAO.findAll();
		for (Iterator<CachedS3Bucket> iter = buckets.iterator(); iter.hasNext();) {
			CachedS3Bucket bucket = iter.next();
			result = result && updateCache(bucket, false);
		}
		return result;
	}

	@Override
	public CachedS3Bucket createBucket(String bucketName) throws InvalidBucketNameException {
		validateBucketName(bucketName);
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(QCachedS3Bucket.cachedS3Bucket.name.eq(bucketName));
		if (bucket != null) {
			throw new EntityExistsException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketName));
		}
		bucket = new CachedS3Bucket(bucketName);
		cachedS3BucketDAO.save(bucket);
		return bucket;
	}

	/**
	 * Validates the given name based on Amazons rules.
	 * // @formatter:off
	 * Bucket names must be at least 3 and no more than 63 characters long.
	 * Bucket names must be a series of one or more labels. Adjacent labels are separated by a single period (.). 
	 * Bucket names can contain lowercase letters, numbers, and hyphens. 
	 * // @formatter:on
	 * @param bucketName Desired name of the bucket.
	 * @throws InvalidBucketNameException will be thrown if the name does not apply the rules above.
	 */
	private void validateBucketName(String bucketName) throws InvalidBucketNameException {
		if (bucketName.length() < 3 || bucketName.length() > 63 || bucketName.startsWith(".")
				|| bucketName.endsWith(".") || bucketName.contains("..") || !bucketName.matches("[0-9a-z-]*")) {
			throw new InvalidBucketNameException();
		}
	}

	@Override
	public boolean deleteBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketId));
		}
		cachedS3BucketDAO.delete(bucket);
		return true;
	}

	@Override
	public CachedS3Bucket getBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			if (bucket == null) {
				throw new EntityNotFoundException(MessageFormat.format(ERR_MSG_404_BUCKET, bucketId));
			}
		}
		return bucket;
	}

}