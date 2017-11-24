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
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Maps;

import de.pretrendr.dataccess.CachedS3BucketDAO;
import de.pretrendr.dataccess.CachedS3ObjectDAO;
import de.pretrendr.dataccess.CachedS3WordCountPairDAO;
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
		return Lists.newArrayList(bucket.getWordCount());
	}

	@Override
	public List<CachedS3Object> getAllObjectsByBucketId(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		return Lists.newArrayList(bucket.getObjects());
	}

	@Override
	public List<CachedS3Object> getAllObjectsByBucket(String bucketName) {
		CachedS3Bucket bucket = cachedS3BucketDAO
				.findOne(QCachedS3Bucket.cachedS3Bucket.name.equalsIgnoreCase(bucketName));
		if (bucket != null) {
			return getAllObjectsByBucketId(bucket.getId());
		} else {
			throw new EntityNotFoundException(
					MessageFormat.format("Bucket with name {0} could not be found.", bucketName));
		}

	}

	@Override
	public List<CachedS3WordCountPair> getWordCountMapByBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		return Lists.newArrayList(bucket.getWordCount());
	}

	@Override
	public boolean updateCacheByBucket(String bucketName) {
		CachedS3Bucket bucket = cachedS3BucketDAO
				.findOne(QCachedS3Bucket.cachedS3Bucket.name.equalsIgnoreCase(bucketName));
		return updateCache(bucket);
	}

	@Override
	public boolean updateCacheByBucket(UUID bucketId) {
		CachedS3Bucket bucket = cachedS3BucketDAO.findOne(bucketId);
		return updateCache(bucket);
	}

	private boolean updateCache(CachedS3Bucket bucket) {
		boolean doWordCount = false;
		Map<String, Integer> wordCounts = Maps.newHashMap();

		List<CachedS3Object> unvisitedObjects = Lists.newArrayList(bucket.getObjects());

		ObjectListing ol = s3.listObjects(bucket.getName());
		List<S3ObjectSummary> objects = ol.getObjectSummaries();
		for (S3ObjectSummary os : objects) {
			CachedS3Object cachedS3Object = null;
			try {
				cachedS3Object = bucket.getObjects().stream().filter(o -> o.getName().equals(os.getKey())).findFirst()
						.get();
				unvisitedObjects.remove(cachedS3Object);
			} catch (Exception e) {
				cachedS3Object = new CachedS3Object(bucket, os.getKey(), DateTime.now(),
						new DateTime(os.getLastModified()));
				bucket.getObjects().add(cachedS3Object);
				doWordCount = true;
			}
			if (cachedS3Object == null) {
			} else {
				if (cachedS3Object.getLastModified() == null
						|| new DateTime(os.getLastModified()).isAfter(cachedS3Object.getLastModified())) {
					cachedS3Object.setLastModified(new DateTime(os.getLastModified()));
					// cachedS3ObjectDAO.save(cachedS3Object);
					doWordCount = true;
				}
			}
		}
		bucket.getObjects().removeAll(unvisitedObjects);
		cachedS3ObjectDAO.delete(unvisitedObjects);
		if (doWordCount) {
			for (S3ObjectSummary os : objects) {
				try {
					S3Object s3Object = s3.getObject(bucket.getName(), os.getKey());
					S3ObjectInputStream inputStream = s3Object.getObjectContent();
					File file = new File(os.getKey());
					FileUtils.copyInputStreamToFile(inputStream, file);
					try (BufferedReader br = new BufferedReader(new FileReader(file))) {
						String line;
						while ((line = br.readLine()) != null) {
							String[] wordAndCount = line.split("\t");
							if (wordAndCount.length == 2) {
								wordCounts.put(wordAndCount[0], Integer.parseInt(wordAndCount[1]));
							} else {
								log.error(MessageFormat.format(
										"Could not parse S3 entry:'{0}' in bucket:'{1}' file:'{2}'", line,
										bucket.getName(), os.getKey()));
							}
						}
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
			cachedS3BucketDAO.save(bucket);
		}

		return true;
	}

	@Override
	public void updateAllBuckets() {
		Iterable<CachedS3Bucket> buckets = cachedS3BucketDAO.findAll();
		for (Iterator<CachedS3Bucket> iter = buckets.iterator(); iter.hasNext();) {
			CachedS3Bucket bucket = iter.next();
			boolean result = updateCache(bucket);
			bucket = cachedS3BucketDAO.findOne(bucket.getId());
		}
	}

}
