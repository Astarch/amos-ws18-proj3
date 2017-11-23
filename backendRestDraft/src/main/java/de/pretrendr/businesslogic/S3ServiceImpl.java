package de.pretrendr.businesslogic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
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
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class S3ServiceImpl implements S3Service {
	@Autowired
	final private AmazonS3 s3;

	@Autowired
	private CachedS3BucketDAO cachedS3BucketDAO;
	@Autowired
	private CachedS3ObjectDAO cachedS3ObjectDAO;
	@Autowired
	private CachedS3WordCountPairDAO cachedS3WordCountPairDAO;

	@Autowired
	public S3ServiceImpl(AmazonS3 s3) {
		this.s3 = s3;
	}

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
	public Map<String, Integer> updateCacheByBucket(String bucketName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Integer> updateCacheByBucket(UUID bucketId) {
		// TODO Auto-generated method stub
		return null;
	}

}
