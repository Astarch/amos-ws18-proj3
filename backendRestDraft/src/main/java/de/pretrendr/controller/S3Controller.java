package de.pretrendr.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.pretrendr.businesslogic.S3Service;
import de.pretrendr.model.CachedS3Bucket;
import de.pretrendr.model.CachedS3Object;
import de.pretrendr.model.CachedS3WordCountPair;

@RequestMapping("/api/s3")
@RestController
@CrossOrigin
public class S3Controller {
	private final String bucket_name = "amos-pretrendr-commoncrawl-sample";

	@Autowired
	S3Service s3Service;

	@RequestMapping(value = "/wordCountMapByBucketName/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3WordCountPair>> getWordCountByBucketName(
			@PathVariable("bucketname") String bucketname) throws IOException {
		List<CachedS3WordCountPair> list = s3Service.getWordCountMapByBucket(bucketname);

		return new ResponseEntity<List<CachedS3WordCountPair>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/wordCountMapByBucketId/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3WordCountPair>> getWordCountByBucketId(@PathVariable("bucketId") UUID bucketId)
			throws IOException {
		List<CachedS3WordCountPair> list = s3Service.getWordCountMapByBucket(bucketId);

		return new ResponseEntity<List<CachedS3WordCountPair>>(list, HttpStatus.OK);
	}

	@RequestMapping(value = "/bucket/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Bucket>> getAllBuckets() throws IOException {
		List<CachedS3Bucket> bucketList = s3Service.getAllBuckets();

		return new ResponseEntity<List<CachedS3Bucket>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/object/getAllByBucketId/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Object>> getAllObjectsByBucketId(@PathVariable("bucketId") final UUID bucketId)
			throws IOException {
		List<CachedS3Object> bucketList = s3Service.getAllObjectsByBucketId(bucketId);

		return new ResponseEntity<List<CachedS3Object>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/object/getAllByBucketName/{bucketName}", method = RequestMethod.GET)
	public ResponseEntity<List<CachedS3Object>> getAllObjectsByBucketName(@PathVariable("bucketName") String bucketName)
			throws IOException {
		List<CachedS3Object> bucketList = s3Service.getAllObjectsByBucket(bucketName);

		return new ResponseEntity<List<CachedS3Object>>(bucketList, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateCacheByBucketName/{bucketname}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> updateCacheByBucket(@PathVariable("bucketname") String bucketname)
			throws IOException {
		boolean result = s3Service.updateCacheByBucket(bucketname);

		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/updateCacheByBucketId/{bucketId}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> updateCacheByBucket(@PathVariable("bucketId") UUID bucketId) throws IOException {
		boolean result = s3Service.updateCacheByBucket(bucketId);

		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

}
