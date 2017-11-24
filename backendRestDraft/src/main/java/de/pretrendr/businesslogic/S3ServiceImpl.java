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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public S3ServiceImpl(AmazonS3 s3, CachedS3BucketDAO cachedS3BucketDAO, CachedS3ObjectDAO cachedS3ObjectDAO,
			CachedS3WordCountPairDAO cachedS3WordCountPairDAO) {
		this.s3 = s3;
		this.cachedS3BucketDAO = cachedS3BucketDAO;
		this.cachedS3ObjectDAO = cachedS3ObjectDAO;
		this.cachedS3WordCountPairDAO = cachedS3WordCountPairDAO;

	}

	@Override
	public Map<String, Integer> getWordCountMapFromBucketName(String bucket_name) throws IOException {
		Map<String, Integer> wordCounts = Maps.newHashMap();
		ObjectListing ol = s3.listObjects(bucket_name);
		List<S3ObjectSummary> objects = ol.getObjectSummaries(); // summary, contains names of files
		for (S3ObjectSummary os : objects) {
			S3Object s3object = s3.getObject(bucket_name, os.getKey()); // object, basically one of the files
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			File file = new File(os.getKey()); // actual file on local drive
			FileUtils.copyInputStreamToFile(inputStream, file); // save streamed data to file
			try (BufferedReader br = new BufferedReader(new FileReader(file))) { // reading the file again
				String line;
				while ((line = br.readLine()) != null) { // parsing word->count line by line
					String[] wordAndCount = line.split("\t");
					if (wordAndCount.length == 2) {
						wordCounts.put(wordAndCount[0], Integer.parseInt(wordAndCount[1]));
					} else {
						log.error(MessageFormat.format("Could not parse S3 entry: {0}", line));
					}
				}
			}
			FileUtils.forceDelete(file);
		}
		return wordCounts;
	}

	@Override
	public List<CachedS3Bucket> getAllBuckets() {
		return Lists.newArrayList(cachedS3BucketDAO.findAll());
	}

	@Override
	public List<CachedS3WordCountPair> getWordCountMapByBucket(String bucketName) throws IOException {
		CachedS3Bucket bucket = cachedS3BucketDAO
				.findOne(QCachedS3Bucket.cachedS3Bucket.name.equalsIgnoreCase(bucketName));
		if (bucket != null) {
			return Lists.newArrayList(bucket.getWordCount());
		} else {
			throw new EntityNotFoundException(
					MessageFormat.format("{0} with name {1} could not be found.", "Bucket", bucketName));
		}
	}

	@Override
	public List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format("Bucket with id {0} could not be found.", bucketId));
		}
		return Lists.newArrayList(bucket.getObjects());
	}

	@Override
	public List<CachedS3Object> getAllObjectsByBucketName(String bucketName) {
		CachedS3Bucket bucket = cachedS3BucketDAO
				.findOne(QCachedS3Bucket.cachedS3Bucket.name.equalsIgnoreCase(bucketName));
		if (bucket == null) {
			throw new EntityNotFoundException(
					MessageFormat.format("Bucket with name {0} could not be found.", bucketName));
		}
		return getAllObjectsByBucketId(bucket.getId());

	}

	@Override
	public List<CachedS3WordCountPair> getWordCountMapByBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format("Bucket with id {0} could not be found.", bucketId));
		}
		return Lists.newArrayList(bucket.getWordCount());
	}

	@Override
	public CachedS3Bucket updateCacheByBucket(String bucketName) {
		CachedS3Bucket bucket = cachedS3BucketDAO
				.findOne(QCachedS3Bucket.cachedS3Bucket.name.equalsIgnoreCase(bucketName));
		if (bucket == null) {
			throw new EntityNotFoundException(
					MessageFormat.format("Bucket with name {0} could not be found.", bucketName));
		}
		updateCache(bucket, true);
		return bucket;
	}

	@Override
	public CachedS3Bucket updateCacheByBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			throw new EntityNotFoundException(MessageFormat.format("Bucket with id {0} could not be found.", bucketId));
		}
		updateCache(bucket, true);
		return bucket;
	}

	@Transactional
	private boolean updateCache(CachedS3Bucket bucket, boolean forceUpdate) {
		log.debug(MessageFormat.format("Updating cache for {0}, force update is {1}", bucket.getName(), forceUpdate));
		boolean doWordCount = false;
		Map<String, Integer> wordCounts = Maps.newHashMap();

		List<CachedS3Object> unvisitedObjects = Lists.newArrayList(bucket.getObjects());

		try {
			ObjectListing ol = s3.listObjects(bucket.getName());
			log.debug("Found bucket in Amazon S3.");
			List<S3ObjectSummary> objects = ol.getObjectSummaries();
			log.debug(MessageFormat.format("Checking {0} objects now.", ol.getObjectSummaries().size()));
			for (S3ObjectSummary os : objects) {
				CachedS3Object cachedS3Object = null;
				try {
					cachedS3Object = bucket.getObjects().stream().filter(o -> o.getName().equals(os.getKey()))
							.findFirst().get();
					log.debug(MessageFormat.format("Object {0} exists.", os.getKey()));
					unvisitedObjects.remove(cachedS3Object);
					if (forceUpdate || cachedS3Object.getLastModified() == null
							|| new DateTime(os.getLastModified()).isAfter(cachedS3Object.getLastModified())) {
						cachedS3Object.setLastModified(DateTime.now());
						cachedS3Object = cachedS3ObjectDAO.save(cachedS3Object);
						doWordCount = true;
					}
				} catch (Exception e) {
					log.debug(MessageFormat.format("Object {0} does not exist. Will be created.", os.getKey()));
					cachedS3Object = new CachedS3Object(bucket, os.getKey(), DateTime.now(), DateTime.now());
					cachedS3Object = cachedS3ObjectDAO.save(cachedS3Object);
					bucket.getObjects().add(cachedS3Object);
					doWordCount = true;
				}
			}
			bucket.getObjects().removeAll(unvisitedObjects);
			log.debug(MessageFormat.format(
					"{0} objects are no longer needed. Will be removed. This leads to a total of {1} objects in cache.",
					unvisitedObjects.size(), bucket.getObjects().size()));
			cachedS3ObjectDAO.delete(unvisitedObjects);

			if (doWordCount) {
				int count = 0;
				log.debug("Starting with wordcount now.");
				for (S3ObjectSummary os : objects) {
					try {
						log.debug(MessageFormat.format("Parsing file {0}...", os.getKey()));
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
									log.error(MessageFormat.format(
											"Could not parse S3 entry:'{0}' in bucket:'{1}' file:'{2}'", line,
											bucket.getName(), os.getKey()));
								}
							}
							count += innercount;
							log.debug(MessageFormat.format("Parsing file {0} resulted in {1} pairs. {2} in total",
									os.getKey(), innercount, count));
						}

						FileUtils.forceDelete(file);
					} catch (IOException e) {
						log.error(MessageFormat.format(
								"Could not work with file while updating cache for bucket {0} and object {1}",
								bucket.getName(), os.getKey()));
					}
				}
				bucket.getWordCount().clear();
				for (String word : wordCounts.keySet()) {
					bucket.getWordCount().add(new CachedS3WordCountPair(bucket, word, wordCounts.get(word)));
				}
				bucket.setStillAvailable(true);
				bucket.setLastModified(DateTime.now());
				log.error(MessageFormat.format("All files parsed. {0} pairs in total.", count));
			}
		} catch (AmazonS3Exception s3e) {
			bucket.setStillAvailable(false);
			bucket.setLastModified(DateTime.now());
		}
		log.error("Updating database.");
		cachedS3BucketDAO.save(bucket);
		log.error(MessageFormat.format("Bucket {0} is now up-to-date.", bucket.getName()));

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
		CachedS3Bucket bucket = new CachedS3Bucket(bucketName);
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
	public boolean deleteBucket(String bucketName) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(QCachedS3Bucket.cachedS3Bucket.name.eq(bucketName));
		if (bucket == null) {
			return false;
		}
		cachedS3BucketDAO.delete(bucket);
		return true;
	}

	@Override
	public boolean deleteBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		if (bucket == null) {
			return false;
		}
		cachedS3BucketDAO.delete(bucket);
		return true;
	}

}
